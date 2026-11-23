package com.mowmaster.dust.Features.Guide.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

// A guide scene's blocks and block entities for one frame, as a little world to look things up in: the block colours
// (tint sources) are worked out in it (a jar's lid takes its element's colour from its block entity), and block entity
// renderers look round in it (GuideContext.blocks: a Dust Separator Extension finding its separator). Everything is
// in plains, at full light, with nothing past the scene.
public final class GuideSceneBlocks implements BlockAndTintGetter
{
    private final Map<BlockPos, BlockState> states = new HashMap<>();
    private final Map<BlockPos, BlockEntity> blockEntities = new HashMap<>();
    private @Nullable Biome biome;

    void clear()
    {
        states.clear();
        blockEntities.clear();
    }

    void put(BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity)
    {
        states.put(pos, state);
        if (blockEntity != null) blockEntities.put(pos, blockEntity);
        else blockEntities.remove(pos);
    }

    @Override
    public @Nullable BlockEntity getBlockEntity(BlockPos pos)
    {
        return blockEntities.get(pos);
    }

    @Override
    public BlockState getBlockState(BlockPos pos)
    {
        return states.getOrDefault(pos, Blocks.AIR.defaultBlockState());
    }

    @Override
    public FluidState getFluidState(BlockPos pos)
    {
        return getBlockState(pos).getFluidState();
    }

    @Override
    public int getHeight()
    {
        return 384;
    }

    @Override
    public int getMinY()
    {
        return -64;
    }

    @Override
    public CardinalLighting cardinalLighting()
    {
        return CardinalLighting.DEFAULT;
    }

    @Override
    public LevelLightEngine getLightEngine()
    {
        return LevelLightEngine.EMPTY;
    }

    // Grass, leaves and water as in plains (white if there's no world to find plains in)
    @Override
    public int getBlockTint(BlockPos pos, ColorResolver resolver)
    {
        Biome plains = plains();
        return plains == null ? -1 : resolver.getColor(plains, pos.getX(), pos.getZ());
    }

    private @Nullable Biome plains()
    {
        var level = Minecraft.getInstance().level;
        if (biome == null && level != null) biome = level.registryAccess().lookupOrThrow(Registries.BIOME).getOptional(Biomes.PLAINS).orElse(null);
        return biome;
    }
}
