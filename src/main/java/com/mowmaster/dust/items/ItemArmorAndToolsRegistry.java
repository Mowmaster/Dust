package com.mowmaster.dust.items;

import com.mowmaster.dust.items.armors.ItemCrystalArmor;
import com.mowmaster.dust.items.trinkets.ItemCrystalAxe;
import com.mowmaster.dust.items.trinkets.ItemCrystalPickaxe;
import com.mowmaster.dust.items.trinkets.ItemCrystalSword;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemArmorAndToolsRegistry
{
    //public static ItemArmor.ArmorMaterial material = EnumHelper.addArmorMaterial("name",Reference.MODID + ":name", durability 33 is diamond, armor values 20max {H,C,L,B}, enchantability, toughnessF)
    public static ItemArmor.ArmorMaterial crystalArmorMaterial = EnumHelper.addArmorMaterial("crystal",Reference.MODID + ":crystal",50, new int[]{4,8,5,3},20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,3.0F);


    //public static ToolMaterial addToolMaterial(String name, int harvestLevel, int maxUses, float efficiency, float damage, int enchantability)
    //public static final Item.ToolMaterial crystalMaterial = EnumHelper.addToolMaterial("crystal",harvest level,uses,effiency,damage,enchantability);
    public static Item.ToolMaterial crystalToolMaterial = EnumHelper.addToolMaterial("crystal",3,2000,8F,3.0F,20);




    public static ItemSword crystalSword;

    // will probably become theyre own class respectively
   // public static ItemBow crystalBow;
  //  public static ItemAxe crystalAxe;
   // public static ItemSpade crystalShovel;
   // public static ItemHoe crystalHoe;

    public static ItemCrystalPickaxe crystalPickaxe;
    public static ItemCrystalAxe crystalAxe;

    public static ItemArmor crystalHelmet;
    public static ItemArmor crystalChestplate;
    public static ItemArmor crystalLeggings;
    public static ItemArmor crystalBoots;
    public static ItemShield crystalShield;


    public static void init()
    {
        //weapons
        crystalSword = new ItemCrystalSword(crystalToolMaterial);

        // tool section
        crystalPickaxe = new ItemCrystalPickaxe(crystalToolMaterial);
        crystalAxe = new ItemCrystalAxe(crystalToolMaterial);

        crystalHelmet = new ItemCrystalArmor(crystalArmorMaterial, 1, EntityEquipmentSlot.HEAD,"crystalhelmet","crystalhelmet");
        crystalChestplate = new ItemCrystalArmor(crystalArmorMaterial, 1, EntityEquipmentSlot.CHEST,"crystalchestplate","crystalchestplate");
        crystalLeggings = new ItemCrystalArmor(crystalArmorMaterial, 2, EntityEquipmentSlot.LEGS,"crystalleggings","crystalleggings");
        crystalBoots = new ItemCrystalArmor(crystalArmorMaterial, 1, EntityEquipmentSlot.FEET,"crystalboots","crystalboots");
        //crystalBoots = new ItemCrystalArmor(Material, Layer, What Equipment Slot,unlocName,RegName);




    }

    public static void register()
    {
        // tool and weapon section
        registerItem(crystalSword);
        registerItem(crystalPickaxe);
        registerItem(crystalAxe);
        registerItem(crystalHelmet);
        registerItem(crystalChestplate);
        registerItem(crystalLeggings);
        registerItem(crystalBoots);


    }

    public static void registerRenders()
    {
        registerRender(crystalHelmet);
        registerRender(crystalChestplate);
        registerRender(crystalLeggings);
        registerRender(crystalBoots);
// weapon renders
        registerRender(crystalSword);
        registerRender(crystalPickaxe);
        registerRender(crystalAxe);
    }

    public static void registerItem(Item item)
    {
        ForgeRegistries.ITEMS.register(item);
    }

    public static void registerRender(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID, item.getUnlocalizedName().substring(5)), "inventory"));
    }

    public static void registerRender(Item item, int meta, String fileName)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Reference.MODID, fileName), "inventory"));
    }




}