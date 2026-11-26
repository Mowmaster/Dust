package com.mowmaster.dust.Features.Armor.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Armor.ArmorSetBonuses;
import com.mowmaster.dust.Features.Armor.ElementalArmor;
import com.mowmaster.dust.Features.Armor.ElementalArmorItem;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

// The elemental armor on the client: each piece is drawn with its set's own 3D model and texture (ElementalArmorModels)
// instead of the flat armour layers, and the set-bonus tooltips count the local player's pieces.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class ElementalArmorClient
{
    private ElementalArmorClient() {}

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        ElementalArmorModels.registerLayers(event);
        ArmorSetBonuses.localCount = set -> {
            Player player = Minecraft.getInstance().player;
            return player == null ? 0 : ArmorSetBonuses.count(player, set);
        };
    }

    @SubscribeEvent
    public static void registerExtensions(RegisterClientExtensionsEvent event)
    {
        for (ElementalArmorSet set : ElementalArmorSet.values()) {
            Item[] pieces = ElementalArmor.ITEMS.get(set).values().stream().map(item -> (Item) item.get()).toArray(Item[]::new);
            event.registerItem(new IClientItemExtensions()
            {
                @Override
                public Model getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model original)
                {
                    return itemStack.getItem() instanceof ElementalArmorItem piece ? ElementalArmorModels.model(piece.set(), piece.type()) : original;
                }

                @Override
                public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, Identifier original)
                {
                    return ElementalArmorModels.texture(set);
                }
            }, pieces);
        }
    }
}
