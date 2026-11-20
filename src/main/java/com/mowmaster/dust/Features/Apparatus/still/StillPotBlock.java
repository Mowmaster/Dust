package com.mowmaster.dust.Features.Apparatus.still;

import com.mowmaster.dust.Features.Apparatus.ApparatusBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;

// The Still Pot (models/block/apparatus/still_pot.json): the still's working block (StillPotBlockEntity), an apparatus
// like the others (intake on top, status with an empty hand, a redstone signal pauses it), but tubes don't join on: its
// dust goes up into the towers, and its soot into the containers beside it.
public class StillPotBlock extends ApparatusBlock
{
    public StillPotBlock(Properties properties)
    {
        super(properties, Shapes.or(Block.box(0, 0, 0, 16, 16, 16)), StillPotBlockEntity::new);
    }

    @Override
    public boolean connectsDustTube(BlockState state, Direction side)
    {
        return false;
    }
}
