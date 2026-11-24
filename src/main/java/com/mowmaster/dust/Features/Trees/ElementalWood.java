package com.mowmaster.dust.Features.Trees;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.function.Supplier;

// The elemental trees: one for each primal and each element made of two primals, each with its own wood (a full set of
// blocks: ElementalWoods), magical fruit of its element (ElementalFruitBlock), and the charcoal its colour burns as
// (Charcoal). They grow in biomes rich in their element (Features/Worldgen/ElementalTreeFeature), from saplings anywhere.
//   wood          element    tree shape   charcoal    biomes
//   emberwood     Fire       acacia       cherry red  savannas, badlands, deserts
//   tidewood      Water      fancy oak    blue        rivers, beaches, swamps
//   loamwood      Earth      oak          orange      plains, forests, taigas
//   pearlwood     Order      tall birch   white       snowy biomes, mountain slopes
//   umbralwood    Chaos      azalea       violet      spooky biomes
//   sunwood       Energy     oak          yellow      savannas, plateaus
//   cinderwood    Explosion  acacia       dark red    badlands
//   frostwood     Ice        spruce       blue        snowy and icy biomes
//   abysswood     Abyss      azalea       blue        swamps
//   slatewood     Stone      pine         orange      mountain slopes, windswept, stony shores
//   bogwood       Mud        oak          dark red    swamps, mushroom fields
//   zephyrwood    Air        fancy oak    violet      windswept, hills
//   verdantwood   Life       tall birch   blue        jungles, lush caves, flower biomes
//   gildwood      Metal      oak          yellow      badlands, savannas
public enum ElementalWood implements StringRepresentable
{
    EMBERWOOD("emberwood", () -> DustElements.FIRE, TreeShape.ACACIA, Charcoal.CHERRY_RED, MapColor.COLOR_RED, 0xE0622A,
            List.of(Tags.Biomes.IS_SAVANNA, Tags.Biomes.IS_BADLANDS, Tags.Biomes.IS_DESERT)),
    TIDEWOOD("tidewood", () -> DustElements.WATER, TreeShape.FANCY, Charcoal.BLUE, MapColor.COLOR_LIGHT_BLUE, 0x3FB4C9,
            List.of(Tags.Biomes.IS_RIVER, Tags.Biomes.IS_BEACH, Tags.Biomes.IS_SWAMP)),
    LOAMWOOD("loamwood", () -> DustElements.EARTH, TreeShape.OAK, Charcoal.ORANGE, MapColor.DIRT, 0x6E9B3A,
            List.of(Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, Tags.Biomes.IS_TAIGA)),
    PEARLWOOD("pearlwood", () -> DustElements.ORDER, TreeShape.TALL, Charcoal.WHITE, MapColor.QUARTZ, 0xEDEBD5,
            List.of(Tags.Biomes.IS_SNOWY, Tags.Biomes.IS_MOUNTAIN_SLOPE)),
    UMBRALWOOD("umbralwood", () -> DustElements.CHAOS, TreeShape.AZALEA, Charcoal.VIOLET, MapColor.COLOR_PURPLE, 0x6B3C8F,
            List.of(Tags.Biomes.IS_SPOOKY)),
    SUNWOOD("sunwood", () -> DustElements.ENERGY, TreeShape.OAK, Charcoal.YELLOW, MapColor.COLOR_YELLOW, 0xF2D33D,
            List.of(Tags.Biomes.IS_SAVANNA, Tags.Biomes.IS_PLATEAU)),
    CINDERWOOD("cinderwood", () -> DustElements.EXPLOSION, TreeShape.ACACIA, Charcoal.DARK_RED, MapColor.COLOR_BLACK, 0x9A3B2C,
            List.of(Tags.Biomes.IS_BADLANDS)),
    FROSTWOOD("frostwood", () -> DustElements.ICE, TreeShape.SPRUCE, Charcoal.BLUE, MapColor.ICE, 0xCDEBF5,
            List.of(Tags.Biomes.IS_SNOWY, Tags.Biomes.IS_ICY)),
    ABYSSWOOD("abysswood", () -> DustElements.ABYSS, TreeShape.AZALEA, Charcoal.BLUE, MapColor.COLOR_BLUE, 0x2A5A8C,
            List.of(Tags.Biomes.IS_SWAMP)),
    SLATEWOOD("slatewood", () -> DustElements.STONE, TreeShape.PINE, Charcoal.ORANGE, MapColor.STONE, 0x6E7F6A,
            List.of(Tags.Biomes.IS_MOUNTAIN_SLOPE, Tags.Biomes.IS_WINDSWEPT, Tags.Biomes.IS_STONY_SHORES)),
    BOGWOOD("bogwood", () -> DustElements.MUD, TreeShape.OAK, Charcoal.DARK_RED, MapColor.COLOR_BROWN, 0x6B6B2E,
            List.of(Tags.Biomes.IS_SWAMP, Tags.Biomes.IS_MUSHROOM)),
    ZEPHYRWOOD("zephyrwood", () -> DustElements.AIR, TreeShape.FANCY, Charcoal.VIOLET, MapColor.COLOR_PINK, 0xD7B8F0,
            List.of(Tags.Biomes.IS_WINDSWEPT, Tags.Biomes.IS_HILL)),
    VERDANTWOOD("verdantwood", () -> DustElements.LIFE, TreeShape.TALL, Charcoal.BLUE, MapColor.COLOR_CYAN, 0x43C25B,
            List.of(Tags.Biomes.IS_JUNGLE, Tags.Biomes.IS_LUSH, Tags.Biomes.IS_FLORAL)),
    GILDWOOD("gildwood", () -> DustElements.METAL, TreeShape.OAK, Charcoal.YELLOW, MapColor.GOLD, 0xE8C14A,
            List.of(Tags.Biomes.IS_BADLANDS, Tags.Biomes.IS_SAVANNA));

