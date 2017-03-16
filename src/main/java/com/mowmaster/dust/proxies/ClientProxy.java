package com.mowmaster.dust.proxies;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ClientProxy extends CommonProxy
{
    @Override
    @SideOnly(Side.CLIENT)
    public void PreInit()
    {
        ItemRegistry.registerRenders();
        BlockRegistry.registerRenders();
    }

    @Override
    public void registerModelBakeryVarients()
    {
        ModelBakery.registerItemVariants(ItemRegistry.crystal,
                new ResourceLocation(Reference.MODID,"crystal_red"),
                new ResourceLocation(Reference.MODID,"crystal_blue"),
                new ResourceLocation(Reference.MODID,"crystal_yellow"),
                new ResourceLocation(Reference.MODID,"crystal_purple"),
                new ResourceLocation(Reference.MODID,"crystal_green"),
                new ResourceLocation(Reference.MODID,"crystal_orange"),
                new ResourceLocation(Reference.MODID,"crystal_white"),
                new ResourceLocation(Reference.MODID,"crystal_black"),
                new ResourceLocation(Reference.MODID,"crystal_clear")
        );

        ModelBakery.registerItemVariants(ItemRegistry.dust,
                new ResourceLocation(Reference.MODID,"dust_red"),
                new ResourceLocation(Reference.MODID,"dust_blue"),
                new ResourceLocation(Reference.MODID,"dust_yellow"),
                new ResourceLocation(Reference.MODID,"dust_purple"),
                new ResourceLocation(Reference.MODID,"dust_green"),
                new ResourceLocation(Reference.MODID,"dust_orange"),
                new ResourceLocation(Reference.MODID,"dust_white"),
                new ResourceLocation(Reference.MODID,"dust_black"),
                new ResourceLocation(Reference.MODID,"dust_clear")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockRegistry.orered),
                new ResourceLocation(Reference.MODID, "ore_red_ore"),
                new ResourceLocation(Reference.MODID, "ore_red_five"),
                new ResourceLocation(Reference.MODID, "ore_red_four"),
                new ResourceLocation(Reference.MODID, "ore_red_three"),
                new ResourceLocation(Reference.MODID, "ore_red_two"),
                new ResourceLocation(Reference.MODID, "ore_red_one"),
                new ResourceLocation(Reference.MODID, "ore_red_base")
        );



    }

}
