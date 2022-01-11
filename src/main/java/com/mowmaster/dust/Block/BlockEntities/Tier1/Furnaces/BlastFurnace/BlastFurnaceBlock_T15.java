package com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces.BlastFurnace;

import com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces.DustFurnacesBaseBlock;
import com.mowmaster.dust.References.ColorReference;

public class BlastFurnaceBlock_T15 extends DustFurnacesBaseBlock
{

    public BlastFurnaceBlock_T15(Properties p_152915_) {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR));
    }
}
