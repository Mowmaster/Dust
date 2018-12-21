package com.mowmaster.dust.blocks.crystal;

import com.mowmaster.dust.blocks.blockbasics.BlockBasicDirectional;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.misc.DustConfigurationFile.funhaters;
import static com.mowmaster.dust.misc.DustConfigurationFile.oreRegrowthRate;
import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;
import static com.mowmaster.dust.world.biome.BiomeRegistry.*;


public class BlockCrystal extends BlockBasicDirectional
{

    public static Block redCrystalFive;
    public static Block redCrystalFour;
    public static Block redCrystalThree;
    public static Block redCrystalTwo;
    public static Block redCrystalOne;

    public static Block blueCrystalFive;
    public static Block blueCrystalFour;
    public static Block blueCrystalThree;
    public static Block blueCrystalTwo;
    public static Block blueCrystalOne;

    public static Block yellowCrystalFive;
    public static Block yellowCrystalFour;
    public static Block yellowCrystalThree;
    public static Block yellowCrystalTwo;
    public static Block yellowCrystalOne;

    public static Block purpleCrystalFive;
    public static Block purpleCrystalFour;
    public static Block purpleCrystalThree;
    public static Block purpleCrystalTwo;
    public static Block purpleCrystalOne;

    public static Block orangeCrystalFive;
    public static Block orangeCrystalFour;
    public static Block orangeCrystalThree;
    public static Block orangeCrystalTwo;
    public static Block orangeCrystalOne;

    public static Block greenCrystalFive;
    public static Block greenCrystalFour;
    public static Block greenCrystalThree;
    public static Block greenCrystalTwo;
    public static Block greenCrystalOne;

    public static Block whiteCrystalFive;
    public static Block whiteCrystalFour;
    public static Block whiteCrystalThree;
    public static Block whiteCrystalTwo;
    public static Block whiteCrystalOne;

    public static Block blackCrystalFive;
    public static Block blackCrystalFour;
    public static Block blackCrystalThree;
    public static Block blackCrystalTwo;
    public static Block blackCrystalOne;

