package com.mowmaster.dust.DeferredRegistery;

import com.mowmaster.dust.Block.BlockEntities.CrystalCluster.EffectCrystalClusterBlockEntity;
import com.mowmaster.dust.Block.BlockEntities.CustomDustBlock.CustomPowderedBlockEntity;
import com.mowmaster.dust.Block.BlockEntities.DustJar.DustJarBlockEntity;
import com.mowmaster.dust.Block.BlockEntities.Pedestal.BasePedestalBlockEntity;
//import com.mowmaster.dust.Block.BlockEntities.Tier1.Crafter.CrafterBlockEntity_T15;
import com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.Crusher.CrusherBlockEntity_T15;
import com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.Furnaces.BlastFurnace.BlastFurnaceBlockEntity_T15;
import com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.Furnaces.Furnace.SmelterFurnaceBlockEntity_T15;
import com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.Furnaces.Smoker.SmokerFurnaceBlockEntity_T15;
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

    public static final RegistryObject<BlockEntityType<DustJarBlockEntity>> DUST_JAR = BLOCK_ENTITIES.register(
            "block_entity_dust_jar",
            () -> BlockEntityType.Builder.of(DustJarBlockEntity::new, DeferredRegisterTileBlocks.BLOCK_DUST_JAR.get()).build(null));

    public static final RegistryObject<BlockEntityType<ScrollCrafterBlockEntity_T15>> CRAFTER_SCROLL_T15 = BLOCK_ENTITIES.register(
            "block_entity_crafter_scroll_t15",
            () -> BlockEntityType.Builder.of(ScrollCrafterBlockEntity_T15::new, DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlastFurnaceBlockEntity_T15>> FURNACE_BLAST_T15 = BLOCK_ENTITIES.register(
            "block_entity_furnace_blast_t15",
            () -> BlockEntityType.Builder.of(BlastFurnaceBlockEntity_T15::new, DeferredRegisterTileBlocks.BLOCK_FURNACE_BLAST_T15.get()).build(null));

    public static final RegistryObject<BlockEntityType<SmokerFurnaceBlockEntity_T15>> FURNACE_SMOKER_T15 = BLOCK_ENTITIES.register(
            "block_entity_furnace_smoker_t15",
            () -> BlockEntityType.Builder.of(SmokerFurnaceBlockEntity_T15::new, DeferredRegisterTileBlocks.BLOCK_FURNACE_SMOKER_T15.get()).build(null));

    public static final RegistryObject<BlockEntityType<SmelterFurnaceBlockEntity_T15>> FURNACE_SMELTER_T15 = BLOCK_ENTITIES.register(
            "block_entity_furnace_smelter_t15",
            () -> BlockEntityType.Builder.of(SmelterFurnaceBlockEntity_T15::new, DeferredRegisterTileBlocks.BLOCK_FURNACE_SMELTER_T15.get()).build(null));

    public static final RegistryObject<BlockEntityType<CrusherBlockEntity_T15>> CRUSHER_T15 = BLOCK_ENTITIES.register(
            "block_entity_crusher_t15",
            () -> BlockEntityType.Builder.of(CrusherBlockEntity_T15::new, DeferredRegisterTileBlocks.BLOCK_CRUSHER_T15.get()).build(null));

    /*public static final RegistryObject<BlockEntityType<CrafterBlockEntity_T15>> CRAFTER_T15 = BLOCK_ENTITIES.register(
            "block_entity_crafter_t15",
            () -> BlockEntityType.Builder.of(CrafterBlockEntity_T15::new, DeferredRegisterTileBlocks.BLOCK_CRAFTER_T15.get()).build(null));
*/






    private DeferredBlockEntityTypes() {
    }
}
