package com.mowmaster.dust.Features.Spells.Client;

import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.dust.Features.Spells.pylons.PylonBlock;
import com.mowmaster.dust.Features.Spells.pylons.PylonBlockEntity;
import com.mowmaster.dust.Features.Spells.pylons.PylonConfig;
import com.mowmaster.dust.Features.Spells.pylons.PylonType;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

// The modifier pylons' colours, one tint source per model tint index (models/block/pylon/blank_pylon_* and
// shape_pylon*):
//   0          the body: the pylon's kind (Damage red, Count blue, Cost green, Change and Shape plain); a blessed
//              modifier purple, a corrupted one black; a spell break dark red, bright red while powered
//   1          the lockout ring: gold once locked
//   2 + n      upgrade node n: black once filled, plain while empty. A pylon with fewer nodes than its model shows
//              (blessed / corrupted / spell break / inert ones use the 1-node model) colours the extra ones like its body.
// The block entity asks for a redraw whenever any of that changes.
public class PylonTint implements BlockTintSource
{
    public static final int BODY = 0;
    public static final int LOCKOUT = 1;
    public static final int FIRST_NODE = 2;

    private static final int WHITE = 0xFFFFFFFF;
    private static final int GOLD = 0xFFFFC23C;
    private static final int FILLED = 0xFF1E1E1E;
    private static final int BLESSED = 0xFFA04CFF;
    private static final int CORRUPTED = 0xFF2A2A2A;
    private static final int BREAK_OFF = 0xFF6A2A2A;
    private static final int BREAK_ON = 0xFFFF4040;

    private final int index;

    private PylonTint(int index)
    {
        this.index = index;
    }

    // All of them, in tint index order, for RegisterColorHandlersEvent.BlockTintSources
    public static List<BlockTintSource> all()
    {
        List<BlockTintSource> list = new ArrayList<>();
        for (int i = 0; i < FIRST_NODE + PylonConfig.MAX_NODES; i++) list.add(new PylonTint(i));
        return list;
    }

    // The colour of a kind of pylon (also its item's)
    public static int kindColor(PylonType type)
    {
        return switch (type) {
            case DAMAGE -> 0xFFE04040;
            case COUNT -> 0xFF4080E8;
            case COST -> 0xFF40B858;
            case CHANGE, SHAPE -> WHITE;
        };
    }

    // No world position (e.g. a falling block): the body in its kind's colour, the rest plain
    @Override
    public int color(BlockState state)
    {
        return index == BODY && state.getBlock() instanceof PylonBlock pylon ? kindColor(pylon.type()) : WHITE;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos)
    {
        if (!(level.getBlockEntity(pos) instanceof PylonBlockEntity pylon)) return color(state);
        if (index == LOCKOUT) return pylon.locked() ? GOLD : WHITE;
        int body = body(pylon);
        if (index == BODY) return body;
        int node = index - FIRST_NODE;
        if (node >= pylon.maxNodes()) return body;
        return node < pylon.nodes() ? FILLED : WHITE;
    }

    private static int body(PylonBlockEntity pylon)
    {
        if (pylon.main() != null && pylon.isSpellBreak()) return pylon.powered() ? BREAK_ON : BREAK_OFF;
        if (pylon.tier() == ModifierTier.BLESSED) return BLESSED;
        if (pylon.tier() == ModifierTier.CORRUPTED) return CORRUPTED;
        return kindColor(pylon.type());
    }
}
