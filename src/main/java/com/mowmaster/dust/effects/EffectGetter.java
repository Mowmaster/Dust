package com.mowmaster.dust.effects;

import com.google.common.collect.Maps;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.Loader;

import java.util.Map;

public class EffectGetter
{
    private static final EffectGetter EFFECT_BASE = new EffectGetter();
    private final Map<Integer, EffectHashMap> effectList = Maps.<Integer, EffectHashMap>newHashMap();
    public static EffectGetter instance()
    {
        return EFFECT_BASE;
    }
    private boolean immersiveE=(Loader.isModLoaded("immersiveengineering"))?(true):(false);
    private boolean astralSorc=(Loader.isModLoaded("astralsorcery"))?(true):(false);
    private boolean bloodMagic=(Loader.isModLoaded("bloodmagic"))?(true):(false);
    private boolean botania=(Loader.isModLoaded("botania"))?(true):(false);
    private boolean champions=(Loader.isModLoaded("champions"))?(true):(false);
    private boolean naturalPledge=(Loader.isModLoaded("naturalpledge"))?(true):(false);
    private boolean naturesAura=(Loader.isModLoaded("naturesaura"))?(true):(false);
    private boolean quark=(Loader.isModLoaded("quark"))?(true):(false);
    private boolean railCraft=(Loader.isModLoaded("railcraft"))?(true):(false);
    private boolean rustic=(Loader.isModLoaded("rustic"))?(true):(false);
    private boolean thaumcraft=(Loader.isModLoaded("thaumcraft"))?(true):(false);
    private boolean twilightForest=(Loader.isModLoaded("twilightforest"))?(true):(false);
    private boolean wizardry=(Loader.isModLoaded("wizardry"))?(true):(false);

    private boolean abyssal=(Loader.isModLoaded("abyssalcraft"))?(true):(false);
    private boolean aov=(Loader.isModLoaded("aov"))?(true):(false);
    private boolean bewitchment=(Loader.isModLoaded("bewitchment"))?(true):(false);
    private boolean bop=(Loader.isModLoaded("biomesoplenty"))?(true):(false);
    private boolean conarm=(Loader.isModLoaded("conarm"))?(true):(false);
    private boolean ebwizardry=(Loader.isModLoaded("ebwizardry"))?(true):(false);
    private boolean extraspells=(Loader.isModLoaded("extra_spells"))?(true):(false);
    private boolean forbiddenarcanus=(Loader.isModLoaded("forbidden_arcanus"))?(true):(false);
    private boolean midnight=(Loader.isModLoaded("midnight"))?(true):(false);
    private boolean roots=(Loader.isModLoaded("roots"))?(true):(false);
    private boolean tcon=(Loader.isModLoaded("tconstruct"))?(true):(false);
    private boolean tfspellpack=(Loader.isModLoaded("tfspellpack"))?(true):(false);
    private boolean thaumadditions=(Loader.isModLoaded("thaumadditions"))?(true):(false);
    private boolean betweenlands=(Loader.isModLoaded("thebetweenlands"))?(true):(false);
    private boolean tombstone=(Loader.isModLoaded("tombstone"))?(true):(false);
    private boolean totemic=(Loader.isModLoaded("totemic"))?(true):(false);
    private boolean vampirism=(Loader.isModLoaded("vampirism"))?(true):(false);
    private boolean xat=(Loader.isModLoaded("xat"))?(true):(false);
    private boolean xenos=(Loader.isModLoaded("xreliquary"))?(true):(false);




