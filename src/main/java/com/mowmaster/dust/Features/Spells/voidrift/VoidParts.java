package com.mowmaster.dust.Features.Spells.voidrift;

import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

// Void's self and block parts (its entity part is void damage, SimpleParts.voidDamage)
public final class VoidParts
{
    // Chest: the spell's duration x 12 (a minute at the default). Rift: duration x 2 (10 seconds at the default).
    public static final int CHEST_TIME_SCALE = 12;
    public static final int RIFT_TIME_SCALE = 2;

    private VoidParts() {}

    // Self: a winged ender chest at your shoulder (players only: only they have an ender chest)
    public static void chest(SpellCast cast, LivingEntity target)
    {
        if (target instanceof Player player) VoidChestEntity.summon(cast.level(), player, cast.stats().duration() * CHEST_TIME_SCALE);
    }

    // Block: a rift in the space against the face hit. Size widens it by half a block a level.
    public static void rift(SpellCast cast, BlockPos pos, Direction face)
    {
        Vec3 at = Vec3.atCenterOf(pos.relative(face));
        VoidRiftEntity.open(cast.level(), at, 0.75F + cast.stats().size() * 0.5F, cast.stats().duration() * RIFT_TIME_SCALE);
    }
}
