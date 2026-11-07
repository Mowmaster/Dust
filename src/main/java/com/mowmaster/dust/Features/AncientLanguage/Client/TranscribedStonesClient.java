package com.mowmaster.dust.Features.AncientLanguage.Client;

import com.mowmaster.dust.Features.AncientLanguage.TranscribedStones;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.SectionPos;

// Client side of TranscribedStones
public final class TranscribedStonesClient
{
    private TranscribedStonesClient() {}

    // Block tints are worked out when a chunk section is built into a mesh, so a stone only changes look when its
    // section is rebuilt. Marks the sections of every stone that was added or removed as needing a rebuild.
    public static void redrawChanged(TranscribedStones before, TranscribedStones after)
    {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        for (GlobalPos stone : after.stones()) {
            if (!before.stones().contains(stone)) redraw(level, stone);
        }
        for (GlobalPos stone : before.stones()) {
            if (!after.stones().contains(stone)) redraw(level, stone);
        }
    }

    private static void redraw(ClientLevel level, GlobalPos stone)
    {
        if (!stone.dimension().equals(level.dimension())) return;
        level.setSectionDirtyWithNeighbors(
                SectionPos.blockToSectionCoord(stone.pos().getX()),
                SectionPos.blockToSectionCoord(stone.pos().getY()),
                SectionPos.blockToSectionCoord(stone.pos().getZ()));
    }

    // Has the local player transcribed the stone at this position (in the level being drawn)?
    public static boolean isTranscribed(net.minecraft.core.BlockPos pos)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) return false;
        return minecraft.player.getData(com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry.TRANSCRIBED_STONES)
                .contains(minecraft.level, pos);
    }
}
