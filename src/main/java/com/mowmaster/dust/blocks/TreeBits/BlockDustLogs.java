package com.mowmaster.dust.blocks.TreeBits;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.mowmaster.dust.dust.BLOCK_GROUP;
import static com.mowmaster.dust.references.Reference.MODID;


public class BlockDustLogs extends LogBlock
{
    public BlockDustLogs(MaterialColor color,Properties builder) {
        super(color,builder);
    }

    private static final ResourceLocation RESLOC_LOGS_000 = new ResourceLocation(MODID, "woodbits/log/logs000");
    private static final ResourceLocation RESLOC_LOGS_001 = new ResourceLocation(MODID, "woodbits/log/logs001");
    private static final ResourceLocation RESLOC_LOGS_002 = new ResourceLocation(MODID, "woodbits/log/logs002");
    private static final ResourceLocation RESLOC_LOGS_003 = new ResourceLocation(MODID, "woodbits/log/logs003");
    private static final ResourceLocation RESLOC_LOGS_010 = new ResourceLocation(MODID, "woodbits/log/logs010");
    private static final ResourceLocation RESLOC_LOGS_011 = new ResourceLocation(MODID, "woodbits/log/logs011");
    private static final ResourceLocation RESLOC_LOGS_012 = new ResourceLocation(MODID, "woodbits/log/logs012");
    private static final ResourceLocation RESLOC_LOGS_013 = new ResourceLocation(MODID, "woodbits/log/logs013");
    private static final ResourceLocation RESLOC_LOGS_020 = new ResourceLocation(MODID, "woodbits/log/logs020");
    private static final ResourceLocation RESLOC_LOGS_021 = new ResourceLocation(MODID, "woodbits/log/logs021");
    private static final ResourceLocation RESLOC_LOGS_022 = new ResourceLocation(MODID, "woodbits/log/logs022");
    private static final ResourceLocation RESLOC_LOGS_023 = new ResourceLocation(MODID, "woodbits/log/logs023");
    private static final ResourceLocation RESLOC_LOGS_030 = new ResourceLocation(MODID, "woodbits/log/logs030");
    private static final ResourceLocation RESLOC_LOGS_031 = new ResourceLocation(MODID, "woodbits/log/logs031");
    private static final ResourceLocation RESLOC_LOGS_032 = new ResourceLocation(MODID, "woodbits/log/logs032");
    private static final ResourceLocation RESLOC_LOGS_033 = new ResourceLocation(MODID, "woodbits/log/logs033");

    private static final ResourceLocation RESLOC_LOGS_100 = new ResourceLocation(MODID, "woodbits/log/logs100");
    private static final ResourceLocation RESLOC_LOGS_101 = new ResourceLocation(MODID, "woodbits/log/logs101");
    private static final ResourceLocation RESLOC_LOGS_102 = new ResourceLocation(MODID, "woodbits/log/logs102");
    private static final ResourceLocation RESLOC_LOGS_103 = new ResourceLocation(MODID, "woodbits/log/logs103");
    private static final ResourceLocation RESLOC_LOGS_110 = new ResourceLocation(MODID, "woodbits/log/logs110");
    private static final ResourceLocation RESLOC_LOGS_111 = new ResourceLocation(MODID, "woodbits/log/logs111");
    private static final ResourceLocation RESLOC_LOGS_112 = new ResourceLocation(MODID, "woodbits/log/logs112");
    private static final ResourceLocation RESLOC_LOGS_113 = new ResourceLocation(MODID, "woodbits/log/logs113");
    private static final ResourceLocation RESLOC_LOGS_120 = new ResourceLocation(MODID, "woodbits/log/logs120");
    private static final ResourceLocation RESLOC_LOGS_121 = new ResourceLocation(MODID, "woodbits/log/logs121");
    private static final ResourceLocation RESLOC_LOGS_122 = new ResourceLocation(MODID, "woodbits/log/logs122");
    private static final ResourceLocation RESLOC_LOGS_123 = new ResourceLocation(MODID, "woodbits/log/logs123");
    private static final ResourceLocation RESLOC_LOGS_130 = new ResourceLocation(MODID, "woodbits/log/logs130");
    private static final ResourceLocation RESLOC_LOGS_131 = new ResourceLocation(MODID, "woodbits/log/logs131");
    private static final ResourceLocation RESLOC_LOGS_132 = new ResourceLocation(MODID, "woodbits/log/logs132");
    private static final ResourceLocation RESLOC_LOGS_133 = new ResourceLocation(MODID, "woodbits/log/logs133");

