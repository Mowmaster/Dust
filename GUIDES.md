# Guides

Dust's guides show players how something is used: hover an item in any screen (inventory, chests, creative tabs,
recipe viewers), hold **G** (rebindable: *Options > Controls > Dust: Guide*), and an animated scene of it in use opens.
Drag to turn the scene, scroll to zoom, hover a block or item for its name. The buttons (or Left / R / Space / Right)
go to the previous scene, replay, pause and go to the next scene. Click or drag along the timeline to jump; its marks
are where each text box starts. Esc goes back to the screen the guide was opened from.

Any mod or resource pack can add guides, with no code at all (JSON files), or in code.

The code lives in `com.mowmaster.dust.Features.Guide` (it's meant to move to MowLib later, under a new package).

## Adding a guide with a JSON file

Put a file in `assets/<namespace>/guides/` (subfolders are fine). Its path is the scene's id:
`assets/mymod/guides/press/basics.json` is `mymod:press/basics`. A file with the same id as another replaces it, so a
resource pack can rewrite any scene. Scenes load with the client's resources: press F3+T to reload after editing one.

```json
{
  "title": {"translate": "guide.mymod.press.title"},
  "items": ["mymod:press"],
  "steps": [
    {"type": "show", "pos": [2, 1, 2], "state": "mymod:press[facing=north]"},
    {"type": "idle", "ticks": 10},
    {"type": "text", "text": {"translate": "guide.mymod.press.1"}, "point": [2.5, 2, 2.5], "ticks": 80},
    {"type": "click", "point": [2.5, 2, 2.5], "item": "minecraft:iron_ingot"},
    {"type": "idle", "ticks": 40},
    {"type": "item", "id": "plate", "item": "minecraft:iron_ingot", "from": [2.5, 2.6, 2.5], "at": [2.5, 2, 2.5], "ticks": 15},
    {"type": "idle", "ticks": 60}
  ]
}
```

### The scene

| Field    | Default                    | What it is |
|----------|----------------------------|------------|
| `title`  | (required)                 | A text component: `"plain words"` or `{"translate": "lang.key"}` |
| `items`  | (required)                 | The item ids it's shown for. Items of mods that aren't installed are skipped |
| `order`  | `0`                        | An item with several scenes shows them by `order`, then by id |
| `plate`  | 5 x 5, smooth stone / polished andesite | The floor at y = 0: `{"width": 5, "depth": 5, "block": "...", "alt": "..."}` (a checker of `block` and `alt`; width 0: no floor) |
| `blocks` | none                       | Blocks there from the start: `[{"pos": [x, y, z], "to": [x, y, z], "state": "...", "data": {...}, "tint": "#RRGGBB"}]` (`to` fills the box; `data` and `tint`: see "Block entities and tints" below) |
| `camera` | `{"yaw": 45, "pitch": 30, "zoom": 1}` | `yaw`: degrees turned round from looking at the south side (45 = from the south-east). `pitch`: degrees looking down. `zoom`: 1 fits the scene to the view |
| `steps`  | none                       | The timeline (below) |

Positions are in blocks. The floor is y = 0, starting from x = 0 and z = 0. A block at `[2, 1, 2]` stands on a 5 x 5
floor's middle, and `[2.5, 2, 2.5]` is the middle of its top. Points can be fractions (a pad on a block's top).

Block states are written as in commands: `"minecraft:oak_stairs[facing=east,half=top]"`. Vanilla's
`{"Name": "...", "Properties": {...}}` also works.

Items are an id (`"minecraft:paper"`), or `{"id": "...", "count": 3, "components": {...}}`, with components as in
`/give`, for example `{"id": "dust:alchemical_dust", "components": {"dust:alchemical_element": "dust:fire"}}`.

Colours are `"#RRGGBB"` or `"#AARRGGBB"`.

### Steps

The steps run one after another. Each one starts when the step before it has waited its `wait` ticks (20 ticks = 1
second). Every step can set `wait`; each type has its own default, shown below. Text boxes, highlights and clicks
default to no wait, so they stay on screen while the steps after them play.

| `type`      | Fields (defaults)                                                                                      | Default `wait` | What it does |
|-------------|--------------------------------------------------------------------------------------------------------|----------------|--------------|
| `idle`      | `ticks` (20)                                                                                           | `ticks`        | Waits |
| `show`      | `pos`, `to` (none), `state`, `data` (none), `tint` (none), `ticks` (10)                                | `ticks`        | Blocks drop into place (the box from `pos` to `to`) |
| `hide`      | `pos`, `to` (none), `ticks` (10)                                                                       | `ticks`        | Blocks lift away |
| `place`     | `pos`, `to` (none), `state`, `data` (none), `tint` (none)                                              | 0              | Blocks change at once (a machine switching on, a crop growing) |
| `data`      | `pos`, `to` (none), `data`                                                                             | 0              | Changes what block entities hold, without changing the blocks: merged into their data, so only the keys given change (a jar filling up, a machine moving on to its next phase) |
| `item`      | `id` (none), `item`, `at`, `from` (none), `ticks` (0), `lasts` (-1), `spin` (false), `scale` (0.6)     | `ticks`        | An item at a point, sliding from `from` to `at` over `ticks`. It stays for `lasts` ticks (-1: until removed). Name it with `id` to move it later (a new `item` step with the same id replaces it) or remove it |
| `remove`    | `id`                                                                                                   | 0              | Takes a named item away |
| `text`      | `text`, `point` (none), `side` (`right`), `ticks` (80)                                                 | 0              | A text box for `ticks`, with a line to `point` if given (`side`: `right`, `left`, `above`, `below`). Without a point it sits in the view's top left corner |
| `highlight` | `pos`, or `from` and `to`; `color` (`#FFFF55`), `ticks` (60)                                          | 0              | An outline round a block, or round any box (fractions allowed: a single pad) |
| `click`     | `point`, `button` (`right`), `item` (none), `sneak` (false), `ticks` (40)                             | 0              | Shows a click there: a pulsing ring, a mouse with the button lit, the held item and "Right-click" |
| `rotate`    | `degrees`, `ticks` (30)                                                                                | `ticks`        | Turns the view round |

