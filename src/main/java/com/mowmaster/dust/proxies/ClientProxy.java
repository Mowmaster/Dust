package com.mowmaster.dust.proxies;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.buildingblocks.BlockDustBasicMeta;
import com.mowmaster.dust.blocks.buildingblocks.BlockLootBlock;
import com.mowmaster.dust.blocks.crystal.BlockCrystalOre;
import com.mowmaster.dust.blocks.treebits.BlockCharcoal;
import com.mowmaster.dust.blocks.treebits.BlockDustLeaf;
import com.mowmaster.dust.items.*;
import com.mowmaster.dust.particles.ParticleCreator;
import com.mowmaster.dust.particles.ParticleEvents;
import com.mowmaster.dust.particles.ParticleHandler;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import com.mowmaster.dust.tiles.TileCrystalCrusherBasic;
import com.mowmaster.dust.tiles.TileDustBlock;
import com.mowmaster.dust.tiles.TilePedestal;
import com.mowmaster.dust.tiles.render.RenderDustBlock;
import com.mowmaster.dust.tiles.render.RenderTileCrystalCluster;
import com.mowmaster.dust.tiles.render.RenderTileCrystalCrusher;
import com.mowmaster.dust.tiles.render.RenderTilePedestal;
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
        ClientRegistry.bindTileEntitySpecialRenderer(TileCrystalCrusherBasic.class,new RenderTileCrystalCrusher());


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