    private static final ResourceLocation RESLOC_LOGS_200 = new ResourceLocation(MODID, "woodbits/log/logs200");
    private static final ResourceLocation RESLOC_LOGS_201 = new ResourceLocation(MODID, "woodbits/log/logs201");
    private static final ResourceLocation RESLOC_LOGS_202 = new ResourceLocation(MODID, "woodbits/log/logs202");
    private static final ResourceLocation RESLOC_LOGS_203 = new ResourceLocation(MODID, "woodbits/log/logs203");
    private static final ResourceLocation RESLOC_LOGS_210 = new ResourceLocation(MODID, "woodbits/log/logs210");
    private static final ResourceLocation RESLOC_LOGS_211 = new ResourceLocation(MODID, "woodbits/log/logs211");
    private static final ResourceLocation RESLOC_LOGS_212 = new ResourceLocation(MODID, "woodbits/log/logs212");
    private static final ResourceLocation RESLOC_LOGS_213 = new ResourceLocation(MODID, "woodbits/log/logs213");
    private static final ResourceLocation RESLOC_LOGS_220 = new ResourceLocation(MODID, "woodbits/log/logs220");
    private static final ResourceLocation RESLOC_LOGS_221 = new ResourceLocation(MODID, "woodbits/log/logs221");
    private static final ResourceLocation RESLOC_LOGS_222 = new ResourceLocation(MODID, "woodbits/log/logs222");
    private static final ResourceLocation RESLOC_LOGS_223 = new ResourceLocation(MODID, "woodbits/log/logs223");
    private static final ResourceLocation RESLOC_LOGS_230 = new ResourceLocation(MODID, "woodbits/log/logs230");
    private static final ResourceLocation RESLOC_LOGS_231 = new ResourceLocation(MODID, "woodbits/log/logs231");
    private static final ResourceLocation RESLOC_LOGS_232 = new ResourceLocation(MODID, "woodbits/log/logs232");
    private static final ResourceLocation RESLOC_LOGS_233 = new ResourceLocation(MODID, "woodbits/log/logs233");

