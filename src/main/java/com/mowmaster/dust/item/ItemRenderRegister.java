package com.mowmaster.dust.item;

import com.mowmaster.dust.init.ModItems;
import com.mowmaster.dust.reference.reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

/**
 * Created by KingMowmaster on 1/10/2016.
 */
public final class ItemRenderRegister {

    public static void registerItemRenderer()
    {
        reg(ModItems.ItemCrystalRed);
    }

    public static void reg(Item item){
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(reference.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
    }

}


