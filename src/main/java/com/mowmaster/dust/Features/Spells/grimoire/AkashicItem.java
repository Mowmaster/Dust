package com.mowmaster.dust.Features.Spells.grimoire;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Spells.SpellCasting;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.Spell;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.resource.ResourceStack;
import org.jspecify.annotations.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

// The Akashic: a grimoire with as many pages as there are grimoire page keys (GrimoireItem.PAGES). Spells cast from it
// cost no dust or mana, and their trees get ten times the spell experience (SpellCasting.isAkashic,
// SpellScrollItem.giveExperience). Otherwise they're cast exactly as from a Grimoire.
// Held in either hand, it also shows a Spell Pylon's pool on the reading HUD (SpellPylonReading).
// It belongs to one player (OWNER): they're given one the first time they join a world (onLogIn; once per world, kept
// through death). Anyone else carrying it, unless in creative mode, sees it vanish with a poof (inventoryTick).
// Every-spell mode: until a Spell Scroll is first put in it, it holds every spell there is at its first level (each
// element with each shape, then each fusion with each shape: everySpell), with no tree (so no experience). The grimoire keys' next / previous turn through
// them, saying "Element - Shape" over the hotbar. Once a scroll goes in, those spells are gone for good (AKASHIC_SPELL
// set to -1) and it works as a Grimoire of its own scrolls.
@EventBusSubscriber(modid = DustReferences.MODID)
public class AkashicItem extends GrimoireItem
{
    public static final UUID OWNER = UUID.fromString("a32d6e2c-7936-43b1-aa20-396c7a298314");
    private static final int CLOSED = -1;
    private static @Nullable List<Spell> everySpell;
    public AkashicItem(Properties properties)
    {
        super(properties);
    }

    // Whether this player holds one, in either hand
    public static boolean holds(Player player)
    {
        return player.getMainHandItem().getItem() instanceof AkashicItem || player.getOffhandItem().getItem() instanceof AkashicItem;
    }

    // ---- whose it is ----

    // Whether this player may have it: its owner, or anyone in creative mode
    public static boolean allowed(Player player)
    {
        return player.isCreative() || OWNER.equals(player.getUUID());
    }

    // Gone, with a poof, from a player who may not have it
    private static void vanish(ServerLevel level, Entity holder, ItemStack stack)
    {
        stack.setCount(0);
        level.playSound(null, holder.getX(), holder.getY(), holder.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.8F, 1.4F);
        level.sendParticles(ParticleTypes.POOF, holder.getX(), holder.getY() + 1, holder.getZ(), 12, 0.3, 0.4, 0.3, 0.02);
    }

