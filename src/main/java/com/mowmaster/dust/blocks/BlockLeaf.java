package com.mowmaster.dust.blocks;


import com.mowmaster.dust.blocks.item.IMetaBlockName;
import com.mowmaster.dust.enums.CrystalBlocks;
import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.items.ItemDust;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static com.mowmaster.dust.items.ItemRegistry.dust;
import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;



//Maybe try extending Block leaves sometime?
public class BlockLeaf  extends Block implements IMetaBlockName
{

    //the string text is what you use in your json file
    public static final PropertyEnum LEAVES = PropertyEnum.create("leaves",CrystalBlocks.CrystalLeaves.class);


    public BlockLeaf(String unloc)
    {
        super(Material.LEAVES);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unloc));
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.RED));
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(DUSTTABS);
        //this.setSoundType(SoundType.WOOD);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,new IProperty[]{LEAVES});
    }


    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        int blockmeta = placer.getHeldItem(EnumHand.MAIN_HAND).getMetadata();
        return getStateFromMeta(blockmeta);
    }



    @Override
    public int getMetaFromState(IBlockState state)
    {
        CrystalBlocks.CrystalLeaves leaves = (CrystalBlocks.CrystalLeaves) state.getValue(LEAVES);
        return leaves.getID();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LEAVES,CrystalBlocks.CrystalLeaves.values()[meta]);
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (int i = 0; i < CrystalBlocks.CrystalLeaves.values().length; i++)
        {
            list.add(new ItemStack(itemIn,1,i));
        }
    }

    public String getSpecialName(ItemStack stack)
    {
        return CrystalBlocks.CrystalLeaves.values()[stack.getItemDamage()].getName();
    }


    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(Item.getItemFromBlock(this),1,getMetaFromState(world.getBlockState(pos)));
    }


    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (worldIn.isRainingAt(pos.up()) && !worldIn.getBlockState(pos.down()).isFullyOpaque() && rand.nextInt(15) == 1)
        {
            double d0 = (double)((float)pos.getX() + rand.nextFloat());
            double d1 = (double)pos.getY() - 0.05D;
            double d2 = (double)((float)pos.getZ() + rand.nextFloat());
            worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    /**
     * Pass true to draw this block using fancy graphics, or false for fast graphics.
     */

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public boolean isVisuallyOpaque()
    {
        return false;
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        int meta = this.getMetaFromState(state);
        Random rn = new Random();
        int drop = rn.nextInt(20);
        if(!worldIn.isRemote)
        {
            if(drop == 0)
            {
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.dust, 1, meta)));
            }
        }

    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        Random rn = new Random();
        int drop = rn.nextInt(20);
        Item dropped;
        dropped = null;
        if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.RED)))
        {

            if(drop <= 18) {dropped = null;}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingred);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.BLUE)))
        {
            if(drop <= 18) {dropped = null;}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingblue);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.YELLOW)))
        {
            if(drop <= 18) {dropped = null;}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingyellow);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.PURPLE)))
        {
            if(drop <= 18) {dropped = null;}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingpurple);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.ORANGE)))
        {
            if(drop <= 18) {dropped = null;}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingorange);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.GREEN)))
        {
            if(drop <= 18) {dropped = null;}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplinggreen);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.WHITE)))
        {
            if(drop <= 18) {dropped = null;}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingwhite);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.BLACK)))
        {
            if(drop <= 18) {dropped = null;}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingblack);}
        }
        return dropped;
    }
}





/*
package com.mowmaster.dust.blocks;



import com.mowmaster.dust.blocks.item.IMetaBlockName;
import com.mowmaster.dust.enums.CrystalBlocks;
import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Random;

import static com.mowmaster.dust.items.ItemRegistry.dust;
import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;



//Maybe try extending Block leaves sometime?
public class BlockLeaf  extends Block
{
    public BlockLeaf(String unloc, String registryName)
    {
        super(Material.LEAVES);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unloc));
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(DUSTTABS);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        Random rn = new Random();
        int drop = rn.nextInt(20);
        Item dropped;
        dropped = null;

        if(this.equals(BlockRegistry.leafred))
        {
            if(drop <= 16) {dropped = null;}
            else if(drop>=17 && drop<=18) {dropped = ItemRegistry.dust.getItemById(0);}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingred);}
        }
        else if(state.equals(CrystalBlocks.CrystalLeaves.BLUE))
        {
            if(drop <= 16) {dropped = null;}
            else if(drop>=17 && drop<=18) {dropped = ItemRegistry.dust.getItemById(1);}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingblue);}
        }
        else if(state.equals(CrystalBlocks.CrystalLeaves.YELLOW))
        {
            if(drop <= 16) {dropped = null;}
            else if(drop>=17 && drop<=18) {dropped = ItemRegistry.dust.getItemById(2);}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingyellow);}
        }
        else if(state.equals(CrystalBlocks.CrystalLeaves.PURPLE))
        {
            if(drop <= 16) {dropped = null;}
            else if(drop>=17 && drop<=18) {dropped = ItemRegistry.dust.getItemById(3);}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingpurple);}
        }
        else if(state.equals(CrystalBlocks.CrystalLeaves.ORANGE))
        {
            if(drop <= 16) {dropped = null;}
            else if(drop>=17 && drop<=18) {dropped = ItemRegistry.dust.getItemById(5);}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingorange);}
        }
        else if(state.equals(CrystalBlocks.CrystalLeaves.GREEN))
        {
            if(drop <= 16) {dropped = null;}
            else if(drop>=17 && drop<=18) {dropped = ItemRegistry.dust.getItemById(4);}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplinggreen);}
        }
        else if(state.equals(CrystalBlocks.CrystalLeaves.WHITE))
        {
            if(drop <= 16) {dropped = null;}
            else if(drop>=17 && drop<=18) {dropped = ItemRegistry.dust.getItemById(6);}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingwhite);}
        }
        else if(state.equals(CrystalBlocks.CrystalLeaves.BLACK))
        {
            if(drop <= 16) {dropped = null;}
            else if(drop>=17 && drop<=18) {dropped = ItemRegistry.dust.getItemById(7);}
            else if(drop==19) {dropped = Item.getItemFromBlock(BlockRegistry.saplingblack);}
        }
        return dropped;
    }


    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (worldIn.isRainingAt(pos.up()) && !worldIn.getBlockState(pos.down()).isFullyOpaque() && rand.nextInt(15) == 1)
        {
            double d0 = (double)((float)pos.getX() + rand.nextFloat());
            double d1 = (double)pos.getY() - 0.05D;
            double d2 = (double)((float)pos.getZ() + rand.nextFloat());
            worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
/*
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
*/
    /**
     * Pass true to draw this block using fancy graphics, or false for fast graphics.
     */
/*
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public boolean isVisuallyOpaque()
    {
        return false;
    }


}
*/