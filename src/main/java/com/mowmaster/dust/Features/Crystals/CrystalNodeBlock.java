package com.mowmaster.dust.Features.Crystals;

import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

// A crystal of one element, small, medium or large (CrystalSize), growing out of whichever face it was placed on, like
// vanilla's amethyst buds (AmethystClusterBlock: FACING, WATERLOGGED, needs the face behind it). Each element has its
// own three (ElementCrystals), sharing three models tinted with the element's colour (Client/ElementCrystalTint).
//   Silk Touch     the crystal itself (loot table): a small one is the element's crystal item, which places it again
//                  (CrystalPlacing); medium and large are their own items
//   anything else  an element orb worth the crystal's value: 1, 4 or 16 dust (spawnAfterBreak)
// They grow on Budding Crystals (BuddingCrystalBlock), and form in element geodes (Features/Worldgen).
public class CrystalNodeBlock extends AmethystClusterBlock implements ElementCrystal
{
    private final DustElement element;
    private final CrystalSize size;

    public CrystalNodeBlock(DustElement element, CrystalSize size, Properties properties)
    {
        super(size.height, size.inset, properties);
        this.element = element;
        this.size = size;
    }

    @Override
    public DustElement element()
    {
        return element;
    }

    public CrystalSize size()
    {
        return size;
    }

    // Broken without Silk Touch (by any tool, or none, or an explosion): its dust comes back as an orb
    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience)
    {
        super.spawnAfterBreak(state, level, pos, tool, dropExperience);
        if (!CrystalDrops.silkTouch(level, tool)) ElementOrbEntity.spawn(level, element, size.value, Vec3.atCenterOf(pos));
    }

    // Picked (middle click): a small one is its element's crystal
    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData)
    {
        if (size == CrystalSize.SMALL) return CrystalElements.stackOf(element);
        return super.getCloneItemStack(level, pos, state, includeData);
    }
}
