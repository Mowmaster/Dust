package com.mowmaster.dust.Items.Tools;

import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class BaseCrystalToolHoe extends HoeItem {
    public BaseCrystalToolHoe(Tier p_42961_, int p_42962_, float p_42963_, Properties p_42964_) {
        super(p_42961_, p_42962_, p_42963_, p_42964_);
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