### Block entities and tints

A block with a block entity gets one in the scene, drawn by its own block entity renderer, as in the world: a chest's
lid, a machine's moving parts, what a jar holds. `data` is what it holds, written as the block entity saves it (as for
`/setblock`): a JSON object, or the same as SNBT text.

```json
{"type": "show", "pos": [2, 1, 2], "state": "dust:dust_jar", "data": {"contents": {"element": "dust:fire", "amount": 300}}}
{"type": "data", "pos": [2, 1, 2], "data": {"contents": {"amount": 250}}}
{"type": "show", "pos": [3, 1, 2], "state": "minecraft:chest[facing=south]", "data": "{Items: [{Slot: 0b, id: \"minecraft:apple\", count: 3}]}"}
```

Each `show`, `place` or `data` step sets its blocks' block entities up afresh from their data, and they play on from
there (see "Block entities that move", below). Jumping back along the timeline sets them up again.

A block is tinted as in the world (grass and leaves as in plains, a jar's lid the colour of what's in it), worked out
from the scene's own blocks and block entities. `tint` (`"#RRGGBB"`) draws every tinted part of its model that colour
instead: the dust in a filled Glass Dust Tube (`"state": "dust:dust_tube[west=tube,east=tube,filled=true]", "tint": "#FF0000"`).

`item` steps are still there for things that aren't blocks: what's held, what's thrown, an item on its way somewhere.

Keep the words in your lang file (`{"translate": "..."}`) so they can be translated. Dust's own scenes are in
`src/main/resources/assets/dust/guides/` as examples.

## Adding a guide in code

Subscribe to `RegisterGuidesEvent`. It's fired once on every mod's event bus during client setup.

```java
@EventBusSubscriber(modid = MyMod.MODID, value = Dist.CLIENT)
public final class MyGuides
{
    @SubscribeEvent
    public static void register(RegisterGuidesEvent event)
    {
        BlockState press = MyBlocks.PRESS.get().defaultBlockState();
        event.register(Identifier.fromNamespaceAndPath(MyMod.MODID, "press/basics"),
                GuideScene.builder(Component.translatable("guide.mymod.press.title"))
                        .items(MyItems.PRESS.get())
                        .show(new BlockPos(2, 1, 2), press).idle(10)
                        .text(Component.translatable("guide.mymod.press.1"), new Vec3(2.5, 2, 2.5), 80)
                        .click(new Vec3(2.5, 2, 2.5), true, Items.IRON_INGOT, 40).idle(40)
                        .item("plate", Items.IRON_INGOT, new Vec3(2.5, 2.6, 2.5), new Vec3(2.5, 2, 2.5), 15).idle(60)
                        .build());
    }
}
```

The builder has a method for each step type. `step(GuideStep)` adds any step with every field set.

To open a guide from your own code (a button in a book, say), call `GuideClient.open(stack, currentScreen)`.
Closing the guide goes back to `currentScreen`.

## Block entities that move

In a guide a block entity has no level: it's made with `EntityBlock.newBlockEntity`, loaded from the scene's `data`, and
drawn by its renderer at full light. Most renderers work as they are. Two things need a word in yours:

- **Time and neighbours.** A renderer that animates by `level.getGameTime()`, or looks at the blocks round it, asks
  `GuideContext` instead: `GuideContext.ticks(blockEntity.getLevel(), partialTick)` is the game time in the world and
  the scene's time in a guide (so it pauses, replays and jumps with the scene), and
  `GuideContext.blocks(blockEntity.getLevel())` is the level, or the scene's blocks and block entities.
- **Playing on.** Nothing comes from the server in a guide. A block entity that moves by counting ticks on the client
  (how far into a phase it is) implements `GuideAnimated`: the guide calls `guideTick()` once for every tick since the
  scene set it up. It can just count as on the client, or run its whole cycle as the server would, without the world
  (no particles, sounds or level). Dust's mixers and centrifuge run their cycle that way: a scene fills them with
  `data` steps, then starts them with one more (`{"phase": "CLOSING"}`), and they mix, tip out and tip back by
  themselves.

A block entity that can't be made, loaded or drawn without a level is left out of the scene (and logged), never
crashing the guide.
