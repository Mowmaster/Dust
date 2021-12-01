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

public class FilterDurability extends BaseFilter{
    public FilterDurability(Properties p_41383_) {
        super(p_41383_);
    }

    public int getPercentDamaged(ItemStack itemIn)
    {
        if(itemIn.isDamageableItem())
        {
            int maxDamage = itemIn.getMaxDamage();
            int damage = itemIn.getDamageValue();
            int durabilityCurrent = maxDamage-damage;
            int percentDurability = Math.floorDiv((durabilityCurrent*100),maxDamage);
            return percentDurability;
        }
        return 100;
    }

    public int getDurabilityTarget(ItemStack filter)
    {
        int returner = 0;
        List<ItemStack> filterQueue = readFilterQueueFromNBT(filter,getFilterMode(filter));
        if(filterQueue.size()>0)
        {
            for(int i=0;i<filterQueue.size();i++)
            {
                ItemStack stackGet = filterQueue.get(i);
                returner += getPercentDamaged(stackGet);
                if(returner>100)break;
            }
        }

        return (returner>100)?(100):(returner);
    }

    @Override
    public boolean canAcceptItem(BasePedestalBlockEntity pedestal, ItemStack itemStackIn, int mode) {
        boolean filterBool=getFilterType(pedestal.getFilterInPedestal(),mode);
        int durabilityTarget = getDurabilityTarget(pedestal.getFilterInPedestal());

        int percentDurabilityCurrent = getPercentDamaged(itemStackIn);
        int percentToBeat = durabilityTarget;

        if(mode==0)
        {
            if(filterBool)
            {
                return (itemStackIn.isDamageableItem())?((percentDurabilityCurrent<=percentToBeat)?(true):(false)):(false);
            }
            else
            {
                return (itemStackIn.isDamageableItem())?((percentDurabilityCurrent>=percentToBeat)?(true):(false)):(false);
            }
        }
        else return !filterBool;

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
                                    if(itemInHand.getItem() instanceof IPedestalFilter)
                                    {
                                        boolean getCurrentType = getFilterType(itemInHand);
                                        setFilterType(itemInHand,!getCurrentType);
                                        TranslatableComponent changed = new TranslatableComponent(MODID + ".filter_message_changed");
                                        changed.withStyle(ChatFormatting.GREEN);
                                        TranslatableComponent white = new TranslatableComponent(MODID + ".filter_message_above");
                                        TranslatableComponent black = new TranslatableComponent(MODID + ".filter_message_below");
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
                                    if(buildQueue.size() > 0 && getFilterMode(itemInHand)<=0)
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
        if(!filterStack.getItem().equals(DeferredRegisterItems.FILTER_BASE.get()))
        {
            TranslatableComponent filterList = new TranslatableComponent(filterStack.getDisplayName().getString());
            filterList.withStyle(ChatFormatting.WHITE);
            player.sendMessage(filterList, Util.NIL_UUID);

            boolean filterType = getFilterType(filterStack);
            TranslatableComponent filterList2 = new TranslatableComponent(MODID + ".filters.tooltip_filtertype");
            TranslatableComponent above = new TranslatableComponent(MODID + ".filters.tooltip_filterabove");
            TranslatableComponent below = new TranslatableComponent(MODID + ".filters.tooltip_filterbelow");
            filterList2.append((filterType)?(below):(above));
            filterList2.withStyle(ChatFormatting.GOLD);
            player.sendMessage(filterList2, Util.NIL_UUID);

            List<ItemStack> filterQueue = readFilterQueueFromNBT(filterStack,getFilterMode(filterStack));
            if(filterQueue.size()>0)
            {
                TranslatableComponent enchant = new TranslatableComponent(MODID + ".filters.tooltip_filterlist");
                enchant.withStyle(ChatFormatting.LIGHT_PURPLE);
                player.sendMessage(enchant, Util.NIL_UUID);

                TranslatableComponent enchants = new TranslatableComponent(""+getDurabilityTarget(pedestal.getFilterInPedestal())+"");
                enchants.withStyle(ChatFormatting.GRAY);
                player.sendMessage(enchants, Util.NIL_UUID);
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

        boolean filterType = getFilterType(p_41421_);
        TranslatableComponent filterList = new TranslatableComponent(MODID + ".filters.tooltip_filtertype");
        TranslatableComponent white = new TranslatableComponent(MODID + ".filters.tooltip_filterabove");
        TranslatableComponent black = new TranslatableComponent(MODID + ".filters.tooltip_filterbelow");
        filterList.append((filterType)?(black):(white));
        filterList.withStyle(ChatFormatting.GOLD);
        p_41423_.add(filterList);

        List<ItemStack> filterQueue = readFilterQueueFromNBT(p_41421_,getFilterMode(p_41421_));
        if(filterQueue.size()>0)
        {
            TranslatableComponent enchant = new TranslatableComponent(MODID + ".filters.tooltip_filterlist_durability");
            enchant.withStyle(ChatFormatting.LIGHT_PURPLE);
            p_41423_.add(enchant);

            TranslatableComponent enchants = new TranslatableComponent(""+getDurabilityTarget(p_41421_)+"");
            enchants.withStyle(ChatFormatting.GRAY);
            p_41423_.add(enchants);
        }
    }
}
