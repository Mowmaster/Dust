package com.mowmaster.dust.capabilities;

import com.mowmaster.dust.references.Reference;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityFlightHandler implements ICapabilitySerializable<NBTTagString>
{
    public final static ResourceLocation FLIGHT = new ResourceLocation(Reference.MODID, "hasFlight");

    @CapabilityInject(CapabilityFlight.class)
    public static Capability<CapabilityFlight> INSTANCE = null;
    private static String OWNER_KEY = "owner";
    private final CapabilityFlight capability;

    public CapabilityFlightHandler() {
        this.capability = new CapabilityFlight();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == INSTANCE;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == INSTANCE) {
            return INSTANCE.cast(this.capability);
        }

        return null;
    }

    @Override
    public NBTTagString serializeNBT() {
        return capability.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagString nbt) {
        capability.deserializeNBT(nbt);
    }

    public static class CapabilityFlightStorage implements Capability.IStorage<CapabilityFlight> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<CapabilityFlight> capability, CapabilityFlight instance, EnumFacing side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<CapabilityFlight> capability, CapabilityFlight instance, EnumFacing side, NBTBase nbt) {
            instance.deserializeNBT((NBTTagString) nbt);
        }
    }
}
