package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.DustComposition.ElementItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

// A sanctum's Arcane Vault, like a trial chamber's vault (ArcaneVaultBlockEntity does the work): a Vault Key of its
// element (VaultKeyItem) opens it once for each player, and it gives out loot as good as that player's Magic Level
// (VaultLoot), one thing at a time, out of its front. Its element is its SET; FACING is the way its keyhole faces; its
// STATE shows what it's doing:
//   INACTIVE   nobody near who can still open it (its runes dim)
//   ACTIVE     someone near who can (its runes light up, and a glimpse of what it holds turns inside)
//   UNLOCKING  a key just went in
//   EJECTING   giving out the loot
// It can't be mined for anything. In creative, use an element's crystal on it to change its element.
public class ArcaneVaultBlock extends Block implements EntityBlock
{
    public enum State implements StringRepresentable
    {
        INACTIVE("inactive"), ACTIVE("active"), UNLOCKING("unlocking"), EJECTING("ejecting");

        private final String name;

        State(String name)
        {
            this.name = name;
        }

        // Lit up: anything but waiting in the dark
        public boolean lit()
        {
            return this != INACTIVE;
        }

        @Override
        public String getSerializedName()
        {
            return name;
        }
    }

    public static final EnumProperty<ElementalArmorSet> SET = ArcaneSpawnerBlock.SET;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<State> STATE = EnumProperty.create("vault_state", State.class);

    public ArcaneVaultBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(SET, ElementalArmorSet.PYREBORN).setValue(FACING, Direction.NORTH).setValue(STATE, State.INACTIVE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(SET, FACING, STATE);
    }

    // The keyhole faces whoever places it
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
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ArcaneVaultBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (level.isClientSide() || type != Sanctums.ARCANE_VAULT_ENTITY.get()) return null;
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof ArcaneVaultBlockEntity vault && tickLevel instanceof ServerLevel server) vault.serverTick(server, tickState);
        };
    }

    // A key goes in (ArcaneVaultBlockEntity.tryUnlock says whether it opens); in creative, a crystal changes its element
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (player.getAbilities().instabuild && stack.getItem() instanceof ElementItem item) {
            ElementalArmorSet set = ElementalArmorSet.of(item.element());
            if (set == null) return InteractionResult.TRY_WITH_EMPTY_HAND;
            if (!level.isClientSide()) level.setBlock(pos, state.setValue(SET, set), Block.UPDATE_ALL);
            return InteractionResult.SUCCESS;
        }
        if (!(stack.getItem() instanceof VaultKeyItem)) return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (level instanceof ServerLevel server && server.getBlockEntity(pos) instanceof ArcaneVaultBlockEntity vault) vault.tryUnlock(server, player, stack);
        return InteractionResult.SUCCESS;
    }

    // Motes of its element rising inside it while it's lit, more while it gives out its loot
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        State vaultState = state.getValue(STATE);
        if (!vaultState.lit()) return;
        int count = vaultState == State.ACTIVE ? (random.nextInt(2) == 0 ? 1 : 0) : 3;
        int color = state.getValue(SET).element().color();
        for (int i = 0; i < count; i++) {
            level.addParticle(new DustParticleOptions(color, 0.9F), pos.getX() + 0.2 + random.nextDouble() * 0.6, pos.getY() + 0.15 + random.nextDouble() * 0.6,
                    pos.getZ() + 0.2 + random.nextDouble() * 0.6, 0, 0.02, 0);
        }
    }
}
