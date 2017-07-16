package com.mowmaster.dust.misc;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AchievementHandler
{
    /*
    private static List<Achievement> achievements = new ArrayList<Achievement>();

    public static Achievement achievementCrystal = createAchievement("minecrystal",0,0, ItemRegistry.crystal);
    public static Achievement achievementDust = createAchievement("getdust",0,1, ItemRegistry.dust);
    public static Achievement achievementSapling = createAchievement("plantatree",1,1, BlockRegistry.saplingpurple);
    public static Achievement achievementFindMachine = createAchievement("machineblock",-2,-2, ItemRegistry.crushingcomponents);
    public static Achievement achievementLoot = createAchievement("foundloot",-2,-1, BlockRegistry.pot1);


    public static Achievement achievementScrollA = createAchievement("scrolla",-2,1, ItemRegistry.scroll);
    public static Achievement achievementScrollB = createAchievement("scrollb",-2,2, ItemRegistry.scroll);
    public static Achievement achievementScrollC = createAchievement("scrollc",-2,3, ItemRegistry.scroll);
    public static Achievement achievementScrollD = createAchievement("scrolld",-2,4, ItemRegistry.scroll);
    public static Achievement achievementScrollE = createAchievement("scrolle",-2,5, ItemRegistry.scroll);
    public static Achievement achievementScrollF = createAchievement("scrollf",-2,6, ItemRegistry.scroll);
    public static Achievement achievementScrollG = createAchievement("scrollg",-2,7, ItemRegistry.scroll);
    public static Achievement achievementScrollH = createAchievement("scrollh",-2,8, ItemRegistry.scroll);
    public static Achievement achievementScrollI = createAchievement("scrolli",-2,9, ItemRegistry.scroll);
    public static Achievement achievementScrollJ = createAchievement("scrollj",-3,1, ItemRegistry.scroll);
    public static Achievement achievementScrollK = createAchievement("scrollk",-3,2, ItemRegistry.scroll);
    public static Achievement achievementScrollL = createAchievement("scrolll",-3,3, ItemRegistry.scroll);
    public static Achievement achievementScrollM = createAchievement("scrollm",-3,4, ItemRegistry.scroll);
    public static Achievement achievementScrollN = createAchievement("scrolln",-3,5, ItemRegistry.scroll);
    public static Achievement achievementScrollO = createAchievement("scrollo",-3,6, ItemRegistry.scroll);
    public static Achievement achievementScrollP = createAchievement("scrollp",-3,7, ItemRegistry.scroll);
    public static Achievement achievementScrollQ = createAchievement("scrollq",-3,8, ItemRegistry.scroll);
    public static Achievement achievementScrollR = createAchievement("scrollr",-3,9, ItemRegistry.scroll);
    public static Achievement achievementScrollS = createAchievement("scrolls",-4,1, ItemRegistry.scroll);
    public static Achievement achievementScrollT = createAchievement("scrollt",-4,2, ItemRegistry.scroll);
    public static Achievement achievementScrollU = createAchievement("scrollu",-4,3, ItemRegistry.scroll);
    public static Achievement achievementScrollV = createAchievement("scrollv",-4,4, ItemRegistry.scroll);
    public static Achievement achievementScrollW = createAchievement("scrollw",-4,5, ItemRegistry.scroll);
    public static Achievement achievementScrollX = createAchievement("scrollx",-4,6, ItemRegistry.scroll);
    public static Achievement achievementScrollY = createAchievement("scrolly",-4,7, ItemRegistry.scroll);
    public static Achievement achievementScrollZ = createAchievement("scrollz",-4,8, ItemRegistry.scroll);
    public static Achievement achievementScrollFirst = createAchievement("scrollfirst",-2,0, ItemRegistry.scroll);//First Scroll
    public static Achievement achievementScrollVowels = createAchievement("scrollvowels",-3,0, ItemRegistry.scroll);//First Scroll
    public static Achievement achievementScrollAll = createAchievement("scrollall",-5,0, Items.BOOK);//If all scrolls obtained







    public static void registerAchievement()
    {
        Achievement[] achievementArray = new Achievement[achievements.size()];
        for(Achievement achievement : achievements)
        {
            achievement.registerStat();
            achievementArray[achievements.indexOf(achievement)] = achievement;
        }
        AchievementPage.registerAchievementPage(new AchievementPage(Reference.MODID, achievementArray));
    }

    private static Achievement createAchievement(String name, int posX, int posY, Item iconItem)
    {
        Achievement achievement = new Achievement("achievement." + name,name,posX,posY,iconItem,(Achievement)null);
        achievements.add(achievement);
        return achievement;
    }

    private static Achievement createAchievement(String name, int posX, int posY, Block iconBlock)
    {
        Achievement achievement = new Achievement("achievement." + name,name,posX,posY,iconBlock,(Achievement)null);
        achievements.add(achievement);
        return achievement;    }

    private static Achievement createAchievement(String name, int posX, int posY, ItemStack iconItemStack)
    {
        Achievement achievement = new Achievement("achievement." + name,name,posX,posY,iconItemStack,(Achievement)null);
        achievements.add(achievement);
        return achievement;    }


*/
}
