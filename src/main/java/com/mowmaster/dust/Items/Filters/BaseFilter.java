package com.mowmaster.dust.Items.Filters;

import com.mowmaster.dust.Block.Pedestal.BasePedestalBlockEntity;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.Items.Tools.FilterTool;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.Util.PedestalUtilities;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.extensions.IForgeAbstractMinecart;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.mowmaster.dust.References.Constants.MODID;

public class BaseFilter extends Item implements IPedestalFilter
{
    public boolean filterType = false;

    public BaseFilter(Properties p_41383_) {
        super(p_41383_);
    }

    public static int getFilterMode(ItemStack stack) {
        return readModeFromNBT(stack);
    }

    public String getFilterModeString(int mode) {

        switch(mode)
        {
            case 0: return "item";
            case 1: return "fluid";
            case 2: return "energy";
            case 3: return "xp";
            default: return "item";
        }
    }

    public String getFilterModeString(ItemStack stack) {

        switch(getFilterMode(stack))
        {
            case 0: return "item";
            case 1: return "fluid";
            case 2: return "energy";
            case 3: return "xp";
            default: return "item";
        }
    }

    public static void saveModeToNBT(ItemStack filter, int mode)
    {
        CompoundTag compound = new CompoundTag();
        if(filter.hasTag())
        {
            compound = filter.getTag();
        }
        compound.putInt(MODID+"_filter_mode",mode);
        filter.setTag(compound);
    }

    public static int readModeFromNBT(ItemStack filter) {
        if(filter.hasTag())
        {
            CompoundTag getCompound = filter.getTag();
            return getCompound.getInt(MODID+"_filter_mode");
        }
        return 0;
    }

    public boolean doItemsMatch(ItemStack stackPedestal, ItemStack itemStackIn)
    {
        return ItemHandlerHelper.canItemStacksStack(stackPedestal,itemStackIn);
    }

    //Left Click
    //No methods exist without using a client one and needing to send packets back and forth, BLEH

    //Right Click
    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        Level world = p_41432_;
        Player player = p_41433_;
        InteractionHand hand = p_41434_;
        ItemStack itemInHand = player.getItemInHand(hand);
        ItemStack itemInOffhand = player.getOffhandItem();


