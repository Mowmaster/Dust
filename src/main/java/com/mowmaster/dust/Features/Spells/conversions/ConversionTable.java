package com.mowmaster.dust.Features.Spells.conversions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;

import java.util.List;

// One file of conversions: data/<namespace>/dust_spell_conversions/<name>.json
//   { "element": "dust:stone", "conversions": [ { "from": "minecraft:gravel", "to": "minecraft:stone" }, ... ] }
// Every file for an element adds to that element's block effect; the first rule that matches a block is used. A pack
// replaces one of Dust's files by shipping a file with the same name.
public record ConversionTable(DustElement element, List<BlockConversion> conversions)
{
    public static final Codec<ConversionTable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DustElements.CODEC.fieldOf("element").forGetter(ConversionTable::element),
            BlockConversion.CODEC.listOf().fieldOf("conversions").forGetter(ConversionTable::conversions)
    ).apply(instance, ConversionTable::new));
}
