package com.mowmaster.dust.Features.Bosses;

import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Armor.ArmorSetBonuses;
import com.mowmaster.dust.Features.Armor.ElementalArmor;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import com.mowmaster.dust.Features.Spells.SpellCasting;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.SpellScrollItem;
import com.mowmaster.dust.Features.Spells.api.ElementEffect;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellShape;
import com.mowmaster.dust.Features.Spells.api.SpellStats;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

// An element boss: an Archon, one for each elemental armor set (ElementalArmorSet), wearing its set (and getting its
// bonuses: ArmorSetBonuses.tick). It fights with its element's spells (ArchonSpells), paying nothing for them, but
// casting half as often as a player could (twice SpellScrollItem.cooldownFor between casts) and its bolts flying half
// as fast. Before each cast it stops and gathers the spell for TELEGRAPH_TICKS (arms out, motes of its colour), so a
// player watching can step out of the way. Up close it strikes as well.
// Its level (1-MAX_LEVEL) is set as it spawns: from the nearest player's Magic Level (one level per ten), or by
// whatever spawns it (setBossLevel; a dungeon's depth), or by command (/summon dust:pyreborn_archon ~ ~ ~ {BossLevel:12}).
// Each level adds HEALTH_PER_LEVEL of its base health, armour and attack, makes its spells more potent (power) and its
// bolts hit harder (impactDamage).
// It takes no harm from its own element, double from its complementary element, and less from elements made from its
// own (BossDamage). Its own element's effects don't take hold on it, a heal of its complementary element does nothing,
// and a spell of that element that does no damage itself still wounds it (onSpellHit). It can't be calmed or enthralled,
// trapped in a boat or cobweb, or led through a portal.
// It never despawns, shows a boss bar to everyone near, and always drops a loot bag of its set (LootBags) when it dies.
// A Herald (makeHerald: a labyrinth's mini-boss) is the same with a third of the health, its bolt and first special
// only, and a HERALD_BAG_CHANCE of a loot bag (dust otherwise).
public class ArchonEntity extends Monster
{
    public static final int MAX_LEVEL = 100;
    public static final double BASE_HEALTH = 150.0;
    // Each level after the first: this much more of the base health
    public static final double HEALTH_PER_LEVEL = 0.2;
    // Ticks it gathers a spell before it goes off
    public static final int TELEGRAPH_TICKS = 10;
    // It keeps between these distances from its target while it casts (blocks)
    private static final double NEAR = 6.0, FAR = 20.0;
    // Below this much health it's enraged: its specials rest twice as fast
    private static final float ENRAGED_BELOW = 0.5F;
    // A Herald's health, of an Archon's at its level; and its chance of dropping a loot bag
    private static final double HERALD_HEALTH = 0.35;
    private static final float HERALD_BAG_CHANCE = 0.35F;

    private static final EntityDataAccessor<Integer> LEVEL = SynchedEntityData.defineId(ArchonEntity.class, EntityDataSerializers.INT);
    // The kit spell it's gathering, plus one (0: not casting)
    private static final EntityDataAccessor<Integer> CASTING = SynchedEntityData.defineId(ArchonEntity.class, EntityDataSerializers.INT);

    private final ElementalArmorSet set;
    private final ServerBossEvent bossBar;
    // Ticks before each kit spell can be cast again
    private final int[] rest;
    // Ticks before it can start gathering another spell
    private int castDelay;
    // Ticks left gathering the chosen spell
    private int telegraph;
    private int chosen = -1;
    // Its level's numbers are in its attributes (false: a new one, from /summon with a BossLevel; it starts at full health)
    private boolean levelApplied;
    // A Herald: a lesser Archon guarding a labyrinth room (makeHerald)
    private boolean herald;

