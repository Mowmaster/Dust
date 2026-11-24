package com.mowmaster.dust.Features.Flora;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.function.Supplier;

// The elemental flowers: one for each primal and each element made of two primals, growing in the biomes rich in its
// element (Features/Worldgen/ElementalFlowerFeature). Each is a vanilla flower infused with its element's dust at the
// Element Crafter once it has been researched (seen with Research Notes); stews made with it give its effect.
//   name              element    biomes                          made from          stew effect
//   emberbell         Fire       deserts, badlands               poppy              Fire Resistance
//   tidecall_lily     Water      rivers, beaches, swamps         blue orchid        Water Breathing
//   amberglaze_lily   Earth      plains, forests                 orange tulip       Resistance
//   peakwhite         Order      mountain peaks, snowy biomes    lily of the valley Slow Falling
//   duskbloom         Chaos      spooky biomes, swamps           allium             Blindness
//   sunspark          Energy     savannas, deserts               dandelion          Speed
//   cinderpod         Explosion  badlands, savannas              red tulip          Strength
//   frostcall         Ice        snowy and icy biomes            azure bluet        Slowness
//   deepglow          Abyss      swamps, beaches                 cornflower         Night Vision
//   jadeleaf          Stone      mountain slopes, windswept      oxeye daisy        Resistance
//   mirecap           Mud        swamps, mushroom fields         brown mushroom     Poison
//   windwheel (the Windwheel Aster, Dust's first flower: flower_windwheel)
//                     Air        windswept, plains, hills        pink tulip         Wind Charged
//   dewcup            Life       jungles, lush caves             white tulip        Regeneration
//   gildthorn         Metal      badlands, savannas              torchflower        Haste
public enum ElementalFlower
{
    EMBERBELL("emberbell", () -> DustElements.FIRE, List.of(Tags.Biomes.IS_DESERT, Tags.Biomes.IS_BADLANDS), () -> Items.POPPY, MobEffects.FIRE_RESISTANCE, 8),
    TIDECALL_LILY("tidecall_lily", () -> DustElements.WATER, List.of(Tags.Biomes.IS_RIVER, Tags.Biomes.IS_BEACH, Tags.Biomes.IS_SWAMP), () -> Items.BLUE_ORCHID, MobEffects.WATER_BREATHING, 8),
    AMBERGLAZE_LILY("amberglaze_lily", () -> DustElements.EARTH, List.of(Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST), () -> Items.ORANGE_TULIP, MobEffects.RESISTANCE, 6),
    PEAKWHITE("peakwhite", () -> DustElements.ORDER, List.of(Tags.Biomes.IS_MOUNTAIN_PEAK, Tags.Biomes.IS_SNOWY), () -> Items.LILY_OF_THE_VALLEY, MobEffects.SLOW_FALLING, 8),
    DUSKBLOOM("duskbloom", () -> DustElements.CHAOS, List.of(Tags.Biomes.IS_SPOOKY, Tags.Biomes.IS_SWAMP), () -> Items.ALLIUM, MobEffects.BLINDNESS, 5),
    SUNSPARK("sunspark", () -> DustElements.ENERGY, List.of(Tags.Biomes.IS_SAVANNA, Tags.Biomes.IS_DESERT), () -> Items.DANDELION, MobEffects.SPEED, 8),
    CINDERPOD("cinderpod", () -> DustElements.EXPLOSION, List.of(Tags.Biomes.IS_BADLANDS, Tags.Biomes.IS_SAVANNA), () -> Items.RED_TULIP, MobEffects.STRENGTH, 6),
    FROSTCALL("frostcall", () -> DustElements.ICE, List.of(Tags.Biomes.IS_SNOWY, Tags.Biomes.IS_ICY), () -> Items.AZURE_BLUET, MobEffects.SLOWNESS, 6),
    DEEPGLOW("deepglow", () -> DustElements.ABYSS, List.of(Tags.Biomes.IS_SWAMP, Tags.Biomes.IS_BEACH), () -> Items.CORNFLOWER, MobEffects.NIGHT_VISION, 8),
    JADELEAF("jadeleaf", () -> DustElements.STONE, List.of(Tags.Biomes.IS_MOUNTAIN_SLOPE, Tags.Biomes.IS_WINDSWEPT, Tags.Biomes.IS_STONY_SHORES), () -> Items.OXEYE_DAISY, MobEffects.RESISTANCE, 6),
    MIRECAP("mirecap", () -> DustElements.MUD, List.of(Tags.Biomes.IS_SWAMP, Tags.Biomes.IS_MUSHROOM), () -> Items.BROWN_MUSHROOM, MobEffects.POISON, 6),
    WINDWHEEL("flower_windwheel", () -> DustElements.AIR, List.of(Tags.Biomes.IS_WINDSWEPT, Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_HILL), () -> Items.PINK_TULIP, MobEffects.WIND_CHARGED, 10),
    DEWCUP("dewcup", () -> DustElements.LIFE, List.of(Tags.Biomes.IS_JUNGLE, Tags.Biomes.IS_LUSH), () -> Items.WHITE_TULIP, MobEffects.REGENERATION, 6),
    GILDTHORN("gildthorn", () -> DustElements.METAL, List.of(Tags.Biomes.IS_BADLANDS, Tags.Biomes.IS_SAVANNA), () -> Items.TORCHFLOWER, MobEffects.HASTE, 8);

    // The dust an elemental flower is worth (the orb it gives when broken without shears or Silk Touch)
    public static final int VALUE = 2;

    public final String id;
    private final Supplier<DustElement> element;
    public final List<TagKey<Biome>> biomes;
    public final Supplier<Item> source;
    public final Holder<MobEffect> stewEffect;
    public final float stewSeconds;

    ElementalFlower(String id, Supplier<DustElement> element, List<TagKey<Biome>> biomes, Supplier<Item> source, Holder<MobEffect> stewEffect, float stewSeconds)
    {
        this.id = id;
        this.element = element;
        this.biomes = biomes;
        this.source = source;
        this.stewEffect = stewEffect;
        this.stewSeconds = stewSeconds;
    }

    public DustElement element()
    {
        return element.get();
    }

    public boolean growsIn(Holder<Biome> biome)
    {
        for (TagKey<Biome> tag : biomes) if (biome.is(tag)) return true;
        return false;
    }
}
