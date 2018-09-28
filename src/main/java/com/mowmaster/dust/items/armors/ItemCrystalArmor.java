package com.mowmaster.dust.items.armors;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.items.armors.models.ModelCrystalHelmet;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opencl.CL;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ItemCrystalArmor extends ItemArmor
{

    private boolean showHelm=false;
    private boolean showChest=false;
    private boolean showLegs=false;
    private boolean showBoots=false;
    int colorMultiplier = 8;
    private int colorHelm=0;
    private int colorChest=0;
    private int colorLeg=0;
    private int colorBoot=0;


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
        return true;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }

    private int getColorCrystalHelm(ItemStack stack)
    {
        if(stack !=null && stack.isItemEnchanted())
        {
            NBTTagList list = stack.getEnchantmentTagList();

            if (list == null) {
                return colorHelm = 0;
            }

            int id = 0;
            Enchantment e = Enchantment.getEnchantmentByID(id);

            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound compound = list.getCompoundTagAt(i);
                id = compound.getShort("id");
                e = Enchantment.getEnchantmentByID(id);

                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION,stack)>0)
                {
                    colorHelm = 0;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.AQUA_AFFINITY,stack)>0)
                {
                    colorHelm = colorMultiplier;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.RESPIRATION,stack)>0)
                {
                    colorHelm = colorMultiplier * 2;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION,stack)>0)
                {
                    colorHelm = colorMultiplier * 3;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS,stack)>0)
                {
                    colorHelm = colorMultiplier * 4;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION,stack)>0)
                {
                    colorHelm = colorMultiplier * 5;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING,stack)>0)
                {
                    colorHelm = colorMultiplier * 6;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION,stack)>0)
                {
                    colorHelm = colorMultiplier * 7;
                }
            }
        }
        return colorHelm;
    }

    private int getColorCrystalChest(ItemStack stack)
    {
        if(stack !=null && stack.isItemEnchanted())
        {
            NBTTagList list = stack.getEnchantmentTagList();

            if (list == null) {
                return colorChest = 0;
            }

            int id = 0;
            Enchantment e = Enchantment.getEnchantmentByID(id);

            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound compound = list.getCompoundTagAt(i);
                id = compound.getShort("id");
                e = Enchantment.getEnchantmentByID(id);

                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION,stack)>0)
                {
                    colorChest = 0;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantLastResort,stack)>0)
                {
                    colorChest = colorMultiplier;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantSteadfast,stack)>0)
                {
                    colorChest = colorMultiplier * 2;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION,stack)>0)
                {
                    colorChest = colorMultiplier * 3;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS,stack)>0)
                {
                    colorChest = colorMultiplier * 4;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION,stack)>0)
                {
                    colorChest = colorMultiplier * 5;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING,stack)>0)
                {
                    colorChest = colorMultiplier * 6;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION,stack)>0)
                {
                    colorChest = colorMultiplier * 7;
                }
            }
        }
        return colorChest;
    }

    private int getColorCrystalLeg(ItemStack stack)
    {
        if(stack !=null && stack.isItemEnchanted())
        {
            NBTTagList list = stack.getEnchantmentTagList();

            if (list == null) {
                return colorLeg = 0;
            }

            int id = 0;
            Enchantment e = Enchantment.getEnchantmentByID(id);

            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound compound = list.getCompoundTagAt(i);
                id = compound.getShort("id");
                e = Enchantment.getEnchantmentByID(id);

                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION,stack)>0)
                {
                    colorLeg = 0;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentStepAssist,stack)>0)
                {
                    colorLeg = colorMultiplier;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentQuickPace,stack)>0)
                {
                    colorLeg = colorMultiplier * 2;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION,stack)>0)
                {
                    colorLeg = colorMultiplier * 3;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS,stack)>0)
                {
                    colorLeg = colorMultiplier * 4;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION,stack)>0)
                {
                    colorLeg = colorMultiplier * 5;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING,stack)>0)
                {
                    colorLeg = colorMultiplier * 6;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION,stack)>0)
                {
                    colorLeg = colorMultiplier * 7;
                }
            }
        }
        return colorLeg;
    }

    private int getColorCrystalBoot(ItemStack stack)
    {
        if(stack !=null && stack.isItemEnchanted())
        {
            NBTTagList list = stack.getEnchantmentTagList();

            if (list == null) {
                return colorBoot = 0;
            }

            int id = 0;
            Enchantment e = Enchantment.getEnchantmentByID(id);

            //System.out.println(e.getName());

            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound compound = list.getCompoundTagAt(i);
                id = compound.getShort("id");
                e = Enchantment.getEnchantmentByID(id);

                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION,stack)>0)
                {
                    colorBoot = 0;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER,stack)>0)
                {
                    colorBoot = colorMultiplier;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING,stack)>0)
                {
                    colorBoot = colorMultiplier * 2;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION,stack)>0)
                {
                    colorBoot = colorMultiplier * 3;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS,stack)>0)
                {
                    colorBoot = colorMultiplier * 4;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION,stack)>0)
                {
                    colorBoot = colorMultiplier * 5;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING,stack)>0)
                {
                    colorBoot = colorMultiplier * 6;
                }
                else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION,stack)>0)
                {
                    colorBoot = colorMultiplier * 7;
                }
            }
        }
        return colorBoot;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if(!world.isRemote)
        {
            if(!player.inventory.armorItemInSlot(3).isEmpty() && player.inventory.armorItemInSlot(3).getItem() instanceof ItemCrystalArmor
                    && !player.inventory.armorItemInSlot(2).isEmpty() && player.inventory.armorItemInSlot(2).getItem() instanceof ItemCrystalArmor
                    && !player.inventory.armorItemInSlot(1).isEmpty() && player.inventory.armorItemInSlot(1).getItem() instanceof ItemCrystalArmor
                    && !player.inventory.armorItemInSlot(0).isEmpty() && player.inventory.armorItemInSlot(0).getItem() instanceof ItemCrystalArmor
                    )
            {
                if(colorHelm==colorChest && colorChest==colorLeg && colorLeg==colorBoot && player.inventory.armorItemInSlot(3).isItemEnchanted())
                {
                    if(colorHelm==0)
                    {
                        this.effectPlayer(player, MobEffects.RESISTANCE,0);
                        this.effectPlayer(player, MobEffects.STRENGTH,1);
                    }
                    else if(colorHelm==colorMultiplier)
                    {
                        this.effectPlayer(player,MobEffects.WATER_BREATHING,0);
                        this.effectPlayer(player, MobEffects.RESISTANCE,0);
                        this.effectPlayer(player, MobEffects.STRENGTH,0);
                    }
                    else if(colorHelm==colorMultiplier * 2)
                    {
                        this.effectPlayer(player,MobEffects.SATURATION,0);
                        this.effectPlayer(player, MobEffects.RESISTANCE,0);
                        this.effectPlayer(player, MobEffects.STRENGTH,0);
                    }
                    else if(colorHelm==colorMultiplier * 3)
                    {
                        this.effectPlayer(player,MobEffects.RESISTANCE,1);
                        this.effectPlayer(player, MobEffects.STRENGTH,0);
                    }
                    else if(colorHelm==colorMultiplier * 4)
                    {
                        this.effectPlayer(player,MobEffects.REGENERATION,0);
                        this.effectPlayer(player, MobEffects.RESISTANCE,0);
                        this.effectPlayer(player, MobEffects.STRENGTH,0);
                    }
                    else if(colorHelm==colorMultiplier * 5)
                    {
                        this.effectPlayer(player,MobEffects.HASTE,0);
                        this.effectPlayer(player, MobEffects.RESISTANCE,0);
                        this.effectPlayer(player, MobEffects.STRENGTH,0);
                    }
                    else if(colorHelm==colorMultiplier * 6)
                    {
                        this.effectPlayer(player, PotionRegistry.POTION_FLIGHT,0);
                        this.effectPlayer(player, MobEffects.RESISTANCE,0);
                        this.effectPlayer(player, MobEffects.STRENGTH,0);
                    }
                    else if(colorHelm==colorMultiplier * 7)
                    {
                        this.effectPlayer(player,MobEffects.NIGHT_VISION,0);
                        this.effectPlayer(player, MobEffects.RESISTANCE,0);
                        this.effectPlayer(player, MobEffects.STRENGTH,0);
                    }
                }
                else
                {
                    this.effectPlayer(player, MobEffects.RESISTANCE,0);
                    this.effectPlayer(player, MobEffects.STRENGTH,0);
                }
            }
        }
        else
        {
            if(world.isRemote)
            {
                if(player.inventory.armorItemInSlot(3) !=null && player.inventory.armorItemInSlot(3).isItemEnchanted())
                {
                    getColorCrystalHelm(player.inventory.armorItemInSlot(3));
                }
                else colorHelm=0;

                if(player.inventory.armorItemInSlot(2) !=null && player.inventory.armorItemInSlot(2).isItemEnchanted())
                {
                    getColorCrystalChest(player.inventory.armorItemInSlot(2));
                }
                else colorChest=0;

                if(player.inventory.armorItemInSlot(1) !=null && player.inventory.armorItemInSlot(1).isItemEnchanted())
                {
                    getColorCrystalLeg(player.inventory.armorItemInSlot(1));
                }
                else colorLeg=0;

                if(player.inventory.armorItemInSlot(0) !=null && player.inventory.armorItemInSlot(0).isItemEnchanted())
                {
                    getColorCrystalBoot(player.inventory.armorItemInSlot(0));
                }
                else colorBoot=0;
            }
        }
    }



    private void effectPlayer(EntityPlayer player, Potion potion, int amplifier)
    {
        if(player.getActivePotionEffect(potion) == null)
        {
            player.addPotionEffect(new PotionEffect(potion,120,amplifier,false,false));
        }
    }



    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if(!itemStack.isEmpty())
        {
            if(itemStack.getItem() instanceof ItemArmor)
            {
                ModelCrystalHelmet armorModel = new ModelCrystalHelmet(1.0f,colorHelm,colorChest,colorLeg,colorBoot);
                ModelCrystalHelmet armorModelLegs = new ModelCrystalHelmet(0.75f,colorHelm,colorChest,colorLeg,colorBoot);


                armorModel.bipedHead.showModel = (armorSlot == EntityEquipmentSlot.HEAD);armorModel.bipedHeadwear.showModel = (armorSlot == EntityEquipmentSlot.HEAD);
                armorModel.bipedBody.showModel = armorSlot == EntityEquipmentSlot.CHEST || (armorSlot == EntityEquipmentSlot.CHEST);
                armorModel.bipedRightArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
                armorModel.bipedLeftArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
                armorModelLegs.bipedRightLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS;
                armorModelLegs.bipedLeftLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS;
                armorModelLegs.bipedRightLeg.showModel = armorSlot == EntityEquipmentSlot.FEET;
                armorModelLegs.bipedLeftLeg.showModel = armorSlot == EntityEquipmentSlot.FEET;

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