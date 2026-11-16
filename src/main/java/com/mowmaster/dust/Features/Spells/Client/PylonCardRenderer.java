package com.mowmaster.dust.Features.Spells.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mowmaster.dust.Features.Spells.pylons.PylonBlockEntity;
import com.mowmaster.dust.Features.Spells.pylons.PylonReading;
import com.mowmaster.dust.Features.Spells.pylons.SpellPylonBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// A modifier pylon's card: it rises out of the top like the Spell Pylon's element icon (SpellPylonRenderer.genie)
// while a player is near (PylonBlockEntity.clientTick), swirling up, stretched thin, then popping to size, and poofs
// away when they leave. It always faces the camera.
public class PylonCardRenderer implements BlockEntityRenderer<PylonBlockEntity, PylonCardRenderer.State>
{
    private static final int FULL_BRIGHT = 0xF000F0;
    // The card's size once it's out (blocks across), where it starts (the pylon's top) and how far it rises
    private static final float CARD_SIZE = 0.55F;
    private static final double BASE_Y = 1.0;
    private static final float SWIRL_RADIUS = 0.18F;
    private static final float SWIRL_SPEED = 0.6F;

    public PylonCardRenderer(BlockEntityRendererProvider.Context context)
    {
    }

    public static class State extends BlockEntityRenderState
    {
        @Nullable Identifier card;
        float appear;
        float time;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(PylonBlockEntity pylon, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(pylon, state, partialTick, cameraPos, crumblingOverlay);
        state.appear = pylon.appear(partialTick);
        state.card = state.appear > 0 ? PylonReading.cardTexture(pylon.modifier()) : null;
        state.time = pylon.getLevel() == null ? 0 : (pylon.getLevel().getGameTime() % 24000) + partialTick;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        if (state.card == null) return;
        float t = Mth.clamp(state.appear, 0F, 1F);
        float rise = 1 - (1 - t) * (1 - t) * (1 - t);
        float overshoot = 1.7F;
        float pop = 1 + (overshoot + 1) * (float) Math.pow(t - 1, 3) + overshoot * (float) Math.pow(t - 1, 2);
        float size = CARD_SIZE * Math.max(0.05F, pop);
        float swirl = SWIRL_RADIUS * (1 - rise);
        float angle = state.time * SWIRL_SPEED;
        float bob = t >= 1 ? Mth.sin(state.time * 0.1F) * 0.04F : 0;
        double y = BASE_Y + SpellPylonBlockEntity.ICON_RISE * rise + bob;

        poseStack.pushPose();
        poseStack.translate(0.5 + Mth.cos(angle) * swirl, y, 0.5 + Mth.sin(angle) * swirl);
        poseStack.rotate(camera.orientation);
        poseStack.scale(size * (0.35F + 0.65F * t), size * (1.6F - 0.6F * t), size);
        int alpha = Math.round(255 * Math.min(1F, t * 2));
        int color = (alpha << 24) | 0xFFFFFF;
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(state.card), (pose, buffer) -> {
            vertex(buffer, pose, -0.5F, -0.5F, 0, 1, color, 1);
            vertex(buffer, pose, 0.5F, -0.5F, 1, 1, color, 1);
            vertex(buffer, pose, 0.5F, 0.5F, 1, 0, color, 1);
            vertex(buffer, pose, -0.5F, 0.5F, 0, 0, color, 1);
            vertex(buffer, pose, -0.5F, 0.5F, 0, 0, color, -1);
            vertex(buffer, pose, 0.5F, 0.5F, 1, 0, color, -1);
            vertex(buffer, pose, 0.5F, -0.5F, 1, 1, color, -1);
            vertex(buffer, pose, -0.5F, -0.5F, 0, 1, color, -1);
        });
        poseStack.popPose();
    }

    private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float u, float v, int color, float normalZ)
    {
        buffer.addVertex(pose, x, y, 0).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(FULL_BRIGHT).setNormal(pose, 0, 0, normalZ);
    }

    // The card floats above the block
    @Override
    public AABB getRenderBoundingBox(PylonBlockEntity pylon)
    {
        return new AABB(pylon.getBlockPos()).expandTowards(0, 1.5, 0);
    }
}
