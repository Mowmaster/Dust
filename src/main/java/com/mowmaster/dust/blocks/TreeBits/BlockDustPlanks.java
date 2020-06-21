package com.mowmaster.dust.blocks.TreeBits;

import net.minecraft.block.Block;
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


public class BlockDustPlanks extends Block
{
    public BlockDustPlanks(Properties builder) {
        super(builder);
    }

    private static final ResourceLocation RESLOC_PLANK_000 = new ResourceLocation(MODID, "woodbits/planks/plank000");
    private static final ResourceLocation RESLOC_PLANK_001 = new ResourceLocation(MODID, "woodbits/planks/plank001");
    private static final ResourceLocation RESLOC_PLANK_002 = new ResourceLocation(MODID, "woodbits/planks/plank002");
    private static final ResourceLocation RESLOC_PLANK_003 = new ResourceLocation(MODID, "woodbits/planks/plank003");
    private static final ResourceLocation RESLOC_PLANK_010 = new ResourceLocation(MODID, "woodbits/planks/plank010");
    private static final ResourceLocation RESLOC_PLANK_011 = new ResourceLocation(MODID, "woodbits/planks/plank011");
    private static final ResourceLocation RESLOC_PLANK_012 = new ResourceLocation(MODID, "woodbits/planks/plank012");
    private static final ResourceLocation RESLOC_PLANK_013 = new ResourceLocation(MODID, "woodbits/planks/plank013");
    private static final ResourceLocation RESLOC_PLANK_020 = new ResourceLocation(MODID, "woodbits/planks/plank020");
    private static final ResourceLocation RESLOC_PLANK_021 = new ResourceLocation(MODID, "woodbits/planks/plank021");
    private static final ResourceLocation RESLOC_PLANK_022 = new ResourceLocation(MODID, "woodbits/planks/plank022");
    private static final ResourceLocation RESLOC_PLANK_023 = new ResourceLocation(MODID, "woodbits/planks/plank023");
    private static final ResourceLocation RESLOC_PLANK_030 = new ResourceLocation(MODID, "woodbits/planks/plank030");
    private static final ResourceLocation RESLOC_PLANK_031 = new ResourceLocation(MODID, "woodbits/planks/plank031");
    private static final ResourceLocation RESLOC_PLANK_032 = new ResourceLocation(MODID, "woodbits/planks/plank032");
    private static final ResourceLocation RESLOC_PLANK_033 = new ResourceLocation(MODID, "woodbits/planks/plank033");

    private static final ResourceLocation RESLOC_PLANK_100 = new ResourceLocation(MODID, "woodbits/planks/plank100");
    private static final ResourceLocation RESLOC_PLANK_101 = new ResourceLocation(MODID, "woodbits/planks/plank101");
    private static final ResourceLocation RESLOC_PLANK_102 = new ResourceLocation(MODID, "woodbits/planks/plank102");
    private static final ResourceLocation RESLOC_PLANK_103 = new ResourceLocation(MODID, "woodbits/planks/plank103");
    private static final ResourceLocation RESLOC_PLANK_110 = new ResourceLocation(MODID, "woodbits/planks/plank110");
    private static final ResourceLocation RESLOC_PLANK_111 = new ResourceLocation(MODID, "woodbits/planks/plank111");
    private static final ResourceLocation RESLOC_PLANK_112 = new ResourceLocation(MODID, "woodbits/planks/plank112");
    private static final ResourceLocation RESLOC_PLANK_113 = new ResourceLocation(MODID, "woodbits/planks/plank113");
    private static final ResourceLocation RESLOC_PLANK_120 = new ResourceLocation(MODID, "woodbits/planks/plank120");
    private static final ResourceLocation RESLOC_PLANK_121 = new ResourceLocation(MODID, "woodbits/planks/plank121");
    private static final ResourceLocation RESLOC_PLANK_122 = new ResourceLocation(MODID, "woodbits/planks/plank122");
    private static final ResourceLocation RESLOC_PLANK_123 = new ResourceLocation(MODID, "woodbits/planks/plank123");
    private static final ResourceLocation RESLOC_PLANK_130 = new ResourceLocation(MODID, "woodbits/planks/plank130");
    private static final ResourceLocation RESLOC_PLANK_131 = new ResourceLocation(MODID, "woodbits/planks/plank131");
    private static final ResourceLocation RESLOC_PLANK_132 = new ResourceLocation(MODID, "woodbits/planks/plank132");
    private static final ResourceLocation RESLOC_PLANK_133 = new ResourceLocation(MODID, "woodbits/planks/plank133");

