package com.mowmaster.dust.Features.Labyrinth;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Sanctums.ArcaneSpawnerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

// A rune set in a labyrinth puzzle room's wall, carved for an element (SET; tinted its colour): a spell holding that
// element striking it lights it (LIT; LabyrinthRooms.onSpellBlock). Light them all and the room gives up its chest.
// Unbreakable.
public class LabyrinthRuneBlock extends Block
{
    public static final EnumProperty<ElementalArmorSet> SET = ArcaneSpawnerBlock.SET;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public LabyrinthRuneBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(SET, ElementalArmorSet.PYREBORN).setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(SET, LIT);
    }

    // Unlit, a slow trickle of its element's motes, so it can be told apart before it's struck
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (random.nextInt(state.getValue(LIT) ? 2 : 6) != 0) return;
        level.addParticle(new DustParticleOptions(state.getValue(SET).element().color(), 1.0F),
                pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0, 0.02, 0);
    }
}
