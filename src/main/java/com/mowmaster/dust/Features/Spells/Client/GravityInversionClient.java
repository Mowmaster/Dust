package com.mowmaster.dust.Features.Spells.Client;

import com.google.common.reflect.TypeToken;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Spells.effects.GravityInversion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

// How upside-down creatures look (effects/GravityInversion):
//   drawn upside down: the render state's isUpsideDown, vanilla's own flip for a mob named Dinnerbone or Grumm (turned
//   over inside its box, so its feet are at the top). The modifier runs for every LivingEntityRenderer and its
//   subclasses, players' too.
//   the view of an upside-down player (or whatever the camera is on) is rolled half a turn. Only the picture turns:
//   the mouse and the movement keys still work as they do the right way up, so on screen left and right are swapped.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class GravityInversionClient
{
    private GravityInversionClient() {}

    @SubscribeEvent
    public static void registerModifiers(RegisterRenderStateModifiersEvent event)
    {
        event.registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {},
                (LivingEntity entity, LivingEntityRenderState state) -> {
                    if (GravityInversion.inverted(entity)) state.isUpsideDown = true;
                });
    }

    @SubscribeEvent
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event)
    {
        Entity viewer = Minecraft.getInstance().getCameraEntity();
        if (viewer != null && GravityInversion.inverted(viewer)) event.setRoll(event.getRoll() + 180F);
    }
}