    private EffectGetter() {
        //()?():()

        //this.addSpellEffect(xRRRBBBYYY, new PotionEffect(MobEffects.STRENGTH));x=1 for good effect x=2 for bad effect otherwise its 0-100% of the mix is which of the three colors

        /*
        *
        * RED & BLUE (NO YELLOW)
        *
         */
        this.addSpellEffect(1100000000, new PotionEffect(MobEffects.STRENGTH)); //100% RED
        this.addSpellEffect(1099001000, new PotionEffect(MobEffects.INSTANT_HEALTH));
        //this.addSpellEffect(1098002000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1097003000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1096004000, new PotionEffect(MobEffects.STRENGTH));
        if(botania){this.addSpellEffect(1095005000, new PotionEffect(Potion.getPotionFromResourceLocation("botania:emptiness")));}
        //this.addSpellEffect(1094006000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1093007000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1092008000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1091009000, new PotionEffect(MobEffects.STRENGTH));
        if(wizardry){this.addSpellEffect(1090010000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:steroid")));}

        //this.addSpellEffect(1089011000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1088012000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1087013000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1086014000, new PotionEffect(MobEffects.STRENGTH));
        if(botania){this.addSpellEffect(1085015000, new PotionEffect(Potion.getPotionFromResourceLocation("botania:allure")));}
        //this.addSpellEffect(1084016000, new PotionEffect(MobEffects.STRENGTH));
        if(botania){this.addSpellEffect(1083017000, new PotionEffect(Potion.getPotionFromResourceLocation("botania:soulcross")));}
        //this.addSpellEffect(1082018000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1081019000, new PotionEffect(MobEffects.STRENGTH));
        if(bloodMagic){this.addSpellEffect(1080020000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:soul_snare")));}

        //this.addSpellEffect(1079021000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1078022000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1077023000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1076024000, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1075025000, new PotionEffect(PotionRegistry.POTION_MAGNETISM));
        //this.addSpellEffect(1074026000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1073027000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1072028000, new PotionEffect(MobEffects.STRENGTH));
        if(botania){this.addSpellEffect(1071029000, new PotionEffect(Potion.getPotionFromResourceLocation("botania:bloodthirst")));}
        if(bloodMagic){this.addSpellEffect(1070030000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:bounce")));}

        if(wizardry){this.addSpellEffect(1069031000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:low_gravity")));}
        //this.addSpellEffect(1068032000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1067033000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1066034000, new PotionEffect(MobEffects.STRENGTH));
        if(naturalPledge){this.addSpellEffect(1065035000, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:immorality")));}
        //this.addSpellEffect(1064036000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1063037000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1062038000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1061039000, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1060040000, new PotionEffect(PotionRegistry.POTION_VOIDSTORAGE));

        //this.addSpellEffect(1059041000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1058042000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1057043000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1056044000, new PotionEffect(MobEffects.STRENGTH));
        if(quark){this.addSpellEffect(1055045000, new PotionEffect(Potion.getPotionFromResourceLocation("quark:resilience")));}
        if(wizardry){this.addSpellEffect(1054046000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:zach_corruption")));}
        //this.addSpellEffect(1053047000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1052048000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1051049000, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1050050000, new PotionEffect(MobEffects.RESISTANCE)); // PURPLE

        //this.addSpellEffect(1049051000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1048052000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1047053000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1046054000, new PotionEffect(MobEffects.STRENGTH));
        if(wizardry){this.addSpellEffect(1045055000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:slippery")));}
        //this.addSpellEffect(1044056000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1043057000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1042058000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1041059000, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1040060000, new PotionEffect(PotionRegistry.POTION_WATERQUICKNESS));

        //this.addSpellEffect(1039061000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1038062000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1037063000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1036064000, new PotionEffect(MobEffects.STRENGTH));
        if(wizardry){this.addSpellEffect(1035065000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:grace")));}
        //this.addSpellEffect(1034066000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1033067000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1032068000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1031069000, new PotionEffect(MobEffects.STRENGTH));
        if(railCraft){this.addSpellEffect(1030070000, new PotionEffect(Potion.getPotionFromResourceLocation("railcraft:creosote")));}

        //this.addSpellEffect(1029071000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1028072000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1027073000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1026074000, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1025075000, new PotionEffect(MobEffects.FIRE_RESISTANCE));
        //this.addSpellEffect(1024076000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1023077000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1022078000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1021079000, new PotionEffect(MobEffects.STRENGTH));
        if(naturalPledge){this.addSpellEffect(1020080000, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:trap_seer")));}

        //this.addSpellEffect(1019081000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1018082000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1017083000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1016084000, new PotionEffect(MobEffects.STRENGTH));
        if(astralSorc){this.addSpellEffect(1015085000, new PotionEffect(Potion.getPotionFromResourceLocation("astralsorcery:potiondropmodifier")));}
        //this.addSpellEffect(1014086000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1013087000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1012088000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1011089000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1010090000, new PotionEffect(MobEffects.STRENGTH));

        //this.addSpellEffect(1009091000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1008092000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1007093000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1006094000, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1005095000, new PotionEffect(MobEffects.LUCK));
        //this.addSpellEffect(1004096000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1003097000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1002098000, new PotionEffect(MobEffects.STRENGTH));
        if(botania){this.addSpellEffect(1001099000, new PotionEffect(Potion.getPotionFromResourceLocation("botania:clear")));}
        this.addSpellEffect(1000100000, new PotionEffect(MobEffects.WATER_BREATHING)); //100% BLUE

        /*
        *
        * RED & YELLOW (NO BLUE)
        *
         */
        //this.addSpellEffect(1100000000, new PotionEffect(MobEffects.STRENGTH)); //100% RED (x2)
        if(xenos){this.addSpellEffect(1099000001, new PotionEffect(Potion.getPotionFromResourceLocation("xreliquary:cure_potion")));}
        //this.addSpellEffect(1098000002, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1097000003, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1096000004, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1095000005, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1094000006, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1093000007, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1092000008, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1091000009, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1090000010, new PotionEffect(MobEffects.HEALTH_BOOST));

        //this.addSpellEffect(1089000011, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1088000012, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1087000013, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1086000014, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1085000015, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1084000016, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1083000017, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1082000018, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1081000019, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1080000020, new PotionEffect(MobEffects.STRENGTH));

        //this.addSpellEffect(1089000011, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1088000012, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1087000013, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1086000014, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1085000015, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1084000016, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1083000017, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1082000018, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1081000019, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1080000020, new PotionEffect(MobEffects.STRENGTH));

        //this.addSpellEffect(1079000021, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1078000022, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1077000023, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1076000024, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1075000025, new PotionEffect(MobEffects.JUMP_BOOST));
        //this.addSpellEffect(1074000026, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1073000027, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1072000028, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1071000029, new PotionEffect(MobEffects.STRENGTH));
        if(wizardry){this.addSpellEffect(1070000030, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:crash")));}

        //this.addSpellEffect(1069000031, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1068000032, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1067000033, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1066000034, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1065000035, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1064000036, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1063000037, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1062000038, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1061000039, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1060000040, new PotionEffect(PotionRegistry.POTION_TILLER));

        //this.addSpellEffect(1059000041, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1058000042, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1057000043, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1056000044, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1055000045, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1054000046, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1053000047, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1052000048, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1051000049, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1050000050, new PotionEffect(MobEffects.HASTE)); //ORANGE

        //this.addSpellEffect(1049000051, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1048000052, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1047000053, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1046000054, new PotionEffect(MobEffects.STRENGTH));
        if(astralSorc){this.addSpellEffect(1045000055, new PotionEffect(Potion.getPotionFromResourceLocation("astralsorcery:potioncheatdeath")));}
        //this.addSpellEffect(1044000056, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1043000057, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1042000058, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1041000059, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1040000060, new PotionEffect(PotionRegistry.POTION_HARVESTER));

        //this.addSpellEffect(1039000061, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1038000062, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1037000063, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1036000064, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1035000065, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1034000066, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1033000067, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1032000068, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1031000069, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1030000070, new PotionEffect(MobEffects.STRENGTH));

        //this.addSpellEffect(1029000071, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1028000072, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1027000073, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1026000074, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1025000075, new PotionEffect(MobEffects.NIGHT_VISION));
        //this.addSpellEffect(1024000076, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1023000077, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1022000078, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1021000079, new PotionEffect(MobEffects.STRENGTH));
        if(rustic){this.addSpellEffect(1020000080, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:ironskin")));}

        //this.addSpellEffect(1019000081, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1018000082, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1017000083, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1016000084, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1015000085, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1014000086, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1013000087, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1012000088, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1011000089, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1010000090, new PotionEffect(MobEffects.ABSORPTION));

        //this.addSpellEffect(1009000091, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1008000092, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1007000093, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1006000094, new PotionEffect(MobEffects.STRENGTH));
        if(rustic){this.addSpellEffect(1005000095, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:full")));}
        //this.addSpellEffect(1004000096, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1003000097, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1002000098, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1001000099, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1000000100, new PotionEffect(MobEffects.SATURATION)); //100% YELLOW


        /*
        *
        * BLUE & YELLOW (NO RED)
        *
         */
        //this.addSpellEffect(1000100000, new PotionEffect(MobEffects.STRENGTH)); //100% BLUE (x2)
        //this.addSpellEffect(1000099001, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000098002, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000097003, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000096004, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000095005, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000094006, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000093007, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000092008, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000091009, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1000090010, new PotionEffect(MobEffects.SPEED));

        //this.addSpellEffect(1000089011, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000088012, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000087013, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000086014, new PotionEffect(MobEffects.STRENGTH));
        if(bloodMagic){this.addSpellEffect(1000085015, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:boost")));}
        //this.addSpellEffect(1000084016, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000083017, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000082018, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000081019, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000080020, new PotionEffect(MobEffects.STRENGTH));

        //this.addSpellEffect(1000079021, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000078022, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000077023, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000076024, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1000075025, new PotionEffect(PotionRegistry.POTION_QUICKNESS));
        //this.addSpellEffect(1000074026, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000073027, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000072028, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000071029, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1000070030, new PotionEffect(PotionRegistry.POTION_STEPASSIST));

        //this.addSpellEffect(1000069031, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000068032, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000067033, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000066034, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000065035, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000064036, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000063037, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000062038, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000061039, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1000060040, new PotionEffect(PotionRegistry.POTION_PLANTER));

        //this.addSpellEffect(1000059041, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000058042, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000057043, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000056044, new PotionEffect(MobEffects.STRENGTH));
        if(rustic){this.addSpellEffect(1000055045, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:wither_ward")));}
        //this.addSpellEffect(1000054046, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000053047, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000052048, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000051049, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1000050050, new PotionEffect(MobEffects.REGENERATION)); //GREEN

        //this.addSpellEffect(1000049051, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000048052, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000047053, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000046054, new PotionEffect(MobEffects.STRENGTH));
        if(rustic){this.addSpellEffect(1000045055, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:magic_resistance")));}
        //this.addSpellEffect(1000044056, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000043057, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000042058, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000041059, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1000040060, new PotionEffect(PotionRegistry.POTION_GROWER));

        //this.addSpellEffect(1000039061, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000038062, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000037063, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000036064, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000035065, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000034066, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000033067, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000032068, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000031069, new PotionEffect(MobEffects.STRENGTH));
        if(wizardry){this.addSpellEffect(1000030070, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:phase")));}

        //this.addSpellEffect(1000029071, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000028072, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000027073, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000026074, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(1000025075, new PotionEffect(MobEffects.INVISIBILITY));
        if(wizardry){this.addSpellEffect(1000024076, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:vanish")));}
        //this.addSpellEffect(1000023077, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000022078, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000021079, new PotionEffect(MobEffects.STRENGTH));
        if(quark){this.addSpellEffect(1000020080, new PotionEffect(Potion.getPotionFromResourceLocation("quark:danger_sight")));}

        //this.addSpellEffect(1000019081, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000018082, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000017083, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000016084, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000015085, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000014086, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000013087, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000012088, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000011089, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000010090, new PotionEffect(MobEffects.STRENGTH));

        //this.addSpellEffect(1000009091, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000008092, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000007093, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000006094, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000005095, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000004096, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000003097, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(1000002098, new PotionEffect(MobEffects.STRENGTH));
        if(xenos){this.addSpellEffect(1000001099, new PotionEffect(Potion.getPotionFromResourceLocation("xreliquary:pacification_potion")));}
        //this.addSpellEffect(1000000100, new PotionEffect(MobEffects.STRENGTH)); // 100% YELLOW (x2)

        /*
        *
        * RED BLUE AND YELLOW
        *
         */
        if(bloodMagic){this.addSpellEffect(1033033033, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:flight")));}
        else if(xenos){this.addSpellEffect(1033033033, new PotionEffect(Potion.getPotionFromResourceLocation("xreliquary:flight_potion")));}
        else{this.addSpellEffect(1033033033, new PotionEffect(PotionRegistry.POTION_SLOWFALL));}

        this.addSpellEffect(1050025025, new PotionEffect(PotionRegistry.POTION_SLOWFALL));

        if(rustic){this.addSpellEffect(1025050025, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:feather")));}
        else{this.addSpellEffect(1025050025, new PotionEffect(PotionRegistry.POTION_SLOWFALL));}

        if(botania){this.addSpellEffect(1025025050, new PotionEffect(Potion.getPotionFromResourceLocation("botania:featherfeet")));}
        else{this.addSpellEffect(1025025050, new PotionEffect(PotionRegistry.POTION_SLOWFALL));}



        /*
        *
        * NEGATIVE EFFECTS
        *
         */



        /*
        *
        * RED & BLUE (NO YELLOW)
        *
         */
        this.addSpellEffect(2100000000, new PotionEffect(MobEffects.WEAKNESS)); //100% RED
        this.addSpellEffect(2099001000, new PotionEffect(MobEffects.INSTANT_DAMAGE));
        //this.addSpellEffect(2098002000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2097003000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2096004000, new PotionEffect(MobEffects.STRENGTH));
        if(bloodMagic){this.addSpellEffect(2095005000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:sacrificial_lamb")));}
        //this.addSpellEffect(2094006000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2093007000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2092008000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2091009000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2090010000, new PotionEffect(MobEffects.STRENGTH));

        //this.addSpellEffect(2089011000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2088012000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2087013000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2086014000, new PotionEffect(MobEffects.STRENGTH));
        if(astralSorc){this.addSpellEffect(2085015000, new PotionEffect(Potion.getPotionFromResourceLocation("astralsorcery:potionspellplague")));}
        //this.addSpellEffect(2084016000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2083017000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2082018000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2081019000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2080020000, new PotionEffect(MobEffects.STRENGTH));

        //this.addSpellEffect(2079021000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2078022000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2077023000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2076024000, new PotionEffect(MobEffects.STRENGTH));
        if(bloodMagic){this.addSpellEffect(2075025000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:cling")));}
        //this.addSpellEffect(2074026000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2073027000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2072028000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2071029000, new PotionEffect(MobEffects.STRENGTH));
        if(champions){this.addSpellEffect(2070030000, new PotionEffect(Potion.getPotionFromResourceLocation("champions:jailed")));}

        //this.addSpellEffect(2069031000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2068032000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2067033000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2066034000, new PotionEffect(MobEffects.STRENGTH));
        if(wizardry){this.addSpellEffect(2065035000, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:nullify_gravity")));}
        //this.addSpellEffect(2064036000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2063037000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2062038000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2061039000, new PotionEffect(MobEffects.STRENGTH));
        if(astralSorc){this.addSpellEffect(2060040000, new PotionEffect(Potion.getPotionFromResourceLocation("astralsorcery:potionbleed")));}

        //this.addSpellEffect(2059041000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2058042000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2057043000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2056044000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2055045000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2054046000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2053047000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2052048000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2051049000, new PotionEffect(MobEffects.STRENGTH));
        if(champions){this.addSpellEffect(2050050000, new PotionEffect(Potion.getPotionFromResourceLocation("champions:injured")));}

        //this.addSpellEffect(2049051000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2048052000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2047053000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2046054000, new PotionEffect(MobEffects.STRENGTH));
        if(naturalPledge){this.addSpellEffect(2045055000, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:rooted")));}
        //this.addSpellEffect(2044056000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2043057000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2042058000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2041059000, new PotionEffect(MobEffects.STRENGTH));
        if(thaumcraft){this.addSpellEffect(2040060000, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:sunscorned")));}

        //this.addSpellEffect(2039061000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2038062000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2037063000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2036064000, new PotionEffect(MobEffects.STRENGTH));
        if(bloodMagic){this.addSpellEffect(2035065000, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:fire_fuse")));}
        //this.addSpellEffect(2034066000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2033067000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2032068000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2031069000, new PotionEffect(MobEffects.STRENGTH));
        if(immersiveE){this.addSpellEffect(2030070000, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:conductive")));}

        //this.addSpellEffect(2029071000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2028072000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2027073000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2026074000, new PotionEffect(MobEffects.STRENGTH));
        if(immersiveE){this.addSpellEffect(2025075000, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:flammable")));}
        //this.addSpellEffect(2024076000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2023077000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2022078000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2021079000, new PotionEffect(MobEffects.STRENGTH));
        if(naturalPledge){this.addSpellEffect(2020080000, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:everburn")));}

        //this.addSpellEffect(2019081000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2018082000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2017083000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2016084000, new PotionEffect(MobEffects.STRENGTH));
        if(naturalPledge){this.addSpellEffect(2015085000, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:faithlessness")));}
        //this.addSpellEffect(2014086000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2013087000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2012088000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2011089000, new PotionEffect(MobEffects.STRENGTH));
        if(rustic){this.addSpellEffect(2010090000, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:blazing_trail")));}

        //this.addSpellEffect(2009091000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2008092000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2007093000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2006094000, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(2005095000, new PotionEffect(MobEffects.UNLUCK));
        //this.addSpellEffect(2004096000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2003097000, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2002098000, new PotionEffect(MobEffects.STRENGTH));
        if(quark){this.addSpellEffect(2001099000, new PotionEffect(Potion.getPotionFromResourceLocation("quark:blue")));}
        this.addSpellEffect(2000100000, new PotionEffect(PotionRegistry.POTION_DROWNING)); //100% BLUE

        /*
        *
        * RED & YELLOW (NO BLUE)
        *
         */
        //this.addSpellEffect(2100000000, new PotionEffect(MobEffects.STRENGTH)); //100% RED (x2)
        //this.addSpellEffect(2099000001, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2098000002, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2097000003, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2096000004, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2095000005, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2094000006, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2093000007, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2092000008, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2091000009, new PotionEffect(MobEffects.STRENGTH));
        if(quark){this.addSpellEffect(2090000010, new PotionEffect(Potion.getPotionFromResourceLocation("quark:curse")));}

        //this.addSpellEffect(2089000011, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2088000012, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2087000013, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2086000014, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2085000015, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2084000016, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2083000017, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2082000018, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2081000019, new PotionEffect(MobEffects.STRENGTH));
        if(bloodMagic){this.addSpellEffect(2080000020, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:soul_fray")));}

        //this.addSpellEffect(2089000011, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2088000012, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2087000013, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2086000014, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2085000015, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2084000016, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2083000017, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2082000018, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2081000019, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2080000020, new PotionEffect(MobEffects.STRENGTH));

        //this.addSpellEffect(2079000021, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2078000022, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2077000023, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2076000024, new PotionEffect(MobEffects.STRENGTH));
        if(astralSorc){this.addSpellEffect(2075000025, new PotionEffect(Potion.getPotionFromResourceLocation("astralsorcery:potiontimefreeze")));}
        //this.addSpellEffect(2074000026, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2073000027, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2072000028, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2071000029, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2070000030, new PotionEffect(MobEffects.STRENGTH));

        //this.addSpellEffect(2069000031, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2068000032, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2067000033, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2066000034, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2065000035, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2064000036, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2063000037, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2062000038, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2061000039, new PotionEffect(MobEffects.STRENGTH));
        if(rustic){this.addSpellEffect(2060000040, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:tipsy")));}

        //this.addSpellEffect(2059000041, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2058000042, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2057000043, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2056000044, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2055000045, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2054000046, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2053000047, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2052000048, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2051000049, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(2050000050, new PotionEffect(MobEffects.MINING_FATIGUE)); //ORANGE

        //this.addSpellEffect(2049000051, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2048000052, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2047000053, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2046000054, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2045000055, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2044000056, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2043000057, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2042000058, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2041000059, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2040000060, new PotionEffect(MobEffects.STRENGTH));

        //this.addSpellEffect(2039000061, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2038000062, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2037000063, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2036000064, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2035000065, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2034000066, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2033000067, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2032000068, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2031000069, new PotionEffect(MobEffects.STRENGTH));
        if(bloodMagic){this.addSpellEffect(2030000070, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:planar_binding")));}

        //this.addSpellEffect(2029000071, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2028000072, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2027000073, new PotionEffect(MobEffects.STRENGTH));
        if(naturalPledge){this.addSpellEffect(2026000074, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:drab")));}
        this.addSpellEffect(2025000075, new PotionEffect(MobEffects.BLINDNESS));
        //this.addSpellEffect(2024000076, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2023000077, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2022000078, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2021000079, new PotionEffect(MobEffects.STRENGTH));
        if(naturalPledge){this.addSpellEffect(2020000080, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:overcharged")));}

        //this.addSpellEffect(2019000081, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2018000082, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2017000083, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2016000084, new PotionEffect(MobEffects.STRENGTH));
        if(thaumcraft){this.addSpellEffect(2015000085, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:thaumarhia")));}
        //this.addSpellEffect(2014000086, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2013000087, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2012000088, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2011000089, new PotionEffect(MobEffects.STRENGTH));
        if(thaumcraft){this.addSpellEffect(2010000090, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:unnaturalhunger")));}

        //this.addSpellEffect(2009000091, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2008000092, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2007000093, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2006000094, new PotionEffect(MobEffects.STRENGTH));
    if(rustic){this.addSpellEffect(2005000095, new PotionEffect(Potion.getPotionFromResourceLocation("rustic:shame")));}
        //this.addSpellEffect(2004000096, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2003000097, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2002000098, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2001000099, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(2000000100, new PotionEffect(MobEffects.HUNGER)); //100% YELLOW


        /*
        *
        * BLUE & YELLOW (NO RED)
        *
         */
        //this.addSpellEffect(2000100000, new PotionEffect(MobEffects.STRENGTH)); //100% BLUE (x2)
        //this.addSpellEffect(2000099001, new PotionEffect(MobEffects.STRENGTH));
        if(wizardry){this.addSpellEffect(2000098002, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:suffocate")));}
        //this.addSpellEffect(2000097003, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000096004, new PotionEffect(MobEffects.STRENGTH));
        if(naturesAura){this.addSpellEffect(2000095005, new PotionEffect(Potion.getPotionFromResourceLocation("naturesaura:breathless")));}
        //this.addSpellEffect(2000094006, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000093007, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000092008, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000091009, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(2000090010, new PotionEffect(MobEffects.SLOWNESS));

        //this.addSpellEffect(2000089011, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000088012, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000087013, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000086014, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000085015, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000084016, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000083017, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000082018, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000081019, new PotionEffect(MobEffects.STRENGTH));
        if(wizardry){this.addSpellEffect(2000080020, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:time_slow")));}

        //this.addSpellEffect(2000079021, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000078022, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000077023, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000076024, new PotionEffect(MobEffects.STRENGTH));
        if(immersiveE){this.addSpellEffect(2000075025, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:slippery")));}
        //this.addSpellEffect(2000074026, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000073027, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000072028, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000071029, new PotionEffect(MobEffects.STRENGTH));
        if(immersiveE){this.addSpellEffect(2000070030, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:sticky")));}

        //this.addSpellEffect(2000069031, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000068032, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000067033, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000066034, new PotionEffect(MobEffects.STRENGTH));
        if(twilightForest){this.addSpellEffect(2000065035, new PotionEffect(Potion.getPotionFromResourceLocation("twilightforest:frosted")));}
        //this.addSpellEffect(2000064036, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000063037, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000062038, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000061039, new PotionEffect(MobEffects.STRENGTH));
        if(immersiveE){this.addSpellEffect(2000060040, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:stunned")));}

        //this.addSpellEffect(2000059041, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000058042, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000057043, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000056044, new PotionEffect(MobEffects.STRENGTH));
        if(immersiveE){this.addSpellEffect(2000055045, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:flashed")));}
        //this.addSpellEffect(2000054046, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000053047, new PotionEffect(MobEffects.STRENGTH));
        if(champions){this.addSpellEffect(2000052048, new PotionEffect(Potion.getPotionFromResourceLocation("champions:plague")));}
        //this.addSpellEffect(2000051049, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(2000050050, new PotionEffect(MobEffects.WITHER)); //GREEN

        //this.addSpellEffect(2000049051, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000048052, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000047053, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000046054, new PotionEffect(MobEffects.STRENGTH));
        if(bloodMagic){this.addSpellEffect(2000045055, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:plant_leach")));}
        //this.addSpellEffect(2000044056, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000043057, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000042058, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000041059, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(2000040060, new PotionEffect(MobEffects.POISON));

        //this.addSpellEffect(2000039061, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000038062, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000037063, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000036064, new PotionEffect(MobEffects.STRENGTH));
        if(bloodMagic){this.addSpellEffect(2000035065, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:deafness")));}
        //this.addSpellEffect(2000034066, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000033067, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000032068, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000031069, new PotionEffect(MobEffects.STRENGTH));
        if(thaumcraft){this.addSpellEffect(2000030070, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:blurredvision")));}

        //this.addSpellEffect(2000029071, new PotionEffect(MobEffects.STRENGTH));
        if(thaumcraft){this.addSpellEffect(2000028072, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:fluxtaint")));}
        //this.addSpellEffect(2000027073, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000026074, new PotionEffect(MobEffects.STRENGTH));
        this.addSpellEffect(2000025075, new PotionEffect(MobEffects.GLOWING));
        //this.addSpellEffect(2000024076, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000023077, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000022078, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000021079, new PotionEffect(MobEffects.STRENGTH));
        if(thaumcraft){this.addSpellEffect(2000020080, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:deathgaze")));}

        //this.addSpellEffect(2000019081, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000018082, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000017083, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000016084, new PotionEffect(MobEffects.STRENGTH));
        if(bloodMagic){this.addSpellEffect(2000015085, new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:constrict")));}
        //this.addSpellEffect(2000014086, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000013087, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000012088, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000011089, new PotionEffect(MobEffects.STRENGTH));
        if(thaumcraft){this.addSpellEffect(2000010090, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:infectiousvisexhaust")));}

        //this.addSpellEffect(2000009091, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000008092, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000007093, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000006094, new PotionEffect(MobEffects.STRENGTH));
        if(thaumcraft){this.addSpellEffect(2000005095, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:warpward")));}
        if(thaumcraft){this.addSpellEffect(2000004096, new PotionEffect(Potion.getPotionFromResourceLocation("thaumcraft:visexhaust")));}
        //this.addSpellEffect(2000003097, new PotionEffect(MobEffects.STRENGTH));
        //this.addSpellEffect(2000002098, new PotionEffect(MobEffects.STRENGTH));
        if(quark){this.addSpellEffect(2000001099, new PotionEffect(Potion.getPotionFromResourceLocation("quark:black")));}
        //this.addSpellEffect(2000000100, new PotionEffect(MobEffects.STRENGTH)); // 100% YELLOW (x2)

        /*
        *
        * RED BLUE AND YELLOW
        *
         */

        this.addSpellEffect(2033033033, new PotionEffect(MobEffects.LEVITATION)); // 100% BLACK
        this.addSpellEffect(2050025025, new PotionEffect(PotionRegistry.POTION_PETRIFIED));
        if(quark){this.addSpellEffect(2025050025, new PotionEffect(Potion.getPotionFromResourceLocation("quark:white")));}
        else{this.addSpellEffect(2025050025, new PotionEffect(PotionRegistry.POTION_PETRIFIED));}
        if(naturalPledge){this.addSpellEffect(2025025050, new PotionEffect(Potion.getPotionFromResourceLocation("naturalpledge:featherweight")));}
        else if(wizardry){this.addSpellEffect(2025025050, new PotionEffect(Potion.getPotionFromResourceLocation("wizardry:null_movement")));}
        else{this.addSpellEffect(2025025050, new PotionEffect(PotionRegistry.POTION_PETRIFIED));}
    }

    /*
    SUPPORT TO ADD

abyssalcraft:cplague,
abyssalcraft:dplague,

aov:aid,
aov:balance,
aov:coldchill,
aov:ewer,
aov:faith,
aov:naturesbounty,
aov:slowfall,
aov:spear,
aov:spire,
aov:stalwart,
aov:zeal,

bewitchment:absence,
bewitchment:arachnophobia,
bewitchment:blight,
bewitchment:citrinitas,
bewitchment:corruption,
bewitchment:cursed_leaps,
bewitchment:deflection,
bewitchment:desertification,
bewitchment:deviants_decomposure,
bewitchment:disrobing,
bewitchment:fear,
bewitchment:fertility,
bewitchment:grace,
bewitchment:harvest,
bewitchment:hellfire,
bewitchment:hellworld,
bewitchment:holy_water,
bewitchment:iceworld,
bewitchment:magic_resistance,
bewitchment:magic_weakness,
bewitchment:mending,
bewitchment:mortal_coil,
bewitchment:pesticide,
bewitchment:purification,
bewitchment:revealing,
bewitchment:rubedo,
bewitchment:ruin,
bewitchment:shell_armor,
bewitchment:sinking,
bewitchment:sleeping,
bewitchment:spore_cloud,
bewitchment:volatility,
bewitchment:wednesday,
bewitchment:wolfsbane,

biomesoplenty:curse,

conarm:superhotpotion,

ebwizardry:arcane_jammer,
ebwizardry:containment,
ebwizardry:curse_of_enfeeblement,
ebwizardry:curse_of_soulbinding,
ebwizardry:curse_of_undeath,
ebwizardry:decay,
ebwizardry:empowerment,
ebwizardry:fear,
ebwizardry:fireskin,
ebwizardry:font_of_mana,
ebwizardry:frost,
ebwizardry:frost_step,
ebwizardry:ice_shroud,
ebwizardry:mind_control,
ebwizardry:mind_trick,
ebwizardry:muffle,
ebwizardry:paralysis,
ebwizardry:sixth_sense,
ebwizardry:slow_time,
ebwizardry:static_aura,
ebwizardry:transience,
ebwizardry:ward,

extra_spells:leeching,
extra_spells:vampirism,

forbidden_arcanus:fly,
forbidden_arcanus:spectral_vision,

midnight:confusion,
midnight:darkness,
midnight:dragon_guard,
midnight:pollinated,
midnight:stunned,
midnight:tormented,
midnight:unstable_fall,

roots:animal_sense,
roots:danger_sense,
roots:freeze,
roots:geas,
roots:invulnerability,
roots:petal_shell,
roots:time_stop,

tconstruct:carry,
tconstruct:dot,
tconstruct:enderference,
tconstruct:insatiable,
tconstruct:magnetic,
tconstruct:momentum,
tconstruct:splinter,

tfspellpack:ironwood_heart,
tfspellpack:mist_cloak,

thaumadditions:sanity_checker,
thaumadditions:sonus,

thebetweenlands:effect_basilisk,
thebetweenlands:effect_blindman,
thebetweenlands:effect_catseyes,
thebetweenlands:effect_decay,
thebetweenlands:effect_deformed,
thebetweenlands:effect_draining,
thebetweenlands:effect_drunkard,
thebetweenlands:effect_feasting,
thebetweenlands:effect_foggedmind,
thebetweenlands:effect_gillsgrowth,
thebetweenlands:effect_healing,
thebetweenlands:effect_heavyweight,
thebetweenlands:effect_hunterssense,
thebetweenlands:effect_hunterssensemaster,
thebetweenlands:effect_isolatedsenses,
thebetweenlands:effect_lightweight,
thebetweenlands:effect_limbless,
thebetweenlands:effect_lumbering,
thebetweenlands:effect_masking,
thebetweenlands:effect_nimblefeet,
thebetweenlands:effect_petrify,
thebetweenlands:effect_poisonsting,
thebetweenlands:effect_ripening,
thebetweenlands:effect_sagittarius,
thebetweenlands:effect_slugarm,
thebetweenlands:effect_spiderbreed,
thebetweenlands:effect_starvation,
thebetweenlands:effect_stenching,
thebetweenlands:effect_strength,
thebetweenlands:effect_swiftarm,
thebetweenlands:effect_toughskin,
thebetweenlands:effect_unclouded,
thebetweenlands:effect_weakbow,
thebetweenlands:effect_weakness,
thebetweenlands:effect_wings,
thebetweenlands:enlightened,
thebetweenlands:root_bound,

tombstone:diversion,
tombstone:exorcism,
tombstone:feather_fall,
tombstone:ghostly_shape,
tombstone:preservation,
tombstone:purification,
tombstone:reach,
tombstone:true_sight,
tombstone:unstable_intangibleness,

totemic:bat,
totemic:spider,

vampirism:disguise_as_vampire,
vampirism:fire_protection,
vampirism:freeze,
vampirism:garlic,
vampirism:poison,
vampirism:sanguinare,
vampirism:saturation,
vampirism:sunscreen,
vampirism:thirst,

xat:dwarf,
xat:enhanced,
xat:fairy,
xat:restorative,

     */


    public void addSpellEffect(int colorIn, PotionEffect effectOut)
    {
        if(colorIn>=1000000000)
        {
            this.effectList.put(colorIn,new EffectHashMap(colorIn,effectOut));;
        }
        else return;
    }

    public boolean hasPotionEffect(int getColorIn)
    {
        for (Map.Entry<Integer, EffectHashMap> entry : this.effectList.entrySet())
        {
            if (getColorIn == entry.getKey())
            {
                return true;
            }
        }

        return false;
    }

    public PotionEffect getPotionEffect(int getColorIn)
    {
        for (Map.Entry<Integer, EffectHashMap> entry : this.effectList.entrySet())
        {
            if (getColorIn == entry.getKey())
            {
                return entry.getValue().getOutput();
            }
        }

        return null;
    }
}
