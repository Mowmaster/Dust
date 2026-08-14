package com.mowmaster.dust.Features.EffectScrolls.Item;

import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustElementAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class EarthStickItem extends Item {

    public EarthStickItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockState clickedBlockState = level.getBlockState(context.getClickedPos());
        Block clickedBlock = clickedBlockState.getBlock();

        if(!level.isClientSide()) {
            if (context.getPlayer() instanceof ServerPlayer serverPlayer) {

                if(clickedBlock instanceof CropBlock cropBlock)
                {
                    if(cropBlock.getAge(clickedBlockState) < cropBlock.getMaxAge())
                    {
                        if(ToolMagicHelper.toolUseOn((ServerLevel)level,serverPlayer,context, EnumElement.EARTH,1,1,1))
                        {
                            cropBlock.growCrops(level,context.getClickedPos(),clickedBlockState);
                            ((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER,
                                    context.getClickedPos().getX()+0.5,
                                    context.getClickedPos().getY()+0.5,
                                    context.getClickedPos().getZ()+0.5,
                                    5,level.getRandom().nextDouble(),0,0,0.5);

                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }

        return InteractionResult.FAIL;
    }
}
