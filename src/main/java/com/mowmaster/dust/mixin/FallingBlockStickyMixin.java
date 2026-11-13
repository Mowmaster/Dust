package com.mowmaster.dust.mixin;

import com.mowmaster.dust.Features.Spells.blocks.Sticking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// A falling block stuck to something by a slime patch (Slime: Features/Spells/blocks/Sticking) doesn't fall. A falling
// block checks whether to fall in its scheduled tick; this skips that tick while it's stuck. None of vanilla's falling
// blocks (sand, gravel, concrete powder, anvils, the dragon egg) override tick, so this covers them all.
@Mixin(FallingBlock.class)
public abstract class FallingBlockStickyMixin
{
    // HEAD + cancellable: runs first, and ci.cancel() returns from tick before any of it runs
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void dust$stayStuck(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci)
    {
        if (Sticking.isStuck(level, pos)) ci.cancel();
    }
}
