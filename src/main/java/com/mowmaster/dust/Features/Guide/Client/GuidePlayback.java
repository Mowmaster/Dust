package com.mowmaster.dust.Features.Guide.Client;

import com.mowmaster.dust.Features.Guide.GuideItem;
import com.mowmaster.dust.Features.Guide.GuideScene;
import com.mowmaster.dust.Features.Guide.GuideStep;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

// A guide scene's timeline worked out: when each step starts (each waits for the one before's "wait"), how long the
// whole thing runs, and what's on show at any moment (at). Nothing changes as it plays: at(time) is worked out afresh
// from the steps each frame, so the screen can pause, replay or jump anywhere on the timeline.
// A "data" step merges into what the block's block entity held before it: worked out once here, by going through the
// steps in order (the same order at() goes through them), so each frame hands the renderer the same tags.
public final class GuidePlayback
{
    // Ticks the scene stays on its last frame before it counts as finished
    private static final int TAIL = 20;
    // How far blocks drop in from (blocks)
    private static final float DROP = 1.25F;

    private final GuideScene scene;
    private final int[] starts;
    private final int length;
    private final List<Integer> keyframes = new ArrayList<>();
    // Item stacks made once, by step (GuideItem.stack)
    private final Map<Integer, ItemStack> stacks = new HashMap<>();
    // What each "data" step leaves in each block entity it changes, by step
    private final Map<Integer, Map<BlockPos, CompoundTag>> merged = new HashMap<>();
    // The box everything in the scene sits in, for the camera to fit
    private final Vector3f centre;
    private final float radius;

    public GuidePlayback(GuideScene scene)
    {
        this.scene = scene;
        List<GuideStep> steps = scene.steps();
        starts = new int[steps.size()];
        int time = 0, end = 0;
        for (int i = 0; i < steps.size(); i++) {
            GuideStep step = steps.get(i);
            starts[i] = time;
            end = Math.max(end, time + Math.max(step.length(), step.delay()));
            if (step instanceof GuideStep.Text) keyframes.add(time);
            time += step.delay();
        }
        length = Math.max(end, time) + TAIL;
        mergeData();

        AABB bounds = null;
        GuideScene.Plate plate = scene.plate();
        if (plate.width() > 0 && plate.depth() > 0) bounds = new AABB(0, 0, 0, plate.width(), 1, plate.depth());
        for (GuideScene.Placed placed : scene.blocks()) bounds = grow(bounds, boxOf(placed.pos(), placed.to()));
        for (GuideStep step : steps) {
            if (step instanceof GuideStep.Show show) bounds = grow(bounds, boxOf(show.pos(), show.to()));
            else if (step instanceof GuideStep.Place place) bounds = grow(bounds, boxOf(place.pos(), place.to()));
            else if (step instanceof GuideStep.ShowItem item) bounds = grow(bounds, new AABB(item.at(), item.at()).inflate(0.25));
        }
        if (bounds == null) bounds = new AABB(0, 0, 0, 1, 1, 1);
        Vec3 middle = bounds.getCenter();
        centre = new Vector3f((float) middle.x, (float) middle.y, (float) middle.z);
        radius = (float) Math.max(1.5, Math.sqrt(bounds.getXsize() * bounds.getXsize() + bounds.getYsize() * bounds.getYsize()
                + bounds.getZsize() * bounds.getZsize()) / 2);
    }

