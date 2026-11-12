package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Machines.DistillationLayout;
import com.mowmaster.dust.Features.Machines.ResearchDistillationBlock;
import com.mowmaster.dust.Features.Machines.ResearchDistillationBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// The Distillation Table's moving parts (the table, scroll, flask and jar are the block model):
//   the glyph (textures/block/station/scroll_distalation_glyph.png): rises a little off the scroll into the bottom of
//     the flask, shrinking to fit and spinning, while it boils; sinks back as the results move to the outputs
//   the liquid: a quarter of the flask in the colour of what can boil, boiling down to nothing; the jar filling a
//     quarter with what boiled over, then emptying into its output
//   the item in the flask, the containers on their pads and the three outputs, lying flat
public class ResearchDistillationRenderer implements BlockEntityRenderer<ResearchDistillationBlockEntity, ResearchDistillationRenderer.State>
{
    private static final float ITEM_SCALE = 0.35F;
    // How long (ticks) the glyph takes to rise into the flask, and to sink back
    private static final float RISE_TICKS = 15F;
    private static final Identifier GLYPH = Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/station/scroll_distalation_glyph.png");
    private static final RenderType LIQUID = RenderTypes.entityTranslucent(Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/station/pot_fluid.png"));

    private final ItemModelResolver itemModelResolver;

    public ResearchDistillationRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState input = new ItemStackRenderState();
        final ItemStackRenderState bottles = new ItemStackRenderState();
        final ItemStackRenderState bowls = new ItemStackRenderState();
        final ItemStackRenderState paper = new ItemStackRenderState();
        final ItemStackRenderState flaskOut = new ItemStackRenderState();
        final ItemStackRenderState jarOut = new ItemStackRenderState();
        final ItemStackRenderState sootOut = new ItemStackRenderState();
        Direction facing = Direction.NORTH;
        // -1: idle, else ticks since it started
        float progress = -1;
        double flaskLiquid;
        int flaskColor = -1;
        double jarLiquid;
        int jarColor = -1;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(ResearchDistillationBlockEntity table, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(table, state, partialTick, cameraPos, crumblingOverlay);
        state.progress = table.progress(partialTick);
        // Once it's boiled dry, the item is gone from the flask (it becomes the outputs)
        boolean boiledDry = state.progress >= ResearchDistillationBlockEntity.BOIL_TICKS;
        itemModelResolver.updateForTopItem(state.input, boiledDry ? ItemStack.EMPTY : table.getInput(),
                ItemDisplayContext.GROUND, table.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(state.bottles, table.getBottles(), ItemDisplayContext.GROUND, table.getLevel(), null, 1);
        itemModelResolver.updateForTopItem(state.bowls, table.getBowls(), ItemDisplayContext.GROUND, table.getLevel(), null, 2);
        itemModelResolver.updateForTopItem(state.sootOut, table.getSootOut(), ItemDisplayContext.GROUND, table.getLevel(), null, 3);
        itemModelResolver.updateForTopItem(state.paper, table.getPaper(), ItemDisplayContext.GROUND, table.getLevel(), null, 6);
        itemModelResolver.updateForTopItem(state.flaskOut, table.getFlaskOut(), ItemDisplayContext.GROUND, table.getLevel(), null, 4);
        itemModelResolver.updateForTopItem(state.jarOut, table.getJarOut(), ItemDisplayContext.GROUND, table.getLevel(), null, 5);
        state.facing = table.getBlockState().getValue(ResearchDistillationBlock.FACING);
        state.flaskLiquid = table.flaskLiquid(partialTick);
        state.flaskColor = table.getInput().isEmpty() || boiledDry ? -1 : table.flaskColor();
        state.jarLiquid = table.jarLiquid(partialTick);
        state.jarColor = table.jarColor();
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        Vec3 flask = DistillationLayout.FLASK;
        Vec3 jar = DistillationLayout.JAR;

        // What's in the flask, and the liquid over it
        StationRendering.flatItem(state.input, flask.add(0, 0.05, 0), state.facing, 0, ITEM_SCALE * 0.5F, state.lightCoords, poseStack, submitNodeCollector);
        if (state.flaskColor >= 0 && state.flaskLiquid > flask.y + 0.05) {
            StationRendering.square(LIQUID, new Vec3(flask.x, state.flaskLiquid, flask.z), DistillationLayout.FLASK_WIDTH, DistillationLayout.FLASK_DEPTH, 0,
                    0xD0000000 | state.flaskColor, state.lightCoords, state.facing, poseStack, submitNodeCollector);
        }
        // What's boiled over into the jar
        if (state.jarColor >= 0 && state.jarLiquid > jar.y + 0.05) {
            StationRendering.square(LIQUID, new Vec3(jar.x, state.jarLiquid, jar.z), DistillationLayout.JAR_SIZE, DistillationLayout.JAR_SIZE, 0,
                    0xD0000000 | state.jarColor, state.lightCoords, state.facing, poseStack, submitNodeCollector);
        }

        // The glyph: up off the scroll into the flask (shrinking to fit) as it starts, back down as it ends
        if (state.progress >= 0) {
            float rise = Math.min(1F, state.progress / RISE_TICKS);
            float sink = Math.max(0F, (state.progress - ResearchDistillationBlockEntity.BOIL_TICKS) / ResearchDistillationBlockEntity.TRANSFER_TICKS);
            float up = Math.max(0F, rise - sink);
            Vec3 at = DistillationLayout.GLYPH_SCROLL.lerp(DistillationLayout.GLYPH_FLASK, up).add(0, 0.05, 0);
            double width = Mth.lerp(up, DistillationLayout.GLYPH_SCROLL_WIDTH, DistillationLayout.GLYPH_FLASK_SIZE);
            double depth = Mth.lerp(up, DistillationLayout.GLYPH_SCROLL_DEPTH, DistillationLayout.GLYPH_FLASK_SIZE);
            StationRendering.glyph(GLYPH, at, width, depth, (state.progress * 6F) % 360F, state.progress, state.facing, poseStack, submitNodeCollector);
        }

        StationRendering.flatItem(state.bottles, DistillationLayout.BOTTLE_PAD, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        StationRendering.flatItem(state.bowls, DistillationLayout.BOWL_PAD, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        StationRendering.flatItem(state.paper, DistillationLayout.PAPER_PAD, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        StationRendering.flatItem(state.sootOut, DistillationLayout.SOOT_OUTPUT, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        StationRendering.flatItem(state.flaskOut, DistillationLayout.FLASK_OUTPUT, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        StationRendering.flatItem(state.jarOut, DistillationLayout.JAR_OUTPUT, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
    }
}
