package com.mowmaster.dust.Features.Guide.Client;

import com.mojang.logging.LogUtils;
import com.mowmaster.dust.Features.Guide.GuideAnimated;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

// The block entities of the guide scene being drawn (GuideSceneRenderer). Each is made from its block
// (EntityBlock.newBlockEntity, with no level) and loaded with the scene's "data" for it, then played on (GuideAnimated)
// to how many ticks it's been since the scene set it up. It's kept from frame to frame while its block, data and start
// stay the same; jumping back along the timeline makes it afresh. One that can't be made, loaded, played or drawn is
// left out (and logged): a guide never takes the game down, whatever mod the block is from.
final class GuideBlockEntities
{
    private static final Logger LOGGER = LogUtils.getLogger();
    // The most ticks a block entity is played on for (a very long scene still opens at once)
    private static final int MAX_TICKS = 20 * 60 * 10;

    private static final class Entry
    {
        final BlockState state;
        final @Nullable CompoundTag data;
        final float since;
        @Nullable BlockEntity blockEntity;
        int ticked;

        Entry(GuideFrame.BlockDraw draw, @Nullable BlockEntity blockEntity)
        {
            this.state = draw.state();
            this.data = draw.data();
            this.since = draw.since();
            this.blockEntity = blockEntity;
        }

        boolean matches(GuideFrame.BlockDraw draw)
        {
            return state == draw.state() && since == draw.since() && Objects.equals(data, draw.data());
        }
    }

    private final Map<BlockPos, Entry> entries = new HashMap<>();
    private final Set<BlockPos> seen = new HashSet<>();

    void beginFrame()
    {
        seen.clear();
    }

    // Forgets the blocks that weren't in this frame
    void endFrame()
    {
        entries.keySet().retainAll(seen);
    }

    void clear()
    {
        entries.clear();
    }

    // The block entity of a block in this frame (null: it has none, or it couldn't be made), played on to ticks since
    // the scene set it up
    @Nullable BlockEntity get(GuideFrame.BlockDraw draw, int ticks)
    {
        BlockPos pos = draw.pos();
        seen.add(pos);
        if (!(draw.state().getBlock() instanceof EntityBlock block)) {
            entries.remove(pos);
            return null;
        }
        Entry entry = entries.get(pos);
        if (entry == null || !entry.matches(draw) || entry.ticked > ticks) {
            entry = new Entry(draw, make(block, draw));
            entries.put(pos, entry);
        }
        if (entry.blockEntity instanceof GuideAnimated animated) {
            int target = Math.min(ticks, MAX_TICKS);
            try {
                while (entry.ticked < target) {
                    animated.guideTick();
                    entry.ticked++;
                }
            } catch (RuntimeException e) {
                LOGGER.warn("Guide scene: couldn't play on the block entity of {}", draw.state(), e);
                entry.blockEntity = null;
            }
        }
        return entry.blockEntity;
    }

    // Its renderer failed: it's left out from now on (until the scene sets the block up again)
    void failed(BlockPos pos)
    {
        Entry entry = entries.get(pos);
        if (entry != null) entry.blockEntity = null;
    }

    private static @Nullable BlockEntity make(EntityBlock block, GuideFrame.BlockDraw draw)
    {
        try {
            BlockEntity blockEntity = block.newBlockEntity(draw.pos(), draw.state());
            var level = Minecraft.getInstance().level;
            if (blockEntity != null && draw.data() != null && level != null) {
                blockEntity.loadCustomOnly(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), draw.data()));
            }
            return blockEntity;
        } catch (RuntimeException e) {
            LOGGER.warn("Guide scene: couldn't make the block entity of {} from {}", draw.state(), draw.data(), e);
            return null;
        }
    }
}
