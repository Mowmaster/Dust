package com.mowmaster.dust.recipes;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.enchantments.EnchantmentLastResort;
import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.items.ItemArmorAndToolsRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.misc.AchievementHandler;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import static com.mowmaster.dust.blocks.BlockRegistry.*;
import static com.mowmaster.dust.items.ItemRegistry.*;

public class CraftingRecipes
{
    public static void CraftingRecipes() {

        ItemStack oldHelm = new ItemStack(ItemArmorAndToolsRegistry.crystalHelmet);
        ItemStack enchantedHelmRed = oldHelm.copy();
        ItemStack enchantedHelmBlue = oldHelm.copy();
        ItemStack enchantedHelmYellow = oldHelm.copy();
        ItemStack enchantedHelmPurple = oldHelm.copy();
        ItemStack enchantedHelmGreen = oldHelm.copy();
        ItemStack enchantedHelmOrange = oldHelm.copy();
        ItemStack enchantedHelmWhite = oldHelm.copy();
        ItemStack enchantedHelmBlack = oldHelm.copy();
        enchantedHelmRed.addEnchantment(Enchantment.getEnchantmentByID(1),1);//Fire Prot
        enchantedHelmBlue.addEnchantment(Enchantment.getEnchantmentByID(6),1);//Aqua Aff
        enchantedHelmYellow.addEnchantment(Enchantment.getEnchantmentByID(5),1);//Resp
        enchantedHelmPurple.addEnchantment(Enchantment.getEnchantmentByID(4),1);//Proj Prot
        enchantedHelmGreen.addEnchantment(Enchantment.getEnchantmentByID(7),1);//Thorns
        enchantedHelmOrange.addEnchantment(Enchantment.getEnchantmentByID(3),1);//Blast Prot
        enchantedHelmWhite.addEnchantment(Enchantment.getEnchantmentByID(34),1);//Unbreaking
        enchantedHelmBlack.addEnchantment(Enchantment.getEnchantmentByID(0),1);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetRed"),null,enchantedHelmRed,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,0), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetBlue"),null,enchantedHelmBlue,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,1), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetYellow"),null,enchantedHelmYellow,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,2), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetPurple"),null,enchantedHelmPurple,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,3), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetGreen"),null,enchantedHelmGreen,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,4), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetOrange"),null,enchantedHelmOrange,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,5), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetWhite"),null,enchantedHelmWhite,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,6), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetBlack"),null,enchantedHelmBlack,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,7), 'A', oldHelm});

        ItemStack enchantedHelmRed2 = oldHelm.copy();
        ItemStack enchantedHelmBlue2 = oldHelm.copy();
        ItemStack enchantedHelmYellow2 = oldHelm.copy();
        ItemStack enchantedHelmPurple2 = oldHelm.copy();
        ItemStack enchantedHelmGreen2 = oldHelm.copy();
        ItemStack enchantedHelmOrange2 = oldHelm.copy();
        ItemStack enchantedHelmWhite2 = oldHelm.copy();
        ItemStack enchantedHelmBlack2 = oldHelm.copy();
        enchantedHelmRed2.addEnchantment(Enchantment.getEnchantmentByID(1),2);//Fire Prot
        enchantedHelmBlue2.addEnchantment(Enchantment.getEnchantmentByID(6),2);//Aqua Aff
        enchantedHelmYellow2.addEnchantment(Enchantment.getEnchantmentByID(5),2);//Resp
        enchantedHelmPurple2.addEnchantment(Enchantment.getEnchantmentByID(4),2);//Proj Prot
        enchantedHelmGreen2.addEnchantment(Enchantment.getEnchantmentByID(7),2);//Thorns
        enchantedHelmOrange2.addEnchantment(Enchantment.getEnchantmentByID(3),2);//Blast Prot
        enchantedHelmWhite2.addEnchantment(Enchantment.getEnchantmentByID(34),2);//Unbreaking
        enchantedHelmBlack2.addEnchantment(Enchantment.getEnchantmentByID(0),2);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetRed2"),null,enchantedHelmRed2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.redstone),'D', new ItemStack(ItemRegistry.dust,1,0), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetBlue2"),null,enchantedHelmBlue2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.bluestone),'D', new ItemStack(ItemRegistry.dust,1,1), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetYellow2"),null,enchantedHelmYellow2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.yellowstone),'D', new ItemStack(ItemRegistry.dust,1,2), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetPurple2"),null,enchantedHelmPurple2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.purplestone),'D', new ItemStack(ItemRegistry.dust,1,3), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetGreen2"),null,enchantedHelmGreen2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.greenstone),'D', new ItemStack(ItemRegistry.dust,1,4), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetOrange2"),null,enchantedHelmOrange2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.orangestone),'D', new ItemStack(ItemRegistry.dust,1,5), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetWhite2"),null,enchantedHelmWhite2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.whitestone),'D', new ItemStack(ItemRegistry.dust,1,6), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetBlack2"),null,enchantedHelmBlack2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.blackstone),'D', new ItemStack(ItemRegistry.dust,1,7), 'A', oldHelm});

        ItemStack enchantedHelmRed3 = oldHelm.copy();
        ItemStack enchantedHelmBlue3 = oldHelm.copy();
        ItemStack enchantedHelmYellow3 = oldHelm.copy();
        ItemStack enchantedHelmPurple3 = oldHelm.copy();
        ItemStack enchantedHelmGreen3 = oldHelm.copy();
        ItemStack enchantedHelmOrange3 = oldHelm.copy();
        ItemStack enchantedHelmWhite3 = oldHelm.copy();
        ItemStack enchantedHelmBlack3 = oldHelm.copy();
        enchantedHelmRed3.addEnchantment(Enchantment.getEnchantmentByID(1),3);//Fire Prot
        enchantedHelmBlue3.addEnchantment(Enchantment.getEnchantmentByID(6),3);//Aqua Aff
        enchantedHelmYellow3.addEnchantment(Enchantment.getEnchantmentByID(5),3);//Resp
        enchantedHelmPurple3.addEnchantment(Enchantment.getEnchantmentByID(4),3);//Proj Prot
        enchantedHelmGreen3.addEnchantment(Enchantment.getEnchantmentByID(7),3);//Thorns
        enchantedHelmOrange3.addEnchantment(Enchantment.getEnchantmentByID(3),3);//Blast Prot
        enchantedHelmWhite3.addEnchantment(Enchantment.getEnchantmentByID(34),3);//Unbreaking
        enchantedHelmBlack3.addEnchantment(Enchantment.getEnchantmentByID(0),3);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetRed3"),null,enchantedHelmRed3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,0),'D', new ItemStack(ItemRegistry.dust,1,0), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetBlue3"),null,enchantedHelmBlue3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,1),'D', new ItemStack(ItemRegistry.dust,1,1), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetYellow3"),null,enchantedHelmYellow3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,2),'D', new ItemStack(ItemRegistry.dust,1,2), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetPurple3"),null,enchantedHelmPurple3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,3),'D', new ItemStack(ItemRegistry.dust,1,3), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetGreen3"),null,enchantedHelmGreen3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,4),'D', new ItemStack(ItemRegistry.dust,1,4), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetOrange3"),null,enchantedHelmOrange3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,5),'D', new ItemStack(ItemRegistry.dust,1,5), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetWhite3"),null,enchantedHelmWhite3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,6),'D', new ItemStack(ItemRegistry.dust,1,6), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetBlack3"),null,enchantedHelmBlack3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,7),'D', new ItemStack(ItemRegistry.dust,1,7), 'A', oldHelm});

        ItemStack enchantedHelmRed4 = oldHelm.copy();
        ItemStack enchantedHelmBlue4 = oldHelm.copy();
        ItemStack enchantedHelmYellow4 = oldHelm.copy();
        ItemStack enchantedHelmPurple4 = oldHelm.copy();
        ItemStack enchantedHelmGreen4 = oldHelm.copy();
        ItemStack enchantedHelmOrange4 = oldHelm.copy();
        ItemStack enchantedHelmWhite4 = oldHelm.copy();
        ItemStack enchantedHelmBlack4 = oldHelm.copy();
        enchantedHelmRed4.addEnchantment(Enchantment.getEnchantmentByID(1),4);//Fire Prot
        enchantedHelmBlue4.addEnchantment(Enchantment.getEnchantmentByID(6),4);//Aqua Aff
        enchantedHelmYellow4.addEnchantment(Enchantment.getEnchantmentByID(5),4);//Resp
        enchantedHelmPurple4.addEnchantment(Enchantment.getEnchantmentByID(4),4);//Proj Prot
        enchantedHelmGreen4.addEnchantment(Enchantment.getEnchantmentByID(7),4);//Thorns
        enchantedHelmOrange4.addEnchantment(Enchantment.getEnchantmentByID(3),4);//Blast Prot
        enchantedHelmWhite4.addEnchantment(Enchantment.getEnchantmentByID(70),1);//Mending
        enchantedHelmBlack4.addEnchantment(Enchantment.getEnchantmentByID(0),4);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetRed4"),null,enchantedHelmRed4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,0), 'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetBlue4"),null,enchantedHelmBlue4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,1),'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetYellow4"),null,enchantedHelmYellow4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,2),'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetPurple4"),null,enchantedHelmPurple4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,3),'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetGreen4"),null,enchantedHelmGreen4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,4),'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetOrange4"),null,enchantedHelmOrange4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,5),'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetWhite4"),null,enchantedHelmWhite4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,6),'A', oldHelm});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalHelmetBlack4"),null,enchantedHelmBlack4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,7),'A', oldHelm});


        ItemStack oldChest = new ItemStack(ItemArmorAndToolsRegistry.crystalChestplate);
        ItemStack enchantedChestRed = oldChest.copy();
        ItemStack enchantedChestBlue = oldChest.copy();
        ItemStack enchantedChestYellow = oldChest.copy();
        ItemStack enchantedChestPurple = oldChest.copy();
        ItemStack enchantedChestGreen = oldChest.copy();
        ItemStack enchantedChestOrange = oldChest.copy();
        ItemStack enchantedChestWhite = oldChest.copy();
        ItemStack enchantedChestBlack = oldChest.copy();
        enchantedChestRed.addEnchantment(Enchantment.getEnchantmentByID(1),1);//Fire Prot
        enchantedChestBlue.addEnchantment(EnchantmentRegistry.enchantLastResort,1);
        enchantedChestYellow.addEnchantment(EnchantmentRegistry.enchantSteadfast,1);
        enchantedChestPurple.addEnchantment(Enchantment.getEnchantmentByID(4),1);//Proj Prot
        enchantedChestGreen.addEnchantment(Enchantment.getEnchantmentByID(7),1);//Thorns
        enchantedChestOrange.addEnchantment(Enchantment.getEnchantmentByID(3),1);//Blast Prot
        enchantedChestWhite.addEnchantment(Enchantment.getEnchantmentByID(34),1);//Unbreaking
        enchantedChestBlack.addEnchantment(Enchantment.getEnchantmentByID(0),1);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateRed"),null,enchantedChestRed,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,0), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateBlue"),null,enchantedChestBlue,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,1), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateYellow"),null,enchantedChestYellow,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,2), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplatePurple"),null,enchantedChestPurple,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,3), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateGreen"),null,enchantedChestGreen,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,4), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateOrange"),null,enchantedChestOrange,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,5), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateWhite"),null,enchantedChestWhite,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,6), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateBlack"),null,enchantedChestBlack,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,7), 'A', oldChest});

        ItemStack enchantedChestRed2 = oldChest.copy();
        ItemStack enchantedChestBlue2 = oldChest.copy();
        ItemStack enchantedChestYellow2 = oldChest.copy();
        ItemStack enchantedChestPurple2 = oldChest.copy();
        ItemStack enchantedChestGreen2 = oldChest.copy();
        ItemStack enchantedChestOrange2 = oldChest.copy();
        ItemStack enchantedChestWhite2 = oldChest.copy();
        ItemStack enchantedChestBlack2 = oldChest.copy();
        enchantedChestRed2.addEnchantment(Enchantment.getEnchantmentByID(1),2);//Fire Prot
        enchantedChestBlue2.addEnchantment(EnchantmentRegistry.enchantLastResort,2);
        enchantedChestYellow2.addEnchantment(EnchantmentRegistry.enchantSteadfast,2);
        enchantedChestPurple2.addEnchantment(Enchantment.getEnchantmentByID(4),2);//Proj Prot
        enchantedChestGreen2.addEnchantment(Enchantment.getEnchantmentByID(7),2);//Thorns
        enchantedChestOrange2.addEnchantment(Enchantment.getEnchantmentByID(3),2);//Blast Prot
        enchantedChestWhite2.addEnchantment(Enchantment.getEnchantmentByID(34),2);//Unbreaking
        enchantedChestBlack2.addEnchantment(Enchantment.getEnchantmentByID(0),2);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateRed2"),null,enchantedChestRed2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.redstone),'D', new ItemStack(ItemRegistry.dust,1,0), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateBlue2"),null,enchantedChestBlue2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.bluestone),'D', new ItemStack(ItemRegistry.dust,1,1), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateYellow2"),null,enchantedChestYellow2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.yellowstone),'D', new ItemStack(ItemRegistry.dust,1,2), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplatePurple2"),null,enchantedChestPurple2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.purplestone),'D', new ItemStack(ItemRegistry.dust,1,3), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateGreen2"),null,enchantedChestGreen2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.greenstone),'D', new ItemStack(ItemRegistry.dust,1,4), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateOrange2"),null,enchantedChestOrange2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.orangestone),'D', new ItemStack(ItemRegistry.dust,1,5), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateWhite2"),null,enchantedChestWhite2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.whitestone),'D', new ItemStack(ItemRegistry.dust,1,6), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateBlack2"),null,enchantedChestBlack2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.blackstone),'D', new ItemStack(ItemRegistry.dust,1,7), 'A', oldChest});

        ItemStack enchantedChestRed3 = oldChest.copy();
        ItemStack enchantedChestBlue3 = oldChest.copy();
        ItemStack enchantedChestYellow3 = oldChest.copy();
        ItemStack enchantedChestPurple3 = oldChest.copy();
        ItemStack enchantedChestGreen3 = oldChest.copy();
        ItemStack enchantedChestOrange3 = oldChest.copy();
        ItemStack enchantedChestWhite3 = oldChest.copy();
        ItemStack enchantedChestBlack3 = oldChest.copy();
        enchantedChestRed3.addEnchantment(Enchantment.getEnchantmentByID(1),3);//Fire Prot
        enchantedChestBlue3.addEnchantment(EnchantmentRegistry.enchantLastResort,3);
        enchantedChestYellow3.addEnchantment(EnchantmentRegistry.enchantSteadfast,3);
        enchantedChestPurple3.addEnchantment(Enchantment.getEnchantmentByID(4),3);//Proj Prot
        enchantedChestGreen3.addEnchantment(Enchantment.getEnchantmentByID(7),3);//Thorns
        enchantedChestOrange3.addEnchantment(Enchantment.getEnchantmentByID(3),3);//Blast Prot
        enchantedChestWhite3.addEnchantment(Enchantment.getEnchantmentByID(34),3);//Unbreaking
        enchantedChestBlack3.addEnchantment(Enchantment.getEnchantmentByID(0),3);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateRed3"),null,enchantedChestRed3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,0),'D', new ItemStack(ItemRegistry.dust,1,0), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateBlue3"),null,enchantedChestBlue3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,1),'D', new ItemStack(ItemRegistry.dust,1,1), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateYellow3"),null,enchantedChestYellow3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,2),'D', new ItemStack(ItemRegistry.dust,1,2), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplatePurple3"),null,enchantedChestPurple3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,3),'D', new ItemStack(ItemRegistry.dust,1,3), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateGreen3"),null,enchantedChestGreen3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,4),'D', new ItemStack(ItemRegistry.dust,1,4), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateOrange3"),null,enchantedChestOrange3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,5),'D', new ItemStack(ItemRegistry.dust,1,5), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateWhite3"),null,enchantedChestWhite3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,6),'D', new ItemStack(ItemRegistry.dust,1,6), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateBlack3"),null,enchantedChestBlack3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,7),'D', new ItemStack(ItemRegistry.dust,1,7), 'A', oldChest});

        ItemStack enchantedChestRed4 = oldChest.copy();
        ItemStack enchantedChestBlue4 = oldChest.copy();
        ItemStack enchantedChestYellow4 = oldChest.copy();
        ItemStack enchantedChestPurple4 = oldChest.copy();
        ItemStack enchantedChestGreen4 = oldChest.copy();
        ItemStack enchantedChestOrange4 = oldChest.copy();
        ItemStack enchantedChestWhite4 = oldChest.copy();
        ItemStack enchantedChestBlack4 = oldChest.copy();
        enchantedChestRed4.addEnchantment(Enchantment.getEnchantmentByID(1),4);//Fire Prot
        enchantedChestBlue4.addEnchantment(EnchantmentRegistry.enchantLastResort,4);
        enchantedChestYellow4.addEnchantment(EnchantmentRegistry.enchantSteadfast,4);
        enchantedChestPurple4.addEnchantment(Enchantment.getEnchantmentByID(4),4);//Proj Prot
        enchantedChestGreen4.addEnchantment(Enchantment.getEnchantmentByID(7),4);//Thorns
        enchantedChestOrange4.addEnchantment(Enchantment.getEnchantmentByID(3),4);//Blast Prot
        enchantedChestWhite4.addEnchantment(Enchantment.getEnchantmentByID(70),1);//Mending
        enchantedChestBlack4.addEnchantment(Enchantment.getEnchantmentByID(0),4);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateRed4"),null,enchantedChestRed4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,0), 'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateBlue4"),null,enchantedChestBlue4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,1),'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateYellow4"),null,enchantedChestYellow4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,2),'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplatePurple4"),null,enchantedChestPurple4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,3),'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateGreen4"),null,enchantedChestGreen4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,4),'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateOrange4"),null,enchantedChestOrange4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,5),'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateWhite4"),null,enchantedChestWhite4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,6),'A', oldChest});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalChestplateBlack4"),null,enchantedChestBlack4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,7),'A', oldChest});


        ItemStack oldLegs = new ItemStack(ItemArmorAndToolsRegistry.crystalLeggings);
        ItemStack enchantedLegsRed = oldLegs.copy();
        ItemStack enchantedLegsBlue = oldLegs.copy();
        ItemStack enchantedLegsYellow = oldLegs.copy();
        ItemStack enchantedLegsPurple = oldLegs.copy();
        ItemStack enchantedLegsGreen = oldLegs.copy();
        ItemStack enchantedLegsOrange = oldLegs.copy();
        ItemStack enchantedLegsWhite = oldLegs.copy();
        ItemStack enchantedLegsBlack = oldLegs.copy();
        enchantedLegsRed.addEnchantment(Enchantment.getEnchantmentByID(1),1);//Fire Prot
        enchantedLegsBlue.addEnchantment(EnchantmentRegistry.enchantmentStepAssist,1);
        enchantedLegsYellow.addEnchantment(EnchantmentRegistry.enchantmentQuickPace,1);
        enchantedLegsPurple.addEnchantment(Enchantment.getEnchantmentByID(4),1);//Proj Prot
        enchantedLegsGreen.addEnchantment(Enchantment.getEnchantmentByID(7),1);//Thorns
        enchantedLegsOrange.addEnchantment(Enchantment.getEnchantmentByID(3),1);//Blast Prot
        enchantedLegsWhite.addEnchantment(Enchantment.getEnchantmentByID(34),1);//Unbreaking
        enchantedLegsBlack.addEnchantment(Enchantment.getEnchantmentByID(0),1);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsRed"),null,enchantedLegsRed,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,0), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsBlue"),null,enchantedLegsBlue,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,1), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsYellow"),null,enchantedLegsYellow,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,2), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsPurple"),null,enchantedLegsPurple,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,3), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsGreen"),null,enchantedLegsGreen,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,4), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsOrange"),null,enchantedLegsOrange,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,5), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsWhite"),null,enchantedLegsWhite,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,6), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsBlack"),null,enchantedLegsBlack,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,7), 'A', oldLegs});

        ItemStack enchantedLegsRed2 = oldLegs.copy();
        ItemStack enchantedLegsBlue2 = oldLegs.copy();
        ItemStack enchantedLegsYellow2 = oldLegs.copy();
        ItemStack enchantedLegsPurple2 = oldLegs.copy();
        ItemStack enchantedLegsGreen2 = oldLegs.copy();
        ItemStack enchantedLegsOrange2 = oldLegs.copy();
        ItemStack enchantedLegsWhite2 = oldLegs.copy();
        ItemStack enchantedLegsBlack2 = oldLegs.copy();
        enchantedLegsRed2.addEnchantment(Enchantment.getEnchantmentByID(1),2);//Fire Prot
        enchantedLegsBlue2.addEnchantment(EnchantmentRegistry.enchantmentStepAssist,2);
        enchantedLegsYellow2.addEnchantment(EnchantmentRegistry.enchantmentQuickPace,2);
        enchantedLegsPurple2.addEnchantment(Enchantment.getEnchantmentByID(4),2);//Proj Prot
        enchantedLegsGreen2.addEnchantment(Enchantment.getEnchantmentByID(7),2);//Thorns
        enchantedLegsOrange2.addEnchantment(Enchantment.getEnchantmentByID(3),2);//Blast Prot
        enchantedLegsWhite2.addEnchantment(Enchantment.getEnchantmentByID(34),2);//Unbreaking
        enchantedLegsBlack2.addEnchantment(Enchantment.getEnchantmentByID(0),2);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsRed2"),null,enchantedLegsRed2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.redstone),'D', new ItemStack(ItemRegistry.dust,1,0), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsBlue2"),null,enchantedLegsBlue2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.bluestone),'D', new ItemStack(ItemRegistry.dust,1,1), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsYellow2"),null,enchantedLegsYellow2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.yellowstone),'D', new ItemStack(ItemRegistry.dust,1,2), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsPurple2"),null,enchantedLegsPurple2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.purplestone),'D', new ItemStack(ItemRegistry.dust,1,3), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsGreen2"),null,enchantedLegsGreen2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.greenstone),'D', new ItemStack(ItemRegistry.dust,1,4), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsOrange2"),null,enchantedLegsOrange2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.orangestone),'D', new ItemStack(ItemRegistry.dust,1,5), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsWhite2"),null,enchantedLegsWhite2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.whitestone),'D', new ItemStack(ItemRegistry.dust,1,6), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsBlack2"),null,enchantedLegsBlack2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.blackstone),'D', new ItemStack(ItemRegistry.dust,1,7), 'A', oldLegs});

        ItemStack enchantedLegsRed3 = oldLegs.copy();
        ItemStack enchantedLegsBlue3 = oldLegs.copy();
        ItemStack enchantedLegsYellow3 = oldLegs.copy();
        ItemStack enchantedLegsPurple3 = oldLegs.copy();
        ItemStack enchantedLegsGreen3 = oldLegs.copy();
        ItemStack enchantedLegsOrange3 = oldLegs.copy();
        ItemStack enchantedLegsWhite3 = oldLegs.copy();
        ItemStack enchantedLegsBlack3 = oldLegs.copy();
        enchantedLegsRed3.addEnchantment(Enchantment.getEnchantmentByID(1),3);//Fire Prot
        enchantedLegsBlue3.addEnchantment(EnchantmentRegistry.enchantmentStepAssist,3);
        enchantedLegsYellow3.addEnchantment(EnchantmentRegistry.enchantmentQuickPace,3);
        enchantedLegsPurple3.addEnchantment(Enchantment.getEnchantmentByID(4),3);//Proj Prot
        enchantedLegsGreen3.addEnchantment(Enchantment.getEnchantmentByID(7),3);//Thorns
        enchantedLegsOrange3.addEnchantment(Enchantment.getEnchantmentByID(3),3);//Blast Prot
        enchantedLegsWhite3.addEnchantment(Enchantment.getEnchantmentByID(34),3);//Unbreaking
        enchantedLegsBlack3.addEnchantment(Enchantment.getEnchantmentByID(0),3);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsRed3"),null,enchantedLegsRed3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,0),'D', new ItemStack(ItemRegistry.dust,1,0), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsBlue3"),null,enchantedLegsBlue3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,1),'D', new ItemStack(ItemRegistry.dust,1,1), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsYellow3"),null,enchantedLegsYellow3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,2),'D', new ItemStack(ItemRegistry.dust,1,2), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsPurple3"),null,enchantedLegsPurple3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,3),'D', new ItemStack(ItemRegistry.dust,1,3), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsGreen3"),null,enchantedLegsGreen3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,4),'D', new ItemStack(ItemRegistry.dust,1,4), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsOrange3"),null,enchantedLegsOrange3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,5),'D', new ItemStack(ItemRegistry.dust,1,5), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsWhite3"),null,enchantedLegsWhite3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,6),'D', new ItemStack(ItemRegistry.dust,1,6), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsBlack3"),null,enchantedLegsBlack3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,7),'D', new ItemStack(ItemRegistry.dust,1,7), 'A', oldLegs});

        ItemStack enchantedLegsRed4 = oldLegs.copy();
        ItemStack enchantedLegsBlue4 = oldLegs.copy();
        ItemStack enchantedLegsYellow4 = oldLegs.copy();
        ItemStack enchantedLegsPurple4 = oldLegs.copy();
        ItemStack enchantedLegsGreen4 = oldLegs.copy();
        ItemStack enchantedLegsOrange4 = oldLegs.copy();
        ItemStack enchantedLegsWhite4 = oldLegs.copy();
        ItemStack enchantedLegsBlack4 = oldLegs.copy();
        enchantedLegsRed4.addEnchantment(Enchantment.getEnchantmentByID(1),4);//Fire Prot
        enchantedLegsBlue4.addEnchantment(EnchantmentRegistry.enchantmentStepAssist,4);
        enchantedLegsYellow4.addEnchantment(EnchantmentRegistry.enchantmentQuickPace,4);
        enchantedLegsPurple4.addEnchantment(Enchantment.getEnchantmentByID(4),4);//Proj Prot
        enchantedLegsGreen4.addEnchantment(Enchantment.getEnchantmentByID(7),4);//Thorns
        enchantedLegsOrange4.addEnchantment(Enchantment.getEnchantmentByID(3),4);//Blast Prot
        enchantedLegsWhite4.addEnchantment(Enchantment.getEnchantmentByID(70),1);//Mending
        enchantedLegsBlack4.addEnchantment(Enchantment.getEnchantmentByID(0),4);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsRed4"),null,enchantedLegsRed4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,0), 'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsBlue4"),null,enchantedLegsBlue4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,1),'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsYellow4"),null,enchantedLegsYellow4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,2),'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsPurple4"),null,enchantedLegsPurple4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,3),'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsGreen4"),null,enchantedLegsGreen4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,4),'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsOrange4"),null,enchantedLegsOrange4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,5),'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsWhite4"),null,enchantedLegsWhite4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,6),'A', oldLegs});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalLeggingsBlack4"),null,enchantedLegsBlack4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,7),'A', oldLegs});


        ItemStack oldBoots = new ItemStack(ItemArmorAndToolsRegistry.crystalBoots);
        ItemStack enchantedBootsRed = oldBoots.copy();
        ItemStack enchantedBootsBlue = oldBoots.copy();
        ItemStack enchantedBootsYellow = oldBoots.copy();
        ItemStack enchantedBootsPurple = oldBoots.copy();
        ItemStack enchantedBootsGreen = oldBoots.copy();
        ItemStack enchantedBootsOrange = oldBoots.copy();
        ItemStack enchantedBootsWhite = oldBoots.copy();
        ItemStack enchantedBootsBlack = oldBoots.copy();
        enchantedBootsRed.addEnchantment(Enchantment.getEnchantmentByID(1),1);//Fire Prot
        enchantedBootsBlue.addEnchantment(Enchantment.getEnchantmentByID(8),1);//Strider
        enchantedBootsYellow.addEnchantment(Enchantment.getEnchantmentByID(2),1);//FeatherFall
        enchantedBootsPurple.addEnchantment(Enchantment.getEnchantmentByID(4),1);//Proj Prot
        enchantedBootsGreen.addEnchantment(Enchantment.getEnchantmentByID(7),1);//Thorns
        enchantedBootsOrange.addEnchantment(Enchantment.getEnchantmentByID(3),1);//Blast Prot
        enchantedBootsWhite.addEnchantment(Enchantment.getEnchantmentByID(34),1);//Unbreaking
        enchantedBootsBlack.addEnchantment(Enchantment.getEnchantmentByID(0),1);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsRed"),null,enchantedBootsRed,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,0), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsBlue"),null,enchantedBootsBlue,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,1), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsYellow"),null,enchantedBootsYellow,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,2), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsPurple"),null,enchantedBootsPurple,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,3), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsGreen"),null,enchantedBootsGreen,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,4), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsOrange"),null,enchantedBootsOrange,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,5), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsWhite"),null,enchantedBootsWhite,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,6), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsBlack"),null,enchantedBootsBlack,new Object[]{"DDD", "DAD", "DDD", 'D', new ItemStack(ItemRegistry.dust,1,7), 'A', oldBoots});

        ItemStack enchantedBootsRed2 = oldBoots.copy();
        ItemStack enchantedBootsBlue2 = oldBoots.copy();
        ItemStack enchantedBootsYellow2 = oldBoots.copy();
        ItemStack enchantedBootsPurple2 = oldBoots.copy();
        ItemStack enchantedBootsGreen2 = oldBoots.copy();
        ItemStack enchantedBootsOrange2 = oldBoots.copy();
        ItemStack enchantedBootsWhite2 = oldBoots.copy();
        ItemStack enchantedBootsBlack2 = oldBoots.copy();
        enchantedBootsRed2.addEnchantment(Enchantment.getEnchantmentByID(1),2);//Fire Prot
        enchantedBootsBlue2.addEnchantment(Enchantment.getEnchantmentByID(8),2);//Strider
        enchantedBootsYellow2.addEnchantment(Enchantment.getEnchantmentByID(2),2);//FeatherFall
        enchantedBootsPurple2.addEnchantment(Enchantment.getEnchantmentByID(4),2);//Proj Prot
        enchantedBootsGreen2.addEnchantment(Enchantment.getEnchantmentByID(7),2);//Thorns
        enchantedBootsOrange2.addEnchantment(Enchantment.getEnchantmentByID(3),2);//Blast Prot
        enchantedBootsWhite2.addEnchantment(Enchantment.getEnchantmentByID(34),2);//Unbreaking
        enchantedBootsBlack2.addEnchantment(Enchantment.getEnchantmentByID(0),2);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsRed2"),null,enchantedBootsRed2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.redstone),'D', new ItemStack(ItemRegistry.dust,1,0), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsBlue2"),null,enchantedBootsBlue2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.bluestone),'D', new ItemStack(ItemRegistry.dust,1,1), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsYellow2"),null,enchantedBootsYellow2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.yellowstone),'D', new ItemStack(ItemRegistry.dust,1,2), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsPurple2"),null,enchantedBootsPurple2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.purplestone),'D', new ItemStack(ItemRegistry.dust,1,3), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsGreen2"),null,enchantedBootsGreen2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.greenstone),'D', new ItemStack(ItemRegistry.dust,1,4), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsOrange2"),null,enchantedBootsOrange2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.orangestone),'D', new ItemStack(ItemRegistry.dust,1,5), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsWhite2"),null,enchantedBootsWhite2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.whitestone),'D', new ItemStack(ItemRegistry.dust,1,6), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsBlack2"),null,enchantedBootsBlack2,new Object[]{"BDB", "DAD", "BDB", 'B',new ItemStack(BlockRegistry.blackstone),'D', new ItemStack(ItemRegistry.dust,1,7), 'A', oldBoots});

        ItemStack enchantedBootsRed3 = oldBoots.copy();
        ItemStack enchantedBootsBlue3 = oldBoots.copy();
        ItemStack enchantedBootsYellow3 = oldBoots.copy();
        ItemStack enchantedBootsPurple3 = oldBoots.copy();
        ItemStack enchantedBootsGreen3 = oldBoots.copy();
        ItemStack enchantedBootsOrange3 = oldBoots.copy();
        ItemStack enchantedBootsWhite3 = oldBoots.copy();
        ItemStack enchantedBootsBlack3 = oldBoots.copy();
        enchantedBootsRed3.addEnchantment(Enchantment.getEnchantmentByID(1),3);//Fire Prot
        enchantedBootsBlue3.addEnchantment(Enchantment.getEnchantmentByID(8),3);//Strider
        enchantedBootsYellow3.addEnchantment(Enchantment.getEnchantmentByID(2),3);//FeatherFall
        enchantedBootsPurple3.addEnchantment(Enchantment.getEnchantmentByID(4),3);//Proj Prot
        enchantedBootsGreen3.addEnchantment(Enchantment.getEnchantmentByID(7),3);//Thorns
        enchantedBootsOrange3.addEnchantment(Enchantment.getEnchantmentByID(3),3);//Blast Prot
        enchantedBootsWhite3.addEnchantment(Enchantment.getEnchantmentByID(34),3);//Unbreaking
        enchantedBootsBlack3.addEnchantment(Enchantment.getEnchantmentByID(0),3);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsRed3"),null,enchantedBootsRed3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,0),'D', new ItemStack(ItemRegistry.dust,1,0), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsBlue3"),null,enchantedBootsBlue3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,1),'D', new ItemStack(ItemRegistry.dust,1,1), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsYellow3"),null,enchantedBootsYellow3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,2),'D', new ItemStack(ItemRegistry.dust,1,2), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsPurple3"),null,enchantedBootsPurple3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,3),'D', new ItemStack(ItemRegistry.dust,1,3), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsGreen3"),null,enchantedBootsGreen3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,4),'D', new ItemStack(ItemRegistry.dust,1,4), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsOrange3"),null,enchantedBootsOrange3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,5),'D', new ItemStack(ItemRegistry.dust,1,5), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsWhite3"),null,enchantedBootsWhite3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,6),'D', new ItemStack(ItemRegistry.dust,1,6), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsBlack3"),null,enchantedBootsBlack3,new Object[]{"CDC", "DAD", "CDC", 'C',new ItemStack(ItemRegistry.crystal,1,7),'D', new ItemStack(ItemRegistry.dust,1,7), 'A', oldBoots});

        ItemStack enchantedBootsRed4 = oldBoots.copy();
        ItemStack enchantedBootsBlue4 = oldBoots.copy();
        ItemStack enchantedBootsYellow4 = oldBoots.copy();
        ItemStack enchantedBootsPurple4 = oldBoots.copy();
        ItemStack enchantedBootsGreen4 = oldBoots.copy();
        ItemStack enchantedBootsOrange4 = oldBoots.copy();
        ItemStack enchantedBootsWhite4 = oldBoots.copy();
        ItemStack enchantedBootsBlack4 = oldBoots.copy();
        enchantedBootsRed4.addEnchantment(Enchantment.getEnchantmentByID(1),4);//Fire Prot
        enchantedBootsBlue4.addEnchantment(Enchantment.getEnchantmentByID(8),4);//Strider
        enchantedBootsYellow4.addEnchantment(Enchantment.getEnchantmentByID(2),4);//FeatherFall
        enchantedBootsPurple4.addEnchantment(Enchantment.getEnchantmentByID(4),4);//Proj Prot
        enchantedBootsGreen4.addEnchantment(Enchantment.getEnchantmentByID(7),4);//Thorns
        enchantedBootsOrange4.addEnchantment(Enchantment.getEnchantmentByID(3),4);//Blast Prot
        enchantedBootsWhite4.addEnchantment(Enchantment.getEnchantmentByID(70),1);//Mending
        enchantedBootsBlack4.addEnchantment(Enchantment.getEnchantmentByID(0),4);//Prot
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsRed4"),null,enchantedBootsRed4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,0), 'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsBlue4"),null,enchantedBootsBlue4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,1),'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsYellow4"),null,enchantedBootsYellow4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,2),'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsPurple4"),null,enchantedBootsPurple4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,3),'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsGreen4"),null,enchantedBootsGreen4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,4),'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsOrange4"),null,enchantedBootsOrange4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,5),'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsWhite4"),null,enchantedBootsWhite4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,6),'A', oldBoots});
        GameRegistry.addShapedRecipe(new ResourceLocation("crystalBootsBlack4"),null,enchantedBootsBlack4,new Object[]{"CCC", "CAC", "CCC", 'C',new ItemStack(ItemRegistry.crystal,1,7),'A', oldBoots});


    }
}
