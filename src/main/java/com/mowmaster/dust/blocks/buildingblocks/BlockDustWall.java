package com.mowmaster.dust.blocks.buildingblocks;

import com.mowmaster.dust.blocks.blockbasics.BlockBasicWall;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;

public class BlockDustWall extends BlockBasicWall
{
    public static Block redstonewalls;
    public static Block redbrickwalls;
    public static Block redbrickwalls2;
    public static Block bluestonewalls;
    public static Block bluebrickwalls;
    public static Block bluebrickwalls2;
    public static Block yellowstonewalls;
    public static Block yellowbrickwalls;
    public static Block yellowbrickwalls2;
    public static Block purplestonewalls;
    public static Block purplebrickwalls;
    public static Block purplebrickwalls2;
    public static Block orangestonewalls;
    public static Block orangebrickwalls;
    public static Block orangebrickwalls2;
    public static Block greenstonewalls;
    public static Block greenbrickwalls;
    public static Block greenbrickwalls2;
    public static Block whitestonewalls;
    public static Block whitebrickwalls;
    public static Block whitebrickwalls2;
    public static Block blackstonewalls;
    public static Block blackbrickwalls;
    public static Block blackbrickwalls2;

    public BlockDustWall(String unloc, String registryName, Block block, SoundType soundType, int hardness, int resistance, int lightopacity)
    {
        super (block);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightOpacity(lightopacity);
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.setSoundType(soundType);
        this.useNeighborBrightness = true;
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> list) {
        list.add(new ItemStack(this));
    }

    public static void Init()
    {
        redstonewalls = new BlockDustWall("redstonewalls", "ancient/redstonewalls", BlockDustBasicMeta.duststone, SoundType.STONE, 3, 20, 10);
        redbrickwalls = new BlockDustWall("redbrickwalls", "ancient/redbrickwalls", BlockDustBasicMeta.dustbrick, SoundType.STONE, 3, 20, 10);
        redbrickwalls2 = new BlockDustWall("redbrickwalls2", "ancient/redbrickwalls2", BlockDustBasicMeta.dustbricks, SoundType.STONE, 3, 20, 10);
        bluestonewalls = new BlockDustWall("bluestonewalls", "ancient/bluestonewalls", BlockDustBasicMeta.duststone, SoundType.STONE, 3, 20, 10);
        bluebrickwalls = new BlockDustWall("bluebrickwalls", "ancient/bluebrickwalls", BlockDustBasicMeta.dustbrick, SoundType.STONE, 3, 20, 10);
        bluebrickwalls2 = new BlockDustWall("bluebrickwalls2", "ancient/bluebrickwalls2", BlockDustBasicMeta.dustbricks, SoundType.STONE, 3, 20, 10);
        yellowstonewalls = new BlockDustWall("yellowstonewalls", "ancient/yellowstonewalls", BlockDustBasicMeta.duststone, SoundType.STONE, 3, 20, 10);
        yellowbrickwalls = new BlockDustWall("yellowbrickwalls", "ancient/yellowbrickwalls", BlockDustBasicMeta.dustbrick, SoundType.STONE, 3, 20, 10);
        yellowbrickwalls2 = new BlockDustWall("yellowbrickwalls2", "ancient/yellowbrickwalls2",BlockDustBasicMeta.dustbricks, SoundType.STONE, 3, 20, 10);
        purplestonewalls = new BlockDustWall("purplestonewalls", "ancient/purplestonewalls", BlockDustBasicMeta.duststone, SoundType.STONE, 3, 20, 10);
        purplebrickwalls = new BlockDustWall("purplebrickwalls", "ancient/purplebrickwalls", BlockDustBasicMeta.dustbrick, SoundType.STONE, 3, 20, 10);
        purplebrickwalls2 = new BlockDustWall("purplebrickwalls2", "ancient/purplebrickwalls2", BlockDustBasicMeta.dustbricks, SoundType.STONE, 3, 20, 10);
        orangestonewalls = new BlockDustWall("orangestonewalls", "ancient/orangestonewalls", BlockDustBasicMeta.duststone, SoundType.STONE, 3, 20, 10);
        orangebrickwalls = new BlockDustWall("orangebrickwalls", "ancient/orangebrickwalls", BlockDustBasicMeta.dustbrick, SoundType.STONE, 3, 20, 10);
        orangebrickwalls2 = new BlockDustWall("orangebrickwalls2", "ancient/orangebrickwalls2", BlockDustBasicMeta.dustbricks, SoundType.STONE, 3, 20, 10);
        greenstonewalls = new BlockDustWall("greenstonewalls", "ancient/greenstonewalls", BlockDustBasicMeta.duststone, SoundType.STONE, 3, 20, 10);
        greenbrickwalls = new BlockDustWall("greenbrickwalls", "ancient/greenbrickwalls", BlockDustBasicMeta.dustbrick, SoundType.STONE, 3, 20, 10);
        greenbrickwalls2 = new BlockDustWall("greenbrickwalls2", "ancient/greenbrickwalls2", BlockDustBasicMeta.dustbricks, SoundType.STONE, 3, 20, 10);
        whitestonewalls = new BlockDustWall("whitestonewalls", "ancient/whitestonewalls", BlockDustBasicMeta.duststone, SoundType.STONE, 3, 20, 10);
        whitebrickwalls = new BlockDustWall("whitebrickwalls", "ancient/whitebrickwalls", BlockDustBasicMeta.dustbrick, SoundType.STONE, 3, 20, 10);
        whitebrickwalls2 = new BlockDustWall("whitebrickwalls2", "ancient/whitebrickwalls2", BlockDustBasicMeta.dustbricks, SoundType.STONE, 3, 20, 10);
        blackstonewalls = new BlockDustWall("blackstonewalls", "ancient/blackstonewalls", BlockDustBasicMeta.duststone, SoundType.STONE, 3, 20, 10);
        blackbrickwalls = new BlockDustWall("blackbrickwalls", "ancient/blackbrickwalls", BlockDustBasicMeta.dustbrick, SoundType.STONE, 3, 20, 10);
        blackbrickwalls2 = new BlockDustWall("blackbrickwalls2", "ancient/blackbrickwalls2", BlockDustBasicMeta.dustbricks, SoundType.STONE, 3, 20, 10);
    }

