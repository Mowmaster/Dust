package com.mowmaster.dust.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mowmaster.dust.Features.Spells.blocks.Sticking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

// Slime patches move with their blocks when a piston pushes or pulls them (Features/Spells/blocks/Sticking). moveBlocks
// asks the resolver for the list of blocks to move once, just before moving them: this sees that list on its way and
// moves the patches one step the same way. Server only; clients get the result through the synced chunk data.
@Mixin(PistonBaseBlock.class)
public abstract class PistonBaseBlockStickyMixin
{
    @ModifyExpressionValue(method = "moveBlocks", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/piston/PistonStructureResolver;getToPush()Ljava/util/List;"))
    private List<BlockPos> dust$movePatches(List<BlockPos> toPush, @Local(argsOnly = true) Level level, @Local PistonStructureResolver resolver)
    {
        if (level instanceof ServerLevel) Sticking.moveWith(level, toPush, resolver.getPushDirection());
        return toPush;
    }
}