    // Goes through the steps in order, keeping what each block entity holds, to work out what each "data" step leaves
    private void mergeData()
    {
        Map<BlockPos, CompoundTag> held = new HashMap<>();
        for (GuideScene.Placed placed : scene.blocks()) forEach(placed.pos(), placed.to(), pos -> hold(held, pos, placed.data()));
        List<GuideStep> steps = scene.steps();
        for (int i = 0; i < steps.size(); i++) {
            switch (steps.get(i)) {
                case GuideStep.Show show -> forEach(show.pos(), show.to(), pos -> hold(held, pos, show.data()));
                case GuideStep.Place place -> forEach(place.pos(), place.to(), pos -> hold(held, pos, place.data()));
                case GuideStep.Hide hide -> forEach(hide.pos(), hide.to(), held::remove);
                case GuideStep.Data data -> {
                    Map<BlockPos, CompoundTag> left = new HashMap<>();
                    forEach(data.pos(), data.to(), pos -> {
                        CompoundTag before = held.get(pos);
                        CompoundTag after = (before == null ? new CompoundTag() : before.copy()).merge(data.data());
                        held.put(pos, after);
                        left.put(pos, after);
                    });
                    merged.put(i, left);
                }
                default -> {}
            }
        }
    }

    private static void hold(Map<BlockPos, CompoundTag> held, BlockPos pos, Optional<CompoundTag> data)
    {
        if (data.isPresent()) held.put(pos, data.get());
        else held.remove(pos);
    }

    private static AABB grow(AABB bounds, AABB box)
    {
        return bounds == null ? box : bounds.minmax(box);
    }

    private static AABB boxOf(BlockPos pos, Optional<BlockPos> to)
    {
        BlockPos other = to.orElse(pos);
        return new AABB(Math.min(pos.getX(), other.getX()), Math.min(pos.getY(), other.getY()), Math.min(pos.getZ(), other.getZ()),
                Math.max(pos.getX(), other.getX()) + 1, Math.max(pos.getY(), other.getY()) + 1, Math.max(pos.getZ(), other.getZ()) + 1);
    }

    private static void forEach(BlockPos pos, Optional<BlockPos> to, Consumer<BlockPos> action)
    {
        BlockPos other = to.orElse(pos);
        // betweenClosed reuses one mutable position, so each is copied (immutable) before it's kept
        for (BlockPos at : BlockPos.betweenClosed(pos, other)) action.accept(at.immutable());
    }

    public GuideScene scene() { return scene; }
    public int length() { return length; }
    // Where text boxes start (marked on the timeline, and jumped between)
    public List<Integer> keyframes() { return keyframes; }
    public Vector3f centre() { return centre; }
    public float radius() { return radius; }

    // How far through something lasting ticks, 0-1
    private static float progress(float local, int ticks)
    {
        return ticks <= 0 ? 1 : Mth.clamp(local / ticks, 0, 1);
    }

    // Fading in over the first few ticks and out over the last few
    private static float fade(float local, int ticks)
    {
        return Mth.clamp(Math.min(local / 5F, (ticks - local) / 5F), 0, 1);
    }

    private static float easeOut(float t)
    {
        float u = 1 - t;
        return 1 - u * u * u;
    }

    private static float smooth(float t)
    {
        return t * t * (3 - 2 * t);
    }

    private ItemStack stack(int step, GuideItem item)
    {
        return stacks.computeIfAbsent(step, key -> item.stack());
    }

