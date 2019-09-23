package com.mowmaster.dust.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.mowmaster.dust.references.Reference.MODID;




public class BlockDustStone extends Block
{
    public BlockDustStone(Properties builder) {
        super(builder);
    }

    private static final ResourceLocation RESLOC_STONE_RED = new ResourceLocation(MODID, "stonered");
    private static final ResourceLocation RESLOC_STONE_RED1 = new ResourceLocation(MODID, "stonered");
    private static final ResourceLocation RESLOC_STONE_RED2 = new ResourceLocation(MODID, "stonered");
    private static final ResourceLocation RESLOC_STONE_RED3 = new ResourceLocation(MODID, "stonered");
    private static final ResourceLocation RESLOC_STONE_RED4 = new ResourceLocation(MODID, "stonered");
    private static final ResourceLocation RESLOC_STONE_RED5 = new ResourceLocation(MODID, "stonered");

    private static final ResourceLocation RESLOC_STONE_GREEN = new ResourceLocation(MODID, "stonegreen");
    private static final ResourceLocation RESLOC_STONE_BLUE = new ResourceLocation(MODID, "stoneblue");
    private static final ResourceLocation RESLOC_STONE_WHITE = new ResourceLocation(MODID, "stonewhite");
    private static final ResourceLocation RESLOC_STONE_BLACK = new ResourceLocation(MODID, "stoneblack");

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(ITEM_STONE_RED);
        event.getRegistry().register(ITEM_STONE_RED1);
        event.getRegistry().register(ITEM_STONE_RED2);
        event.getRegistry().register(ITEM_STONE_RED3);
        event.getRegistry().register(ITEM_STONE_RED4);
        event.getRegistry().register(ITEM_STONE_RED5);

        event.getRegistry().register(ITEM_STONE_GREEN);
        event.getRegistry().register(ITEM_STONE_BLUE);
        event.getRegistry().register(ITEM_STONE_WHITE);
        event.getRegistry().register(ITEM_STONE_BLACK);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(STONE_RED);
        event.getRegistry().register(STONE_RED1);
        event.getRegistry().register(STONE_RED2);
        event.getRegistry().register(STONE_RED3);
        event.getRegistry().register(STONE_RED4);
        event.getRegistry().register(STONE_RED5);

        event.getRegistry().register(STONE_GREEN);
        event.getRegistry().register(STONE_BLUE);
        event.getRegistry().register(STONE_WHITE);
        event.getRegistry().register(STONE_BLACK);
    }



    public static void handleBlockColors(ColorHandlerEvent.Block event) {

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711680;} else {return -1;}},STONE_RED);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 2752512;} else {return -1;}},STONE_RED1);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5505024;} else {return -1;}},STONE_RED2);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 8323072;} else {return -1;}},STONE_RED3);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141120;} else {return -1;}},STONE_RED4);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 13893632;} else {return -1;}},STONE_RED5);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65280;} else {return -1;}},STONE_GREEN);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 255;} else {return -1;}},STONE_BLUE);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777215;} else {return -1;}},STONE_WHITE);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 2105376;} else {return -1;}},STONE_BLACK);

    }

    public static void handleItemColors(ColorHandlerEvent.Item event) {

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711680;}else {return -1;}},ITEM_STONE_RED);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 2752512;}else {return -1;}},ITEM_STONE_RED1);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5505024;}else {return -1;}},ITEM_STONE_RED2);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 8323072;}else {return -1;}},ITEM_STONE_RED3);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141120;}else {return -1;}},ITEM_STONE_RED4);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 13893632;}else {return -1;}},ITEM_STONE_RED5);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65280;}else {return -1;}},ITEM_STONE_GREEN);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 255;}else {return -1;}},ITEM_STONE_BLUE);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777215;}else {return -1;}},ITEM_STONE_WHITE);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 2105376;}else {return -1;}},ITEM_STONE_BLACK);

    }

    public static final Block STONE_RED = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_RED);
    public static final Block STONE_RED1 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_RED+"1");
    public static final Block STONE_RED2 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_RED+"2");
    public static final Block STONE_RED3 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_RED+"3");
    public static final Block STONE_RED4 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_RED+"4");
    public static final Block STONE_RED5 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_RED+"5");

    public static final Block STONE_GREEN = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.GREEN_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_GREEN);
    public static final Block STONE_BLUE = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.BLUE_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_BLUE);
    public static final Block STONE_WHITE = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_WHITE);
    public static final Block STONE_BLACK = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.BLACK_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_BLACK);

    public static final Item ITEM_STONE_RED = new BlockItem(STONE_RED, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_RED);
    public static final Item ITEM_STONE_RED1 = new BlockItem(STONE_RED1, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_RED+"1");
    public static final Item ITEM_STONE_RED2 = new BlockItem(STONE_RED2, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_RED+"2");
    public static final Item ITEM_STONE_RED3 = new BlockItem(STONE_RED3, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_RED+"3");
    public static final Item ITEM_STONE_RED4 = new BlockItem(STONE_RED4, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_RED+"4");
    public static final Item ITEM_STONE_RED5 = new BlockItem(STONE_RED5, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_RED+"5");

    public static final Item ITEM_STONE_GREEN = new BlockItem(STONE_GREEN, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_GREEN);
    public static final Item ITEM_STONE_BLUE = new BlockItem(STONE_BLUE, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_BLUE);
    public static final Item ITEM_STONE_WHITE = new BlockItem(STONE_WHITE, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_WHITE);
    public static final Item ITEM_STONE_BLACK = new BlockItem(STONE_BLACK, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_STONE_BLACK);
}
