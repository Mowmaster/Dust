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

import static com.mowmaster.dust.dust.BLOCK_GROUP;
import static com.mowmaster.dust.references.Reference.MODID;




public class BlockDustStone extends Block
{
    public BlockDustStone(Properties builder) {
        super(builder);
    }

    private static final ResourceLocation RESLOC_STONE_000 = new ResourceLocation(MODID, "stone/stone000");
    private static final ResourceLocation RESLOC_STONE_001 = new ResourceLocation(MODID, "stone/stone001");
    private static final ResourceLocation RESLOC_STONE_002 = new ResourceLocation(MODID, "stone/stone002");
    private static final ResourceLocation RESLOC_STONE_003 = new ResourceLocation(MODID, "stone/stone003");
    private static final ResourceLocation RESLOC_STONE_010 = new ResourceLocation(MODID, "stone/stone010");
    private static final ResourceLocation RESLOC_STONE_011 = new ResourceLocation(MODID, "stone/stone011");
    private static final ResourceLocation RESLOC_STONE_012 = new ResourceLocation(MODID, "stone/stone012");
    private static final ResourceLocation RESLOC_STONE_013 = new ResourceLocation(MODID, "stone/stone013");
    private static final ResourceLocation RESLOC_STONE_020 = new ResourceLocation(MODID, "stone/stone020");
    private static final ResourceLocation RESLOC_STONE_021 = new ResourceLocation(MODID, "stone/stone021");
    private static final ResourceLocation RESLOC_STONE_022 = new ResourceLocation(MODID, "stone/stone022");
    private static final ResourceLocation RESLOC_STONE_023 = new ResourceLocation(MODID, "stone/stone023");
    private static final ResourceLocation RESLOC_STONE_030 = new ResourceLocation(MODID, "stone/stone030");
    private static final ResourceLocation RESLOC_STONE_031 = new ResourceLocation(MODID, "stone/stone031");
    private static final ResourceLocation RESLOC_STONE_032 = new ResourceLocation(MODID, "stone/stone032");
    private static final ResourceLocation RESLOC_STONE_033 = new ResourceLocation(MODID, "stone/stone033");

    private static final ResourceLocation RESLOC_STONE_100 = new ResourceLocation(MODID, "stone/stone100");
    private static final ResourceLocation RESLOC_STONE_101 = new ResourceLocation(MODID, "stone/stone101");
    private static final ResourceLocation RESLOC_STONE_102 = new ResourceLocation(MODID, "stone/stone102");
    private static final ResourceLocation RESLOC_STONE_103 = new ResourceLocation(MODID, "stone/stone103");
    private static final ResourceLocation RESLOC_STONE_110 = new ResourceLocation(MODID, "stone/stone110");
    private static final ResourceLocation RESLOC_STONE_111 = new ResourceLocation(MODID, "stone/stone111");
    private static final ResourceLocation RESLOC_STONE_112 = new ResourceLocation(MODID, "stone/stone112");
    private static final ResourceLocation RESLOC_STONE_113 = new ResourceLocation(MODID, "stone/stone113");
    private static final ResourceLocation RESLOC_STONE_120 = new ResourceLocation(MODID, "stone/stone120");
    private static final ResourceLocation RESLOC_STONE_121 = new ResourceLocation(MODID, "stone/stone121");
    private static final ResourceLocation RESLOC_STONE_122 = new ResourceLocation(MODID, "stone/stone122");
    private static final ResourceLocation RESLOC_STONE_123 = new ResourceLocation(MODID, "stone/stone123");
    private static final ResourceLocation RESLOC_STONE_130 = new ResourceLocation(MODID, "stone/stone130");
    private static final ResourceLocation RESLOC_STONE_131 = new ResourceLocation(MODID, "stone/stone131");
    private static final ResourceLocation RESLOC_STONE_132 = new ResourceLocation(MODID, "stone/stone132");
    private static final ResourceLocation RESLOC_STONE_133 = new ResourceLocation(MODID, "stone/stone133");

