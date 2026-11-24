package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.Features.Trees.ElementalWood;
import com.mowmaster.dust.Features.Trees.WoodSet;
import net.minecraft.data.BlockFamily;

import java.util.EnumMap;
import java.util.Map;

// Each elemental wood's planks family (vanilla's BlockFamily, as BlockFamilies makes oak's): its models
// (DustProviderModel) and its crafting recipes (DustProviderBasicRecipe) are made from it as vanilla's woods' are
final class DustWoodFamilies
{
    private static final Map<ElementalWood, BlockFamily> FAMILIES = new EnumMap<>(ElementalWood.class);

    private DustWoodFamilies() {}

    static synchronized BlockFamily of(WoodSet set)
    {
        return FAMILIES.computeIfAbsent(set.type(), wood -> new BlockFamily.Builder(set.planks().get())
                .button(set.button().get())
                .fence(set.fence().get())
                .fenceGate(set.fenceGate().get())
                .pressurePlate(set.pressurePlate().get())
                .slab(set.slab().get())
                .stairs(set.stairs().get())
                .recipeGroupPrefix("wooden")
                .recipeUnlockedBy("has_planks")
                .getFamily());
    }
}
