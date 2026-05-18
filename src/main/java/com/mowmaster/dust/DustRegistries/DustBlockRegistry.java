package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.Features.CrystalBlocks.Block.CrystalPathBaseBlock;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.CrystalBlocks.Block.CrystalStone;
import com.mowmaster.dust.Features.DustyDelights.Block.CropBlockLettuce;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class DustBlockRegistry {
    public static final DeferredRegister.Blocks DUSTBLOCKS = DeferredRegister.createBlocks(DustReferences.MODID);

    /*
    If a block doesnt need a loot table this way the datagen wont fail
    public static final DeferredBlock<Block> BLOCK_OF_DUST_RED = registerBlock("blockofdust_red", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops().noLootTable()));
     */
    public static final DeferredBlock<Block> BLOCK_OF_DUST_RED = registerBlock("blockofdust_red", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_DUST_GREEN = registerBlock("blockofdust_green", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_DUST_BLUE = registerBlock("blockofdust_blue", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_DUST_WHITE = registerBlock("blockofdust_white", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_DUST_BLACK = registerBlock("blockofdust_black", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));


    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_RED = registerBlock("blockofcrystal_red", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_GREEN = registerBlock("blockofcrystal_green", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_BLUE = registerBlock("blockofcrystal_blue", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_WHITE = registerBlock("blockofcrystal_white", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_BLACK = registerBlock("blockofcrystal_black", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));


    public static final DeferredBlock<Block> INERT_CRYSTAL_ORE = registerBlock("block_inert_crystal_ore", properties -> new DropExperienceBlock(
            UniformInt.of(2,4), properties.sound(SoundType.STONE).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> INERT_CRYSTAL_ORE_DEEPSLATE = registerBlock("block_inert_crystal_ore_deepslate", properties -> new DropExperienceBlock(
            UniformInt.of(2,4), properties.sound(SoundType.DEEPSLATE).strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> CRYSTAL_PATH_TIER1 = registerBlock("crystal_path_tier1", properties -> new CrystalPathBaseBlock(
            properties.sound(SoundType.AMETHYST).strength(2f).requiresCorrectToolForDrops(), 1));
    public static final DeferredBlock<Block> CRYSTAL_PATH_TIER2 = registerBlock("crystal_path_tier2", properties -> new CrystalPathBaseBlock(
            properties.sound(SoundType.AMETHYST).strength(2f).requiresCorrectToolForDrops(), 4));
    public static final DeferredBlock<Block> CRYSTAL_PATH_TIER3 = registerBlock("crystal_path_tier3", properties -> new CrystalPathBaseBlock(
            properties.sound(SoundType.AMETHYST).strength(2f).requiresCorrectToolForDrops(), 8));
    public static final DeferredBlock<Block> CRYSTAL_PATH_TIER4 = registerBlock("crystal_path_tier4", properties -> new CrystalPathBaseBlock(
            properties.sound(SoundType.AMETHYST).strength(2f).requiresCorrectToolForDrops(), 12));
    public static final DeferredBlock<Block> BLOCK_PLANKS_GREEN = registerBlock("planks_green", properties -> new Block(
            properties.sound(SoundType.WOOD).strength(2.0f, 3.0f).sound(SoundType.WOOD).ignitedByLava()));
    public static final DeferredBlock<Block> BLOCK_STAIRS_GREEN = registerBlock("stairs_green", properties -> new StairBlock(
            DustBlockRegistry.BLOCK_PLANKS_GREEN.get().defaultBlockState(),
            properties.sound(SoundType.WOOD).strength(2.0f, 3.0f).sound(SoundType.WOOD).ignitedByLava())
    );
    public static final DeferredBlock<Block> BLOCK_SLAB_GREEN = registerBlock("slab_green", properties -> new SlabBlock(
            properties.sound(SoundType.WOOD).strength(2.0f, 3.0f).ignitedByLava())
    );
    public static final DeferredBlock<Block> BLOCK_PRESSUREPLATE_GREEN = registerBlock("pressureplate_green", properties -> new PressurePlateBlock(
            BlockSetType.OAK,
            properties
                    .sound(SoundType.WOOD)
                    .strength(0.5f)
                    .ignitedByLava()
                    .noCollision()
                    .forceSolidOn()
                    .pushReaction(PushReaction.DESTROY)
            )
    );
    public static final DeferredBlock<Block> BLOCK_BUTTON_GREEN = registerBlock("button_green", properties -> new ButtonBlock(
            BlockSetType.OAK,
            20,
            properties.sound(SoundType.WOOD)
                    .strength(0.5f)
                    .ignitedByLava()
                    .noCollision()
                    .forceSolidOn()
                    .pushReaction(PushReaction.DESTROY))
    );

    public static final DeferredBlock<Block> BLOCK_FENCE_GREEN = registerBlock("fence_green", properties -> new FenceBlock(
            properties.sound(SoundType.WOOD).strength(2.0f, 3.0f).ignitedByLava())
    );
    public static final DeferredBlock<Block> BLOCK_FENCEGATE_GREEN = registerBlock("fencegate_green", properties -> new FenceGateBlock(WoodType.OAK,
            properties.sound(SoundType.WOOD).strength(2.0f, 3.0f).ignitedByLava())
    );
    public static final DeferredBlock<Block> BLOCK_WALL_GREEN = registerBlock("wall_green", properties -> new WallBlock(
            properties.sound(SoundType.WOOD).strength(2.0f, 3.0f).ignitedByLava().forceSolidOn())
    );

    public static final DeferredBlock<Block> BLOCK_TRAPDOOR_GREEN = registerBlock("trapdoor_green", properties -> new TrapDoorBlock(
                    BlockSetType.OAK,
                    properties
                            .sound(SoundType.WOOD)
                            .strength(0.5f)
                            .ignitedByLava()
                            .noOcclusion()
                            .isValidSpawn(Blocks::never)
            )
    );
    public static final DeferredBlock<Block> BLOCK_DOOR_GREEN = registerBlock("door_green", properties -> new DoorBlock(
                    BlockSetType.OAK,
                    properties
                            .sound(SoundType.WOOD)
                            .strength(0.5f)
                            .ignitedByLava()
                            .noOcclusion()
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> CROP_LETTUCE = DUSTBLOCKS.registerBlock("crop_lettuce",
            properties -> new CropBlockLettuce(properties.mapColor(MapColor.PLANT)
                    .noCollision().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<Block> BLOCK_FLOWER_WHINDWHEEL = registerBlock("flower_windwheel",
            properties -> new FlowerBlock(
                    MobEffects.WIND_CHARGED, 10F, properties
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)
            ));
    public static final DeferredBlock<Block> BLOCK_POTTEDFLOWER_WINDWHEEL = DUSTBLOCKS.registerBlock("pottedflower_windwheel",
            properties -> new FlowerPotBlock(null, BLOCK_FLOWER_WHINDWHEEL,
                    properties
                            .noOcclusion()
                            .instabreak()
                            .pushReaction(PushReaction.DESTROY)
            ));

    public static final DeferredBlock<Block> CRYSTAL_STONE_RED = registerBlock("crystal_stone_red",
            properties -> new CrystalStone(properties
                    .mapColor(MapColor.COLOR_RED).sound(SoundType.STONE).strength(2f)
                    .pushReaction(PushReaction.NORMAL).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CRYSTAL_STONE_GREEN = registerBlock("crystal_stone_green",
            properties -> new CrystalStone(properties
                    .mapColor(MapColor.COLOR_RED).sound(SoundType.STONE).strength(2f)
                    .pushReaction(PushReaction.NORMAL).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CRYSTAL_STONE_BLUE = registerBlock("crystal_stone_blue",
            properties -> new CrystalStone(properties
                    .mapColor(MapColor.COLOR_RED).sound(SoundType.STONE).strength(2f)
                    .pushReaction(PushReaction.NORMAL).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CRYSTAL_STONE_WHITE = registerBlock("crystal_stone_white",
            properties -> new CrystalStone(properties
                    .mapColor(MapColor.COLOR_RED).sound(SoundType.STONE).strength(2f)
                    .pushReaction(PushReaction.NORMAL).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CRYSTAL_STONE_BLACK = registerBlock("crystal_stone_black",
            properties -> new CrystalStone(properties
                    .mapColor(MapColor.COLOR_RED).sound(SoundType.STONE).strength(2f)
                    .pushReaction(PushReaction.NORMAL).requiresCorrectToolForDrops()));














    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function)
    {
        DeferredBlock<T> toReturn = DUSTBLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block)
    {
        DustItemRegistry.DUSTITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus eventBus)
    {
        DUSTBLOCKS.register(eventBus);
    }
}
