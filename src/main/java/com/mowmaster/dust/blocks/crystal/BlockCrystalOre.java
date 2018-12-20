package com.mowmaster.dust.blocks.crystal;

import com.mowmaster.dust.blocks.blockbasics.BlockBasic;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockCrystalOre extends BlockBasic
{
    public static Block redore;
    public static Block blueore;
    public static Block yellowore;
    public static Block purpleore;
    public static Block orangeore;
    public static Block greenore;
    public static Block whiteore;
    public static Block blackore;


    public BlockCrystalOre(String unloc, String registryName, Material material, SoundType soundType, int hardness, int resistance, int lightopacity)
    {
        super(material);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightOpacity(lightopacity);
        this.setSoundType(soundType);
        this.setCreativeTab(DUSTBLOCKSTABS);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        ItemStack returned = new ItemStack(redore,1);
        if(state.getBlock().equals(redore)){returned = new ItemStack(redore,1);}
        else if(state.getBlock().equals(blueore)){returned = new ItemStack(blueore,1);}
        else if(state.getBlock().equals(yellowore)){returned = new ItemStack(yellowore,1);}
        else if(state.getBlock().equals(purpleore)){returned = new ItemStack(purpleore,1);}
        else if(state.getBlock().equals(greenore)){returned = new ItemStack(greenore,1);}
        else if(state.getBlock().equals(orangeore)){returned = new ItemStack(orangeore,1);}
        else if(state.getBlock().equals(whiteore)){returned = new ItemStack(whiteore,1);}
        else if(state.getBlock().equals(blackore)){returned = new ItemStack(blackore,1);}

        return returned;
    }



    @Override
    public Item getItemDropped(IBlockState state,Random random,int fortune)
    {
        return ItemRegistry.crystal;
    }

    @Override
    public int damageDropped(IBlockState state) {
        int count = 0;
        if(this.getDefaultState().getBlock().equals(redore)) {count=0;}
        else if(this.getDefaultState().getBlock().equals(blueore)) {count=1;}
        else if(this.getDefaultState().getBlock().equals(yellowore)) {count=2;}
        else if(this.getDefaultState().getBlock().equals(purpleore)) {count=3;}
        else if(this.getDefaultState().getBlock().equals(greenore)) {count=4;}
        else if(this.getDefaultState().getBlock().equals(orangeore)) {count=5;}
        else if(this.getDefaultState().getBlock().equals(whiteore)) {count=6;}
        else if(this.getDefaultState().getBlock().equals(blackore)) {count=7;}
        return count;
    }

    @Override
    public int quantityDropped(Random random) {
        int count = 1+ random.nextInt(4);//guarenteed 1 but could be up to 5
        return count;
    }

    private Boolean oreToCrystal(World worldIn, BlockPos pos, Block crystalOreBlock, Block crystalState)
    {
        if(!worldIn.isRemote) {
            if (this.equals(crystalOreBlock)) {
                worldIn.setBlockState(pos, crystalState.getDefaultState());
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.dust, 1, 8)));
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote)
        {
            oreToCrystal(worldIn,pos,redore,BlockCrystal.redCrystalFive);
            oreToCrystal(worldIn,pos,blueore,BlockCrystal.blueCrystalFive);
            oreToCrystal(worldIn,pos,yellowore,BlockCrystal.yellowCrystalFive);
            oreToCrystal(worldIn,pos,purpleore,BlockCrystal.purpleCrystalFive);
            oreToCrystal(worldIn,pos,greenore,BlockCrystal.greenCrystalFive);
            oreToCrystal(worldIn,pos,orangeore,BlockCrystal.orangeCrystalFive);
            oreToCrystal(worldIn,pos,whiteore,BlockCrystal.whiteCrystalFive);
            oreToCrystal(worldIn,pos,blackore,BlockCrystal.blackCrystalFive);
        }
    }

    public static void Init()
    {
        redore = new BlockCrystalOre("redore", "red/redore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        blueore = new BlockCrystalOre("blueore", "blue/blueore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        yellowore = new BlockCrystalOre("yellowore", "yellow/yellowore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        purpleore = new BlockCrystalOre("purpleore", "purple/purpleore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        orangeore = new BlockCrystalOre("orangeore", "orange/orangeore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        greenore = new BlockCrystalOre("greenore", "green/greenore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        whiteore = new BlockCrystalOre("whiteore", "white/whiteore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        blackore = new BlockCrystalOre("blackore", "black/blackore", Material.ROCK, SoundType.STONE, 3, 15, 10);
    }

    public static void Register()
    {
        registerBlock(redore);
        registerBlock(blueore);
        registerBlock(yellowore);
        registerBlock(purpleore);
        registerBlock(orangeore);
        registerBlock(greenore);
        registerBlock(whiteore);
        registerBlock(blackore);
    }

    public static void RegisterRender()
    {
        registerRender(redore);
        registerRender(blueore);
        registerRender(yellowore);
        registerRender(purpleore);
        registerRender(orangeore);
        registerRender(greenore);
        registerRender(whiteore);
        registerRender(blackore);
    }


}
