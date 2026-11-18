package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Machines.CrusherLayout;
import com.mowmaster.dust.Features.Machines.ElementReplicatorBlockEntity;
import com.mowmaster.dust.Features.Machines.StationBlock;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import static com.mowmaster.dust.Features.Machines.ReplicatorLayout.*;

// The Element Replicator's moving parts (the frame and both sigils are the block model):
//   the item to copy   standing on the bottom sigil, slowly turning
//   the scan           the bottom sigil's glyph (scroll_copy_glyph) spinning up through the item and back down
//   the print          the top sigil's glyph (scroll_paste_glyph) spinning, then rising as the copy grows up beneath it
//                      (glowing while it's printed); once the glyph is at the top it's gone and the copy is done
//   the copy           standing on the top sigil, turning with the item it was copied from
// The copy grows taller rather than being uncovered a layer at a time: an item's model can't be cut at a height
// without reaching into how items are drawn, which differs between Minecraft versions.
public class ElementReplicatorRenderer implements BlockEntityRenderer<ElementReplicatorBlockEntity, ElementReplicatorRenderer.State>
{
    private static final Identifier COPY_GLYPH = Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/station/scroll_copy_glyph.png");
    private static final Identifier PASTE_GLYPH = Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/station/scroll_paste_glyph.png");
    // How fast the items turn and the glyphs spin (degrees a tick)
    private static final float TURN_SPEED = 1.5F;
    private static final float SPIN_SPEED = 12F;
    private static final AABB FLAT_ITEM = new AABB(-0.25, -0.125, -0.03125, 0.25, 0.375, 0.03125);

    private final ItemModelResolver itemModelResolver;

    public ElementReplicatorRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState template = new ItemStackRenderState();
        // The copy being printed, or the finished one
        final ItemStackRenderState copy = new ItemStackRenderState();
        Direction facing = Direction.NORTH;
        float progress = -1;
        // Ticks, with the partial tick: the items' turning
        float time;
        float scanLift;
        float printed;
        boolean printing;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(ElementReplicatorBlockEntity replicator, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(replicator, state, partialTick, cameraPos, crumblingOverlay);
        state.facing = replicator.getBlockState().getValue(StationBlock.FACING);
        state.progress = replicator.progress(partialTick);
        state.time = replicator.getLevel() == null ? 0 : replicator.getLevel().getGameTime() + partialTick;
        state.scanLift = scanLift(state.progress);
        state.printed = printed(state.progress);
        state.printing = state.progress >= PRINT_START;
        ItemStack copy = state.printing ? replicator.getCopy() : replicator.getOutput();
        itemModelResolver.updateForTopItem(state.template, replicator.getTemplate(), ItemDisplayContext.GROUND, replicator.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(state.copy, copy, ItemDisplayContext.GROUND, replicator.getLevel(), null, 1);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        float turn = (state.time * TURN_SPEED) % 360F;
        standing(state.template, BOTTOM_SIGIL, state.facing, turn, 1F, state.lightCoords, poseStack, submitNodeCollector);
        if (state.progress >= 0 && !state.printing) {
            // The scan: up through the item, then back down while the dust flows in
            StationRendering.glyph(COPY_GLYPH, BOTTOM_SIGIL.add(0, SCAN_HEIGHT * state.scanLift, 0), SIGIL_SIZE, SIGIL_SIZE,
                    (state.progress * SPIN_SPEED) % 360F, state.progress, state.facing, poseStack, submitNodeCollector);
        }
        if (!state.printing) {
            standing(state.copy, TOP_SIGIL, state.facing, turn, 1F, state.lightCoords, poseStack, submitNodeCollector);
            return;
        }
        // The print: the copy grows up under the glyph, glowing; the glyph rides its top edge
        float height = state.printed > 0
                ? standing(state.copy, TOP_SIGIL, state.facing, turn, state.printed, StationRendering.FULL_BRIGHT, poseStack, submitNodeCollector)
                : 0;
        StationRendering.glyph(PASTE_GLYPH, TOP_SIGIL.add(0, height * state.printed, 0), SIGIL_SIZE, SIGIL_SIZE,
                ((state.progress - PRINT_START) * SPIN_SPEED) % 360F, state.progress, state.facing, poseStack, submitNodeCollector);
    }

    // An item standing upright on a point (pixels, model facing north), as it looks on the ground, sized to fit
    // (ITEM_HEIGHT tall, or narrower than ITEM_WIDTH), turned yaw degrees, and grown to grow (0-1) of its height from
    // the bottom up. Returns its full height in pixels (0 if there's nothing to draw).
    // getModelBoundingBox: the box round the item's model as drawn on the ground (in blocks, about its middle), so its
    // bottom can be put on the point whatever its shape.
    private static float standing(ItemStackRenderState item, Vec3 pixels, Direction facing, float yaw, float grow, int light,
                                  PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        if (item.isEmpty()) return 0;
        AABB box = item.getModelBoundingBox();
        // A model that doesn't say how big it is: sized as a flat item on the ground (half a block, its bottom an eighth
        // below its middle), rather than blown up to fit
        if (box.maxY - box.minY < 1.0E-3) box = FLAT_ITEM;
        double height = box.maxY - box.minY;
        double width = Math.max(Math.max(box.maxX - box.minX, box.maxZ - box.minZ), 0.01);
        float scale = (float) Math.min(ITEM_HEIGHT / 16 / height, ITEM_WIDTH / 16 / width);
        Vec3 at = CrusherLayout.turned(pixels, facing);
        poseStack.pushPose();
        poseStack.translate(at.x, at.y, at.z);
        poseStack.rotate(Axis.YP.rotationDegrees(StationRendering.yaw(facing) + yaw));
        poseStack.scale(scale, scale * grow, scale);
        poseStack.translate(0, -box.minY, 0);
        item.submit(poseStack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
        return (float) (height * scale * 16);
    }
}
