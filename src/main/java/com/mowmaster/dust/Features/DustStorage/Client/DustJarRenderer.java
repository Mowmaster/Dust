package com.mowmaster.dust.Features.DustStorage.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustStorage.DustJarBlockEntity;
import com.mowmaster.dust.Features.DustStorage.DustJarContents;
import com.mowmaster.dust.Features.Machines.Client.StationRendering;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// The dust inside a Dust Jar, like the Element Crafter's jars: a box of the white dust block's texture tinted the
// element's colour, standing as high as the jar is full (a thin layer at least, so a little dust still shows).
// The jar's inside, in model pixels (dust_jar.json): 4-12 across both ways, from its floor (1) up to under the lid (11).
public class DustJarRenderer implements BlockEntityRenderer<DustJarBlockEntity, DustJarRenderer.State>
{
    private static final RenderType DUST = RenderTypes.entityCutout(Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/blockofdust_white.png"));
    private static final double INSIDE_LOW = 4, INSIDE_HIGH = 12;
    private static final double FILL_BOTTOM = 1, FILL_TOP = 10.9;
    // Kept just off the glass, so the two don't flicker where they'd touch
    private static final double INSET = 0.05;

    public DustJarRenderer(BlockEntityRendererProvider.Context context)
    {
    }

    public static class State extends BlockEntityRenderState
    {
        int color = 0xFFFFFFFF;
        // How high the dust stands (pixels); FILL_BOTTOM: none
        double fill = FILL_BOTTOM;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(DustJarBlockEntity jar, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(jar, state, partialTick, cameraPos, crumblingOverlay);
        state.color = jar.element() == null ? 0xFFFFFFFF : 0xFF000000 | jar.element().color();
        state.fill = fillHeight(jar.amount());
    }

    // How high the dust stands for this much in the jar
    private static double fillHeight(int amount)
    {
        if (amount <= 0) return FILL_BOTTOM;
        double share = Math.min(1.0, amount / (double) DustJarContents.CAPACITY);
        return FILL_BOTTOM + Math.max(0.15, (FILL_TOP - FILL_BOTTOM) * share);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        if (state.fill <= FILL_BOTTOM) return;
        // The jar doesn't turn, so it's drawn as for a north-facing model
        StationRendering.box(DUST, INSIDE_LOW + INSET, FILL_BOTTOM, INSIDE_LOW + INSET, INSIDE_HIGH - INSET, state.fill, INSIDE_HIGH - INSET,
                state.color, state.lightCoords, Direction.NORTH, poseStack, submitNodeCollector);
    }
}
