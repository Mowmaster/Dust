package com.mowmaster.dust.Features.Guide.Client;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;

// One frame of a guide scene, handed to the GUI to draw as a "picture in picture" (GuideSceneRenderer renders it into
// a texture of its own, which is then drawn in the screen's rectangle x0,y0 - x1,y1). 26.x draws GUIs this way:
// screens only describe what to draw (render states); the drawing happens later.
//   frame     what's there (GuidePlayback.at)
//   view      the camera (GuideCamera.view)
//   itemYaw   degrees to turn resting items so they face the viewer
//   scale     GUI pixels per block
public record GuideSceneRenderState(GuideFrame frame, Matrix4f view, float itemYaw, int x0, int y0, int x1, int y1, float scale,
                                    @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements PictureInPictureRenderState
{
    public GuideSceneRenderState(GuideFrame frame, Matrix4f view, float itemYaw, int x0, int y0, int x1, int y1, float scale,
                                 @Nullable ScreenRectangle scissorArea)
    {
        this(frame, view, itemYaw, x0, y0, x1, y1, scale, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
    }
}
