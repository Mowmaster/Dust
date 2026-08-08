package com.mowmaster.dust.DustLoot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustLootRegistry;
import com.mowmaster.dust.Features.DustEntities.baseOrbEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootModifier;
//DOESNT WORK
public class AddCustomOrbModifier extends LootModifier {


    public static final MapCodec<AddCustomOrbModifier> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    LootModifier.codecStart(instance)
                            .and(Codec.INT.fieldOf("minimum")
                                    .forGetter(AddCustomOrbModifier::getMinimum))
                            .and(Codec.INT.fieldOf("maximum")
                                    .forGetter(AddCustomOrbModifier::getMaximum))
                            .apply(instance, AddCustomOrbModifier::new)
            );

    private final int minimum;
    private final int maximum;

    public AddCustomOrbModifier(
            LootItemCondition[] conditions,
            int priority,
            int minimum,
            int maximum
    ) {
        super(conditions, priority);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public int getMinimum() {
        return this.minimum;
    }

    public int getMaximum() {
        return this.maximum;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(
            ObjectArrayList<ItemStack> generatedLoot,
            LootContext context
    ) {
        int min = Math.min(this.minimum, this.maximum);
        int max = Math.max(this.minimum, this.maximum);

        int amount = min + context.getLevel().getRandom().nextInt(max - min + 1);


        // Loot generation normally happens server-side, but keep this check
        // to prevent accidental client-side spawning.
        if (!(context.getLevel() instanceof ServerLevel level)) {
            return generatedLoot;
        }

        //Vec3 position = context.getOrigin();
        for (int i = 0; i < amount; i++) {
            baseOrbEntity orb = DustEntityRegistry.FIRE_ORB.get().create(level, EntitySpawnReason.SPAWN_ITEM_USE);

            if (orb == null) {
                continue;
            }

            orb.setValue(amount);

            level.addFreshEntity(orb);
        }

        // Do not add an item to the loot.
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends LootModifier> codec() {
        return DustLootRegistry.ADD_CUSTOM_ORB.get();
    }
}
