package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Machines.EvaporationLayout;
import com.mowmaster.dust.Features.Machines.MixingLayout;
import com.mowmaster.dust.Features.Machines.ResearchEvaporationBlockEntity;
import com.mowmaster.dust.Features.Machines.ResearchMixingBlockEntity;
import com.mowmaster.dust.Features.Machines.StationBlock;
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
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The Mixing Table's moving parts (the table, scroll and pot are the block model, research_mixing_body.json):
//   the spoon (the Evaporation Table's: same pot, same place), going once round the pot with each stir
//   the liquid in the pot: the two dusts' colours mixed, turning to the new element's as it's stirred
//   the glyph (textures/block/station/scroll_mixing_glyph.png), spinning over the pot, then sinking into it
//   the dusts on their pads and the output, lying flat
public class ResearchMixingRenderer implements BlockEntityRenderer<ResearchMixingBlockEntity, ResearchMixingRenderer.State>
{
    private static final float ITEM_SCALE = 0.35F;
    private static final Identifier GLYPH = Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/station/scroll_mixing_glyph.png");
    private static final RenderType LIQUID = RenderTypes.entityTranslucent(Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/station/pot_fluid.png"));

    private final ItemModelResolver itemModelResolver;

    public ResearchMixingRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState left = new ItemStackRenderState();
        final ItemStackRenderState right = new ItemStackRenderState();
        final ItemStackRenderState output = new ItemStackRenderState();
        Direction facing = Direction.NORTH;
        ResearchEvaporationBlockEntity.Phase phase = ResearchEvaporationBlockEntity.Phase.IDLE;
        float spoonAngle;
        double liquidHeight;
        int liquidColor = -1;
        float time;
        float settle;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(ResearchMixingBlockEntity table, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(table, state, partialTick, cameraPos, crumblingOverlay);
        itemModelResolver.updateForTopItem(state.left, table.getLeft(), ItemDisplayContext.GROUND, table.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(state.right, table.getRight(), ItemDisplayContext.GROUND, table.getLevel(), null, 1);
        itemModelResolver.updateForTopItem(state.output, table.getOutput(), ItemDisplayContext.GROUND, table.getLevel(), null, 2);
        state.facing = table.getBlockState().getValue(StationBlock.FACING);
        state.phase = table.phase();
        state.spoonAngle = ((table.stirsDone() + table.stirProgress(partialTick)) * 360F) % 360F;
        state.liquidHeight = table.liquidHeight(partialTick);
        state.liquidColor = table.liquidColor(partialTick);
        state.time = table.time() + partialTick;
        state.settle = table.settleProgress(partialTick);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        Vec3 pot = EvaporationLayout.POT;

        // The spoon, turned about the pot's middle (in the model's own north-facing space, then turned to the facing)
        BlockStateModelPart spoon = Minecraft.getInstance().getModelManager().getStandaloneModel(ResearchEvaporationRenderer.SPOON);
        if (spoon != null) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0, 0.5);
            poseStack.rotate(Axis.YP.rotationDegrees(StationRendering.yaw(state.facing)));
            poseStack.translate(-0.5, 0, -0.5);
            poseStack.translate(pot.x / 16, 0, pot.z / 16);
            poseStack.rotate(Axis.YP.rotationDegrees(-state.spoonAngle));
            poseStack.translate(-pot.x / 16, 0, -pot.z / 16);
            submitNodeCollector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), List.of(spoon), BlockModelRenderState.EMPTY_TINTS,
                    state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        if (state.liquidColor >= 0 && state.liquidHeight > pot.y + 0.05) {
            StationRendering.square(LIQUID, new Vec3(pot.x, state.liquidHeight, pot.z), EvaporationLayout.POT_INSIDE, EvaporationLayout.POT_INSIDE, 0,
                    0xD0000000 | state.liquidColor, state.lightCoords, state.facing, poseStack, submitNodeCollector);
        }

        // The glyph: spinning over the rim while stirring, sinking to the liquid while settling
        if (state.phase != ResearchEvaporationBlockEntity.Phase.IDLE) {
            double y = Mth.lerp(state.settle, EvaporationLayout.GLYPH_TOP.y, state.liquidHeight + 0.2);
            StationRendering.glyph(GLYPH, new Vec3(pot.x, y, pot.z), EvaporationLayout.GLYPH_SIZE, EvaporationLayout.GLYPH_SIZE,
                    (state.time * 6F) % 360F, state.time, state.facing, poseStack, submitNodeCollector);
        }

        StationRendering.flatItem(state.left, MixingLayout.LEFT_INPUT, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        StationRendering.flatItem(state.right, MixingLayout.RIGHT_INPUT, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        StationRendering.flatItem(state.output, MixingLayout.OUTPUT, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
    }
}
