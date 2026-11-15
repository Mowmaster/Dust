package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

// A wand (the Fire Wand, the Ice Wand): its element's plain Projectile spell, no modifiers, at base strength. No dust or
// mana: each cast uses a point of durability instead. Casting one teaches the player the Projectile shape, and anyone
// a wand's bolt hits learns it too (SpellCast), so a skeleton or stray carrying one (WandMobs) is a way to learn it.
// element: a Supplier because the elements are set up after items are registered.
public class SpellWandItem extends Item
{
    public static final int DURABILITY = 32;
    // Ticks between a player's casts
    public static final int COOLDOWN = 20;

    private final Supplier<DustElement> element;

    public SpellWandItem(Supplier<DustElement> element, Properties properties)
    {
        super(properties.durability(DURABILITY));
        this.element = element;
    }

    public DustElement element()
    {
        return element.get();
    }

    // The spell a wand of this element casts
    public static Spell spellOf(DustElement element)
    {
        return new Spell(element, SpellRegistries.PROJECTILE.getId(), Map.of(), List.of());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(stack)) return InteractionResult.FAIL;
        if (player instanceof ServerPlayer serverPlayer) {
            Spell spell = spellOf(element());
            if (cast(SpellCast.by(serverPlayer, spell, spell.stats(1.0F)))) {
                SpellKnowledge.unlockShape(serverPlayer, SpellRegistries.PROJECTILE.getId());
                stack.hurtAndBreak(1, serverPlayer, hand.asEquipmentSlot());
                player.getCooldowns().addCooldown(stack, COOLDOWN);
            }
        }
        return InteractionResult.SUCCESS;
    }

    // A mob's cast: from its eyes towards the target's middle (WandMobs)
    public static boolean castAt(LivingEntity caster, DustElement element, LivingEntity target)
    {
        Spell spell = spellOf(element);
        Vec3 from = caster.getEyePosition();
        Vec3 direction = target.getBoundingBox().getCenter().subtract(from).normalize();
        return cast(new SpellCast((ServerLevel) caster.level(), caster, spell, spell.stats(1.0F), from, direction));
    }

    private static boolean cast(SpellCast cast)
    {
        boolean landed = SpellCasting.deliver(cast, SpellRegistries.PROJECTILE.get());
        if (landed) cast.level().playSound(null, cast.origin().x, cast.origin().y, cast.origin().z, SoundEvents.EVOKER_CAST_SPELL, SoundSource.NEUTRAL, 0.6F, 1.6F);
        return landed;
    }
}
