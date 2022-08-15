package com.mowmaster.dust.Compat.JEI;

import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.mowmaster.dust.References.Constants.MODID;

@JeiPlugin
public class DustPlugin implements IModPlugin {

    public static final ResourceLocation PLUGIN_UID = new ResourceLocation(MODID, "plugin/main");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    private static void addInfoPage(IRecipeRegistration reg, Collection<Item> items, String name) {
        if (items.isEmpty()) return;
        String key = getDescKey(new ResourceLocation(MODID, name));
        List<ItemStack> stacks = items.stream().map(ItemStack::new).collect(Collectors.toList());
        reg.addIngredientInfo(stacks, VanillaTypes.ITEM, new TranslatableComponent(key));
    }

    public static void addValueInfoPage(IRecipeRegistration reg, Item item, String name, Object... values) {
        Collection<Item> items = Collections.singletonList(item);
        addValueInfoPage(reg, items, name, values);
    }

    private static void addValueInfoPage(IRecipeRegistration reg, Collection<Item> items, String name, Object... values) {
        if (items.isEmpty()) return;
        String key = getDescKey(new ResourceLocation(MODID, name));
        List<ItemStack> stacks = items.stream().map(ItemStack::new).collect(Collectors.toList());
        reg.addIngredientInfo(stacks, VanillaTypes.ITEM, new TranslatableComponent(I18n.get(key, values)));
    }

    private static String getDescKey(ResourceLocation name) {
        return  name.getPath();
    }

    private static Item getItemFromIngredient(Ingredient ingredient) {
        return ingredient.getItems()[0].getItem();
    }

    /*
        OLD PEDESTALS CODE, MAY WORK, MAY NOT
    public static final IRecipeType<SmeltingRecipeAdvanced> SMELTING_ADVANCED_TYPE = SmeltingRecipeAdvanced.recipeType;
    public static final IRecipeType<CrusherRecipeAdvanced> CRUSHER_ADVANCED_TYPE = CrusherRecipeAdvanced.recipeType;
    public static final IRecipeType<SawMillRecipeAdvanced> SAWING_ADVANCED_TYPE = SawMillRecipeAdvanced.recipeType;
    public static final IRecipeType<CrusherRecipe> CRUSHER_TYPE = CrusherRecipe.recipeType;
    public static final IRecipeType<SawMillRecipe> SAWING_TYPE = SawMillRecipe.recipeType;
    public static final IRecipeType<CobbleGenRecipe> COBBLEGEN_TYPE = CobbleGenRecipe.recipeType;
    public static final IRecipeType<CobbleGenSilkRecipe> COBBLEGENSILK_TYPE = CobbleGenSilkRecipe.recipeType;
    public static final IRecipeType<ColoredPedestalRecipe> COLORING_TYPE = ColoredPedestalRecipe.recipeType;
    public static final IRecipeType<ColoredPedestalRecipe> COLORINGP_TYPE = ColoredPedestalRecipe.recipeType;
    public static final IRecipeType<FluidtoExpConverterRecipe> FLUIDTOXP_TYPE = FluidtoExpConverterRecipe.recipeType;*/

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        /*
        OLD PEDESTALS CODE, MAY WORK, MAY NOT
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(SMELTING_ADVANCED_TYPE), SmeltingAdvancedRecipeCategory.UID);
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(CRUSHER_ADVANCED_TYPE), CrusherAdvancedRecipeCategory.UID);
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(SAWING_ADVANCED_TYPE), SawmillAdvancedRecipeCategory.UID);
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(CRUSHER_TYPE), CrusherRecipeCategory.UID);
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(SAWING_TYPE), SawMillRecipeCategory.UID);
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(COBBLEGEN_TYPE), CobbleGenRecipeCategory.UID);
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(COBBLEGENSILK_TYPE), CobbleGenSilkRecipeCategory.UID);
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(COLORING_TYPE), ColorPedestalRecipeCategory.UID);
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(COLORINGP_TYPE), ColorPalletRecipeCategory.UID);
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(FLUIDTOXP_TYPE), FluidtoExpConverterRecipeCategory.UID);
*/
        addValueInfoPage(registration,DeferredRegisterItems.TOOL_LINKINGTOOL.get(),DeferredRegisterItems.TOOL_LINKINGTOOL.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.TOOL_LINKINGTOOL.get(),DeferredRegisterItems.TOOL_LINKINGTOOL.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.TOOL_LINKINGTOOLBACKWARDS.get(),DeferredRegisterItems.TOOL_LINKINGTOOLBACKWARDS.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.TOOL_LINKINGTOOLBACKWARDS.get(),DeferredRegisterItems.TOOL_LINKINGTOOLBACKWARDS.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.TOOL_UPGRADETOOL.get(),DeferredRegisterItems.TOOL_UPGRADETOOL.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.TOOL_UPGRADETOOL.get(),DeferredRegisterItems.TOOL_UPGRADETOOL.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.TOOL_FILTERTOOL.get(),DeferredRegisterItems.TOOL_FILTERTOOL.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.TOOL_FILTERTOOL.get(),DeferredRegisterItems.TOOL_FILTERTOOL.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.TOOL_TAGTOOL.get(),DeferredRegisterItems.TOOL_TAGTOOL.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.TOOL_TAGTOOL.get(),DeferredRegisterItems.TOOL_TAGTOOL.get().getDescriptionId()+".interaction");


