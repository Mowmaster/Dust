package com.mowmaster.dust.Capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityExperience
{
    public static final Capability<IExperienceStorage> EXPERIENCE = CapabilityManager.get(new CapabilityToken<>(){});

    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(IExperienceStorage.class);
    }
}
