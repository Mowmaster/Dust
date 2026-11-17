package com.mowmaster.dust.Features.Spells.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Spells.entities.TelekinesisCubeEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.RenderShape;

// Draws Telekinesis's cube the way vanilla draws a sulfur cube holding a block: the block (turned to the cube's yaw) inside
// a slightly bigger see-through jelly shell (18 pixels across around the 16-pixel block, as SulfurCubeModel's outer
// box). Our own shell texture and geometry, not vanilla's sulfur cube model: that's 26.3-only, and this builds for
// 26.1.2 too. The shell wobbles a little while the cube moves.
public class TelekinesisCubeRenderer extends EntityRenderer<TelekinesisCubeEntity, TelekinesisCubeRenderer.State>
{
    private static final RenderType SHELL = RenderTypes.entityTranslucent(Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/entity/telekinesis_cube.png"));
    // Half the shell's width, in blocks (9 of 16 pixels)
    private static final float SHELL_HALF = 9F / 16F;

    public TelekinesisCubeRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        this.shadowRadius = 0.5F;
    }

    public static class State extends EntityRenderState
    {
        final MovingBlockRenderState block = new MovingBlockRenderState();
        float yaw;
        float wobble;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(TelekinesisCubeEntity entity, State state, float partialTick)
    {
        super.extractRenderState(entity, state, partialTick);
        BlockPos at = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
        state.block.randomSeedPos = entity.blockPosition();
        state.block.blockPos = at;
        state.block.blockState = entity.carriedBlock();
        if (entity.level() instanceof ClientLevel level) {
            state.block.biome = level.getBiome(at);
            state.block.cardinalLighting = level.cardinalLighting();
            state.block.lightEngine = level.getLightEngine();
        }
        state.yaw = entity.getYRot();
        double speed = entity.getDeltaMovement().length();
        state.wobble = (float) Math.min(speed, 1.0) * 0.06F * Mth.sin(state.ageInTicks * 1.3F);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        poseStack.pushPose();
        // About the cube's middle (the entity's position is its bottom centre)
        poseStack.translate(0, 0.5, 0);
        poseStack.rotate(Axis.YP.rotationDegrees(-state.yaw));
        if (state.block.blockState != null && state.block.blockState.getRenderShape() == RenderShape.MODEL) {
            poseStack.pushPose();
            poseStack.translate(-0.5, -0.5, -0.5);
            // Stonecutter: 26.2 added the outline colour (the glow outline when the entity is glowing)
            //? if >=26.2 {
            collector.submitMovingBlock(poseStack, state.block, state.outlineColor);
            //?} else {
            /*collector.submitMovingBlock(poseStack, state.block);
            *///?}
            poseStack.popPose();
        }
        // The jelly: squashed a little one way and stretched the other as it wobbles
        poseStack.scale(1 + state.wobble, 1 - state.wobble, 1 + state.wobble);
        shell(poseStack, collector, state.lightCoords);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    // A cube SHELL_HALF each way from the middle, the whole texture on each face
    private static void shell(PoseStack poseStack, SubmitNodeCollector collector, int light)
    {
        float s = SHELL_HALF;
        collector.submitCustomGeometry(poseStack, SHELL, (pose, buffer) -> {
            // up, down
            face(buffer, pose, light, 0, 1, 0, -s, s, -s, -s, s, s, s, s, s, s, s, -s);
            face(buffer, pose, light, 0, -1, 0, -s, -s, s, -s, -s, -s, s, -s, -s, s, -s, s);
            // north (-z), south (+z)
            face(buffer, pose, light, 0, 0, -1, s, s, -s, s, -s, -s, -s, -s, -s, -s, s, -s);
            face(buffer, pose, light, 0, 0, 1, -s, s, s, -s, -s, s, s, -s, s, s, s, s);
            // west (-x), east (+x)
            face(buffer, pose, light, -1, 0, 0, -s, s, -s, -s, -s, -s, -s, -s, s, -s, s, s);
            face(buffer, pose, light, 1, 0, 0, s, s, s, s, -s, s, s, -s, -s, s, s, -s);
        });
    }

    // One face: four corners (x, y, z each) counter-clockwise seen from outside, from the top left
    private static void face(VertexConsumer buffer, PoseStack.Pose pose, int light, float nx, float ny, float nz,
                             float x0, float y0, float z0, float x1, float y1, float z1,
                             float x2, float y2, float z2, float x3, float y3, float z3)
    {
        corner(buffer, pose, x0, y0, z0, 0, 0, light, nx, ny, nz);
        corner(buffer, pose, x1, y1, z1, 0, 1, light, nx, ny, nz);
        corner(buffer, pose, x2, y2, z2, 1, 1, light, nx, ny, nz);
        corner(buffer, pose, x3, y3, z3, 1, 0, light, nx, ny, nz);
    }

    private static void corner(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int light,
                               float nx, float ny, float nz)
    {
        buffer.addVertex(pose, x, y, z).setColor(0xFFFFFFFF).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, nx, ny, nz);
    }
}
