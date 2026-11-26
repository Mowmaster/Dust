package com.mowmaster.dust.Features.Labyrinth;

import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Bosses.Archons;
import com.mowmaster.dust.Features.Bosses.LootBags;
import com.mowmaster.dust.Features.Crystals.ElementCrystals;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Trees.ElementalWood;
import com.mowmaster.dust.Features.Trees.ElementalWoods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;

// What a labyrinth's chests hold (treasure rooms, solved puzzles, the challenge island): the run's element's dust,
// crystals and fruit, experience bottles, sometimes a medium or large crystal, and a chance (better for richer chests
// and higher runs) of one of its Archon's loot bags or its Sigil
public final class LabyrinthLoot
{
    private LabyrinthLoot() {}

    // A chest at pos (facing the way given, or north), filled: richness 0 (treasure), 1 (a puzzle), 2 (a challenge)
    public static void chest(LevelAccessor level, BlockPos pos, LabyrinthInstance run, int richness)
    {
        chest(level, pos, Direction.NORTH, run, richness);
    }

    public static void chest(LevelAccessor level, BlockPos pos, Direction facing, LabyrinthInstance run, int richness)
    {
        level.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing), 3);
        if (level.getBlockEntity(pos) instanceof Container chest) fill(chest, run.set, run.bossLevel, level.getRandom(), richness);
    }

    public static void fill(Container chest, ElementalArmorSet set, int level, RandomSource random, int richness)
    {
        DustElement element = set.element();
        put(chest, random, new ItemStack(DustItemRegistry.DUSTS.get(element).get(), 8 + random.nextInt(17) + 4 * richness));
        put(chest, random, new ItemStack(DustItemRegistry.CRYSTALS.get(element).get(), 2 + random.nextInt(5)));
        put(chest, random, new ItemStack(Items.EXPERIENCE_BOTTLE, 2 + random.nextInt(4) + richness));
        for (ElementalWood wood : ElementalWood.values()) {
            if (wood.element().equals(element)) put(chest, random, new ItemStack(ElementalWoods.set(wood).fruit().get(), 2 + random.nextInt(4)));
        }
        if (random.nextFloat() < 0.3F + 0.1F * richness) put(chest, random, new ItemStack(ElementCrystals.MEDIUM_ITEMS.get(element).get()));
        if (random.nextFloat() < 0.08F + 0.06F * richness) put(chest, random, new ItemStack(ElementCrystals.LARGE_ITEMS.get(element).get()));
        if (random.nextFloat() < Math.min(0.6F, 0.15F + 0.1F * richness + 0.01F * level)) put(chest, random, LootBags.bag(set, level));
        if (random.nextFloat() < 0.03F + 0.02F * richness) put(chest, random, new ItemStack(Archons.SIGILS.get(set).get()));
    }

    private static void put(Container chest, RandomSource random, ItemStack stack)
    {
        for (int attempt = 0; attempt < 8; attempt++) {
            int slot = random.nextInt(chest.getContainerSize());
            if (chest.getItem(slot).isEmpty()) {
                chest.setItem(slot, stack);
                return;
            }
        }
    }
}
