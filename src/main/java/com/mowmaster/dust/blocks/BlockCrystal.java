package com.mowmaster.dust.blocks;

import com.mojang.authlib.properties.Property;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.misc.AchievementHandler;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.Random;
import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;
import static com.mowmaster.dust.world.biome.BiomeRegistry.*;


public class BlockCrystal extends BlockDirectional
{

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
        this.setHardness(10);
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

        if (rand.nextInt(5) == 0)
        {
            worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, d0 + (double)enumfacing.getFrontOffsetX() * d3, d1 + (double)enumfacing.getFrontOffsetY() * d3, d2 + (double)enumfacing.getFrontOffsetZ() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
        }
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote && entityIn instanceof EntityArrow)
        {
            EntityArrow entityarrow = (EntityArrow)entityIn;
            if (entityarrow.isBurning() )
            {
                if(this.equals(BlockRegistry.redCrystalFive) || this.equals(BlockRegistry.blueCrystalFive) || this.equals(BlockRegistry.yellowCrystalFive) || this.equals(BlockRegistry.purpleCrystalFive) || this.equals(BlockRegistry.orangeCrystalFive) || this.equals(BlockRegistry.greenCrystalFive) || this.equals(BlockRegistry.whiteCrystalFive) || this.equals(BlockRegistry.blackCrystalFive)) {worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,7.0F, true);}
                else if(this.equals(BlockRegistry.redCrystalFour) || this.equals(BlockRegistry.blueCrystalFour) || this.equals(BlockRegistry.yellowCrystalFour) || this.equals(BlockRegistry.purpleCrystalFour) || this.equals(BlockRegistry.orangeCrystalFour) || this.equals(BlockRegistry.greenCrystalFour) || this.equals(BlockRegistry.whiteCrystalFour) || this.equals(BlockRegistry.blackCrystalFour)) {worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,6.0F, true);}
                else if(this.equals(BlockRegistry.redCrystalThree) || this.equals(BlockRegistry.blueCrystalThree) || this.equals(BlockRegistry.yellowCrystalThree) || this.equals(BlockRegistry.purpleCrystalThree) || this.equals(BlockRegistry.orangeCrystalThree) || this.equals(BlockRegistry.greenCrystalThree) || this.equals(BlockRegistry.whiteCrystalThree) || this.equals(BlockRegistry.blackCrystalThree)) {worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,5.0F, true);}
                else if(this.equals(BlockRegistry.redCrystalTwo) || this.equals(BlockRegistry.blueCrystalTwo) || this.equals(BlockRegistry.yellowCrystalTwo) || this.equals(BlockRegistry.purpleCrystalTwo) || this.equals(BlockRegistry.orangeCrystalTwo) || this.equals(BlockRegistry.greenCrystalTwo) || this.equals(BlockRegistry.whiteCrystalTwo) || this.equals(BlockRegistry.blackCrystalTwo)) {worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,4.0F, true);}
                else if(this.equals(BlockRegistry.redCrystalOne) || this.equals(BlockRegistry.blueCrystalOne) || this.equals(BlockRegistry.yellowCrystalOne) || this.equals(BlockRegistry.purpleCrystalOne) || this.equals(BlockRegistry.orangeCrystalOne) || this.equals(BlockRegistry.greenCrystalOne) || this.equals(BlockRegistry.whiteCrystalOne) || this.equals(BlockRegistry.blackCrystalOne)) {worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,3.0F, true);}
            }
        }
        if (!worldIn.isRemote && entityIn instanceof EntityAnimal || entityIn instanceof EntitySquid || entityIn instanceof EntityVillager || entityIn instanceof EntityBat || entityIn instanceof EntityPlayer)
        {
            if(!(entityIn instanceof EntityZombieHorse)) {
                if (!(entityIn instanceof EntitySkeletonHorse)) {
                    if (this.equals(BlockRegistry.redCrystalFive) || this.equals(BlockRegistry.blueCrystalFive) || this.equals(BlockRegistry.yellowCrystalFive) || this.equals(BlockRegistry.purpleCrystalFive) || this.equals(BlockRegistry.orangeCrystalFive) || this.equals(BlockRegistry.greenCrystalFive) || this.equals(BlockRegistry.whiteCrystalFive) || this.equals(BlockRegistry.blackCrystalFive)) {
                        worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 7.0F, true);
                        worldIn.setBlockToAir(pos);
                    } else if (this.equals(BlockRegistry.redCrystalFour) || this.equals(BlockRegistry.blueCrystalFour) || this.equals(BlockRegistry.yellowCrystalFour) || this.equals(BlockRegistry.purpleCrystalFour) || this.equals(BlockRegistry.orangeCrystalFour) || this.equals(BlockRegistry.greenCrystalFour) || this.equals(BlockRegistry.whiteCrystalFour) || this.equals(BlockRegistry.blackCrystalFour)) {
                        worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 6.0F, true);
                        worldIn.setBlockToAir(pos);
                    } else if (this.equals(BlockRegistry.redCrystalThree) || this.equals(BlockRegistry.blueCrystalThree) || this.equals(BlockRegistry.yellowCrystalThree) || this.equals(BlockRegistry.purpleCrystalThree) || this.equals(BlockRegistry.orangeCrystalThree) || this.equals(BlockRegistry.greenCrystalThree) || this.equals(BlockRegistry.whiteCrystalThree) || this.equals(BlockRegistry.blackCrystalThree)) {
                        worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 5.0F, true);
                        worldIn.setBlockToAir(pos);
                    } else if (this.equals(BlockRegistry.redCrystalTwo) || this.equals(BlockRegistry.blueCrystalTwo) || this.equals(BlockRegistry.yellowCrystalTwo) || this.equals(BlockRegistry.purpleCrystalTwo) || this.equals(BlockRegistry.orangeCrystalTwo) || this.equals(BlockRegistry.greenCrystalTwo) || this.equals(BlockRegistry.whiteCrystalTwo) || this.equals(BlockRegistry.blackCrystalTwo)) {
                        worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 4.0F, true);
                        worldIn.setBlockToAir(pos);
                    } else if (this.equals(BlockRegistry.redCrystalOne) || this.equals(BlockRegistry.blueCrystalOne) || this.equals(BlockRegistry.yellowCrystalOne) || this.equals(BlockRegistry.purpleCrystalOne) || this.equals(BlockRegistry.orangeCrystalOne) || this.equals(BlockRegistry.greenCrystalOne) || this.equals(BlockRegistry.whiteCrystalOne) || this.equals(BlockRegistry.blackCrystalOne)) {
                        worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 3.0F, true);
                        worldIn.setBlockToAir(pos);
                    }
                }
            }
        }
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote)
        {
            if(this.equals(BlockRegistry.redCrystalFive) || this.equals(BlockRegistry.blueCrystalFive) || this.equals(BlockRegistry.yellowCrystalFive) || this.equals(BlockRegistry.purpleCrystalFive) || this.equals(BlockRegistry.orangeCrystalFive) || this.equals(BlockRegistry.greenCrystalFive) || this.equals(BlockRegistry.whiteCrystalFive) || this.equals(BlockRegistry.blackCrystalFive))
            {
                worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,7.0F, true);
            }

            if(this.equals(BlockRegistry.redCrystalFour) || this.equals(BlockRegistry.blueCrystalFour) || this.equals(BlockRegistry.yellowCrystalFour) || this.equals(BlockRegistry.purpleCrystalFour) || this.equals(BlockRegistry.orangeCrystalFour) || this.equals(BlockRegistry.greenCrystalFour) || this.equals(BlockRegistry.whiteCrystalFour) || this.equals(BlockRegistry.blackCrystalFour))
            {
                worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,6.0F, true);
            }

            if(this.equals(BlockRegistry.redCrystalThree) || this.equals(BlockRegistry.blueCrystalThree) || this.equals(BlockRegistry.yellowCrystalThree) || this.equals(BlockRegistry.purpleCrystalThree) || this.equals(BlockRegistry.orangeCrystalThree) || this.equals(BlockRegistry.greenCrystalThree) || this.equals(BlockRegistry.whiteCrystalThree) || this.equals(BlockRegistry.blackCrystalThree))
            {
                worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,5.0F, true);
            }

            if(this.equals(BlockRegistry.redCrystalTwo) || this.equals(BlockRegistry.blueCrystalTwo) || this.equals(BlockRegistry.yellowCrystalTwo) || this.equals(BlockRegistry.purpleCrystalTwo) || this.equals(BlockRegistry.orangeCrystalTwo) || this.equals(BlockRegistry.greenCrystalTwo) || this.equals(BlockRegistry.whiteCrystalTwo) || this.equals(BlockRegistry.blackCrystalTwo))
            {
                worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,4.0F, true);
            }

            if(this.equals(BlockRegistry.redCrystalOne) || this.equals(BlockRegistry.blueCrystalOne) || this.equals(BlockRegistry.yellowCrystalOne) || this.equals(BlockRegistry.purpleCrystalOne) || this.equals(BlockRegistry.orangeCrystalOne) || this.equals(BlockRegistry.greenCrystalOne) || this.equals(BlockRegistry.whiteCrystalOne) || this.equals(BlockRegistry.blackCrystalOne))
            {
                worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,3.0F, true);
            }
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        if (!worldIn.isRemote) {
                if (this.equals(BlockRegistry.redCrystalFive)) {
                    worldIn.setBlockState(pos, BlockRegistry.redCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 0)));
                } else if (this.equals(BlockRegistry.redCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.redCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 0)));
                } else if (this.equals(BlockRegistry.redCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.redCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 0)));
                } else if (this.equals(BlockRegistry.redCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.redCrystalOne.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 0)));
                } else if (this.equals(BlockRegistry.redCrystalOne)) {
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 0)));
                    return;
                }

                if (this.equals(BlockRegistry.blueCrystalFive)) {
                    worldIn.setBlockState(pos, BlockRegistry.blueCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 1)));
                } else if (this.equals(BlockRegistry.blueCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.blueCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 1)));
                } else if (this.equals(BlockRegistry.blueCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.blueCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 1)));
                } else if (this.equals(BlockRegistry.blueCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.blueCrystalOne.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 1)));
                } else if (this.equals(BlockRegistry.blueCrystalOne)) {
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 1)));
                    return;
                }

                if (this.equals(BlockRegistry.yellowCrystalFive)) {
                    worldIn.setBlockState(pos, BlockRegistry.yellowCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 2)));
                } else if (this.equals(BlockRegistry.yellowCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.yellowCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 2)));
                } else if (this.equals(BlockRegistry.yellowCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.yellowCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 2)));
                } else if (this.equals(BlockRegistry.yellowCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.yellowCrystalOne.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 2)));
                } else if (this.equals(BlockRegistry.yellowCrystalOne)) {
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 2)));
                    return;
                }

                if (this.equals(BlockRegistry.purpleCrystalFive)) {
                    worldIn.setBlockState(pos, BlockRegistry.purpleCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 3)));
                } else if (this.equals(BlockRegistry.purpleCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.purpleCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 3)));
                } else if (this.equals(BlockRegistry.purpleCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.purpleCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 3)));
                } else if (this.equals(BlockRegistry.purpleCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.purpleCrystalOne.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 3)));
                } else if (this.equals(BlockRegistry.purpleCrystalOne)) {
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 3)));
                    return;
                }

                if (this.equals(BlockRegistry.greenCrystalFive)) {
                    worldIn.setBlockState(pos, BlockRegistry.greenCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.crystal, 1, 4)));
                } else if (this.equals(BlockRegistry.greenCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.greenCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.crystal, 1, 4)));
                } else if (this.equals(BlockRegistry.greenCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.greenCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.crystal, 1, 4)));
                } else if (this.equals(BlockRegistry.greenCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.greenCrystalOne.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.crystal, 1, 4)));
                } else if (this.equals(BlockRegistry.greenCrystalOne)) {
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.crystal, 1, 4)));
                    return;
                }

                if (this.equals(BlockRegistry.orangeCrystalFive)) {
                    worldIn.setBlockState(pos, BlockRegistry.orangeCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 5)));
                } else if (this.equals(BlockRegistry.orangeCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.orangeCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 5)));
                } else if (this.equals(BlockRegistry.orangeCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.orangeCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 5)));
                } else if (this.equals(BlockRegistry.orangeCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.orangeCrystalOne.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 5)));
                } else if (this.equals(BlockRegistry.orangeCrystalOne)) {
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.crystal, 1, 5)));
                    return;
                }


                if (this.equals(BlockRegistry.whiteCrystalFive)) {
                    worldIn.setBlockState(pos, BlockRegistry.whiteCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.6, pos.getY() + 1.0, pos.getZ() + 0.6, new ItemStack(ItemRegistry.crystal, 1, 6)));
                } else if (this.equals(BlockRegistry.whiteCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.whiteCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.6, pos.getY() + 1.0, pos.getZ() + 0.6, new ItemStack(ItemRegistry.crystal, 1, 6)));
                } else if (this.equals(BlockRegistry.whiteCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.whiteCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.6, pos.getY() + 1.0, pos.getZ() + 0.6, new ItemStack(ItemRegistry.crystal, 1, 6)));
                } else if (this.equals(BlockRegistry.whiteCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.whiteCrystalOne.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.6, pos.getY() + 1.0, pos.getZ() + 0.6, new ItemStack(ItemRegistry.crystal, 1, 6)));
                } else if (this.equals(BlockRegistry.whiteCrystalOne)) {
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.6, pos.getY() + 1.0, pos.getZ() + 0.6, new ItemStack(ItemRegistry.crystal, 1, 6)));
                    return;
                }

                if (this.equals(BlockRegistry.blackCrystalFive)) {
                    worldIn.setBlockState(pos, BlockRegistry.blackCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.7, pos.getY() + 1.0, pos.getZ() + 0.7, new ItemStack(ItemRegistry.crystal, 1, 7)));
                } else if (this.equals(BlockRegistry.blackCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.blackCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.7, pos.getY() + 1.0, pos.getZ() + 0.7, new ItemStack(ItemRegistry.crystal, 1, 7)));
                } else if (this.equals(BlockRegistry.blackCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.blackCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.7, pos.getY() + 1.0, pos.getZ() + 0.7, new ItemStack(ItemRegistry.crystal, 1, 7)));
                } else if (this.equals(BlockRegistry.blackCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.blackCrystalOne.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.7, pos.getY() + 1.0, pos.getZ() + 0.7, new ItemStack(ItemRegistry.crystal, 1, 7)));
                } else if (this.equals(BlockRegistry.blackCrystalOne)) {
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.7, pos.getY() + 1.0, pos.getZ() + 0.7, new ItemStack(ItemRegistry.crystal, 1, 7)));
                    return;
                }
        }
    }


    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        super.randomTick(worldIn, pos, state, random);
        final int CHANCE = 100;

        int rand = random.nextInt(1000);

        if(worldIn.getBiome(pos).equals(crystal_hot) || worldIn.getBiome(pos).equals(crystal_warm) || worldIn.getBiome(pos).equals(crystal_cold) || worldIn.getBiome(pos).equals(crystal_crystal)) {
            EnumFacing facing = state.getValue(FACING);
            if (rand < CHANCE) {
            /*
            if (this.equals(BlockRegistry.redCrystalFive)) {
                worldIn.setBlockState(pos, BlockRegistry.redOre.getDefaultState().withProperty(BlockCrystal.FACING, facing));
            } else if (this.equals(BlockRegistry.blueCrystalFive)) {
                worldIn.setBlockState(pos, BlockRegistry.blueOre.getDefaultState().withProperty(BlockCrystal.FACING, facing));
            } else if (this.equals(BlockRegistry.yellowCrystalFive)) {
                worldIn.setBlockState(pos, BlockRegistry.yellowOre.getDefaultState().withProperty(BlockCrystal.FACING, facing));
            } else if (this.equals(BlockRegistry.purpleCrystalFive)) {
                worldIn.setBlockState(pos, BlockRegistry.purpleOre.getDefaultState().withProperty(BlockCrystal.FACING, facing));
            } else if (this.equals(BlockRegistry.orangeCrystalFive)) {
                worldIn.setBlockState(pos, BlockRegistry.orangeOre.getDefaultState().withProperty(BlockCrystal.FACING, facing));
            } else if (this.equals(BlockRegistry.greenCrystalFive)) {
                worldIn.setBlockState(pos, BlockRegistry.greenOre.getDefaultState().withProperty(BlockCrystal.FACING, facing));
            } else if (this.equals(BlockRegistry.whiteCrystalFive)) {
                worldIn.setBlockState(pos, BlockRegistry.whiteOre.getDefaultState().withProperty(BlockCrystal.FACING, facing));
            } else if (this.equals(BlockRegistry.blackCrystalFive)) {
                worldIn.setBlockState(pos, BlockRegistry.blackOre.getDefaultState().withProperty(BlockCrystal.FACING, facing));
            } else */
                if (this.equals(BlockRegistry.redCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.redCrystalFive.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.blueCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.blueCrystalFive.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.yellowCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.yellowCrystalFive.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.purpleCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.purpleCrystalFive.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.orangeCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.orangeCrystalFive.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.greenCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.greenCrystalFive.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.whiteCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.whiteCrystalFive.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.blackCrystalFour)) {
                    worldIn.setBlockState(pos, BlockRegistry.blackCrystalFive.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.redCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.redCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.blueCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.blueCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.yellowCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.yellowCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.purpleCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.purpleCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.orangeCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.orangeCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.greenCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.greenCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.whiteCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.whiteCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.blackCrystalThree)) {
                    worldIn.setBlockState(pos, BlockRegistry.blackCrystalFour.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.redCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.redCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.blueCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.blueCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.yellowCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.yellowCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.purpleCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.purpleCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.orangeCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.orangeCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.greenCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.greenCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.whiteCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.whiteCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.blackCrystalTwo)) {
                    worldIn.setBlockState(pos, BlockRegistry.blackCrystalThree.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.redCrystalOne)) {
                    worldIn.setBlockState(pos, BlockRegistry.redCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.blueCrystalOne)) {
                    worldIn.setBlockState(pos, BlockRegistry.blueCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.yellowCrystalOne)) {
                    worldIn.setBlockState(pos, BlockRegistry.yellowCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.purpleCrystalOne)) {
                    worldIn.setBlockState(pos, BlockRegistry.purpleCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.orangeCrystalOne)) {
                    worldIn.setBlockState(pos, BlockRegistry.orangeCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.greenCrystalOne)) {
                    worldIn.setBlockState(pos, BlockRegistry.greenCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.whiteCrystalOne)) {
                    worldIn.setBlockState(pos, BlockRegistry.whiteCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                } else if (this.equals(BlockRegistry.blackCrystalOne)) {
                    worldIn.setBlockState(pos, BlockRegistry.blackCrystalTwo.getDefaultState().withProperty(BlockCrystal.FACING, facing));
                }
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

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        if (!playerIn.hasAchievement(AchievementHandler.achievementCrystal))
        {
            playerIn.addStat(AchievementHandler.achievementCrystal);
        }
    }

}