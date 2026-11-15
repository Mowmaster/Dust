package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.mowlib.keys.IKeyPlaceable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

// The Dust Jar's item: placed like any block, or with MowLib's place key (IKeyPlaceable). Carries its dust in the
// dust:dust_jar component (the block entity reads it back when placed) and shows it in the tooltip.
public class DustJarItem extends BlockItem implements IKeyPlaceable
{
    public DustJarItem(Block block, Properties properties)
    {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        super.appendHoverText(stack, context, display, builder, flag);
        DustJarContents contents = stack.getOrDefault(DustComponentDataRegistry.DUST_JAR.get(), DustJarContents.EMPTY);
        Component line = contents.element().isEmpty()
                ? Component.translatable("dust.dust_jar.empty")
                : Component.translatable("dust.dust_jar.contents", contents.element().get().name(), contents.amount(), DustJarContents.CAPACITY);
        builder.accept(line.copy().withStyle(ChatFormatting.GRAY));
    }
}
