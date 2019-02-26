package com.mowmaster.dust.blocks.utility;

import com.mowmaster.dust.blocks.blockbasics.BlockBasic;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileDustStructureSpawner;
import com.mowmaster.dust.tiles.TileLootBlock;
import com.mowmaster.dust.world.structures.DustStructureGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

import static com.mowmaster.dust.misc.DustConfigurationFile.devBlocks;
import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockStructureSpawner extends BlockBasic implements ITileEntityProvider
{

    public static Block structure1;
    public static Block structure2;
    public static Boolean inDev = devBlocks;


    public BlockStructureSpawner(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(9999);
        this.setResistance(9999);
        this.setLightOpacity(0);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(DUSTBLOCKSTABS);
    }


    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Random rand = new Random();
        if(!worldIn.isRemote)
        {

                if(playerIn.getHeldItemMainhand().getItem().equals(Items.NAME_TAG))
                {
                    worldIn.setBlockState(pos,BlockStructureSpawner.structure2.getDefaultState());
                    TileEntity tileentity = worldIn.getTileEntity(pos);
                    if (tileentity instanceof TileDustStructureSpawner) {
                        ((TileDustStructureSpawner) tileentity).setName(playerIn.getHeldItemMainhand().getDisplayName().toString());
                    }
                    return true;
                }
                else
                {
                    DustStructureGenerator genme = new DustStructureGenerator(playerIn.getHeldItemMainhand().getDisplayName().toString());
                    genme.generate(worldIn,rand,pos.add(0,-1,0));
                    return true;
                }
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileDustStructureSpawner();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileDustStructureSpawner();
    }

    public static void Init()
    {
        structure1 = new BlockStructureSpawner("structure1","structure1");
        structure2 = new BlockStructureSpawner("structure2","structure2");
    }

    public static void Register()
    {
        registerBlock(structure1);
        registerBlock(structure2);

    }

    public static void RegisterRender()
    {
        registerRender(structure1);
        registerRender(structure2);

    }


}
