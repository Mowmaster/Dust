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
    /** The list of smelting results. */
    private final Map<Integer, ItemStack> spellCraftingStoneList = Maps.<Integer, ItemStack>newHashMap();

    /**
     * Returns an instance of FurnaceRecipes.
     */
    public static SpellCraftingStone instance()
    {
        return SPELL_CRAFTING_STONE;
    }

    private SpellCraftingStone()
    {
        //this.addSpellCraftingStoneRecipe(0,new ItemStack(BlockDustStone.ITEM_STONE_000,1));

        this.addSpellCraftingStoneRecipe(0,new ItemStack(BlockDustStone.ITEM_STONE_000));
        this.addSpellCraftingStoneRecipe(50,new ItemStack(BlockDustStone.ITEM_STONE_001));
        this.addSpellCraftingStoneRecipe(101,new ItemStack(BlockDustStone.ITEM_STONE_002));
        this.addSpellCraftingStoneRecipe(153,new ItemStack(BlockDustStone.ITEM_STONE_003));
        this.addSpellCraftingStoneRecipe(204,new ItemStack(BlockDustStone.ITEM_STONE_004));
        this.addSpellCraftingStoneRecipe(255,new ItemStack(BlockDustStone.ITEM_STONE_005));
        this.addSpellCraftingStoneRecipe(12851,new ItemStack(BlockDustStone.ITEM_STONE_010));
        this.addSpellCraftingStoneRecipe(12901,new ItemStack(BlockDustStone.ITEM_STONE_011));
        this.addSpellCraftingStoneRecipe(12952,new ItemStack(BlockDustStone.ITEM_STONE_012));
        this.addSpellCraftingStoneRecipe(13004,new ItemStack(BlockDustStone.ITEM_STONE_013));
        this.addSpellCraftingStoneRecipe(13055,new ItemStack(BlockDustStone.ITEM_STONE_014));
        this.addSpellCraftingStoneRecipe(13106,new ItemStack(BlockDustStone.ITEM_STONE_015));
        this.addSpellCraftingStoneRecipe(25958,new ItemStack(BlockDustStone.ITEM_STONE_020));
        this.addSpellCraftingStoneRecipe(26008,new ItemStack(BlockDustStone.ITEM_STONE_021));
        this.addSpellCraftingStoneRecipe(26059,new ItemStack(BlockDustStone.ITEM_STONE_022));
        this.addSpellCraftingStoneRecipe(26111,new ItemStack(BlockDustStone.ITEM_STONE_023));
        this.addSpellCraftingStoneRecipe(26162,new ItemStack(BlockDustStone.ITEM_STONE_024));
        this.addSpellCraftingStoneRecipe(26213,new ItemStack(BlockDustStone.ITEM_STONE_025));
        this.addSpellCraftingStoneRecipe(39066,new ItemStack(BlockDustStone.ITEM_STONE_030));
        this.addSpellCraftingStoneRecipe(39116,new ItemStack(BlockDustStone.ITEM_STONE_031));
        this.addSpellCraftingStoneRecipe(39167,new ItemStack(BlockDustStone.ITEM_STONE_032));
        this.addSpellCraftingStoneRecipe(39219,new ItemStack(BlockDustStone.ITEM_STONE_033));
        this.addSpellCraftingStoneRecipe(39270,new ItemStack(BlockDustStone.ITEM_STONE_034));
        this.addSpellCraftingStoneRecipe(39321,new ItemStack(BlockDustStone.ITEM_STONE_035));
        this.addSpellCraftingStoneRecipe(52173,new ItemStack(BlockDustStone.ITEM_STONE_040));
        this.addSpellCraftingStoneRecipe(52223,new ItemStack(BlockDustStone.ITEM_STONE_041));
        this.addSpellCraftingStoneRecipe(52274,new ItemStack(BlockDustStone.ITEM_STONE_042));
        this.addSpellCraftingStoneRecipe(52326,new ItemStack(BlockDustStone.ITEM_STONE_043));
        this.addSpellCraftingStoneRecipe(52377,new ItemStack(BlockDustStone.ITEM_STONE_044));
        this.addSpellCraftingStoneRecipe(52428,new ItemStack(BlockDustStone.ITEM_STONE_045));
        this.addSpellCraftingStoneRecipe(65280,new ItemStack(BlockDustStone.ITEM_STONE_050));
        this.addSpellCraftingStoneRecipe(65330,new ItemStack(BlockDustStone.ITEM_STONE_051));
        this.addSpellCraftingStoneRecipe(65381,new ItemStack(BlockDustStone.ITEM_STONE_052));
        this.addSpellCraftingStoneRecipe(65433,new ItemStack(BlockDustStone.ITEM_STONE_053));
        this.addSpellCraftingStoneRecipe(65484,new ItemStack(BlockDustStone.ITEM_STONE_054));
        this.addSpellCraftingStoneRecipe(65535,new ItemStack(BlockDustStone.ITEM_STONE_055));

        this.addSpellCraftingStoneRecipe(3289907,new ItemStack(BlockDustStone.ITEM_STONE_100));
        this.addSpellCraftingStoneRecipe(3289957,new ItemStack(BlockDustStone.ITEM_STONE_101));
        this.addSpellCraftingStoneRecipe(3290008,new ItemStack(BlockDustStone.ITEM_STONE_102));
        this.addSpellCraftingStoneRecipe(3290060,new ItemStack(BlockDustStone.ITEM_STONE_103));
        this.addSpellCraftingStoneRecipe(3290111,new ItemStack(BlockDustStone.ITEM_STONE_104));
        this.addSpellCraftingStoneRecipe(3290162,new ItemStack(BlockDustStone.ITEM_STONE_105));
        this.addSpellCraftingStoneRecipe(3302758,new ItemStack(BlockDustStone.ITEM_STONE_110));
        this.addSpellCraftingStoneRecipe(3302808,new ItemStack(BlockDustStone.ITEM_STONE_111));
        this.addSpellCraftingStoneRecipe(3302859,new ItemStack(BlockDustStone.ITEM_STONE_112));
        this.addSpellCraftingStoneRecipe(3302911,new ItemStack(BlockDustStone.ITEM_STONE_113));
        this.addSpellCraftingStoneRecipe(3302962,new ItemStack(BlockDustStone.ITEM_STONE_114));
        this.addSpellCraftingStoneRecipe(3303013,new ItemStack(BlockDustStone.ITEM_STONE_115));
        this.addSpellCraftingStoneRecipe(3315865,new ItemStack(BlockDustStone.ITEM_STONE_120));
        this.addSpellCraftingStoneRecipe(3315915,new ItemStack(BlockDustStone.ITEM_STONE_121));
        this.addSpellCraftingStoneRecipe(3315966,new ItemStack(BlockDustStone.ITEM_STONE_122));
        this.addSpellCraftingStoneRecipe(3316018,new ItemStack(BlockDustStone.ITEM_STONE_123));
        this.addSpellCraftingStoneRecipe(3316069,new ItemStack(BlockDustStone.ITEM_STONE_124));
        this.addSpellCraftingStoneRecipe(3316120,new ItemStack(BlockDustStone.ITEM_STONE_125));
        this.addSpellCraftingStoneRecipe(3328973,new ItemStack(BlockDustStone.ITEM_STONE_130));
        this.addSpellCraftingStoneRecipe(3329023,new ItemStack(BlockDustStone.ITEM_STONE_131));
        this.addSpellCraftingStoneRecipe(3329074,new ItemStack(BlockDustStone.ITEM_STONE_132));
        this.addSpellCraftingStoneRecipe(3329126,new ItemStack(BlockDustStone.ITEM_STONE_133));
        this.addSpellCraftingStoneRecipe(3329177,new ItemStack(BlockDustStone.ITEM_STONE_134));
        this.addSpellCraftingStoneRecipe(3329228,new ItemStack(BlockDustStone.ITEM_STONE_135));
        this.addSpellCraftingStoneRecipe(3342080,new ItemStack(BlockDustStone.ITEM_STONE_140));
        this.addSpellCraftingStoneRecipe(3342130,new ItemStack(BlockDustStone.ITEM_STONE_141));
        this.addSpellCraftingStoneRecipe(3342181,new ItemStack(BlockDustStone.ITEM_STONE_142));
        this.addSpellCraftingStoneRecipe(3342233,new ItemStack(BlockDustStone.ITEM_STONE_143));
        this.addSpellCraftingStoneRecipe(3342284,new ItemStack(BlockDustStone.ITEM_STONE_144));
        this.addSpellCraftingStoneRecipe(3342335,new ItemStack(BlockDustStone.ITEM_STONE_145));
        this.addSpellCraftingStoneRecipe(3355187,new ItemStack(BlockDustStone.ITEM_STONE_150));
        this.addSpellCraftingStoneRecipe(3355237,new ItemStack(BlockDustStone.ITEM_STONE_151));
        this.addSpellCraftingStoneRecipe(3355288,new ItemStack(BlockDustStone.ITEM_STONE_152));
        this.addSpellCraftingStoneRecipe(3355340,new ItemStack(BlockDustStone.ITEM_STONE_153));
        this.addSpellCraftingStoneRecipe(3355391,new ItemStack(BlockDustStone.ITEM_STONE_154));
        this.addSpellCraftingStoneRecipe(3355442,new ItemStack(BlockDustStone.ITEM_STONE_155));

        this.addSpellCraftingStoneRecipe(6645350,new ItemStack(BlockDustStone.ITEM_STONE_200));
        this.addSpellCraftingStoneRecipe(6645400,new ItemStack(BlockDustStone.ITEM_STONE_201));
        this.addSpellCraftingStoneRecipe(6645451,new ItemStack(BlockDustStone.ITEM_STONE_202));
        this.addSpellCraftingStoneRecipe(6645503,new ItemStack(BlockDustStone.ITEM_STONE_203));
        this.addSpellCraftingStoneRecipe(6645554,new ItemStack(BlockDustStone.ITEM_STONE_204));
        this.addSpellCraftingStoneRecipe(6645605,new ItemStack(BlockDustStone.ITEM_STONE_205));
        this.addSpellCraftingStoneRecipe(6658201,new ItemStack(BlockDustStone.ITEM_STONE_210));
        this.addSpellCraftingStoneRecipe(6658251,new ItemStack(BlockDustStone.ITEM_STONE_211));
        this.addSpellCraftingStoneRecipe(6658302,new ItemStack(BlockDustStone.ITEM_STONE_212));
        this.addSpellCraftingStoneRecipe(6658354,new ItemStack(BlockDustStone.ITEM_STONE_213));
        this.addSpellCraftingStoneRecipe(6658405,new ItemStack(BlockDustStone.ITEM_STONE_214));
        this.addSpellCraftingStoneRecipe(6658456,new ItemStack(BlockDustStone.ITEM_STONE_215));
        this.addSpellCraftingStoneRecipe(6671308,new ItemStack(BlockDustStone.ITEM_STONE_220));
        this.addSpellCraftingStoneRecipe(6671358,new ItemStack(BlockDustStone.ITEM_STONE_221));
        this.addSpellCraftingStoneRecipe(6671409,new ItemStack(BlockDustStone.ITEM_STONE_222));
        this.addSpellCraftingStoneRecipe(6671461,new ItemStack(BlockDustStone.ITEM_STONE_223));
        this.addSpellCraftingStoneRecipe(6671512,new ItemStack(BlockDustStone.ITEM_STONE_224));
        this.addSpellCraftingStoneRecipe(6671563,new ItemStack(BlockDustStone.ITEM_STONE_225));
        this.addSpellCraftingStoneRecipe(6684416,new ItemStack(BlockDustStone.ITEM_STONE_230));
        this.addSpellCraftingStoneRecipe(6684466,new ItemStack(BlockDustStone.ITEM_STONE_231));
        this.addSpellCraftingStoneRecipe(6684517,new ItemStack(BlockDustStone.ITEM_STONE_232));
        this.addSpellCraftingStoneRecipe(6684569,new ItemStack(BlockDustStone.ITEM_STONE_233));
        this.addSpellCraftingStoneRecipe(6684620,new ItemStack(BlockDustStone.ITEM_STONE_234));
        this.addSpellCraftingStoneRecipe(6684671,new ItemStack(BlockDustStone.ITEM_STONE_235));
        this.addSpellCraftingStoneRecipe(6697523,new ItemStack(BlockDustStone.ITEM_STONE_240));
        this.addSpellCraftingStoneRecipe(6697573,new ItemStack(BlockDustStone.ITEM_STONE_241));
        this.addSpellCraftingStoneRecipe(6697624,new ItemStack(BlockDustStone.ITEM_STONE_242));
        this.addSpellCraftingStoneRecipe(6697676,new ItemStack(BlockDustStone.ITEM_STONE_243));
        this.addSpellCraftingStoneRecipe(6697727,new ItemStack(BlockDustStone.ITEM_STONE_244));
        this.addSpellCraftingStoneRecipe(6697778,new ItemStack(BlockDustStone.ITEM_STONE_245));
        this.addSpellCraftingStoneRecipe(6710630,new ItemStack(BlockDustStone.ITEM_STONE_250));
        this.addSpellCraftingStoneRecipe(6710680,new ItemStack(BlockDustStone.ITEM_STONE_251));
        this.addSpellCraftingStoneRecipe(6710731,new ItemStack(BlockDustStone.ITEM_STONE_252));
        this.addSpellCraftingStoneRecipe(6710783,new ItemStack(BlockDustStone.ITEM_STONE_253));
        this.addSpellCraftingStoneRecipe(6710834,new ItemStack(BlockDustStone.ITEM_STONE_254));
        this.addSpellCraftingStoneRecipe(6710885,new ItemStack(BlockDustStone.ITEM_STONE_255));

        this.addSpellCraftingStoneRecipe(10000794,new ItemStack(BlockDustStone.ITEM_STONE_300));
        this.addSpellCraftingStoneRecipe(10000844,new ItemStack(BlockDustStone.ITEM_STONE_301));
        this.addSpellCraftingStoneRecipe(10000895,new ItemStack(BlockDustStone.ITEM_STONE_302));
        this.addSpellCraftingStoneRecipe(10000947,new ItemStack(BlockDustStone.ITEM_STONE_303));
        this.addSpellCraftingStoneRecipe(10000998,new ItemStack(BlockDustStone.ITEM_STONE_304));
        this.addSpellCraftingStoneRecipe(10001049,new ItemStack(BlockDustStone.ITEM_STONE_305));
        this.addSpellCraftingStoneRecipe(10013645,new ItemStack(BlockDustStone.ITEM_STONE_310));
        this.addSpellCraftingStoneRecipe(10013695,new ItemStack(BlockDustStone.ITEM_STONE_311));
        this.addSpellCraftingStoneRecipe(10013746,new ItemStack(BlockDustStone.ITEM_STONE_312));
        this.addSpellCraftingStoneRecipe(10013798,new ItemStack(BlockDustStone.ITEM_STONE_313));
        this.addSpellCraftingStoneRecipe(10013849,new ItemStack(BlockDustStone.ITEM_STONE_314));
        this.addSpellCraftingStoneRecipe(10013900,new ItemStack(BlockDustStone.ITEM_STONE_315));
        this.addSpellCraftingStoneRecipe(10026752,new ItemStack(BlockDustStone.ITEM_STONE_320));
        this.addSpellCraftingStoneRecipe(10026802,new ItemStack(BlockDustStone.ITEM_STONE_321));
        this.addSpellCraftingStoneRecipe(10026853,new ItemStack(BlockDustStone.ITEM_STONE_322));
        this.addSpellCraftingStoneRecipe(10026905,new ItemStack(BlockDustStone.ITEM_STONE_323));
        this.addSpellCraftingStoneRecipe(10026956,new ItemStack(BlockDustStone.ITEM_STONE_324));
        this.addSpellCraftingStoneRecipe(10027007,new ItemStack(BlockDustStone.ITEM_STONE_325));
        this.addSpellCraftingStoneRecipe(10039860,new ItemStack(BlockDustStone.ITEM_STONE_330));
        this.addSpellCraftingStoneRecipe(10039910,new ItemStack(BlockDustStone.ITEM_STONE_331));
        this.addSpellCraftingStoneRecipe(10039961,new ItemStack(BlockDustStone.ITEM_STONE_332));
        this.addSpellCraftingStoneRecipe(10040013,new ItemStack(BlockDustStone.ITEM_STONE_333));
        this.addSpellCraftingStoneRecipe(10010064,new ItemStack(BlockDustStone.ITEM_STONE_334));
        this.addSpellCraftingStoneRecipe(10040115,new ItemStack(BlockDustStone.ITEM_STONE_335));
        this.addSpellCraftingStoneRecipe(10052967,new ItemStack(BlockDustStone.ITEM_STONE_340));
        this.addSpellCraftingStoneRecipe(10053017,new ItemStack(BlockDustStone.ITEM_STONE_341));
        this.addSpellCraftingStoneRecipe(10053068,new ItemStack(BlockDustStone.ITEM_STONE_342));
        this.addSpellCraftingStoneRecipe(10053120,new ItemStack(BlockDustStone.ITEM_STONE_343));
        this.addSpellCraftingStoneRecipe(10053171,new ItemStack(BlockDustStone.ITEM_STONE_344));
        this.addSpellCraftingStoneRecipe(10053222,new ItemStack(BlockDustStone.ITEM_STONE_345));
        this.addSpellCraftingStoneRecipe(10066074,new ItemStack(BlockDustStone.ITEM_STONE_350));
        this.addSpellCraftingStoneRecipe(10066124,new ItemStack(BlockDustStone.ITEM_STONE_351));
        this.addSpellCraftingStoneRecipe(10066175,new ItemStack(BlockDustStone.ITEM_STONE_352));
        this.addSpellCraftingStoneRecipe(10066227,new ItemStack(BlockDustStone.ITEM_STONE_353));
        this.addSpellCraftingStoneRecipe(10066278,new ItemStack(BlockDustStone.ITEM_STONE_354));
        this.addSpellCraftingStoneRecipe(10066329,new ItemStack(BlockDustStone.ITEM_STONE_355));

        this.addSpellCraftingStoneRecipe(13356237,new ItemStack(BlockDustStone.ITEM_STONE_400));
        this.addSpellCraftingStoneRecipe(13356287,new ItemStack(BlockDustStone.ITEM_STONE_401));
        this.addSpellCraftingStoneRecipe(13356338,new ItemStack(BlockDustStone.ITEM_STONE_402));
        this.addSpellCraftingStoneRecipe(13356390,new ItemStack(BlockDustStone.ITEM_STONE_403));
        this.addSpellCraftingStoneRecipe(13356441,new ItemStack(BlockDustStone.ITEM_STONE_404));
        this.addSpellCraftingStoneRecipe(13356492,new ItemStack(BlockDustStone.ITEM_STONE_405));
        this.addSpellCraftingStoneRecipe(13369088,new ItemStack(BlockDustStone.ITEM_STONE_410));
        this.addSpellCraftingStoneRecipe(13369138,new ItemStack(BlockDustStone.ITEM_STONE_411));
        this.addSpellCraftingStoneRecipe(13369189,new ItemStack(BlockDustStone.ITEM_STONE_412));
        this.addSpellCraftingStoneRecipe(13369241,new ItemStack(BlockDustStone.ITEM_STONE_413));
        this.addSpellCraftingStoneRecipe(13369292,new ItemStack(BlockDustStone.ITEM_STONE_414));
        this.addSpellCraftingStoneRecipe(13369343,new ItemStack(BlockDustStone.ITEM_STONE_415));
        this.addSpellCraftingStoneRecipe(13382195,new ItemStack(BlockDustStone.ITEM_STONE_420));
        this.addSpellCraftingStoneRecipe(13382245,new ItemStack(BlockDustStone.ITEM_STONE_421));
        this.addSpellCraftingStoneRecipe(13382296,new ItemStack(BlockDustStone.ITEM_STONE_422));
        this.addSpellCraftingStoneRecipe(13382348,new ItemStack(BlockDustStone.ITEM_STONE_423));
        this.addSpellCraftingStoneRecipe(13382399,new ItemStack(BlockDustStone.ITEM_STONE_424));
        this.addSpellCraftingStoneRecipe(13382450,new ItemStack(BlockDustStone.ITEM_STONE_425));
        this.addSpellCraftingStoneRecipe(13395303,new ItemStack(BlockDustStone.ITEM_STONE_430));
        this.addSpellCraftingStoneRecipe(13395353,new ItemStack(BlockDustStone.ITEM_STONE_431));
        this.addSpellCraftingStoneRecipe(13395404,new ItemStack(BlockDustStone.ITEM_STONE_432));
        this.addSpellCraftingStoneRecipe(13395456,new ItemStack(BlockDustStone.ITEM_STONE_433));
        this.addSpellCraftingStoneRecipe(13395507,new ItemStack(BlockDustStone.ITEM_STONE_434));
        this.addSpellCraftingStoneRecipe(13395558,new ItemStack(BlockDustStone.ITEM_STONE_435));
        this.addSpellCraftingStoneRecipe(13408410,new ItemStack(BlockDustStone.ITEM_STONE_440));
        this.addSpellCraftingStoneRecipe(13408460,new ItemStack(BlockDustStone.ITEM_STONE_441));
        this.addSpellCraftingStoneRecipe(13408511,new ItemStack(BlockDustStone.ITEM_STONE_442));
        this.addSpellCraftingStoneRecipe(13408563,new ItemStack(BlockDustStone.ITEM_STONE_443));
        this.addSpellCraftingStoneRecipe(13408614,new ItemStack(BlockDustStone.ITEM_STONE_444));
        this.addSpellCraftingStoneRecipe(13408665,new ItemStack(BlockDustStone.ITEM_STONE_445));
        this.addSpellCraftingStoneRecipe(13421517,new ItemStack(BlockDustStone.ITEM_STONE_450));
        this.addSpellCraftingStoneRecipe(13421567,new ItemStack(BlockDustStone.ITEM_STONE_451));
        this.addSpellCraftingStoneRecipe(13421618,new ItemStack(BlockDustStone.ITEM_STONE_452));
        this.addSpellCraftingStoneRecipe(13421670,new ItemStack(BlockDustStone.ITEM_STONE_453));
        this.addSpellCraftingStoneRecipe(13421721,new ItemStack(BlockDustStone.ITEM_STONE_454));
        this.addSpellCraftingStoneRecipe(13421772,new ItemStack(BlockDustStone.ITEM_STONE_455));

        this.addSpellCraftingStoneRecipe(16711680,new ItemStack(BlockDustStone.ITEM_STONE_500));
        this.addSpellCraftingStoneRecipe(16711730,new ItemStack(BlockDustStone.ITEM_STONE_501));
        this.addSpellCraftingStoneRecipe(16711781,new ItemStack(BlockDustStone.ITEM_STONE_502));
        this.addSpellCraftingStoneRecipe(16711833,new ItemStack(BlockDustStone.ITEM_STONE_503));
        this.addSpellCraftingStoneRecipe(16711884,new ItemStack(BlockDustStone.ITEM_STONE_504));
        this.addSpellCraftingStoneRecipe(16711935,new ItemStack(BlockDustStone.ITEM_STONE_505));
        this.addSpellCraftingStoneRecipe(16724531,new ItemStack(BlockDustStone.ITEM_STONE_510));
        this.addSpellCraftingStoneRecipe(16724581,new ItemStack(BlockDustStone.ITEM_STONE_511));
        this.addSpellCraftingStoneRecipe(16724632,new ItemStack(BlockDustStone.ITEM_STONE_512));
        this.addSpellCraftingStoneRecipe(16724684,new ItemStack(BlockDustStone.ITEM_STONE_513));
        this.addSpellCraftingStoneRecipe(16724735,new ItemStack(BlockDustStone.ITEM_STONE_514));
        this.addSpellCraftingStoneRecipe(16724786,new ItemStack(BlockDustStone.ITEM_STONE_515));
        this.addSpellCraftingStoneRecipe(16737638,new ItemStack(BlockDustStone.ITEM_STONE_520));
        this.addSpellCraftingStoneRecipe(16737688,new ItemStack(BlockDustStone.ITEM_STONE_521));
        this.addSpellCraftingStoneRecipe(16737739,new ItemStack(BlockDustStone.ITEM_STONE_522));
        this.addSpellCraftingStoneRecipe(16737791,new ItemStack(BlockDustStone.ITEM_STONE_523));
        this.addSpellCraftingStoneRecipe(16737842,new ItemStack(BlockDustStone.ITEM_STONE_524));
        this.addSpellCraftingStoneRecipe(16737893,new ItemStack(BlockDustStone.ITEM_STONE_525));
        this.addSpellCraftingStoneRecipe(16750746,new ItemStack(BlockDustStone.ITEM_STONE_530));
        this.addSpellCraftingStoneRecipe(16750796,new ItemStack(BlockDustStone.ITEM_STONE_531));
        this.addSpellCraftingStoneRecipe(16750847,new ItemStack(BlockDustStone.ITEM_STONE_532));
        this.addSpellCraftingStoneRecipe(16750899,new ItemStack(BlockDustStone.ITEM_STONE_533));
        this.addSpellCraftingStoneRecipe(16750950,new ItemStack(BlockDustStone.ITEM_STONE_534));
        this.addSpellCraftingStoneRecipe(16751001,new ItemStack(BlockDustStone.ITEM_STONE_535));
        this.addSpellCraftingStoneRecipe(16763853,new ItemStack(BlockDustStone.ITEM_STONE_540));
        this.addSpellCraftingStoneRecipe(16763903,new ItemStack(BlockDustStone.ITEM_STONE_541));
        this.addSpellCraftingStoneRecipe(16763954,new ItemStack(BlockDustStone.ITEM_STONE_542));
        this.addSpellCraftingStoneRecipe(16764006,new ItemStack(BlockDustStone.ITEM_STONE_543));
        this.addSpellCraftingStoneRecipe(16764057,new ItemStack(BlockDustStone.ITEM_STONE_544));
        this.addSpellCraftingStoneRecipe(16764108,new ItemStack(BlockDustStone.ITEM_STONE_545));
        this.addSpellCraftingStoneRecipe(16776960,new ItemStack(BlockDustStone.ITEM_STONE_550));
        this.addSpellCraftingStoneRecipe(16777010,new ItemStack(BlockDustStone.ITEM_STONE_551));
        this.addSpellCraftingStoneRecipe(16777061,new ItemStack(BlockDustStone.ITEM_STONE_552));
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
