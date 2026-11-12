package com.mowmaster.dust.Features.Machines;

import java.util.List;

// A research station's block entity: its clickable slots (StationInteractions). Checked in order, so put small slots
// before big ones that overlap them.
public interface IStationSlots
{
    List<StationSlot> slots();
}
