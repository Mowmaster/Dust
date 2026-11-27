package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

// A key to the Arcane Vaults of one element (dust:<set>_vault_key): use it on a vault of its element and the vault opens
// for you, once (ArcaneVaultBlockEntity), using the key up. An Arcane Spawner of that element sometimes gives one to each
// player who helped beat its wave (ArcaneSpawnerBlockEntity).
public class VaultKeyItem extends Item
{
    private final ElementalArmorSet set;

    public VaultKeyItem(ElementalArmorSet set, Properties properties)
    {
        super(properties);
        this.set = set;
    }

    public ElementalArmorSet set()
    {
        return set;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        super.appendHoverText(stack, context, display, builder, flag);
        builder.accept(Component.translatable("dust.vault_key.opens", Component.translatable("dust.armor_set." + set.id)).withStyle(ChatFormatting.GRAY));
    }
}
