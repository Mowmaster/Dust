package com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15;

import com.google.common.collect.Maps;
import com.mowmaster.dust.Block.BlockEntities.DustJar.DustJarBlockItem;
import com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.ScrollCrafterBlockBase;
import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import com.mowmaster.dust.Capabilities.Dust.IDustHandler;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.Items.ColorApplicator;
import com.mowmaster.dust.Items.ColoredCrystalDustBase;
import com.mowmaster.dust.Items.Readable.RepairScrolls.BaseRepairScroll;
import com.mowmaster.dust.Items.Readable.RepairScrolls.T2RepairScroll;
import com.mowmaster.dust.Recipes.CrystalNodeRecipe;
import com.mowmaster.dust.Recipes.MachineBlockRepairItemsHintRecipe;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.References.Constants;
import com.mowmaster.dust.Util.DustMagicUtil;
import com.mowmaster.dust.Util.MessageUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.IntStream;

import static com.mowmaster.dust.References.Constants.MODID;

public class ScrollCrafterBlock_T15 extends ScrollCrafterBlockBase {


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

    @Nullable
    protected MachineBlockRepairItemsHintRecipe getRecipe(Level level, ItemStack stackIn) {
        Container cont = Constants.blankContainer;
        cont.setItem(0,stackIn);
        List<MachineBlockRepairItemsHintRecipe> recipes = level.getRecipeManager().getRecipesFor(MachineBlockRepairItemsHintRecipe.MACHINE_REPAIR_ITEMS_HINT,cont,level);
        return recipes.size() > 0 ? level.getRecipeManager().getRecipesFor(MachineBlockRepairItemsHintRecipe.MACHINE_REPAIR_ITEMS_HINT,cont,level).get(0) : null;
    }

    protected String getProcessResultTitle(MachineBlockRepairItemsHintRecipe recipe) {
        return (recipe == null)?(""):(recipe.getResultTitle());
    }

    protected String getProcessResultDescription(MachineBlockRepairItemsHintRecipe recipe) {
        return (recipe == null)?(""):(recipe.getResultDescription());
    }

    protected boolean getProcessResultIsLocalized(MachineBlockRepairItemsHintRecipe recipe) {
        return (recipe == null)?(false):(recipe.getResultLocalized());
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {

        if(!p_60504_.isClientSide())
        {
            ItemStack itemInHand = p_60506_.getItemInHand(p_60507_);
            ItemStack itemInMainHand = p_60506_.getMainHandItem();
            ItemStack itemInOffHand = p_60506_.getOffhandItem();
            if(itemInMainHand.getItem() instanceof ColorApplicator)
            {
                int getColor = ColorReference.getColorFromItemStackInt(itemInMainHand);
                BlockState newState = ColorReference.addColorToBlockState(p_60503_,getColor);
                p_60504_.setBlock(p_60505_,newState,3);
                return InteractionResult.SUCCESS;
            }
            else if(itemInOffHand.getItem() instanceof ColorApplicator)
            {
                int getColor = ColorReference.getColorFromItemStackInt(itemInOffHand);
                BlockState newState = ColorReference.addColorToBlockState(p_60503_,getColor);
                p_60504_.setBlock(p_60505_,newState,3);
                return InteractionResult.SUCCESS;
            }
            else if(p_60504_.getBlockEntity(p_60505_) instanceof ScrollCrafterBlockEntity_T15)
            {
                ScrollCrafterBlockEntity_T15 table = (ScrollCrafterBlockEntity_T15)p_60504_.getBlockEntity(p_60505_);
                if(!table.isFullyRepaired())
                {
                    if(itemInMainHand.getItem() instanceof T2RepairScroll)
                    {
                        T2RepairScroll scroll = (T2RepairScroll)itemInMainHand.getItem();
                        scroll.setMachineBlock(itemInMainHand,this);
                        scroll.setRepairItem(itemInMainHand,table.getNextRepairItem());
                        if(!scroll.getRepairItem(itemInMainHand).isEmpty())
                        {
                            ItemStack repairItemOnScroll = scroll.getRepairItem(itemInMainHand);
                            MachineBlockRepairItemsHintRecipe recipe = getRecipe(p_60504_,repairItemOnScroll);
                            String title = getProcessResultTitle(recipe);
                            String description = getProcessResultDescription(recipe);
                            boolean isLocalized = getProcessResultIsLocalized(recipe);
                            scroll.setHintTitle(itemInMainHand,title);
                            scroll.setHintDescription(itemInMainHand,description);
                            scroll.setHintLocalization(itemInMainHand,isLocalized);
                            if(scroll.getItemFound(itemInMainHand))scroll.setItemFound(itemInMainHand,false);
                            MessageUtils.messagePopup(p_60506_, ChatFormatting.GOLD,MODID + ".hint.scroll_made");
                        }
                        return InteractionResult.SUCCESS;
                    }
                    else if(table.isValidItem(itemInMainHand))
                    {
                        if(table.addRepairItem(p_60506_,itemInMainHand,false))
                        {
                            itemInMainHand.shrink(1);
                            Inventory inv = p_60506_.getInventory();
                            ItemStack itemFound = ItemStack.EMPTY;
                            itemFound = IntStream.range(0,inv.getContainerSize())
                                    .mapToObj((inv)::getItem)//Function being applied to each interval
                                    .filter(itemStack -> itemStack.getItem() instanceof T2RepairScroll)
                                    //.filter(itemStack -> ((T2RepairScroll)itemStack.getItem()).getRepairItem(itemStack).getItem().equals(itemInMainHand.getItem()))
                                    .findFirst().orElse(ItemStack.EMPTY);
                            if(!itemFound.isEmpty())
                            {
                                for(int i=0;i<inv.getContainerSize();i++)
                                {
                                    if(inv.getItem(i).getItem().equals(itemFound.getItem()))
                                    {
                                        ItemStack stackie = inv.getItem(i);
                                        T2RepairScroll scroll = ((T2RepairScroll)stackie.getItem());
                                        scroll.setMachineBlock(stackie,Blocks.AIR);
                                        scroll.setRepairItem(stackie,ItemStack.EMPTY);
                                        scroll.setHintTitle(stackie,"");
                                        scroll.setHintDescription(stackie, "");
                                        scroll.setHintLocalization(stackie, false);
                                        Map<Enchantment, Integer> enchantsNone = Maps.<Enchantment, Integer>newLinkedHashMap();
                                        EnchantmentHelper.setEnchantments(enchantsNone,stackie);
                                        inv.setItem(i,stackie);
                                        break;
                                    }
                                }
                            }
                            MessageUtils.messagePopup(p_60506_, ChatFormatting.GOLD,MODID + ".hint.repair_made");
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
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
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                    else
                    {
                        if(table.addDust(DustMagic.getDustMagicInItemStack(itemInHand), IDustHandler.DustAction.SIMULATE)>0)
                        {
                            itemInHand.shrink(table.addDust(DustMagic.getDustMagicInItemStack(itemInHand), IDustHandler.DustAction.EXECUTE));
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
                    if(!table.getScrollCraftingOutput().isEmpty())
                    {
                        ItemStack stackReturned = table.craftScrolls(itemInHand.getCount());
                        itemInHand.shrink(stackReturned.getCount());
                        ItemHandlerHelper.giveItemToPlayer(p_60506_,stackReturned);
                    }
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
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
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
