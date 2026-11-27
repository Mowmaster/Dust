package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Bosses.LootBags;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import com.mowmaster.mowlib.display.ISpinningItemHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

// What an Arcane Vault (ArcaneVaultBlock) does, by its STATE:
//   INACTIVE / ACTIVE   twice a second, looks for a player within RANGE who hasn't opened it yet (creative ones too, as
//                       a vanilla vault); ACTIVE while there is one, showing a glimpse of its loot (a new one each second)
//   UNLOCKING           a key went in (tryUnlock): UNLOCK_TICKS later it starts giving out the loot
//   EJECTING            one thing every EJECT_EVERY ticks, out of its front, the next one turning inside; then it rests
// A key of its element opens it for one player, once: it remembers who opened it (rewarded), until its sanctum is to be
// fought through again (reset: SanctumKeystoneBlockEntity, after a labyrinth run from there is won). The loot is rolled
// as the key goes in, by that player's Magic Level (VaultLoot).
// What turns inside is drawn by MowLib's SpinningItemRenderer (ISpinningItemHolder); it's synced to clients with the
// rest of the block entity's data (CBSyncedBlockEntity).
public class ArcaneVaultBlockEntity extends CBSyncedBlockEntity implements ISpinningItemHolder
{
    public static final double RANGE = 4.5;
    private static final int UNLOCK_TICKS = 14;
    private static final int EJECT_EVERY = 10;

    private final Set<UUID> rewarded = new HashSet<>();
    private final List<ItemStack> toEject = new ArrayList<>();
    private ItemStack display = ItemStack.EMPTY;
    private long nextStep;

    public ArcaneVaultBlockEntity(BlockPos pos, BlockState state)
    {
        super(Sanctums.ARCANE_VAULT_ENTITY.get(), pos, state);
    }

    private ElementalArmorSet set()
    {
        return getBlockState().getValue(ArcaneVaultBlock.SET);
    }

    private ArcaneVaultBlock.State state()
    {
        return getBlockState().getValue(ArcaneVaultBlock.STATE);
    }

    @Override
    public ItemStack getDisplayedItem()
    {
        return display;
    }

    @Override
    public double getDisplayHeight()
    {
        return 0.45;
    }

    @Override
    public float getDisplayScale()
    {
        return 0.75F;
    }

    @Override
    public float getSpinSpeed()
    {
        return state() == ArcaneVaultBlock.State.ACTIVE ? 3.0F : 10.0F;
    }

    // Everyone may open it again (its sanctum is to be fought through again)
    public void reset()
    {
        if (rewarded.isEmpty()) return;
        rewarded.clear();
        sync();
    }

    void serverTick(ServerLevel level, BlockState state)
    {
        long now = level.getGameTime();
        switch (state.getValue(ArcaneVaultBlock.STATE)) {
            case INACTIVE, ACTIVE -> {
                if (now % 10 != 0) return;
                boolean someone = someoneCanOpen(level);
                if (someone != (state.getValue(ArcaneVaultBlock.STATE) == ArcaneVaultBlock.State.ACTIVE)) {
                    setState(level, someone ? ArcaneVaultBlock.State.ACTIVE : ArcaneVaultBlock.State.INACTIVE);
                    sound(level, someone ? SoundEvents.VAULT_ACTIVATE : SoundEvents.VAULT_DEACTIVATE);
                }
                if (someone && now % 20 == 0) {
                    display = VaultLoot.glimpse(set(), level.getRandom());
                    sync();
                } else if (!someone && !display.isEmpty()) {
                    display = ItemStack.EMPTY;
                    sync();
                }
            }
            case UNLOCKING -> {
                if (now < nextStep) return;
                nextStep = now + EJECT_EVERY;
                setState(level, ArcaneVaultBlock.State.EJECTING);
                sound(level, SoundEvents.VAULT_OPEN_SHUTTER);
            }
            case EJECTING -> {
                if (now < nextStep) return;
                if (toEject.isEmpty()) {
                    display = ItemStack.EMPTY;
                    setState(level, ArcaneVaultBlock.State.INACTIVE);
                    sound(level, SoundEvents.VAULT_CLOSE_SHUTTER);
                    sync();
                    return;
                }
                eject(level, toEject.remove(0));
                display = toEject.isEmpty() ? ItemStack.EMPTY : toEject.get(0).copyWithCount(1);
                nextStep = now + EJECT_EVERY;
                sync();
            }
        }
    }

    // A player near enough who hasn't opened it yet
    private boolean someoneCanOpen(ServerLevel level)
    {
        Vec3 centre = Vec3.atCenterOf(worldPosition);
        for (ServerPlayer player : level.players()) {
            if (player.isSpectator() || !player.isAlive() || rewarded.contains(player.getUUID())) continue;
            if (player.distanceToSqr(centre) <= RANGE * RANGE) return true;
        }
        return false;
    }

