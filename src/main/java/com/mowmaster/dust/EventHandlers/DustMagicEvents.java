package com.mowmaster.dust.EventHandlers;

import com.mowmaster.dust.DustCommands.*;
import com.mowmaster.dust.DustDataGen.DustTags;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementalOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.Networking.*;
import com.mowmaster.dust.Features.Research.ObservationPayloads;
import com.mowmaster.dust.Features.Research.ResearchNotesPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jspecify.annotations.Nullable;

@EventBusSubscriber(modid = DustReferences.MODID)
public class DustMagicEvents {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event)
    {
        PayloadRegistrar registrar = event.registrar("1");

        // Handlers for these two are client-only, registered in DustClient (RegisterClientPayloadHandlersEvent)
        registrar.playToClient(ObservationPayloads.Progress.TYPE, ObservationPayloads.Progress.STREAM_CODEC);
        registrar.playToClient(ObservationPayloads.Discovered.TYPE, ObservationPayloads.Discovered.STREAM_CODEC);
        registrar.playToServer(ResearchNotesPayload.TYPE, ResearchNotesPayload.STREAM_CODEC, ResearchNotesPayload::handle);
        registrar.playToServer(PacketOfDustAuraC2S.TYPE, PacketOfDustAuraC2S.STREAM_CODEC, PacketReceiverServer::handleTestPacket);
        registrar.playToClient(PacketOfDustAuraS2C.TYPE, PacketOfDustAuraS2C.STREAM_CODEC, PacketReceiverClient::handleClientPacket);

        registrar.playToClient(S2CPacketElementFire.TYPE, S2CPacketElementFire.STREAM_CODEC, PacketReceiverClient::handleCPElementFire);
        registrar.playToClient(S2CPacketElementWater.TYPE, S2CPacketElementWater.STREAM_CODEC, PacketReceiverClient::handleCPElementWater);
        registrar.playToClient(S2CPacketElementEarth.TYPE, S2CPacketElementEarth.STREAM_CODEC, PacketReceiverClient::handleCPElementEarth);
        registrar.playToClient(S2CPacketElementChaos.TYPE, S2CPacketElementChaos.STREAM_CODEC, PacketReceiverClient::handleCPElementChaos);
        registrar.playToClient(S2CPacketElementOrder.TYPE, S2CPacketElementOrder.STREAM_CODEC, PacketReceiverClient::handleCPElementOrder);


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
        else {DustMagicAttachmentHelper.setMana(player ,0);}
        //maxmana
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_MAXMANA))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MAXMANA,DustMagicAttachmentHelper.getManaMaximum(player));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MAXMANA,20);}
        //mana increase
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYINCREASE))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYINCREASE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYINCREASE));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYINCREASE,0);}
        //mana increase multiplier
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYMULTIPLIER))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYMULTIPLIER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYMULTIPLIER));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYMULTIPLIER,0);}
        //mana regen rate (per second)
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_REGENERATION_RATE))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_REGENERATION_RATE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_REGENERATION_RATE));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_REGENERATION_RATE,1);}

        //AFFINITY
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_AFFINITY))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_AFFINITY,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_AFFINITY));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_AFFINITY,"none");}

        //DUSTMAGIC Capacity Increase
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYINCREASE))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYINCREASE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYINCREASE));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYINCREASE,0);}
        //DUSTMAGIC Capacity Multiplier
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYMULTIPLIER))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYMULTIPLIER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYMULTIPLIER));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYMULTIPLIER,0);}

        //DUSTMAGIC Fire Count
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE,0);}
        //DUSTMAGIC Fire Max
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_FIRE))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_FIRE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_FIRE));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_FIRE,10);}
        //DUSTMAGIC WATER Count
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER,0);}
        //DUSTMAGIC WATER Max
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_WATER))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_WATER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_WATER));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_WATER,10);}
        //DUSTMAGIC EARTH Count
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH,0);}
        //DUSTMAGIC EARTH Max
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_EARTH))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_EARTH,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_EARTH));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_EARTH,10);}
        //DUSTMAGIC CHAOS Count
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS,0);}
        //DUSTMAGIC CHAOS Max
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_CHAOS))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_CHAOS,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_CHAOS));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_CHAOS,10);}
        //DUSTMAGIC ORDER Count
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER,0);}
        //DUSTMAGIC ORDER Max
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_ORDER))
        {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_ORDER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_ORDER));}
        else {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_ORDER,10);}

        //TODO: Dont forget to init researchData later


    }

    //Do i need these if im syncing my stuff in the registry with the custom methods????
    @SubscribeEvent
    public static void setPlayersAttributesOnClone(PlayerEvent.Clone event) {
        Player newPlayer = event.getEntity();
        if(DustMagicAttachmentHelper.hasUnlockedMana(event.getOriginal())){DustMagicAttachmentHelper.unlockMana(newPlayer);}
        else{DustMagicAttachmentHelper.lockMana(newPlayer);}
        DustMagicAttachmentHelper.setMana(newPlayer, event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA));
        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MAXMANA,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_MAXMANA));
        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYMULTIPLIER,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYMULTIPLIER));
        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYINCREASE,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYINCREASE));
        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_REGENERATION_RATE,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_REGENERATION_RATE));

        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_AFFINITY,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_AFFINITY));

        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYMULTIPLIER,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYMULTIPLIER));
        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYINCREASE,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYINCREASE));

        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_FIRE,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_FIRE));
        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE));

        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_WATER,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_WATER));
        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER));

        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_EARTH,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_EARTH));
        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH));

        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_CHAOS,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_CHAOS));
        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS));

        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_ORDER,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_ORDER));
        newPlayer.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER,event.getOriginal().getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER));

    }

    @SubscribeEvent
    public static void setPlayersAttributesOnDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if(DustMagicAttachmentHelper.hasUnlockedMana(player)){DustMagicAttachmentHelper.unlockMana(player);}
        else{DustMagicAttachmentHelper.lockMana(player);}
        DustMagicAttachmentHelper.setMana(player, player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MAXMANA,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MAXMANA));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYMULTIPLIER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYMULTIPLIER));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYINCREASE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYINCREASE));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_REGENERATION_RATE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_REGENERATION_RATE));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_AFFINITY,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_AFFINITY));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYMULTIPLIER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYMULTIPLIER));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYINCREASE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYINCREASE));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_FIRE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_FIRE));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_WATER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_WATER));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_EARTH,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_EARTH));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_CHAOS,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_CHAOS));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_ORDER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_ORDER));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER));


    }

    @SubscribeEvent
    public static void setPlayersAttributesOnRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if(DustMagicAttachmentHelper.hasUnlockedMana(player)){DustMagicAttachmentHelper.unlockMana(player);}
        else{DustMagicAttachmentHelper.lockMana(player);}
        DustMagicAttachmentHelper.setMana( player, player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MAXMANA,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MAXMANA));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYMULTIPLIER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYMULTIPLIER));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYINCREASE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_CAPACITYINCREASE));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_REGENERATION_RATE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA_REGENERATION_RATE));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_AFFINITY,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_AFFINITY));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYMULTIPLIER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYMULTIPLIER));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYINCREASE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENT_CAPACITYINCREASE));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_FIRE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_FIRE));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_WATER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_WATER));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_EARTH,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_EARTH));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_CHAOS,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_CHAOS));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS));

        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_ORDER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_ORDER));
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER,player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER));
    }

    @SubscribeEvent
    public static void onCommand(RegisterCommandsEvent event) {
        new PlayerManaAdd(event.getDispatcher());
        new PlayerUnlockMana(event.getDispatcher());
        new PlayerModifyElement(event.getDispatcher());
        new AncientGlyphsCommand(event.getDispatcher());
    }


    // Dust items that land in the world turn into one orb of their element, worth the whole stack.
    // This used to loop once per item in the stack and spawn an orb (worth the full stack) every time, so a stack of 3
    // made 3 stacked orbs worth 3 each: the "orb picked up 2-3 times" bug.
    @SubscribeEvent
    public static void onDustDropped(EntityJoinLevelEvent event) {
        // EntityJoinLevelEvent fires on both logical sides; only the server spawns orbs
        if (!(event.getEntity() instanceof ItemEntity itemEntity) || !(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }

        ItemStack stack = itemEntity.getItem();
        if (!stack.is(DustTags.Items.MAGICAL_DUST_ITEMS)) {
            return;
        }

        EntityType<? extends ElementalOrbEntityBase> orbType = orbTypeFor(stack);
        if (orbType == null) {
            return;
        }

        ElementalOrbEntityBase orb = orbType.create(serverLevel, EntitySpawnReason.TRIGGERED);
        if (orb == null) {
            return;
        }
        orb.setPos(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
        orb.setValue(stack.getCount());
        serverLevel.addFreshEntity(orb);

        // Cancelling stops the item entity from being added to the level
        event.setCanceled(true);
    }

    // Which orb a dust item turns into; null for dusts without an orb
    private static @Nullable EntityType<? extends ElementalOrbEntityBase> orbTypeFor(ItemStack stack) {
        if (stack.is(DustItemRegistry.DUST_RED.get())) return DustEntityRegistry.FIRE_ORB.get();
        if (stack.is(DustItemRegistry.DUST_BLUE.get())) return DustEntityRegistry.WATER_ORB.get();
        if (stack.is(DustItemRegistry.DUST_GREEN.get())) return DustEntityRegistry.EARTH_ORB.get();
        if (stack.is(DustItemRegistry.DUST_BLACK.get())) return DustEntityRegistry.CHAOS_ORB.get();
        if (stack.is(DustItemRegistry.DUST_WHITE.get())) return DustEntityRegistry.ORDER_ORB.get();
        return null;
    }
}
