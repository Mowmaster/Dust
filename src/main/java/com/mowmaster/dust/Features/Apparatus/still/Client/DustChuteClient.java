package com.mowmaster.dust.Features.Apparatus.still.Client;

import com.mowmaster.dust.Features.Apparatus.still.DustChuteFilterPayload;
import com.mowmaster.dust.Features.Research.Client.ResearchBook;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

// A Dust Chute right-clicked with Research Notes: if the book is open at an element's page, the server is told to filter
// the chute to that element (DustChuteFilterPayload)
public final class DustChuteClient
{
    private DustChuteClient() {}

    public static void filterFromNotes(BlockPos pos)
    {
        Identifier element = ResearchBook.openElement();
        if (element == null) {
            if (Minecraft.getInstance().player != null) Minecraft.getInstance().player.sendOverlayMessage(Component.translatable("dust.chute.open_element_page"));
            return;
        }
        net.neoforged.neoforge.client.network.ClientPacketDistributor.sendToServer(new DustChuteFilterPayload(pos, element));
    }
}
