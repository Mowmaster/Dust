package com.mowmaster.dust;

import com.mowmaster.mowlib.client.HudAnchor;
import net.neoforged.neoforge.common.ModConfigSpec;

// Dust's settings for this player's screen only (config/dust-client.toml, never sent to servers). Registered in
// DustClient; editable in game from Mods > Dust > Config.
// [research_book.main_hand] / [research_book.offhand]: where the held research book is drawn and how big.
public final class DustClientConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.EnumValue<HudAnchor> MAIN_ANCHOR;
    public static final ModConfigSpec.IntValue MAIN_X;
    public static final ModConfigSpec.IntValue MAIN_Y;
    public static final ModConfigSpec.IntValue MAIN_SCALE;
    public static final ModConfigSpec.EnumValue<HudAnchor> OFF_ANCHOR;
    public static final ModConfigSpec.IntValue OFF_X;
    public static final ModConfigSpec.IntValue OFF_Y;
    public static final ModConfigSpec.IntValue OFF_SCALE;

    static {
        BUILDER.comment("The Research Notes / Shared Research Guide shown on screen while held").push("research_book");

        BUILDER.comment("Held in the main hand (like a map)").push("main_hand");
        MAIN_ANCHOR = BUILDER.comment("Which part of the screen it's pinned to").defineEnum("anchor", HudAnchor.BOTTOM_CENTER);
        MAIN_X = BUILDER.comment("Move right (+) or left (-)").defineInRange("x", 0, -2000, 2000);
        MAIN_Y = BUILDER.comment("Move down (+) or up (-)").defineInRange("y", -28, -2000, 2000);
        MAIN_SCALE = BUILDER.comment("Size in percent").defineInRange("scale", 55, 20, 200);
        BUILDER.pop();

        BUILDER.comment("Held in the offhand").push("offhand");
        OFF_ANCHOR = BUILDER.comment("Which part of the screen it's pinned to").defineEnum("anchor", HudAnchor.MIDDLE_RIGHT);
        OFF_X = BUILDER.comment("Move right (+) or left (-)").defineInRange("x", 0, -2000, 2000);
        OFF_Y = BUILDER.comment("Move down (+) or up (-)").defineInRange("y", 0, -2000, 2000);
        OFF_SCALE = BUILDER.comment("Size in percent").defineInRange("scale", 45, 20, 200);
        BUILDER.pop();

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    private DustClientConfig() {}
}
