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
        crystalRecipes();
        woodRecipes();
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

    // Element crystals: four of an element's crystals make a block of it (as amethyst shards do)
    private void crystalRecipes() {
        for (DustElement element : DustElements.all()) {
            Item crystal = DustItemRegistry.CRYSTALS.get(element).get();
            shaped(RecipeCategory.BUILDING_BLOCKS, com.mowmaster.dust.Features.Crystals.ElementCrystals.BLOCK_ITEMS.get(element).get())
                    .pattern("CC").pattern("CC")
                    .define('C', crystal)
                    .unlockedBy(getHasName(crystal), has(crystal))
                    .save(output);
        }
    }

    // The elemental woods (Features/Trees), as vanilla's: 4 planks from any of the wood's logs, 3 wood (bark) from 4 logs,
    // the planks family (stairs, slab, fence, gate, button, pressure plate: DustWoodFamilies), 6 vertical slabs from 3
    // planks stood in a column, a slab turned on its side and back again, and a chest from 8 planks round one of the
    // wood's logs (an empty middle is vanilla's chest, which any planks make). Each wood's logs smelt into its charcoal.
    private void woodRecipes() {
        for (com.mowmaster.dust.Features.Trees.WoodSet set : com.mowmaster.dust.Features.Trees.ElementalWoods.all()) {
            com.mowmaster.dust.Features.Trees.ElementalWood wood = set.type();
            net.minecraft.tags.TagKey<Item> logs = DustTags.Items.logs(wood);
            Item planks = set.planks().asItem();
            planksFromLogs(planks, logs, 4);
            woodFromLogs(set.wood().asItem(), set.log().asItem());
            woodFromLogs(set.strippedWood().asItem(), set.strippedLog().asItem());
            generateRecipes(DustWoodFamilies.of(set), net.minecraft.world.flag.FeatureFlags.REGISTRY.allFlags());
            Item verticalSlab = set.verticalSlab().asItem(), slab = set.slab().asItem();
            shaped(RecipeCategory.BUILDING_BLOCKS, verticalSlab, 6)
                    .pattern("P").pattern("P").pattern("P")
                    .define('P', planks)
                    .group("wooden_vertical_slab")
                    .unlockedBy(getHasName(planks), has(planks))
                    .save(output);
            shapeless(RecipeCategory.BUILDING_BLOCKS, verticalSlab).requires(slab)
                    .unlockedBy(getHasName(slab), has(slab))
                    .save(output, DustReferences.MODID + ":" + getItemName(verticalSlab) + "_from_slab");
            shapeless(RecipeCategory.BUILDING_BLOCKS, slab).requires(verticalSlab)
                    .unlockedBy(getHasName(verticalSlab), has(verticalSlab))
                    .save(output, DustReferences.MODID + ":" + getItemName(slab) + "_from_vertical_slab");
            shaped(RecipeCategory.DECORATIONS, set.chest().asItem())
                    .pattern("PPP").pattern("PLP").pattern("PPP")
                    .define('P', planks)
                    .define('L', logs)
                    .unlockedBy(getHasName(planks), has(planks))
                    .save(output);
            Item charcoal = com.mowmaster.dust.Features.Trees.ElementalWoods.CHARCOAL.get(wood.charcoal).get();
            SimpleCookingRecipeBuilder.smelting(tag(logs), RecipeCategory.MISC, CookingBookCategory.MISC, charcoal, 0.15F, 200)
                    .group(getItemName(charcoal))
                    .unlockedBy("has_" + wood.id + "_logs", has(logs))
                    .save(output, DustReferences.MODID + ":" + getItemName(charcoal) + "_from_" + wood.id);
        }
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
        // Glass Dust Tubes: copper and glass round Alchemical Soot, eight at a time
        shaped(RecipeCategory.MISC, DustBlockRegistry.DUST_TUBE.get(), 8)
                .pattern("CGC")
                .pattern("GSG")
                .pattern("CGC")
                .define('C', Items.COPPER_INGOT).define('G', Items.GLASS).define('S', soot)
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
        // The Element Replicator: soot round a Knowledge crystal, two papers for its sigils, two Dust Jars for the dust
        // it draws on
        elementShaped(DustBlockRegistry.ELEMENT_REPLICATOR.asItem(), "element_replicator", Optional.empty(),
                java.util.Map.of('A', Ingredient.of(soot), 'P', Ingredient.of(Items.PAPER), 'J', Ingredient.of(DustBlockRegistry.DUST_JAR.asItem()),
                        'K', Ingredient.of(DustItemRegistry.CRYSTALS.get(DustElements.KNOWLEDGE).get())),
                List.of("APA", "JKJ", "APA"), cost(DustElements.KNOWLEDGE, 50), cost(DustElements.CHANGE, 50));
        // The alchemical apparatus: each is its research station built into copper, iron and a blackstone plinth (so the
        // station has to be made, and researched, first), with some dust from the jars
        Ingredient copperIngot = Ingredient.of(Items.COPPER_INGOT), iron = Ingredient.of(Items.IRON_INGOT), blackstone = Ingredient.of(Items.POLISHED_BLACKSTONE);
        Ingredient glass = Ingredient.of(Items.GLASS), furnace = Ingredient.of(Items.FURNACE), cauldron = Ingredient.of(Items.CAULDRON);
        elementShaped(DustBlockRegistry.CRUSHING_MILL.asItem(), "crushing_mill", Optional.empty(),
                java.util.Map.of('C', copperIngot, 'H', Ingredient.of(Items.HOPPER), 'I', iron, 'R', Ingredient.of(DustBlockRegistry.RESEARCH_CRUSHER.asItem()), 'B', blackstone),
                List.of("CHC", "IRI", "BBB"), cost(DustElements.EARTH, 50));
        elementShaped(DustBlockRegistry.ALCHEMICAL_SEPARATOR.asItem(), "alchemical_separator", Optional.empty(),
                java.util.Map.of('C', copperIngot, 'G', glass, 'I', iron, 'R', Ingredient.of(DustBlockRegistry.RESEARCH_SEPARATION.asItem()), 'B', blackstone),
                List.of("CGC", "IRI", "BBB"), cost(DustElements.ORDER, 50));
        elementShaped(DustBlockRegistry.EVAPORATING_KETTLE.asItem(), "evaporating_kettle", Optional.empty(),
                java.util.Map.of('C', copperIngot, 'K', cauldron, 'I', iron, 'R', Ingredient.of(DustBlockRegistry.RESEARCH_EVAPORATION.asItem()), 'B', blackstone, 'F', furnace),
                List.of("CKC", "IRI", "BFB"), cost(DustElements.FIRE, 30), cost(DustElements.WATER, 30));
        // The Still (it replaced the Alembic): the pot is the Distillation Table built into copper; a firebox of bricks
        // round a furnace; towers of copper and glass; a copper cap
        elementShaped(DustBlockRegistry.STILL_FIREBOX.asItem(), "still_firebox", Optional.empty(),
                java.util.Map.of('B', Ingredient.of(Items.BRICKS), 'F', furnace, 'C', Ingredient.of(Items.CHARCOAL)),
                List.of("BBB", "BFB", "BCB"), cost(DustElements.FIRE, 20));
        elementShaped(DustBlockRegistry.STILL_POT.asItem(), "still_pot", Optional.empty(),
                java.util.Map.of('C', copperIngot, 'K', cauldron, 'R', Ingredient.of(DustBlockRegistry.RESEARCH_DISTILLATION.asItem())),
                List.of("CRC", "CKC", "CCC"), cost(DustElements.WATER, 30), cost(DustElements.FIRE, 20));
        elementShaped(DustBlockRegistry.STILL_TOWER.asItem(), "still_tower", Optional.empty(),
                java.util.Map.of('C', copperIngot, 'G', glass),
                List.of("C C", "GCG", "C C"), cost(DustElements.WATER, 10));
        elementShaped(DustBlockRegistry.STILL_TOP.asItem(), "still_top", Optional.empty(),
                java.util.Map.of('C', copperIngot),
                List.of(" C ", "C C", "CCC"), cost(DustElements.AIR, 10));
        // The Dust Chute: a stone back and a brass trough; the Dust Collector: a stone box under an iron grate
        elementShaped(DustBlockRegistry.DUST_CHUTE.asItem(), "dust_chute", Optional.empty(),
                java.util.Map.of('S', Ingredient.of(Items.SMOOTH_STONE), 'C', copperIngot),
                List.of("S  ", "SC ", "S C"), cost(DustElements.EARTH, 10));
        elementShaped(DustBlockRegistry.DUST_COLLECTOR.asItem(), "dust_collector", Optional.empty(),
                java.util.Map.of('S', Ingredient.of(Items.SMOOTH_STONE), 'I', Ingredient.of(Items.IRON_BARS), 'H', Ingredient.of(Items.HOPPER)),
                List.of("SIS", "SHS", "SSS"), cost(DustElements.EARTH, 20), cost(DustElements.AIR, 10));
        // The Dust Separator: a copper drum on a stone frame; an extension is more drum; the Dust Mixer: an iron bowl
        // between stone sides
        elementShaped(DustBlockRegistry.DUST_SEPARATOR.asItem(), "dust_seperator", Optional.empty(),
                java.util.Map.of('C', copperIngot, 'S', Ingredient.of(Items.SMOOTH_STONE), 'R', Ingredient.of(DustBlockRegistry.RESEARCH_SEPARATION.asItem())),
                List.of("SCC", "SRC", "SSS"), cost(DustElements.ORDER, 40));
        elementShaped(DustBlockRegistry.DUST_SEPARATOR_EXTENSION.asItem(), "dust_seperator_extension", Optional.empty(),
                java.util.Map.of('C', copperIngot, 'S', Ingredient.of(Items.SMOOTH_STONE)),
                List.of("CCC", "C C", "SSS"), cost(DustElements.ORDER, 10));
        elementShaped(DustBlockRegistry.DUST_MIXER.asItem(), "dust_mixer", Optional.empty(),
                java.util.Map.of('I', iron, 'S', Ingredient.of(Items.SMOOTH_STONE), 'B', Ingredient.of(Items.IRON_BARS), 'R', Ingredient.of(DustBlockRegistry.RESEARCH_COMBINATION.asItem())),
                List.of("SBS", "IRI", "SIS"), cost(DustElements.FIRE, 30), cost(DustElements.EARTH, 30));
        // The Wet Dust Mixer: a metal pot under a stone stand; the Dust Cyclone and its hopper: glass barrels in stone
        // straps; the Dust Centrifuge: glass vials on a brass spinner over a stone base
        elementShaped(DustBlockRegistry.DUST_MIXER_WET.asItem(), "dust_mixer_wet", Optional.empty(),
                java.util.Map.of('S', Ingredient.of(Items.SMOOTH_STONE), 'I', iron, 'C', cauldron, 'R', Ingredient.of(DustBlockRegistry.RESEARCH_MIXING.asItem())),
                List.of("SSS", "IRS", "CIS"), cost(DustElements.WATER, 30), cost(DustElements.CHAOS, 30));
        elementShaped(DustBlockRegistry.DUST_CYCLONE.asItem(), "dust_cyclone", Optional.empty(),
                java.util.Map.of('S', Ingredient.of(Items.SMOOTH_STONE), 'G', glass, 'R', Ingredient.of(DustBlockRegistry.RESEARCH_SEPARATION.asItem())),
                List.of("SGS", "GRG", "SGS"), cost(DustElements.ORDER, 30), cost(DustElements.EARTH, 20));
        elementShaped(DustBlockRegistry.DUST_CYCLONE_HOPPER.asItem(), "dust_cyclone_hopper", Optional.empty(),
                java.util.Map.of('S', Ingredient.of(Items.SMOOTH_STONE), 'G', glass),
                List.of("SGS", "G G", " G "), cost(DustElements.EARTH, 10));
        elementShaped(DustBlockRegistry.DUST_CENTRIFUGE.asItem(), "dust_centrifuge", Optional.empty(),
                java.util.Map.of('S', Ingredient.of(Items.SMOOTH_STONE), 'G', Ingredient.of(Items.GLASS_BOTTLE), 'C', copperIngot, 'R', Ingredient.of(DustBlockRegistry.RESEARCH_SEPARATION.asItem())),
                List.of("GCG", "CRC", "SSS"), cost(DustElements.WATER, 30), cost(DustElements.CHAOS, 20));
        // The Crystal Display: a gold-trimmed stand of polished stone round an amethyst block, with Magic and Order dust
        elementShaped(DustBlockRegistry.CRYSTAL_DISPLAY.asItem(), "crystal_display", Optional.empty(),
                java.util.Map.of('G', Ingredient.of(Items.GOLD_INGOT), 'A', Ingredient.of(Items.AMETHYST_BLOCK), 'S', Ingredient.of(Items.POLISHED_ANDESITE),
                        'B', Ingredient.of(Items.SMOOTH_STONE_SLAB)),
                List.of("GAG", " S ", "BSB"), cost(DustElements.MAGIC, 20), cost(DustElements.ORDER, 20));
        elementShaped(DustBlockRegistry.TRANSMUTER.asItem(), "transmuter", Optional.empty(),
                java.util.Map.of('G', glass, 'A', Ingredient.of(Items.AMETHYST_SHARD), 'I', iron, 'R', Ingredient.of(DustBlockRegistry.RESEARCH_TRANSFORMATION.asItem()), 'B', blackstone),
                List.of("GAG", "IRI", "BBB"), cost(DustElements.ORDER, 30), cost(DustElements.CHAOS, 30));
        elementShaped(DustBlockRegistry.CONJOINING_CRUCIBLE.asItem(), "conjoining_crucible", Optional.empty(),
                java.util.Map.of('I', iron, 'K', cauldron, 'R', Ingredient.of(DustBlockRegistry.RESEARCH_COMBINATION.asItem()), 'B', blackstone, 'F', furnace),
                List.of("IKI", "IRI", "BFB"), cost(DustElements.FIRE, 50));
        elementShaped(DustBlockRegistry.MIXING_VAT.asItem(), "mixing_vat", Optional.empty(),
                java.util.Map.of('C', copperIngot, 'V', Ingredient.of(Items.BARREL), 'I', iron, 'R', Ingredient.of(DustBlockRegistry.RESEARCH_MIXING.asItem()), 'B', blackstone),
                List.of("CVC", "IRI", "BBB"), cost(DustElements.WATER, 50));
        // The Dissolving Retort breaks elements apart, as the Separation Table breaks compounds down
        elementShaped(DustBlockRegistry.DISSOLVING_RETORT.asItem(), "dissolving_retort", Optional.empty(),
                java.util.Map.of('C', copperIngot, 'G', glass, 'I', iron, 'R', Ingredient.of(DustBlockRegistry.RESEARCH_SEPARATION.asItem()), 'B', blackstone, 'F', furnace),
                List.of("CGC", "IRI", "BFB"), cost(DustElements.CHAOS, 50));
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
        // The elemental flowers: a vanilla flower infused with 10 of the element's dust, once the flower has been researched
        // (seen with Research Notes: its Blocks tab entry)
        for (com.mowmaster.dust.Features.Flora.ElementalFlower flower : com.mowmaster.dust.Features.Flora.ElementalFlower.values()) {
            net.minecraft.world.level.block.Block block = com.mowmaster.dust.Features.Flora.ElementalFlowers.FLOWERS.get(flower).get();
            elementShapeless(block.asItem(), 1, flower.id, Optional.of(new DustRecipe.ResearchKey(
                            net.minecraft.resources.Identifier.fromNamespaceAndPath(DustReferences.MODID, "blocks"),
                            net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block))),
                    List.of(Ingredient.of(flower.source.get())), cost(flower.element(), 10));
        }
        // The elemental saplings: the vanilla sapling of the same shape of tree infused with 20 of the element's dust, once
        // the sapling has been researched (its Blocks tab entry), as for the flowers
        for (com.mowmaster.dust.Features.Trees.WoodSet set : com.mowmaster.dust.Features.Trees.ElementalWoods.all()) {
            net.minecraft.world.level.block.Block sapling = set.sapling().get();
            Item source = switch (set.type().shape) {
                case ACACIA -> Items.ACACIA_SAPLING;
                case TALL -> Items.BIRCH_SAPLING;
                case SPRUCE, PINE -> Items.SPRUCE_SAPLING;
                case AZALEA -> Items.AZALEA;
                default -> Items.OAK_SAPLING;
            };
            elementShapeless(sapling.asItem(), 1, set.type().id + "_sapling", Optional.of(new DustRecipe.ResearchKey(
                            net.minecraft.resources.Identifier.fromNamespaceAndPath(DustReferences.MODID, "blocks"),
                            net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(sapling))),
                    List.of(Ingredient.of(source)), cost(set.type().element(), 20));
        }
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
