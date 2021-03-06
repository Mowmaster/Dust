package com.mowmaster.dust.items;

import com.mowmaster.dust.references.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemCharcoal extends Item
{
    int fuelValue = -1;
    public ItemCharcoal(String unlocName, String registryName, int fuelBurnTime)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.fuelValue = fuelBurnTime;
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack) {
        return fuelValue;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("Still holds a faint mysterious power even after being smelted...");
    }
}
