package com.mowmaster.dust.Features.Spells.api;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.SpellKnowledge;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

// One cast of a spell, as its shape delivers it: where it's cast from, by whom, its element's effect and its stats.
// The shape calls applySelf / applyBlock / applyEntity; these spread it by the spell's size and run the element's
// effect on each thing. A player caster learns what each effect did (SpellKnowledge) and the shape it came by; a
// player it lands on learns the shape too.
public final class SpellCast
{
    private final ServerLevel level;
    private final @Nullable LivingEntity caster;
    private final Spell spell;
    private final SpellStats stats;
    private final Vec3 origin;
    private final Vec3 direction;

    // caster: null for a block casting it (then origin and direction say where from and which way)
    public SpellCast(ServerLevel level, @Nullable LivingEntity caster, Spell spell, SpellStats stats, Vec3 origin, Vec3 direction)
    {
        this.level = level;
        this.caster = caster;
        this.spell = spell;
        this.stats = stats;
        this.origin = origin;
        this.direction = direction;
    }

    // From a living caster: from their eyes, the way they're looking
    public static SpellCast by(LivingEntity caster, Spell spell, SpellStats stats)
    {
        return new SpellCast((ServerLevel) caster.level(), caster, spell, stats, caster.getEyePosition(), caster.getLookAngle());
    }

    public ServerLevel level() { return level; }
    public @Nullable LivingEntity caster() { return caster; }
    public Spell spell() { return spell; }
    public SpellStats stats() { return stats; }
    public Vec3 origin() { return origin; }
    public Vec3 direction() { return direction; }
    public DustElement element() { return spell.element(); }

    public ElementEffect effect()
    {
        ElementEffect effect = SpellRegistries.ELEMENT_EFFECT_REGISTRY.getValue(spell.element().id());
        return effect == null ? ElementEffect.NONE : effect;
    }

    // ---- delivering ----

    public void applySelf(LivingEntity target)
    {
        effect().onSelf(this, target);
        learn(SpellTarget.SELF);
        if (target instanceof ServerPlayer player && player != caster) SpellKnowledge.unlockShape(player, spell.shape());
    }

    // The block hit, and with size, every block within size of it (a cube)
    public void applyBlock(BlockPos pos, Direction face)
    {
        int size = stats.size();
        for (BlockPos at : BlockPos.betweenClosed(pos.offset(-size, -size, -size), pos.offset(size, size, size))) {
            effect().onBlock(this, at.immutable(), face);
        }
        learn(SpellTarget.BLOCK);
    }

    // The entity hit, and with size, every other living entity within size of it (never the caster)
    public void applyEntity(Entity target)
    {
        effect().onEntity(this, target);
        if (target instanceof ServerPlayer player && player != caster) SpellKnowledge.unlockShape(player, spell.shape());
        if (stats.size() > 0) {
            for (Entity nearby : around(target.position(), stats.size())) {
                if (nearby != target) effect().onEntity(this, nearby);
            }
        }
        learn(SpellTarget.ENTITY);
    }

    // Living entities within radius of a point, except the caster
    public List<LivingEntity> around(Vec3 centre, double radius)
    {
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(centre, centre).inflate(radius),
                entity -> entity != caster && entity.isAlive() && entity.position().distanceTo(centre) <= radius);
    }

    // A player caster notes what this effect did, and the shape it came by
    private void learn(SpellTarget target)
    {
        if (caster instanceof ServerPlayer player) {
            SpellKnowledge.noteEffect(player, spell.element(), target);
            SpellKnowledge.unlockShape(player, spell.shape());
        }
    }
}
