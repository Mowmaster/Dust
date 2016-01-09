package com.mowmaster.dust.item;

import com.mowmaster.dust.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

/**
 * Created by KingMowmaster on 1/9/2016.
 */
public class ItemRenderRegistry {

    public static void registerItemRenders()
    {
        registerItem(ModItems.crystalred);
    }

    public static void registerItem(Item item)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("dust" + ":" + item.getUnlocalizedName().substring(5), "inventory"));
    }
}
