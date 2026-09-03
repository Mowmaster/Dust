package com.mowmaster.dust.Features.Pedestals;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.CrystalBlocks.Item.CrystalChiselBaseItem;
import com.mowmaster.dust.Features.CrystalBlocks.Item.CrystalHalfSawItem;
import com.mowmaster.dust.Features.CrystalTools.Item.CrystalArmorItem;
import com.mowmaster.dust.Features.CrystalTools.Item.CrystalToolHammer;
import com.mowmaster.dust.Features.CrystalTools.Item.CrystalToolMattock;
import com.mowmaster.dust.Features.CrystalTools.Item.CrystalWeaponSword;
import com.mowmaster.dust.Features.CrystalTools.Materials.DustMaterialArmor;
import com.mowmaster.dust.Features.CrystalTools.Materials.DustMaterialTools;
import com.mowmaster.dust.Features.DustyDelights.DDFoodProperties;
import com.mowmaster.dust.Features.EffectScrolls.Item.EarthStickItem;
import com.mowmaster.dust.Features.EffectScrolls.Item.FireStickItem;
import com.mowmaster.dust.Features.FocusedBooks.EnchantableBookItem;
import com.mowmaster.dust.Features.FocusedBooks.FocusedBookItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

public class RegistryPedestalItem
{
    public static final DeferredRegister.Items PEDESTALITEMS = DeferredRegister.createItems(DustReferences.MODID+"_pedestal");


    public static void register(IEventBus eventBus)
    {
        PEDESTALITEMS.register(eventBus);
    }

}
