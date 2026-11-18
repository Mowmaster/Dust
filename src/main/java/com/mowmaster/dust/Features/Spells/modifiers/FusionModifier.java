package com.mowmaster.dust.Features.Spells.modifiers;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.api.ElementEffect;
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
// Each fusion is made by hand for one pair (register one per pair in SpellRegistries.MODIFIERS). It isn't drawn: a
// Fusion card links two spells (pylons/SpellLinks), and if their elements are this pair, both spells get the fusion
// (a Fused Crystal carries it on to a new spell too).
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

    // The fused parts' EffectTypes (DOT / INSTANT): null for a part that falls back to the element's own (and its type)
    private com.mowmaster.dust.Features.Spells.api.@Nullable EffectType selfType, blockType, entityType;

    public FusionModifier types(com.mowmaster.dust.Features.Spells.api.EffectType self, com.mowmaster.dust.Features.Spells.api.EffectType block,
                                com.mowmaster.dust.Features.Spells.api.EffectType entity)
    {
        this.selfType = self;
        this.blockType = block;
        this.entityType = entity;
        return this;
    }

    public FusionModifier self(BasicEffect.SelfPart part) { this.self = part; return this; }
    public FusionModifier block(BasicEffect.BlockPart part) { this.block = part; return this; }
    public FusionModifier entity(BasicEffect.EntityPart part) { this.entity = part; return this; }

    public DustElement first() { return first; }
    public DustElement second() { return second; }

    @Override
    public int weight() { return 4; }

    // Never drawn: a Fusion link between a spell of each element gives both spells the fusion (pylons/SpellLinks)
    @Override
    public boolean canRoll(ModifierContext context)
    {
        return false;
    }

    // Whether this fusion is of these two elements (either way round)
    public boolean joins(DustElement a, DustElement b)
    {
        return (first.equals(a) && second.equals(b)) || (first.equals(b) && second.equals(a));
    }

    @Override
    public Map<DustElement, Integer> cost(DustElement spellElement)
    {
        // The spell already pays 1 of its own element (SpellConfig.BASE_DUST_COST): 1 more of that, 2 of the other
        DustElement other = spellElement.equals(first) ? second : first;
        return Map.of(spellElement, 0, other, 1);
    }

    // The fused effect: this fusion's parts, and the spell's own element for any it doesn't have
    public ElementEffect fuse(ElementEffect base)
    {
        return new ElementEffect()
        {
            // A part this fusion has: its own type (INSTANT if it didn't say); otherwise the element's
            @Override
            public com.mowmaster.dust.Features.Spells.api.EffectType type(com.mowmaster.dust.Features.Spells.api.SpellTarget target)
            {
                var own = switch (target) {
                    case SELF -> self == null ? null : selfType == null ? com.mowmaster.dust.Features.Spells.api.EffectType.INSTANT : selfType;
                    case BLOCK -> block == null ? null : blockType == null ? com.mowmaster.dust.Features.Spells.api.EffectType.INSTANT : blockType;
                    case ENTITY -> entity == null ? null : entityType == null ? com.mowmaster.dust.Features.Spells.api.EffectType.INSTANT : entityType;
                };
                return own != null ? own : base.type(target);
            }

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
