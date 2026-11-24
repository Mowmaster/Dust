package com.mowmaster.dust.Features.Flora;

import com.mowmaster.dust.Features.Crystals.CrystalDrops;
import com.mowmaster.dust.Features.Crystals.CrystalNodeBlock;
import com.mowmaster.dust.Features.Crystals.CrystalSize;
import com.mowmaster.dust.Features.Crystals.ElementCrystals;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.Magic.ElementAura;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

// An elemental flower (ElementalFlower). Besides being a flower (it grows on dirt and grass, and on sand and terracotta
// for the desert ones), it steadies the aura: on a random tick (1 in CRYSTALLIZE_ODDS), if the chunk holds more than
// CRYSTALLIZE_AT times its normal of one of its element's primals (ChunkAura), it draws a dust of its element out of the
// aura and crystallizes it into a small crystal of its element on the ground beside it (never more than MAX_CRYSTALS of
// them round it).
// Broken with shears or Silk Touch it drops itself (loot table); otherwise its dust comes back as an element orb worth
// ElementalFlower.VALUE.
public class ElementalFlowerBlock extends FlowerBlock
{
    public static final float CRYSTALLIZE_AT = 1.25F;
    private static final int CRYSTALLIZE_ODDS = 3;
    private static final int MAX_CRYSTALS = 3;
    private static final int REACH = 2;

    private final ElementalFlower flower;

    public ElementalFlowerBlock(ElementalFlower flower, Properties properties)
    {
        super(flower.stewEffect, flower.stewSeconds, properties);
        this.flower = flower;
    }

    public ElementalFlower flower()
    {
        return flower;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
    {
        return super.mayPlaceOn(state, level, pos) || state.is(BlockTags.SAND) || state.is(BlockTags.TERRACOTTA);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state)
    {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (random.nextInt(CRYSTALLIZE_ODDS) != 0) return;
        DustElement element = flower.element();
        boolean overFull = false;
        for (EnumElement primal : ElementAura.primalsOf(element)) {
            if (ElementAura.fullness(level, pos, primal) >= CRYSTALLIZE_AT) overFull = true;
        }
        if (!overFull) return;
        int crystals = 0;
        for (BlockPos at : BlockPos.betweenClosed(pos.offset(-REACH, -1, -REACH), pos.offset(REACH, 1, REACH))) {
            if (level.getBlockState(at).getBlock() instanceof CrystalNodeBlock) crystals++;
        }
        if (crystals >= MAX_CRYSTALS) return;
        CrystalNodeBlock crystal = ElementCrystals.node(element, CrystalSize.SMALL);
        for (int tries = 0; tries < 6; tries++) {
            BlockPos at = pos.offset(random.nextInt(REACH * 2 + 1) - REACH, random.nextInt(3) - 1, random.nextInt(REACH * 2 + 1) - REACH);
            if (!level.getBlockState(at).isAir()) continue;
            BlockState into = crystal.defaultBlockState().setValue(AmethystClusterBlock.FACING, Direction.UP);
            if (!into.canSurvive(level, at)) continue;
            level.setBlockAndUpdate(at, into);
            ElementAura.take(level, pos, element, CrystalSize.SMALL.value);
            level.sendParticles(new DustParticleOptions(element.color(), 1.0F), at.getX() + 0.5, at.getY() + 0.3, at.getZ() + 0.5, 8, 0.2, 0.2, 0.2, 0);
            return;
        }
    }

    // Broken by hand or with a tool other than shears, without Silk Touch: an orb of its dust
    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience)
    {
        super.spawnAfterBreak(state, level, pos, tool, dropExperience);
        if (tool.getItem() instanceof ShearsItem || CrystalDrops.silkTouch(level, tool)) return;
        ElementOrbEntity.spawn(level, flower.element(), ElementalFlower.VALUE, Vec3.atCenterOf(pos));
    }
}