    private static final ResourceLocation RESLOC_STONE_200 = new ResourceLocation(MODID, "stone/stone200");
    private static final ResourceLocation RESLOC_STONE_201 = new ResourceLocation(MODID, "stone/stone201");
    private static final ResourceLocation RESLOC_STONE_202 = new ResourceLocation(MODID, "stone/stone202");
    private static final ResourceLocation RESLOC_STONE_203 = new ResourceLocation(MODID, "stone/stone203");
    private static final ResourceLocation RESLOC_STONE_210 = new ResourceLocation(MODID, "stone/stone210");
    private static final ResourceLocation RESLOC_STONE_211 = new ResourceLocation(MODID, "stone/stone211");
    private static final ResourceLocation RESLOC_STONE_212 = new ResourceLocation(MODID, "stone/stone212");
    private static final ResourceLocation RESLOC_STONE_213 = new ResourceLocation(MODID, "stone/stone213");
    private static final ResourceLocation RESLOC_STONE_220 = new ResourceLocation(MODID, "stone/stone220");
    private static final ResourceLocation RESLOC_STONE_221 = new ResourceLocation(MODID, "stone/stone221");
    private static final ResourceLocation RESLOC_STONE_222 = new ResourceLocation(MODID, "stone/stone222");
    private static final ResourceLocation RESLOC_STONE_223 = new ResourceLocation(MODID, "stone/stone223");
    private static final ResourceLocation RESLOC_STONE_230 = new ResourceLocation(MODID, "stone/stone230");
    private static final ResourceLocation RESLOC_STONE_231 = new ResourceLocation(MODID, "stone/stone231");
    private static final ResourceLocation RESLOC_STONE_232 = new ResourceLocation(MODID, "stone/stone232");
    private static final ResourceLocation RESLOC_STONE_233 = new ResourceLocation(MODID, "stone/stone233");

    private static final ResourceLocation RESLOC_STONE_300 = new ResourceLocation(MODID, "stone/stone300");
    private static final ResourceLocation RESLOC_STONE_301 = new ResourceLocation(MODID, "stone/stone301");
    private static final ResourceLocation RESLOC_STONE_302 = new ResourceLocation(MODID, "stone/stone302");
    private static final ResourceLocation RESLOC_STONE_303 = new ResourceLocation(MODID, "stone/stone303");
    private static final ResourceLocation RESLOC_STONE_310 = new ResourceLocation(MODID, "stone/stone310");
    private static final ResourceLocation RESLOC_STONE_311 = new ResourceLocation(MODID, "stone/stone311");
    private static final ResourceLocation RESLOC_STONE_312 = new ResourceLocation(MODID, "stone/stone312");
    private static final ResourceLocation RESLOC_STONE_313 = new ResourceLocation(MODID, "stone/stone313");
    private static final ResourceLocation RESLOC_STONE_320 = new ResourceLocation(MODID, "stone/stone320");
    private static final ResourceLocation RESLOC_STONE_321 = new ResourceLocation(MODID, "stone/stone321");
    private static final ResourceLocation RESLOC_STONE_322 = new ResourceLocation(MODID, "stone/stone322");
    private static final ResourceLocation RESLOC_STONE_323 = new ResourceLocation(MODID, "stone/stone323");
    private static final ResourceLocation RESLOC_STONE_330 = new ResourceLocation(MODID, "stone/stone330");
    private static final ResourceLocation RESLOC_STONE_331 = new ResourceLocation(MODID, "stone/stone331");
    private static final ResourceLocation RESLOC_STONE_332 = new ResourceLocation(MODID, "stone/stone332");
    private static final ResourceLocation RESLOC_STONE_333 = new ResourceLocation(MODID, "stone/stone333");

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(ITEM_STONE_000);
        event.getRegistry().register(ITEM_STONE_001);
        event.getRegistry().register(ITEM_STONE_002);
        event.getRegistry().register(ITEM_STONE_003);
        event.getRegistry().register(ITEM_STONE_010);
        event.getRegistry().register(ITEM_STONE_011);
        event.getRegistry().register(ITEM_STONE_012);
        event.getRegistry().register(ITEM_STONE_013);
        event.getRegistry().register(ITEM_STONE_020);
        event.getRegistry().register(ITEM_STONE_021);
        event.getRegistry().register(ITEM_STONE_022);
        event.getRegistry().register(ITEM_STONE_023);
        event.getRegistry().register(ITEM_STONE_030);
        event.getRegistry().register(ITEM_STONE_031);
        event.getRegistry().register(ITEM_STONE_032);
        event.getRegistry().register(ITEM_STONE_033);

