package com.mowmaster.dust.Features.DustComposition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Every element of dust, in order from the primals up. Fire, water and earth are the red, blue and green of a colour;
// order makes it paler and chaos darker (DustComposition#colorOf). Each mix is listed with its parts.
//
// Other mods can add elements with register (from their mod constructor, so both sides have them), and give them an
// icon at textures/gui/sprites/element/<path>.png and a name at dust.element.<namespace>.<path>.
public final class DustElements
{
    // LinkedHashMap keeps them in the order they were registered (the order the book lists them in)
    private static final Map<Identifier, DustElement> ELEMENTS = Collections.synchronizedMap(new LinkedHashMap<>());


    // ---- Primal: the five dusts ----
    public static final DustElement FIRE = primal("fire", EnumElement.FIRE);
    public static final DustElement WATER = primal("water", EnumElement.WATER);
    public static final DustElement EARTH = primal("earth", EnumElement.EARTH);
    public static final DustElement ORDER = primal("order", EnumElement.ORDER);
    public static final DustElement CHAOS = primal("chaos", EnumElement.CHAOS);

    // ---- A primal + order (paler) or chaos (darker) ----
    public static final DustElement ENERGY = register("energy", FIRE, ORDER);
    // uncontrolled fire (gunpowder, TNT, creepers)
    public static final DustElement EXPLOSION = register("explosion", FIRE, CHAOS);
    public static final DustElement ICE = register("ice", WATER, ORDER);
    // the deep, dark ocean (ink, prismarine, the heart of the sea)
    public static final DustElement ABYSS = register("abyss", WATER, CHAOS);
    public static final DustElement STONE = register("stone", EARTH, ORDER);
    public static final DustElement MUD = register("mud", EARTH, CHAOS);

    // ---- Secondary: two primal colours mixed, then + order / chaos ----
    public static final DustElement AIR = register("air", FIRE, WATER);
    public static final DustElement VACUUM = register("vacuum", AIR, CHAOS);
    public static final DustElement MOTION = register("motion", AIR, ORDER);

    public static final DustElement LIFE = register("life", WATER, EARTH);
    public static final DustElement DEATH = register("death", LIFE, CHAOS);
    // life restored (golden apples, regeneration, totems)
    public static final DustElement HEALING = register("healing", LIFE, ORDER);

    public static final DustElement METAL = register("metal", FIRE, EARTH);
    public static final DustElement TOOL = register("tool", METAL, ORDER);
    public static final DustElement DAMAGE = register("damage", METAL, CHAOS);

    // ---- Tertiary: a secondary + a primal colour, then + order / chaos ----
    public static final DustElement LIGHT = register("light", FIRE, AIR);
    public static final DustElement DARKNESS = register("darkness", LIGHT, CHAOS);
    // pure, bright light (glowstone, beacons, gold's shine)
    public static final DustElement RADIANCE = register("radiance", LIGHT, ORDER);

    public static final DustElement LIGHTNING = register("lightning", AIR, WATER);
    public static final DustElement FLIGHT = register("flight", LIGHTNING, ORDER);
    public static final DustElement CHANGE = register("change", LIGHTNING, CHAOS);

    public static final DustElement SLIME = register("slime", LIFE, WATER);
    public static final DustElement POISON = register("poison", SLIME, CHAOS);
    public static final DustElement SPIRIT = register("spirit", SLIME, ORDER);

    // colour wheel name is spring green (0,255,128)
    public static final DustElement SEEDS = register("seeds", LIFE, EARTH);
    // tended, ordered growth (crops, farmland, bone meal)
    public static final DustElement GROWTH = register("growth", SEEDS, ORDER);
    // growth from rot (mushrooms, nether fungi, mycelium)
    public static final DustElement FUNGUS = register("fungus", SEEDS, CHAOS);

    // colour wheel name is chartreuse (128,255,0)
    public static final DustElement ARMOR = register("armor", METAL, EARTH);
    // armour made whole (shields, armour, blast-proof blocks)
    public static final DustElement PROTECTION = register("protection", ARMOR, ORDER);
    // armour wearing away (oxidised copper, weathering)
    public static final DustElement RUST = register("rust", ARMOR, CHAOS);

    public static final DustElement CRYSTAL = register("crystal", METAL, FIRE);
    // cut, perfect crystal (diamonds, emeralds)
    public static final DustElement GEM = register("gem", CRYSTAL, ORDER);
    // broken, raw crystal (shards, dripstone, rough ore); dark orange is brown, the brown dye's element
    public static final DustElement FRACTURE = register("fracture", CRYSTAL, CHAOS);

    // ---- Quaternary: mixes of mixes ----
    public static final DustElement KNOWLEDGE = register("knowledge", SPIRIT, LIFE);
    public static final DustElement CREATURE = register("creature", LIFE, SEEDS);
    public static final DustElement FLESH = register("flesh", CREATURE, CHAOS);
    public static final DustElement PLANTS = register("plants", SEEDS, EARTH);
    public static final DustElement TREES = register("trees", PLANTS, EARTH);
    public static final DustElement VOID = register("void", VACUUM, CHAOS);
    // spirit bound or lost (soul sand, soul fire, the sculk)
    public static final DustElement SOUL = register("soul", SPIRIT, CHAOS);
    // knowledge given power (enchanting, potions brewing, experience)
    public static final DustElement MAGIC = register("magic", KNOWLEDGE, ENERGY);
    // change through the void (ender pearls, the End, shulkers, chorus)
    public static final DustElement WARP = register("warp", CHANGE, VOID);
    // moving air (note blocks, music discs, goat horns, bells)
    public static final DustElement SOUND = register("sound", MOTION, AIR);

    private DustElements() {}

    private static DustElement primal(String name, EnumElement primal)
    {
        return add(new DustElement(DustElement.dustId(name), primal));
    }

    private static DustElement register(String name, DustElement... parts)
    {
        return register(DustElement.dustId(name), parts);
    }

    // A new element mixed from others (DustElement... is "varargs": any number of elements, passed as an array)
    public static DustElement register(Identifier id, DustElement... parts)
    {
        return add(new DustElement(id, List.of(parts)));
    }

    private static DustElement add(DustElement element)
    {
        if (ELEMENTS.putIfAbsent(element.id(), element) != null) throw new IllegalArgumentException("Dust element registered twice: " + element.id());
        return element;
    }

    public static @Nullable DustElement get(Identifier id)
    {
        return ELEMENTS.get(id);
    }

    // The primal element for one of the five dusts
    public static DustElement of(EnumElement primal)
    {
        return switch (primal) {
            case FIRE -> FIRE;
            case WATER -> WATER;
            case EARTH -> EARTH;
            case CHAOS -> CHAOS;
            case ORDER -> ORDER;
        };
    }

    public static List<DustElement> all()
    {
        synchronized (ELEMENTS) {
            return new ArrayList<>(ELEMENTS.values());
        }
    }

    // Saved / sent as its id ("dust:knowledge"). comapFlatMap: reading can fail (an unknown id) and says why;
    // writing just takes the id.
    public static final Codec<DustElement> CODEC = Identifier.CODEC.comapFlatMap(
            id -> {
                DustElement element = get(id);
                return element != null ? DataResult.success(element) : DataResult.error(() -> "Unknown dust element: " + id);
            },
            DustElement::id);
}
