package com.mowmaster.dust.Items.Tools;

import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class BaseCrystalToolSword extends SwordItem {

    public BaseCrystalToolSword(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
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
