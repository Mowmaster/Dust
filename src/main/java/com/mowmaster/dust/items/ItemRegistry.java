package com.mowmaster.dust.items;

import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class ItemRegistry
{
    public static Item crystal_red;


    public static void RegisterItems()
    {
        GameRegistry.register(crystal_red = new ItemBasic("crystal_red").setRegistryName("crystal_red"));
    }
}