    private static final ResourceLocation RESLOC_LOGS_300 = new ResourceLocation(MODID, "woodbits/log/logs300");
    private static final ResourceLocation RESLOC_LOGS_301 = new ResourceLocation(MODID, "woodbits/log/logs301");
    private static final ResourceLocation RESLOC_LOGS_302 = new ResourceLocation(MODID, "woodbits/log/logs302");
    private static final ResourceLocation RESLOC_LOGS_303 = new ResourceLocation(MODID, "woodbits/log/logs303");
    private static final ResourceLocation RESLOC_LOGS_310 = new ResourceLocation(MODID, "woodbits/log/logs310");
    private static final ResourceLocation RESLOC_LOGS_311 = new ResourceLocation(MODID, "woodbits/log/logs311");
    private static final ResourceLocation RESLOC_LOGS_312 = new ResourceLocation(MODID, "woodbits/log/logs312");
    private static final ResourceLocation RESLOC_LOGS_313 = new ResourceLocation(MODID, "woodbits/log/logs313");
    private static final ResourceLocation RESLOC_LOGS_320 = new ResourceLocation(MODID, "woodbits/log/logs320");
    private static final ResourceLocation RESLOC_LOGS_321 = new ResourceLocation(MODID, "woodbits/log/logs321");
    private static final ResourceLocation RESLOC_LOGS_322 = new ResourceLocation(MODID, "woodbits/log/logs322");
    private static final ResourceLocation RESLOC_LOGS_323 = new ResourceLocation(MODID, "woodbits/log/logs323");
    private static final ResourceLocation RESLOC_LOGS_330 = new ResourceLocation(MODID, "woodbits/log/logs330");
    private static final ResourceLocation RESLOC_LOGS_331 = new ResourceLocation(MODID, "woodbits/log/logs331");
    private static final ResourceLocation RESLOC_LOGS_332 = new ResourceLocation(MODID, "woodbits/log/logs332");
    private static final ResourceLocation RESLOC_LOGS_333 = new ResourceLocation(MODID, "woodbits/log/logs333");

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(ITEM_LOGS_000);
        event.getRegistry().register(ITEM_LOGS_001);
        event.getRegistry().register(ITEM_LOGS_002);
        event.getRegistry().register(ITEM_LOGS_003);
        event.getRegistry().register(ITEM_LOGS_010);
        event.getRegistry().register(ITEM_LOGS_011);
        event.getRegistry().register(ITEM_LOGS_012);
        event.getRegistry().register(ITEM_LOGS_013);
        event.getRegistry().register(ITEM_LOGS_020);
        event.getRegistry().register(ITEM_LOGS_021);
        event.getRegistry().register(ITEM_LOGS_022);
        event.getRegistry().register(ITEM_LOGS_023);
        event.getRegistry().register(ITEM_LOGS_030);
        event.getRegistry().register(ITEM_LOGS_031);
        event.getRegistry().register(ITEM_LOGS_032);
        event.getRegistry().register(ITEM_LOGS_033);

        event.getRegistry().register(ITEM_LOGS_100);
        event.getRegistry().register(ITEM_LOGS_101);
        event.getRegistry().register(ITEM_LOGS_102);
        event.getRegistry().register(ITEM_LOGS_103);
        event.getRegistry().register(ITEM_LOGS_110);
        event.getRegistry().register(ITEM_LOGS_111);
        event.getRegistry().register(ITEM_LOGS_112);
        event.getRegistry().register(ITEM_LOGS_113);
        event.getRegistry().register(ITEM_LOGS_120);
        event.getRegistry().register(ITEM_LOGS_121);
        event.getRegistry().register(ITEM_LOGS_122);
        event.getRegistry().register(ITEM_LOGS_123);
        event.getRegistry().register(ITEM_LOGS_130);
        event.getRegistry().register(ITEM_LOGS_131);
        event.getRegistry().register(ITEM_LOGS_132);
        event.getRegistry().register(ITEM_LOGS_133);

        event.getRegistry().register(ITEM_LOGS_200);
        event.getRegistry().register(ITEM_LOGS_201);
        event.getRegistry().register(ITEM_LOGS_202);
        event.getRegistry().register(ITEM_LOGS_203);
        event.getRegistry().register(ITEM_LOGS_210);
        event.getRegistry().register(ITEM_LOGS_211);
        event.getRegistry().register(ITEM_LOGS_212);
        event.getRegistry().register(ITEM_LOGS_213);
        event.getRegistry().register(ITEM_LOGS_220);
        event.getRegistry().register(ITEM_LOGS_221);
        event.getRegistry().register(ITEM_LOGS_222);
        event.getRegistry().register(ITEM_LOGS_223);
        event.getRegistry().register(ITEM_LOGS_230);
        event.getRegistry().register(ITEM_LOGS_231);
        event.getRegistry().register(ITEM_LOGS_232);
        event.getRegistry().register(ITEM_LOGS_233);

