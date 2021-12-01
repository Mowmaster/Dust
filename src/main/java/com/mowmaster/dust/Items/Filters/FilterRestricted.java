package com.mowmaster.dust.Items.Filters;

import com.mowmaster.dust.Block.Pedestal.BasePedestalBlockEntity;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.Items.Tools.FilterTool;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.IntStream;

import static com.mowmaster.dust.References.Constants.MODID;

public class FilterRestricted extends BaseFilter{
    public FilterRestricted(Properties p_41383_) {
        super(p_41383_);
    }

    public static int getColor(ItemStack filterIn)
    {
        return 65280;
    }

    @Override
    public int canAcceptCount(BasePedestalBlockEntity pedestal, Level world, BlockPos pos, ItemStack itemInPedestal, ItemStack itemStackIncoming, int mode) {
        if(itemInPedestal.isEmpty())
        {
            ItemStack filter = pedestal.getFilterInPedestal();
            List<ItemStack> stackCurrent = readFilterQueueFromNBT(filter,mode);
            int range = stackCurrent.size();
            int count = 0;
            int maxIncomming = itemStackIncoming.getMaxStackSize();
            for(int i=0;i<range;i++)
            {
                count +=stackCurrent.get(i).getCount();
                if(count>=maxIncomming)break;
            }
            if(mode==0)
            {
                return (count>0)?((count>maxIncomming)?(maxIncomming):(count)):(1);
            }
            else return count;
        }
        return 0;
    }

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
                                    //Dont set white/blacklist as we're only doing restricted for all types
                                    return InteractionResultHolder.fail(itemInHand);
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
                                    if(buildQueue.size() > 0 && getFilterMode(itemInHand)<=3)
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
                            return InteractionResultHolder.success(itemInHand);
                        }
                    }
                }
            }
        }

        return InteractionResultHolder.fail(itemInHand);
    }

    @Override
    public void chatDetails(Player player, BasePedestalBlockEntity pedestal) {
        ItemStack filterStack = pedestal.getFilterInPedestal();
        TranslatableComponent filterList = new TranslatableComponent(filterStack.getDisplayName().getString());
        filterList.withStyle(ChatFormatting.GOLD);
        player.sendMessage(filterList, Util.NIL_UUID);

        TranslatableComponent enchant = new TranslatableComponent(MODID + ".filters.tooltip_filterlist_count");
        enchant.withStyle(ChatFormatting.LIGHT_PURPLE);
        player.sendMessage(enchant, Util.NIL_UUID);

        TranslatableComponent enchants = new TranslatableComponent("1");
        List<ItemStack> filterQueue = readFilterQueueFromNBT(filterStack,getFilterMode(filterStack));
        int range = filterQueue.size();
        if(range>0)
        {
            int count = 0;
            for(int i=0;i<range;i++)
            {
                count +=filterQueue.get(i).getCount();
                if(count>=64)break;
            }

            enchants = new TranslatableComponent(""+((count>0)?((count>64)?(64):(count)):(1))+"");
        }
        enchants.withStyle(ChatFormatting.GRAY);
        player.sendMessage(enchants, Util.NIL_UUID);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {

        TranslatableComponent enchant = new TranslatableComponent(MODID + ".filters.tooltip_filterlist_count");
        enchant.withStyle(ChatFormatting.LIGHT_PURPLE);
        p_41423_.add(enchant);

        TranslatableComponent enchants = new TranslatableComponent("1");
        List<ItemStack> filterQueue = readFilterQueueFromNBT(p_41421_,getFilterMode(p_41421_));
        int range = filterQueue.size();
        if(range>0)
        {
            int count = 0;
            for(int i=0;i<range;i++)
            {
                count +=filterQueue.get(i).getCount();
                if(count>=64)break;
            }

            enchants = new TranslatableComponent(""+((count>0)?((count>64)?(64):(count)):(1))+"");

        }
        enchants.withStyle(ChatFormatting.GRAY);
        p_41423_.add(enchants);
    }
}
