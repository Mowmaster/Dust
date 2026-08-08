package com.mowmaster.dust.Features.DustEntities;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class baseOrbRenderState extends EntityRenderState {
    public int icon;

    public baseOrbRenderState() {
    }
}
