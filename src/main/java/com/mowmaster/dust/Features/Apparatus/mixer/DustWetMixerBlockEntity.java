package com.mowmaster.dust.Features.Apparatus.mixer;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.ElementCombining;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

// The Wet Dust Mixer's working: the Dust Mixer's cycle (DustMixerBlockEntity), but for the elements that mix with a
// gaseous or aqueous one (the Mixing Table's pairs: ElementCombining.combinedBySolids false). Its phases as it plays
// them out (Client/DustWetMixerRenderer): OPEN its head is tilted back while it fills; CLOSING the head comes down into
// the pot; MIXING the beater turns; OPENING the head tilts back up; TIPPING / DUMPING / RETURNING the pot tips
// forward, pours out in front, and comes back.
public class DustWetMixerBlockEntity extends DustMixerBlockEntity
{
    public DustWetMixerBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.DUST_MIXER_WET.get(), pos, state);
    }

    // Any element can be the first of a pair
    @Override
    protected boolean takesAlone(DustElement dust)
    {
        return true;
    }

    // Two that mix into an element, at least one of them gaseous or aqueous
    @Override
    protected boolean pairs(DustElement first, DustElement second)
    {
        return ElementCombining.combine(first, second) != null && !ElementCombining.combinedBySolids(first, second);
    }
}
