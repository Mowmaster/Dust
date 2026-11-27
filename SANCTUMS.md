# Sanctum and labyrinth templates

Every sanctum is built from one structure template (an `.nbt` file) that you make in-game with a structure block. When
a sanctum generates, Dust picks a template, turns it a random way, swaps its stand-in blocks for the element's own
blocks, fills its empty chests and sets up its portal.

The labyrinths behind the sanctums' portals are built from templates too: an entrance hall, one room template per
cell of the maze, corridors and an arena (see [Labyrinth templates](#labyrinth-templates) at the end). They use the
same stand-in blocks, data markers and structure void as sanctums.

The code is in `com.mowmaster.dust.Features.Sanctums` (`TemplateScan` reads a template, `SanctumTemplates` finds a
sanctum's parts, `SanctumProcessor` swaps the blocks, `SanctumPiece` places it, `SanctumStructure` picks where) and
`com.mowmaster.dust.Features.Labyrinth` (`LabyrinthBuilder`).

## Where templates go, and their names

A template's id is `dust:sanctum/<name>`. Minecraft looks for it in:

- the mod: `src/main/resources/data/dust/structure/sanctum/<name>.nbt`
- a datapack: `data/dust/structure/sanctum/<name>.nbt`
- the world's `generated/dust/structures/sanctum/<name>.nbt`, where a structure block saves. This copy wins in that
  world.

Names:

| Name                                  | Used by |
|---------------------------------------|---------|
| `sanctum`, `sanctum_2`, `sanctum_3`…  | every element that has no templates of its own |
| `<set>`, `<set>_2`, `<set>_3`…        | only that element, e.g. `pyreborn`, `tidewoven_2` |

Numbers start at 2 and can't skip: the first missing number ends the list. Each sanctum picks one of its list at random.
The sets are `pyreborn`, `tidewoven`, `rootbound`, `lumen`, `riftborn`, `sparkwright`, `blastward`, `rimefrost`,
`deepwatch`, `bastion`, `mirewalker`, `zephyr`, `verdant` and `gilded`.

The mod ships `sanctum.nbt`: the layout the code used to build (27 × 21 × 39), with two Arcane Vaults in the portal
chamber.

## Stand-in blocks

Build with these and each sanctum gets its element's blocks (`SanctumPalette` lists each element's):

| Build with                     | Becomes |
|--------------------------------|---------|
| White concrete                 | the element's wall block (one in five is its second wall block) |
| Light gray concrete            | the element's second wall block |
| Gray concrete                  | the element's floor |
| Cyan concrete                  | the element's crystal block |
| Yellow concrete                | the element's light |
| Oak log, wood, stripped log and wood, planks, stairs, slab, fence, fence gate, button, pressure plate, leaves | the same block of the element's wood (facing, shape and so on kept) |
| Chest                          | the element's wood chest |

Everything else (stone stairs, glass, lanterns, carpets, item frames…) is placed as you built it. Entities saved with the
template (armor stands, paintings, item frames) are placed too, so don't save any mobs.

## Special blocks

**Arcane Spawner** (Dust's creative tab): place them where you like. Each takes the sanctum's element and starts
waiting. The portal opens once every one of them has been beaten. Each player who helps beat one has a 1 in 5 chance
of a Vault Key of its element (`spawner_key_chance` in Dust's config).

**Arcane Vault** (creative tab): place it with its keyhole facing where the loot should come out. It takes the
sanctum's element. A Vault Key of that element opens it once for each player, and it gives out loot as good as that
player's Magic Level. When the sanctum resets (after a labyrinth run from it is won), everyone can open its vaults again.

**Sanctum Keystone** (creative tab): use exactly one. It goes in the bottom row of the portal frame, **directly under
one of the two bottom blocks of the opening**. The opening must be air, 2 blocks wide and 3 tall, with solid frame
blocks on both sides, so it's exactly 2 wide one way. The portal faces whichever side has more open space in front of
it. Players come back from the labyrinth up to 4 blocks out in front of it (on the keystone's side of the opening),
so keep that spot clear with a floor under it, or add a `return` marker. A keystone placed by hand does nothing; only
a generated sanctum sets one up.

**Chests and barrels**: leave them empty and they get the element's loot (dust, crystals, its tree's fruit, maybe a
sapling, now and then a loot bag). Put items in one, or give it a loot table, and it keeps exactly that.

## Data markers

A data marker is a structure block in **Data** mode, its name typed in the box. Each marker is replaced by air.

| Name      | What it does |
|-----------|--------------|
| `surface` | Stand it on the shrine's floor, where a player standing on the ground would be. The template is placed so that this spot is at ground level. The layer below it is the ground layer: where the ground dips under it, wall blocks fill the gap (up to 8 deep), and a spot is only used if the ground at that layer's corners is within 4 blocks of level. Without a `surface` marker, the template's bottom layer goes into the ground's top layer, centred (good for buildings that sit on the surface). |
| `return`  | Optional: where players come back to from the labyrinth, facing away from the portal. |

`dust:surface` and `dust:return` work too.

## Air and structure void

- **Air** in the template is placed as air: it carves rooms, the shaft and the space above the shrine out of the ground.
- **Structure void** (`minecraft:structure_void`) is skipped, so the ground there stays as it was.

A structure block saves everything in its box except structure void. Anything in the box that isn't part of your
build must be structure void, or the sanctum carves a big empty box out of the ground. In Save mode, turn on
**Show Invisible Blocks** to see air and structure void as small coloured boxes.

## Editing the default sanctum

1. In a creative world (a superflat is easiest), get a structure block: `/give @s minecraft:structure_block`.
2. Place it somewhere with open air above. Say it's at X Y Z. Fill the space the template will take with structure void,
   so the parts the template leaves alone are still structure void when you save:
   `/fill X Y+1 Z X+26 Y+21 Z+38 minecraft:structure_void` (work out the numbers; the default is 27 × 21 × 39).
3. In the structure block: **Load** mode, name `dust:sanctum/sanctum`, press LOAD (it shows the outline), then LOAD
   again to place it.
4. Change it. Keep the one keystone with its 2 × 3 opening, and the `surface` marker on the shrine's floor.
5. Switch the structure block to **Save** mode with the same name, relative position `0 1 0` and size `27 21 39`
   (bigger if you need it, up to 48 each way), and press SAVE. The file goes to the world's
   `generated/dust/structures/sanctum/sanctum.nbt`, and sanctums that generate in that world from then on use it.
6. To put it in the mod, copy that file over `src/main/resources/data/dust/structure/sanctum/sanctum.nbt`, or add it
   beside it as `sanctum_2.nbt`, `pyreborn.nbt` and so on. Then delete the copy in the world's `generated` folder, or it
   keeps winning in that world.

## Building a new one

- Dig the rooms into natural ground, or build anywhere, using the stand-ins. Add the `surface` marker, one keystone in
  its frame, spawners and chests. A structure block saves at most 48 × 48 × 48.
- Then turn everything in the box that isn't your build into structure void, for example
  `/fill <corner> <corner> minecraft:structure_void replace minecraft:stone`, and again for dirt, deepslate,
  grass_block and so on. The air in your rooms stays air.
- Above ground, air left in the box flattens any hill it lands in. Replace that air with structure void too, except
  where you want the space kept clear (the old shrine clears 6 blocks above its platform).
- `/fill` changes at most 32,768 blocks at a time, so split big boxes into parts.

## Testing

- `/place structure dust:sanctum` builds a real one at your position. It fails over water or on steep ground, so try
  another spot. `/locate structure dust:sanctum` finds generated ones.
- `/place template dust:sanctum/sanctum` places the bare template (stand-ins and all, the keystone not set up). Use it
  only to look.
- A sanctum that was half generated when its template changed finishes with the new one, so judge changes on newly
  generated sanctums.

## 26.1.2

26.3 saves templates in a newer format that 26.1.2 can't read directly. The 26.1.2 build converts any 26.3-saved
sanctum template from the mod or a datapack as it loads (one line in the log). Templates saved in 26.1.2 work in both
versions. Blocks that only exist in 26.3 become air in 26.1.2.

## Labyrinth templates

A labyrinth run is built when a party first steps through a sanctum's portal. Its plan (which cells of the maze have
rooms, how they join, and each room's type) is still worked out by the code. Each piece is a template, in
`data/dust/structure/labyrinth/`:

| Name        | What it is |
|-------------|------------|
| `hall`      | Where players arrive. The maze starts through its **south** wall (+Z). |
| `combat`    | Arcane Spawners. Its doors seal while a wave is on. |
| `miniboss`  | A Herald wakes at its centre when a player comes in. |
| `puzzle`    | Labyrinth Runes in its walls; each is given an element as it's built. Lighting them all puts a chest at its centre. |
| `challenge` | A pit to cross. A fall below its centre's level puts you back at its `ledge` marker. |
| `treasure`  | Chests and Arcane Vaults. |
| `sanctuary` | Mends whoever stands in it. |
| `corridor`  | One length of corridor, running north to south (+Z). It's laid end to end between rooms, the last one cut short. |
| `arena`     | Where the Archon fights. Its way in is the middle of its **north** wall (-Z). |

As with sanctums, numbered variants (`combat_2`, `combat_3`…) are picked at random, and templates in a folder named after
an element's set (`labyrinth/pyreborn/combat`, …) are used for that element instead of the shared ones. The mod ships
defaults for all of them: today's rooms, three combat rooms, two puzzles, and a treasure room with two chests and two
vaults.

How the pieces fit:

- **Rooms are boxes.** A template's outer layer is its walls, floor and ceiling. Players can't break those, and the
  next run clears the whole box. Keep rooms within **23 × 23** (cells are 26 apart, leaving room for corridors) and
  odd-sized, so they have a middle.
- **The centre.** Each room is turned a random way and placed with its centre on its cell. The centre is the
  `centre` marker if it has one, otherwise the middle of the template standing on its bottom layer. That's the floor
  height corridors meet it at. A room whose floor isn't its bottom layer (the challenge's pit) needs a `centre` marker.
- **Doorways** are cut through the walls wherever a corridor meets a room, in line with its centre, as wide and high as
  the corridor's inside (3 × 4 by default). Keep the middle of each wall clear: a doorway is cut from the outside in
  until it reaches the room's inside, at most 4 blocks deep.
- **The corridor** template's outer layer is walls, floor and ceiling around its inside, and its length is how often
  it repeats (the default is 5 long with a light in the middle of its ceiling).
- **Markers:**
  - `spawn` (hall): where players arrive.
  - `exit` (hall): stand it in one of the bottom blocks of a 2 × 3 opening in a frame. The opening becomes the way out.
  - `boss` (arena): where the Archon wakes, and where the way out opens when it falls. It must line up with the middle
    of the arena's north wall, which is where the way in is cut.
  - `ledge` (challenge).
- **In rooms:** Arcane Spawners and Arcane Vaults take the run's element. Empty chests and barrels get the labyrinth's
  loot (richer in a challenge). Labyrinth Runes are each given an element.

Labyrinth Runes have no item: place one with `/setblock ~ ~ ~ dust:labyrinth_rune`. Edit a labyrinth template the same
way as a sanctum (load it with a structure block as `dust:labyrinth/combat` and so on, save it again). A change shows
up in the next run that's built, not in one already going.

