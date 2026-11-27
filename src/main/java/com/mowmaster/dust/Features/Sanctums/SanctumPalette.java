package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Crystals.ElementCrystals;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Trees.ElementalWood;
import com.mowmaster.dust.Features.Trees.ElementalWoods;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;
import java.util.Map;

// The blocks a sanctum (its template's stand-ins: SanctumProcessor) and a labyrinth (Features/Labyrinth) of one element
// are built from: walls (with a second kind mixed in), floors, the element's crystal blocks as accents (inlays, the
// portal's frame), its wood's logs as pillars, and a light. Vanilla blocks by id, so a renamed field (26.3's copper)
// doesn't matter.
public record SanctumPalette(BlockState wall, BlockState wallAlt, BlockState floor, BlockState accent, BlockState pillar, BlockState light)
{
    private static final Map<ElementalArmorSet, SanctumPalette> PALETTES = new EnumMap<>(ElementalArmorSet.class);

    public static synchronized SanctumPalette of(ElementalArmorSet set)
    {
        return PALETTES.computeIfAbsent(set, SanctumPalette::make);
    }

    // A wall block: mostly the wall, one in five the other kind
    public BlockState wall(RandomSource random)
    {
        return random.nextInt(5) == 0 ? wallAlt : wall;
    }

    private static SanctumPalette make(ElementalArmorSet set)
    {
        return switch (set) {
            case PYREBORN -> of(set, "nether_bricks", "red_nether_bricks", "polished_blackstone_bricks", "shroomlight");
            case TIDEWOVEN -> of(set, "prismarine_bricks", "prismarine", "dark_prismarine", "sea_lantern");
            case ROOTBOUND -> of(set, "mossy_stone_bricks", "stone_bricks", "mossy_cobblestone", "verdant_froglight");
            case LUMEN -> of(set, "quartz_bricks", "calcite", "smooth_quartz", "pearlescent_froglight");
            case RIFTBORN -> of(set, "purpur_block", "obsidian", "obsidian", "crying_obsidian");
            case SPARKWRIGHT -> of(set, "waxed_cut_copper", "waxed_copper_block", "polished_tuff", "glowstone");
            case BLASTWARD -> of(set, "polished_blackstone_bricks", "cracked_polished_blackstone_bricks", "polished_basalt", "glowstone");
            case RIMEFROST -> of(set, "packed_ice", "blue_ice", "polished_diorite", "sea_lantern");
            case DEEPWATCH -> of(set, "deepslate_tiles", "deepslate_bricks", "polished_deepslate", "sea_lantern");
            case BASTION -> of(set, "stone_bricks", "cracked_stone_bricks", "polished_andesite", "glowstone");
            case MIREWALKER -> of(set, "mud_bricks", "packed_mud", "packed_mud", "ochre_froglight");
            case ZEPHYR -> of(set, "calcite", "diorite", "smooth_quartz", "pearlescent_froglight");
            case VERDANT -> of(set, "mossy_cobblestone", "moss_block", "moss_block", "shroomlight");
            case GILDED -> of(set, "waxed_oxidized_cut_copper", "waxed_oxidized_copper", "polished_tuff", "glowstone");
        };
    }

    private static SanctumPalette of(ElementalArmorSet set, String wall, String wallAlt, String floor, String light)
    {
        DustElement element = set.element();
        BlockState accent = ElementCrystals.BLOCKS.get(element).get().defaultBlockState();
        BlockState pillar = Blocks.STONE_BRICKS.defaultBlockState();
        for (ElementalWood wood : ElementalWood.values()) {
            if (wood.element().equals(element)) pillar = ElementalWoods.set(wood).log().get().defaultBlockState();
        }
        return new SanctumPalette(block(wall), block(wallAlt), block(floor), accent, pillar, block(light));
    }

    private static BlockState block(String id)
    {
        Block block = BuiltInRegistries.BLOCK.getValue(Identifier.withDefaultNamespace(id));
        return block == Blocks.AIR ? Blocks.STONE_BRICKS.defaultBlockState() : block.defaultBlockState();
    }
}
