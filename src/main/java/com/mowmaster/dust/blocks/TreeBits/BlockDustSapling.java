package com.mowmaster.dust.blocks.TreeBits;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.trees.Tree;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.mowmaster.dust.dust.BLOCK_GROUP;
import static com.mowmaster.dust.references.Reference.MODID;

public class BlockDustSapling extends SaplingBlock
{
    public BlockDustSapling(Tree treeIn, Properties builder) {
        super(treeIn,builder);
    }

    private static final ResourceLocation RESLOC_SAPLING_000 = new ResourceLocation(MODID, "woodbits/saplings/sapling000");
    private static final ResourceLocation RESLOC_SAPLING_001 = new ResourceLocation(MODID, "woodbits/saplings/sapling001");
    private static final ResourceLocation RESLOC_SAPLING_002 = new ResourceLocation(MODID, "woodbits/saplings/sapling002");
    private static final ResourceLocation RESLOC_SAPLING_003 = new ResourceLocation(MODID, "woodbits/saplings/sapling003");
    private static final ResourceLocation RESLOC_SAPLING_010 = new ResourceLocation(MODID, "woodbits/saplings/sapling010");
    private static final ResourceLocation RESLOC_SAPLING_011 = new ResourceLocation(MODID, "woodbits/saplings/sapling011");
    private static final ResourceLocation RESLOC_SAPLING_012 = new ResourceLocation(MODID, "woodbits/saplings/sapling012");
    private static final ResourceLocation RESLOC_SAPLING_013 = new ResourceLocation(MODID, "woodbits/saplings/sapling013");
    private static final ResourceLocation RESLOC_SAPLING_020 = new ResourceLocation(MODID, "woodbits/saplings/sapling020");
    private static final ResourceLocation RESLOC_SAPLING_021 = new ResourceLocation(MODID, "woodbits/saplings/sapling021");
    private static final ResourceLocation RESLOC_SAPLING_022 = new ResourceLocation(MODID, "woodbits/saplings/sapling022");
    private static final ResourceLocation RESLOC_SAPLING_023 = new ResourceLocation(MODID, "woodbits/saplings/sapling023");
    private static final ResourceLocation RESLOC_SAPLING_030 = new ResourceLocation(MODID, "woodbits/saplings/sapling030");
    private static final ResourceLocation RESLOC_SAPLING_031 = new ResourceLocation(MODID, "woodbits/saplings/sapling031");
    private static final ResourceLocation RESLOC_SAPLING_032 = new ResourceLocation(MODID, "woodbits/saplings/sapling032");
    private static final ResourceLocation RESLOC_SAPLING_033 = new ResourceLocation(MODID, "woodbits/saplings/sapling033");

    private static final ResourceLocation RESLOC_SAPLING_100 = new ResourceLocation(MODID, "woodbits/saplings/sapling100");
    private static final ResourceLocation RESLOC_SAPLING_101 = new ResourceLocation(MODID, "woodbits/saplings/sapling101");
    private static final ResourceLocation RESLOC_SAPLING_102 = new ResourceLocation(MODID, "woodbits/saplings/sapling102");
    private static final ResourceLocation RESLOC_SAPLING_103 = new ResourceLocation(MODID, "woodbits/saplings/sapling103");
    private static final ResourceLocation RESLOC_SAPLING_110 = new ResourceLocation(MODID, "woodbits/saplings/sapling110");
    private static final ResourceLocation RESLOC_SAPLING_111 = new ResourceLocation(MODID, "woodbits/saplings/sapling111");
    private static final ResourceLocation RESLOC_SAPLING_112 = new ResourceLocation(MODID, "woodbits/saplings/sapling112");
    private static final ResourceLocation RESLOC_SAPLING_113 = new ResourceLocation(MODID, "woodbits/saplings/sapling113");
    private static final ResourceLocation RESLOC_SAPLING_120 = new ResourceLocation(MODID, "woodbits/saplings/sapling120");
    private static final ResourceLocation RESLOC_SAPLING_121 = new ResourceLocation(MODID, "woodbits/saplings/sapling121");
    private static final ResourceLocation RESLOC_SAPLING_122 = new ResourceLocation(MODID, "woodbits/saplings/sapling122");
    private static final ResourceLocation RESLOC_SAPLING_123 = new ResourceLocation(MODID, "woodbits/saplings/sapling123");
    private static final ResourceLocation RESLOC_SAPLING_130 = new ResourceLocation(MODID, "woodbits/saplings/sapling130");
    private static final ResourceLocation RESLOC_SAPLING_131 = new ResourceLocation(MODID, "woodbits/saplings/sapling131");
    private static final ResourceLocation RESLOC_SAPLING_132 = new ResourceLocation(MODID, "woodbits/saplings/sapling132");
    private static final ResourceLocation RESLOC_SAPLING_133 = new ResourceLocation(MODID, "woodbits/saplings/sapling133");

