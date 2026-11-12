package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.Features.Machines.ProductSource;
import com.mowmaster.mowlib.datamaps.CBBlockColor;
import com.mowmaster.mowlib.datamaps.CBDataMaps;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

// An item model tint: the colour of the block a product was made from (its dust:source), so crushed stone is grey and
// crushed dirt brown. The colour comes from MowLib's block colours (the mowlib:block_colors data map: each plain block's
// average texture colour), else the block's map colour. Used in the item model as {"type": "dust:source_block"}.
// The tint is 0xAARRGGBB; white (-1) leaves the texture as it is.
public record SourceBlockTint() implements ItemTintSource
{
    // No settings: a "unit" codec that always gives the one tint
    public static final MapCodec<SourceBlockTint> MAP_CODEC = MapCodec.unit(new SourceBlockTint());

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity)
    {
        ItemStack source = ProductSource.original(stack);
        if (!(source.getItem() instanceof BlockItem blockItem)) return -1;
        Block block = blockItem.getBlock();
        CBBlockColor color = block.builtInRegistryHolder().getData(CBDataMaps.BLOCK_COLORS);
        int rgb = color != null ? color.rgb() : block.defaultMapColor().col;
        return 0xFF000000 | rgb;
    }

    @Override
    public MapCodec<SourceBlockTint> type()
    {
        return MAP_CODEC;
    }
}
