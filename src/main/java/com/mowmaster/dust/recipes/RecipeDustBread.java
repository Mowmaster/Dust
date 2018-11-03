package com.mowmaster.dust.recipes;

import com.google.gson.JsonObject;
import com.mowmaster.dust.effects.EffectPicker;
import com.mowmaster.dust.enums.CrystalTypes;
import com.mowmaster.dust.items.ItemDust;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeTippedArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.mowmaster.dust.misc.DustConfigurationFile.effectMaximum;

public class RecipeDustBread //extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    /*
    int count = 0;
    int red = 0;
    int blue = 0;
    int yellow = 0;
    int white = 0;
    int black = 0;

    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        if (inv.getWidth() == 3 && inv.getHeight() == 3)
        {
            for (int i = 0; i < inv.getWidth(); ++i)
            {
                for (int j = 0; j < inv.getHeight(); ++j)
                {
                    ItemStack itemstack = inv.getStackInRowAndColumn(i, j);
                    if(i==1 && j==1)
                    {
                        if(itemstack.getItem().equals(Items.BREAD)){continue;}
                        else return false;
                    }

                    if(itemstack.getItem() instanceof ItemDust || itemstack.getItem().equals(Items.BREAD) || itemstack.equals(ItemStack.EMPTY))
                    {
                        if(itemstack.getItem().equals(Items.BREAD))
                        {
                            continue;
                        }
                        else
                        {
                            switch (itemstack.getMetadata())
                            {
                                case 0:
                                    red += 2;
                                    count++;
                                    break;
                                case 1:
                                    blue += 2;
                                    count++;
                                    break;
                                case 2:
                                    yellow += 2;
                                    count++;
                                    break;
                                case 3://purple
                                    red++;
                                    blue++;
                                    count++;
                                    break;
                                case 4://green
                                    yellow++;
                                    blue++;
                                    count++;
                                    break;
                                case 5://orange
                                    red++;
                                    yellow++;
                                    count++;
                                    break;
                                case 6:
                                    white++;
                                    count++;
                                    break;
                                case 7:
                                    black++;
                                    count++;
                                    break;
                            }
                        }
                    }
                    else return false;
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    //Returns an Item that is the result of this recipe


    private static int potencyLimiter =  effectMaximum;
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack itemstack = inv.getStackInRowAndColumn(1, 1);

        if (itemstack.getItem() != Items.BREAD)
        {
            return ItemStack.EMPTY;
        }
        else
        {
            PotionEffect effect = EffectPicker.getEffectFromInputs(red, blue, yellow, white, black, 20 * count,potencyLimiter, false, true, CrystalTypes.EffectTypes.DUST);
            ItemStack itemstack1 = new ItemStack(ItemRegistry.dustbread, 1);
            NBTTagCompound cmpd = new NBTTagCompound();
            cmpd.setTag("breadeffect",effect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            itemstack1.setTagCompound(cmpd);
            return itemstack1;
        }
    }

    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        return NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    public boolean isHidden()
    {
        return true;
    }

    //Used to determine if this recipe can fit in a grid of the given width/height
    public boolean canFit(int width, int height)
    {
        return width >= 2 && height >= 2;
    }
     */

}
