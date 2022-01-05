package com.mowmaster.dust.Items.Scrolls;

import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.Util.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;


public class ScrollBase extends EffectItemBase
{
    public ScrollBase(Item.Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {

        if(getEffectFromScroll(stack) !=null)
        {
            if(getEffectFromScroll(stack).getEffect().isInstantenous())return 4;
        }

        switch(getRenderType(stack))
        {
            case 0: return 32;
            case 1: return 16;
            case 2: return 8;
            case 3: return 4;
            default: return 64;
        }
    }

    public static int getColorRender(ItemStack stack)
    {
        return ColorReference.getColorFromItemStackInt(stack);
    }

    public static int getColorRenderRibbon(ItemStack stack)
    {
        /*
        ribbon = potency color
        normal:RED - 16733525
        1:ORANGE - 16755200
        2:YELLOW - 16777045
        3:GREEN - 5635925
        4:BLUE - 43775
        5:PURPLE - 11163135
        6+:PINK - 16755455
         */
        if(getEffectFromScroll(stack) != null)
        {
            switch(getEffectFromScroll(stack).getAmplifier())
            {
                case 0: return 16733525;
                case 1: return 16755200;
                case 2: return 16777045;
                case 3: return 5635925;
                case 4: return 43775;
                case 5: return 11163135;
                default: return 16755455;
            }
        }
        return 16733525;
    }

    public static int getColorRenderCoin(ItemStack stack)
    {
        if(getEffectFromScroll(stack) != null)
        {
            MobEffectCategory category = getEffectFromScroll(stack).getEffect().getCategory();
            if(category == MobEffectCategory.BENEFICIAL) {return 5635925;}//Green
            else if(category == MobEffectCategory.NEUTRAL) {return 43775;}//Blue
            else if(category == MobEffectCategory.HARMFUL) {return 16733525;}//Red
        }
        return ColorReference.getColorFromItemStackInt(stack);
    }

    public static int getRenderType(ItemStack stack)
    {
        if(getEffectFromScroll(stack) != null)
        {
            int girth = (getEffectFromScroll(stack).getDuration()/20);
            if(girth>0 && girth<=450)return 0;
            else if(girth>450 && girth<=1800)return 1;
            else if(girth>1800 && girth<=3600)return 2;
            else if(girth>3600)return 3;
        }
        return 0;
    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        Level world = p_41432_;
        if(!world.isClientSide())
        {
            Player player = p_41433_;
            InteractionHand hand = p_41434_;
            ItemStack stackInHand = player.getItemInHand(hand);
            ItemStack mainhand = player.getMainHandItem();
            ItemStack offhand = player.getOffhandItem();
            //Build Color List from NBT
            HitResult result = player.pick(5,0,false);
            BlockPos pos = new BlockPos(result.getLocation().x,result.getLocation().y,result.getLocation().z);

            if(stackInHand.getItem() instanceof ScrollBase)
            {
                MobEffectInstance getEffect = getEffectFromScroll(stackInHand);
                if(getEffect != null)
                {
                    System.out.println(result.getType());
                    if(result.getType().equals(HitResult.Type.ENTITY))
                    {
                        //Give Effects to Entity

                        List<LivingEntity> eList = world.getEntitiesOfClass(LivingEntity.class,new AABB(pos));
                        LivingEntity entity = (eList.size()>0)?(eList.get(0)):(null);
                        if(entity != null)
                        {
                            if(entity.addEffect(getEffect))
                            {
                                stackInHand.shrink(1);
                                return InteractionResultHolder.success(stackInHand);
                            }
                        }

                    }
                    /*else if(result.getType().equals(HitResult.Type.BLOCK))
                    {
                        //Give Effects to Block??? Traps???
                    }*/
                    else
                    {
                        if(player.addEffect(getEffect))
                        {
                            stackInHand.shrink(1);
                            return InteractionResultHolder.success(stackInHand);
                        }
                    }
                }
            }
        }

        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);

        MobEffectInstance getEffect = getEffectFromScroll(p_41421_);
        if(getEffect != null)
        {
            ChatFormatting format = ChatFormatting.WHITE;
            MobEffectCategory cat = getEffect.getEffect().getCategory();
            if(cat == MobEffectCategory.BENEFICIAL) {format = ChatFormatting.GREEN;}//Green
            else if(cat == MobEffectCategory.NEUTRAL) {format = ChatFormatting.BLUE;}//Blue
            else if(cat == MobEffectCategory.HARMFUL) {format = ChatFormatting.RED;}//Red
            int minutes = (getEffect.getDuration()/20)/60;
            int seconds =  (int)(((double)(getEffect.getDuration()/20)%60));
            TooltipUtils.addTooltipMessageWithStyle(p_41423_,getEffect.getEffect().getDisplayName().getString() + " " + TooltipUtils.getRomanNumeral(getEffect.getAmplifier()) + " - " + minutes + ":" + ((seconds<10)?("0"+seconds):(seconds)),format);
        }
    }
}
