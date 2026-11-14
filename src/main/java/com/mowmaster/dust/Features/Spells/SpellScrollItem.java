package com.mowmaster.dust.Features.Spells;

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
        if (player instanceof ServerPlayer serverPlayer && castFrom(serverPlayer, stack)) {
            player.getCooldowns().addCooldown(stack, COOLDOWN);
        }
        return InteractionResult.SUCCESS;
    }

    // Casts the spell on a scroll (held, or kept in a Grimoire) and gives its tree the experience. True if it was cast.
    public static boolean castFrom(ServerPlayer player, ItemStack scroll)
    {
        Spell spell = scroll.get(DustComponentDataRegistry.SPELL.get());
        if (spell == null || !SpellCasting.cast(player, spell)) return false;
        // The spell's tree gains experience: one per pylon the spell used, wherever the tree is
        SpellTreeLink link = scroll.get(DustComponentDataRegistry.SPELL_TREE.get());
        if (link != null) {
            SpellTreeData trees = SpellTreeData.get(player.level().getServer());
            trees.addExperience(link.tree(), link.pylons());
            // How far they've taken a spell of this element (fusions need two at FUSION_LEVEL)
            int treeLevel = com.mowmaster.dust.Features.Spells.pylons.SpellLevels.levelForXp(trees.experience(link.tree()));
            SpellMastery.note(player, spell.element(), treeLevel);
            // The first tree of theirs to reach the Beam level unlocks Beam for them, for every element
            if (treeLevel >= com.mowmaster.dust.Features.Spells.pylons.PylonConfig.BEAM_LEVEL
                    && SpellKnowledge.unlockShape(player, SpellRegistries.BEAM.getId())) {
                player.sendSystemMessage(Component.translatable("dust.pylon.beam_unlocked"));
            }
        }
        return true;
    }

    public static final int CAST_COOLDOWN = COOLDOWN;
}
