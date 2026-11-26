package com.mowmaster.dust.Features.Labyrinth;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;

// The labyrinth's own blocks (no items; nothing drops; unbreakable):
//   dust:labyrinth_seal   the wall of light that seals a doorway
//   dust:labyrinth_rune   a puzzle room's rune, lit by a spell of its element
public final class LabyrinthBlocks
{
    public static final DeferredBlock<LabyrinthSealBlock> SEAL = DustBlockRegistry.DUSTBLOCKS.registerBlock("labyrinth_seal",
            properties -> new LabyrinthSealBlock(properties.mapColor(MapColor.NONE).sound(SoundType.GLASS).strength(-1.0F, 3600000.0F)
                    .noLootTable().noOcclusion().lightLevel(state -> 6)));
    public static final DeferredBlock<LabyrinthRuneBlock> RUNE = DustBlockRegistry.DUSTBLOCKS.registerBlock("labyrinth_rune",
            properties -> new LabyrinthRuneBlock(properties.mapColor(MapColor.STONE).sound(SoundType.DEEPSLATE_TILES).strength(-1.0F, 3600000.0F)
                    .noLootTable().lightLevel(state -> state.getValue(LabyrinthRuneBlock.LIT) ? 12 : 3)));

    private LabyrinthBlocks() {}

    // Loads the class (and so registers the blocks) from Labyrinth.init
    public static void init() {}
}
