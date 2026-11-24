package com.mowmaster.dust.Features.Trees;

import com.mowmaster.dust.DustReferences;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
//? if >=26.3 {
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.levelgen.feature.Feature;
//?} else {
/*import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.Optional;
*///?}

// Each elemental wood's tree: the worldgen entry dust:<wood>_tree (26.3: a feature made in datagen, DustWorldgen.trees;
// 26.1.2: a configured feature, src/main/resources-26.1.2/data/dust/worldgen/configured_feature/<wood>_tree.json) that
// its sapling grows (TreeGrower) and that Features/Worldgen/ElementalTreeFeature places in the wild.
// Stonecutter: 26.3 keeps trees in the "worldgen/feature" registry and a TreeGrower picks from weighted lists; 26.1.2
// keeps them in "worldgen/configured_feature" and a TreeGrower takes Optionals.
public final class ElementalTrees
{
    private ElementalTrees() {}

    public static Identifier treeId(ElementalWood wood)
    {
        return Identifier.fromNamespaceAndPath(DustReferences.MODID, wood.id + "_tree");
    }

    //? if >=26.3 {
    public static ResourceKey<Feature> treeKey(ElementalWood wood)
    {
        return ResourceKey.create(Registries.FEATURE, treeId(wood));
    }

    public static TreeGrower grower(ElementalWood wood)
    {
        return new TreeGrower(DustReferences.MODID + ":" + wood.id, WeightedList.of(treeKey(wood)), WeightedList.of(), WeightedList.of(), treeKey(wood));
    }
    //?} else {
    /*public static ResourceKey<ConfiguredFeature<?, ?>> treeKey(ElementalWood wood)
    {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, treeId(wood));
    }

    public static TreeGrower grower(ElementalWood wood)
    {
        return new TreeGrower(DustReferences.MODID + ":" + wood.id, Optional.empty(), Optional.of(treeKey(wood)), Optional.empty());
    }
    *///?}
}
