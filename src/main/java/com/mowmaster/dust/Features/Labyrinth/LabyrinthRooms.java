package com.mowmaster.dust.Features.Labyrinth;

import com.mowmaster.dust.Features.Bosses.ArchonEntity;
import com.mowmaster.dust.Features.Bosses.Archons;
import com.mowmaster.dust.Features.Bosses.BossDamage;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Sanctums.ArcaneSpawnerBlock;
import com.mowmaster.dust.Features.Sanctums.ArcaneSpawnerBlockEntity;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

// Runs a labyrinth run's rooms (LabyrinthRoom), once a second from Labyrinth's upkeep:
//   combat     its doors seal while one of its Arcane Spawners' waves is on and players are in the room; cleared when
//              they've all been beaten
//   miniboss   a player stepping in wakes its Herald (sealing the doors while anyone's in there); when it falls the room is
//              cleared (heraldBeaten), and once every Herald has fallen the arena's seal lifts
//   puzzle     the first player in is told what to do; a spell striking one of its runes lights it if the spell holds
//              the rune's element (onSpellBlock); all lit, a chest appears in the middle
//   challenge  falling into the pit puts you back on the ledge, a little hurt
//   sanctuary  whoever's in the room is mended (Regeneration)
// A room's doors stay open while nobody's in it, so players coming back (after dying) can always get in.
public final class LabyrinthRooms
{
    private LabyrinthRooms() {}

