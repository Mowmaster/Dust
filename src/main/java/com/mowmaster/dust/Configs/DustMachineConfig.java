package com.mowmaster.dust.Configs;

import com.mowmaster.dust.Dust;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class DustMachineConfig
{
    public static class Common {
        public final ForgeConfigSpec.IntValue dustPerCrystal;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Machine Settings").push("Machines");

            dustPerCrystal = builder
                    .comment("Dust Return per Crushed Crystal")
                    .defineInRange("dustPerCrystal", 1, 0, Integer.MAX_VALUE);
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
