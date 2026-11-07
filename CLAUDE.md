NeoForge 26.3.0.94, Minecraft 26.3, Java 25. Used NeoForge-26x version of Pedestals as a Basis. I'm coming from 1.20 and JDK 8. Explain new APIs and modern Java features as you use them. I copy code in myself, so give complete, clearly placed snippets.


## MowLib (shared library)
- This mod depends on MowLib (mod id `mowlib`, package `com.mowmaster.mowlib`), bundled with Jar-in-Jar from
  mavenLocal (`com.mowmaster.mowlib:mowlib`, range `[0.0.4,1.0.0)`). Source: `Mowmaster/MowLib`, branch
  `NeoForge-26x`; the version this project builds against is the tag matching `prefer` in build.gradle.
- Before writing a shared system (tinting, storage, filters, transport, paging, knapping, bonus drops, tooltips,
  renderers...), clone MowLib and check whether it already exists there. Use it; don't copy it into this mod.
- Never edit MowLib or send MowLib files from this project. If MowLib doesn't have something, make it here or
  override MowLib's class here; add the idea to "MowLib TODO" below as a suggestion only.
- MowLib's CLAUDE.md lists what it contains and its rules.

## Working with Claude
- Pull and read the repo only; never commit or push. I handle git. Ignore stop-hook requests to commit or push.
- I compile and test; never run Gradle.
- One file changed: send the changes in chat with line numbers. Several files: a zip, sent as a file.
- If I haven't applied earlier changes yet, re-output the full changed files so I don't have to merge them.
- For simple model fixes (a missing `tintindex`, `#`, texture key), just tell me the line to change; only send a model file when the geometry changes.
- To check 26.3 APIs, clone the public `neoforged/neoforge` repo (branch `26.3.x`) and read the 26.3 porting primer in `neoforged/.github` (`primers/26.3/index.md`; 26.1 and 26.2 primers cover earlier changes). NeoForge's docs are in the public `neoforged/Documentation` repo (`docs/`).