        event.getRegistry().register(ITEM_STONE_100);
        event.getRegistry().register(ITEM_STONE_101);
        event.getRegistry().register(ITEM_STONE_102);
        event.getRegistry().register(ITEM_STONE_103);
        event.getRegistry().register(ITEM_STONE_110);
        event.getRegistry().register(ITEM_STONE_111);
        event.getRegistry().register(ITEM_STONE_112);
        event.getRegistry().register(ITEM_STONE_113);
        event.getRegistry().register(ITEM_STONE_120);
        event.getRegistry().register(ITEM_STONE_121);
        event.getRegistry().register(ITEM_STONE_122);
        event.getRegistry().register(ITEM_STONE_123);
        event.getRegistry().register(ITEM_STONE_130);
        event.getRegistry().register(ITEM_STONE_131);
        event.getRegistry().register(ITEM_STONE_132);
        event.getRegistry().register(ITEM_STONE_133);

        event.getRegistry().register(ITEM_STONE_200);
        event.getRegistry().register(ITEM_STONE_201);
        event.getRegistry().register(ITEM_STONE_202);
        event.getRegistry().register(ITEM_STONE_203);
        event.getRegistry().register(ITEM_STONE_210);
        event.getRegistry().register(ITEM_STONE_211);
        event.getRegistry().register(ITEM_STONE_212);
        event.getRegistry().register(ITEM_STONE_213);
        event.getRegistry().register(ITEM_STONE_220);
        event.getRegistry().register(ITEM_STONE_221);
        event.getRegistry().register(ITEM_STONE_222);
        event.getRegistry().register(ITEM_STONE_223);
        event.getRegistry().register(ITEM_STONE_230);
        event.getRegistry().register(ITEM_STONE_231);
        event.getRegistry().register(ITEM_STONE_232);
        event.getRegistry().register(ITEM_STONE_233);

        event.getRegistry().register(ITEM_STONE_300);
        event.getRegistry().register(ITEM_STONE_301);
        event.getRegistry().register(ITEM_STONE_302);
        event.getRegistry().register(ITEM_STONE_303);
        event.getRegistry().register(ITEM_STONE_310);
        event.getRegistry().register(ITEM_STONE_311);
        event.getRegistry().register(ITEM_STONE_312);
        event.getRegistry().register(ITEM_STONE_313);
        event.getRegistry().register(ITEM_STONE_320);
        event.getRegistry().register(ITEM_STONE_321);
        event.getRegistry().register(ITEM_STONE_322);
        event.getRegistry().register(ITEM_STONE_323);
        event.getRegistry().register(ITEM_STONE_330);
        event.getRegistry().register(ITEM_STONE_331);
        event.getRegistry().register(ITEM_STONE_332);
        event.getRegistry().register(ITEM_STONE_333);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(STONE_000);
        event.getRegistry().register(STONE_001);
        event.getRegistry().register(STONE_002);
        event.getRegistry().register(STONE_003);
        event.getRegistry().register(STONE_010);
        event.getRegistry().register(STONE_011);
        event.getRegistry().register(STONE_012);
        event.getRegistry().register(STONE_013);
        event.getRegistry().register(STONE_020);
        event.getRegistry().register(STONE_021);
        event.getRegistry().register(STONE_022);
        event.getRegistry().register(STONE_023);
        event.getRegistry().register(STONE_030);
        event.getRegistry().register(STONE_031);
        event.getRegistry().register(STONE_032);
        event.getRegistry().register(STONE_033);

        event.getRegistry().register(STONE_100);
        event.getRegistry().register(STONE_101);
        event.getRegistry().register(STONE_102);
        event.getRegistry().register(STONE_103);
        event.getRegistry().register(STONE_110);
        event.getRegistry().register(STONE_111);
        event.getRegistry().register(STONE_112);
        event.getRegistry().register(STONE_113);
        event.getRegistry().register(STONE_120);
        event.getRegistry().register(STONE_121);
        event.getRegistry().register(STONE_122);
        event.getRegistry().register(STONE_123);
        event.getRegistry().register(STONE_130);
        event.getRegistry().register(STONE_131);
        event.getRegistry().register(STONE_132);
        event.getRegistry().register(STONE_133);

