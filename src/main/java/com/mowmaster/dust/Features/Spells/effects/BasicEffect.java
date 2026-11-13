package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.Features.Spells.api.ElementEffect;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

// An element effect put together from parts, for elements whose effects are short: a mob effect, some damage, a push.
// Each part is optional (a missing one does nothing yet). Built in SpellRegistries, e.g.
//   new BasicEffect().self(BasicEffect.give(MobEffects.SPEED)).entity(Effects::slow)
// The part interfaces are functional interfaces (one method each), so a lambda or method reference fills them.
public final class BasicEffect implements ElementEffect
{
    @FunctionalInterface
    public interface SelfPart { void apply(SpellCast cast, LivingEntity target); }

    @FunctionalInterface
    public interface BlockPart { void apply(SpellCast cast, BlockPos pos, Direction face); }

    @FunctionalInterface
    public interface EntityPart { void apply(SpellCast cast, Entity target); }

    private @Nullable SelfPart self;
    private @Nullable BlockPart block;
    private @Nullable EntityPart entity;

    public BasicEffect self(SelfPart part) { this.self = part; return this; }
    public BasicEffect block(BlockPart part) { this.block = part; return this; }
    public BasicEffect entity(EntityPart part) { this.entity = part; return this; }

    // A mob effect for the spell's duration at its potency, as a self part
    public static SelfPart give(Holder<MobEffect> effect)
    {
        return (cast, target) -> EffectHelper.give(cast, target, effect);
    }

    // The same, as an entity part (living targets only)
    public static EntityPart inflict(Holder<MobEffect> effect)
    {
        return (cast, target) -> EffectHelper.give(cast, target, effect);
    }

    @Override
    public void onSelf(SpellCast cast, LivingEntity target)
    {
        if (self != null) self.apply(cast, target);
    }

    @Override
    public void onBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        if (block != null) block.apply(cast, pos, face);
    }

    @Override
    public void onEntity(SpellCast cast, Entity target)
    {
        if (entity != null) entity.apply(cast, target);
    }
}
