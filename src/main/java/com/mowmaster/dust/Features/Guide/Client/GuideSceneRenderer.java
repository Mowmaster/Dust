package com.mowmaster.dust.Features.Guide.Client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mowmaster.dust.DustReferences;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.slf4j.Logger;

import java.util.List;

// Draws a guide scene's blocks and items into its picture-in-picture texture (GuideSceneRenderState), all at full light
// with the GUI's item lighting, so every scene looks the same wherever it's opened.
//   blocks          straight from their block state's model (BlockModelResolver: no world needed). Tinted parts take
//                   the scene's "tint" if it gives one, else their colour as in the world, worked out in the scene
//                   (GuideSceneBlocks: a jar's lid the colour of what's in it, grass as in plains)
//   block entities  each block that has one gets it (GuideBlockEntities: made from the scene's "data" and played on
//                   from when the scene set it up), drawn by its own block entity renderer: a jar's dust and labels, a
//                   machine's moving parts. They have no level: a renderer asks GuideContext for the time (the scene's)
//                   and for the blocks round it (the scene's)
//   items           resting or sliding, facing the viewer
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

    private static final Logger LOGGER = LogUtils.getLogger();
    // How far toward the viewer the camera is taken to be, for renderers that ask where it is (blocks)
    private static final float EYE_DISTANCE = 16;

    private final BlockModelRenderState block = new BlockModelRenderState();
    private final ItemStackRenderState item = new ItemStackRenderState();
    private final GuideBlockEntities blockEntities = new GuideBlockEntities();
    private final GuideSceneBlocks sceneBlocks = new GuideSceneBlocks();

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
        GuideFrame frame = state.frame();
        List<GuideFrame.BlockDraw> draws = frame.blocks();
        // The scene as a little world: its blocks, and their block entities made and played on to now
        sceneBlocks.clear();
        blockEntities.beginFrame();
        BlockEntity[] made = new BlockEntity[draws.size()];
        for (int i = 0; i < draws.size(); i++) {
            GuideFrame.BlockDraw draw = draws.get(i);
            made[i] = blockEntities.get(draw, (int) Math.max(0, frame.time() - draw.since()));
            sceneBlocks.put(draw.pos(), draw.state(), made[i]);
        }
        blockEntities.endFrame();
        Vector3f eye = new Matrix4f(state.view()).invert().transformPosition(0, 0, -EYE_DISTANCE, new Vector3f());
        Vec3 eyePos = new Vec3(eye.x, eye.y, eye.z);
        CameraRenderState camera = camera(state.view(), eyePos);
        BlockModelResolver blocks = minecraft.getBlockModelResolver();
        GuideContext.begin(sceneBlocks, frame.time());
        try {
            for (int i = 0; i < draws.size(); i++) {
                GuideFrame.BlockDraw draw = draws.get(i);
                blocks.update(block, draw.state(), DISPLAY);
                boolean rendered = made[i] != null && minecraft.getBlockEntityRenderDispatcher().getRenderer(made[i]) != null;
                // As in the world: a block that isn't drawn as a model there (a chest) is drawn by its block entity
                // renderer alone. Its model here is the one for showing it elsewhere (a chest in a minecart).
                boolean model = !block.isEmpty() && (!rendered || draw.state().getRenderShape() == RenderShape.MODEL);
                if (!model && !rendered) continue;
                pose.pushPose();
                pose.translate(draw.pos().getX(), draw.pos().getY() + draw.lift(), draw.pos().getZ());
                if (draw.size() < 1) {
                    // Shrinking about its middle
                    pose.translate(0.5F, 0.5F, 0.5F);
                    pose.scale(draw.size(), draw.size(), draw.size());
                    pose.translate(-0.5F, -0.5F, -0.5F);
                }
                if (model) {
                    tint(draw);
                    block.submit(pose, collector, FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
                }
                if (rendered) {
                    float age = Math.max(0, frame.time() - draw.since());
                    submitBlockEntity(made[i], age - (int) age, eyePos, pose, collector, camera);
                }
                pose.popPose();
            }
        } finally {
            GuideContext.end();
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

    // The colours of a block's tinted parts: the scene's tint for it, or as in the world (worked out in the scene). A tint
    // source that can't work in the scene keeps the colour it has with no world.
    private void tint(GuideFrame.BlockDraw draw)
    {
        IntList layers = block.tintLayers();
        if (layers.isEmpty()) return;
        if (draw.tint() != null) {
            for (int i = 0; i < layers.size(); i++) layers.set(i, draw.tint().intValue());
            return;
        }
        List<BlockTintSource> sources = Minecraft.getInstance().getBlockColors().getTintSources(draw.state());
        if (sources.size() != layers.size()) return;
        for (int i = 0; i < layers.size(); i++) {
            try {
                layers.set(i, sources.get(i).colorInWorld(draw.state(), sceneBlocks, draw.pos()));
            } catch (RuntimeException ignored) {
                // keeps its colour from no world
            }
        }
    }

    // A block entity drawn by its renderer, as it would be in the world (at full light). It gets a pose stack of its
    // own, so a renderer that fails halfway can't leave the scene's turned.
    private <E extends BlockEntity, S extends BlockEntityRenderState> void submitBlockEntity(E blockEntity, float partialTick, Vec3 eye, PoseStack pose,
                                                                                            SubmitNodeCollector collector, CameraRenderState camera)
    {
        BlockEntityRenderer<E, S> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(blockEntity);
        if (renderer == null) return;
        BlockPos pos = blockEntity.getBlockPos();
        try {
            S renderState = renderer.createRenderState();
            renderer.extractRenderState(blockEntity, renderState, partialTick, eye, null);
            renderState.lightCoords = FULL_BRIGHT;
            PoseStack own = new PoseStack();
            own.last().set(pose.last());
            renderer.submit(renderState, own, collector, camera);
        } catch (RuntimeException e) {
            LOGGER.warn("Guide scene: couldn't draw the block entity of {}", blockEntity.getBlockState(), e);
            blockEntities.failed(pos);
        }
    }

    // Where the camera is for block entity renderers that ask: toward the viewer, and turned so a flat face drawn
    // turned by it (as name tags and other things that face the camera are) faces the viewer. The view turns what's
    // drawn by its rotation, so the camera's turn is that undone, then half a turn about x: in the picture y runs down
    // the screen and the viewer is on the -z side (GuideCamera), the other way round from the world's camera.
    private static CameraRenderState camera(Matrix4f view, Vec3 eye)
    {
        CameraRenderState camera = new CameraRenderState();
        camera.pos = eye;
        camera.blockPos = BlockPos.containing(eye);
        camera.initialized = true;
        camera.orientation = view.getNormalizedRotation(new Quaternionf()).conjugate().mul(new Quaternionf().rotationX((float) Math.PI));
        return camera;
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
