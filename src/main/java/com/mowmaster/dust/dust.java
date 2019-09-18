package com.mowmaster.dust;

import com.mowmaster.dust.blocks.BlockDustStone;
import com.mowmaster.dust.crafting.SpellCraftingBasic;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LifecycleEventProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;


@EventBusSubscriber(modid = Reference.MODID, bus = Bus.MOD)
@Mod(Reference.MODID)
public class dust
{



    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        BlockDustStone.onItemRegistryReady(event);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        BlockDustStone.onBlockRegistryReady(event);
    }


}