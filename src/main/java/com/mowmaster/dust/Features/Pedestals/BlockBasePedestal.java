package com.mowmaster.dust.Features.Pedestals;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jspecify.annotations.Nullable;

public class BlockBasePedestal extends BaseEntityBlock {
    public static final MapCodec<BlockBasePedestal> CODEC = simpleCodec(BlockBasePedestal::new);
    protected final VoxelShape CUP;
    protected final VoxelShape CDOWN;
    protected final VoxelShape CNORTH;
    protected final VoxelShape CSOUTH;
    protected final VoxelShape CEAST;
    protected final VoxelShape CWEST;
    protected final VoxelShape LCUP;
    protected final VoxelShape LCDOWN;
    protected final VoxelShape LCNORTH;
    protected final VoxelShape LCSOUTH;
    protected final VoxelShape LCEAST;
    protected final VoxelShape LCWEST;

    public BlockBasePedestal(Properties properties) {
        super(properties);
        this.CUP = Shapes.or(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D),
                Block.box(5.0D, 2.0D, 5.0D, 11.0D, 10.0D, 11.0D),
                Block.box(4.0D, 10.0D, 4.0D, 12.0D, 12.0D, 12.0D));
        this.CDOWN = Shapes.or(Block.box(3.0D, 14.0D, 3.0D, 13.0D, 16.0D, 13.0D),
                Block.box(5.0D, 6.0D, 5.0D, 11.0D, 14.0D, 11.0D),
                Block.box(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D));
        this.CNORTH = Shapes.or(Block.box(3.0D, 3.0D, 14.0D, 13.0D, 13.0D, 16.0D),
                Block.box(5.0D, 5.0D, 6.0D, 11.0D, 11.0D, 14.0D),
                Block.box(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 6.0D));
        this.CSOUTH = Shapes.or(Block.box(3.0D, 3.0D, 0.0D, 13.0D, 13.0D, 2.0D),
                Block.box(5.0D, 5.0D, 2.0D, 11.0D, 11.0D, 10.0D),
                Block.box(4.0D, 4.0D, 10.0D, 12.0D, 12.0D, 12.0D));
        this.CEAST = Shapes.or(Block.box(0.0D, 3.0D, 3.0D, 2.0D, 13.0D, 13.0D),
                Block.box(2.0D, 5.0D, 5.0D, 10.0D, 11.0D, 11.0D),
                Block.box(10.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D));
        this.CWEST = Shapes.or(Block.box(14.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D),
                Block.box(6.0D, 5.0D, 5.0D, 14.0D, 11.0D, 11.0D),
                Block.box(4.0D, 4.0D, 4.0D, 6.0D, 12.0D, 12.0D));