    static void tick(ServerLevel level, LabyrinthData data, LabyrinthInstance run, List<ServerPlayer> inside)
    {
        for (LabyrinthRoom room : run.rooms) {
            List<ServerPlayer> here = new ArrayList<>();
            for (ServerPlayer player : inside) if (room.interior.isInside(player.blockPosition())) here.add(player);
            switch (room.type) {
                case COMBAT -> {
                    // Nobody in it: its doors open (and its spawner, maybe in an unloaded chunk, is left alone)
                    if (here.isEmpty()) {
                        seal(level, run, room, false);
                        break;
                    }
                    // (a room saved before rooms kept their spawners had its one in the middle)
                    boolean fighting = false, beaten = true;
                    for (BlockPos pos : room.spawners.isEmpty() ? List.of(room.centre) : room.spawners) {
                        ArcaneSpawnerBlock.Phase phase = level.getBlockEntity(pos) instanceof ArcaneSpawnerBlockEntity spawner ? spawner.phase() : ArcaneSpawnerBlock.Phase.COOLDOWN;
                        if (phase == ArcaneSpawnerBlock.Phase.ACTIVE) fighting = true;
                        if (phase != ArcaneSpawnerBlock.Phase.COOLDOWN) beaten = false;
                    }
                    if (beaten && room.state != LabyrinthRoom.State.CLEARED) room.state = LabyrinthRoom.State.CLEARED;
                    seal(level, run, room, fighting);
                }
                case MINIBOSS -> {
                    if (room.state == LabyrinthRoom.State.DORMANT && !here.isEmpty()) wakeHerald(level, run, room, here);
                    if (room.state == LabyrinthRoom.State.ACTIVE && !here.isEmpty()) {
                        // Its Herald went missing without falling (taken away by a command): another wakes
                        Entity herald = room.mob == null ? null : level.getEntity(room.mob);
                        if (herald == null || !herald.isAlive()) wakeHerald(level, run, room, here);
                    }
                    seal(level, run, room, room.state == LabyrinthRoom.State.ACTIVE && !here.isEmpty());
                }
                case PUZZLE -> {
                    if (room.state == LabyrinthRoom.State.DORMANT && !here.isEmpty()) {
                        room.state = LabyrinthRoom.State.ACTIVE;
                        for (ServerPlayer player : here) player.sendOverlayMessage(Component.translatable("dust.labyrinth.puzzle"));
                    }
                }
                case CHALLENGE -> {
                    for (ServerPlayer player : here) {
                        if (room.ledge != null && player.getY() < room.centre.getY() - 1.5) {
                            player.teleportTo(room.ledge.getX() + 0.5, room.ledge.getY(), room.ledge.getZ() + 0.5);
                            player.hurtServer(level, level.damageSources().fall(), 3.0F);
                            player.sendOverlayMessage(Component.translatable("dust.labyrinth.fell"));
                        }
                    }
                }
                case SANCTUARY -> {
                    for (ServerPlayer player : here) player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1, true, true));
                }
                case TREASURE -> {
                }
            }
        }
        data.setDirty();
    }

    // Seals or opens a room's doors (only what's changed)
    private static void seal(ServerLevel level, LabyrinthInstance run, LabyrinthRoom room, boolean sealed)
    {
        if (room.sealed == sealed) return;
        room.sealed = sealed;
        for (BoundingBox door : room.doors) setSeal(level, run, door, sealed);
        level.playSound(null, room.centre, sealed ? SoundEvents.BEACON_ACTIVATE : SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 0.8F, sealed ? 1.4F : 1.0F);
    }

    // Fills a doorway with Labyrinth Seals, or empties it of them
    static void setSeal(ServerLevel level, LabyrinthInstance run, BoundingBox door, boolean sealed)
    {
        BlockState seal = LabyrinthBlocks.SEAL.get().defaultBlockState().setValue(LabyrinthSealBlock.SET, run.set);
        for (BlockPos pos : BlockPos.betweenClosed(door.minX(), door.minY(), door.minZ(), door.maxX(), door.maxY(), door.maxZ())) {
            BlockState there = level.getBlockState(pos);
            if (sealed && there.canBeReplaced()) level.setBlock(pos, seal, 2);
            else if (!sealed && there.getBlock() instanceof LabyrinthSealBlock) level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }

    private static void wakeHerald(ServerLevel level, LabyrinthInstance run, LabyrinthRoom room, List<ServerPlayer> here)
    {
        ArchonEntity herald = Archons.TYPES.get(run.set).get().create(level, EntitySpawnReason.EVENT);
        if (herald == null) return;
        herald.makeHerald();
        herald.snapTo(Vec3.atBottomCenterOf(room.centre), level.getRandom().nextFloat() * 360.0F, 0.0F);
        herald.setBossLevel(run.bossLevel);
        herald.finalizeSpawn(level, level.getCurrentDifficultyAt(room.centre), EntitySpawnReason.EVENT, null);
        herald.addTag(Labyrinth.runTag(run));
        if (!level.addFreshEntity(herald)) return;
        room.mob = herald.getUUID();
        room.state = LabyrinthRoom.State.ACTIVE;
        level.playSound(null, room.centre, SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.HOSTILE, 1.0F, 0.8F);
        for (ServerPlayer player : here) player.sendSystemMessage(Component.translatable("dust.labyrinth.herald", herald.getDisplayName()));
    }

    // A Herald has fallen: its room is cleared, and if it was the last, the arena opens
    static void heraldBeaten(ServerLevel level, LabyrinthData data, LabyrinthInstance run, ArchonEntity herald)
    {
        LabyrinthRoom room = run.roomAt(herald.blockPosition());
        if (room == null || room.type != LabyrinthRoom.Type.MINIBOSS) {
            for (LabyrinthRoom candidate : run.rooms) if (candidate.mob != null && candidate.mob.equals(herald.getUUID())) room = candidate;
        }
        if (room != null) {
            room.state = LabyrinthRoom.State.CLEARED;
            seal(level, run, room, false);
        }
        boolean left = false;
        for (LabyrinthRoom other : run.rooms) if (other.type == LabyrinthRoom.Type.MINIBOSS && other.state != LabyrinthRoom.State.CLEARED) left = true;
        if (!left && !run.arenaOpen) {
            run.arenaOpen = true;
            if (run.arenaDoor != null) setSeal(level, run, run.arenaDoor, false);
            for (ServerPlayer player : Labyrinth.playersIn(level, run)) player.sendSystemMessage(Component.translatable("dust.labyrinth.arena_open"));
        }
        data.setDirty();
    }

    // A spell has struck a block (SpellCast.applyBlock): in a labyrinth, a rune of an element the spell holds lights up
    public static void onSpellBlock(SpellCast cast, BlockPos pos)
    {
        ServerLevel level = cast.level();
        if (!Labyrinth.is(level)) return;
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof LabyrinthRuneBlock) || state.getValue(LabyrinthRuneBlock.LIT)) return;
        DustElement rune = state.getValue(LabyrinthRuneBlock.SET).element();
        if (!cast.element().equals(rune) && BossDamage.share(cast.element(), rune) <= 0.0F) {
            level.playSound(null, pos, SoundEvents.SHIELD_BLOCK.value(), SoundSource.BLOCKS, 0.6F, 0.6F);
            return;
        }
        level.setBlock(pos, state.setValue(LabyrinthRuneBlock.LIT, true), 3);
        Vec3 at = Vec3.atCenterOf(pos);
        level.sendParticles(new DustParticleOptions(rune.color(), 1.5F), at.x, at.y, at.z, 16, 0.4, 0.4, 0.4, 0);
        level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.2F, 1.0F);
        LabyrinthData data = LabyrinthData.get(level.getServer());
        LabyrinthInstance run = data.at(pos);
        if (run == null) return;
        for (LabyrinthRoom room : run.rooms) {
            if (!room.runes.contains(pos) || room.state == LabyrinthRoom.State.CLEARED) continue;
            boolean all = true;
            for (BlockPos other : room.runes) {
                BlockState there = level.getBlockState(other);
                if (!(there.getBlock() instanceof LabyrinthRuneBlock) || !there.getValue(LabyrinthRuneBlock.LIT)) all = false;
            }
            if (!all) continue;
            room.state = LabyrinthRoom.State.CLEARED;
            LabyrinthLoot.chest(level, room.centre, run, 1);
            Vec3 middle = Vec3.atCenterOf(room.centre);
            level.sendParticles(new DustParticleOptions(run.set.element().color(), 2.0F), middle.x, middle.y, middle.z, 40, 0.8, 0.8, 0.8, 0);
            level.playSound(null, room.centre, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            for (ServerPlayer player : Labyrinth.playersIn(level, run)) {
                if (room.interior.isInside(player.blockPosition())) player.sendOverlayMessage(Component.translatable("dust.labyrinth.solved"));
            }
            data.setDirty();
        }
    }
}
