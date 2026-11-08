package com.mowmaster.dust.Features.Research.Tabs;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Research.ResearchTab;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Effects: every status effect (Speed, Poison, Dust's Scorched...), shown as a potion bottle in the effect's colour.
// Unlocked by having the effect yourself, or by watching a mob that has it (ResearchObserver). Brewing a potion while
// watching the brewing stand records what it's brewed with, and what lengthens or strengthens it.
public class EffectResearchTab extends ResearchTab
{
    public EffectResearchTab()
    {
        super(Identifier.fromNamespaceAndPath(DustReferences.MODID, "effects"), 500);
    }

    public static Identifier idOf(Holder<MobEffect> effect)
    {
        return BuiltInRegistries.MOB_EFFECT.getKey(effect.value());
    }

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
        return list;
    }

    // A plain potion bottle tinted with the effect's colour (no effect on it, so no tooltip lines)
    @Override
    public ItemStack icon(Identifier entry)
    {
        ItemStack bottle = new ItemStack(Items.POTION);
        int color = BuiltInRegistries.MOB_EFFECT.getValue(entry).getColor();
        bottle.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(color), List.of(), Optional.empty()));
        return bottle;
    }

    // In the list, each effect shows its status effect icon (the one beside the inventory) rather than a bottle.
    // Effect icons are the sprites mob_effect/<effect path> in the effect's namespace (what Hud.getMobEffectSprite
    // works out; worked out here because Hud is client-only and tabs are loaded on the server too).
    @Override
    public Identifier listSprite(Identifier entry)
    {
        return entry.withPrefix("mob_effect/");
    }

    @Override
    public Component name(Identifier entry)
    {
        return BuiltInRegistries.MOB_EFFECT.getValue(entry).getDisplayName();
    }

    // Effects don't drop anything, and nothing drops them
    @Override
    public boolean showsDrops()
    {
        return false;
    }
}
