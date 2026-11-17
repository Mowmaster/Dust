package com.mowmaster.dust.Features.DustStorage.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustStorage.DustJarBlockEntity;
import com.mowmaster.dust.Features.DustStorage.DustJarContents;
import com.mowmaster.dust.Features.Machines.Client.StationRendering;
import com.mowmaster.dust.Features.Machines.Compounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// A Dust Jar's moving parts:
//   the dust inside, like the Element Crafter's jars: a box of the white dust block's texture tinted the element's
//     colour, as high as the jar is full (a thin layer at least, so a little dust still shows). The jar's inside, in
//     model pixels (dust_jar.json): 4-12 across both ways, from its floor (1) up to under the lid (11).
//   a Jar Label on each labelled side: a slip of paper on the glass with the element's icon (a "?" until the player has
//     discovered it) and how much is in the jar under it
public class DustJarRenderer implements BlockEntityRenderer<DustJarBlockEntity, DustJarRenderer.State>
{
    private static final RenderType DUST = RenderTypes.entityCutout(Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/blockofdust_white.png"));
    private static final RenderType LABEL = RenderTypes.entityCutout(Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/station/jar_label.png"));
    private static final double INSIDE_LOW = 4, INSIDE_HIGH = 12;
    private static final double FILL_BOTTOM = 1, FILL_TOP = 10.9;
    // Kept just off the glass, so the two don't flicker where they'd touch
    private static final double INSET = 0.05;

    // The label on a jar's north side, in pixels (the glass is at z = 3): the paper, and the icon on it
    private static final float LABEL_Z = 2.94F, ICON_Z = 2.92F, TEXT_Z = 2.92F;
    private static final float LABEL_LEFT = 5, LABEL_RIGHT = 11, LABEL_BOTTOM = 2.5F, LABEL_TOP = 10.5F;
    private static final float ICON_LEFT = 6, ICON_RIGHT = 10, ICON_BOTTOM = 6, ICON_TOP = 10;
    // The amount under the icon: its top this high (pixels), font pixels this big in blocks
    private static final float TEXT_TOP = 5.4F, TEXT_SCALE = 1.5F / 16 / 9;
    private static final int INK = 0xFF2A2018;

    public DustJarRenderer(BlockEntityRendererProvider.Context context)
    {
    }

    public static class State extends BlockEntityRenderState
    {
        int color = 0xFFFFFFFF;
        // How high the dust stands (pixels); FILL_BOTTOM: none
        double fill = FILL_BOTTOM;
        final List<Direction> labels = new ArrayList<>();
        // What the labels show: the element's icon texture (null: none set, or not discovered: a "?"), and the amount
        @Nullable Identifier icon;
        @Nullable FormattedCharSequence unknown;
        @Nullable FormattedCharSequence amount;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(DustJarBlockEntity jar, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(jar, state, partialTick, cameraPos, crumblingOverlay);
        DustElement element = jar.element();
        state.color = element == null ? 0xFFFFFFFF : 0xFF000000 | element.color();
        state.fill = fillHeight(jar.amount());
        state.labels.clear();
        state.labels.addAll(jar.labels());
        var player = Minecraft.getInstance().player;
        boolean known = element != null && player != null && Compounds.knows(player, element);
        // The element's GUI sprite, drawn as a texture of its own (textures/gui/sprites/element/<name>.png), untinted
        state.icon = known ? Identifier.fromNamespaceAndPath(element.icon().getNamespace(), "textures/gui/sprites/" + element.icon().getPath() + ".png") : null;
        state.unknown = element != null && !known ? Component.literal("?").getVisualOrderText() : null;
        state.amount = element == null ? null : Component.literal(String.valueOf(jar.amount())).getVisualOrderText();
    }

    // How high the dust stands for this much in the jar
    private static double fillHeight(int amount)
    {
        if (amount <= 0) return FILL_BOTTOM;
        double share = Math.min(1.0, amount / (double) DustJarContents.CAPACITY);
        return FILL_BOTTOM + Math.max(0.15, (FILL_TOP - FILL_BOTTOM) * share);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        if (state.fill > FILL_BOTTOM) {
            // The jar doesn't turn, so it's drawn as for a north-facing model
            StationRendering.box(DUST, INSIDE_LOW + INSET, FILL_BOTTOM, INSIDE_LOW + INSET, INSIDE_HIGH - INSET, state.fill, INSIDE_HIGH - INSET,
                    state.color, state.lightCoords, Direction.NORTH, poseStack, submitNodeCollector);
        }
        for (Direction side : state.labels) submitLabel(side, state, poseStack, submitNodeCollector);
    }

    // One label, drawn as if on the north side and then turned round the jar's middle to its own side
    // (180 - toYRot: north 0, east -90, south 180, west 90)
    private static void submitLabel(Direction side, State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.rotate(Axis.YP.rotationDegrees(180 - side.toYRot()));
        poseStack.translate(-0.5, 0, -0.5);
        quad(LABEL, LABEL_LEFT, LABEL_BOTTOM, LABEL_RIGHT, LABEL_TOP, LABEL_Z, 0xFFFFFFFF, state.lightCoords, poseStack, submitNodeCollector);
        if (state.icon != null) {
            quad(RenderTypes.entityCutout(state.icon), ICON_LEFT, ICON_BOTTOM, ICON_RIGHT, ICON_TOP, ICON_Z, 0xFFFFFFFF, state.lightCoords, poseStack, submitNodeCollector);
        } else if (state.unknown != null) {
            text(state.unknown, 9.4F, 2.4F / 16 / 9, state, poseStack, submitNodeCollector);
        }
        if (state.amount != null) text(state.amount, TEXT_TOP, TEXT_SCALE, state, poseStack, submitNodeCollector);
        poseStack.popPose();
    }

    // Centred text on the north face, its top at topPixels. Someone reading it stands north looking south (+z), so the
    // text is turned half round (as a name tag faces the camera) and flipped upright (the -y scale).
    private static void text(FormattedCharSequence line, float topPixels, float scale, State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        Font font = Minecraft.getInstance().font;
        poseStack.pushPose();
        poseStack.translate(8 / 16.0, topPixels / 16.0, TEXT_Z / 16.0);
        poseStack.rotate(Axis.YP.rotationDegrees(180));
        poseStack.scale(scale, -scale, scale);
        submitNodeCollector.submitText(poseStack, -font.width(line) / 2F, 0, line, false, Font.DisplayMode.NORMAL, state.lightCoords, INK, 0, 0);
        poseStack.popPose();
    }

    // A flat square on the north face (pixels), the whole texture on it, tinted color. Drawn from both sides, so it
    // shows whichever way the faces cull. Seen from the north, left is +x (u 0) and right is -x (u 1).
    private static void quad(RenderType type, float left, float bottom, float right, float top, float zPixels, int color, int light,
                             PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        float x0 = left / 16, x1 = right / 16, y0 = bottom / 16, y1 = top / 16, z = zPixels / 16;
        submitNodeCollector.submitCustomGeometry(poseStack, type, (pose, buffer) -> {
            vertex(buffer, pose, x1, y1, z, 0, 0, color, light, -1);
            vertex(buffer, pose, x1, y0, z, 0, 1, color, light, -1);
            vertex(buffer, pose, x0, y0, z, 1, 1, color, light, -1);
            vertex(buffer, pose, x0, y1, z, 1, 0, color, light, -1);
            vertex(buffer, pose, x0, y1, z, 1, 0, color, light, 1);
            vertex(buffer, pose, x0, y0, z, 1, 1, color, light, 1);
            vertex(buffer, pose, x1, y0, z, 0, 1, color, light, 1);
            vertex(buffer, pose, x1, y1, z, 0, 0, color, light, 1);
        });
    }

    private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int color, int light, float normalZ)
    {
        buffer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0, 0, normalZ);
    }
}
