package com.mowmaster.dust.Features.Sanctums;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Trees.ElementalWood;
import com.mowmaster.dust.Features.Trees.ElementalWoods;
import com.mowmaster.dust.Features.Trees.WoodSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// Turns a sanctum template's stand-in blocks into its element's (SANCTUMS.md lists them):
//   white concrete        the palette's wall (one in five its second wall block)
//   light gray concrete   the palette's second wall block
//   gray concrete         the palette's floor
//   cyan concrete         the element's crystal block
//   yellow concrete       the palette's light
//   oak blocks, chests    the element's wood: log, wood, stripped log and wood, planks, stairs, slab, fence, fence
//                         gate, button, pressure plate, leaves, and its chest (same facing, shape and so on)
// Everything else stays as built. Arcane Spawners and Arcane Vaults take the element and start waiting; what the
// template saved of their block entities and the keystone's (from the world they were built in) is dropped, so they
// start fresh. The
// wall choice is seeded by position, so it doesn't change with the chunk being built. Registered as dust:sanctum
// (DustFeatureRegistry), though only SanctumPiece uses it.
//? if >=26.3 {
public class SanctumProcessor implements StructureProcessor
//?} else {
/*public class SanctumProcessor extends StructureProcessor
*///?}
{
    public static final MapCodec<SanctumProcessor> CODEC = ElementalArmorSet.CODEC.fieldOf("set").xmap(SanctumProcessor::new, processor -> processor.set);

    private final ElementalArmorSet set;
    private final Map<Block, Function<RandomSource, BlockState>> stone = new HashMap<>();
    private final Map<Block, Block> wood;

    public SanctumProcessor(ElementalArmorSet set)
    {
        this.set = set;
        SanctumPalette palette = SanctumPalette.of(set);
        stone.put(concrete("white"), palette::wall);
        stone.put(concrete("light_gray"), random -> palette.wallAlt());
        stone.put(concrete("gray"), random -> palette.floor());
        stone.put(concrete("cyan"), random -> palette.accent());
        stone.put(concrete("yellow"), random -> palette.light());
        this.wood = woodSwaps(set);
    }

    // A concrete, by id (26.3 keeps them in Blocks.CONCRETE, a ColorCollection; 26.1.2 as Blocks.WHITE_CONCRETE and so on)
    private static Block concrete(String color)
    {
        return BuiltInRegistries.BLOCK.getValue(Identifier.withDefaultNamespace(color + "_concrete"));
    }

    @Override
    public @Nullable StructureBlockInfo process(LevelReader level, BlockPos offset, BlockPos pivot, StructureBlockInfo original, StructureBlockInfo current,
                                                StructurePlaceSettings settings, @Nullable StructureTemplate template)
    {
        BlockState state = current.state();
        if (state.is(Sanctums.ARCANE_SPAWNER.get())) {
            return new StructureBlockInfo(current.pos(), state.setValue(ArcaneSpawnerBlock.SET, set)
                    .setValue(ArcaneSpawnerBlock.PHASE, ArcaneSpawnerBlock.Phase.WAITING), null);
        }
        if (state.is(Sanctums.ARCANE_VAULT.get())) {
            return new StructureBlockInfo(current.pos(), state.setValue(ArcaneVaultBlock.SET, set)
                    .setValue(ArcaneVaultBlock.STATE, ArcaneVaultBlock.State.INACTIVE), null);
        }
        if (state.is(Sanctums.SANCTUM_KEYSTONE.get())) return new StructureBlockInfo(current.pos(), state, null);
        Function<RandomSource, BlockState> forStone = stone.get(state.getBlock());
        if (forStone != null) return new StructureBlockInfo(current.pos(), forStone.apply(settings.getRandom(current.pos())), current.nbt());
        Block forWood = wood.get(state.getBlock());
        return forWood == null ? current : new StructureBlockInfo(current.pos(), forWood.withPropertiesOf(state), current.nbt());
    }

    // The oak blocks (and the plain chest) each of the element's wood blocks stands in for
    private static Map<Block, Block> woodSwaps(ElementalArmorSet set)
    {
        Map<Block, Block> swaps = new HashMap<>();
        for (ElementalWood type : ElementalWood.values()) {
            if (!type.element().equals(set.element())) continue;
            WoodSet wood = ElementalWoods.set(type);
            swaps.put(Blocks.OAK_LOG, wood.log().get());
            swaps.put(Blocks.OAK_WOOD, wood.wood().get());
            swaps.put(Blocks.STRIPPED_OAK_LOG, wood.strippedLog().get());
            swaps.put(Blocks.STRIPPED_OAK_WOOD, wood.strippedWood().get());
            swaps.put(Blocks.OAK_PLANKS, wood.planks().get());
            swaps.put(Blocks.OAK_STAIRS, wood.stairs().get());
            swaps.put(Blocks.OAK_SLAB, wood.slab().get());
            swaps.put(Blocks.OAK_FENCE, wood.fence().get());
            swaps.put(Blocks.OAK_FENCE_GATE, wood.fenceGate().get());
            swaps.put(Blocks.OAK_BUTTON, wood.button().get());
            swaps.put(Blocks.OAK_PRESSURE_PLATE, wood.pressurePlate().get());
            swaps.put(Blocks.OAK_LEAVES, wood.leaves().get());
            swaps.put(Blocks.CHEST, wood.chest().get());
        }
        return swaps;
    }

    // Stonecutter: 26.3's processors give their codec (registered as is); before, a StructureProcessorType wrapping it
    //? if >=26.3 {
    @Override
    public MapCodec<SanctumProcessor> codec()
    {
        return CODEC;
    }
    //?} else {
    /*@Override
    protected net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType<?> getType()
    {
        return com.mowmaster.dust.DustRegistries.DustFeatureRegistry.SANCTUM_PROCESSOR.get();
    }
    *///?}
}
