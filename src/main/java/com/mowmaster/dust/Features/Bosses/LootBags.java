package com.mowmaster.dust.Features.Bosses;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.Armor.ElementalArmor;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Crystals.ElementCrystals;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Trees.ElementalWood;
import com.mowmaster.dust.Features.Trees.ElementalWoods;
import com.mowmaster.dust.Features.Trees.WoodSet;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// An Archon's loot bag (LootBagItem): every Archon drops one of its set, marked with its level. Opened, it gives
// ROLLS things (one more for every ten levels, at most MAX_ROLLS), each roll first picking how rare (Tier, by weight;
// the rarer tiers weigh more at higher levels) and then something of that tier:
//   Common     its element's dust, crystals, small crystals, its wood's fruit, sapling or logs
//   Uncommon   more dust, a crystal block, a medium crystal, more fruit, bottles o' enchanting
//   Rare       its set's Sigil (TrinketItem), a large crystal, crystal blocks
//   Epic       a piece of its set's armour
//   Legendary  one of the three Archon trinkets: Prism of the Archons, Heart of Mana, Void Locket
// At level 1 a roll is Epic about 3% of the time (a bag holds a piece about 9% of the time); at level 10 about 5%.
// A datapack can replace a set's bag with a loot table at data/dust/loot_table/loot_bags/<set>.json (the "gift" kind:
// this_entity is the player opening it; luck is the bag's level).
public final class LootBags
{
    public enum Tier
    {
        COMMON(ChatFormatting.WHITE), UNCOMMON(ChatFormatting.GREEN), RARE(ChatFormatting.AQUA), EPIC(ChatFormatting.LIGHT_PURPLE), LEGENDARY(ChatFormatting.GOLD);

        public final ChatFormatting color;

        Tier(ChatFormatting color)
        {
            this.color = color;
        }

        public Component label()
        {
            return Component.translatable("dust.loot_bag.tier." + name().toLowerCase(java.util.Locale.ROOT)).withStyle(color);
        }

        // A datapack table's items: how rare they read, from their own rarity
        static Tier of(ItemStack stack)
        {
            Rarity rarity = stack.getRarity();
            if (rarity == Rarity.EPIC) return EPIC;
            if (rarity == Rarity.RARE) return RARE;
            if (rarity == Rarity.UNCOMMON) return UNCOMMON;
            return COMMON;
        }
    }

    public record Reward(Tier tier, ItemStack stack) {}

    public static final int ROLLS = 3;
    public static final int MAX_ROLLS = 6;

    private LootBags() {}

    public static ItemStack bag(ElementalArmorSet set, int level)
    {
        ItemStack bag = new ItemStack(Archons.BAGS.get(set).get());
        bag.set(DustComponentDataRegistry.LOOT_BAG_LEVEL.get(), Math.max(1, level));
        return bag;
    }

    public static int level(ItemStack bag)
    {
        return Math.max(1, bag.getOrDefault(DustComponentDataRegistry.LOOT_BAG_LEVEL.get(), 1));
    }

    public static int rolls(int level)
    {
        return Math.min(MAX_ROLLS, ROLLS + level / 10);
    }

    // A tier's weight for one roll at this level
    public static float weight(Tier tier, int level)
    {
        return switch (tier) {
            case COMMON -> 60.0F;
            case UNCOMMON -> 26.0F + 0.5F * level;
            case RARE -> 9.0F + 0.6F * level;
            case EPIC -> 3.0F + 0.3F * level;
            case LEGENDARY -> 0.6F + 0.08F * level;
        };
    }

    private static Tier rollTier(RandomSource random, int level)
    {
        float total = 0;
        for (Tier tier : Tier.values()) total += weight(tier, level);
        float roll = random.nextFloat() * total;
        for (Tier tier : Tier.values()) {
            roll -= weight(tier, level);
            if (roll < 0) return tier;
        }
        return Tier.COMMON;
    }

