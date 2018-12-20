package com.mowmaster.dust.blocks.buildingblocks;

import com.mowmaster.dust.blocks.blockbasics.BlockBasicStair;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;




public class BlockDustStair extends BlockBasicStair
{
    public static Block redstonestairs;
    public static Block redbrickstairs;
    public static Block redbrickstairs2;
    public static Block redplankstairs;
    public static Block bluestonestairs;
    public static Block bluebrickstairs;
    public static Block bluebrickstairs2;
    public static Block blueplankstairs;
    public static Block yellowstonestairs;
    public static Block yellowbrickstairs;
    public static Block yellowbrickstairs2;
    public static Block yellowplankstairs;
    public static Block purplestonestairs;
    public static Block purplebrickstairs;
    public static Block purplebrickstairs2;
    public static Block purpleplankstairs;
    public static Block orangestonestairs;
    public static Block orangebrickstairs;
    public static Block orangebrickstairs2;
    public static Block orangeplankstairs;
    public static Block greenstonestairs;
    public static Block greenbrickstairs;
    public static Block greenbrickstairs2;
    public static Block greenplankstairs;
    public static Block whitestonestairs;
    public static Block whitebrickstairs;
    public static Block whitebrickstairs2;
    public static Block whiteplankstairs;
    public static Block blackstonestairs;
    public static Block blackbrickstairs;
    public static Block blackbrickstairs2;
    public static Block blackplankstairs;

    public BlockDustStair(String unloc, String registryName, Block block, SoundType soundType, int hardness, int resistance, int lightopacity)
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
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public static void Init()
    {
        redstonestairs = new BlockDustStair("redstonestairs", "ancient/redstonestairs", BlockDustBasic.redstone, SoundType.STONE, 3, 20, 10);
        redbrickstairs = new BlockDustStair("redbrickstairs", "ancient/redbrickstairs", BlockDustBasic.redbricks, SoundType.STONE, 3, 20, 10);
        redbrickstairs2 = new BlockDustStair("redbrickstairs2", "ancient/redbrickstairs2", BlockDustBasic.redbricks2, SoundType.STONE, 3, 20, 10);
        redplankstairs = new BlockDustStair("redplankstairs", "ancient/redplankstairs", BlockDustBasic.redplanks, SoundType.WOOD, 2, 10, 10);
        bluestonestairs = new BlockDustStair("bluestonestairs", "ancient/bluestonestairs", BlockDustBasic.bluestone, SoundType.STONE, 3, 20, 10);
        bluebrickstairs = new BlockDustStair("bluebrickstairs", "ancient/bluebrickstairs", BlockDustBasic.bluebricks, SoundType.STONE, 3, 20, 10);
        bluebrickstairs2 = new BlockDustStair("bluebrickstairs2", "ancient/bluebrickstairs2", BlockDustBasic.bluebricks2, SoundType.STONE, 3, 20, 10);
        blueplankstairs = new BlockDustStair("blueplankstairs", "ancient/blueplankstairs", BlockDustBasic.blueplanks, SoundType.WOOD, 2, 10, 10);
        yellowstonestairs = new BlockDustStair("yellowstonestairs", "ancient/yellowstonestairs", BlockDustBasic.yellowstone, SoundType.STONE, 3, 20, 10);
        yellowbrickstairs = new BlockDustStair("yellowbrickstairs", "ancient/yellowbrickstairs", BlockDustBasic.yellowbricks, SoundType.STONE, 3, 20, 10);
        yellowbrickstairs2 = new BlockDustStair("yellowbrickstairs2", "ancient/yellowbrickstairs2", BlockDustBasic.yellowbricks2, SoundType.STONE, 3, 20, 10);
        yellowplankstairs = new BlockDustStair("yellowplankstairs", "ancient/yellowplankstairs", BlockDustBasic.yellowplanks, SoundType.WOOD, 2, 10, 10);
        purplestonestairs = new BlockDustStair("purplestonestairs", "ancient/purplestonestairs", BlockDustBasic.purplestone, SoundType.STONE, 3, 20, 10);
        purplebrickstairs = new BlockDustStair("purplebrickstairs", "ancient/purplebrickstairs", BlockDustBasic.purplebricks, SoundType.STONE, 3, 20, 10);
        purplebrickstairs2 = new BlockDustStair("purplebrickstairs2", "ancient/purplebrickstairs2", BlockDustBasic.purplebricks2, SoundType.STONE, 3, 20, 10);
        purpleplankstairs = new BlockDustStair("purpleplankstairs", "ancient/purpleplankstairs", BlockDustBasic.purpleplanks, SoundType.WOOD, 2, 10, 10);
        orangestonestairs = new BlockDustStair("orangestonestairs", "ancient/orangestonestairs", BlockDustBasic.orangestone, SoundType.STONE, 3, 20, 10);
        orangebrickstairs = new BlockDustStair("orangebrickstairs", "ancient/orangebrickstairs", BlockDustBasic.orangebricks, SoundType.STONE, 3, 20, 10);
        orangebrickstairs2 = new BlockDustStair("orangebrickstairs2", "ancient/orangebrickstairs2", BlockDustBasic.orangebricks2, SoundType.STONE, 3, 20, 10);
        orangeplankstairs = new BlockDustStair("orangeplankstairs", "ancient/orangeplankstairs", BlockDustBasic.orangeplanks, SoundType.WOOD, 2, 10, 10);
        greenstonestairs = new BlockDustStair("greenstonestairs", "ancient/greenstonestairs", BlockDustBasic.greenstone, SoundType.STONE, 3, 20, 10);
        greenbrickstairs = new BlockDustStair("greenbrickstairs", "ancient/greenbrickstairs", BlockDustBasic.greenbricks, SoundType.STONE, 3, 20, 10);
        greenbrickstairs2 = new BlockDustStair("greenbrickstairs2", "ancient/greenbrickstairs2", BlockDustBasic.greenbricks2, SoundType.STONE, 3, 20, 10);
        greenplankstairs = new BlockDustStair("greenplankstairs", "ancient/greenplankstairs", BlockDustBasic.greenplanks, SoundType.WOOD, 2, 10, 10);
        whitestonestairs = new BlockDustStair("whitestonestairs", "ancient/whitestonestairs", BlockDustBasic.whitestone, SoundType.STONE, 3, 20, 10);
        whitebrickstairs = new BlockDustStair("whitebrickstairs", "ancient/whitebrickstairs", BlockDustBasic.whitebricks, SoundType.STONE, 3, 20, 10);
        whitebrickstairs2 = new BlockDustStair("whitebrickstairs2", "ancient/whitebrickstairs2", BlockDustBasic.whitebricks2, SoundType.STONE, 3, 20, 10);
        whiteplankstairs = new BlockDustStair("whiteplankstairs", "ancient/whiteplankstairs", BlockDustBasic.whiteplanks, SoundType.WOOD, 2, 10, 10);
        blackstonestairs = new BlockDustStair("blackstonestairs", "ancient/blackstonestairs", BlockDustBasic.blackstone, SoundType.STONE, 3, 20, 10);
        blackbrickstairs = new BlockDustStair("blackbrickstairs", "ancient/blackbrickstairs", BlockDustBasic.blackbricks, SoundType.STONE, 3, 20, 10);
        blackbrickstairs2 = new BlockDustStair("blackbrickstairs2", "ancient/blackbrickstairs2", BlockDustBasic.blackbricks2, SoundType.STONE, 3, 20, 10);
        blackplankstairs = new BlockDustStair("blackplankstairs", "ancient/blackplankstairs", BlockDustBasic.blackplanks, SoundType.WOOD, 2, 10, 10);
    }

