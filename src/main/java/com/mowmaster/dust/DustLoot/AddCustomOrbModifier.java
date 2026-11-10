package com.mowmaster.dust.DustLoot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustLootRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.Optional;

// Spawns ONE orb of "element" (default dust:fire) worth minimum..maximum dust where the loot is dropped, instead of adding an item.
// Not used by any loot modifier json yet. Why it "didn't work" before: the orb was spawned without a position
// (at 0,0,0) and once per point of value, each orb worth the full amount.
public class AddCustomOrbModifier extends LootModifier {

    public static final MapCodec<AddCustomOrbModifier> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    LootModifier.codecStart(instance)
                            .and(Codec.INT.fieldOf("minimum").forGetter(AddCustomOrbModifier::getMinimum))
                            .and(Codec.INT.fieldOf("maximum").forGetter(AddCustomOrbModifier::getMaximum))
                            .and(DustElements.CODEC.optionalFieldOf("element", DustElements.FIRE).forGetter(AddCustomOrbModifier::getElement))
                            .apply(instance, AddCustomOrbModifier::new)
            );

    private final int minimum;
    private final int maximum;
    private final DustElement element;

    public AddCustomOrbModifier(Optional<Holder<LootItemCondition>> condition, int priority, int minimum, int maximum, DustElement element) {
        super(condition, priority);
        this.minimum = minimum;
        this.maximum = maximum;
        this.element = element;
    }

    public DustElement getElement() {
        return this.element;
    }

    public int getMinimum() {
        return this.minimum;
    }

    public int getMaximum() {
        return this.maximum;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ServerLevel level = context.getLevel();
        // ORIGIN is where the loot drops (the mob's or block's position). Some tables (fishing, gifts) may not have it.
        Vec3 origin = context.getOptional(LootContextParams.ORIGIN);
        if (origin == null) {
            return generatedLoot;
        }

        int min = Math.min(this.minimum, this.maximum);
        int max = Math.max(this.minimum, this.maximum);
        int amount = min + context.getRandom().nextInt(max - min + 1);
        if (amount <= 0) {
            return generatedLoot;
        }

        ElementOrbEntity orb = ElementOrbEntity.create(level, this.element, amount, origin);
        if (orb != null) {
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
