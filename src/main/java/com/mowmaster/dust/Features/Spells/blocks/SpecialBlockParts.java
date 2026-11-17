package com.mowmaster.dust.Features.Spells.blocks;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.entities.AnimatedBlockEntity;
import com.mowmaster.dust.Features.Spells.entities.FloatingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

// The special blocks' spell parts. How long the temporary ones last: the spell's duration x TIME_SCALE (20 seconds at the
// default 5)
public final class SpecialBlockParts
{
    public static final int TIME_SCALE = 4;

    private SpecialBlockParts() {}

    // How long a block the spell makes lasts: its usual time, or (Silky) for good (-1: no fading tick is set)
    private static int lifetime(SpellCast cast, int ticks)
    {
        return com.mowmaster.dust.Features.Spells.SpellRegistries.silky(cast) ? -1 : ticks;
    }

    // Light: a mote of light against the face hit, in the Light element's colour (dye it to change it)
    public static void light(SpellCast cast, BlockPos pos, Direction face)
    {
        ((SpellLightBlock) DustBlockRegistry.SPELL_LIGHT.get()).place(cast.level(), pos.relative(face), cast.element().color());
    }

    // Darkness: the block goes see-through (still solid) for a while
    public static void veil(SpellCast cast, BlockPos pos, Direction face)
    {
        PhantomBlock.make(cast.level(), pos, true, lifetime(cast, cast.stats().duration() * TIME_SCALE));
    }

    // Spirit: the block can be walked through for a while
    public static void phase(SpellCast cast, BlockPos pos, Direction face)
    {
        PhantomBlock.make(cast.level(), pos, false, lifetime(cast, cast.stats().duration() * TIME_SCALE));
    }

    // Armor: the block is warded (for good)
    public static void ward(SpellCast cast, BlockPos pos, Direction face)
    {
        Warding.ward(cast.level(), pos);
    }

    // Protection: warded glass against the face hit
    public static void wardedGlass(SpellCast cast, BlockPos pos, Direction face)
    {
        BlockPos at = pos.relative(face);
        if (cast.level().getBlockState(at).canBeReplaced()) cast.level().setBlockAndUpdate(at, DustBlockRegistry.WARDED_GLASS.get().defaultBlockState());
    }

    // Flight: the block floats free for a while
    public static void floatBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        int ticks = cast.stats().duration() * TIME_SCALE;
        FloatingBlockEntity.lift(cast.level(), pos, com.mowmaster.dust.Features.Spells.SpellRegistries.silky(cast) ? Integer.MAX_VALUE : ticks);
    }

    // Creature: the block comes alive for a while
    public static void animate(SpellCast cast, BlockPos pos, Direction face)
    {
        AnimatedBlockEntity.animate(cast.level(), pos, cast.stats().duration() * TIME_SCALE * 3 / 2);
    }
}
