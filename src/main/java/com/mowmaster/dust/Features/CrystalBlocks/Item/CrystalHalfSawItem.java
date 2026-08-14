package com.mowmaster.dust.Features.CrystalBlocks.Item;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustElementAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.EffectScrolls.Item.ToolMagicHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
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

public class CrystalHalfSawItem extends Item {
    private static final Map<Block, Block> SAW_MAP =
            Map.ofEntries(
                    Map.entry(Blocks.ACACIA_PLANKS, Blocks.ACACIA_SLAB),
                    Map.entry(Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_SLAB),
                    Map.entry(Blocks.BIRCH_PLANKS, Blocks.BIRCH_SLAB),
                    Map.entry(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SLAB),
                    Map.entry(Blocks.CHERRY_PLANKS, Blocks.CHERRY_SLAB),
                    Map.entry(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SLAB),
                    Map.entry(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_SLAB),
                    Map.entry(Blocks.OAK_PLANKS, Blocks.OAK_SLAB),
                    Map.entry(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SLAB),
                    Map.entry(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_SLAB),
                    Map.entry(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_SLAB),
                    Map.entry(Blocks.WARPED_PLANKS, Blocks.WARPED_SLAB),
                    Map.entry(DustBlockRegistry.BLOCK_PLANKS_GREEN.get(), DustBlockRegistry.BLOCK_SLAB_GREEN.get())
            );


    public CrystalHalfSawItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        //Called when rightclicking on a block with this item
        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();

        if(!level.isClientSide()) {
            if(SAW_MAP.containsKey(clickedBlock))
            {
                if(SAW_MAP.get(clickedBlock) instanceof SlabBlock slabBlock)
                {
                    if(context.getPlayer() instanceof ServerPlayer serverPlayer
                    && ToolMagicHelper.toolUseOn((ServerLevel)level,serverPlayer,context, EnumElement.CHAOS,1,1,1))
                    {
                        BlockState state = slabBlock.defaultBlockState().trySetValue(SlabBlock.TYPE,SlabType.DOUBLE);
                        level.setBlockAndUpdate(context.getClickedPos(), state);
                        return InteractionResult.SUCCESS;
                    }
                }
            } else if (clickedBlock instanceof SlabBlock slabIn) {
                if(slabIn.defaultBlockState().is(BlockTags.WOODEN_SLABS))
                {
                    if(slabIn.withPropertiesOf(level.getBlockState(context.getClickedPos())).getValue(SlabBlock.TYPE).equals(SlabType.DOUBLE))
                    {
                        if(context.getPlayer() instanceof ServerPlayer serverPlayer
                        && ToolMagicHelper.toolUseOn((ServerLevel)level,serverPlayer,context, EnumElement.CHAOS,1,1,1))
                        {
                            level.addFreshEntity(new ItemEntity(level,context.getClickedPos().getX(),context.getClickedPos().getY(),context.getClickedPos().getZ(), new ItemStack(Items.STICK,4)));
                            level.setBlockAndUpdate(context.getClickedPos(), Blocks.AIR.defaultBlockState());
                            return InteractionResult.SUCCESS;
                        }
                    }
                    else{
                        if(context.getPlayer() instanceof ServerPlayer serverPlayer
                                && ToolMagicHelper.toolUseOn((ServerLevel)level,serverPlayer,context, EnumElement.CHAOS,1,1,1)){
                            level.addFreshEntity(new ItemEntity(level,context.getClickedPos().getX(),context.getClickedPos().getY(),context.getClickedPos().getZ(), new ItemStack(Items.STICK,2)));
                            level.setBlockAndUpdate(context.getClickedPos(), Blocks.AIR.defaultBlockState());
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        if(SAW_MAP.containsKey(clickedBlock))level.playSound(context.getPlayer(),context.getClickedPos(), SoundEvents.CRAFTER_CRAFT, SoundSource.BLOCKS, 2f, 1f);

        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if(Minecraft.getInstance().hasShiftDown())
        {
            builder.accept(Component.translatable(DustReferences.TOOLTIP_SHIFTDOWNMESSAGE + "halfsaw_iron"));
        }
        else {
            builder.accept(DustReferences.TOOLTIP_SHIFTPROMPT);
        }
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }
}
