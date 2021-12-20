package com.mowmaster.dust.DeferredRegistery;

import com.mowmaster.dust.Block.BlockEntities.CrystalCluster.EffectCrystalClusterBlockEntity;
import com.mowmaster.dust.Block.BlockEntities.CustomDustBlock.CustomPowderedBlockEntity;
import com.mowmaster.dust.Block.BlockEntities.DustJar.DustJarBlockEntity;
import com.mowmaster.dust.Block.BlockEntities.Pedestal.BasePedestalBlockEntity;
import com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15.ScrollCrafterBlockEntity_T15;
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

    public static final RegistryObject<BlockEntityType<ScrollCrafterBlockEntity_T15>> CRAFTER_SCROLL_T15 = BLOCK_ENTITIES.register(
            "block_entity_crafter_scroll_t15",
            () -> BlockEntityType.Builder.of(ScrollCrafterBlockEntity_T15::new, DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get()).build(null));

    public static final RegistryObject<BlockEntityType<DustJarBlockEntity>> DUST_JAR = BLOCK_ENTITIES.register(
            "block_entity_dust_jar",
            () -> BlockEntityType.Builder.of(DustJarBlockEntity::new, DeferredRegisterTileBlocks.BLOCK_DUST_JAR.get()).build(null));






    private DeferredBlockEntityTypes() {
    }
}
