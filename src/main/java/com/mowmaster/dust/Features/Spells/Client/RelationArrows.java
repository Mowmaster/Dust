package com.mowmaster.dust.Features.Spells.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.Spells.radiance.RadianceRelations;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

// Arrows around the crosshair pointing at players you have a standing with (Radiance): red at players you've fought
// in the last 5 minutes, green at players you've marked friendly. Straight up means straight ahead; an arrow on the
// right means they're to your right, at the bottom means behind you. Only players your client can see (loaded nearby).
// Read from your own RadianceRelations, which the server syncs to you alone.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class RelationArrows
{
    private static final Identifier LAYER = Identifier.fromNamespaceAndPath(DustReferences.MODID, "relation_arrows");
    // A white arrow pointing up (16x16), tinted red or green when drawn
    private static final Identifier ARROW = Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/gui/relation_arrow.png");
    private static final int SIZE = 16;
    // How far from the crosshair the arrows sit, in GUI pixels
    private static final float RADIUS = 30;
    private static final int RED = 0xFFFF4040;
    private static final int GREEN = 0xFF40FF40;

    private RelationArrows() {}

    @SubscribeEvent
    public static void registerLayer(RegisterGuiLayersEvent event)
    {
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, LAYER, RelationArrows::render);
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker)
    {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer self = minecraft.player;
        if (self == null || minecraft.level == null) return;
        RadianceRelations relations = self.getData(DustAttachmentTypeRegistry.RADIANCE_RELATIONS);
        if (relations.hostile().isEmpty() && relations.friends().isEmpty()) return;
        long now = minecraft.level.getGameTime();
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
        for (Player other : minecraft.level.players()) {
            if (other == self) continue;
            if (relations.isHostile(other.getUUID(), now)) arrow(graphics, self, other, partialTick, RED);
            else if (relations.isFriend(other.getUUID(), now)) arrow(graphics, self, other, partialTick, GREEN);
        }
    }

    private static void arrow(GuiGraphicsExtractor graphics, LocalPlayer self, Player other, float partialTick, int color)
    {
        Vec3 to = other.getPosition(partialTick).subtract(self.getPosition(partialTick));
        // The direction to them as a yaw (Minecraft's: 0 = south, turning right adds), minus the way you face, wrapped
        // to -180..180: 0 ahead, 90 to the right, 180 behind
        float yaw = (float) (Mth.atan2(to.z, to.x) * Mth.RAD_TO_DEG) - 90.0F;
        float relative = Mth.wrapDegrees(yaw - self.getViewYRot(partialTick));
        // The GUI's pose is a 2D matrix stack: move to the screen centre, turn, then draw the arrow above the centre
        graphics.pose().pushMatrix();
        graphics.pose().translate(graphics.guiWidth() / 2.0F, graphics.guiHeight() / 2.0F);
        graphics.pose().rotate(relative * Mth.DEG_TO_RAD);
        // blit(pipeline, texture, x, y, u, v, width, height, textureWidth, textureHeight, colour)
        graphics.blit(RenderPipelines.GUI_TEXTURED, ARROW, -SIZE / 2, (int) -RADIUS - SIZE / 2, 0, 0, SIZE, SIZE, SIZE, SIZE, color);
        graphics.pose().popMatrix();
    }
}
