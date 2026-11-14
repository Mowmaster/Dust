package com.mowmaster.dust.Features.Spells.pylons;

import com.mowmaster.dust.Features.Spells.api.ModifierTier;
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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

// A modifier pylon (Shape, Change, Cost, Count, Damage: PylonType). Placed where a Spell Pylon shows a ghost, it joins
// that tree and gets its roll (SpellPylonBlockEntity.attach); anywhere else it stays inert. Right-click: what it holds
// (the Shape Pylon: the next shape you know). Sneak-right-click: fill one of its upgrade nodes, for a level (that locks
// it). Break an unlocked pylon to reroll it (it won't roll the same there again); a locked one can't be broken, only
// taken down with its Spell Pylon.
// A spell break needs redstone power for the line to carry on past it.
// Its look (block state; the tints come from the block entity, Client/PylonTint):
//   parent   the side its parent (the pylon before it) is on: the model is turned so its back arm points there
//   links    which of its other sides lead on to the next pylons (PylonLinks: the connection arms)
//   nodes    how many upgrade nodes it has (blank_pylon_..._level_<nodes>; the Shape Pylon ignores it)
public class PylonBlock extends Block implements EntityBlock
{
    public static final EnumProperty<Direction> PARENT = EnumProperty.create("parent", Direction.class, Direction.Plane.HORIZONTAL);
    public static final EnumProperty<PylonLinks> LINKS = EnumProperty.create("links", PylonLinks.class);
    public static final IntegerProperty NODES = IntegerProperty.create("nodes", 1, PylonConfig.MAX_NODES);

    private final PylonType type;

    public PylonBlock(Properties properties, PylonType type)
    {
        super(properties);
        this.type = type;
        registerDefaultState(stateDefinition.any().setValue(PARENT, Direction.SOUTH).setValue(LINKS, PylonLinks.NONE).setValue(NODES, 1));
    }

    public PylonType type()
    {
        return type;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(PARENT, LINKS, NODES);
    }

    // Until it joins a tree, its back arm points at the player (towards wherever they're building from)
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(PARENT, context.getHorizontalDirection().getOpposite());
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
            SpellPylonBlockEntity tree = pylon.main() != null && level.getBlockEntity(pylon.main()) instanceof SpellPylonBlockEntity spellPylon ? spellPylon : null;
            // isSecondaryUseActive: sneaking (the "use the other way" key)
            if (tree != null && player.isSecondaryUseActive() && type != PylonType.SHAPE) {
                tree.upgrade(pylon, serverPlayer);
            } else if (tree != null && type == PylonType.SHAPE) {
                tree.cycleShape(pylon, serverPlayer);
            } else {
                serverPlayer.sendOverlayMessage(SpellPylonBlockEntity.describe(pylon));
            }
        }
        return InteractionResult.SUCCESS;
    }

    // ---- locked: can't be broken (only with its Spell Pylon, which removes it itself) ----

    private static boolean isLocked(BlockGetter level, BlockPos pos)
    {
        return level.getBlockEntity(pos) instanceof PylonBlockEntity pylon && pylon.locked();
    }

    // How fast a player mines it: not at all while locked (like bedrock)
    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos)
    {
        return isLocked(level, pos) ? 0.0F : super.getDestroyProgress(state, player, level, pos);
    }

    // Creative players break blocks instantly, without mining: NeoForge asks the block here first (false: it stays)
    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, ItemStack tool, boolean willHarvest, FluidState fluid)
    {
        if (isLocked(level, pos)) {
            if (player instanceof ServerPlayer serverPlayer) serverPlayer.sendOverlayMessage(Component.translatable("dust.pylon.locked"));
            return false;
        }
        return super.onDestroyedByPlayer(state, level, pos, player, tool, willHarvest, fluid);
    }

    // Explosions don't take a locked pylon either
    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion)
    {
        return isLocked(level, pos) ? 3600000.0F : super.getExplosionResistance(state, level, pos, explosion);
    }

    // Redstone changing next to a spell break: whether it's powered (its colour follows)
    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbor, @Nullable Orientation orientation, boolean moving)
    {
        super.neighborChanged(state, level, pos, neighbor, orientation, moving);
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof PylonBlockEntity pylon && pylon.isSpellBreak()) pylon.updatePower();
    }

    // Blessed pylons glint purple, corrupted ones smoke, powered spell breaks spark (client only; the client has the
    // block entity's copy)
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (!(level.getBlockEntity(pos) instanceof PylonBlockEntity pylon) || pylon.main() == null) return;
        double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.6, y = pos.getY() + 0.6 + random.nextDouble() * 0.6;
        double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.6;
        if (pylon.isSpellBreak()) {
            if (pylon.powered()) level.addParticle(new DustParticleOptions(0xFF2020, 0.7F), x, y, z, 0, 0.01, 0);
        } else if (pylon.tier() == ModifierTier.BLESSED) {
            level.addParticle(new DustParticleOptions(0xB060FF, 0.6F), x, y, z, 0, 0.02, 0);
        } else if (pylon.tier() == ModifierTier.CORRUPTED) {
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0.02, 0);
        }
    }
}