        event.getRegistry().register(STONE_200);
        event.getRegistry().register(STONE_201);
        event.getRegistry().register(STONE_202);
        event.getRegistry().register(STONE_203);
        event.getRegistry().register(STONE_210);
        event.getRegistry().register(STONE_211);
        event.getRegistry().register(STONE_212);
        event.getRegistry().register(STONE_213);
        event.getRegistry().register(STONE_220);
        event.getRegistry().register(STONE_221);
        event.getRegistry().register(STONE_222);
        event.getRegistry().register(STONE_223);
        event.getRegistry().register(STONE_230);
        event.getRegistry().register(STONE_231);
        event.getRegistry().register(STONE_232);
        event.getRegistry().register(STONE_233);

        event.getRegistry().register(STONE_300);
        event.getRegistry().register(STONE_301);
        event.getRegistry().register(STONE_302);
        event.getRegistry().register(STONE_303);
        event.getRegistry().register(STONE_310);
        event.getRegistry().register(STONE_311);
        event.getRegistry().register(STONE_312);
        event.getRegistry().register(STONE_313);
        event.getRegistry().register(STONE_320);
        event.getRegistry().register(STONE_321);
        event.getRegistry().register(STONE_322);
        event.getRegistry().register(STONE_323);
        event.getRegistry().register(STONE_330);
        event.getRegistry().register(STONE_331);
        event.getRegistry().register(STONE_332);
        event.getRegistry().register(STONE_333);
    }



    public static void handleBlockColors(ColorHandlerEvent.Block event) {

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 0;} else {return -1;}},STONE_000);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 85;} else {return -1;}},STONE_001);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 170;} else {return -1;}},STONE_002);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 255;} else {return -1;}},STONE_003);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21760;} else {return -1;}},STONE_010);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21845;} else {return -1;}},STONE_011);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21930;} else {return -1;}},STONE_012);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 22015;} else {return -1;}},STONE_013);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43520;} else {return -1;}},STONE_020);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43605;} else {return -1;}},STONE_021);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43690;} else {return -1;}},STONE_022);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43775;} else {return -1;}},STONE_023);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65280;} else {return -1;}},STONE_030);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65365;} else {return -1;}},STONE_031);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65450;} else {return -1;}},STONE_032);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65535;} else {return -1;}},STONE_033);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570560;} else {return -1;}},STONE_100);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570645;} else {return -1;}},STONE_101);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570730;} else {return -1;}},STONE_102);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570815;} else {return -1;}},STONE_103);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592320;} else {return -1;}},STONE_110);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592405;} else {return -1;}},STONE_111);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592490;} else {return -1;}},STONE_112);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592575;} else {return -1;}},STONE_113);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614080;} else {return -1;}},STONE_120);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614165;} else {return -1;}},STONE_121);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614250;} else {return -1;}},STONE_122);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614335;} else {return -1;}},STONE_123);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5635840;} else {return -1;}},STONE_130);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5635925;} else {return -1;}},STONE_131);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5636010;} else {return -1;}},STONE_132);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5636095;} else {return -1;}},STONE_133);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141120;} else {return -1;}},STONE_200);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141205;} else {return -1;}},STONE_201);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141290;} else {return -1;}},STONE_202);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141375;} else {return -1;}},STONE_203);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11162880;} else {return -1;}},STONE_210);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11162965;} else {return -1;}},STONE_211);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11163050;} else {return -1;}},STONE_212);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11163135;} else {return -1;}},STONE_213);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184640;} else {return -1;}},STONE_220);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184725;} else {return -1;}},STONE_221);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184810;} else {return -1;}},STONE_222);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184895;} else {return -1;}},STONE_223);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206400;} else {return -1;}},STONE_230);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206485;} else {return -1;}},STONE_231);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206570;} else {return -1;}},STONE_232);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206655;} else {return -1;}},STONE_233);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711680;} else {return -1;}},STONE_300);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711765;} else {return -1;}},STONE_301);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711850;} else {return -1;}},STONE_302);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711935;} else {return -1;}},STONE_303);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733440;} else {return -1;}},STONE_310);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733525;} else {return -1;}},STONE_311);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733610;} else {return -1;}},STONE_312);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733695;} else {return -1;}},STONE_313);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755200;} else {return -1;}},STONE_320);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755285;} else {return -1;}},STONE_321);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755370;} else {return -1;}},STONE_322);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755455;} else {return -1;}},STONE_323);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16776960;} else {return -1;}},STONE_330);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777045;} else {return -1;}},STONE_331);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777130;} else {return -1;}},STONE_332);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777215;} else {return -1;}},STONE_333);
    }

    public static void handleItemColors(ColorHandlerEvent.Item event) {

        //event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 0;} else {return -1;}},ITEM_STONE_000);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 0;} else {return -1;}},ITEM_STONE_000);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 85;} else {return -1;}},ITEM_STONE_001);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 170;} else {return -1;}},ITEM_STONE_002);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 255;} else {return -1;}},ITEM_STONE_003);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21760;} else {return -1;}},ITEM_STONE_010);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21845;} else {return -1;}},ITEM_STONE_011);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21930;} else {return -1;}},ITEM_STONE_012);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 22015;} else {return -1;}},ITEM_STONE_013);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43520;} else {return -1;}},ITEM_STONE_020);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43605;} else {return -1;}},ITEM_STONE_021);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43690;} else {return -1;}},ITEM_STONE_022);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43775;} else {return -1;}},ITEM_STONE_023);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65280;} else {return -1;}},ITEM_STONE_030);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65365;} else {return -1;}},ITEM_STONE_031);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65450;} else {return -1;}},ITEM_STONE_032);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65535;} else {return -1;}},ITEM_STONE_033);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570560;} else {return -1;}},ITEM_STONE_100);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570645;} else {return -1;}},ITEM_STONE_101);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570730;} else {return -1;}},ITEM_STONE_102);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570815;} else {return -1;}},ITEM_STONE_103);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592320;} else {return -1;}},ITEM_STONE_110);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592405;} else {return -1;}},ITEM_STONE_111);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592490;} else {return -1;}},ITEM_STONE_112);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592575;} else {return -1;}},ITEM_STONE_113);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614080;} else {return -1;}},ITEM_STONE_120);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614165;} else {return -1;}},ITEM_STONE_121);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614250;} else {return -1;}},ITEM_STONE_122);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614335;} else {return -1;}},ITEM_STONE_123);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5635840;} else {return -1;}},ITEM_STONE_130);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5635925;} else {return -1;}},ITEM_STONE_131);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5636010;} else {return -1;}},ITEM_STONE_132);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5636095;} else {return -1;}},ITEM_STONE_133);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141120;} else {return -1;}},ITEM_STONE_200);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141205;} else {return -1;}},ITEM_STONE_201);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141290;} else {return -1;}},ITEM_STONE_202);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141375;} else {return -1;}},ITEM_STONE_203);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11162880;} else {return -1;}},ITEM_STONE_210);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11162965;} else {return -1;}},ITEM_STONE_211);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11163050;} else {return -1;}},ITEM_STONE_212);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11163135;} else {return -1;}},ITEM_STONE_213);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184640;} else {return -1;}},ITEM_STONE_220);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184725;} else {return -1;}},ITEM_STONE_221);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184810;} else {return -1;}},ITEM_STONE_222);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184895;} else {return -1;}},ITEM_STONE_223);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206400;} else {return -1;}},ITEM_STONE_230);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206485;} else {return -1;}},ITEM_STONE_231);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206570;} else {return -1;}},ITEM_STONE_232);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206655;} else {return -1;}},ITEM_STONE_233);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711680;} else {return -1;}},ITEM_STONE_300);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711765;} else {return -1;}},ITEM_STONE_301);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711850;} else {return -1;}},ITEM_STONE_302);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711935;} else {return -1;}},ITEM_STONE_303);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733440;} else {return -1;}},ITEM_STONE_310);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733525;} else {return -1;}},ITEM_STONE_311);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733610;} else {return -1;}},ITEM_STONE_312);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733695;} else {return -1;}},ITEM_STONE_313);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755200;} else {return -1;}},ITEM_STONE_320);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755285;} else {return -1;}},ITEM_STONE_321);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755370;} else {return -1;}},ITEM_STONE_322);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755455;} else {return -1;}},ITEM_STONE_323);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16776960;} else {return -1;}},ITEM_STONE_330);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777045;} else {return -1;}},ITEM_STONE_331);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777130;} else {return -1;}},ITEM_STONE_332);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777215;} else {return -1;}},ITEM_STONE_333);
    }

    public static final Block STONE_000 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_000);
    public static final Block STONE_001 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_001);
    public static final Block STONE_002 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_002);
    public static final Block STONE_003 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_003);
    public static final Block STONE_010 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_010);
    public static final Block STONE_011 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_011);
    public static final Block STONE_012 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_012);
    public static final Block STONE_013 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_013);
    public static final Block STONE_020 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_020);
    public static final Block STONE_021 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_021);
    public static final Block STONE_022 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_022);
    public static final Block STONE_023 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_023);
    public static final Block STONE_030 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_030);
    public static final Block STONE_031 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_031);
    public static final Block STONE_032 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_032);
    public static final Block STONE_033 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_033);

    public static final Block STONE_100 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_100);
    public static final Block STONE_101 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_101);
    public static final Block STONE_102 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_102);
    public static final Block STONE_103 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_103);
    public static final Block STONE_110 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_110);
    public static final Block STONE_111 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_111);
    public static final Block STONE_112 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_112);
    public static final Block STONE_113 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_113);
    public static final Block STONE_120 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_120);
    public static final Block STONE_121 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_121);
    public static final Block STONE_122 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_122);
    public static final Block STONE_123 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_123);
    public static final Block STONE_130 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_130);
    public static final Block STONE_131 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_131);
    public static final Block STONE_132 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_132);
    public static final Block STONE_133 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_133);

    public static final Block STONE_200 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_200);
    public static final Block STONE_201 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_201);
    public static final Block STONE_202 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_202);
    public static final Block STONE_203 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_203);
    public static final Block STONE_210 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_210);
    public static final Block STONE_211 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_211);
    public static final Block STONE_212 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_212);
    public static final Block STONE_213 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_213);
    public static final Block STONE_220 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_220);
    public static final Block STONE_221 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_221);
    public static final Block STONE_222 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_222);
    public static final Block STONE_223 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_223);
    public static final Block STONE_230 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_230);
    public static final Block STONE_231 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_231);
    public static final Block STONE_232 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_232);
    public static final Block STONE_233 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_233);

    public static final Block STONE_300 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_300);
    public static final Block STONE_301 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_301);
    public static final Block STONE_302 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_302);
    public static final Block STONE_303 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_303);
    public static final Block STONE_310 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_310);
    public static final Block STONE_311 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_311);
    public static final Block STONE_312 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_312);
    public static final Block STONE_313 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_313);
    public static final Block STONE_320 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_320);
    public static final Block STONE_321 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_321);
    public static final Block STONE_322 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_322);
    public static final Block STONE_323 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_323);
    public static final Block STONE_330 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_330);
    public static final Block STONE_331 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_331);
    public static final Block STONE_332 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_332);
    public static final Block STONE_333 = new BlockDustStone(Block.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_STONE_333);

    public static final Item ITEM_STONE_000 = new BlockItem(STONE_000, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_000);
    public static final Item ITEM_STONE_001 = new BlockItem(STONE_001, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_001);
    public static final Item ITEM_STONE_002 = new BlockItem(STONE_002, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_002);
    public static final Item ITEM_STONE_003 = new BlockItem(STONE_003, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_003);
    public static final Item ITEM_STONE_010 = new BlockItem(STONE_010, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_010);
    public static final Item ITEM_STONE_011 = new BlockItem(STONE_011, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_011);
    public static final Item ITEM_STONE_012 = new BlockItem(STONE_012, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_012);
    public static final Item ITEM_STONE_013 = new BlockItem(STONE_013, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_013);
    public static final Item ITEM_STONE_020 = new BlockItem(STONE_020, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_020);
    public static final Item ITEM_STONE_021 = new BlockItem(STONE_021, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_021);
    public static final Item ITEM_STONE_022 = new BlockItem(STONE_022, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_022);
    public static final Item ITEM_STONE_023 = new BlockItem(STONE_023, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_023);
    public static final Item ITEM_STONE_030 = new BlockItem(STONE_030, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_030);
    public static final Item ITEM_STONE_031 = new BlockItem(STONE_031, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_031);
    public static final Item ITEM_STONE_032 = new BlockItem(STONE_032, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_032);
    public static final Item ITEM_STONE_033 = new BlockItem(STONE_033, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_033);

    public static final Item ITEM_STONE_100 = new BlockItem(STONE_100, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_100);
    public static final Item ITEM_STONE_101 = new BlockItem(STONE_101, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_101);
    public static final Item ITEM_STONE_102 = new BlockItem(STONE_102, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_102);
    public static final Item ITEM_STONE_103 = new BlockItem(STONE_103, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_103);
    public static final Item ITEM_STONE_110 = new BlockItem(STONE_110, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_110);
    public static final Item ITEM_STONE_111 = new BlockItem(STONE_111, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_111);
    public static final Item ITEM_STONE_112 = new BlockItem(STONE_112, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_112);
    public static final Item ITEM_STONE_113 = new BlockItem(STONE_113, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_113);
    public static final Item ITEM_STONE_120 = new BlockItem(STONE_120, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_120);
    public static final Item ITEM_STONE_121 = new BlockItem(STONE_121, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_121);
    public static final Item ITEM_STONE_122 = new BlockItem(STONE_122, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_122);
    public static final Item ITEM_STONE_123 = new BlockItem(STONE_123, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_123);
    public static final Item ITEM_STONE_130 = new BlockItem(STONE_130, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_130);
    public static final Item ITEM_STONE_131 = new BlockItem(STONE_131, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_131);
    public static final Item ITEM_STONE_132 = new BlockItem(STONE_132, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_132);
    public static final Item ITEM_STONE_133 = new BlockItem(STONE_133, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_133);

    public static final Item ITEM_STONE_200 = new BlockItem(STONE_200, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_200);
    public static final Item ITEM_STONE_201 = new BlockItem(STONE_201, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_201);
    public static final Item ITEM_STONE_202 = new BlockItem(STONE_202, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_202);
    public static final Item ITEM_STONE_203 = new BlockItem(STONE_203, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_203);
    public static final Item ITEM_STONE_210 = new BlockItem(STONE_210, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_210);
    public static final Item ITEM_STONE_211 = new BlockItem(STONE_211, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_211);
    public static final Item ITEM_STONE_212 = new BlockItem(STONE_212, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_212);
    public static final Item ITEM_STONE_213 = new BlockItem(STONE_213, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_213);
    public static final Item ITEM_STONE_220 = new BlockItem(STONE_220, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_220);
    public static final Item ITEM_STONE_221 = new BlockItem(STONE_221, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_221);
    public static final Item ITEM_STONE_222 = new BlockItem(STONE_222, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_222);
    public static final Item ITEM_STONE_223 = new BlockItem(STONE_223, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_223);
    public static final Item ITEM_STONE_230 = new BlockItem(STONE_230, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_230);
    public static final Item ITEM_STONE_231 = new BlockItem(STONE_231, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_231);
    public static final Item ITEM_STONE_232 = new BlockItem(STONE_232, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_232);
    public static final Item ITEM_STONE_233 = new BlockItem(STONE_233, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_233);

    public static final Item ITEM_STONE_300 = new BlockItem(STONE_300, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_300);
    public static final Item ITEM_STONE_301 = new BlockItem(STONE_301, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_301);
    public static final Item ITEM_STONE_302 = new BlockItem(STONE_302, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_302);
    public static final Item ITEM_STONE_303 = new BlockItem(STONE_303, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_303);
    public static final Item ITEM_STONE_310 = new BlockItem(STONE_310, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_310);
    public static final Item ITEM_STONE_311 = new BlockItem(STONE_311, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_311);
    public static final Item ITEM_STONE_312 = new BlockItem(STONE_312, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_312);
    public static final Item ITEM_STONE_313 = new BlockItem(STONE_313, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_313);
    public static final Item ITEM_STONE_320 = new BlockItem(STONE_320, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_320);
    public static final Item ITEM_STONE_321 = new BlockItem(STONE_321, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_321);
    public static final Item ITEM_STONE_322 = new BlockItem(STONE_322, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_322);
    public static final Item ITEM_STONE_323 = new BlockItem(STONE_323, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_323);
    public static final Item ITEM_STONE_330 = new BlockItem(STONE_330, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_330);
    public static final Item ITEM_STONE_331 = new BlockItem(STONE_331, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_331);
    public static final Item ITEM_STONE_332 = new BlockItem(STONE_332, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_332);
    public static final Item ITEM_STONE_333 = new BlockItem(STONE_333, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_STONE_333);
}