    public static Boolean funHatersExplode = funhaters;
    //Covers all crystals
    //Using BlockEndRod as an "example"
    private static AxisAlignedBB CUP = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.9D, 0.75D);
    private static AxisAlignedBB CDOWN = new AxisAlignedBB(0.25D, 1.0D, 0.25D, 0.75D, 0.1D, 0.75D);

    private static AxisAlignedBB CNORTH = new AxisAlignedBB(0.25D, 0.75D, 0.1D, 0.75D, 0.25D, 1.0D);
    private static AxisAlignedBB CSOUTH = new AxisAlignedBB(0.25D, 0.75D, 0.0D, 0.75D, 0.25D, 0.9D);

    private static AxisAlignedBB CWEST = new AxisAlignedBB(0.1D, 0.75D, 0.25D, 1.0D, 0.25D, 0.75D);
    private static AxisAlignedBB CEAST = new AxisAlignedBB(0.0D, 0.75D, 0.25D, 0.9D, 0.25D, 0.75D);


    public BlockCrystal(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(3);
        this.setResistance(5);
        this.setLightLevel(2.0F);
        this.setSoundType(SoundType.GLASS);
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setCreativeTab(DUSTTABS);


    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return null;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,new IProperty[]{FACING});
    }

    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos.offset(facing.getOpposite()));

        if (iblockstate.getBlock() == this)
        {
            EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);

            if (enumfacing == facing)
            {
                return this.getDefaultState().withProperty(FACING, facing.getOpposite());
            }
        }

        return this.getDefaultState().withProperty(FACING, facing);
    }



    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }



    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState state = this.getDefaultState();
        state = state.withProperty(FACING, EnumFacing.getFront(meta));
        return state;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (((EnumFacing)state.getValue(FACING)))
        {
            case UP:
            default:
                return CUP;
            case DOWN:
                return CDOWN;
            case NORTH:
                return CNORTH;
            case EAST:
                return CEAST;
            case SOUTH:
                return CSOUTH;
            case WEST:
                return CWEST;
        }
    }
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }


    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);
        double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);


            //worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, d0 + (double)enumfacing.getFrontOffsetX() * d3, d1 + (double)enumfacing.getFrontOffsetY() * d3, d2 + (double)enumfacing.getFrontOffsetZ() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
            //Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleRosePetals(worldIn,d0 + (double)enumfacing.getFrontOffsetX() * d3, d1 + (double)enumfacing.getFrontOffsetY() * d3, d2 + (double)enumfacing.getFrontOffsetZ() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, 1,1,1));

    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {

        if (!worldIn.isRemote && entityIn instanceof EntityArrow)
        {
            EntityArrow entityarrow = (EntityArrow)entityIn;
            if (entityarrow.isBurning() )
            {
                destroyedByExplosion(worldIn,pos,redCrystalFive,blueCrystalFive,yellowCrystalFive,purpleCrystalFive,greenCrystalFive,orangeCrystalFive,whiteCrystalFive,blackCrystalFive,7.0F);
                destroyedByExplosion(worldIn,pos,redCrystalFour,blueCrystalFour,yellowCrystalFour,purpleCrystalFour,greenCrystalFour,orangeCrystalFour,whiteCrystalFour,blackCrystalFour,6.0F);
                destroyedByExplosion(worldIn,pos,redCrystalThree,blueCrystalThree,yellowCrystalThree,purpleCrystalThree,greenCrystalThree,orangeCrystalThree,whiteCrystalThree,blackCrystalThree,5.0F);
                destroyedByExplosion(worldIn,pos,redCrystalTwo,blueCrystalTwo,yellowCrystalTwo,purpleCrystalTwo,greenCrystalTwo,orangeCrystalTwo,whiteCrystalTwo,blackCrystalTwo,4.0F);
                destroyedByExplosion(worldIn,pos,redCrystalOne,blueCrystalOne,yellowCrystalOne,purpleCrystalOne,greenCrystalOne,orangeCrystalOne,whiteCrystalOne,blackCrystalOne,3.0F);
            }
        }

        if(!funHatersExplode)
        {
            if (!worldIn.isRemote && entityIn instanceof EntityAnimal || entityIn instanceof EntitySquid || entityIn instanceof EntityVillager || entityIn instanceof EntityBat || entityIn instanceof EntityPlayer)
            {
                if(!(entityIn instanceof EntityZombieHorse)) {
                    if (!(entityIn instanceof EntitySkeletonHorse)) {
                        destroyedByExplosion(worldIn,pos,redCrystalFive,blueCrystalFive,yellowCrystalFive,purpleCrystalFive,greenCrystalFive,orangeCrystalFive,whiteCrystalFive,blackCrystalFive,7.0F);
                        destroyedByExplosion(worldIn,pos,redCrystalFour,blueCrystalFour,yellowCrystalFour,purpleCrystalFour,greenCrystalFour,orangeCrystalFour,whiteCrystalFour,blackCrystalFour,6.0F);
                        destroyedByExplosion(worldIn,pos,redCrystalThree,blueCrystalThree,yellowCrystalThree,purpleCrystalThree,greenCrystalThree,orangeCrystalThree,whiteCrystalThree,blackCrystalThree,5.0F);
                        destroyedByExplosion(worldIn,pos,redCrystalTwo,blueCrystalTwo,yellowCrystalTwo,purpleCrystalTwo,greenCrystalTwo,orangeCrystalTwo,whiteCrystalTwo,blackCrystalTwo,4.0F);
                        destroyedByExplosion(worldIn,pos,redCrystalOne,blueCrystalOne,yellowCrystalOne,purpleCrystalOne,greenCrystalOne,orangeCrystalOne,whiteCrystalOne,blackCrystalOne,3.0F);
                    }
                }
            }
        }

    }

    private boolean destroyedByExplosion(World worldIn, BlockPos pos,Block redBlock,Block blueBlock,Block yellowBlock,Block purpleBlock,Block greenBlock,Block orangeBlock,Block whiteBlock,Block blackBlock,Float blast )
    {
        if(this.equals(redBlock) || this.equals(blueBlock) || this.equals(yellowBlock) || this.equals(purpleBlock) || this.equals(greenBlock) || this.equals(orangeBlock) || this.equals(whiteBlock) || this.equals(blackBlock))
        {
            worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,blast, true);
            return true;
        }
        return false;
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote)
        {
            destroyedByExplosion(worldIn,pos,redCrystalFive,blueCrystalFive,yellowCrystalFive,purpleCrystalFive,greenCrystalFive,orangeCrystalFive,whiteCrystalFive,blackCrystalFive,7.0F);
            destroyedByExplosion(worldIn,pos,redCrystalFour,blueCrystalFour,yellowCrystalFour,purpleCrystalFour,greenCrystalFour,orangeCrystalFour,whiteCrystalFour,blackCrystalFour,6.0F);
            destroyedByExplosion(worldIn,pos,redCrystalThree,blueCrystalThree,yellowCrystalThree,purpleCrystalThree,greenCrystalThree,orangeCrystalThree,whiteCrystalThree,blackCrystalThree,5.0F);
            destroyedByExplosion(worldIn,pos,redCrystalTwo,blueCrystalTwo,yellowCrystalTwo,purpleCrystalTwo,greenCrystalTwo,orangeCrystalTwo,whiteCrystalTwo,blackCrystalTwo,4.0F);
            destroyedByExplosion(worldIn,pos,redCrystalOne,blueCrystalOne,yellowCrystalOne,purpleCrystalOne,greenCrystalOne,orangeCrystalOne,whiteCrystalOne,blackCrystalOne,3.0F);
        }
    }

    private Boolean crystalMined(World worldIn,BlockPos pos,IBlockState state, Block crystalLarge,Block crystalSmall, int meta)
    {
        EnumFacing facing = state.getValue(FACING);
        if(this.equals(crystalLarge))
        {
            worldIn.setBlockState(pos, crystalSmall.getDefaultState().withProperty(BlockCrystal.FACING, facing));
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, meta)));
            return true;
        }
        return false;
    }

    private Boolean crystalDepleated(World worldIn,BlockPos pos,IBlockState state, Block crystalLarge, int meta)
    {
        EnumFacing facing = state.getValue(FACING);
        if(this.equals(crystalLarge))
        {
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, meta)));
            return true;
        }
        return false;
    }


    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        if (!worldIn.isRemote) {
            crystalMined(worldIn,pos,state,redCrystalFive,redCrystalFour,0);
            crystalMined(worldIn,pos,state,redCrystalFour,redCrystalThree,0);
            crystalMined(worldIn,pos,state,redCrystalThree,redCrystalTwo,0);
            crystalMined(worldIn,pos,state,redCrystalTwo,redCrystalOne,0);
            crystalDepleated(worldIn,pos,state,redCrystalOne,0);
            crystalMined(worldIn,pos,state,blueCrystalFive,blueCrystalFour,1);
            crystalMined(worldIn,pos,state,blueCrystalFour,blueCrystalThree,1);
            crystalMined(worldIn,pos,state,blueCrystalThree,blueCrystalTwo,1);
            crystalMined(worldIn,pos,state,blueCrystalTwo,blueCrystalOne,1);
            crystalDepleated(worldIn,pos,state,blueCrystalOne,1);
            crystalMined(worldIn,pos,state,yellowCrystalFive,yellowCrystalFour,2);
            crystalMined(worldIn,pos,state,yellowCrystalFour,yellowCrystalThree,2);
            crystalMined(worldIn,pos,state,yellowCrystalThree,yellowCrystalTwo,2);
            crystalMined(worldIn,pos,state,yellowCrystalTwo,yellowCrystalOne,2);
            crystalDepleated(worldIn,pos,state,yellowCrystalOne,2);
            crystalMined(worldIn,pos,state,purpleCrystalFive,purpleCrystalFour,3);
            crystalMined(worldIn,pos,state,purpleCrystalFour,purpleCrystalThree,3);
            crystalMined(worldIn,pos,state,purpleCrystalThree,purpleCrystalTwo,3);
            crystalMined(worldIn,pos,state,purpleCrystalTwo,purpleCrystalOne,3);
            crystalDepleated(worldIn,pos,state,purpleCrystalOne,3);
            crystalMined(worldIn,pos,state,greenCrystalFive,greenCrystalFour,4);
            crystalMined(worldIn,pos,state,greenCrystalFour,greenCrystalThree,4);
            crystalMined(worldIn,pos,state,greenCrystalThree,greenCrystalTwo,4);
            crystalMined(worldIn,pos,state,greenCrystalTwo,greenCrystalOne,4);
            crystalDepleated(worldIn,pos,state,greenCrystalOne,4);
            crystalMined(worldIn,pos,state,orangeCrystalFive,orangeCrystalFour,5);
            crystalMined(worldIn,pos,state,orangeCrystalFour,orangeCrystalThree,5);
            crystalMined(worldIn,pos,state,orangeCrystalThree,orangeCrystalTwo,5);
            crystalMined(worldIn,pos,state,orangeCrystalTwo,orangeCrystalOne,5);
            crystalDepleated(worldIn,pos,state,orangeCrystalOne,5);
            crystalMined(worldIn,pos,state,whiteCrystalFive,whiteCrystalFour,6);
            crystalMined(worldIn,pos,state,whiteCrystalFour,whiteCrystalThree,6);
            crystalMined(worldIn,pos,state,whiteCrystalThree,whiteCrystalTwo,6);
            crystalMined(worldIn,pos,state,whiteCrystalTwo,whiteCrystalOne,6);
            crystalDepleated(worldIn,pos,state,whiteCrystalOne,6);
            crystalMined(worldIn,pos,state,blackCrystalFive,blackCrystalFour,7);
            crystalMined(worldIn,pos,state,blackCrystalFour,blackCrystalThree,7);
            crystalMined(worldIn,pos,state,blackCrystalThree,blackCrystalTwo,7);
            crystalMined(worldIn,pos,state,blackCrystalTwo,blackCrystalOne,7);
            crystalDepleated(worldIn,pos,state,blackCrystalOne,7);
        }
    }


    private boolean regrowCrystal(World worldIn, BlockPos pos, EnumFacing facing, Block smallCrystal, Block largeCrystal)
    {
        if (this.equals(smallCrystal)) {
            worldIn.setBlockState(pos, largeCrystal.getDefaultState().withProperty(BlockCrystal.FACING, facing));
            return true;
        }
        return false;
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        super.randomTick(worldIn, pos, state, random);
        final int CHANCE = oreRegrowthRate;

        int rand = random.nextInt(1000);

        //if(worldIn.getBiome(pos).equals(crystal_hot) || worldIn.getBiome(pos).equals(crystal_warm) || worldIn.getBiome(pos).equals(crystal_cold) || worldIn.getBiome(pos).equals(crystal_crystal)) {
        if(worldIn.getBiome(pos).equals(crystal_crystal)) {
            EnumFacing facing = state.getValue(FACING);
            if (rand < CHANCE)
            {
                regrowCrystal(worldIn,pos,facing,redCrystalOne,redCrystalTwo);
                regrowCrystal(worldIn,pos,facing,redCrystalTwo,redCrystalThree);
                regrowCrystal(worldIn,pos,facing,redCrystalThree,redCrystalFour);
                regrowCrystal(worldIn,pos,facing,redCrystalFour,redCrystalFive);
                //regrowCrystal(worldIn,pos,facing,redCrystalFive,redOre);
                regrowCrystal(worldIn,pos,facing,blueCrystalOne,blueCrystalTwo);
                regrowCrystal(worldIn,pos,facing,blueCrystalTwo,blueCrystalThree);
                regrowCrystal(worldIn,pos,facing,blueCrystalThree,blueCrystalFour);
                regrowCrystal(worldIn,pos,facing,blueCrystalFour,blueCrystalFive);
                //regrowCrystal(worldIn,pos,facing,blueCrystalFive,blueOre);
                regrowCrystal(worldIn,pos,facing,yellowCrystalOne,yellowCrystalTwo);
                regrowCrystal(worldIn,pos,facing,yellowCrystalTwo,yellowCrystalThree);
                regrowCrystal(worldIn,pos,facing,yellowCrystalThree,yellowCrystalFour);
                regrowCrystal(worldIn,pos,facing,yellowCrystalFour,yellowCrystalFive);
                //regrowCrystal(worldIn,pos,facing,yellowCrystalFive,yellowOre);
                regrowCrystal(worldIn,pos,facing,purpleCrystalOne,purpleCrystalTwo);
                regrowCrystal(worldIn,pos,facing,purpleCrystalTwo,purpleCrystalThree);
                regrowCrystal(worldIn,pos,facing,purpleCrystalThree,purpleCrystalFour);
                regrowCrystal(worldIn,pos,facing,purpleCrystalFour,purpleCrystalFive);
                //regrowCrystal(worldIn,pos,facing,purpleCrystalFive,purpleOre);
                regrowCrystal(worldIn,pos,facing,greenCrystalOne,greenCrystalTwo);
                regrowCrystal(worldIn,pos,facing,greenCrystalTwo,greenCrystalThree);
                regrowCrystal(worldIn,pos,facing,greenCrystalThree,greenCrystalFour);
                regrowCrystal(worldIn,pos,facing,greenCrystalFour,greenCrystalFive);
                //regrowCrystal(worldIn,pos,facing,greenCrystalFive,greenOre);
                regrowCrystal(worldIn,pos,facing,orangeCrystalOne,orangeCrystalTwo);
                regrowCrystal(worldIn,pos,facing,orangeCrystalTwo,orangeCrystalThree);
                regrowCrystal(worldIn,pos,facing,orangeCrystalThree,orangeCrystalFour);
                regrowCrystal(worldIn,pos,facing,orangeCrystalFour,orangeCrystalFive);
                //regrowCrystal(worldIn,pos,facing,orangeCrystalFive,orangeOre);
                regrowCrystal(worldIn,pos,facing,whiteCrystalOne,whiteCrystalTwo);
                regrowCrystal(worldIn,pos,facing,whiteCrystalTwo,whiteCrystalThree);
                regrowCrystal(worldIn,pos,facing,whiteCrystalThree,whiteCrystalFour);
                regrowCrystal(worldIn,pos,facing,whiteCrystalFour,whiteCrystalFive);
                //regrowCrystal(worldIn,pos,facing,whiteCrystalFive,whiteOre);
                regrowCrystal(worldIn,pos,facing,blackCrystalOne,blackCrystalTwo);
                regrowCrystal(worldIn,pos,facing,blackCrystalTwo,blackCrystalThree);
                regrowCrystal(worldIn,pos,facing,blackCrystalThree,blackCrystalFour);
                regrowCrystal(worldIn,pos,facing,blackCrystalFour,blackCrystalFive);
                //regrowCrystal(worldIn,pos,facing,blackCrystalFive,blackOre);
            }
        }
    }

    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        if (this.getItemDropped(state, RANDOM, fortune) != Item.getItemFromBlock(this))
        {
            return 1 + RANDOM.nextInt(5);
        }
        return 0;
    }
