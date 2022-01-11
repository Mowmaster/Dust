package com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces;

import com.mowmaster.dust.Block.BlockEntities.Tier1.Tier1BaseBlock;
import com.mowmaster.dust.References.ColorReference;

public class DustFurnacesBaseBlock extends Tier1BaseBlock
{
    public DustFurnacesBaseBlock(Properties p_152915_) {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR));
    }
}