        event.getRegistry().register(ITEM_LOGS_300);
        event.getRegistry().register(ITEM_LOGS_301);
        event.getRegistry().register(ITEM_LOGS_302);
        event.getRegistry().register(ITEM_LOGS_303);
        event.getRegistry().register(ITEM_LOGS_310);
        event.getRegistry().register(ITEM_LOGS_311);
        event.getRegistry().register(ITEM_LOGS_312);
        event.getRegistry().register(ITEM_LOGS_313);
        event.getRegistry().register(ITEM_LOGS_320);
        event.getRegistry().register(ITEM_LOGS_321);
        event.getRegistry().register(ITEM_LOGS_322);
        event.getRegistry().register(ITEM_LOGS_323);
        event.getRegistry().register(ITEM_LOGS_330);
        event.getRegistry().register(ITEM_LOGS_331);
        event.getRegistry().register(ITEM_LOGS_332);
        event.getRegistry().register(ITEM_LOGS_333);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(LOGS_000);
        event.getRegistry().register(LOGS_001);
        event.getRegistry().register(LOGS_002);
        event.getRegistry().register(LOGS_003);
        event.getRegistry().register(LOGS_010);
        event.getRegistry().register(LOGS_011);
        event.getRegistry().register(LOGS_012);
        event.getRegistry().register(LOGS_013);
        event.getRegistry().register(LOGS_020);
        event.getRegistry().register(LOGS_021);
        event.getRegistry().register(LOGS_022);
        event.getRegistry().register(LOGS_023);
        event.getRegistry().register(LOGS_030);
        event.getRegistry().register(LOGS_031);
        event.getRegistry().register(LOGS_032);
        event.getRegistry().register(LOGS_033);

        event.getRegistry().register(LOGS_100);
        event.getRegistry().register(LOGS_101);
        event.getRegistry().register(LOGS_102);
        event.getRegistry().register(LOGS_103);
        event.getRegistry().register(LOGS_110);
        event.getRegistry().register(LOGS_111);
        event.getRegistry().register(LOGS_112);
        event.getRegistry().register(LOGS_113);
        event.getRegistry().register(LOGS_120);
        event.getRegistry().register(LOGS_121);
        event.getRegistry().register(LOGS_122);
        event.getRegistry().register(LOGS_123);
        event.getRegistry().register(LOGS_130);
        event.getRegistry().register(LOGS_131);
        event.getRegistry().register(LOGS_132);
        event.getRegistry().register(LOGS_133);

        event.getRegistry().register(LOGS_200);
        event.getRegistry().register(LOGS_201);
        event.getRegistry().register(LOGS_202);
        event.getRegistry().register(LOGS_203);
        event.getRegistry().register(LOGS_210);
        event.getRegistry().register(LOGS_211);
        event.getRegistry().register(LOGS_212);
        event.getRegistry().register(LOGS_213);
        event.getRegistry().register(LOGS_220);
        event.getRegistry().register(LOGS_221);
        event.getRegistry().register(LOGS_222);
        event.getRegistry().register(LOGS_223);
        event.getRegistry().register(LOGS_230);
        event.getRegistry().register(LOGS_231);
        event.getRegistry().register(LOGS_232);
        event.getRegistry().register(LOGS_233);

