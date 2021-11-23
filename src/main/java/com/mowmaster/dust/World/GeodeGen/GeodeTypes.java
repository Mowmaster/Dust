/*
package com.mowmaster.dust.World.GeodeGen;

import com.mowmaster.dust.Blocks.GeneratedBlocks.BaseCrystalNodeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;

public class GeodeTypes {
    */
/*000(BaseCrystalNodeBlock.BLK_BLOCK_INERTNODE, Lazy.of(BlockInit.DEEPSLATE_RUBY_ORE), OreGenConfig.ruby_min_vein_size.get(), OreGenConfig.ruby_max_vein_size.get(), OreGenConfig.ruby_min_height.get(), OreGenConfig.ruby_max_height.get(), OreGenConfig.generate_ruby.get()),
    003(BaseCrystalNodeBlock.BLK_BLOCK_INERTNODE, Lazy.of(BlockInit.DEEPSLATE_SAPPHIRE_ORE), OreGenConfig.sapphire_min_vein_size.get(), OreGenConfig.sapphire_max_vein_size.get(), OreGenConfig.sapphire_min_height.get(), OreGenConfig.sapphire_max_height.get(), OreGenConfig.generate_sapphire.get()),
    030(BaseCrystalNodeBlock.BLK_BLOCK_INERTNODE, Lazy.of(BlockInit.DEEPSLATE_TOPAZ_ORE), OreGenConfig.topaz_min_vein_size.get(), OreGenConfig.topaz_max_vein_size.get(), OreGenConfig.topaz_min_height.get(), OreGenConfig.topaz_max_height.get(), OreGenConfig.generate_topaz.get()),
    300(BaseCrystalNodeBlock.BLK_BLOCK_INERTNODE, Lazy.of(BlockInit.DEEPSLATE_AMETHYST_ORE), OreGenConfig.amethyst_min_vein_size.get(), OreGenConfig.amethyst_max_vein_size.get(), OreGenConfig.amethyst_min_height.get(), OreGenConfig.amethyst_max_height.get(), OreGenConfig.generate_amethyst.get()),
    *//*

    Type333(Lazy.of(BaseCrystalNodeBlock.BLK_BLOCK_INERTNODE), Lazy.of(BaseCrystalNodeBlock.BLK_BLOCK_INERTNODE), 1, 5, 0, 64, true);

    private final Lazy<Block> block;
    private final Lazy<Block> deepslateBlock;
    private final int minVeinSize;
    private final int maxVeinSize;
    private final int minHeight;
    private final int maxHeight;
    private final boolean generateOreToggle;

    GeodeTypes(Lazy<Block> block, Lazy<Block> deepslateBlock, int minVeinSize, int maxVeinSize, int minHeight, int maxHeight, boolean generateOreToggle) {
        this.block = block;
        this.deepslateBlock = deepslateBlock;
        this.minVeinSize = minVeinSize;
        this.maxVeinSize = maxVeinSize;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.generateOreToggle = generateOreToggle;
    }

    public Lazy<Block> getBlock() {
        return block;
    }

    public Lazy<Block> getDeepslateBlock() {
        return deepslateBlock;
    }

    public int getMinVeinSize() {
        return minVeinSize;
    }

    public int getMaxVeinSize() {
        return maxVeinSize;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public boolean getGenerateOreToggle() {
        return generateOreToggle;
    }

    public static GeodeTypes get(Block block) {
        for (GeodeTypes ore : values()) {
            if (block == ore.block) {
                return ore;
            }
        }
        return null;
    }
}
*/
