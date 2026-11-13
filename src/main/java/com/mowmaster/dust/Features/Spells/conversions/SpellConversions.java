package com.mowmaster.dust.Features.Spells.conversions;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// The block effects that turn one block into another, from JSON tables in datapacks
// (data/<namespace>/dust_spell_conversions/*.json: ConversionTable, BlockConversion). Loaded with the server's data
// (and /reload); Dust's own tables are made by datagen (DustProviderSpellConversions). An element's block effect
// calls convert: the first rule of that element matching the block is used.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class SpellConversions extends SimpleJsonResourceReloadListener<ConversionTable>
{
    public static final String DIRECTORY = "dust_spell_conversions";
    private static Map<DustElement, List<BlockConversion>> byElement = Map.of();

    private SpellConversions()
    {
        // FileToIdConverter.json: every .json file under data/*/dust_spell_conversions/
        super(ConversionTable.CODEC, FileToIdConverter.json(DIRECTORY));
    }

    @Override
    protected void apply(Map<Identifier, ConversionTable> tables, ResourceManager resources, ProfilerFiller profiler)
    {
        Map<DustElement, List<BlockConversion>> loaded = new HashMap<>();
        // Sorted by file id, so the order rules are tried in doesn't depend on the order files happened to load in
        tables.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEach(entry -> loaded.computeIfAbsent(entry.getValue().element(), element -> new ArrayList<>()).addAll(entry.getValue().conversions()));
        byElement = loaded;
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddServerReloadListenersEvent event)
    {
        event.addListener(Identifier.fromNamespaceAndPath(DustReferences.MODID, DIRECTORY), new SpellConversions());
    }

    public static List<BlockConversion> rules(DustElement element)
    {
        return byElement.getOrDefault(element, List.of());
    }

    // ---- applying ----

    // The block effect of any element with a table: the first rule matching the block changes it. True if one did.
    public static boolean convert(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        BlockState state = level.getBlockState(pos);
        RandomSource random = level.getRandom();
        for (BlockConversion rule : rules(cast.element())) {
            if (!rule.cycle().isEmpty()) {
                for (int i = 0; i < rule.cycle().size(); i++) {
                    if (!rule.cycle().get(i).matches(state)) continue;
                    Block next = rule.cycle().get((i + 1) % rule.cycle().size()).pick(random);
                    if (next == null) return false;
                    set(level, pos, carryOver(state, next.defaultBlockState()));
                    return true;
                }
                continue;
            }
            if (rule.from().isEmpty() || !rule.from().get().matches(state)) continue;
            if (!rule.spread().isEmpty()) {
                spread(level, pos, state, rule);
                return true;
            }
            if (rule.sourceOnly() && !state.getFluidState().isSource()) continue;
            if (rule.nearFluid().isPresent() && !nearFluid(level, pos, rule)) continue;
            if (random.nextFloat() >= rule.chance()) {
                if (rule.fail().isPresent()) {
                    Block failed = rule.fail().get().pick(random);
                    if (failed != null) set(level, pos, failed.defaultBlockState());
                }
                level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 6, 0.25, 0.25, 0.25, 0.01);
                return true;
            }
            for (BlockConversion.Drop drop : rule.drops()) Block.popResource(level, pos, new ItemStack(drop.item(), drop.count()));
            Block to = pick(rule, pos, random);
            if (to != null) set(level, pos, carryOver(state, to.defaultBlockState()));
            else if (!rule.drops().isEmpty()) set(level, pos, Blocks.AIR.defaultBlockState());
            return true;
        }
        return false;
    }

    // What the block becomes: to_deep below y 0 if there is one, else one of to (null: nothing named)
    private static @Nullable Block pick(BlockConversion rule, BlockPos pos, RandomSource random)
    {
        if (pos.getY() < 0 && rule.toDeep().isPresent()) return rule.toDeep().get().pick(random);
        if (rule.to().isEmpty()) return null;
        return rule.to().get(random.nextInt(rule.to().size())).pick(random);
    }

    private static boolean nearFluid(ServerLevel level, BlockPos pos, BlockConversion rule)
    {
        for (Direction side : Direction.values()) {
            if (level.getFluidState(pos.relative(side)).is(rule.nearFluid().get())) return true;
        }
        return false;
    }

    // The block spreads onto matching blocks right next to it
    private static void spread(ServerLevel level, BlockPos pos, BlockState state, BlockConversion rule)
    {
        for (BlockPos near : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            BlockState there = level.getBlockState(near);
            for (BlockRef onto : rule.spread()) {
                if (onto.matches(there)) {
                    set(level, near.immutable(), carryOver(there, state.getBlock().defaultBlockState()));
                    break;
                }
            }
        }
    }

    private static void set(ServerLevel level, BlockPos pos, BlockState state)
    {
        level.setBlockAndUpdate(pos, state);
        level.sendParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 6, 0.3, 0.3, 0.3, 0.2);
    }

    // The old state's properties that the new block also has (by name, e.g. facing, waterlogged), carried over
    public static BlockState carryOver(BlockState from, BlockState to)
    {
        BlockState result = to;
        for (Property<?> property : from.getProperties()) {
            Property<?> same = to.getBlock().getStateDefinition().getProperty(property.getName());
            if (same != null) result = copy(from, property, result, same);
        }
        return result;
    }

    // Generics: the two properties may have different value types, so the value goes across by name
    private static <A extends Comparable<A>, B extends Comparable<B>> BlockState copy(BlockState from, Property<A> property, BlockState to, Property<B> same)
    {
        String value = property.getName(from.getValue(property));
        return same.getValue(value).map(parsed -> to.setValue(same, parsed)).orElse(to);
    }
}
