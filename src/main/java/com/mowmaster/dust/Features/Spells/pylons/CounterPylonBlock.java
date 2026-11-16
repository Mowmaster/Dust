package com.mowmaster.dust.Features.Spells.pylons;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The Blessed Pylon (an angel on top) and the Corrupted Pylon (a devil): placed near a Spell Pylon (within RANGE), its
// nodes show how far that tree's blessing (to SpellDeck.BLESS_AT) or corruption (to SpellDeck.CORRUPT_AT) has come:
// the blessed nodes white, turning blue; the corrupted ones black, turning red (Client/CounterPylonTint). It looks for
// the nearest Spell Pylon once a second (a scheduled tick) and keeps the count in its LEVEL, so the block redraws when
// it changes. Looked at, MowLib's reading HUD shows the count. Not part of any spell.
public class CounterPylonBlock extends Block implements com.mowmaster.mowlib.reading.IReadableBlock
{
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    // How far the tree's counter has come (0 to the counter's limit)
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, SpellDeck.BLESS_AT);
    // How far it looks for a Spell Pylon (blocks, each way)
    public static final int RANGE = 8;
    private static final int CHECK_TICKS = 20;
    // The pylon (the angel / devil reaches higher, but the outline is the pylon)
    private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 15, 13);

    private final boolean blessed;

    public CounterPylonBlock(Properties properties, boolean blessed)
    {
        super(properties);
        this.blessed = blessed;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LEVEL, 0));
    }

    public boolean blessed() { return blessed; }

    // How many nodes it has: one per step of its counter
    public int limit()
    {
        return blessed ? SpellDeck.BLESS_AT : SpellDeck.CORRUPT_AT;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, LEVEL);
    }

    // The angel / devil faces whoever places it
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    // ---- keeping count ----

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
    {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide()) level.scheduleTick(pos, this, 1);
    }

    // Once a second: the nearest Spell Pylon's count, into LEVEL if it changed; then again in a second
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        SpellPylonBlockEntity tree = nearestTree(level, pos);
        int count = tree == null ? 0 : Math.min(limit(), blessed ? tree.blessed() : tree.corruption());
        if (count != state.getValue(LEVEL)) level.setBlock(pos, state.setValue(LEVEL, count), Block.UPDATE_CLIENTS);
        level.scheduleTick(pos, this, CHECK_TICKS);
    }

    // The nearest Spell Pylon within RANGE (null if none)
    public static @Nullable SpellPylonBlockEntity nearestTree(Level level, BlockPos pos)
    {
        SpellPylonBlockEntity best = null;
        double bestDistance = Double.MAX_VALUE;
        // betweenClosed: every position in the box (the same mutable BlockPos each time, so it isn't kept)
        for (BlockPos at : BlockPos.betweenClosed(pos.offset(-RANGE, -RANGE, -RANGE), pos.offset(RANGE, RANGE, RANGE))) {
            if (level.getBlockEntity(at) instanceof SpellPylonBlockEntity tree) {
                double distance = at.distSqr(pos);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = tree;
                }
            }
        }
        return best;
    }

    // ---- MowLib's reading HUD ----

    @Override
    public com.mowmaster.mowlib.reading.@Nullable ReadingPaper read(com.mowmaster.mowlib.reading.ReadingContext context)
    {
        if (SpellPylonReading.holdsResearchBook(context.player())) return null;
        Component title = getName().copy().withStyle(ChatFormatting.BOLD);
        SpellPylonBlockEntity tree = nearestTree(context.level(), context.pos());
        if (tree == null) return com.mowmaster.mowlib.reading.ReadingPaper.of(List.of(title, Component.translatable("hud.dust.counter.none").withStyle(ChatFormatting.ITALIC)));
        Component count = blessed ? Component.translatable("hud.dust.counter.blessed", tree.blessed(), SpellDeck.BLESS_AT).withStyle(ChatFormatting.BLUE)
                : Component.translatable("hud.dust.counter.corrupted", tree.corruption(), SpellDeck.CORRUPT_AT).withStyle(ChatFormatting.DARK_RED);
        return com.mowmaster.mowlib.reading.ReadingPaper.of(List.of(title, count));
    }
}
