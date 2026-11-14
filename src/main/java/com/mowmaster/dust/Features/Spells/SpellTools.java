package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.DustDataGen.DustTags;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.Spells.api.Spell;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

// Spell tools: a crystal weapon or tool used on a Spell Pylon takes its spell (a Touch spell only:
// SpellPylonBlockEntity.recordTool), and casts it as it's used:
//   weapons (tag dust:spell_weapons: the crystal sword and spear)   on hitting a creature, at that creature
//   tools (tag dust:spell_tools: the crystal pickaxe, axe, shovel, hoe, mattock, hammer)   on striking a block, at it
// The tool pays the mana in durability (a point per mana, at least 1); the dust comes from the player as usual. Its
// casts give the spell's tree experience like a scroll's, and it waits the spell's cooldown between casts.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class SpellTools
{
    private SpellTools() {}

    public static boolean isSpellTool(ItemStack stack)
    {
        return stack.is(DustTags.Items.SPELL_WEAPONS) || stack.is(DustTags.Items.SPELL_TOOLS);
    }

    // A weapon striking a creature
    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || !(event.getTarget() instanceof LivingEntity target)) return;
        ItemStack weapon = player.getMainHandItem();
        if (!weapon.is(DustTags.Items.SPELL_WEAPONS)) return;
        cast(player, weapon, cast -> {
            cast.applyEntity(target);
            return true;
        });
    }

    // A tool striking a block (the moment it starts to mine it)
    @SubscribeEvent
    public static void onStrikeBlock(PlayerInteractEvent.LeftClickBlock event)
    {
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START || !(event.getEntity() instanceof ServerPlayer player)) return;
        ItemStack tool = player.getMainHandItem();
        if (!tool.is(DustTags.Items.SPELL_TOOLS) || event.getFace() == null) return;
        cast(player, tool, cast -> {
            cast.applyBlock(event.getPos(), event.getFace());
            return true;
        });
    }

    // Casts the tool's spell (if it has one and isn't cooling down), then the cooldown and the tree's experience
    private static void cast(ServerPlayer player, ItemStack tool, java.util.function.Predicate<com.mowmaster.dust.Features.Spells.api.SpellCast> landing)
    {
        Spell spell = tool.get(DustComponentDataRegistry.SPELL.get());
        if (spell == null || player.getCooldowns().isOnCooldown(tool)) return;
        Spell held = spell;
        var link = tool.get(DustComponentDataRegistry.SPELL_TREE.get());
        if (!SpellCasting.castWithTool(player, held, tool, EquipmentSlot.MAINHAND, landing)) return;
        player.getCooldowns().addCooldown(tool, SpellScrollItem.cooldownFor(held));
        SpellScrollItem.giveExperience(player, held, link);
    }
}
