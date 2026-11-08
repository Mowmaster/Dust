package com.mowmaster.dust.Features.Research.Tabs;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Research.EnchantmentIcons;
import com.mowmaster.dust.Features.Research.ResearchTab;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Effects: every status effect (Speed, Poison, Dust's Scorched...), then every enchantment.
//
// Status effects show their status effect icon in the list and a potion bottle in the effect's colour elsewhere.
// Unlocked by having the effect yourself, or by watching a mob that has it (ResearchObserver). Brewing a potion while
// watching the brewing stand records what it's brewed with, and what lengthens or strengthens it.
//
// Enchantments show their icon from EnchantmentIcons in the list and an enchanted book elsewhere. Unlocked by holding
// an item that has one, or enchanting something at an enchanting table.
// Their entry ids are the enchantment id with "enchantment/" in front of the path (minecraft:enchantment/sharpness),
// so an enchantment can never be mistaken for an effect with the same name.
public class EffectResearchTab extends ResearchTab
{
    private static final String ENCHANTMENT_PREFIX = "enchantment/";

    public EffectResearchTab()
    {
        super(Identifier.fromNamespaceAndPath(DustReferences.MODID, "effects"), 500);
    }

    public static Identifier idOf(Holder<MobEffect> effect)
    {
        return BuiltInRegistries.MOB_EFFECT.getKey(effect.value());
    }

    // ---- enchantment entries ----

    public static Identifier enchantmentEntry(Identifier enchantment)
    {
        return enchantment.withPrefix(ENCHANTMENT_PREFIX);
    }

    // The entry id for an enchantment holder (null for one that isn't registered, which shouldn't happen).
    // unwrapKey() gives the registry key the holder was registered under, if it has one.
    public static @Nullable Identifier enchantmentEntry(Holder<Enchantment> enchantment)
    {
        return enchantment.unwrapKey().map(key -> enchantmentEntry(key.identifier())).orElse(null);
    }

    public static boolean isEnchantment(Identifier entry)
    {
        return entry.getPath().startsWith(ENCHANTMENT_PREFIX);
    }

    // minecraft:enchantment/sharpness -> minecraft:sharpness
    public static Identifier enchantmentId(Identifier entry)
    {
        return Identifier.fromNamespaceAndPath(entry.getNamespace(), entry.getPath().substring(ENCHANTMENT_PREFIX.length()));
    }

    // Enchantments live in a "datapack registry": loaded with the world's data (so datapacks can add some), not fixed
    // at startup like effects. So they're looked up through the world's RegistryAccess, which ResearchTabs keeps.
    public static Optional<Holder.Reference<Enchantment>> enchantment(Identifier entry)
    {
        RegistryAccess registries = ResearchTabs.registries();
        if (registries == null) return Optional.empty();
        return registries.lookupOrThrow(Registries.ENCHANTMENT).get(ResourceKey.create(Registries.ENCHANTMENT, enchantmentId(entry)));
    }

    // ---- the tab ----

    @Override
    public ItemStack tabIcon()
    {
        return new ItemStack(Items.BREWING_STAND);
    }

    @Override
    protected List<Identifier> computeEntries()
    {
        List<Identifier> list = new ArrayList<>();
        for (MobEffect effect : BuiltInRegistries.MOB_EFFECT) list.add(BuiltInRegistries.MOB_EFFECT.getKey(effect));
        RegistryAccess registries = ResearchTabs.registries();
        if (registries != null) {
            // listElementIds() streams every registered key; forEachOrdered keeps the registry's order
            registries.lookupOrThrow(Registries.ENCHANTMENT).listElementIds()
                    .forEachOrdered(key -> list.add(enchantmentEntry(key.identifier())));
        }
        return list;
    }

    // Effects: a plain potion bottle tinted with the effect's colour (no effect on it, so no tooltip lines).
    // Enchantments: an enchanted book.
    @Override
    public ItemStack icon(Identifier entry)
    {
        if (isEnchantment(entry)) return new ItemStack(Items.ENCHANTED_BOOK);
        ItemStack bottle = new ItemStack(Items.POTION);
        int color = BuiltInRegistries.MOB_EFFECT.getValue(entry).getColor();
        bottle.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(color), List.of(), Optional.empty()));
        return bottle;
    }

    // In the list, each effect shows its status effect icon (the one beside the inventory) rather than a bottle.
    // Effect icons are the sprites mob_effect/<effect path> in the effect's namespace (what Hud.getMobEffectSprite
    // works out; worked out here because Hud is client-only and tabs are loaded on the server too).
    // Enchantments show their icon from EnchantmentIcons, or the enchanted book if they have none.
    @Override
    public @Nullable Identifier listSprite(Identifier entry)
    {
        if (isEnchantment(entry)) return EnchantmentIcons.sprite(enchantmentId(entry));
        return entry.withPrefix("mob_effect/");
    }

    @Override
    public Component name(Identifier entry)
    {
        if (isEnchantment(entry)) {
            // The enchantment's own name if the world's data is loaded, else the usual lang key
            // (enchantment.minecraft.sharpness; Util.makeDescriptionId builds "<prefix>.<namespace>.<path>")
            return enchantment(entry).map(holder -> holder.value().description())
                    .orElseGet(() -> Component.translatable(Util.makeDescriptionId("enchantment", enchantmentId(entry))));
        }
        return BuiltInRegistries.MOB_EFFECT.getValue(entry).getDisplayName();
    }

    @Override
    public Component defaultNote(Identifier entry)
    {
        if (isEnchantment(entry)) return Component.translatable("research.dust.note.default.enchantments", name(entry));
        return super.defaultNote(entry);
    }

    // Effects don't drop anything, and nothing drops them
    @Override
    public boolean showsDrops()
    {
        return false;
    }
}
