package com.mowmaster.dust.Features.Bosses.Client;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

// What ArchonRenderer draws an Archon from: the usual humanoid state, its set (its skin and eye colour) and whether
// it's gathering a spell
public class ArchonRenderState extends HumanoidRenderState
{
    public ElementalArmorSet set = ElementalArmorSet.PYREBORN;
    public boolean casting;
}
