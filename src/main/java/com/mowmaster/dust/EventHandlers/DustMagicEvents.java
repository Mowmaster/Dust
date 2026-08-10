package com.mowmaster.dust.EventHandlers;

import com.mowmaster.dust.DustCommands.*;
import com.mowmaster.dust.DustDataGen.DustTags;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustRegistries.DustPotionRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.baseOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustElementAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.Networking.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = DustReferences.MODID)
public class DustMagicEvents {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event)
    {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(PacketOfDustAuraC2S.TYPE, PacketOfDustAuraC2S.STREAM_CODEC, PacketReceiverServer::handleTestPacket);
        registrar.playToClient(PacketOfDustAuraS2C.TYPE, PacketOfDustAuraS2C.STREAM_CODEC, PacketReceiverClient::handleClientPacket);
    }

    @SubscribeEvent
    public static void setPlayerAttributesOnSpawn(PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player = event.getEntity();

        //Mana Unlock State
        if(DustMagicAttachmentHelper.hasUnlockedMana(player))
        {DustMagicAttachmentHelper.unlockMana(player);}
        else {DustMagicAttachmentHelper.lockMana(player);}
        //Mana Initialization (condition based on unlock state where needed, but init everything up front)
        if(DustMagicAttachmentHelper.hasMana(player))
        {DustMagicAttachmentHelper.setMana(player, player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA));}
        else
        {DustMagicAttachmentHelper.setMana(player ,0);}
        //Element Init (sets base capacity for all elements)
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS))
        {DustElementAttachmentHelper.setElementBaseMaximum((ServerPlayer) player,
                    player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).getBaseElementCapacity());}
        else {DustElementAttachmentHelper.setElementBaseMaximum((ServerPlayer) player,10);}

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS.get(), player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS.get()));

        //TODO: Dont forget to init researchData later


    }

    //Do i need these if im syncing my stuff in the registry with the custom methods????
    @SubscribeEvent
    public static void setPlayersAttributesOnClone(PlayerEvent.Clone event) {
        Player newPlayer = event.getEntity();
        if(DustMagicAttachmentHelper.hasUnlockedMana(event.getOriginal())){DustMagicAttachmentHelper.unlockMana(newPlayer);}
        else{DustMagicAttachmentHelper.lockMana(newPlayer);}
        DustMagicAttachmentHelper.setMana(newPlayer, event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA));

        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS.get(), event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS.get()));
    }

    @SubscribeEvent
    public static void setPlayersAttributesOnDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if(DustMagicAttachmentHelper.hasUnlockedMana(player)){DustMagicAttachmentHelper.unlockMana(player);}
        else{DustMagicAttachmentHelper.lockMana(player);}
        DustMagicAttachmentHelper.setMana(player, player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS.get(), player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS.get()));
    }

    @SubscribeEvent
    public static void setPlayersAttributesOnRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if(DustMagicAttachmentHelper.hasUnlockedMana(player)){DustMagicAttachmentHelper.unlockMana(player);}
        else{DustMagicAttachmentHelper.lockMana(player);}
        DustMagicAttachmentHelper.setMana( player, player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS.get(), player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS.get()));

    }

    @SubscribeEvent
    public static void onCommand(RegisterCommandsEvent event) {
        new PlayerManaAdd(event.getDispatcher());
        new PlayerUnlockMana(event.getDispatcher());
        new PlayerModifyFireElement(event.getDispatcher());
        new PlayerModifyWaterElement(event.getDispatcher());
        new PlayerModifyEarthElement(event.getDispatcher());
        new PlayerModifyChaosElement(event.getDispatcher());
        new PlayerModifyOrderElement(event.getDispatcher());

    }

    @SubscribeEvent
    public static void onBrewingRegister(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addMix(Potions.FIRE_RESISTANCE, Items.BLAZE_POWDER, DustPotionRegistry.POTION_SCORCH);
    }


    @SubscribeEvent
    public static void onDustPickup(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof ItemEntity itemEntity)) {
            return;
        }

        // EntityJoinLevelEvent can run on both logical sides
        if (event.getLevel().isClientSide()) {
            return;
        }

        ItemStack stack = itemEntity.getItem();

        if (!stack.is(DustTags.Items.MAGICAL_DUST_ITEMS)) {
            return;
        }

        ServerLevel level = (ServerLevel) event.getLevel();
        int amount = stack.getCount();

        for (int i = 0; i < amount; i++) {
            baseOrbEntity orb = new baseOrbEntity(DustEntityRegistry.FIRE_ORB.get(),level);
            if(stack.is(DustItemRegistry.DUST_RED.get()))orb = new baseOrbEntity(DustEntityRegistry.FIRE_ORB.get(),level);
            else if(stack.is(DustItemRegistry.DUST_BLUE.get()))orb = new baseOrbEntity(DustEntityRegistry.WATER_ORB.get(),level);
            else if(stack.is(DustItemRegistry.DUST_GREEN.get()))orb = new baseOrbEntity(DustEntityRegistry.EARTH_ORB.get(),level);
            else if(stack.is(DustItemRegistry.DUST_BLACK.get()))orb = new baseOrbEntity(DustEntityRegistry.CHAOS_ORB.get(),level);
            else if(stack.is(DustItemRegistry.DUST_WHITE.get()))orb = new baseOrbEntity(DustEntityRegistry.ORDER_ORB.get(),level);


            if (orb == null) {
                continue;
            }

            orb.setPos(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
            orb.setValue(amount);
            level.addFreshEntity(orb);
            event.setCanceled(true);
            itemEntity.discard();
        }
    }
}
