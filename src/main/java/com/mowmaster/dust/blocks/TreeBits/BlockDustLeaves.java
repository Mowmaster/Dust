package com.mowmaster.dust.blocks.TreeBits;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
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


public class BlockDustLeaves extends LeavesBlock
{
    public BlockDustLeaves(Properties builder) {
        super(builder);
    }

    private static final ResourceLocation RESLOC_LEAVES_000 = new ResourceLocation(MODID, "woodbits/leaves/leaves000");
    private static final ResourceLocation RESLOC_LEAVES_001 = new ResourceLocation(MODID, "woodbits/leaves/leaves001");
    private static final ResourceLocation RESLOC_LEAVES_002 = new ResourceLocation(MODID, "woodbits/leaves/leaves002");
    private static final ResourceLocation RESLOC_LEAVES_003 = new ResourceLocation(MODID, "woodbits/leaves/leaves003");
    private static final ResourceLocation RESLOC_LEAVES_010 = new ResourceLocation(MODID, "woodbits/leaves/leaves010");
    private static final ResourceLocation RESLOC_LEAVES_011 = new ResourceLocation(MODID, "woodbits/leaves/leaves011");
    private static final ResourceLocation RESLOC_LEAVES_012 = new ResourceLocation(MODID, "woodbits/leaves/leaves012");
    private static final ResourceLocation RESLOC_LEAVES_013 = new ResourceLocation(MODID, "woodbits/leaves/leaves013");
    private static final ResourceLocation RESLOC_LEAVES_020 = new ResourceLocation(MODID, "woodbits/leaves/leaves020");
    private static final ResourceLocation RESLOC_LEAVES_021 = new ResourceLocation(MODID, "woodbits/leaves/leaves021");
    private static final ResourceLocation RESLOC_LEAVES_022 = new ResourceLocation(MODID, "woodbits/leaves/leaves022");
    private static final ResourceLocation RESLOC_LEAVES_023 = new ResourceLocation(MODID, "woodbits/leaves/leaves023");
    private static final ResourceLocation RESLOC_LEAVES_030 = new ResourceLocation(MODID, "woodbits/leaves/leaves030");
    private static final ResourceLocation RESLOC_LEAVES_031 = new ResourceLocation(MODID, "woodbits/leaves/leaves031");
    private static final ResourceLocation RESLOC_LEAVES_032 = new ResourceLocation(MODID, "woodbits/leaves/leaves032");
    private static final ResourceLocation RESLOC_LEAVES_033 = new ResourceLocation(MODID, "woodbits/leaves/leaves033");

    private static final ResourceLocation RESLOC_LEAVES_100 = new ResourceLocation(MODID, "woodbits/leaves/leaves100");
    private static final ResourceLocation RESLOC_LEAVES_101 = new ResourceLocation(MODID, "woodbits/leaves/leaves101");
    private static final ResourceLocation RESLOC_LEAVES_102 = new ResourceLocation(MODID, "woodbits/leaves/leaves102");
    private static final ResourceLocation RESLOC_LEAVES_103 = new ResourceLocation(MODID, "woodbits/leaves/leaves103");
    private static final ResourceLocation RESLOC_LEAVES_110 = new ResourceLocation(MODID, "woodbits/leaves/leaves110");
    private static final ResourceLocation RESLOC_LEAVES_111 = new ResourceLocation(MODID, "woodbits/leaves/leaves111");
    private static final ResourceLocation RESLOC_LEAVES_112 = new ResourceLocation(MODID, "woodbits/leaves/leaves112");
    private static final ResourceLocation RESLOC_LEAVES_113 = new ResourceLocation(MODID, "woodbits/leaves/leaves113");
    private static final ResourceLocation RESLOC_LEAVES_120 = new ResourceLocation(MODID, "woodbits/leaves/leaves120");
    private static final ResourceLocation RESLOC_LEAVES_121 = new ResourceLocation(MODID, "woodbits/leaves/leaves121");
    private static final ResourceLocation RESLOC_LEAVES_122 = new ResourceLocation(MODID, "woodbits/leaves/leaves122");
    private static final ResourceLocation RESLOC_LEAVES_123 = new ResourceLocation(MODID, "woodbits/leaves/leaves123");
    private static final ResourceLocation RESLOC_LEAVES_130 = new ResourceLocation(MODID, "woodbits/leaves/leaves130");
    private static final ResourceLocation RESLOC_LEAVES_131 = new ResourceLocation(MODID, "woodbits/leaves/leaves131");
    private static final ResourceLocation RESLOC_LEAVES_132 = new ResourceLocation(MODID, "woodbits/leaves/leaves132");
    private static final ResourceLocation RESLOC_LEAVES_133 = new ResourceLocation(MODID, "woodbits/leaves/leaves133");

