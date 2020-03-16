package com.mowmaster.dust.effects;

import com.mowmaster.dust.references.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PotionRegistry {

    public static final Potion VOIDSTORAGE = new Potion(Reference.MODID + "_voidstorage", new EffectInstance(PotionEffects.voidstorage, 200)).setRegistryName(Reference.MODID + ":voidstorage");

    @SubscribeEvent
    public static void onPotEffectRegistry(RegistryEvent.Register<Effect> event) {
        IForgeRegistry<Effect> r = event.getRegistry();
        register(r, new PotionVoidStorage(EffectType.NEUTRAL, 0xcccc00), "voidstorage");
    }

    private static void register(IForgeRegistry<Effect> r, PotionBasePlayerRClickBlock pot, String name) {
        pot.setRegistryName(new ResourceLocation(Reference.MODID, name));
        r.register(pot);
        PotionEffects.effects.add(pot);
    }

    @SubscribeEvent
    public static void onPotRegistry(RegistryEvent.Register<Potion> event) {
        IForgeRegistry<Potion> r = event.getRegistry();
        r.register(VOIDSTORAGE);
    }

    public static class PotionEffects {

        public static final List<PotionBasePlayerRClickBlock> effects = new ArrayList<PotionBasePlayerRClickBlock>();
        @ObjectHolder(Reference.MODID + ":voidstorage")
        public static PotionBasePlayerRClickBlock voidstorage;
    }

    public static class PotionItem {

        @ObjectHolder(Reference.MODID + ":voidstorage")
        public static Potion voidstorage;
    }

    public static void setup(FMLCommonSetupEvent event) {
        /*basicBrewing(PotionRegistry.PotionItem.voidstorage, Items.CLAY);
        splashBrewing(PotionRegistry.PotionItem.voidstorage);
        lingerBrewing(PotionRegistry.PotionItem.voidstorage, Items.CLAY);*/
    }

    private static void basicBrewing(Potion pot, Item item) {
        ItemStack AWKWARD = PotionUtils.addPotionToItemStack(
                new ItemStack(Items.POTION), Potions.AWKWARD);
        //hmm wat
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.fromStacks(AWKWARD), Ingredient.fromItems(item),
                PotionUtils.addPotionToItemStack(
                        new ItemStack(Items.POTION), pot)));
    }

    private static void splashBrewing(Potion pot) {
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.fromStacks(PotionUtils.addPotionToItemStack(
                new ItemStack(Items.POTION), pot)),
                Ingredient.fromStacks(new ItemStack(Items.GUNPOWDER)),
                PotionUtils.addPotionToItemStack(
                        new ItemStack(Items.SPLASH_POTION), pot)));
    }

    private static void lingerBrewing(Potion pot, Item item) {
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.fromStacks(PotionUtils.addPotionToItemStack(
                new ItemStack(Items.LINGERING_POTION), Potions.AWKWARD)),
                Ingredient.fromStacks(new ItemStack(item)),
                PotionUtils.addPotionToItemStack(
                        new ItemStack(Items.LINGERING_POTION), pot)));
    }
}
