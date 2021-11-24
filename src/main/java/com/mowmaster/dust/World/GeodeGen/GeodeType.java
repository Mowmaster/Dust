package com.mowmaster.dust.World.GeodeGen;

import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;

public enum GeodeType
{
    CRYSTAL_BLOCK(Lazy.of(DeferredRegisterBlocks.CRYSTAL_BLOCK), 8, 25, 50, 1),
    CRYSTAL_NODE(Lazy.of(DeferredRegisterBlocks.CRYSTAL_NODE), 1, 0, 50, 1);

    private final Lazy<Block> block;
    private final int maxVeinSize;
    private final int minHeight;
    private final int maxHeight;
    private final int veinsPerChunk;

    GeodeType(Lazy<Block> block, int maxVeinSize, int minHeight, int maxHeight, int veinsPerChunk) {
        this.block = block;
        this.maxVeinSize = maxVeinSize;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.veinsPerChunk = veinsPerChunk;
    }


    public int getVeinsPerChunk() {
        return veinsPerChunk;
    }

    public Lazy<Block> getBlock() {
        return block;
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

    public static GeodeType get(Block block) {
        for (GeodeType geodeType : values()) {
            if(block == geodeType.block) {
                return geodeType;
            }
        }
        return null;
    }
}
