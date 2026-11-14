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
        lines.add(at++, Component.translatable("dust.spell.tooltip.element", spell.element().name(), name("spell_shape", spell.shape()))
                .withStyle(ChatFormatting.GRAY));
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
            Component line = Component.literal(" ").append(name("spell_modifier", spell.modifiers().get(i)));
            // A blessed / corrupted modifier's rolled strength: " Surging 34%"
            if (spell.strength(i) > 0) line = line.copy().append(" " + spell.strength(i) + "%");
            lines.add(at++, line.copy().withStyle(ChatFormatting.DARK_PURPLE));
        }
        StringBuilder cost = new StringBuilder();
        spell.dustCost().forEach((element, amount) -> cost.append(cost.isEmpty() ? "" : ", ").append(amount).append(' ').append(element.name().getString()));
        lines.add(at, Component.translatable("dust.spell.tooltip.cost", cost.toString()).withStyle(ChatFormatting.DARK_GRAY));
    }

    // "spell_shape.dust.projectile" -> "Projectile" (from the lang file)
    private static Component name(String kind, Identifier id)
    {
        return Component.translatable(kind + "." + id.getNamespace() + "." + id.getPath());
    }
}
