package com.mowmaster.dust.Features.Research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

// One way something was seen being made: the station it was made at (a crafting table, furnace, smoker, campfire...;
// empty for the 2x2 inventory grid), the ingredient grid (width columns, slots row by row, minecraft:air for an empty
// slot), and the item made. Saved on the result's entry ("Crafted from") and on each ingredient's ("Crafted into").
public record RecipeRecord(Optional<Identifier> station, int width, List<Identifier> slots, Identifier output)
{
    public static final Identifier EMPTY = Identifier.withDefaultNamespace("air");

    public static final Codec<RecipeRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.optionalFieldOf("station").forGetter(RecipeRecord::station),
            Codec.INT.fieldOf("width").forGetter(RecipeRecord::width),
            Identifier.CODEC.listOf().fieldOf("slots").forGetter(RecipeRecord::slots),
            Identifier.CODEC.fieldOf("output").forGetter(RecipeRecord::output)
    ).apply(instance, RecipeRecord::new));

    public int rows()
    {
        return width <= 0 ? 0 : (slots.size() + width - 1) / width;
    }
}
