package com.mowmaster.dust.Features.Spells.modifiers;

import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import com.mowmaster.dust.Features.Spells.api.ModifierContext;
import com.mowmaster.dust.Features.Spells.api.SpellFragment;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import com.mowmaster.dust.Features.Spells.pylons.PylonConfig;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// The Fragment slot: a stand-in in the pool for "a fragment". It can be rolled when
//   the tree has reached more fragment milestones (PylonConfig.FRAGMENT_LEVELS: 25, 50, 75, 100) than the line has fragments
//   the line has had a spell break for each fragment it would then hold
//   there's a fragment for the spell's element that isn't in the line yet
// Rolled, it becomes one of those fragments at random (resolveRoll); the pylon never shows the slot itself.
public class FragmentSlotModifier implements SpellModifier
{
    @Override
    public ModifierCategory category() { return ModifierCategory.CHANGE; }

    @Override
    public int weight() { return 8; }

    @Override
    public boolean canRoll(ModifierContext context)
    {
        long fragments = context.countOf(SpellFragment.class);
        return fragments < PylonConfig.fragmentSlots(context.level()) && fragments < context.breaks() && !candidates(context).isEmpty();
    }

    @Override
    public @Nullable Identifier resolveRoll(ModifierContext context, RandomSource random)
    {
        List<Identifier> candidates = candidates(context);
        return candidates.isEmpty() ? null : candidates.get(random.nextInt(candidates.size()));
    }

    // Fragments for this element (or any element) not in the line yet
    private static List<Identifier> candidates(ModifierContext context)
    {
        List<Identifier> candidates = new ArrayList<>();
        for (var entry : SpellRegistries.MODIFIER_REGISTRY.entrySet()) {
            Identifier id = entry.getKey().identifier();
            if (entry.getValue() instanceof SpellFragment fragment && fragment.fitsElement(context.element()) && !context.has(id)) candidates.add(id);
        }
        return candidates;
    }
}
