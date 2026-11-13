package com.mowmaster.dust.Features.Spells.pylons;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

// Which spell tree a recorded spell came from, and how many pylons it used (the dust:spell_tree data component on a
// Spell Scroll). Each cast gives the tree that many experience (SpellTreeData).
public record SpellTreeLink(UUID tree, int pylons)
{
    public static final Codec<SpellTreeLink> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("tree").forGetter(SpellTreeLink::tree),
            Codec.INT.fieldOf("pylons").forGetter(SpellTreeLink::pylons)
    ).apply(instance, SpellTreeLink::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellTreeLink> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
}
