package com.mowmaster.dust.Features.Sanctums;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustRegistries.DustFeatureRegistry;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Trees.ElementalWood;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Sanctums as a structure (dust:sanctum), so /locate structure dust:sanctum finds them and they keep their distance
// from each other: the structure set dust:sanctums tries one in each 22 x 22 chunk area, at least 8 chunks from the
// next (DustWorldgen; a datapack can change that). At the chosen chunk's middle it needs dry land (not ocean, river or
// beach); its element is one whose trees grow in the biome (or any of the fourteen), and its look one of the sanctum
// templates (SanctumTemplates), turned a random way with its anchor on the ground there. The ground under the
// template's ground layer mustn't be too steep (each corner within SLOPE blocks of the anchor's), and it must fit
// between the world's floor and its build limit. SanctumPiece places it. None generate unless the config's
// worldgen.sanctums is on (it's off by default).
public class SanctumStructure extends Structure
{
    public static final MapCodec<SanctumStructure> CODEC = simpleCodec(SanctumStructure::new);
    private static final int SLOPE = 4;

    public SanctumStructure(StructureSettings settings)
    {
        super(settings);
    }

    @Override
    public StructureType<?> type()
    {
        return DustFeatureRegistry.SANCTUM_STRUCTURE.get();
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context)
    {
        // Off unless the config turns them on (worldgen.sanctums)
        if (!com.mowmaster.dust.DustConfig.flag(com.mowmaster.dust.DustConfig.SANCTUM_STRUCTURES)) return Optional.empty();
        ChunkPos chunk = context.chunkPos();
        ChunkGenerator generator = context.chunkGenerator();
        LevelHeightAccessor heights = context.heightAccessor();
        RandomState state = context.randomState();
        int cx = chunk.getMiddleBlockX(), cz = chunk.getMiddleBlockZ();
        int surface = generator.getFirstFreeHeight(cx, cz, Heightmap.Types.WORLD_SURFACE_WG, heights, state);
        if (!dry(generator, heights, state, cx, cz, surface)) return Optional.empty();

        // The biome there: not in or by water; it picks the element
        int qx = QuartPos.fromBlock(cx), qy = QuartPos.fromBlock(surface), qz = QuartPos.fromBlock(cz);
        // Stonecutter: 26.3 resolves biomes through the context's resolver (RandomState no longer has the climate sampler)
        //? if >=26.3 {
        Holder<Biome> biome = context.biomeResolver().getNoiseBiome(qx, qy, qz);
        //?} else {
        /*Holder<Biome> biome = context.biomeSource().getNoiseBiome(qx, qy, qz, context.randomState().sampler());
        *///?}
        if (biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_DEEP_OCEAN) || biome.is(BiomeTags.IS_RIVER) || biome.is(BiomeTags.IS_BEACH)) return Optional.empty();

        RandomSource random = context.random();
        ElementalArmorSet set = pickSet(biome, random);
        StructureTemplateManager templates = context.structureTemplateManager();
        Identifier id = SanctumTemplates.pick(templates, set, random);
        if (id == null) return Optional.empty();
        SanctumTemplates.Layout layout = SanctumTemplates.layout(id, templates.getOrCreate(id));

        // Turned a random way, the anchor on the ground at the chunk's middle
        Rotation rotation = Rotation.getRandom(random);
        StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(rotation);
        BlockPos anchor = StructureTemplate.calculateRelativePosition(settings, layout.anchor());
        BlockPos position = new BlockPos(cx - anchor.getX(), surface - anchor.getY(), cz - anchor.getZ());
        for (BlockPos corner : layout.corners()) {
            BlockPos at = StructureTemplate.calculateRelativePosition(settings, corner).offset(position);
            if (Math.abs(generator.getFirstFreeHeight(at.getX(), at.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heights, state) - surface) > SLOPE) return Optional.empty();
        }
        if (position.getY() < heights.getMinY() + 4 || position.getY() + layout.size().getY() > heights.getMaxY()) return Optional.empty();
        return Optional.of(new GenerationStub(new BlockPos(cx, surface, cz), builder -> builder.addPiece(new SanctumPiece(templates, id, position, rotation, set))));
    }

    // The top block of the column (surface: the first free block above it) isn't water or lava
    private static boolean dry(ChunkGenerator generator, LevelHeightAccessor heights, RandomState state, int x, int z, int surface)
    {
        return generator.getBaseColumn(x, z, heights, state).getBlock(surface - 1).getFluidState().isEmpty();
    }

    // An element whose trees grow in this biome, or any
    private static ElementalArmorSet pickSet(Holder<Biome> biome, RandomSource random)
    {
        List<ElementalArmorSet> suited = new ArrayList<>();
        for (ElementalWood wood : ElementalWood.values()) {
            ElementalArmorSet set = ElementalArmorSet.of(wood.element());
            if (set != null && wood.growsIn(biome)) suited.add(set);
        }
        if (suited.isEmpty()) return ElementalArmorSet.values()[random.nextInt(ElementalArmorSet.values().length)];
        return suited.get(random.nextInt(suited.size()));
    }
}
