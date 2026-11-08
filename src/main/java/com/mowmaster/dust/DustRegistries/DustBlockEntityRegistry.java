package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.TestingDiskBlockEntity;
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
    public static final Supplier<BlockEntityType<TestingDiskBlockEntity>> TESTING_DISK = BLOCK_ENTITIES.register("testing_disk",
            () -> new BlockEntityType<>(TestingDiskBlockEntity::new, Set.of(DustBlockRegistry.TESTING_DISK.get())));

    public static void register(IEventBus eventBus)
    {
        BLOCK_ENTITIES.register(eventBus);
    }
}
