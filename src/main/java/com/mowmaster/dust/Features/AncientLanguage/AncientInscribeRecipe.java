package com.mowmaster.dust.Features.AncientLanguage;

import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustRegistries.DustRecipeRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

// Ancient Ink + one item with writing on it -> the same item with its writing in the ancient script:
// a written book's pages, an item's custom name (anvil name) and its lore. The ink is used up.
// A "special" recipe: its result depends on the input, so it's code rather than a json pattern.
public class AncientInscribeRecipe extends CustomRecipe
{
    public static final AncientInscribeRecipe INSTANCE = new AncientInscribeRecipe();

    @Override
    public boolean matches(CraftingInput input, Level level)
    {
        return findTarget(input) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput input)
    {
        ItemStack target = findTarget(input);
        if (target == null) return ItemStack.EMPTY;

        ItemStack result = target.copyWithCount(1);

        WrittenBookContent book = result.get(DataComponents.WRITTEN_BOOK_CONTENT);
        if (book != null) {
            // Filterable = the raw text plus an optional chat-filtered copy; map changes both
            result.set(DataComponents.WRITTEN_BOOK_CONTENT,
                    book.withReplacedPages(book.pages().stream().map(page -> page.map(text -> (Component) AncientLanguage.inscribe(text))).toList()));
        }
        Component name = result.get(DataComponents.CUSTOM_NAME);
        if (name != null) {
            result.set(DataComponents.CUSTOM_NAME, AncientLanguage.inscribe(name));
        }
        ItemLore lore = result.get(DataComponents.LORE);
        if (lore != null) {
            result.set(DataComponents.LORE, new ItemLore(lore.lines().stream().map(line -> (Component) AncientLanguage.inscribe(line)).toList()));
        }
        return result;
    }

    @Override
    public RecipeSerializer<AncientInscribeRecipe> getSerializer()
    {
        return DustRecipeRegistry.ANCIENT_INSCRIBE.get();
    }

    // Exactly one Ancient Ink and one other item that has some plain (not yet ancient) writing; else null
    private static @Nullable ItemStack findTarget(CraftingInput input)
    {
        ItemStack ink = null;
        ItemStack target = null;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.isEmpty()) continue;
            if (stack.is(DustItemRegistry.ANCIENT_INK.get())) {
                if (ink != null) return null;
                ink = stack;
            } else {
                if (target != null) return null;
                target = stack;
            }
        }
        return ink != null && target != null && hasPlainWriting(target) ? target : null;
    }

    private static boolean hasPlainWriting(ItemStack stack)
    {
        WrittenBookContent book = stack.get(DataComponents.WRITTEN_BOOK_CONTENT);
        if (book != null && book.pages().stream().anyMatch(page -> !AncientLanguage.isAncient(page.raw()))) return true;
        Component name = stack.get(DataComponents.CUSTOM_NAME);
        if (name != null && !AncientLanguage.isAncient(name)) return true;
        ItemLore lore = stack.get(DataComponents.LORE);
        return lore != null && lore.lines().stream().anyMatch(line -> !AncientLanguage.isAncient(line));
    }
}
