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
// The shape calls applySelf / applyBlock / applyEntity; these run the element's effect on each thing, through the
// spell's shape modifiers:
//   Rune        instead of landing, a rune is left on the ground there (RuneBlock), which puts the effect on whatever
//               steps on it later
//   Wall        it spreads in a line across the spell's path
//   AOE         every block within the radius, and the nearest creatures in it (as many as the target count)
//   Chaining    from a creature it jumps on to the nearest ones not yet hit
//   Lingering   a pool where it lands keeps putting the effect on what's in it
// A secondary cast (a rune going off, a chain's jump...) just runs the effect, so nothing spreads twice.
// A player caster learns what each effect did (SpellKnowledge) and the shape it came by; a player it lands on learns
// the shape too.
public final class SpellCast
{
    private final ServerLevel level;
    private final @Nullable LivingEntity caster;
    private final Spell spell;
    private final SpellStats stats;
    private final Vec3 origin;
    private final Vec3 direction;
    // Set by a modifier that delivered the spell itself (SpellModifier.beforeDelivery), so others don't again
    private boolean delivered;
    // A cast that only runs the effect (no rune, wall, AOE, chain or pool): see the top
    private boolean secondary;
    // Things done once per cast, whatever's hit (firstTime)
    private final java.util.Set<String> done = new java.util.HashSet<>();
    // The Spell Caster that cast it (a block, so caster is null): effects use the containers around it as a player's
    // hands and inventory (caster/CasterStorage). Its projectile and echoes carry it on.
    private @Nullable BlockPos casterBlock;

