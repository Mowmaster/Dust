package com.mowmaster.dust.Features.Lamps;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// The Lovely Lamp: burns Seeds dust. While lit, the animals it can see within RADIUS:
//   grow up and get over breeding half as fast again: every GROW_EVERY ticks, a second more of each (a baby's growing
//     shows its green sparkles); the burning pays for this (a unit each 10 seconds)
//   breed: every BREED_EVERY ticks, two of a kind that are grown, rested and not in love already fall in love (hearts),
//     at a unit each; one pair at a time, and not once there are HERD_LIMIT of that kind in reach (so a pen doesn't
//     fill up for ever)
public class LovelyLampBlockEntity extends LampBlockEntity
{
    public static final int GROW_EVERY = 40;
    public static final int BREED_EVERY = 100;
    public static final int HERD_LIMIT = 16;

    public LovelyLampBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.LOVELY_LAMP.get(), pos, state);
    }

    @Override public DustElement element() { return DustElements.SEEDS; }
    @Override public int color() { return 0xFF8CC6; }

    @Override
    protected void work(ServerLevel level)
    {
        // Spread out by position, so lamps placed together don't all act on the same tick
        long time = level.getGameTime() + worldPosition.asLong();
        if (time % GROW_EVERY == 0) hurryUp(level);
        if (time % BREED_EVERY == 0) matchmake(level);
    }

    // The animals in reach that it can see (a clear line to their eyes)
    private List<Animal> animals(ServerLevel level)
    {
        Vec3 here = Vec3.atCenterOf(worldPosition);
        return level.getEntitiesOfClass(Animal.class, new AABB(worldPosition).inflate(RADIUS), animal -> animal.isAlive()
                && animal.position().distanceToSqr(here) <= RADIUS * RADIUS && sees(level, animal.getEyePosition(), animal.blockPosition()));
    }

    // A second more growing up for each baby (forced: it shows its green sparkles), a second less to wait before
    // breeding again for each parent (getAge: how long a baby has left to grow, as a negative number of ticks, or a
    // parent has left to wait, as a positive one)
    private void hurryUp(ServerLevel level)
    {
        for (Animal animal : animals(level)) {
            if (animal.isBaby()) animal.ageUp(1, true);
            else if (animal.getAge() > 0) animal.setAge(Math.max(0, animal.getAge() - 20));
        }
    }

    // One pair of the same kind falls in love, if there's a pair ready and the herd isn't full
    private void matchmake(ServerLevel level)
    {
        Map<EntityType<?>, List<Animal>> byKind = new LinkedHashMap<>();
        for (Animal animal : animals(level)) byKind.computeIfAbsent(animal.getType(), kind -> new ArrayList<>()).add(animal);
        for (List<Animal> herd : byKind.values()) {
            if (herd.size() >= HERD_LIMIT) continue;
            List<Animal> ready = herd.stream().filter(animal -> !animal.isBaby() && animal.getAge() == 0 && !animal.isInLove() && animal.canFallInLove()).toList();
            if (ready.size() < 2 || fuel() < 2) continue;
            // setInLove(null): in love as if fed, with no player to credit (hearts show)
            ready.get(0).setInLove(null);
            ready.get(1).setInLove(null);
            use(2);
            return;
        }
    }
}
