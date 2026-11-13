package com.mowmaster.dust.mixin;

import com.mowmaster.dust.Features.Spells.blocks.Sticking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

// Blocks stuck together by slime patches move together, like slime blocks. A piston works out what it moves in
// PistonStructureResolver.resolve(); once that has its list, this adds every block stuck to one in it, using the
// resolver's own addBlockLine, which brings the push limit (12), immovable blocks (left behind) and what gets in the way
// along with it. Blocks added are checked in turn, so whole stuck structures come along.
@Mixin(PistonStructureResolver.class)
public abstract class PistonStructureResolverMixin
{
    // @Shadow: stands for the target class's own private field / method of the same name
    @Shadow @Final private Level level;
    @Shadow @Final private Direction pushDirection;
    @Shadow @Final private List<BlockPos> toPush;

    @Shadow
    private boolean addBlockLine(BlockPos pos, Direction direction)
    {
        throw new AssertionError();
    }

    // RETURN: just before resolve() returns. cir.getReturnValue() is what it was going to return; setReturnValue changes it.
    @Inject(method = "resolve", at = @At("RETURN"), cancellable = true)
    private void dust$pullStuckBlocks(CallbackInfoReturnable<Boolean> cir)
    {
        if (!cir.getReturnValueZ()) return;
        // The list grows as blocks are added, so it's walked by index
        for (int i = 0; i < toPush.size(); i++) {
            BlockPos pos = toPush.get(i);
            for (Direction side : Direction.values()) {
                // What's ahead is pushed already
                if (side == pushDirection) continue;
                BlockPos next = pos.relative(side);
                if (toPush.contains(next) || level.getBlockState(next).isAir() || !Sticking.stuckTogether(level, pos, side)) continue;
                if (!addBlockLine(next, side)) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }
    }
}