        if(itemInOffhand.isEmpty())
        {
            if(!itemInHand.getItem().equals(DeferredRegisterItems.FILTER_BASE))
            {
                if(itemInHand.getItem() instanceof IPedestalFilter)
                {

                    HitResult result = player.pick(5,0,false);
                    if(player.isCrouching() || player.getAbilities().flying)
                    {
                        //System.out.println(result.getType());

                        if(result.getType().equals(HitResult.Type.MISS))
                        {
                            if(player.isCrouching())
                            {
                                ItemStack itemInMainHand = player.getMainHandItem();
                                ItemStack itemInOffHand = player.getOffhandItem();
                                //Should prevent it from its nbt changing???
                                if(itemInOffHand.getItem() instanceof FilterTool)
                                {
                                    if(itemInMainHand.getItem() instanceof IPedestalFilter)
                                    {
                                        removeFilterQueueHandler(itemInMainHand);
                                        //TODO: localize this text
                                        TranslatableComponent output = new TranslatableComponent(MODID + ".filter_message_cleared");
                                        output.withStyle(ChatFormatting.WHITE);
                                        player.displayClientMessage(output,true);
                                        return InteractionResultHolder.success(itemInHand);
                                    }
                                }
                                else
                                {
                                    if(itemInHand.getItem() instanceof IPedestalFilter)
                                    {
                                        boolean getCurrentType = getFilterType(itemInHand);
                                        setFilterType(itemInHand,!getCurrentType);
                                        TranslatableComponent changed = new TranslatableComponent(MODID + ".filter_message_changed");
                                        changed.withStyle(ChatFormatting.GREEN);
                                        TranslatableComponent white = new TranslatableComponent(MODID + ".filter_message_whitelist");
                                        TranslatableComponent black = new TranslatableComponent(MODID + ".filter_message_blacklist");
                                        changed.append((!getCurrentType)?(black):(white));
                                        player.displayClientMessage(changed,true);
                                        return InteractionResultHolder.success(itemInHand);
                                    }
                                }
                            }
                        }
                        else if(result.getType().equals(HitResult.Type.BLOCK))
                        {
                            if(player.isCrouching())
                            {
                                if(itemInHand.getItem() instanceof IPedestalFilter)
                                {
                                    UseOnContext context = new UseOnContext(player,hand,((BlockHitResult) result));
                                    BlockHitResult res = new BlockHitResult(context.getClickLocation(), context.getHorizontalDirection(), context.getClickedPos(), false);
                                    BlockPos posBlock = res.getBlockPos();

                                    List<ItemStack> buildQueue = buildFilterQueue(world,posBlock);

                                    //Restricts it to Items and Fluids only, this way Energy and XP are toggles on/off using the whitelist/blacklist
                                    if(buildQueue.size() > 0 && getFilterMode(itemInHand)<=1)
                                    {
                                        writeFilterQueueToNBT(itemInHand,buildQueue, getFilterMode(itemInHand));
                                        return InteractionResultHolder.success(itemInHand);
                                    }
                                    return InteractionResultHolder.fail(itemInHand);
                                }
                            }
                        }
                    }
                }
            }
        }
        else
        {
            if(!itemInHand.getItem().equals(DeferredRegisterItems.FILTER_BASE))
            {
                if(itemInOffhand.getItem() instanceof IPedestalFilter)
                {
                    HitResult result = player.pick(5,0,false);
                    if(result.getType().equals(HitResult.Type.MISS))
                    {
                        if(player.isCrouching())
                        {
                            int mode = getFilterMode(itemInOffhand)+1;
                            int setNewMode = (mode<=3)?(mode):(0);
                            saveModeToNBT(itemInOffhand,setNewMode);
                            ColorReference.addColorToItemStack(itemInOffhand,getFilterTypeColor(itemInOffhand));
                            player.setItemInHand(hand,itemInOffhand);
                        }
                    }
                }
            }
        }

        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public boolean getFilterType() {
        //state 0|
        //state 1|false = whitelist
        //state 2|true = blacklist
        return filterType;
    }

    @Override
    public boolean getFilterType(ItemStack filterItem) {
        //false = whitelist
        //true = blacklist
        getFilterTypeFromNBT(filterItem,getFilterMode(filterItem));
        return getFilterType();
    }

    public boolean getFilterType(ItemStack filterItem, int mode) {
        //false = whitelist
        //true = blacklist
        getFilterTypeFromNBT(filterItem,mode);
        return getFilterType();
    }

    @Override
    public void setFilterType(ItemStack filterItem, boolean filterSet) {
        filterType = filterSet;
        if(filterSet) { ColorReference.addColorToItemStack(filterItem,2763306); }
        else ColorReference.addColorToItemStack(filterItem,16777215);
        writeFilterTypeToNBT(filterItem,getFilterMode(filterItem));
    }

    public int getFilterTypeColor(ItemStack filterItem)
    {
        return (getFilterType(filterItem))?(2763306):(16777215);
    }

    @Override
    public boolean canAcceptItem(BasePedestalBlockEntity pedestal, ItemStack itemStackIn, int mode) {
        return true;
    }

    public boolean canTransferItems(ItemStack filter) { return !getFilterType(filter,0); }

    public boolean canTransferFluids(ItemStack filter) { return !getFilterType(filter,1); }

    public boolean canTransferEnergy(ItemStack filter)
    {
        return !getFilterType(filter,2);
    }

    public boolean canTransferXP(ItemStack filter)
    {
        return !getFilterType(filter,3);
    }

