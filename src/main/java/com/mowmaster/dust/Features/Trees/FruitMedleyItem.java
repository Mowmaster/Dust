package com.mowmaster.dust.Features.Trees;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.ElementNames;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.function.Consumer;

// A Fruit Medley (FruitMedleyRecipe: a bowl and six elemental fruits of up to three kinds). Eaten, it gives the
// affinity of each kind of fruit in it (FruitAffinity) for 5 minutes per fruit of that kind: six of one kind is one
// affinity for 30 minutes; 3 + 2 + 1 is three affinities (each at the three-affinity bonuses) for 15, 10 and 5 minutes.
// Like any fruit affinity it replaces those already running. Leaves its bowl.
public class FruitMedleyItem extends Item
{
    public static final FoodProperties FOOD = new FoodProperties.Builder().nutrition(10).saturationModifier(0.6F).alwaysEdible().build();

    public FruitMedleyItem(Properties properties)
    {
        super(properties.stacksTo(1).food(FOOD).usingConvertsTo(Items.BOWL));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity)
    {
        FruitMedley medley = itemStack.get(DustComponentDataRegistry.FRUIT_MEDLEY.get());
        if (!level.isClientSide() && medley != null) FruitAffinity.give(entity, medley.affinities());
        return super.finishUsingItem(itemStack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        super.appendHoverText(stack, context, display, builder, flag);
        FruitMedley medley = stack.get(DustComponentDataRegistry.FRUIT_MEDLEY.get());
        if (medley == null) return;
        Map<DustElement, Integer> affinities = medley.affinities();
        affinities.forEach((element, count) -> builder.accept(affinityLine(element, count)));
        builder.accept(tierLine(affinities.size()));
    }

    // "[icon] Fire Affinity (30 min)"
    public static Component affinityLine(DustElement element, int count)
    {
        return Component.translatable("dust.affinity.line", ElementNames.forClient(element), FruitAffinity.BASE_TICKS * count / 1200)
                .withStyle(ChatFormatting.GRAY);
    }

    // What this many affinities at once give each of them
    public static Component tierLine(int affinities)
    {
        float[] tier = FruitAffinity.tier(affinities);
        return Component.translatable("dust.affinity.tier." + Math.max(1, Math.min(3, affinities)),
                Math.round(tier[0] * 100), Math.round(tier[1] * 100), Math.round(tier[2] * 100)).withStyle(ChatFormatting.DARK_GRAY);
    }
}
