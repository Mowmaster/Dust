package com.mowmaster.dust.References;

import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.Recipes.MachineBlockRepairItemsRecipe;
import com.mowmaster.dust.Recipes.MobEffectColorRecipe;
import com.mowmaster.dust.Recipes.MobEffectColorRecipeCorrupted;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EffectPickerReference
{
    @Nullable
    protected static MobEffectColorRecipe getRecipeMobEffectColor(Level level, ItemStack stackIn) {
        Container container = Constants.blankContainer;
        container.setItem(0,stackIn);
        List<MobEffectColorRecipe> recipes = level.getRecipeManager().getRecipesFor(MobEffectColorRecipe.MOBEFFECTCOLOR,container,level);
        return recipes.size() > 0 ? level.getRecipeManager().getRecipesFor(MobEffectColorRecipe.MOBEFFECTCOLOR,container,level).get(0) : null;
    }

    protected static String getProcessResultMobEffectColorRecipe(MobEffectColorRecipe recipe) {
        return (recipe == null)?(""):(recipe.getResultEffectName());
    }

    protected static int getProcessResultEffectColorRecipe(MobEffectColorRecipe recipe) {
        return (recipe == null)?(ColorReference.DEFAULTCOLOR):(recipe.getResultEffectColor());
    }

    protected static int getProcessResultEffectInstantDurationRecipe(MobEffectColorRecipe recipe) {
        return (recipe == null)?(1):(recipe.getResultInstantTickDuration());
    }

    @Nullable
    protected static MobEffectColorRecipeCorrupted getRecipeMobEffectColorCorrupted(Level level, ItemStack stackIn) {
        Container container = Constants.blankContainer;
        container.setItem(0,stackIn);
        List<MobEffectColorRecipeCorrupted> recipes = level.getRecipeManager().getRecipesFor(MobEffectColorRecipeCorrupted.MOBEFFECTCOLORCORRUPTED,container,level);
        return recipes.size() > 0 ? level.getRecipeManager().getRecipesFor(MobEffectColorRecipeCorrupted.MOBEFFECTCOLORCORRUPTED,container,level).get(0) : null;
    }

    protected static String getProcessResultMobEffectColorRecipeCorrupted(MobEffectColorRecipeCorrupted recipe) {
        return (recipe == null)?(""):(recipe.getResultEffectName());
    }

    protected static int getProcessResultEffectColorRecipeCorrupted(MobEffectColorRecipeCorrupted recipe) {
        return (recipe == null)?(ColorReference.DEFAULTCOLOR):(recipe.getResultEffectColor());
    }

    protected static int getProcessResultEffectInstantDurationRecipeCorrupted(MobEffectColorRecipeCorrupted recipe) {
        return (recipe == null)?(1):(recipe.getResultInstantTickDuration());
    }

    public static MobEffect getEffectForColor(Level level, boolean corruption, int currentColor)
    {
        if(corruption)
        {
            ItemStack stack = ColorReference.addColorToItemStack(new ItemStack(DeferredRegisterItems.COLORED_CRYSTAL.get()),currentColor);
            ResourceLocation location = new ResourceLocation(getProcessResultMobEffectColorRecipeCorrupted(getRecipeMobEffectColorCorrupted(level,stack)));
            if(Registry.MOB_EFFECT.getOptional(location).isPresent())return Registry.MOB_EFFECT.getOptional(location).get();
        }
        else if (!corruption)
        {
            ItemStack stack = ColorReference.addColorToItemStack(new ItemStack(DeferredRegisterItems.COLORED_CRYSTAL.get()),currentColor);
            ResourceLocation location = new ResourceLocation(getProcessResultMobEffectColorRecipe(getRecipeMobEffectColor(level,stack)));
            if(Registry.MOB_EFFECT.getOptional(location).isPresent())return Registry.MOB_EFFECT.getOptional(location).get();
        }

        return getRandomNegativeEffect();
    }

    public static int getColorForEffect(Level level, boolean corruption, int currentColor)
    {
        if(corruption)
        {
            ItemStack stack = ColorReference.addColorToItemStack(new ItemStack(DeferredRegisterItems.COLORED_CRYSTAL.get()),currentColor);
            return getProcessResultEffectColorRecipeCorrupted(getRecipeMobEffectColorCorrupted(level,stack));
        }
        else if (!corruption)
        {

            ItemStack stack = ColorReference.addColorToItemStack(new ItemStack(DeferredRegisterItems.COLORED_CRYSTAL.get()),currentColor);
            return getProcessResultEffectColorRecipe(getRecipeMobEffectColor(level,stack));
        }

        return ColorReference.DEFAULTCOLOR;
    }

    public static int getInstantDuration(Level level, boolean corruption, int currentColor)
    {
        if(corruption)
        {
            ItemStack stack = ColorReference.addColorToItemStack(new ItemStack(DeferredRegisterItems.COLORED_CRYSTAL.get()),currentColor);
            return getProcessResultEffectInstantDurationRecipeCorrupted(getRecipeMobEffectColorCorrupted(level,stack));
        }
        else if (!corruption)
        {
            ItemStack stack = ColorReference.addColorToItemStack(new ItemStack(DeferredRegisterItems.COLORED_CRYSTAL.get()),currentColor);
            return getProcessResultEffectInstantDurationRecipe(getRecipeMobEffectColor(level,stack));
        }

        return ColorReference.DEFAULTCOLOR;
    }

    public static MobEffect getRandomNegativeEffect()
    {
        Random rand = new Random();
        Map<Integer, MobEffect> NEGEFFECT = Map.ofEntries(
                Map.entry(0, MobEffects.BAD_OMEN),
                Map.entry(1,MobEffects.BLINDNESS),
                Map.entry(2,MobEffects.GLOWING),
                Map.entry(3,MobEffects.HUNGER),
                Map.entry(4,MobEffects.HARM),
                Map.entry(5,MobEffects.LEVITATION),
                Map.entry(6,MobEffects.DIG_SLOWDOWN),
                Map.entry(7,MobEffects.CONFUSION),
                Map.entry(8,MobEffects.POISON),
                Map.entry(9,MobEffects.MOVEMENT_SLOWDOWN),
                Map.entry(10,MobEffects.UNLUCK),
                Map.entry(11,MobEffects.WEAKNESS),
                Map.entry(12,MobEffects.WITHER)
        );

        return NEGEFFECT.getOrDefault(rand.nextInt(13),MobEffects.HUNGER);
    }
}
