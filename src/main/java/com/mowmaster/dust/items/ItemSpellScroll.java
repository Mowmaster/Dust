package com.mowmaster.dust.items;

import com.mowmaster.dust.effects.PotionQuickness;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemSpellScroll extends Item
{
    PotionEffect potionEffect = new PotionEffect(MobEffects.LUCK);
    public ItemSpellScroll(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        potionEffect = PotionEffect.readCustomPotionEffectFromNBT(stack.getTagCompound().getCompoundTag("scrolleffect"));
        if(entityIn.isSneaking())
        {
            stack.setStackDisplayName("Potency: " + PotionEffect.readCustomPotionEffectFromNBT(stack.getTagCompound().getCompoundTag("scrolleffect")).getAmplifier() +
                    " Duration: " + PotionEffect.readCustomPotionEffectFromNBT(stack.getTagCompound().getCompoundTag("scrolleffect")).getDuration()/20 + " seconds");
        }
        else stack.setStackDisplayName("Scroll of " + PotionEffect.readCustomPotionEffectFromNBT(stack.getTagCompound().getCompoundTag("scrolleffect")).getEffectName().replace("effect.","").toUpperCase());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.addPotionEffect(PotionEffect.readCustomPotionEffectFromNBT(playerIn.getHeldItem(handIn).getTagCompound().getCompoundTag("scrolleffect")));
        playerIn.getHeldItemMainhand().shrink(1);
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
        return ActionResult.newResult(EnumActionResult.PASS,playerIn.getHeldItem(handIn));
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        String count = "";
        switch (potionEffect.getAmplifier())
        {
            case 0:
                count = "I";
                break;
            case 1:
                count = "II";
                break;
            case 2:
                count = "III";
                break;
            case 3:
                count = "IV";
                break;
            case 4:
                count = "V";
                break;
            case 5:
                count = "VI";
                break;
            case 6:
                count = "VII";
                break;
            case 7:
                count = "VIII";
                break;
            case 8:
                count = "IX";
                break;
            case 9:
                count = "X";
                break;
        }
        tooltip.add(potionEffect.getEffectName().replace("effect.","") + " " + count);
        tooltip.add("Duration: " + potionEffect.getDuration()/20 + " seconds");
    }
     */



}


