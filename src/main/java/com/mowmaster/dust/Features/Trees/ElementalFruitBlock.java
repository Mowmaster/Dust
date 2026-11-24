package com.mowmaster.dust.Features.Trees;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.Magic.ChunkAura;
import com.mowmaster.dust.Features.Magic.ElementAura;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

// A magical fruit growing on an elemental tree (ElementalWood): hanging under its leaves (FACING up, towards what holds
// it) or from the side of its trunk (FACING the trunk). It only grows on a tree as the tree grows (ElementalFruitDecorator);
// the fruit itself can't be planted.
// It ripens (AGE 0 to RIPE) only by drawing its element out of the aura: on a random tick (1 in RIPEN_ODDS), if the chunk
// holds more than its normal of the fruit's primal (for an element of two primals, of either one), it takes one dust of
// that primal and grows a stage. Right-click a ripe one to pick it: the fruit (ElementalFruitItem) drops and it starts
// again from the first stage. Broken ripe, it drops its fruit; unripe, nothing. Falls if what holds it goes.
// Models: models/block/fruit/fruit_{hanging,side}_stage<age>.json, the fruit tinted its element's colour.
public class ElementalFruitBlock extends Block
{
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    public static final int RIPE = 2;
    public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class, side -> side != Direction.DOWN);
    private static final int RIPEN_ODDS = 3;

    // Shapes for each stage: hanging (support above), and on the side of a trunk to the north (turned for the others)
    private static final double[][] HANGING = {{6, 10, 6, 10, 16, 10}, {5, 7, 5, 11, 16, 11}, {4, 5, 4, 12, 16, 12}};
    private static final double[][] SIDE_NORTH = {{6, 7, 0, 10, 12, 5}, {5, 5, 0, 11, 12, 7}, {4, 3, 0, 12, 12, 9}};
    private static final Map<Direction, VoxelShape[]> SHAPES = new EnumMap<>(Direction.class);

    static {
        VoxelShape[] hanging = new VoxelShape[3];
        for (int age = 0; age <= RIPE; age++) hanging[age] = box(HANGING[age]);
        SHAPES.put(Direction.UP, hanging);
        for (Direction side : Direction.Plane.HORIZONTAL) {
            VoxelShape[] shapes = new VoxelShape[3];
            for (int age = 0; age <= RIPE; age++) shapes[age] = box(turn(SIDE_NORTH[age], side));
            SHAPES.put(side, shapes);
        }
    }

    private final ElementalWood wood;
    private final Supplier<? extends Item> fruit;

    public ElementalFruitBlock(ElementalWood wood, Supplier<? extends Item> fruit, Properties properties)
    {
        super(properties);
        this.wood = wood;
        this.fruit = fruit;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(AGE, 0));
    }

    public ElementalWood wood()
    {
        return wood;
    }

    private static VoxelShape box(double[] b)
    {
        return Block.box(b[0], b[1], b[2], b[3], b[4], b[5]);
    }

    // A box drawn against the north side, turned to lie against another side (as the blockstate turns the model)
    private static double[] turn(double[] b, Direction side)
    {
        return switch (side) {
            case EAST -> new double[]{16 - b[5], b[1], b[0], 16 - b[2], b[4], b[3]};
            case SOUTH -> new double[]{16 - b[3], b[1], 16 - b[5], 16 - b[0], b[4], 16 - b[2]};
            case WEST -> new double[]{b[2], b[1], 16 - b[3], b[5], b[4], 16 - b[0]};
            default -> b;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, AGE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state.getValue(FACING))[state.getValue(AGE)];
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state)
    {
        return state.getValue(AGE) < RIPE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (random.nextInt(RIPEN_ODDS) != 0) return;
        EnumElement source = feedingPrimal(level, pos, wood.element());
        if (source == null) return;
        ChunkAura.add(level, pos, source, -1);
        level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), Block.UPDATE_CLIENTS);
        level.sendParticles(new DustParticleOptions(wood.element().color(), 0.8F), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                5, 0.25, 0.25, 0.25, 0);
    }

    // The over-full primal a fruit of this element ripens on: its element's primal, or (for an element of two) the fuller
    // of the two, if the chunk holds more than its normal of it; null if neither
    public static @Nullable EnumElement feedingPrimal(Level level, BlockPos pos, DustElement element)
    {
        EnumElement best = null;
        float fullest = 1.0F;
        for (EnumElement primal : ElementAura.primalsOf(element)) {
            float fullness = ElementAura.fullness(level, pos, primal);
            if (fullness > fullest) {
                fullest = fullness;
                best = primal;
            }
        }
        return best;
    }

    // Held by its own tree: its wood's log or bark (wood) or leaves
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        return holds(level.getBlockState(pos.relative(state.getValue(FACING))));
    }

    public boolean holds(BlockState support)
    {
        WoodSet set = ElementalWoods.set(wood);
        return support.is(set.log().get()) || support.is(set.wood().get()) || support.is(set.leaves().get());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour,
                                     BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        if (directionToNeighbour == state.getValue(FACING) && !state.canSurvive(level, pos)) return Blocks.AIR.defaultBlockState();
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    // Picking a ripe fruit
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (state.getValue(AGE) < RIPE) return InteractionResult.PASS;
        if (!level.isClientSide()) {
            popResource(level, pos, new ItemStack(fruit.get()));
            level.playSound(null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
            BlockState picked = state.setValue(AGE, 0);
            level.setBlock(pos, picked, Block.UPDATE_CLIENTS);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, picked));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData)
    {
        return new ItemStack(fruit.get());
    }
}
