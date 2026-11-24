package com.mowmaster.dust.Features.Crystals.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Crystals.CrystalDisplayBlockEntity;
import com.mowmaster.dust.Features.Guide.Client.GuideContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The crystal on a Crystal Display: the large crystal model (models/block/crystal/crystal_large.json, its faces on tint
// index 1) standing on the stand's top (12 pixels up), turning slowly. A 2x2 or 3x3 group draws one crystal twice or
// three times as big over the middle of the square, from its master; the others draw none. Its colour is the
// display's crystal's (a group's: the mix of all its crystals), grey once spent or inert; a working crystal glows.
public class CrystalDisplayRenderer implements BlockEntityRenderer<CrystalDisplayBlockEntity, CrystalDisplayRenderer.State>
{
    public static final StandaloneModelKey<BlockStateModelPart> CRYSTAL = new StandaloneModelKey<>(() -> "dust:crystal_display_crystal");
    private static final int SPENT_COLOR = 0xFF8C8C8C;
    private static final float TOP = 12 / 16F;
    // Degrees a tick it turns
    private static final float SPIN = 0.75F;

    public static void registerModels(ModelEvent.RegisterStandalone event)
    {
        event.register(CRYSTAL, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/crystal/crystal_large")));
    }

    public CrystalDisplayRenderer(BlockEntityRendererProvider.Context context) {}

    public static class State extends BlockEntityRenderState
    {
        float scale;
        double offset;
        int color = 0xFFFFFFFF;
        boolean glowing;
        float spin;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(CrystalDisplayBlockEntity display, State state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(display, state, partialTick, cameraPos, crumblingOverlay);
        boolean shown = display.element() != null && display.leads();
        state.scale = shown ? display.groupSize() : 0;
        state.offset = (display.groupSize() - 1) / 2.0;
        state.glowing = !display.groupSpent();
        state.color = display.groupSpent() ? SPENT_COLOR : 0xFF000000 | display.groupColor();
        state.spin = GuideContext.ticks(display.getLevel(), partialTick) * SPIN % 360;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        if (state.scale <= 0) return;
        BlockStateModelPart crystal = Minecraft.getInstance().getModelManager().getStandaloneModel(CRYSTAL);
        if (crystal == null) return;
        poseStack.pushPose();
        poseStack.translate(0.5 + state.offset, TOP, 0.5 + state.offset);
        poseStack.rotate(Axis.YP.rotationDegrees(state.spin));
        poseStack.scale(state.scale, state.scale, state.scale);
        poseStack.translate(-0.5, 0, -0.5);
        int light = state.glowing ? 0xF000F0 : state.lightCoords;
        collector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), List.of(crystal), new int[]{-1, state.color}, light, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    // A group's crystal stands over the whole square, up to three blocks high
    @Override
    public AABB getRenderBoundingBox(CrystalDisplayBlockEntity display)
    {
        BlockPos pos = display.getBlockPos();
        int size = display.groupSize();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + size, pos.getY() + 1 + size, pos.getZ() + size);
    }
}
