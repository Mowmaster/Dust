package com.mowmaster.dust.Features.Armor;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import java.util.function.Supplier;

// The elemental armor sets: one for each primal and each element of two primals, robes in the warlock style (3D models:
// Client/ElementalArmorModels), each with a 2-piece and a 4-piece bonus (ArmorSetBonuses). Worn by the element bosses,
// and found in their loot bags.
//   set           element    2 pieces                                      4 pieces
//   Pyreborn      Fire       immune to fire; Fire spells 15% stronger      melee attackers burn; Fire spells 30% cheaper
//   Tidewoven     Water      water breathing; Dolphin's Grace in water     regeneration when wet and hurt; Water spells 30% cheaper
//   Rootbound     Earth      +50% knockback resistance, +2 toughness       absorption every 30 s; Earth spells 25% stronger
//   Lumen         Order      mana comes back 50% faster                    every spell 15% cheaper; shrugs off wither and poison
//   Riftborn      Chaos      1 cast in 10 is free                          spells wither what they hit; Chaos spells 25% stronger
//   Sparkwright   Energy     10% faster                                    mana twice as fast on the move; Energy spells 30% cheaper
//   Blastward     Explosion  explosions do 60% less                        spell kills burst; Explosion spells 25% stronger
//   Rimefrost     Ice        never slowed or frozen                        melee attackers slowed; Ice spells 25% stronger
//   Deepwatch     Abyss      night vision, water breathing                 spells 20% stronger in the dark; Abyss spells 30% cheaper
//   Bastion       Stone      +4 armour                                     40% less damage while crouching; Stone spells 25% stronger
//   Mirewalker    Mud        melee attackers slowed                        regeneration on soil; Mud spells 30% cheaper
//   Zephyr        Air        no fall damage                                15% faster, Jump Boost; Air spells 25% stronger
//   Verdant       Life       regeneration when hurt                        kills heal; Life spells 30% stronger
//   Gilded        Metal      +2 toughness, +10% attack damage              25% of melee damage reflected; Metal spells 30% cheaper
public enum ElementalArmorSet implements StringRepresentable
{
    PYREBORN("pyreborn", () -> DustElements.FIRE, 0x7A1E16, 0xD9A441, 0.15F, 0F, 0.3F),
    TIDEWOVEN("tidewoven", () -> DustElements.WATER, 0x1F4E79, 0xC0C8D0, 0F, 0F, 0.3F),
    ROOTBOUND("rootbound", () -> DustElements.EARTH, 0x5B4A2E, 0x8A7F70, 0F, 0.25F, 0F),
    LUMEN("lumen", () -> DustElements.ORDER, 0xEFEFF5, 0xF2D16B, 0F, 0F, 0F),
    RIFTBORN("riftborn", () -> DustElements.CHAOS, 0x2A1838, 0x6B4D8F, 0F, 0.25F, 0F),
    SPARKWRIGHT("sparkwright", () -> DustElements.ENERGY, 0x8A2C3C, 0xD8D8E0, 0F, 0F, 0.3F),
    BLASTWARD("blastward", () -> DustElements.EXPLOSION, 0x4A1F14, 0x9A6A3A, 0F, 0.25F, 0F),
    RIMEFROST("rimefrost", () -> DustElements.ICE, 0xBFD9EE, 0xE8F4FF, 0F, 0.25F, 0F),
    DEEPWATCH("deepwatch", () -> DustElements.ABYSS, 0x14213D, 0x3A6E8F, 0F, 0F, 0.3F),
    BASTION("bastion", () -> DustElements.STONE, 0x6E6A64, 0xA8A49C, 0F, 0.25F, 0F),
    MIREWALKER("mirewalker", () -> DustElements.MUD, 0x4E3B26, 0x6E5A3A, 0F, 0F, 0.3F),
    ZEPHYR("zephyr", () -> DustElements.AIR, 0xE6D9F2, 0xF0F0F0, 0F, 0.25F, 0F),
    VERDANT("verdant", () -> DustElements.LIFE, 0x2F6B3A, 0xE0C870, 0F, 0.3F, 0F),
    GILDED("gilded", () -> DustElements.METAL, 0x3A3328, 0xF2C94C, 0F, 0F, 0.3F);

    // Saved by name ("pyreborn"...)
    public static final Codec<ElementalArmorSet> CODEC = StringRepresentable.fromEnum(ElementalArmorSet::values);

    public final String id;
    private final Supplier<DustElement> element;
    // The item icons' colours: cloth and trim
    public final int clothColor, trimColor;
    // Spell bonuses for the set's own element: power at 2 pieces, power at 4 pieces, cost reduction at 4 pieces
    public final float twoPower, fourPower, fourCost;

    ElementalArmorSet(String id, Supplier<DustElement> element, int clothColor, int trimColor, float twoPower, float fourPower, float fourCost)
    {
        this.id = id;
        this.element = element;
        this.clothColor = clothColor;
        this.trimColor = trimColor;
        this.twoPower = twoPower;
        this.fourPower = fourPower;
        this.fourCost = fourCost;
    }

    public DustElement element()
    {
        return element.get();
    }

    // The set worn by bosses of this element, if any
    public static ElementalArmorSet of(DustElement element)
    {
        for (ElementalArmorSet set : values()) if (set.element() == element) return set;
        return null;
    }

    @Override
    public String getSerializedName()
    {
        return id;
    }
}
