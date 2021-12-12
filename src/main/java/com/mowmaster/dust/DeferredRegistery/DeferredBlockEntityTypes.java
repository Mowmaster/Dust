package com.mowmaster.dust.DeferredRegistery;

import com.mowmaster.dust.Block.BlockEntities.CrystalCluster.EffectCrystalClusterBlockEntity;
import com.mowmaster.dust.Block.BlockEntities.CustomDustBlock.CustomPowderedBlockEntity;
import com.mowmaster.dust.Block.BlockEntities.Pedestal.BasePedestalBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mowmaster.dust.References.Constants.MODID;

public class DeferredBlockEntityTypes
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITIES, MODID);

    public static final RegistryObject<BlockEntityType<BasePedestalBlockEntity>> PEDESTAL = BLOCK_ENTITIES.register(
            "block_entity_pedestal",
            () -> BlockEntityType.Builder.of(BasePedestalBlockEntity::new, DeferredRegisterTileBlocks.BLOCK_PEDESTAL.get()).build(null));

    public static final RegistryObject<BlockEntityType<CustomPowderedBlockEntity>> DUST = BLOCK_ENTITIES.register(
            "block_entity_dust",
            () -> BlockEntityType.Builder.of(CustomPowderedBlockEntity::new, DeferredRegisterTileBlocks.BLOCK_POWDERED_DUST.get()).build(null));

    public static final RegistryObject<BlockEntityType<EffectCrystalClusterBlockEntity>> CLUSTER = BLOCK_ENTITIES.register(
            "block_entity_cluster",
            () -> BlockEntityType.Builder.of(EffectCrystalClusterBlockEntity::new, DeferredRegisterTileBlocks.BLOCK_CRYSTAL_CLUSTER.get()).build(null));




    private DeferredBlockEntityTypes() {
    }
}
