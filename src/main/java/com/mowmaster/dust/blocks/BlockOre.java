package com.mowmaster.dust.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;


public class BlockOre extends Block
{
    public static String[] types = new String[] { "crystalore0", "crystalore1", "crystalore2", "crystalore3", "crystalore4", "crystalore5", "crystalore6", "crystalore7" };
    public static final PropertyEnum<BlockOre.EnumType> VARIANT = PropertyEnum.<BlockOre.EnumType> create("variant", BlockOre.EnumType.class);

    public BlockOre() {

        super(Material.ROCK);
        this.setUnlocalizedName("compressed_andesite");
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockOre.EnumType.CRYSTALORE0));
        this.setRegistryName("compressed_andesite");
        this.setHardness(1.5f);
        this.setHarvestLevel("pickaxe", 1);
        //this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public IBlockState getStateFromMeta (int meta) {

        return this.getDefaultState().withProperty(VARIANT, BlockOre.EnumType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState (IBlockState state) {

        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState () {

        return new BlockStateContainer(this, new IProperty[] { VARIANT });
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks (Item item, CreativeTabs tab, List<ItemStack> list) {

        for (int meta = 0; meta < 8; meta++)
            list.add(new ItemStack(item, 1, meta));
    }

    public enum EnumType implements IStringSerializable {

        CRYSTALORE0(0, "crystalore0"),
        CRYSTALORE1(1, "crystalore1"),
        CRYSTALORE2(2, "crystalore2"),
        CRYSTALORE3(3, "crystalore3"),
        CRYSTALORE4(4, "crystalore4"),
        CRYSTALORE5(5,"crystalore5"),
        CRYSTALORE6(6, "crystalore6"),
        CRYSTALORE7(7, "crystalore7");

        private static final BlockOre.EnumType[] META_LOOKUP = new BlockOre.EnumType[values().length];
        private final int meta;
        private final String name;

        EnumType(int meta, String name) {

            this.meta = meta;
            this.name = name;
        }

        public int getMetadata () {

            return this.meta;
        }

        @Override
        public String toString () {

            return this.name;
        }

        public static BlockOre.EnumType byMetadata (int meta) {

            if (meta < 0 || meta >= META_LOOKUP.length)
                meta = 0;

            return META_LOOKUP[meta];
        }

        @Override
        public String getName () {

            return this.name;
        }

        static {

            for (final BlockOre.EnumType type : values())
                META_LOOKUP[type.getMetadata()] = type;
        }
    }
}
