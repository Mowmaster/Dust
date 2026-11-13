package com.mowmaster.dust.Features.Spells.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

// A thin layer a spell leaves on the ground (Poison's pool, Flesh's blight): it lies on top of a solid block, can be
// walked through, and does something to whatever stands in it. Placing a block there replaces it; it washes away if
// the block under it goes.
public abstract class SpellLayerBlock extends Block
{
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);

    protected SpellLayerBlock(Properties properties)
    {
        super(properties);
    }

    // What standing in it does (server side, every tick while inside)
    protected abstract void affect(ServerLevel level, BlockPos pos, LivingEntity entity);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return Shapes.empty();
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    // Something changed next to it: gone if the ground under it went
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction,
                                     BlockPos neighborPos, BlockState neighbor, RandomSource random)
    {
        return direction == Direction.DOWN && !canSurvive(state, level, pos) ? net.minecraft.world.level.block.Blocks.AIR.defaultBlockState() : state;
    }

    // Placing any block there just replaces it
    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context)
    {
        return true;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effects, boolean intersects)
    {
        if (level instanceof ServerLevel server && entity instanceof LivingEntity living) affect(server, pos, living);
    }

    // Whether a layer could lie at pos: air there, solid ground under it
    public boolean canLieAt(LevelReader level, BlockPos pos)
    {
        return level.getBlockState(pos).isAir() && canSurvive(defaultBlockState(), level, pos);
    }
}
