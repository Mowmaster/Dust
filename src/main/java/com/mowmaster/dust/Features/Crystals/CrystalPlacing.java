package com.mowmaster.dust.Features.Crystals;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;

// An element's crystal item (the small crystal: DustItemRegistry.CRYSTALS) places its small crystal on the face it's
// used on, as a block item would. UseItemOnBlockEvent's ITEM_AFTER_BLOCK phase comes after the block clicked had its
// say (a station or display taking the crystal comes first), where an item's own useOn would run.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class CrystalPlacing
{
    private CrystalPlacing() {}

    @SubscribeEvent
    public static void onUseItemOnBlock(UseItemOnBlockEvent event)
    {
        if (event.getUsePhase() != UseItemOnBlockEvent.UsePhase.ITEM_AFTER_BLOCK) return;
        ItemStack stack = event.getItemStack();
        DustElement element = CrystalElements.of(stack);
        if (element == null) return;
        BlockPlaceContext context = new BlockPlaceContext(event.getUseOnContext());
        if (!context.canPlace()) return;
        Block crystal = ElementCrystals.node(element, CrystalSize.SMALL);
        BlockState state = crystal.getStateForPlacement(context);
        Level level = event.getLevel();
        BlockPos at = context.getClickedPos();
        if (state == null || !state.canSurvive(level, at)) return;
        if (!level.isClientSide()) {
            level.setBlock(at, state, Block.UPDATE_ALL_IMMEDIATE);
            var sound = state.getSoundType(level, at, event.getPlayer());
            level.playSound(null, at, sound.getPlaceSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
            Player player = event.getPlayer();
            if (player == null || !player.getAbilities().instabuild) stack.shrink(1);
        }
        event.cancelWithResult(InteractionResult.SUCCESS);
    }
}
