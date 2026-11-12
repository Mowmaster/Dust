package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.mowlib.storage.StorageInteractions;
import com.mowmaster.mowlib.util.CBPixelBox;
import com.mowmaster.mowlib.util.LeftClickCooldown;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jspecify.annotations.Nullable;

// Putting things into research stations and taking them out, by where you click (each station's IStationSlots):
//   right-click a slot with an item it takes    puts it in (as many as the slot takes)
//   left-click a slot with something in it      takes 1 out (sneak: as many as a stack)
// Left-clicking anywhere else mines the block as normal. Same rules as MowLib's storage blocks (StorageInteractions,
// LeftClickCooldown), so every station handles the same way.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class StationInteractions
{
    private StationInteractions() {}

    // The slot at this point of the block (a hit point in pixels, 0-16 on each axis), or null
    public static @Nullable StationSlot slotAt(Level level, BlockPos pos, Vec3 pixel)
    {
        BlockState state = level.getBlockState(pos);
        if (!(level.getBlockEntity(pos) instanceof IStationSlots station)) return null;
        // The slots' areas are drawn for the model facing north: turn them the way the block faces
        Direction facing = state.getOptionalValue(BlockStateProperties.HORIZONTAL_FACING).orElse(Direction.NORTH);
        int turns = CBPixelBox.quarterTurns(facing);
        for (StationSlot slot : station.slots()) {
            if (slot.area().rotateY(turns).contains(pixel)) return slot;
        }
        return null;
    }

    // Call from a station's useItemOn. SUCCESS if the item went into the slot clicked; otherwise TRY_WITH_EMPTY_HAND
    // (so the item does its own thing, e.g. a block is placed against the station).
    public static InteractionResult rightClick(ItemStack stack, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (stack.isEmpty()) return InteractionResult.TRY_WITH_EMPTY_HAND;
        StationSlot slot = slotAt(level, pos, CBPixelBox.pixelInside(hit));
        if (slot == null || !slot.accepts().test(stack)) return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (!level.isClientSide()) {
            int moved = slot.inserter().insert(stack, player);
            if (moved > 0) {
                stack.consume(moved, player); // creative players keep their stack
                level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.8F, 1.0F);
            }
        }
        return InteractionResult.SUCCESS;
    }

    // Left-click: the event doesn't say where on the block was hit, so look along the player's view to find out.
    // Cancelling the event (when a slot with something in it was clicked) stops the block being mined (and, in
    // creative, broken); it's cancelled on both sides so the client doesn't start breaking it either.
    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event)
    {
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) return;
        Player player = event.getEntity();
        Level level = player.level();
        BlockPos pos = event.getPos();
        if (!(level.getBlockEntity(pos) instanceof IStationSlots)) return;
        // pick: what the player's crosshair hits within their reach (false: not fluids)
        HitResult hit = player.pick(player.blockInteractionRange(), 1.0F, false);
        if (!(hit instanceof BlockHitResult blockHit) || !blockHit.getBlockPos().equals(pos)) return;
        StationSlot slot = slotAt(level, pos, CBPixelBox.pixelInside(blockHit));
        if (slot == null || slot.contents().get().isEmpty()) return;

        event.setCanceled(true);
        if (level.isClientSide() || !LeftClickCooldown.ready(player, level)) return;
        ItemStack taken = slot.extractor().extract(player.isShiftKeyDown() ? 64 : 1);
        if (!taken.isEmpty()) {
            StorageInteractions.giveToPlayer(player, taken);
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 0.8F, 1.0F);
        }
    }
}
