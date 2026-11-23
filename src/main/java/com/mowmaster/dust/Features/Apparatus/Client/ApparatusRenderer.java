package com.mowmaster.dust.Features.Apparatus.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Apparatus.ApparatusBlockEntity;
import com.mowmaster.dust.Features.Apparatus.CrushingMillBlockEntity;
import com.mowmaster.dust.Features.Apparatus.DustApparatusBlockEntity;
import com.mowmaster.dust.Features.Apparatus.ItemApparatusBlockEntity;
import com.mowmaster.dust.Features.Apparatus.MixingVatBlockEntity;
import com.mowmaster.dust.Features.Apparatus.SeparatorBlockEntity;
import com.mowmaster.dust.Features.Apparatus.TransmuterBlockEntity;
import com.mowmaster.dust.Features.Apparatus.ConjoiningCrucibleBlockEntity;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.Client.StationRendering;
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
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The alchemical apparatus's moving parts (the rest is each block model, models/block/apparatus/*.json). Points are
// model pixels with the model facing north; everything is turned with the block about its middle.
//   Crushing Mill        its two grinding wheels (mill_wheel.json) turning towards each other while it works, and the
//                        block being crushed sinking between them
//   Separator            its copper drum (separator_drum.json) spinning in the glass case
//   Transmuter           what it's making, growing and turning in the glass dome, glowing; its pattern on the dome's front
//   Conjoining Crucible  its pattern on the plate on the front
//   Dissolving Retort    its pattern on the plate on the belly's front
//   Mixing Vat           the liquid in the vat, in the pattern's colour; the paddle (vat_paddle.json) stirring while it
//                        works; its pattern on the front plate
// The Kettle and the Alembic have no moving parts: their steam, bubbles and flames are particles (their block entities'
// client ticks).
public class ApparatusRenderer implements BlockEntityRenderer<ApparatusBlockEntity, ApparatusRenderer.State>
{
    public static final StandaloneModelKey<BlockStateModelPart> MILL_WHEEL = new StandaloneModelKey<>(() -> "dust:mill_wheel");
    public static final StandaloneModelKey<BlockStateModelPart> SEPARATOR_DRUM = new StandaloneModelKey<>(() -> "dust:separator_drum");
    public static final StandaloneModelKey<BlockStateModelPart> VAT_PADDLE = new StandaloneModelKey<>(() -> "dust:vat_paddle");

    private static final Identifier LIQUID = Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/apparatus/vat_liquid.png");
    // The vat's liquid with no pattern (a murky brown), and how see-through it is (0xAA......)
    private static final int PLAIN_LIQUID = 0x6A5838;
    private static final int LIQUID_ALPHA = 0xE0000000;