    private static final ResourceLocation RESLOC_LEAVES_200 = new ResourceLocation(MODID, "woodbits/leaves/leaves200");
    private static final ResourceLocation RESLOC_LEAVES_201 = new ResourceLocation(MODID, "woodbits/leaves/leaves201");
    private static final ResourceLocation RESLOC_LEAVES_202 = new ResourceLocation(MODID, "woodbits/leaves/leaves202");
    private static final ResourceLocation RESLOC_LEAVES_203 = new ResourceLocation(MODID, "woodbits/leaves/leaves203");
    private static final ResourceLocation RESLOC_LEAVES_210 = new ResourceLocation(MODID, "woodbits/leaves/leaves210");
    private static final ResourceLocation RESLOC_LEAVES_211 = new ResourceLocation(MODID, "woodbits/leaves/leaves211");
    private static final ResourceLocation RESLOC_LEAVES_212 = new ResourceLocation(MODID, "woodbits/leaves/leaves212");
    private static final ResourceLocation RESLOC_LEAVES_213 = new ResourceLocation(MODID, "woodbits/leaves/leaves213");
    private static final ResourceLocation RESLOC_LEAVES_220 = new ResourceLocation(MODID, "woodbits/leaves/leaves220");
    private static final ResourceLocation RESLOC_LEAVES_221 = new ResourceLocation(MODID, "woodbits/leaves/leaves221");
    private static final ResourceLocation RESLOC_LEAVES_222 = new ResourceLocation(MODID, "woodbits/leaves/leaves222");
    private static final ResourceLocation RESLOC_LEAVES_223 = new ResourceLocation(MODID, "woodbits/leaves/leaves223");
    private static final ResourceLocation RESLOC_LEAVES_230 = new ResourceLocation(MODID, "woodbits/leaves/leaves230");
    private static final ResourceLocation RESLOC_LEAVES_231 = new ResourceLocation(MODID, "woodbits/leaves/leaves231");
    private static final ResourceLocation RESLOC_LEAVES_232 = new ResourceLocation(MODID, "woodbits/leaves/leaves232");
    private static final ResourceLocation RESLOC_LEAVES_233 = new ResourceLocation(MODID, "woodbits/leaves/leaves233");

