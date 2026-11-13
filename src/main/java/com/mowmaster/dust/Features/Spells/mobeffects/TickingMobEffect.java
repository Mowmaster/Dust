package com.mowmaster.dust.Features.Spells.mobeffects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

// A spell mob effect that does something every so often while it lasts (Cooling, Chilled, Bleeding, Tangled...).
//   interval   ticks between ticks of it, at level I (each level above halves it, down to every 5 ticks; 1 = every tick)
//   tick       what it does: (level, entity, amplifier)
public class TickingMobEffect extends MobEffect
{
    @FunctionalInterface
    public interface Tick { void apply(ServerLevel level, LivingEntity entity, int amplifier); }

    private final int interval;
    private final Tick tick;

    public TickingMobEffect(MobEffectCategory category, int color, int interval, Tick tick)
    {
        super(category, color);
        this.interval = interval;
        this.tick = tick;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int remainingDuration, int amplifier)
    {
        int every = interval <= 1 ? 1 : Math.max(5, interval >> amplifier);
        return remainingDuration % every == 0;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier)
    {
        tick.apply(level, entity, amplifier);
        return true;
    }
}
