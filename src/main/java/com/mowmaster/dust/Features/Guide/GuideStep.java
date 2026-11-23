package com.mowmaster.dust.Features.Guide;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Optional;

// One step of a guide scene's timeline (GuideScene.steps), in a file as {"type": "...", ...}. The steps run one after
// another: each starts when the one before has waited its "wait" ticks (each type has a sensible default, below), so a
// text box and a highlight can show while the blocks after them appear. Positions are in blocks: the base plate is
// y = 0, from x = 0 and z = 0; a block at [2, 1, 2] stands on the plate; [2.5, 2, 2.5] is the middle of its top.
//   idle       {"ticks": 20}                                         just wait
//   show       {"pos": [x,y,z], "to": [x,y,z]?, "state": "...", "data": {...}?, "tint": "#RRGGBB"?, "ticks": 10}
//                                                                    blocks drop into place (the box pos..to)
//   hide       {"pos", "to"?, "ticks": 10}                            blocks lift away
//   place      {"pos", "to"?, "state", "data"?, "tint"?}             blocks change at once (no wait)
//   data       {"pos", "to"?, "data": {...}}                         changes what block entities hold (merged into
//                                                                    their data: only the keys given change); no wait
//   item       {"id": "name", "item": ..., "at": [x,y,z], "from": [x,y,z]?, "ticks": 0, "lasts": -1, "spin": false,
//               "scale": 0.6}                                       an item, sliding from "from" to "at" over ticks;
//                                                                    shown for "lasts" ticks (-1: until removed)
//   remove     {"id": "name"}                                        takes an item away (no wait)
//   text       {"text": <text component>, "point": [x,y,z]?, "side": "right", "ticks": 80}
//                                                                    a text box (pointing at "point"); no wait
//   highlight  {"pos": [x,y,z]} or {"from": [x,y,z], "to": [x,y,z]}, "color": "#FFFF55", "ticks": 60
//                                                                    an outline round a block or box; no wait
//   click      {"point": [x,y,z], "button": "right", "item": ...?, "sneak": false, "ticks": 40}
//                                                                    shows a click there (with the held item); no wait
//   rotate     {"degrees": 90, "ticks": 30}                          turns the view round
// Text is a text component: "plain words", or {"translate": "lang.key"} (so it can be translated).
// "data" is what a block's block entity holds, as it saves it (GuideCodecs.DATA): its block entity renderer draws it (a
// jar's dust, a machine's moving parts), played on from when it was set (GuideAnimated). "tint": the colour every tinted
// part of the block's model is drawn (a filled Glass Dust Tube's dust); without it, a block is tinted as in the world.
public sealed interface GuideStep permits GuideStep.Idle, GuideStep.Show, GuideStep.Hide, GuideStep.Place, GuideStep.Data, GuideStep.ShowItem,
        GuideStep.RemoveItem, GuideStep.Text, GuideStep.Highlight, GuideStep.Click, GuideStep.Rotate
{
    // "wait" not given: the step's own default
    int DEFAULT = -1;

    String type();

    // Ticks from this step's start until the next one starts
    int delay();

    // Ticks this step keeps doing something for after it starts (how long the scene has to run for it)
    int length();

    private static int or(int wait, int otherwise)
    {
        return wait >= 0 ? wait : otherwise;
    }

    private static MapCodec<Integer> waitField()
    {
        return Codec.INT.optionalFieldOf("wait", DEFAULT);
    }

    // Which side of the point a text box sits
    enum Side implements StringRepresentable
    {
        RIGHT, LEFT, ABOVE, BELOW;

        public static final Codec<Side> CODEC = StringRepresentable.fromEnum(Side::values);

        @Override
        public String getSerializedName()
        {
            return name().toLowerCase(java.util.Locale.ROOT);
        }
    }

    record Idle(int ticks) implements GuideStep
    {
        static final MapCodec<Idle> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                Codec.INT.optionalFieldOf("ticks", 20).forGetter(Idle::ticks)
        ).apply(i, Idle::new));
        public String type() { return "idle"; }
        public int delay() { return ticks; }
        public int length() { return ticks; }
    }

    record Show(BlockPos pos, Optional<BlockPos> to, BlockState state, Optional<CompoundTag> data, Optional<Integer> tint, int ticks, int waitTicks)
            implements GuideStep
    {
        static final MapCodec<Show> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(Show::pos),
                BlockPos.CODEC.optionalFieldOf("to").forGetter(Show::to),
                GuideCodecs.BLOCK_STATE.fieldOf("state").forGetter(Show::state),
                GuideCodecs.DATA.optionalFieldOf("data").forGetter(Show::data),
                GuideCodecs.COLOR.optionalFieldOf("tint").forGetter(Show::tint),
                Codec.INT.optionalFieldOf("ticks", 10).forGetter(Show::ticks),
                waitField().forGetter(Show::waitTicks)
        ).apply(i, Show::new));
        public String type() { return "show"; }
        public int delay() { return or(waitTicks, ticks); }
        public int length() { return ticks; }
    }

    record Hide(BlockPos pos, Optional<BlockPos> to, int ticks, int waitTicks) implements GuideStep
    {
        static final MapCodec<Hide> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(Hide::pos),
                BlockPos.CODEC.optionalFieldOf("to").forGetter(Hide::to),
                Codec.INT.optionalFieldOf("ticks", 10).forGetter(Hide::ticks),
                waitField().forGetter(Hide::waitTicks)
        ).apply(i, Hide::new));
        public String type() { return "hide"; }
        public int delay() { return or(waitTicks, ticks); }
        public int length() { return ticks; }
    }

    record Place(BlockPos pos, Optional<BlockPos> to, BlockState state, Optional<CompoundTag> data, Optional<Integer> tint, int waitTicks) implements GuideStep
    {
        static final MapCodec<Place> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(Place::pos),
                BlockPos.CODEC.optionalFieldOf("to").forGetter(Place::to),
                GuideCodecs.BLOCK_STATE.fieldOf("state").forGetter(Place::state),
                GuideCodecs.DATA.optionalFieldOf("data").forGetter(Place::data),
                GuideCodecs.COLOR.optionalFieldOf("tint").forGetter(Place::tint),
                waitField().forGetter(Place::waitTicks)
        ).apply(i, Place::new));
        public String type() { return "place"; }
        public int delay() { return or(waitTicks, 0); }
        public int length() { return 0; }
    }

    record Data(BlockPos pos, Optional<BlockPos> to, CompoundTag data, int waitTicks) implements GuideStep
    {
        static final MapCodec<Data> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(Data::pos),
                BlockPos.CODEC.optionalFieldOf("to").forGetter(Data::to),
                GuideCodecs.DATA.fieldOf("data").forGetter(Data::data),
                waitField().forGetter(Data::waitTicks)
        ).apply(i, Data::new));
        public String type() { return "data"; }
        public int delay() { return or(waitTicks, 0); }
        public int length() { return 0; }
    }

    record ShowItem(String id, GuideItem item, Vec3 at, Optional<Vec3> from, int ticks, int lasts, boolean spin, float scale, int waitTicks)
            implements GuideStep
    {
        static final MapCodec<ShowItem> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                Codec.STRING.optionalFieldOf("id", "").forGetter(ShowItem::id),
                GuideItem.CODEC.fieldOf("item").forGetter(ShowItem::item),
                Vec3.CODEC.fieldOf("at").forGetter(ShowItem::at),
                Vec3.CODEC.optionalFieldOf("from").forGetter(ShowItem::from),
                Codec.INT.optionalFieldOf("ticks", 0).forGetter(ShowItem::ticks),
                Codec.INT.optionalFieldOf("lasts", -1).forGetter(ShowItem::lasts),
                Codec.BOOL.optionalFieldOf("spin", false).forGetter(ShowItem::spin),
                Codec.FLOAT.optionalFieldOf("scale", 0.6F).forGetter(ShowItem::scale),
                waitField().forGetter(ShowItem::waitTicks)
        ).apply(i, ShowItem::new));
        public String type() { return "item"; }
        public int delay() { return or(waitTicks, ticks); }
        public int length() { return Math.max(ticks, lasts); }
    }

    record RemoveItem(String id, int waitTicks) implements GuideStep
    {
        static final MapCodec<RemoveItem> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                Codec.STRING.fieldOf("id").forGetter(RemoveItem::id),
                waitField().forGetter(RemoveItem::waitTicks)
        ).apply(i, RemoveItem::new));
        public String type() { return "remove"; }
        public int delay() { return or(waitTicks, 0); }
        public int length() { return 0; }
    }

    record Text(Component text, Optional<Vec3> point, Side side, int ticks, int waitTicks) implements GuideStep
    {
        static final MapCodec<Text> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                ComponentSerialization.CODEC.fieldOf("text").forGetter(Text::text),
                Vec3.CODEC.optionalFieldOf("point").forGetter(Text::point),
                Side.CODEC.optionalFieldOf("side", Side.RIGHT).forGetter(Text::side),
                Codec.INT.optionalFieldOf("ticks", 80).forGetter(Text::ticks),
                waitField().forGetter(Text::waitTicks)
        ).apply(i, Text::new));
        public String type() { return "text"; }
        public int delay() { return or(waitTicks, 0); }
        public int length() { return ticks; }
    }

    record Highlight(Optional<BlockPos> pos, Optional<Vec3> from, Optional<Vec3> to, int color, int ticks, int waitTicks) implements GuideStep
    {
        static final MapCodec<Highlight> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                BlockPos.CODEC.optionalFieldOf("pos").forGetter(Highlight::pos),
                Vec3.CODEC.optionalFieldOf("from").forGetter(Highlight::from),
                Vec3.CODEC.optionalFieldOf("to").forGetter(Highlight::to),
                GuideCodecs.COLOR.optionalFieldOf("color", 0xFFFFFF55).forGetter(Highlight::color),
                Codec.INT.optionalFieldOf("ticks", 60).forGetter(Highlight::ticks),
                waitField().forGetter(Highlight::waitTicks)
        ).apply(i, Highlight::new));
        public String type() { return "highlight"; }
        public int delay() { return or(waitTicks, 0); }
        public int length() { return ticks; }

        // The outlined box: a whole block (pos), or from..to
        public AABB box()
        {
            if (pos.isPresent()) return new AABB(pos.get());
            Vec3 a = from.orElse(Vec3.ZERO);
            return new AABB(a, to.orElse(a.add(1, 1, 1)));
        }
    }

    record Click(Vec3 point, String button, Optional<GuideItem> item, boolean sneak, int ticks, int waitTicks) implements GuideStep
    {
        static final MapCodec<Click> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                Vec3.CODEC.fieldOf("point").forGetter(Click::point),
                Codec.STRING.optionalFieldOf("button", "right").forGetter(Click::button),
                GuideItem.CODEC.optionalFieldOf("item").forGetter(Click::item),
                Codec.BOOL.optionalFieldOf("sneak", false).forGetter(Click::sneak),
                Codec.INT.optionalFieldOf("ticks", 40).forGetter(Click::ticks),
                waitField().forGetter(Click::waitTicks)
        ).apply(i, Click::new));
        public String type() { return "click"; }
        public int delay() { return or(waitTicks, 0); }
        public int length() { return ticks; }
        public boolean right() { return !"left".equals(button); }
    }

    record Rotate(float degrees, int ticks, int waitTicks) implements GuideStep
    {
        static final MapCodec<Rotate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                Codec.FLOAT.fieldOf("degrees").forGetter(Rotate::degrees),
                Codec.INT.optionalFieldOf("ticks", 30).forGetter(Rotate::ticks),
                waitField().forGetter(Rotate::waitTicks)
        ).apply(i, Rotate::new));
        public String type() { return "rotate"; }
        public int delay() { return or(waitTicks, ticks); }
        public int length() { return ticks; }
    }

    Map<String, MapCodec<? extends GuideStep>> TYPES = Map.ofEntries(
            Map.entry("idle", Idle.CODEC), Map.entry("show", Show.CODEC), Map.entry("hide", Hide.CODEC), Map.entry("place", Place.CODEC),
            Map.entry("data", Data.CODEC), Map.entry("item", ShowItem.CODEC), Map.entry("remove", RemoveItem.CODEC), Map.entry("text", Text.CODEC),
            Map.entry("highlight", Highlight.CODEC), Map.entry("click", Click.CODEC), Map.entry("rotate", Rotate.CODEC));

    // dispatch: reads "type", then the rest of the object with that type's codec
    Codec<GuideStep> CODEC = Codec.STRING
            .validate(type -> TYPES.containsKey(type) ? DataResult.success(type) : DataResult.error(() -> "Unknown guide step type '" + type + "'"))
            .dispatch("type", GuideStep::type, TYPES::get);
}
