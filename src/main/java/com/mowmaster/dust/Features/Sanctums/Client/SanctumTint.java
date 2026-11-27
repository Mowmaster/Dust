package com.mowmaster.dust.Features.Sanctums.Client;

import com.mowmaster.dust.Features.Sanctums.ArcaneSpawnerBlock;
import com.mowmaster.dust.Features.Sanctums.ArcaneVaultBlock;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

// The Arcane Spawner's and Arcane Vault's runes and the Arcane Portal's sheet (tint index 0) in their element's colour
// (their SET), brightened so the dark elements still show (Chaos, all black: violet); a vault's runes are dim while
// nobody near can open it
public class SanctumTint implements BlockTintSource
{
    @Override
    public int color(BlockState state)
    {
        if (!state.hasProperty(ArcaneSpawnerBlock.SET)) return 0xFFFFFFFF;
        int rgb = bright(state.getValue(ArcaneSpawnerBlock.SET).element().color());
        if (state.hasProperty(ArcaneVaultBlock.STATE) && !state.getValue(ArcaneVaultBlock.STATE).lit()) rgb = dim(rgb);
        return 0xFF000000 | rgb;
    }

    // A third as bright
    private static int dim(int rgb)
    {
        return (((rgb >> 16) & 0xFF) / 3) << 16 | (((rgb >> 8) & 0xFF) / 3) << 8 | (rgb & 0xFF) / 3;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos)
    {
        return color(state);
    }

    public static int bright(int rgb)
    {
        int red = (rgb >> 16) & 0xFF, green = (rgb >> 8) & 0xFF, blue = rgb & 0xFF;
        int max = Math.max(red, Math.max(green, blue));
        if (max == 0) return 0x9050FF;
        if (max >= 0xC0) return rgb;
        return (red * 0xFF / max) << 16 | (green * 0xFF / max) << 8 | (blue * 0xFF / max);
    }
}
