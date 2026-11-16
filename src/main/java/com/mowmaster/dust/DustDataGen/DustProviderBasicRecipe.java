package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustRegistries.DustPotionRegistry;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.AncientInscribeRecipe;
import com.mowmaster.dust.Features.DustComposition.DustRecipe;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import com.mowmaster.dust.Features.Research.ResearchUnlocks;
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
                .requires(DustItemRegistry.DUST_BLACK.get())
                .requires(DustItemRegistry.DUST_WHITE.get())
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

        // Grimoire: a book, leather and an amethyst shard (a placeholder recipe until it has a proper place in the tech)
        shapeless(RecipeCategory.TOOLS, DustItemRegistry.GRIMOIRE.get())
                .requires(Items.BOOK)
                .requires(Items.LEATHER)
                .requires(Items.AMETHYST_SHARD)
                .unlockedBy(getHasName(DustItemRegistry.SPELL_SCROLL.get()), has(DustItemRegistry.SPELL_SCROLL.get()))
                .save(output);

        // Element crafting (the Element Crafter), examples: grid items plus dust from the jars
        SpecialRecipeBuilder.special(() -> new com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe(
                        new ItemStackTemplate(Items.BLAZE_POWDER),
                        List.of(), List.of(Ingredient.of(Items.GUNPOWDER)),
                        List.of(new com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.ElementCost(com.mowmaster.dust.Features.DustComposition.DustElements.FIRE, 20)),
                        Optional.empty()))
                .save(output, DustReferences.MODID + ":element_crafting/blaze_powder");
        SpecialRecipeBuilder.special(() -> new com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe(
                        new ItemStackTemplate(Items.GLOWSTONE_DUST, 2),
                        List.of(), List.of(Ingredient.of(Items.REDSTONE)),
                        List.of(new com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.ElementCost(com.mowmaster.dust.Features.DustComposition.DustElements.LIGHT, 10),
                                new com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.ElementCost(com.mowmaster.dust.Features.DustComposition.DustElements.FIRE, 5)),
                        Optional.empty()))
                .save(output, DustReferences.MODID + ":element_crafting/glowstone_dust");

        // Parchment, for rubbing glyph walls: paper and a bit of charcoal
        shapeless(RecipeCategory.MISC, DustItemRegistry.PARCHMENT.get(), 3)
                .requires(Items.PAPER).requires(Items.PAPER).requires(Items.PAPER)
                .requires(Items.CHARCOAL)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(output);
        // Ancient Ink + a written book / named item -> written in the ancient script (code-driven "special" recipe)
        SpecialRecipeBuilder.special(() -> AncientInscribeRecipe.INSTANCE)
                .save(output, DustReferences.MODID + ":ancient_inscribe");

        List<ItemLike> DUST_SMELTABLES = List.of(DustItemRegistry.CRYSTAL_RED);

        oreSmelting(DUST_SMELTABLES,RecipeCategory.MISC, CookingBookCategory.BLOCKS, DustItemRegistry.DUST_RED.get(), 0.25f, 200, DustReferences.MODID);
        oreBlasting(DUST_SMELTABLES,RecipeCategory.MISC, CookingBookCategory.BLOCKS, DustItemRegistry.DUST_RED.get(), 0.25f, 100, DustReferences.MODID);


        shaped(RecipeCategory.BUILDING_BLOCKS, DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get())
                .pattern("CCC")
                .pattern("CCC")
                .pattern("CCC")
                .define('C', DustItemRegistry.CRYSTAL_RED.get())
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, DustItemRegistry.CRYSTAL_RED.get())
                .pattern("DDD")
                .pattern("DID")
                .pattern("DDD")
                .define('D', DustItemRegistry.DUST_RED.get())
                .define('I', DustItemRegistry.CRYSTAL_INERT.get())
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_INERT.get()), has(DustItemRegistry.CRYSTAL_INERT.get()))
                .save(output, DustReferences.MODID + ":" + "inertcrystal_dustcrafting_red");

        shapeless(RecipeCategory.MISC, DustItemRegistry.CRYSTAL_RED.get(),9)
                .requires(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get())
                .unlockedBy(getHasName(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get()), has(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_SWORD.get())
                .pattern("C")
                .pattern("C")
                .pattern("S")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('S', Items.STICK)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_AXE.get())
                .pattern("CC")
                .pattern("CS")
                .pattern(" S")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('S', Items.STICK)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_PICKAXE.get())
                .pattern("CCC")
                .pattern(" S ")
                .pattern(" S ")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('S', Items.STICK)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_SHOVEL.get())
                .pattern(" C ")
                .pattern(" S ")
                .pattern(" S ")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('S', Items.STICK)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_HOE.get())
                .pattern("CC")
                .pattern(" S")
                .pattern(" S")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('S', Items.STICK)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_SPEAR.get())
                .pattern("  C")
                .pattern(" C ")
                .pattern("S  ")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('S', Items.STICK)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shapeless(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_MATTOCK.get())
                .requires(DustItemRegistry.CRYSTAL_HOE.get())
                .requires(DustItemRegistry.CRYSTAL_AXE.get())
                .requires(DustItemRegistry.CRYSTAL_SHOVEL.get())
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_AXE.get()), has(DustItemRegistry.CRYSTAL_AXE.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_HAMMER.get())
                .pattern("BBB")
                .pattern(" S ")
                .pattern(" S ")
                .define('B', DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.asItem()).define('S', Items.STICK)
                .unlockedBy(getHasName(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get()), has(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_HELMET.get())
                .pattern("CCC")
                .pattern("CAC")
                .pattern("CCC")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('A', Items.LEATHER_HELMET)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_CHESTPLATE.get())
                .pattern("CCC")
                .pattern("CAC")
                .pattern("CCC")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('A', Items.LEATHER_CHESTPLATE)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_LEGGINGS.get())
                .pattern("CCC")
                .pattern("CAC")
                .pattern("CCC")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('A', Items.LEATHER_LEGGINGS)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_BOOTS.get())
                .pattern("CCC")
                .pattern("CAC")
                .pattern("CCC")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('A', Items.LEATHER_BOOTS)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_HORSE_ARMOR.get())
                .pattern("CCC")
                .pattern("CAC")
                .pattern("CCC")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('A', Items.LEATHER_HORSE_ARMOR)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
    }

    // The crafting table recipes for the dust machines (the Element Tester starts it all; the research tables are the
    // Tester plus what each one works with)
    private void survivalTableRecipes() {
        Item alchemicalDust = DustItemRegistry.ALCHEMICAL_DUST.get();
        Item tester = DustBlockRegistry.ELEMENT_TESTER.asItem();
        shaped(RecipeCategory.MISC, DustBlockRegistry.ELEMENT_TESTER.get())
                .pattern("CCC").pattern("SSS").pattern("S S")
                .define('C', DustTags.Items.CRYSTALS_OR_AMETHYST).define('S', Items.STONE)
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                .save(output);
        shaped(RecipeCategory.MISC, DustBlockRegistry.RESEARCH_CRUSHER.get())
                .pattern("SSS").pattern("SIS").pattern("SSS")
                .define('S', Items.STONE).define('I', Items.IRON_BLOCK)
                .unlockedBy(getHasName(Items.IRON_BLOCK), has(Items.IRON_BLOCK))
                .save(output);
        // Any Alchemical Dust: the item ignores its element here (a plain ingredient doesn't look at data components)
        shaped(RecipeCategory.MISC, DustBlockRegistry.DUST_JAR.get())
                .pattern("SSS").pattern("GDG").pattern("GGG")
                .define('S', net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier.withDefaultNamespace("slabs"))).define('G', Items.GLASS).define('D', alchemicalDust)
                .unlockedBy(getHasName(alchemicalDust), has(alchemicalDust))
                .save(output);
        shaped(RecipeCategory.MISC, DustBlockRegistry.ELEMENT_CRAFTER.get())
                .pattern("SJS").pattern("JCJ").pattern("SJS")
                .define('S', alchemicalDust).define('J', DustBlockRegistry.DUST_JAR.asItem()).define('C', Items.CRAFTING_TABLE)
                .unlockedBy(getHasName(DustBlockRegistry.DUST_JAR.asItem()), has(DustBlockRegistry.DUST_JAR.asItem()))
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
        // The Transformation, Combination and Mixing Tables are dust recipes that need their Research tab discovery,
        // which crafting the Element Crafter unlocks (Features/Research/ResearchProgress)
        Item copper = Items.COPPER_BLOCK.weathering().unaffected();
        gatedTableRecipe(DustBlockRegistry.RESEARCH_TRANSFORMATION.asItem(), "research_transformation", tester, copper, Items.PAPER);
        gatedTableRecipe(DustBlockRegistry.RESEARCH_COMBINATION.asItem(), "research_combination", tester, copper, Items.COAL_BLOCK, Items.PAPER);
        gatedTableRecipe(DustBlockRegistry.RESEARCH_MIXING.asItem(), "research_mixing", tester, copper, Items.BUCKET, Items.PAPER);

        // Magical Flint and Steel: a flint and steel ringed with Alchemical Dust (any elements)
        shaped(RecipeCategory.TOOLS, DustItemRegistry.MAGICAL_FLINT_AND_STEEL.get())
                .pattern("DDD").pattern("DFD").pattern("DDD")
                .define('D', alchemicalDust).define('F', Items.FLINT_AND_STEEL)
                .unlockedBy(getHasName(alchemicalDust), has(alchemicalDust))
                .save(output);
        // Jar Label: paper and something sticky, a slime ball or Slime Alchemical Dust. CompoundIngredient: either one;
        // DataComponentIngredient: Alchemical Dust only when its element (the dust:alchemical_element component) is slime.
        shapeless(RecipeCategory.MISC, DustItemRegistry.JAR_LABEL.get())
                .requires(Items.PAPER)
                .requires(net.neoforged.neoforge.common.crafting.CompoundIngredient.of(Ingredient.of(Items.SLIME_BALL),
                        net.neoforged.neoforge.common.crafting.DataComponentIngredient.of(false, com.mowmaster.dust.DustRegistries.DustComponentDataRegistry.ALCHEMICAL_ELEMENT.get(),
                                com.mowmaster.dust.Features.DustComposition.DustElements.SLIME, alchemicalDust)))
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(output);
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
        elementShaped(DustBlockRegistry.DUST_GRINDER.asItem(), "dust_grinder", unlock("dust_grinder"), java.util.Map.of('S', stone, 'A', Ingredient.of(DustItemRegistry.ALCHEMICAL_DUST.get())),
                List.of("SSS", "AAA", "SSS"), cost(com.mowmaster.dust.Features.DustComposition.DustElements.FRACTURE, 10));
        // The pylons: one pattern, a different element for each
        java.util.Map<Character, Ingredient> pylonKey = java.util.Map.of('S', stone, 'A', Ingredient.of(soot));
        List<String> pylon = List.of("SAS", " S ", "SSS");
        elementShaped(DustBlockRegistry.SPELL_PYLON.asItem(), "spell_pylon", unlock("spell_pylon"), pylonKey, pylon,
                cost(com.mowmaster.dust.Features.DustComposition.DustElements.FIRE, 10), cost(com.mowmaster.dust.Features.DustComposition.DustElements.WATER, 10),
                cost(com.mowmaster.dust.Features.DustComposition.DustElements.EARTH, 10));
        elementShaped(DustBlockRegistry.SHAPE_PYLON.asItem(), "shape_pylon", unlock("shape_pylon"), pylonKey, pylon, cost(com.mowmaster.dust.Features.DustComposition.DustElements.CHANGE, 10));
        elementShaped(DustBlockRegistry.NEUTRAL_PYLON.asItem(), "neutral_pylon", Optional.empty(), pylonKey, pylon, cost(com.mowmaster.dust.Features.DustComposition.DustElements.ORDER, 10));
        elementShaped(DustBlockRegistry.DAMAGE_PYLON.asItem(), "damage_pylon", unlock("damage_pylon"), pylonKey, pylon, cost(com.mowmaster.dust.Features.DustComposition.DustElements.DEATH, 100));
        elementShaped(DustBlockRegistry.EXPERIENCED_PYLON.asItem(), "experienced_pylon", unlock("experienced_pylon"), pylonKey, pylon, cost(com.mowmaster.dust.Features.DustComposition.DustElements.KNOWLEDGE, 100));
        elementShaped(DustBlockRegistry.THRIFTY_PYLON.asItem(), "thrifty_pylon", unlock("thrifty_pylon"), pylonKey, pylon, cost(com.mowmaster.dust.Features.DustComposition.DustElements.TOOL, 100));
        elementShaped(DustBlockRegistry.GIANT_PYLON.asItem(), "giant_pylon", unlock("giant_pylon"), pylonKey, pylon, cost(com.mowmaster.dust.Features.DustComposition.DustElements.GROWTH, 100));
        elementShaped(DustBlockRegistry.SPELL_CASTER.asItem(), "spell_caster", Optional.empty(),
                java.util.Map.of('S', stone, 'A', Ingredient.of(soot), 'C', Ingredient.of(DustItemRegistry.CRYSTALS.get(com.mowmaster.dust.Features.DustComposition.DustElements.ENERGY).get())),
                List.of("SSS", "SAC", "SSS"), cost(com.mowmaster.dust.Features.DustComposition.DustElements.ENERGY, 100));
        // Spell Scroll: shapeless, paper and soot
        SpecialRecipeBuilder.special(() -> new com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe(
                        new ItemStackTemplate(DustItemRegistry.SPELL_SCROLL.get()), List.of(), List.of(Ingredient.of(Items.PAPER), Ingredient.of(soot)),
                        List.of(cost(com.mowmaster.dust.Features.DustComposition.DustElements.TOOL, 10)), Optional.empty()))
                .save(output, DustReferences.MODID + ":element_crafting/spell_scroll");
    }

    private static com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.ElementCost cost(com.mowmaster.dust.Features.DustComposition.DustElement element, int amount) {
        return new com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.ElementCost(element, amount);
    }

    private void elementShaped(Item result, String name, Optional<DustRecipe.ResearchKey> research,
                               java.util.Map<Character, Ingredient> key, List<String> rows,
                               com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.ElementCost... costs) {
        SpecialRecipeBuilder.special(() -> com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.shaped(new ItemStackTemplate(result), key, rows, List.of(costs), research))
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
