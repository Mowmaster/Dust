package com.mowmaster.dust.Features.Spells.blocks;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.Crafting.ElementCrafterBlockEntity;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Magic.ChunkAura;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellConfig;
import com.mowmaster.dust.Features.Spells.api.SpellStats;
import com.mowmaster.dust.Features.Spells.api.SpellTarget;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// A rune's spell (RuneBlock):
//   spell, stats    what it casts when stepped on (the cast's, frozen as it was cast)
//   owner           who cast it: their own Entity rune doesn't go off for them (a Self rune does: it's a buff)
//   kind            ENTITY: the entity effect on whatever steps on it; SELF: the self effect
//   expiresAt       the game time it fades (SpellConfig.RUNE_SECONDS, plus Rune Duration); -1 with Permanent
// It goes off for each creature at most once every TRIGGER_COOLDOWN ticks. A permanent rune pays each trigger's dust
// itself: from Element Crafter jars within JAR_REACH, or else the chunk's aura; with neither, it stays quiet.
// The spell is synced so the client can tint it (Client/RuneTint).
public class RuneBlockEntity extends CBSyncedBlockEntity
{
    public static final int TRIGGER_COOLDOWN = 20;
    public static final int JAR_REACH = 4;

    private @Nullable Spell spell;
    private SpellStats stats = SpellStats.defaults();
    private @Nullable UUID owner;
    private SpellTarget kind = SpellTarget.ENTITY;
    private long expiresAt = -1;
    // Not saved: when each creature last set it off
    private final Map<UUID, Long> lastTriggered = new HashMap<>();

    public RuneBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.RUNE.get(), pos, state);
    }

    public @Nullable DustElement element()
    {
        return spell == null ? null : spell.element();
    }

    public boolean permanent()
    {
        return spell != null && spell.modifiers().contains(SpellRegistries.PERMANENT.getId());
    }

    void setup(SpellCast cast, SpellTarget kind)
    {
        this.spell = cast.spell();
        this.stats = cast.stats();
        this.owner = cast.caster() == null ? null : cast.caster().getUUID();
        this.kind = kind;
        if (permanent()) {
            expiresAt = -1;
        } else {
            int seconds = SpellConfig.RUNE_SECONDS + 20 * cast.spell().levelsOf(SpellRegistries.RUNE_DURATION.getId());
            expiresAt = cast.level().getGameTime() + 20L * seconds;
        }
        sync();
    }

    void trigger(ServerLevel level, LivingEntity entity)
    {
        if (spell == null) return;
        if (kind == SpellTarget.ENTITY && entity.getUUID().equals(owner)) return;
        long now = level.getGameTime();
        Long last = lastTriggered.get(entity.getUUID());
        if (last != null && now - last < TRIGGER_COOLDOWN) return;
        lastTriggered.put(entity.getUUID(), now);
        if (permanent() && !payDust(level)) {
            level.sendParticles(ParticleTypes.SMOKE, worldPosition.getX() + 0.5, worldPosition.getY() + 0.1, worldPosition.getZ() + 0.5, 3, 0.2, 0, 0.2, 0);
            return;
        }
        // Cast from the rune (as its caster, if they're about); a secondary cast, so it doesn't leave another rune
        LivingEntity caster = owner != null && level.getEntity(owner) instanceof LivingEntity living ? living : null;
        SpellCast cast = new SpellCast(level, caster, spell, stats, Vec3.atCenterOf(worldPosition), new Vec3(0, -1, 0)).asSecondary();
        if (kind == SpellTarget.SELF) cast.applySelf(entity);
        else cast.applyEntity(entity);
    }

    // A permanent rune's dust for one trigger: all of it from the jars nearby if they have it, else from the aura
    private boolean payDust(ServerLevel level)
    {
        Map<DustElement, Integer> cost = spell.dustCost();
        for (BlockPos at : BlockPos.betweenClosed(worldPosition.offset(-JAR_REACH, -JAR_REACH, -JAR_REACH), worldPosition.offset(JAR_REACH, JAR_REACH, JAR_REACH))) {
            if (!(level.getBlockEntity(at) instanceof ElementCrafterBlockEntity crafter)) continue;
            boolean enough = cost.entrySet().stream().allMatch(need -> crafter.dustOf(need.getKey()) >= need.getValue());
            if (!enough) continue;
            cost.forEach(crafter::takeDust);
            return true;
        }
        int total = cost.values().stream().mapToInt(Integer::intValue).sum();
        if (ChunkAura.of(level, worldPosition).total() < total) return false;
        ChunkAura.take(level, worldPosition, total);
        return true;
    }

    // Once a second: faded yet?
    public static void serverTick(Level level, BlockPos pos, BlockState state, RuneBlockEntity rune)
    {
        if (level.getGameTime() % 20 != 0 || rune.expiresAt < 0) return;
        if (level.getGameTime() >= rune.expiresAt) level.removeBlock(pos, false);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (spell != null) output.store("spell", Spell.CODEC, spell);
        output.store("stats", SpellStats.CODEC, stats);
        if (owner != null) output.store("owner", UUIDUtil.CODEC, owner);
        output.store("kind", SpellTarget.CODEC, kind);
        output.putLong("expires_at", expiresAt);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        spell = input.read("spell", Spell.CODEC).orElse(null);
        stats = input.read("stats", SpellStats.CODEC).orElse(SpellStats.defaults());
        owner = input.read("owner", UUIDUtil.CODEC).orElse(null);
        kind = input.read("kind", SpellTarget.CODEC).orElse(SpellTarget.ENTITY);
        expiresAt = input.getLongOr("expires_at", -1);
        // On the client: its tint comes from the spell's element, so draw it again
        if (level != null && level.isClientSide()) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
}