    // Opens a bag of this set and level for the player: gives what's in it (dropped at their feet when their
    // inventory is full), tells them, and tells everyone about an Epic or Legendary find
    public static List<Reward> open(ServerLevel level, Player player, ElementalArmorSet set, int bagLevel)
    {
        List<Reward> rewards = fromDatapack(level, player, set, bagLevel);
        if (rewards == null) {
            rewards = new ArrayList<>();
            for (int i = 0; i < rolls(bagLevel); i++) {
                Tier tier = rollTier(player.getRandom(), bagLevel);
                rewards.add(new Reward(tier, roll(tier, set, player.getRandom())));
            }
        }
        Tier best = Tier.COMMON;
        for (Reward reward : rewards) {
            if (reward.stack().isEmpty()) continue;
            if (reward.tier().ordinal() > best.ordinal()) best = reward.tier();
            player.sendSystemMessage(Component.translatable("dust.loot_bag.found", reward.tier().label(),
                    reward.stack().getCount(), reward.stack().getHoverName().copy().withStyle(reward.tier().color)));
            if (reward.tier().ordinal() >= Tier.EPIC.ordinal()) {
                level.getServer().getPlayerList().broadcastSystemMessage(Component.translatable("dust.loot_bag.broadcast", player.getDisplayName(),
                        reward.stack().getHoverName().copy().withStyle(reward.tier().color), reward.tier().label()), false);
            }
            com.mowmaster.mowlib.storage.StorageInteractions.giveToPlayer(player, reward.stack().copy());
        }
        level.playSound(null, player.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (best.ordinal() >= Tier.EPIC.ordinal()) {
            level.playSound(null, player.blockPosition(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS, 0.8F, 1.0F);
        } else if (best == Tier.RARE) {
            level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8F, 1.2F);
        }
        return rewards;
    }

    // A datapack's table for this set's bag, rolled; null when there's none
    private static @Nullable List<Reward> fromDatapack(ServerLevel level, Player player, ElementalArmorSet set, int bagLevel)
    {
        ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(DustReferences.MODID, "loot_bags/" + set.id));
        LootTable table = level.getServer().reloadableRegistries().getLootTable(key);
        if (table == LootTable.EMPTY) return null;
        LootParams params = new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, player.position())
                .withParameter(LootContextParams.THIS_ENTITY, player).withLuck(bagLevel).create(LootContextParamSets.GIFT);
        List<Reward> rewards = new ArrayList<>();
        for (ItemStack stack : table.getRandomItems(params)) rewards.add(new Reward(Tier.of(stack), stack));
        return rewards;
    }

    // Something of this tier from a bag of this set
    public static ItemStack roll(Tier tier, ElementalArmorSet set, RandomSource random)
    {
        DustElement element = set.element();
        WoodSet wood = wood(element);
        return switch (tier) {
            case COMMON -> switch (random.nextInt(6)) {
                case 0 -> stack(DustItemRegistry.DUSTS.get(element).get(), 4, 12, random);
                case 1 -> stack(DustItemRegistry.CRYSTALS.get(element).get(), 1, 3, random);
                case 2 -> stack(ElementCrystals.SMALL_ITEMS.get(element).get(), 1, 2, random);
                case 3 -> wood == null ? stack(DustItemRegistry.DUSTS.get(element).get(), 4, 12, random) : stack(wood.fruit().get(), 1, 3, random);
                case 4 -> wood == null ? stack(DustItemRegistry.CRYSTALS.get(element).get(), 1, 3, random) : stack(wood.sapling().get(), 1, 1, random);
                default -> wood == null ? stack(DustItemRegistry.DUSTS.get(element).get(), 4, 12, random) : stack(wood.log().get(), 4, 8, random);
            };
            case UNCOMMON -> switch (random.nextInt(5)) {
                case 0 -> stack(DustItemRegistry.DUSTS.get(element).get(), 16, 32, random);
                case 1 -> stack(ElementCrystals.BLOCK_ITEMS.get(element).get(), 1, 1, random);
                case 2 -> stack(ElementCrystals.MEDIUM_ITEMS.get(element).get(), 1, 1, random);
                case 3 -> wood == null ? stack(DustItemRegistry.CRYSTALS.get(element).get(), 4, 8, random) : stack(wood.fruit().get(), 4, 8, random);
                default -> stack(Items.EXPERIENCE_BOTTLE, 3, 6, random);
            };
            case RARE -> {
                int roll = random.nextInt(20);
                if (roll < 11) yield new ItemStack(Archons.SIGILS.get(set).get());
                if (roll < 16) yield new ItemStack(ElementCrystals.LARGE_ITEMS.get(element).get());
                yield stack(ElementCrystals.BLOCK_ITEMS.get(element).get(), 2, 4, random);
            }
            case EPIC -> new ItemStack(ElementalArmor.item(set, ElementalArmor.PIECES.get(random.nextInt(ElementalArmor.PIECES.size()))));
            case LEGENDARY -> new ItemStack(Archons.LEGENDARY.get(random.nextInt(Archons.LEGENDARY.size())).get());
        };
    }

    private static ItemStack stack(ItemLike item, int min, int max, RandomSource random)
    {
        Item asItem = item.asItem();
        return new ItemStack(asItem, Math.min(asItem.getDefaultMaxStackSize(), min + random.nextInt(max - min + 1)));
    }

    // The elemental wood of this element, if it has one
    private static @Nullable WoodSet wood(DustElement element)
    {
        for (ElementalWood wood : ElementalWood.values()) {
            if (wood.element().equals(element)) return ElementalWoods.set(wood);
        }
        return null;
    }
}
