package com.mowmaster.dust.Features.Spells.blocks;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

// A block a spell has made unreal for a while (PhantomBlockEntity remembers what it is):
//   Veiled (Darkness)   see-through, but still solid
//   Phased (Spirit)     see-through and passable
// When its time is up (a scheduled tick) it becomes itself again; a phased block waits while anything stands in it.
// It can't be broken meanwhile (so nothing is lost or duplicated).
public class PhantomBlock extends Block implements EntityBlock
{
    private final boolean solid;

    public PhantomBlock(Properties properties, boolean solid)
    {
        super(properties);
        this.solid = solid;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new PhantomBlockEntity(pos, state);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return solid ? Shapes.block() : Shapes.empty();
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (!solid && !level.getEntitiesOfClass(LivingEntity.class, new AABB(pos)).isEmpty()) {
            level.scheduleTick(pos, this, 20);
            return;
        }
        BlockState original = level.getBlockEntity(pos) instanceof PhantomBlockEntity phantom ? phantom.original() : null;
        level.setBlockAndUpdate(pos, original == null ? net.minecraft.world.level.block.Blocks.AIR.defaultBlockState() : original);
    }

    // Turns the block at pos phantom for ticks: veiled (solid) or phased. Plain whole blocks only (as corruption).
    public static boolean make(ServerLevel level, BlockPos pos, boolean solid, int ticks)
    {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || !state.getFluidState().isEmpty() || level.getBlockEntity(pos) != null || state.getDestroySpeed(level, pos) < 0
                || !state.isCollisionShapeFullBlock(level, pos)) {
            return false;
        }
        Block phantom = (solid ? DustBlockRegistry.VEILED : DustBlockRegistry.PHASED).get();
        level.setBlockAndUpdate(pos, phantom.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof PhantomBlockEntity entity) entity.setOriginal(state);
        level.scheduleTick(pos, phantom, ticks);
        return true;
    }
}
