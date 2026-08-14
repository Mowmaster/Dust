package com.mowmaster.dust.Features.EffectScrolls.Item;

import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustElementAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.context.UseOnContext;

public class ToolMagicHelper
{
    public static boolean toolUseOn(ServerLevel level, ServerPlayer player, UseOnContext context, EnumElement elementCost, int costElement, int costMana, int durabilityCost)
    {
        int element = elementCost.getElementCount(player);
        int mana = DustMagicAttachmentHelper.getManaLevel(player);
        if (element > 0 && mana > 0) {
            elementCost.removeElement(player,costElement,false);
            DustMagicAttachmentHelper.removeMana(player,costMana);
            return true;
        } else if(mana > 0) {
            context.getItemInHand().hurtAndBreak(durabilityCost, (level), player,
                    item -> player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
            return true;
        }
        else {
            player.sendOverlayMessage(Component.literal("Not Enough Mana or Dust For Spell"));
            return false;
        }
    }
}
