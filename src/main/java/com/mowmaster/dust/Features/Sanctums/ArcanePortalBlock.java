package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Labyrinth.Labyrinth;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

// The way into an element's labyrinth, and out of it: a sheet like a nether portal (standing along its AXIS) in its
// element's colour (SET). A sanctum's portal opens when its spawners are beaten (SanctumKeystoneBlockEntity) and takes a
// player who steps in to a labyrinth (Labyrinth.enter); an EXIT portal (in a labyrinth) takes them back to where they
// came in (Labyrinth.leave). It can't be broken.
public class ArcanePortalBlock extends Block
{
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final EnumProperty<ElementalArmorSet> SET = ArcaneSpawnerBlock.SET;
    public static final BooleanProperty EXIT = BooleanProperty.create("exit");
    private static final VoxelShape X_SHAPE = Block.box(0, 0, 6, 16, 16, 10);
    private static final VoxelShape Z_SHAPE = Block.box(6, 0, 0, 10, 16, 16);

    public ArcanePortalBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(AXIS, Direction.Axis.X).setValue(SET, ElementalArmorSet.PYREBORN).setValue(EXIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(AXIS, SET, EXIT);
    }

    public static BlockState state(Direction.Axis axis, ElementalArmorSet set, boolean exit)
    {
        return Sanctums.ARCANE_PORTAL.get().defaultBlockState().setValue(AXIS, axis).setValue(SET, set).setValue(EXIT, exit);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return state.getValue(AXIS) == Direction.Axis.Z ? Z_SHAPE : X_SHAPE;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effects, boolean precise)
    {
        if (!(level instanceof ServerLevel server) || !(entity instanceof ServerPlayer player)) return;
        if (player.isOnPortalCooldown() || player.isPassenger() || player.isVehicle()) return;
        player.setPortalCooldown();
        if (state.getValue(EXIT)) {
            Labyrinth.leave(player);
            return;
        }
        SanctumKeystoneBlockEntity keystone = keystone(server, pos);
        if (keystone != null) Labyrinth.enter(player, keystone);
    }

    // The keystone in the frame's bottom row, below this portal
    private static @Nullable SanctumKeystoneBlockEntity keystone(ServerLevel level, BlockPos pos)
    {
        BlockPos bottom = pos;
        while (level.getBlockState(bottom).getBlock() instanceof ArcanePortalBlock && bottom.getY() > level.getMinY()) bottom = bottom.below();
        for (BlockPos near : BlockPos.betweenClosed(bottom.offset(-2, 0, -2), bottom.offset(2, 0, 2))) {
            if (level.getBlockEntity(near) instanceof SanctumKeystoneBlockEntity keystone) return keystone;
        }
        return null;
    }

    // Motes of its element drifting in it
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        int color = state.getValue(SET).element().color();
        boolean alongX = state.getValue(AXIS) == Direction.Axis.X;
        for (int i = 0; i < 2; i++) {
            double x = pos.getX() + (alongX ? random.nextDouble() : 0.5 + (random.nextDouble() - 0.5) * 0.3);
            double z = pos.getZ() + (alongX ? 0.5 + (random.nextDouble() - 0.5) * 0.3 : random.nextDouble());
            level.addParticle(new DustParticleOptions(color, 1.0F), x, pos.getY() + random.nextDouble(), z, 0, 0.02, 0);
        }
    }
}
