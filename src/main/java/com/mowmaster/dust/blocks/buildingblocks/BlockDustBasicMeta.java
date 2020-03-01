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
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockDustBasicMeta extends BlockBasic implements IMetaBlockName
{
    public static final PropertyEnum COLORS = PropertyEnum.create("basicblocks",CrystalBlocks.CrystalColors.class);
    public static Block dustplanks;
    public static Block duststone;
    public static Block dustbrick;
    public static Block dustbricks;


    public BlockDustBasicMeta(String unloc, Material material, SoundType soundType, int hardness, int resistance, int lightopacity)
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

    public String getSpecialName(ItemStack stack)
    {
        return CrystalBlocks.CrystalColors.values()[stack.getItemDamage()].getName();
    }

    public int damageDropped(IBlockState state)
    {
        return ((CrystalBlocks.CrystalColors)state.getValue(COLORS)).getID();
    }

    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (CrystalBlocks.CrystalColors enumdyecolor : CrystalBlocks.CrystalColors.values())
        {
            items.add(new ItemStack(this, 1, enumdyecolor.getID()));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(COLORS,CrystalBlocks.CrystalColors.values()[meta]);
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((CrystalBlocks.CrystalColors)state.getValue(COLORS)).getID();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {COLORS});
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

    public static void bakeBlock()
    {
        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockDustBasicMeta.dustplanks),
                new ResourceLocation(Reference.MODID, "dustplanks_red"),
                new ResourceLocation(Reference.MODID, "dustplanks_blue"),
                new ResourceLocation(Reference.MODID, "dustplanks_yellow"),
                new ResourceLocation(Reference.MODID, "dustplanks_purple"),
                new ResourceLocation(Reference.MODID, "dustplanks_orange"),
                new ResourceLocation(Reference.MODID, "dustplanks_green"),
                new ResourceLocation(Reference.MODID, "dustplanks_white"),
                new ResourceLocation(Reference.MODID, "dustplanks_black")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockDustBasicMeta.duststone),
                new ResourceLocation(Reference.MODID, "duststone_red"),
                new ResourceLocation(Reference.MODID, "duststone_blue"),
                new ResourceLocation(Reference.MODID, "duststone_yellow"),
                new ResourceLocation(Reference.MODID, "duststone_purple"),
                new ResourceLocation(Reference.MODID, "duststone_orange"),
                new ResourceLocation(Reference.MODID, "duststone_green"),
                new ResourceLocation(Reference.MODID, "duststone_white"),
                new ResourceLocation(Reference.MODID, "duststone_black")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockDustBasicMeta.dustbrick),
                new ResourceLocation(Reference.MODID, "dustbrick_red"),
                new ResourceLocation(Reference.MODID, "dustbrick_blue"),
                new ResourceLocation(Reference.MODID, "dustbrick_yellow"),
                new ResourceLocation(Reference.MODID, "dustbrick_purple"),
                new ResourceLocation(Reference.MODID, "dustbrick_orange"),
                new ResourceLocation(Reference.MODID, "dustbrick_green"),
                new ResourceLocation(Reference.MODID, "dustbrick_white"),
                new ResourceLocation(Reference.MODID, "dustbrick_black")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockDustBasicMeta.dustbricks),
                new ResourceLocation(Reference.MODID, "dustbricks_red"),
                new ResourceLocation(Reference.MODID, "dustbricks_blue"),
                new ResourceLocation(Reference.MODID, "dustbricks_yellow"),
                new ResourceLocation(Reference.MODID, "dustbricks_purple"),
                new ResourceLocation(Reference.MODID, "dustbricks_orange"),
                new ResourceLocation(Reference.MODID, "dustbricks_green"),
                new ResourceLocation(Reference.MODID, "dustbricks_white"),
                new ResourceLocation(Reference.MODID, "dustbricks_black")
        );
    }

}
