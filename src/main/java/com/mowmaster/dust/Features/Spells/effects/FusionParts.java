package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

// The fusions' own effects (modifiers/FusionModifier, registered in SpellRegistries)
public final class FusionParts
{
    private FusionParts() {}

    // Fracture + Earth, on a block: breaks it as a pickaxe would.
    //   Potency    the pickaxe's tier: under 1 wood, 1 stone, 2 iron, 3 diamond, 4 and up netherite. A block that needs a
    //              better pickaxe isn't touched.
    //   Luck       Fortune: one level for every 5% of luck (5 Luck levels), up to Fortune V
    //   Silky      Silk Touch
    public static void mine(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.getDestroySpeed(level, pos) < 0) return;
        ItemStack pickaxe = pickaxe(cast.stats().potency());
        if (state.requiresCorrectToolForDrops() && !pickaxe.isCorrectToolForDrops(state)) return;
        // The registry of enchantments is data (it can change with datapacks), so they're looked up through the level
        var enchantments = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        if (SpellRegistries.silky(cast)) {
            pickaxe.enchant(enchantments.getOrThrow(Enchantments.SILK_TOUCH), 1);
        } else {
            int fortune = Math.min(5, (int) Math.floor(cast.stats().luck() * 100 / 5));
            if (fortune > 0) pickaxe.enchant(enchantments.getOrThrow(Enchantments.FORTUNE), fortune);
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        // The drops this pickaxe would get, worked out before the block goes
        List<ItemStack> drops = Block.getDrops(state, level, pos, blockEntity, cast.caster(), pickaxe);
        level.destroyBlock(pos, false, cast.caster());
        for (ItemStack drop : drops) Block.popResource(level, pos, drop);
        // Experience from ores and the like (true: as a player mining it would)
        state.spawnAfterBreak(level, pos, pickaxe, true);
    }

    private static ItemStack pickaxe(float potency)
    {
        if (potency >= 4) return new ItemStack(Items.NETHERITE_PICKAXE);
        if (potency >= 3) return new ItemStack(Items.DIAMOND_PICKAXE);
        if (potency >= 2) return new ItemStack(Items.IRON_PICKAXE);
        if (potency >= 1) return new ItemStack(Items.STONE_PICKAXE);
        return new ItemStack(Items.WOODEN_PICKAXE);
    }
}
