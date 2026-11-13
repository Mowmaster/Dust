package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

// Trees on a mob: roots build up on it like Scorch's flames. Each hit adds a stack per level of the spell (shown as the
// Rooting effect's level, each stack slowing it a little); stacks fade if it goes duration x 4 without being hit. At
// 100 stacks a tree bursts up where it stands: the trunk grows through it, so it's Rooted (can't move or jump) and
// suffocates inside the wood unless it breaks its way out. Bosses build roots but no tree grows on them.
public final class RootParts
{
    public static final int TREE_STACKS = 100;
    public static final int STACKS_PER_LEVEL = 10;
    public static final int FADE_TIME_SCALE = 4;
    // How long Rooted holds it: duration x ROOTED_TIME_SCALE (20 seconds at the default)
    public static final int ROOTED_TIME_SCALE = 4;

    // A random kind of tree: the log and leaves it's built from
    private record Wood(Block log, Block leaves) {}
    private static final List<Wood> WOODS = List.of(new Wood(Blocks.OAK_LOG, Blocks.OAK_LEAVES), new Wood(Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES),
            new Wood(Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES), new Wood(Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_LEAVES));

    private RootParts() {}

    public static void roots(SpellCast cast, Entity target)
    {
        if (!(target instanceof LivingEntity living) || !living.isAlive()) return;
        MobEffectInstance current = living.getEffect(DustEffectsRegistry.ROOTING);
        int stacks = (current == null ? 0 : current.getAmplifier() + 1) + STACKS_PER_LEVEL * (cast.stats().amplifier() + 1);
        ServerLevel level = cast.level();
        level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ROOTED_DIRT.defaultBlockState()),
                living.getX(), living.getY() + 0.2, living.getZ(), 6, 0.3, 0.1, 0.3, 0.05);
        if (stacks >= TREE_STACKS && !living.is(Tags.EntityTypes.BOSSES)) {
            living.removeEffect(DustEffectsRegistry.ROOTING);
            growTree(level, living, cast.stats().duration() * ROOTED_TIME_SCALE);
            return;
        }
        // The amplifier is stacks - 1 (level I = 1 stack). A higher amplifier always replaces the old instance.
        living.addEffect(new MobEffectInstance(DustEffectsRegistry.ROOTING, cast.stats().duration() * FADE_TIME_SCALE,
                Math.min(stacks, TREE_STACKS) - 1), cast.caster());
    }

    // A tree through the target: a trunk from its feet up past its head, leaves around the top. Only air, plants,
    // water and the like are replaced, so it can't break into builds.
    private static void growTree(ServerLevel level, LivingEntity target, int rootedTicks)
    {
        RandomSource random = level.getRandom();
        Wood wood = WOODS.get(random.nextInt(WOODS.size()));
        BlockPos base = target.blockPosition();
        // Centred in the trunk's block, so the wood surrounds it
        target.teleportTo(base.getX() + 0.5, base.getY(), base.getZ() + 0.5);
        target.setDeltaMovement(0, 0, 0);
        target.addEffect(new MobEffectInstance(DustEffectsRegistry.ROOTED, rootedTicks, 0));

        int height = Math.max(5, (int) Math.ceil(target.getBbHeight()) + 3) + random.nextInt(2);
        for (int y = 0; y < height; y++) place(level, base.above(y), wood.log().defaultBlockState());
        // Leaves: two wide layers just under the top, then two narrow ones over it (corners trimmed off)
        BlockPos top = base.above(height - 1);
        BlockState leaves = wood.leaves().defaultBlockState();
        for (int dy = -2; dy <= 1; dy++) {
            int radius = dy < 0 ? 2 : 1;
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    boolean corner = Math.abs(dx) == radius && Math.abs(dz) == radius;
                    if (corner && (radius == 1 || random.nextBoolean())) continue;
                    place(level, top.offset(dx, dy, dz), leaves);
                }
            }
        }
        level.playSound(null, base, SoundEvents.ROOTED_DIRT_PLACE, SoundSource.BLOCKS, 1.5F, 0.6F);
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, base.getX() + 0.5, base.getY() + 1, base.getZ() + 0.5, 20, 0.6, 1.0, 0.6, 0);
    }

    private static void place(ServerLevel level, BlockPos pos, BlockState state)
    {
        if (level.getBlockState(pos).canBeReplaced()) level.setBlock(pos, state, Block.UPDATE_ALL);
    }
}
