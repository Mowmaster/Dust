package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustCompositions;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.ElementNames;
import com.mowmaster.dust.Features.DustComposition.ElementTooltip;
import com.mowmaster.dust.Features.DustStorage.DustJarBlockEntity;
import com.mowmaster.dust.Features.DustStorage.DustJars;
import com.mowmaster.dust.Features.DustStorage.DustStream;
import com.mowmaster.mowlib.tint.CBTintData;
import com.mowmaster.mowlib.tint.ITintHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mowmaster.dust.Features.Machines.ReplicatorLayout.*;

// The Element Replicator: copies an item whose elements the player has fully worked out, with dust drawn from the Dust
// Jars round it.
//   the item to copy   put on the bottom sigil by a player's hand only (no hopper or pipe reaches it:
//                      StationSlot.Role.MANUAL), and only if that player has fully worked out what it's made of (the
//                      row of elements in its tooltip: ElementTooltip.forPlayer). It stays there: each run copies it
//                      again. Left-click takes it back while it isn't running.
//   the cost           DUST_PER_ELEMENT of each element for each time the item lists it (blue wool is water + creature +
//                      protection: 1 of each), from the Dust Jars within JAR_RANGE blocks (nearest first), taken when
//                      the dust starts flowing
//   a run              started with a Magical Flint and Steel (ElementReplicatorBlock), a dispenser firing one into it,
//                      or a fire spell landing on it (activate). ReplicatorLayout has the timing:
//                        scan    the bottom glyph spins up through the item
//                        draw    the dust is taken, and streams from each jar into the top sigil the whole time the
//                                bottom glyph sinks back (DustStream, held)
//                        print   the top glyph spins, then rises as the copy appears beneath it
//                      If the jars no longer hold enough when the dust should start flowing, it fizzles out.
//   the copy           on the top sigil when it's done: left-click takes it, as does a hopper underneath (out the
//                      bottom, like every station's output). It won't start again until the copy has been taken.
//                      A copy is the plain item, keeping only what makes it the kind of thing it is (KEPT: a potion's
//                      effect, a goat horn's tune, a dye colour, banner patterns...), never what's in it or has been
//                      added to it (contents, enchantments, damage, a name, a jar's dust), so the elements pay for all
//                      of it.
//   colours            dyed with MowLib's dyes, like the Element Crafter (one colour: the slabs, rails and sigils are
//                      tint index 0, the posts 1; both region 0)
// What's saved is also what clients get (CBSyncedBlockEntity): the item to copy, the copy, how far into a run it is and
// the colours. Clients count the run on by themselves between syncs (start, draw, print, done).
public class ElementReplicatorBlockEntity extends StationBlockEntity implements ITintHolder
{
    public static final int JAR_RANGE = 8;
    public static final int DUST_PER_ELEMENT = 1;
    // Each jar's stream is drawn at least this thick (DustStream's amount), so even a cheap copy shows a good flow
    private static final int STREAM_SIZE = 40;

    private final CBTintData tints = new CBTintData(this, ElementReplicatorBlock.DYE_REGIONS);
    private ItemStack template = ItemStack.EMPTY;
    private ItemStack output = ItemStack.EMPTY;
    // What a run makes from the template (copyOf; worked out when first asked for, forgotten when the template changes)
    private @Nullable ItemStack copy;
    private int progress = -1;

