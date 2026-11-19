package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.ElementTesterBlockEntity;
import com.mowmaster.dust.Features.Machines.ResearchCrusherBlockEntity;
import com.mowmaster.dust.Features.Machines.ResearchCombinationBlockEntity;
import com.mowmaster.dust.Features.Spells.pylons.PylonBlockEntity;
import com.mowmaster.dust.Features.Spells.blocks.CorruptedBlockEntity;
import com.mowmaster.dust.Features.Spells.blocks.PhantomBlockEntity;
import com.mowmaster.dust.Features.Spells.pylons.SpellPylonBlockEntity;
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
    // The Element Crafter (Features/Crafting): the grid, the jars and the result
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Crafting.ElementCrafterBlockEntity>> ELEMENT_CRAFTER = BLOCK_ENTITIES.register("element_crafter",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Crafting.ElementCrafterBlockEntity::new, Set.of(DustBlockRegistry.ELEMENT_CRAFTER.get())));
    // The Element Replicator (Features/Machines): the item to copy, the copy, the run and the dye colour
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Machines.ElementReplicatorBlockEntity>> ELEMENT_REPLICATOR = BLOCK_ENTITIES.register("element_replicator",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Machines.ElementReplicatorBlockEntity::new, Set.of(DustBlockRegistry.ELEMENT_REPLICATOR.get())));

    // Dust storage (Features/DustStorage)
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.DustStorage.DustJarBlockEntity>> DUST_JAR = BLOCK_ENTITIES.register("dust_jar",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.DustStorage.DustJarBlockEntity::new, Set.of(DustBlockRegistry.DUST_JAR.get())));
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.DustStorage.DustGrinderBlockEntity>> DUST_GRINDER = BLOCK_ENTITIES.register("dust_grinder",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.DustStorage.DustGrinderBlockEntity::new, Set.of(DustBlockRegistry.DUST_GRINDER.get())));

    // Lamps (Features/Lamps)
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Lamps.GrowthLampBlockEntity>> GROWTH_LAMP = BLOCK_ENTITIES.register("growth_lamp",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Lamps.GrowthLampBlockEntity::new, Set.of(DustBlockRegistry.GROWTH_LAMP.get())));
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Lamps.LovelyLampBlockEntity>> LOVELY_LAMP = BLOCK_ENTITIES.register("lovely_lamp",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Lamps.LovelyLampBlockEntity::new, Set.of(DustBlockRegistry.LOVELY_LAMP.get())));

    // The alchemical apparatus (Features/Apparatus)
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Apparatus.CrushingMillBlockEntity>> CRUSHING_MILL = BLOCK_ENTITIES.register("crushing_mill",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Apparatus.CrushingMillBlockEntity::new, Set.of(DustBlockRegistry.CRUSHING_MILL.get())));
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Apparatus.SeparatorBlockEntity>> ALCHEMICAL_SEPARATOR = BLOCK_ENTITIES.register("alchemical_separator",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Apparatus.SeparatorBlockEntity::new, Set.of(DustBlockRegistry.ALCHEMICAL_SEPARATOR.get())));
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Apparatus.EvaporatingKettleBlockEntity>> EVAPORATING_KETTLE = BLOCK_ENTITIES.register("evaporating_kettle",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Apparatus.EvaporatingKettleBlockEntity::new, Set.of(DustBlockRegistry.EVAPORATING_KETTLE.get())));
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Apparatus.AlembicBlockEntity>> ALEMBIC = BLOCK_ENTITIES.register("alembic",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Apparatus.AlembicBlockEntity::new, Set.of(DustBlockRegistry.ALEMBIC.get())));
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Apparatus.TransmuterBlockEntity>> TRANSMUTER = BLOCK_ENTITIES.register("transmuter",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Apparatus.TransmuterBlockEntity::new, Set.of(DustBlockRegistry.TRANSMUTER.get())));
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Apparatus.ConjoiningCrucibleBlockEntity>> CONJOINING_CRUCIBLE = BLOCK_ENTITIES.register("conjoining_crucible",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Apparatus.ConjoiningCrucibleBlockEntity::new, Set.of(DustBlockRegistry.CONJOINING_CRUCIBLE.get())));
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Apparatus.MixingVatBlockEntity>> MIXING_VAT = BLOCK_ENTITIES.register("mixing_vat",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Apparatus.MixingVatBlockEntity::new, Set.of(DustBlockRegistry.MIXING_VAT.get())));
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Apparatus.DissolvingRetortBlockEntity>> DISSOLVING_RETORT = BLOCK_ENTITIES.register("dissolving_retort",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Apparatus.DissolvingRetortBlockEntity::new, Set.of(DustBlockRegistry.DISSOLVING_RETORT.get())));

    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Spells.caster.SpellCasterBlockEntity>> SPELL_CASTER = BLOCK_ENTITIES.register("spell_caster",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Spells.caster.SpellCasterBlockEntity::new, Set.of(DustBlockRegistry.SPELL_CASTER.get())));

    public static final Supplier<BlockEntityType<ResearchCrusherBlockEntity>> RESEARCH_CRUSHER = BLOCK_ENTITIES.register("research_crusher",
            () -> new BlockEntityType<>(ResearchCrusherBlockEntity::new, Set.of(DustBlockRegistry.RESEARCH_CRUSHER.get())));

    public static final Supplier<BlockEntityType<ResearchSeparationBlockEntity>> RESEARCH_SEPARATION = BLOCK_ENTITIES.register("research_separation",
            () -> new BlockEntityType<>(ResearchSeparationBlockEntity::new, Set.of(DustBlockRegistry.RESEARCH_SEPARATION.get())));

    public static final Supplier<BlockEntityType<ResearchEvaporationBlockEntity>> RESEARCH_EVAPORATION = BLOCK_ENTITIES.register("research_evaporation",
            () -> new BlockEntityType<>(ResearchEvaporationBlockEntity::new, Set.of(DustBlockRegistry.RESEARCH_EVAPORATION.get())));
    public static final Supplier<BlockEntityType<ResearchDistillationBlockEntity>> RESEARCH_DISTILLATION = BLOCK_ENTITIES.register("research_distalation",
            () -> new BlockEntityType<>(ResearchDistillationBlockEntity::new, Set.of(DustBlockRegistry.RESEARCH_DISTILLATION.get())));
    public static final Supplier<BlockEntityType<CorruptedBlockEntity>> CORRUPTED = BLOCK_ENTITIES.register("corrupted_block",
            () -> new BlockEntityType<>(CorruptedBlockEntity::new, Set.of(DustBlockRegistry.CORRUPTED.get())));
    public static final Supplier<BlockEntityType<PhantomBlockEntity>> PHANTOM = BLOCK_ENTITIES.register("phantom_block",
            () -> new BlockEntityType<>(PhantomBlockEntity::new, Set.of(DustBlockRegistry.VEILED.get(), DustBlockRegistry.PHASED.get())));
    // A rune's spell (Features/Spells/blocks/RuneBlockEntity)
    public static final Supplier<BlockEntityType<com.mowmaster.dust.Features.Spells.blocks.RuneBlockEntity>> RUNE = BLOCK_ENTITIES.register("rune",
            () -> new BlockEntityType<>(com.mowmaster.dust.Features.Spells.blocks.RuneBlockEntity::new, Set.of(DustBlockRegistry.RUNE.get())));
    public static final Supplier<BlockEntityType<SpellPylonBlockEntity>> SPELL_PYLON = BLOCK_ENTITIES.register("spell_pylon",
            () -> new BlockEntityType<>(SpellPylonBlockEntity::new, Set.of(DustBlockRegistry.SPELL_PYLON.get())));
    public static final Supplier<BlockEntityType<PylonBlockEntity>> PYLON = BLOCK_ENTITIES.register("pylon",
            () -> new BlockEntityType<>(PylonBlockEntity::new, Set.of(DustBlockRegistry.SHAPE_PYLON.get(), DustBlockRegistry.NEUTRAL_PYLON.get(),
                    DustBlockRegistry.THRIFTY_PYLON.get(), DustBlockRegistry.GIANT_PYLON.get(), DustBlockRegistry.DAMAGE_PYLON.get(), DustBlockRegistry.EXPERIENCED_PYLON.get())));
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