    private static final ResourceLocation RESLOC_SAPLING_200 = new ResourceLocation(MODID, "woodbits/saplings/sapling200");
    private static final ResourceLocation RESLOC_SAPLING_201 = new ResourceLocation(MODID, "woodbits/saplings/sapling201");
    private static final ResourceLocation RESLOC_SAPLING_202 = new ResourceLocation(MODID, "woodbits/saplings/sapling202");
    private static final ResourceLocation RESLOC_SAPLING_203 = new ResourceLocation(MODID, "woodbits/saplings/sapling203");
    private static final ResourceLocation RESLOC_SAPLING_210 = new ResourceLocation(MODID, "woodbits/saplings/sapling210");
    private static final ResourceLocation RESLOC_SAPLING_211 = new ResourceLocation(MODID, "woodbits/saplings/sapling211");
    private static final ResourceLocation RESLOC_SAPLING_212 = new ResourceLocation(MODID, "woodbits/saplings/sapling212");
    private static final ResourceLocation RESLOC_SAPLING_213 = new ResourceLocation(MODID, "woodbits/saplings/sapling213");
    private static final ResourceLocation RESLOC_SAPLING_220 = new ResourceLocation(MODID, "woodbits/saplings/sapling220");
    private static final ResourceLocation RESLOC_SAPLING_221 = new ResourceLocation(MODID, "woodbits/saplings/sapling221");
    private static final ResourceLocation RESLOC_SAPLING_222 = new ResourceLocation(MODID, "woodbits/saplings/sapling222");
    private static final ResourceLocation RESLOC_SAPLING_223 = new ResourceLocation(MODID, "woodbits/saplings/sapling223");
    private static final ResourceLocation RESLOC_SAPLING_230 = new ResourceLocation(MODID, "woodbits/saplings/sapling230");
    private static final ResourceLocation RESLOC_SAPLING_231 = new ResourceLocation(MODID, "woodbits/saplings/sapling231");
    private static final ResourceLocation RESLOC_SAPLING_232 = new ResourceLocation(MODID, "woodbits/saplings/sapling232");
    private static final ResourceLocation RESLOC_SAPLING_233 = new ResourceLocation(MODID, "woodbits/saplings/sapling233");

