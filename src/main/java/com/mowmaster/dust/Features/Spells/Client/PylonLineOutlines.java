package com.mowmaster.dust.Features.Spells.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Spells.pylons.SpellPylonBlockEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

import java.util.ArrayList;
import java.util.List;

// A Spell Pylon right-clicked with an empty hand: its tree's lines are outlined for SHOW_TICKS (client only, nothing
// is sent): the Spell Pylon and the main line in gold, each branch in its own colour (SpellPylonBlockEntity.lines).
// Drawn like MowLib's LinkOutlines (which only outlines its ILinkable blocks, so it can't be used for pylons): two
// steps, as 26.x draws the level -
//   ExtractLevelRenderStateEvent   copy which blocks, their shapes and colours into the frame's render state
//   SubmitCustomGeometryEvent      hand their edges to the SubmitNodeCollector as lines (submitShapeOutline: what
//                                  vanilla uses for the block you're looking at)
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class PylonLineOutlines
{
    public static final int SHOW_TICKS = 20 * 10;
    // The main line's colour, then the branches' in turn (0xAARRGGBB, fully opaque)
    private static final int MAIN = 0xFFFFD24A;
    private static final int[] BRANCHES = {0xFF55FFFF, 0xFFFF55FF, 0xFF55FF55, 0xFFFF7744, 0xFF7799FF, 0xFFFFFFFF, 0xFFFF5577, 0xFFAAFF55};
    private static final float LINE_WIDTH = 3.0F;
    // A ContextKey names one piece of custom data on the render state (like a typed map key)
    private static final ContextKey<List<Outline>> OUTLINES = new ContextKey<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "pylon_lines"));

    private record Outline(BlockPos pos, VoxelShape shape, int color) {}
    private record Shown(BlockPos pos, int color) {}

    // What's shown: in which dimension, until when (game time), which blocks in which colours
    private static @org.jspecify.annotations.Nullable ResourceKey<Level> dimension;
    private static long until;
    private static List<Shown> shown = List.of();

    private PylonLineOutlines() {}

    // Outlines this Spell Pylon's tree for SHOW_TICKS (replacing any shown before)
    public static void show(SpellPylonBlockEntity pylon)
    {
        Level level = pylon.getLevel();
        if (level == null) return;
        List<Shown> list = new ArrayList<>();
        list.add(new Shown(pylon.getBlockPos(), MAIN));
        List<List<Integer>> lines = pylon.lines();
        List<BlockPos> chain = pylon.chain();
        for (int line = 0; line < lines.size(); line++) {
            int color = line == 0 ? MAIN : BRANCHES[(line - 1) % BRANCHES.length];
            for (int index : lines.get(line)) if (index < chain.size()) list.add(new Shown(chain.get(index).immutable(), color));
        }
        shown = List.copyOf(list);
        dimension = level.dimension();
        until = level.getGameTime() + SHOW_TICKS;
    }

    // ---- step 1: what to draw (the level can be read here) ----

    @SubscribeEvent
    public static void extract(ExtractLevelRenderStateEvent event)
    {
        ClientLevel level = event.getLevel();
        if (shown.isEmpty()) return;
        if (level.dimension() != dimension || level.getGameTime() > until) {
            shown = List.of();
            return;
        }
        List<Outline> outlines = new ArrayList<>();
        for (Shown entry : shown) {
            if (!level.isLoaded(entry.pos())) continue;
            VoxelShape shape = level.getBlockState(entry.pos()).getShape(level, entry.pos());
            // A broken pylon (air): no outline
            if (shape.isEmpty()) continue;
            outlines.add(new Outline(entry.pos(), shape, entry.color()));
        }
        event.getRenderState().setRenderData(OUTLINES, outlines);
    }

    // ---- step 2: hand the lines to the renderer ----

    @SubscribeEvent
    public static void submit(SubmitCustomGeometryEvent event)
    {
        List<Outline> outlines = event.getLevelRenderState().getRenderData(OUTLINES);
        if (outlines == null || outlines.isEmpty()) return;
        // The pose stack is relative to the camera, so each block is moved by (its position - the camera's)
        Vec3 camera = event.getLevelRenderState().cameraRenderState.pos;
        PoseStack poseStack = event.getPoseStack();
        for (Outline outline : outlines) {
            poseStack.pushPose();
            poseStack.translate(outline.pos().getX() - camera.x, outline.pos().getY() - camera.y, outline.pos().getZ() - camera.z);
            // Stonecutter: shape outlines became a render feature in 26.2; before that, ShapeRenderer drew the lines
            // straight into the game's line buffer (as MowLib's LinkOutlines does on 26.1.2)
            //? if >=26.2 {
            event.getSubmitNodeCollector().submitShapeOutline(poseStack, outline.shape(), RenderTypes.lines(), outline.color(), LINE_WIDTH, false);
            //?} else {
            /*com.mojang.blaze3d.vertex.VertexConsumer lines = net.minecraft.client.Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderTypes.lines());
            net.minecraft.client.renderer.ShapeRenderer.renderShape(poseStack, lines, outline.shape(), 0, 0, 0, outline.color(), LINE_WIDTH);
            *///?}
            poseStack.popPose();
        }
    }
}
