package com.mowmaster.dust.blocks;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

/**
 * Created by KingMowmaster on 3/15/2017.
 */
public class BlockBasic extends Block
{
    public BlockBasic(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(20);
        this.setResistance(20);
        this.setLightOpacity(10);
        this.setCreativeTab(DUSTTABS);
    }

    @Override
    public Item getItemDropped(IBlockState state,Random random,int fortune)
    {
        return null;
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote)
        {
            if(this.equals(BlockRegistry.redOre))
            {
                worldIn.setBlockState(pos,BlockRegistry.redCrystalFive.getDefaultState());
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,new ItemStack(ItemRegistry.dust,1,8)));
            }
            else if(this.equals(BlockRegistry.blueOre))
            {
                worldIn.setBlockState(pos,BlockRegistry.blueCrystalFive.getDefaultState());
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,new ItemStack(ItemRegistry.dust,1,8)));
            }
            else if(this.equals(BlockRegistry.yellowOre))
            {
                worldIn.setBlockState(pos,BlockRegistry.yellowCrystalFive.getDefaultState());
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,new ItemStack(ItemRegistry.dust,1,8)));
            }
            else if(this.equals(BlockRegistry.purpleOre))
            {
                worldIn.setBlockState(pos,BlockRegistry.purpleCrystalFive.getDefaultState());
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,new ItemStack(ItemRegistry.dust,1,8)));
            }
            else if(this.equals(BlockRegistry.orangeOre))
            {
                worldIn.setBlockState(pos,BlockRegistry.orangeCrystalFive.getDefaultState());
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,new ItemStack(ItemRegistry.dust,1,8)));
            }
            else if(this.equals(BlockRegistry.greenOre))
            {
                worldIn.setBlockState(pos,BlockRegistry.greenCrystalFive.getDefaultState());
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,new ItemStack(ItemRegistry.dust,1,8)));
            }
            else if(this.equals(BlockRegistry.whiteOre))
            {
                worldIn.setBlockState(pos,BlockRegistry.whiteCrystalFive.getDefaultState());
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,new ItemStack(ItemRegistry.dust,1,8)));
            }
            else if(this.equals(BlockRegistry.blackOre))
            {
                worldIn.setBlockState(pos,BlockRegistry.blackCrystalFive.getDefaultState());
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,new ItemStack(ItemRegistry.dust,1,8)));
            }
        }
    }
}
