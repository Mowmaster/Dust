package com.mowmaster.dust.Features.CrystalBlocks.Item;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustDataGen.DustTags;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;
import java.util.function.Consumer;

public class CrystalChiselBaseItem extends Item {
    private static final Map<Block, Block> CHISEL_MAP =
            Map.of(
                    Blocks.STONE, Blocks.STONE_BRICKS,
                    Blocks.TERRACOTTA, Blocks.BRICKS,
                    DustBlockRegistry.BLOCK_OF_CRYSTAL_BLUE.get(), DustBlockRegistry.CRYSTAL_PATH_TIER1.get()
            );

    private static final Map<Block, Block> CHISEL_DUST =
            Map.of(
                    DustBlockRegistry.CRYSTAL_PATH_TIER1.get(), DustBlockRegistry.CRYSTAL_PATH_TIER2.get()
            );
    private static final Map<Block, Item> CHISEL_DUST_COST =
            Map.of(
                    DustBlockRegistry.CRYSTAL_PATH_TIER2.get(), DustItemRegistry.DUST_BLUE.get()
            );

    private static final Map<Block, Block> CHISEL_CRYSTAL =
            Map.of(
                    DustBlockRegistry.CRYSTAL_PATH_TIER2.get(), DustBlockRegistry.CRYSTAL_PATH_TIER3.get()
            );
    private static final Map<Block, Item> CHISEL_CRYSTAL_COST =
            Map.of(
                    DustBlockRegistry.CRYSTAL_PATH_TIER3.get(), DustItemRegistry.CRYSTAL_BLUE.get()
            );

    private static final Map<Block, Block> CHISEL_CRYSTALBLOCK =
            Map.of(
                    DustBlockRegistry.CRYSTAL_PATH_TIER3.get(), DustBlockRegistry.CRYSTAL_PATH_TIER4.get()
            );

    public CrystalChiselBaseItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        //Called when rightclicking on a block with this item
        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();

        if(!level.isClientSide()) {
            if(CHISEL_MAP.containsKey(clickedBlock))
            {
                level.setBlockAndUpdate(context.getClickedPos(), CHISEL_MAP.get(clickedBlock).defaultBlockState());
                context.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), context.getPlayer(),
                        item -> context.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                level.playSound(context.getPlayer(),context.getClickedPos(), SoundEvents.ANVIL_HIT, SoundSource.BLOCKS, 2f, 1f);
            }
            else if(CHISEL_DUST.containsKey(clickedBlock))
            {
                if(context.getPlayer().getOffhandItem().is(CHISEL_DUST_COST.get(CHISEL_DUST.get(clickedBlock))))
                {
                    level.setBlockAndUpdate(context.getClickedPos(), CHISEL_DUST.get(clickedBlock).defaultBlockState());
                    context.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), context.getPlayer(),
                            item -> context.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                    context.getPlayer().getOffhandItem().shrink(1);
                }
            }
            else if(CHISEL_CRYSTAL.containsKey(clickedBlock))
            {
                if(context.getPlayer().getOffhandItem().is(CHISEL_CRYSTAL_COST.get(CHISEL_CRYSTAL.get(clickedBlock))))
                {
                    level.setBlockAndUpdate(context.getClickedPos(), CHISEL_CRYSTAL.get(clickedBlock).defaultBlockState());
                    context.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), context.getPlayer(),
                            item -> context.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                    context.getPlayer().getOffhandItem().shrink(1);
                }
            }
            else if(CHISEL_CRYSTALBLOCK.containsKey(clickedBlock))
            {
                if(context.getPlayer().getOffhandItem().getItem() instanceof BlockItem blockIn)
                {
                    if(blockIn.getBlock().defaultBlockState().is(DustTags.Blocks.MAGICAL_CRYSTAL_BLOCKS))
                    {
                        level.setBlockAndUpdate(context.getClickedPos(), CHISEL_CRYSTALBLOCK.get(clickedBlock).defaultBlockState());
                        context.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), context.getPlayer(),
                                item -> context.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                        context.getPlayer().getOffhandItem().shrink(1);
                    }
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if(Minecraft.getInstance().hasShiftDown())
        {
            builder.accept(Component.translatable(DustReferences.TOOLTIP_SHIFTDOWNMESSAGE + "chisel_iron"));
        }
        else {
            builder.accept(DustReferences.TOOLTIP_SHIFTPROMPT);
        }
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }
}
