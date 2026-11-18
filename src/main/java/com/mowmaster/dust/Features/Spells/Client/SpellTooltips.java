package com.mowmaster.dust.Features.Spells.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellTarget;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;
import java.util.Map;

// A Spell Scroll's tooltip: its element and shape, each attribute's level, its modifiers, and what it costs
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class SpellTooltips
{
    private SpellTooltips() {}

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event)
    {
        Spell spell = event.getItemStack().get(DustComponentDataRegistry.SPELL.get());
        if (spell == null) return;
        List<Component> lines = event.getToolTip();
        int at = Math.min(1, lines.size());
        // A fused spell goes by its fusion's name (SpellNames.spellForClient)
        lines.add(at++, Component.translatable("dust.spell.tooltip.element",
                        com.mowmaster.dust.Features.Spells.SpellNames.spellForClient(spell.element(), com.mowmaster.dust.Features.Spells.SpellNames.fusionOf(spell)),
                        name("spell_shape", spell.shape()))
                .withStyle(ChatFormatting.GRAY));
        // Locked (SpellSync): it no longer follows its tree
        if (event.getItemStack().has(DustComponentDataRegistry.SPELL_LOCKED.get())) lines.add(at++, Component.translatable("dust.spell.tooltip.locked").withStyle(ChatFormatting.GOLD));
        // Whether its effect lingers or is instant (EffectType): a Self spell lands on the caster, the others on a
        // creature or a block, whichever they hit
        com.mowmaster.dust.Features.Spells.api.ElementEffect effect = com.mowmaster.dust.Features.Spells.SpellRegistries.ELEMENT_EFFECT_REGISTRY.getValue(spell.element().id());
        if (effect != null) {
            Component type = spell.shape().equals(com.mowmaster.dust.Features.Spells.SpellRegistries.SELF.getId())
                    ? Component.translatable("dust.spell.tooltip.type_self", effect.type(SpellTarget.SELF).displayName())
                    : Component.translatable("dust.spell.tooltip.type_hit", effect.type(SpellTarget.ENTITY).displayName(), effect.type(SpellTarget.BLOCK).displayName());
            lines.add(at++, type.copy().withStyle(ChatFormatting.DARK_GRAY));
        }
        for (Map.Entry<Identifier, Integer> attribute : spell.attributes().entrySet()) {
            lines.add(at++, Component.translatable("dust.spell.tooltip.attribute", name("spell_attribute", attribute.getKey()), attribute.getValue())
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
        for (int i = 0; i < spell.modifiers().size(); i++) {
            // Its name for this spell's element and shape ("Scorch Stacks" for Potency on a Fire bolt): SpellNames
            Component line = Component.literal(" ").append(com.mowmaster.dust.Features.Spells.SpellNames.modifier(spell.modifiers().get(i), spell.element(), spell.shape(),
                    com.mowmaster.dust.Features.Spells.SpellNames.fusionOf(spell)));
            // Its levels: " Damage 4"
            if (spell.level(i) > 1) line = line.copy().append(" " + spell.level(i));
            lines.add(at++, line.copy().withStyle(ChatFormatting.DARK_PURPLE));
        }
        net.minecraft.network.chat.MutableComponent cost = Component.empty();
        // A Spell Stack's cost is all of its spells'
        spell.totalDustCost().forEach((element, amount) -> {
            if (!cost.getSiblings().isEmpty()) cost.append(", ");
            cost.append(amount + " ").append(com.mowmaster.dust.Features.DustComposition.ElementNames.forClient(element));
        });
        lines.add(at, Component.translatable("dust.spell.tooltip.cost", cost).withStyle(ChatFormatting.DARK_GRAY));
    }

    // "spell_shape.dust.projectile" -> "Projectile" (from the lang file)
    private static Component name(String kind, Identifier id)
    {
        return Component.translatable(kind + "." + id.getNamespace() + "." + id.getPath());
    }
}
