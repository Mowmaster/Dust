package com.mowmaster.dust.handlers;

import com.mowmaster.dust.references.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by KingMowmaster on 5/20/2018.
 */
public class ClientEvents
{
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent event) {
        event.getMap().registerSprite(new ResourceLocation(Reference.MODID, "particles/rose_petals"));

    }
}
