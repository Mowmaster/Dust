package com.mowmaster.dust.Features.Apparatus.still;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.DustReferences;
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
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The flame inside a lit Burnable Firebox (firebox_flame.json: two crossed planes of a greyscale animated flame,
// firebox_flame.png, glowing), tinted by how much fuel it has, like a fire's heat: white with a full stack (and the
// piece burning), through yellow, orange and red, to dark red at FUEL_LOW or less.
public class StillFireboxRenderer implements BlockEntityRenderer<StillFireboxBlockEntity, StillFireboxRenderer.State>
{
    public static final StandaloneModelKey<BlockStateModelPart> FLAME = new StandaloneModelKey<>(() -> "dust:firebox_flame");
    // At or below this share of a full stack, the flame is dark red
    private static final float FUEL_LOW = 0.1F;
    // The colours from FUEL_LOW (dark red) up to full (white), 0xRRGGBB
    private static final int[] HEAT = {0x7A0A04, 0xD82A0C, 0xFF7A1A, 0xFFD04A, 0xFFFFFF};

    // Called from DustClient's ModelEvent.RegisterStandalone handler
    public static void registerModels(ModelEvent.RegisterStandalone event)
    {
        event.register(FLAME, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/firebox_flame")));
    }

    public StillFireboxRenderer(BlockEntityRendererProvider.Context context) {}

    public static class State extends BlockEntityRenderState
    {
        boolean lit;
        int color = HEAT[0];
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(StillFireboxBlockEntity firebox, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(firebox, state, partialTick, cameraPos, crumblingOverlay);
        state.lit = firebox.getBlockState().getValue(StillFireboxBlock.LIT);
        state.color = heat(firebox.fuelShare());
    }

    // The flame's colour for this share of a full stack of fuel (0-1)
    private static int heat(float share)
    {
        float t = Math.clamp((share - FUEL_LOW) / (1 - FUEL_LOW), 0F, 1F) * (HEAT.length - 1);
        int i = Math.min((int) t, HEAT.length - 2);
        float f = t - i;
        int a = HEAT[i], b = HEAT[i + 1], color = 0;
        for (int shift = 0; shift <= 16; shift += 8) {
            int from = (a >> shift) & 0xFF, to = (b >> shift) & 0xFF;
            color |= Math.round(from + (to - from) * f) << shift;
        }
        return color;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        if (!state.lit) return;
        BlockStateModelPart flame = Minecraft.getInstance().getModelManager().getStandaloneModel(FLAME);
        if (flame == null) return;
        // One tint colour, for the model's tintindex 0 (0xAARRGGBB)
        collector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), List.of(flame), new int[]{0xFF000000 | state.color},
                state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    }
}
