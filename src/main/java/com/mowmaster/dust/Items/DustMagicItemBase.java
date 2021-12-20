package com.mowmaster.dust.Items;

import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DustMagicItemBase extends Item {
    public DustMagicItemBase(Properties p_41383_) {
        super(p_41383_);
    }

    public static DustMagic getDustMagicFromStack(ItemStack stackIn)
    {
        return DustMagic.getDustMagicInItemStack(stackIn);
    }

    public static int getColor(ItemStack stackIn)
    {
        return getDustMagicFromStack(stackIn).getDustColor();
    }

}
