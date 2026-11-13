package com.mowmaster.dust.Features.Spells.blocks;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

// What a corrupted block was, so purifying it can bring it back
public class CorruptedBlockEntity extends BlockEntity
{
    private BlockState original = Blocks.STONE.defaultBlockState();

    public CorruptedBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.CORRUPTED.get(), pos, state);
    }

    public BlockState original() { return original; }

    public void setOriginal(BlockState original)
    {
        this.original = original;
        setChanged();
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.store("original", BlockState.CODEC, original);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        original = input.read("original", BlockState.CODEC).orElse(Blocks.STONE.defaultBlockState());
    }
}
