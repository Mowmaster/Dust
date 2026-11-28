package com.mowmaster.dust.Integration.jei;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.Research.RecipeCatalog;
import com.mowmaster.dust.Features.Research.RecipeRecord;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchData;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Dust's JEI plugin (26.3 only; JEI is optional, and this class is only loaded by JEI). It adds two pages to JEI
// (ElementCrafterCategory): the Element Crafter's recipes, and Dust's own crafting table recipes (dust:dust_crafting,
// which JEI's crafting page doesn't know: the research tables...). Each recipe is shown only once the player may make it: a recipe that needs a research
// entry (a Research tab discovery, RecipeCatalog.lockOf) shows once that's unlocked; the rest always show.
// Clients don't get recipes in 26.x, so the recipes are the research book's (RecipeCatalog, sent to every client when
// they join and when datapacks reload). Once a second (and when JEI starts) it adds any new ones, and hides or shows
// each as the player's research changes.
// Nothing here may be referenced from the rest of the mod: without JEI its classes don't exist. So it subscribes to the
// client tick itself (addListener) rather than with @EventBusSubscriber, which would load it whether JEI is there or not.
@JeiPlugin
public class DustJeiPlugin implements IModPlugin
{
    private static final Identifier UID = Identifier.fromNamespaceAndPath(DustReferences.MODID, "jei");
    private static final int REFRESH_TICKS = 20;

    private static @Nullable IJeiRuntime runtime;
    private static boolean listening;
    // The recipes given to JEI on each page, and those of them hidden now
    private static final java.util.Map<IRecipeType<RecipeRecord>, Set<RecipeRecord>> ADDED = new java.util.HashMap<>();
    private static final java.util.Map<IRecipeType<RecipeRecord>, Set<RecipeRecord>> HIDDEN = new java.util.HashMap<>();
    private static int ticks;

    @Override
    public Identifier getPluginUid()
    {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {
        registration.addRecipeCategories(ElementCrafterCategory.elementCrafter(registration.getJeiHelpers().getGuiHelper()),
                ElementCrafterCategory.dustCrafting(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addCraftingStation(ElementCrafterCategory.TYPE, DustBlockRegistry.ELEMENT_CRAFTER.get());
        registration.addCraftingStation(ElementCrafterCategory.DUST_CRAFTING, net.minecraft.world.level.block.Blocks.CRAFTING_TABLE);
    }

    // The recipes themselves are added once JEI is running (refresh): the catalog may arrive before or after JEI starts
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
    {
        runtime = jeiRuntime;
        ADDED.clear();
        HIDDEN.clear();
        if (!listening) {
            listening = true;
            NeoForge.EVENT_BUS.addListener(DustJeiPlugin::onClientTick);
        }
        refresh();
    }

    @Override
    public void onRuntimeUnavailable()
    {
        runtime = null;
        ADDED.clear();
        HIDDEN.clear();
    }

    private static void onClientTick(ClientTickEvent.Post event)
    {
        if (++ticks % REFRESH_TICKS == 0) refresh();
    }

    // Adds the catalog's new recipes to each page, then hides the locked ones and shows the unlocked ones
    private static void refresh()
    {
        IJeiRuntime jei = runtime;
        Player player = Minecraft.getInstance().player;
        if (jei == null || player == null) return;
        Identifier crafter = BuiltInRegistries.ITEM.getKey(DustBlockRegistry.ELEMENT_CRAFTER.get().asItem());
        ResearchData research = Research.get(player);
        refresh(jei.getRecipeManager(), research, ElementCrafterCategory.TYPE, recipe -> recipe.station().filter(crafter::equals).isPresent());
        refresh(jei.getRecipeManager(), research, ElementCrafterCategory.DUST_CRAFTING, RecipeCatalog::isDustTableRecipe);
    }

    private static void refresh(IRecipeManager recipes, ResearchData research, IRecipeType<RecipeRecord> type, java.util.function.Predicate<RecipeRecord> onPage)
    {
        Set<RecipeRecord> current = new HashSet<>();
        for (List<RecipeRecord> list : RecipeCatalog.all().values()) {
            for (RecipeRecord recipe : list) {
                if (onPage.test(recipe)) current.add(recipe);
            }
        }
        Set<RecipeRecord> added = ADDED.computeIfAbsent(type, key -> new HashSet<>());
        Set<RecipeRecord> hidden = HIDDEN.computeIfAbsent(type, key -> new HashSet<>());
        List<RecipeRecord> fresh = current.stream().filter(recipe -> !added.contains(recipe)).toList();
        if (!fresh.isEmpty()) {
            recipes.addRecipes(type, fresh);
            added.addAll(fresh);
        }
        List<RecipeRecord> hide = new ArrayList<>();
        List<RecipeRecord> show = new ArrayList<>();
        for (RecipeRecord recipe : added) {
            // Gone from the catalog (a datapack reload took it away): hidden for good
            boolean visible = current.contains(recipe) && unlocked(research, recipe);
            if (!visible && !hidden.contains(recipe)) hide.add(recipe);
            else if (visible && hidden.contains(recipe)) show.add(recipe);
        }
        if (!hide.isEmpty()) {
            recipes.hideRecipes(type, hide);
            hidden.addAll(hide);
        }
        if (!show.isEmpty()) {
            recipes.unhideRecipes(type, show);
            show.forEach(hidden::remove);
        }
    }

    // Whether the player may make the recipe: it needs no research, or they've unlocked the entry it needs (a Research
    // tab discovery: all of them count while DustConfig.ALL_RESEARCH_UNLOCKED is on)
    private static boolean unlocked(ResearchData research, RecipeRecord recipe)
    {
        return RecipeCatalog.lockOf(recipe).map(key -> {
            ResearchEntry entry = Research.byKey(key.tab(), key.entry());
            return entry != null && research.has(entry);
        }).orElse(true);
    }
}
