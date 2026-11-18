package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustRegistries.DustPotionRegistry;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.AncientInscribeRecipe;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustComposition.DustRecipe;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import com.mowmaster.dust.Features.Research.ResearchUnlocks;
import net.minecraft.client.color.item.Dye;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.data.recipes.BrewingRecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.Optional;

public class DustProviderBasicRecipe extends RecipeProvider {
    // 26.3: recipes (and their advancements) are registry entries. The provider is built by the registry
    // bootstrap that create() returns, which DustDataGenerator hands to GatherDataRegistryEntriesEvent.
    protected DustProviderBasicRecipe(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput)
    {
        super(recipeOutput, advancementOutput);
    }

    public static MultiRegistryBootstrap create()
    {
        return RecipeProvider.asBootstrap(DustProviderBasicRecipe::new);
    }



    @Override
    protected void buildRecipes() {
        buildBrewingRecipes();

        // Ancient Ink: an ink sac steeped in black (chaos) and white (order) dust. Placeholder recipe.
        shapeless(RecipeCategory.MISC, DustItemRegistry.ANCIENT_INK.get())
                .requires(Items.INK_SAC)
                .requires(Items.GOLD_NUGGET)
                .requires(Items.GLASS_BOTTLE)
                .unlockedBy(getHasName(Items.INK_SAC), has(Items.INK_SAC))
                .save(output);
        // Research Notes: Ancient Ink, a feather and a book
        shapeless(RecipeCategory.MISC, DustItemRegistry.RESEARCH_NOTES.get())
                .requires(DustItemRegistry.ANCIENT_INK.get())
                .requires(Items.FEATHER)
                .requires(Items.BOOK)
                .unlockedBy(getHasName(DustItemRegistry.ANCIENT_INK.get()), has(DustItemRegistry.ANCIENT_INK.get()))
                .save(output);
        // Shared Research Guide: your Research Notes (given back, see ResearchBookItem) + a Book and Quill + Ancient Ink.
        // The copy of your research and your name are added as it's taken out (ResearchObserver.onCrafted).
        shapeless(RecipeCategory.MISC, DustItemRegistry.SHARED_RESEARCH_GUIDE.get())
                .requires(DustItemRegistry.RESEARCH_NOTES.get())
                .requires(Items.WRITABLE_BOOK)
                .requires(DustItemRegistry.ANCIENT_INK.get())
                .unlockedBy(getHasName(DustItemRegistry.RESEARCH_NOTES.get()), has(DustItemRegistry.RESEARCH_NOTES.get()))
                .save(output);

        // ---- survival recipes ----
        survivalTableRecipes();
        elementCrafterRecipes();

        // Parchment, for rubbing glyph walls: paper and a bit of charcoal
        shapeless(RecipeCategory.MISC, DustItemRegistry.PARCHMENT.get(), 3)
                .requires(Items.PAPER).requires(Items.PAPER).requires(Items.PAPER)
                .requires(Items.CHARCOAL)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(output);
        // Ancient Ink + a written book / named item -> written in the ancient script (code-driven "special" recipe)
        SpecialRecipeBuilder.special(() -> AncientInscribeRecipe.INSTANCE)
                .save(output, DustReferences.MODID + ":ancient_inscribe");
        // A Spell Scroll + a gold ingot -> locked; a written Spell Scroll alone -> a blank one (code-driven "special" recipe)
        SpecialRecipeBuilder.special(() -> com.mowmaster.dust.Features.Spells.SpellScrollRecipe.INSTANCE)
                .save(output, DustReferences.MODID + ":spell_scroll_crafting");

    }