    // Called from DustClient's ModelEvent.RegisterStandalone handler
    public static void registerModels(ModelEvent.RegisterStandalone event)
    {
        event.register(MILL_WHEEL, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/mill_wheel")));
        event.register(SEPARATOR_DRUM, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/separator_drum")));
        event.register(VAT_PADDLE, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/vat_paddle")));
    }

    private enum Kind { MILL, SEPARATOR, TRANSMUTER, CRUCIBLE, VAT, RETORT, OTHER }

    private final ItemModelResolver itemModelResolver;

    public ApparatusRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState item = new ItemStackRenderState();
        final ItemStackRenderState pattern = new ItemStackRenderState();
        Kind kind = Kind.OTHER;
        Direction facing = Direction.NORTH;
        // 0-1 through the piece of work, or -1 while idle
        float progress = -1;
        // Ticks with the partial tick (turning and spinning)
        float time;
        int liquidColor = PLAIN_LIQUID;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(ApparatusBlockEntity apparatus, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(apparatus, state, partialTick, cameraPos, crumblingOverlay);
        state.facing = apparatus.getBlockState().getValue(StationBlock.FACING);
        state.time = com.mowmaster.dust.Features.Guide.Client.GuideContext.ticks(apparatus.getLevel(), partialTick);
        // A switch on the block entity's class (Java 21's type patterns: the first case it's an instance of)
        state.kind = switch (apparatus) {
            case CrushingMillBlockEntity mill -> Kind.MILL;
            case SeparatorBlockEntity separator -> Kind.SEPARATOR;
            case TransmuterBlockEntity transmuter -> Kind.TRANSMUTER;
            case ConjoiningCrucibleBlockEntity crucible -> Kind.CRUCIBLE;
            case MixingVatBlockEntity vat -> Kind.VAT;
            case com.mowmaster.dust.Features.Apparatus.DissolvingRetortBlockEntity retort -> Kind.RETORT;
            default -> Kind.OTHER;
        };
        ItemStack item = ItemStack.EMPTY;
        ItemStack pattern = ItemStack.EMPTY;
        state.progress = -1;
        if (apparatus instanceof ItemApparatusBlockEntity worker) {
            state.progress = worker.progress(partialTick);
            item = worker.getWorking();
        }
        if (apparatus instanceof DustApparatusBlockEntity maker) {
            state.progress = maker.progress(partialTick);
            pattern = maker.getPattern();
            DustElement element = maker.patternElement();
            state.liquidColor = element == null ? PLAIN_LIQUID : element.color() & 0xFFFFFF;
        }
        if (apparatus instanceof TransmuterBlockEntity transmuter && state.progress >= 0) item = transmuter.product();
        itemModelResolver.updateForTopItem(state.item, item, state.kind == Kind.MILL ? ItemDisplayContext.FIXED : ItemDisplayContext.GROUND,
                apparatus.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(state.pattern, pattern, ItemDisplayContext.GROUND, apparatus.getLevel(), null, 1);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        poseStack.pushPose();
        // Into the model's own space: turned to the block's facing about its middle, in pixels from here on
        poseStack.translate(0.5, 0, 0.5);
        poseStack.rotate(Axis.YP.rotationDegrees(StationRendering.yaw(state.facing)));
        poseStack.translate(-0.5, 0, -0.5);
        boolean working = state.progress >= 0;
        switch (state.kind) {
            case MILL -> {
                float angle = working ? (state.time * 14F) % 360F : 0F;
                // The wheel model's axle is at the block's middle: moved up into the hopper, one each side of the middle
                part(MILL_WHEEL, new Vec3(8, 8, 8), Axis.XP.rotationDegrees(angle), new Vec3(0, 4.5, -2), state, poseStack, collector);
                part(MILL_WHEEL, new Vec3(8, 8, 8), Axis.XP.rotationDegrees(-angle), new Vec3(0, 4.5, 2), state, poseStack, collector);
                if (working) item(state.item, new Vec3(8, 15.5 - 3 * state.progress, 8), 0, 0.3F * (1 - 0.5F * state.progress), state.lightCoords, poseStack, collector);
            }
            case SEPARATOR -> {
                float angle = working ? (state.time * 24F) % 360F : 0F;
                part(SEPARATOR_DRUM, new Vec3(8, 0, 8), Axis.YP.rotationDegrees(angle), Vec3.ZERO, state, poseStack, collector);
            }
            case TRANSMUTER -> {
                if (working) {
                    float grow = 0.15F + 0.3F * state.progress;
                    item(state.item, new Vec3(8, 10 + Math.sin(state.time * 0.15) * 0.4, 8), (state.time * 4F) % 360F, grow, StationRendering.FULL_BRIGHT, poseStack, collector);
                }
                plate(state.pattern, new Vec3(8, 9.5, 2.85), 0.45F, state.lightCoords, poseStack, collector);
            }
            case CRUCIBLE -> plate(state.pattern, new Vec3(8, 8.5, 2.6), 0.5F, state.lightCoords, poseStack, collector);
            case RETORT -> plate(state.pattern, new Vec3(8, 8.75, 3.1), 0.45F, state.lightCoords, poseStack, collector);
            case VAT -> {
                // The liquid's surface (a square, see-through, in the pattern's colour), then the paddle and pattern
                poseStack.pushPose();
                StationRendering.square(RenderTypes.entityTranslucent(LIQUID), new Vec3(8, 10.6, 8), 9, 9, 0, LIQUID_ALPHA | state.liquidColor,
                        state.lightCoords, Direction.NORTH, poseStack, collector);
                poseStack.popPose();
                float angle = working ? (state.time * 10F) % 360F : 0F;
                part(VAT_PADDLE, new Vec3(8, 0, 8), Axis.YP.rotationDegrees(angle), Vec3.ZERO, state, poseStack, collector);
                plate(state.pattern, new Vec3(8, 7.25, 1.6), 0.45F, state.lightCoords, poseStack, collector);
            }
            default -> {}
        }
        poseStack.popPose();
    }

    // A standalone model (its own block model, drawn here): turned about a point of the model (pixels), and moved by
    // offset (pixels)
    private static void part(StandaloneModelKey<BlockStateModelPart> key, Vec3 about, org.joml.Quaternionf turn, Vec3 offset, State state,
                             PoseStack poseStack, SubmitNodeCollector collector)
    {
        BlockStateModelPart model = Minecraft.getInstance().getModelManager().getStandaloneModel(key);
        if (model == null) return;
        poseStack.pushPose();
        Vec3 pivot = about.add(offset).scale(1 / 16.0);
        poseStack.translate(pivot.x, pivot.y, pivot.z);
        poseStack.rotate(turn);
        poseStack.translate(-about.x / 16.0, -about.y / 16.0, -about.z / 16.0);
        collector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), List.of(model), BlockModelRenderState.EMPTY_TINTS,
                state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    // An item standing at a point (pixels), turned yaw degrees about up, at this scale
    private static void item(ItemStackRenderState item, Vec3 at, float yaw, float scale, int light, PoseStack poseStack, SubmitNodeCollector collector)
    {
        if (item.isEmpty()) return;
        poseStack.pushPose();
        poseStack.translate(at.x / 16.0, at.y / 16.0, at.z / 16.0);
        poseStack.rotate(Axis.YP.rotationDegrees(yaw));
        poseStack.scale(scale, scale, scale);
        item.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    // A pattern shown on the front, facing out (an item model faces south: half a turn faces it north, the front)
    private static void plate(ItemStackRenderState item, Vec3 at, float scale, int light, PoseStack poseStack, SubmitNodeCollector collector)
    {
        item(item, at, 180F, scale, light, poseStack, collector);
    }
}