    // caster: null for a block casting it (then origin and direction say where from and which way)
    public SpellCast(ServerLevel level, @Nullable LivingEntity caster, Spell spell, SpellStats stats, Vec3 origin, Vec3 direction)
    {
        this.level = level;
        this.caster = caster;
        this.spell = spell;
        this.stats = stats;
        this.origin = origin;
        this.direction = direction;
        // Cast in dusty air: the spark sets it off (DustClouds)
        com.mowmaster.dust.Features.DustStorage.DustClouds.spark(level, origin, caster);
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
    public boolean delivered() { return delivered; }
    public void markDelivered() { delivered = true; }
    public boolean isSecondary() { return secondary; }
    public @Nullable BlockPos casterBlock() { return casterBlock; }

    // Marks it as cast by the Spell Caster at pos (null: not by one); returns the same cast
    public SpellCast fromBlock(@Nullable BlockPos pos)
    {
        casterBlock = pos;
        return this;
    }

    // The same cast, only running the effect (for a rune, a chain's jump, a lingering pool)
    public SpellCast asSecondary()
    {
        SpellCast copy = new SpellCast(level, caster, spell, stats, origin, direction);
        copy.secondary = true;
        copy.casterBlock = casterBlock;
        return copy;
    }

    // The same cast with other numbers (for one creature: statsAgainst)
    private SpellCast withStats(SpellStats other)
    {
        SpellCast copy = new SpellCast(level, caster, spell, other, origin, direction);
        copy.secondary = secondary;
        copy.casterBlock = casterBlock;
        return copy;
    }

    // Each modifier's onImpact, where it landed (not for secondary casts)
    private void impact(Vec3 at, @Nullable Entity target)
    {
        for (SpellModifier modifier : spell.modifierList()) modifier.onImpact(this, at, target);
    }

    // True the first time this is asked with a key during this cast, false after: for something an effect should do
    // once however many things the spell lands on (one explosion sound for an AOE of blocks)
    public boolean firstTime(String key)
    {
        return done.add(key);
    }

    // Whether the spell has this modifier
    public boolean has(net.minecraft.resources.Identifier modifier)
    {
        return spell.modifiers().contains(modifier);
    }

    // The element's effect, or with a fusion in the spell, the fusion's (falling back to the element's for parts the
    // fusion doesn't have)
    public ElementEffect effect()
    {
        ElementEffect effect = SpellRegistries.ELEMENT_EFFECT_REGISTRY.getValue(spell.element().id());
        ElementEffect base = effect == null ? ElementEffect.NONE : effect;
        ElementEffect chosen = base;
        for (SpellModifier modifier : spell.modifierList()) {
            if (modifier instanceof com.mowmaster.dust.Features.Spells.modifiers.FusionModifier fusion) {
                chosen = fusion.fuse(base);
                break;
            }
        }
        // Prevent Block Action / Prevent Attack Action: that part does nothing
        boolean noBlocks = has(SpellRegistries.PREVENT_BLOCK.getId()), noCreatures = has(SpellRegistries.PREVENT_ATTACK.getId());
        if (!noBlocks && !noCreatures) return chosen;
        ElementEffect inner = chosen;
        return new ElementEffect()
        {
            @Override
            public EffectType type(SpellTarget target) { return inner.type(target); }

            @Override
            public void onSelf(SpellCast cast, LivingEntity target) { inner.onSelf(cast, target); }

            @Override
            public void onBlock(SpellCast cast, BlockPos pos, Direction face)
            {
                if (!noBlocks) inner.onBlock(cast, pos, face);
            }

            @Override
            public void onEntity(SpellCast cast, Entity target)
            {
                if (!noCreatures) inner.onEntity(cast, target);
            }

            @Override
            public boolean damages() { return !noCreatures && inner.damages(); }
        };
    }

    // ---- delivering ----

    public void applySelf(LivingEntity target)
    {
        // Rune: a rune at the caster's feet instead, giving the self effect to whoever steps on it
        if (!secondary && has(SpellRegistries.RUNE.getId())) {
            com.mowmaster.dust.Features.Spells.blocks.RuneBlock.place(this, target.blockPosition(), SpellTarget.SELF);
            learn(SpellTarget.SELF);
            return;
        }
        effect().onSelf(this, target);
        learn(SpellTarget.SELF);
        if (target instanceof ServerPlayer player && player != caster) SpellKnowledge.unlockShape(player, spell.shape());
    }

    // The block hit (on face), and with size, every block within size of it (a cube); then the shape modifiers
    public void applyBlock(BlockPos pos, Direction face)
    {
        Vec3 impact = Vec3.atCenterOf(pos.relative(face));
        // Landing in dusty air sets it off (DustClouds)
        com.mowmaster.dust.Features.DustStorage.DustClouds.spark(level, impact, caster);
        if (!secondary && has(SpellRegistries.RUNE.getId())) {
            com.mowmaster.dust.Features.Spells.blocks.RuneBlock.place(this, pos.relative(face), SpellTarget.ENTITY);
            learn(SpellTarget.BLOCK);
            return;
        }
        if (!secondary && has(SpellRegistries.WALL.getId())) {
            wall(impact, pos);
            learn(SpellTarget.BLOCK);
            return;
        }
        blockEffect(pos, face);
        if (!secondary) {
            if (has(SpellRegistries.AOE.getId())) {
                double radius = aoeRadius();
                int r = (int) Math.ceil(radius);
                for (BlockPos at : BlockPos.betweenClosed(pos.offset(-r, -r, -r), pos.offset(r, r, r))) {
                    if (!at.equals(pos) && Vec3.atCenterOf(at).distanceTo(Vec3.atCenterOf(pos)) <= radius) effect().onBlock(this, at.immutable(), face);
                }
                for (LivingEntity target : nearest(impact, radius, targetCount(), null)) entityEffect(target);
            }
            linger(impact);
            impact(impact, null);
        }
        learn(SpellTarget.BLOCK);
    }

    // The entity hit, and with size, every other living entity within size of it (never the caster); then the shape
    // modifiers
    public void applyEntity(Entity target)
    {
        com.mowmaster.dust.Features.DustStorage.DustClouds.spark(level, target.position(), caster);
        if (!secondary && has(SpellRegistries.RUNE.getId())) {
            com.mowmaster.dust.Features.Spells.blocks.RuneBlock.place(this, target.blockPosition(), SpellTarget.ENTITY);
            learn(SpellTarget.ENTITY);
            return;
        }
        if (!secondary && has(SpellRegistries.WALL.getId())) {
            wall(target.position().add(0, 0.1, 0), null);
            learn(SpellTarget.ENTITY);
            return;
        }
        entityEffect(target);
        if (!secondary) {
            // AOE: the nearest others around it, up to the target count with it
            if (has(SpellRegistries.AOE.getId())) {
                for (LivingEntity other : nearest(target.position(), aoeRadius(), targetCount() - 1, target)) entityEffect(other);
            }
            if (has(SpellRegistries.CHAINING.getId())) chain(target);
            linger(target.position());
            impact(target.position(), target);
        }
        learn(SpellTarget.ENTITY);
    }

    // The effect on one block, spread by the old size stat (a cube)
    private void blockEffect(BlockPos pos, Direction face)
    {
        int size = stats.size();
        for (BlockPos at : BlockPos.betweenClosed(pos.offset(-size, -size, -size), pos.offset(size, size, size))) {
            effect().onBlock(this, at.immutable(), face);
        }
    }

    // The effect on one entity, spread by the old size stat. A modifier can change the numbers for this one creature
    // (Pride); Greed's spell no longer spares friends and allies (Social.spellBypass, just while it lands).
    private void entityEffect(Entity target)
    {
        SpellStats own = stats;
        for (SpellModifier modifier : spell.modifierList()) own = modifier.statsAgainst(this, target, own);
        SpellCast cast = own == stats ? this : withStats(own);
        boolean greedy = com.mowmaster.dust.Features.Spells.modifiers.Sins.greedy(spell);
        if (greedy) com.mowmaster.dust.Features.Spells.radiance.Social.spellBypass = true;
        try {
            effect().onEntity(cast, target);
        } finally {
            if (greedy) com.mowmaster.dust.Features.Spells.radiance.Social.spellBypass = false;
        }
        if (target instanceof ServerPlayer player && player != caster) SpellKnowledge.unlockShape(player, spell.shape());
        if (stats.size() > 0) {
            for (Entity nearby : around(target.position(), stats.size())) {
                if (nearby != target) effect().onEntity(this, nearby);
            }
        }
    }

    // ---- the shape modifiers ----

    // AOE's radius: its own, plus Size
    public double aoeRadius()
    {
        double radius = SpellConfig.AOE_RADIUS + SpellConfig.SIZE_PER_LEVEL * spell.levelsOf(SpellRegistries.SIZE_MODIFIER.getId());
        // Gluttony: twice the size
        return com.mowmaster.dust.Features.Spells.modifiers.Sins.gluttonous(spell) ? radius * 2 : radius;
    }

    // How many creatures AOE reaches: 1, plus Target Count
    private int targetCount()
    {
        return 1 + spell.levelsOf(SpellRegistries.TARGET_COUNT.getId());
    }

    // The nearest living entities to a point within radius (not the caster, not except), at most count of them
    private List<LivingEntity> nearest(Vec3 centre, double radius, int count, @Nullable Entity except)
    {
        if (count <= 0) return List.of();
        return around(centre, radius).stream().filter(entity -> entity != except)
                .sorted(java.util.Comparator.comparingDouble(entity -> entity.distanceToSqr(centre))).limit(count).toList();
    }

    // Chaining: from the creature hit, to the nearest not yet hit within the search distance, and on from there, as many
    // jumps as the chain count; a line of dust shows each jump
    private void chain(Entity first)
    {
        int jumps = 1 + spell.levelsOf(SpellRegistries.CHAIN_COUNT.getId());
        double search = SpellConfig.CHAIN_SEARCH + spell.levelsOf(SpellRegistries.SEARCH_DISTANCE.getId());
        java.util.Set<Entity> hit = new java.util.HashSet<>();
        hit.add(first);
        Entity from = first;
        for (int i = 0; i < jumps; i++) {
            Vec3 at = from.position().add(0, from.getBbHeight() / 2, 0);
            LivingEntity next = around(at, search).stream().filter(entity -> !hit.contains(entity))
                    .min(java.util.Comparator.comparingDouble(entity -> entity.distanceToSqr(at))).orElse(null);
            if (next == null) return;
            line(at, next.position().add(0, next.getBbHeight() / 2, 0));
            hit.add(next);
            entityEffect(next);
            from = next;
        }
    }

    // A line of dust in the element's colour between two points
    private void line(Vec3 from, Vec3 to)
    {
        var dust = new net.minecraft.core.particles.DustParticleOptions(element().color(), 0.8F);
        double length = from.distanceTo(to);
        Vec3 step = to.subtract(from).normalize().scale(0.3);
        Vec3 at = from;
        for (double along = 0; along < length; along += 0.3, at = at.add(step)) {
            level.sendParticles(dust, at.x, at.y, at.z, 1, 0, 0, 0, 0);
        }
    }

    // Wall: across the path (the cast's direction, flattened: a wall stands upright) at centre, WALL_HALF either side
    // plus Wall Width, as deep as Wall Depth. Every creature standing in it gets the effect; hitting a block, the blocks
    // in the line do too (the row the hit block is in).
    private void wall(Vec3 centre, @Nullable BlockPos hitBlock)
    {
        Vec3 along = new Vec3(direction.x, 0, direction.z);
        if (along.lengthSqr() < 1.0E-4) along = caster == null ? new Vec3(0, 0, 1) : new Vec3(caster.getLookAngle().x, 0, caster.getLookAngle().z);
        along = along.normalize();
        Vec3 across = new Vec3(-along.z, 0, along.x);
        double half = SpellConfig.WALL_HALF + SpellConfig.WALL_PER_LEVEL * spell.levelsOf(SpellRegistries.WALL_WIDTH.getId());
        double depth = SpellConfig.WALL_PER_LEVEL * spell.levelsOf(SpellRegistries.WALL_DEPTH.getId());
        if (com.mowmaster.dust.Features.Spells.modifiers.Sins.gluttonous(spell)) {
            half *= 2;
            depth *= 2;
        }
        var dust = new net.minecraft.core.particles.DustParticleOptions(element().color(), 1.0F);
        java.util.Set<BlockPos> blocks = new java.util.LinkedHashSet<>();
        for (double w = -half; w <= half + 1.0E-6; w += 0.5) {
            for (double d = 0; d <= depth + 1.0E-6; d += 0.5) {
                Vec3 point = centre.add(across.scale(w)).add(along.scale(d));
                level.sendParticles(dust, point.x, point.y + 0.5, point.z, 2, 0.1, 0.4, 0.1, 0);
                // The row the hit block is in: the same offsets from its centre
                if (hitBlock != null) blocks.add(BlockPos.containing(Vec3.atCenterOf(hitBlock).add(across.scale(w)).add(along.scale(d))));
            }
        }
        for (BlockPos at : blocks) {
            if (!level.getBlockState(at).isAir()) blockEffect(at, Direction.UP);
        }
        // Creatures in it: within the wall's box (a little over a block high above and below the line)
        AABB box = new AABB(centre, centre).inflate(half + depth + 1, 2, half + depth + 1);
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, box, entity -> entity != caster && entity.isAlive())) {
            Vec3 rel = target.position().subtract(centre);
            double sideways = rel.dot(across), forward = rel.dot(along);
            if (Math.abs(sideways) <= half + 0.5 && forward >= -0.75 && forward <= depth + 0.75 && Math.abs(rel.y) <= 2) entityEffect(target);
        }
    }

    // Lingering: a pool at the point, putting the effect on every creature within AOE's radius each LINGER_INTERVAL
    // ticks for the spell's duration (SpellScheduler runs the later ones)
    private void linger(Vec3 centre)
    {
        if (!has(SpellRegistries.LINGERING.getId())) return;
        double radius = aoeRadius();
        SpellCast pool = asSecondary();
        var dust = new net.minecraft.core.particles.DustParticleOptions(element().color(), 1.2F);
        for (int at = SpellConfig.LINGER_INTERVAL; at <= stats.duration(); at += SpellConfig.LINGER_INTERVAL) {
            com.mowmaster.dust.Features.Spells.SpellScheduler.later(level.getServer(), at, () -> {
                level.sendParticles(dust, centre.x, centre.y + 0.1, centre.z, 12, radius / 2, 0.05, radius / 2, 0);
                for (LivingEntity target : pool.around(centre, radius)) pool.effect().onEntity(pool, target);
            });
        }
    }

    // Living entities within radius of a point, except the caster (unless the spell is Greedy: then them too)
    public List<LivingEntity> around(Vec3 centre, double radius)
    {
        boolean greedy = com.mowmaster.dust.Features.Spells.modifiers.Sins.greedy(spell);
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(centre, centre).inflate(radius),
                entity -> (greedy || entity != caster) && entity.isAlive() && entity.position().distanceTo(centre) <= radius);
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
