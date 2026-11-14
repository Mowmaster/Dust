package com.mowmaster.dust.Features.Spells.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.Spells.pylons.SpellPylonBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// The Spell Pylon's ghosts: where the next pylon can go, a pylon floats there, turning and bobbing, glowing. The Shape
// Pylon's place shows a Shape Pylon; a modifier pylon's place shows the four kinds in turn.
// And its icons: the element's over the Spell Pylon, the shape's over the Shape Pylon, coming out of the little dark
// square on top like a genie from a lamp (swirling up, stretched thin, then popping to size) while a player is near
// (SpellPylonBlockEntity.clientTick), always facing the camera.
public class SpellPylonRenderer implements BlockEntityRenderer<SpellPylonBlockEntity, SpellPylonRenderer.State>
{
    private static final int FULL_BRIGHT = 0xF000F0;
    private static final float GHOST_SCALE = 0.6F;

    private final ItemModelResolver itemModelResolver;

    public SpellPylonRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState ghost = new ItemStackRenderState();
        // Each ghost place, relative to the Spell Pylon
        final List<Vec3> places = new ArrayList<>();
        float time;
        // The icons: how far out (0-1), their textures and colour, and where the Shape Pylon is (relative)
        float appear;
        @Nullable Identifier elementIcon;
        @Nullable Identifier shapeIcon;
        int color;
        @Nullable Vec3 shapeAt;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(SpellPylonBlockEntity pylon, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(pylon, state, partialTick, cameraPos, crumblingOverlay);
        state.places.clear();
        List<BlockPos> ghosts = pylon.ghostPositions();
        state.time = pylon.getLevel() == null ? 0 : (pylon.getLevel().getGameTime() % 24000) + partialTick;
        extractIcons(pylon, state, partialTick);
        if (ghosts.isEmpty()) {
            state.ghost.clear();
            return;
        }
        BlockPos origin = pylon.getBlockPos();
        for (BlockPos ghost : ghosts) state.places.add(Vec3.atLowerCornerOf(ghost.subtract(origin)));
        Block shown;
        if (pylon.chain().isEmpty()) {
            shown = DustBlockRegistry.SHAPE_PYLON.get();
        } else {
            Block[] kinds = {DustBlockRegistry.CHANGE_PYLON.get(), DustBlockRegistry.COST_PYLON.get(), DustBlockRegistry.COUNT_PYLON.get(),
                    DustBlockRegistry.DAMAGE_PYLON.get()};
            shown = kinds[(int) (state.time / 30) % kinds.length];
        }
        itemModelResolver.updateForTopItem(state.ghost, new ItemStack(shown), ItemDisplayContext.GROUND, pylon.getLevel(), null, 0);
    }