    private static final ResourceLocation RESLOC_PLANK_200 = new ResourceLocation(MODID, "woodbits/planks/plank200");
    private static final ResourceLocation RESLOC_PLANK_201 = new ResourceLocation(MODID, "woodbits/planks/plank201");
    private static final ResourceLocation RESLOC_PLANK_202 = new ResourceLocation(MODID, "woodbits/planks/plank202");
    private static final ResourceLocation RESLOC_PLANK_203 = new ResourceLocation(MODID, "woodbits/planks/plank203");
    private static final ResourceLocation RESLOC_PLANK_210 = new ResourceLocation(MODID, "woodbits/planks/plank210");
    private static final ResourceLocation RESLOC_PLANK_211 = new ResourceLocation(MODID, "woodbits/planks/plank211");
    private static final ResourceLocation RESLOC_PLANK_212 = new ResourceLocation(MODID, "woodbits/planks/plank212");
    private static final ResourceLocation RESLOC_PLANK_213 = new ResourceLocation(MODID, "woodbits/planks/plank213");
    private static final ResourceLocation RESLOC_PLANK_220 = new ResourceLocation(MODID, "woodbits/planks/plank220");
    private static final ResourceLocation RESLOC_PLANK_221 = new ResourceLocation(MODID, "woodbits/planks/plank221");
    private static final ResourceLocation RESLOC_PLANK_222 = new ResourceLocation(MODID, "woodbits/planks/plank222");
    private static final ResourceLocation RESLOC_PLANK_223 = new ResourceLocation(MODID, "woodbits/planks/plank223");
    private static final ResourceLocation RESLOC_PLANK_230 = new ResourceLocation(MODID, "woodbits/planks/plank230");
    private static final ResourceLocation RESLOC_PLANK_231 = new ResourceLocation(MODID, "woodbits/planks/plank231");
    private static final ResourceLocation RESLOC_PLANK_232 = new ResourceLocation(MODID, "woodbits/planks/plank232");
    private static final ResourceLocation RESLOC_PLANK_233 = new ResourceLocation(MODID, "woodbits/planks/plank233");

