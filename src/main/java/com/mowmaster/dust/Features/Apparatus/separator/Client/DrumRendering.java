package com.mowmaster.dust.Features.Apparatus.separator.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Machines.Client.StationRendering;
import com.mowmaster.dust.Features.Guide.Client.GuideContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The Dust Separator's drum, shared by the separator and its extensions: the drum model (dust_seperator_drum.json /
// dust_seperator_extension_drum.json, made facing north) turning about its axis (along x in the model, through y 9,
// z 8 pixels), every section by the same angle from the game time so they turn together (in a guide scene, from the
// scene's time: GuideContext).
public final class DrumRendering
{
    public static final StandaloneModelKey<BlockStateModelPart> DRUM = new StandaloneModelKey<>(() -> "dust:dust_seperator_drum");
    public static final StandaloneModelKey<BlockStateModelPart> EXTENSION_DRUM = new StandaloneModelKey<>(() -> "dust:dust_seperator_extension_drum");
    // Degrees a tick the drum turns
    private static final float SPEED = 9F;

    private DrumRendering() {}

    public static void registerModels(ModelEvent.RegisterStandalone event)
    {
        event.register(DRUM, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_seperator_drum")));
        event.register(EXTENSION_DRUM, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_seperator_extension_drum")));
    }

    // The drum's angle now (degrees), if it's turning; 0 if not. level: the block entity's (none in a guide scene)
    public static float angle(@Nullable Level level, boolean spinning, float partialTick)
    {
        if (!spinning) return 0;
        return GuideContext.ticks(level, partialTick) * SPEED % 360;
    }

    public static void submit(StandaloneModelKey<BlockStateModelPart> key, Direction facing, float angle, int light, PoseStack poseStack, SubmitNodeCollector collector)
    {
        poseStack.pushPose();
        // Turned with the block (the models face north)
        poseStack.translate(0.5, 0, 0.5);
        poseStack.rotate(Axis.YP.rotationDegrees(StationRendering.yaw(facing)));
        poseStack.translate(-0.5, 0, -0.5);
        BlockStateModelPart drum = Minecraft.getInstance().getModelManager().getStandaloneModel(key);
        if (drum != null) {
            poseStack.translate(0, 9 / 16.0, 8 / 16.0);
            poseStack.rotate(Axis.XP.rotationDegrees(angle));
            poseStack.translate(0, -9 / 16.0, -8 / 16.0);
            collector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), List.of(drum), net.minecraft.client.renderer.block.BlockModelRenderState.EMPTY_TINTS,
                    light, OverlayTexture.NO_OVERLAY, 0);
        }
        poseStack.popPose();
    }
}
