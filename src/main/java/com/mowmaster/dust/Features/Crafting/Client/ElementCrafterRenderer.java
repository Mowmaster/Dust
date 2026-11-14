package com.mowmaster.dust.Features.Crafting.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Crafting.ElementCrafterBlockEntity;
import com.mowmaster.dust.Features.Crafting.ElementCrafterLayout;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.Client.StationRendering;
import com.mowmaster.dust.Features.Machines.StationBlock;
import com.mowmaster.mowlib.client.GhostItemRenderer;
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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// The Element Crafter's grid items, lying flat in their cells; the dust in each jar, filling it as it fills (the white
// dust block's texture tinted the jar's element colour); and the result floating over the grid as a ghost (MowLib's
// GhostItemRenderer: the result's name above it, the dust it takes below)
public class ElementCrafterRenderer implements BlockEntityRenderer<ElementCrafterBlockEntity, ElementCrafterRenderer.State>
{
    private static final float ITEM_SCALE = 0.28F;
    // entityCutout: an opaque texture (see-through pixels cut out), lit like an entity
    private static final RenderType DUST = RenderTypes.entityCutout(Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/blockofdust_white.png"));
    // Kept just inside the glass, so the two don't flicker where they'd touch
    private static final double INSET = 0.05;

    private final ItemModelResolver itemModelResolver;
    private final GhostItemRenderer ghostRenderer;

    public ElementCrafterRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
        this.ghostRenderer = new GhostItemRenderer(context.itemModelResolver(), context.font());
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState[] cells = new ItemStackRenderState[ElementCrafterLayout.CELLS];
        final GhostItemRenderer.Ghosts ghosts = new GhostItemRenderer.Ghosts();
        // Each jar's dust: its colour (0xAARRGGBB) and how high it stands (pixels); no dust: the height is the bottom
        final int[] jarColors = new int[ElementCrafterLayout.JARS];
        final double[] jarFills = new double[ElementCrafterLayout.JARS];
        Direction facing = Direction.NORTH;
        float spin;

        State()
        {
            for (int i = 0; i < cells.length; i++) cells[i] = new ItemStackRenderState();
        }
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(ElementCrafterBlockEntity crafter, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(crafter, state, partialTick, cameraPos, crumblingOverlay);
        for (int i = 0; i < ElementCrafterLayout.CELLS; i++) {
            itemModelResolver.updateForTopItem(state.cells[i], crafter.getCell(i), ItemDisplayContext.GROUND, crafter.getLevel(), null, i);
        }
        for (int jar = 0; jar < ElementCrafterLayout.JARS; jar++) {
            DustElement element = crafter.jarElement(jar);
            state.jarColors[jar] = element == null ? 0xFFFFFFFF : 0xFF000000 | element.color();
            state.jarFills[jar] = element == null ? ElementCrafterLayout.FILL_BOTTOM : ElementCrafterLayout.fillHeight(crafter.jarAmount(jar));
        }
        ghostRenderer.extract(state.ghosts, crafter.getGhosts(), crafter.getLevel(), crafter.getBlockPos(), ElementCrafterLayout.CELLS);
        state.facing = crafter.getBlockState().getValue(StationBlock.FACING);
        state.spin = crafter.getLevel() == null ? 0 : ((crafter.getLevel().getGameTime() + partialTick) * 2F) % 360F;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        for (int i = 0; i < ElementCrafterLayout.CELLS; i++) {
            StationRendering.flatItem(state.cells[i], ElementCrafterLayout.cellCentre(i), state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        }
        for (int jar = 0; jar < ElementCrafterLayout.JARS; jar++) {
            if (state.jarFills[jar] <= ElementCrafterLayout.FILL_BOTTOM) continue;
            double[] inside = ElementCrafterLayout.jarInside(jar);
            StationRendering.box(DUST, inside[0] + INSET, ElementCrafterLayout.FILL_BOTTOM, inside[1] + INSET,
                    inside[2] - INSET, state.jarFills[jar], inside[3] - INSET, state.jarColors[jar], state.lightCoords,
                    state.facing, poseStack, submitNodeCollector);
        }
        ghostRenderer.submit(state.ghosts, Direction.UP, state.spin, poseStack, submitNodeCollector, camera);
    }

    // The ghost floats above the block: draw while that's in view too
    @Override
    public AABB getRenderBoundingBox(ElementCrafterBlockEntity crafter)
    {
        return GhostItemRenderer.boundingBox(crafter.getBlockPos(), crafter.getGhosts());
    }
}