    private static final ResourceLocation RESLOC_SAPLING_300 = new ResourceLocation(MODID, "woodbits/saplings/sapling300");
    private static final ResourceLocation RESLOC_SAPLING_301 = new ResourceLocation(MODID, "woodbits/saplings/sapling301");
    private static final ResourceLocation RESLOC_SAPLING_302 = new ResourceLocation(MODID, "woodbits/saplings/sapling302");
    private static final ResourceLocation RESLOC_SAPLING_303 = new ResourceLocation(MODID, "woodbits/saplings/sapling303");
    private static final ResourceLocation RESLOC_SAPLING_310 = new ResourceLocation(MODID, "woodbits/saplings/sapling310");
    private static final ResourceLocation RESLOC_SAPLING_311 = new ResourceLocation(MODID, "woodbits/saplings/sapling311");
    private static final ResourceLocation RESLOC_SAPLING_312 = new ResourceLocation(MODID, "woodbits/saplings/sapling312");
    private static final ResourceLocation RESLOC_SAPLING_313 = new ResourceLocation(MODID, "woodbits/saplings/sapling313");
    private static final ResourceLocation RESLOC_SAPLING_320 = new ResourceLocation(MODID, "woodbits/saplings/sapling320");
    private static final ResourceLocation RESLOC_SAPLING_321 = new ResourceLocation(MODID, "woodbits/saplings/sapling321");
    private static final ResourceLocation RESLOC_SAPLING_322 = new ResourceLocation(MODID, "woodbits/saplings/sapling322");
    private static final ResourceLocation RESLOC_SAPLING_323 = new ResourceLocation(MODID, "woodbits/saplings/sapling323");
    private static final ResourceLocation RESLOC_SAPLING_330 = new ResourceLocation(MODID, "woodbits/saplings/sapling330");
    private static final ResourceLocation RESLOC_SAPLING_331 = new ResourceLocation(MODID, "woodbits/saplings/sapling331");
    private static final ResourceLocation RESLOC_SAPLING_332 = new ResourceLocation(MODID, "woodbits/saplings/sapling332");
    private static final ResourceLocation RESLOC_SAPLING_333 = new ResourceLocation(MODID, "woodbits/saplings/sapling333");

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(ITEM_SAPLING_000);
        event.getRegistry().register(ITEM_SAPLING_001);
        event.getRegistry().register(ITEM_SAPLING_002);
        event.getRegistry().register(ITEM_SAPLING_003);
        event.getRegistry().register(ITEM_SAPLING_010);
        event.getRegistry().register(ITEM_SAPLING_011);
        event.getRegistry().register(ITEM_SAPLING_012);
        event.getRegistry().register(ITEM_SAPLING_013);
        event.getRegistry().register(ITEM_SAPLING_020);
        event.getRegistry().register(ITEM_SAPLING_021);
        event.getRegistry().register(ITEM_SAPLING_022);
        event.getRegistry().register(ITEM_SAPLING_023);
        event.getRegistry().register(ITEM_SAPLING_030);
        event.getRegistry().register(ITEM_SAPLING_031);
        event.getRegistry().register(ITEM_SAPLING_032);
        event.getRegistry().register(ITEM_SAPLING_033);

        event.getRegistry().register(ITEM_SAPLING_100);
        event.getRegistry().register(ITEM_SAPLING_101);
        event.getRegistry().register(ITEM_SAPLING_102);
        event.getRegistry().register(ITEM_SAPLING_103);
        event.getRegistry().register(ITEM_SAPLING_110);
        event.getRegistry().register(ITEM_SAPLING_111);
        event.getRegistry().register(ITEM_SAPLING_112);
        event.getRegistry().register(ITEM_SAPLING_113);
        event.getRegistry().register(ITEM_SAPLING_120);
        event.getRegistry().register(ITEM_SAPLING_121);
        event.getRegistry().register(ITEM_SAPLING_122);
        event.getRegistry().register(ITEM_SAPLING_123);
        event.getRegistry().register(ITEM_SAPLING_130);
        event.getRegistry().register(ITEM_SAPLING_131);
        event.getRegistry().register(ITEM_SAPLING_132);
        event.getRegistry().register(ITEM_SAPLING_133);

        event.getRegistry().register(ITEM_SAPLING_200);
        event.getRegistry().register(ITEM_SAPLING_201);
        event.getRegistry().register(ITEM_SAPLING_202);
        event.getRegistry().register(ITEM_SAPLING_203);
        event.getRegistry().register(ITEM_SAPLING_210);
        event.getRegistry().register(ITEM_SAPLING_211);
        event.getRegistry().register(ITEM_SAPLING_212);
        event.getRegistry().register(ITEM_SAPLING_213);
        event.getRegistry().register(ITEM_SAPLING_220);
        event.getRegistry().register(ITEM_SAPLING_221);
        event.getRegistry().register(ITEM_SAPLING_222);
        event.getRegistry().register(ITEM_SAPLING_223);
        event.getRegistry().register(ITEM_SAPLING_230);
        event.getRegistry().register(ITEM_SAPLING_231);
        event.getRegistry().register(ITEM_SAPLING_232);
        event.getRegistry().register(ITEM_SAPLING_233);

