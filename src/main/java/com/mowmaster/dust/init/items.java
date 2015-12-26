package com.mowmaster.dust.init;

import com.mowmaster.dust.reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class items {
    public static Item crystal_red;

    public static void init()
    {
        crystal_red = new Item().setUnlocalizedName("crystal_red");
    }
    public static void register(){
        GameRegistry.registerItem(crystal_red, crystal_red.getUnlocalizedName());
    }
    public static void registerRenders(){
        registerRender(crystal_red);
    }
    public static void registerRender(Item item){
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(reference.MOD_ID + ":" + item.getUnlocalizedName(), "inventory"));
    }
}