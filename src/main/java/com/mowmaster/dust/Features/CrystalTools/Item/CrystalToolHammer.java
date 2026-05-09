package com.mowmaster.dust.Features.CrystalTools.Item;

import com.mowmaster.dust.DustDataGen.DustTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrystalToolHammer extends Item {

    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>();

    public CrystalToolHammer(ToolMaterial toolMaterial, float attackDamageBaseline, float attackSpeedBaseline, Properties properties) {
        super(properties.tool(toolMaterial, BlockTags.MINEABLE_WITH_PICKAXE, attackDamageBaseline, attackSpeedBaseline, 0f));
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState state, BlockPos pos, LivingEntity owner) {
        if(itemStack.getItem() instanceof CrystalToolHammer hammerItem && owner instanceof ServerPlayer serverPlayer) {
            if(HARVESTED_BLOCKS.contains(pos)) {
                return true;
            }

            for(BlockPos position : getBlocksToBeDestroyed(1, pos, serverPlayer)) {
                if(pos == position || !hammerItem.isCorrectToolForDrops(itemStack, level.getBlockState(position))) {
                    continue;
                }

                HARVESTED_BLOCKS.add(position);
                if(serverPlayer.gameMode.destroyBlock(position))
                {
                    if (!level.isClientSide() && state.getDestroySpeed(level, pos) != 0.0F) {
                        itemStack.hurtAndBreak(1, owner, EquipmentSlot.MAINHAND);
                    }
                }
                HARVESTED_BLOCKS.remove(position);
            }
        }

        return true;
    }

    // Done with the help of https://github.com/CoFH/CoFHCore/blob/1.19.x/src/main/java/cofh/core/event/AreaEffectEvents.java
    // Don't be a jerk License
    private List<BlockPos> getBlocksToBeDestroyed(int range, BlockPos initalBlockPos, ServerPlayer player) {
        List<BlockPos> positions = new ArrayList<>();

        BlockHitResult traceResult = player.level().clip(new ClipContext(player.getEyePosition(1f),
                (player.getEyePosition(1f).add(player.getViewVector(1f).scale(6f))),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if(traceResult.getType() == HitResult.Type.MISS) {
            return positions;
        }

        if(traceResult.getDirection() == Direction.DOWN || traceResult.getDirection() == Direction.UP) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX() + x, initalBlockPos.getY(), initalBlockPos.getZ() + y));
                }
            }
        }

        if(traceResult.getDirection() == Direction.NORTH || traceResult.getDirection() == Direction.SOUTH) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX() + x, initalBlockPos.getY() + y, initalBlockPos.getZ()));
                }
            }
        }

        if(traceResult.getDirection() == Direction.EAST || traceResult.getDirection() == Direction.WEST) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX(), initalBlockPos.getY() + y, initalBlockPos.getZ() + x));
                }
            }
        }

        return positions;
    }
}