    public ElementReplicatorBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.ELEMENT_REPLICATOR.get(), pos, state);
    }

    public ItemStack getTemplate() { return template; }
    public ItemStack getOutput() { return output; }
    public boolean isRunning() { return progress >= 0; }

    // What the run is making (the renderer draws it being printed)
    public ItemStack getCopy()
    {
        if (copy == null) copy = copyOf(template);
        return copy;
    }

    public float progress(float partialTick)
    {
        return progress < 0 ? -1 : Math.min(RUN_TICKS, progress + partialTick);
    }

    // ---- copies ----

    // What a copy keeps from the item it's copied from: what makes it the kind of thing it is. A method rather than a
    // constant because Dust's own component types only exist once the registries have loaded.
    private static List<DataComponentType<?>> kept()
    {
        return List.of(DataComponents.POTION_CONTENTS, DataComponents.POTION_DURATION_SCALE, DataComponents.SUSPICIOUS_STEW_EFFECTS,
                DataComponents.OMINOUS_BOTTLE_AMPLIFIER, DataComponents.INSTRUMENT, DataComponents.PAINTING_VARIANT, DataComponents.DYED_COLOR,
                DataComponents.BASE_COLOR, DataComponents.BANNER_PATTERNS, DataComponents.FIREWORKS, DataComponents.FIREWORK_EXPLOSION,
                // A Fused Crystal's fusion, a process product's source (crushed stone knows it was stone)
                DustComponentDataRegistry.FUSION.get(), DustComponentDataRegistry.SOURCE.get());
    }

    // A copy of the item: the plain item with the kept components. copyFrom sets one component to whatever the other
    // stack has for it (or takes it away if that has none); a DataComponentType<?> fits its <T> parameter because Java
    // "captures" the unknown type for the call.
    public static ItemStack copyOf(ItemStack template)
    {
        if (template.isEmpty()) return ItemStack.EMPTY;
        ItemStack copy = new ItemStack(template.getItem());
        for (DataComponentType<?> type : kept()) copy.copyFrom(type, template);
        return copy;
    }

    // The dust a copy takes: DUST_PER_ELEMENT of each element for each time the item lists it (empty: it can't be copied)
    public static Map<DustElement, Integer> cost(ItemStack template)
    {
        Map<DustElement, Integer> cost = new LinkedHashMap<>();
        // Compounds and Alchemical Dust are what research is made of, not things to copy
        if (template.isEmpty() || Compounds.of(template) != null || AlchemicalDust.is(template)) return cost;
        DustComposition composition = DustCompositions.of(template);
        if (composition == null) return cost;
        composition.counted().forEach((element, count) -> cost.put(element, count * DUST_PER_ELEMENT));
        return cost;
    }

    // ---- clicking (StationInteractions) ----

    @Override
    public List<StationSlot> slots()
    {
        // The item to copy: only from a player's hand (manual: no handler for hoppers and pipes), while it's empty and
        // not running; hidden from left-clicks while running
        StationSlot templateSlot = new StationSlot(TEMPLATE_AREA, stack -> !isRunning() && template.isEmpty(), this::putTemplate,
                () -> isRunning() ? ItemStack.EMPTY : template, this::takeTemplate).manual();
        return List.of(templateSlot, StationSlot.output(OUTPUT_AREA, () -> output, stack -> output = stack, this::sync));
    }

    // One of the held item, if the player has fully worked it out; otherwise they're told why not and nothing goes in
    private int putTemplate(ItemStack stack, @Nullable Player player)
    {
        if (player == null || isRunning() || !template.isEmpty()) return 0;
        if (cost(stack).isEmpty()) {
            player.sendOverlayMessage(Component.translatable("dust.replicator.cannot_copy"));
            return 0;
        }
        if (ElementTooltip.forPlayer(stack, player) == null) {
            player.sendOverlayMessage(Component.translatable("dust.replicator.not_researched"));
            return 0;
        }
        setTemplate(stack.copyWithCount(1));
        return 1;
    }

    private ItemStack takeTemplate(int max)
    {
        if (isRunning() || template.isEmpty() || max <= 0) return ItemStack.EMPTY;
        ItemStack taken = template;
        setTemplate(ItemStack.EMPTY);
        return taken;
    }

    private void setTemplate(ItemStack stack)
    {
        template = stack;
        copy = null;
        sync();
    }

    // ---- starting ----

    // Why it can't start (a lang key suffix: dust.replicator.<problem>), or null if it can as far as what's in it goes.
    // Clients can ask this; whether the jars hold enough only the server knows (cannotStart(ServerLevel)).
    public @Nullable String cannotStart()
    {
        if (isRunning()) return "running";
        if (template.isEmpty()) return "empty";
        if (!output.isEmpty()) return "output";
        if (cost(template).isEmpty()) return "cannot_copy";
        return null;
    }

    public @Nullable String cannotStart(ServerLevel level)
    {
        String problem = cannotStart();
        if (problem != null) return problem;
        return DustJars.hold(DustJars.nearby(level, worldPosition, JAR_RANGE), cost(template)) ? null : "no_dust";
    }

    public void start(ServerLevel level)
    {
        progress = 0;
        level.playSound(null, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.2F);
        sync();
    }

    // Automation (StationBlockEntity): a dispenser's Magical Flint and Steel or a fire spell starts it if it can
    @Override
    public boolean activate(ServerLevel level, @Nullable Player by)
    {
        if (cannotStart(level) != null) return false;
        start(level);
        return true;
    }

    // ---- ticking ----

    public static void serverTick(Level level, BlockPos pos, BlockState state, ElementReplicatorBlockEntity replicator)
    {
        if (replicator.progress < 0 || !(level instanceof ServerLevel server)) return;
        replicator.progress++;
        if (replicator.progress == DRAW_START) {
            if (!replicator.drawDust(server)) {
                replicator.fizzle(server);
                return;
            }
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 0.8F);
            replicator.sync();
        }
        else if (replicator.progress == PRINT_START) {
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.2F);
            replicator.sync();
        }
        else if (replicator.progress >= RUN_TICKS) replicator.finish(server);
        else replicator.setChanged();
    }

    // Client: count on for the animation (the renderer); while printing, motes in the colours of the item's elements
    // where the top glyph is laying the copy down
    public static void clientTick(Level level, BlockPos pos, BlockState state, ElementReplicatorBlockEntity replicator)
    {
        if (replicator.progress < 0) return;
        if (replicator.progress < RUN_TICKS) replicator.progress++;
        float printed = printed(replicator.progress);
        if (printed <= 0 || printed >= 1) return;
        DustComposition composition = DustCompositions.of(replicator.template);
        if (composition == null || composition.elements().isEmpty()) return;
        RandomSource random = level.getRandom();
        Vec3 line = topSigil(pos).add(0, ITEM_HEIGHT / 16 * printed, 0);
        for (int i = 0; i < 2; i++) {
            DustElement element = composition.elements().get(random.nextInt(composition.elements().size()));
            double angle = random.nextDouble() * Math.PI * 2;
            double radius = 0.1 + random.nextDouble() * 0.15;
            level.addParticle(new DustParticleOptions(element.color(), 0.5F), line.x + Math.cos(angle) * radius, line.y,
                    line.z + Math.sin(angle) * radius, 0, 0, 0);
        }
    }

    // Where the dust flows to and the copy stands: just above the top sigil's middle (world position)
    private static Vec3 topSigil(BlockPos pos)
    {
        return Vec3.atLowerCornerOf(pos).add(TOP_SIGIL.x / 16, TOP_SIGIL.y / 16, TOP_SIGIL.z / 16);
    }

    // The dust for the copy, from the nearest jars: false (and nothing taken) if they no longer hold enough. Each jar
    // drawn on streams its dust into the top sigil until the bottom glyph is back down.
    private boolean drawDust(ServerLevel level)
    {
        Map<DustElement, Integer> cost = cost(template);
        List<DustJarBlockEntity> jars = DustJars.nearby(level, worldPosition, JAR_RANGE);
        if (cost.isEmpty() || !DustJars.hold(jars, cost)) return false;
        Vec3 to = topSigil(worldPosition).add(0, 0.1, 0);
        for (Map.Entry<DustElement, Integer> need : cost.entrySet()) {
            int left = need.getValue();
            for (DustJarBlockEntity jar : jars) {
                if (left <= 0) break;
                if (!need.getKey().equals(jar.element())) continue;
                int taken = jar.extract(left);
                if (taken <= 0) continue;
                left -= taken;
                DustStream.send(level, Vec3.atCenterOf(jar.getBlockPos()), to, need.getKey().color(), Math.max(STREAM_SIZE, taken),
                        STREAM_TRAVEL, STREAM_HOLD);
            }
        }
        return true;
    }

    // The dust ran out before it could flow: the run stops with a puff of smoke
    private void fizzle(ServerLevel level)
    {
        progress = -1;
        Vec3 at = topSigil(worldPosition);
        level.sendParticles(ParticleTypes.SMOKE, at.x, at.y + 0.1, at.z, 12, 0.2, 0.05, 0.2, 0.01);
        level.playSound(null, worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.6F, 1.2F);
        sync();
    }

    // Done: the copy stands on the top sigil, with a little burst in its elements' colours
    private void finish(ServerLevel level)
    {
        progress = -1;
        output = getCopy().copy();
        Vec3 at = topSigil(worldPosition).add(0, ITEM_HEIGHT / 32, 0);
        for (DustElement element : cost(template).keySet()) {
            level.sendParticles(new DustParticleOptions(element.color(), 0.8F), at.x, at.y, at.z, 6, 0.15, 0.15, 0.15, 0.02);
        }
        level.playSound(null, worldPosition, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0F, 1.1F);
        sync();
    }

    // ---- the player's view ----

    // Right-click with an empty hand: what copying the item takes ("Copying Diamond takes 4 Gem"), each amount red if
    // the jars nearby hold less than that
    public void tellCost(ServerLevel level, Player player)
    {
        String problem = template.isEmpty() ? "empty" : cost(template).isEmpty() ? "cannot_copy" : null;
        if (problem != null) {
            player.sendOverlayMessage(Component.translatable("dust.replicator." + problem));
            return;
        }
        Map<DustElement, Integer> held = DustJars.totals(DustJars.nearby(level, worldPosition, JAR_RANGE));
        MutableComponent list = Component.empty();
        boolean first = true;
        for (Map.Entry<DustElement, Integer> need : cost(template).entrySet()) {
            if (!first) list.append(", ");
            first = false;
            boolean enough = held.getOrDefault(need.getKey(), 0) >= need.getValue();
            list.append(Component.literal(need.getValue() + " ").withStyle(enough ? ChatFormatting.WHITE : ChatFormatting.RED))
                    .append(ElementNames.forViewer(need.getKey(), player));
        }
        player.sendOverlayMessage(Component.translatable("dust.replicator.cost", template.getHoverName(), list));
    }

    // ---- colours (ITintHolder) ----

    @Override
    public int getColor(int region)
    {
        return tints.getColor(region);
    }

    @Override
    public void setColor(int region, int color)
    {
        tints.setColor(region, color);
    }

    // The dye colour (0xRRGGBB; white undyed) for both tint indices: the slabs, rails and sigils, and the posts
    @Override
    public int getTint(int tintIndex)
    {
        return tints.getTint(0);
    }

    // ---- saving (and what clients get) ----

    @Override
    protected void saveAdditional(ValueOutput out)
    {
        super.saveAdditional(out);
        StationSaving.store(out, "template", template);
        StationSaving.store(out, "output", output);
        out.putInt("progress", progress);
        tints.save(out);
    }

    @Override
    protected void loadAdditional(ValueInput in)
    {
        super.loadAdditional(in);
        template = StationSaving.read(in, "template");
        copy = null;
        output = StationSaving.read(in, "output");
        progress = in.getIntOr("progress", -1);
        // The colours: on the client this also asks for the block to be drawn again, so the dye shows
        tints.load(in);
    }

    // Placed from an item that was dyed (crafted with dyes, or broken while dyed): it takes that colour
    @Override
    protected void applyImplicitComponents(DataComponentGetter components)
    {
        super.applyImplicitComponents(components);
        tints.applyItemColor(components, 0);
    }

    // Turned into an item (broken: the loot table copies it; creative pick-block): its colour goes on it
    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components)
    {
        super.collectImplicitComponents(components);
        tints.collectItemColor(components);
    }

    // Broken: the item to copy and the copy drop (dust already drawn for a run is lost)
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level == null) return;
        for (ItemStack stack : List.of(template, output)) {
            if (!stack.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, stack);
        }
    }
}
