package com.mowmaster.dust.Features.Research;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// What a player is looking at, for research: the nearest researchable mob, dropped item, block or fluid along their view within
// observing range (10 blocks, 64 through a spyglass). Used on the server to observe (ResearchObserver) and on the
// client to open the book at that entry (sneak + right-click, or the activation key).
//
// Fluids: from outside a fluid, its surface counts (water and lava have entries). With your eyes inside a fluid you
// look through it, so you can observe what's in the water with you.
public final class ResearchLook
{
    public static final double RANGE = 10.0;
    public static final double SPYGLASS_RANGE = 64.0;

    public record Target(ResearchEntry entry, BlockPos pos) {}

    private ResearchLook() {}

    public static @Nullable Target lookedAt(Player player)
    {
        Level level = player.level();
        double range = player.isScoping() ? SPYGLASS_RANGE : RANGE;
        Vec3 eye = player.getEyePosition();
        Vec3 view = player.getViewVector(1.0F);
        Vec3 end = eye.add(view.scale(range));

        // Solid blocks always; the fluid surface too unless the eyes are already in a fluid
        boolean eyesInFluid = !level.getFluidState(BlockPos.containing(eye)).isEmpty();
        BlockHitResult solid = level.clip(new ClipContext(eye, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        BlockHitResult withFluid = eyesInFluid ? solid : level.clip(new ClipContext(eye, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, player));
        boolean fluidFirst = withFluid.getType() == HitResult.Type.BLOCK && withFluid != solid
                && (solid.getType() == HitResult.Type.MISS || eye.distanceToSqr(withFluid.getLocation()) < eye.distanceToSqr(solid.getLocation()) - 1.0E-4);
        BlockHitResult blockHit = fluidFirst ? withFluid : solid;

        // Mobs in front of whatever block / fluid surface was hit
        Vec3 entityEnd = blockHit.getType() == HitResult.Type.MISS ? end : blockHit.getLocation();
        AABB search = player.getBoundingBox().expandTowards(view.scale(range)).inflate(1.0);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(player, eye, entityEnd, search,
                // Mobs, and dropped items (which can't normally be "picked" by the crosshair)
                entity -> ((entity instanceof LivingEntity && entity.isPickable()) || entity instanceof ItemEntity) && !entity.isSpectator() && entity != player,
                eye.distanceToSqr(entityEnd));
        if (entityHit != null) {
            ResearchEntry entry = ResearchTabs.entryForEntity(entityHit.getEntity());
            if (entry != null) return new Target(entry, entityHit.getEntity().blockPosition());
        }

        if (blockHit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = blockHit.getBlockPos();
            // The fluid's own block (water, lava), even in a waterlogged block
            BlockState state = fluidFirst ? level.getFluidState(pos).createLegacyBlock() : level.getBlockState(pos);
            ResearchEntry entry = ResearchTabs.entryForBlock(state);
            if (entry != null) return new Target(entry, pos);
        }
        return null;
    }
}