    public static void Register()
    {
        registerBlock(redstonestairs);
        registerBlock(redbrickstairs);
        registerBlock(redbrickstairs2);
        registerBlock(redplankstairs);
        registerBlock(bluestonestairs);
        registerBlock(bluebrickstairs);
        registerBlock(bluebrickstairs2);
        registerBlock(blueplankstairs);
        registerBlock(yellowstonestairs);
        registerBlock(yellowbrickstairs);
        registerBlock(yellowbrickstairs2);
        registerBlock(yellowplankstairs);
        registerBlock(purplestonestairs);
        registerBlock(purplebrickstairs);
        registerBlock(purplebrickstairs2);
        registerBlock(purpleplankstairs);
        registerBlock(orangestonestairs);
        registerBlock(orangebrickstairs);
        registerBlock(orangebrickstairs2);
        registerBlock(orangeplankstairs);
        registerBlock(greenstonestairs);
        registerBlock(greenbrickstairs);
        registerBlock(greenbrickstairs2);
        registerBlock(greenplankstairs);
        registerBlock(whitestonestairs);
        registerBlock(whitebrickstairs);
        registerBlock(whitebrickstairs2);
        registerBlock(whiteplankstairs);
        registerBlock(blackstonestairs);
        registerBlock(blackbrickstairs);
        registerBlock(blackbrickstairs2);
        registerBlock(blackplankstairs);
    }

    public static void RegisterRender()
    {
        registerRenderAncient(redstonestairs);
        registerRenderAncient(redbrickstairs);
        registerRenderAncient(redbrickstairs2);
        registerRenderAncient(redplankstairs);
        registerRenderAncient(bluestonestairs);
        registerRenderAncient(bluebrickstairs);
        registerRenderAncient(bluebrickstairs2);
        registerRenderAncient(blueplankstairs);
        registerRenderAncient(yellowstonestairs);
        registerRenderAncient(yellowbrickstairs);
        registerRenderAncient(yellowbrickstairs2);
        registerRenderAncient(yellowplankstairs);
        registerRenderAncient(purplestonestairs);
        registerRenderAncient(purplebrickstairs);
        registerRenderAncient(purplebrickstairs2);
        registerRenderAncient(purpleplankstairs);
        registerRenderAncient(orangestonestairs);
        registerRenderAncient(orangebrickstairs);
        registerRenderAncient(orangebrickstairs2);
        registerRenderAncient(orangeplankstairs);
        registerRenderAncient(greenstonestairs);
        registerRenderAncient(greenbrickstairs);
        registerRenderAncient(greenbrickstairs2);
        registerRenderAncient(greenplankstairs);
        registerRenderAncient(whitestonestairs);
        registerRenderAncient(whitebrickstairs);
        registerRenderAncient(whitebrickstairs2);
        registerRenderAncient(whiteplankstairs);
        registerRenderAncient(blackstonestairs);
        registerRenderAncient(blackbrickstairs);
        registerRenderAncient(blackbrickstairs2);
        registerRenderAncient(blackplankstairs);
    }
}
