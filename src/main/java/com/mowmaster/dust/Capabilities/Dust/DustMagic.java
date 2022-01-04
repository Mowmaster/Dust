package com.mowmaster.dust.Capabilities.Dust;

import com.mowmaster.dust.References.ColorReference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import static com.mowmaster.dust.References.Constants.MODID;

public class DustMagic {

    private int dustColor;
    private int dustAmount;

    public DustMagic(int color, int amount)
    {
        this.dustColor = color;
        this.dustAmount = amount;
    }

    public boolean isEmpty() { return this.getDustColor() == -1; }

    public int getDustColor() {
        return this.dustColor;
    }

    public int getDustAmount() {
        return dustAmount;
    }

    public void setDustColor(int dustColor) {
        this.dustColor = dustColor;
    }

    public void setDustAmount(int dustAmount) {
        this.dustAmount = dustAmount;
    }

    public void grow(int amount) { setDustAmount(this.dustAmount + amount); }

    public void shrink(int amount) {
        setDustAmount(this.dustAmount - amount);
    }

    public boolean isDustEqual(DustMagic magicIn)
    {
        return magicIn.getDustColor() == this.dustColor;
    }

    public boolean isDustEqualOrEmpty(DustMagic magicIn)
    {
        if(this.equals(new DustMagic(-1, 0)) || this.dustAmount <=0)return true;
        else if(this.dustAmount > 0)return magicIn.getDustColor() == this.dustColor;
        return false;
    }

    public static CompoundTag setDustMagicInTag(CompoundTag tag, DustMagic magicIn)
    {
        tag.putInt(MODID + "_dustMagicColor",magicIn.getDustColor());
        tag.putInt(MODID + "_dustMagicAmount",magicIn.getDustAmount());

        return tag;
    }

    public static ItemStack setDustMagicInStack(ItemStack stackToSet, DustMagic magicIn)
    {
        ItemStack copyStack = stackToSet.copy();
        CompoundTag copyStackTag = stackToSet.getOrCreateTag();
        copyStackTag.putInt(MODID + "_dustMagicColor",magicIn.getDustColor());
        copyStackTag.putInt(MODID + "_dustMagicAmount",magicIn.getDustAmount());
        copyStack.setTag(copyStackTag);
        return copyStack;
    }

    public static DustMagic getDustMagicInTag(CompoundTag tag)
    {
        DustMagic magic = new DustMagic(-1, 0);

        if(tag.contains(MODID + "_dustMagicColor"))
        {
            magic.setDustColor(tag.getInt(MODID + "_dustMagicColor"));
            if(tag.contains(MODID + "_dustMagicAmount"))
            {
                magic.setDustAmount(tag.getInt(MODID + "_dustMagicAmount"));
            }
        }

        return magic;
    }

    public static DustMagic getDustMagicInItemStack(ItemStack stack)
    {
        DustMagic magic = new DustMagic(-1, 0);
        if(stack.hasTag())
        {
            CompoundTag tag = stack.getTag();
            if(tag.contains(MODID + "_dustMagicColor"))
            {
                magic.setDustColor(tag.getInt(MODID + "_dustMagicColor"));
                if(tag.contains(MODID + "_dustMagicAmount"))
                {
                    magic.setDustAmount(tag.getInt(MODID + "_dustMagicAmount")*stack.getCount());
                }
            }
            else if(tag.contains(MODID+"_color"))
            {
                magic.setDustColor(ColorReference.getColorFromItemStackInt(stack));
                magic.setDustAmount(stack.getCount());
            }
        }
        return magic;
    }

}