    // Everything on show this far (ticks, fractions allowed) into the scene
    public GuideFrame at(float time)
    {
        Map<BlockPos, GuideFrame.BlockDraw> blocks = new LinkedHashMap<>();
        GuideScene.Plate plate = scene.plate();
        for (int x = 0; x < plate.width(); x++) {
            for (int z = 0; z < plate.depth(); z++) {
                BlockPos pos = new BlockPos(x, 0, z);
                blocks.put(pos, new GuideFrame.BlockDraw(pos, (x + z) % 2 == 0 ? plate.block() : plate.alt(), 0, 1, null, null, 0));
            }
        }
        for (GuideScene.Placed placed : scene.blocks()) {
            forEach(placed.pos(), placed.to(), pos -> blocks.put(pos, new GuideFrame.BlockDraw(pos, placed.state(), 0, 1,
                    placed.data().orElse(null), placed.tint().orElse(null), 0)));
        }
        Map<String, GuideFrame.ItemDraw> items = new LinkedHashMap<>();
        List<GuideFrame.TextDraw> texts = new ArrayList<>();
        List<GuideFrame.BoxDraw> boxes = new ArrayList<>();
        List<GuideFrame.ClickDraw> clicks = new ArrayList<>();
        float yaw = 0;
        List<GuideStep> steps = scene.steps();
        for (int i = 0; i < steps.size(); i++) {
            float local = time - starts[i];
            // Steps start in order, so nothing after this one has started either
            if (local < 0) break;
            float start = starts[i];
            // A switch on the step's type: each case names the record type and binds it (pattern matching)
            switch (steps.get(i)) {
                case GuideStep.Show show -> {
                    float lift = (1 - easeOut(progress(local, show.ticks()))) * DROP;
                    forEach(show.pos(), show.to(), pos -> blocks.put(pos, new GuideFrame.BlockDraw(pos, show.state(), lift, 1,
                            show.data().orElse(null), show.tint().orElse(null), start)));
                }
                case GuideStep.Hide hide -> {
                    float p = progress(local, hide.ticks());
                    forEach(hide.pos(), hide.to(), pos -> {
                        GuideFrame.BlockDraw there = blocks.get(pos);
                        if (p >= 1 || there == null) blocks.remove(pos);
                        else blocks.put(pos, there.moved(p * p * 0.6F, 1 - p));
                    });
                }
                case GuideStep.Place place ->
                        forEach(place.pos(), place.to(), pos -> blocks.put(pos, new GuideFrame.BlockDraw(pos, place.state(), 0, 1,
                                place.data().orElse(null), place.tint().orElse(null), start)));
                case GuideStep.Data data -> {
                    Map<BlockPos, CompoundTag> left = merged.getOrDefault(i, Map.of());
                    forEach(data.pos(), data.to(), pos -> {
                        GuideFrame.BlockDraw there = blocks.get(pos);
                        CompoundTag tag = left.get(pos);
                        if (there != null && tag != null) {
                            blocks.put(pos, new GuideFrame.BlockDraw(pos, there.state(), there.lift(), there.size(), tag, there.tint(), start));
                        }
                    });
                }
                case GuideStep.ShowItem item -> {
                    String key = item.id().isEmpty() ? "#" + i : item.id();
                    if (item.lasts() >= 0 && local >= item.lasts()) {
                        items.remove(key);
                    } else {
                        Vec3 at = item.from().map(from -> from.lerp(item.at(), smooth(progress(local, item.ticks())))).orElse(item.at());
                        items.put(key, new GuideFrame.ItemDraw(stack(i, item.item()), at, item.spin() ? (time * 3) % 360 : 0, item.scale()));
                    }
                }
                case GuideStep.RemoveItem remove -> items.remove(remove.id());
                case GuideStep.Text text -> {
                    if (local < text.ticks()) texts.add(new GuideFrame.TextDraw(text.text(), text.point().orElse(null), text.side(), fade(local, text.ticks())));
                }
                case GuideStep.Highlight highlight -> {
                    if (local < highlight.ticks()) boxes.add(new GuideFrame.BoxDraw(highlight.box(), highlight.color(), fade(local, highlight.ticks())));
                }
                case GuideStep.Click click -> {
                    if (local < click.ticks()) {
                        ItemStack held = click.item().isPresent() ? stack(i, click.item().get()) : ItemStack.EMPTY;
                        clicks.add(new GuideFrame.ClickDraw(click.point(), click.right(), held, click.sneak(), local / click.ticks()));
                    }
                }
                case GuideStep.Rotate rotate -> yaw += rotate.degrees() * smooth(progress(local, rotate.ticks()));
                case GuideStep.Idle idle -> {}
            }
        }
        return new GuideFrame(List.copyOf(blocks.values()), List.copyOf(items.values()), texts, boxes, clicks, yaw, time);
    }
}
