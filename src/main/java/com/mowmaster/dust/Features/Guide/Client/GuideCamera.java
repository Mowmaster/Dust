package com.mowmaster.dust.Features.Guide.Client;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

// How a guide scene is looked at: one matrix used both to draw it (GuideSceneRenderer) and to find where a point of it
// lands on screen (project), so the text boxes and outlines drawn over it line up with the blocks.
//   yaw     degrees turned round from looking at its south side (45: from the south-east)
//   pitch   degrees looking down
//   scale   GUI pixels per block
// The picture-in-picture texture puts its origin in the middle and y down the screen, with the viewer on the -z side
// (as for vanilla's inventory entity, which turns its model upside down the same way: rotateZ(PI)).
// JOML: rotateX/Y/Z and translate each multiply onto the end, so they apply to a point in the opposite order to how
// they're written: translate (centre the scene) first, then yaw, then pitch, then the flip.
public record GuideCamera(Matrix4f view, float scale, float centreX, float centreY)
{
    public static GuideCamera of(Vector3f focus, float yaw, float pitch, float scale, ScreenRectangle viewport)
    {
        Matrix4f view = new Matrix4f()
                .rotateZ((float) Math.PI)
                .rotateX((float) Math.toRadians(-pitch))
                .rotateY((float) Math.toRadians(180 - yaw))
                .translate(-focus.x, -focus.y, -focus.z);
        return new GuideCamera(view, scale, viewport.left() + viewport.width() / 2F, viewport.top() + viewport.height() / 2F);
    }

    // Where a point of the scene (blocks) is on screen (GUI pixels)
    public Vector2f project(double x, double y, double z)
    {
        Vector3f at = view.transformPosition((float) x, (float) y, (float) z, new Vector3f());
        return new Vector2f(centreX + at.x * scale, centreY + at.y * scale);
    }
}
