package com.mowmaster.dust.Features.Guide.Client;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

// What a block entity renderer can ask about where its block entity is being drawn: in the world, or in a guide scene
// (GuideSceneRenderer draws the scene's block entities with their own renderers). A block entity in a guide has no level,
// so a renderer that uses its level for the time or to look round asks here instead:
//   ticks(level, partialTick)   the time to animate by: the level's game time, or the scene's time (so the parts pause,
//                               replay and jump with the scene)
//   blocks(level)               where to look round: the level, or the scene's blocks and block entities (null when
//                               neither: a block entity drawn somewhere else, without a level)
public final class GuideContext
{
    // The scene being drawn (only while GuideSceneRenderer draws its block entities), and its time
    private static @Nullable BlockGetter scene;
    private static float sceneTime;

    private GuideContext() {}

    static void begin(BlockGetter blocks, float time)
    {
        scene = blocks;
        sceneTime = time;
    }

    static void end()
    {
        scene = null;
    }

    // Whether a guide scene is being drawn just now
    public static boolean inGuide()
    {
        return scene != null;
    }

    // Ticks to animate by (fractions allowed): the level's game time (kept under an hour's worth, so a float stays exact
    // enough), or in a guide the scene's time; 0 for a block entity with no level anywhere else
    public static float ticks(@Nullable Level level, float partialTick)
    {
        if (level != null) return (level.getGameTime() % 72000L) + partialTick;
        return scene != null ? sceneTime : 0;
    }

    public static @Nullable BlockGetter blocks(@Nullable Level level)
    {
        return level != null ? level : scene;
    }
}
