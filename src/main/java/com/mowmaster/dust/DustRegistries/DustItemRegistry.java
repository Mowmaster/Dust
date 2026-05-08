package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.Features.CrystalBlocks.Item.CrystalChiselBaseItem;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.CrystalBlocks.Item.CrystalHalfSawItem;
import com.mowmaster.dust.Features.DustyDelights.DDFoodProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

public class DustItemRegistry
{
    public static final DeferredRegister.Items DUSTITEMS = DeferredRegister.createItems(DustReferences.MODID);

    /*
    Crystal Blocks Features
     */


    public static final DeferredItem<Item> DUST_RED = DUSTITEMS.registerSimpleItem("dust_red", properties -> properties);
    public static final DeferredItem<Item> DUST_GREEN = DUSTITEMS.registerSimpleItem("dust_green", properties -> properties);
    public static final DeferredItem<Item> DUST_BLUE = DUSTITEMS.registerSimpleItem("dust_blue", properties -> properties);
    public static final DeferredItem<Item> DUST_WHITE = DUSTITEMS.registerSimpleItem("dust_white", properties -> properties);
    public static final DeferredItem<Item> DUST_BLACK = DUSTITEMS.registerSimpleItem("dust_black", properties -> properties);


    public static final DeferredItem<Item> CRYSTAL_INERT = DUSTITEMS.registerSimpleItem("crystal_inert", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_RED = DUSTITEMS.registerSimpleItem("crystal_red", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_GREEN = DUSTITEMS.registerSimpleItem("crystal_green", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_BLUE = DUSTITEMS.registerSimpleItem("crystal_blue", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_WHITE = DUSTITEMS.registerSimpleItem("crystal_white", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_BLACK = DUSTITEMS.registerSimpleItem("crystal_black", properties -> properties);

    public static final DeferredItem<Item> CRYSTAL_CHISEL_IRON = DUSTITEMS.registerItem("chisel_iron", properties -> new CrystalChiselBaseItem(properties.durability(100)));
    public static final DeferredItem<Item> CRYSTAL_HALFSAW_IRON = DUSTITEMS.registerItem("halfsaw_iron", properties -> new CrystalHalfSawItem(properties.durability(100)));

    public static final DeferredItem<Item> CORNBREAD = DUSTITEMS.registerItem("food_cornbread",
            properties -> new Item(properties.food(DDFoodProperties.CORNBREAD, DDFoodProperties.CORNBREAD_EDIBLE)) {
                @Override
                public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
                    builder.accept(Component.translatable(DustReferences.TOOLTIP + "cornbread"));
                    super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
                }
            });


    public static final DeferredItem<Item> CHARCOAL_WHITE = DUSTITEMS.registerItem("fuel_charcoal_white", Item::new);

    public static void register(IEventBus eventBus)
    {
        DUSTITEMS.register(eventBus);
    }

}
