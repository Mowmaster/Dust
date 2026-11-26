package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.DustComposition.ElementItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

// A sanctum's Arcane Spawner, like a trial spawner (ArcaneSpawnerBlockEntity does the work): it wakes when a player
// comes near, sends a wave of its element's mobs (SanctumMobs), as strong as the strongest nearby player's Magic Level
// allows, and once they're all beaten it gives each player who fought a reward and rests (COOLDOWN). Its element is its
// SET; its PHASE shows what it's doing (the renderer and particles follow it). It can't be mined for anything. In
// creative, use an element's crystal on it to change its element.
public class ArcaneSpawnerBlock extends Block implements EntityBlock
{
    public enum Phase implements StringRepresentable
    {
        WAITING("waiting"), ACTIVE("active"), COOLDOWN("cooldown");

        private final String name;

        Phase(String name)
        {
            this.name = name;
        }

        @Override
        public String getSerializedName()
        {
            return name;
        }
    }

    public static final EnumProperty<ElementalArmorSet> SET = EnumProperty.create("set", ElementalArmorSet.class);
    public static final EnumProperty<Phase> PHASE = EnumProperty.create("phase", Phase.class);

    public ArcaneSpawnerBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(SET, ElementalArmorSet.PYREBORN).setValue(PHASE, Phase.WAITING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(SET, PHASE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ArcaneSpawnerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (level.isClientSide() || type != Sanctums.ARCANE_SPAWNER_ENTITY.get()) return null;
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof ArcaneSpawnerBlockEntity spawner && tickLevel instanceof ServerLevel server) spawner.serverTick(server, tickState);
        };
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!player.getAbilities().instabuild || !(stack.getItem() instanceof ElementItem item)) return InteractionResult.TRY_WITH_EMPTY_HAND;
        ElementalArmorSet set = ElementalArmorSet.of(item.element());
        if (set == null) return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (!level.isClientSide()) level.setBlock(pos, state.setValue(SET, set), Block.UPDATE_ALL);
        return InteractionResult.SUCCESS;
    }

    // Motes of its element rising from it: a few while it waits, a stream while it's active, none while it rests
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        Phase phase = state.getValue(PHASE);
        if (phase == Phase.COOLDOWN) return;
        int count = phase == Phase.ACTIVE ? 3 : (random.nextInt(3) == 0 ? 1 : 0);
        int color = state.getValue(SET).element().color();
        for (int i = 0; i < count; i++) {
            level.addParticle(new DustParticleOptions(color, phase == Phase.ACTIVE ? 1.2F : 0.8F),
                    pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0, 0.03, 0);
        }
    }
}