    private static final ResourceLocation RESLOC_LEAVES_300 = new ResourceLocation(MODID, "woodbits/leaves/leaves300");
    private static final ResourceLocation RESLOC_LEAVES_301 = new ResourceLocation(MODID, "woodbits/leaves/leaves301");
    private static final ResourceLocation RESLOC_LEAVES_302 = new ResourceLocation(MODID, "woodbits/leaves/leaves302");
    private static final ResourceLocation RESLOC_LEAVES_303 = new ResourceLocation(MODID, "woodbits/leaves/leaves303");
    private static final ResourceLocation RESLOC_LEAVES_310 = new ResourceLocation(MODID, "woodbits/leaves/leaves310");
    private static final ResourceLocation RESLOC_LEAVES_311 = new ResourceLocation(MODID, "woodbits/leaves/leaves311");
    private static final ResourceLocation RESLOC_LEAVES_312 = new ResourceLocation(MODID, "woodbits/leaves/leaves312");
    private static final ResourceLocation RESLOC_LEAVES_313 = new ResourceLocation(MODID, "woodbits/leaves/leaves313");
    private static final ResourceLocation RESLOC_LEAVES_320 = new ResourceLocation(MODID, "woodbits/leaves/leaves320");
    private static final ResourceLocation RESLOC_LEAVES_321 = new ResourceLocation(MODID, "woodbits/leaves/leaves321");
    private static final ResourceLocation RESLOC_LEAVES_322 = new ResourceLocation(MODID, "woodbits/leaves/leaves322");
    private static final ResourceLocation RESLOC_LEAVES_323 = new ResourceLocation(MODID, "woodbits/leaves/leaves323");
    private static final ResourceLocation RESLOC_LEAVES_330 = new ResourceLocation(MODID, "woodbits/leaves/leaves330");
    private static final ResourceLocation RESLOC_LEAVES_331 = new ResourceLocation(MODID, "woodbits/leaves/leaves331");
    private static final ResourceLocation RESLOC_LEAVES_332 = new ResourceLocation(MODID, "woodbits/leaves/leaves332");
    private static final ResourceLocation RESLOC_LEAVES_333 = new ResourceLocation(MODID, "woodbits/leaves/leaves333");

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(ITEM_LEAVES_000);
        event.getRegistry().register(ITEM_LEAVES_001);
        event.getRegistry().register(ITEM_LEAVES_002);
        event.getRegistry().register(ITEM_LEAVES_003);
        event.getRegistry().register(ITEM_LEAVES_010);
        event.getRegistry().register(ITEM_LEAVES_011);
        event.getRegistry().register(ITEM_LEAVES_012);
        event.getRegistry().register(ITEM_LEAVES_013);
        event.getRegistry().register(ITEM_LEAVES_020);
        event.getRegistry().register(ITEM_LEAVES_021);
        event.getRegistry().register(ITEM_LEAVES_022);
        event.getRegistry().register(ITEM_LEAVES_023);
        event.getRegistry().register(ITEM_LEAVES_030);
        event.getRegistry().register(ITEM_LEAVES_031);
        event.getRegistry().register(ITEM_LEAVES_032);
        event.getRegistry().register(ITEM_LEAVES_033);

        event.getRegistry().register(ITEM_LEAVES_100);
        event.getRegistry().register(ITEM_LEAVES_101);
        event.getRegistry().register(ITEM_LEAVES_102);
        event.getRegistry().register(ITEM_LEAVES_103);
        event.getRegistry().register(ITEM_LEAVES_110);
        event.getRegistry().register(ITEM_LEAVES_111);
        event.getRegistry().register(ITEM_LEAVES_112);
        event.getRegistry().register(ITEM_LEAVES_113);
        event.getRegistry().register(ITEM_LEAVES_120);
        event.getRegistry().register(ITEM_LEAVES_121);
        event.getRegistry().register(ITEM_LEAVES_122);
        event.getRegistry().register(ITEM_LEAVES_123);
        event.getRegistry().register(ITEM_LEAVES_130);
        event.getRegistry().register(ITEM_LEAVES_131);
        event.getRegistry().register(ITEM_LEAVES_132);
        event.getRegistry().register(ITEM_LEAVES_133);

        event.getRegistry().register(ITEM_LEAVES_200);
        event.getRegistry().register(ITEM_LEAVES_201);
        event.getRegistry().register(ITEM_LEAVES_202);
        event.getRegistry().register(ITEM_LEAVES_203);
        event.getRegistry().register(ITEM_LEAVES_210);
        event.getRegistry().register(ITEM_LEAVES_211);
        event.getRegistry().register(ITEM_LEAVES_212);
        event.getRegistry().register(ITEM_LEAVES_213);
        event.getRegistry().register(ITEM_LEAVES_220);
        event.getRegistry().register(ITEM_LEAVES_221);
        event.getRegistry().register(ITEM_LEAVES_222);
        event.getRegistry().register(ITEM_LEAVES_223);
        event.getRegistry().register(ITEM_LEAVES_230);
        event.getRegistry().register(ITEM_LEAVES_231);
        event.getRegistry().register(ITEM_LEAVES_232);
        event.getRegistry().register(ITEM_LEAVES_233);