    @Override
    public boolean canSendItem(BasePedestalBlockEntity pedestal) {
        return true;
    }

    @Override
    public int canAcceptCount(BasePedestalBlockEntity pedestal, ItemStack itemStackIncoming, int mode) {
        return canAcceptCount(pedestal, pedestal.getLevel(), pedestal.getPos(), pedestal.getItemInPedestal(), itemStackIncoming, mode);
    }

    @Override
    public int canAcceptCount(BasePedestalBlockEntity pedestal, Level world, BlockPos pos, ItemStack itemInPedestal, ItemStack itemStackIncoming, int mode) {
        return Math.min(pedestal.getSlotSizeLimit(), itemStackIncoming.getMaxStackSize());
    }

    @Override
    public void removeFilterQueueHandler(ItemStack filterStack) {
        CompoundTag compound = new CompoundTag();
        if(filterStack.hasTag())
        {
            compound = filterStack.getTag();
            for(int i=0; i<4;i++)
            {
                if(compound.contains(getFilterModeString(i)+"filterqueue"))
                {
                    compound.remove(getFilterModeString(i)+"filterqueue");
                    filterStack.setTag(compound);
                }
            }
        }
    }

    @Override
    public int filterQueueSize(ItemStack filterStack, int mode) {
        int filterQueueSize = 0;
        if(filterStack.hasTag())
        {
            CompoundTag getCompound = filterStack.getTag();
            if(getCompound.contains(getFilterModeString(mode)+"filterqueue"))
            {
                getCompound.get(getFilterModeString(mode)+"filterqueue");
                ItemStackHandler handler = new ItemStackHandler();
                handler.deserializeNBT(getCompound);
                return handler.getSlots();
            }
        }

        return filterQueueSize;
    }

    @Override
    public List<ItemStack> buildFilterQueue(Level world, BlockPos invBlock) {
        List<ItemStack> filterQueue = new ArrayList<>();

        LazyOptional<IItemHandler> cap = PedestalUtilities.findItemHandlerAtPos(world,invBlock,true);
        if(cap.isPresent())
        {
            IItemHandler handler = cap.orElse(null);
            if(handler != null)
            {
                int range = handler.getSlots();
                for(int i=0;i<range;i++)
                {
                    ItemStack stackInSlot = handler.getStackInSlot(i);
                    if(!stackInSlot.isEmpty()) {filterQueue.add(stackInSlot);}
                }
            }
        }

        return filterQueue;
    }

    @Override
    public void writeFilterQueueToNBT(ItemStack filterStack, List<ItemStack> builtFilterQueueList, int mode) {
        CompoundTag compound = new CompoundTag();
        CompoundTag compoundStorage = new CompoundTag();
        if(filterStack.hasTag()){compound = filterStack.getTag();}

        ItemStackHandler handler = new ItemStackHandler();
        handler.setSize(builtFilterQueueList.size());

        for(int i=0;i<handler.getSlots();i++) {handler.setStackInSlot(i,builtFilterQueueList.get(i));}

        compoundStorage = handler.serializeNBT();
        compound.put(getFilterModeString(mode)+"filterqueue",compoundStorage);
        filterStack.setTag(compound);
    }

    @Override
    public List<ItemStack> readFilterQueueFromNBT(ItemStack filterStack, int mode) {
        List<ItemStack> filterQueue = new ArrayList<>();
        if(filterStack.hasTag())
        {
            CompoundTag getCompound = filterStack.getTag();
            if(getCompound.contains(getFilterModeString(mode)+"filterqueue"))
            {
                CompoundTag invTag = getCompound.getCompound(getFilterModeString(mode)+"filterqueue");
                ItemStackHandler handler = new ItemStackHandler();
                ((INBTSerializable<CompoundTag>) handler).deserializeNBT(invTag);

                for(int i=0;i<handler.getSlots();i++) {filterQueue.add(handler.getStackInSlot(i));}
            }
        }

        return filterQueue;
    }

