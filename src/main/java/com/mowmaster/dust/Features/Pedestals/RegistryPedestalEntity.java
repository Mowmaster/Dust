package com.mowmaster.dust.Features.Pedestals;

import com.mowmaster.dust.DustReferences;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RegistryPedestalEntity
{
    public static final DeferredRegister<BlockEntityType<?>> PEDESTAL_BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, DustReferences.MODID);

    public static final Supplier<BlockEntityType<EntityBlockPedestal>> PEDESTAL_BASE =
            PEDESTAL_BLOCK_ENTITIES.register("pedestal_base", () -> new BlockEntityType<>(
                    EntityBlockPedestal::new, RegistryPedestalBlock.PEDESTAL_BLOCK.get()));


    public static void register(IEventBus eventBus) {
        PEDESTAL_BLOCK_ENTITIES.register(eventBus);
    }
}
