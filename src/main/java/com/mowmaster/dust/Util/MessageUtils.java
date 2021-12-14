package com.mowmaster.dust.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.List;

import static com.mowmaster.dust.References.Constants.MODID;

public class MessageUtils
{

    public static void messagePopup(Player player, ChatFormatting color, String localizedMessage)
    {
        TranslatableComponent message = new TranslatableComponent(localizedMessage);
        message.withStyle(color);
        player.displayClientMessage(message,true);
    }

    public static void messagePopupWithAppend(Player player, ChatFormatting color, String localizedMessage, List<String> appendedMessage)
    {
        TranslatableComponent message = new TranslatableComponent(localizedMessage);
        for(int i = 0; i<appendedMessage.size(); i++)
        {
            if(appendedMessage.get(i).contains(MODID))
            {
                message.append(new TranslatableComponent(appendedMessage.get(i)));
            }
            else
            {
                message.append(appendedMessage.get(i));
            }
        }
        message.withStyle(color);
        player.displayClientMessage(message,true);
    }
}
