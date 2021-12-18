package com.mowmaster.dust.Block.BlockEntities.DustJar;

import com.mowmaster.dust.Block.BaseBlocks.BaseColoredBlock;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class DustJarBlock extends BaseColoredBlock {

    protected final VoxelShape JAR;

    public DustJarBlock(Properties p_152915_) {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR));
        this.JAR = Shapes.or(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 13.0D, 13.0D));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        ItemStack picked = state.getBlock().getCloneItemStack(world, pos, state);
        int getColor = ColorReference.getColorFromStateInt(state);
        return ColorReference.addColorToItemStack(picked,getColor);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return this.JAR;
    }
}
