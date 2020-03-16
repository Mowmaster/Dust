package com.mowmaster.dust.crafting;

import com.google.common.collect.Maps;
import com.mowmaster.dust.blocks.BlockDustStone;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;

import java.util.Map;

public class SpellCraftingEffect
{
    private static final SpellCraftingEffect SPELL_CRAFTING_EFFECT = new SpellCraftingEffect();

    private final Map<Integer, Effect> spellCraftingEffectList = Maps.<Integer, Effect>newHashMap();

    public static SpellCraftingEffect instance()
    {
        return SPELL_CRAFTING_EFFECT;
    }

    private SpellCraftingEffect()
    {
        this.addSpellCraftingEffectRecipe(0,Effects.WITHER);//000
        //this.addSpellCraftingEffectRecipe(51,new ItemStack(BlockDustStone.ITEM_STONE_001));
        //this.addSpellCraftingEffectRecipe(102,new ItemStack(BlockDustStone.ITEM_STONE_002));
        //this.addSpellCraftingEffectRecipe(153,new ItemStack(BlockDustStone.ITEM_STONE_003));
        //this.addSpellCraftingEffectRecipe(204,new ItemStack(BlockDustStone.ITEM_STONE_004));
        this.addSpellCraftingEffectRecipe(255,Effects.WATER_BREATHING);//005
        //this.addSpellCraftingEffectRecipe(13056,new ItemStack(BlockDustStone.ITEM_STONE_010));
        //this.addSpellCraftingEffectRecipe(13107,new ItemStack(BlockDustStone.ITEM_STONE_011));
        //this.addSpellCraftingEffectRecipe(13158,new ItemStack(BlockDustStone.ITEM_STONE_012));
        //this.addSpellCraftingEffectRecipe(13209,new ItemStack(BlockDustStone.ITEM_STONE_013));
        //this.addSpellCraftingEffectRecipe(13260,new ItemStack(BlockDustStone.ITEM_STONE_014));
        //this.addSpellCraftingEffectRecipe(13311,new ItemStack(BlockDustStone.ITEM_STONE_015));
        //this.addSpellCraftingEffectRecipe(26112,new ItemStack(BlockDustStone.ITEM_STONE_020));
        //this.addSpellCraftingEffectRecipe(26163,new ItemStack(BlockDustStone.ITEM_STONE_021));
        //this.addSpellCraftingEffectRecipe(26214,new ItemStack(BlockDustStone.ITEM_STONE_022));
        //this.addSpellCraftingEffectRecipe(26265,new ItemStack(BlockDustStone.ITEM_STONE_023));
        //this.addSpellCraftingEffectRecipe(26316,new ItemStack(BlockDustStone.ITEM_STONE_024));
        //this.addSpellCraftingEffectRecipe(26367,new ItemStack(BlockDustStone.ITEM_STONE_025));
        //this.addSpellCraftingEffectRecipe(39168,new ItemStack(BlockDustStone.ITEM_STONE_030));
        //this.addSpellCraftingEffectRecipe(39219,new ItemStack(BlockDustStone.ITEM_STONE_031));
        //this.addSpellCraftingEffectRecipe(39270,new ItemStack(BlockDustStone.ITEM_STONE_032));
        //this.addSpellCraftingEffectRecipe(39321,new ItemStack(BlockDustStone.ITEM_STONE_033));
        //this.addSpellCraftingEffectRecipe(39372,new ItemStack(BlockDustStone.ITEM_STONE_034));
        //this.addSpellCraftingEffectRecipe(39423,new ItemStack(BlockDustStone.ITEM_STONE_035));
        //this.addSpellCraftingEffectRecipe(52224,new ItemStack(BlockDustStone.ITEM_STONE_040));
        //this.addSpellCraftingEffectRecipe(52275,new ItemStack(BlockDustStone.ITEM_STONE_041));
        //this.addSpellCraftingEffectRecipe(52326,new ItemStack(BlockDustStone.ITEM_STONE_042));
        //this.addSpellCraftingEffectRecipe(52377,new ItemStack(BlockDustStone.ITEM_STONE_043));
        //this.addSpellCraftingEffectRecipe(52428,new ItemStack(BlockDustStone.ITEM_STONE_044));
        //this.addSpellCraftingEffectRecipe(52479,new ItemStack(BlockDustStone.ITEM_STONE_045));
        this.addSpellCraftingEffectRecipe(65280,Effects.REGENERATION);//050
        //this.addSpellCraftingEffectRecipe(65331,new ItemStack(BlockDustStone.ITEM_STONE_051));
        //this.addSpellCraftingEffectRecipe(65382,new ItemStack(BlockDustStone.ITEM_STONE_052));
        //this.addSpellCraftingEffectRecipe(65433,new ItemStack(BlockDustStone.ITEM_STONE_053));
        //this.addSpellCraftingEffectRecipe(65484,new ItemStack(BlockDustStone.ITEM_STONE_054));
        //this.addSpellCraftingEffectRecipe(65535,new ItemStack(BlockDustStone.ITEM_STONE_055));

        //this.addSpellCraftingEffectRecipe(3342336,new ItemStack(BlockDustStone.ITEM_STONE_100));
        //this.addSpellCraftingEffectRecipe(3342387,new ItemStack(BlockDustStone.ITEM_STONE_101));
        //this.addSpellCraftingEffectRecipe(3342438,new ItemStack(BlockDustStone.ITEM_STONE_102));
        //this.addSpellCraftingEffectRecipe(3342489,new ItemStack(BlockDustStone.ITEM_STONE_103));
        //this.addSpellCraftingEffectRecipe(3342540,new ItemStack(BlockDustStone.ITEM_STONE_104));
        //this.addSpellCraftingEffectRecipe(3342591,new ItemStack(BlockDustStone.ITEM_STONE_105));
        //this.addSpellCraftingEffectRecipe(3355392,new ItemStack(BlockDustStone.ITEM_STONE_110));
        //this.addSpellCraftingEffectRecipe(3355443,new ItemStack(BlockDustStone.ITEM_STONE_111));
        //this.addSpellCraftingEffectRecipe(3355494,new ItemStack(BlockDustStone.ITEM_STONE_112));
        //this.addSpellCraftingEffectRecipe(3355545,new ItemStack(BlockDustStone.ITEM_STONE_113));
        //this.addSpellCraftingEffectRecipe(3355596,new ItemStack(BlockDustStone.ITEM_STONE_114));
        //this.addSpellCraftingEffectRecipe(3355647,new ItemStack(BlockDustStone.ITEM_STONE_115));
        //this.addSpellCraftingEffectRecipe(3368448,new ItemStack(BlockDustStone.ITEM_STONE_120));
        //this.addSpellCraftingEffectRecipe(3368499,new ItemStack(BlockDustStone.ITEM_STONE_121));
        //this.addSpellCraftingEffectRecipe(3368550,new ItemStack(BlockDustStone.ITEM_STONE_122));
        //this.addSpellCraftingEffectRecipe(3368601,new ItemStack(BlockDustStone.ITEM_STONE_123));
        //this.addSpellCraftingEffectRecipe(3368652,new ItemStack(BlockDustStone.ITEM_STONE_124));
        //this.addSpellCraftingEffectRecipe(3368703,new ItemStack(BlockDustStone.ITEM_STONE_125));
        //this.addSpellCraftingEffectRecipe(3381504,new ItemStack(BlockDustStone.ITEM_STONE_130));
        //this.addSpellCraftingEffectRecipe(3381555,new ItemStack(BlockDustStone.ITEM_STONE_131));
        //this.addSpellCraftingEffectRecipe(3381606,new ItemStack(BlockDustStone.ITEM_STONE_132));
        //this.addSpellCraftingEffectRecipe(3381657,new ItemStack(BlockDustStone.ITEM_STONE_133));
        //this.addSpellCraftingEffectRecipe(3381708,new ItemStack(BlockDustStone.ITEM_STONE_134));
        //this.addSpellCraftingEffectRecipe(3381759,new ItemStack(BlockDustStone.ITEM_STONE_135));
        //this.addSpellCraftingEffectRecipe(3394560,new ItemStack(BlockDustStone.ITEM_STONE_140));
        //this.addSpellCraftingEffectRecipe(3394611,new ItemStack(BlockDustStone.ITEM_STONE_141));
        //this.addSpellCraftingEffectRecipe(3394662,new ItemStack(BlockDustStone.ITEM_STONE_142));
        //this.addSpellCraftingEffectRecipe(3394713,new ItemStack(BlockDustStone.ITEM_STONE_143));
        //this.addSpellCraftingEffectRecipe(3394764,new ItemStack(BlockDustStone.ITEM_STONE_144));
        //this.addSpellCraftingEffectRecipe(3394815,new ItemStack(BlockDustStone.ITEM_STONE_145));
        //this.addSpellCraftingEffectRecipe(3407616,new ItemStack(BlockDustStone.ITEM_STONE_150));
        //this.addSpellCraftingEffectRecipe(3407667,new ItemStack(BlockDustStone.ITEM_STONE_151));
        //this.addSpellCraftingEffectRecipe(3407718,new ItemStack(BlockDustStone.ITEM_STONE_152));
        //this.addSpellCraftingEffectRecipe(3407769,new ItemStack(BlockDustStone.ITEM_STONE_153));
        //this.addSpellCraftingEffectRecipe(3407820,new ItemStack(BlockDustStone.ITEM_STONE_154));
        //this.addSpellCraftingEffectRecipe(3407871,new ItemStack(BlockDustStone.ITEM_STONE_155));

        //this.addSpellCraftingEffectRecipe(6684672,new ItemStack(BlockDustStone.ITEM_STONE_200));
        //this.addSpellCraftingEffectRecipe(6684723,new ItemStack(BlockDustStone.ITEM_STONE_201));
        //this.addSpellCraftingEffectRecipe(6684774,new ItemStack(BlockDustStone.ITEM_STONE_202));
        //this.addSpellCraftingEffectRecipe(6684825,new ItemStack(BlockDustStone.ITEM_STONE_203));
        //this.addSpellCraftingEffectRecipe(6684876,new ItemStack(BlockDustStone.ITEM_STONE_204));
        //this.addSpellCraftingEffectRecipe(6684927,new ItemStack(BlockDustStone.ITEM_STONE_205));
        //this.addSpellCraftingEffectRecipe(6697728,new ItemStack(BlockDustStone.ITEM_STONE_210));
        //this.addSpellCraftingEffectRecipe(6697779,new ItemStack(BlockDustStone.ITEM_STONE_211));
        //this.addSpellCraftingEffectRecipe(6697830,new ItemStack(BlockDustStone.ITEM_STONE_212));
        //this.addSpellCraftingEffectRecipe(6697881,new ItemStack(BlockDustStone.ITEM_STONE_213));
        //this.addSpellCraftingEffectRecipe(6697932,new ItemStack(BlockDustStone.ITEM_STONE_214));
        //this.addSpellCraftingEffectRecipe(6697983,new ItemStack(BlockDustStone.ITEM_STONE_215));
        //this.addSpellCraftingEffectRecipe(6710784,new ItemStack(BlockDustStone.ITEM_STONE_220));
        //this.addSpellCraftingEffectRecipe(6710835,new ItemStack(BlockDustStone.ITEM_STONE_221));
        //this.addSpellCraftingEffectRecipe(6710886,new ItemStack(BlockDustStone.ITEM_STONE_222));
        //this.addSpellCraftingEffectRecipe(6710937,new ItemStack(BlockDustStone.ITEM_STONE_223));
        //this.addSpellCraftingEffectRecipe(6710988,new ItemStack(BlockDustStone.ITEM_STONE_224));
        //this.addSpellCraftingEffectRecipe(6711039,new ItemStack(BlockDustStone.ITEM_STONE_225));
        //this.addSpellCraftingEffectRecipe(6723840,new ItemStack(BlockDustStone.ITEM_STONE_230));
        //this.addSpellCraftingEffectRecipe(6723891,new ItemStack(BlockDustStone.ITEM_STONE_231));
        //this.addSpellCraftingEffectRecipe(6723942,new ItemStack(BlockDustStone.ITEM_STONE_232));
        //this.addSpellCraftingEffectRecipe(6723993,new ItemStack(BlockDustStone.ITEM_STONE_233));
        //this.addSpellCraftingEffectRecipe(6724044,new ItemStack(BlockDustStone.ITEM_STONE_234));
        //this.addSpellCraftingEffectRecipe(6724095,new ItemStack(BlockDustStone.ITEM_STONE_235));
        //this.addSpellCraftingEffectRecipe(6736896,new ItemStack(BlockDustStone.ITEM_STONE_240));
        //this.addSpellCraftingEffectRecipe(6736947,new ItemStack(BlockDustStone.ITEM_STONE_241));
        //this.addSpellCraftingEffectRecipe(6736998,new ItemStack(BlockDustStone.ITEM_STONE_242));
        //this.addSpellCraftingEffectRecipe(6737049,new ItemStack(BlockDustStone.ITEM_STONE_243));
        //this.addSpellCraftingEffectRecipe(6737100,new ItemStack(BlockDustStone.ITEM_STONE_244));
        //this.addSpellCraftingEffectRecipe(6737151,new ItemStack(BlockDustStone.ITEM_STONE_245));
        //this.addSpellCraftingEffectRecipe(6749952,new ItemStack(BlockDustStone.ITEM_STONE_250));
        //this.addSpellCraftingEffectRecipe(6750003,new ItemStack(BlockDustStone.ITEM_STONE_251));
        //this.addSpellCraftingEffectRecipe(6750054,new ItemStack(BlockDustStone.ITEM_STONE_252));
        //this.addSpellCraftingEffectRecipe(6750105,new ItemStack(BlockDustStone.ITEM_STONE_253));
        //this.addSpellCraftingEffectRecipe(6750156,new ItemStack(BlockDustStone.ITEM_STONE_254));
        //this.addSpellCraftingEffectRecipe(6750207,new ItemStack(BlockDustStone.ITEM_STONE_255));

        //this.addSpellCraftingEffectRecipe(10027008,new ItemStack(BlockDustStone.ITEM_STONE_300));
        //this.addSpellCraftingEffectRecipe(10027059,new ItemStack(BlockDustStone.ITEM_STONE_301));
        //this.addSpellCraftingEffectRecipe(10027110,new ItemStack(BlockDustStone.ITEM_STONE_302));
        //this.addSpellCraftingEffectRecipe(10027161,new ItemStack(BlockDustStone.ITEM_STONE_303));
        //this.addSpellCraftingEffectRecipe(10027212,new ItemStack(BlockDustStone.ITEM_STONE_304));
        //this.addSpellCraftingEffectRecipe(10027263,new ItemStack(BlockDustStone.ITEM_STONE_305));
        //this.addSpellCraftingEffectRecipe(10040064,new ItemStack(BlockDustStone.ITEM_STONE_310));
        //this.addSpellCraftingEffectRecipe(10040115,new ItemStack(BlockDustStone.ITEM_STONE_311));
        //this.addSpellCraftingEffectRecipe(10040166,new ItemStack(BlockDustStone.ITEM_STONE_312));
        //this.addSpellCraftingEffectRecipe(10040217,new ItemStack(BlockDustStone.ITEM_STONE_313));
        //this.addSpellCraftingEffectRecipe(10040268,new ItemStack(BlockDustStone.ITEM_STONE_314));
        //this.addSpellCraftingEffectRecipe(10040319,new ItemStack(BlockDustStone.ITEM_STONE_315));
        //this.addSpellCraftingEffectRecipe(10053120,new ItemStack(BlockDustStone.ITEM_STONE_320));
        //this.addSpellCraftingEffectRecipe(10053171,new ItemStack(BlockDustStone.ITEM_STONE_321));
        //this.addSpellCraftingEffectRecipe(10053222,new ItemStack(BlockDustStone.ITEM_STONE_322));
        //this.addSpellCraftingEffectRecipe(10053273,new ItemStack(BlockDustStone.ITEM_STONE_323));
        //this.addSpellCraftingEffectRecipe(10053324,new ItemStack(BlockDustStone.ITEM_STONE_324));
        //this.addSpellCraftingEffectRecipe(10053375,new ItemStack(BlockDustStone.ITEM_STONE_325));
        //this.addSpellCraftingEffectRecipe(10066176,new ItemStack(BlockDustStone.ITEM_STONE_330));
        //this.addSpellCraftingEffectRecipe(10066227,new ItemStack(BlockDustStone.ITEM_STONE_331));
        //this.addSpellCraftingEffectRecipe(10066278,new ItemStack(BlockDustStone.ITEM_STONE_332));
        //this.addSpellCraftingEffectRecipe(10066329,new ItemStack(BlockDustStone.ITEM_STONE_333));
        //this.addSpellCraftingEffectRecipe(10066380,new ItemStack(BlockDustStone.ITEM_STONE_334));
        //this.addSpellCraftingEffectRecipe(10066431,new ItemStack(BlockDustStone.ITEM_STONE_335));
        //this.addSpellCraftingEffectRecipe(10079232,new ItemStack(BlockDustStone.ITEM_STONE_340));
        //this.addSpellCraftingEffectRecipe(10079283,new ItemStack(BlockDustStone.ITEM_STONE_341));
        //this.addSpellCraftingEffectRecipe(10079334,new ItemStack(BlockDustStone.ITEM_STONE_342));
        //this.addSpellCraftingEffectRecipe(10079385,new ItemStack(BlockDustStone.ITEM_STONE_343));
        //this.addSpellCraftingEffectRecipe(10079436,new ItemStack(BlockDustStone.ITEM_STONE_344));
        //this.addSpellCraftingEffectRecipe(10079487,new ItemStack(BlockDustStone.ITEM_STONE_345));
        //this.addSpellCraftingEffectRecipe(10092288,new ItemStack(BlockDustStone.ITEM_STONE_350));
        //this.addSpellCraftingEffectRecipe(10092339,new ItemStack(BlockDustStone.ITEM_STONE_351));
        //this.addSpellCraftingEffectRecipe(10092390,new ItemStack(BlockDustStone.ITEM_STONE_352));
        //this.addSpellCraftingEffectRecipe(10092441,new ItemStack(BlockDustStone.ITEM_STONE_353));
        //this.addSpellCraftingEffectRecipe(10092492,new ItemStack(BlockDustStone.ITEM_STONE_354));
        //this.addSpellCraftingEffectRecipe(10092543,new ItemStack(BlockDustStone.ITEM_STONE_355));

        //this.addSpellCraftingEffectRecipe(13369344,new ItemStack(BlockDustStone.ITEM_STONE_400));
        //this.addSpellCraftingEffectRecipe(13369395,new ItemStack(BlockDustStone.ITEM_STONE_401));
        //this.addSpellCraftingEffectRecipe(13369446,new ItemStack(BlockDustStone.ITEM_STONE_402));
        //this.addSpellCraftingEffectRecipe(13369497,new ItemStack(BlockDustStone.ITEM_STONE_403));
        //this.addSpellCraftingEffectRecipe(13369548,new ItemStack(BlockDustStone.ITEM_STONE_404));
        //this.addSpellCraftingEffectRecipe(13369599,new ItemStack(BlockDustStone.ITEM_STONE_405));
        //this.addSpellCraftingEffectRecipe(13382400,new ItemStack(BlockDustStone.ITEM_STONE_410));
        //this.addSpellCraftingEffectRecipe(13382451,new ItemStack(BlockDustStone.ITEM_STONE_411));
        //this.addSpellCraftingEffectRecipe(13382502,new ItemStack(BlockDustStone.ITEM_STONE_412));
        //this.addSpellCraftingEffectRecipe(13382553,new ItemStack(BlockDustStone.ITEM_STONE_413));
        //this.addSpellCraftingEffectRecipe(13382604,new ItemStack(BlockDustStone.ITEM_STONE_414));
        //this.addSpellCraftingEffectRecipe(13382655,new ItemStack(BlockDustStone.ITEM_STONE_415));
        //this.addSpellCraftingEffectRecipe(13395456,new ItemStack(BlockDustStone.ITEM_STONE_420));
        //this.addSpellCraftingEffectRecipe(13395507,new ItemStack(BlockDustStone.ITEM_STONE_421));
        //this.addSpellCraftingEffectRecipe(13395558,new ItemStack(BlockDustStone.ITEM_STONE_422));
        //this.addSpellCraftingEffectRecipe(13395609,new ItemStack(BlockDustStone.ITEM_STONE_423));
        //this.addSpellCraftingEffectRecipe(13395660,new ItemStack(BlockDustStone.ITEM_STONE_424));
        //this.addSpellCraftingEffectRecipe(13395711,new ItemStack(BlockDustStone.ITEM_STONE_425));
        //this.addSpellCraftingEffectRecipe(13408512,new ItemStack(BlockDustStone.ITEM_STONE_430));
        //this.addSpellCraftingEffectRecipe(13408563,new ItemStack(BlockDustStone.ITEM_STONE_431));
        //this.addSpellCraftingEffectRecipe(13408614,new ItemStack(BlockDustStone.ITEM_STONE_432));
        //this.addSpellCraftingEffectRecipe(13408665,new ItemStack(BlockDustStone.ITEM_STONE_433));
        //this.addSpellCraftingEffectRecipe(13408716,new ItemStack(BlockDustStone.ITEM_STONE_434));
        //this.addSpellCraftingEffectRecipe(13408767,new ItemStack(BlockDustStone.ITEM_STONE_435));
        //this.addSpellCraftingEffectRecipe(13421568,new ItemStack(BlockDustStone.ITEM_STONE_440));
        //this.addSpellCraftingEffectRecipe(13421619,new ItemStack(BlockDustStone.ITEM_STONE_441));
        //this.addSpellCraftingEffectRecipe(13421670,new ItemStack(BlockDustStone.ITEM_STONE_442));
        //this.addSpellCraftingEffectRecipe(13421721,new ItemStack(BlockDustStone.ITEM_STONE_443));
        //this.addSpellCraftingEffectRecipe(13421772,new ItemStack(BlockDustStone.ITEM_STONE_444));
        //this.addSpellCraftingEffectRecipe(13421823,new ItemStack(BlockDustStone.ITEM_STONE_445));
        //this.addSpellCraftingEffectRecipe(13434624,new ItemStack(BlockDustStone.ITEM_STONE_450));
        //this.addSpellCraftingEffectRecipe(13434675,new ItemStack(BlockDustStone.ITEM_STONE_451));
        //this.addSpellCraftingEffectRecipe(13434726,new ItemStack(BlockDustStone.ITEM_STONE_452));
        //this.addSpellCraftingEffectRecipe(13434777,new ItemStack(BlockDustStone.ITEM_STONE_453));
        //this.addSpellCraftingEffectRecipe(13434828,new ItemStack(BlockDustStone.ITEM_STONE_454));
        //this.addSpellCraftingEffectRecipe(13434879,new ItemStack(BlockDustStone.ITEM_STONE_455));

        this.addSpellCraftingEffectRecipe(16711680,Effects.STRENGTH);//500
        //this.addSpellCraftingEffectRecipe(16711731,new ItemStack(BlockDustStone.ITEM_STONE_501));
        //this.addSpellCraftingEffectRecipe(16711782,new ItemStack(BlockDustStone.ITEM_STONE_502));
        //this.addSpellCraftingEffectRecipe(16711833,new ItemStack(BlockDustStone.ITEM_STONE_503));
        //this.addSpellCraftingEffectRecipe(16711884,new ItemStack(BlockDustStone.ITEM_STONE_504));
        //this.addSpellCraftingEffectRecipe(16711935,new ItemStack(BlockDustStone.ITEM_STONE_505));
        //this.addSpellCraftingEffectRecipe(16724736,new ItemStack(BlockDustStone.ITEM_STONE_510));
        //this.addSpellCraftingEffectRecipe(16724787,new ItemStack(BlockDustStone.ITEM_STONE_511));
        //this.addSpellCraftingEffectRecipe(16724838,new ItemStack(BlockDustStone.ITEM_STONE_512));
        //this.addSpellCraftingEffectRecipe(16724889,new ItemStack(BlockDustStone.ITEM_STONE_513));
        //this.addSpellCraftingEffectRecipe(16724940,new ItemStack(BlockDustStone.ITEM_STONE_514));
        //this.addSpellCraftingEffectRecipe(16724991,new ItemStack(BlockDustStone.ITEM_STONE_515));
        //this.addSpellCraftingEffectRecipe(16737792,new ItemStack(BlockDustStone.ITEM_STONE_520));
        //this.addSpellCraftingEffectRecipe(16737843,new ItemStack(BlockDustStone.ITEM_STONE_521));
        //this.addSpellCraftingEffectRecipe(16737894,new ItemStack(BlockDustStone.ITEM_STONE_522));
        //this.addSpellCraftingEffectRecipe(16737945,new ItemStack(BlockDustStone.ITEM_STONE_523));
        //this.addSpellCraftingEffectRecipe(16737996,new ItemStack(BlockDustStone.ITEM_STONE_524));
        //this.addSpellCraftingEffectRecipe(16738047,new ItemStack(BlockDustStone.ITEM_STONE_525));
        //this.addSpellCraftingEffectRecipe(16750848,new ItemStack(BlockDustStone.ITEM_STONE_530));
        //this.addSpellCraftingEffectRecipe(16750899,new ItemStack(BlockDustStone.ITEM_STONE_531));
        //this.addSpellCraftingEffectRecipe(16750950,new ItemStack(BlockDustStone.ITEM_STONE_532));
        //this.addSpellCraftingEffectRecipe(16751001,new ItemStack(BlockDustStone.ITEM_STONE_533));
        //this.addSpellCraftingEffectRecipe(16751052,new ItemStack(BlockDustStone.ITEM_STONE_534));
        //this.addSpellCraftingEffectRecipe(16751103,new ItemStack(BlockDustStone.ITEM_STONE_535));
        //this.addSpellCraftingEffectRecipe(16763904,new ItemStack(BlockDustStone.ITEM_STONE_540));
        //this.addSpellCraftingEffectRecipe(16763955,new ItemStack(BlockDustStone.ITEM_STONE_541));
        //this.addSpellCraftingEffectRecipe(16764006,new ItemStack(BlockDustStone.ITEM_STONE_542));
        //this.addSpellCraftingEffectRecipe(16764057,new ItemStack(BlockDustStone.ITEM_STONE_543));
        //this.addSpellCraftingEffectRecipe(16764108,new ItemStack(BlockDustStone.ITEM_STONE_544));
        //this.addSpellCraftingEffectRecipe(16764159,new ItemStack(BlockDustStone.ITEM_STONE_545));
        //this.addSpellCraftingEffectRecipe(16776960,new ItemStack(BlockDustStone.ITEM_STONE_550));
        //this.addSpellCraftingEffectRecipe(16777011,new ItemStack(BlockDustStone.ITEM_STONE_551));
        //this.addSpellCraftingEffectRecipe(16777062,new ItemStack(BlockDustStone.ITEM_STONE_552));
        //this.addSpellCraftingEffectRecipe(16777113,new ItemStack(BlockDustStone.ITEM_STONE_553));
        //this.addSpellCraftingEffectRecipe(16777164,new ItemStack(BlockDustStone.ITEM_STONE_554));
        this.addSpellCraftingEffectRecipe(16777215,Effects.LEVITATION);//555
    }

    public void addSpellCraftingEffectRecipe(int colorIn, Effect effect)
    {
        if (getResult(colorIn) != Effects.UNLUCK) { return;}
        this.spellCraftingEffectList.put(colorIn, effect);
    }

    /**
     * Returns the smelting result of an item.
     */
    public Effect getResult(int colorIn)
    {
        for (Map.Entry<Integer, Effect> entry : this.spellCraftingEffectList.entrySet())
        {
            if (colorIn == entry.getKey())
            {
                return entry.getValue();
            }
        }

        //ToDo: "Default" effect
        return Effects.UNLUCK;
    }

    public Map<Integer, Effect> getEffectList()
    {
        return this.spellCraftingEffectList;
    }

}
