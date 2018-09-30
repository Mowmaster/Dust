package com.mowmaster.dust.blocks;

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

import javax.annotation.Nullable;
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockPath extends Block
{

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

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
        Vec3d dir = new Vec3d(entity.motionX, entity.motionY, entity.motionZ);

        if(this.equals(BlockRegistry.path1))
        {
            if (dir.lengthVector() > 0.25) {
                dir = dir.normalize();
                entity.addVelocity(dir.x * 0.25, dir.y * 0.25, dir.z * 0.25);
            }
        }
        else if(this.equals(BlockRegistry.path2))
        {
            if (dir.lengthVector() > 0.25) {
                dir = dir.normalize();
                entity.addVelocity(dir.x * 0.5, dir.y * 0.5, dir.z * 0.5);
            }
        }
        else if(this.equals(BlockRegistry.path3))
        {
            if (dir.lengthVector() > 0.25) {
                dir = dir.normalize();
                entity.addVelocity(dir.x * 1, dir.y * 1, dir.z * 1);
            }
        }
        else if(this.equals(BlockRegistry.path4))
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


}