    public ArchonEntity(EntityType<? extends ArchonEntity> type, Level level, ElementalArmorSet set)
    {
        super(type, level);
        this.set = set;
        List<ArchonSpells.ArchonSpell> kit = ArchonSpells.kit(set);
        this.rest = new int[kit.size()];
        // Its specials start half rested, so it doesn't open with all of them at once
        for (int i = 0; i < kit.size(); i++) rest[i] = kit.get(i).rest() / 2;
        this.bossBar = new ServerBossEvent(Mth.createInsecureUUID(random), getDisplayName(), barColor(set), BossEvent.BossBarOverlay.NOTCHED_10);
        this.xpReward = 50;
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, BASE_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.ARMOR, 6.0)
                .add(Attributes.ARMOR_TOUGHNESS, 2.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.SCALE, 1.25);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {
        super.defineSynchedData(builder);
        builder.define(LEVEL, 0);
        builder.define(CASTING, 0);
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new CastGoal(this));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 16.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        // Hurt by another Archon's spell, it doesn't turn on it
        targetSelector.addGoal(1, new HurtByTargetGoal(this, ArchonEntity.class));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public ElementalArmorSet set()
    {
        return set;
    }

    public DustElement element()
    {
        return set.element();
    }

    // 0 until it's been given one
    public int bossLevel()
    {
        return entityData.get(LEVEL);
    }

    public boolean isCasting()
    {
        return entityData.get(CASTING) > 0;
    }

    // How much more potent its spells are than a player's at the same spell: +15% a level
    public float power()
    {
        return 1.0F + 0.15F * (Math.max(1, bossLevel()) - 1);
    }

    // What one of its bolts does on hitting, besides its effect
    public float impactDamage()
    {
        return 3.0F + 0.75F * Math.max(1, bossLevel());
    }

    // Sets its level and heals it to full at it
    public void setBossLevel(int level)
    {
        level = Mth.clamp(level, 1, MAX_LEVEL);
        entityData.set(LEVEL, level);
        applyLevel(level);
        setHealth(getMaxHealth());
        levelApplied = true;
        updateBar();
    }

    // The level an Archon spawning near this player gets: one more for every ten Magic Levels they have
    public static int levelFor(@Nullable Player player)
    {
        return player == null ? 1 : 1 + PlayerMagicHelper.magicLevel(player) / 10;
    }

    // Makes it a Herald (before its level is set): a third of the health, a little smaller, only its first two spells,
    // named as one (the Pyreborn Herald), and its loot bag only a chance
    public void makeHerald()
    {
        herald = true;
        setBase(Attributes.SCALE, 1.0);
        setCustomName(Component.translatable("dust.archon.herald", Component.translatable("dust.armor_set." + set.id)));
        if (bossLevel() > 0) applyLevel(bossLevel());
    }

    public boolean isHerald()
    {
        return herald;
    }

    private void applyLevel(int level)
    {
        setBase(Attributes.MAX_HEALTH, BASE_HEALTH * (1 + HEALTH_PER_LEVEL * (level - 1)) * (herald ? HERALD_HEALTH : 1.0));
        setBase(Attributes.ARMOR, Math.min(20.0, 6.0 + level * 0.5));
        setBase(Attributes.ARMOR_TOUGHNESS, Math.min(12.0, 2.0 + level * 0.25));
        setBase(Attributes.ATTACK_DAMAGE, (herald ? 4.0 : 6.0) + level);
        xpReward = herald ? 20 + 5 * level : 50 + 10 * level;
    }

    private void setBase(Holder<Attribute> attribute, double value)
    {
        AttributeInstance instance = getAttribute(attribute);
        if (instance != null) instance.setBaseValue(value);
    }

    // Its set, made for show: the pieces give no protection (its own is in its attributes) and never drop
    private void equip()
    {
        for (ArmorType type : ElementalArmor.PIECES) {
            ItemStack piece = new ItemStack(ElementalArmor.item(set, type));
            piece.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
            setItemSlot(type.getSlot(), piece);
            setDropChance(type.getSlot(), 0.0F);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data)
    {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data);
        if (bossLevel() <= 0) setBossLevel(levelFor(level.getNearestPlayer(this, 64.0)));
        equip();
        setPersistenceRequired();
        return result;
    }