        event.getRegistry().register(ITEM_LEAVES_300);
        event.getRegistry().register(ITEM_LEAVES_301);
        event.getRegistry().register(ITEM_LEAVES_302);
        event.getRegistry().register(ITEM_LEAVES_303);
        event.getRegistry().register(ITEM_LEAVES_310);
        event.getRegistry().register(ITEM_LEAVES_311);
        event.getRegistry().register(ITEM_LEAVES_312);
        event.getRegistry().register(ITEM_LEAVES_313);
        event.getRegistry().register(ITEM_LEAVES_320);
        event.getRegistry().register(ITEM_LEAVES_321);
        event.getRegistry().register(ITEM_LEAVES_322);
        event.getRegistry().register(ITEM_LEAVES_323);
        event.getRegistry().register(ITEM_LEAVES_330);
        event.getRegistry().register(ITEM_LEAVES_331);
        event.getRegistry().register(ITEM_LEAVES_332);
        event.getRegistry().register(ITEM_LEAVES_333);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(LEAVES_000);
        event.getRegistry().register(LEAVES_001);
        event.getRegistry().register(LEAVES_002);
        event.getRegistry().register(LEAVES_003);
        event.getRegistry().register(LEAVES_010);
        event.getRegistry().register(LEAVES_011);
        event.getRegistry().register(LEAVES_012);
        event.getRegistry().register(LEAVES_013);
        event.getRegistry().register(LEAVES_020);
        event.getRegistry().register(LEAVES_021);
        event.getRegistry().register(LEAVES_022);
        event.getRegistry().register(LEAVES_023);
        event.getRegistry().register(LEAVES_030);
        event.getRegistry().register(LEAVES_031);
        event.getRegistry().register(LEAVES_032);
        event.getRegistry().register(LEAVES_033);

        event.getRegistry().register(LEAVES_100);
        event.getRegistry().register(LEAVES_101);
        event.getRegistry().register(LEAVES_102);
        event.getRegistry().register(LEAVES_103);
        event.getRegistry().register(LEAVES_110);
        event.getRegistry().register(LEAVES_111);
        event.getRegistry().register(LEAVES_112);
        event.getRegistry().register(LEAVES_113);
        event.getRegistry().register(LEAVES_120);
        event.getRegistry().register(LEAVES_121);
        event.getRegistry().register(LEAVES_122);
        event.getRegistry().register(LEAVES_123);
        event.getRegistry().register(LEAVES_130);
        event.getRegistry().register(LEAVES_131);
        event.getRegistry().register(LEAVES_132);
        event.getRegistry().register(LEAVES_133);

        event.getRegistry().register(LEAVES_200);
        event.getRegistry().register(LEAVES_201);
        event.getRegistry().register(LEAVES_202);
        event.getRegistry().register(LEAVES_203);
        event.getRegistry().register(LEAVES_210);
        event.getRegistry().register(LEAVES_211);
        event.getRegistry().register(LEAVES_212);
        event.getRegistry().register(LEAVES_213);
        event.getRegistry().register(LEAVES_220);
        event.getRegistry().register(LEAVES_221);
        event.getRegistry().register(LEAVES_222);
        event.getRegistry().register(LEAVES_223);
        event.getRegistry().register(LEAVES_230);
        event.getRegistry().register(LEAVES_231);
        event.getRegistry().register(LEAVES_232);
        event.getRegistry().register(LEAVES_233);

