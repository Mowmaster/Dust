package com.mowmaster.dust.Features.Spells;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemUseAnimation;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.pylons.SpellTreeData;
import com.mowmaster.dust.Features.Spells.pylons.SpellTreeLink;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// A Spell Scroll: holds one spell (the dust:spell data component), recorded from a spell pylon (step 2; for now
// /dustmagic spell gives one). Right-click to cast it (SpellCasting). A blank scroll does nothing. Its seal is tinted
// the spell's element colour (Client/SpellTint); its tooltip says what the spell is (Client/SpellTooltips).
public class SpellScrollItem extends Item
{
    // Ticks between casts
    private static final int COOLDOWN = 10;

    public SpellScrollItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        Spell spell = stack.get(DustComponentDataRegistry.SPELL.get());
        if (spell == null) {
            if (!level.isClientSide()) player.sendOverlayMessage(Component.translatable("dust.spell.blank"));
            return InteractionResult.FAIL;
        }
        // A Beam spell is channelled: held down, it pulses (onUseTick)
        if (Channeling.channels(spell)) {
            if (player.getCooldowns().isOnCooldown(stack)) return InteractionResult.FAIL;
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
        if (player instanceof ServerPlayer serverPlayer && castFrom(serverPlayer, stack)) {
            player.getCooldowns().addCooldown(stack, cooldownFor(spell));
        }
        return InteractionResult.SUCCESS;
    }

    // ---- channelling a Beam spell (held use) ----

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity)
    {
        Spell spell = stack.get(DustComponentDataRegistry.SPELL.get());
        return spell != null && Channeling.channels(spell) ? Channeling.MAX_USE : 0;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack)
    {
        return ItemUseAnimation.BOW;
    }

    // Each tick it's held: a pulse every interval (Efficiency cheaper the longer it's held); out of dust, it stops
    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining)
    {
        if (!(entity instanceof ServerPlayer player)) return;
        Spell spell = stack.get(DustComponentDataRegistry.SPELL.get());
        if (spell == null) return;
        int held = Channeling.MAX_USE - remaining;
        if (held % Channeling.interval(spell) != 0) return;
        if (!castFrom(player, stack, Channeling.reduction(spell, held))) player.stopUsingItem();
    }

    // Let go: the usual wait before it can be cast again
    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remaining)
    {
        Spell spell = stack.get(DustComponentDataRegistry.SPELL.get());
        if (entity instanceof Player player && spell != null) player.getCooldowns().addCooldown(stack, cooldownFor(spell));
        return true;
    }

    // Casts the spell on a scroll (held, or kept in a Grimoire) and gives its tree the experience. True if it was cast.
    public static boolean castFrom(ServerPlayer player, ItemStack scroll)
    {
        return castFrom(player, scroll, 0);
    }

    // The same, with this much (percent) off the cost (a channelled beam's Efficiency)
    public static boolean castFrom(ServerPlayer player, ItemStack scroll, int costReduction)
    {
        Spell spell = scroll.get(DustComponentDataRegistry.SPELL.get());
        if (spell == null || !SpellCasting.cast(player, spell, costReduction)) return false;
        giveExperience(player, spell, scroll.get(DustComponentDataRegistry.SPELL_TREE.get()));
        return true;
    }

    // A cast's experience to its spell's tree (scrolls, Grimoires, spell tools), wherever the tree is: 1, or with Spell
    // XP Gain 1 + 0.1 a level, rounded up. Each level the tree gains is a point of the caster's Magic Level experience
    // (MagicCredits: taken back if the spell is broken).
    public static void giveExperience(ServerPlayer player, Spell spell, @org.jspecify.annotations.Nullable SpellTreeLink link)
    {
        if (link != null) {
            SpellTreeData trees = SpellTreeData.get(player.level().getServer());
            int before = com.mowmaster.dust.Features.Spells.pylons.SpellLevels.levelForXp(trees.experience(link.tree()));
            // xpPercent: Spell XP Gain's bonus in percent (10 a level); Math.ceil rounds up
            int xp = (int) Math.ceil((100 + spell.stats(1F).xpPercent()) / 100.0);
            // Cast from the Akashic: ten times as much
            if (SpellCasting.isAkashic()) xp *= SpellCasting.AKASHIC_XP;
            trees.addExperience(link.tree(), xp);
            // How far they've taken a spell of this element (fusions need two at FUSION_LEVEL)
            int treeLevel = com.mowmaster.dust.Features.Spells.pylons.SpellLevels.levelForXp(trees.experience(link.tree()));
            SpellMastery.note(player, spell.element(), treeLevel);
            com.mowmaster.dust.Features.Magic.MagicCredits.get(player.level().getServer()).credit(player, link.tree(), treeLevel - before);
        }
    }

    public static final int CAST_COOLDOWN = COOLDOWN;

    // The wait before this spell can be cast again: the usual, less Cast Speed (at least 1 tick)
    public static int cooldownFor(Spell spell)
    {
        int cooldown = Math.max(1, COOLDOWN + spell.stats(1F).cooldown());
        // Sloth: half the wait
        return com.mowmaster.dust.Features.Spells.modifiers.Sins.slothful(spell) ? Math.max(1, cooldown / 2) : cooldown;
    }
}
