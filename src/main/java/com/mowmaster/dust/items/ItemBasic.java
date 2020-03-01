package com.mowmaster.dust.items;

import com.mowmaster.dust.references.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.*;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ItemBasic extends Item
{



    public ItemBasic(String unlocName, String registryName, int stackSize )
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = stackSize;
        this.setCreativeTab(DUSTTABS);
    }

    public ItemBasic(String unlocName,String registryName )
    {
        this( unlocName ,registryName ,1);
        // Calls the previous constructer and passes the needed stacksize paramater to the above method

    }

}