        event.getRegistry().register(LEAVES_300);
        event.getRegistry().register(LEAVES_301);
        event.getRegistry().register(LEAVES_302);
        event.getRegistry().register(LEAVES_303);
        event.getRegistry().register(LEAVES_310);
        event.getRegistry().register(LEAVES_311);
        event.getRegistry().register(LEAVES_312);
        event.getRegistry().register(LEAVES_313);
        event.getRegistry().register(LEAVES_320);
        event.getRegistry().register(LEAVES_321);
        event.getRegistry().register(LEAVES_322);
        event.getRegistry().register(LEAVES_323);
        event.getRegistry().register(LEAVES_330);
        event.getRegistry().register(LEAVES_331);
        event.getRegistry().register(LEAVES_332);
        event.getRegistry().register(LEAVES_333);
    }



    public static void handleBlockColors(ColorHandlerEvent.Block event) {

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 0;} else {return -1;}},LEAVES_000);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 85;} else {return -1;}},LEAVES_001);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 170;} else {return -1;}},LEAVES_002);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 255;} else {return -1;}},LEAVES_003);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21760;} else {return -1;}},LEAVES_010);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21845;} else {return -1;}},LEAVES_011);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21930;} else {return -1;}},LEAVES_012);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 22015;} else {return -1;}},LEAVES_013);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43520;} else {return -1;}},LEAVES_020);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43605;} else {return -1;}},LEAVES_021);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43690;} else {return -1;}},LEAVES_022);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43775;} else {return -1;}},LEAVES_023);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65280;} else {return -1;}},LEAVES_030);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65365;} else {return -1;}},LEAVES_031);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65450;} else {return -1;}},LEAVES_032);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65535;} else {return -1;}},LEAVES_033);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570560;} else {return -1;}},LEAVES_100);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570645;} else {return -1;}},LEAVES_101);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570730;} else {return -1;}},LEAVES_102);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570815;} else {return -1;}},LEAVES_103);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592320;} else {return -1;}},LEAVES_110);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592405;} else {return -1;}},LEAVES_111);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592490;} else {return -1;}},LEAVES_112);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592575;} else {return -1;}},LEAVES_113);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614080;} else {return -1;}},LEAVES_120);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614165;} else {return -1;}},LEAVES_121);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614250;} else {return -1;}},LEAVES_122);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614335;} else {return -1;}},LEAVES_123);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5635840;} else {return -1;}},LEAVES_130);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5635925;} else {return -1;}},LEAVES_131);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5636010;} else {return -1;}},LEAVES_132);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5636095;} else {return -1;}},LEAVES_133);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141120;} else {return -1;}},LEAVES_200);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141205;} else {return -1;}},LEAVES_201);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141290;} else {return -1;}},LEAVES_202);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141375;} else {return -1;}},LEAVES_203);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11162880;} else {return -1;}},LEAVES_210);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11162965;} else {return -1;}},LEAVES_211);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11163050;} else {return -1;}},LEAVES_212);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11163135;} else {return -1;}},LEAVES_213);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184640;} else {return -1;}},LEAVES_220);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184725;} else {return -1;}},LEAVES_221);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184810;} else {return -1;}},LEAVES_222);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184895;} else {return -1;}},LEAVES_223);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206400;} else {return -1;}},LEAVES_230);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206485;} else {return -1;}},LEAVES_231);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206570;} else {return -1;}},LEAVES_232);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206655;} else {return -1;}},LEAVES_233);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711680;} else {return -1;}},LEAVES_300);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711765;} else {return -1;}},LEAVES_301);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711850;} else {return -1;}},LEAVES_302);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711935;} else {return -1;}},LEAVES_303);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733440;} else {return -1;}},LEAVES_310);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733525;} else {return -1;}},LEAVES_311);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733610;} else {return -1;}},LEAVES_312);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733695;} else {return -1;}},LEAVES_313);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755200;} else {return -1;}},LEAVES_320);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755285;} else {return -1;}},LEAVES_321);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755370;} else {return -1;}},LEAVES_322);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755455;} else {return -1;}},LEAVES_323);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16776960;} else {return -1;}},LEAVES_330);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777045;} else {return -1;}},LEAVES_331);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777130;} else {return -1;}},LEAVES_332);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777215;} else {return -1;}},LEAVES_333);
    }

    public static void handleItemColors(ColorHandlerEvent.Item event) {

        //event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 0;} else {return -1;}},ITEM_LEAVES_000);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 0;} else {return -1;}},ITEM_LEAVES_000);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 85;} else {return -1;}},ITEM_LEAVES_001);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 170;} else {return -1;}},ITEM_LEAVES_002);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 255;} else {return -1;}},ITEM_LEAVES_003);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21760;} else {return -1;}},ITEM_LEAVES_010);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21845;} else {return -1;}},ITEM_LEAVES_011);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21930;} else {return -1;}},ITEM_LEAVES_012);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 22015;} else {return -1;}},ITEM_LEAVES_013);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43520;} else {return -1;}},ITEM_LEAVES_020);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43605;} else {return -1;}},ITEM_LEAVES_021);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43690;} else {return -1;}},ITEM_LEAVES_022);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43775;} else {return -1;}},ITEM_LEAVES_023);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65280;} else {return -1;}},ITEM_LEAVES_030);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65365;} else {return -1;}},ITEM_LEAVES_031);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65450;} else {return -1;}},ITEM_LEAVES_032);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65535;} else {return -1;}},ITEM_LEAVES_033);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570560;} else {return -1;}},ITEM_LEAVES_100);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570645;} else {return -1;}},ITEM_LEAVES_101);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570730;} else {return -1;}},ITEM_LEAVES_102);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570815;} else {return -1;}},ITEM_LEAVES_103);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592320;} else {return -1;}},ITEM_LEAVES_110);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592405;} else {return -1;}},ITEM_LEAVES_111);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592490;} else {return -1;}},ITEM_LEAVES_112);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592575;} else {return -1;}},ITEM_LEAVES_113);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614080;} else {return -1;}},ITEM_LEAVES_120);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614165;} else {return -1;}},ITEM_LEAVES_121);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614250;} else {return -1;}},ITEM_LEAVES_122);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614335;} else {return -1;}},ITEM_LEAVES_123);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5635840;} else {return -1;}},ITEM_LEAVES_130);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5635925;} else {return -1;}},ITEM_LEAVES_131);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5636010;} else {return -1;}},ITEM_LEAVES_132);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5636095;} else {return -1;}},ITEM_LEAVES_133);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141120;} else {return -1;}},ITEM_LEAVES_200);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141205;} else {return -1;}},ITEM_LEAVES_201);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141290;} else {return -1;}},ITEM_LEAVES_202);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141375;} else {return -1;}},ITEM_LEAVES_203);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11162880;} else {return -1;}},ITEM_LEAVES_210);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11162965;} else {return -1;}},ITEM_LEAVES_211);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11163050;} else {return -1;}},ITEM_LEAVES_212);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11163135;} else {return -1;}},ITEM_LEAVES_213);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184640;} else {return -1;}},ITEM_LEAVES_220);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184725;} else {return -1;}},ITEM_LEAVES_221);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184810;} else {return -1;}},ITEM_LEAVES_222);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184895;} else {return -1;}},ITEM_LEAVES_223);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206400;} else {return -1;}},ITEM_LEAVES_230);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206485;} else {return -1;}},ITEM_LEAVES_231);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206570;} else {return -1;}},ITEM_LEAVES_232);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206655;} else {return -1;}},ITEM_LEAVES_233);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711680;} else {return -1;}},ITEM_LEAVES_300);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711765;} else {return -1;}},ITEM_LEAVES_301);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711850;} else {return -1;}},ITEM_LEAVES_302);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711935;} else {return -1;}},ITEM_LEAVES_303);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733440;} else {return -1;}},ITEM_LEAVES_310);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733525;} else {return -1;}},ITEM_LEAVES_311);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733610;} else {return -1;}},ITEM_LEAVES_312);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733695;} else {return -1;}},ITEM_LEAVES_313);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755200;} else {return -1;}},ITEM_LEAVES_320);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755285;} else {return -1;}},ITEM_LEAVES_321);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755370;} else {return -1;}},ITEM_LEAVES_322);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755455;} else {return -1;}},ITEM_LEAVES_323);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16776960;} else {return -1;}},ITEM_LEAVES_330);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777045;} else {return -1;}},ITEM_LEAVES_331);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777130;} else {return -1;}},ITEM_LEAVES_332);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777215;} else {return -1;}},ITEM_LEAVES_333);
    }

    public static final Block LEAVES_000 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_000);
    public static final Block LEAVES_001 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_001);
    public static final Block LEAVES_002 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_002);
    public static final Block LEAVES_003 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_003);
    public static final Block LEAVES_010 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_010);
    public static final Block LEAVES_011 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_011);
    public static final Block LEAVES_012 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_012);
    public static final Block LEAVES_013 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_013);
    public static final Block LEAVES_020 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_020);
    public static final Block LEAVES_021 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_021);
    public static final Block LEAVES_022 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_022);
    public static final Block LEAVES_023 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_023);
    public static final Block LEAVES_030 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_030);
    public static final Block LEAVES_031 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_031);
    public static final Block LEAVES_032 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_032);
    public static final Block LEAVES_033 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_033);

    public static final Block LEAVES_100 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_100);
    public static final Block LEAVES_101 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_101);
    public static final Block LEAVES_102 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_102);
    public static final Block LEAVES_103 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_103);
    public static final Block LEAVES_110 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_110);
    public static final Block LEAVES_111 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_111);
    public static final Block LEAVES_112 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_112);
    public static final Block LEAVES_113 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_113);
    public static final Block LEAVES_120 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_120);
    public static final Block LEAVES_121 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_121);
    public static final Block LEAVES_122 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_122);
    public static final Block LEAVES_123 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_123);
    public static final Block LEAVES_130 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_130);
    public static final Block LEAVES_131 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_131);
    public static final Block LEAVES_132 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_132);
    public static final Block LEAVES_133 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_133);

    public static final Block LEAVES_200 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_200);
    public static final Block LEAVES_201 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_201);
    public static final Block LEAVES_202 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_202);
    public static final Block LEAVES_203 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_203);
    public static final Block LEAVES_210 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_210);
    public static final Block LEAVES_211 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_211);
    public static final Block LEAVES_212 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_212);
    public static final Block LEAVES_213 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_213);
    public static final Block LEAVES_220 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_220);
    public static final Block LEAVES_221 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_221);
    public static final Block LEAVES_222 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_222);
    public static final Block LEAVES_223 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_223);
    public static final Block LEAVES_230 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_230);
    public static final Block LEAVES_231 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_231);
    public static final Block LEAVES_232 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_232);
    public static final Block LEAVES_233 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_233);

    public static final Block LEAVES_300 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_300);
    public static final Block LEAVES_301 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_301);
    public static final Block LEAVES_302 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_302);
    public static final Block LEAVES_303 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_303);
    public static final Block LEAVES_310 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_310);
    public static final Block LEAVES_311 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_311);
    public static final Block LEAVES_312 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_312);
    public static final Block LEAVES_313 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_313);
    public static final Block LEAVES_320 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_320);
    public static final Block LEAVES_321 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_321);
    public static final Block LEAVES_322 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_322);
    public static final Block LEAVES_323 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_323);
    public static final Block LEAVES_330 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_330);
    public static final Block LEAVES_331 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_331);
    public static final Block LEAVES_332 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_332);
    public static final Block LEAVES_333 = new BlockDustLeaves(Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_LEAVES_333);

    public static final Item ITEM_LEAVES_000 = new BlockItem(LEAVES_000, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_000);
    public static final Item ITEM_LEAVES_001 = new BlockItem(LEAVES_001, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_001);
    public static final Item ITEM_LEAVES_002 = new BlockItem(LEAVES_002, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_002);
    public static final Item ITEM_LEAVES_003 = new BlockItem(LEAVES_003, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_003);
    public static final Item ITEM_LEAVES_010 = new BlockItem(LEAVES_010, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_010);
    public static final Item ITEM_LEAVES_011 = new BlockItem(LEAVES_011, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_011);
    public static final Item ITEM_LEAVES_012 = new BlockItem(LEAVES_012, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_012);
    public static final Item ITEM_LEAVES_013 = new BlockItem(LEAVES_013, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_013);
    public static final Item ITEM_LEAVES_020 = new BlockItem(LEAVES_020, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_020);
    public static final Item ITEM_LEAVES_021 = new BlockItem(LEAVES_021, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_021);
    public static final Item ITEM_LEAVES_022 = new BlockItem(LEAVES_022, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_022);
    public static final Item ITEM_LEAVES_023 = new BlockItem(LEAVES_023, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_023);
    public static final Item ITEM_LEAVES_030 = new BlockItem(LEAVES_030, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_030);
    public static final Item ITEM_LEAVES_031 = new BlockItem(LEAVES_031, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_031);
    public static final Item ITEM_LEAVES_032 = new BlockItem(LEAVES_032, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_032);
    public static final Item ITEM_LEAVES_033 = new BlockItem(LEAVES_033, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_033);

    public static final Item ITEM_LEAVES_100 = new BlockItem(LEAVES_100, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_100);
    public static final Item ITEM_LEAVES_101 = new BlockItem(LEAVES_101, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_101);
    public static final Item ITEM_LEAVES_102 = new BlockItem(LEAVES_102, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_102);
    public static final Item ITEM_LEAVES_103 = new BlockItem(LEAVES_103, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_103);
    public static final Item ITEM_LEAVES_110 = new BlockItem(LEAVES_110, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_110);
    public static final Item ITEM_LEAVES_111 = new BlockItem(LEAVES_111, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_111);
    public static final Item ITEM_LEAVES_112 = new BlockItem(LEAVES_112, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_112);
    public static final Item ITEM_LEAVES_113 = new BlockItem(LEAVES_113, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_113);
    public static final Item ITEM_LEAVES_120 = new BlockItem(LEAVES_120, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_120);
    public static final Item ITEM_LEAVES_121 = new BlockItem(LEAVES_121, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_121);
    public static final Item ITEM_LEAVES_122 = new BlockItem(LEAVES_122, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_122);
    public static final Item ITEM_LEAVES_123 = new BlockItem(LEAVES_123, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_123);
    public static final Item ITEM_LEAVES_130 = new BlockItem(LEAVES_130, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_130);
    public static final Item ITEM_LEAVES_131 = new BlockItem(LEAVES_131, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_131);
    public static final Item ITEM_LEAVES_132 = new BlockItem(LEAVES_132, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_132);
    public static final Item ITEM_LEAVES_133 = new BlockItem(LEAVES_133, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_133);

    public static final Item ITEM_LEAVES_200 = new BlockItem(LEAVES_200, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_200);
    public static final Item ITEM_LEAVES_201 = new BlockItem(LEAVES_201, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_201);
    public static final Item ITEM_LEAVES_202 = new BlockItem(LEAVES_202, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_202);
    public static final Item ITEM_LEAVES_203 = new BlockItem(LEAVES_203, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_203);
    public static final Item ITEM_LEAVES_210 = new BlockItem(LEAVES_210, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_210);
    public static final Item ITEM_LEAVES_211 = new BlockItem(LEAVES_211, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_211);
    public static final Item ITEM_LEAVES_212 = new BlockItem(LEAVES_212, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_212);
    public static final Item ITEM_LEAVES_213 = new BlockItem(LEAVES_213, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_213);
    public static final Item ITEM_LEAVES_220 = new BlockItem(LEAVES_220, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_220);
    public static final Item ITEM_LEAVES_221 = new BlockItem(LEAVES_221, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_221);
    public static final Item ITEM_LEAVES_222 = new BlockItem(LEAVES_222, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_222);
    public static final Item ITEM_LEAVES_223 = new BlockItem(LEAVES_223, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_223);
    public static final Item ITEM_LEAVES_230 = new BlockItem(LEAVES_230, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_230);
    public static final Item ITEM_LEAVES_231 = new BlockItem(LEAVES_231, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_231);
    public static final Item ITEM_LEAVES_232 = new BlockItem(LEAVES_232, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_232);
    public static final Item ITEM_LEAVES_233 = new BlockItem(LEAVES_233, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_233);

    public static final Item ITEM_LEAVES_300 = new BlockItem(LEAVES_300, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_300);
    public static final Item ITEM_LEAVES_301 = new BlockItem(LEAVES_301, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_301);
    public static final Item ITEM_LEAVES_302 = new BlockItem(LEAVES_302, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_302);
    public static final Item ITEM_LEAVES_303 = new BlockItem(LEAVES_303, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_303);
    public static final Item ITEM_LEAVES_310 = new BlockItem(LEAVES_310, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_310);
    public static final Item ITEM_LEAVES_311 = new BlockItem(LEAVES_311, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_311);
    public static final Item ITEM_LEAVES_312 = new BlockItem(LEAVES_312, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_312);
    public static final Item ITEM_LEAVES_313 = new BlockItem(LEAVES_313, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_313);
    public static final Item ITEM_LEAVES_320 = new BlockItem(LEAVES_320, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_320);
    public static final Item ITEM_LEAVES_321 = new BlockItem(LEAVES_321, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_321);
    public static final Item ITEM_LEAVES_322 = new BlockItem(LEAVES_322, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_322);
    public static final Item ITEM_LEAVES_323 = new BlockItem(LEAVES_323, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_323);
    public static final Item ITEM_LEAVES_330 = new BlockItem(LEAVES_330, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_330);
    public static final Item ITEM_LEAVES_331 = new BlockItem(LEAVES_331, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_331);
    public static final Item ITEM_LEAVES_332 = new BlockItem(LEAVES_332, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_332);
    public static final Item ITEM_LEAVES_333 = new BlockItem(LEAVES_333, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_LEAVES_333);
}
