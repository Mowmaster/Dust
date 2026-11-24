package com.mowmaster.dust.Features.Trees;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.UntintedParticleLeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

// The elemental woods' blocks that are vanilla's own blocks with vanilla wood's flammability (Flammable). Registered by
// ElementalWoods.
public final class WoodBlocks
{
    private WoodBlocks() {}

    // A log or wood (bark on every side), stripped or not. An axe strips the bark off through data maps (made from
    // ElementalWoods' sets): 26.3 neoforge:transformables (datagen, DustProviderDataMaps), 26.1.2 neoforge:strippables
    // (src/main/resources-26.1.2/data/neoforge/data_maps/block/strippables.json).
    public static class Log extends RotatedPillarBlock implements Flammable
    {
        public Log(Properties properties)
        {
            super(properties);
        }

        @Override
        public int igniteOdds()
        {
            return LOG_IGNITE;
        }

        @Override
        public int burnOdds()
        {
            return LOG_BURN;
        }
    }

    public static class Planks extends Block implements Flammable
    {
        public Planks(Properties properties)
        {
            super(properties);
        }

        @Override
        public int igniteOdds()
        {
            return PLANKS_IGNITE;
        }

        @Override
        public int burnOdds()
        {
            return PLANKS_BURN;
        }
    }

    public static class Stairs extends StairBlock implements Flammable
    {
        public Stairs(BlockState baseState, Properties properties)
        {
            super(baseState, properties);
        }

        @Override
        public int igniteOdds()
        {
            return PLANKS_IGNITE;
        }

        @Override
        public int burnOdds()
        {
            return PLANKS_BURN;
        }
    }

    public static class Slab extends SlabBlock implements Flammable
    {
        public Slab(Properties properties)
        {
            super(properties);
        }

        @Override
        public int igniteOdds()
        {
            return PLANKS_IGNITE;
        }

        @Override
        public int burnOdds()
        {
            return PLANKS_BURN;
        }
    }

    public static class Fence extends FenceBlock implements Flammable
    {
        public Fence(Properties properties)
        {
            super(properties);
        }

        @Override
        public int igniteOdds()
        {
            return PLANKS_IGNITE;
        }

        @Override
        public int burnOdds()
        {
            return PLANKS_BURN;
        }
    }

    public static class FenceGate extends FenceGateBlock implements Flammable
    {
        public FenceGate(WoodType type, Properties properties)
        {
            super(type, properties);
        }

        @Override
        public int igniteOdds()
        {
            return PLANKS_IGNITE;
        }

        @Override
        public int burnOdds()
        {
            return PLANKS_BURN;
        }
    }

    // Leaves in the wood's own colour (their texture is coloured, not tinted by the biome); the leaves that drift down
    // from them are that colour too
    public static class Leaves extends UntintedParticleLeavesBlock implements Flammable
    {
        private static final float PARTICLE_CHANCE = 0.01F;

        public Leaves(int leafColor, Properties properties)
        {
            //? if >=26.3 {
            super(PARTICLE_CHANCE, ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, 0xFF000000 | leafColor),
                    net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer.noAmbientSound(), properties);
            //?} else {
            /*super(PARTICLE_CHANCE, ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, 0xFF000000 | leafColor), properties);
            *///?}
        }

        @Override
        public int igniteOdds()
        {
            return LEAVES_IGNITE;
        }

        @Override
        public int burnOdds()
        {
            return LEAVES_BURN;
        }
    }
}
