package com.mowmaster.dust.DustRegistries;

// Stonecutter: 26.1.2 only. On 26.3 brewing recipes are data (src/generated/.../recipe/brewing, made by datagen), and
// this file is all comment. 26.1.2 has no data-driven brewing ("minecraft:brewing" recipes don't exist there, so the
// 26.1.2 build leaves those files out; see build.gradle), so the recipe is added in code instead.
// One addMix covers the potion, splash and lingering versions: vanilla's container mixes already turn any potion into
// splash (gunpowder) and lingering (dragon's breath).
//? if <26.3 {
/*import com.mowmaster.dust.DustReferences;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

// @EventBusSubscriber registers this class's @SubscribeEvent methods by itself, so nothing else refers to this class
@EventBusSubscriber(modid = DustReferences.MODID)
public class DustBrewingLegacy
{
    @SubscribeEvent
    public static void registerBrewing(RegisterBrewingRecipesEvent event)
    {
        // Fire Resistance + blaze powder = Scorch (matches the 26.3 recipes)
        event.getBuilder().addMix(Potions.FIRE_RESISTANCE, Items.BLAZE_POWDER, DustPotionRegistry.POTION_SCORCH);
    }
}
*///?}