    private static void extractIcons(SpellPylonBlockEntity pylon, State state, float partialTick)
    {
        DustElement element = pylon.element();
        state.appear = element == null ? 0 : pylon.appear(partialTick);
        state.elementIcon = null;
        state.shapeIcon = null;
        state.shapeAt = null;
        if (state.appear <= 0 || element == null) return;
        state.color = element.color();
        // The element's GUI sprite, drawn as a texture of its own (textures/gui/sprites/element/<name>.png)
        Identifier sprite = element.icon();
        state.elementIcon = Identifier.fromNamespaceAndPath(sprite.getNamespace(), "textures/gui/sprites/" + sprite.getPath() + ".png");
        Identifier shape = pylon.shape();
        if (shape != null && !pylon.chain().isEmpty()) {
            state.shapeIcon = Identifier.fromNamespaceAndPath(shape.getNamespace(), "textures/block/spell/" + shape.getPath() + ".png");
            state.shapeAt = Vec3.atLowerCornerOf(pylon.chain().getFirst().subtract(pylon.getBlockPos()));
        }
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        if (state.elementIcon != null) genie(state.elementIcon, Vec3.ZERO, state, poseStack, submitNodeCollector, camera);
        if (state.shapeIcon != null && state.shapeAt != null) genie(state.shapeIcon, state.shapeAt, state, poseStack, submitNodeCollector, camera);
        if (state.ghost.isEmpty()) return;
        float bob = Mth.sin(state.time * 0.1F) * 0.06F;
        for (Vec3 place : state.places) {
            poseStack.pushPose();
            poseStack.translate(place.x + 0.5, place.y + 0.3 + bob, place.z + 0.5);
            poseStack.rotate(Axis.YP.rotationDegrees((state.time * 3) % 360));
            poseStack.scale(GHOST_SCALE, GHOST_SCALE, GHOST_SCALE);
            state.ghost.submit(poseStack, submitNodeCollector, FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    // ---- the icons ----

    // How big an icon is once it's out (blocks across)
    private static final float ICON_SIZE = 0.5F;
    // How far round the column it swirls as it comes out (blocks), and how fast (radians a tick)
    private static final float SWIRL_RADIUS = 0.18F;
    private static final float SWIRL_SPEED = 0.6F;

    // One icon coming out of the dark square on the block at `block` (relative to the Spell Pylon), t = state.appear:
    //   rise     up from the square, fast then slowing (ease out)
    //   swirl    round the column, the circle closing as it rises
    //   stretch  thin and tall at first, settling to square
    //   size     popping a little past full size, then back (ease out "back")
    // Once it's out it just bobs. It always faces the camera (camera.orientation: the camera's turn, so the quad lies
    // across the view).
    private static void genie(Identifier texture, Vec3 block, State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
                              CameraRenderState camera)
    {
        float t = Mth.clamp(state.appear, 0F, 1F);
        float rise = 1 - (1 - t) * (1 - t) * (1 - t);
        float overshoot = 1.7F;
        float pop = 1 + (overshoot + 1) * (float) Math.pow(t - 1, 3) + overshoot * (float) Math.pow(t - 1, 2);
        float size = ICON_SIZE * Math.max(0.05F, pop);
        float swirl = SWIRL_RADIUS * (1 - rise);
        float angle = state.time * SWIRL_SPEED;
        float bob = t >= 1 ? Mth.sin(state.time * 0.1F) * 0.04F : 0;
        double y = SpellPylonBlockEntity.ICON_BASE_Y + SpellPylonBlockEntity.ICON_RISE * rise + bob;

        poseStack.pushPose();
        poseStack.translate(block.x + 0.5 + Mth.cos(angle) * swirl, block.y + y, block.z + 0.5 + Mth.sin(angle) * swirl);
        poseStack.rotate(camera.orientation);
        poseStack.scale(size * (0.35F + 0.65F * t), size * (1.6F - 0.6F * t), size);
        int alpha = Math.round(255 * Math.min(1F, t * 2));
        int color = (alpha << 24) | (state.color & 0xFFFFFF);
        // entityTranslucent: see-through pixels blended; drawn on both sides, so it shows whichever way round it is
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(texture), (pose, buffer) -> {
            iconVertex(buffer, pose, -0.5F, -0.5F, 0, 1, color, 1);
            iconVertex(buffer, pose, 0.5F, -0.5F, 1, 1, color, 1);
            iconVertex(buffer, pose, 0.5F, 0.5F, 1, 0, color, 1);
            iconVertex(buffer, pose, -0.5F, 0.5F, 0, 0, color, 1);
            iconVertex(buffer, pose, -0.5F, 0.5F, 0, 0, color, -1);
            iconVertex(buffer, pose, 0.5F, 0.5F, 1, 0, color, -1);
            iconVertex(buffer, pose, 0.5F, -0.5F, 1, 1, color, -1);
            iconVertex(buffer, pose, -0.5F, -0.5F, 0, 1, color, -1);
        });
        poseStack.popPose();
    }

    private static void iconVertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float u, float v, int color, float normalZ)
    {
        buffer.addVertex(pose, x, y, 0).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(FULL_BRIGHT).setNormal(pose, 0, 0, normalZ);
    }

    // The ghosts can be a few blocks from the Spell Pylon (at the end of its line): draw it while any of that is in view
    @Override
    public AABB getRenderBoundingBox(SpellPylonBlockEntity pylon)
    {
        AABB box = new AABB(pylon.getBlockPos()).inflate(1).expandTowards(0, 1, 0);
        for (BlockPos pos : pylon.chain()) box = box.minmax(new AABB(pos).inflate(1));
        return box;
    }
}