        event.getRegistry().register(ITEM_SAPLING_300);
        event.getRegistry().register(ITEM_SAPLING_301);
        event.getRegistry().register(ITEM_SAPLING_302);
        event.getRegistry().register(ITEM_SAPLING_303);
        event.getRegistry().register(ITEM_SAPLING_310);
        event.getRegistry().register(ITEM_SAPLING_311);
        event.getRegistry().register(ITEM_SAPLING_312);
        event.getRegistry().register(ITEM_SAPLING_313);
        event.getRegistry().register(ITEM_SAPLING_320);
        event.getRegistry().register(ITEM_SAPLING_321);
        event.getRegistry().register(ITEM_SAPLING_322);
        event.getRegistry().register(ITEM_SAPLING_323);
        event.getRegistry().register(ITEM_SAPLING_330);
        event.getRegistry().register(ITEM_SAPLING_331);
        event.getRegistry().register(ITEM_SAPLING_332);
        event.getRegistry().register(ITEM_SAPLING_333);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(SAPLING_000);
        event.getRegistry().register(SAPLING_001);
        event.getRegistry().register(SAPLING_002);
        event.getRegistry().register(SAPLING_003);
        event.getRegistry().register(SAPLING_010);
        event.getRegistry().register(SAPLING_011);
        event.getRegistry().register(SAPLING_012);
        event.getRegistry().register(SAPLING_013);
        event.getRegistry().register(SAPLING_020);
        event.getRegistry().register(SAPLING_021);
        event.getRegistry().register(SAPLING_022);
        event.getRegistry().register(SAPLING_023);
        event.getRegistry().register(SAPLING_030);
        event.getRegistry().register(SAPLING_031);
        event.getRegistry().register(SAPLING_032);
        event.getRegistry().register(SAPLING_033);

        event.getRegistry().register(SAPLING_100);
        event.getRegistry().register(SAPLING_101);
        event.getRegistry().register(SAPLING_102);
        event.getRegistry().register(SAPLING_103);
        event.getRegistry().register(SAPLING_110);
        event.getRegistry().register(SAPLING_111);
        event.getRegistry().register(SAPLING_112);
        event.getRegistry().register(SAPLING_113);
        event.getRegistry().register(SAPLING_120);
        event.getRegistry().register(SAPLING_121);
        event.getRegistry().register(SAPLING_122);
        event.getRegistry().register(SAPLING_123);
        event.getRegistry().register(SAPLING_130);
        event.getRegistry().register(SAPLING_131);
        event.getRegistry().register(SAPLING_132);
        event.getRegistry().register(SAPLING_133);

        event.getRegistry().register(SAPLING_200);
        event.getRegistry().register(SAPLING_201);
        event.getRegistry().register(SAPLING_202);
        event.getRegistry().register(SAPLING_203);
        event.getRegistry().register(SAPLING_210);
        event.getRegistry().register(SAPLING_211);
        event.getRegistry().register(SAPLING_212);
        event.getRegistry().register(SAPLING_213);
        event.getRegistry().register(SAPLING_220);
        event.getRegistry().register(SAPLING_221);
        event.getRegistry().register(SAPLING_222);
        event.getRegistry().register(SAPLING_223);
        event.getRegistry().register(SAPLING_230);
        event.getRegistry().register(SAPLING_231);
        event.getRegistry().register(SAPLING_232);
        event.getRegistry().register(SAPLING_233);

