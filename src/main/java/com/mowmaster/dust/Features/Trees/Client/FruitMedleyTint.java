package com.mowmaster.dust.Features.Trees.Client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.Trees.ElementalWood;
import com.mowmaster.dust.Features.Trees.FruitMedley;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.List;

// An item model tint for the Fruit Medley's fruit pieces: piece group `index` (0-2) in the colour of a kind of fruit in it
// (its element's), the most plentiful kind first, round again when there are fewer kinds. {"type": "dust:fruit_medley",
// "index": 0} in the item model; a medley with nothing recorded shows red, green and blue.
public record FruitMedleyTint(int index) implements ItemTintSource
{
    public static final MapCodec<FruitMedleyTint> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("index").forGetter(FruitMedleyTint::index)).apply(instance, FruitMedleyTint::new));
    private static final int[] PLAIN = {0xFFD23C3C, 0xFF5DBB4A, 0xFF4A7BD2};

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity)
    {
        FruitMedley medley = stack.get(DustComponentDataRegistry.FRUIT_MEDLEY.get());
        if (medley == null || medley.fruits().isEmpty()) return PLAIN[Math.floorMod(index, PLAIN.length)];
        List<ElementalWood> kinds = medley.kinds();
        return 0xFF000000 | kinds.get(Math.floorMod(index, kinds.size())).element().color();
    }

    @Override
    public MapCodec<FruitMedleyTint> type()
    {
        return MAP_CODEC;
    }
}
