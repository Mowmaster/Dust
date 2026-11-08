package com.mowmaster.dust;

import com.mowmaster.mowlib.client.HudAnchor;
import net.neoforged.neoforge.common.ModConfigSpec;

// Dust's settings for this player's screen only (config/dust-client.toml, never sent to servers). Registered in
// DustClient; editable in game from Mods > Dust > Config.
// [research_book]: where the held research book is drawn and how big.
public final class DustClientConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.EnumValue<HudAnchor> BOOK_ANCHOR;
    public static final ModConfigSpec.IntValue BOOK_X;
    public static final ModConfigSpec.IntValue BOOK_Y;
    public static final ModConfigSpec.IntValue BOOK_SCALE;

    static {
        BUILDER.comment("The Research Notes / Shared Research Guide, shown at the side of the screen while held in either hand").push("research_book");
        BOOK_ANCHOR = BUILDER.comment("Which part of the screen it's pinned to").defineEnum("anchor", HudAnchor.MIDDLE_LEFT);
        BOOK_X = BUILDER.comment("Move right (+) or left (-)").defineInRange("x", 0, -2000, 2000);
        BOOK_Y = BUILDER.comment("Move down (+) or up (-)").defineInRange("y", 0, -2000, 2000);
        BOOK_SCALE = BUILDER.comment("Size in percent").defineInRange("scale", 45, 20, 200);
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    private DustClientConfig() {}
}
