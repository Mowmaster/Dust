package com.mowmaster.dust.blocks;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.misc.AchievementHandler;
import com.mowmaster.dust.particles.ParticleRosePetals;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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


public class BlockCrystal extends BlockDirectional
{

    public static Boolean canPlayerExplode = funhaters;
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


            worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, d0 + (double)enumfacing.getFrontOffsetX() * d3, d1 + (double)enumfacing.getFrontOffsetY() * d3, d2 + (double)enumfacing.getFrontOffsetZ() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
            //Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleRosePetals(worldIn,d0 + (double)enumfacing.getFrontOffsetX() * d3, d1 + (double)enumfacing.getFrontOffsetY() * d3, d2 + (double)enumfacing.getFrontOffsetZ() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, 1,1,1));

    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {

        if (!worldIn.isRemote && entityIn instanceof EntityArrow)
        {
            EntityArrow entityarrow = (EntityArrow)entityIn;
            if (entityarrow.isBurning() )
            {
                destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalFive,BlockRegistry.blueCrystalFive,BlockRegistry.yellowCrystalFive,BlockRegistry.purpleCrystalFive,BlockRegistry.greenCrystalFive,BlockRegistry.orangeCrystalFive,BlockRegistry.whiteCrystalFive,BlockRegistry.blackCrystalFive,7.0F);
                destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalFour,BlockRegistry.blueCrystalFour,BlockRegistry.yellowCrystalFour,BlockRegistry.purpleCrystalFour,BlockRegistry.greenCrystalFour,BlockRegistry.orangeCrystalFour,BlockRegistry.whiteCrystalFour,BlockRegistry.blackCrystalFour,6.0F);
                destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalThree,BlockRegistry.blueCrystalThree,BlockRegistry.yellowCrystalThree,BlockRegistry.purpleCrystalThree,BlockRegistry.greenCrystalThree,BlockRegistry.orangeCrystalThree,BlockRegistry.whiteCrystalThree,BlockRegistry.blackCrystalThree,5.0F);
                destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalTwo,BlockRegistry.blueCrystalTwo,BlockRegistry.yellowCrystalTwo,BlockRegistry.purpleCrystalTwo,BlockRegistry.greenCrystalTwo,BlockRegistry.orangeCrystalTwo,BlockRegistry.whiteCrystalTwo,BlockRegistry.blackCrystalTwo,4.0F);
                destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalOne,BlockRegistry.blueCrystalOne,BlockRegistry.yellowCrystalOne,BlockRegistry.purpleCrystalOne,BlockRegistry.greenCrystalOne,BlockRegistry.orangeCrystalOne,BlockRegistry.whiteCrystalOne,BlockRegistry.blackCrystalOne,3.0F);
            }
        }

        if(canPlayerExplode == false)
        {
            if (!worldIn.isRemote && entityIn instanceof EntityAnimal || entityIn instanceof EntitySquid || entityIn instanceof EntityVillager || entityIn instanceof EntityBat || entityIn instanceof EntityPlayer)
            {
                if(!(entityIn instanceof EntityZombieHorse)) {
                    if (!(entityIn instanceof EntitySkeletonHorse)) {
                        destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalFive,BlockRegistry.blueCrystalFive,BlockRegistry.yellowCrystalFive,BlockRegistry.purpleCrystalFive,BlockRegistry.greenCrystalFive,BlockRegistry.orangeCrystalFive,BlockRegistry.whiteCrystalFive,BlockRegistry.blackCrystalFive,7.0F);
                        destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalFour,BlockRegistry.blueCrystalFour,BlockRegistry.yellowCrystalFour,BlockRegistry.purpleCrystalFour,BlockRegistry.greenCrystalFour,BlockRegistry.orangeCrystalFour,BlockRegistry.whiteCrystalFour,BlockRegistry.blackCrystalFour,6.0F);
                        destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalThree,BlockRegistry.blueCrystalThree,BlockRegistry.yellowCrystalThree,BlockRegistry.purpleCrystalThree,BlockRegistry.greenCrystalThree,BlockRegistry.orangeCrystalThree,BlockRegistry.whiteCrystalThree,BlockRegistry.blackCrystalThree,5.0F);
                        destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalTwo,BlockRegistry.blueCrystalTwo,BlockRegistry.yellowCrystalTwo,BlockRegistry.purpleCrystalTwo,BlockRegistry.greenCrystalTwo,BlockRegistry.orangeCrystalTwo,BlockRegistry.whiteCrystalTwo,BlockRegistry.blackCrystalTwo,4.0F);
                        destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalOne,BlockRegistry.blueCrystalOne,BlockRegistry.yellowCrystalOne,BlockRegistry.purpleCrystalOne,BlockRegistry.greenCrystalOne,BlockRegistry.orangeCrystalOne,BlockRegistry.whiteCrystalOne,BlockRegistry.blackCrystalOne,3.0F);
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
            destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalFive,BlockRegistry.blueCrystalFive,BlockRegistry.yellowCrystalFive,BlockRegistry.purpleCrystalFive,BlockRegistry.greenCrystalFive,BlockRegistry.orangeCrystalFive,BlockRegistry.whiteCrystalFive,BlockRegistry.blackCrystalFive,7.0F);
            destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalFour,BlockRegistry.blueCrystalFour,BlockRegistry.yellowCrystalFour,BlockRegistry.purpleCrystalFour,BlockRegistry.greenCrystalFour,BlockRegistry.orangeCrystalFour,BlockRegistry.whiteCrystalFour,BlockRegistry.blackCrystalFour,6.0F);
            destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalThree,BlockRegistry.blueCrystalThree,BlockRegistry.yellowCrystalThree,BlockRegistry.purpleCrystalThree,BlockRegistry.greenCrystalThree,BlockRegistry.orangeCrystalThree,BlockRegistry.whiteCrystalThree,BlockRegistry.blackCrystalThree,5.0F);
            destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalTwo,BlockRegistry.blueCrystalTwo,BlockRegistry.yellowCrystalTwo,BlockRegistry.purpleCrystalTwo,BlockRegistry.greenCrystalTwo,BlockRegistry.orangeCrystalTwo,BlockRegistry.whiteCrystalTwo,BlockRegistry.blackCrystalTwo,4.0F);
            destroyedByExplosion(worldIn,pos,BlockRegistry.redCrystalOne,BlockRegistry.blueCrystalOne,BlockRegistry.yellowCrystalOne,BlockRegistry.purpleCrystalOne,BlockRegistry.greenCrystalOne,BlockRegistry.orangeCrystalOne,BlockRegistry.whiteCrystalOne,BlockRegistry.blackCrystalOne,3.0F);
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
            crystalMined(worldIn,pos,state,BlockRegistry.redCrystalFive,BlockRegistry.redCrystalFour,0);
            crystalMined(worldIn,pos,state,BlockRegistry.redCrystalFour,BlockRegistry.redCrystalThree,0);
            crystalMined(worldIn,pos,state,BlockRegistry.redCrystalThree,BlockRegistry.redCrystalTwo,0);
            crystalMined(worldIn,pos,state,BlockRegistry.redCrystalTwo,BlockRegistry.redCrystalOne,0);
            crystalDepleated(worldIn,pos,state,BlockRegistry.redCrystalOne,0);
            crystalMined(worldIn,pos,state,BlockRegistry.blueCrystalFive,BlockRegistry.blueCrystalFour,1);
            crystalMined(worldIn,pos,state,BlockRegistry.blueCrystalFour,BlockRegistry.blueCrystalThree,1);
            crystalMined(worldIn,pos,state,BlockRegistry.blueCrystalThree,BlockRegistry.blueCrystalTwo,1);
            crystalMined(worldIn,pos,state,BlockRegistry.blueCrystalTwo,BlockRegistry.blueCrystalOne,1);
            crystalDepleated(worldIn,pos,state,BlockRegistry.blueCrystalOne,1);
            crystalMined(worldIn,pos,state,BlockRegistry.yellowCrystalFive,BlockRegistry.yellowCrystalFour,2);
            crystalMined(worldIn,pos,state,BlockRegistry.yellowCrystalFour,BlockRegistry.yellowCrystalThree,2);
            crystalMined(worldIn,pos,state,BlockRegistry.yellowCrystalThree,BlockRegistry.yellowCrystalTwo,2);
            crystalMined(worldIn,pos,state,BlockRegistry.yellowCrystalTwo,BlockRegistry.yellowCrystalOne,2);
            crystalDepleated(worldIn,pos,state,BlockRegistry.yellowCrystalOne,2);
            crystalMined(worldIn,pos,state,BlockRegistry.purpleCrystalFive,BlockRegistry.purpleCrystalFour,3);
            crystalMined(worldIn,pos,state,BlockRegistry.purpleCrystalFour,BlockRegistry.purpleCrystalThree,3);
            crystalMined(worldIn,pos,state,BlockRegistry.purpleCrystalThree,BlockRegistry.purpleCrystalTwo,3);
            crystalMined(worldIn,pos,state,BlockRegistry.purpleCrystalTwo,BlockRegistry.purpleCrystalOne,3);
            crystalDepleated(worldIn,pos,state,BlockRegistry.purpleCrystalOne,3);
            crystalMined(worldIn,pos,state,BlockRegistry.greenCrystalFive,BlockRegistry.greenCrystalFour,4);
            crystalMined(worldIn,pos,state,BlockRegistry.greenCrystalFour,BlockRegistry.greenCrystalThree,4);
            crystalMined(worldIn,pos,state,BlockRegistry.greenCrystalThree,BlockRegistry.greenCrystalTwo,4);
            crystalMined(worldIn,pos,state,BlockRegistry.greenCrystalTwo,BlockRegistry.greenCrystalOne,4);
            crystalDepleated(worldIn,pos,state,BlockRegistry.greenCrystalOne,4);
            crystalMined(worldIn,pos,state,BlockRegistry.orangeCrystalFive,BlockRegistry.orangeCrystalFour,5);
            crystalMined(worldIn,pos,state,BlockRegistry.orangeCrystalFour,BlockRegistry.orangeCrystalThree,5);
            crystalMined(worldIn,pos,state,BlockRegistry.orangeCrystalThree,BlockRegistry.orangeCrystalTwo,5);
            crystalMined(worldIn,pos,state,BlockRegistry.orangeCrystalTwo,BlockRegistry.orangeCrystalOne,5);
            crystalDepleated(worldIn,pos,state,BlockRegistry.orangeCrystalOne,5);
            crystalMined(worldIn,pos,state,BlockRegistry.whiteCrystalFive,BlockRegistry.whiteCrystalFour,6);
            crystalMined(worldIn,pos,state,BlockRegistry.whiteCrystalFour,BlockRegistry.whiteCrystalThree,6);
            crystalMined(worldIn,pos,state,BlockRegistry.whiteCrystalThree,BlockRegistry.whiteCrystalTwo,6);
            crystalMined(worldIn,pos,state,BlockRegistry.whiteCrystalTwo,BlockRegistry.whiteCrystalOne,6);
            crystalDepleated(worldIn,pos,state,BlockRegistry.whiteCrystalOne,6);
            crystalMined(worldIn,pos,state,BlockRegistry.blackCrystalFive,BlockRegistry.blackCrystalFour,7);
            crystalMined(worldIn,pos,state,BlockRegistry.blackCrystalFour,BlockRegistry.blackCrystalThree,7);
            crystalMined(worldIn,pos,state,BlockRegistry.blackCrystalThree,BlockRegistry.blackCrystalTwo,7);
            crystalMined(worldIn,pos,state,BlockRegistry.blackCrystalTwo,BlockRegistry.blackCrystalOne,7);
            crystalDepleated(worldIn,pos,state,BlockRegistry.blackCrystalOne,7);
        }
    }

    private boolean regrowCrystal(World worldIn,BlockPos pos,EnumFacing facing,Block smallCrystal, Block largeCrystal)
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
                regrowCrystal(worldIn,pos,facing,BlockRegistry.redCrystalOne,BlockRegistry.redCrystalTwo);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.redCrystalTwo,BlockRegistry.redCrystalThree);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.redCrystalThree,BlockRegistry.redCrystalFour);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.redCrystalFour,BlockRegistry.redCrystalFive);
                //regrowCrystal(worldIn,pos,facing,BlockRegistry.redCrystalFive,BlockRegistry.redOre);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.blueCrystalOne,BlockRegistry.blueCrystalTwo);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.blueCrystalTwo,BlockRegistry.blueCrystalThree);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.blueCrystalThree,BlockRegistry.blueCrystalFour);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.blueCrystalFour,BlockRegistry.blueCrystalFive);
                //regrowCrystal(worldIn,pos,facing,BlockRegistry.blueCrystalFive,BlockRegistry.blueOre);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.yellowCrystalOne,BlockRegistry.yellowCrystalTwo);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.yellowCrystalTwo,BlockRegistry.yellowCrystalThree);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.yellowCrystalThree,BlockRegistry.yellowCrystalFour);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.yellowCrystalFour,BlockRegistry.yellowCrystalFive);
                //regrowCrystal(worldIn,pos,facing,BlockRegistry.yellowCrystalFive,BlockRegistry.yellowOre);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.purpleCrystalOne,BlockRegistry.purpleCrystalTwo);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.purpleCrystalTwo,BlockRegistry.purpleCrystalThree);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.purpleCrystalThree,BlockRegistry.purpleCrystalFour);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.purpleCrystalFour,BlockRegistry.purpleCrystalFive);
                //regrowCrystal(worldIn,pos,facing,BlockRegistry.purpleCrystalFive,BlockRegistry.purpleOre);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.greenCrystalOne,BlockRegistry.greenCrystalTwo);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.greenCrystalTwo,BlockRegistry.greenCrystalThree);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.greenCrystalThree,BlockRegistry.greenCrystalFour);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.greenCrystalFour,BlockRegistry.greenCrystalFive);
                //regrowCrystal(worldIn,pos,facing,BlockRegistry.greenCrystalFive,BlockRegistry.greenOre);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.orangeCrystalOne,BlockRegistry.orangeCrystalTwo);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.orangeCrystalTwo,BlockRegistry.orangeCrystalThree);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.orangeCrystalThree,BlockRegistry.orangeCrystalFour);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.orangeCrystalFour,BlockRegistry.orangeCrystalFive);
                //regrowCrystal(worldIn,pos,facing,BlockRegistry.orangeCrystalFive,BlockRegistry.orangeOre);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.whiteCrystalOne,BlockRegistry.whiteCrystalTwo);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.whiteCrystalTwo,BlockRegistry.whiteCrystalThree);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.whiteCrystalThree,BlockRegistry.whiteCrystalFour);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.whiteCrystalFour,BlockRegistry.whiteCrystalFive);
                //regrowCrystal(worldIn,pos,facing,BlockRegistry.whiteCrystalFive,BlockRegistry.whiteOre);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.blackCrystalOne,BlockRegistry.blackCrystalTwo);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.blackCrystalTwo,BlockRegistry.blackCrystalThree);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.blackCrystalThree,BlockRegistry.blackCrystalFour);
                regrowCrystal(worldIn,pos,facing,BlockRegistry.blackCrystalFour,BlockRegistry.blackCrystalFive);
                //regrowCrystal(worldIn,pos,facing,BlockRegistry.blackCrystalFive,BlockRegistry.blackOre);
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

}