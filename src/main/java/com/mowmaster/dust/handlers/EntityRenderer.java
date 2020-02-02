package com.mowmaster.dust.handlers;

import com.mowmaster.dust.entities.EntityDustRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.items.tools.IItemColorArrow;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;


@Mod.EventBusSubscriber(modid = Reference.MODID)
public class EntityRenderer {
    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(EntityDustRegistry.ENTITIES);
    }

    @SubscribeEvent
    public static void registerItemColourHandlers(final ColorHandlerEvent.Item event) {
        final BlockColors blockColors = event.getBlockColors();
        final ItemColors itemColors = event.getItemColors();

        // Use the Block's colour handler for an ItemBlock
        final IItemColor itemBlockColourHandler = (stack, tintIndex) -> {
            @SuppressWarnings("deprecation")
            final IBlockState state = ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
            return blockColors.colorMultiplier(state, null, null, tintIndex);
        };

        final IItemColor INSTANCE = new IItemColorArrow();

        itemColors.registerItemColorHandler(INSTANCE, ItemRegistry.dustTippedArrow);
        //IItemColorArrow.registerArrowColors();
    }
}
