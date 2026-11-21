package com.mowmaster.dust.Features.Apparatus.still.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.Compounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

// Flat pictures drawn on blocks by the Still's and the dust machines' renderers: a Jar Label (a slip of paper with an
// element's icon) and an element's icon on its own. Drawn as on a block's north face at a depth (pixels from the north
// edge; below 0 stands just out in front of it) and turned round the block's middle to the side wanted.
public final class LabelQuads
{
    public static final RenderType LABEL = RenderTypes.entityCutout(Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/station/jar_label.png"));

    private LabelQuads() {}

    // An element's icon texture (its GUI sprite drawn as a texture of its own), or null if the local player hasn't
    // discovered it (so nothing gives it away)
    public static @Nullable Identifier iconFor(@Nullable DustElement element)
    {
        var player = Minecraft.getInstance().player;
        if (element == null || player == null || !Compounds.knows(player, element)) return null;
        return Identifier.fromNamespaceAndPath(element.icon().getNamespace(), "textures/gui/sprites/" + element.icon().getPath() + ".png");
    }

    // A label on a side: the paper (left-right, bottom-top in pixels, at depth zPixels) and the icon on it
    public static void label(Direction side, float left, float bottom, float right, float top, float zPixels, @Nullable Identifier icon, int light,
                             PoseStack poseStack, SubmitNodeCollector collector)
    {
        poseStack.pushPose();
        turnTo(side, poseStack);
        quad(LABEL, left, bottom, right, top, zPixels, light, poseStack, collector);
        if (icon != null) {
            float w = (right - left) * 0.7F, cx = (left + right) / 2, cy = bottom + (top - bottom) * 0.55F;
            quad(RenderTypes.entityCutout(icon), cx - w / 2, cy - w / 2, cx + w / 2, cy + w / 2, zPixels - 0.02F, light, poseStack, collector);
        }
        poseStack.popPose();
    }

    // Just an icon on a side
    public static void icon(Direction side, Identifier icon, float left, float bottom, float right, float top, float zPixels, int light,
                            PoseStack poseStack, SubmitNodeCollector collector)
    {
        poseStack.pushPose();
        turnTo(side, poseStack);
        quad(RenderTypes.entityCutout(icon), left, bottom, right, top, zPixels, light, poseStack, collector);
        poseStack.popPose();
    }

    // Turns what's drawn for the north face round the block's middle to this side (180 - toYRot: north 0, east -90, ...)
    public static void turnTo(Direction side, PoseStack poseStack)
    {
        poseStack.translate(0.5, 0, 0.5);
        poseStack.rotate(Axis.YP.rotationDegrees(180 - side.toYRot()));
        poseStack.translate(-0.5, 0, -0.5);
    }

    // A flat square on the north face, the whole texture on it, drawn from both sides. Seen from the north, left is +x.
    public static void quad(RenderType type, float left, float bottom, float right, float top, float zPixels, int light,
                            PoseStack poseStack, SubmitNodeCollector collector)
    {
        float x0 = left / 16, x1 = right / 16, y0 = bottom / 16, y1 = top / 16, z = zPixels / 16;
        int color = 0xFFFFFFFF;
        collector.submitCustomGeometry(poseStack, type, (pose, buffer) -> {
            vertex(buffer, pose, 1 - x0, y1, z, 0, 0, color, light, -1);
            vertex(buffer, pose, 1 - x0, y0, z, 0, 1, color, light, -1);
            vertex(buffer, pose, 1 - x1, y0, z, 1, 1, color, light, -1);
            vertex(buffer, pose, 1 - x1, y1, z, 1, 0, color, light, -1);
            vertex(buffer, pose, 1 - x1, y1, z, 1, 0, color, light, 1);
            vertex(buffer, pose, 1 - x1, y0, z, 1, 1, color, light, 1);
            vertex(buffer, pose, 1 - x0, y0, z, 0, 1, color, light, 1);
            vertex(buffer, pose, 1 - x0, y1, z, 0, 0, color, light, 1);
        });
    }

    private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int color, int light, float normalZ)
    {
        buffer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0, 0, normalZ);
    }
}