        event.getRegistry().register(LOGS_300);
        event.getRegistry().register(LOGS_301);
        event.getRegistry().register(LOGS_302);
        event.getRegistry().register(LOGS_303);
        event.getRegistry().register(LOGS_310);
        event.getRegistry().register(LOGS_311);
        event.getRegistry().register(LOGS_312);
        event.getRegistry().register(LOGS_313);
        event.getRegistry().register(LOGS_320);
        event.getRegistry().register(LOGS_321);
        event.getRegistry().register(LOGS_322);
        event.getRegistry().register(LOGS_323);
        event.getRegistry().register(LOGS_330);
        event.getRegistry().register(LOGS_331);
        event.getRegistry().register(LOGS_332);
        event.getRegistry().register(LOGS_333);
    }



    public static void handleBlockColors(ColorHandlerEvent.Block event) {

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 0;} else {return -1;}},LOGS_000);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 85;} else {return -1;}},LOGS_001);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 170;} else {return -1;}},LOGS_002);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 255;} else {return -1;}},LOGS_003);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21760;} else {return -1;}},LOGS_010);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21845;} else {return -1;}},LOGS_011);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21930;} else {return -1;}},LOGS_012);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 22015;} else {return -1;}},LOGS_013);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43520;} else {return -1;}},LOGS_020);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43605;} else {return -1;}},LOGS_021);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43690;} else {return -1;}},LOGS_022);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43775;} else {return -1;}},LOGS_023);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65280;} else {return -1;}},LOGS_030);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65365;} else {return -1;}},LOGS_031);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65450;} else {return -1;}},LOGS_032);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65535;} else {return -1;}},LOGS_033);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570560;} else {return -1;}},LOGS_100);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570645;} else {return -1;}},LOGS_101);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570730;} else {return -1;}},LOGS_102);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570815;} else {return -1;}},LOGS_103);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592320;} else {return -1;}},LOGS_110);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592405;} else {return -1;}},LOGS_111);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592490;} else {return -1;}},LOGS_112);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592575;} else {return -1;}},LOGS_113);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614080;} else {return -1;}},LOGS_120);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614165;} else {return -1;}},LOGS_121);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614250;} else {return -1;}},LOGS_122);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614335;} else {return -1;}},LOGS_123);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5635840;} else {return -1;}},LOGS_130);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5635925;} else {return -1;}},LOGS_131);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5636010;} else {return -1;}},LOGS_132);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5636095;} else {return -1;}},LOGS_133);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141120;} else {return -1;}},LOGS_200);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141205;} else {return -1;}},LOGS_201);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141290;} else {return -1;}},LOGS_202);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141375;} else {return -1;}},LOGS_203);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11162880;} else {return -1;}},LOGS_210);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11162965;} else {return -1;}},LOGS_211);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11163050;} else {return -1;}},LOGS_212);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11163135;} else {return -1;}},LOGS_213);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184640;} else {return -1;}},LOGS_220);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184725;} else {return -1;}},LOGS_221);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184810;} else {return -1;}},LOGS_222);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184895;} else {return -1;}},LOGS_223);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206400;} else {return -1;}},LOGS_230);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206485;} else {return -1;}},LOGS_231);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206570;} else {return -1;}},LOGS_232);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206655;} else {return -1;}},LOGS_233);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711680;} else {return -1;}},LOGS_300);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711765;} else {return -1;}},LOGS_301);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711850;} else {return -1;}},LOGS_302);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711935;} else {return -1;}},LOGS_303);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733440;} else {return -1;}},LOGS_310);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733525;} else {return -1;}},LOGS_311);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733610;} else {return -1;}},LOGS_312);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733695;} else {return -1;}},LOGS_313);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755200;} else {return -1;}},LOGS_320);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755285;} else {return -1;}},LOGS_321);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755370;} else {return -1;}},LOGS_322);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755455;} else {return -1;}},LOGS_323);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16776960;} else {return -1;}},LOGS_330);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777045;} else {return -1;}},LOGS_331);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777130;} else {return -1;}},LOGS_332);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777215;} else {return -1;}},LOGS_333);
    }

    public static void handleItemColors(ColorHandlerEvent.Item event) {

        //event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 0;} else {return -1;}},ITEM_LOGS_000);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 0;} else {return -1;}},ITEM_LOGS_000);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 85;} else {return -1;}},ITEM_LOGS_001);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 170;} else {return -1;}},ITEM_LOGS_002);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 255;} else {return -1;}},ITEM_LOGS_003);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21760;} else {return -1;}},ITEM_LOGS_010);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21845;} else {return -1;}},ITEM_LOGS_011);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21930;} else {return -1;}},ITEM_LOGS_012);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 22015;} else {return -1;}},ITEM_LOGS_013);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43520;} else {return -1;}},ITEM_LOGS_020);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43605;} else {return -1;}},ITEM_LOGS_021);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43690;} else {return -1;}},ITEM_LOGS_022);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43775;} else {return -1;}},ITEM_LOGS_023);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65280;} else {return -1;}},ITEM_LOGS_030);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65365;} else {return -1;}},ITEM_LOGS_031);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65450;} else {return -1;}},ITEM_LOGS_032);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65535;} else {return -1;}},ITEM_LOGS_033);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570560;} else {return -1;}},ITEM_LOGS_100);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570645;} else {return -1;}},ITEM_LOGS_101);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570730;} else {return -1;}},ITEM_LOGS_102);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570815;} else {return -1;}},ITEM_LOGS_103);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592320;} else {return -1;}},ITEM_LOGS_110);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592405;} else {return -1;}},ITEM_LOGS_111);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592490;} else {return -1;}},ITEM_LOGS_112);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592575;} else {return -1;}},ITEM_LOGS_113);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614080;} else {return -1;}},ITEM_LOGS_120);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614165;} else {return -1;}},ITEM_LOGS_121);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614250;} else {return -1;}},ITEM_LOGS_122);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614335;} else {return -1;}},ITEM_LOGS_123);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5635840;} else {return -1;}},ITEM_LOGS_130);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5635925;} else {return -1;}},ITEM_LOGS_131);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5636010;} else {return -1;}},ITEM_LOGS_132);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5636095;} else {return -1;}},ITEM_LOGS_133);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141120;} else {return -1;}},ITEM_LOGS_200);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141205;} else {return -1;}},ITEM_LOGS_201);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141290;} else {return -1;}},ITEM_LOGS_202);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141375;} else {return -1;}},ITEM_LOGS_203);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11162880;} else {return -1;}},ITEM_LOGS_210);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11162965;} else {return -1;}},ITEM_LOGS_211);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11163050;} else {return -1;}},ITEM_LOGS_212);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11163135;} else {return -1;}},ITEM_LOGS_213);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184640;} else {return -1;}},ITEM_LOGS_220);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184725;} else {return -1;}},ITEM_LOGS_221);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184810;} else {return -1;}},ITEM_LOGS_222);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184895;} else {return -1;}},ITEM_LOGS_223);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206400;} else {return -1;}},ITEM_LOGS_230);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206485;} else {return -1;}},ITEM_LOGS_231);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206570;} else {return -1;}},ITEM_LOGS_232);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206655;} else {return -1;}},ITEM_LOGS_233);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711680;} else {return -1;}},ITEM_LOGS_300);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711765;} else {return -1;}},ITEM_LOGS_301);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711850;} else {return -1;}},ITEM_LOGS_302);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711935;} else {return -1;}},ITEM_LOGS_303);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733440;} else {return -1;}},ITEM_LOGS_310);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733525;} else {return -1;}},ITEM_LOGS_311);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733610;} else {return -1;}},ITEM_LOGS_312);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733695;} else {return -1;}},ITEM_LOGS_313);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755200;} else {return -1;}},ITEM_LOGS_320);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755285;} else {return -1;}},ITEM_LOGS_321);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755370;} else {return -1;}},ITEM_LOGS_322);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755455;} else {return -1;}},ITEM_LOGS_323);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16776960;} else {return -1;}},ITEM_LOGS_330);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777045;} else {return -1;}},ITEM_LOGS_331);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777130;} else {return -1;}},ITEM_LOGS_332);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777215;} else {return -1;}},ITEM_LOGS_333);
    }

    public static final Block LOGS_000 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_000);
    public static final Block LOGS_001 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_001);
    public static final Block LOGS_002 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_002);
    public static final Block LOGS_003 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_003);
    public static final Block LOGS_010 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_010);
    public static final Block LOGS_011 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_011);
    public static final Block LOGS_012 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_012);
    public static final Block LOGS_013 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_013);
    public static final Block LOGS_020 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_020);
    public static final Block LOGS_021 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_021);
    public static final Block LOGS_022 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_022);
    public static final Block LOGS_023 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_023);
    public static final Block LOGS_030 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_030);
    public static final Block LOGS_031 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_031);
    public static final Block LOGS_032 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_032);
    public static final Block LOGS_033 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_033);

    public static final Block LOGS_100 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_100);
    public static final Block LOGS_101 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_101);
    public static final Block LOGS_102 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_102);
    public static final Block LOGS_103 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_103);
    public static final Block LOGS_110 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_110);
    public static final Block LOGS_111 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_111);
    public static final Block LOGS_112 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_112);
    public static final Block LOGS_113 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_113);
    public static final Block LOGS_120 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_120);
    public static final Block LOGS_121 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_121);
    public static final Block LOGS_122 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_122);
    public static final Block LOGS_123 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_123);
    public static final Block LOGS_130 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_130);
    public static final Block LOGS_131 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_131);
    public static final Block LOGS_132 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_132);
    public static final Block LOGS_133 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_133);

    public static final Block LOGS_200 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_200);
    public static final Block LOGS_201 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_201);
    public static final Block LOGS_202 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_202);
    public static final Block LOGS_203 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_203);
    public static final Block LOGS_210 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_210);
    public static final Block LOGS_211 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_211);
    public static final Block LOGS_212 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_212);
    public static final Block LOGS_213 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_213);
    public static final Block LOGS_220 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_220);
    public static final Block LOGS_221 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_221);
    public static final Block LOGS_222 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_222);
    public static final Block LOGS_223 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_223);
    public static final Block LOGS_230 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_230);
    public static final Block LOGS_231 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_231);
    public static final Block LOGS_232 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_232);
    public static final Block LOGS_233 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_233);

    public static final Block LOGS_300 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_300);
    public static final Block LOGS_301 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_301);
    public static final Block LOGS_302 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_302);
    public static final Block LOGS_303 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_303);
    public static final Block LOGS_310 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_310);
    public static final Block LOGS_311 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_311);
    public static final Block LOGS_312 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_312);
    public static final Block LOGS_313 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_313);
    public static final Block LOGS_320 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_320);
    public static final Block LOGS_321 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_321);
    public static final Block LOGS_322 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_322);
    public static final Block LOGS_323 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_323);
    public static final Block LOGS_330 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_330);
    public static final Block LOGS_331 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_331);
    public static final Block LOGS_332 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_332);
    public static final Block LOGS_333 = new BlockDustLogs(MaterialColor.WOOD,Properties.from(Blocks.OAK_LOG).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_LOGS_333);

    public static final Item ITEM_LOGS_000 = new BlockItem(LOGS_000, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_000);
    public static final Item ITEM_LOGS_001 = new BlockItem(LOGS_001, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_001);
    public static final Item ITEM_LOGS_002 = new BlockItem(LOGS_002, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_002);
    public static final Item ITEM_LOGS_003 = new BlockItem(LOGS_003, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_003);
    public static final Item ITEM_LOGS_010 = new BlockItem(LOGS_010, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_010);
    public static final Item ITEM_LOGS_011 = new BlockItem(LOGS_011, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_011);
    public static final Item ITEM_LOGS_012 = new BlockItem(LOGS_012, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_012);
    public static final Item ITEM_LOGS_013 = new BlockItem(LOGS_013, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_013);
    public static final Item ITEM_LOGS_020 = new BlockItem(LOGS_020, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_020);
    public static final Item ITEM_LOGS_021 = new BlockItem(LOGS_021, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_021);
    public static final Item ITEM_LOGS_022 = new BlockItem(LOGS_022, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_022);
    public static final Item ITEM_LOGS_023 = new BlockItem(LOGS_023, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_023);
    public static final Item ITEM_LOGS_030 = new BlockItem(LOGS_030, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_030);
    public static final Item ITEM_LOGS_031 = new BlockItem(LOGS_031, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_031);
    public static final Item ITEM_LOGS_032 = new BlockItem(LOGS_032, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_032);
    public static final Item ITEM_LOGS_033 = new BlockItem(LOGS_033, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_033);

    public static final Item ITEM_LOGS_100 = new BlockItem(LOGS_100, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_100);
    public static final Item ITEM_LOGS_101 = new BlockItem(LOGS_101, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_101);
    public static final Item ITEM_LOGS_102 = new BlockItem(LOGS_102, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_102);
    public static final Item ITEM_LOGS_103 = new BlockItem(LOGS_103, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_103);
    public static final Item ITEM_LOGS_110 = new BlockItem(LOGS_110, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_110);
    public static final Item ITEM_LOGS_111 = new BlockItem(LOGS_111, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_111);
    public static final Item ITEM_LOGS_112 = new BlockItem(LOGS_112, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_112);
    public static final Item ITEM_LOGS_113 = new BlockItem(LOGS_113, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_113);
    public static final Item ITEM_LOGS_120 = new BlockItem(LOGS_120, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_120);
    public static final Item ITEM_LOGS_121 = new BlockItem(LOGS_121, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_121);
    public static final Item ITEM_LOGS_122 = new BlockItem(LOGS_122, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_122);
    public static final Item ITEM_LOGS_123 = new BlockItem(LOGS_123, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_123);
    public static final Item ITEM_LOGS_130 = new BlockItem(LOGS_130, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_130);
    public static final Item ITEM_LOGS_131 = new BlockItem(LOGS_131, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_131);
    public static final Item ITEM_LOGS_132 = new BlockItem(LOGS_132, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_132);
    public static final Item ITEM_LOGS_133 = new BlockItem(LOGS_133, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_133);

    public static final Item ITEM_LOGS_200 = new BlockItem(LOGS_200, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_200);
    public static final Item ITEM_LOGS_201 = new BlockItem(LOGS_201, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_201);
    public static final Item ITEM_LOGS_202 = new BlockItem(LOGS_202, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_202);
    public static final Item ITEM_LOGS_203 = new BlockItem(LOGS_203, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_203);
    public static final Item ITEM_LOGS_210 = new BlockItem(LOGS_210, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_210);
    public static final Item ITEM_LOGS_211 = new BlockItem(LOGS_211, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_211);
    public static final Item ITEM_LOGS_212 = new BlockItem(LOGS_212, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_212);
    public static final Item ITEM_LOGS_213 = new BlockItem(LOGS_213, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_213);
    public static final Item ITEM_LOGS_220 = new BlockItem(LOGS_220, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_220);
    public static final Item ITEM_LOGS_221 = new BlockItem(LOGS_221, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_221);
    public static final Item ITEM_LOGS_222 = new BlockItem(LOGS_222, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_222);
    public static final Item ITEM_LOGS_223 = new BlockItem(LOGS_223, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_223);
    public static final Item ITEM_LOGS_230 = new BlockItem(LOGS_230, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_230);
    public static final Item ITEM_LOGS_231 = new BlockItem(LOGS_231, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_231);
    public static final Item ITEM_LOGS_232 = new BlockItem(LOGS_232, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_232);
    public static final Item ITEM_LOGS_233 = new BlockItem(LOGS_233, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_233);

    public static final Item ITEM_LOGS_300 = new BlockItem(LOGS_300, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_300);
    public static final Item ITEM_LOGS_301 = new BlockItem(LOGS_301, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_301);
    public static final Item ITEM_LOGS_302 = new BlockItem(LOGS_302, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_302);
    public static final Item ITEM_LOGS_303 = new BlockItem(LOGS_303, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_303);
    public static final Item ITEM_LOGS_310 = new BlockItem(LOGS_310, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_310);
    public static final Item ITEM_LOGS_311 = new BlockItem(LOGS_311, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_311);
    public static final Item ITEM_LOGS_312 = new BlockItem(LOGS_312, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_312);
    public static final Item ITEM_LOGS_313 = new BlockItem(LOGS_313, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_313);
    public static final Item ITEM_LOGS_320 = new BlockItem(LOGS_320, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_320);
    public static final Item ITEM_LOGS_321 = new BlockItem(LOGS_321, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_321);
    public static final Item ITEM_LOGS_322 = new BlockItem(LOGS_322, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_322);
    public static final Item ITEM_LOGS_323 = new BlockItem(LOGS_323, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_323);
    public static final Item ITEM_LOGS_330 = new BlockItem(LOGS_330, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_330);
    public static final Item ITEM_LOGS_331 = new BlockItem(LOGS_331, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_331);
    public static final Item ITEM_LOGS_332 = new BlockItem(LOGS_332, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_332);
    public static final Item ITEM_LOGS_333 = new BlockItem(LOGS_333, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LOGS_333);
}
