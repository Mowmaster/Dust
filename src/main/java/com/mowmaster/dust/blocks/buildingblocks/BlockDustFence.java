package com.mowmaster.dust.blocks.buildingblocks;

import com.mowmaster.dust.blocks.blockbasics.BlockBasicFence;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockDustFence extends BlockBasicFence
{
    public static Block redplankfences;
    public static Block blueplankfences;
    public static Block yellowplankfences;
    public static Block purpleplankfences;
    public static Block greenplankfences;
    public static Block orangeplankfences;
    public static Block whiteplankfences;
    public static Block blackplankfences;

    public BlockDustFence(String unloc, String registryName, Material material, MapColor color, SoundType soundType, int hardness, int resistance, int lightopacity)
    {
        super (material, color);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setSoundType(soundType);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightOpacity(lightopacity);
        setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)));
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.setSoundType(SoundType.WOOD);
        this.useNeighborBrightness = true;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public static void Init()
    {
        redplankfences = new BlockDustFence("redplankfences", "ancient/redplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        blueplankfences = new BlockDustFence("blueplankfences", "ancient/blueplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        yellowplankfences = new BlockDustFence("yellowplankfences", "ancient/yellowplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        purpleplankfences = new BlockDustFence("purpleplankfences", "ancient/purpleplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        greenplankfences = new BlockDustFence("greenplankfences", "ancient/greenplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        orangeplankfences = new BlockDustFence("orangeplankfences", "ancient/orangeplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        whiteplankfences = new BlockDustFence("whiteplankfences", "ancient/whiteplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        blackplankfences = new BlockDustFence("blackplankfences", "ancient/blackplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
    }

    public static void Register()
    {
        registerBlock(redplankfences);
        registerBlock(blueplankfences);
        registerBlock(yellowplankfences);
        registerBlock(purpleplankfences);
        registerBlock(greenplankfences);
        registerBlock(orangeplankfences);
        registerBlock(whiteplankfences);
        registerBlock(blackplankfences);
    }

    public static void RegisterRender()
    {
        registerRenderAncient(redplankfences);
        registerRenderAncient(blueplankfences);
        registerRenderAncient(yellowplankfences);
        registerRenderAncient(purpleplankfences);
        registerRenderAncient(orangeplankfences);
        registerRenderAncient(greenplankfences);
        registerRenderAncient(whiteplankfences);
        registerRenderAncient(blackplankfences);
    }
}
