package com.mowmaster.dust.spells;

import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public class Spells
{
    public EarthExplosion createExplosion(World world,@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isSmoking)
    {
        return this.newExplosion(world,entityIn, x, y, z, strength, false, isSmoking);
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
     */
    public EarthExplosion newExplosion(World world,@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking)
    {

        EarthExplosion explosion = new EarthExplosion(world, entityIn, x, y, z, strength, isFlaming, isSmoking);
        //if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) return explosion;
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }
}
