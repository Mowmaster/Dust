package com.mowmaster.dust.Features.Guide;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// A guide scene: a little animated diorama showing how something is used, opened by holding the guide key (G) over
// one of its items (GuideInput, GuideScreen). Any mod (or resource pack) adds them as JSON files under
// assets/<namespace>/guides/ (GuideLoader; the file's path is the scene's id), or in code (RegisterGuidesEvent, with
// GuideScene.builder). An item with several scenes shows them in "order", then by id.
//   title     what it's about (a text component)
//   items     the items it's shown for
//   plate     the floor: width x depth blocks at y = 0, a checker of two blocks ("block", "alt"); width 0: none
//   blocks    blocks there from the start: {"pos": [x,y,z], "to": [x,y,z]?, "state": "...", "data": {...}?, "tint": "#RRGGBB"?}
//             data: what its block entity holds (as it saves it: a jar's dust, a machine's phase), drawn by its block
//             entity renderer; tint: the colour every tinted part of its model is drawn (a filled Glass Dust Tube's dust)
//   camera    how it's looked at: yaw (turned this far round from looking at its south side; 45: from the south-east),
//             pitch (looking down this far), zoom (1: fits the view)
//   steps     the timeline (GuideStep)
// The whole format, with an example, is in GUIDES.md at the root of Dust's sources.
public record GuideScene(Component title, List<Identifier> items, int order, Plate plate, List<Placed> blocks, Camera camera, List<GuideStep> steps)
{
    public record Plate(int width, int depth, BlockState block, BlockState alt)
    {
        public static final Plate DEFAULT = new Plate(5, 5, Blocks.SMOOTH_STONE.defaultBlockState(), Blocks.POLISHED_ANDESITE.defaultBlockState());
        public static final Codec<Plate> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.INT.optionalFieldOf("width", DEFAULT.width).forGetter(Plate::width),
                Codec.INT.optionalFieldOf("depth", DEFAULT.depth).forGetter(Plate::depth),
                GuideCodecs.BLOCK_STATE.optionalFieldOf("block", DEFAULT.block).forGetter(Plate::block),
                GuideCodecs.BLOCK_STATE.optionalFieldOf("alt", DEFAULT.alt).forGetter(Plate::alt)
        ).apply(i, Plate::new));
    }

    public record Placed(BlockPos pos, Optional<BlockPos> to, BlockState state, Optional<CompoundTag> data, Optional<Integer> tint)
    {
        public static final Codec<Placed> CODEC = RecordCodecBuilder.create(i -> i.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(Placed::pos),
                BlockPos.CODEC.optionalFieldOf("to").forGetter(Placed::to),
                GuideCodecs.BLOCK_STATE.fieldOf("state").forGetter(Placed::state),
                GuideCodecs.DATA.optionalFieldOf("data").forGetter(Placed::data),
                GuideCodecs.COLOR.optionalFieldOf("tint").forGetter(Placed::tint)
        ).apply(i, Placed::new));
    }

    public record Camera(float yaw, float pitch, float zoom)
    {
        public static final Camera DEFAULT = new Camera(45, 30, 1);
        public static final Codec<Camera> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.FLOAT.optionalFieldOf("yaw", DEFAULT.yaw).forGetter(Camera::yaw),
                Codec.FLOAT.optionalFieldOf("pitch", DEFAULT.pitch).forGetter(Camera::pitch),
                Codec.FLOAT.optionalFieldOf("zoom", DEFAULT.zoom).forGetter(Camera::zoom)
        ).apply(i, Camera::new));
    }

    public static final Codec<GuideScene> CODEC = RecordCodecBuilder.create(i -> i.group(
            ComponentSerialization.CODEC.fieldOf("title").forGetter(GuideScene::title),
            Identifier.CODEC.listOf().fieldOf("items").forGetter(GuideScene::items),
            Codec.INT.optionalFieldOf("order", 0).forGetter(GuideScene::order),
            Plate.CODEC.optionalFieldOf("plate", Plate.DEFAULT).forGetter(GuideScene::plate),
            Placed.CODEC.listOf().optionalFieldOf("blocks", List.of()).forGetter(GuideScene::blocks),
            Camera.CODEC.optionalFieldOf("camera", Camera.DEFAULT).forGetter(GuideScene::camera),
            GuideStep.CODEC.listOf().optionalFieldOf("steps", List.of()).forGetter(GuideScene::steps)
    ).apply(i, GuideScene::new));

    public static Builder builder(Component title)
    {
        return new Builder(title);
    }

    // Building a scene in code, the same as a file would (each method adds a step to the end of the timeline):
    //   GuideScene.builder(Component.translatable("guide.mymod.press.title")).items(MyItems.PRESS.get())
    //       .show(new BlockPos(2, 1, 2), press).idle(10)
    //       .text(Component.translatable("guide.mymod.press.1"), new Vec3(2.5, 2, 2.5), 80).idle(40)
    //       .build();
    public static final class Builder
    {
        private final Component title;
        private final List<Identifier> items = new ArrayList<>();
        private int order;
        private Plate plate = Plate.DEFAULT;
        private final List<Placed> blocks = new ArrayList<>();
        private Camera camera = Camera.DEFAULT;
        private final List<GuideStep> steps = new ArrayList<>();

        private Builder(Component title)
        {
            this.title = title;
        }

        public Builder items(ItemLike... items)
        {
            for (ItemLike item : items) this.items.add(BuiltInRegistries.ITEM.getKey(item.asItem()));
            return this;
        }

        public Builder order(int order) { this.order = order; return this; }
        public Builder plate(int width, int depth, BlockState block, BlockState alt) { this.plate = new Plate(width, depth, block, alt); return this; }
        public Builder noPlate() { this.plate = new Plate(0, 0, plate.block(), plate.alt()); return this; }
        public Builder camera(float yaw, float pitch, float zoom) { this.camera = new Camera(yaw, pitch, zoom); return this; }
        // A block there from the start (with its block entity's data)
        public Builder block(BlockPos pos, BlockState state) { blocks.add(new Placed(pos, Optional.empty(), state, Optional.empty(), Optional.empty())); return this; }
        public Builder block(BlockPos pos, BlockState state, CompoundTag data) { blocks.add(new Placed(pos, Optional.empty(), state, Optional.of(data), Optional.empty())); return this; }
        public Builder blocks(BlockPos from, BlockPos to, BlockState state) { blocks.add(new Placed(from, Optional.of(to), state, Optional.empty(), Optional.empty())); return this; }

        // ---- steps ----
        public Builder step(GuideStep step) { steps.add(step); return this; }
        public Builder idle(int ticks) { return step(new GuideStep.Idle(ticks)); }
        public Builder show(BlockPos pos, BlockState state) { return step(new GuideStep.Show(pos, Optional.empty(), state, Optional.empty(), Optional.empty(), 10, GuideStep.DEFAULT)); }
        public Builder show(BlockPos pos, BlockState state, CompoundTag data) { return step(new GuideStep.Show(pos, Optional.empty(), state, Optional.of(data), Optional.empty(), 10, GuideStep.DEFAULT)); }
        public Builder show(BlockPos from, BlockPos to, BlockState state) { return step(new GuideStep.Show(from, Optional.of(to), state, Optional.empty(), Optional.empty(), 10, GuideStep.DEFAULT)); }
        public Builder hide(BlockPos pos) { return step(new GuideStep.Hide(pos, Optional.empty(), 10, GuideStep.DEFAULT)); }
        public Builder place(BlockPos pos, BlockState state) { return step(new GuideStep.Place(pos, Optional.empty(), state, Optional.empty(), Optional.empty(), GuideStep.DEFAULT)); }
        public Builder place(BlockPos pos, BlockState state, CompoundTag data) { return step(new GuideStep.Place(pos, Optional.empty(), state, Optional.of(data), Optional.empty(), GuideStep.DEFAULT)); }
        // Changes what a block entity holds (merged into its data: only the keys given change)
        public Builder data(BlockPos pos, CompoundTag data) { return step(new GuideStep.Data(pos, Optional.empty(), data, GuideStep.DEFAULT)); }
        // An item resting at a point until removed
        public Builder item(String id, Item item, Vec3 at) { return step(new GuideStep.ShowItem(id, new GuideItem(item, 1), at, Optional.empty(), 0, -1, false, 0.6F, GuideStep.DEFAULT)); }
        // An item sliding from one point to another over ticks, then resting there until removed
        public Builder item(String id, Item item, Vec3 from, Vec3 at, int ticks) { return step(new GuideStep.ShowItem(id, new GuideItem(item, 1), at, Optional.of(from), ticks, -1, false, 0.6F, GuideStep.DEFAULT)); }
        public Builder remove(String id) { return step(new GuideStep.RemoveItem(id, GuideStep.DEFAULT)); }
        public Builder text(Component text, int ticks) { return step(new GuideStep.Text(text, Optional.empty(), GuideStep.Side.RIGHT, ticks, GuideStep.DEFAULT)); }
        public Builder text(Component text, Vec3 point, int ticks) { return step(new GuideStep.Text(text, Optional.of(point), GuideStep.Side.RIGHT, ticks, GuideStep.DEFAULT)); }
        public Builder text(Component text, Vec3 point, GuideStep.Side side, int ticks) { return step(new GuideStep.Text(text, Optional.of(point), side, ticks, GuideStep.DEFAULT)); }
        public Builder highlight(BlockPos pos, int color, int ticks) { return step(new GuideStep.Highlight(Optional.of(pos), Optional.empty(), Optional.empty(), color, ticks, GuideStep.DEFAULT)); }
        public Builder highlight(Vec3 from, Vec3 to, int color, int ticks) { return step(new GuideStep.Highlight(Optional.empty(), Optional.of(from), Optional.of(to), color, ticks, GuideStep.DEFAULT)); }
        public Builder click(Vec3 point, boolean right, Item held, int ticks) { return step(new GuideStep.Click(point, right ? "right" : "left", Optional.ofNullable(held).map(h -> new GuideItem(h, 1)), false, ticks, GuideStep.DEFAULT)); }
        public Builder rotate(float degrees, int ticks) { return step(new GuideStep.Rotate(degrees, ticks, GuideStep.DEFAULT)); }

        public GuideScene build()
        {
            return new GuideScene(title, List.copyOf(items), order, plate, List.copyOf(blocks), camera, List.copyOf(steps));
        }
    }
}
