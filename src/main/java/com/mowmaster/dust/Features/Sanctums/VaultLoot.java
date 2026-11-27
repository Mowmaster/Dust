package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Bosses.LootBags;
import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

// What an Arcane Vault (ArcaneVaultBlockEntity) gives the player who opens it, by their Magic Level (m):
//   how many things   ROLLS, one more for every LEVELS_PER_ROLL levels (at most MAX_ROLLS)
//   how rare each is  the loot bags' tiers (LootBags.rollTier), weighted as for a bag of level m + LEVEL_BONUS, so the
//                     rarer tiers come up more often the higher m is
//   what it is        two times in three, something of that tier from the loot bags' table (LootBags.roll: the
//                     element's dust, crystals and wood, its Sigil, its armour, the Archon trinkets); else treasure
// At Magic Level 0 that's 3 things, each Epic or better about 5% of the time; at 50, 5 things, about 14%; at 100, 8
// things, about 17%. Anything Epic or better is announced to everyone. A datapack can replace a set's vault loot with a
// loot table at data/dust/loot_table/vaults/<set>.json (the "vault" kind: this_entity is the player, tool the key, and
// luck their Magic Level).
public final class VaultLoot
{
    public static final int ROLLS = 3, MAX_ROLLS = 8, LEVELS_PER_ROLL = 20, LEVEL_BONUS = 5;

    private VaultLoot() {}

    // The loot for this player, in the order it comes out
    public static List<LootBags.Reward> roll(ServerLevel level, Player player, ElementalArmorSet set, ItemStack key, Vec3 at)
    {
        int magic = PlayerMagicHelper.magicLevel(player);
        RandomSource random = player.getRandom();
        List<LootBags.Reward> rewards = new ArrayList<>();
        ResourceKey<LootTable> tableKey = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(DustReferences.MODID, "vaults/" + set.id));
        LootTable table = level.getServer().reloadableRegistries().getLootTable(tableKey);
        if (table != LootTable.EMPTY) {
            LootParams params = new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, at).withParameter(LootContextParams.THIS_ENTITY, player)
                    .withParameter(LootContextParams.TOOL, key).withLuck(magic).create(LootContextParamSets.VAULT);
            for (ItemStack stack : table.getRandomItems(params)) if (!stack.isEmpty()) rewards.add(new LootBags.Reward(LootBags.Tier.of(stack), stack));
            return rewards;
        }
        int rolls = Math.min(MAX_ROLLS, ROLLS + magic / LEVELS_PER_ROLL);
        for (int i = 0; i < rolls; i++) {
            LootBags.Tier tier = LootBags.rollTier(random, magic + LEVEL_BONUS);
            rewards.add(new LootBags.Reward(tier, item(tier, set, random)));
        }
        return rewards;
    }

    // Tells everyone about the Epic and Legendary things in it
    public static void announce(ServerLevel level, Player player, List<LootBags.Reward> rewards)
    {
        for (LootBags.Reward reward : rewards) {
            if (reward.tier().ordinal() < LootBags.Tier.EPIC.ordinal()) continue;
            level.getServer().getPlayerList().broadcastSystemMessage(Component.translatable("dust.vault.broadcast", player.getDisplayName(),
                    reward.stack().getHoverName().copy().withStyle(reward.tier().color), reward.tier().label()), false);
        }
    }

    // Something a vault of this set might hold, one of it: what turns inside while someone who can open it is near
    public static ItemStack glimpse(ElementalArmorSet set, RandomSource random)
    {
        ItemStack stack = item(LootBags.rollTier(random, 40), set, random);
        stack.setCount(1);
        return stack;
    }

    private static ItemStack item(LootBags.Tier tier, ElementalArmorSet set, RandomSource random)
    {
        return random.nextInt(3) < 2 ? LootBags.roll(tier, set, random) : treasure(tier, random);
    }

    // Treasure of each tier: ingots and emeralds; diamonds, pearls and golden apples; netherite scrap; a totem or an
    // enchanted golden apple; netherite
    private static ItemStack treasure(LootBags.Tier tier, RandomSource random)
    {
        return switch (tier) {
            case COMMON -> switch (random.nextInt(3)) {
                case 0 -> stack(Items.EMERALD, 2, 4, random);
                case 1 -> stack(Items.IRON_INGOT, 3, 6, random);
                default -> stack(Items.GOLD_INGOT, 2, 4, random);
            };
            case UNCOMMON -> switch (random.nextInt(4)) {
                case 0 -> stack(Items.EMERALD, 4, 8, random);
                case 1 -> stack(Items.DIAMOND, 1, 1, random);
                case 2 -> stack(Items.ENDER_PEARL, 2, 4, random);
                default -> stack(Items.GOLDEN_APPLE, 1, 1, random);
            };
            case RARE -> switch (random.nextInt(3)) {
                case 0 -> stack(Items.DIAMOND, 2, 3, random);
                case 1 -> stack(Items.GOLDEN_APPLE, 2, 3, random);
                default -> stack(Items.NETHERITE_SCRAP, 1, 1, random);
            };
            case EPIC -> switch (random.nextInt(3)) {
                case 0 -> stack(Items.TOTEM_OF_UNDYING, 1, 1, random);
                case 1 -> stack(Items.ENCHANTED_GOLDEN_APPLE, 1, 1, random);
                default -> stack(Items.NETHERITE_SCRAP, 2, 3, random);
            };
            case LEGENDARY -> random.nextBoolean() ? stack(Items.NETHERITE_INGOT, 1, 1, random) : stack(Items.ENCHANTED_GOLDEN_APPLE, 2, 2, random);
        };
    }

    private static ItemStack stack(Item item, int min, int max, RandomSource random)
    {
        return new ItemStack(item, Math.min(item.getDefaultMaxStackSize(), min + random.nextInt(max - min + 1)));
    }
}
