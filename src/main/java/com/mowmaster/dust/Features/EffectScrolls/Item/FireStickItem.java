package com.mowmaster.dust.Features.EffectScrolls.Item;

import com.mowmaster.dust.DustDataGen.DustTags;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustRegistries.DustParticleRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustElementAttachmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.Map;
import java.util.function.Consumer;

public class FireStickItem extends Item {

    private static final Map<Block, Item> FIRESTICK_MAP =
            Map.ofEntries(
                    Map.entry(Blocks.ACACIA_LOG, Items.CHARCOAL),
                    Map.entry(Blocks.PALE_OAK_LOG, Items.CHARCOAL),
                    Map.entry(Blocks.BIRCH_LOG, Items.CHARCOAL),
                    Map.entry(Blocks.DARK_OAK_LOG, Items.CHARCOAL),
                    Map.entry(Blocks.CHERRY_LOG, Items.CHARCOAL),
                    Map.entry(Blocks.JUNGLE_LOG, Items.CHARCOAL),
                    Map.entry(Blocks.MANGROVE_LOG, Items.CHARCOAL),
                    Map.entry(Blocks.OAK_LOG, Items.CHARCOAL),
                    Map.entry(Blocks.SPRUCE_LOG, Items.CHARCOAL),
                    Map.entry(Blocks.BAMBOO_BLOCK, Items.CHARCOAL),
                    Map.entry(Blocks.CRIMSON_HYPHAE, DustItemRegistry.CHARCOAL_WHITE.get()),
                    Map.entry(Blocks.WARPED_HYPHAE, DustItemRegistry.CHARCOAL_WHITE.get())
            );

    public FireStickItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();

        //boolean changed = false;
        if(!level.isClientSide()) {
            if (FIRESTICK_MAP.containsKey(clickedBlock)) {
                if (context.getPlayer() instanceof ServerPlayer serverPlayer) {

                    //changed = true;
                    level.addFreshEntity(new ItemEntity(level,context.getClickedPos().getX(),context.getClickedPos().getY(),context.getClickedPos().getZ(), new ItemStack(FIRESTICK_MAP.get(clickedBlock),1)));
                    level.setBlockAndUpdate(context.getClickedPos(), Blocks.AIR.defaultBlockState());
                    ((ServerLevel) level).sendParticles(DustParticleRegistry.DUSTPARTICLES_SPELLS_FIRE.get(),
                            context.getClickedPos().getX()+0.5,
                            context.getClickedPos().getY()+0.5,
                            context.getClickedPos().getZ()+0.5,
                            1,0,0,0,0.15);
                    ((ServerLevel) level).playPlayerSound(SoundEvents.CAMPFIRE_CRACKLE,SoundSource.BLOCKS,0.5f,0.5f);
                    int fire = DustElementAttachmentHelper.getElementFireInfo(serverPlayer).count();
                    if (fire > 0) {
                        DustElementAttachmentHelper.removeFromElementFire(serverPlayer, 1, false);
                    } else {
                        context.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), serverPlayer,
                                item -> serverPlayer.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                    }
                }
            }
        }
        /*else {
            if(changed)
            {
                for(int i =0; i<10; i++)
                {
                    level.addParticle(DustParticleRegistry.DUSTPARTICLES_SPELLS_FIRE.get(),
                            context.getClickedPos().getX()+0.5,
                            context.getClickedPos().getY()+1.25,
                            context.getClickedPos().getZ()+0.5,
                            0,
                            0,
                            0);
                }
            }
        }*/

        return InteractionResult.SUCCESS;
    }
}
