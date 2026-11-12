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

// Every element of dust, in order from the primals up, each with its phase (ElementPhase: gaseous, aqueous,
// amorphous or crystalline). Fire, water and earth are the red, blue and green of a colour;
// order makes it paler and chaos darker (DustComposition#colorOf). Each mix is listed with its parts.
//
// Other mods can add elements with register (from their mod constructor, so both sides have them), and give them an
// icon at textures/gui/sprites/element/<path>.png and a name at dust.element.<namespace>.<path>.
public final class DustElements
{
    // LinkedHashMap keeps them in the order they were registered (the order the book lists them in)
    private static final Map<Identifier, DustElement> ELEMENTS = Collections.synchronizedMap(new LinkedHashMap<>());

    // Saved / sent as its id ("dust:knowledge"). comapFlatMap: reading can fail (an unknown id) and says why;
    // writing just takes the id.
    // It must come before the elements below: static fields are set in the order they're written, and making an
    // element works out its colour in DustComposition, whose own CODEC uses this one. Declared after the elements, it
    // was still null at that point.
    public static final Codec<DustElement> CODEC = Identifier.CODEC.comapFlatMap(
            id -> {
                DustElement element = get(id);
                return element != null ? DataResult.success(element) : DataResult.error(() -> "Unknown dust element: " + id);
            },
            DustElement::id);

    // ---- Primal: the five dusts ----
    public static final DustElement FIRE = primal("fire", EnumElement.FIRE, ElementPhase.AMORPHOUS);
    public static final DustElement WATER = primal("water", EnumElement.WATER, ElementPhase.AQUEOUS);
    public static final DustElement EARTH = primal("earth", EnumElement.EARTH, ElementPhase.CRYSTALLINE);
    public static final DustElement ORDER = primal("order", EnumElement.ORDER, ElementPhase.CRYSTALLINE);
    public static final DustElement CHAOS = primal("chaos", EnumElement.CHAOS, ElementPhase.GASEOUS);

    // ---- A primal + order (paler) or chaos (darker) ----
    public static final DustElement ENERGY = register("energy", ElementPhase.GASEOUS, FIRE, ORDER);
    // uncontrolled fire (gunpowder, TNT, creepers)
    public static final DustElement EXPLOSION = register("explosion", ElementPhase.AMORPHOUS, FIRE, CHAOS);
    public static final DustElement ICE = register("ice", ElementPhase.CRYSTALLINE, WATER, ORDER);
    // the deep, dark ocean (ink, prismarine, the heart of the sea)
    public static final DustElement ABYSS = register("abyss", ElementPhase.AQUEOUS, WATER, CHAOS);
    public static final DustElement STONE = register("stone", ElementPhase.CRYSTALLINE, EARTH, ORDER);
    public static final DustElement MUD = register("mud", ElementPhase.AMORPHOUS, EARTH, CHAOS);

    // ---- Secondary: two primal colours mixed, then + order / chaos ----
    public static final DustElement AIR = register("air", ElementPhase.GASEOUS, FIRE, WATER);
    public static final DustElement VACUUM = register("vacuum", ElementPhase.GASEOUS, AIR, CHAOS);
    public static final DustElement MOTION = register("motion", ElementPhase.GASEOUS, AIR, ORDER);

    public static final DustElement LIFE = register("life", ElementPhase.GASEOUS, WATER, EARTH);
    public static final DustElement DEATH = register("death", ElementPhase.CRYSTALLINE, LIFE, CHAOS);
    // life restored (golden apples, regeneration, totems)
    public static final DustElement HEALING = register("healing", ElementPhase.AQUEOUS, LIFE, ORDER);

    public static final DustElement METAL = register("metal", ElementPhase.CRYSTALLINE, FIRE, EARTH);
    public static final DustElement TOOL = register("tool", ElementPhase.CRYSTALLINE, METAL, ORDER);
    public static final DustElement DAMAGE = register("damage", ElementPhase.GASEOUS, METAL, CHAOS);

    // ---- Tertiary: a secondary + a primal colour, then + order / chaos ----
    public static final DustElement LIGHT = register("light", ElementPhase.GASEOUS, FIRE, AIR);
    public static final DustElement DARKNESS = register("darkness", ElementPhase.GASEOUS, LIGHT, CHAOS);
    // pure, bright light (glowstone, beacons, gold's shine)
    public static final DustElement RADIANCE = register("radiance", ElementPhase.GASEOUS, LIGHT, ORDER);

