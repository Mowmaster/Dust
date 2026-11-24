package com.mowmaster.dust.Features.Trees;

import com.mowmaster.dust.DustReferences;
import net.minecraft.util.StringRepresentable;

// The charcoals the elemental woods burn down to, by the colour of their wood as if it were the colour of a flame's heat:
// the hotter the colour, the longer it burns (ticks; coal and vanilla charcoal are 1600, the white).
//   dark red 200, cherry red 400, orange 800 (a wood whose colour isn't a flame's), yellow 1200, white 1600,
//   blue 3200, violet 6400
// Items <name>_charcoal (ElementalWoods.CHARCOAL); their burn times are datapack entries (26.3:
// dust:cooking/time_<name>_charcoal, DustWorldgen.charcoalFuels; 26.1.2: the furnace fuel data map).
public enum Charcoal implements StringRepresentable
{
    DARK_RED("dark_red", 200),
    CHERRY_RED("cherry_red", 400),
    ORANGE("orange", 800),
    YELLOW("yellow", 1200),
    WHITE("white", 1600),
    BLUE("blue", 3200),
    VIOLET("violet", 6400);

    private final String name;
    public final int burnTime;

    Charcoal(String name, int burnTime)
    {
        this.name = name;
        this.burnTime = burnTime;
    }

    public String itemName()
    {
        return name + "_charcoal";
    }

    //? if >=26.3 {
    // Its burn time entry, dust:cooking/time_<name>_charcoal (made in datagen: DustWorldgen.charcoalFuels)
    public net.minecraft.resources.ResourceKey<net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider> fuelKey()
    {
        return net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.CONTEXT_INT_PROVIDER,
                net.minecraft.resources.Identifier.fromNamespaceAndPath(DustReferences.MODID, "cooking/time_" + itemName()));
    }
    //?}

    @Override
    public String getSerializedName()
    {
        return name;
    }
}