        addValueInfoPage(registration,DeferredRegisterItems.FILTER_BASE.get(),DeferredRegisterItems.FILTER_BASE.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_ITEM.get(),DeferredRegisterItems.FILTER_ITEM.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_ITEM.get(),DeferredRegisterItems.FILTER_ITEM.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_ITEMSTACK.get(),DeferredRegisterItems.FILTER_ITEMSTACK.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_ITEMSTACK.get(),DeferredRegisterItems.FILTER_ITEMSTACK.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_MOD.get(),DeferredRegisterItems.FILTER_MOD.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_MOD.get(),DeferredRegisterItems.FILTER_MOD.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_TAG.get(),DeferredRegisterItems.FILTER_TAG.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_TAG.get(),DeferredRegisterItems.FILTER_TAG.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_FOOD.get(),DeferredRegisterItems.FILTER_FOOD.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_FOOD.get(),DeferredRegisterItems.FILTER_FOOD.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_DURABILITY.get(),DeferredRegisterItems.FILTER_DURABILITY.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_DURABILITY.get(),DeferredRegisterItems.FILTER_DURABILITY.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_RESTRICTED.get(),DeferredRegisterItems.FILTER_RESTRICTED.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_RESTRICTED.get(),DeferredRegisterItems.FILTER_RESTRICTED.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_ENCHANTED.get(),DeferredRegisterItems.FILTER_ENCHANTED.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_ENCHANTED.get(),DeferredRegisterItems.FILTER_ENCHANTED.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_ENCHANTED_COUNT.get(),DeferredRegisterItems.FILTER_ENCHANTED_COUNT.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_ENCHANTED_COUNT.get(),DeferredRegisterItems.FILTER_ENCHANTED_COUNT.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_ENCHANTED_FUZZY.get(),DeferredRegisterItems.FILTER_ENCHANTED_FUZZY.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_ENCHANTED_FUZZY.get(),DeferredRegisterItems.FILTER_ENCHANTED_FUZZY.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_ENCHANTED_EXACT.get(),DeferredRegisterItems.FILTER_ENCHANTED_EXACT.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.FILTER_ENCHANTED_EXACT.get(),DeferredRegisterItems.FILTER_ENCHANTED_EXACT.get().getDescriptionId()+".interaction");


