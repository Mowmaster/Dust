package com.mowmaster.dust.Features.Apparatus.still;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Machines.Compounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// Client -> server: the player right-clicked a Dust Chute with Research Notes open at an element's page (only the client
// knows which page is open: Client/DustChuteClient). Only within reach, holding the notes, and an element they know.
public record DustChuteFilterPayload(BlockPos pos, Identifier element) implements CustomPacketPayload
{
    public static final Type<DustChuteFilterPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dust_chute_filter"));

    public static final StreamCodec<RegistryFriendlyByteBuf, DustChuteFilterPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, DustChuteFilterPayload::pos,
            Identifier.STREAM_CODEC, DustChuteFilterPayload::element,
            DustChuteFilterPayload::new);

    @Override
    public Type<DustChuteFilterPayload> type()
    {
        return TYPE;
    }

    public static void handle(DustChuteFilterPayload payload, IPayloadContext context)
    {
        if (!(context.player() instanceof ServerPlayer player)) return;
        if (!player.getMainHandItem().is(DustItemRegistry.RESEARCH_NOTES.get()) && !player.getOffhandItem().is(DustItemRegistry.RESEARCH_NOTES.get())) return;
        if (player.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(payload.pos())) > 64) return;
        DustElement element = DustElements.get(payload.element());
        if (element == null || !Compounds.knows(player, element)) return;
        if (!(player.level().getBlockEntity(payload.pos()) instanceof DustChuteBlockEntity chute)) return;
        chute.setFilter(element);
        player.level().playSound(null, payload.pos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.8F, 1.0F);
    }
}
