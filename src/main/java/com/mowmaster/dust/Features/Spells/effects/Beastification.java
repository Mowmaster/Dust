package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.SpellScrollItem;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.grimoire.GrimoireItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jspecify.annotations.Nullable;

// The Change + Creature fusion, Beastification.
//   picking the form   crouch and right-click a creature while holding the spell (a Spell Scroll of it, or a Grimoire or
//                      the Akashic open at it): that kind of creature is your form from now on (beast_choice, kept
//                      through death). Players, bosses and anything that isn't a mob can't be picked.
//   self               you become your form for the spell's duration (the Beastified effect)
//   a player hit       they become the caster's form the same way
//   any other creature it's turned into the caster's form for good (a new creature of that kind where it stood, keeping
//                      its name; bosses aren't changed)
//   blocks             nothing
// While a player is a creature (beast_form, synced so everyone sees it):
//   they're drawn as the creature (Client/BeastificationClient) and take its size (onSize: its box and eye height)
//   casting any spell, or striking with a weapon (an item with a weapon component: swords, axes, tridents...), does
//   the creature's attack instead (BeastAttacks: a ghast's fireball, a creeper's fuse and blast...), or its sound if it
//   has none. Striking at the air is sent from the client (BeastAttackPayload).
// When the effect ends (or is removed, or they die) the form goes.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class Beastification
{
    private Beastification() {}

    // ---- forms ----

    // The creature this entity is turned into, or null
    public static @Nullable EntityType<?> formOf(Entity entity)
    {
        if (!(entity instanceof Player) || !entity.hasData(DustAttachmentTypeRegistry.BEAST_FORM)) return null;
        return typeOf(entity.getData(DustAttachmentTypeRegistry.BEAST_FORM));
    }

    // The creature this player picked as their form, or null
    public static @Nullable EntityType<?> choiceOf(@Nullable Entity entity)
    {
        if (!(entity instanceof Player player)) return null;
        return typeOf(player.getData(DustAttachmentTypeRegistry.BEAST_CHOICE));
    }

    private static @Nullable EntityType<?> typeOf(String id)
    {
        if (id.isEmpty()) return null;
        Identifier key = Identifier.tryParse(id);
        return key == null ? null : BuiltInRegistries.ENTITY_TYPE.getOptional(key).orElse(null);
    }

    private static String idOf(EntityType<?> type)
    {
        return BuiltInRegistries.ENTITY_TYPE.getKey(type).toString();
    }

    // Whether a creature of this kind can be a form (and what others are turned into): a mob that can be summoned, not
    // a player and not a boss
    public static boolean allowedForm(EntityType<?> type)
    {
        return !idOf(type).equals("minecraft:player") && type.canSummon() && !type.builtInRegistryHolder().is(Tags.EntityTypes.BOSSES)
                && net.minecraft.world.entity.ai.attributes.DefaultAttributes.hasSupplier(type);
    }

    // Whether this stack holds a Beastification spell (a scroll of it, or a grimoire open at it)
    public static boolean holdsSpell(ItemStack stack)
    {
        Spell spell = stack.getItem() instanceof SpellScrollItem ? stack.get(DustComponentDataRegistry.SPELL.get())
                : stack.getItem() instanceof GrimoireItem ? GrimoireItem.openSpell(stack) : null;
        return spell != null && spell.modifiers().contains(SpellRegistries.FUSION_CHANGE_CREATURE.getId());
    }

    // ---- casting ----

    public static void self(SpellCast cast, LivingEntity target)
    {
        apply(cast, target);
    }

    public static void entity(SpellCast cast, Entity target)
    {
        apply(cast, target);
    }

    private static void apply(SpellCast cast, Entity target)
    {
        EntityType<?> form = choiceOf(cast.caster());
        if (form == null) {
            if (cast.caster() instanceof ServerPlayer player) player.sendOverlayMessage(Component.translatable("dust.beast.no_form"));
            return;
        }
        if (target instanceof Player player) turnPlayer(cast, player, form);
        else if (target instanceof Mob mob) turnMob(cast.level(), mob, form);
    }

    // A player becomes the form for the spell's duration
    private static void turnPlayer(SpellCast cast, Player player, EntityType<?> form)
    {
        player.addEffect(new MobEffectInstance(DustEffectsRegistry.BEASTIFIED, cast.stats().duration(), cast.stats().amplifier()), cast.caster());
        player.setData(DustAttachmentTypeRegistry.BEAST_FORM, idOf(form));
        puff(cast.level(), player);
    }

    // Any other creature is turned into the form for good: a new one where it stood, facing the same way, with its name
    private static void turnMob(ServerLevel level, Mob mob, EntityType<?> form)
    {
        if (mob.getType() == form || mob.getType().builtInRegistryHolder().is(Tags.EntityTypes.BOSSES)) return;
        Entity made = form.create(level, EntitySpawnReason.CONVERSION);
        if (made == null) return;
        made.snapTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
        if (mob.hasCustomName()) {
            made.setCustomName(mob.getCustomName());
            made.setCustomNameVisible(mob.isCustomNameVisible());
        }
        if (made instanceof Mob newMob) {
            newMob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.CONVERSION, null);
            if (mob.isPersistenceRequired()) newMob.setPersistenceRequired();
        }
        puff(level, mob);
        mob.discard();
        level.addFreshEntity(made);
    }

    private static void puff(ServerLevel level, Entity entity)
    {
        level.sendParticles(ParticleTypes.POOF, entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 20, 0.4, 0.5, 0.4, 0.02);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.PLAYERS, 0.6F, 1.4F);
    }

    // ---- picking the form ----

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteract event)
    {
        Player player = event.getEntity();
        if (!player.isShiftKeyDown() || !holdsSpell(event.getItemStack()) || !(event.getTarget() instanceof LivingEntity target)) return;
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        EntityType<?> type = target.getType();
        if (!allowedForm(type)) {
            serverPlayer.sendOverlayMessage(Component.translatable("dust.beast.not_allowed", type.getDescription()));
            return;
        }
        serverPlayer.setData(DustAttachmentTypeRegistry.BEAST_CHOICE, idOf(type));
        serverPlayer.sendOverlayMessage(Component.translatable("dust.beast.chosen", type.getDescription()));
        serverPlayer.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.6F, 1.6F);
    }

    // ---- while turned ----

    // Striking a creature with a weapon: the form's attack instead
    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event)
    {
        // Server only: cancelled on the client, the strike would never be sent to the server at all
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (formOf(player) == null || !player.getMainHandItem().has(DataComponents.WEAPON)) return;
        event.setCanceled(true);
        BeastAttacks.attack(player, event.getTarget());
    }

    // Each tick: the form goes when the effect has; the size follows the form (both sides); a creeper's fuse burns down
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event)
    {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.level().isClientSide() && !player.getData(DustAttachmentTypeRegistry.BEAST_FORM).isEmpty()
                && !player.hasEffect(DustEffectsRegistry.BEASTIFIED)) {
            player.setData(DustAttachmentTypeRegistry.BEAST_FORM, "");
        }
        String form = player.getData(DustAttachmentTypeRegistry.BEAST_FORM);
        if (!form.equals(player.getData(DustAttachmentTypeRegistry.BEAST_APPLIED))) {
            player.setData(DustAttachmentTypeRegistry.BEAST_APPLIED, form);
            player.refreshDimensions();
        }
        if (player instanceof ServerPlayer serverPlayer) BeastAttacks.tick(serverPlayer);
    }

    // The effect taken away (milk, death...): the form goes at once
    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event)
    {
        if (event.getEffect().is(DustEffectsRegistry.BEASTIFIED) && event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            player.setData(DustAttachmentTypeRegistry.BEAST_FORM, "");
        }
    }

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event)
    {
        if (event.getEffectInstance() != null && event.getEffectInstance().getEffect().is(DustEffectsRegistry.BEASTIFIED)
                && event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            player.setData(DustAttachmentTypeRegistry.BEAST_FORM, "");
        }
    }

    // A turned player takes the creature's size (its box and eye height). hasData first: this is asked while an entity
    // is still being made.
    @SubscribeEvent
    public static void onSize(EntityEvent.Size event)
    {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player) || !entity.hasData(DustAttachmentTypeRegistry.BEAST_APPLIED)) return;
        EntityType<?> form = typeOf(entity.getData(DustAttachmentTypeRegistry.BEAST_APPLIED));
        if (form != null) event.setNewSize(form.getDimensions());
    }
}
