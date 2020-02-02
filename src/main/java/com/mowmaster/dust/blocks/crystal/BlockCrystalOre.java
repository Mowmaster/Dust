package com.mowmaster.dust.blocks.crystal;

import com.mowmaster.dust.blocks.blockbasics.BlockBasic;
import com.mowmaster.dust.blocks.item.IMetaBlockName;
import com.mowmaster.dust.blocks.item.ItemBlockOre;
import com.mowmaster.dust.enums.CrystalBlocks;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockCrystalOre extends BlockBasic implements IMetaBlockName
{
    public static final PropertyEnum COLORS = PropertyEnum.create("oreblock",CrystalBlocks.CrystalColors.class);
    public static Block ore;


    public BlockCrystalOre(String unloc, Material material, SoundType soundType, int hardness, int resistance, int lightopacity)
    {
        super(material);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unloc));
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLORS, CrystalBlocks.CrystalColors.RED));
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
        return ItemRegistry.crystal;
    }

    @Override
    public int damageDropped(IBlockState state) {

        return this.getMetaFromState(state);
    }

    @Override
    public int quantityDropped(Random random) {
        int count = 1+ random.nextInt(4);//guarenteed 1 but could be up to 5
        return count;
    }

    private void oreToCrystal(World worldIn, BlockPos pos, IBlockState crystalState)
    {
        if(!worldIn.isRemote) {
            worldIn.setBlockState(pos, crystalState);
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(ItemRegistry.dust, 1, 8)));
        }

    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote)
        {
            switch (this.getMetaFromState(state))
            {
                case 0:
                    oreToCrystal(worldIn,pos,BlockCrystal.redCrystalFive.getDefaultState());
                    break;
                case 1:
                    oreToCrystal(worldIn,pos,BlockCrystal.blueCrystalFive.getDefaultState());
                    break;
                case 2:
                    oreToCrystal(worldIn,pos,BlockCrystal.yellowCrystalFive.getDefaultState());
                    break;
                case 3:
                    oreToCrystal(worldIn,pos,BlockCrystal.purpleCrystalFive.getDefaultState());
                    break;
                case 4:
                    oreToCrystal(worldIn,pos,BlockCrystal.greenCrystalFive.getDefaultState());
                    break;
                case 5:
                    oreToCrystal(worldIn,pos,BlockCrystal.orangeCrystalFive.getDefaultState());
                    break;
                case 6:
                    oreToCrystal(worldIn,pos,BlockCrystal.whiteCrystalFive.getDefaultState());
                    break;
                case 7:
                    oreToCrystal(worldIn,pos,BlockCrystal.blackCrystalFive.getDefaultState());
                    break;
            }

        }
    }

    public static void Init()
    {
         ore = new BlockCrystalOre("blockore",Material.ROCK, SoundType.STONE, 3, 15, 10);
    }

    public static void Register()
    {
        registerBlock(ore, new ItemBlockOre(ore));
    }

    public static void RegisterRender()
    {
        for (int i = 0; i < CrystalBlocks.CrystalColors.values().length; i++)
        {
            registerRender(ore,i,"blockore_" + CrystalBlocks.CrystalColors.values()[i].getName());
        }
    }

    public static void bakeBlock()
    {
        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockCrystalOre.ore),
                new ResourceLocation(Reference.MODID, "blockore_red"),
                new ResourceLocation(Reference.MODID, "blockore_blue"),
                new ResourceLocation(Reference.MODID, "blockore_yellow"),
                new ResourceLocation(Reference.MODID, "blockore_purple"),
                new ResourceLocation(Reference.MODID, "blockore_orange"),
                new ResourceLocation(Reference.MODID, "blockore_green"),
                new ResourceLocation(Reference.MODID, "blockore_white"),
                new ResourceLocation(Reference.MODID, "blockore_black")
        );
    }

    private void spawnBasicCluster(World worldIn, BlockPos pos, int meta)
    {
        worldIn.setBlockState(pos, BlockCrystalCluster.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileCrystalCluster) {
            if(((TileCrystalCluster) tileentity).getCrystalCount()==0)
            {
                for(int i=0;i<5;i++)
                {
                    ((TileCrystalCluster) tileentity).addCrystal(meta);
                }
            }
        }
    }


}
