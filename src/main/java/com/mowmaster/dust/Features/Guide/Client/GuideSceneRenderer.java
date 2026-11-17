package com.mowmaster.dust.Features.Guide.Client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.DustReferences;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;
import org.joml.Matrix4f;

// Draws a guide scene's blocks and items into its picture-in-picture texture (GuideSceneRenderState). Blocks are drawn
// straight from their block state's model (BlockModelResolver: no world needed), all at full light with the GUI's
// item lighting, so every scene looks the same wherever it's opened. Block entity extras (items on a station's pads)
// aren't drawn: a scene shows them with item steps.
// Versions: 26.3 hands renderToTexture a SubmitNodeCollector to submit models to. 26.1.2's renderer is built with a
// buffer source and gets none, so (as vanilla's GuiEntityRenderer does there) the models go to the game's feature
// dispatcher, which is drawn into the texture straight away. draw() is the same for both.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public class GuideSceneRenderer extends PictureInPictureRenderer<GuideSceneRenderState>
{
    // Full brightness (sky and block light 15)
    private static final int FULL_BRIGHT = 0xF000F0;
    // A display context is just a key models can look up how to show themselves by; one of our own
    private static final BlockDisplayContext DISPLAY = BlockDisplayContext.create();

    private final BlockModelRenderState block = new BlockModelRenderState();
    private final ItemStackRenderState item = new ItemStackRenderState();

    //? if >=26.3 {
    public GuideSceneRenderer()
    {
    }

    @Override
    protected void renderToTexture(GuideSceneRenderState state, PoseStack pose, SubmitNodeCollector collector)
    {
        draw(state, pose, collector);
    }
    //?} else {
    /*public GuideSceneRenderer(net.minecraft.client.renderer.MultiBufferSource.BufferSource buffers)
    {
        super(buffers);
    }

    @Override
    protected void renderToTexture(GuideSceneRenderState state, PoseStack pose)
    {
        net.minecraft.client.renderer.feature.FeatureRenderDispatcher features = Minecraft.getInstance().gameRenderer.getFeatureRenderDispatcher();
        draw(state, pose, features.getSubmitNodeStorage());
        features.renderAllFeatures();
    }
    *///?}

    private void draw(GuideSceneRenderState state, PoseStack pose, SubmitNodeCollector collector)
    {
        Minecraft minecraft = Minecraft.getInstance();
        //? if >=26.3 {
        minecraft.gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_3D);
        //?} else {
        /*minecraft.gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
        *///?}
        pose.mulPose(state.view());
        BlockModelResolver blocks = minecraft.getBlockModelResolver();
        for (GuideFrame.BlockDraw draw : state.frame().blocks()) {
            blocks.update(block, draw.state(), DISPLAY);
            if (block.isEmpty()) continue;
            pose.pushPose();
            pose.translate(draw.pos().getX(), draw.pos().getY() + draw.lift(), draw.pos().getZ());
            if (draw.size() < 1) {
                // Shrinking about its middle
                pose.translate(0.5F, 0.5F, 0.5F);
                pose.scale(draw.size(), draw.size(), draw.size());
                pose.translate(-0.5F, -0.5F, -0.5F);
            }
            block.submit(pose, collector, FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
            pose.popPose();
        }
        ItemModelResolver items = minecraft.getItemModelResolver();
        for (GuideFrame.ItemDraw draw : state.frame().items()) {
            if (draw.stack().isEmpty() || minecraft.level == null) continue;
            items.updateForTopItem(item, draw.stack(), ItemDisplayContext.GROUND, minecraft.level, null, 0);
            pose.pushPose();
            pose.translate(draw.at().x, draw.at().y, draw.at().z);
            // Facing the viewer, plus its spin, turned with a matrix: the same call in both versions
            pose.mulPose(new Matrix4f().rotationY((float) Math.toRadians(state.itemYaw() + draw.spin())));
            pose.scale(draw.scale(), draw.scale(), draw.scale());
            item.submit(pose, collector, FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
            pose.popPose();
        }
    }

    // The middle of the texture is the middle of the view (vanilla's default puts it at the bottom)
    @Override
    protected float getTranslateY(int height, int guiScale)
    {
        return height / 2.0F;
    }

    @Override
    public Class<GuideSceneRenderState> getRenderStateClass()
    {
        return GuideSceneRenderState.class;
    }

    @Override
    protected String getTextureLabel()
    {
        return "dust guide scene";
    }

    // GuideSceneRenderer::new is the no-argument constructor on 26.3 (a Supplier) and the buffer-source one on 26.1.2
    // (a Function): the event takes whichever that version has
    @SubscribeEvent
    public static void register(RegisterPictureInPictureRenderersEvent event)
    {
        event.register(GuideSceneRenderState.class, GuideSceneRenderer::new);
    }
}
