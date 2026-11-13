package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.mowlib.compacting.CompactingChains;
import com.mowmaster.mowlib.compacting.CompactingContents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;

// Fracture on yourself: the stack in your main hand is crushed one step down.
//   Ores become what they're mined for: raw iron, raw copper, coal, diamonds... (ORES below)
//   Anything in a compacting chain drops a tier: blocks to ingots, ingots to nuggets, raw blocks to raw ore. The chains
//   are MowLib's CompactingChains (worked out from crafting recipes, so modded metals work too).
// The pieces go into your inventory; what doesn't fit drops at your feet. Nothing to crush: nothing happens.
public final class FractureParts
{
    private record Crush(Item into, int count) {}

    // Ores, and what one crushes into (about what mining one gives without Fortune)
    private static final Map<Item, Crush> ORES = Map.ofEntries(
            Map.entry(Items.IRON_ORE, new Crush(Items.RAW_IRON, 1)), Map.entry(Items.DEEPSLATE_IRON_ORE, new Crush(Items.RAW_IRON, 1)),
            Map.entry(Items.COPPER_ORE, new Crush(Items.RAW_COPPER, 3)), Map.entry(Items.DEEPSLATE_COPPER_ORE, new Crush(Items.RAW_COPPER, 3)),
            Map.entry(Items.GOLD_ORE, new Crush(Items.RAW_GOLD, 1)), Map.entry(Items.DEEPSLATE_GOLD_ORE, new Crush(Items.RAW_GOLD, 1)),
            Map.entry(Items.NETHER_GOLD_ORE, new Crush(Items.GOLD_NUGGET, 4)),
            Map.entry(Items.COAL_ORE, new Crush(Items.COAL, 1)), Map.entry(Items.DEEPSLATE_COAL_ORE, new Crush(Items.COAL, 1)),
            Map.entry(Items.DIAMOND_ORE, new Crush(Items.DIAMOND, 1)), Map.entry(Items.DEEPSLATE_DIAMOND_ORE, new Crush(Items.DIAMOND, 1)),
            Map.entry(Items.EMERALD_ORE, new Crush(Items.EMERALD, 1)), Map.entry(Items.DEEPSLATE_EMERALD_ORE, new Crush(Items.EMERALD, 1)),
            Map.entry(Items.LAPIS_ORE, new Crush(Items.LAPIS_LAZULI, 5)), Map.entry(Items.DEEPSLATE_LAPIS_ORE, new Crush(Items.LAPIS_LAZULI, 5)),
            Map.entry(Items.REDSTONE_ORE, new Crush(Items.REDSTONE, 4)), Map.entry(Items.DEEPSLATE_REDSTONE_ORE, new Crush(Items.REDSTONE, 4)),
            Map.entry(Items.NETHER_QUARTZ_ORE, new Crush(Items.QUARTZ, 1)),
            Map.entry(Items.ANCIENT_DEBRIS, new Crush(Items.NETHERITE_SCRAP, 1)));

    private FractureParts() {}

    public static void crushHeld(SpellCast cast, LivingEntity target)
    {
        if (!(target instanceof ServerPlayer player)) return;
        ItemStack held = player.getMainHandItem();
        if (held.isEmpty()) return;
        Crush crush = crushOf(held.getItem());
        if (crush == null) return;
        // long: a stack of blocks makes 64 x 9 pieces; plenty of room, but no surprises
        long pieces = (long) held.getCount() * crush.count();
        Item particle = held.getItem();
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        int max = new ItemStack(crush.into()).getMaxStackSize();
        while (pieces > 0) {
            int count = (int) Math.min(max, pieces);
            pieces -= count;
            ItemStack stack = new ItemStack(crush.into(), count);
            // add() puts in what fits and leaves the rest in the stack
            if (!player.getInventory().add(stack) && !stack.isEmpty()) player.spawnAtLocation(player.level(), stack);
        }
        // 26.3: an item particle takes the Item (or an ItemStackTemplate), not an ItemStack
        cast.level().sendParticles(new ItemParticleOption(ParticleTypes.ITEM, particle), player.getX(), player.getY() + 1.2, player.getZ(), 15, 0.3, 0.3, 0.3, 0.08);
        cast.level().playSound(null, player.blockPosition(), SoundEvents.GRINDSTONE_USE, SoundSource.PLAYERS, 0.8F, 1.2F);
    }

    // What an item crushes into: an ore, or a step down its compacting chain (null: it doesn't crush)
    private static Crush crushOf(Item item)
    {
        Crush ore = ORES.get(item);
        if (ore != null) return ore;
        List<CompactingContents.Tier> chain = CompactingChains.find(item);
        if (chain == null) return null;
        for (int i = 1; i < chain.size(); i++) {
            if (chain.get(i).item() == item) {
                CompactingContents.Tier below = chain.get(i - 1);
                // Tier values count the lowest tier: an ingot is 9, a block 81; a block crushes into 81 / 9 = 9 ingots
                return new Crush(below.item(), chain.get(i).value() / below.value());
            }
        }
        return null;
    }
}
