package com.mowmaster.dust.item;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemCraftingPlaceholder extends ItemUpgradeBase
{

    public ItemCraftingPlaceholder(Properties builder) {super(builder.group(dust.ITEM_GROUP));}

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Crafting Placeholder"));
    }

    public static final Item PLACEHOLDER = new ItemCraftingPlaceholder(new Properties().maxStackSize(64).group(dust.ITEM_GROUP)).setRegistryName(new ResourceLocation(MODID, "coin/placeholder"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(PLACEHOLDER);
    }


}
