package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

// What an Arcane Spawner (and the labyrinth's rooms) send at players, by element and tier. The tier is the strongest
// nearby player's Magic Level / 10 (one step every ten levels: TIER_LEVELS). Each element has three pools of vanilla
// mobs (by id, so the 26.3 move of the entity type constants doesn't matter):
//   basic   from tier 0
//   tough   from tier 2 (and more often as the tier rises)
//   elite   from tier 4
// and every mob is made stronger by its tier (buff): more health, damage and armour, a little faster; Resistance from
// tier 3, Strength from tier 5, and Fire Resistance for the fiery elements.
public final class SanctumMobs
{
    public static final int TIER_LEVELS = 10;

    private static final Map<ElementalArmorSet, String[][]> POOLS = new EnumMap<>(ElementalArmorSet.class);
    private static final Identifier BUFF = Identifier.fromNamespaceAndPath(DustReferences.MODID, "sanctum_tier");

    static {
        pools(ElementalArmorSet.PYREBORN, new String[] {"husk", "magma_cube"}, new String[] {"blaze", "husk"}, new String[] {"wither_skeleton", "blaze"});
        pools(ElementalArmorSet.TIDEWOVEN, new String[] {"drowned", "zombie"}, new String[] {"drowned", "witch"}, new String[] {"drowned", "vindicator"});
        pools(ElementalArmorSet.ROOTBOUND, new String[] {"zombie", "silverfish"}, new String[] {"spider", "husk"}, new String[] {"vindicator", "spider"});
        pools(ElementalArmorSet.LUMEN, new String[] {"skeleton", "zombie"}, new String[] {"pillager", "vindicator"}, new String[] {"evoker", "vindicator"});
        pools(ElementalArmorSet.RIFTBORN, new String[] {"endermite", "zombie"}, new String[] {"enderman", "endermite"}, new String[] {"enderman", "phantom"});
        pools(ElementalArmorSet.SPARKWRIGHT, new String[] {"creeper", "skeleton"}, new String[] {"witch", "pillager"}, new String[] {"breeze", "witch"});
        pools(ElementalArmorSet.BLASTWARD, new String[] {"creeper", "husk"}, new String[] {"blaze", "creeper"}, new String[] {"wither_skeleton", "blaze"});
        pools(ElementalArmorSet.RIMEFROST, new String[] {"stray", "zombie"}, new String[] {"stray", "skeleton"}, new String[] {"breeze", "stray"});
        pools(ElementalArmorSet.DEEPWATCH, new String[] {"drowned", "cave_spider"}, new String[] {"witch", "drowned"}, new String[] {"phantom", "witch"});
        pools(ElementalArmorSet.BASTION, new String[] {"silverfish", "skeleton"}, new String[] {"husk", "vindicator"}, new String[] {"vindicator", "pillager"});
        pools(ElementalArmorSet.MIREWALKER, new String[] {"slime", "bogged"}, new String[] {"witch", "slime"}, new String[] {"bogged", "witch"});
        pools(ElementalArmorSet.ZEPHYR, new String[] {"skeleton", "zombie"}, new String[] {"breeze", "skeleton"}, new String[] {"breeze", "phantom"});
        pools(ElementalArmorSet.VERDANT, new String[] {"spider", "zombie"}, new String[] {"cave_spider", "witch"}, new String[] {"evoker", "witch"});
        pools(ElementalArmorSet.GILDED, new String[] {"husk", "pillager"}, new String[] {"vindicator", "pillager"}, new String[] {"vindicator", "evoker"});
    }

    private SanctumMobs() {}

    private static void pools(ElementalArmorSet set, String[] basic, String[] tough, String[] elite)
    {
        POOLS.put(set, new String[][] {basic, tough, elite});
    }

    public static int tier(int magicLevel)
    {
        return Math.max(0, magicLevel / TIER_LEVELS);
    }

    // A mob type for this element at this tier: the basic pool, then the tougher pools as the tier allows (each
    // weighing more as the tier climbs past where it starts)
    public static @Nullable EntityType<?> pick(ElementalArmorSet set, int tier, RandomSource random)
    {
        String[][] pools = POOLS.get(set);
        List<String> choices = new ArrayList<>();
        for (String id : pools[0]) choices.add(id);
        if (tier >= 2) for (int copy = 0; copy < Math.min(3, tier - 1); copy++) for (String id : pools[1]) choices.add(id);
        if (tier >= 4) for (int copy = 0; copy < Math.min(3, tier - 3); copy++) for (String id : pools[2]) choices.add(id);
        String id = choices.get(random.nextInt(choices.size()));
        return BuiltInRegistries.ENTITY_TYPE.getOptional(Identifier.withDefaultNamespace(id)).orElse(null);
    }

    // Makes a mob as strong as its tier
    public static void buff(Mob mob, ElementalArmorSet set, int tier)
    {
        if (tier <= 0) return;
        modify(mob, Attributes.MAX_HEALTH, 0.25 * tier, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        modify(mob, Attributes.ATTACK_DAMAGE, 1.5 * tier, AttributeModifier.Operation.ADD_VALUE);
        modify(mob, Attributes.ARMOR, Math.min(16.0, 2.0 * tier), AttributeModifier.Operation.ADD_VALUE);
        modify(mob, Attributes.MOVEMENT_SPEED, Math.min(0.2, 0.03 * tier), AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        mob.setHealth(mob.getMaxHealth());
        if (tier >= 3) effect(mob, MobEffects.RESISTANCE, tier >= 7 ? 1 : 0);
        if (tier >= 5) effect(mob, MobEffects.STRENGTH, tier >= 9 ? 1 : 0);
        if (set == ElementalArmorSet.PYREBORN || set == ElementalArmorSet.BLASTWARD) effect(mob, MobEffects.FIRE_RESISTANCE, 0);
    }

    private static void modify(Mob mob, Holder<Attribute> attribute, double amount, AttributeModifier.Operation operation)
    {
        AttributeInstance instance = mob.getAttribute(attribute);
        if (instance != null && !instance.hasModifier(BUFF)) instance.addPermanentModifier(new AttributeModifier(BUFF, amount, operation));
    }

    private static void effect(Mob mob, Holder<MobEffect> effect, int amplifier)
    {
        mob.addEffect(new MobEffectInstance(effect, MobEffectInstance.INFINITE_DURATION, amplifier, false, false));
    }
}
