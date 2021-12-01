package com.mowmaster.dust.World.GeodeGen;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.Random;
import java.util.stream.Stream;

public class GeodeDecorator extends PlacementModifier
{
    public static final Codec<GeodeDecorator> CODEC;
    public static final GeodeDecorator INSTANCE = new GeodeDecorator();

    static {
        CODEC = Codec.unit(() -> {
            return INSTANCE;
        });
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, Random random, BlockPos pos) {
        long a = random.nextLong() | 1L;
        long b = random.nextLong() | 1L;
        random.setSeed(((pos.getX() * a * 341873128712L + 12412146) * (pos.getZ() * b * 132897987541L + 5813717)) ^ 423487234);
        return Stream.of(pos);
    }

    @Override
    public PlacementModifierType<?> type() {
        return DeferredRegisterBlocks.RNG_DECORATOR;
    }
}