/*
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        if (!playerIn.hasAchievement(AchievementHandler.achievementCrystal))
        {
            playerIn.addStat(AchievementHandler.achievementCrystal);
        }
    }
*/
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("Will Explode if Living Passive Entities Collide");
    }


    public static void Init()
    {
        redCrystalFive = new BlockCrystal("redcrystalfive", "red/redcrystalfive");
        redCrystalFour = new BlockCrystal("redcrystalfour", "red/redcrystalfour");
        redCrystalThree = new BlockCrystal("redcrystalthree", "red/redcrystalthree");
        redCrystalTwo = new BlockCrystal("redcrystaltwo", "red/redcrystaltwo");
        redCrystalOne = new BlockCrystal("redcrystalone", "red/redcrystalone");

        blueCrystalFive = new BlockCrystal("bluecrystalfive", "blue/bluecrystalfive");
        blueCrystalFour = new BlockCrystal("bluecrystalfour", "blue/bluecrystalfour");
        blueCrystalThree = new BlockCrystal("bluecrystalthree", "blue/bluecrystalthree");
        blueCrystalTwo = new BlockCrystal("bluecrystaltwo", "blue/bluecrystaltwo");
        blueCrystalOne = new BlockCrystal("bluecrystalone", "blue/bluecrystalone");

        yellowCrystalFive = new BlockCrystal("yellowcrystalfive", "yellow/yellowcrystalfive");
        yellowCrystalFour = new BlockCrystal("yellowcrystalfour", "yellow/yellowcrystalfour");
        yellowCrystalThree = new BlockCrystal("yellowcrystalthree", "yellow/yellowcrystalthree");
        yellowCrystalTwo = new BlockCrystal("yellowcrystaltwo", "yellow/yellowcrystaltwo");
        yellowCrystalOne = new BlockCrystal("yellowcrystalone", "yellow/yellowcrystalone");

        purpleCrystalFive = new BlockCrystal("purplecrystalfive", "purple/purplecrystalfive");
        purpleCrystalFour = new BlockCrystal("purplecrystalfour", "purple/purplecrystalfour");
        purpleCrystalThree = new BlockCrystal("purplecrystalthree", "purple/purplecrystalthree");
        purpleCrystalTwo = new BlockCrystal("purplecrystaltwo", "purple/purplecrystaltwo");
        purpleCrystalOne = new BlockCrystal("purplecrystalone", "purple/purplecrystalone");

        orangeCrystalFive = new BlockCrystal("orangecrystalfive", "orange/orangecrystalfive");
        orangeCrystalFour = new BlockCrystal("orangecrystalfour", "orange/orangecrystalfour");
        orangeCrystalThree = new BlockCrystal("orangecrystalthree", "orange/orangecrystalthree");
        orangeCrystalTwo = new BlockCrystal("orangecrystaltwo", "orange/orangecrystaltwo");
        orangeCrystalOne = new BlockCrystal("orangecrystalone", "orange/orangecrystalone");

        greenCrystalFive = new BlockCrystal("greencrystalfive", "green/greencrystalfive");
        greenCrystalFour = new BlockCrystal("greencrystalfour", "green/greencrystalfour");
        greenCrystalThree = new BlockCrystal("greencrystalthree", "green/greencrystalthree");
        greenCrystalTwo = new BlockCrystal("greencrystaltwo", "green/greencrystaltwo");
        greenCrystalOne = new BlockCrystal("greencrystalone", "green/greencrystalone");

        whiteCrystalFive = new BlockCrystal("whitecrystalfive", "white/whitecrystalfive");
        whiteCrystalFour = new BlockCrystal("whitecrystalfour", "white/whitecrystalfour");
        whiteCrystalThree = new BlockCrystal("whitecrystalthree", "white/whitecrystalthree");
        whiteCrystalTwo = new BlockCrystal("whitecrystaltwo", "white/whitecrystaltwo");
        whiteCrystalOne = new BlockCrystal("whitecrystalone", "white/WhiteCrystalOne");

        blackCrystalFive = new BlockCrystal("blackcrystalfive", "black/blackcrystalfive");
        blackCrystalFour = new BlockCrystal("blackcrystalfour", "black/blackcrystalfour");
        blackCrystalThree = new BlockCrystal("blackcrystalthree", "black/blackcrystalthree");
        blackCrystalTwo = new BlockCrystal("blackcrystaltwo", "black/blackcrystaltwo");
        blackCrystalOne = new BlockCrystal("blackcrystalone", "black/blackcrystalone");
    }

    public static void Register()
    {
        registerBlock(redCrystalFive);
        registerBlock(redCrystalFour);
        registerBlock(redCrystalThree);
        registerBlock(redCrystalTwo);
        registerBlock(redCrystalOne);

        registerBlock(blueCrystalFive);
        registerBlock(blueCrystalFour);
        registerBlock(blueCrystalThree);
        registerBlock(blueCrystalTwo);
        registerBlock(blueCrystalOne);

        registerBlock(yellowCrystalFive);
        registerBlock(yellowCrystalFour);
        registerBlock(yellowCrystalThree);
        registerBlock(yellowCrystalTwo);
        registerBlock(yellowCrystalOne);

        registerBlock(purpleCrystalFive);
        registerBlock(purpleCrystalFour);
        registerBlock(purpleCrystalThree);
        registerBlock(purpleCrystalTwo);
        registerBlock(purpleCrystalOne);

        registerBlock(orangeCrystalFive);
        registerBlock(orangeCrystalFour);
        registerBlock(orangeCrystalThree);
        registerBlock(orangeCrystalTwo);
        registerBlock(orangeCrystalOne);

        registerBlock(greenCrystalFive);
        registerBlock(greenCrystalFour);
        registerBlock(greenCrystalThree);
        registerBlock(greenCrystalTwo);
        registerBlock(greenCrystalOne);

        registerBlock(whiteCrystalFive);
        registerBlock(whiteCrystalFour);
        registerBlock(whiteCrystalThree);
        registerBlock(whiteCrystalTwo);
        registerBlock(whiteCrystalOne);

        registerBlock(blackCrystalFive);
        registerBlock(blackCrystalFour);
        registerBlock(blackCrystalThree);
        registerBlock(blackCrystalTwo);
        registerBlock(blackCrystalOne);
    }

    public static void RegisterRender()
    {
        registerRenderCrystal(redCrystalFive);
        registerRenderCrystal(redCrystalFour);
        registerRenderCrystal(redCrystalThree);
        registerRenderCrystal(redCrystalTwo);
        registerRenderCrystal(redCrystalOne);

        registerRenderCrystal(blueCrystalFive);
        registerRenderCrystal(blueCrystalFour);
        registerRenderCrystal(blueCrystalThree);
        registerRenderCrystal(blueCrystalTwo);
        registerRenderCrystal(blueCrystalOne);

        registerRenderCrystal(yellowCrystalFive);
        registerRenderCrystal(yellowCrystalFour);
        registerRenderCrystal(yellowCrystalThree);
        registerRenderCrystal(yellowCrystalTwo);
        registerRenderCrystal(yellowCrystalOne);

        registerRenderCrystal(purpleCrystalFive);
        registerRenderCrystal(purpleCrystalFour);
        registerRenderCrystal(purpleCrystalThree);
        registerRenderCrystal(purpleCrystalTwo);
        registerRenderCrystal(purpleCrystalOne);

        registerRenderCrystal(orangeCrystalFive);
        registerRenderCrystal(orangeCrystalFour);
        registerRenderCrystal(orangeCrystalThree);
        registerRenderCrystal(orangeCrystalTwo);
        registerRenderCrystal(orangeCrystalOne);

        registerRenderCrystal(greenCrystalFive);
        registerRenderCrystal(greenCrystalFour);
        registerRenderCrystal(greenCrystalThree);
        registerRenderCrystal(greenCrystalTwo);
        registerRenderCrystal(greenCrystalOne);

        registerRenderCrystal(whiteCrystalFive);
        registerRenderCrystal(whiteCrystalFour);
        registerRenderCrystal(whiteCrystalThree);
        registerRenderCrystal(whiteCrystalTwo);
        registerRenderCrystal(whiteCrystalOne);

        registerRenderCrystal(blackCrystalFive);
        registerRenderCrystal(blackCrystalFour);
        registerRenderCrystal(blackCrystalThree);
        registerRenderCrystal(blackCrystalTwo);
        registerRenderCrystal(blackCrystalOne);
    }

}