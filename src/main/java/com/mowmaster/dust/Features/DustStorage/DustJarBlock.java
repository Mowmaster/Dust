package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// The Dust Jar (models/block/station/dust_jar.json): 1000 dust of one element, its lid in the element's colour.
// The clicks are in DustJarInteractions; the dust in DustJarBlockEntity. Broken, it drops itself with its dust (the
// loot table copies the dust:dust_jar component from the block entity).
public class DustJarBlock extends Block implements EntityBlock
{
    // The jar and its lid knob: 3-13 across, 14 high
    private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 14, 13);

    public DustJarBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DustJarBlockEntity(pos, state);
    }

    // Element dust pours in; anything else does its own thing (TRY_WITH_EMPTY_HAND: carry on as if the hand were empty)
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof DustJarBlockEntity jar)) return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (level.isClientSide()) {
            // The client only guesses (the arm swings); the server pours
            DustElement dust = CrystalElements.ofDust(stack);
            return dust != null && jar.accepts(dust) ? InteractionResult.SUCCESS : InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        return DustJarInteractions.pour(jar, stack, player) ? InteractionResult.SUCCESS : InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    // Empty hand: empties the player's dust store of the jar's element into it. Sneaking (only reaches here with both
    // hands empty): picks the jar up, dust and all; an empty jar with an element clears it first, then is picked up.
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof DustJarBlockEntity jar)) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (!player.isShiftKeyDown()) {
            DustJarInteractions.storeFromPlayer(jar, player);
        } else if (!jar.clearElement()) {
            // clearElement only clears an empty jar with an element; anything else is picked up
            jar.pickUp((ServerLevel) level, pos, state, player);
        }
        return InteractionResult.SUCCESS;
    }

    // Comparators read how full it is (0-15)
    @Override
    protected boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, net.minecraft.core.Direction direction)
    {
        if (!(level.getBlockEntity(pos) instanceof DustJarBlockEntity jar) || jar.amount() <= 0) return 0;
        return 1 + jar.amount() * 14 / DustJarContents.CAPACITY;
    }
}
