package com.mowmaster.dust.world.biomes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.BiomeGenBase;

/**
 * Created by KingMowmaster on 2/7/2016.
 */
public class biomeCrystal extends BiomeGenBase {

    public biomeCrystal(int num){
        super(num);
    }

    public biomeCrystal setTopFillerBlock(IBlockState topBlock, IBlockState fillerBlock){

        this.fillerBlock = fillerBlock;
        this.topBlock = topBlock;
        return this;
    }

    public biomeCrystal setMinMaxHeight(float minHeight, float maxHeight){

        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        return this;
    }

    /*public biomeCrystal addDecoration(EnumBiomeDecorator decorator, int num){

        switch (decorator) {
            case FLOWERS:
                this.theBiomeDecorator.bigMushroomsPerChunk = 1;
            case GRASS:
                this.theBiomeDecorator.cactiPerChunk = 1;
            case TREE:
                this.theBiomeDecorator.treesPerChunk = 1;

            break;
            default: break;

            return this;
        }
    }


    public static enum EnumBiomeDecorator{
        FLOWERS,
        GRASS,
        TREE,
    }
    */
}
