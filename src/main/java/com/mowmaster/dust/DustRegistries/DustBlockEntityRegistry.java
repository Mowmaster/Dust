package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.ElementTesterBlockEntity;
import com.mowmaster.dust.Features.Machines.ResearchCrusherBlockEntity;
import com.mowmaster.dust.Features.Machines.ResearchCombinationBlockEntity;
import com.mowmaster.dust.Features.Machines.ResearchDistillationBlockEntity;
import com.mowmaster.dust.Features.Machines.ResearchMixingBlockEntity;
import com.mowmaster.dust.Features.Machines.ResearchTransformationBlockEntity;
import com.mowmaster.dust.Features.Machines.ResearchEvaporationBlockEntity;
import com.mowmaster.dust.Features.Machines.ResearchSeparationBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

// Dust's own block entity types. (The dust fire uses MowLib's shared tint block entity, which finds it by itself.)
public class DustBlockEntityRegistry
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DustReferences.MODID);

    // new BlockEntityType<>(factory, blocks): how to make one, and the blocks allowed to have one
    public static final Supplier<BlockEntityType<ElementTesterBlockEntity>> ELEMENT_TESTER = BLOCK_ENTITIES.register("element_tester",
            () -> new BlockEntityType<>(ElementTesterBlockEntity::new, Set.of(DustBlockRegistry.ELEMENT_TESTER.get())));

    public static final Supplier<BlockEntityType<ResearchCrusherBlockEntity>> RESEARCH_CRUSHER = BLOCK_ENTITIES.register("research_crusher",
            () -> new BlockEntityType<>(ResearchCrusherBlockEntity::new, Set.of(DustBlockRegistry.RESEARCH_CRUSHER.get())));

    public static final Supplier<BlockEntityType<ResearchSeparationBlockEntity>> RESEARCH_SEPARATION = BLOCK_ENTITIES.register("research_separation",
            () -> new BlockEntityType<>(ResearchSeparationBlockEntity::new, Set.of(DustBlockRegistry.RESEARCH_SEPARATION.get())));

    public static final Supplier<BlockEntityType<ResearchEvaporationBlockEntity>> RESEARCH_EVAPORATION = BLOCK_ENTITIES.register("research_evaporation",
            () -> new BlockEntityType<>(ResearchEvaporationBlockEntity::new, Set.of(DustBlockRegistry.RESEARCH_EVAPORATION.get())));
    public static final Supplier<BlockEntityType<ResearchDistillationBlockEntity>> RESEARCH_DISTILLATION = BLOCK_ENTITIES.register("research_distalation",
            () -> new BlockEntityType<>(ResearchDistillationBlockEntity::new, Set.of(DustBlockRegistry.RESEARCH_DISTILLATION.get())));
    public static final Supplier<BlockEntityType<ResearchTransformationBlockEntity>> RESEARCH_TRANSFORMATION = BLOCK_ENTITIES.register("research_transformational",
            () -> new BlockEntityType<>(ResearchTransformationBlockEntity::new, Set.of(DustBlockRegistry.RESEARCH_TRANSFORMATION.get())));
    public static final Supplier<BlockEntityType<ResearchCombinationBlockEntity>> RESEARCH_COMBINATION = BLOCK_ENTITIES.register("research_combination",
            () -> new BlockEntityType<>(ResearchCombinationBlockEntity::new, Set.of(DustBlockRegistry.RESEARCH_COMBINATION.get())));
    public static final Supplier<BlockEntityType<ResearchMixingBlockEntity>> RESEARCH_MIXING = BLOCK_ENTITIES.register("research_mixing",
            () -> new BlockEntityType<>(ResearchMixingBlockEntity::new, Set.of(DustBlockRegistry.RESEARCH_MIXING.get())));

    public static void register(IEventBus eventBus)
    {
        BLOCK_ENTITIES.register(eventBus);
    }
}
