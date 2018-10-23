package com.mowmaster.dust.items;

import com.mowmaster.dust.references.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

//import static com.mowmaster.dust.misc.AchievementHandler.achievementScrollA;


public class ItemCoin extends Item
{
    public ItemCoin(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(stack.getItem().equals(ItemRegistry.filterUpgrade))
        {
            tooltip.add("Filter Upgrade");
        }
        else if(stack.getItem().equals(ItemRegistry.fuzzyFilterUpgrade))
        {
            tooltip.add("Fuzzy Filter Upgrade");
        }
        else if(stack.getItem().equals(ItemRegistry.filterBlacklistUpgrade))
        {
            tooltip.add("Blacklist Filter Upgrade");
        }
        else if(stack.getItem().equals(ItemRegistry.fuzzyFilterBlacklistUpgrade))
        {
            tooltip.add("Fuzzy Blacklist Filter Upgrade");
        }
    }
}


