package com.mowmaster.dust.Features.Spells.mobeffects;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Spells.effects.BlockMarks;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Map;
import java.util.Optional;

// Tool spells:
//   Tool Sense (self)   starting to break a block switches to the best tool for it on the hotbar; a tool that breaks
//                       is replaced by another of the same from the inventory
//   a marked block      can be harvested with a tool one tier below what it needs (UtilityParts.markLowerTier)
@EventBusSubscriber(modid = DustReferences.MODID)
public final class ToolSense
{
    private ToolSense() {}

    // The best tool on the hotbar for this block: the one that mines it fastest (and gets its drops, if it needs a tool)
    @SubscribeEvent
    public static void onStartBreaking(PlayerInteractEvent.LeftClickBlock event)
    {
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START || !(event.getEntity() instanceof ServerPlayer player)
                || !player.hasEffect(DustEffectsRegistry.TOOL_SENSE)) {
            return;
        }
        BlockState state = player.level().getBlockState(event.getPos());
        Inventory inventory = player.getInventory();
        int best = inventory.getSelectedSlot();
        float bestSpeed = speed(inventory.getItem(best), state);
        for (int slot = 0; slot < Inventory.getSelectionSize(); slot++) {
            float speed = speed(inventory.getItem(slot), state);
            if (speed > bestSpeed) {
                best = slot;
                bestSpeed = speed;
            }
        }
        if (best != inventory.getSelectedSlot()) {
            inventory.setSelectedSlot(best);
            // The client chooses the held slot; tell it about the change
            player.connection.send(new ClientboundSetHeldSlotPacket(best));
        }
    }

    // How fast a stack mines the block (0 if the block needs a tool and this one won't get its drops)
    private static float speed(ItemStack stack, BlockState state)
    {
        if (state.requiresCorrectToolForDrops() && !stack.isCorrectToolForDrops(state)) return 0;
        return stack.getDestroySpeed(state);
    }

    // A tool broke: another of the same kind from the inventory takes its place (next tick, once the broken one is gone)
    @SubscribeEvent
    public static void onToolBroke(PlayerDestroyItemEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || event.getHand() == null || !player.hasEffect(DustEffectsRegistry.TOOL_SENSE)) return;
        ItemStack broken = event.getOriginal();
        InteractionHand hand = event.getHand();
        MinecraftServer server = player.level().getServer();
        // TickTask: a job the server runs on a later tick
        server.schedule(new TickTask(server.getTickCount() + 1, () -> {
            if (!player.getItemInHand(hand).isEmpty()) return;
            Inventory inventory = player.getInventory();
            for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
                ItemStack spare = inventory.getItem(slot);
                if (!spare.isEmpty() && spare.is(broken.getItem()) && spare != player.getItemInHand(hand)) {
                    player.setItemInHand(hand, spare.copy());
                    inventory.setItem(slot, ItemStack.EMPTY);
                    return;
                }
            }
        }));
    }

    // ---- one tier lower ----

    // A tool's tier: from the "can't get drops from" tag its tool component starts with (wood/gold 0, stone and copper 1,
    // iron 2, diamond 3, netherite 4); -1 for anything that isn't a tiered tool
    private static final Map<TagKey<Block>, Integer> TOOL_TIERS = Map.of(
            BlockTags.INCORRECT_FOR_WOODEN_TOOL, 0, BlockTags.INCORRECT_FOR_GOLD_TOOL, 0, BlockTags.INCORRECT_FOR_STONE_TOOL, 1,
            BlockTags.INCORRECT_FOR_COPPER_TOOL, 1, BlockTags.INCORRECT_FOR_IRON_TOOL, 2, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 3,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 4);

    private static int toolTier(ItemStack stack)
    {
        Tool tool = stack.get(DataComponents.TOOL);
        if (tool == null) return -1;
        for (Tool.Rule rule : tool.rules()) {
            // unwrapKey: the tag a HolderSet was made from, if it was made from one
            Optional<TagKey<Block>> tag = rule.blocks() instanceof HolderSet.Named<Block> named ? Optional.of(named.key()) : Optional.empty();
            if (tag.isPresent() && TOOL_TIERS.containsKey(tag.get())) return TOOL_TIERS.get(tag.get());
        }
        return -1;
    }

    // The tier a block needs (stone 1, iron 2, diamond 3; 0 for anything)
    private static int neededTier(BlockState state)
    {
        if (state.is(BlockTags.NEEDS_DIAMOND_TOOL)) return 3;
        if (state.is(BlockTags.NEEDS_IRON_TOOL)) return 2;
        if (state.is(BlockTags.NEEDS_STONE_TOOL)) return 1;
        return 0;
    }

    @SubscribeEvent
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event)
    {
        if (event.canHarvest() || !(event.getLevel() instanceof Level level) || !BlockMarks.has(level, event.getPos(), BlockMarks.LOWER_TIER)) return;
        ItemStack held = event.getEntity().getMainHandItem();
        BlockState state = event.getTargetBlock();
        // The right kind of tool (it mines the block quickly), at most one tier short
        if (held.getDestroySpeed(state) > 1 && toolTier(held) >= 0 && toolTier(held) + 1 >= neededTier(state)) event.setCanHarvest(true);
    }
}
