package com.mowmaster.dust.proxies;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.treebits.BlockDustLeaf;
import com.mowmaster.dust.items.ItemArmorAndToolsRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.particles.ParticleCreator;
import com.mowmaster.dust.particles.ParticleEvents;
import com.mowmaster.dust.particles.ParticleHandler;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import com.mowmaster.dust.tiles.TileCrystalCrusher;
import com.mowmaster.dust.tiles.TileDustBlock;
import com.mowmaster.dust.tiles.TilePedestal;
import com.mowmaster.dust.tiles.render.RenderDustBlock;
import com.mowmaster.dust.tiles.render.RenderTileCrystalCluster;
import com.mowmaster.dust.tiles.render.RenderTileCrystalCrusher;
import com.mowmaster.dust.tiles.render.RenderTilePedestal;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
        ClientRegistry.bindTileEntitySpecialRenderer(TileDustBlock.class,new RenderDustBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCrystalCrusher.class,new RenderTileCrystalCrusher());


        ParticleEvents parts = new ParticleEvents();
        MinecraftForge.EVENT_BUS.register(parts);
        FMLCommonHandler.instance().bus().register(parts);
    }

    @Override
    public void spawnMagicParticle(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, int color, float scale, int maxAge, float gravity, boolean collision, boolean fade) {
        ParticleHandler.spawnParticle(() -> new ParticleCreator(world, posX, posY, posZ, motionX, motionY, motionZ, color, scale, maxAge, gravity, collision, fade), posX, posY, posZ, 32);
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

        ModelBakery.registerItemVariants(ItemRegistry.wikiscroll,
                new ResourceLocation(Reference.MODID,"wikiscroll_one"),
                new ResourceLocation(Reference.MODID,"wikiscroll_two"),
                new ResourceLocation(Reference.MODID,"wikiscroll_three"),
                new ResourceLocation(Reference.MODID,"wikiscroll_four"),
                new ResourceLocation(Reference.MODID,"wikiscroll_five"),
                new ResourceLocation(Reference.MODID,"wikiscroll_six"),
                new ResourceLocation(Reference.MODID,"wikiscroll_seven"),
                new ResourceLocation(Reference.MODID,"wikiscroll_eight"),
                new ResourceLocation(Reference.MODID,"wikiscroll_nine"),
                new ResourceLocation(Reference.MODID,"wikiscroll_ten"),
                new ResourceLocation(Reference.MODID,"wikiscroll_eleven"),
                new ResourceLocation(Reference.MODID,"wikiscroll_twelve"),
                new ResourceLocation(Reference.MODID,"wikiscroll_thirteen"),
                new ResourceLocation(Reference.MODID,"wikiscroll_fourteen"),
                new ResourceLocation(Reference.MODID,"wikiscroll_fifteen"),
                new ResourceLocation(Reference.MODID,"wikiscroll_sixteen")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockDustLeaf.leaf),
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
