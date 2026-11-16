package com.mowmaster.dust.Features.Research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

// One way something was seen being made: the station it was made at (a crafting table, furnace, smoker, campfire...;
// empty for the 2x2 inventory grid), the ingredient grid (width columns, slots row by row, minecraft:air for an empty
// slot), and the item made. Saved on the result's entry ("Crafted from") and on each ingredient's ("Crafted into").
// elements: dust an Element Crafter recipe uses from its jars (none for anything else; RecipeCatalog fills it in).
public record RecipeRecord(Optional<Identifier> station, int width, List<Identifier> slots, Identifier output, List<Cost> elements)
{
    public static final Identifier EMPTY = Identifier.withDefaultNamespace("air");

    // amount of an element's dust
    public record Cost(DustElement element, int amount)
    {
        public static final Codec<Cost> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                DustElements.CODEC.fieldOf("element").forGetter(Cost::element),
                Codec.INT.fieldOf("amount").forGetter(Cost::amount)
        ).apply(instance, Cost::new));
    }

    public static final Codec<RecipeRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.optionalFieldOf("station").forGetter(RecipeRecord::station),
            Codec.INT.fieldOf("width").forGetter(RecipeRecord::width),
            Identifier.CODEC.listOf().fieldOf("slots").forGetter(RecipeRecord::slots),
            Identifier.CODEC.fieldOf("output").forGetter(RecipeRecord::output),
            // optionalFieldOf with a default: research saved before this existed still loads
            Cost.CODEC.listOf().optionalFieldOf("elements", List.of()).forGetter(RecipeRecord::elements)
    ).apply(instance, RecipeRecord::new));

    // Without element costs (what a crafting grid shows)
    public RecipeRecord(Optional<Identifier> station, int width, List<Identifier> slots, Identifier output)
    {
        this(station, width, slots, output, List.of());
    }

    // The same recipe (station, grid and result), whatever its costs: for not listing one twice
    public boolean sameAs(RecipeRecord other)
    {
        return station.equals(other.station) && width == other.width && slots.equals(other.slots) && output.equals(other.output);
    }

    public int rows()
    {
        return width <= 0 ? 0 : (slots.size() + width - 1) / width;
    }
}
