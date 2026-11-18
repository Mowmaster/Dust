package com.mowmaster.dust.Features.Lamps;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;

// The Lovely Lamp: LovelyLampBlockEntity does the work
public class LovelyLampBlock extends LampBlock
{
    public LovelyLampBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected BlockEntityType<? extends LampBlockEntity> lampType()
    {
        return DustBlockEntityRegistry.LOVELY_LAMP.get();
    }

    // Stonecutter: before 26.3, blocks were saved with a codec (MapCodec), and LanternBlock makes every subclass give
    // one. simpleCodec builds one that only stores the block properties. 26.3 dropped block codecs.
    //? if <26.3 {
    /*public static final com.mojang.serialization.MapCodec<LovelyLampBlock> CODEC = simpleCodec(LovelyLampBlock::new);

    @Override
    public com.mojang.serialization.MapCodec<LovelyLampBlock> codec()
    {
        return CODEC;
    }
    *///?}
}
