package com.mowmaster.dust.Features.Spells.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

// Protection's block effect: warded glass. Solid to mobs (and their arrows), but players walk straight through.
public class WardedGlassBlock extends Block
{
    public WardedGlassBlock(Properties properties)
    {
        super(properties);
    }

    // EntityCollisionContext: the collision check for an entity, which knows which entity it's for
    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        if (context instanceof EntityCollisionContext entityContext && entityContext.getEntity() instanceof Player) return Shapes.empty();
        return Shapes.block();
    }

    // Light and faces show through, like glass
    @Override
    protected boolean skipRendering(BlockState state, BlockState neighbor, net.minecraft.core.Direction direction)
    {
        return neighbor.is(this) || super.skipRendering(state, neighbor, direction);
    }
}
