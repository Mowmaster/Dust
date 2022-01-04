package com.mowmaster.dust.Items.Scrolls;

import com.mowmaster.dust.Items.DustMagicItemBase;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.Util.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.References.Constants.MODID;

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
        normal:RED -
        1:ORANGE -
        2:YELLOW -
        3:GREEN -
        4:BLUE -
        5:PURPLE -
        6+:PINK -
         */
        switch(getEffectFromScroll(stack).getAmplifier())
        {
            case 0: return 1;
            case 1: return 1;
            case 2: return 1;
            case 3: return 1;
            case 4: return 1;
            case 5: return 1;
            default: return 1;
        }
    }

    public static int getColorRenderCoin(ItemStack stack)
    {
        MobEffectCategory category = getEffectFromScroll(stack).getEffect().getCategory();
        if(category == MobEffectCategory.BENEFICIAL) {return 65280;}//Green
        else if(category == MobEffectCategory.NEUTRAL) {return 255;}//Blue
        else if(category == MobEffectCategory.HARMFUL) {return 16711680;}//Red
        return ColorReference.getColorFromItemStackInt(stack);
    }

    public int getRenderType(ItemStack stack)
    {
        if(getEffectFromScroll(stack) != null)
        {
            int girth = getEffectFromScroll(stack).getDuration();
            if(girth>0 && girth<=450)return 0;
            else if(girth>450 && girth<=1800)return 1;
            else if(girth>1800 && girth<=3600)return 2;
            else if(girth>3600 && girth<=9600)return 3;
        }
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);


    }
}
