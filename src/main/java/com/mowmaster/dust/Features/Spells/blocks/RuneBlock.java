package com.mowmaster.dust.Features.Spells.blocks;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jspecify.annotations.Nullable;

// The Rune modifier's block: a glyph on the ground (one of the glyph stone letters, at random: glyph 0-25 = a-z),
// tinted the spell's element colour. Whatever walks onto it gets the spell's effect (RuneBlockEntity has the spell,
// who cast it, how long it lasts). Walked through, broken with a punch.
public class RuneBlock extends SpellLayerBlock implements EntityBlock
{
    public static final int GLYPHS = 26;
    public static final IntegerProperty GLYPH = IntegerProperty.create("glyph", 0, GLYPHS - 1);
    // How far it looks down for ground to lie on, from where the spell landed
    private static final int GROUND_SEARCH = 4;

    public RuneBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(GLYPH, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(GLYPH);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new RuneBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (level.isClientSide() || type != DustBlockEntityRegistry.RUNE.get()) return null;
        BlockEntityTicker<RuneBlockEntity> ticker = RuneBlockEntity::serverTick;
        return (BlockEntityTicker<T>) ticker;
    }

    // Something stepped on it: the rune goes off for it
    @Override
    protected void affect(ServerLevel level, BlockPos pos, LivingEntity entity)
    {
        if (level.getBlockEntity(pos) instanceof RuneBlockEntity rune) rune.trigger(level, entity);
    }

    // A faint shimmer in its colour
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (random.nextInt(3) != 0 || !(level.getBlockEntity(pos) instanceof RuneBlockEntity rune)) return;
        DustElement element = rune.element();
        if (element == null) return;
        level.addParticle(new DustParticleOptions(element.color(), 0.5F), pos.getX() + 0.2 + random.nextDouble() * 0.6, pos.getY() + 0.1,
                pos.getZ() + 0.2 + random.nextDouble() * 0.6, 0, 0.02, 0);
    }

    // Leaves a rune for this cast near pos: there if it can lie there, or the first place it can below (ground under
    // it, air where it goes). kind: what its effect is when stepped on (ENTITY: the entity effect; SELF: the self
    // effect, for a Self spell's rune). Nowhere to lie: nothing happens.
    public static void place(SpellCast cast, BlockPos near, SpellTarget kind)
    {
        ServerLevel level = cast.level();
        RuneBlock rune = (RuneBlock) DustBlockRegistry.RUNE.get();
        BlockPos at = near;
        if (!level.getBlockState(at).isAir() && !(level.getBlockState(at).getBlock() instanceof RuneBlock)) at = at.above();
        for (int i = 0; i < GROUND_SEARCH && !rune.canLieAt(level, at) && !(level.getBlockState(at).getBlock() instanceof RuneBlock); i++) at = at.below();
        if (!rune.canLieAt(level, at) && !(level.getBlockState(at).getBlock() instanceof RuneBlock)) return;
        level.setBlockAndUpdate(at, rune.defaultBlockState().setValue(GLYPH, level.getRandom().nextInt(GLYPHS)));
        if (level.getBlockEntity(at) instanceof RuneBlockEntity entity) entity.setup(cast, kind);
        level.sendParticles(new DustParticleOptions(cast.element().color(), 1.0F), at.getX() + 0.5, at.getY() + 0.1, at.getZ() + 0.5, 16, 0.35, 0.05, 0.35, 0);
    }
}
