package com.mowmaster.dust.item;

import com.mowmaster.dust.init.ModItems;
import com.mowmaster.dust.reference.reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;



public final class ItemRenderRegister {

    public static void registerItemRenderer()
    {
        reg(ModItems.ItemCrystalRed);
        reg(ModItems.ItemCrystalBlue);
        reg(ModItems.ItemCrystalYellow);
        reg(ModItems.ItemCrystalGreen);
        reg(ModItems.ItemCrystalOrange);
        reg(ModItems.ItemCrystalPurple);
        reg(ModItems.ItemCrystalBlack);
        reg(ModItems.ItemCrystalWhite);

        reg(ModItems.crystalSwordRed);
        reg(ModItems.crystalSwordBlue);
        reg(ModItems.crystalSwordYellow);
        reg(ModItems.crystalSwordGreen);
        reg(ModItems.crystalSwordOrange);
        reg(ModItems.crystalSwordPurple);
        reg(ModItems.crystalSwordBlack);
        reg(ModItems.crystalSwordWhite);

    }

    public static void reg(Item item){
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(reference.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
    }

}


