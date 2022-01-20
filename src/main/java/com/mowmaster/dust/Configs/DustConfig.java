package com.mowmaster.dust.Configs;

import com.mowmaster.dust.Dust;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class DustConfig
{
    public static class Common {
        public final ForgeConfigSpec.BooleanValue generate333;
        public final ForgeConfigSpec.BooleanValue generate000;
        public final ForgeConfigSpec.BooleanValue generate300;
        public final ForgeConfigSpec.BooleanValue generate030;
        public final ForgeConfigSpec.BooleanValue generate003;
        public final ForgeConfigSpec.BooleanValue generateGeodeRandom;

        public final ForgeConfigSpec.IntValue geodeRarity333;
        public final ForgeConfigSpec.IntValue geodeRarity000;
        public final ForgeConfigSpec.IntValue geodeRarity300;
        public final ForgeConfigSpec.IntValue geodeRarity030;
        public final ForgeConfigSpec.IntValue geodeRarity003;
        public final ForgeConfigSpec.IntValue geodeRarityGeodeRandom;

        public final ForgeConfigSpec.IntValue effectMaxPotency;

        public final ForgeConfigSpec.IntValue dustPerCrystal;
        public final ForgeConfigSpec.IntValue repairitemsToCraft_ScrollCrafter_T15;
        public final ForgeConfigSpec.IntValue repairitemsToCraft_BlastFurnace_T15;
        public final ForgeConfigSpec.IntValue repairitemsToCraft_Smoker_T15;
        public final ForgeConfigSpec.IntValue repairitemsToCraft_Smelter_T15;

        public final ForgeConfigSpec.IntValue normalEffectTicksDurationPerDust;
        public final ForgeConfigSpec.IntValue instaEffectDustPerEffectBurst;


        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Dust World Generation Settings")
                    .push("Generation");

            generate333 = builder.comment("Generate Geode (White) [Default: true]").define("generate333", true);

            generate000 = builder.comment("Generate Geode (Black) [Default: true]").define("generate000", true);

            generate300 = builder.comment("Generate Geode (Red) [Default: true]").define("generate300", true);

            generate030 = builder.comment("Generate Geode (Green) [Default: true]").define("generate030", true);

            generate003 = builder.comment("Generate Geode (Blue) [Default: true]").define("generate003", true);

            generateGeodeRandom = builder.comment("Generate Geode (Random Color) [Default: true]").define("generateGeodeRandom", true);


            builder.pop();
            builder.comment("Rarity settings").push("Rarity");

            geodeRarity333 = builder
                    .comment("Geode (White) Rarity [Default: 300] (The higher the value the rarer)")
                    .defineInRange("geodeRarity333", 300, 0, Integer.MAX_VALUE);

            geodeRarity000 = builder
                    .comment("Geode (Black) Rarity [Default: 300] (The higher the value the rarer)")
                    .defineInRange("geodeRarity000", 300, 0, Integer.MAX_VALUE);

            geodeRarity300 = builder
                    .comment("Geode (Red) Rarity [Default: 180] (The higher the value the rarer)")
                    .defineInRange("geodeRarity300", 180, 0, Integer.MAX_VALUE);

            geodeRarity030 = builder
                    .comment("Geode (Green) Rarity [Default: 180] (The higher the value the rarer)")
                    .defineInRange("geodeRarity030", 180, 0, Integer.MAX_VALUE);

            geodeRarity003 = builder
                    .comment("Geode (Blue) Rarity [Default: 180] (The higher the value the rarer)")
                    .defineInRange("geodeRarity003", 180, 0, Integer.MAX_VALUE);

            geodeRarityGeodeRandom = builder
                    .comment("Geode (Random Color) Rarity [Default: 600] (The higher the value the rarer)")
                    .defineInRange("geodeRarityGeodeRandom", 600, 0, Integer.MAX_VALUE);

            builder.pop();

            builder.comment("Potency Setting").push("Potency");

            effectMaxPotency = builder
                    .comment("Max Effect Potency")
                    .defineInRange("maxPotency", 5, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.comment("Machine Settings").push("Machines");

            dustPerCrystal = builder
                    .comment("Dust Return per Crushed Crystal")
                    .defineInRange("dustPerCrystal", 1, 0, Integer.MAX_VALUE);
            repairitemsToCraft_ScrollCrafter_T15 = builder
                    .comment("Repair Items Needed To Craft the Scroll Crafter Tier 1.5")
                    .defineInRange("repairItemsNeeded_scrollcrafter_t15", 3, 1, Integer.MAX_VALUE);
            repairitemsToCraft_BlastFurnace_T15 = builder
                    .comment("Repair Items Needed To Craft the Blast Furnace Tier 1.5")
                    .defineInRange("repairItemsNeeded_blastfurnace_t15", 2, 1, Integer.MAX_VALUE);
            repairitemsToCraft_Smoker_T15 = builder
                    .comment("Repair Items Needed To Craft the Smoker Tier 1.5")
                    .defineInRange("repairItemsNeeded_smoker_t15", 1, 1, Integer.MAX_VALUE);
            repairitemsToCraft_Smelter_T15 = builder
                    .comment("Repair Items Needed To Craft the Smelter Tier 1.5")
                    .defineInRange("repairItemsNeeded_smelter_t15", 2, 1, Integer.MAX_VALUE);



            normalEffectTicksDurationPerDust = builder
                    .comment("Ticks Of Duration for Normal Effects Per Dust Used")
                    .defineInRange("normalEffectTicksDurationPerDust", 20, 1, Integer.MAX_VALUE);
            instaEffectDustPerEffectBurst = builder
                    .comment("Dust Need per Burst of an Instant Effect")
                    .defineInRange("instaEffectDustPerEffectBurst", 16, 1, Integer.MAX_VALUE);
            builder.pop();
        }
    }

    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {
        Dust.LOGGER.debug("Loaded Dust's config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
        Dust.LOGGER.debug("Dust's config just got changed on the file system!");
    }
}