        addValueInfoPage(registration,DeferredRegisterItems.AUGMENT_PEDESTAL_NOCOLLIDE.get(),DeferredRegisterItems.AUGMENT_PEDESTAL_NOCOLLIDE.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.AUGMENT_PEDESTAL_NOCOLLIDE.get(),DeferredRegisterItems.AUGMENT_PEDESTAL_NOCOLLIDE.get().getDescriptionId()+".crafting");
        addValueInfoPage(registration,DeferredRegisterItems.AUGMENT_PEDESTAL_ROUNDROBIN.get(),DeferredRegisterItems.AUGMENT_PEDESTAL_ROUNDROBIN.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.AUGMENT_PEDESTAL_ROUNDROBIN.get(),DeferredRegisterItems.AUGMENT_PEDESTAL_ROUNDROBIN.get().getDescriptionId()+".crafting");
        addValueInfoPage(registration,DeferredRegisterItems.AUGMENT_PEDESTAL_RENDERDIFFUSER.get(),DeferredRegisterItems.AUGMENT_PEDESTAL_RENDERDIFFUSER.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.AUGMENT_PEDESTAL_RENDERDIFFUSER.get(),DeferredRegisterItems.AUGMENT_PEDESTAL_RENDERDIFFUSER.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.AUGMENT_PEDESTAL_RENDERDIFFUSER.get(),DeferredRegisterItems.AUGMENT_PEDESTAL_RENDERDIFFUSER.get().getDescriptionId()+".crafting");


        addValueInfoPage(registration,DeferredRegisterItems.PEDESTAL_UPGRADE_BASE.get(),DeferredRegisterItems.PEDESTAL_UPGRADE_BASE.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.PEDESTAL_UPGRADE_IMPORT.get(),DeferredRegisterItems.PEDESTAL_UPGRADE_IMPORT.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.PEDESTAL_UPGRADE_IMPORT.get(),DeferredRegisterItems.PEDESTAL_UPGRADE_IMPORT.get().getDescriptionId()+".interaction");
        addValueInfoPage(registration,DeferredRegisterItems.PEDESTAL_UPGRADE_EXPORT.get(),DeferredRegisterItems.PEDESTAL_UPGRADE_EXPORT.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.PEDESTAL_UPGRADE_EXPORT.get(),DeferredRegisterItems.PEDESTAL_UPGRADE_EXPORT.get().getDescriptionId()+".interaction");


