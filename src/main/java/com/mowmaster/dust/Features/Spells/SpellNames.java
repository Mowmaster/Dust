package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.api.SpellTarget;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

// A modifier's name as players see it, which can say what it does for the spell's element: Potency on a Fire spell
// landing on a creature is "Scorch Stacks". The lang file is searched from most to least specific, and the first key
// that exists is used:
//   spell_modifier.<ns>.<modifier>.<element>.<target>   e.g. spell_modifier.dust.potency.fire.entity
//   spell_modifier.<ns>.<modifier>.<element>            e.g. spell_modifier.dust.potency.fire
//   spell_modifier.<ns>.<modifier>                      e.g. spell_modifier.dust.potency
// <element> is the element's name for Dust's own elements (fire), and <namespace>.<name> for another mod's. <target> is
// self for a Self spell, entity for the rest (they land on creatures and blocks; the names are for the creature).
// Language.has works on both sides: NeoForge loads every mod's en_us lang on the server too, so the pylon's messages
// (built there) get the same names as the tooltips.
public final class SpellNames
{
    private SpellNames() {}

    public static Component modifier(Identifier modifier, @Nullable DustElement element, @Nullable Identifier shape)
    {
        String base = "spell_modifier." + modifier.getNamespace() + "." + modifier.getPath();
        if (element != null) {
            Identifier id = element.id();
            String byElement = base + "." + (id.getNamespace().equals(DustReferences.MODID) ? id.getPath() : id.getNamespace() + "." + id.getPath());
            if (shape != null) {
                SpellTarget target = shape.equals(SpellRegistries.SELF.getId()) ? SpellTarget.SELF : SpellTarget.ENTITY;
                String byTarget = byElement + "." + target.getSerializedName();
                if (Language.getInstance().has(byTarget)) return Component.translatable(byTarget);
            }
            if (Language.getInstance().has(byElement)) return Component.translatable(byElement);
        }
        return Component.translatable(base);
    }
}