    // The crafting table recipes for the dust machines (the Element Tester starts it all; the research tables are the
    // Tester plus what each one works with)
    private void survivalTableRecipes() {
        Item alchemicalDust = DustItemRegistry.ALCHEMICAL_DUST.get();
        Item tester = DustBlockRegistry.ELEMENT_TESTER.asItem();
        Item soot = DustItemRegistry.ALCHEMICAL_SOOT.get();


        //Needed for Crushing blocks before analysing them
        shaped(RecipeCategory.MISC, DustBlockRegistry.RESEARCH_CRUSHER.get())
                .pattern("SSS")
                .pattern("SIS")
                .pattern("SSS")
                .define('S', Items.STONE)
                .define('I', Items.IRON_BLOCK)
                .unlockedBy(getHasName(Items.IRON_BLOCK), has(Items.IRON_BLOCK))
                .save(output);
        shaped(RecipeCategory.MISC, DustBlockRegistry.ELEMENT_TESTER.get())
                .pattern("CCC").pattern("SSS").pattern("S S")
                .define('C', DustTags.Items.CRYSTALS_OR_AMETHYST)
                .define('S', Items.STONE)
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                .save(output);


        shapeless(RecipeCategory.MISC, DustBlockRegistry.RESEARCH_SEPARATION.get())
                .requires(tester).requires(Items.PAPER)
                .unlockedBy(getHasName(tester), has(tester)).save(output);
        shapeless(RecipeCategory.MISC, DustBlockRegistry.RESEARCH_EVAPORATION.get())
                .requires(tester).requires(Items.PAPER).requires(Items.BUCKET)
                .unlockedBy(getHasName(tester), has(tester)).save(output);
        shapeless(RecipeCategory.MISC, DustBlockRegistry.RESEARCH_DISTILLATION.get())
                .requires(tester).requires(Items.PAPER).requires(Items.GLASS_BOTTLE)
                .unlockedBy(getHasName(tester), has(tester)).save(output);

        // Alchemical Soot gates it till after some research has happened
        shaped(RecipeCategory.MISC, DustBlockRegistry.DUST_JAR.get())
                .pattern("SSS")
                .pattern("GDG")
                .pattern("GGG")
                .define('S', net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier.withDefaultNamespace("slabs")))
                .define('G', Items.GLASS)
                .define('D', soot)
                .unlockedBy(getHasName(soot), has(soot))
                .save(output);
        shaped(RecipeCategory.MISC, DustBlockRegistry.ELEMENT_CRAFTER.get())
                .pattern("SJS")
                .pattern("JCJ")
                .pattern("SJS")
                .define('S', soot).define('J', DustBlockRegistry.DUST_JAR.asItem()).define('C', Items.CRAFTING_TABLE)
                .unlockedBy(getHasName(DustBlockRegistry.DUST_JAR.asItem()), has(DustBlockRegistry.DUST_JAR.asItem()))
                .save(output);


        // The Transformation, Combination and Mixing Tables are dust recipes that need their Research tab discovery,
        // which crafting the Element Crafter unlocks (Features/Research/ResearchProgress)
        Item copper = Items.COPPER_BLOCK.weathering().unaffected();
        gatedTableRecipe(DustBlockRegistry.RESEARCH_TRANSFORMATION.asItem(), "research_transformation", tester, copper, Items.PAPER);
        gatedTableRecipe(DustBlockRegistry.RESEARCH_COMBINATION.asItem(), "research_combination", tester, copper, Items.COAL_BLOCK, Items.PAPER);
        gatedTableRecipe(DustBlockRegistry.RESEARCH_MIXING.asItem(), "research_mixing", tester, copper, Items.BUCKET, Items.PAPER);

        //Jar Labels
        elementShapeless(DustItemRegistry.JAR_LABEL.get(), 1, "jar_label", Optional.empty(),
                List.of(Ingredient.of(Items.PAPER)),
                cost(DustElements.SLIME, 10));

