package com.mowmaster.dust.Features.Labyrinth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

// Where a player in a labyrinth goes back to (in front of the sanctum portal they came through), and which run they're
// in. Kept on the player (Labyrinth.RETURN) from going in until they come out.
public record ReturnPoint(ResourceKey<Level> dimension, Vec3 pos, float yaw, long run)
{
    public static final ReturnPoint NONE = new ReturnPoint(Level.OVERWORLD, Vec3.ZERO, 0.0F, -1L);

    public static final Codec<ReturnPoint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(ReturnPoint::dimension),
            Vec3.CODEC.fieldOf("pos").forGetter(ReturnPoint::pos),
            Codec.FLOAT.fieldOf("yaw").forGetter(ReturnPoint::yaw),
            Codec.LONG.fieldOf("run").forGetter(ReturnPoint::run)
    ).apply(instance, ReturnPoint::new));

    public boolean present()
    {
        return run >= 0;
    }
}
