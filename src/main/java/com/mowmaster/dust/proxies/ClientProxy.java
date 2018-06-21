package com.mowmaster.dust.proxies;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.handlers.ClientEvents;
import com.mowmaster.dust.items.ItemArmorAndToolsRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import com.mowmaster.dust.tiles.TilePedestal;
import com.mowmaster.dust.tiles.render.RenderTileCrystalCluster;
import com.mowmaster.dust.tiles.render.RenderTilePedestal;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ClientProxy extends CommonProxy
{
    @Override
    @SideOnly(Side.CLIENT)
    public void PreInit()
    {
        ItemRegistry.registerRenders();
        ItemArmorAndToolsRegistry.registerRenders();
        BlockRegistry.registerRenders();

        ClientRegistry.bindTileEntitySpecialRenderer(TileCrystalCluster.class,new RenderTileCrystalCluster());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePedestal.class,new RenderTilePedestal());

        ClientEvents clientEvents= new ClientEvents();
        MinecraftForge.EVENT_BUS.register(clientEvents);
        FMLCommonHandler.instance().bus().register(clientEvents);
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
                new ResourceLocation(Reference.MODID,"dust_stone")
        );
        ModelBakery.registerItemVariants(ItemRegistry.bit,
                new ResourceLocation(Reference.MODID,"bit_red"),
                new ResourceLocation(Reference.MODID,"bit_blue"),
                new ResourceLocation(Reference.MODID,"bit_yellow"),
                new ResourceLocation(Reference.MODID,"bit_purple"),
                new ResourceLocation(Reference.MODID,"bit_green"),
                new ResourceLocation(Reference.MODID,"bit_orange"),
                new ResourceLocation(Reference.MODID,"bit_white"),
                new ResourceLocation(Reference.MODID,"bit_black")
        );

        ModelBakery.registerItemVariants(ItemRegistry.dustyCharcoal,
                new ResourceLocation(Reference.MODID,"charcoal_red"),
                new ResourceLocation(Reference.MODID,"charcoal_blue"),
                new ResourceLocation(Reference.MODID,"charcoal_yellow"),
                new ResourceLocation(Reference.MODID,"charcoal_purple"),
                new ResourceLocation(Reference.MODID,"charcoal_green"),
                new ResourceLocation(Reference.MODID,"charcoal_orange"),
                new ResourceLocation(Reference.MODID,"charcoal_white"),
                new ResourceLocation(Reference.MODID,"charcoal_black")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockRegistry.leaf),
                new ResourceLocation(Reference.MODID, "leaves_red"),
                new ResourceLocation(Reference.MODID, "leaves_blue"),
                new ResourceLocation(Reference.MODID, "leaves_yellow"),
                new ResourceLocation(Reference.MODID, "leaves_purple"),
                new ResourceLocation(Reference.MODID, "leaves_orange"),
                new ResourceLocation(Reference.MODID, "leaves_green"),
                new ResourceLocation(Reference.MODID, "leaves_white"),
                new ResourceLocation(Reference.MODID, "leaves_black")
        );



    }

}
