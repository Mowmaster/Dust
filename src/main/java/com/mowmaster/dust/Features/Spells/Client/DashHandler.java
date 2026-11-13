package com.mowmaster.dust.Features.Spells.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

// Dashing (Lightning on yourself): double-tap a movement key to dash a couple of blocks that way, ledge or not. A
// player moves themselves on their own client (the server accepts it), so this all happens client side.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class DashHandler
{
    // Two presses this close together (ticks) are a double tap; dashes this far apart at least
    private static final int DOUBLE_TAP_TICKS = 7;
    private static final int COOLDOWN_TICKS = 20;
    private static final double DASH_SPEED = 1.1;

    // Per movement key (forward, back, left, right): whether it was down last tick, and when it was last pressed
    private static final boolean[] WAS_DOWN = new boolean[4];
    private static final long[] LAST_PRESS = new long[4];
    private static long lastDash = -COOLDOWN_TICKS;

    private DashHandler() {}

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) return;
        long now = minecraft.level.getGameTime();
        KeyMapping[] keys = {minecraft.options.keyUp, minecraft.options.keyDown, minecraft.options.keyLeft, minecraft.options.keyRight};
        // Each key's direction relative to where the player faces: (sideways, forwards)
        double[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for (int i = 0; i < keys.length; i++) {
            boolean down = keys[i].isDown();
            if (down && !WAS_DOWN[i]) {
                boolean doubleTap = now - LAST_PRESS[i] <= DOUBLE_TAP_TICKS;
                LAST_PRESS[i] = now;
                if (doubleTap && now - lastDash >= COOLDOWN_TICKS && player.hasEffect(DustEffectsRegistry.DASHING)) {
                    dash(player, directions[i][0], directions[i][1]);
                    lastDash = now;
                    LAST_PRESS[i] = 0;
                }
            }
            WAS_DOWN[i] = down;
        }
    }

    private static void dash(LocalPlayer player, double sideways, double forwards)
    {
        // The movement relative to the facing, turned to the world by the player's yaw
        double yaw = Math.toRadians(player.getYRot());
        double x = sideways * Math.cos(yaw) - forwards * Math.sin(yaw);
        double z = forwards * Math.cos(yaw) + sideways * Math.sin(yaw);
        Vec3 push = new Vec3(x, 0, z).normalize().scale(DASH_SPEED);
        player.setDeltaMovement(push.x, Math.max(player.getDeltaMovement().y, 0.15), push.z);
        for (int i = 0; i < 8; i++) {
            player.level().addParticle(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY() + 0.2 + i * 0.2, player.getZ(), -push.x * 0.2, 0, -push.z * 0.2);
        }
    }
}
