package com.mowmaster.dust.block.dustFire;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.block.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public  class DustFireRedBlock extends BaseFireBlock {
    public static final MapCodec<DustFireRedBlock> CODEC = simpleCodec(DustFireRedBlock::new);

    public MapCodec<DustFireRedBlock> codec() {
        return CODEC;
    }

    public DustFireRedBlock(BlockBehaviour.Properties p_56653_) {
        super(p_56653_, 2.0F);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext context) {
        return ModBlocks.DUSTFIREBLOCK_RED.get().defaultBlockState();
    }

    public static BlockState getState(BlockGetter reader, BlockPos pos) {
        return ModBlocks.DUSTFIREBLOCK_RED.get().defaultBlockState();
    }

    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return this.canSurvive(state, level, currentPos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSurviveOnBlock(level.getBlockState(pos.below()));
    }

    public static boolean canSurviveOnBlock(BlockState state) {
        return true;
    }

    protected boolean canBurn(BlockState state) {
        return true;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if(entity instanceof ItemEntity itemEntity) {
            if(itemEntity.getItem().getItem() == Items.PORKCHOP.asItem()) {
                itemEntity.setItem(new ItemStack(Items.COOKED_PORKCHOP, itemEntity.getItem().getCount()));
                itemEntity.fireImmune();
            }
        }

        super.stepOn(level, pos, state, entity);
    }
}
