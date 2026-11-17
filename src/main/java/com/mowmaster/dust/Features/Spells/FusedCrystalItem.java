package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustComposition.ElementNames;
import com.mowmaster.dust.Features.Spells.modifiers.FusionModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

// A Fused Crystal: what's left of a fused spell when it's broken (pylons/SpellLinks), carrying its fusion (the
// dust:fusion component). Drawn in its two elements' colours (Client/FusedCrystalTint: layer 0 the first, layer 1 the
// second). Used on a Spell Pylon with no element, the spell starts as that fusion (SpellPylonBlockEntity.setFused).
public class FusedCrystalItem extends Item
{
    public FusedCrystalItem(Properties properties)
    {
        super(properties);
    }

    public static ItemStack of(Identifier fusion)
    {
        ItemStack stack = new ItemStack(DustItemRegistry.FUSED_CRYSTAL.get());
        stack.set(DustComponentDataRegistry.FUSION.get(), fusion);
        return stack;
    }

    public static @Nullable Identifier fusionOf(ItemStack stack)
    {
        return stack.get(DustComponentDataRegistry.FUSION.get());
    }

    // The fusion itself (null if the stack has none, or it's gone)
    public static @Nullable FusionModifier modifierOf(ItemStack stack)
    {
        Identifier id = fusionOf(stack);
        return id != null && SpellRegistries.MODIFIER_REGISTRY.getValue(id) instanceof FusionModifier fusion ? fusion : null;
    }

    // "Fused Crystal (Excavation)"
    @Override
    public Component getName(ItemStack stack)
    {
        Identifier id = fusionOf(stack);
        if (id == null) return super.getName(stack);
        return Component.translatable("item.dust.fused_crystal.named", Component.translatable("spell_modifier." + id.getNamespace() + "." + id.getPath()));
    }

    // Its two elements
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        FusionModifier fusion = modifierOf(stack);
        if (fusion != null) {
            builder.accept(Component.translatable("item.dust.fused_crystal.elements", ElementNames.plainForClient(fusion.first()), ElementNames.plainForClient(fusion.second()))
                    .withStyle(ChatFormatting.GRAY));
        }
        builder.accept(Component.translatable("item.dust.fused_crystal.use").withStyle(ChatFormatting.DARK_GRAY));
        super.appendHoverText(stack, context, display, builder, flag);
    }
}
