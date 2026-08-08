package com.mowmaster.dust;

import com.mowmaster.dust.DustRegistries.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DustReferences.MODID)
public class Dust {
    public static final Logger LOGGER = LogUtils.getLogger();

    public Dust(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        DustItemRegistry.register(modEventBus);
        DustBlockRegistry.register(modEventBus);
        DustEntityRegistry.register(modEventBus);

        DustDataComponentTypeRegistry.register(modEventBus);
        DustAttachmentTypeRegistry.register(modEventBus);

        DustConsumeEffectsRegistry.register(modEventBus);
        DustEffectsRegistry.register(modEventBus);
        DustPotionRegistry.register(modEventBus);

        DustLootRegistry.register(modEventBus);

        DustCreativeTabRegistry.CREATIVE_MODE_TABS.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(()-> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.getId(), DustBlockRegistry.BLOCK_POTTEDFLOWER_WINDWHEEL);
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            //event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
