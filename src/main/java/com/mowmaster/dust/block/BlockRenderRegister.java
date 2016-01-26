package com.mowmaster.dust.block;


import com.mowmaster.dust.init.ModBlocks;
import com.mowmaster.dust.reference.reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class BlockRenderRegister
{
    public static void registerBlockRenderer()
    {
        reg(ModBlocks.RedCrystalOre);
        reg(ModBlocks.BlueCrystalOre);
        reg(ModBlocks.YellowCrystalOre);
        reg(ModBlocks.GreenCrystalOre);
        reg(ModBlocks.OrangeCrystalOre);
        reg(ModBlocks.PurpleCrystalOre);
        reg(ModBlocks.BlackCrystalOre);
        reg(ModBlocks.WhiteCrystalOre);
    }

    public static void reg(Block block)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(reference.MOD_ID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
    }
}
