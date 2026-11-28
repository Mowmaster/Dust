package com.mowmaster.dust.Features.Spells.Client;

import com.google.common.reflect.TypeToken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Spells.effects.BeastAttackPayload;
import com.mowmaster.dust.Features.Spells.effects.Beastification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

// How a player turned into a creature (effects/Beastification) is drawn, and their swings at the air:
//   each player turned has a stand-in creature of their form here on the client (never added to the level). Each tick
//   it's moved and turned with them, so it walks and looks as they do (calculateEntityAnimation: its legs).
//   When the player's render state is filled in (a render state modifier), the stand-in's is filled in too and kept on
//   it as render data (FORM_STATE); when the player is about to be drawn (RenderLivingEvent.Pre), they're drawn as the
//   stand-in instead (their own body, armour and held items aren't drawn).
//   a swing with a weapon at nothing (LeftClickEmpty) is sent to the server (BeastAttackPayload), which does the
//   creature's attack
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class BeastificationClient
{
    private static final ContextKey<EntityRenderState> FORM_STATE = new ContextKey<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "beast_form"));
    // Each turned player's stand-in. Client side only, where entity ids are unique, so a WeakHashMap keyed by the player
    // is safe; a player that's gone is dropped.
    private static final Map<Player, LivingEntity> STAND_INS = new WeakHashMap<>();
    // The next stand-in's id (counting down from here)
    private static int nextId = -100_000;

    private BeastificationClient() {}

    // ---- the stand-ins ----

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            STAND_INS.clear();
            return;
        }
        STAND_INS.keySet().removeIf(player -> player.level() != level || Beastification.formOf(player) == null);
        for (Player player : level.players()) {
            EntityType<?> form = Beastification.formOf(player);
            if (form == null) continue;
            LivingEntity standIn = STAND_INS.get(player);
            if (standIn == null || standIn.getType() != form) {
                if (!(form.create(level, EntitySpawnReason.LOAD) instanceof LivingEntity made)) continue;
                // 26.3 won't draw an entity that has no id; a stand-in is never added to the level, so it gets one of
                // its own, below zero where no real entity's id ever is
                made.setId(nextId--);
                made.snapTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
                standIn = made;
                STAND_INS.put(player, standIn);
            }
            follow(standIn, player);
        }
    }

    // Moves and turns the stand-in with its player, keeping last tick's place and turn for smooth drawing
    private static void follow(LivingEntity standIn, Player player)
    {
        standIn.setOldPosAndRot();
        standIn.yBodyRotO = standIn.yBodyRot;
        standIn.yHeadRotO = standIn.yHeadRot;
        standIn.setPos(player.getX(), player.getY(), player.getZ());
        standIn.setYRot(player.getYRot());
        standIn.setXRot(player.getXRot());
        standIn.yBodyRot = player.yBodyRot;
        standIn.yHeadRot = player.yHeadRot;
        standIn.setOnGround(player.onGround());
        standIn.setShiftKeyDown(player.isShiftKeyDown());
        standIn.tickCount = player.tickCount;
        standIn.calculateEntityAnimation(false);
    }

    // ---- drawing ----

    @SubscribeEvent
    public static void registerModifiers(RegisterRenderStateModifiersEvent event)
    {
        event.registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {},
                (LivingEntity entity, LivingEntityRenderState state) -> {
                    LivingEntity standIn = entity instanceof Player player ? STAND_INS.get(player) : null;
                    EntityRenderState form = standIn == null ? null : Minecraft.getInstance().getEntityRenderDispatcher().extractEntity(standIn, state.partialTick);
                    // Upside down (the Inversion fusion): the creature is drawn upside down too
                    if (form instanceof LivingEntityRenderState living && com.mowmaster.dust.Features.Spells.effects.GravityInversion.inverted(entity)) living.isUpsideDown = true;
                    // Set every time: render states are reused
                    state.setRenderData(FORM_STATE, form);
                });
    }

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<?, ?, ?> event)
    {
        EntityRenderState form = event.getRenderState().getRenderData(FORM_STATE);
        if (form == null) return;
        event.setCanceled(true);
        submit(form, event.getPoseStack(), event.getSubmitNodeCollector());
    }

    // Draws a render state with its own renderer, where the pose stack already is (the player's place)
    private static <S extends EntityRenderState> void submit(S state, PoseStack poseStack, SubmitNodeCollector collector)
    {
        EntityRenderer<?, ? super S> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(state);
        renderer.submit(state, poseStack, collector, camera());
    }

    private static CameraRenderState camera()
    {
        //? if >=26.3 {
        return Minecraft.getInstance().gameRenderer.gameRenderState().levelRenderState.cameraRenderState;
        //?} else {
        /*return Minecraft.getInstance().gameRenderer.getGameRenderState().levelRenderState.cameraRenderState;
        *///?}
    }

    // ---- swinging at the air ----

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event)
    {
        Player player = event.getEntity();
        if (Beastification.formOf(player) == null || !player.getMainHandItem().has(DataComponents.WEAPON)) return;
        ClientPacketDistributor.sendToServer(new BeastAttackPayload());
    }

    // The stand-in drawn for a player, if they're turned (for anything else that wants it)
    public static @Nullable Entity standIn(Player player)
    {
        return STAND_INS.get(player);
    }
}
