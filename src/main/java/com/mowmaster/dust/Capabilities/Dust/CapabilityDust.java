package com.mowmaster.dust.Capabilities.Dust;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityDust
{
    public static final Capability<IDustTank> DUST = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IDustHandler> DUST_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});

    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(IDustTank.class);
        event.register(IDustHandler.class);
    }
}
