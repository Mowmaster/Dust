package com.mowmaster.dust.Features.AncientLanguage.Client;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

// Colour of the glyph carving on a glyph stone (tint index 0 = the glyph overlay in the block model).
// Carved dark normally; once the local player has transcribed that stone, the same colour as the stone, so to them
// the face looks worn blank. Other players still see the glyph, because each client works this out for its own
// player. Colours are ARGB (0xFF = fully opaque).
public class GlyphStoneTint implements BlockTintSource
{
    public static final int CARVED = 0xFF2E2924;
    public static final int WORN = 0xFF7A766E;

    // In hand / inventory
    @Override
    public int color(BlockState state)
    {
        return CARVED;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos)
    {
        return TranscribedStonesClient.isTranscribed(pos) ? WORN : CARVED;
    }
}
