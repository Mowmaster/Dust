package com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces;

import com.google.common.collect.Maps;
import com.mowmaster.dust.Block.BlockEntities.Tier1.Tier1BaseBlock;
import com.mowmaster.dust.Block.BlockEntities.Tier1.Tier1BaseBlockEntity;
import com.mowmaster.dust.Items.ColorApplicator;
import com.mowmaster.dust.Items.Readable.RepairScrolls.T2RepairScroll;
import com.mowmaster.dust.Recipes.MachineBlockRepairItemsHintRecipe;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.Util.MessageUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Map;
import java.util.stream.IntStream;

import static com.mowmaster.dust.References.Constants.MODID;

public class DustFurnacesBaseBlock extends Tier1BaseBlock
{
    public static final DirectionProperty SIDED_ROTATION_4 = BlockStateProperties.HORIZONTAL_FACING;

    public DustFurnacesBaseBlock(Properties p_152915_) {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR).setValue(SIDED_ROTATION_4, Direction.NORTH));
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
        LevelAccessor levelaccessor = p_48689_.getLevel();
        BlockPos blockpos = p_48689_.getClickedPos();
        Direction direction = p_48689_.getClickedFace();
        BlockState blockstate = levelaccessor.getBlockState(p_48689_.getClickedPos().relative(direction.getOpposite()));
        int getColor = ColorReference.getColorFromStateInt(blockstate);
        return blockstate.is(this) &&
                blockstate.getValue(SIDED_ROTATION_4) == direction
                ?
                ColorReference.addColorToBlockState(this.defaultBlockState(),getColor).setValue(SIDED_ROTATION_4, p_48689_.getHorizontalDirection())
                :
                ColorReference.addColorToBlockState(this.defaultBlockState(),getColor).setValue(SIDED_ROTATION_4, p_48689_.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState p_152033_, Rotation p_152034_) {
        return p_152033_.setValue(SIDED_ROTATION_4, p_152034_.rotate(p_152033_.getValue(SIDED_ROTATION_4)));
    }

    @Override
    public BlockState mirror(BlockState p_152030_, Mirror p_152031_) {
        return p_152030_.rotate(p_152031_.getRotation(p_152030_.getValue(SIDED_ROTATION_4)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_152043_) {
        p_152043_.add(SIDED_ROTATION_4, COLOR_RED, COLOR_GREEN, COLOR_BLUE);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {

        if(!p_60504_.isClientSide())
        {
            ItemStack itemInHand = p_60506_.getItemInHand(p_60507_);
            if(p_60504_.getBlockEntity(p_60505_) instanceof DustFurnacesBaseBlockEntity furnaceBE)
            {
                if(!furnaceBE.isFullyRepaired()) return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
                else if(furnaceBE.getAllowedInputSlotForMachine(itemInHand) != -1)
                {
                    if(furnaceBE.addInputItem(itemInHand,true).getCount() != itemInHand.getCount())
                    {
                        ItemStack stackReturned = furnaceBE.addInputItem(itemInHand,false);
                        if(stackReturned.isEmpty())itemInHand.shrink(itemInHand.getCount());
                        else
                        {
                            itemInHand.shrink(itemInHand.getCount()-stackReturned.getCount());
                        }
                    }
                }
            }
        }
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(p_60515_.getBlock() != p_60518_.getBlock())
        {
            BlockEntity blockEntity = p_60516_.getBlockEntity(p_60517_);
            if(blockEntity instanceof DustFurnacesBaseBlockEntity furnaceblockentity) {
                furnaceblockentity.dropInputsItems(p_60516_,p_60517_);
            }
            super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
        }
    }
}
