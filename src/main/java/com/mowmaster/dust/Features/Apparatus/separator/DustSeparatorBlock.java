package com.mowmaster.dust.Features.Apparatus.separator;

import com.mowmaster.dust.Features.Apparatus.ApparatusBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

// The Dust Separator (models/block/apparatus/dust_seperator.json; the block draws its base, dust_seperator_base.json,
// and Client/DustSeparatorRenderer its turning drum): a tumbling drum that separates what's fed into it
// (DustSeparatorBlockEntity). Fed from its sides and top (the blue crystal marks its intake end, to the left of its
// front); tubes join on underneath only (the red crystal), where each drum section's element goes; up to
// MAX_EXTENSIONS Dust Separator Extensions lengthen the drum from its open end, to the right of its front.
// A Jar Label under a section keeps it to its element; shears scrape it off.
public class DustSeparatorBlock extends ApparatusBlock
{
    public static final int MAX_EXTENSIONS = 5;

    public DustSeparatorBlock(Properties properties)
    {
        super(properties, Block.box(0, 0, 0, 16, 14, 16), DustSeparatorBlockEntity::new);
    }

    // The way the drum runs from its intake to its open end (to the right of its front)
    public static Direction openSide(BlockState state)
    {
        return state.getValue(FACING).getClockWise();
    }

    @Override
    public boolean connectsDustTube(BlockState state, Direction side)
    {
        return side == Direction.DOWN;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        InteractionResult labelled = SectionLabels.use(stack, level, pos, player, hand);
        return labelled != null ? labelled : super.useItemOn(stack, state, level, pos, player, hand, hit);
    }
}