    // A player puts a key in: it opens if it's a key of this vault's element, the player hasn't opened it before, and it
    // isn't busy giving out someone's loot. The key is used up and the loot rolled now.
    void tryUnlock(ServerLevel level, Player player, ItemStack key)
    {
        if (!(key.getItem() instanceof VaultKeyItem keyItem)) return;
        ElementalArmorSet set = set();
        if (state().lit() && state() != ArcaneVaultBlock.State.ACTIVE) {
            sound(level, SoundEvents.VAULT_INSERT_ITEM_FAIL);
            return;
        }
        if (keyItem.set() != set) {
            player.sendOverlayMessage(Component.translatable("dust.vault.wrong_key", new ItemStack(Sanctums.KEYS.get(set).get()).getHoverName()));
            sound(level, SoundEvents.VAULT_INSERT_ITEM_FAIL);
            return;
        }
        if (rewarded.contains(player.getUUID())) {
            player.sendOverlayMessage(Component.translatable("dust.vault.opened"));
            sound(level, SoundEvents.VAULT_REJECT_REWARDED_PLAYER);
            return;
        }
        Vec3 front = front();
        List<LootBags.Reward> loot = VaultLoot.roll(level, player, set, key, front);
        toEject.clear();
        for (LootBags.Reward reward : loot) if (!reward.stack().isEmpty()) toEject.add(reward.stack().copy());
        if (toEject.isEmpty()) {
            sound(level, SoundEvents.VAULT_INSERT_ITEM_FAIL);
            return;
        }
        key.consume(1, player);
        rewarded.add(player.getUUID());
        VaultLoot.announce(level, player, loot);
        display = toEject.get(0).copyWithCount(1);
        nextStep = level.getGameTime() + UNLOCK_TICKS;
        setState(level, ArcaneVaultBlock.State.UNLOCKING);
        sound(level, SoundEvents.VAULT_INSERT_ITEM);
        level.sendParticles(new DustParticleOptions(set.element().color(), 1.4F), front.x, front.y, front.z, 16, 0.25, 0.25, 0.25, 0);
        sync();
    }

    // Just outside the middle of its front
    private Vec3 front()
    {
        Direction facing = getBlockState().getValue(ArcaneVaultBlock.FACING);
        return Vec3.atCenterOf(worldPosition).add(facing.getStepX() * 0.7, -0.1, facing.getStepZ() * 0.7);
    }

    // One thing out of its front, tossed gently forward
    private void eject(ServerLevel level, ItemStack stack)
    {
        RandomSource random = level.getRandom();
        Direction facing = getBlockState().getValue(ArcaneVaultBlock.FACING);
        Vec3 at = front();
        ItemEntity item = new ItemEntity(level, at.x, at.y, at.z, stack);
        item.setDeltaMovement(facing.getStepX() * 0.12 + (random.nextDouble() - 0.5) * 0.06, 0.2 + random.nextDouble() * 0.05,
                facing.getStepZ() * 0.12 + (random.nextDouble() - 0.5) * 0.06);
        item.setDefaultPickUpDelay();
        level.addFreshEntity(item);
        sound(level, SoundEvents.VAULT_EJECT_ITEM);
        level.sendParticles(new DustParticleOptions(set().element().color(), 1.2F), at.x, at.y, at.z, 8, 0.15, 0.15, 0.15, 0);
    }

    private void sound(ServerLevel level, SoundEvent sound)
    {
        level.playSound(null, worldPosition, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private void setState(ServerLevel level, ArcaneVaultBlock.State state)
    {
        BlockState blockState = getBlockState();
        if (blockState.getValue(ArcaneVaultBlock.STATE) != state) level.setBlock(worldPosition, blockState.setValue(ArcaneVaultBlock.STATE, state), Block.UPDATE_ALL);
        setChanged();
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.store("rewarded", UUIDUtil.CODEC.listOf(), List.copyOf(rewarded));
        output.store("to_eject", ItemStack.CODEC.listOf(), List.copyOf(toEject));
        if (!display.isEmpty()) output.store("display", ItemStack.OPTIONAL_CODEC, display);
        output.putLong("next_step", nextStep);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        rewarded.clear();
        input.read("rewarded", UUIDUtil.CODEC.listOf()).ifPresent(rewarded::addAll);
        toEject.clear();
        input.read("to_eject", ItemStack.CODEC.listOf()).ifPresent(toEject::addAll);
        display = input.read("display", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        nextStep = input.getLongOr("next_step", 0L);
    }
}
