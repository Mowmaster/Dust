package com.mowmaster.dust.blocks;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;
import static net.minecraft.block.material.MapColor.COLORS;


public class BlockCharcoal extends BlockBasic
{
    public static Block charcoalRed;
    public static Block charcoalBlue;
    public static Block charcoalYellow;
    public static Block charcoalPurple;
    public static Block charcoalGreen;
    public static Block charcoalOrange;
    public static Block charcoalWhite;
    public static Block charcoalBlack;

    public BlockCharcoal(String unloc, String registryName)
    {
        super(Material.WOOD);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(3);
        this.setResistance(15);
        this.setLightOpacity(0);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(DUSTBLOCKSTABS);
    }


    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    public static void BlockCharcoalInit()
    {
        charcoalRed = new BlockCharcoal("blockcharcoalred","blockcharcoalred");
        charcoalBlue = new BlockCharcoal("blockcharcoalblue","blockcharcoalblue");
        charcoalYellow = new BlockCharcoal("blockcharcoalyellow","blockcharcoalyellow");
        charcoalPurple = new BlockCharcoal("blockcharcoalpurple","blockcharcoalpurple");
        charcoalGreen = new BlockCharcoal("blockcharcoalgreen","blockcharcoalgreen");
        charcoalOrange = new BlockCharcoal("blockcharcoalorange","blockcharcoalorange");
        charcoalWhite = new BlockCharcoal("blockcharcoalwhite","blockcharcoalwhite");
        charcoalBlack = new BlockCharcoal("blockcharcoalblack","blockcharcoalblack");
    }

    public static void BlockCharcoalRegister()
    {
        registerBlock(charcoalRed);
        registerBlock(charcoalBlue);
        registerBlock(charcoalYellow);
        registerBlock(charcoalPurple);
        registerBlock(charcoalGreen);
        registerBlock(charcoalOrange);
        registerBlock(charcoalWhite);
        registerBlock(charcoalBlack);
    }

    public static void BlockCharcoalRegisterRender()
    {
        registerRender(charcoalRed);
        registerRender(charcoalBlue);
        registerRender(charcoalYellow);
        registerRender(charcoalPurple);
        registerRender(charcoalGreen);
        registerRender(charcoalOrange);
        registerRender(charcoalWhite);
        registerRender(charcoalBlack);
    }
}
