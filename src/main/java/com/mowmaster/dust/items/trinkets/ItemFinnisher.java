package com.mowmaster.dust.items.trinkets;

import com.mowmaster.dust.references.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ItemFinnisher extends ItemFood


{
    private PotionEffect[] effects;

///PotionEffect... effects means that you can use a list of them and they will go into the PotionEffect[] array
    public ItemFinnisher(String unlocName, String registryName,int amount, float saturation, boolean isWolfFood, PotionEffect... effects) {
        super(amount, saturation, isWolfFood);
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 1;
        this.setCreativeTab(DUSTTABS);
        this.effects = effects;
        this.setAlwaysEdible();

    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        super.onFoodEaten(stack, worldIn, player);

        for (int i = 0; i < effects.length; i ++) {
            if (!worldIn.isRemote && effects[i] != null && effects[i].getDuration() > 0)
                player.addPotionEffect(new PotionEffect(this.effects[i]));
        }
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("Become Imbued with the power of the Finnish");
        tooltip.add("~FinnishFool");
    }
}
