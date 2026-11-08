package com.mowmaster.dust.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mowmaster.dust.Features.DustComposition.DustRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// Dust recipes need the crafter to have unlocked their research, but a recipe is never told who's crafting.
// CraftingMenu.slotChangedCraftingGrid is where the server picks the result for a crafting table or the inventory's
// 2x2 grid, and it knows the player: it asks resultSlots.setRecipeUsed(player, recipe) whether this player may use
// the recipe (vanilla's answer is about the "limited crafting" game rule) and leaves the result empty if not.
// This adds Dust's own question to that one: a dust recipe the player hasn't unlocked gives no result.
//
// @WrapOperation (MixinExtras, which NeoForge ships) wraps that one method call: our method runs in its place and
// decides whether to call the original (original.call). Several mods can wrap the same call without clashing.
@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin
{
    @WrapOperation(method = "slotChangedCraftingGrid",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setRecipeUsed(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/crafting/RecipeHolder;)Z"))
    private static boolean dust$checkResearch(ResultContainer resultSlots, ServerPlayer player, RecipeHolder<?> recipe, Operation<Boolean> original)
    {
        return DustRecipe.allows(player, recipe) && original.call(resultSlots, player, recipe);
    }
}