    @Override
    public void tick()
    {
        super.tick();
        if (level().isClientSide()) {
            // A few motes of its colour drift up around it; more while it gathers a spell
            int motes = isCasting() ? 3 : (random.nextInt(3) == 0 ? 1 : 0);
            for (int i = 0; i < motes; i++) {
                level().addParticle(new DustParticleOptions(element().color(), isCasting() ? 1.2F : 0.7F),
                        getRandomX(0.8), getRandomY(), getRandomZ(0.8), 0, 0.02, 0);
            }
            return;
        }
        // Summoned without a level, or without its armour (a command with data): it gets them now
        if (bossLevel() <= 0) setBossLevel(levelFor(level().getNearestPlayer(this, 64.0)));
        if (getItemBySlot(EquipmentSlot.CHEST).isEmpty()) equip();
        if (tickCount % 10 == 0) ArmorSetBonuses.tick(this);
    }

    @Override
    protected void customServerAiStep(ServerLevel level)
    {
        super.customServerAiStep(level);
        boolean enraged = getHealth() < getMaxHealth() * ENRAGED_BELOW;
        for (int i = 0; i < rest.length; i++) if (rest[i] > 0) rest[i] = Math.max(0, rest[i] - (enraged ? 2 : 1));
        if (castDelay > 0) castDelay--;
        if (telegraph > 0) {
            telegraph--;
            LivingEntity target = getTarget();
            if (target != null) getLookControl().setLookAt(target, 30.0F, 30.0F);
            gatherParticles(level);
            if (telegraph == 0) {
                if (target != null && target.isAlive() && chosen >= 0) release(level, target);
                else stopCasting();
            }
        }
        bossBar.setProgress(getHealth() / getMaxHealth());
    }

    // Ready to start gathering another spell
    boolean readyToCast()
    {
        return castDelay <= 0 && telegraph <= 0;
    }

    // Picks the spell to cast (the specials first, as each is rested; the heal only when hurt) and starts gathering it
    void startCast()
    {
        List<ArchonSpells.ArchonSpell> kit = ArchonSpells.kit(set);
        int pick = -1;
        // A Herald knows only its bolt and its first special
        for (int i = (herald ? Math.min(kit.size(), 2) : kit.size()) - 1; i >= 0; i--) {
            if (rest[i] > 0) continue;
            if (kit.get(i).kind() == ArchonSpells.Kind.SELF && getHealth() > getMaxHealth() * ArchonSpells.SELF_BELOW) continue;
            pick = i;
            break;
        }
        if (pick < 0) return;
        chosen = pick;
        telegraph = TELEGRAPH_TICKS;
        entityData.set(CASTING, pick + 1);
        playSound(SoundEvents.EVOKER_PREPARE_ATTACK, 1.0F, 1.2F);
    }

    private void stopCasting()
    {
        chosen = -1;
        telegraph = 0;
        entityData.set(CASTING, 0);
    }

    // The gathered spell goes off: from its eyes, at the middle of its target (a heal: on itself)
    private void release(ServerLevel level, LivingEntity target)
    {
        ArchonSpells.ArchonSpell chosenSpell = ArchonSpells.kit(set).get(chosen);
        Spell spell = chosenSpell.spell();
        SpellStats stats = spell.stats(power());
        Vec3 origin = getEyePosition();
        Vec3 direction = getLookAngle();
        if (chosenSpell.kind() == ArchonSpells.Kind.BOLT) {
            // Half as fast as a player's bolt, flying straight (no drop over its range), and the blow itself hurts
            stats = stats.withSpeed(stats.speed() * 0.5F).withGravity(0.0F).withImpactDamage(impactDamage());
            Vec3 toTarget = target.getBoundingBox().getCenter().subtract(origin);
            if (toTarget.lengthSqr() > 1.0E-4) direction = toTarget.normalize();
        }
        SpellCast cast = new SpellCast(level, this, spell, stats, origin, direction);
        SpellShape shape = SpellRegistries.SHAPE_REGISTRY.getValue(spell.shape());
        if (shape != null) SpellCasting.deliver(cast, shape);
        rest[chosen] = chosenSpell.rest();
        // Half as often as a player could: twice their wait between casts (the gathering is part of it)
        castDelay = Math.max(0, 2 * SpellScrollItem.cooldownFor(spell) - TELEGRAPH_TICKS);
        stopCasting();
        playSound(SoundEvents.EVOKER_CAST_SPELL, 1.0F, 1.0F);
    }

