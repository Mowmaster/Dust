package com.mowmaster.dust.Features.EffectScrolls.Research;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.Set;

public final class DustResearchData {
    private final Set<Identifier> researched = new ObjectOpenHashSet<>();

    // Basic ops
    public boolean has(Item item) {
        return has(BuiltInRegistries.ITEM.getKey(item));
    }

    public boolean has(Identifier itemId) {
        return researched.contains(itemId);
    }

    public boolean add(Item item) {
        return add(BuiltInRegistries.ITEM.getKey(item));
    }

    public boolean add(Identifier itemId) {
        return researched.add(itemId);
    }

    public boolean remove(Item item) {
        return remove(BuiltInRegistries.ITEM.getKey(item));
    }

    public boolean remove(Identifier itemId) {
        return researched.remove(itemId);
    }

    public Set<Identifier> asSet() {
        return researched; // expose read-only in your real code if you prefer
    }

    // Persistence (Codec)
    public static final Codec<DustResearchData> CODEC = Identifier.CODEC.listOf().xmap(
            list -> {
                DustResearchData d = new DustResearchData();
                d.researched.addAll(list);
                return d;
            },
            d -> new ArrayList<>(d.researched)
    );

    // Networking (StreamCodec)
    public static final StreamCodec<RegistryFriendlyByteBuf, DustResearchData> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public DustResearchData decode(RegistryFriendlyByteBuf buf) {
                    int size = buf.readVarInt();
                    DustResearchData d = new DustResearchData();
                    for (int i = 0; i < size; i++) {
                        Identifier id = buf.readIdentifier();
                        d.researched.add(id);
                    }
                    return d;
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, DustResearchData d) {
                    buf.writeVarInt(d.researched.size());
                    for (Identifier id : d.researched) {
                        buf.writeIdentifier(id);
                    }
                }
            };
}