    private static final ResourceLocation RESLOC_PLANK_300 = new ResourceLocation(MODID, "woodbits/planks/plank300");
    private static final ResourceLocation RESLOC_PLANK_301 = new ResourceLocation(MODID, "woodbits/planks/plank301");
    private static final ResourceLocation RESLOC_PLANK_302 = new ResourceLocation(MODID, "woodbits/planks/plank302");
    private static final ResourceLocation RESLOC_PLANK_303 = new ResourceLocation(MODID, "woodbits/planks/plank303");
    private static final ResourceLocation RESLOC_PLANK_310 = new ResourceLocation(MODID, "woodbits/planks/plank310");
    private static final ResourceLocation RESLOC_PLANK_311 = new ResourceLocation(MODID, "woodbits/planks/plank311");
    private static final ResourceLocation RESLOC_PLANK_312 = new ResourceLocation(MODID, "woodbits/planks/plank312");
    private static final ResourceLocation RESLOC_PLANK_313 = new ResourceLocation(MODID, "woodbits/planks/plank313");
    private static final ResourceLocation RESLOC_PLANK_320 = new ResourceLocation(MODID, "woodbits/planks/plank320");
    private static final ResourceLocation RESLOC_PLANK_321 = new ResourceLocation(MODID, "woodbits/planks/plank321");
    private static final ResourceLocation RESLOC_PLANK_322 = new ResourceLocation(MODID, "woodbits/planks/plank322");
    private static final ResourceLocation RESLOC_PLANK_323 = new ResourceLocation(MODID, "woodbits/planks/plank323");
    private static final ResourceLocation RESLOC_PLANK_330 = new ResourceLocation(MODID, "woodbits/planks/plank330");
    private static final ResourceLocation RESLOC_PLANK_331 = new ResourceLocation(MODID, "woodbits/planks/plank331");
    private static final ResourceLocation RESLOC_PLANK_332 = new ResourceLocation(MODID, "woodbits/planks/plank332");
    private static final ResourceLocation RESLOC_PLANK_333 = new ResourceLocation(MODID, "woodbits/planks/plank333");

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(ITEM_PLANK_000);
        event.getRegistry().register(ITEM_PLANK_001);
        event.getRegistry().register(ITEM_PLANK_002);
        event.getRegistry().register(ITEM_PLANK_003);
        event.getRegistry().register(ITEM_PLANK_010);
        event.getRegistry().register(ITEM_PLANK_011);
        event.getRegistry().register(ITEM_PLANK_012);
        event.getRegistry().register(ITEM_PLANK_013);
        event.getRegistry().register(ITEM_PLANK_020);
        event.getRegistry().register(ITEM_PLANK_021);
        event.getRegistry().register(ITEM_PLANK_022);
        event.getRegistry().register(ITEM_PLANK_023);
        event.getRegistry().register(ITEM_PLANK_030);
        event.getRegistry().register(ITEM_PLANK_031);
        event.getRegistry().register(ITEM_PLANK_032);
        event.getRegistry().register(ITEM_PLANK_033);

        event.getRegistry().register(ITEM_PLANK_100);
        event.getRegistry().register(ITEM_PLANK_101);
        event.getRegistry().register(ITEM_PLANK_102);
        event.getRegistry().register(ITEM_PLANK_103);
        event.getRegistry().register(ITEM_PLANK_110);
        event.getRegistry().register(ITEM_PLANK_111);
        event.getRegistry().register(ITEM_PLANK_112);
        event.getRegistry().register(ITEM_PLANK_113);
        event.getRegistry().register(ITEM_PLANK_120);
        event.getRegistry().register(ITEM_PLANK_121);
        event.getRegistry().register(ITEM_PLANK_122);
        event.getRegistry().register(ITEM_PLANK_123);
        event.getRegistry().register(ITEM_PLANK_130);
        event.getRegistry().register(ITEM_PLANK_131);
        event.getRegistry().register(ITEM_PLANK_132);
        event.getRegistry().register(ITEM_PLANK_133);

        event.getRegistry().register(ITEM_PLANK_200);
        event.getRegistry().register(ITEM_PLANK_201);
        event.getRegistry().register(ITEM_PLANK_202);
        event.getRegistry().register(ITEM_PLANK_203);
        event.getRegistry().register(ITEM_PLANK_210);
        event.getRegistry().register(ITEM_PLANK_211);
        event.getRegistry().register(ITEM_PLANK_212);
        event.getRegistry().register(ITEM_PLANK_213);
        event.getRegistry().register(ITEM_PLANK_220);
        event.getRegistry().register(ITEM_PLANK_221);
        event.getRegistry().register(ITEM_PLANK_222);
        event.getRegistry().register(ITEM_PLANK_223);
        event.getRegistry().register(ITEM_PLANK_230);
        event.getRegistry().register(ITEM_PLANK_231);
        event.getRegistry().register(ITEM_PLANK_232);
        event.getRegistry().register(ITEM_PLANK_233);

