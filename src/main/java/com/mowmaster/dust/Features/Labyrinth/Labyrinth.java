package com.mowmaster.dust.Features.Labyrinth;

import com.mowmaster.dust.DustConfig;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.Bosses.ArchonEntity;
import com.mowmaster.dust.Features.Bosses.Archons;
import com.mowmaster.dust.Features.Sanctums.SanctumKeystoneBlockEntity;
import com.mowmaster.dust.Features.Spells.blocks.PhantomBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerRespawnPositionEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// The labyrinth: one dimension (dust:labyrinth, data/dust/dimension/labyrinth.json: void, an overworld sky) where each
// run is built in its own region (a slot: SPACING blocks apart on a GRID x GRID grid; LabyrinthData), like a Vault
// Hunters vault but without needing a new dimension each time. A sanctum's portal takes its players in (enter): the
// first one starts a run (LabyrinthBuilder builds it), the rest of the party joins it while it lasts. No timers.
//   Leaving       through an exit portal: back in front of the sanctum portal (leave). There's one by the way in, and
//                 one opens in the arena when the Archon falls.
//   Dying         you wake in front of the sanctum portal, not in the labyrinth (onRespawnPosition), keeping
//                 everything you had, experience too (keep / onClone; config labyrinth.keep_inventory)
//   The maze      rooms of every kind between the hall and the arena (LabyrinthLayout, LabyrinthRooms): fights,
//                 puzzles, a challenge, treasure, a sanctuary, and Heralds (lesser Archons) who must all fall before the
//                 arena opens
//   The Archon    wakes when a player steps into the arena (wake), at the run's level; beaten, it opens the way out and
//                 sets the sanctum's spawners waiting again (onDeath)
//   Blocks        walls, floors and ceilings can't be broken; anything else fades for PHASE_TICKS when broken (a
//                 phased Phantom Block, like the Spirit spell's) and comes back, dropping nothing; blocks players place
//                 can be taken back. Explosions and mobs break nothing; no portals can be lit; no spawn can be set.
//   Upkeep        each second (upkeep): a run with nobody in it for CLOSE_AFTER ticks closes and its leftovers go;
//                 its slot is cleared and rebuilt by the next run that takes it
@EventBusSubscriber(modid = DustReferences.MODID)
public final class Labyrinth
{
    public static final ResourceKey<Level> LEVEL = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(DustReferences.MODID, "labyrinth"));
    public static final int SPACING = 2048;
    public static final int GRID = 64;
    public static final int BASE_Y = 80;
    public static final int CLOSE_AFTER = 20 * 60;
    public static final int PHASE_TICKS = 20 * 30;
    // Mobs a run puts in its region carry this and its run number, so ones from an earlier run in the same slot (still
    // saved in chunks that hadn't loaded when it closed) don't come back with the next (onEntityJoin)
    public static final String RUN_TAG = "dust_labyrinth_run_";

    public static final Supplier<AttachmentType<ReturnPoint>> RETURN = DustAttachmentTypeRegistry.ATTACHMENT_TYPES.register("labyrinth_return",
            () -> AttachmentType.builder(() -> ReturnPoint.NONE).serialize(ReturnPoint.CODEC.fieldOf("labyrinth_return")).build());
    public static final Supplier<AttachmentType<KeptInventory>> KEPT = DustAttachmentTypeRegistry.ATTACHMENT_TYPES.register("labyrinth_kept",
            () -> AttachmentType.builder(() -> KeptInventory.NONE).serialize(KeptInventory.CODEC.fieldOf("labyrinth_kept")).build());

    private Labyrinth() {}

    // Loads the class (and so registers the attachments above, and the labyrinth's blocks) from the mod constructor (Dust)
    public static void init()
    {
        LabyrinthBlocks.init();
    }

    public static String runTag(LabyrinthInstance run)
    {
        return RUN_TAG + run.run;
    }

    // Marks a mob spawned in a labyrinth (by a room's Arcane Spawner) as its run's
    public static void claim(ServerLevel level, Entity entity)
    {
        if (!is(level)) return;
        LabyrinthInstance run = LabyrinthData.get(level.getServer()).at(entity.blockPosition());
        if (run != null) entity.addTag(runTag(run));
    }

    public static boolean is(Level level)
    {
        return level.dimension() == LEVEL;
    }

    // The middle of a slot's region, at the labyrinth's floor height
    public static BlockPos origin(int slot)
    {
        return new BlockPos((slot % GRID) * SPACING, BASE_Y, (slot / GRID) * SPACING);
    }

    // The slot whose region holds pos (-1: none)
    public static int slotAt(BlockPos pos)
    {
        int gx = Math.floorDiv(pos.getX() + SPACING / 2, SPACING), gz = Math.floorDiv(pos.getZ() + SPACING / 2, SPACING);
        if (gx < 0 || gx >= GRID || gz < 0 || gz >= GRID) return -1;
        return gz * GRID + gx;
    }

    // ---- going in and out ----

    // A player steps into a sanctum's open portal: into the run it leads to (a new one if there's none going on)
    public static void enter(ServerPlayer player, SanctumKeystoneBlockEntity keystone)
    {
        MinecraftServer server = player.level().getServer();
        ServerLevel labyrinth = server.getLevel(LEVEL);
        if (labyrinth == null || keystone.getLevel() == null) {
            player.sendOverlayMessage(Component.translatable("dust.labyrinth.missing"));
            return;
        }
        LabyrinthData data = LabyrinthData.get(server);
        LabyrinthInstance run = data.get(keystone.slot());
        boolean going = run != null && run.run == keystone.run()
                && (run.state == LabyrinthInstance.State.OPEN || run.state == LabyrinthInstance.State.BOSS);
        if (!going) {
            LabyrinthData.NewRun created = data.create(keystone.set(), ArchonEntity.levelFor(player), player.getRandom().nextLong());
            run = created.run();
            run.keystone = GlobalPos.of(keystone.getLevel().dimension(), keystone.getBlockPos());
            LabyrinthBuilder.build(labyrinth, run, created.previous());
            run.lastOccupied = labyrinth.getGameTime();
            data.setDirty();
            keystone.setRun(run.slot, run.run);
        }
        player.setData(RETURN.get(), new ReturnPoint(player.level().dimension(), keystone.returnPos(), keystone.returnYaw(), run.run));
        player.teleport(new TeleportTransition(labyrinth, Vec3.atBottomCenterOf(run.spawn), Vec3.ZERO, run.spawnYaw, 0.0F, TeleportTransition.PLAY_PORTAL_SOUND));
        player.sendSystemMessage(Component.translatable("dust.labyrinth.enter", Component.translatable("entity.dust." + run.set.id + "_archon"), run.bossLevel));
    }

    // Back to where they came in (or, not knowing, to their own spawn)
    public static void leave(ServerPlayer player)
    {
        ReturnPoint point = player.getData(RETURN.get());
        player.setData(RETURN.get(), ReturnPoint.NONE);
        ServerLevel target = point.present() ? player.level().getServer().getLevel(point.dimension()) : null;
        if (target == null) {
            player.teleport(player.findRespawnPositionAndUseSpawnBlock(false, TeleportTransition.DO_NOTHING));
            return;
        }
        player.teleport(new TeleportTransition(target, point.pos(), Vec3.ZERO, point.yaw(), 0.0F, TeleportTransition.PLAY_PORTAL_SOUND));
    }

    // ---- dying ----

    @SubscribeEvent
    public static void onRespawnPosition(PlayerRespawnPositionEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || !is(player.level())) return;
        ReturnPoint point = player.getData(RETURN.get());
        ServerLevel target = point.present() ? player.level().getServer().getLevel(point.dimension()) : null;
        if (target == null) return;
        event.setTeleportTransition(new TeleportTransition(target, point.pos(), Vec3.ZERO, point.yaw(), 0.0F, TeleportTransition.DO_NOTHING));
    }

    // Last, so only a death that happens: an Archon beaten, or a player's things put by for their respawn
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeath(LivingDeathEvent event)
    {
        LivingEntity entity = event.getEntity();
        if (!(entity.level() instanceof ServerLevel level) || !is(level)) return;
        if (entity instanceof ArchonEntity archon && archon.isHerald()) {
            LabyrinthData data = LabyrinthData.get(level.getServer());
            LabyrinthInstance run = data.at(archon.blockPosition());
            if (run != null) LabyrinthRooms.heraldBeaten(level, data, run, archon);
        } else if (entity instanceof ArchonEntity archon) {
            bossBeaten(level, archon);
        }
        else if (entity instanceof ServerPlayer player && DustConfig.LABYRINTH_KEEP_INVENTORY.get()) keep(player);
    }

    private static void keep(ServerPlayer player)
    {
        Inventory inventory = player.getInventory();
        List<ItemStack> items = new ArrayList<>();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) items.add(inventory.getItem(slot).copy());
        player.setData(KEPT.get(), new KeptInventory(items, player.experienceLevel, player.experienceProgress, player.totalExperience));
        // Nothing left to drop
        inventory.clearContent();
        player.experienceLevel = 0;
        player.experienceProgress = 0.0F;
        player.totalExperience = 0;
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event)
    {
        if (!event.isWasDeath()) return;
        KeptInventory kept = event.getOriginal().getData(KEPT.get());
        if (kept.isEmpty()) return;
        Player player = (Player) event.getEntity();
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < kept.items().size() && slot < inventory.getContainerSize(); slot++) inventory.setItem(slot, kept.items().get(slot).copy());
        player.experienceLevel = kept.level();
        player.experienceProgress = kept.progress();
        player.totalExperience = kept.total();
        player.setData(KEPT.get(), KeptInventory.NONE);
    }

    // Logging in inside a run that's over (or not theirs): back out
    @SubscribeEvent
    public static void onLogIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || !is(player.level())) return;
        LabyrinthInstance run = LabyrinthData.get(player.level().getServer()).at(player.blockPosition());
        ReturnPoint point = player.getData(RETURN.get());
        if (run == null || run.state == LabyrinthInstance.State.CLOSED || !point.present() || point.run() != run.run) leave(player);
    }

    @SubscribeEvent
    public static void onSetSpawn(PlayerSetSpawnEvent event)
    {
        if (is(event.getEntity().level())) event.setCanceled(true);
    }

    // ---- the Archon ----

    private static void wake(ServerLevel level, LabyrinthData data, LabyrinthInstance run, List<ServerPlayer> inside)
    {
        ArchonEntity boss = Archons.TYPES.get(run.set).get().create(level, EntitySpawnReason.EVENT);
        if (boss == null) return;
        boss.snapTo(Vec3.atBottomCenterOf(run.bossSpawn), 180.0F, 0.0F);
        boss.addTag(runTag(run));
        boss.setBossLevel(run.bossLevel);
        boss.finalizeSpawn(level, level.getCurrentDifficultyAt(run.bossSpawn), EntitySpawnReason.EVENT, null);
        if (!level.addFreshEntity(boss)) return;
        run.boss = boss.getUUID();
        data.setState(run, LabyrinthInstance.State.BOSS);
        level.playSound(null, run.bossSpawn, SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 0.7F, 1.2F);
        for (ServerPlayer player : inside) player.sendSystemMessage(Component.translatable("dust.labyrinth.boss", boss.getDisplayName()));
    }

    private static void bossBeaten(ServerLevel level, ArchonEntity archon)
    {
        LabyrinthData data = LabyrinthData.get(level.getServer());
        LabyrinthInstance run = data.at(archon.blockPosition());
        if (run == null || run.state == LabyrinthInstance.State.COMPLETE || run.state == LabyrinthInstance.State.CLOSED) return;
        data.setState(run, LabyrinthInstance.State.COMPLETE);
        LabyrinthBuilder.openExit(level, run);
        if (run.keystone != null) data.requestReset(run.keystone);
        for (ServerPlayer player : playersIn(level, run)) player.sendSystemMessage(Component.translatable("dust.labyrinth.won", archon.getDisplayName()));
    }

    // ---- upkeep ----

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event)
    {
        if (!(event.getLevel() instanceof ServerLevel level) || !is(level) || level.getGameTime() % 20 != 0) return;
        LabyrinthData data = LabyrinthData.get(level.getServer());
        long now = level.getGameTime();
        for (LabyrinthInstance run : data.active()) {
            List<ServerPlayer> inside = playersIn(level, run);
            if (!inside.isEmpty()) {
                run.lastOccupied = now;
                data.setDirty();
            } else if (now - run.lastOccupied > CLOSE_AFTER) {
                close(level, data, run);
                continue;
            }
            LabyrinthRooms.tick(level, data, run, inside);
            if (run.state == LabyrinthInstance.State.OPEN && run.arena != null) {
                for (ServerPlayer player : inside) {
                    if (run.arena.isInside(player.blockPosition())) {
                        wake(level, data, run, inside);
                        break;
                    }
                }
            } else if (run.state == LabyrinthInstance.State.BOSS && !inside.isEmpty()) {
                // Its Archon went missing without dying (taken away by a command): it wakes again for the next visitor
                Entity boss = run.boss == null ? null : level.getEntity(run.boss);
                if (boss == null || !boss.isAlive()) data.setState(run, LabyrinthInstance.State.OPEN);
            }
        }
    }

    public static List<ServerPlayer> playersIn(ServerLevel level, LabyrinthInstance run)
    {
        List<ServerPlayer> inside = new ArrayList<>();
        for (ServerPlayer player : level.players()) if (slotAt(player.blockPosition()) == run.slot) inside.add(player);
        return inside;
    }

    private static void close(ServerLevel level, LabyrinthData data, LabyrinthInstance run)
    {
        data.setState(run, LabyrinthInstance.State.CLOSED);
        run.boss = null;
        AABB built = builtBox(run);
        if (built != null) for (Entity entity : level.getEntitiesOfClass(Entity.class, built, entity -> !(entity instanceof Player))) entity.discard();
    }

    // Everything the run built (its rooms with their walls), or null if nothing was
    static @Nullable AABB builtBox(LabyrinthInstance run)
    {
        BoundingBox all = null;
        for (BoundingBox box : run.interiors) all = all == null ? box : BoundingBox.encapsulatingBoxes(List.of(all, box)).orElse(all);
        return all == null ? null : AABB.of(all.inflatedBy(2));
    }

    // Things turning up outside a run, in a closed one, or belonging to an earlier run of the slot (its chunks loading
    // again with its old mobs in them) don't stay
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event)
    {
        Entity entity = event.getEntity();
        if (!is(event.getLevel()) || entity instanceof Player || !(event.getLevel() instanceof ServerLevel level)) return;
        LabyrinthInstance run = LabyrinthData.get(level.getServer()).at(entity.blockPosition());
        if (run == null || run.state == LabyrinthInstance.State.CLOSED) {
            event.setCanceled(true);
            return;
        }
        for (String tag : entity.entityTags()) {
            if (tag.startsWith(RUN_TAG) && !tag.equals(runTag(run))) {
                event.setCanceled(true);
                return;
            }
        }
    }

    // ---- blocks ----

    @SubscribeEvent
    public static void onBreak(BreakBlockEvent event)
    {
        if (!(event.getLevel() instanceof ServerLevel level) || !is(level)) return;
        Player player = event.getPlayer();
        if (player.getAbilities().instabuild) return;
        BlockPos pos = event.getPos();
        LabyrinthData data = LabyrinthData.get(level.getServer());
        LabyrinthInstance run = data.at(pos);
        // Their own block: as anywhere
        if (run != null && run.placed.remove(pos.asLong())) {
            data.setDirty();
            return;
        }
        event.setCanceled(true);
        if (run == null || run.isShell(pos) || !PhantomBlock.make(level, pos, false, PHASE_TICKS)) {
            player.sendOverlayMessage(Component.translatable("dust.labyrinth.unbreakable"));
        }
    }

    @SubscribeEvent
    public static void onPlace(BlockEvent.EntityPlaceEvent event)
    {
        if (!(event.getLevel() instanceof ServerLevel level) || !is(level) || !(event.getEntity() instanceof Player)) return;
        LabyrinthData data = LabyrinthData.get(level.getServer());
        LabyrinthInstance run = data.at(event.getPos());
        if (run != null && run.placed.add(event.getPos().asLong())) data.setDirty();
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event)
    {
        if (is(event.getLevel())) event.getAffectedBlocks().clear();
    }

    @SubscribeEvent
    public static void onPortalSpawn(BlockEvent.PortalSpawnEvent event)
    {
        if (event.getLevel() instanceof Level level && is(level)) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onMobGriefing(EntityMobGriefingEvent event)
    {
        if (is(event.getEntity().level())) event.setCanGrief(false);
    }
}
