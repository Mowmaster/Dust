package com.mowmaster.dust.Features.Trees;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustRegistries.DustFeatureRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.Magic.BiomeElements;
import com.mowmaster.dust.Features.Magic.ElementAura;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Hangs 0 to MAX_PODS of an elemental tree's fruit (ElementalFruitBlock) on it as it grows: under its leaves, or on its
// trunk (not the lowest TRUNK_SKIP logs). Only on a tree growing where its element is over-full:
//   a sapling growing now     the chunk's aura holds more than its normal of the fruit's primal (for an element of two
//                             primals, of both of them: ElementAura)
//   a tree of the new world   the land is one rich in the primal (in both: BiomeElements)
// A sapling's tree starts its fruit green; a wild one's are at any stage. Tree feature decorator dust:elemental_fruit
// {"wood": "<wood>"} on each elemental tree (DustWorldgen.trees; 26.1.2: the trees' configured_feature json).
public class ElementalFruitDecorator extends TreeDecorator
{
    public static final MapCodec<ElementalFruitDecorator> CODEC = ElementalWood.CODEC.fieldOf("wood").xmap(ElementalFruitDecorator::new, decorator -> decorator.wood);
    public static final int MAX_PODS = 4;
    private static final int TRUNK_SKIP = 2;

    private final ElementalWood wood;

    public ElementalFruitDecorator(ElementalWood wood)
    {
        this.wood = wood;
    }

    @Override
    protected TreeDecoratorType<?> type()
    {
        return DustFeatureRegistry.ELEMENTAL_FRUIT.get();
    }

    @Override
    public void place(Context context)
    {
        if (context.logs().isEmpty()) return;
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos base = context.logs().getFirst();
        if (!canBear(level, base, wood.element())) return;
        int pods = random.nextInt(MAX_PODS + 1);
        if (pods == 0) return;
        // Where a fruit could hang, and the side it hangs from
        List<BlockPos> spots = new ArrayList<>();
        List<Direction> holds = new ArrayList<>();
        for (BlockPos leaf : context.leaves()) {
            if (context.isAir(leaf.below())) {
                spots.add(leaf.below());
                holds.add(Direction.UP);
            }
        }
        for (BlockPos log : context.logs()) {
            if (log.getY() - base.getY() < TRUNK_SKIP) continue;
            for (Direction side : Direction.Plane.HORIZONTAL) {
                if (context.isAir(log.relative(side))) {
                    spots.add(log.relative(side));
                    holds.add(side.getOpposite());
                }
            }
        }
        boolean sapling = level instanceof ServerLevel;
        BlockState fruit = ElementalWoods.set(wood).fruitPod().get().defaultBlockState();
        Set<BlockPos> used = new HashSet<>();
        for (int tries = 0; tries < pods * 6 && used.size() < pods && !spots.isEmpty(); tries++) {
            int pick = random.nextInt(spots.size());
            BlockPos at = spots.get(pick);
            if (!used.add(at)) continue;
            int age = sapling ? 0 : random.nextInt(ElementalFruitBlock.RIPE + 1);
            context.setBlock(at, fruit.setValue(ElementalFruitBlock.FACING, holds.get(pick)).setValue(ElementalFruitBlock.AGE, age));
        }
    }

    // Whether a tree growing at pos bears this element's fruit (see above)
    public static boolean canBear(WorldGenLevel level, BlockPos pos, DustElement element)
    {
        List<EnumElement> primals = ElementAura.primalsOf(element);
        if (primals.isEmpty()) return false;
        if (level instanceof ServerLevel server) {
            for (EnumElement primal : primals) if (!ElementAura.overNormal(server, pos, primal)) return false;
            return true;
        }
        Holder<Biome> biome = level.getBiome(pos);
        for (EnumElement primal : primals) if (!BiomeElements.isRich(biome, primal)) return false;
        return true;
    }
}
