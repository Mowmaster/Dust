package com.mowmaster.dust.items.armors;

import com.mowmaster.dust.items.ItemArmorAndToolsRegistry;
import com.mowmaster.dust.items.armors.models.ModelCrystalHelmet;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ItemCrystalArmor extends ItemArmor
{
    public ItemCrystalArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot entityEquipmentSlot, String unlocName, String registryName)
    {
        super(material,renderIndex,entityEquipmentSlot);
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setMaxStackSize(1);
        this.setCreativeTab(DUSTTABS);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if(player.inventory.armorItemInSlot(3) !=null && player.inventory.armorItemInSlot(3).getItem() == ItemArmorAndToolsRegistry.crystalHelmet
                && player.inventory.armorItemInSlot(2) !=null && player.inventory.armorItemInSlot(2).getItem() == ItemArmorAndToolsRegistry.crystalChestplate
                && player.inventory.armorItemInSlot(1) !=null && player.inventory.armorItemInSlot(1).getItem() == ItemArmorAndToolsRegistry.crystalLeggings
                && player.inventory.armorItemInSlot(0) !=null && player.inventory.armorItemInSlot(0).getItem() == ItemArmorAndToolsRegistry.crystalBoots)
        {
            this.effectPlayer(player, MobEffects.RESISTANCE,0);
        }
    }

    private void effectPlayer(EntityPlayer player, Potion potion, int amplifier)
    {
        if(player.getActivePotionEffect(potion) == null ||player.getActivePotionEffect(potion).getDuration() <=1)
        {
            player.addPotionEffect(new PotionEffect(potion,100,amplifier,false,false));
        }
    }

    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if(!itemStack.isEmpty())
        {
            if(itemStack.getItem() instanceof ItemArmor)
            {
                ModelCrystalHelmet armorModel = new ModelCrystalHelmet(1.0f);
                ModelCrystalHelmet armorModelLegs = new ModelCrystalHelmet(0.5f);

                armorModel.bipedHead.showModel = (armorSlot == EntityEquipmentSlot.HEAD);
                armorModel.bipedHeadwear.showModel = (armorSlot == EntityEquipmentSlot.HEAD);
                armorModel.bipedBody.showModel = armorSlot == EntityEquipmentSlot.CHEST || (armorSlot == EntityEquipmentSlot.CHEST);
                armorModel.bipedRightArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
                armorModel.bipedLeftArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
                armorModelLegs.bipedRightLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS || (armorSlot == EntityEquipmentSlot.FEET);
                armorModelLegs.bipedLeftLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS || (armorSlot == EntityEquipmentSlot.FEET);

                armorModel.isSneak = _default.isSneak;
                armorModel.isRiding = _default.isRiding;
                armorModel.isChild = _default.isChild;
                armorModel.rightArmPose = _default.rightArmPose;
                armorModel.leftArmPose = _default.leftArmPose;

                armorModelLegs.isSneak = _default.isSneak;
                armorModelLegs.isRiding = _default.isRiding;
                armorModelLegs.isChild = _default.isChild;
                armorModelLegs.rightArmPose = _default.rightArmPose;
                armorModelLegs.leftArmPose = _default.leftArmPose;

                return armorModel;
            }
        }
        return null;
    }
}
