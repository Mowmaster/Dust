package com.mowmaster.dust.world.structures.LootTables;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.monster.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by KingMowmaster on 6/30/2017.
 */
public class SpawnerTypesPassive
{
    private static ArrayList<SpawnerTypesPassive.DungeonMob> dungeonMobs = new ArrayList<SpawnerTypesPassive.DungeonMob>();

    /**
     * Adds a mob to the possible list of creatures the spawner will create.
     * If the mob is already in the spawn list, the rarity will be added to the existing one,
     * causing the mob to be more common.
     *
     * @param name The name of the monster, use the same name used when registering the entity.
     * @param rarity The rarity of selecting this mob over others. Must be greater then 0.
     *        Vanilla Minecraft has the following mobs:
     *        Spider   100
     *        Skeleton 100
     *        Zombie   200
     *        Meaning, Zombies are twice as common as spiders or skeletons.
     * @return The new rarity of the monster,
     */
    public static float addDungeonMob(ResourceLocation name, int rarity)
    {
        if (rarity <= 0)
        {
            throw new IllegalArgumentException("Rarity must be greater then zero");
        }

        for (SpawnerTypesPassive.DungeonMob mob : dungeonMobs)
        {
            if (name.equals(mob.type))
            {
                return mob.itemWeight += rarity;
            }
        }

        dungeonMobs.add(new SpawnerTypesPassive.DungeonMob(rarity, name));
        return rarity;
    }

    // TODO: remove
    /** @deprecated use {@link #removeDungeonMob(ResourceLocation)} */
    @Deprecated
    public static int removeDungeonMob(String name)
    {
        return removeDungeonMob(new ResourceLocation(name));
    }

    /**
     * Will completely remove a Mob from the dungeon spawn list.
     *
     * @param name The name of the mob to remove
     * @return The rarity of the removed mob, prior to being removed.
     */
    public static int removeDungeonMob(ResourceLocation name)
    {
        for (SpawnerTypesPassive.DungeonMob mob : dungeonMobs)
        {
            if (name.equals(mob.type))
            {
                dungeonMobs.remove(mob);
                return mob.itemWeight;
            }
        }
        return 0;
    }

    /**
     * Gets a random mob name from the list.
     * @param rand World generation random number generator
     * @return The mob name
     */
    public static ResourceLocation getRandomDungeonMob(Random rand)
    {
        SpawnerTypesPassive.DungeonMob mob = WeightedRandom.getRandomItem(rand, dungeonMobs);
        return mob.type;
    }


    public static class DungeonMob extends WeightedRandom.Item
    {
        public ResourceLocation type;
        public DungeonMob(int weight, ResourceLocation type)
        {
            super(weight);
            this.type = type;
        }

        @Override
        public boolean equals(Object target)
        {
            return target instanceof SpawnerTypesPassive.DungeonMob && type.equals(((SpawnerTypesPassive.DungeonMob)target).type);
        }
    }

    static
    {
        addDungeonMob(new ResourceLocation("cow"), 1000);
        addDungeonMob(new ResourceLocation("pig"),   1000);
        addDungeonMob(new ResourceLocation("sheep"),   1000);
        addDungeonMob(new ResourceLocation("rabbit"),   1000);

        addDungeonMob(new ResourceLocation("horse"),   500);
        addDungeonMob(new ResourceLocation("donkey"),   500);
        addDungeonMob(new ResourceLocation("mule"),   500);
        addDungeonMob(new ResourceLocation("llama"),   500);

        addDungeonMob(new ResourceLocation("polar_bear"),   250);
        addDungeonMob(new ResourceLocation("squid"),   250);

        addDungeonMob(new ResourceLocation("mooshroom"),   100);

        addDungeonMob(new ResourceLocation("villager"),   25);
        addDungeonMob(new ResourceLocation("villager_golem"),   25);

        addDungeonMob(new ResourceLocation("xp_bottle"),   1);
    }

    private static final ResourceLocation[] SPAWNERTYPES = new ResourceLocation[] {EntityList.getKey(EntityExpBottle.class),EntityList.getKey(EntityVex.class),
            EntityList.getKey(EntitySlime.class),EntityList.getKey(EntityBlaze.class),EntityList.getKey(EntityEnderman.class),EntityList.getKey(EntityCreeper.class),
            EntityList.getKey(EntityWitch.class),EntityList.getKey(EntitySkeleton.class), EntityList.getKey(EntityZombie.class), EntityList.getKey(EntityCaveSpider.class),
            EntityList.getKey(EntitySpider.class)};
}
