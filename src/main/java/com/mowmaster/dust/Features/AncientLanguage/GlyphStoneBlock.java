package com.mowmaster.dust.Features.AncientLanguage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

// A stone with one ancient glyph carved on its front face. Ruins (AncientRuinFeature) build words out of a row of
// these; Parchment copies the row. GLYPH 0-25 = a-z.
// Creative players can change the glyph by right-clicking with an empty hand (sneak to go backwards), for building
// your own glyph walls. Breaking one gives cobblestone (see the loot table), so glyphs can't be collected.
public class GlyphStoneBlock extends HorizontalDirectionalBlock
{
    public static final IntegerProperty GLYPH = IntegerProperty.create("glyph", 0, AncientLanguage.GLYPH_COUNT - 1);

    public GlyphStoneBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, net.minecraft.core.Direction.NORTH).setValue(GLYPH, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, GLYPH);
    }

    // The glyph faces the player placing it
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        if (!player.isCreative()) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            int step = player.isShiftKeyDown() ? AncientLanguage.GLYPH_COUNT - 1 : 1;
            level.setBlock(pos, state.setValue(GLYPH, (state.getValue(GLYPH) + step) % AncientLanguage.GLYPH_COUNT), 3);
        }
        return InteractionResult.SUCCESS;
    }
}
