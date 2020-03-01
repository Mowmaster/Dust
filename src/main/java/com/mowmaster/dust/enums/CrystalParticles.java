package com.mowmaster.dust.enums;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;


public enum  CrystalParticles
{

    RedCrystal("reddust", 30, false);

    private final String particleName;
    private final int particleID;
    private final boolean shouldIgnoreRange;
    private final int argumentCount;
    private static final Map<Integer, CrystalParticles> PARTICLES = Maps.<Integer, CrystalParticles>newHashMap();
    private static final Map<String, CrystalParticles> BY_NAME = Maps.<String, CrystalParticles>newHashMap();

    private CrystalParticles(String particleNameIn, int particleIDIn, boolean shouldIgnoreRangeIn, int argumentCountIn)
    {
        this.particleName = particleNameIn;
        this.particleID = particleIDIn;
        this.shouldIgnoreRange = shouldIgnoreRangeIn;
        this.argumentCount = argumentCountIn;
    }

    private CrystalParticles(String particleNameIn, int particleIDIn, boolean shouldIgnoreRangeIn)
    {
        this(particleNameIn, particleIDIn, shouldIgnoreRangeIn, 0);
    }

    public static Set<String> getParticleNames()
    {
        return BY_NAME.keySet();
    }

    public String getParticleName()
    {
        return this.particleName;
    }

    public int getParticleID()
    {
        return this.particleID;
    }

    public int getArgumentCount()
    {
        return this.argumentCount;
    }

    public boolean getShouldIgnoreRange()
    {
        return this.shouldIgnoreRange;
    }

    /**
     * Gets the relative CrystalParticles by id.
     */
    @Nullable
    public static CrystalParticles getParticleFromId(int particleId)
    {
        return (CrystalParticles)PARTICLES.get(Integer.valueOf(particleId));
    }

    @Nullable
    public static CrystalParticles getByName(String nameIn)
    {
        return (CrystalParticles)BY_NAME.get(nameIn);
    }

    static
    {
        for (CrystalParticles enumparticletypes : values())
        {
            PARTICLES.put(Integer.valueOf(enumparticletypes.getParticleID()), enumparticletypes);
            BY_NAME.put(enumparticletypes.getParticleName(), enumparticletypes);
        }
    }
}
