package com.mowmaster.dust.Features.Trees;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

// An elemental wood's sapling: grows into its tree (ElementalTrees) like any sapling, with bone meal or in time. The
// trees of hot dry lands (ElementalWood.growsOnSand) take root in sand and terracotta as well as dirt.
// A sapling grown where the aura is over-full in its element bears fruit (ElementalFruitDecorator).
public class ElementalSaplingBlock extends SaplingBlock
{
    private final ElementalWood wood;

    public ElementalSaplingBlock(ElementalWood wood, Properties properties)
    {
        super(ElementalTrees.grower(wood), properties);
        this.wood = wood;
    }

    public ElementalWood wood()
    {
        return wood;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
    {
        return super.mayPlaceOn(state, level, pos) || wood.growsOnSand() && (state.is(BlockTags.SAND) || state.is(BlockTags.TERRACOTTA));
    }
}
