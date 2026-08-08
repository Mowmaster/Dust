package com.mowmaster.dust.Features.DustEntities;

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

public class baseOrbRenderer extends EntityRenderer<baseOrbEntity, baseOrbRenderState> {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(
                    DustReferences.MODID,
                    "textures/entity/fire_orb.png"
            );
    private static final RenderType RENDER_TYPE;

    public baseOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    protected int getBlockLightLevel(baseOrbEntity entity, BlockPos blockPos) {
        return Mth.clamp(super.getBlockLightLevel(entity, blockPos) + 7, 0, 15);
    }

    public void submit(baseOrbRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        int icon = state.icon;
        float u0 = (float)(icon % 4 * 16 + 0) / 64.0F;
        float u1 = (float)(icon % 4 * 16 + 16) / 64.0F;
        float v0 = (float)(icon / 4 * 16 + 0) / 64.0F;
        float v1 = (float)(icon / 4 * 16 + 16) / 64.0F;
        int rc = 255;
        int gc = 0;
        int bc = 0;
        /*
        float r = 1.0F;
        float xo = 0.5F;
        float yo = 0.25F;
        float br = 255.0F;
        float rr = state.ageInTicks / 2.0F;
        int rc = 255;
        int gc = (int)((Mth.sin((double)(rr + 0.0F)) + 1.0F) * 0.5F * 100F);
        int bc = (int)((Mth.sin((double)(rr + 4.1887903F)) + 1.0F) * 0.1F * 100F)
        */
        poseStack.translate(0.0F, 0.1F, 0.0F);
        poseStack.mulPose(camera.orientation);
        float s = 0.3F;
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

    public baseOrbRenderState createRenderState() {
        return new baseOrbRenderState();
    }

    public void extractRenderState(baseOrbEntity entity, baseOrbRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.icon = entity.getIcon();
    }

    static {
        RENDER_TYPE = RenderTypes.entityTranslucentCullItemTarget(TEXTURE);
    }

}
