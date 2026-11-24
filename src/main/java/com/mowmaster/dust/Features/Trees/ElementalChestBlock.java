package com.mowmaster.dust.Features.Trees;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// A chest of one of the elemental woods: a vanilla chest (two side by side make a double chest, but only of the same
// wood) drawn in its wood's colours (Client/ElementalChestRenderer, textures entity/chest/<wood>[_left|_right]). All of
// them share one block entity type (dust:elemental_chest).
public class ElementalChestBlock extends ChestBlock
{
    private final ElementalWood wood;

    public ElementalChestBlock(ElementalWood wood, Properties properties)
    {
        super(DustBlockEntityRegistry.ELEMENTAL_CHEST::get, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE, properties);
        this.wood = wood;
    }

    public ElementalWood wood()
    {
        return wood;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        return new ElementalChestBlockEntity(worldPosition, blockState);
    }
}
