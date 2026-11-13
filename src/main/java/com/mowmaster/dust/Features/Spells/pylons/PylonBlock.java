package com.mowmaster.dust.Features.Spells.pylons;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

// A modifier pylon (Shape, Change, Cost, Count, Damage: PylonType). Placed where a Spell Pylon shows a ghost, it joins
// that tree and gets its roll (SpellPylonBlockEntity.attach); anywhere else it stays inert. Right-click: what it holds
// (the Shape Pylon: the next shape you know). Break it to reroll it (it won't roll the same there again).
// A spell break needs redstone power for the line to carry on past it.
public class PylonBlock extends Block implements EntityBlock
{
    public static final EnumProperty<PylonState> STATE = EnumProperty.create("state", PylonState.class);

    private final PylonType type;

    public PylonBlock(Properties properties, PylonType type)
    {
        super(properties);
        this.type = type;
        registerDefaultState(stateDefinition.any().setValue(STATE, PylonState.INERT));
    }

    public PylonType type()
    {
        return type;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(STATE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new PylonBlockEntity(pos, state);
    }

    // Placed: if it's in one of a neighbouring tree's ghost places, it joins that tree
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide() || !(level.getBlockEntity(pos) instanceof PylonBlockEntity pylon)) return;
        SpellPylonBlockEntity tree = treeNextTo(level, pos);
        if (tree != null && tree.accepts(pos, type)) {
            tree.attach(pylon, placer instanceof ServerPlayer player ? player : null);
        } else if (placer instanceof ServerPlayer player) {
            player.sendOverlayMessage(Component.translatable("dust.pylon.not_connected"));
        }
    }

    // The tree whose Spell Pylon, or any of whose pylons, is beside pos
    private static @Nullable SpellPylonBlockEntity treeNextTo(Level level, BlockPos pos)
    {
        for (Direction side : Direction.Plane.HORIZONTAL) {
            BlockEntity beside = level.getBlockEntity(pos.relative(side));
            if (beside instanceof SpellPylonBlockEntity spellPylon) return spellPylon;
            if (beside instanceof PylonBlockEntity pylon && pylon.main() != null
                    && level.getBlockEntity(pylon.main()) instanceof SpellPylonBlockEntity spellPylon) {
                return spellPylon;
            }
        }
        return null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof PylonBlockEntity pylon)) return InteractionResult.PASS;
        if (player instanceof ServerPlayer serverPlayer) {
            if (type == PylonType.SHAPE && pylon.main() != null && level.getBlockEntity(pylon.main()) instanceof SpellPylonBlockEntity tree) {
                tree.cycleShape(pylon, serverPlayer);
            } else {
                serverPlayer.sendOverlayMessage(SpellPylonBlockEntity.describe(pylon));
            }
        }
        return InteractionResult.SUCCESS;
    }

    // Redstone changing next to a spell break: its look follows (on / off)
    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbor, @Nullable Orientation orientation, boolean moving)
    {
        super.neighborChanged(state, level, pos, neighbor, orientation, moving);
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof PylonBlockEntity pylon && pylon.isSpellBreak()) pylon.updateState();
    }

    // Blessed pylons glint gold, corrupted ones smoke, powered spell breaks spark (client only)
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        PylonState look = state.getValue(STATE);
        double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.6, y = pos.getY() + 0.6 + random.nextDouble() * 0.6;
        double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.6;
        switch (look) {
            case BLESSED -> level.addParticle(new DustParticleOptions(0xFFD54A, 0.6F), x, y, z, 0, 0.02, 0);
            case CORRUPTED -> level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0.02, 0);
            case BREAK_ON -> level.addParticle(new DustParticleOptions(0xFF2020, 0.7F), x, y, z, 0, 0.01, 0);
            default -> {}
        }
    }
}
