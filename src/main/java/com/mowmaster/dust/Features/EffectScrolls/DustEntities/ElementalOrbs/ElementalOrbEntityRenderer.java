package com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mowmaster.dust.DustReferences;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

// The renderer for element orbs: a camera-facing quad of the orb texture, tinted with the orb's element colour (read
// from the entity each frame, so one renderer covers every element). Based on vanilla's ExperienceOrbRenderer.
// <T extends ElementOrbEntity> lets the same class render any orb subclass.
public class ElementalOrbEntityRenderer<T extends ElementOrbEntity> extends EntityRenderer<T, ElementalOrbEntityRenderState> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/entity/fire_orb.png");
    // 26.3: entityTranslucentCullItemTarget was removed; vanilla's XP orb uses entityTranslucent
    private static final RenderType RENDER_TYPE = RenderTypes.entityTranslucent(TEXTURE);

    public ElementalOrbEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    @Override
    protected int getBlockLightLevel(T entity, BlockPos blockPos) {
        return Mth.clamp(super.getBlockLightLevel(entity, blockPos) + 7, 0, 15);
    }

    @Override
    public void submit(ElementalOrbEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        int icon = state.icon;
        float u0 = (float)(icon % 4 * 16 + 0) / 64.0F;
        float u1 = (float)(icon % 4 * 16 + 16) / 64.0F;
        float v0 = (float)(icon / 4 * 16 + 0) / 64.0F;
        float v1 = (float)(icon / 4 * 16 + 16) / 64.0F;
        int rc = (state.color >> 16) & 0xFF;
        int gc = (state.color >> 8) & 0xFF;
        int bc = state.color & 0xFF;
        poseStack.translate(0.0F, 0.1F, 0.0F);
        // 26.3: PoseStack#mulPose(Quaternionf) was renamed to rotate
        poseStack.rotate(camera.orientation);
        poseStack.scale(0.2F, 0.2F, 0.2F);
        submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) -> {
            vertex(buffer, pose, -0.5F, -0.25F, rc, gc, bc, u0, v1, state.lightCoords);
            vertex(buffer, pose, 0.5F, -0.25F, rc, gc, bc, u1, v1, state.lightCoords);
            vertex(buffer, pose, 0.5F, 0.75F, rc, gc, bc, u1, v0, state.lightCoords);
            vertex(buffer, pose, -0.5F, 0.75F, rc, gc, bc, u0, v0, state.lightCoords);
        });
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, int r, int g, int b, float u, float v, int lightCoords) {
        buffer.addVertex(pose, x, y, 0.0F).setColor(r, g, b, 128).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ElementalOrbEntityRenderState createRenderState() {
        return new ElementalOrbEntityRenderState();
    }

    @Override
    public void extractRenderState(T entity, ElementalOrbEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.icon = entity.getIcon();
        int color = entity.getElement().color();
        // Very dark elements (chaos, void) as a dark grey, or the orb would be an invisible black square
        boolean tooDark = ((color >> 16) & 0xFF) < 0x32 && ((color >> 8) & 0xFF) < 0x32 && (color & 0xFF) < 0x32;
        state.color = tooDark ? 0x323232 : color;
    }
}
