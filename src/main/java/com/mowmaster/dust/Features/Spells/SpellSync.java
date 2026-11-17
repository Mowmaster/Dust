package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.pylons.SpellTreeLink;
import com.mowmaster.dust.Features.Spells.pylons.SpellTreeSpells;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

// Keeps a recorded spell (a Spell Scroll, a scroll in a Grimoire, a spell tool) up to date with its tree, so it needn't
// be recorded again after every pylon: the Spell Pylon publishes its spell as it changes (SpellTreeSpells), and the
// item picks it up as it's carried (inventoryTick) and before it's cast.
//   locked (dust:spell_locked)   never changes again: a scroll crafted with a gold ingot (SpellScrollRecipe), or one
//                                whose Spell Pylon was broken
public final class SpellSync
{
    private SpellSync() {}

    public static boolean locked(ItemStack stack)
    {
        return stack.has(DustComponentDataRegistry.SPELL_LOCKED.get());
    }

    public static void lock(ItemStack stack)
    {
        stack.set(DustComponentDataRegistry.SPELL_LOCKED.get(), Unit.INSTANCE);
    }

    // Brings the item's spell up to date with its tree; locks it if the tree's Spell Pylon is gone. True if it changed.
    public static boolean refresh(ItemStack stack, MinecraftServer server)
    {
        SpellTreeLink link = stack.get(DustComponentDataRegistry.SPELL_TREE.get());
        if (link == null || locked(stack)) return false;
        SpellTreeSpells trees = SpellTreeSpells.get(server);
        if (trees.ended(link.tree())) {
            lock(stack);
            return true;
        }
        SpellTreeSpells.Current now = trees.current(link.tree());
        if (now == null) return false;
        Spell held = stack.get(DustComponentDataRegistry.SPELL.get());
        if (Objects.equals(held, now.spell()) && link.pylons() == now.pylons()) return false;
        stack.set(DustComponentDataRegistry.SPELL.get(), now.spell());
        stack.set(DustComponentDataRegistry.SPELL_TREE.get(), new SpellTreeLink(link.tree(), now.pylons()));
        return true;
    }
}
