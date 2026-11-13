package com.mowmaster.dust.Features.Spells.voidrift;

import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

// Void's self effect: a little winged ender chest that flies at its caster's shoulder. Right-click it (only its owner
// can) to open your ender chest. While it's open it doesn't age, so it won't vanish under you. Hit it to send it away.
// It isn't saved with the world: log out, change dimension or die and it's gone.
public class VoidChestEntity extends Entity
{
    // Whether its owner has it open: the renderer flaps the wings slower while it is
    private static final EntityDataAccessor<Boolean> OPEN = SynchedEntityData.defineId(VoidChestEntity.class, EntityDataSerializers.BOOLEAN);
    private static final Component TITLE = Component.translatable("container.enderchest");
    // How far it lets its owner get before it jumps back to them
    private static final double TELEPORT_DISTANCE = 16;

    private @Nullable UUID owner;
    private int lifetime = 20 * 60;
    private int age;

    public VoidChestEntity(EntityType<? extends VoidChestEntity> type, Level level)
    {
        super(type, level);
        noPhysics = true;
        setNoGravity(true);
    }

    // Calls a chest for this player, sending away one they already have nearby
    public static void summon(ServerLevel level, Player player, int lifetime)
    {
        for (VoidChestEntity old : level.getEntitiesOfClass(VoidChestEntity.class, player.getBoundingBox().inflate(64), chest -> player.getUUID().equals(chest.owner))) {
            old.vanish(level);
        }
        VoidChestEntity chest = DustEntityRegistry.VOID_CHEST.get().create(level, EntitySpawnReason.TRIGGERED);
        if (chest == null) return;
        chest.owner = player.getUUID();
        chest.lifetime = lifetime;
        Vec3 at = chest.perch(player);
        chest.setPos(at.x, at.y, at.z);
        chest.setYRot(player.getYRot());
        level.addFreshEntity(chest);
        level.sendParticles(ParticleTypes.PORTAL, at.x, at.y + 0.2, at.z, 30, 0.3, 0.3, 0.3, 0.5);
        level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.6F, 1.6F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {
        builder.define(OPEN, false);
    }

    public boolean isOpen()
    {
        return entityData.get(OPEN);
    }

    // Where it likes to be: above and behind its owner's right shoulder, bobbing a little.
    // Minecraft's yaw: facing direction is (-sin yaw, 0, cos yaw), so right is (-cos yaw, 0, -sin yaw).
    private Vec3 perch(Player player)
    {
        float yaw = player.getYRot() * Mth.DEG_TO_RAD;
        double rightX = -Mth.cos(yaw), rightZ = -Mth.sin(yaw);
        double backX = Mth.sin(yaw), backZ = -Mth.cos(yaw);
        double bob = Mth.sin(tickCount * 0.15F) * 0.08;
        return player.position().add(rightX * 0.8 + backX * 0.4, player.getBbHeight() + 0.1 + bob, rightZ * 0.8 + backZ * 0.4);
    }

    @Override
    public void tick()
    {
        super.tick();
        if (!(level() instanceof ServerLevel level)) return;
        // getPlayerByUUID only looks in this level, so an owner who changed dimension counts as gone
        Player player = owner == null ? null : level.getPlayerByUUID(owner);
        if (player == null || !player.isAlive()) {
            vanish(level);
            return;
        }

        // Fly toward its perch: a quarter of the way each tick, which eases in as it gets close
        Vec3 target = perch(player);
        Vec3 next = position().distanceTo(target) > TELEPORT_DISTANCE ? target : position().lerp(target, 0.25);
        setPos(next.x, next.y, next.z);
        setYRot(Mth.rotLerp(0.3F, getYRot(), player.getYRot()));

        // Open while its owner is looking in their ender chest through it (or through any ender chest: same inventory)
        boolean open = player.containerMenu instanceof ChestMenu menu && menu.getContainer() == player.getEnderChestInventory();
        if (open != isOpen()) {
            entityData.set(OPEN, open);
            if (!open) level.playSound(null, blockPosition(), SoundEvents.ENDER_CHEST_CLOSE, SoundSource.PLAYERS, 0.5F, 1.2F);
        }
        if (!open && ++age >= lifetime) vanish(level);
    }

    private void vanish(ServerLevel level)
    {
        level.sendParticles(ParticleTypes.PORTAL, getX(), getY() + 0.2, getZ(), 30, 0.3, 0.3, 0.3, 0.5);
        level.playSound(null, blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5F, 1.8F);
        discard();
    }

    // Right-click: its owner opens their ender chest. The client says it worked too, so the hand swings.
    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location)
    {
        // The owner isn't synced, so the client can't tell whose it is; the server decides
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (owner == null || !player.getUUID().equals(owner)) return InteractionResult.PASS;
        // SimpleMenuProvider: a menu factory and a title. The lambda builds vanilla's 3-row chest menu over the
        // player's ender chest inventory, the same menu an ender chest block opens.
        player.openMenu(new SimpleMenuProvider((id, inventory, p) -> ChestMenu.threeRows(id, inventory, p.getEnderChestInventory()), TITLE));
        level().playSound(null, blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundSource.PLAYERS, 0.5F, 1.2F);
        return InteractionResult.SUCCESS_SERVER;
    }

    // Hit by its owner: it leaves. Nothing else can hurt it.
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount)
    {
        if (source.getEntity() instanceof Player player && player.getUUID().equals(owner)) vanish(level);
        return false;
    }

    // Pickable: the crosshair can land on it, which right-clicking it needs
    @Override
    public boolean isPickable()
    {
        return !isRemoved();
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    // Not saved with the chunk: it only makes sense next to an owner who's here now
    @Override
    public boolean shouldBeSaved()
    {
        return false;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
    }
}
