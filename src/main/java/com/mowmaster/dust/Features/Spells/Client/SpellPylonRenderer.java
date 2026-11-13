package com.mowmaster.dust.Features.Spells.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
        if (ghosts.isEmpty()) return;
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

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
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

    // The ghosts can be a few blocks from the Spell Pylon (at the end of its line): draw it while any of that is in view
    @Override
    public AABB getRenderBoundingBox(SpellPylonBlockEntity pylon)
    {
        AABB box = new AABB(pylon.getBlockPos()).inflate(1);
        for (BlockPos pos : pylon.chain()) box = box.minmax(new AABB(pos).inflate(1));
        return box;
    }
}
