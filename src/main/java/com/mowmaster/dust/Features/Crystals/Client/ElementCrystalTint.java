package com.mowmaster.dust.Features.Crystals.Client;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

// An element's crystal blocks (Features/Crystals/ElementCrystals), one tint source per model tintindex (registered in
// DustClient): 0 white, 1 the element's colour (the crystal models' faces all use 1). The same in hand as in the world.
public class ElementCrystalTint implements BlockTintSource
{
    private final int color;

    private ElementCrystalTint(int color)
    {
        this.color = color;
    }

    public static List<BlockTintSource> of(DustElement element)
    {
        return List.of(new ElementCrystalTint(0xFFFFFFFF), new ElementCrystalTint(0xFF000000 | element.color()));
    }

    @Override
    public int color(BlockState state)
    {
        return color;
    }
}
