package com.mowmaster.dust.DustLoot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustRegistries.DustLootRegistry;
import com.mowmaster.dust.Features.AncientLanguage.AncientWords;
import com.mowmaster.dust.Features.AncientLanguage.AncientWritingItem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.Optional;

// With the given chance, adds an Ancient Tablet with a random word to the loot (chests: see the loot modifier datagen).
public class AddAncientTabletModifier extends LootModifier
{
    public static final MapCodec<AddAncientTabletModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            LootModifier.codecStart(instance)
                    .and(Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter(m -> m.chance))
                    .apply(instance, AddAncientTabletModifier::new));

    private final float chance;

    public AddAncientTabletModifier(Optional<Holder<LootItemCondition>> condition, int priority, float chance)
    {
        super(condition, priority);
        this.chance = chance;
    }

    public AddAncientTabletModifier(LootItemCondition condition, float chance)
    {
        this(Optional.of(Holder.direct(condition)), IGlobalLootModifier.DEFAULT_PRIORITY, chance);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
    {
        if (context.getRandom().nextFloat() < chance) {
            generatedLoot.add(AncientWritingItem.withWord(new ItemStack(DustItemRegistry.ANCIENT_TABLET.get()), AncientWords.random(context.getRandom())));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec()
    {
        return DustLootRegistry.ADD_ANCIENT_TABLET.get();
    }
}