        event.getRegistry().register(ITEM_PLANK_300);
        event.getRegistry().register(ITEM_PLANK_301);
        event.getRegistry().register(ITEM_PLANK_302);
        event.getRegistry().register(ITEM_PLANK_303);
        event.getRegistry().register(ITEM_PLANK_310);
        event.getRegistry().register(ITEM_PLANK_311);
        event.getRegistry().register(ITEM_PLANK_312);
        event.getRegistry().register(ITEM_PLANK_313);
        event.getRegistry().register(ITEM_PLANK_320);
        event.getRegistry().register(ITEM_PLANK_321);
        event.getRegistry().register(ITEM_PLANK_322);
        event.getRegistry().register(ITEM_PLANK_323);
        event.getRegistry().register(ITEM_PLANK_330);
        event.getRegistry().register(ITEM_PLANK_331);
        event.getRegistry().register(ITEM_PLANK_332);
        event.getRegistry().register(ITEM_PLANK_333);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(PLANK_000);
        event.getRegistry().register(PLANK_001);
        event.getRegistry().register(PLANK_002);
        event.getRegistry().register(PLANK_003);
        event.getRegistry().register(PLANK_010);
        event.getRegistry().register(PLANK_011);
        event.getRegistry().register(PLANK_012);
        event.getRegistry().register(PLANK_013);
        event.getRegistry().register(PLANK_020);
        event.getRegistry().register(PLANK_021);
        event.getRegistry().register(PLANK_022);
        event.getRegistry().register(PLANK_023);
        event.getRegistry().register(PLANK_030);
        event.getRegistry().register(PLANK_031);
        event.getRegistry().register(PLANK_032);
        event.getRegistry().register(PLANK_033);

        event.getRegistry().register(PLANK_100);
        event.getRegistry().register(PLANK_101);
        event.getRegistry().register(PLANK_102);
        event.getRegistry().register(PLANK_103);
        event.getRegistry().register(PLANK_110);
        event.getRegistry().register(PLANK_111);
        event.getRegistry().register(PLANK_112);
        event.getRegistry().register(PLANK_113);
        event.getRegistry().register(PLANK_120);
        event.getRegistry().register(PLANK_121);
        event.getRegistry().register(PLANK_122);
        event.getRegistry().register(PLANK_123);
        event.getRegistry().register(PLANK_130);
        event.getRegistry().register(PLANK_131);
        event.getRegistry().register(PLANK_132);
        event.getRegistry().register(PLANK_133);

        event.getRegistry().register(PLANK_200);
        event.getRegistry().register(PLANK_201);
        event.getRegistry().register(PLANK_202);
        event.getRegistry().register(PLANK_203);
        event.getRegistry().register(PLANK_210);
        event.getRegistry().register(PLANK_211);
        event.getRegistry().register(PLANK_212);
        event.getRegistry().register(PLANK_213);
        event.getRegistry().register(PLANK_220);
        event.getRegistry().register(PLANK_221);
        event.getRegistry().register(PLANK_222);
        event.getRegistry().register(PLANK_223);
        event.getRegistry().register(PLANK_230);
        event.getRegistry().register(PLANK_231);
        event.getRegistry().register(PLANK_232);
        event.getRegistry().register(PLANK_233);

