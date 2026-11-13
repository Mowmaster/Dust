package com.mowmaster.dust.Features.Spells.blocks;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

// What a veiled / phased block really is, so it can come back when the spell ends
public class PhantomBlockEntity extends BlockEntity
{
    private BlockState original = Blocks.STONE.defaultBlockState();

    public PhantomBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.PHANTOM.get(), pos, state);
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