        this.LCUP = Shapes.or(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D),
                Block.box(4.0D, 2.0D, 4.0D, 12.0D, 3.0D, 12.0D),
                Block.box(5.0D, 3.0D, 5.0D, 11.0D, 4.0D, 11.0D),
                Block.box(4.5D, 4.0D, 4.5D, 11.5D, 5.0D, 11.5D),
                Block.box(5.0D, 5.0D, 5.0D, 11.0D, 10.0D, 11.0D),
                Block.box(4.0D, 10.0D, 4.0D, 12.0D, 12.0D, 12.0D));
        this.LCDOWN = Shapes.or(Block.box(3.0D, 14.0D, 3.0D, 13.0D, 16.0D, 13.0D),
                Block.box(4.0D, 13.0D, 4.0D, 12.0D, 14.0D, 12.0D),
                Block.box(5.0D, 12.0D, 5.0D, 11.0D, 13.0D, 11.0D),
                Block.box(4.5D, 11.0D, 4.5D, 11.5D, 12.0D, 11.5D),
                Block.box(5.0D, 6.0D, 5.0D, 11.0D, 11.0D, 11.0D),
                Block.box(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D));
        this.LCNORTH = Shapes.or(Block.box(3.0D, 3.0D, 14.0D, 13.0D, 13.0D, 16.0D),
                Block.box(4.0D, 4.0D, 13.0D, 12.0D, 12.0D, 14.0D),
                Block.box(5.0D, 5.0D, 12.0D, 11.0D, 11.0D, 13.0D),
                Block.box(4.5D, 4.5D, 11.0D, 11.5D, 11.5D, 12.0D),
                Block.box(5.0D, 5.0D, 6.0D, 11.0D, 11.0D, 11.0D),
                Block.box(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 6.0D));
        this.LCSOUTH = Shapes.or(Block.box(3.0D, 3.0D, 0.0D, 13.0D, 13.0D, 2.0D),
                Block.box(4.0D, 4.0D, 2.0D, 12.0D, 12.0D, 3.0D),
                Block.box(5.0D, 5.0D, 3.0D, 11.0D, 11.0D, 4.0D),
                Block.box(4.5D, 4.5D, 4.0D, 11.5D, 11.5D, 5.0D),
                Block.box(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 10.0D),
                Block.box(4.0D, 4.0D, 10.0D, 12.0D, 12.0D, 12.0D));
        this.LCEAST = Shapes.or(Block.box(0.0D, 3.0D, 3.0D, 2.0D, 13.0D, 13.0D),
                Block.box(2.0D, 4.0D, 4.0D, 3.0D, 12.0D, 12.0D),
                Block.box(3.0D, 5.0D, 5.0D, 4.0D, 11.0D, 11.0D),
                Block.box(4.0D, 4.5D, 4.5D, 5.0D, 11.5D, 11.5D),
                Block.box(5.0D, 5.0D, 5.0D, 10.0D, 11.0D, 11.0D),
                Block.box(10.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D));
        this.LCWEST = Shapes.or(Block.box(14.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D),
                Block.box(13.0D, 4.0D, 4.0D, 14.0D, 12.0D, 12.0D),
                Block.box(12.0D, 5.0D, 5.0D, 13.0D, 11.0D, 11.0D),
                Block.box(11.0D, 4.5D, 4.5D, 12.0D, 11.5D, 11.5D),
                Block.box(6.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D),
                Block.box(4.0D, 4.0D, 4.0D, 6.0D, 12.0D, 12.0D));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return CUP;
        /*Direction direction = state.getValue(FACING);
        return switch (direction) {
            case NORTH -> (state.getValue(LIT)) ? (this.LCNORTH) : (this.CNORTH);
            case SOUTH -> (state.getValue(LIT)) ? (this.LCSOUTH) : (this.CSOUTH);
            case EAST -> (state.getValue(LIT)) ? (this.LCEAST) : (this.CEAST);
            case WEST -> (state.getValue(LIT)) ? (this.LCWEST) : (this.CWEST);
            case DOWN -> (state.getValue(LIT)) ? (this.LCDOWN) : (this.CDOWN);
            default -> (state.getValue(LIT)) ? (this.LCUP) : (this.CUP);
        };*/
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, ItemStack toolStack, boolean willHarvest, FluidState fluid) {

        if(level.getBlockEntity(pos) instanceof EntityBlockPedestal pedestal)
        {
            pedestal.dropInventoryItems();
            level.updateNeighbourForOutputSignal(pos,this);

        }
        return super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {

        if(level.getBlockEntity(pos) instanceof EntityBlockPedestal pedestal)
        {
            if(!pedestal.isEmpty())
            {
                if(player.isShiftKeyDown())
                {
                    ItemStack returned = pedestal.removeStackFromPedestal();
                    if(!player.getInventory().add(returned))
                    {
                        player.drop(returned, false);
                    }
                    level.playSound(player,pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                }
                else
                {
                    ItemStack returned = pedestal.removeSingleFromPedestal();
                    if(!player.getInventory().add(returned))
                    {
                        player.drop(returned, false);
                    }
                    level.playSound(player,pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                }

            }



            level.updateNeighbourForOutputSignal(pos,this);
        }

        super.attack(state, level, pos, player);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if(level.getBlockEntity(pos) instanceof EntityBlockPedestal pedestal)
        {
            int consumedAmount = pedestal.insertStackToPedestal(itemStack);
            if(consumedAmount>0)
            {
                itemStack.consume(consumedAmount,player);
                level.playSound(player,pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
            }

            level.updateNeighbourForOutputSignal(pos,this);
        }


        return InteractionResult.SUCCESS;
    }




    /*
    BLOCK ENTITY STUFF
    */

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EntityBlockPedestal(blockPos, blockState);
    }
}
