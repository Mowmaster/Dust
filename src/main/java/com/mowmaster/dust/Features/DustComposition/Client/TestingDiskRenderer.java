package com.mowmaster.dust.Features.DustComposition.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.TestingDiskBlockEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.mowlib.client.SpinningItemRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// The Dust Testing Disk: its item turning on top (MowLib's SpinningItemRenderer), and a beam of each dust's colour
// shining out of the item. Each kind of dust always points the same way (five directions around the item, a little
// upwards), and its beam's length is that dust's share of everything in the item (all fire: MAX_LENGTH; a third
// fire: a third as long). A disk only ever shows the primal dusts, never which elements they make up.
// (Broken disks, still to come, won't show the shares: just which dusts are there.)
public class TestingDiskRenderer extends SpinningItemRenderer<TestingDiskBlockEntity>
{
    // The length of a beam for a dust that's all of the item
    private static final double MAX_LENGTH = 3.0;
    private static final float HALF_WIDTH = 0.025F;
    // How far above level the beams point (radians), and the angle between neighbouring dusts (a fifth of a turn)
    private static final double TILT = Math.toRadians(15);
    private static final double SPREAD = Math.PI * 2 / EnumElement.values().length;

    public TestingDiskRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    // SpinningItemRenderer's state, plus each dust's share of the item, 0-1 (indexed by EnumElement#ordinal), and the
    // item's height
    public static class State extends SpinningItemRenderer.State
    {
        final float[] shares = new float[EnumElement.values().length];
        double itemHeight;
    }

    @Override
    public SpinningItemRenderer.State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(TestingDiskBlockEntity disk, SpinningItemRenderer.State base, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        super.extractRenderState(disk, base, partialTick, cameraPos, crumblingOverlay);
        // A pattern-matching instanceof (Java 16+): checks the type and names the cast value in one go
        if (!(base instanceof State state)) return;
        DustComposition composition = disk.composition();
        int total = composition == null ? 0 : composition.total();
        for (EnumElement element : EnumElement.values()) {
            float share = total == 0 ? 0 : (float) composition.get(element) / total;
            // A disk that can't show shares gives every dust that's there the same short beam
            state.shares[element.ordinal()] = share > 0 && !disk.showsShares() ? 0.25F : share;
        }
        state.itemHeight = disk.getDisplayHeight();
    }

    @Override
    public void submit(SpinningItemRenderer.State base, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        super.submit(base, poseStack, submitNodeCollector, camera);
        if (!(base instanceof State state)) return;

        // The beams are drawn in camera space (each is turned to face the camera, which sits at 0,0,0). The pose
        // arrives moved to the block's corner, so move it back to the camera and give the beams camera-relative points.
        Vec3 corner = Vec3.atLowerCornerOf(state.blockPos).subtract(camera.pos);
        Vec3 from = corner.add(0.5, state.itemHeight, 0.5);
        poseStack.pushPose();
        poseStack.translate(-corner.x, -corner.y, -corner.z);
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.beaconBeam(BeaconRenderer.BEAM_LOCATION, true), (pose, buffer) -> {
            for (EnumElement element : EnumElement.values()) {
                float share = state.shares[element.ordinal()];
                if (share <= 0) continue;
                double length = share * MAX_LENGTH;
                double angle = element.ordinal() * SPREAD;
                Vec3 direction = new Vec3(Math.cos(angle) * Math.cos(TILT), Math.sin(TILT), Math.sin(angle) * Math.cos(TILT));
                // 0xFF000000 |: fully opaque. Black dust as a very dark grey, so it still reads as a beam.
                int color = 0xFF000000 | (element == EnumElement.CHAOS ? 0x202020 : DustComposition.pureColor(element));
                strip(pose, buffer, from, from.add(direction.scale(length)), color);
            }
        });
        poseStack.popPose();
    }

    // One beam from a to b (both relative to the camera): a flat strip turned to face the camera, at full brightness,
    // with a back face so it shows from either side. The beacon beam texture runs along it, once per block.
    // (MowLib's newer versions have this as BeamRenderer.strip; switch to that once Dust builds against one.)
    private static void strip(PoseStack.Pose pose, VertexConsumer buffer, Vec3 a, Vec3 b, int color)
    {
        Vec3 along = b.subtract(a);
        double length = along.length();
        if (length < 1.0E-3) return;
        // Sideways: at right angles both to the beam and to the line from the camera (at 0,0,0) to the beam's middle
        Vec3 side = along.cross(a.add(b).scale(0.5));
        if (side.lengthSqr() < 1.0E-8) return;
        side = side.normalize().scale(HALF_WIDTH);

        float v1 = (float) length;
        Vec3 a0 = a.subtract(side), a1 = a.add(side), b1 = b.add(side), b0 = b.subtract(side);
        vertex(pose, buffer, a0, 0, 0, color);
        vertex(pose, buffer, a1, 1, 0, color);
        vertex(pose, buffer, b1, 1, v1, color);
        vertex(pose, buffer, b0, 0, v1, color);
        // The back, wound the other way round
        vertex(pose, buffer, b0, 0, v1, color);
        vertex(pose, buffer, b1, 1, v1, color);
        vertex(pose, buffer, a1, 1, 0, color);
        vertex(pose, buffer, a0, 0, 0, color);
    }

    // The beacon beam render type's vertex format: position, colour, texture, overlay, light, normal.
    // 0xF000F0 is full block and sky light, so the beams glow in the dark.
    private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, Vec3 at, float u, float v, int color)
    {
        buffer.addVertex(pose, (float) at.x, (float) at.y, (float) at.z)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(0xF000F0)
                .setNormal(pose, 0, 1, 0);
    }

    // The block and the space the beams can reach. NeoForge skips renderers whose box is off screen, so without this
    // the beams would vanish whenever the disk itself is out of view.
    @Override
    public AABB getRenderBoundingBox(TestingDiskBlockEntity disk)
    {
        return new AABB(disk.getBlockPos()).inflate(MAX_LENGTH);
    }
}