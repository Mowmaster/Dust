package com.mowmaster.dust.Features.Pedestals;

import com.mowmaster.dust.DustReferences;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegistryMenuTypePedestal
{
    public static DeferredRegister<MenuType<?>> PEDESTAL_MENUS =
            DeferredRegister.create(Registries.MENU, DustReferences.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<MenuPedestal>> PEDESTAL_MENU =
            registerMenuType("pedestal_menu", MenuPedestal::new);

    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory)
    {
        return PEDESTAL_MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus)
    {
        PEDESTAL_MENUS.register(eventBus);
    }
}
