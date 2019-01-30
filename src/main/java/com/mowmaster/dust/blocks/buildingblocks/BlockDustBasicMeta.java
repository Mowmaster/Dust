package com.mowmaster.dust.blocks.buildingblocks;

import com.mowmaster.dust.blocks.blockbasics.BlockBasic;
import com.mowmaster.dust.blocks.item.IMetaBlockName;
import com.mowmaster.dust.blocks.item.ItemBlockOre;
import com.mowmaster.dust.enums.CrystalBlocks;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockDustBasicMeta extends BlockBasic implements IMetaBlockName
{
    public static final PropertyEnum COLORS = PropertyEnum.create("basicblocks",CrystalBlocks.CrystalColors.class);
    public static Block duststone;
    public static Block dustbrick;
    public static Block dustbricks;
    public static Block dustplanks;

    public BlockDustBasicMeta(String unloc, Material material, SoundType soundType, int hardness, int resistance, int lightopacity)
    {
        super(material);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unloc));
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightOpacity(lightopacity);
        this.setSoundType(soundType);
        this.setCreativeTab(DUSTBLOCKSTABS);
    }
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,new IProperty[]{COLORS});
    }


    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        int blockmeta = placer.getHeldItem(EnumHand.MAIN_HAND).getMetadata();
        return getStateFromMeta(blockmeta);
    }



    @Override
    public int getMetaFromState(IBlockState state)
    {
        CrystalBlocks.CrystalColors colors = (CrystalBlocks.CrystalColors) state.getValue(COLORS);
        return colors.getID();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(COLORS,CrystalBlocks.CrystalColors.values()[meta]);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < CrystalBlocks.CrystalColors.values().length; i++)
        {
            items.add(new ItemStack(this,1,i));
        }
    }

    public String getSpecialName(ItemStack stack)
    {
        return CrystalBlocks.CrystalColors.values()[stack.getItemDamage()].getName();
    }


    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(Item.getItemFromBlock(this),1,getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public Item getItemDropped(IBlockState state,Random random,int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    public static void Init()
    {
        dustplanks = new BlockDustBasicMeta("dustplanks",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        duststone = new BlockDustBasicMeta("duststone",Material.ROCK, SoundType.STONE, 3, 20, 10);
        dustbrick = new BlockDustBasicMeta("dustbrick",Material.ROCK, SoundType.STONE, 3, 20, 10);
        dustbricks = new BlockDustBasicMeta("dustbricks",Material.ROCK, SoundType.STONE, 3, 20, 10);
    }

    public static void Register()
    {
        registerBlock(dustplanks, new ItemBlockOre(dustplanks));
        registerBlock(duststone, new ItemBlockOre(duststone));
        registerBlock(dustbrick, new ItemBlockOre(dustbrick));
        registerBlock(dustbricks, new ItemBlockOre(dustbricks));
    }

    public static void RegisterRender()
    {
        for (int i = 0; i < CrystalBlocks.CrystalColors.values().length; i++)
        {
            registerRender(dustplanks,i,"dustplanks_" + CrystalBlocks.CrystalColors.values()[i].getName());
            registerRender(duststone,i,"duststone_" + CrystalBlocks.CrystalColors.values()[i].getName());
            registerRender(dustbrick,i,"dustbrick_" + CrystalBlocks.CrystalColors.values()[i].getName());
            registerRender(dustbricks,i,"dustbricks_" + CrystalBlocks.CrystalColors.values()[i].getName());
        }

    }

}
