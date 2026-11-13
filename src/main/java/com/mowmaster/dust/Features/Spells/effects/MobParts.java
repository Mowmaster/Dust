package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustEffects.DustDamageTypes;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

// The step-6 effect parts: pulling things in, curing, thralls, growth, splinters, crystal prisons, flares...
public final class MobParts
{
    private MobParts() {}

    // ---- Vacuum ----

    // Everything loose (items, experience, living things but the caster) within radius of centre is pulled towards it
    public static void pullTowards(SpellCast cast, Vec3 centre, double radius, Predicate<Entity> which)
    {
        for (Entity entity : cast.level().getEntities((Entity) null, new AABB(centre, centre).inflate(radius), which)) {
            if (entity == cast.caster() || entity.position().distanceTo(centre) > radius) continue;
            Vec3 pull = centre.subtract(entity.position());
            Vec3 step = pull.lengthSqr() < 1 ? pull.scale(0.3) : pull.normalize().scale(0.6 * cast.stats().potency());
            entity.push(step.x, step.y * 0.5 + 0.1, step.z);
            entity.needsSync = true;
        }
    }

    private static boolean loose(Entity entity)
    {
        return entity instanceof ItemEntity || entity instanceof ExperienceOrb || entity instanceof LivingEntity;
    }

    public static void vacuumBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        pullTowards(cast, Vec3.atCenterOf(pos.relative(face)), 5 + cast.stats().size(), MobParts::loose);
    }

    // The mobs around it are pulled in together around it
    public static void vacuumMob(SpellCast cast, Entity target)
    {
        pullTowards(cast, target.position(), 5 + cast.stats().size(), entity -> entity instanceof LivingEntity && entity != target);
    }

    // ---- Life: curing the undead ----

    // 26.3: the vanilla entity types are constants on EntityTypes (they used to be on EntityType itself)
    private static final Map<EntityType<?>, EntityType<? extends Mob>> CURES = Map.of(
            EntityTypes.ZOMBIE_VILLAGER, EntityTypes.VILLAGER,
            EntityTypes.ZOMBIFIED_PIGLIN, EntityTypes.PIGLIN,
            EntityTypes.ZOGLIN, EntityTypes.HOGLIN,
            EntityTypes.ZOMBIE_HORSE, EntityTypes.HORSE,
            EntityTypes.SKELETON_HORSE, EntityTypes.HORSE);

    // A zombified creature turns back into what it was (a cured villager forgets its trade). Life's entity part is
    // PetGhosts.reviveOrCure: this, unless the target is a pet's ghost.
    public static void cure(SpellCast cast, Entity target)
    {
        EntityType<? extends Mob> cured = CURES.get(target.getType());
        if (cured == null || !(target instanceof Mob mob)) return;
        // convertTo: replaces the mob with a new one of the type, keeping its name, position and equipment
        // (ConversionParams.single: keep equipment, don't keep the team...)
        Mob result = mob.convertTo(cured, ConversionParams.single(mob, true, true), converted -> {});
        if (result != null) {
            cast.level().sendParticles(ParticleTypes.HAPPY_VILLAGER, result.getX(), result.getY() + 1, result.getZ(), 15, 0.4, 0.6, 0.4, 0);
            cast.level().playSound(null, result.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
    }

    // ---- Fungus: a thrall ----

    public static final String THRALL_OWNER = "dust_thrall_owner";

    // It fights for the caster for a while: Enthralled (it hunts the monsters around it, never the caster or theirs)
    public static void enthrall(SpellCast cast, Entity target)
    {
        if (!(target instanceof Mob mob)) return;
        if (cast.caster() != null) mob.getPersistentData().putString(THRALL_OWNER, cast.caster().getStringUUID());
        mob.setTarget(null);
        EffectHelper.give(cast, mob, DustEffectsRegistry.ENTHRALLED);
    }

    // ---- Tool: picking things up ----

    public static void pickUpLoot(SpellCast cast, Entity target)
    {
        if (target instanceof Mob mob) mob.setCanPickUpLoot(true);
    }

    // ---- Seeds: back to its breeding item ----

    // What animals eat, tried in turn until one is food for this animal
    private static final List<Item> BREEDING_ITEMS = List.of(Items.WHEAT, Items.WHEAT_SEEDS, Items.CARROT, Items.POTATO, Items.BEETROOT,
            Items.BEETROOT_SEEDS, Items.GOLDEN_CARROT, Items.DANDELION, Items.HAY_BLOCK, Items.COD, Items.SALMON, Items.TROPICAL_FISH_BUCKET,
            Items.SWEET_BERRIES, Items.GLOW_BERRIES, Items.BAMBOO, Items.SEAGRASS, Items.SLIME_BALL, Items.CACTUS, Items.TORCHFLOWER_SEEDS,
            Items.WARPED_FUNGUS, Items.CRIMSON_FUNGUS, Items.SPIDER_EYE, Items.BEEF, Items.ROTTEN_FLESH, Items.TROPICAL_FISH, Items.MELON_SLICE);

    // An animal turns into its breeding item (the first food it eats from BREEDING_ITEMS). Other mobs will turn into a
    // summoning item once summoning exists.
    public static void toBreedingItem(SpellCast cast, Entity target)
    {
        if (!(target instanceof Animal animal) || !animal.getPassengers().isEmpty()) return;
        for (Item item : BREEDING_ITEMS) {
            ItemStack food = new ItemStack(item);
            if (!animal.isFood(food)) continue;
            cast.level().sendParticles(ParticleTypes.POOF, animal.getX(), animal.getY() + 0.5, animal.getZ(), 12, 0.3, 0.3, 0.3, 0.02);
            animal.spawnAtLocation(cast.level(), food);
            animal.discard();
            return;
        }
    }

    // ---- Growth ----

    // Bonemeal on the block (and, later, faster machines)
    public static void bonemeal(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        if (BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), level, pos)) {
            // Level event 1505: the bonemeal sparkles and sound, as when a player uses it
            level.levelEvent(1505, pos, 15);
        }
    }

    // A baby grows 10% of the rest of the way up; a slime or magma cube grows a size
    public static void grow(SpellCast cast, Entity target)
    {
        if (target instanceof AgeableMob ageable && ageable.isBaby()) {
            int left = -ageable.getAge();
            ageable.ageUp(Math.max(1, left / 10 / 20), true);
        } else if (target instanceof Slime slime && slime.getSize() < 8) {
            slime.setSize(slime.getSize() + 1, true);
        } else {
            return;
        }
        cast.level().sendParticles(ParticleTypes.HAPPY_VILLAGER, target.getX(), target.getY() + 0.5, target.getZ(), 10, 0.4, 0.4, 0.4, 0);
    }

    // ---- Crystal: splinters ----

    private static final int SPLINTER_DEPTH = 4;

    // Kinetic damage, and shards splinter off into everything within 3 blocks (half the damage); whatever a splinter
    // kills splinters in turn, a few times over
    public static void shatter(SpellCast cast, Entity target)
    {
        splinter(cast, target, cast.stats().damage(), SPLINTER_DEPTH);
    }

    private static void splinter(SpellCast cast, Entity target, float damage, int depth)
    {
        ServerLevel level = cast.level();
        target.hurtServer(level, DustDamageTypes.create(level, DustDamageTypes.KINETIC_KEY, cast.caster()), damage);
        level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AMETHYST_CLUSTER.defaultBlockState()),
                target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 12, 0.3, 0.3, 0.3, 0.1);
        if (depth <= 0) return;
        for (LivingEntity near : cast.around(target.position(), 3)) {
            if (near == target || near.isDeadOrDying()) continue;
            boolean wasAlive = near.isAlive();
            near.hurtServer(level, DustDamageTypes.create(level, DustDamageTypes.KINETIC_KEY, cast.caster()), damage / 2);
            if (wasAlive && near.isDeadOrDying()) splinter(cast, near, damage / 2, depth - 1);
        }
    }

    // ---- Gem: a crystal prison ----

    // Encased: it can't move; when the crystal breaks (the effect ends) it takes kinetic damage and is left Brittle
    public static void encase(SpellCast cast, Entity target)
    {
        EffectHelper.give(cast, target, DustEffectsRegistry.ENCASED);
        cast.level().playSound(null, target.blockPosition(), SoundEvents.AMETHYST_BLOCK_PLACE, SoundSource.HOSTILE, 1.0F, 0.8F);
    }

    // ---- Air: a falling block ----

    // The block falls, if there's room beneath it (not bedrock-hard blocks, not blocks with contents)
    public static void topple(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.getDestroySpeed(level, pos) < 0 || level.getBlockEntity(pos) != null) return;
        if (FallingBlock.isFree(level.getBlockState(pos.below()))) FallingBlockEntity.fall(level, pos, state);
    }

    // ---- block marks: Knowledge (more experience), Damage (quicker to break) ----

    public static void markWise(SpellCast cast, BlockPos pos, Direction face)
    {
        BlockMarks.mark(cast.level(), pos, BlockMarks.WISE, cast.stats().duration() * 12);
    }

    public static void markQuickBreak(SpellCast cast, BlockPos pos, Direction face)
    {
        BlockMarks.mark(cast.level(), pos, BlockMarks.QUICK_BREAK, cast.stats().duration() * 12);
    }

    // ---- Explosion: a flare ----

    // Gliding: a firework boost. Otherwise a red firework bursts high above (a flare)
    public static void flare(SpellCast cast, LivingEntity target)
    {
        ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
        FireworkExplosion burst = new FireworkExplosion(FireworkExplosion.Shape.LARGE_BALL, IntList.of(0xFF2020), IntList.of(0xFF8080), true, true);
        rocket.set(DataComponents.FIREWORKS, new Fireworks(target.isFallFlying() ? 1 : 3, target.isFallFlying() ? List.of() : List.of(burst)));
        FireworkRocketEntity firework = target.isFallFlying()
                ? new FireworkRocketEntity(cast.level(), rocket, target)
                : new FireworkRocketEntity(cast.level(), rocket, target.getX(), target.getY() + target.getBbHeight(), target.getZ(), false);
        cast.level().addFreshEntity(firework);
    }

    // ---- Encased breaking (from SpellMobEffects when it ends) ----

    public static void breakOut(ServerLevel level, LivingEntity entity, int amplifier)
    {
        entity.hurtServer(level, DustDamageTypes.create(level, DustDamageTypes.KINETIC_KEY), 4.0F * (amplifier + 1));
        entity.addEffect(new MobEffectInstance(DustEffectsRegistry.BRITTLE, 20 * 8, amplifier));
        level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AMETHYST_BLOCK.defaultBlockState()),
                entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 30, 0.4, 0.6, 0.4, 0.1);
        level.playSound(null, entity.blockPosition(), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.HOSTILE, 1.0F, 0.8F);
    }
}
