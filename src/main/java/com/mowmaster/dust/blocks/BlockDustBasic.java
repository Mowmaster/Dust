package com.mowmaster.dust.blocks;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockDustBasic extends BlockBasic
{
    public static Block redstone;
    public static Block redbricks;
    public static Block redbricks2;
    public static Block redplanks;
    public static Block bluestone;
    public static Block bluebricks;
    public static Block bluebricks2;
    public static Block blueplanks;
    public static Block yellowstone;
    public static Block yellowbricks;
    public static Block yellowbricks2;
    public static Block yellowplanks;
    public static Block purplestone;
    public static Block purplebricks;
    public static Block purplebricks2;
    public static Block purpleplanks;
    public static Block orangestone;
    public static Block orangebricks;
    public static Block orangebricks2;
    public static Block orangeplanks;
    public static Block greenstone;
    public static Block greenbricks;
    public static Block greenbricks2;
    public static Block greenplanks;
    public static Block whitestone;
    public static Block whitebricks;
    public static Block whitebricks2;
    public static Block whiteplanks;
    public static Block blackstone;
    public static Block blackbricks;
    public static Block blackbricks2;
    public static Block blackplanks;

    public static Block darksoilbase;

    public BlockDustBasic(String unloc, String registryName, Material material, SoundType soundType, int hardness, int resistance, int lightopacity)
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
    public Item getItemDropped(IBlockState state,Random random,int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    public static void Init()
    {
        redstone = new BlockDustBasic("redstone", "ancient/redstone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        redbricks = new BlockDustBasic("redbricks", "ancient/redbricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        redbricks2 = new BlockDustBasic("redbricks2", "ancient/redbricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        redplanks = new BlockDustBasic("redplanks", "ancient/redplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        bluestone = new BlockDustBasic("bluestone", "ancient/bluestone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        bluebricks = new BlockDustBasic("bluebricks", "ancient/bluebricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        bluebricks2 = new BlockDustBasic("bluebricks2", "ancient/bluebricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        blueplanks = new BlockDustBasic("blueplanks", "ancient/blueplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        yellowstone = new BlockDustBasic("yellowstone", "ancient/yellowstone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        yellowbricks = new BlockDustBasic("yellowbricks", "ancient/yellowbricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        yellowbricks2 = new BlockDustBasic("yellowbricks2", "ancient/yellowbricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        yellowplanks = new BlockDustBasic("yellowplanks", "ancient/yellowplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        purplestone = new BlockDustBasic("purplestone", "ancient/purplestone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        purplebricks = new BlockDustBasic("purplebricks", "ancient/purplebricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        purplebricks2 = new BlockDustBasic("purplebricks2", "ancient/purplebricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        purpleplanks = new BlockDustBasic("purpleplanks", "ancient/purpleplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        orangestone = new BlockDustBasic("orangestone", "ancient/orangestone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        orangebricks = new BlockDustBasic("orangebricks", "ancient/orangebricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        orangebricks2 = new BlockDustBasic("orangebricks2", "ancient/orangebricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        orangeplanks = new BlockDustBasic("orangeplanks", "ancient/orangeplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        greenstone = new BlockDustBasic("greenstone", "ancient/greenstone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        greenbricks = new BlockDustBasic("greenbricks", "ancient/greenbricks", Material.ROCK, SoundType.STONE, 30, 20, 10);
        greenbricks2 = new BlockDustBasic("greenbricks2", "ancient/greenbricks2", Material.ROCK, SoundType.STONE, 30, 20, 10);
        greenplanks = new BlockDustBasic("greenplanks", "ancient/greenplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        whitestone = new BlockDustBasic("whitestone", "ancient/whitestone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        whitebricks = new BlockDustBasic("whitebricks", "ancient/whitebricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        whitebricks2 = new BlockDustBasic("whitebricks2", "ancient/whitebricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        whiteplanks = new BlockDustBasic("whiteplanks", "ancient/whiteplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        blackstone = new BlockDustBasic("blackstone", "ancient/blackstone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        blackbricks = new BlockDustBasic("blackbricks", "ancient/blackbricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        blackbricks2 = new BlockDustBasic("blackbricks2", "ancient/blackbricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        blackplanks = new BlockDustBasic("blackplanks", "ancient/blackplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);

        darksoilbase = new BlockDustBasic("darksoilbase","darksoilbase",Material.GROUND,SoundType.GROUND,3,20,0);
    }

    public static void Register()
    {
        registerBlock(redstone);
        registerBlock(redbricks);
        registerBlock(redbricks2);
        registerBlock(redplanks);
        registerBlock(bluestone);
        registerBlock(bluebricks);
        registerBlock(bluebricks2);
        registerBlock(blueplanks);
        registerBlock(yellowstone);
        registerBlock(yellowbricks);
        registerBlock(yellowbricks2);
        registerBlock(yellowplanks);
        registerBlock(purplestone);
        registerBlock(purplebricks);
        registerBlock(purplebricks2);
        registerBlock(purpleplanks);
        registerBlock(orangestone);
        registerBlock(orangebricks);
        registerBlock(orangebricks2);
        registerBlock(orangeplanks);
        registerBlock(greenstone);
        registerBlock(greenbricks);
        registerBlock(greenbricks2);
        registerBlock(greenplanks);
        registerBlock(whitestone);
        registerBlock(whitebricks);
        registerBlock(whitebricks2);
        registerBlock(whiteplanks);
        registerBlock(blackstone);
        registerBlock(blackbricks);
        registerBlock(blackbricks2);
        registerBlock(blackplanks);

        registerBlock(darksoilbase);
    }

    public static void RegisterRender()
    {
        registerRenderAncient(redstone);
        registerRenderAncient(redbricks);
        registerRenderAncient(redbricks2);
        registerRenderAncient(redplanks);
        registerRenderAncient(bluestone);
        registerRenderAncient(bluebricks);
        registerRenderAncient(bluebricks2);
        registerRenderAncient(blueplanks);
        registerRenderAncient(yellowstone);
        registerRenderAncient(yellowbricks);
        registerRenderAncient(yellowbricks2);
        registerRenderAncient(yellowplanks);
        registerRenderAncient(purplestone);
        registerRenderAncient(purplebricks);
        registerRenderAncient(purplebricks2);
        registerRenderAncient(purpleplanks);
        registerRenderAncient(orangestone);
        registerRenderAncient(orangebricks);
        registerRenderAncient(orangebricks2);
        registerRenderAncient(orangeplanks);
        registerRenderAncient(greenstone);
        registerRenderAncient(greenbricks);
        registerRenderAncient(greenbricks2);
        registerRenderAncient(greenplanks);
        registerRenderAncient(whitestone);
        registerRenderAncient(whitebricks);
        registerRenderAncient(whitebricks2);
        registerRenderAncient(whiteplanks);
        registerRenderAncient(blackstone);
        registerRenderAncient(blackbricks);
        registerRenderAncient(blackbricks2);
        registerRenderAncient(blackplanks);

        registerRender(darksoilbase);
    }

}
