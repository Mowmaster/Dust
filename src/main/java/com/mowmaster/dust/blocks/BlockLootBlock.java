package com.mowmaster.dust.blocks;

import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileLootBlock;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;


public class BlockLootBlock extends BlockBasic implements ITileEntityProvider
{
    public static Block lootblockaircommon;
    public static Block lootblockairuncommon;
    public static Block lootblockairrare;
    public static Block lootblockairlegendary;
    public static Block lootblockairexotic;
    public static Block lootblockcommon;
    public static Block lootblockuncommon;
    public static Block lootblockrare;
    public static Block lootblocklegendary;
    public static Block lootblockexotic;
    public static Block lootblockpassivespawner;
    public static Block lootblockhostilespawner;
    public static Block lootblockpillar;
    public static Block lootblockcrystalcluster;

    public BlockLootBlock(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(50);
        this.setResistance(50);
        this.setSoundType(SoundType.STONE);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileLootBlock();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileLootBlock();
    }

    public static void Init()
    {
        lootblockaircommon = new BlockLootBlock("lootblockaircommon","lootblockaircommon");
        lootblockairuncommon = new BlockLootBlock("lootblockairuncommon","lootblockairuncommon");
        lootblockairrare = new BlockLootBlock("lootblockairrare","lootblockairrare");
        lootblockairlegendary = new BlockLootBlock("lootblockairlegendary","lootblockairlegendary");
        lootblockairexotic = new BlockLootBlock("lootblockairexotic","lootblockairexotic");

        lootblockcommon = new BlockLootBlock("lootblockcommon","lootblockcommon");
        lootblockuncommon = new BlockLootBlock("lootblockuncommon","lootblockuncommon");
        lootblockrare = new BlockLootBlock("lootblockrare","lootblockrare");
        lootblocklegendary = new BlockLootBlock("lootblocklegendary","lootblocklegendary");
        lootblockexotic = new BlockLootBlock("lootblockexotic","lootblockexotic");
        lootblockpassivespawner = new BlockLootBlock("lootblockpassivespawner","lootblockpassivespawner");
        lootblockhostilespawner = new BlockLootBlock("lootblockhostilespawner","lootblockhostilespawner");
        lootblockpillar = new BlockLootBlock("lootblockpillar","lootblockpillar");
        lootblockcrystalcluster = new BlockLootBlock("lootblockcrystalcluster","lootblockcrystalcluster");
    }

    public static void Register()
    {
        registerBlock(lootblockaircommon);
        registerBlock(lootblockairuncommon);
        registerBlock(lootblockairrare);
        registerBlock(lootblockairlegendary);
        registerBlock(lootblockairexotic);
        registerBlock(lootblockcommon);
        registerBlock(lootblockuncommon);
        registerBlock(lootblockrare);
        registerBlock(lootblocklegendary);
        registerBlock(lootblockexotic);
        registerBlock(lootblockpassivespawner);
        registerBlock(lootblockhostilespawner);
        registerBlock(lootblockpillar);
        registerBlock(lootblockcrystalcluster);
    }

    public static void RegisterRender()
    {
        registerRender(lootblockaircommon);
        registerRender(lootblockairuncommon);
        registerRender(lootblockairrare);
        registerRender(lootblockairlegendary);
        registerRender(lootblockairexotic);
        registerRender(lootblockcommon);
        registerRender(lootblockuncommon);
        registerRender(lootblockrare);
        registerRender(lootblocklegendary);
        registerRender(lootblockexotic);
        registerRender(lootblockpassivespawner);
        registerRender(lootblockhostilespawner);
        registerRender(lootblockpillar);
        registerRender(lootblockcrystalcluster);
    }

}
