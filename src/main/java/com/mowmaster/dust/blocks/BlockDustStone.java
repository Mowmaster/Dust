package com.mowmaster.dust.blocks;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class BlockDustStone extends Block
{
    public BlockDustStone(Properties builder) {
        super(builder);
    }

    private static final ResourceLocation RESLOC_STONE_RED = new ResourceLocation(Reference.MODID, "stonered");
    private static final ResourceLocation RESLOC_STONE_GREEN = new ResourceLocation(Reference.MODID, "stonegreen");
    private static final ResourceLocation RESLOC_STONE_BLUE = new ResourceLocation(Reference.MODID, "stoneblue");
    private static final ResourceLocation RESLOC_STONE_WHITE = new ResourceLocation(Reference.MODID, "stonewhite");
    private static final ResourceLocation RESLOC_STONE_BLACK = new ResourceLocation(Reference.MODID, "stoneblack");

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(ITEM_STONE_RED);
        event.getRegistry().register(ITEM_STONE_GREEN);
        event.getRegistry().register(ITEM_STONE_BLUE);
        event.getRegistry().register(ITEM_STONE_WHITE);
        event.getRegistry().register(ITEM_STONE_BLACK);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(STONE_RED);
        event.getRegistry().register(STONE_GREEN);
        event.getRegistry().register(STONE_BLUE);
        event.getRegistry().register(STONE_WHITE);
        event.getRegistry().register(STONE_BLACK);
    }

    public static void handleBlockColors(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {
            if (tintIndex == 1) {
                return 16711680;
            } else {
                return -1;
            }
        },  STONE_RED);
    }

    public static void handleItemColors(ColorHandlerEvent.Item event) {

        event.getItemColors().register((itemstack, tintIndex) -> {
            if (tintIndex == 1) {
                return 16711680;
            } else {
                return -1;
            }
        },  ITEM_STONE_RED);
    }

    public static final Block STONE_RED = new Block(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(5.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_RED);
    public static final Block STONE_GREEN = new Block(Block.Properties.create(Material.ROCK, MaterialColor.GREEN_TERRACOTTA).hardnessAndResistance(5.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_GREEN);
    public static final Block STONE_BLUE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLUE_TERRACOTTA).hardnessAndResistance(5.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_BLUE);
    public static final Block STONE_WHITE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(5.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_WHITE);
    public static final Block STONE_BLACK = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLACK_TERRACOTTA).hardnessAndResistance(5.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_BLACK);

    public static final Item ITEM_STONE_RED = new BlockItem(STONE_RED, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_RED);
    public static final Item ITEM_STONE_GREEN = new BlockItem(STONE_GREEN, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_GREEN);
    public static final Item ITEM_STONE_BLUE = new BlockItem(STONE_BLUE, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_BLUE);
    public static final Item ITEM_STONE_WHITE = new BlockItem(STONE_WHITE, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_WHITE);
    public static final Item ITEM_STONE_BLACK = new BlockItem(STONE_BLACK, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_BLACK);
}