    public static final DustElement LIGHTNING = register("lightning", ElementPhase.AMORPHOUS, AIR, WATER);
    public static final DustElement FLIGHT = register("flight", ElementPhase.GASEOUS, LIGHTNING, ORDER);
    public static final DustElement CHANGE = register("change", ElementPhase.AQUEOUS, LIGHTNING, CHAOS);

    public static final DustElement SLIME = register("slime", ElementPhase.AMORPHOUS, LIFE, WATER);
    public static final DustElement POISON = register("poison", ElementPhase.AQUEOUS, SLIME, CHAOS);
    public static final DustElement SPIRIT = register("spirit", ElementPhase.GASEOUS, SLIME, ORDER);

    // colour wheel name is spring green (0,255,128)
    public static final DustElement SEEDS = register("seeds", ElementPhase.CRYSTALLINE, LIFE, EARTH);
    // tended, ordered growth (crops, farmland, bone meal)
    public static final DustElement GROWTH = register("growth", ElementPhase.AMORPHOUS, SEEDS, ORDER);
    // growth from rot (mushrooms, nether fungi, mycelium)
    public static final DustElement FUNGUS = register("fungus", ElementPhase.CRYSTALLINE, SEEDS, CHAOS);

    // colour wheel name is chartreuse (128,255,0)
    public static final DustElement ARMOR = register("armor", ElementPhase.CRYSTALLINE, METAL, EARTH);
    // armour made whole (shields, armour, blast-proof blocks)
    public static final DustElement PROTECTION = register("protection", ElementPhase.CRYSTALLINE, ARMOR, ORDER);
    // armour wearing away (oxidised copper, weathering)
    public static final DustElement RUST = register("rust", ElementPhase.AMORPHOUS, ARMOR, CHAOS);

    public static final DustElement CRYSTAL = register("crystal", ElementPhase.CRYSTALLINE, METAL, FIRE);
    // cut, perfect crystal (diamonds, emeralds)
    public static final DustElement GEM = register("gem", ElementPhase.CRYSTALLINE, CRYSTAL, ORDER);
    // broken, raw crystal (shards, dripstone, rough ore); dark orange is brown, the brown dye's element
    public static final DustElement FRACTURE = register("fracture", ElementPhase.AMORPHOUS, CRYSTAL, CHAOS);

    // ---- Quaternary: mixes of mixes ----
    public static final DustElement KNOWLEDGE = register("knowledge", ElementPhase.AQUEOUS, SPIRIT, LIFE);
    public static final DustElement CREATURE = register("creature", ElementPhase.AMORPHOUS, LIFE, SEEDS);
    public static final DustElement FLESH = register("flesh", ElementPhase.AMORPHOUS, CREATURE, CHAOS);
    public static final DustElement PLANTS = register("plants", ElementPhase.CRYSTALLINE, SEEDS, EARTH);
    public static final DustElement TREES = register("trees", ElementPhase.CRYSTALLINE, PLANTS, EARTH);
    public static final DustElement VOID = register("void", ElementPhase.GASEOUS, VACUUM, CHAOS);
    // spirit bound or lost (soul sand, soul fire, the sculk)
    public static final DustElement SOUL = register("soul", ElementPhase.GASEOUS, SPIRIT, CHAOS);
    // knowledge given power (enchanting, potions brewing, experience)
    public static final DustElement MAGIC = register("magic", ElementPhase.AQUEOUS, KNOWLEDGE, ENERGY);
    // change through the void (ender pearls, the End, shulkers, chorus)
    public static final DustElement WARP = register("warp", ElementPhase.GASEOUS, CHANGE, VOID);
    // moving air (note blocks, music discs, goat horns, bells)
    public static final DustElement SOUND = register("sound", ElementPhase.GASEOUS, MOTION, AIR);

    private DustElements() {}

    private static DustElement primal(String name, EnumElement primal, ElementPhase phase)
    {
        return add(new DustElement(DustElement.dustId(name), primal, phase));
    }

    private static DustElement register(String name, ElementPhase phase, DustElement... parts)
    {
        return register(DustElement.dustId(name), phase, parts);
    }

    // A new element mixed from others, and its phase (DustElement... is "varargs": any number of elements, passed as
    // an array; it has to come last)
    public static DustElement register(Identifier id, ElementPhase phase, DustElement... parts)
    {
        return add(new DustElement(id, List.of(parts), phase));
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
}
