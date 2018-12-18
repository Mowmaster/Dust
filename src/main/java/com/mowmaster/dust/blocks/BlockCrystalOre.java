package com.mowmaster.dust.blocks;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockCrystalOre extends Block
{
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
        ItemStack returned = new ItemStack(BlockRegistry.redOre,1);
        if(state.getBlock().equals(BlockRegistry.redOre)){returned = new ItemStack(BlockRegistry.redOre,1);}
        else if(state.getBlock().equals(BlockRegistry.blueOre)){returned = new ItemStack(BlockRegistry.blueOre,1);}
        else if(state.getBlock().equals(BlockRegistry.yellowOre)){returned = new ItemStack(BlockRegistry.yellowOre,1);}
        else if(state.getBlock().equals(BlockRegistry.purpleOre)){returned = new ItemStack(BlockRegistry.purpleOre,1);}
        else if(state.getBlock().equals(BlockRegistry.greenOre)){returned = new ItemStack(BlockRegistry.greenOre,1);}
        else if(state.getBlock().equals(BlockRegistry.orangeOre)){returned = new ItemStack(BlockRegistry.orangeOre,1);}
        else if(state.getBlock().equals(BlockRegistry.whiteOre)){returned = new ItemStack(BlockRegistry.whiteOre,1);}
        else if(state.getBlock().equals(BlockRegistry.blackOre)){returned = new ItemStack(BlockRegistry.blackOre,1);}

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
        if(this.getDefaultState().getBlock().equals(BlockRegistry.redOre)) {count=0;}
        else if(this.getDefaultState().getBlock().equals(BlockRegistry.blueOre)) {count=1;}
        else if(this.getDefaultState().getBlock().equals(BlockRegistry.yellowOre)) {count=2;}
        else if(this.getDefaultState().getBlock().equals(BlockRegistry.purpleOre)) {count=3;}
        else if(this.getDefaultState().getBlock().equals(BlockRegistry.greenOre)) {count=4;}
        else if(this.getDefaultState().getBlock().equals(BlockRegistry.orangeOre)) {count=5;}
        else if(this.getDefaultState().getBlock().equals(BlockRegistry.whiteOre)) {count=6;}
        else if(this.getDefaultState().getBlock().equals(BlockRegistry.blackOre)) {count=7;}
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
            oreToCrystal(worldIn,pos,BlockRegistry.redOre,BlockRegistry.redCrystalFive);
            oreToCrystal(worldIn,pos,BlockRegistry.blueOre,BlockRegistry.blueCrystalFive);
            oreToCrystal(worldIn,pos,BlockRegistry.yellowOre,BlockRegistry.yellowCrystalFive);
            oreToCrystal(worldIn,pos,BlockRegistry.purpleOre,BlockRegistry.purpleCrystalFive);
            oreToCrystal(worldIn,pos,BlockRegistry.greenOre,BlockRegistry.greenCrystalFive);
            oreToCrystal(worldIn,pos,BlockRegistry.orangeOre,BlockRegistry.orangeCrystalFive);
            oreToCrystal(worldIn,pos,BlockRegistry.whiteOre,BlockRegistry.whiteCrystalFive);
            oreToCrystal(worldIn,pos,BlockRegistry.blackOre,BlockRegistry.blackCrystalFive);
        }
    }


}
