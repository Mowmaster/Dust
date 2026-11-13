package com.mowmaster.dust.Features.Spells.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.Spells.blocks.StickyFaces;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Draws slime patches (Slime: Features/Spells/blocks/Sticking) on the faces they're on: a see-through slime splotch laid
// just off the face, like the slime on a sticky piston's head. 26.3 renders in two steps, so this does too: while the
// frame's state is gathered (ExtractLevelRenderStateEvent) it collects the patches near the camera from the chunks'
// synced data, with their light; then (SubmitCustomGeometryEvent) it hands the quads to the renderer.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class StickyPatchRenderer
{
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/spell/sticky_patch.png");
    private static final RenderType RENDER_TYPE = RenderTypes.entityTranslucent(TEXTURE);
    // Patches are drawn within this many chunks of the camera
    private static final int CHUNK_RADIUS = 4;
    // How far off the face the patch floats, so it doesn't flicker into the block's own surface
    private static final float LIFT = 0.002F;

    private record Patch(BlockPos pos, Direction face, int light) {}

    // Where the gathered patches wait on the frame's state between the two steps
    private static final ContextKey<List<Patch>> PATCHES = new ContextKey<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "sticky_patches"));

    private StickyPatchRenderer() {}

    @SubscribeEvent
    public static void onExtract(ExtractLevelRenderStateEvent event)
    {
        ClientLevel level = event.getLevel();
        BlockPos camera = BlockPos.containing(event.getCamera().position());
        int cx = camera.getX() >> 4, cz = camera.getZ() >> 4;
        List<Patch> patches = new ArrayList<>();
        for (int x = cx - CHUNK_RADIUS; x <= cx + CHUNK_RADIUS; x++) {
            for (int z = cz - CHUNK_RADIUS; z <= cz + CHUNK_RADIUS; z++) {
                // false: only chunks the client already has (don't ask for new ones)
                if (!(level.getChunkSource().getChunk(x, z, ChunkStatus.FULL, false) instanceof LevelChunk chunk)) continue;
                for (Map.Entry<BlockPos, StickyFaces.Entry> e : chunk.getData(DustAttachmentTypeRegistry.STICKY_FACES).blocks().entrySet()) {
                    BlockPos pos = e.getKey();
                    if (!level.getBlockState(pos).is(e.getValue().block())) continue;
                    for (Direction face : Direction.values()) {
                        if (!e.getValue().has(face)) continue;
                        // Lit by the light in front of the face (the block itself is dark inside)
                        patches.add(new Patch(pos, face, LightCoordsUtil.getLightCoords(level, pos.relative(face))));
                    }
                }
            }
        }
        if (!patches.isEmpty()) event.getRenderState().setRenderData(PATCHES, patches);
    }

    @SubscribeEvent
    public static void onSubmit(SubmitCustomGeometryEvent event)
    {
        List<Patch> patches = event.getLevelRenderState().getRenderData(PATCHES);
        if (patches == null) return;
        Vec3 camera = event.getLevelRenderState().cameraRenderState.pos;
        PoseStack poseStack = event.getPoseStack();
        for (Patch patch : patches) {
            poseStack.pushPose();
            // Positions are drawn relative to the camera
            poseStack.translate(patch.pos().getX() - camera.x, patch.pos().getY() - camera.y, patch.pos().getZ() - camera.z);
            event.getSubmitNodeCollector().submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) -> quad(pose, buffer, patch.face(), patch.light()));
            poseStack.popPose();
        }
    }

    // One square covering the face of the block at 0..1, lifted just off it, facing out
    private static void quad(PoseStack.Pose pose, VertexConsumer buffer, Direction face, int light)
    {
        float lo = -LIFT, hi = 1 + LIFT;
        float[][] corners = switch (face) {
            case DOWN -> new float[][] {{0, lo, 0}, {1, lo, 0}, {1, lo, 1}, {0, lo, 1}};
            case UP -> new float[][] {{0, hi, 1}, {1, hi, 1}, {1, hi, 0}, {0, hi, 0}};
            case NORTH -> new float[][] {{1, 0, lo}, {0, 0, lo}, {0, 1, lo}, {1, 1, lo}};
            case SOUTH -> new float[][] {{0, 0, hi}, {1, 0, hi}, {1, 1, hi}, {0, 1, hi}};
            case WEST -> new float[][] {{lo, 0, 0}, {lo, 0, 1}, {lo, 1, 1}, {lo, 1, 0}};
            case EAST -> new float[][] {{hi, 0, 1}, {hi, 0, 0}, {hi, 1, 0}, {hi, 1, 1}};
        };
        float[][] uvs = {{0, 1}, {1, 1}, {1, 0}, {0, 0}};
        for (int i = 0; i < 4; i++) {
            buffer.addVertex(pose, corners[i][0], corners[i][1], corners[i][2])
                    .setColor(255, 255, 255, 255)
                    .setUv(uvs[i][0], uvs[i][1])
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(pose, face.getStepX(), face.getStepY(), face.getStepZ());
        }
    }
}
