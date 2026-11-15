package com.mowmaster.dust.Features.DustStorage.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustStorage.DustGrinderBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The Dust Grinder's moving parts (the base is the block model, dust_grinder_base.json):
//   the top and its handle (dust_grinder_top.json, a standalone model like the crusher's wheels), turning one full turn
//     about the block's middle while it grinds
//   what's in the hole, sitting in the middle of the top (it turns with it)
public class DustGrinderRenderer implements BlockEntityRenderer<DustGrinderBlockEntity, DustGrinderRenderer.State>
{
    private static final float ITEM_SCALE = 0.3F;
    public static final StandaloneModelKey<BlockStateModelPart> TOP = new StandaloneModelKey<>(() -> "dust:dust_grinder_top");

    // Called from DustClient's ModelEvent.RegisterStandalone handler
    public static void registerModels(ModelEvent.RegisterStandalone event)
    {
        event.register(TOP, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/dust_grinder_top")));
    }

    private final ItemModelResolver itemModelResolver;

    public DustGrinderRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState input = new ItemStackRenderState();
        float angle;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(DustGrinderBlockEntity grinder, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(grinder, state, partialTick, cameraPos, crumblingOverlay);
        itemModelResolver.updateForTopItem(state.input, grinder.getInput(), ItemDisplayContext.FIXED, grinder.getLevel(), null, 0);
        state.angle = grinder.angle(partialTick);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        // Everything turns about the block's middle (x, z = 0.5); clockwise seen from above is a negative angle about up
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.rotate(Axis.YP.rotationDegrees(-state.angle));
        poseStack.translate(-0.5, 0, -0.5);

        BlockStateModelPart top = Minecraft.getInstance().getModelManager().getStandaloneModel(TOP);
        if (top != null) {
            submitNodeCollector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), List.of(top), BlockModelRenderState.EMPTY_TINTS,
                    state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }
        // The item in the hole: lying flat a little below the top's surface (the hole is pixels 6-10, 13-16 up)
        if (!state.input.isEmpty()) {
            poseStack.translate(0.5, 14.2 / 16, 0.5);
            poseStack.rotate(Axis.XP.rotationDegrees(90));
            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            state.input.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }
        poseStack.popPose();
    }
}
