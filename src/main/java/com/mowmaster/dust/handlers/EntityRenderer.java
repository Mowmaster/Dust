package com.mowmaster.dust.handlers;

import com.mowmaster.dust.entities.EntityDustRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.items.tools.IItemColorArrow;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@Mod.EventBusSubscriber(value = Side.CLIENT,modid = Reference.MODID)
@SideOnly(Side.CLIENT)
public class EntityRenderer {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(EntityDustRegistry.ENTITIES);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerItemColourHandlers(final ColorHandlerEvent.Item event) {
        final ItemColors itemColors = event.getItemColors();

        final IItemColor INSTANCE = new IItemColorArrow();
        itemColors.registerItemColorHandler(INSTANCE, ItemRegistry.dustTippedArrow);
    }

}