        //Magical Flint and Steel
        elementShaped(DustItemRegistry.MAGICAL_FLINT_AND_STEEL.get(), "magical_flint_and_steel", Optional.empty(),
                java.util.Map.of('F', Ingredient.of(Items.FLINT), 'S', Ingredient.of(Items.IRON_INGOT)),
                List.of(" F", "S "), cost(DustElements.MAGIC, 10));
    }

    // The Research tab discovery a recipe needs: dust:unlock_<name> (ResearchProgress)
    private static Optional<DustRecipe.ResearchKey> unlock(String name) {
        return Optional.of(new DustRecipe.ResearchKey(
                net.minecraft.resources.Identifier.fromNamespaceAndPath(DustReferences.MODID, "research"),
                net.minecraft.resources.Identifier.fromNamespaceAndPath(DustReferences.MODID, "unlock_" + name)));
    }

    // A shapeless crafting table recipe only players with its discovery can craft (a dust recipe with no dust)
    private void gatedTableRecipe(Item result, String name, Item... ingredients) {
        List<Ingredient> list = java.util.Arrays.stream(ingredients).map(Ingredient::of).toList();
        SpecialRecipeBuilder.special(() -> new DustRecipe(new ItemStackTemplate(result), List.of(), list, unlock(name)))
                .save(output, DustReferences.MODID + ":" + name);
    }

    // Element Crafter recipes (dust:element_crafting): shaped, with dust from the crafter's jars. The Dust Grinder and
    // the pylons need their Research tab discoveries (ResearchProgress); the Neutral Pylon and Spell Caster don't.
    private void elementCrafterRecipes() {
        Item soot = DustItemRegistry.ALCHEMICAL_SOOT.get();
        Ingredient stone = Ingredient.of(Items.STONE);

        elementShaped(DustBlockRegistry.DUST_GRINDER.asItem(), "dust_grinder", unlock("dust_grinder"),
                java.util.Map.of('S', stone, 'A', Ingredient.of(DustItemRegistry.ALCHEMICAL_DUST.get())),
                List.of("SSS", "AAA", "SSS"), cost(DustElements.FRACTURE, 10));
        // The lamps: a lantern with a crystal of what they burn on top, and some of that dust from the jars
        elementShaped(DustBlockRegistry.GROWTH_LAMP.asItem(), "growth_lamp", Optional.empty(),
                java.util.Map.of('C', Ingredient.of(DustItemRegistry.CRYSTALS.get(DustElements.GROWTH).get()), 'L', Ingredient.of(Items.LANTERN)),
                List.of("C", "L"), cost(DustElements.GROWTH, 20));
        elementShaped(DustBlockRegistry.LOVELY_LAMP.asItem(), "lovely_lamp", Optional.empty(),
                java.util.Map.of('C', Ingredient.of(DustItemRegistry.CRYSTALS.get(DustElements.SEEDS).get()), 'L', Ingredient.of(Items.LANTERN)),
                List.of("C", "L"), cost(DustElements.SEEDS, 20));
        // The pylons: one pattern, a different element for each
        java.util.Map<Character, Ingredient> pylonKey = java.util.Map.of('S', stone, 'A', Ingredient.of(soot));
        List<String> pylon = List.of("SAS", " S ", "SSS");
        elementShaped(DustBlockRegistry.SPELL_PYLON.asItem(), "spell_pylon", unlock("spell_pylon"), pylonKey, pylon,
                cost(DustElements.FIRE, 10), cost(DustElements.WATER, 10),
                cost(DustElements.EARTH, 10));
        elementShaped(DustBlockRegistry.SHAPE_PYLON.asItem(), "shape_pylon", unlock("shape_pylon"), pylonKey, pylon, cost(DustElements.CHANGE, 10));
        elementShaped(DustBlockRegistry.NEUTRAL_PYLON.asItem(), "neutral_pylon", Optional.empty(), pylonKey, pylon, cost(DustElements.ORDER, 10));
        elementShaped(DustBlockRegistry.DAMAGE_PYLON.asItem(), "damage_pylon", unlock("damage_pylon"), pylonKey, pylon, cost(DustElements.DEATH, 100));
        elementShaped(DustBlockRegistry.EXPERIENCED_PYLON.asItem(), "experienced_pylon", unlock("experienced_pylon"), pylonKey, pylon, cost(DustElements.KNOWLEDGE, 100));
        elementShaped(DustBlockRegistry.THRIFTY_PYLON.asItem(), "thrifty_pylon", unlock("thrifty_pylon"), pylonKey, pylon, cost(DustElements.TOOL, 100));
        elementShaped(DustBlockRegistry.GIANT_PYLON.asItem(), "giant_pylon", unlock("giant_pylon"), pylonKey, pylon, cost(DustElements.GROWTH, 100));
        elementShaped(DustBlockRegistry.SPELL_CASTER.asItem(), "spell_caster", Optional.empty(),
                java.util.Map.of('S', stone, 'A', Ingredient.of(soot), 'C', Ingredient.of(DustItemRegistry.CRYSTALS.get(DustElements.ENERGY).get())),
                List.of("SSS", "SAC", "SSS"), cost(DustElements.ENERGY, 100));
        // Spell Scroll: shapeless, paper and soot
        elementShapeless(DustItemRegistry.SPELL_SCROLL.get(), 1, "spell_scroll", Optional.empty(),
                List.of(Ingredient.of(Items.PAPER), Ingredient.of(soot)),
                cost(DustElements.TOOL, 10));
    }

    private static com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.ElementCost cost(DustElement element, int amount) {
        return new com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.ElementCost(element, amount);
    }

    private void elementShaped(Item result, String name, Optional<DustRecipe.ResearchKey> research,
                               java.util.Map<Character, Ingredient> key, List<String> rows,
                               com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.ElementCost... costs) {
        SpecialRecipeBuilder.special(() -> com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.shaped(new ItemStackTemplate(result), key, rows, List.of(costs), research))
                .save(output, DustReferences.MODID + ":element_crafting/" + name);
    }


    // A shapeless Element Crafter recipe: one grid item for each ingredient, in any slots, plus dust from the jars.
    // Each Ingredient.of(...) in the list is one item in the grid; Ingredient.of(A, B) is one item that can be A or B.
    private void elementShapeless(Item result, int count, String name, Optional<DustRecipe.ResearchKey> research,
                                  List<Ingredient> ingredients,
                                  com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.ElementCost... costs) {
        SpecialRecipeBuilder.special(() -> new com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe(
                        new ItemStackTemplate(result, count), List.of(), ingredients, List.of(costs), research))
                .save(output, DustReferences.MODID + ":element_crafting/" + name);
    }



    // 26.3: brewing is recipe data now (RegisterBrewingRecipesEvent is gone). A brewing recipe names the exact
    // container, so the mix needs one recipe per bottle type, and the splash/lingering conversions need their own
    // recipes for every potion (vanilla only makes them for its own potions).
    private void buildBrewingRecipes() {
        // Fire Resistance + Blaze Powder -> Scorch, in all three bottles
        for (Item container : List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION)) {
            BrewingRecipeBuilder.brewingMix(container, Potions.FIRE_RESISTANCE, Items.BLAZE_POWDER, DustPotionRegistry.POTION_SCORCH)
                    .save(output, DustReferences.MODID + ":brewing/" + getItemName(container) + "_potion_scorch");
        }
        // Scorch potion -> splash (gunpowder) -> lingering (dragon's breath)
        BrewingRecipeBuilder.brewingContainerTransform(Items.POTION, DustPotionRegistry.POTION_SCORCH, Items.GUNPOWDER, Items.SPLASH_POTION)
                .save(output, DustReferences.MODID + ":brewing/potion_scorch_to_splash");
        BrewingRecipeBuilder.brewingContainerTransform(Items.SPLASH_POTION, DustPotionRegistry.POTION_SCORCH, Items.DRAGON_BREATH, Items.LINGERING_POTION)
                .save(output, DustReferences.MODID + ":brewing/potion_scorch_to_lingering");
    }

    @Override
    protected <T extends AbstractCookingRecipe> void oreCooking(AbstractCookingRecipe.Factory<T> factory, List<ItemLike> smeltables, RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, float experience, int cookingTime, String group, String fromDesc) {
        for (ItemLike item : smeltables) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(item), craftingCategory, cookingCategory, result, experience, cookingTime, factory)
                    .group(group)
                    .unlockedBy(getHasName(item), this.has(item))
                    .save(this.output, DustReferences.MODID + ":" + getItemName(result) + fromDesc + "_" + getItemName(item));
        }
    }

}