        addValueInfoPage(registration,DeferredRegisterItems.COLORED_CRYSTAL.get(),DeferredRegisterItems.COLORED_CRYSTAL.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.COLOR_APPLICATOR.get(),DeferredRegisterItems.COLOR_APPLICATOR.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.COLOR_APPLICATOR.get(),DeferredRegisterItems.COLOR_APPLICATOR.get().getDescriptionId()+".interaction");


        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_NODE.get().asItem(),DeferredRegisterBlocks.CRYSTAL_NODE.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_CLUSTER_FULL.get().asItem(),DeferredRegisterBlocks.CRYSTAL_CLUSTER_FULL.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_CLUSTER_LARGE.get().asItem(),DeferredRegisterBlocks.CRYSTAL_CLUSTER_LARGE.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_CLUSTER_MEDIUM.get().asItem(),DeferredRegisterBlocks.CRYSTAL_CLUSTER_MEDIUM.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_CLUSTER_SMALL.get().asItem(),DeferredRegisterBlocks.CRYSTAL_CLUSTER_SMALL.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterTileBlocks.BLOCK_CRYSTAL_CLUSTER.get().asItem(),DeferredRegisterTileBlocks.BLOCK_CRYSTAL_CLUSTER.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_BLOCK.get().asItem(),DeferredRegisterBlocks.CRYSTAL_BLOCK.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_STONE.get().asItem(),DeferredRegisterBlocks.CRYSTAL_STONE.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_STONE.get().asItem(),DeferredRegisterBlocks.CRYSTAL_STONE.get().getDescriptionId()+".crafting");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_STONE_SLAB.get().asItem(),DeferredRegisterBlocks.CRYSTAL_STONE_SLAB.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_STONE_STAIR.get().asItem(),DeferredRegisterBlocks.CRYSTAL_STONE_STAIR.get().getDescriptionId()+".description");

        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_DUST_BLOCK.get().asItem(),DeferredRegisterBlocks.CRYSTAL_DUST_BLOCK.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.COLORED_CRYSTAL_DUST.get(),DeferredRegisterItems.COLORED_CRYSTAL_DUST.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterTileBlocks.BLOCK_POWDERED_DUST.get().asItem(),DeferredRegisterTileBlocks.BLOCK_POWDERED_DUST.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get().asItem(),DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterTileBlocks.BLOCK_DUST_JAR.get().asItem(),DeferredRegisterTileBlocks.BLOCK_DUST_JAR.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.SCROLL_T2_REPAIR.get(),DeferredRegisterItems.SCROLL_T2_REPAIR.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterItems.EFFECT_SCROLL.get(),DeferredRegisterItems.EFFECT_SCROLL.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.BASE_MACHINE_BLOCK.get().asItem(),DeferredRegisterBlocks.BASE_MACHINE_BLOCK.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.BASE_WORKSTATION_BLOCK.get().asItem(),DeferredRegisterBlocks.BASE_WORKSTATION_BLOCK.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterTileBlocks.BLOCK_FURNACE_BLAST_T15.get().asItem(),DeferredRegisterTileBlocks.BLOCK_FURNACE_BLAST_T15.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterTileBlocks.BLOCK_FURNACE_SMOKER_T15.get().asItem(),DeferredRegisterTileBlocks.BLOCK_FURNACE_SMOKER_T15.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterTileBlocks.BLOCK_FURNACE_SMELTER_T15.get().asItem(),DeferredRegisterTileBlocks.BLOCK_FURNACE_SMELTER_T15.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterTileBlocks.BLOCK_CRUSHER_T15.get().asItem(),DeferredRegisterTileBlocks.BLOCK_CRUSHER_T15.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_STONE_BRICK.get().asItem(),DeferredRegisterBlocks.CRYSTAL_STONE_BRICK.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_STONE_BRICK_SLAB.get().asItem(),DeferredRegisterBlocks.CRYSTAL_STONE_BRICK_SLAB.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_STONE_BRICK_STAIR.get().asItem(),DeferredRegisterBlocks.CRYSTAL_STONE_BRICK_STAIR.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_STONE_BRICKS.get().asItem(),DeferredRegisterBlocks.CRYSTAL_STONE_BRICKS.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_STONE_BRICKS_SLAB.get().asItem(),DeferredRegisterBlocks.CRYSTAL_STONE_BRICKS_SLAB.get().getDescriptionId()+".description");
        addValueInfoPage(registration,DeferredRegisterBlocks.CRYSTAL_STONE_BRICKS_STAIR.get().asItem(),DeferredRegisterBlocks.CRYSTAL_STONE_BRICKS_STAIR.get().getDescriptionId()+".description");










        addValueInfoPage(registration,DeferredRegisterTileBlocks.BLOCK_PEDESTAL.get().asItem(),DeferredRegisterTileBlocks.BLOCK_PEDESTAL.get().getDescriptionId()+".description");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        /*
        OLD PEDESTALS CODE, MAY WORK, MAY NOT
        registry.addRecipeCategories(new SmeltingAdvancedRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CrusherAdvancedRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SawmillAdvancedRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CrusherRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new SawMillRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CobbleGenRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CobbleGenSilkRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ColorPedestalRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new ColorPalletRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FluidtoExpConverterRecipeCategory(registry.getJeiHelpers().getGuiHelper()));*/
    }


    {
        /*
        OLD PEDESTALS CODE, MAY WORK, MAY NOT
        //Crafting-Anvil
        registration.addRecipeCatalyst(new ItemStack(ItemUpgradeCrafter.CRAFTER_THREE), VanillaRecipeCategoryUid.CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(ItemUpgradeExpAnvil.XPANVIL), VanillaRecipeCategoryUid.ANVIL);
        //Crusher
        registration.addRecipeCatalyst(new ItemStack(ItemUpgradeCrusher.CRUSHER), CrusherRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ItemUpgradeEnergyCrusher.RFCRUSHER), CrusherRecipeCategory.UID);
        ItemStack getCrusher = new ItemStack(ItemUpgradeCrusher.CRUSHER.getItem());
        getCrusher.addEnchantment(EnchantmentRegistry.ADVANCED,1);
        registration.addRecipeCatalyst(getCrusher, CrusherAdvancedRecipeCategory.UID);
        ItemStack getCrusherRF = new ItemStack(ItemUpgradeEnergyCrusher.RFCRUSHER.getItem());
        getCrusherRF.addEnchantment(EnchantmentRegistry.ADVANCED,1);
        registration.addRecipeCatalyst(getCrusherRF, CrusherAdvancedRecipeCategory.UID);
        //Sawmill
        registration.addRecipeCatalyst(new ItemStack(ItemUpgradeSawMill.SAWMILL), SawMillRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ItemUpgradeEnergySawMill.RFSAWMILL), SawMillRecipeCategory.UID);
        ItemStack getSawmill = new ItemStack(ItemUpgradeSawMill.SAWMILL.getItem());
        getSawmill.addEnchantment(EnchantmentRegistry.ADVANCED,1);
        registration.addRecipeCatalyst(getSawmill, SawmillAdvancedRecipeCategory.UID);
        ItemStack getSawmillRF = new ItemStack(ItemUpgradeEnergySawMill.RFSAWMILL.getItem());
        getSawmillRF.addEnchantment(EnchantmentRegistry.ADVANCED,1);
        registration.addRecipeCatalyst(getSawmillRF, SawmillAdvancedRecipeCategory.UID);
        //Smelter
        registration.addRecipeCatalyst(new ItemStack(ItemUpgradeFurnace.SMELTER), VanillaRecipeCategoryUid.FURNACE);
        registration.addRecipeCatalyst(new ItemStack(ItemUpgradeEnergyFurnace.RFSMELTER), VanillaRecipeCategoryUid.FURNACE);
        ItemStack getSmelter = new ItemStack(ItemUpgradeFurnace.SMELTER.getItem());
        getSmelter.addEnchantment(EnchantmentRegistry.ADVANCED,1);
        registration.addRecipeCatalyst(getSmelter, SmeltingAdvancedRecipeCategory.UID);
        ItemStack getSmelterRF = new ItemStack(ItemUpgradeEnergyFurnace.RFSMELTER.getItem());
        getSmelterRF.addEnchantment(EnchantmentRegistry.ADVANCED,1);
        registration.addRecipeCatalyst(getSmelterRF, SmeltingAdvancedRecipeCategory.UID);
        //Colored Pedestals
        registration.addRecipeCatalyst(new ItemStack(ItemColorPallet.COLORPALLET), ColorPedestalRecipeCategory.UID);
        //Color Pallets
        registration.addRecipeCatalyst(new ItemStack(ItemLinkingTool.DEFAULT), ColorPalletRecipeCategory.UID);
        //Cobble Gen
        registration.addRecipeCatalyst(new ItemStack(ItemUpgradeCobbleGen.COBBLE), CobbleGenRecipeCategory.UID);
        //Cobble Gen Silk
        ItemStack getStack = new ItemStack(ItemUpgradeCobbleGen.COBBLE.getItem());
        getStack.addEnchantment(Enchantments.SILK_TOUCH,1);
        registration.addRecipeCatalyst(getStack, CobbleGenSilkRecipeCategory.UID);
        //Fluid To XP
        registration.addRecipeCatalyst(new ItemStack(ItemUpgradeExpFluidConverter.FLUIDXPCONVERTER), FluidtoExpConverterRecipeCategory.UID);
    */
    }
}
