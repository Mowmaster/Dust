package com.mowmaster.dust.misc;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KingMowmaster on 5/24/2017.
 */
public class AchievementHandler
{
    private static List<Achievement> achievements = new ArrayList<Achievement>();


    public static Achievement achievementCrystal = createAchievement("minecrystal",0,0, ItemRegistry.crystal);




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
}
