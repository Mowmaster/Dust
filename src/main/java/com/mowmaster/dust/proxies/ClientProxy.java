package com.mowmaster.dust.proxies;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.buildingblocks.BlockDustBasicMeta;
import com.mowmaster.dust.blocks.buildingblocks.BlockLootBlock;
import com.mowmaster.dust.blocks.crystal.BlockCrystalOre;
import com.mowmaster.dust.blocks.treebits.BlockCharcoal;
import com.mowmaster.dust.blocks.treebits.BlockDustLeaf;
import com.mowmaster.dust.entities.EntityDustRegistry;
import com.mowmaster.dust.entities.EntityDustTippedArrow;
import com.mowmaster.dust.handlers.EntityRenderer;
import com.mowmaster.dust.items.*;
import com.mowmaster.dust.items.tools.IItemColorArrow;
import com.mowmaster.dust.particles.ParticleCreator;
import com.mowmaster.dust.particles.ParticleEvents;
import com.mowmaster.dust.particles.ParticleHandler;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.*;
import com.mowmaster.dust.tiles.render.*;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;


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
        ClientRegistry.bindTileEntitySpecialRenderer(TileCrystalCrusherBasic.class,new RenderTileCrystalCrusher());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCrystalFurnaceBasic.class,new RenderTileCrystalFurnaceBasic());

        RenderingRegistry.registerEntityRenderingHandler(EntityDustTippedArrow.class, renderManager -> new RenderArrow<EntityDustTippedArrow>(renderManager) {
            @Nullable
            @Override
            protected ResourceLocation getEntityTexture(EntityDustTippedArrow entity) {
                return new ResourceLocation(Reference.MODID, "textures/entities/arrow.png");
            }
        });

        ParticleEvents parts = new ParticleEvents();
        MinecraftForge.EVENT_BUS.register(parts);
        FMLCommonHandler.instance().bus().register(parts);

        /*EntityRenderer entityRenderer = new EntityRenderer();
        MinecraftForge.EVENT_BUS.register(entityRenderer);
        FMLCommonHandler.instance().bus().register(entityRenderer);*/
    }

    @Override
    public void spawnMagicParticle(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, int color, float scale, int maxAge, float gravity, boolean collision, boolean fade) {
        ParticleHandler.spawnParticle(() -> new ParticleCreator(world, posX, posY, posZ, motionX, motionY, motionZ, color, scale, maxAge, gravity, collision, fade), posX, posY, posZ, 32);
    }

    @Override
    public void registerModelBakeryVarients()
    {
        ItemCrystal.bakeItem();
        ItemDust.bakeItem();
        ItemBit.bakeItem();
        ItemWikiScroll.bakeItem();

        BlockDustLeaf.bakeBlock();
        BlockCharcoal.bakeBlock();
        BlockDustBasicMeta.bakeBlock();
        BlockLootBlock.bakeBlock();
        BlockCrystalOre.bakeBlock();
    }

}
