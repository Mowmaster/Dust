package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Spells.api.ElementEffect;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.Nullable;

// Earth
//   Self     Steadfast: harder to knock back
//   Block    a dirt block in the nearest free spot: right above, then below, then the sides, then further out (up to
//            SEARCH blocks away); air or a replaceable plant (grass, flowers) counts as free
//   Entity   a pointed dripstone drops on it from 5 blocks up
public class EarthEffect implements ElementEffect
{
    private static final int SEARCH = 3;
    private static final int DRIPSTONE_HEIGHT = 5;

    @Override
    public void onSelf(SpellCast cast, LivingEntity target)
    {
        EffectHelper.give(cast, target, DustEffectsRegistry.STEADFAST);
    }

    @Override
    public void onBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        BlockPos free = nearestFree(cast.level(), pos);
        if (free != null) cast.level().setBlockAndUpdate(free, Blocks.DIRT.defaultBlockState());
    }

    // Above, below, then the four sides, then the rest of the 3x3x3 around pos; then the shell one further out...
    private static @Nullable BlockPos nearestFree(ServerLevel level, BlockPos pos)
    {
        for (Direction first : new Direction[] {Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST}) {
            if (isFree(level, pos.relative(first))) return pos.relative(first);
        }
        for (int radius = 1; radius <= SEARCH; radius++) {
            for (int dy = radius; dy >= -radius; dy--) {
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        // Only the shell at this radius (the inside was checked at smaller radii)
                        if (Math.max(Math.abs(dx), Math.max(Math.abs(dy), Math.abs(dz))) != radius) continue;
                        BlockPos at = pos.offset(dx, dy, dz);
                        if (isFree(level, at)) return at;
                    }
                }
            }
        }
        return null;
    }

    private static boolean isFree(ServerLevel level, BlockPos pos)
    {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || (state.canBeReplaced() && state.getFluidState().isEmpty() && !state.is(BlockTags.FIRE));
    }

    @Override
    public void onEntity(SpellCast cast, Entity target)
    {
        ServerLevel level = cast.level();
        BlockPos above = target.blockPosition().above(DRIPSTONE_HEIGHT);
        if (!level.getBlockState(above).isAir()) return;
        BlockState stalactite = Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(BlockStateProperties.VERTICAL_DIRECTION, Direction.DOWN);
        // FallingBlockEntity.fall: turns the block at pos into a falling block entity (here there's only air). It hurts
        // what it lands on like a falling stalactite; disableDrop: it shatters rather than dropping as an item.
        FallingBlockEntity falling = FallingBlockEntity.fall(level, above, stalactite);
        falling.setHurtsEntities(cast.stats().damage(), 40);
        falling.disableDrop();
    }
}
