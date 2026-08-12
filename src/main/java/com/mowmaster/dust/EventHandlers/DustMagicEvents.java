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
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumAffinity;
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
    public static void onDustDropped(EntityJoinLevelEvent event) {
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

    @SubscribeEvent
    public static void onElementalOrbPickedUp()
    {

    }
}
