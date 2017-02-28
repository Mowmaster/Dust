package com.mowmaster.dust.items;

import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

/**
 * Created by KingMowmaster on 2/25/2017.
 */
public class ItemRenderRegistry
{
    public static void ItemRR()
    {
        register(ItemRegistry.crystal_red);
    }
    public static void register(Item item)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Reference.MODID + ":" + item.getUnlocalizedName().substring(5),"inventory"));
    }
}
