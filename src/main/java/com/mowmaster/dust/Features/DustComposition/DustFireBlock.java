package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.mowlib.tint.CBTintBlockEntity;
import com.mowmaster.mowlib.tint.IColorableBlock;
import com.mowmaster.mowlib.tint.ITintHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

// The fire a Magical Flint and Steel lights by burning something: its flames take the colour of the burned thing's dust
// (DustComposition#color). It doesn't spread or burn blocks, and goes out by itself after a while.
//
// The colour is kept in MowLib's tint block entity: implementing IColorableBlock gives the block a CBTintBlockEntity,
// and MowLib's client tint source colours every model face with "tintindex": 0 from it. Players can't change it: dyeing
// only happens when a block's useItemOn calls tryDyeBlock, and this one doesn't.
// The flame textures are greyscale so the tint shows its true colour (a tint multiplies the texture's colour).
public class DustFireBlock extends BaseFireBlock implements IColorableBlock
{
    // How long it burns: 15-25 seconds
    private static final int MIN_BURN_TICKS = 300;
    private static final int MAX_BURN_TICKS = 500;

    public DustFireBlock(Properties properties)
    {
        // 1.0: the damage it does to whoever stands in it (normal fire 1, soul fire 2)
        super(properties, 1.0F);
    }

    // Lights a dust fire here in this colour. Returns false if it can't burn here.
    public boolean light(Level level, BlockPos pos, int color)
    {
        BlockState state = defaultBlockState();
        if (!level.getBlockState(pos).canBeReplaced() || !state.canSurvive(level, pos)) return false;
        level.setBlock(pos, state, UPDATE_ALL_IMMEDIATE);
        if (level.getBlockEntity(pos) instanceof ITintHolder tint) tint.setColor(0, color);
        return true;
    }

    // The colour of the dust fire at pos (white if it has none)
    public static int colorAt(LevelReader level, BlockPos pos)
    {
        return level.getBlockEntity(pos) instanceof ITintHolder tint ? tint.getTint(0) : CBTintBlockEntity.DEFAULT_TINT;
    }

    // Burns on top of anything solid, and on the surface of lava (where lava itself is burned)
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP) || level.getFluidState(below).is(FluidTags.LAVA);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour,
                                     BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        return canSurvive(state, level, pos) ? state : Blocks.AIR.defaultBlockState();
    }

    // Unlike BaseFireBlock#onPlace this never lights a nether portal; it just starts the countdown to going out
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
    {
        if (oldState.is(state.getBlock())) return;
        if (!state.canSurvive(level, pos)) {
            level.removeBlock(pos, false);
            return;
        }
        level.scheduleTick(pos, this, MIN_BURN_TICKS + level.getRandom().nextInt(MAX_BURN_TICKS - MIN_BURN_TICKS));
    }

    // The scheduled tick: burnt out
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        level.removeBlock(pos, false);
    }

    // BaseFireBlock's crackle and smoke, plus a few motes of dust in the flame's colour drifting up
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        super.animateTick(state, level, pos, random);
        if (random.nextInt(3) == 0) {
            level.addParticle(new DustParticleOptions(colorAt(level, pos), 1.0F),
                    pos.getX() + random.nextDouble(), pos.getY() + 0.3 + random.nextDouble() * 0.6, pos.getZ() + random.nextDouble(),
                    0.0, 0.05, 0.0);
        }
    }

    // Burns mobs and players like fire, but leaves dropped items alone, so the rest of a stack that was burned from
    // isn't destroyed by the fire
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise)
    {
        if (entity instanceof ItemEntity) return;
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
    }

    // Only used for BaseFireBlock's smoke particles near flammable neighbours
    @Override
    protected boolean canBurn(BlockState state)
    {
        return true;
    }
}
