package com.mowmaster.dust.Features.Spells.modifiers;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.api.ElementEffect;
import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import com.mowmaster.dust.Features.Spells.api.ModifierContext;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import com.mowmaster.dust.Features.Spells.effects.BasicEffect;
import com.mowmaster.dust.Features.Spells.pylons.PylonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

import java.util.Map;

// A fusion: two elements joined into what is really a different spell, with its own self, block and entity effects.
// Each fusion is made by hand for one pair (register one per pair in SpellRegistries.MODIFIERS). It's rolled as a
// modifier on a spell of either element, once:
//   the placing player has taken spells of both elements to PylonConfig.FUSION_LEVEL (SpellMastery)
//   there's been a spell break earlier in the line (not necessarily just before)
//   the line has no fusion yet
// Casting a fused spell runs the fusion's parts in place of the element's (SpellCast.effect); a part the fusion doesn't
// define falls back to the spell's own element. It costs dust of both elements.
public class FusionModifier implements SpellModifier
{
    private final DustElement first;
    private final DustElement second;
    private BasicEffect.@Nullable SelfPart self;
    private BasicEffect.@Nullable BlockPart block;
    private BasicEffect.@Nullable EntityPart entity;

    public FusionModifier(DustElement first, DustElement second)
    {
        this.first = first;
        this.second = second;
    }

    public FusionModifier self(BasicEffect.SelfPart part) { this.self = part; return this; }
    public FusionModifier block(BasicEffect.BlockPart part) { this.block = part; return this; }
    public FusionModifier entity(BasicEffect.EntityPart part) { this.entity = part; return this; }

    public DustElement first() { return first; }
    public DustElement second() { return second; }

    @Override
    public ModifierCategory category() { return ModifierCategory.CHANGE; }

    @Override
    public int weight() { return 4; }

    @Override
    public boolean canRoll(ModifierContext context)
    {
        boolean ours = context.element().equals(first) || context.element().equals(second);
        return ours && context.breaks() >= 1 && context.countOf(FusionModifier.class) == 0
                && context.masteryOf(first) >= PylonConfig.FUSION_LEVEL && context.masteryOf(second) >= PylonConfig.FUSION_LEVEL;
    }

    @Override
    public Map<DustElement, Integer> cost(DustElement spellElement)
    {
        return Map.of(first, 2, second, 2);
    }

    // The fused effect: this fusion's parts, and the spell's own element for any it doesn't have
    public ElementEffect fuse(ElementEffect base)
    {
        return new ElementEffect()
        {
            @Override
            public void onSelf(SpellCast cast, LivingEntity target)
            {
                if (self != null) self.apply(cast, target);
                else base.onSelf(cast, target);
            }

            @Override
            public void onBlock(SpellCast cast, BlockPos pos, Direction face)
            {
                if (block != null) block.apply(cast, pos, face);
                else base.onBlock(cast, pos, face);
            }

            @Override
            public void onEntity(SpellCast cast, Entity target)
            {
                if (entity != null) entity.apply(cast, target);
                else base.onEntity(cast, target);
            }
        };
    }
}
