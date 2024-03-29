package com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15;

import com.mowmaster.dust.Block.BlockEntities.DustJar.DustJarBlockItem;
import com.mowmaster.dust.Block.BlockEntities.Tier1.Tier1BaseBlock;
import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import com.mowmaster.dust.Capabilities.Dust.IDustHandler;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.Items.ColoredCrystalDustBase;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.Util.MessageUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.*; 

import static com.mowmaster.dust.References.Constants.MODID;

public class ScrollCrafterBlock_T15 extends Tier1BaseBlock {


    public static final DirectionProperty SIDED_ROTATION_4 = BlockStateProperties.HORIZONTAL_FACING;
    protected final VoxelShape TABLE;

    public ScrollCrafterBlock_T15(Properties p_152915_) {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR).setValue(SIDED_ROTATION_4, Direction.NORTH));
        this.TABLE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 4.0D, 12.0D, 4.0D),
                Block.box(12.0D, 0.0D, 12.0D, 16.0D, 12.0D, 16.0D),
                Block.box(0.0D, 0.0D, 12.0D, 4.0D, 12.0D, 16.0D),
                Block.box(12.0D, 0.0D, 0.0D, 16.0D, 12.0D, 4.0D),
                Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return this.TABLE;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        ItemStack picked = state.getBlock().getCloneItemStack(world, pos, state);
        int getColor = ColorReference.getColorFromStateInt(state);
        return ColorReference.addColorToItemStack(picked,getColor);
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

    //Left Click Action
    @Override
    public void attack(BlockState p_60499_, Level p_60500_, BlockPos p_60501_, Player p_60502_) {
        if(!p_60500_.isClientSide())
        {
            BlockEntity blockEntity = p_60500_.getBlockEntity(p_60501_);
            if(blockEntity instanceof ScrollCrafterBlockEntity_T15 scrollCrafter)
            {
                ItemStack itemInHand = p_60502_.getMainHandItem();
                ItemStack itemInOffHand = p_60502_.getOffhandItem();
                ItemStack itemInMainHand = p_60502_.getMainHandItem();

                if(scrollCrafter.hasItemInTable(0) && itemInOffHand.getItem().equals(scrollCrafter.getItemInTable(0).getItem()))
                {
                    if(p_60502_.isCrouching())
                    {
                        ItemHandlerHelper.giveItemToPlayer(p_60502_,scrollCrafter.removeItemInTable(0,scrollCrafter.getItemInTable(0).getCount()));
                    }
                    else
                    {
                        ItemHandlerHelper.giveItemToPlayer(p_60502_,scrollCrafter.removeItemInTable(0,1));
                    }
                }
                else if(scrollCrafter.hasItemInTable(1) && itemInOffHand.getItem().equals(scrollCrafter.getItemInTable(1).getItem()))
                {
                    if(p_60502_.isCrouching())
                    {
                        ItemHandlerHelper.giveItemToPlayer(p_60502_,scrollCrafter.removeItemInTable(1,scrollCrafter.getItemInTable(1).getCount()));
                    }
                    else
                    {
                        ItemHandlerHelper.giveItemToPlayer(p_60502_,scrollCrafter.removeItemInTable(1,1));
                    }
                }
                else if(scrollCrafter.hasItemInTable(2) && itemInOffHand.getItem().equals(scrollCrafter.getItemInTable(2).getItem()))
                {
                    if(p_60502_.isCrouching())
                    {
                        ItemHandlerHelper.giveItemToPlayer(p_60502_,scrollCrafter.removeItemInTable(2, scrollCrafter.getItemInTable(2).getCount()));
                    }
                    else
                    {
                        ItemHandlerHelper.giveItemToPlayer(p_60502_,scrollCrafter.removeItemInTable(2, 1));
                    }
                }
                else if(scrollCrafter.hasDust() && itemInOffHand.getItem() instanceof DustJarBlockItem && !(itemInMainHand.getItem() instanceof DustJarBlockItem))
                {
                    DustMagic getJarMagic = DustMagic.getDustMagicInItemStack(itemInOffHand);
                    int capacity = (itemInOffHand.hasTag())?((itemInOffHand.getTag().contains(MODID + "_dustCapacity"))?(itemInOffHand.getTag().getInt(MODID + "_dustCapacity")):(1000)):(1000);
                    int spaceInJar = capacity - getJarMagic.getDustAmount();
                    if(getJarMagic.isDustEqualOrEmpty(scrollCrafter.getStoredDust()))
                    {
                        if(scrollCrafter.removeDust(spaceInJar, IDustHandler.DustAction.SIMULATE).getDustAmount()>0)
                        {
                            if(p_60502_.isCrouching())
                            {
                                DustMagic crafterRemoval = scrollCrafter.removeDust(10, IDustHandler.DustAction.EXECUTE);
                                if(getJarMagic.getDustColor()<0 && getJarMagic.getDustAmount()<=0) { getJarMagic = crafterRemoval; }
                                else { getJarMagic.setDustAmount((getJarMagic.getDustAmount() + crafterRemoval.getDustAmount())); }
                                DustMagic.setDustMagicInStack(itemInOffHand,getJarMagic);
                                MessageUtils.messagePopupWithAppend(p_60502_,ChatFormatting.WHITE,MODID + ".dustmagic_amount_post_removal", Arrays.asList(""+scrollCrafter.getStoredDust().getDustAmount()+""));
                            }
                            else
                            {
                                DustMagic crafterRemoval = scrollCrafter.removeDust(1, IDustHandler.DustAction.EXECUTE);
                                if(getJarMagic.getDustColor()<0 && getJarMagic.getDustAmount()<=0) { getJarMagic = crafterRemoval; }
                                else { getJarMagic.setDustAmount((getJarMagic.getDustAmount() + crafterRemoval.getDustAmount())); }
                                DustMagic.setDustMagicInStack(itemInOffHand,getJarMagic);
                                MessageUtils.messagePopupWithAppend(p_60502_,ChatFormatting.WHITE,MODID + ".dustmagic_amount_post_removal", Arrays.asList(""+scrollCrafter.getStoredDust().getDustAmount()+""));
                            }
                        }
                    }
                }
                else if(scrollCrafter.hasDust() && !(itemInOffHand.getItem() instanceof DustJarBlockItem) && itemInMainHand.getItem() instanceof DustJarBlockItem)
                {
                    DustMagic getJarMagic = DustMagic.getDustMagicInItemStack(itemInMainHand);
                    int capacity = (itemInMainHand.hasTag())?((itemInMainHand.getTag().contains(MODID + "_dustCapacity"))?(itemInMainHand.getTag().getInt(MODID + "_dustCapacity")):(1000)):(1000);
                    int spaceInJar = capacity - getJarMagic.getDustAmount();
                    if(getJarMagic.isDustEqualOrEmpty(scrollCrafter.getStoredDust()))
                    {
                        if(scrollCrafter.removeDust(spaceInJar, IDustHandler.DustAction.SIMULATE).getDustAmount()>0)
                        {
                            if(p_60502_.isCrouching())
                            {
                                DustMagic crafterRemoval = scrollCrafter.removeDust(spaceInJar, IDustHandler.DustAction.EXECUTE);
                                if(getJarMagic.getDustColor()<0 && getJarMagic.getDustAmount()<=0) { getJarMagic = crafterRemoval; }
                                else { getJarMagic.setDustAmount((getJarMagic.getDustAmount() + crafterRemoval.getDustAmount())); }
                                DustMagic.setDustMagicInStack(itemInMainHand,getJarMagic);
                                MessageUtils.messagePopupWithAppend(p_60502_,ChatFormatting.WHITE,MODID + ".dustmagic_amount_post_removal", Arrays.asList(""+scrollCrafter.getStoredDust().getDustAmount()+""));
                            }
                            else
                            {
                                DustMagic crafterRemoval = scrollCrafter.removeDust(100, IDustHandler.DustAction.EXECUTE);
                                if(getJarMagic.getDustColor()<0 && getJarMagic.getDustAmount()<=0) { getJarMagic = crafterRemoval; }
                                else { getJarMagic.setDustAmount((getJarMagic.getDustAmount() + crafterRemoval.getDustAmount())); }
                                DustMagic.setDustMagicInStack(itemInMainHand,getJarMagic);
                                MessageUtils.messagePopupWithAppend(p_60502_,ChatFormatting.WHITE,MODID + ".dustmagic_amount_post_removal", Arrays.asList(""+scrollCrafter.getStoredDust().getDustAmount()+""));
                            }
                        }
                    }
                }
            }
        }

        super.attack(p_60499_, p_60500_, p_60501_, p_60502_);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {

        if(!p_60504_.isClientSide())
        {
            ItemStack itemInHand = p_60506_.getItemInHand(p_60507_);
            ItemStack itemInMainHand = p_60506_.getMainHandItem();
            ItemStack itemInOffHand = p_60506_.getOffhandItem();

            if(p_60504_.getBlockEntity(p_60505_) instanceof ScrollCrafterBlockEntity_T15 table)
            {
                if(!table.isFullyRepaired()) return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
                else if(itemInHand.getItem() instanceof ColoredCrystalDustBase)
                {
                    //System.out.println("THIS IS DUST");
                    if(table.hasDust())
                    {
                        //System.out.println("HAS DUST");
                        if(table.canAcceptDust(DustMagic.getDustMagicInItemStack(itemInHand)))
                        {
                            if(table.addDust(DustMagic.getDustMagicInItemStack(itemInHand), IDustHandler.DustAction.SIMULATE)>0)
                            {
                                itemInHand.shrink(table.addDust(DustMagic.getDustMagicInItemStack(itemInHand), IDustHandler.DustAction.EXECUTE));
                                MessageUtils.messagePopupWithAppend(p_60506_,ChatFormatting.WHITE,MODID + ".dustmagic_amount_post_removal", Arrays.asList(""+table.getStoredDust().getDustAmount()+""));
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                    else
                    {
                        if(table.addDust(DustMagic.getDustMagicInItemStack(itemInHand), IDustHandler.DustAction.SIMULATE)>0)
                        {
                            itemInHand.shrink(table.addDust(DustMagic.getDustMagicInItemStack(itemInHand), IDustHandler.DustAction.EXECUTE));
                            MessageUtils.messagePopupWithAppend(p_60506_,ChatFormatting.WHITE,MODID + ".dustmagic_amount_post_removal", Arrays.asList(""+table.getStoredDust().getDustAmount()+""));
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
                else if(itemInHand.getItem() instanceof DustJarBlockItem)
                {
                    DustMagic magicJar = DustMagic.getDustMagicInItemStack(itemInHand);
                    if(table.hasDust())
                    {
                        if(magicJar.getDustAmount()>0)
                        {
                            DustMagic magicTable = table.getStoredDust();
                            if(magicJar.isDustEqual(magicTable))
                            {
                                int count = table.getStoredDust().getDustAmount();
                                int capacity = table.getDustCapacity();
                                int spaceInTable = capacity-count;
                                int countToAdd = (magicJar.getDustAmount()>spaceInTable)?(spaceInTable):(magicJar.getDustAmount());
                                DustMagic magicToAdd = new DustMagic(magicJar.getDustColor(),countToAdd);
                                if(table.addDust(magicToAdd, IDustHandler.DustAction.SIMULATE)>0)
                                {
                                    int countHappened = table.addDust(magicToAdd, IDustHandler.DustAction.EXECUTE);
                                    if(countHappened>0)
                                    {
                                        int magicLeftAmount = magicJar.getDustAmount()-countHappened;
                                        DustMagic magicLeft = (magicLeftAmount<=0)?(new DustMagic(-1, 0)):(new DustMagic(magicJar.getDustColor(),magicLeftAmount));
                                        DustMagic.setDustMagicInStack(itemInHand,magicLeft);
                                        MessageUtils.messagePopupWithAppend(p_60506_,ChatFormatting.WHITE,MODID + ".dustmagic_amount_post_removal", Arrays.asList(""+table.getStoredDust().getDustAmount()+""));
                                        return InteractionResult.SUCCESS;
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        if(magicJar.getDustAmount()>0)
                        {
                            if(table.addDust(magicJar, IDustHandler.DustAction.SIMULATE) > 0)
                            {
                                if(table.addDust(magicJar, IDustHandler.DustAction.EXECUTE) > 0)
                                {
                                    DustMagic.setDustMagicInStack(itemInHand,new DustMagic(-1, 0));
                                    MessageUtils.messagePopupWithAppend(p_60506_,ChatFormatting.WHITE,MODID + ".dustmagic_amount_post_removal", Arrays.asList(""+table.getStoredDust().getDustAmount()+""));
                                    return InteractionResult.SUCCESS;
                                }
                            }
                        }
                    }
                }
                else if(table.isItemAllowedInTable(itemInHand))
                {
                    if(table.addItemInTable(itemInHand,true).getCount() < itemInHand.getCount())
                    {
                        ItemStack stackReturned = table.addItemInTable(itemInHand,false);
                        itemInHand.shrink(itemInHand.getCount()-stackReturned.getCount());
                    }
                }
                else if(itemInHand.getItem().equals(Items.STRING))
                {
                    if(table.hasEnoughToCraftScroll())
                    {
                        if(!table.getScrollCrafted().isEmpty())
                        {
                            ItemStack stackReturned = table.craftScrolls(itemInHand.getCount());
                            itemInHand.shrink(stackReturned.getCount());
                            ItemHandlerHelper.giveItemToPlayer(p_60506_,stackReturned);
                        }
                        else {MessageUtils.messagePopup(p_60506_,ChatFormatting.WHITE,MODID + ".scroll_crafter.not_enough_dust");}
                    }
                    else {MessageUtils.messagePopup(p_60506_,ChatFormatting.WHITE,MODID + ".scroll_crafter.not_enough_to_craft");}
                }
                else if(itemInMainHand.isEmpty())
                {
                    if(p_60506_.isCrouching())
                    {
                        if(table.hasDust())
                        {
                            DustMagic magic = table.getStoredDust();
                            MessageUtils.messagePlayerChatWithAppend(p_60506_,ChatFormatting.WHITE,MODID + ".dust_in_jar", Arrays.asList(ColorReference.getColorName(magic.getDustColor()),": ",""+magic.getDustAmount()+""));
                        }
                        if(!table.getScrollCrafted().isEmpty())MessageUtils.messagePlayerChat(p_60506_,ChatFormatting.RED,table.getScrollCrafted().getDisplayName().getString());
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
            if(blockEntity instanceof ScrollCrafterBlockEntity_T15 crafter) {
                crafter.dropInventoryItems(p_60516_,p_60517_);
                crafter.dropInventoryItemsPrivate(p_60516_,p_60517_);
            }
            super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_152043_) {
        p_152043_.add(SIDED_ROTATION_4, COLOR_RED, COLOR_GREEN, COLOR_BLUE);
    }



    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return DeferredBlockEntityTypes.CRAFTER_SCROLL_T15.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : (level0, pos, state0, blockEntity) -> ((ScrollCrafterBlockEntity_T15) blockEntity).tick();
    }
}
