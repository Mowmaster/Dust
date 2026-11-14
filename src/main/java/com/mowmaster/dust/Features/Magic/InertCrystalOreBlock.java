package com.mowmaster.dust.Features.Magic;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;

// Inert Crystal Ore: the one crystal ore that generates (DustWorldgen, about as common as diamond). Left in the ground
// it slowly takes on its chunk's aura: now and then (random ticks, like copper weathering) it turns into one of the
// five primal ores, picked at random weighted by how much of each primal the chunk holds (ChunkAura). A desert chunk is
// rich in Fire, so its ore mostly becomes Fire ore; an ocean's mostly Water. Each change uses up a little of that
// primal's aura. Mined before it changes, it drops inert crystals.
public class InertCrystalOreBlock extends DropExperienceBlock
{
    // On a random tick, a 1 in CHANGE_ODDS chance of changing (an ore block gets a random tick about once a minute,
    // so about once in this many minutes)
    public static final int CHANGE_ODDS = 40;
    // The aura one change uses up
    public static final float CHANGE_COST = 1;

    private final boolean deepslate;

    public InertCrystalOreBlock(boolean deepslate, IntProvider experience, Properties properties)
    {
        super(experience, properties);
        this.deepslate = deepslate;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state)
    {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (random.nextInt(CHANGE_ODDS) != 0) return;
        ChunkAuraData aura = ChunkAura.of(level, pos);
        float total = aura.total();
        if (total <= 0) return;
        // A weighted pick: a random point along the chunk's aura, and whichever primal's share it falls in
        float pick = random.nextFloat() * total;
        EnumElement primal = EnumElement.values()[EnumElement.values().length - 1];
        for (EnumElement each : EnumElement.values()) {
            pick -= aura.get(each);
            if (pick <= 0) {
                primal = each;
                break;
            }
        }
        Block into = (deepslate ? DustBlockRegistry.PRIMAL_ORES_DEEPSLATE : DustBlockRegistry.PRIMAL_ORES).get(primal).get();
        level.setBlock(pos, into.defaultBlockState(), Block.UPDATE_ALL);
        ChunkAura.add(level, pos, primal, -CHANGE_COST);
    }
}
