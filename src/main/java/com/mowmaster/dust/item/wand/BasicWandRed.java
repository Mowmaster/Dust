package com.mowmaster.dust.item.wand;

import com.mowmaster.dust.block.dustFire.DustFireRedBlock;
import com.mowmaster.dust.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class BasicWandRed extends Item {
    public BasicWandRed(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(context.getClickedFace());
        if (DustFireRedBlock.canBePlacedAt(level, blockpos1, context.getHorizontalDirection())) {
            level.playSound(player, blockpos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlock(blockpos1, DustFireRedBlock.getState(level, blockpos), 11);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
            ItemStack itemstack = context.getItemInHand();
            if (player instanceof ServerPlayer) {
                if(!removeDust(player)){
                    itemstack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        } else {
            return InteractionResult.FAIL;
        }
    }

    public boolean canRemoveDust(Player player)
    {
        return player.getInventory().contains(new ItemStack(ModItems.DUST_RED.get()));
    }

    public boolean removeDust(Player player)
    {
        if(canRemoveDust(player))
        {
            Inventory inv = player.getInventory();
            inv.removeItem(inv.findSlotMatchingItem(new ItemStack(ModItems.DUST_RED.get())),1);
            return true;
        }

        return false;
    }

    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_FLINT_ACTIONS.contains(itemAbility);
    }
}