    @Override
    public void writeFilterTypeToNBT(ItemStack filterStack, int mode) {
        CompoundTag compound = new CompoundTag();
        if(filterStack.hasTag())
        {
            compound = filterStack.getTag();
        }
        compound.putBoolean(getFilterModeString(mode)+"filter_type",this.filterType);
        filterStack.setTag(compound);
    }

    @Override
    public boolean getFilterTypeFromNBT(ItemStack filterStack, int mode) {
        if(filterStack.hasTag())
        {
            CompoundTag getCompound = filterStack.getTag();
            this.filterType = getCompound.getBoolean(getFilterModeString(mode)+"filter_type");
        }
        return filterType;
    }



    @Override
    public void chatDetails(Player player, BasePedestalBlockEntity pedestal) {
        ItemStack filterStack = pedestal.getFilterInPedestal();
        if(!filterStack.getItem().equals(DeferredRegisterItems.FILTER_BASE.get()))
        {
            TranslatableComponent filterList = new TranslatableComponent(filterStack.getDisplayName().getString());
            filterList.withStyle(ChatFormatting.GOLD);
            player.sendMessage(filterList, Util.NIL_UUID);

            //For each Mode
            for(int i=0;i<4;i++)
            {
                List<ItemStack> filterQueue = readFilterQueueFromNBT(filterStack,i);
                if(filterQueue.size()>0)
                {
                    TranslatableComponent enchant = new TranslatableComponent(MODID + ".filters.tooltip_filterlist");
                    enchant.withStyle(ChatFormatting.LIGHT_PURPLE);
                    player.sendMessage(enchant, Util.NIL_UUID);

                    for(int j=0;j<filterQueue.size();j++) {

                        if(!filterQueue.get(j).isEmpty())
                        {
                            TranslatableComponent enchants = new TranslatableComponent(filterQueue.get(j).getDisplayName().getString());
                            enchants.withStyle(ChatFormatting.GRAY);
                            player.sendMessage(enchants, Util.NIL_UUID);
                        }
                    }
                }
            }
        }
        else
        {
            TranslatableComponent base = new TranslatableComponent(MODID + ".baseItem");
            base.withStyle(ChatFormatting.DARK_RED);
            player.sendMessage(base, Util.NIL_UUID);
        }
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);

        if(!p_41421_.getItem().equals(DeferredRegisterItems.FILTER_BASE))
        {
            boolean filterType = getFilterType(p_41421_);
            TranslatableComponent filterList = new TranslatableComponent(MODID + ".filters.tooltip_filtertype");
            TranslatableComponent white = new TranslatableComponent(MODID + ".filters.tooltip_filterwhite");
            TranslatableComponent black = new TranslatableComponent(MODID + ".filters.tooltip_filterblack");
            filterList.append((filterType)?(black):(white));
            filterList.withStyle(ChatFormatting.GOLD);
            p_41423_.add(filterList);

            List<ItemStack> filterQueue = readFilterQueueFromNBT(p_41421_,getFilterMode(p_41421_));
            if(filterQueue.size()>0)
            {
                TranslatableComponent enchant = new TranslatableComponent(MODID + ".filters.tooltip_filterlist");
                enchant.withStyle(ChatFormatting.LIGHT_PURPLE);
                p_41423_.add(enchant);

                for(int i=0;i<filterQueue.size();i++) {

                    if(!filterQueue.get(i).isEmpty())
                    {
                        TranslatableComponent enchants = new TranslatableComponent(filterQueue.get(i).getDisplayName().getString());
                        enchants.withStyle(ChatFormatting.GRAY);
                        p_41423_.add(enchants);
                    }
                }
            }
        }
        else
        {
            TranslatableComponent base = new TranslatableComponent(MODID + ".filter_tooltip_baseItem");
            base.withStyle(ChatFormatting.DARK_RED);
            p_41423_.add(base);
        }
    }
}