    // Two motes circling it at chest height while it gathers a spell
    private void gatherParticles(ServerLevel level)
    {
        DustParticleOptions dust = new DustParticleOptions(element().color(), 1.3F);
        double radius = getBbWidth() * 0.9;
        for (int i = 0; i < 2; i++) {
            double angle = tickCount * 0.6 + i * Math.PI;
            level.sendParticles(dust, getX() + Math.cos(angle) * radius, getY(0.65), getZ() + Math.sin(angle) * radius, 1, 0, 0.05, 0, 0);
        }
    }

    // ---- what hurts it ----

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount)
    {
        // /kill, the void, an execution: as for anything else
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return super.hurtServer(level, source, amount);
        // The Zephyr Archon rides the air: no fall damage
        if (set == ElementalArmorSet.ZEPHYR && source.is(DamageTypeTags.IS_FALL)) return false;
        float factor = BossDamage.factor(element(), BossDamage.elementOf(source));
        if (factor <= 0.0F) {
            // Its own element: it shrugs it off
            level.sendParticles(new DustParticleOptions(element().color(), 1.5F), getX(), getY(0.6), getZ(), 10, 0.4, 0.5, 0.4, 0);
            playSound(SoundEvents.SHIELD_BLOCK.value(), 0.8F, 1.4F);
            return false;
        }
        if (factor >= 2.0F) level.sendParticles(ParticleTypes.ENCHANTED_HIT, getX(), getY(0.6), getZ(), 14, 0.4, 0.6, 0.4, 0.2);
        return super.hurtServer(level, source, amount * factor);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect)
    {
        SpellCast landing = SpellCast.landing;
        if (landing != null && landing.element().equals(element())) return false;
        if (effect.is(DustEffectsRegistry.CALMED) || effect.is(DustEffectsRegistry.ENTHRALLED)) return false;
        return super.canBeAffected(effect);
    }

    @Override
    public void heal(float amount)
    {
        // Its complementary element can't mend it (onSpellHit wounds it instead)
        SpellCast landing = SpellCast.landing;
        if (landing != null && landing.element().equals(BossDamage.complement(element()))) return;
        super.heal(amount);
    }

    // A spell has just landed on target (SpellCast.entityEffect): a spell of an Archon's complementary element that
    // does no damage itself (Healing on the Explosion Archon, Order on the Riftborn) wounds it anyway, as magic doubled
    // (hurtServer doubles it, the spell still landing)
    public static void onSpellHit(SpellCast cast, Entity target)
    {
        if (!(target instanceof ArchonEntity archon) || !archon.isAlive()) return;
        if (!cast.element().equals(BossDamage.complement(archon.element()))) return;
        ElementEffect effect = SpellRegistries.ELEMENT_EFFECT_REGISTRY.getValue(cast.element().id());
        if (effect != null && effect.damages()) return;
        LivingEntity caster = cast.caster();
        DamageSource source = caster == null ? cast.level().damageSources().magic() : cast.level().damageSources().indirectMagic(caster, caster);
        archon.hurtServer(cast.level(), source, cast.stats().damage());
    }

    // ---- death, despawning, the boss bar ----

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit)
    {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        if (!herald || random.nextFloat() < HERALD_BAG_CHANCE) spawnAtLocation(level, LootBags.bag(set, Math.max(1, bossLevel())));
        else spawnAtLocation(level, new ItemStack(com.mowmaster.dust.DustRegistries.DustItemRegistry.DUSTS.get(element()).get(), 8 + random.nextInt(9)));
    }

    // It can't be trapped in a boat or minecart, led through a portal or slowed by cobwebs and berry bushes
    @Override
    protected boolean canRide(Entity vehicle)
    {
        return false;
    }

    @Override
    public boolean canUsePortal(boolean ignorePassenger)
    {
        return false;
    }

    @Override
    public void makeStuckInBlock(net.minecraft.world.level.block.state.BlockState state, Vec3 speedMultiplier)
    {
    }

    @Override
    public boolean removeWhenFarAway(double distance)
    {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence()
    {
        return true;
    }

    private void updateBar()
    {
        bossBar.setName(Component.translatable("dust.archon.bar", getDisplayName(), Math.max(1, bossLevel())));
    }

    @Override
    public void setCustomName(@Nullable Component name)
    {
        super.setCustomName(name);
        updateBar();
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player)
    {
        super.startSeenByPlayer(player);
        bossBar.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player)
    {
        super.stopSeenByPlayer(player);
        bossBar.removePlayer(player);
    }

    private static BossEvent.BossBarColor barColor(ElementalArmorSet set)
    {
        return switch (set) {
            case PYREBORN, BLASTWARD -> BossEvent.BossBarColor.RED;
            case TIDEWOVEN, RIMEFROST, DEEPWATCH -> BossEvent.BossBarColor.BLUE;
            case ROOTBOUND, BASTION, MIREWALKER, VERDANT -> BossEvent.BossBarColor.GREEN;
            case LUMEN -> BossEvent.BossBarColor.WHITE;
            case RIFTBORN -> BossEvent.BossBarColor.PURPLE;
            case SPARKWRIGHT, ZEPHYR -> BossEvent.BossBarColor.PINK;
            case GILDED -> BossEvent.BossBarColor.YELLOW;
        };
    }

    // ---- saving ----

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {
        super.addAdditionalSaveData(output);
        output.putInt("BossLevel", bossLevel());
        output.putBoolean("LevelApplied", levelApplied);
        output.putBoolean("Herald", herald);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
        super.readAdditionalSaveData(input);
        herald = input.getBooleanOr("Herald", false);
        int level = input.getIntOr("BossLevel", 0);
        if (level <= 0) return;
        if (input.getBooleanOr("LevelApplied", false)) {
            entityData.set(LEVEL, Mth.clamp(level, 1, MAX_LEVEL));
            applyLevel(bossLevel());
            levelApplied = true;
            updateBar();
        } else {
            // New, from a command with {BossLevel:n}: full health at that level
            setBossLevel(level);
        }
    }

    // ---- fighting ----

    // While it has a target: keeps NEAR-FAR blocks away, circling (strafing) and switching direction now and then;
    // closes in when it can't see it; plants its feet while gathering a spell; casts whenever it's ready and can see
    // it; strikes when it's in reach.
    static class CastGoal extends Goal
    {
        private final ArchonEntity archon;
        private int strafeTicks;
        private boolean clockwise;
        private int meleeCooldown;

        CastGoal(ArchonEntity archon)
        {
            this.archon = archon;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse()
        {
            LivingEntity target = archon.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean requiresUpdateEveryTick()
        {
            return true;
        }

        @Override
        public void start()
        {
            archon.setAggressive(true);
        }

        @Override
        public void stop()
        {
            archon.setAggressive(false);
            archon.getNavigation().stop();
        }

        @Override
        public void tick()
        {
            LivingEntity target = archon.getTarget();
            if (target == null) return;
            double distance = archon.distanceToSqr(target);
            boolean sees = archon.getSensing().hasLineOfSight(target);
            archon.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (archon.isCasting()) {
                archon.getNavigation().stop();
                archon.getMoveControl().strafe(0.0F, 0.0F);
            } else if (!sees || distance > FAR * FAR) {
                archon.getNavigation().moveTo(target, 1.0);
            } else {
                archon.getNavigation().stop();
                if (++strafeTicks >= 40 && archon.getRandom().nextInt(20) == 0) {
                    clockwise = !clockwise;
                    strafeTicks = 0;
                }
                float forward = distance < NEAR * NEAR ? -0.5F : (distance > (FAR - 4) * (FAR - 4) ? 0.5F : 0.0F);
                archon.getMoveControl().strafe(forward, clockwise ? 0.5F : -0.5F);
                archon.lookAt(target, 30.0F, 30.0F);
            }
            if (sees && archon.readyToCast()) archon.startCast();
            if (meleeCooldown > 0) meleeCooldown--;
            double reach = archon.getBbWidth() * 1.5 + target.getBbWidth();
            if (sees && meleeCooldown <= 0 && distance <= reach * reach) {
                // Stonecutter: 26.1.2 has no swingForAttack (its swing takes just the hand)
                //? if >=26.3 {
                archon.swingForAttack(InteractionHand.MAIN_HAND);
                //?} else {
                /*archon.swing(InteractionHand.MAIN_HAND);
                *///?}
                archon.doHurtTarget((ServerLevel) archon.level(), target);
                meleeCooldown = 20;
            }
        }
    }
}