    public static void Register()
    {
        registerBlock(redstonewalls);
        registerBlock(redbrickwalls);
        registerBlock(redbrickwalls2);
        registerBlock(bluestonewalls);
        registerBlock(bluebrickwalls);
        registerBlock(bluebrickwalls2);
        registerBlock(yellowstonewalls);
        registerBlock(yellowbrickwalls);
        registerBlock(yellowbrickwalls2);
        registerBlock(purplestonewalls);
        registerBlock(purplebrickwalls);
        registerBlock(purplebrickwalls2);
        registerBlock(orangestonewalls);
        registerBlock(orangebrickwalls);
        registerBlock(orangebrickwalls2);
        registerBlock(greenstonewalls);
        registerBlock(greenbrickwalls);
        registerBlock(greenbrickwalls2);
        registerBlock(whitestonewalls);
        registerBlock(whitebrickwalls);
        registerBlock(whitebrickwalls2);
        registerBlock(blackstonewalls);
        registerBlock(blackbrickwalls);
        registerBlock(blackbrickwalls2);
    }

    public static void RegisterRender()
    {
        registerRenderAncient(redstonewalls);
        registerRenderAncient(redbrickwalls);
        registerRenderAncient(redbrickwalls2);
        registerRenderAncient(bluestonewalls);
        registerRenderAncient(bluebrickwalls);
        registerRenderAncient(bluebrickwalls2);
        registerRenderAncient(yellowstonewalls);
        registerRenderAncient(yellowbrickwalls);
        registerRenderAncient(yellowbrickwalls2);
        registerRenderAncient(purplestonewalls);
        registerRenderAncient(purplebrickwalls);
        registerRenderAncient(purplebrickwalls2);
        registerRenderAncient(orangestonewalls);
        registerRenderAncient(orangebrickwalls);
        registerRenderAncient(orangebrickwalls2);
        registerRenderAncient(greenstonewalls);
        registerRenderAncient(greenbrickwalls);
        registerRenderAncient(greenbrickwalls2);
        registerRenderAncient(whitestonewalls);
        registerRenderAncient(whitebrickwalls);
        registerRenderAncient(whitebrickwalls2);
        registerRenderAncient(blackstonewalls);
        registerRenderAncient(blackbrickwalls);
        registerRenderAncient(blackbrickwalls2);
    }
}
