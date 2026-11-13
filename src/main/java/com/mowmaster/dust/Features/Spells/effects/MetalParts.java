package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustConfig;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

// Metal's block effect: the block is marked for a while (duration x 12). Broken while marked, an ore drops one more of
// its first drop; any other block drops a metal nugget, copper, iron or gold by the weights in config/dust-synced.toml
// (70 / 20 / 10 by default). One bonus per mark. (MowLib's BonusDrops roll by block type and tool; this is one marked
// block, so it's a drops hook like Knowledge's.)
@EventBusSubscriber(modid = DustReferences.MODID)
public final class MetalParts
{
    public static final int MARK_TIME_SCALE = 12;

    private MetalParts() {}

    public static void markBonus(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        if (level.getBlockState(pos).isAir()) return;
        BlockMarks.mark(level, pos, BlockMarks.METAL_BONUS, cast.stats().duration() * MARK_TIME_SCALE);
        level.sendParticles(ParticleTypes.WAX_ON, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 12, 0.4, 0.4, 0.4, 0.5);
    }

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event)
    {
        ServerLevel level = event.getLevel();
        BlockPos pos = event.getPos();
        if (!BlockMarks.has(level, pos, BlockMarks.METAL_BONUS)) return;
        BlockMarks.remove(level, pos, BlockMarks.METAL_BONUS);
        ItemStack bonus;
        if (event.getState().is(Tags.BlockItems.ORES.block())) {
            if (event.getDrops().isEmpty()) return;
            bonus = event.getDrops().getFirst().getItem().copyWithCount(1);
        } else {
            bonus = nugget(level);
            if (bonus.isEmpty()) return;
        }
        event.getDrops().add(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, bonus));
    }

    // A nugget picked by the configured weights (nothing if they're all 0)
    private static ItemStack nugget(ServerLevel level)
    {
        int copper = DustConfig.METAL_COPPER_WEIGHT.get(), iron = DustConfig.METAL_IRON_WEIGHT.get(), gold = DustConfig.METAL_GOLD_WEIGHT.get();
        int total = copper + iron + gold;
        if (total <= 0) return ItemStack.EMPTY;
        int roll = level.getRandom().nextInt(total);
        if (roll < copper) return new ItemStack(Items.COPPER_NUGGET);
        if (roll < copper + iron) return new ItemStack(Items.IRON_NUGGET);
        return new ItemStack(Items.GOLD_NUGGET);
    }
}
