package com.mowmaster.dust.Features.soul;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.Optional;
import java.util.UUID;

// Attached to an entity Soul's spells made or emptied (attachment SOUL_TETHER), tying it to a soul elsewhere:
//   ANCHOR       a player's ghost, their temporary respawn (spirit: the player)
//   PLAYER_BODY  the body a player's soul was pulled out of (a mannequin; spirit: the player)
//   MOB_BODY     a mob whose soul was pulled out (spirit: its wisp)
// caster: who cast it, so a body that dies counts as their kill. Its timer is the Ghostly effect on the entity: when that
// ends with the tether still there, the soul didn't make it back (SoulEvents).
public record SoulTether(Kind kind, UUID spirit, Optional<UUID> caster)
{
    public enum Kind { ANCHOR, PLAYER_BODY, MOB_BODY }

    // Enums as their names: Codec.STRING.xmap(read, write) turns a string codec into an enum codec
    private static final Codec<Kind> KIND_CODEC = Codec.STRING.xmap(Kind::valueOf, Kind::name);

    public static final Codec<SoulTether> CODEC = RecordCodecBuilder.create(i -> i.group(
            KIND_CODEC.fieldOf("kind").forGetter(SoulTether::kind),
            UUIDUtil.CODEC.fieldOf("spirit").forGetter(SoulTether::spirit),
            UUIDUtil.CODEC.optionalFieldOf("caster").forGetter(SoulTether::caster)
    ).apply(i, SoulTether::new));

    // The attachment's default (never used: Soul only reads tethers it set, with getExistingData)
    public static final SoulTether NONE = new SoulTether(Kind.ANCHOR, new UUID(0, 0), Optional.empty());
}
