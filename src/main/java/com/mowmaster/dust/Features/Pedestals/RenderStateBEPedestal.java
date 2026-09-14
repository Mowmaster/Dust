package com.mowmaster.dust.Features.Pedestals;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.level.Level;

public class RenderStateBEPedestal extends BlockEntityRenderState
{
    public Level level;
    public float rotation;

    public final ItemStackRenderState renderState = new ItemStackRenderState();


}
