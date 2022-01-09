package com.mowmaster.dust.Items.Scrolls;

import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.Util.NameComponentUtils;
import com.mowmaster.dust.Util.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static com.mowmaster.dust.References.Constants.MODID;


public class ScrollBase extends EffectItemBase
{
    private boolean canAddtoPlayer = true;
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
            case 0: return 16;
            case 1: return 8;
            case 2: return 4;
            case 3: return 1;
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
        MobEffectInstance getEffect = getEffectFromScroll(stack);
        if(getEffect != null)
        {
            int girth = (getEffectFromScroll(stack).getDuration()/20);
            if(getEffect.getEffect().isInstantenous())
            {
                int dur = getEffect.getDuration();
                if(dur>0 && dur<=25)return 0;
                else if(dur>50 && dur<=100)return 1;
                else if(dur>100 && dur<=200)return 2;
                else if(dur>200)return 3;
            }
            if(girth>0 && girth<=45)return 0;
            else if(girth>45 && girth<=180)return 1;
            else if(girth>180 && girth<=720)return 2;
            else if(girth>720)return 3;
        }
        return 0;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity entity, InteractionHand hand) {

        if (entity.level.isClientSide) return InteractionResult.PASS;
        if(stack.getItem() instanceof ScrollBase && playerIn.isCrouching())
        {
            MobEffectInstance getEffect = getEffectFromScroll(stack);
            if(getEffect != null)
            {
                if (entity instanceof LivingEntity) {
                    LivingEntity target = (LivingEntity)entity;
                    if(target != null)
                    {
                        MobEffectInstance getEffectMob = new MobEffectInstance(getEffect.getEffect(),getEffect.getDuration(), getEffect.getAmplifier(),false,true, ((entity instanceof Player)?(true):(false)));

                        if(target.addEffect(getEffectMob))
                        {
                            this.canAddtoPlayer = false;
                            stack.shrink(1);
                            return InteractionResult.SUCCESS;
                        }
                    }
                    else
                    {
                        if(playerIn.addEffect(getEffect))
                        {
                            this.canAddtoPlayer = false;
                            stack.shrink(1);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {

        if(!p_41432_.isClientSide())
        {
            HitResult result = p_41433_.pick(5,0,false);
            BlockPos pos = new BlockPos(result.getLocation().x,result.getLocation().y,result.getLocation().z);
            /*
            *
            * Alright well im using the bool check for now, but if that ever fails, here is a head start on getting the entity the player is looking at.
            * ALSO a yarn version of what i want to do Provided by Fly#3448 in discord DM's
            * or in a link: https://github.com/Flytre/FlytreLib/blob/1.18-2/Common/src/main/java/net/flytre/flytre_lib/api/base/util/EntityUtils.java
            *
            List<LivingEntity> entityList = p_41433_.level.getNearbyEntities(LivingEntity.class,TargetingConditions.forCombat().range(5),(LivingEntity) p_41433_,new AABB(result.getLocation(),p_41433_.getLookAngle()));
            System.out.println(entityList);
            if(entityList.size()>0)
            {
                for(LivingEntity entity : entityList)
                {
                    if(p_41433_.hasLineOfSight(entity))
                    {
                        System.out.println(entity);
                    }
                }
            }*/


            if(!p_41433_.isCrouching())
            {
                ItemStack heldStack = p_41433_.getItemInHand(p_41434_);
                MobEffectInstance getEffect = getEffectFromScroll(heldStack);
                if(getEffect != null)
                {
                    if(p_41433_.addEffect(getEffect))
                    {
                        heldStack.shrink(1);
                    }
                }
            }
            /*
            if(result.getType().equals(HitResult.Type.MISS))
            {

            }
            else if(result.getType().equals(HitResult.Type.BLOCK))
            {

            }
            */
        }

        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        MobEffectInstance getEffect = getEffectFromScroll(p_41458_);
        if(getEffect != null)
        {
            return NameComponentUtils.createComponentName(getEffect.getEffect().getDisplayName(),new TranslatableComponent(MODID + ".effect_scroll.text").getString());
        }

        return super.getName(p_41458_);
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
            String time = "" + minutes + ":" + ((seconds<10)?("0"+seconds):(seconds)) + "";
            if(getEffect.getEffect().isInstantenous()) time = new TranslatableComponent(MODID + ".effect_scroll.instant").getString() + getEffect.getDuration() + new TranslatableComponent(MODID + ".effect_scroll.instant_time").getString();
            TooltipUtils.addTooltipMessageWithStyle(p_41423_,getEffect.getEffect().getDisplayName().getString() + " " + TooltipUtils.getRomanNumeral(getEffect.getAmplifier()) + " - " + time,format);
        }

        List<String> listy = Arrays.asList(MODID + ".effect_scroll.use",MODID + ".effect_scroll.use_on");
        List<ChatFormatting> chatty = Arrays.asList(ChatFormatting.GOLD,ChatFormatting.GREEN);
        TooltipUtils.addTooltipShiftMessageMultiWithStyle(p_41423_,listy,chatty);
    }
}
