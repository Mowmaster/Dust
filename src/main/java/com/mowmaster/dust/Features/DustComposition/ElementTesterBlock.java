package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.Features.Machines.ProductSource;
import com.mowmaster.dust.Features.Machines.StationInteractions;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Element Tester: a workstation that's upgraded by what's put on it. Its first use is the Elemental Examiner.
//   Right-click the top with a crystal    sets it on the table (up to five, one of each element). The model changes
//                                         with how many there are (CRYSTALS), and each crystal takes its element's colour.
//   Right-click the plate with an item    (once it has a crystal) puts one on it. From the plate a gauge of lines runs to
//                                         each crystal, lit by how much of that crystal's element the item is made of;
//                                         at 100% the crystal sparkles.
//   Left-click the plate / a crystal      takes the item / that crystal back (see StationInteractions)
// The examiner only sees the elements an item is made of directly (blue wool = creature + protection + water: 33%
// water, nothing else, even with all five primal crystals on). Whoever puts an item on while carrying Research Notes
// notes what the crystals show, and discovers a crystal's element when the item is all that element.
public class ElementTesterBlock extends Block implements EntityBlock
{
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty CRYSTALS = IntegerProperty.create("crystals", 0, ExaminerLayout.MAX_CRYSTALS);

    // The table top and its four legs (Block.box takes pixels: minX, minY, minZ, maxX, maxY, maxZ)
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 8, 0, 16, 12, 16),
            Block.box(0, 0, 0, 4, 8, 4), Block.box(12, 0, 0, 16, 8, 4),
            Block.box(0, 0, 12, 4, 8, 16), Block.box(12, 0, 12, 16, 8, 16));

    public ElementTesterBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(CRYSTALS, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, CRYSTALS);
    }

    // Faces the way the player looks, so the plate is on their side and the crystals beyond it
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
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

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ElementTesterBlockEntity(pos, state);
    }

    // ---- using it ----

    // Crystals and items go where they're clicked (the block entity's slots; StationInteractions also handles
    // left-clicks to take things off)
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return StationInteractions.rightClick(stack, level, pos, player, hit);
    }

    // What the crystals show goes into the player's research (DustResearch.noteMeasured)
    public static void report(ServerPlayer player, ElementTesterBlockEntity tester, ItemStack item)
    {
        DustComposition composition = DustCompositions.of(item);
        if (composition == null) {
            // sendOverlayMessage: shown over the hotbar (the "action bar") rather than in chat
            player.sendOverlayMessage(Component.translatable("dust.element_tester.no_dust", item.getHoverName()));
            return;
        }
        // LinkedHashMap keeps the crystals' order
        Map<DustElement, Integer> measured = new LinkedHashMap<>();
        for (DustElement crystal : tester.crystals()) measured.put(crystal, tester.percent(crystal));
        // A product's findings go on what it was made from (crushed stone's on stone)
        DustResearch.noteMeasured(player, ResearchTabs.entryForItem(ProductSource.original(item)), measured);
    }

    // A crystal whose element is all of the item sparkles in its colour (client only; called every frame or so)
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (!(level.getBlockEntity(pos) instanceof ElementTesterBlockEntity tester) || tester.getItem().isEmpty()) return;
        List<DustElement> crystals = tester.crystals();
        ExaminerLayout layout = ExaminerLayout.of(crystals.size());
        if (layout == null) return;
        for (int i = 0; i < crystals.size(); i++) {
            if (tester.percent(crystals.get(i)) < 100 || random.nextInt(2) != 0) continue;
            Vec3 at = ExaminerLayout.turned(layout.crystals()[i], state.getValue(FACING));
            level.addParticle(new DustParticleOptions(crystals.get(i).color(), 0.8F),
                    pos.getX() + at.x + (random.nextDouble() - 0.5) * 0.2, pos.getY() + at.y + (random.nextDouble() - 0.3) * 0.2,
                    pos.getZ() + at.z + (random.nextDouble() - 0.5) * 0.2, 0.0, 0.02, 0.0);
        }
    }
}
