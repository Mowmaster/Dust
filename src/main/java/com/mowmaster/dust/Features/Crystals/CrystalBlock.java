package com.mowmaster.dust.Features.Crystals;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.world.level.block.AmethystBlock;

// A block of one element's crystal (the lining of an element geode), chiming like amethyst; drops itself, and is made
// from four of the element's crystals. Tinted with its element's colour (Client/ElementCrystalTint).
public class CrystalBlock extends AmethystBlock implements ElementCrystal
{
    private final DustElement element;

    public CrystalBlock(DustElement element, Properties properties)
    {
        super(properties);
        this.element = element;
    }

    @Override
    public DustElement element()
    {
        return element;
    }
}
