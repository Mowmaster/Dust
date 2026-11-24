package com.mowmaster.dust.Features.Trees;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.DustRegistries.DustRecipeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.EnumMap;
import java.util.Map;

// A Fruit Medley: a bowl and FRUITS elemental fruits, of at most MAX_KINDS kinds, in any slots (a "special" recipe: the
// medley remembers which fruits went in, FruitMedley)
public class FruitMedleyRecipe extends CustomRecipe
{
    public static final FruitMedleyRecipe INSTANCE = new FruitMedleyRecipe();
    public static final int FRUITS = 6;
    public static final int MAX_KINDS = 3;

    @Override
    public boolean matches(CraftingInput input, Level level)
    {
        return !assemble(input).isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input)
    {
        boolean bowl = false;
        int fruits = 0;
        Map<ElementalWood, Integer> counts = new EnumMap<>(ElementalWood.class);
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.isEmpty()) continue;
            if (stack.is(Items.BOWL) && !bowl) bowl = true;
            else if (stack.getItem() instanceof ElementalFruitItem fruit) {
                counts.merge(fruit.wood(), 1, Integer::sum);
                fruits++;
            }
            else return ItemStack.EMPTY;
        }
        if (!bowl || fruits != FRUITS || counts.size() > MAX_KINDS) return ItemStack.EMPTY;
        ItemStack medley = new ItemStack(ElementalWoods.MEDLEY.get());
        medley.set(DustComponentDataRegistry.FRUIT_MEDLEY.get(), new FruitMedley(counts));
        return medley;
    }

    @Override
    public RecipeSerializer<FruitMedleyRecipe> getSerializer()
    {
        return DustRecipeRegistry.FRUIT_MEDLEY.get();
    }
}
