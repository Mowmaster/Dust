package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.DecipherMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DustMenuRegistry
{
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, DustReferences.MODID);

    // IMenuTypeExtension.create: a menu type whose client side is built from extra data the server sends when it
    // opens (here: the word and which hand holds the tablet)
    public static final Supplier<MenuType<DecipherMenu>> DECIPHER = MENUS.register("decipher",
            () -> IMenuTypeExtension.create(DecipherMenu::new));

    public static void register(IEventBus eventBus)
    {
        MENUS.register(eventBus);
    }
}
