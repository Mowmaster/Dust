package com.mowmaster.dust.Features.Spells.Client;

import com.google.common.reflect.TypeToken;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Spells.effects.PetGhosts;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

// Pets' ghosts (PetGhosts) are drawn see-through. Vanilla already has this look: it's how a spectator sees an invisible
// mob. LivingEntityRenderer draws the body translucent when its render state says isInvisible (the body is hidden)
// but not isInvisibleToPlayer (this player may still see it). Ghosts get exactly that. Like an invisible mob, the
// extra layers (collar, saddle, armour) aren't drawn.
// Vanilla's translucent body is very faint, so ghosts are also tagged with render data (GHOST), and
// mixin/client/LivingEntityRendererGhostMixin draws them with GHOST_COLOR instead.
// A render state modifier is NeoForge's hook into 26.3's split rendering: it runs right after a renderer's
// extractRenderState has filled the state from the entity, so it can change what gets drawn without a mixin.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class GhostRendering
{
    // A ghost's tint: ARGB, the top byte is the opacity. 0x80 is about 50% (vanilla's is 0x26, about 15%). Raise it
    // to make ghosts more solid, lower it for fainter.
    /*
    0x26	about 15% (vanilla)
    0x60	about 38%
    0x80	about 50% (now)
    0xB0	about 70%
    */
    public static final int GHOST_COLOR = 0x80FFFFFF;

    // Render data: NeoForge lets mods put their own values on a render state, under a key. This one says "a ghost".
    public static final ContextKey<Boolean> GHOST = new ContextKey<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "pet_ghost"));

    private GhostRendering() {}

    @SubscribeEvent
    public static void registerModifiers(RegisterRenderStateModifiersEvent event)
    {
        // TypeToken: Guava's way to write a generic type as a value (a plain Class can't carry the <...> part). This
        // one means "every LivingEntityRenderer, whatever entity and state it uses", so every mob's renderer gets it.
        event.registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {},
                (LivingEntity entity, LivingEntityRenderState state) -> {
                    boolean ghost = PetGhosts.isGhost(entity);
                    // Set every time, so a revived pet's reused render state doesn't stay a ghost
                    state.setRenderData(GHOST, ghost);
                    if (ghost) {
                        state.isInvisible = true;
                        state.isInvisibleToPlayer = false;
                    }
                });
    }
}