        event.getRegistry().register(SAPLING_300);
        event.getRegistry().register(SAPLING_301);
        event.getRegistry().register(SAPLING_302);
        event.getRegistry().register(SAPLING_303);
        event.getRegistry().register(SAPLING_310);
        event.getRegistry().register(SAPLING_311);
        event.getRegistry().register(SAPLING_312);
        event.getRegistry().register(SAPLING_313);
        event.getRegistry().register(SAPLING_320);
        event.getRegistry().register(SAPLING_321);
        event.getRegistry().register(SAPLING_322);
        event.getRegistry().register(SAPLING_323);
        event.getRegistry().register(SAPLING_330);
        event.getRegistry().register(SAPLING_331);
        event.getRegistry().register(SAPLING_332);
        event.getRegistry().register(SAPLING_333);
    }



    public static void handleBlockColors(ColorHandlerEvent.Block event) {

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 0;} else {return -1;}},SAPLING_000);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 85;} else {return -1;}},SAPLING_001);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 170;} else {return -1;}},SAPLING_002);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 255;} else {return -1;}},SAPLING_003);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21760;} else {return -1;}},SAPLING_010);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21845;} else {return -1;}},SAPLING_011);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 21930;} else {return -1;}},SAPLING_012);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 22015;} else {return -1;}},SAPLING_013);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43520;} else {return -1;}},SAPLING_020);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43605;} else {return -1;}},SAPLING_021);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43690;} else {return -1;}},SAPLING_022);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 43775;} else {return -1;}},SAPLING_023);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65280;} else {return -1;}},SAPLING_030);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65365;} else {return -1;}},SAPLING_031);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65450;} else {return -1;}},SAPLING_032);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 65535;} else {return -1;}},SAPLING_033);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570560;} else {return -1;}},SAPLING_100);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570645;} else {return -1;}},SAPLING_101);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570730;} else {return -1;}},SAPLING_102);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5570815;} else {return -1;}},SAPLING_103);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592320;} else {return -1;}},SAPLING_110);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592405;} else {return -1;}},SAPLING_111);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592490;} else {return -1;}},SAPLING_112);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5592575;} else {return -1;}},SAPLING_113);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614080;} else {return -1;}},SAPLING_120);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614165;} else {return -1;}},SAPLING_121);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614250;} else {return -1;}},SAPLING_122);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5614335;} else {return -1;}},SAPLING_123);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5635840;} else {return -1;}},SAPLING_130);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5635925;} else {return -1;}},SAPLING_131);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5636010;} else {return -1;}},SAPLING_132);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 5636095;} else {return -1;}},SAPLING_133);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141120;} else {return -1;}},SAPLING_200);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141205;} else {return -1;}},SAPLING_201);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141290;} else {return -1;}},SAPLING_202);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11141375;} else {return -1;}},SAPLING_203);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11162880;} else {return -1;}},SAPLING_210);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11162965;} else {return -1;}},SAPLING_211);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11163050;} else {return -1;}},SAPLING_212);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11163135;} else {return -1;}},SAPLING_213);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184640;} else {return -1;}},SAPLING_220);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184725;} else {return -1;}},SAPLING_221);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184810;} else {return -1;}},SAPLING_222);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11184895;} else {return -1;}},SAPLING_223);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206400;} else {return -1;}},SAPLING_230);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206485;} else {return -1;}},SAPLING_231);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206570;} else {return -1;}},SAPLING_232);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 11206655;} else {return -1;}},SAPLING_233);

        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711680;} else {return -1;}},SAPLING_300);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711765;} else {return -1;}},SAPLING_301);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711850;} else {return -1;}},SAPLING_302);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16711935;} else {return -1;}},SAPLING_303);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733440;} else {return -1;}},SAPLING_310);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733525;} else {return -1;}},SAPLING_311);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733610;} else {return -1;}},SAPLING_312);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16733695;} else {return -1;}},SAPLING_313);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755200;} else {return -1;}},SAPLING_320);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755285;} else {return -1;}},SAPLING_321);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755370;} else {return -1;}},SAPLING_322);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16755455;} else {return -1;}},SAPLING_323);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16776960;} else {return -1;}},SAPLING_330);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777045;} else {return -1;}},SAPLING_331);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777130;} else {return -1;}},SAPLING_332);
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return 16777215;} else {return -1;}},SAPLING_333);
    }

    public static void handleItemColors(ColorHandlerEvent.Item event) {

        //event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 0;} else {return -1;}},ITEM_SAPLING_000);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 0;} else {return -1;}},ITEM_SAPLING_000);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 85;} else {return -1;}},ITEM_SAPLING_001);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 170;} else {return -1;}},ITEM_SAPLING_002);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 255;} else {return -1;}},ITEM_SAPLING_003);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21760;} else {return -1;}},ITEM_SAPLING_010);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21845;} else {return -1;}},ITEM_SAPLING_011);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 21930;} else {return -1;}},ITEM_SAPLING_012);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 22015;} else {return -1;}},ITEM_SAPLING_013);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43520;} else {return -1;}},ITEM_SAPLING_020);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43605;} else {return -1;}},ITEM_SAPLING_021);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43690;} else {return -1;}},ITEM_SAPLING_022);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 43775;} else {return -1;}},ITEM_SAPLING_023);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65280;} else {return -1;}},ITEM_SAPLING_030);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65365;} else {return -1;}},ITEM_SAPLING_031);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65450;} else {return -1;}},ITEM_SAPLING_032);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 65535;} else {return -1;}},ITEM_SAPLING_033);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570560;} else {return -1;}},ITEM_SAPLING_100);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570645;} else {return -1;}},ITEM_SAPLING_101);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570730;} else {return -1;}},ITEM_SAPLING_102);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5570815;} else {return -1;}},ITEM_SAPLING_103);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592320;} else {return -1;}},ITEM_SAPLING_110);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592405;} else {return -1;}},ITEM_SAPLING_111);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592490;} else {return -1;}},ITEM_SAPLING_112);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5592575;} else {return -1;}},ITEM_SAPLING_113);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614080;} else {return -1;}},ITEM_SAPLING_120);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614165;} else {return -1;}},ITEM_SAPLING_121);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614250;} else {return -1;}},ITEM_SAPLING_122);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5614335;} else {return -1;}},ITEM_SAPLING_123);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5635840;} else {return -1;}},ITEM_SAPLING_130);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5635925;} else {return -1;}},ITEM_SAPLING_131);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5636010;} else {return -1;}},ITEM_SAPLING_132);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 5636095;} else {return -1;}},ITEM_SAPLING_133);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141120;} else {return -1;}},ITEM_SAPLING_200);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141205;} else {return -1;}},ITEM_SAPLING_201);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141290;} else {return -1;}},ITEM_SAPLING_202);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11141375;} else {return -1;}},ITEM_SAPLING_203);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11162880;} else {return -1;}},ITEM_SAPLING_210);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11162965;} else {return -1;}},ITEM_SAPLING_211);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11163050;} else {return -1;}},ITEM_SAPLING_212);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11163135;} else {return -1;}},ITEM_SAPLING_213);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184640;} else {return -1;}},ITEM_SAPLING_220);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184725;} else {return -1;}},ITEM_SAPLING_221);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184810;} else {return -1;}},ITEM_SAPLING_222);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11184895;} else {return -1;}},ITEM_SAPLING_223);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206400;} else {return -1;}},ITEM_SAPLING_230);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206485;} else {return -1;}},ITEM_SAPLING_231);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206570;} else {return -1;}},ITEM_SAPLING_232);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 11206655;} else {return -1;}},ITEM_SAPLING_233);

        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711680;} else {return -1;}},ITEM_SAPLING_300);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711765;} else {return -1;}},ITEM_SAPLING_301);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711850;} else {return -1;}},ITEM_SAPLING_302);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16711935;} else {return -1;}},ITEM_SAPLING_303);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733440;} else {return -1;}},ITEM_SAPLING_310);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733525;} else {return -1;}},ITEM_SAPLING_311);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733610;} else {return -1;}},ITEM_SAPLING_312);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16733695;} else {return -1;}},ITEM_SAPLING_313);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755200;} else {return -1;}},ITEM_SAPLING_320);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755285;} else {return -1;}},ITEM_SAPLING_321);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755370;} else {return -1;}},ITEM_SAPLING_322);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16755455;} else {return -1;}},ITEM_SAPLING_323);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16776960;} else {return -1;}},ITEM_SAPLING_330);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777045;} else {return -1;}},ITEM_SAPLING_331);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777130;} else {return -1;}},ITEM_SAPLING_332);
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return 16777215;} else {return -1;}},ITEM_SAPLING_333);
    }

    public static final Block SAPLING_000 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_000);
    public static final Block SAPLING_001 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_001);
    public static final Block SAPLING_002 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_002);
    public static final Block SAPLING_003 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_003);
    public static final Block SAPLING_010 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_010);
    public static final Block SAPLING_011 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_011);
    public static final Block SAPLING_012 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_012);
    public static final Block SAPLING_013 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_013);
    public static final Block SAPLING_020 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_020);
    public static final Block SAPLING_021 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_021);
    public static final Block SAPLING_022 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_022);
    public static final Block SAPLING_023 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_023);
    public static final Block SAPLING_030 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_030);
    public static final Block SAPLING_031 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_031);
    public static final Block SAPLING_032 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_032);
    public static final Block SAPLING_033 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_033);

    public static final Block SAPLING_100 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_100);
    public static final Block SAPLING_101 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_101);
    public static final Block SAPLING_102 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_102);
    public static final Block SAPLING_103 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_103);
    public static final Block SAPLING_110 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_110);
    public static final Block SAPLING_111 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_111);
    public static final Block SAPLING_112 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_112);
    public static final Block SAPLING_113 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_113);
    public static final Block SAPLING_120 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_120);
    public static final Block SAPLING_121 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_121);
    public static final Block SAPLING_122 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_122);
    public static final Block SAPLING_123 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_123);
    public static final Block SAPLING_130 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_130);
    public static final Block SAPLING_131 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_131);
    public static final Block SAPLING_132 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_132);
    public static final Block SAPLING_133 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_133);

    public static final Block SAPLING_200 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_200);
    public static final Block SAPLING_201 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_201);
    public static final Block SAPLING_202 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_202);
    public static final Block SAPLING_203 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_203);
    public static final Block SAPLING_210 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_210);
    public static final Block SAPLING_211 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_211);
    public static final Block SAPLING_212 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_212);
    public static final Block SAPLING_213 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_213);
    public static final Block SAPLING_220 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_220);
    public static final Block SAPLING_221 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_221);
    public static final Block SAPLING_222 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_222);
    public static final Block SAPLING_223 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_223);
    public static final Block SAPLING_230 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_230);
    public static final Block SAPLING_231 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_231);
    public static final Block SAPLING_232 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_232);
    public static final Block SAPLING_233 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_233);

    public static final Block SAPLING_300 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_300);
    public static final Block SAPLING_301 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_301);
    public static final Block SAPLING_302 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_302);
    public static final Block SAPLING_303 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_303);
    public static final Block SAPLING_310 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_310);
    public static final Block SAPLING_311 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_311);
    public static final Block SAPLING_312 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_312);
    public static final Block SAPLING_313 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_313);
    public static final Block SAPLING_320 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_320);
    public static final Block SAPLING_321 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_321);
    public static final Block SAPLING_322 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_322);
    public static final Block SAPLING_323 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_323);
    public static final Block SAPLING_330 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_330);
    public static final Block SAPLING_331 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_331);
    public static final Block SAPLING_332 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_332);
    public static final Block SAPLING_333 = new BlockDustSapling(null,Properties.from(Blocks.OAK_LEAVES)).setRegistryName(RESLOC_SAPLING_333);

    public static final Item ITEM_SAPLING_000 = new BlockItem(SAPLING_000, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_000);
    public static final Item ITEM_SAPLING_001 = new BlockItem(SAPLING_001, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_001);
    public static final Item ITEM_SAPLING_002 = new BlockItem(SAPLING_002, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_002);
    public static final Item ITEM_SAPLING_003 = new BlockItem(SAPLING_003, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_003);
    public static final Item ITEM_SAPLING_010 = new BlockItem(SAPLING_010, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_010);
    public static final Item ITEM_SAPLING_011 = new BlockItem(SAPLING_011, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_011);
    public static final Item ITEM_SAPLING_012 = new BlockItem(SAPLING_012, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_012);
    public static final Item ITEM_SAPLING_013 = new BlockItem(SAPLING_013, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_013);
    public static final Item ITEM_SAPLING_020 = new BlockItem(SAPLING_020, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_020);
    public static final Item ITEM_SAPLING_021 = new BlockItem(SAPLING_021, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_021);
    public static final Item ITEM_SAPLING_022 = new BlockItem(SAPLING_022, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_022);
    public static final Item ITEM_SAPLING_023 = new BlockItem(SAPLING_023, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_023);
    public static final Item ITEM_SAPLING_030 = new BlockItem(SAPLING_030, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_030);
    public static final Item ITEM_SAPLING_031 = new BlockItem(SAPLING_031, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_031);
    public static final Item ITEM_SAPLING_032 = new BlockItem(SAPLING_032, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_032);
    public static final Item ITEM_SAPLING_033 = new BlockItem(SAPLING_033, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_033);

    public static final Item ITEM_SAPLING_100 = new BlockItem(SAPLING_100, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_100);
    public static final Item ITEM_SAPLING_101 = new BlockItem(SAPLING_101, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_101);
    public static final Item ITEM_SAPLING_102 = new BlockItem(SAPLING_102, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_102);
    public static final Item ITEM_SAPLING_103 = new BlockItem(SAPLING_103, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_103);
    public static final Item ITEM_SAPLING_110 = new BlockItem(SAPLING_110, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_110);
    public static final Item ITEM_SAPLING_111 = new BlockItem(SAPLING_111, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_111);
    public static final Item ITEM_SAPLING_112 = new BlockItem(SAPLING_112, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_112);
    public static final Item ITEM_SAPLING_113 = new BlockItem(SAPLING_113, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_113);
    public static final Item ITEM_SAPLING_120 = new BlockItem(SAPLING_120, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_120);
    public static final Item ITEM_SAPLING_121 = new BlockItem(SAPLING_121, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_121);
    public static final Item ITEM_SAPLING_122 = new BlockItem(SAPLING_122, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_122);
    public static final Item ITEM_SAPLING_123 = new BlockItem(SAPLING_123, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_123);
    public static final Item ITEM_SAPLING_130 = new BlockItem(SAPLING_130, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_130);
    public static final Item ITEM_SAPLING_131 = new BlockItem(SAPLING_131, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_131);
    public static final Item ITEM_SAPLING_132 = new BlockItem(SAPLING_132, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_132);
    public static final Item ITEM_SAPLING_133 = new BlockItem(SAPLING_133, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_133);

    public static final Item ITEM_SAPLING_200 = new BlockItem(SAPLING_200, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_200);
    public static final Item ITEM_SAPLING_201 = new BlockItem(SAPLING_201, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_201);
    public static final Item ITEM_SAPLING_202 = new BlockItem(SAPLING_202, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_202);
    public static final Item ITEM_SAPLING_203 = new BlockItem(SAPLING_203, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_203);
    public static final Item ITEM_SAPLING_210 = new BlockItem(SAPLING_210, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_210);
    public static final Item ITEM_SAPLING_211 = new BlockItem(SAPLING_211, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_211);
    public static final Item ITEM_SAPLING_212 = new BlockItem(SAPLING_212, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_212);
    public static final Item ITEM_SAPLING_213 = new BlockItem(SAPLING_213, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_213);
    public static final Item ITEM_SAPLING_220 = new BlockItem(SAPLING_220, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_220);
    public static final Item ITEM_SAPLING_221 = new BlockItem(SAPLING_221, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_221);
    public static final Item ITEM_SAPLING_222 = new BlockItem(SAPLING_222, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_222);
    public static final Item ITEM_SAPLING_223 = new BlockItem(SAPLING_223, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_223);
    public static final Item ITEM_SAPLING_230 = new BlockItem(SAPLING_230, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_230);
    public static final Item ITEM_SAPLING_231 = new BlockItem(SAPLING_231, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_231);
    public static final Item ITEM_SAPLING_232 = new BlockItem(SAPLING_232, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_232);
    public static final Item ITEM_SAPLING_233 = new BlockItem(SAPLING_233, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_233);

    public static final Item ITEM_SAPLING_300 = new BlockItem(SAPLING_300, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_300);
    public static final Item ITEM_SAPLING_301 = new BlockItem(SAPLING_301, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_301);
    public static final Item ITEM_SAPLING_302 = new BlockItem(SAPLING_302, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_302);
    public static final Item ITEM_SAPLING_303 = new BlockItem(SAPLING_303, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_303);
    public static final Item ITEM_SAPLING_310 = new BlockItem(SAPLING_310, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_310);
    public static final Item ITEM_SAPLING_311 = new BlockItem(SAPLING_311, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_311);
    public static final Item ITEM_SAPLING_312 = new BlockItem(SAPLING_312, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_312);
    public static final Item ITEM_SAPLING_313 = new BlockItem(SAPLING_313, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_313);
    public static final Item ITEM_SAPLING_320 = new BlockItem(SAPLING_320, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_320);
    public static final Item ITEM_SAPLING_321 = new BlockItem(SAPLING_321, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_321);
    public static final Item ITEM_SAPLING_322 = new BlockItem(SAPLING_322, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_322);
    public static final Item ITEM_SAPLING_323 = new BlockItem(SAPLING_323, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_323);
    public static final Item ITEM_SAPLING_330 = new BlockItem(SAPLING_330, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_330);
    public static final Item ITEM_SAPLING_331 = new BlockItem(SAPLING_331, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_331);
    public static final Item ITEM_SAPLING_332 = new BlockItem(SAPLING_332, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_332);
    public static final Item ITEM_SAPLING_333 = new BlockItem(SAPLING_333, new Item.Properties().group(BLOCK_GROUP)) {}.setRegistryName(RESLOC_SAPLING_333);
}
