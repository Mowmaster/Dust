package com.mowmaster.dust.DeferredRegistery;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.Block.BaseBlocks.*;
import com.mowmaster.dust.Block.BuildingBlocks.BaseColoredSlabBlock;
import com.mowmaster.dust.Block.BuildingBlocks.BaseColoredStairBlock;
import com.mowmaster.dust.Block.BuildingBlocks.ColoredStoneBlock;
import com.mowmaster.dust.Block.GeneratedBlocks.BaseCrystalClusterBlock;
import com.mowmaster.dust.Block.GeneratedBlocks.BaseCrystalNodeBlock;
import com.mowmaster.dust.CreativeTabs.DustBlockTabs;
import com.mowmaster.dust.World.GeodeGen.GeodeDecorator;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.mowmaster.dust.References.Constants.MODID;

public class DeferredRegisterBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,MODID);
    public static final PlacementModifierType<GeodeDecorator> RNG_DECORATOR = register("dust_rng_initializer", GeodeDecorator.CODEC);
    //public static final DeferredRegister<FeatureDecorator<?>> DECORATORS = DeferredRegister.create(ForgeRegistries.DECORATORS, MOD_ID);
    //public static final RegistryObject<FeatureDecorator<NoneDecoratorConfiguration>> RNG_DECORATOR = DECORATORS.register("rng_initializer", GeodeDecorator::new);

    public static final RegistryObject<Block> CRYSTAL_NODE = registerBlock("block_inertnode",
            () -> new BaseCrystalNodeBlock(BlockBehaviour.Properties.of(Material.AMETHYST).randomTicks().strength(2.0F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops().lightLevel((p_152629_) -> { return 10; })));

    public static final RegistryObject<Block> CRYSTAL_CLUSTER_FULL = registerBlock("block_inertcluster_full",
            () -> new BaseCrystalClusterBlock(7, 3, BlockBehaviour.Properties.of(Material.AMETHYST).strength(1.0F).sound(SoundType.AMETHYST_CLUSTER).requiresCorrectToolForDrops().lightLevel((p_152629_) -> { return 10; })));
    public static final RegistryObject<Block> CRYSTAL_CLUSTER_LARGE = registerBlock("block_inertcluster_large",
            () -> new BaseCrystalClusterBlock(5, 3, BlockBehaviour.Properties.of(Material.AMETHYST).strength(0.75F).sound(SoundType.LARGE_AMETHYST_BUD).requiresCorrectToolForDrops().lightLevel((p_152629_) -> { return 5; })));
    public static final RegistryObject<Block> CRYSTAL_CLUSTER_MEDIUM = registerBlock("block_inertcluster_medium",
            () -> new BaseCrystalClusterBlock(4, 3, BlockBehaviour.Properties.of(Material.AMETHYST).strength(0.5F).sound(SoundType.MEDIUM_AMETHYST_BUD).requiresCorrectToolForDrops().lightLevel((p_152629_) -> { return 3; })));
    public static final RegistryObject<Block> CRYSTAL_CLUSTER_SMALL = registerBlock("block_inertcluster_small",
            () -> new BaseCrystalClusterBlock(3, 4, BlockBehaviour.Properties.of(Material.AMETHYST).strength(0.25F).sound(SoundType.SMALL_AMETHYST_BUD).requiresCorrectToolForDrops().lightLevel((p_152629_) -> { return 1; })));

    public static final RegistryObject<Block> CRYSTAL_BLOCK = registerBlock("block_crystal",
            () -> new BaseColoredCrystalBlock(BlockBehaviour.Properties.of(Material.AMETHYST).strength(1.0F).requiresCorrectToolForDrops().sound(SoundType.AMETHYST).lightLevel((p_152629_) -> { return 10; })));
    public static final RegistryObject<Block> CRYSTAL_BLOCK_COMPACT = registerBlock("block_crystal_compact",
            () -> new BaseColoredCrystalBlock(BlockBehaviour.Properties.of(Material.AMETHYST).strength(1.0F).requiresCorrectToolForDrops().sound(SoundType.AMETHYST).lightLevel((p_152629_) -> { return 15; })));

    public static final RegistryObject<Block> CRYSTAL_STONE = registerBlock("block_crystal_stone",
            () -> new ColoredStoneBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> CRYSTAL_STONE_SLAB = registerBlock("block_crystal_stone_slab",
            () -> new BaseColoredSlabBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> CRYSTAL_STONE_STAIR = registerBlock("block_crystal_stone_stair",
            () -> new BaseColoredStairBlock(CRYSTAL_STONE.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> CRYSTAL_STONE_BRICK = registerBlock("block_crystal_stone_brick",
            () -> new ColoredStoneBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> CRYSTAL_STONE_BRICK_SLAB = registerBlock("block_crystal_stone_brick_slab",
            () -> new BaseColoredSlabBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> CRYSTAL_STONE_BRICK_STAIR = registerBlock("block_crystal_stone_brick_stair",
            () -> new BaseColoredStairBlock(CRYSTAL_STONE_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> CRYSTAL_STONE_BRICKS = registerBlock("block_crystal_stone_bricks",
            () -> new ColoredStoneBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> CRYSTAL_STONE_BRICKS_SLAB = registerBlock("block_crystal_stone_bricks_slab",
            () -> new BaseColoredSlabBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> CRYSTAL_STONE_BRICKS_STAIR = registerBlock("block_crystal_stone_bricks_stair",
            () -> new BaseColoredStairBlock(CRYSTAL_STONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> CRYSTAL_DUST_BLOCK = registerBlock("block_crystal_dust",
            () -> new BaseColoredPowderedBlock(BlockBehaviour.Properties.of(Material.SAND).strength(0.25F).sound(SoundType.SAND).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> BASE_MACHINE_BLOCK = registerBlock("block_machine_base",
            () -> new BaseMachineBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> BASE_WORKSTATION_BLOCK = registerBlock("block_workstation_base",
            () -> new BaseWorkStationBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));







    public static final RegistryObject<Block> RENDER_BLOCK_GRATE = registerBlock("block_renderblock_grate",
            () -> new BaseRenderBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0F).sound(SoundType.STONE)));



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        DeferredRegisterItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(DustBlockTabs.TAB_BLOCKS)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    //For Geodes
    private static <P extends PlacementModifier> PlacementModifierType<P> register(String name, Codec<P> codec) {
        return Registry.register(Registry.PLACEMENT_MODIFIERS, name, () -> {
            return codec;
        });
    }
}
