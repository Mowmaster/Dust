package com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ElementalOrbEntityRenderState extends EntityRenderState {
    public int icon;
    // The element's colour, 0xRRGGBB
    public int color;

    public ElementalOrbEntityRenderState() {
    }
}
