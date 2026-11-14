package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mowmaster.dust.Features.Machines.CrusherLayout;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

// Drawing shared by the research stations' renderers:
//   flatItem    an item lying face up on the station (top pointing away from the front)
//   glyph       a scroll's glyph, glowing and spinning in the air (a square of a greyscale glyph texture, tinted)
//   liquid      a flat square of tinted liquid (the evaporation pot)
// Points are model pixels with the model facing north; they're turned to the block's facing (CrusherLayout.turned).
public final class StationRendering
{
    public static final int FULL_BRIGHT = 0xF000F0;
    // The glyph's glow, pulsing between these two colours (0xAARRGGBB)
    private static final int GLOW_BRIGHT = 0xFFF0E0FF;
    private static final int GLOW_DIM = 0x80A070FF;

    private StationRendering() {}

    // Clockwise quarter turns the blockstate gives each facing (the models face north)
    public static int quarterTurns(Direction facing)
    {
        return switch (facing) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        };
    }

    // The yaw (degrees about up) that turns something drawn for a north-facing model to the block's facing
    public static float yaw(Direction facing)
    {
        return -quarterTurns(facing) * 90F;
    }

    // An item lying face up at a point, turned extraYaw degrees more. The GROUND item model stands upright; tipping it
    // -90 degrees about x lays it face up with its top pointing away from the front.
    public static void flatItem(ItemStackRenderState item, Vec3 pixels, Direction facing, float extraYaw, float scale, int light,
                                PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        if (item.isEmpty()) return;
        Vec3 at = CrusherLayout.turned(pixels, facing);
        poseStack.pushPose();
        poseStack.translate(at.x, at.y, at.z);
        poseStack.rotate(Axis.YP.rotationDegrees(yaw(facing) + extraYaw));
        poseStack.rotate(Axis.XP.rotationDegrees(-90));
        poseStack.scale(scale, scale, scale);
        item.submit(poseStack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    // A glowing glyph: a width x depth (pixels) square centred on a point, spun spin degrees, at full brightness in a
    // colour pulsing with time (ticks). Drawn on both sides so it shows from above and below.
    // entityTranslucent: a texture drawn with see-through pixels blended, the way entities with transparency are.
    public static void glyph(Identifier texture, Vec3 centrePixels, double width, double depth, float spin, float time, Direction facing,
                             PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        float pulse = 0.5F + 0.5F * Mth.sin(time * 0.6F);
        square(RenderTypes.entityTranslucent(texture), centrePixels, width, depth, spin, lerpColor(GLOW_DIM, GLOW_BRIGHT, pulse), FULL_BRIGHT,
                facing, poseStack, submitNodeCollector);
    }

    // A flat square of texture, tinted color (0xAARRGGBB), lit with light
    public static void square(RenderType type, Vec3 centrePixels, double width, double depth, float spin, int color, int light, Direction facing,
                              PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        Vec3 centre = CrusherLayout.turned(centrePixels, facing);
        float halfX = (float) (width / 32);
        float halfZ = (float) (depth / 32);
        poseStack.pushPose();
        poseStack.translate(centre.x, centre.y, centre.z);
        poseStack.rotate(Axis.YP.rotationDegrees(yaw(facing) + spin));
        submitNodeCollector.submitCustomGeometry(poseStack, type, (pose, buffer) -> {
            vertex(buffer, pose, -halfX, -halfZ, 0, 0, color, light, 1);
            vertex(buffer, pose, -halfX, halfZ, 0, 1, color, light, 1);
            vertex(buffer, pose, halfX, halfZ, 1, 1, color, light, 1);
            vertex(buffer, pose, halfX, -halfZ, 1, 0, color, light, 1);
            vertex(buffer, pose, halfX, -halfZ, 1, 0, color, light, -1);
            vertex(buffer, pose, halfX, halfZ, 1, 1, color, light, -1);
            vertex(buffer, pose, -halfX, halfZ, 0, 1, color, light, -1);
            vertex(buffer, pose, -halfX, -halfZ, 0, 0, color, light, -1);
        });
        poseStack.popPose();
    }

    // A solid-looking box of texture, tinted color (0xAARRGGBB): its four sides and top (no bottom: it sits on
    // something), between two corners in model pixels (facing north), turned with the block. The texture is laid on at
    // one texture pixel per model pixel, so a small box shows part of it rather than the whole thing squashed.
    // Used for the Element Crafter's jars of dust.
    public static void box(RenderType type, double x0, double y0, double z0, double x1, double y1, double z1, int color, int light,
                           Direction facing, PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        float ax = (float) (x0 / 16), ay = (float) (y0 / 16), az = (float) (z0 / 16);
        float bx = (float) (x1 / 16), by = (float) (y1 / 16), bz = (float) (z1 / 16);
        // Texture spans: width and depth for the top, height for the sides (UVs 0-1 cover the texture's 16 pixels)
        float w = bx - ax, h = by - ay, d = bz - az;
        poseStack.pushPose();
        // Turn about the block's middle, as the blockstate turns the model
        poseStack.translate(0.5F, 0F, 0.5F);
        poseStack.rotate(Axis.YP.rotationDegrees(yaw(facing)));
        poseStack.translate(-0.5F, 0F, -0.5F);
        submitNodeCollector.submitCustomGeometry(poseStack, type, (pose, buffer) -> {
            // top (up)
            corner(buffer, pose, ax, by, az, 0, 0, color, light, 0, 1, 0);
            corner(buffer, pose, ax, by, bz, 0, d, color, light, 0, 1, 0);
            corner(buffer, pose, bx, by, bz, w, d, color, light, 0, 1, 0);
            corner(buffer, pose, bx, by, az, w, 0, color, light, 0, 1, 0);
            // north (-z)
            corner(buffer, pose, bx, by, az, 0, 0, color, light, 0, 0, -1);
            corner(buffer, pose, bx, ay, az, 0, h, color, light, 0, 0, -1);
            corner(buffer, pose, ax, ay, az, w, h, color, light, 0, 0, -1);
            corner(buffer, pose, ax, by, az, w, 0, color, light, 0, 0, -1);
            // south (+z)
            corner(buffer, pose, ax, by, bz, 0, 0, color, light, 0, 0, 1);
            corner(buffer, pose, ax, ay, bz, 0, h, color, light, 0, 0, 1);
            corner(buffer, pose, bx, ay, bz, w, h, color, light, 0, 0, 1);
            corner(buffer, pose, bx, by, bz, w, 0, color, light, 0, 0, 1);
            // west (-x)
            corner(buffer, pose, ax, by, az, 0, 0, color, light, -1, 0, 0);
            corner(buffer, pose, ax, ay, az, 0, h, color, light, -1, 0, 0);
            corner(buffer, pose, ax, ay, bz, d, h, color, light, -1, 0, 0);
            corner(buffer, pose, ax, by, bz, d, 0, color, light, -1, 0, 0);
            // east (+x)
            corner(buffer, pose, bx, by, bz, 0, 0, color, light, 1, 0, 0);
            corner(buffer, pose, bx, ay, bz, 0, h, color, light, 1, 0, 0);
            corner(buffer, pose, bx, ay, az, d, h, color, light, 1, 0, 0);
            corner(buffer, pose, bx, by, az, d, 0, color, light, 1, 0, 0);
        });
        poseStack.popPose();
    }

    private static void corner(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int color, int light,
                               float normalX, float normalY, float normalZ)
    {
        buffer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, normalX, normalY, normalZ);
    }

    private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float z, float u, float v, int color, int light, float normalY)
    {
        buffer.addVertex(pose, x, 0, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0, normalY, 0);
    }

    // How far a rising glyph is up, 0-1: up over its first 20 ticks, back down over its last 10 (total: how long the
    // whole run is; progress -1: idle). Mth.clamp keeps a value between two bounds.
    public static float lift(float progress, int total)
    {
        if (progress < 0) return 0;
        float up = Mth.clamp(progress / 20F, 0, 1);
        float down = Mth.clamp((total - progress) / 10F, 0, 1);
        return Math.min(up, down);
    }

    // Between two 0xAARRGGBB colours, t 0-1
    public static int lerpColor(int from, int to, float t)
    {
        int out = 0;
        for (int shift = 0; shift <= 24; shift += 8) {
            int a = (from >>> shift) & 0xFF, b = (to >>> shift) & 0xFF;
            out |= (Math.round(a + (b - a) * t) & 0xFF) << shift;
        }
        return out;
    }
}
