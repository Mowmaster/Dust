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
    public static Block redstonefences;
    public static Block redbrickfences;
    public static Block redbrickfences2;
    public static Block redplankfences;
    public static Block bluestonefences;
    public static Block bluebrickfences;
    public static Block bluebrickfences2;
    public static Block blueplankfences;
    public static Block yellowstonefences;
    public static Block yellowbrickfences;
    public static Block yellowbrickfences2;
    public static Block yellowplankfences;
    public static Block purplestonefences;
    public static Block purplebrickfences;
    public static Block purplebrickfences2;
    public static Block purpleplankfences;
    public static Block orangestonefences;
    public static Block orangebrickfences;
    public static Block orangebrickfences2;
    public static Block orangeplankfences;
    public static Block greenstonefences;
    public static Block greenbrickfences;
    public static Block greenbrickfences2;
    public static Block greenplankfences;
    public static Block whitestonefences;
    public static Block whitebrickfences;
    public static Block whitebrickfences2;
    public static Block whiteplankfences;
    public static Block blackstonefences;
    public static Block blackbrickfences;
    public static Block blackbrickfences2;
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
        this.setSoundType(SoundType.STONE);
        this.useNeighborBrightness = true;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public static void Init()
    {
        redstonefences = new BlockDustFence("redstonefences", "ancient/redstonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        redbrickfences = new BlockDustFence("redbrickfences", "ancient/redbrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        redbrickfences2 = new BlockDustFence("redbrickfences2", "ancient/redbrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        redplankfences = new BlockDustFence("redplankfences", "ancient/redplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        bluestonefences = new BlockDustFence("bluestonefences", "ancient/bluestonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        bluebrickfences = new BlockDustFence("bluebrickfences", "ancient/bluebrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        bluebrickfences2 = new BlockDustFence("bluebrickfences2", "ancient/bluebrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        blueplankfences = new BlockDustFence("blueplankfences", "ancient/blueplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        yellowstonefences = new BlockDustFence("yellowstonefences", "ancient/yellowstonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        yellowbrickfences = new BlockDustFence("yellowbrickfences", "ancient/yellowbrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        yellowbrickfences2 = new BlockDustFence("yellowbrickfences2", "ancient/yellowbrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        yellowplankfences = new BlockDustFence("yellowplankfences", "ancient/yellowplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        purplestonefences = new BlockDustFence("purplestonefences", "ancient/purplestonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        purplebrickfences = new BlockDustFence("purplebrickfences", "ancient/purplebrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        purplebrickfences2 = new BlockDustFence("purplebrickfences2", "ancient/purplebrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        purpleplankfences = new BlockDustFence("purpleplankfences", "ancient/purpleplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        orangestonefences = new BlockDustFence("orangestonefences", "ancient/orangestonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        orangebrickfences = new BlockDustFence("orangebrickfences", "ancient/orangebrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        orangebrickfences2 = new BlockDustFence("orangebrickfences2", "ancient/orangebrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        orangeplankfences = new BlockDustFence("orangeplankfences", "ancient/orangeplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        greenstonefences = new BlockDustFence("greenstonefences", "ancient/greenstonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        greenbrickfences = new BlockDustFence("greenbrickfences", "ancient/greenbrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        greenbrickfences2 = new BlockDustFence("greenbrickfences2", "ancient/greenbrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        greenplankfences = new BlockDustFence("greenplankfences", "ancient/greenplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        whitestonefences = new BlockDustFence("whitestonefences", "ancient/whitestonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        whitebrickfences = new BlockDustFence("whitebrickfences", "ancient/whitebrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        whitebrickfences2 = new BlockDustFence("whitebrickfences2", "ancient/whitebrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        whiteplankfences = new BlockDustFence("whiteplankfences", "ancient/whiteplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        blackstonefences = new BlockDustFence("blackstonefences", "ancient/blackstonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        blackbrickfences = new BlockDustFence("blackbrickfences", "ancient/blackbrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        blackbrickfences2 = new BlockDustFence("blackbrickfences2", "ancient/blackbrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        blackplankfences = new BlockDustFence("blackplankfences", "ancient/blackplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
    }

    public static void Register()
    {
        registerBlock(redstonefences);
        registerBlock(redbrickfences);
        registerBlock(redbrickfences2);
        registerBlock(redplankfences);
        registerBlock(bluestonefences);
        registerBlock(bluebrickfences);
        registerBlock(bluebrickfences2);
        registerBlock(blueplankfences);
        registerBlock(yellowstonefences);
        registerBlock(yellowbrickfences);
        registerBlock(yellowbrickfences2);
        registerBlock(yellowplankfences);
        registerBlock(purplestonefences);
        registerBlock(purplebrickfences);
        registerBlock(purplebrickfences2);
        registerBlock(purpleplankfences);
        registerBlock(orangestonefences);
        registerBlock(orangebrickfences);
        registerBlock(orangebrickfences2);
        registerBlock(orangeplankfences);
        registerBlock(greenstonefences);
        registerBlock(greenbrickfences);
        registerBlock(greenbrickfences2);
        registerBlock(greenplankfences);
        registerBlock(whitestonefences);
        registerBlock(whitebrickfences);
        registerBlock(whitebrickfences2);
        registerBlock(whiteplankfences);
        registerBlock(blackstonefences);
        registerBlock(blackbrickfences);
        registerBlock(blackbrickfences2);
        registerBlock(blackplankfences);
    }

    public static void RegisterRender()
    {
        registerRenderAncient(redstonefences);
        registerRenderAncient(redbrickfences);
        registerRenderAncient(redbrickfences2);
        registerRenderAncient(redplankfences);
        registerRenderAncient(bluestonefences);
        registerRenderAncient(bluebrickfences);
        registerRenderAncient(bluebrickfences2);
        registerRenderAncient(blueplankfences);
        registerRenderAncient(yellowstonefences);
        registerRenderAncient(yellowbrickfences);
        registerRenderAncient(yellowbrickfences2);
        registerRenderAncient(yellowplankfences);
        registerRenderAncient(purplestonefences);
        registerRenderAncient(purplebrickfences);
        registerRenderAncient(purplebrickfences2);
        registerRenderAncient(purpleplankfences);
        registerRenderAncient(orangestonefences);
        registerRenderAncient(orangebrickfences);
        registerRenderAncient(orangebrickfences2);
        registerRenderAncient(orangeplankfences);
        registerRenderAncient(greenstonefences);
        registerRenderAncient(greenbrickfences);
        registerRenderAncient(greenbrickfences2);
        registerRenderAncient(greenplankfences);
        registerRenderAncient(whitestonefences);
        registerRenderAncient(whitebrickfences);
        registerRenderAncient(whitebrickfences2);
        registerRenderAncient(whiteplankfences);
        registerRenderAncient(blackstonefences);
        registerRenderAncient(blackbrickfences);
        registerRenderAncient(blackbrickfences2);
        registerRenderAncient(blackplankfences);
    }
}
