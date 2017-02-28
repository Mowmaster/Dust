package com.mowmaster.dust.items;

import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class ItemRegistry
{


    public static Item redcrystal;
    public static Item crystal;




    public static void init()
    {
        redcrystal = new ItemBasic("crystal_red","crystal_red");
        crystal = new ItemCrystal("crystal");

    }

    public static void register()
    {
        registerItem(redcrystal);
        registerItem(crystal);
    }

    public static void registerRenders()
    {

        registerRender(redcrystal);

       for(int i = 0; i < CrystalItems.CrystalTypes.values().length; i++)
       {
           registerRender(crystal,i,"crystal_" + CrystalItems.CrystalTypes.values()[i].getName());
       }
    }

    public static void registerItem(Item item)
    {
        GameRegistry.register(item);
    }

    public static void registerRender(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID, item.getUnlocalizedName().substring(5)), "inventory"));
    }

    public static void registerRender(Item item, int meta, String fileName)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Reference.MODID, fileName), "inventory"));
    }




}
