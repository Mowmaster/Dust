package com.mowmaster.dust.items;

import com.mowmaster.dust.references.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import static com.mowmaster.dust.configtabs.DustyTab.DUSTTABS;

public class ItemBasic extends Item
{
    public ItemBasic(String unloc)
    {
        super();
        this.setUnlocalizedName(unloc);
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
    }
}
