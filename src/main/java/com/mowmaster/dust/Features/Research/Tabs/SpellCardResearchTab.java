package com.mowmaster.dust.Features.Research.Tabs;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import com.mowmaster.dust.Features.Research.ResearchTab;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import com.mowmaster.dust.Features.Spells.ModifierInfo;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import com.mowmaster.dust.Features.Spells.modifiers.LeveledModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

// Spell Cards: every spell modifier, shown by its card. An entry stays faded until the player first has that modifier
// in a spell (a pylon of theirs draws it, or they cast a spell with it: unlock). Its overview says what it does, what
// one level adds (ModifierInfo), and how it comes: which pylons, from what spell level, how often, how many levels a
// node, any limits.
public class SpellCardResearchTab extends ResearchTab
{
    public SpellCardResearchTab()
    {
        super(Identifier.fromNamespaceAndPath(DustReferences.MODID, "spell_cards"), 560);
    }

    // The player has had this modifier in a spell: its entry, if they don't have it yet (with the usual toast)
    public static void unlock(ServerPlayer player, Identifier modifier)
    {
        ResearchEntry entry = new ResearchEntry(ResearchTabs.SPELL_CARDS, modifier);
        if (!Research.get(player).has(entry)) Research.observe(player, entry, player.blockPosition());
    }

    @Override
    public ItemStack tabIcon()
    {
        return new ItemStack(DustItemRegistry.CARD_AOE.get());
    }

    @Override
    protected List<Identifier> computeEntries()
    {
        List<Identifier> list = new ArrayList<>();
        for (var entry : SpellRegistries.MODIFIER_REGISTRY.entrySet()) list.add(entry.getKey().identifier());
        return list;
    }

    @Override
    public ItemStack icon(Identifier entry)
    {
        return DustItemRegistry.cardOf(entry);
    }

    @Override
    public Component name(Identifier entry)
    {
        return Component.translatable("spell_modifier." + entry.getNamespace() + "." + entry.getPath());
    }

    // What it does and how it comes, a line each
    @Override
    public Component defaultNote(Identifier entry)
    {
        SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(entry);
        MutableComponent note = Component.empty();
        if (ModifierInfo.perLevel(entry) != null) {
            note.append(Component.translatable("research.dust.card.per_level", ModifierInfo.perLevelLine(entry)));
        } else {
            note.append(ModifierInfo.description(entry));
        }
        if (modifier == null) return note;
        note.append("\n\n");
        // Where it comes from
        if (modifier.tier() == ModifierTier.BLESSED) note.append(Component.translatable("research.dust.card.blessed"));
        else if (modifier.tier() == ModifierTier.CORRUPTED) note.append(Component.translatable("research.dust.card.corrupted"));
        else note.append(Component.translatable("research.dust.card.pylons", pylons(modifier)));
        // From what spell level, how often, how many levels a node
        if (modifier.unlockLevel() > 0) note.append("\n").append(Component.translatable("research.dust.card.unlock", modifier.unlockLevel()));
        if (modifier instanceof LeveledModifier leveled) {
            if (leveled.everyLevelsCount() > 0) note.append("\n").append(Component.translatable("research.dust.card.every", leveled.everyLevelsCount()));
            if (!leveled.allowedShapes().isEmpty()) {
                MutableComponent shapes = Component.empty();
                for (Identifier shape : leveled.allowedShapes()) {
                    if (!shapes.getSiblings().isEmpty()) shapes.append(", ");
                    shapes.append(Component.translatable("spell_shape." + shape.getNamespace() + "." + shape.getPath()));
                }
                note.append("\n").append(Component.translatable("research.dust.card.shapes", shapes));
            }
            if (leveled.branchOnly()) note.append("\n").append(Component.translatable("research.dust.card.once_per_branch"));
        }
        if (modifier.singleLevel()) {
            note.append("\n").append(Component.translatable("research.dust.card.one_level"));
        } else if (modifier.tier() == ModifierTier.NORMAL) {
            int min = modifier.minLevelsPerNode(), max = modifier.maxLevelsPerNode();
            note.append("\n").append(min == max ? Component.translatable("research.dust.card.per_node", min)
                    : Component.translatable("research.dust.card.per_node_range", min, max));
        }
        if (modifier.oncePerSpell()) note.append("\n").append(Component.translatable("research.dust.card.once_per_spell"));
        else if (modifier.uses() > 0) note.append("\n").append(Component.translatable("research.dust.card.uses", modifier.uses()));
        return note;
    }

    // "Damage, Neutral": its categories' pylons, and the Neutral pylon unless it's for category pylons only
    private static Component pylons(SpellModifier modifier)
    {
        MutableComponent text = Component.empty();
        for (ModifierCategory category : modifier.categories()) {
            if (!text.getSiblings().isEmpty()) text.append(", ");
            text.append(Component.translatable("research.dust.card.category." + category.getSerializedName()));
        }
        if (!modifier.categoryPylonsOnly()) {
            if (!text.getSiblings().isEmpty()) text.append(", ");
            text.append(Component.translatable("research.dust.card.category.neutral"));
        }
        return text;
    }

    // Spell cards are neither dropped nor made of dust
    @Override
    public boolean showsDrops()
    {
        return false;
    }

    @Override
    public boolean showsDustPage()
    {
        return false;
    }

    @Override
    public boolean bottomTab()
    {
        return true;
    }
}
