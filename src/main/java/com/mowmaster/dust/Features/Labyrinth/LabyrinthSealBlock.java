package com.mowmaster.dust.Features.Labyrinth;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Sanctums.ArcaneSpawnerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

// A wall of light across a labyrinth doorway (LabyrinthRooms seals a room's doors while its fight is on; the arena's
// way in stays sealed until every Herald has fallen). See-through, in its element's colour (SET), unbreakable.
public class LabyrinthSealBlock extends Block
{
    public static final EnumProperty<ElementalArmorSet> SET = ArcaneSpawnerBlock.SET;

    public LabyrinthSealBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(SET, ElementalArmorSet.PYREBORN));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(SET);
    }

    // Seals side by side show as one sheet (no faces between them), and light passes through
    @Override
    protected boolean skipRendering(BlockState state, BlockState adjacent, Direction direction)
    {
        return adjacent.is(this) || super.skipRendering(state, adjacent, direction);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state)
    {
        return true;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (random.nextInt(4) != 0) return;
        level.addParticle(new DustParticleOptions(state.getValue(SET).element().color(), 0.8F),
                pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0, 0.01, 0);
    }
}
