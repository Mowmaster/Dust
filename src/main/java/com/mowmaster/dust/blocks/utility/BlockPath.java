package com.mowmaster.dust.blocks.utility;

import com.mowmaster.dust.blocks.blockbasics.BlockBasic;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockPath extends BlockBasic
{
//Look at Block.addRunningeffects
    public static Block path1;
    public static Block path2;
    public static Block path3;
    public static Block path4;

    public BlockPath(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(3);
        this.setResistance(15);
        this.setLightOpacity(0);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(DUSTBLOCKSTABS);
    }


    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9D, 1.0D);
    }

    /*@Override
    public boolean addRunningEffects(IBlockState state, World world, BlockPos pos, Entity entity) {
        return super.addRunningEffects(state, world, pos, entity);
    }*/

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
        Vec3d dir = new Vec3d(entity.motionX, entity.motionY, entity.motionZ);

        if(this.equals(path1))
        {
            if (dir.lengthVector() > 0.25) {
                dir = dir.normalize();
                entity.addVelocity(dir.x * 0.25, dir.y * 0.25, dir.z * 0.25);
            }
        }
        else if(this.equals(path2))
        {
            if (dir.lengthVector() > 0.25) {
                dir = dir.normalize();
                entity.addVelocity(dir.x * 0.5, dir.y * 0.5, dir.z * 0.5);
            }
        }
        else if(this.equals(path3))
        {
            if (dir.lengthVector() > 0.25) {
                dir = dir.normalize();
                entity.addVelocity(dir.x * 1, dir.y * 1, dir.z * 1);
            }
        }
        else if(this.equals(path4))
        {
            if (dir.lengthVector() > 0.25) {
                dir = dir.normalize();
                entity.addVelocity(dir.x * 2, dir.y * 2, dir.z * 2);
            }
        }

        /*
        Vec3d dir = new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
        if (dir.lengthVector() > 0.25) {
            dir = dir.normalize();
            if(this.equals(BlockRegistry.path1))
            {
                entity.addVelocity(dir.x * 0.125, dir.y * 0.125, dir.z * 0.125);
            }
            else if(this.equals(BlockRegistry.path2))
            {
                entity.addVelocity(dir.x * 0.3, dir.y * 0.3, dir.z * 0.3);
            }
            else if(this.equals(BlockRegistry.path3))
            {
                entity.addVelocity(dir.x * 0.5, dir.y * 0.5, dir.z * 0.5);
            }
            else if(this.equals(BlockRegistry.path4))
            {
                entity.addVelocity(dir.x * 1, dir.y * 1, dir.z * 1);
            }
        }
         */

    }

    public static void Init()
    {
        path1 = new BlockPath("path1","path1");
        path2 = new BlockPath("path2","path2");
        path3 = new BlockPath("path3","path3");
        path4 = new BlockPath("path4","path4");
    }

    public static void Register()
    {
        registerBlock(path1);
        registerBlock(path2);
        registerBlock(path3);
        registerBlock(path4);
    }

    public static void RegisterRender()
    {
        registerRender(path1);
        registerRender(path2);
        registerRender(path3);
        registerRender(path4);
    }


}