        event.getRegistry().register(PLANK_300);
        event.getRegistry().register(PLANK_301);
        event.getRegistry().register(PLANK_302);
        event.getRegistry().register(PLANK_303);
        event.getRegistry().register(PLANK_310);
        event.getRegistry().register(PLANK_311);
        event.getRegistry().register(PLANK_312);
        event.getRegistry().register(PLANK_313);
        event.getRegistry().register(PLANK_320);
        event.getRegistry().register(PLANK_321);
        event.getRegistry().register(PLANK_322);
        event.getRegistry().register(PLANK_323);
        event.getRegistry().register(PLANK_330);
        event.getRegistry().register(PLANK_331);
        event.getRegistry().register(PLANK_332);
        event.getRegistry().register(PLANK_333);
    }



    public static void handleBlockColors(ColorHandlerEvent.Block event) {

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 0;} else {return -1;}},PLANK_000);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 85;} else {return -1;}},PLANK_001);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 170;} else {return -1;}},PLANK_002);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 255;} else {return -1;}},PLANK_003);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21760;} else {return -1;}},PLANK_010);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21845;} else {return -1;}},PLANK_011);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21930;} else {return -1;}},PLANK_012);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 22015;} else {return -1;}},PLANK_013);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43520;} else {return -1;}},PLANK_020);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43605;} else {return -1;}},PLANK_021);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43690;} else {return -1;}},PLANK_022);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43775;} else {return -1;}},PLANK_023);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65280;} else {return -1;}},PLANK_030);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65365;} else {return -1;}},PLANK_031);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65450;} else {return -1;}},PLANK_032);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65535;} else {return -1;}},PLANK_033);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570560;} else {return -1;}},PLANK_100);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570645;} else {return -1;}},PLANK_101);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570730;} else {return -1;}},PLANK_102);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570815;} else {return -1;}},PLANK_103);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592320;} else {return -1;}},PLANK_110);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592405;} else {return -1;}},PLANK_111);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592490;} else {return -1;}},PLANK_112);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592575;} else {return -1;}},PLANK_113);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614080;} else {return -1;}},PLANK_120);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614165;} else {return -1;}},PLANK_121);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614250;} else {return -1;}},PLANK_122);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614335;} else {return -1;}},PLANK_123);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5635840;} else {return -1;}},PLANK_130);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5635925;} else {return -1;}},PLANK_131);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5636010;} else {return -1;}},PLANK_132);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5636095;} else {return -1;}},PLANK_133);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141120;} else {return -1;}},PLANK_200);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141205;} else {return -1;}},PLANK_201);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141290;} else {return -1;}},PLANK_202);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141375;} else {return -1;}},PLANK_203);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11162880;} else {return -1;}},PLANK_210);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11162965;} else {return -1;}},PLANK_211);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11163050;} else {return -1;}},PLANK_212);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11163135;} else {return -1;}},PLANK_213);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184640;} else {return -1;}},PLANK_220);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184725;} else {return -1;}},PLANK_221);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184810;} else {return -1;}},PLANK_222);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184895;} else {return -1;}},PLANK_223);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206400;} else {return -1;}},PLANK_230);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206485;} else {return -1;}},PLANK_231);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206570;} else {return -1;}},PLANK_232);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206655;} else {return -1;}},PLANK_233);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711680;} else {return -1;}},PLANK_300);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711765;} else {return -1;}},PLANK_301);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711850;} else {return -1;}},PLANK_302);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711935;} else {return -1;}},PLANK_303);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733440;} else {return -1;}},PLANK_310);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733525;} else {return -1;}},PLANK_311);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733610;} else {return -1;}},PLANK_312);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733695;} else {return -1;}},PLANK_313);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755200;} else {return -1;}},PLANK_320);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755285;} else {return -1;}},PLANK_321);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755370;} else {return -1;}},PLANK_322);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755455;} else {return -1;}},PLANK_323);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16776960;} else {return -1;}},PLANK_330);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777045;} else {return -1;}},PLANK_331);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777130;} else {return -1;}},PLANK_332);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777215;} else {return -1;}},PLANK_333);
    }

    public static void handleItemColors(ColorHandlerEvent.Item event) {

        //event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 0;} else {return -1;}},ITEM_PLANK_000);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 0;} else {return -1;}},ITEM_PLANK_000);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 85;} else {return -1;}},ITEM_PLANK_001);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 170;} else {return -1;}},ITEM_PLANK_002);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 255;} else {return -1;}},ITEM_PLANK_003);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21760;} else {return -1;}},ITEM_PLANK_010);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21845;} else {return -1;}},ITEM_PLANK_011);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21930;} else {return -1;}},ITEM_PLANK_012);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 22015;} else {return -1;}},ITEM_PLANK_013);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43520;} else {return -1;}},ITEM_PLANK_020);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43605;} else {return -1;}},ITEM_PLANK_021);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43690;} else {return -1;}},ITEM_PLANK_022);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43775;} else {return -1;}},ITEM_PLANK_023);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65280;} else {return -1;}},ITEM_PLANK_030);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65365;} else {return -1;}},ITEM_PLANK_031);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65450;} else {return -1;}},ITEM_PLANK_032);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65535;} else {return -1;}},ITEM_PLANK_033);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570560;} else {return -1;}},ITEM_PLANK_100);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570645;} else {return -1;}},ITEM_PLANK_101);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570730;} else {return -1;}},ITEM_PLANK_102);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570815;} else {return -1;}},ITEM_PLANK_103);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592320;} else {return -1;}},ITEM_PLANK_110);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592405;} else {return -1;}},ITEM_PLANK_111);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592490;} else {return -1;}},ITEM_PLANK_112);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592575;} else {return -1;}},ITEM_PLANK_113);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614080;} else {return -1;}},ITEM_PLANK_120);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614165;} else {return -1;}},ITEM_PLANK_121);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614250;} else {return -1;}},ITEM_PLANK_122);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614335;} else {return -1;}},ITEM_PLANK_123);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5635840;} else {return -1;}},ITEM_PLANK_130);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5635925;} else {return -1;}},ITEM_PLANK_131);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5636010;} else {return -1;}},ITEM_PLANK_132);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5636095;} else {return -1;}},ITEM_PLANK_133);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141120;} else {return -1;}},ITEM_PLANK_200);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141205;} else {return -1;}},ITEM_PLANK_201);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141290;} else {return -1;}},ITEM_PLANK_202);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141375;} else {return -1;}},ITEM_PLANK_203);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11162880;} else {return -1;}},ITEM_PLANK_210);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11162965;} else {return -1;}},ITEM_PLANK_211);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11163050;} else {return -1;}},ITEM_PLANK_212);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11163135;} else {return -1;}},ITEM_PLANK_213);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184640;} else {return -1;}},ITEM_PLANK_220);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184725;} else {return -1;}},ITEM_PLANK_221);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184810;} else {return -1;}},ITEM_PLANK_222);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184895;} else {return -1;}},ITEM_PLANK_223);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206400;} else {return -1;}},ITEM_PLANK_230);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206485;} else {return -1;}},ITEM_PLANK_231);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206570;} else {return -1;}},ITEM_PLANK_232);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206655;} else {return -1;}},ITEM_PLANK_233);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711680;} else {return -1;}},ITEM_PLANK_300);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711765;} else {return -1;}},ITEM_PLANK_301);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711850;} else {return -1;}},ITEM_PLANK_302);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711935;} else {return -1;}},ITEM_PLANK_303);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733440;} else {return -1;}},ITEM_PLANK_310);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733525;} else {return -1;}},ITEM_PLANK_311);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733610;} else {return -1;}},ITEM_PLANK_312);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733695;} else {return -1;}},ITEM_PLANK_313);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755200;} else {return -1;}},ITEM_PLANK_320);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755285;} else {return -1;}},ITEM_PLANK_321);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755370;} else {return -1;}},ITEM_PLANK_322);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755455;} else {return -1;}},ITEM_PLANK_323);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16776960;} else {return -1;}},ITEM_PLANK_330);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777045;} else {return -1;}},ITEM_PLANK_331);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777130;} else {return -1;}},ITEM_PLANK_332);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777215;} else {return -1;}},ITEM_PLANK_333);
    }

    public static final Block PLANK_000 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_000);
    public static final Block PLANK_001 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_001);
    public static final Block PLANK_002 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_002);
    public static final Block PLANK_003 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_003);
    public static final Block PLANK_010 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_010);
    public static final Block PLANK_011 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_011);
    public static final Block PLANK_012 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_012);
    public static final Block PLANK_013 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_013);
    public static final Block PLANK_020 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_020);
    public static final Block PLANK_021 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_021);
    public static final Block PLANK_022 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_022);
    public static final Block PLANK_023 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_023);
    public static final Block PLANK_030 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_030);
    public static final Block PLANK_031 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_031);
    public static final Block PLANK_032 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_032);
    public static final Block PLANK_033 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_033);

    public static final Block PLANK_100 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_100);
    public static final Block PLANK_101 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_101);
    public static final Block PLANK_102 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_102);
    public static final Block PLANK_103 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_103);
    public static final Block PLANK_110 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_110);
    public static final Block PLANK_111 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_111);
    public static final Block PLANK_112 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_112);
    public static final Block PLANK_113 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_113);
    public static final Block PLANK_120 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_120);
    public static final Block PLANK_121 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_121);
    public static final Block PLANK_122 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_122);
    public static final Block PLANK_123 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_123);
    public static final Block PLANK_130 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_130);
    public static final Block PLANK_131 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_131);
    public static final Block PLANK_132 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_132);
    public static final Block PLANK_133 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_133);

    public static final Block PLANK_200 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_200);
    public static final Block PLANK_201 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_201);
    public static final Block PLANK_202 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_202);
    public static final Block PLANK_203 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_203);
    public static final Block PLANK_210 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_210);
    public static final Block PLANK_211 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_211);
    public static final Block PLANK_212 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_212);
    public static final Block PLANK_213 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_213);
    public static final Block PLANK_220 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_220);
    public static final Block PLANK_221 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_221);
    public static final Block PLANK_222 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_222);
    public static final Block PLANK_223 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_223);
    public static final Block PLANK_230 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_230);
    public static final Block PLANK_231 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_231);
    public static final Block PLANK_232 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_232);
    public static final Block PLANK_233 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_233);

    public static final Block PLANK_300 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_300);
    public static final Block PLANK_301 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_301);
    public static final Block PLANK_302 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_302);
    public static final Block PLANK_303 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_303);
    public static final Block PLANK_310 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_310);
    public static final Block PLANK_311 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_311);
    public static final Block PLANK_312 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_312);
    public static final Block PLANK_313 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_313);
    public static final Block PLANK_320 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_320);
    public static final Block PLANK_321 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_321);
    public static final Block PLANK_322 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_322);
    public static final Block PLANK_323 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_323);
    public static final Block PLANK_330 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_330);
    public static final Block PLANK_331 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_331);
    public static final Block PLANK_332 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_332);
    public static final Block PLANK_333 = new BlockDustPlanks(Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.WOOD)).setRegistryName(RESLOC_PLANK_333);

    public static final Item ITEM_PLANK_000 = new BlockItem(PLANK_000, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_000);
    public static final Item ITEM_PLANK_001 = new BlockItem(PLANK_001, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_001);
    public static final Item ITEM_PLANK_002 = new BlockItem(PLANK_002, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_002);
    public static final Item ITEM_PLANK_003 = new BlockItem(PLANK_003, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_003);
    public static final Item ITEM_PLANK_010 = new BlockItem(PLANK_010, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_010);
    public static final Item ITEM_PLANK_011 = new BlockItem(PLANK_011, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_011);
    public static final Item ITEM_PLANK_012 = new BlockItem(PLANK_012, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_012);
    public static final Item ITEM_PLANK_013 = new BlockItem(PLANK_013, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_013);
    public static final Item ITEM_PLANK_020 = new BlockItem(PLANK_020, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_020);
    public static final Item ITEM_PLANK_021 = new BlockItem(PLANK_021, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_021);
    public static final Item ITEM_PLANK_022 = new BlockItem(PLANK_022, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_022);
    public static final Item ITEM_PLANK_023 = new BlockItem(PLANK_023, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_023);
    public static final Item ITEM_PLANK_030 = new BlockItem(PLANK_030, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_030);
    public static final Item ITEM_PLANK_031 = new BlockItem(PLANK_031, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_031);
    public static final Item ITEM_PLANK_032 = new BlockItem(PLANK_032, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_032);
    public static final Item ITEM_PLANK_033 = new BlockItem(PLANK_033, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_033);

    public static final Item ITEM_PLANK_100 = new BlockItem(PLANK_100, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_100);
    public static final Item ITEM_PLANK_101 = new BlockItem(PLANK_101, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_101);
    public static final Item ITEM_PLANK_102 = new BlockItem(PLANK_102, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_102);
    public static final Item ITEM_PLANK_103 = new BlockItem(PLANK_103, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_103);
    public static final Item ITEM_PLANK_110 = new BlockItem(PLANK_110, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_110);
    public static final Item ITEM_PLANK_111 = new BlockItem(PLANK_111, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_111);
    public static final Item ITEM_PLANK_112 = new BlockItem(PLANK_112, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_112);
    public static final Item ITEM_PLANK_113 = new BlockItem(PLANK_113, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_113);
    public static final Item ITEM_PLANK_120 = new BlockItem(PLANK_120, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_120);
    public static final Item ITEM_PLANK_121 = new BlockItem(PLANK_121, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_121);
    public static final Item ITEM_PLANK_122 = new BlockItem(PLANK_122, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_122);
    public static final Item ITEM_PLANK_123 = new BlockItem(PLANK_123, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_123);
    public static final Item ITEM_PLANK_130 = new BlockItem(PLANK_130, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_130);
    public static final Item ITEM_PLANK_131 = new BlockItem(PLANK_131, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_131);
    public static final Item ITEM_PLANK_132 = new BlockItem(PLANK_132, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_132);
    public static final Item ITEM_PLANK_133 = new BlockItem(PLANK_133, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_133);

    public static final Item ITEM_PLANK_200 = new BlockItem(PLANK_200, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_200);
    public static final Item ITEM_PLANK_201 = new BlockItem(PLANK_201, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_201);
    public static final Item ITEM_PLANK_202 = new BlockItem(PLANK_202, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_202);
    public static final Item ITEM_PLANK_203 = new BlockItem(PLANK_203, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_203);
    public static final Item ITEM_PLANK_210 = new BlockItem(PLANK_210, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_210);
    public static final Item ITEM_PLANK_211 = new BlockItem(PLANK_211, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_211);
    public static final Item ITEM_PLANK_212 = new BlockItem(PLANK_212, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_212);
    public static final Item ITEM_PLANK_213 = new BlockItem(PLANK_213, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_213);
    public static final Item ITEM_PLANK_220 = new BlockItem(PLANK_220, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_220);
    public static final Item ITEM_PLANK_221 = new BlockItem(PLANK_221, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_221);
    public static final Item ITEM_PLANK_222 = new BlockItem(PLANK_222, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_222);
    public static final Item ITEM_PLANK_223 = new BlockItem(PLANK_223, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_223);
    public static final Item ITEM_PLANK_230 = new BlockItem(PLANK_230, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_230);
    public static final Item ITEM_PLANK_231 = new BlockItem(PLANK_231, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_231);
    public static final Item ITEM_PLANK_232 = new BlockItem(PLANK_232, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_232);
    public static final Item ITEM_PLANK_233 = new BlockItem(PLANK_233, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_233);

    public static final Item ITEM_PLANK_300 = new BlockItem(PLANK_300, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_300);
    public static final Item ITEM_PLANK_301 = new BlockItem(PLANK_301, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_301);
    public static final Item ITEM_PLANK_302 = new BlockItem(PLANK_302, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_302);
    public static final Item ITEM_PLANK_303 = new BlockItem(PLANK_303, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_303);
    public static final Item ITEM_PLANK_310 = new BlockItem(PLANK_310, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_310);
    public static final Item ITEM_PLANK_311 = new BlockItem(PLANK_311, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_311);
    public static final Item ITEM_PLANK_312 = new BlockItem(PLANK_312, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_312);
    public static final Item ITEM_PLANK_313 = new BlockItem(PLANK_313, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_313);
    public static final Item ITEM_PLANK_320 = new BlockItem(PLANK_320, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_320);
    public static final Item ITEM_PLANK_321 = new BlockItem(PLANK_321, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_321);
    public static final Item ITEM_PLANK_322 = new BlockItem(PLANK_322, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_322);
    public static final Item ITEM_PLANK_323 = new BlockItem(PLANK_323, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_323);
    public static final Item ITEM_PLANK_330 = new BlockItem(PLANK_330, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_330);
    public static final Item ITEM_PLANK_331 = new BlockItem(PLANK_331, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_331);
    public static final Item ITEM_PLANK_332 = new BlockItem(PLANK_332, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_332);
    public static final Item ITEM_PLANK_333 = new BlockItem(PLANK_333, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_PLANK_333);
}
