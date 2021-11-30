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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.mowmaster.dust.References.Constants.MODID;

public class FilterFood extends BaseFilter{
    public FilterFood(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean canAcceptItem(BasePedestalBlockEntity pedestal, ItemStack itemStackIn) {
        boolean filterBool=getFilterType(pedestal.getFilterInPedestal());
        boolean returner = filterBool;

        if(itemStackIn.isEnchanted() || itemStackIn.getItem().equals(Items.ENCHANTED_BOOK))
        {
            ItemStack filter = pedestal.getFilterInPedestal();
            List<ItemStack> stackCurrent = readFilterQueueFromNBT(filter,getFilterMode(filter));
            int range = stackCurrent.size();

            Map<Enchantment, Integer> mapIncomming = EnchantmentHelper.getEnchantments(itemStackIn);

            for(Map.Entry<Enchantment, Integer> entry : mapIncomming.entrySet()) {
                Enchantment enchantment = entry.getKey();
                ItemStack itemFromInv = ItemStack.EMPTY;
                itemFromInv = IntStream.range(0,range)//Int Range
                        .mapToObj((stackCurrent)::get)//Function being applied to each interval
                        //Check to make sure filter item is enchanted
                        .filter(itemStack -> itemStack.isEnchanted() || itemStack.getItem().equals(Items.ENCHANTED_BOOK))
                        //Check if filter item has any enchant that the item in the pedestal has
                        .filter(itemStack -> EnchantmentHelper.getEnchantments(itemStack).containsKey(enchantment))
                        .findFirst().orElse(ItemStack.EMPTY);

                if(!itemFromInv.isEmpty())
                {
                    returner = !filterBool;
                }
            }
        }

        return returner;
    }

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
                            return InteractionResultHolder.fail(itemInHand);
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
}
