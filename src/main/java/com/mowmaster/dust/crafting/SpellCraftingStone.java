package com.mowmaster.dust.crafting;

import com.google.common.collect.Maps;
import com.mowmaster.dust.blocks.BlockDustStone;
import com.mowmaster.dust.item.ItemDust;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class SpellCraftingStone
{
    private static final SpellCraftingStone SPELL_CRAFTING_STONE = new SpellCraftingStone();

    private final Map<Integer, ItemStack> spellCraftingStoneList = Maps.<Integer, ItemStack>newHashMap();

    public static SpellCraftingStone instance()
    {
        return SPELL_CRAFTING_STONE;
    }

    private SpellCraftingStone()
    {
        this.addSpellCraftingStoneRecipe(0,new ItemStack(BlockDustStone.ITEM_STONE_000));
        this.addSpellCraftingStoneRecipe(51,new ItemStack(BlockDustStone.ITEM_STONE_001));
        this.addSpellCraftingStoneRecipe(102,new ItemStack(BlockDustStone.ITEM_STONE_002));
        this.addSpellCraftingStoneRecipe(153,new ItemStack(BlockDustStone.ITEM_STONE_003));
        this.addSpellCraftingStoneRecipe(204,new ItemStack(BlockDustStone.ITEM_STONE_004));
        this.addSpellCraftingStoneRecipe(255,new ItemStack(BlockDustStone.ITEM_STONE_005));
        this.addSpellCraftingStoneRecipe(13056,new ItemStack(BlockDustStone.ITEM_STONE_010));
        this.addSpellCraftingStoneRecipe(13107,new ItemStack(BlockDustStone.ITEM_STONE_011));
        this.addSpellCraftingStoneRecipe(13158,new ItemStack(BlockDustStone.ITEM_STONE_012));
        this.addSpellCraftingStoneRecipe(13209,new ItemStack(BlockDustStone.ITEM_STONE_013));
        this.addSpellCraftingStoneRecipe(13260,new ItemStack(BlockDustStone.ITEM_STONE_014));
        this.addSpellCraftingStoneRecipe(13311,new ItemStack(BlockDustStone.ITEM_STONE_015));
        this.addSpellCraftingStoneRecipe(26112,new ItemStack(BlockDustStone.ITEM_STONE_020));
        this.addSpellCraftingStoneRecipe(26163,new ItemStack(BlockDustStone.ITEM_STONE_021));
        this.addSpellCraftingStoneRecipe(26214,new ItemStack(BlockDustStone.ITEM_STONE_022));
        this.addSpellCraftingStoneRecipe(26265,new ItemStack(BlockDustStone.ITEM_STONE_023));
        this.addSpellCraftingStoneRecipe(26316,new ItemStack(BlockDustStone.ITEM_STONE_024));
        this.addSpellCraftingStoneRecipe(26367,new ItemStack(BlockDustStone.ITEM_STONE_025));
        this.addSpellCraftingStoneRecipe(39168,new ItemStack(BlockDustStone.ITEM_STONE_030));
        this.addSpellCraftingStoneRecipe(39219,new ItemStack(BlockDustStone.ITEM_STONE_031));
        this.addSpellCraftingStoneRecipe(39270,new ItemStack(BlockDustStone.ITEM_STONE_032));
        this.addSpellCraftingStoneRecipe(39321,new ItemStack(BlockDustStone.ITEM_STONE_033));
        this.addSpellCraftingStoneRecipe(39372,new ItemStack(BlockDustStone.ITEM_STONE_034));
        this.addSpellCraftingStoneRecipe(39423,new ItemStack(BlockDustStone.ITEM_STONE_035));
        this.addSpellCraftingStoneRecipe(52224,new ItemStack(BlockDustStone.ITEM_STONE_040));
        this.addSpellCraftingStoneRecipe(52275,new ItemStack(BlockDustStone.ITEM_STONE_041));
        this.addSpellCraftingStoneRecipe(52326,new ItemStack(BlockDustStone.ITEM_STONE_042));
        this.addSpellCraftingStoneRecipe(52377,new ItemStack(BlockDustStone.ITEM_STONE_043));
        this.addSpellCraftingStoneRecipe(52428,new ItemStack(BlockDustStone.ITEM_STONE_044));
        this.addSpellCraftingStoneRecipe(52479,new ItemStack(BlockDustStone.ITEM_STONE_045));
        this.addSpellCraftingStoneRecipe(65280,new ItemStack(BlockDustStone.ITEM_STONE_050));
        this.addSpellCraftingStoneRecipe(65331,new ItemStack(BlockDustStone.ITEM_STONE_051));
        this.addSpellCraftingStoneRecipe(65382,new ItemStack(BlockDustStone.ITEM_STONE_052));
        this.addSpellCraftingStoneRecipe(65433,new ItemStack(BlockDustStone.ITEM_STONE_053));
        this.addSpellCraftingStoneRecipe(65484,new ItemStack(BlockDustStone.ITEM_STONE_054));
        this.addSpellCraftingStoneRecipe(65535,new ItemStack(BlockDustStone.ITEM_STONE_055));

        this.addSpellCraftingStoneRecipe(3342336,new ItemStack(BlockDustStone.ITEM_STONE_100));
        this.addSpellCraftingStoneRecipe(3342387,new ItemStack(BlockDustStone.ITEM_STONE_101));
        this.addSpellCraftingStoneRecipe(3342438,new ItemStack(BlockDustStone.ITEM_STONE_102));
        this.addSpellCraftingStoneRecipe(3342489,new ItemStack(BlockDustStone.ITEM_STONE_103));
        this.addSpellCraftingStoneRecipe(3342540,new ItemStack(BlockDustStone.ITEM_STONE_104));
        this.addSpellCraftingStoneRecipe(3342591,new ItemStack(BlockDustStone.ITEM_STONE_105));
        this.addSpellCraftingStoneRecipe(3355392,new ItemStack(BlockDustStone.ITEM_STONE_110));
        this.addSpellCraftingStoneRecipe(3355443,new ItemStack(BlockDustStone.ITEM_STONE_111));
        this.addSpellCraftingStoneRecipe(3355494,new ItemStack(BlockDustStone.ITEM_STONE_112));
        this.addSpellCraftingStoneRecipe(3355545,new ItemStack(BlockDustStone.ITEM_STONE_113));
        this.addSpellCraftingStoneRecipe(3355596,new ItemStack(BlockDustStone.ITEM_STONE_114));
        this.addSpellCraftingStoneRecipe(3355647,new ItemStack(BlockDustStone.ITEM_STONE_115));
        this.addSpellCraftingStoneRecipe(3368448,new ItemStack(BlockDustStone.ITEM_STONE_120));
        this.addSpellCraftingStoneRecipe(3368499,new ItemStack(BlockDustStone.ITEM_STONE_121));
        this.addSpellCraftingStoneRecipe(3368550,new ItemStack(BlockDustStone.ITEM_STONE_122));
        this.addSpellCraftingStoneRecipe(3368601,new ItemStack(BlockDustStone.ITEM_STONE_123));
        this.addSpellCraftingStoneRecipe(3368652,new ItemStack(BlockDustStone.ITEM_STONE_124));
        this.addSpellCraftingStoneRecipe(3368703,new ItemStack(BlockDustStone.ITEM_STONE_125));
        this.addSpellCraftingStoneRecipe(3381504,new ItemStack(BlockDustStone.ITEM_STONE_130));
        this.addSpellCraftingStoneRecipe(3381555,new ItemStack(BlockDustStone.ITEM_STONE_131));
        this.addSpellCraftingStoneRecipe(3381606,new ItemStack(BlockDustStone.ITEM_STONE_132));
        this.addSpellCraftingStoneRecipe(3381657,new ItemStack(BlockDustStone.ITEM_STONE_133));
        this.addSpellCraftingStoneRecipe(3381708,new ItemStack(BlockDustStone.ITEM_STONE_134));
        this.addSpellCraftingStoneRecipe(3381759,new ItemStack(BlockDustStone.ITEM_STONE_135));
        this.addSpellCraftingStoneRecipe(3394560,new ItemStack(BlockDustStone.ITEM_STONE_140));
        this.addSpellCraftingStoneRecipe(3394611,new ItemStack(BlockDustStone.ITEM_STONE_141));
        this.addSpellCraftingStoneRecipe(3394662,new ItemStack(BlockDustStone.ITEM_STONE_142));
        this.addSpellCraftingStoneRecipe(3394713,new ItemStack(BlockDustStone.ITEM_STONE_143));
        this.addSpellCraftingStoneRecipe(3394764,new ItemStack(BlockDustStone.ITEM_STONE_144));
        this.addSpellCraftingStoneRecipe(3394815,new ItemStack(BlockDustStone.ITEM_STONE_145));
        this.addSpellCraftingStoneRecipe(3407616,new ItemStack(BlockDustStone.ITEM_STONE_150));
        this.addSpellCraftingStoneRecipe(3407667,new ItemStack(BlockDustStone.ITEM_STONE_151));
        this.addSpellCraftingStoneRecipe(3407718,new ItemStack(BlockDustStone.ITEM_STONE_152));
        this.addSpellCraftingStoneRecipe(3407769,new ItemStack(BlockDustStone.ITEM_STONE_153));
        this.addSpellCraftingStoneRecipe(3407820,new ItemStack(BlockDustStone.ITEM_STONE_154));
        this.addSpellCraftingStoneRecipe(3407871,new ItemStack(BlockDustStone.ITEM_STONE_155));

        this.addSpellCraftingStoneRecipe(6684672,new ItemStack(BlockDustStone.ITEM_STONE_200));
        this.addSpellCraftingStoneRecipe(6684723,new ItemStack(BlockDustStone.ITEM_STONE_201));
        this.addSpellCraftingStoneRecipe(6684774,new ItemStack(BlockDustStone.ITEM_STONE_202));
        this.addSpellCraftingStoneRecipe(6684825,new ItemStack(BlockDustStone.ITEM_STONE_203));
        this.addSpellCraftingStoneRecipe(6684876,new ItemStack(BlockDustStone.ITEM_STONE_204));
        this.addSpellCraftingStoneRecipe(6684927,new ItemStack(BlockDustStone.ITEM_STONE_205));
        this.addSpellCraftingStoneRecipe(6697728,new ItemStack(BlockDustStone.ITEM_STONE_210));
        this.addSpellCraftingStoneRecipe(6697779,new ItemStack(BlockDustStone.ITEM_STONE_211));
        this.addSpellCraftingStoneRecipe(6697830,new ItemStack(BlockDustStone.ITEM_STONE_212));
        this.addSpellCraftingStoneRecipe(6697881,new ItemStack(BlockDustStone.ITEM_STONE_213));
        this.addSpellCraftingStoneRecipe(6697932,new ItemStack(BlockDustStone.ITEM_STONE_214));
        this.addSpellCraftingStoneRecipe(6697983,new ItemStack(BlockDustStone.ITEM_STONE_215));
        this.addSpellCraftingStoneRecipe(6710784,new ItemStack(BlockDustStone.ITEM_STONE_220));
        this.addSpellCraftingStoneRecipe(6710835,new ItemStack(BlockDustStone.ITEM_STONE_221));
        this.addSpellCraftingStoneRecipe(6710886,new ItemStack(BlockDustStone.ITEM_STONE_222));
        this.addSpellCraftingStoneRecipe(6710937,new ItemStack(BlockDustStone.ITEM_STONE_223));
        this.addSpellCraftingStoneRecipe(6710988,new ItemStack(BlockDustStone.ITEM_STONE_224));
        this.addSpellCraftingStoneRecipe(6711039,new ItemStack(BlockDustStone.ITEM_STONE_225));
        this.addSpellCraftingStoneRecipe(6723840,new ItemStack(BlockDustStone.ITEM_STONE_230));
        this.addSpellCraftingStoneRecipe(6723891,new ItemStack(BlockDustStone.ITEM_STONE_231));
        this.addSpellCraftingStoneRecipe(6723942,new ItemStack(BlockDustStone.ITEM_STONE_232));
        this.addSpellCraftingStoneRecipe(6723993,new ItemStack(BlockDustStone.ITEM_STONE_233));
        this.addSpellCraftingStoneRecipe(6724044,new ItemStack(BlockDustStone.ITEM_STONE_234));
        this.addSpellCraftingStoneRecipe(6724095,new ItemStack(BlockDustStone.ITEM_STONE_235));
        this.addSpellCraftingStoneRecipe(6736896,new ItemStack(BlockDustStone.ITEM_STONE_240));
        this.addSpellCraftingStoneRecipe(6736947,new ItemStack(BlockDustStone.ITEM_STONE_241));
        this.addSpellCraftingStoneRecipe(6736998,new ItemStack(BlockDustStone.ITEM_STONE_242));
        this.addSpellCraftingStoneRecipe(6737049,new ItemStack(BlockDustStone.ITEM_STONE_243));
        this.addSpellCraftingStoneRecipe(6737100,new ItemStack(BlockDustStone.ITEM_STONE_244));
        this.addSpellCraftingStoneRecipe(6737151,new ItemStack(BlockDustStone.ITEM_STONE_245));
        this.addSpellCraftingStoneRecipe(6749952,new ItemStack(BlockDustStone.ITEM_STONE_250));
        this.addSpellCraftingStoneRecipe(6750003,new ItemStack(BlockDustStone.ITEM_STONE_251));
        this.addSpellCraftingStoneRecipe(6750054,new ItemStack(BlockDustStone.ITEM_STONE_252));
        this.addSpellCraftingStoneRecipe(6750105,new ItemStack(BlockDustStone.ITEM_STONE_253));
        this.addSpellCraftingStoneRecipe(6750156,new ItemStack(BlockDustStone.ITEM_STONE_254));
        this.addSpellCraftingStoneRecipe(6750207,new ItemStack(BlockDustStone.ITEM_STONE_255));

        this.addSpellCraftingStoneRecipe(10027008,new ItemStack(BlockDustStone.ITEM_STONE_300));
        this.addSpellCraftingStoneRecipe(10027059,new ItemStack(BlockDustStone.ITEM_STONE_301));
        this.addSpellCraftingStoneRecipe(10027110,new ItemStack(BlockDustStone.ITEM_STONE_302));
        this.addSpellCraftingStoneRecipe(10027161,new ItemStack(BlockDustStone.ITEM_STONE_303));
        this.addSpellCraftingStoneRecipe(10027212,new ItemStack(BlockDustStone.ITEM_STONE_304));
        this.addSpellCraftingStoneRecipe(10027263,new ItemStack(BlockDustStone.ITEM_STONE_305));
        this.addSpellCraftingStoneRecipe(10040064,new ItemStack(BlockDustStone.ITEM_STONE_310));
        this.addSpellCraftingStoneRecipe(10040115,new ItemStack(BlockDustStone.ITEM_STONE_311));
        this.addSpellCraftingStoneRecipe(10040166,new ItemStack(BlockDustStone.ITEM_STONE_312));
        this.addSpellCraftingStoneRecipe(10040217,new ItemStack(BlockDustStone.ITEM_STONE_313));
        this.addSpellCraftingStoneRecipe(10040268,new ItemStack(BlockDustStone.ITEM_STONE_314));
        this.addSpellCraftingStoneRecipe(10040319,new ItemStack(BlockDustStone.ITEM_STONE_315));
        this.addSpellCraftingStoneRecipe(10053120,new ItemStack(BlockDustStone.ITEM_STONE_320));
        this.addSpellCraftingStoneRecipe(10053171,new ItemStack(BlockDustStone.ITEM_STONE_321));
        this.addSpellCraftingStoneRecipe(10053222,new ItemStack(BlockDustStone.ITEM_STONE_322));
        this.addSpellCraftingStoneRecipe(10053273,new ItemStack(BlockDustStone.ITEM_STONE_323));
        this.addSpellCraftingStoneRecipe(10053324,new ItemStack(BlockDustStone.ITEM_STONE_324));
        this.addSpellCraftingStoneRecipe(10053375,new ItemStack(BlockDustStone.ITEM_STONE_325));
        this.addSpellCraftingStoneRecipe(10066176,new ItemStack(BlockDustStone.ITEM_STONE_330));
        this.addSpellCraftingStoneRecipe(10066227,new ItemStack(BlockDustStone.ITEM_STONE_331));
        this.addSpellCraftingStoneRecipe(10066278,new ItemStack(BlockDustStone.ITEM_STONE_332));
        this.addSpellCraftingStoneRecipe(10066329,new ItemStack(BlockDustStone.ITEM_STONE_333));
        this.addSpellCraftingStoneRecipe(10066380,new ItemStack(BlockDustStone.ITEM_STONE_334));
        this.addSpellCraftingStoneRecipe(10066431,new ItemStack(BlockDustStone.ITEM_STONE_335));
        this.addSpellCraftingStoneRecipe(10079232,new ItemStack(BlockDustStone.ITEM_STONE_340));
        this.addSpellCraftingStoneRecipe(10079283,new ItemStack(BlockDustStone.ITEM_STONE_341));
        this.addSpellCraftingStoneRecipe(10079334,new ItemStack(BlockDustStone.ITEM_STONE_342));
        this.addSpellCraftingStoneRecipe(10079385,new ItemStack(BlockDustStone.ITEM_STONE_343));
        this.addSpellCraftingStoneRecipe(10079436,new ItemStack(BlockDustStone.ITEM_STONE_344));
        this.addSpellCraftingStoneRecipe(10079487,new ItemStack(BlockDustStone.ITEM_STONE_345));
        this.addSpellCraftingStoneRecipe(10092288,new ItemStack(BlockDustStone.ITEM_STONE_350));
        this.addSpellCraftingStoneRecipe(10092339,new ItemStack(BlockDustStone.ITEM_STONE_351));
        this.addSpellCraftingStoneRecipe(10092390,new ItemStack(BlockDustStone.ITEM_STONE_352));
        this.addSpellCraftingStoneRecipe(10092441,new ItemStack(BlockDustStone.ITEM_STONE_353));
        this.addSpellCraftingStoneRecipe(10092492,new ItemStack(BlockDustStone.ITEM_STONE_354));
        this.addSpellCraftingStoneRecipe(10092543,new ItemStack(BlockDustStone.ITEM_STONE_355));

        this.addSpellCraftingStoneRecipe(13369344,new ItemStack(BlockDustStone.ITEM_STONE_400));
        this.addSpellCraftingStoneRecipe(13369395,new ItemStack(BlockDustStone.ITEM_STONE_401));
        this.addSpellCraftingStoneRecipe(13369446,new ItemStack(BlockDustStone.ITEM_STONE_402));
        this.addSpellCraftingStoneRecipe(13369497,new ItemStack(BlockDustStone.ITEM_STONE_403));
        this.addSpellCraftingStoneRecipe(13369548,new ItemStack(BlockDustStone.ITEM_STONE_404));
        this.addSpellCraftingStoneRecipe(13369599,new ItemStack(BlockDustStone.ITEM_STONE_405));
        this.addSpellCraftingStoneRecipe(13382400,new ItemStack(BlockDustStone.ITEM_STONE_410));
        this.addSpellCraftingStoneRecipe(13382451,new ItemStack(BlockDustStone.ITEM_STONE_411));
        this.addSpellCraftingStoneRecipe(13382502,new ItemStack(BlockDustStone.ITEM_STONE_412));
        this.addSpellCraftingStoneRecipe(13382553,new ItemStack(BlockDustStone.ITEM_STONE_413));
        this.addSpellCraftingStoneRecipe(13382604,new ItemStack(BlockDustStone.ITEM_STONE_414));
        this.addSpellCraftingStoneRecipe(13382655,new ItemStack(BlockDustStone.ITEM_STONE_415));
        this.addSpellCraftingStoneRecipe(13395456,new ItemStack(BlockDustStone.ITEM_STONE_420));
        this.addSpellCraftingStoneRecipe(13395507,new ItemStack(BlockDustStone.ITEM_STONE_421));
        this.addSpellCraftingStoneRecipe(13395558,new ItemStack(BlockDustStone.ITEM_STONE_422));
        this.addSpellCraftingStoneRecipe(13395609,new ItemStack(BlockDustStone.ITEM_STONE_423));
        this.addSpellCraftingStoneRecipe(13395660,new ItemStack(BlockDustStone.ITEM_STONE_424));
        this.addSpellCraftingStoneRecipe(13395711,new ItemStack(BlockDustStone.ITEM_STONE_425));
        this.addSpellCraftingStoneRecipe(13408512,new ItemStack(BlockDustStone.ITEM_STONE_430));
        this.addSpellCraftingStoneRecipe(13408563,new ItemStack(BlockDustStone.ITEM_STONE_431));
        this.addSpellCraftingStoneRecipe(13408614,new ItemStack(BlockDustStone.ITEM_STONE_432));
        this.addSpellCraftingStoneRecipe(13408665,new ItemStack(BlockDustStone.ITEM_STONE_433));
        this.addSpellCraftingStoneRecipe(13408716,new ItemStack(BlockDustStone.ITEM_STONE_434));
        this.addSpellCraftingStoneRecipe(13408767,new ItemStack(BlockDustStone.ITEM_STONE_435));
        this.addSpellCraftingStoneRecipe(13421568,new ItemStack(BlockDustStone.ITEM_STONE_440));
        this.addSpellCraftingStoneRecipe(13421619,new ItemStack(BlockDustStone.ITEM_STONE_441));
        this.addSpellCraftingStoneRecipe(13421670,new ItemStack(BlockDustStone.ITEM_STONE_442));
        this.addSpellCraftingStoneRecipe(13421721,new ItemStack(BlockDustStone.ITEM_STONE_443));
        this.addSpellCraftingStoneRecipe(13421772,new ItemStack(BlockDustStone.ITEM_STONE_444));
        this.addSpellCraftingStoneRecipe(13421823,new ItemStack(BlockDustStone.ITEM_STONE_445));
        this.addSpellCraftingStoneRecipe(13434624,new ItemStack(BlockDustStone.ITEM_STONE_450));
        this.addSpellCraftingStoneRecipe(13434675,new ItemStack(BlockDustStone.ITEM_STONE_451));
        this.addSpellCraftingStoneRecipe(13434726,new ItemStack(BlockDustStone.ITEM_STONE_452));
        this.addSpellCraftingStoneRecipe(13434777,new ItemStack(BlockDustStone.ITEM_STONE_453));
        this.addSpellCraftingStoneRecipe(13434828,new ItemStack(BlockDustStone.ITEM_STONE_454));
        this.addSpellCraftingStoneRecipe(13434879,new ItemStack(BlockDustStone.ITEM_STONE_455));

        this.addSpellCraftingStoneRecipe(16711680,new ItemStack(BlockDustStone.ITEM_STONE_500));
        this.addSpellCraftingStoneRecipe(16711731,new ItemStack(BlockDustStone.ITEM_STONE_501));
        this.addSpellCraftingStoneRecipe(16711782,new ItemStack(BlockDustStone.ITEM_STONE_502));
        this.addSpellCraftingStoneRecipe(16711833,new ItemStack(BlockDustStone.ITEM_STONE_503));
        this.addSpellCraftingStoneRecipe(16711884,new ItemStack(BlockDustStone.ITEM_STONE_504));
        this.addSpellCraftingStoneRecipe(16711935,new ItemStack(BlockDustStone.ITEM_STONE_505));
        this.addSpellCraftingStoneRecipe(16724736,new ItemStack(BlockDustStone.ITEM_STONE_510));
        this.addSpellCraftingStoneRecipe(16724787,new ItemStack(BlockDustStone.ITEM_STONE_511));
        this.addSpellCraftingStoneRecipe(16724838,new ItemStack(BlockDustStone.ITEM_STONE_512));
        this.addSpellCraftingStoneRecipe(16724889,new ItemStack(BlockDustStone.ITEM_STONE_513));
        this.addSpellCraftingStoneRecipe(16724940,new ItemStack(BlockDustStone.ITEM_STONE_514));
        this.addSpellCraftingStoneRecipe(16724991,new ItemStack(BlockDustStone.ITEM_STONE_515));
        this.addSpellCraftingStoneRecipe(16737792,new ItemStack(BlockDustStone.ITEM_STONE_520));
        this.addSpellCraftingStoneRecipe(16737843,new ItemStack(BlockDustStone.ITEM_STONE_521));
        this.addSpellCraftingStoneRecipe(16737894,new ItemStack(BlockDustStone.ITEM_STONE_522));
        this.addSpellCraftingStoneRecipe(16737945,new ItemStack(BlockDustStone.ITEM_STONE_523));
        this.addSpellCraftingStoneRecipe(16737996,new ItemStack(BlockDustStone.ITEM_STONE_524));
        this.addSpellCraftingStoneRecipe(16738047,new ItemStack(BlockDustStone.ITEM_STONE_525));
        this.addSpellCraftingStoneRecipe(16750848,new ItemStack(BlockDustStone.ITEM_STONE_530));
        this.addSpellCraftingStoneRecipe(16750899,new ItemStack(BlockDustStone.ITEM_STONE_531));
        this.addSpellCraftingStoneRecipe(16750950,new ItemStack(BlockDustStone.ITEM_STONE_532));
        this.addSpellCraftingStoneRecipe(16751001,new ItemStack(BlockDustStone.ITEM_STONE_533));
        this.addSpellCraftingStoneRecipe(16751052,new ItemStack(BlockDustStone.ITEM_STONE_534));
        this.addSpellCraftingStoneRecipe(16751103,new ItemStack(BlockDustStone.ITEM_STONE_535));
        this.addSpellCraftingStoneRecipe(16763904,new ItemStack(BlockDustStone.ITEM_STONE_540));
        this.addSpellCraftingStoneRecipe(16763955,new ItemStack(BlockDustStone.ITEM_STONE_541));
        this.addSpellCraftingStoneRecipe(16764006,new ItemStack(BlockDustStone.ITEM_STONE_542));
        this.addSpellCraftingStoneRecipe(16764057,new ItemStack(BlockDustStone.ITEM_STONE_543));
        this.addSpellCraftingStoneRecipe(16764108,new ItemStack(BlockDustStone.ITEM_STONE_544));
        this.addSpellCraftingStoneRecipe(16764159,new ItemStack(BlockDustStone.ITEM_STONE_545));
        this.addSpellCraftingStoneRecipe(16776960,new ItemStack(BlockDustStone.ITEM_STONE_550));
        this.addSpellCraftingStoneRecipe(16777011,new ItemStack(BlockDustStone.ITEM_STONE_551));
        this.addSpellCraftingStoneRecipe(16777062,new ItemStack(BlockDustStone.ITEM_STONE_552));
        this.addSpellCraftingStoneRecipe(16777113,new ItemStack(BlockDustStone.ITEM_STONE_553));
        this.addSpellCraftingStoneRecipe(16777164,new ItemStack(BlockDustStone.ITEM_STONE_554));
        this.addSpellCraftingStoneRecipe(16777215,new ItemStack(BlockDustStone.ITEM_STONE_555));


    }

    /**
     * Adds a smelting recipe using an Item as the input item.
     */
    public void addColorRecipe(int colorIn, ItemStack stack)
    {
        this.addSpellCraftingStoneRecipe(colorIn, stack);
    }

    /**
     * Adds a smelting recipe using an ItemStack as the input for the recipe.
     */
    public void addSpellCraftingStoneRecipe(int colorIn, ItemStack stack)
    {
        if (getResult(colorIn) != ItemStack.EMPTY) { return;}
        this.spellCraftingStoneList.put(colorIn, stack);
    }

    /**
     * Returns the smelting result of an item.
     */
    public ItemStack getResult(int colorIn)
    {
        for (Map.Entry<Integer, ItemStack> entry : this.spellCraftingStoneList.entrySet())
        {
            if (colorIn == entry.getKey())
            {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }

    public Map<Integer, ItemStack> getCrushingList()
    {
        return this.spellCraftingStoneList;
    }

}
