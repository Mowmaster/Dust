package com.mowmaster.dust.DeferredRegistery;

import com.mowmaster.dust.Block.BaseBlocks.BaseColoredCrystalBlock;
import com.mowmaster.dust.Block.BuildingBlocks.BaseColoredSlabBlock;
import com.mowmaster.dust.Block.BuildingBlocks.BaseColoredStairBlock;
import com.mowmaster.dust.Block.BuildingBlocks.ColoredStoneBlock;
import com.mowmaster.dust.Block.GeneratedBlocks.BaseCrystalClusterBlock;
import com.mowmaster.dust.Block.GeneratedBlocks.BaseCrystalNodeBlock;
import com.mowmaster.dust.CreativeTabs.DustBlockTabs;
import com.mowmaster.dust.World.GeodeGen.GeodeDecorator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.mowmaster.dust.References.Constants.MODID;
import static net.minecraftforge.versions.forge.ForgeVersion.MOD_ID;

public class DeferredRegisterBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,MODID);
    public static final DeferredRegister<FeatureDecorator<?>> DECORATORS = DeferredRegister.create(ForgeRegistries.DECORATORS, MOD_ID);

    public static final RegistryObject<FeatureDecorator<NoneDecoratorConfiguration>> RNG_DECORATOR = DECORATORS.register("rng_initializer", GeodeDecorator::new);

    public static final RegistryObject<Block> CRYSTAL_NODE = registerBlock("block_inertnode",
            () -> new BaseCrystalNodeBlock(BlockBehaviour.Properties.of(Material.AMETHYST).randomTicks().strength(3.0F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops().lightLevel((p_152629_) -> { return 10; })));

    public static final RegistryObject<Block> CRYSTAL_CLUSTER_FULL = registerBlock("block_inertcluster_full",
            () -> new BaseCrystalClusterBlock(7, 3, BlockBehaviour.Properties.of(Material.AMETHYST).strength(2.0F).sound(SoundType.AMETHYST_CLUSTER).lightLevel((p_152629_) -> { return 10; })));
    public static final RegistryObject<Block> CRYSTAL_CLUSTER_LARGE = registerBlock("block_inertcluster_large",
            () -> new BaseCrystalClusterBlock(5, 3, BlockBehaviour.Properties.of(Material.AMETHYST).strength(1.5F).sound(SoundType.LARGE_AMETHYST_BUD).lightLevel((p_152629_) -> { return 5; })));
    public static final RegistryObject<Block> CRYSTAL_CLUSTER_MEDIUM = registerBlock("block_inertcluster_medium",
            () -> new BaseCrystalClusterBlock(4, 3, BlockBehaviour.Properties.of(Material.AMETHYST).strength(1.0F).sound(SoundType.MEDIUM_AMETHYST_BUD).lightLevel((p_152629_) -> { return 3; })));
    public static final RegistryObject<Block> CRYSTAL_CLUSTER_SMALL = registerBlock("block_inertcluster_small",
            () -> new BaseCrystalClusterBlock(3, 4, BlockBehaviour.Properties.of(Material.AMETHYST).strength(0.5F).sound(SoundType.SMALL_AMETHYST_BUD).lightLevel((p_152629_) -> { return 1; })));

    public static final RegistryObject<Block> CRYSTAL_BLOCK = registerBlock("block_crystal",
            () -> new BaseColoredCrystalBlock(BlockBehaviour.Properties.of(Material.AMETHYST).strength(2.0F).sound(SoundType.AMETHYST).lightLevel((p_152629_) -> { return 15; })));

    public static final RegistryObject<Block> CRYSTAL_STONE = registerBlock("block_crystal_stone",
            () -> new ColoredStoneBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> CRYSTAL_STONE_SLAB = registerBlock("block_crystal_stone_slab",
            () -> new BaseColoredSlabBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> CRYSTAL_STONE_STAIR = registerBlock("block_crystal_stone_stair",
            () -> new BaseColoredStairBlock(CRYSTAL_STONE.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).sound(SoundType.STONE)));



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
}
