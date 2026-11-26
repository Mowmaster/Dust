package com.mowmaster.dust.Features.Bosses.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.DustReferences;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

// An Archon's glowing eyes (like a spider's: the "eyes" render type, lit whatever the light), one white texture
// tinted its element's colour, brightened so dark elements still glow (Chaos, all black: violet)
public class ArchonGlowLayer extends RenderLayer<ArchonRenderState, HumanoidModel<ArchonRenderState>>
{
    private static final Identifier EYES = Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/entity/archon/archon_eyes.png");
    private static final int FULL_BRIGHT = 0xF000F0;

    public ArchonGlowLayer(RenderLayerParent<ArchonRenderState, HumanoidModel<ArchonRenderState>> parent)
    {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, ArchonRenderState state, float yRot, float xRot)
    {
        int color = 0xFF000000 | glow(state.set.element().color());
        //? if >=26.3 {
        collector.order(1).submitModel(getParentModel(), state, poseStack, RenderTypes.eyes(EYES), FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color, null, state.outlineColor);
        //?} else {
        /*collector.order(1).submitModel(getParentModel(), state, poseStack, RenderTypes.eyes(EYES), FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color, null, state.outlineColor, null);
        *///?}
    }

    // The colour scaled up until its brightest channel is full
    private static int glow(int rgb)
    {
        int red = (rgb >> 16) & 0xFF, green = (rgb >> 8) & 0xFF, blue = rgb & 0xFF;
        int max = Math.max(red, Math.max(green, blue));
        if (max == 0) return 0x9050FF;
        return (red * 255 / max) << 16 | (green * 255 / max) << 8 | (blue * 255 / max);
    }
}
