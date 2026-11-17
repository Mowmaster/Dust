package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustRegistries.DustRecipeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

// Two shapeless crafts with a Spell Scroll (a "special" recipe: the result depends on the scroll, so it's code):
//   scroll with a spell + a gold ingot   the same scroll, locked: it no longer follows its tree (SpellSync)
//   any scroll with a spell on it alone  a new blank scroll (a way to reset one)
public class SpellScrollRecipe extends CustomRecipe
{
    public static final SpellScrollRecipe INSTANCE = new SpellScrollRecipe();

    @Override
    public boolean matches(CraftingInput input, Level level)
    {
        return !assemble(input).isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input)
    {
        ItemStack scroll = null;
        boolean gold = false;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.isEmpty()) continue;
            if (stack.is(DustItemRegistry.SPELL_SCROLL.get()) && scroll == null) scroll = stack;
            else if (stack.is(Items.GOLD_INGOT) && !gold) gold = true;
            else return ItemStack.EMPTY;
        }
        if (scroll == null || !written(scroll)) return ItemStack.EMPTY;
        if (!gold) return new ItemStack(DustItemRegistry.SPELL_SCROLL.get());
        if (SpellSync.locked(scroll) || !scroll.has(DustComponentDataRegistry.SPELL.get())) return ItemStack.EMPTY;
        ItemStack locked = scroll.copyWithCount(1);
        SpellSync.lock(locked);
        return locked;
    }

    // Has something on it (a spell, its tree, or a lock): a blank scroll alone makes nothing
    private static boolean written(@Nullable ItemStack scroll)
    {
        return scroll != null && (scroll.has(DustComponentDataRegistry.SPELL.get()) || scroll.has(DustComponentDataRegistry.SPELL_TREE.get())
                || SpellSync.locked(scroll));
    }

    @Override
    public RecipeSerializer<SpellScrollRecipe> getSerializer()
    {
        return DustRecipeRegistry.SPELL_SCROLL_CRAFTING.get();
    }
}