    // The owner's first time in this world: one Akashic
    @SubscribeEvent
    public static void onLogIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || !OWNER.equals(player.getUUID())) return;
        if (player.getData(DustAttachmentTypeRegistry.AKASHIC_GIVEN)) return;
        player.setData(DustAttachmentTypeRegistry.AKASHIC_GIVEN, true);
        ItemStack akashic = new ItemStack(DustItemRegistry.AKASHIC.get());
        if (!player.getInventory().add(akashic)) player.spawnAtLocation(player.level(), akashic);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot)
    {
        if (entity instanceof Player player && !allowed(player)) {
            vanish(level, player, stack);
            return;
        }
        // A scroll put in closes the every-spell pages for good
        if (codex(stack) && hasScrolls(stack)) stack.set(DustComponentDataRegistry.AKASHIC_SPELL.get(), CLOSED);
        super.inventoryTick(stack, level, entity, slot);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        if (!allowed(player)) {
            if (level instanceof ServerLevel server) vanish(server, player, player.getItemInHand(hand));
            return InteractionResult.FAIL;
        }
        return super.use(level, player, hand);
    }

    @Override
    public boolean castPage(ServerPlayer player, ItemStack grimoire, int page)
    {
        if (!allowed(player)) {
            vanish(player.level(), player, grimoire);
            return false;
        }
        return SpellCasting.akashic(() -> super.castPage(player, grimoire, page));
    }

    // ---- every-spell mode ----

    // Every spell at its first level: each element (in order) with each shape (in the order they're registered)
    public static List<Spell> everySpell()
    {
        if (everySpell == null) {
            List<Identifier> shapes = new ArrayList<>();
            for (var shape : SpellRegistries.SHAPE_REGISTRY) {
                Identifier id = SpellRegistries.SHAPE_REGISTRY.getKey(shape);
                if (id != null) shapes.add(id);
            }
            List<Spell> spells = new ArrayList<>();
            for (DustElement element : DustElements.all()) {
                for (Identifier shape : shapes) spells.add(new Spell(element, shape, Map.of(), List.of()));
            }
            // Then every fusion with each shape (a fused spell is its first element's, carrying the fusion)
            for (var modifier : SpellRegistries.MODIFIER_REGISTRY) {
                if (!(modifier instanceof com.mowmaster.dust.Features.Spells.modifiers.FusionModifier fusion)) continue;
                Identifier id = SpellRegistries.MODIFIER_REGISTRY.getKey(modifier);
                if (id == null) continue;
                for (Identifier shape : shapes) spells.add(new Spell(fusion.first(), shape, Map.of(), List.of(id)));
            }
            everySpell = List.copyOf(spells);
        }
        return everySpell;
    }

    private static boolean hasScrolls(ItemStack stack)
    {
        return stack.getItem() instanceof AkashicItem akashic && !akashic.contents(stack).stacks().stream().allMatch(ResourceStack::isEmpty);
    }

    // Whether it's still in every-spell mode: no scroll has ever gone in
    public static boolean codex(ItemStack stack)
    {
        return stack.getOrDefault(DustComponentDataRegistry.AKASHIC_SPELL.get(), 0) != CLOSED && !hasScrolls(stack);
    }

    private static int chosen(ItemStack stack)
    {
        int size = everySpell().size();
        return size == 0 ? 0 : Math.floorMod(stack.getOrDefault(DustComponentDataRegistry.AKASHIC_SPELL.get(), 0), size);
    }

    // In every-spell mode, whatever page is asked for is the chosen spell, on a scroll made for it
    @Override
    public ItemStack scrollAt(ItemStack grimoire, int page)
    {
        if (!codex(grimoire) || everySpell().isEmpty()) return super.scrollAt(grimoire, page);
        ItemStack scroll = new ItemStack(DustItemRegistry.SPELL_SCROLL.get());
        scroll.set(DustComponentDataRegistry.SPELL.get(), everySpell().get(chosen(grimoire)));
        return scroll;
    }

    @Override
    public Component turn(ItemStack stack, int direction)
    {
        if (!codex(stack) || everySpell().isEmpty()) return super.turn(stack, direction);
        stack.set(DustComponentDataRegistry.AKASHIC_SPELL.get(), Math.floorMod(chosen(stack) + (direction < 0 ? -1 : 1), everySpell().size()));
        return describe(stack, getPage(stack));
    }

    // "Fire - Projectile"
    @Override
    public Component describe(ItemStack stack, int page)
    {
        if (!codex(stack) || everySpell().isEmpty()) return super.describe(stack, page);
        Spell spell = everySpell().get(chosen(stack));
        Identifier fusion = com.mowmaster.dust.Features.Spells.SpellNames.fusionOf(spell);
        Component name = fusion != null ? com.mowmaster.dust.Features.Spells.SpellNames.fusionName(fusion) : spell.element().name();
        return Component.translatable("dust.akashic.spell", name,
                Component.translatable("spell_shape." + spell.shape().getNamespace() + "." + spell.shape().getPath()));
    }

    // Every cast from it goes through these three: the boost is switched on just for the cast
    @Override
    protected boolean castHeld(ServerPlayer player, ItemStack scroll)
    {
        return SpellCasting.akashic(() -> super.castHeld(player, scroll));
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining)
    {
        SpellCasting.akashic(() -> {
            super.onUseTick(level, entity, stack, remaining);
            return true;
        });
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        builder.accept(Component.translatable("item.dust.akashic.tooltip.xp").withStyle(ChatFormatting.GOLD));
        builder.accept(Component.translatable("item.dust.akashic.tooltip.cost").withStyle(ChatFormatting.GOLD));
        builder.accept(Component.translatable("item.dust.akashic.tooltip.pool").withStyle(ChatFormatting.GRAY));
        if (codex(stack)) builder.accept(Component.translatable("item.dust.akashic.tooltip.every_spell", everySpell().size()).withStyle(ChatFormatting.LIGHT_PURPLE));
        super.appendHoverText(stack, context, display, builder, flag);
    }
}
