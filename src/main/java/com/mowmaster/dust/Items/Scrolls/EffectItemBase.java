package com.mowmaster.dust.Items.Scrolls;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EffectItemBase extends Item
{

    private MobEffectInstance storedPotionEffect;

    public EffectItemBase(Properties p_41383_)
    {
        super(p_41383_);
        this.storedPotionEffect = null;
    }

    public void setEffectToScroll(ItemStack stack)
    {
        CompoundTag tag = new CompoundTag();
        if(stack.hasTag())tag = stack.getTag();
        if(storedPotionEffect!=null)storedPotionEffect.save(tag);
        stack.setTag(tag);
    }

    public void setEffectToScroll(ItemStack stack, MobEffectInstance instance)
    {
        CompoundTag tag = new CompoundTag();
        if(stack.hasTag())tag = stack.getTag();
        if(instance!=null)instance.save(tag);
        stack.setTag(tag);
    }

    public static MobEffectInstance getEffectFromScroll(ItemStack stack)
    {
        if(stack.hasTag())
        {
            MobEffectInstance instance = null;
            CompoundTag tag = stack.getTag();
            return (MobEffectInstance.load(tag)!=null)?(MobEffectInstance.load(tag)):(null);
        }
        return null;
    }



}
