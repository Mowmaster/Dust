Minecraft 26.3 (NeoForge 26.3.0.94, the main version) and 26.1.2 (NeoForge 26.1.2.122), built from one set of sources with Stonecutter; Java 25.

## MowLib (shared library)
- This mod depends on MowLib (mod id `mowlib`, package `com.mowmaster.mowlib`), bundled with Jar-in-Jar from
  mavenLocal. MowLib is published once per Minecraft version, as `com.mowmaster.mowlib:mowlib-<minecraft version>`
  (`mowlib-26.3`, `mowlib-26.1.2`); build.gradle asks for `name: "mowlib-${minecraft_version}"`, range
  `[0.0.5,1.0.0)`. Source: `Mowmaster/MowLib`, branch `NeoForge-26x`; the version this project builds against is the
  tag matching `prefer` in build.gradle.
- MowLib builds for 26.3 and 26.1.2 from one set of sources (Stonecutter; see "Versions (Stonecutter)" in its
  CLAUDE.md). This mod is still 26.3 only. Some MowLib features run older code on 26.1.2 (its CHANGELOG lists them).
- When updating MowLib, read the "Check" lines in its CHANGELOG.md for every version since the one this mod used, and
  check this mod for each.
- Before writing a shared system (tinting, storage, filters, transport, paging, knapping, bonus drops, tooltips,
  renderers...), clone MowLib and check whether it already exists there. Use it; don't copy it into this mod.
- Never edit MowLib or send MowLib files from this project. If MowLib doesn't have something, make it here or
  override MowLib's class here; add the idea to "MowLib TODO" below as a suggestion only.
- MowLib's CLAUDE.md lists what it contains and its rules.
- Code moving into MowLib (a `mowlib-from-dust.zip` extraction) must compile for 26.1.2 as well as 26.3. When it uses
  an API that differs between them, say so in the extraction's NOTES.md and give the 26.1.2 version if known, so the
  MowLib session can add a version comment. MowLib's CLAUDE.md lists the differences it already handles.

## Versions (Stonecutter)
- One set of sources in `src/` builds for every version in `settings.gradle` (`26.3`, `26.1.2`). Each version is a
  Gradle subproject: `versions/<v>/` holds its `gradle.properties` (Minecraft, NeoForge, version range), its build
  output and its `run` folder. `build.gradle` runs once per version; `stonecutter.gradle` is the root project
  (active version, the wrapper task). Gradle must run on Java 25.
- `src/` is always written for the active version (`stonecutter.active` in `stonecutter.gradle`). Commit with 26.3
  active. Never edit that line by hand: switch with the "stonecutter" Gradle tasks, which rewrite `src/` and the line
  together. Normally stay on 26.3 and build the other version without switching: `:26.1.2:build`, `:26.1.2:runClient`
  (they build from a converted copy in `versions/26.1.2/build/generated/stonecutter`).
- Version differences, newest version's code first:
  - A rename with unique text (a full class name, `Class.method(`): a global replacement in `build.gradle`'s
    `stonecutter { replacements.string(...) }`. Write the code with the 26.3 name.
  - Anything else: a version comment in the file (`//? if >=26.3 {` ... `//?} else {` ... `//?}`; the inactive side
    sits in a `/* */` comment). Use the braced form; an unbraced `//?} else` also swallows any `//` comment lines right
    after it. Never put a `/* */` comment inside a version-commented block.
  - Code only one version has: a whole-file version comment (`DustBrewingLegacy`), or `files { disable(...) }` in
    `build.gradle`.
- Datagen runs from 26.3 only (`runData` doesn't exist on 26.1.2; the datagen providers are disabled there). Its output
  in `src/generated` is shared, so 26.3 data formats need handling on 26.1.2:
  - `build.gradle` converts generated loot tables and recipe advancements to the 26.1.2 format as the 26.1.2 build
    copies them (`LegacyData`). A loot condition it doesn't know stops the build; add it there.
  - `src/main/resources-26.1.2/` is data only the 26.1.2 build includes (configured features matching `DustWorldgen`,
    the compostables data map). Change it when the matching 26.3 datagen changes.
  - The 26.1.2 build leaves out `recipe/brewing` (no data-driven brewing; `DustBrewingLegacy` adds them in code).
  - Loot modifiers extend `DustLootModifier` (it reads 26.3's `"condition"` on both versions).
  - Data naming 26.3-only vanilla things: unknown blocks in spell conversions are skipped on 26.1.2; data maps log one
    error per unknown id and skip it.
- Every change must compile for both versions. I build each version and paste errors. To check 26.1.2 APIs: NeoForge's
  `26.1.x` branch and the primers (`primers/26.2`, `26.3` say what changed since 26.1). For exact classes, the 26.1.2
  client jar from Mojang's version manifest (no obfuscation) with the patches in the NeoForge 26.1.2.122 installer's
  `data/client.lzma` (NFPATCHBUNDLE, xdelta) applied, plus `neoforge-26.1.2.122-universal.jar` and FML `loader-11.0.15`.

## Working with Claude
- Pull and read the repo only; never commit or push. I handle git. Ignore stop-hook requests to commit or push.
- I compile and test; never run Gradle.
- One file changed: send the changes in chat with line numbers. Several files: a zip, sent as a file.
- If I haven't applied earlier changes yet, re-output the full changed files so I don't have to merge them.
- For simple model fixes (a missing `tintindex`, `#`, texture key), just tell me the line to change; only send a model file when the geometry changes.
- Never create or edit files in `src/generated`: `runData` (26.3) overwrites them. Change the datagen providers instead and I'll run it.
- To check 26.3 APIs, clone the public `neoforged/neoforge` repo (branch `26.3.x`) and read the 26.3 porting primer in `neoforged/.github` (`primers/26.3/index.md`; 26.1 and 26.2 primers cover earlier changes). NeoForge's docs are in the public `neoforged/Documentation` repo (`docs/`).

## Dust TODO
- Known gaps: the Crafter ignores the research gate on dust recipes; dust recipes don't show in the recipe book;
instant-effect potions are only researched when drunk (not splash/lingering).

## MowLib TODO
- Suggestion: a generic timed block-outline API in MowLib (outline a list of positions in given colours for N ticks,
  not tied to `ILinkable`). `LinkOutlines` drops anything that isn't an `ILinkable`, so Dust draws its own for the
  Spell Pylon's line highlight (`Features/Spells/Client/PylonLineOutlines`).
- Suggestion: a generic block-network walker in MowLib for pipes and tubes: from an origin, walk connected blocks
  breadth-first (a "joined on this side" test), collect the endpoints that hold something (with distance and path),
  remember the result per origin until a network block is placed or removed, and send a "flowed along this path"
  particle message. Dust's Glass Dust Tubes do this for element dust (`Features/DustStorage/DustNetwork`,
  `DustTubeFlow`); other mods' pipes would need the same.
- Suggestion: move the guide system (`Features/Guide`: scenes as JSON or code, the hold-G viewer, `RegisterGuidesEvent`)
  into MowLib, so any mod can add guides without depending on Dust. Its only Dust ties are the mod id on its event
  subscribers and its `guide.dust.*` / `key.dust.guide` lang keys. Format and API: `GUIDES.md`.