    public static final Codec<ElementalWood> CODEC = StringRepresentable.fromEnum(ElementalWood::values);

    public final String id;
    private final Supplier<DustElement> element;
    public final TreeShape shape;
    public final Charcoal charcoal;
    public final MapColor mapColor;
    // The colour of its leaves (drawn in their texture; the leaves drifting down from them are tinted this)
    public final int leafColor;
    public final List<TagKey<Biome>> biomes;
    // Buttons, pressure plates and fence gates take their sounds from these (wood's, by default)
    public final BlockSetType setType;
    public final WoodType woodType;

    ElementalWood(String id, Supplier<DustElement> element, TreeShape shape, Charcoal charcoal, MapColor mapColor, int leafColor, List<TagKey<Biome>> biomes)
    {
        this.id = id;
        this.element = element;
        this.shape = shape;
        this.charcoal = charcoal;
        this.mapColor = mapColor;
        this.leafColor = leafColor;
        this.biomes = biomes;
        this.setType = BlockSetType.register(new BlockSetType(DustReferences.MODID + ":" + id));
        this.woodType = WoodType.register(new WoodType(DustReferences.MODID + ":" + id, setType));
    }

    public DustElement element()
    {
        return element.get();
    }

    public boolean growsIn(Holder<Biome> biome)
    {
        for (TagKey<Biome> tag : biomes) if (biome.is(tag)) return true;
        return false;
    }

    // The look of its fruit's item (textures item/fruit/<shape> and <shape>_stem; the fruit is tinted its element's colour)
    public String fruitShape()
    {
        return switch (this) {
            case EMBERWOOD, LOAMWOOD, SUNWOOD, BOGWOOD -> "round";
            case TIDEWOOD, PEARLWOOD, UMBRALWOOD, GILDWOOD -> "pear";
            case CINDERWOOD, SLATEWOOD, VERDANTWOOD -> "pod";
            case FROSTWOOD, ABYSSWOOD, ZEPHYRWOOD -> "cluster";
        };
    }

    // Desert and badlands trees grow on sand and terracotta too
    public boolean growsOnSand()
    {
        return biomes.contains(Tags.Biomes.IS_DESERT) || biomes.contains(Tags.Biomes.IS_BADLANDS);
    }

    @Override
    public String getSerializedName()
    {
        return id;
    }
}
