package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

// The line under Alchemical Dust's name: its element's icon, its name (or ??? until the player has discovered it), and
// its phase. Client/ClientAlchemicalDustTooltip draws it (registered in DustClient).
public record AlchemicalDustTooltip(DustElement element, boolean known) implements TooltipComponent {}
