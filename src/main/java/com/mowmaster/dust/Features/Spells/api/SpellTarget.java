package com.mowmaster.dust.Features.Spells.api;

import net.minecraft.util.StringRepresentable;

// What an element's effect lands on: the caster (usually a buff), a block (or the area around it), or another entity
// (usually a debuff or an attack). The shape decides which: casting on yourself, hitting a block, hitting an entity.
public enum SpellTarget implements StringRepresentable
{
    SELF("self"), BLOCK("block"), ENTITY("entity");

    public static final com.mojang.serialization.Codec<SpellTarget> CODEC = StringRepresentable.fromEnum(SpellTarget::values);

    private final String name;

    SpellTarget(String name)
    {
        this.name = name;
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }
}
