package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.mixin.AbstractFurnaceAccessor;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// Step 7a's effect parts: refuelling furnaces, felling trees, planting, warping
public final class UtilityParts
{
    private UtilityParts() {}

    // ---- Energy: fuel ----

    // A stick's worth of fuel (5 seconds of burning) x potency into every furnace within 5 blocks (more with size)
    public static void fuelFurnaces(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        int radius = 5 + cast.stats().size();
        int fuel = Math.round(100 * cast.stats().potency());
        for (BlockPos at : BlockPos.betweenClosed(pos.offset(-radius, -radius, -radius), pos.offset(radius, radius, radius))) {
            if (!(level.getBlockEntity(at) instanceof AbstractFurnaceBlockEntity furnace)) continue;
            AbstractFurnaceAccessor timer = (AbstractFurnaceAccessor) furnace;
            int left = timer.dust$getLitTimeRemaining() + fuel;
            timer.dust$setLitTimeRemaining(left);
            timer.dust$setLitTotalTime(Math.max(timer.dust$getLitTotalTime(), left));
            BlockState state = level.getBlockState(at);
            if (state.hasProperty(AbstractFurnaceBlock.LIT) && !state.getValue(AbstractFurnaceBlock.LIT)) {
                level.setBlockAndUpdate(at, state.setValue(AbstractFurnaceBlock.LIT, true));
            }
            furnace.setChanged();
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, at.getX() + 0.5, at.getY() + 0.8, at.getZ() + 0.5, 6, 0.3, 0.2, 0.3, 0.05);
        }
    }

    // ---- Trees: felling ----

    private static final int MAX_LOGS = 256;

    // A log that's part of a tree (its logs touch natural leaves) comes down whole: every connected log breaks and drops.
    // A log in a build (no natural leaves) is left alone.
    public static void fellTree(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        if (!level.getBlockState(pos).is(BlockTags.LOGS)) return;
        List<BlockPos> logs = new ArrayList<>();
        Set<BlockPos> seen = new HashSet<>();
        ArrayDeque<BlockPos> open = new ArrayDeque<>();
        open.add(pos.immutable());
        seen.add(pos.immutable());
        boolean natural = false;
        while (!open.isEmpty() && logs.size() < MAX_LOGS) {
            BlockPos at = open.poll();
            logs.add(at);
            for (BlockPos near : BlockPos.betweenClosed(at.offset(-1, -1, -1), at.offset(1, 1, 1))) {
                BlockState there = level.getBlockState(near);
                if (there.getBlock() instanceof LeavesBlock && !there.getValue(LeavesBlock.PERSISTENT)) natural = true;
                if (there.is(BlockTags.LOGS) && seen.add(near.immutable())) open.add(near.immutable());
            }
        }
        if (!natural) return;
        for (BlockPos log : logs) level.destroyBlock(log, true, cast.caster());
    }

    // ---- Plants ----

    // Seeds on a block: what's in the caster's hand that can be planted (a seed, sapling, flower...), the main hand
    // first, planted on the block as if they'd right-clicked its top with it (an empty flower pot takes a flower). Used up
    // as a player's planting uses it (not in creative). Cast by a Spell Caster: something from the containers around it
    // instead (they stand in for a player's hands: CasterStorage).
    public static void plantFromHand(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        com.mowmaster.dust.Features.Spells.caster.CasterStorage storage = com.mowmaster.dust.Features.Spells.caster.CasterStorage.of(cast);
        if (storage != null) {
            plantFromStorage(level, storage, pos);
            return;
        }
        if (!(cast.caster() instanceof Player player)) return;
        BlockState state = level.getBlockState(pos);
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(pos).add(0, 0.5, 0), Direction.UP, pos, false);
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (!plantable(stack)) continue;
            // A flower pot takes it through the pot's own use (only pots: other blocks' uses, a composter's, aren't planting)
            if (state.getBlock() instanceof FlowerPotBlock && state.useItemOn(stack, level, player, hand, hit).consumesAction()) return;
            if (stack.useOn(new UseOnContext(player, hand, hit)).consumesAction()) return;
        }
    }

    // What can be planted: a block item for a plant (a seed, sapling, flower, mushroom...)
    private static boolean plantable(ItemStack stack)
    {
        return stack.getItem() instanceof BlockItem item && (item.getBlock() instanceof VegetationBlock || item.getBlock() instanceof CropBlock);
    }

    // A random flower on the block (or in it, if it's an empty flower pot). Cast by a Spell Caster with containers
    // around it, something from them is planted instead (plantFromStorage).
    public static void plantFlower(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        com.mowmaster.dust.Features.Spells.caster.CasterStorage storage = com.mowmaster.dust.Features.Spells.caster.CasterStorage.of(cast);
        if (storage != null && storage.hasItems()) {
            plantFromStorage(level, storage, pos);
            return;
        }
        BlockState state = level.getBlockState(pos);
        List<Block> flowers = new ArrayList<>();
        for (Holder<Block> flower : BuiltInRegistries.BLOCK.getTagOrEmpty(BlockTags.SMALL_FLOWERS)) {
            // A player only plants flowers they've fully researched (every element in it found and discovered: the
            // same test as the element tooltip). A Spell Caster (no player) plants any.
            if (cast.caster() instanceof ServerPlayer player
                    && com.mowmaster.dust.Features.DustComposition.ElementTooltip.forPlayer(new ItemStack(flower.value()), player) == null) continue;
            flowers.add(flower.value());
        }
        if (flowers.isEmpty()) {
            if (cast.caster() instanceof ServerPlayer player
                    && cast.firstTime("plant_unknown")) player.sendOverlayMessage(Component.translatable("dust.spell.unknown_plants"));
            return;
        }
        if (state.getBlock() instanceof FlowerPotBlock pot && pot.getPotted() == net.minecraft.world.level.block.Blocks.AIR) {
            // Stonecutter: 26.1.2's pot map is keyed by the flower's id, with suppliers of the potted block
            //? if >=26.3 {
            List<Block> pottable = flowers.stream().filter(pot.getFullPots()::containsKey).toList();
            if (!pottable.isEmpty()) level.setBlockAndUpdate(pos, pot.getFullPots().get(pottable.get(level.getRandom().nextInt(pottable.size()))).defaultBlockState());
            //?} else {
            /*var pots = pot.getFullPotsView();
            List<Block> pottable = flowers.stream().filter(flower -> pots.containsKey(BuiltInRegistries.BLOCK.getKey(flower))).toList();
            if (!pottable.isEmpty()) level.setBlockAndUpdate(pos, pots.get(BuiltInRegistries.BLOCK.getKey(pottable.get(level.getRandom().nextInt(pottable.size())))).get().defaultBlockState());
            *///?}
            return;
        }
        BlockPos above = pos.above();
        if (!level.getBlockState(above).isAir()) return;
        Block flower = flowers.get(level.getRandom().nextInt(flowers.size()));
        if (flower.defaultBlockState().canSurvive(level, above)) {
            level.setBlockAndUpdate(above, flower.defaultBlockState());
            level.levelEvent(1505, above, 6);
        }
    }

    // The first thing in a Spell Caster's containers that can be planted on the block (the containers stand in for a
    // player's inventory: CasterStorage), planted on top of it as a player would plant it, or put in it if it's an
    // empty flower pot. Only what's planted is used up.
    private static void plantFromStorage(ServerLevel level, com.mowmaster.dust.Features.Spells.caster.CasterStorage storage, BlockPos pos)
    {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof FlowerPotBlock pot && pot.getPotted() == net.minecraft.world.level.block.Blocks.AIR) {
            storage.useOne(UtilityParts::plantable, stack -> potPlant(level, pos, pot, stack));
            return;
        }
        BlockPos above = pos.above();
        if (!level.getBlockState(above).canBeReplaced()) return;
        storage.useOne(UtilityParts::plantable, stack -> {
            // Placed as a dispenser places a block (no player), on top of the block: BlockItem.place checks it can
            // grow there (a seed on farmland, a sapling on dirt...)
            BlockItem item = (BlockItem) stack.getItem();
            boolean planted = item.place(new DirectionalPlaceContext(level, above, Direction.DOWN, stack, Direction.UP)).consumesAction();
            if (planted) level.levelEvent(1505, above, 6);
            return planted;
        });
    }

    // Puts the item's plant in an empty flower pot, if it's one that can be potted
    private static boolean potPlant(ServerLevel level, BlockPos pos, FlowerPotBlock pot, ItemStack stack)
    {
        Block plant = ((BlockItem) stack.getItem()).getBlock();
        // Stonecutter: 26.1.2's pot map is keyed by the plant's id, with suppliers of the potted block
        //? if >=26.3 {
        Block potted = pot.getFullPots().get(plant);
        //?} else {
        /*var supplier = pot.getFullPotsView().get(BuiltInRegistries.BLOCK.getKey(plant));
        Block potted = supplier == null ? null : supplier.get();
        *///?}
        if (potted == null || potted == net.minecraft.world.level.block.Blocks.AIR) return false;
        level.setBlockAndUpdate(pos, potted.defaultBlockState());
        return true;
    }

    // ---- Warp ----

    // ---- Warp ----

    // The caster's warp mark (null if they haven't set one)
    private static @Nullable GlobalPos mark(@Nullable LivingEntity caster)
    {
        return caster instanceof Player player ? player.getData(DustAttachmentTypeRegistry.WARP_MARK).orElse(null) : null;
    }

    private static void setMark(Player player, GlobalPos pos)
    {
        player.setData(DustAttachmentTypeRegistry.WARP_MARK, Optional.of(pos));
        player.sendOverlayMessage(Component.translatable("dust.warp.marked", pos.pos().getX(), pos.pos().getY(), pos.pos().getZ()));
    }

    // Where a warp sends things: the caster's mark, else their respawn point (null: no player caster)
    private static @Nullable TeleportTransition destination(SpellCast cast)
    {
        if (!(cast.caster() instanceof ServerPlayer player)) return null;
        GlobalPos mark = mark(player);
        if (mark != null) {
            ServerLevel level = cast.level().getServer().getLevel(mark.dimension());
            if (level != null) return new TeleportTransition(level, Vec3.atBottomCenterOf(safeSpot(level, mark.pos())), Vec3.ZERO, player.getYRot(), player.getXRot(),
                    TeleportTransition.DO_NOTHING);
        }
        // Their bed / anchor / world spawn, without using up an anchor charge
        return player.findRespawnPositionAndUseSpawnBlock(false, TeleportTransition.DO_NOTHING);
    }

    // The mark may have been built over since it was set: the first spot at or above it (up to 8 blocks) with two
    // blocks of room to stand in. If there's none, the mark itself.
    private static BlockPos safeSpot(ServerLevel level, BlockPos mark)
    {
        for (int up = 0; up <= 8; up++) {
            BlockPos feet = mark.above(up);
            if (level.getBlockState(feet).getCollisionShape(level, feet).isEmpty()
                    && level.getBlockState(feet.above()).getCollisionShape(level, feet.above()).isEmpty()) {
                return feet;
            }
        }
        return mark;
    }

    private static void warp(SpellCast cast, Entity entity, TeleportTransition to)
    {
        cast.level().sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + 1, entity.getZ(), 30, 0.4, 0.8, 0.4, 0.2);
        entity.teleport(new TeleportTransition(to.newLevel(), to.position(), Vec3.ZERO, entity.getYRot(), entity.getXRot(), TeleportTransition.DO_NOTHING));
        // Arrives standing still: no fall damage carried over from before the warp
        entity.resetFallDistance();
        to.newLevel().sendParticles(ParticleTypes.REVERSE_PORTAL, to.position().x, to.position().y + 1, to.position().z, 20, 0.4, 0.8, 0.4, 0.05);
        to.newLevel().playSound(null, BlockPos.containing(to.position()), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    // Self: sneaking, it marks where you stand; otherwise it warps you to your mark (or your respawn point)
    public static void warpSelf(SpellCast cast, LivingEntity target)
    {
        if (!(target instanceof ServerPlayer player)) return;
        if (player.isShiftKeyDown()) {
            setMark(player, GlobalPos.of(player.level().dimension(), player.blockPosition()));
            return;
        }
        TeleportTransition to = destination(cast);
        if (to != null) warp(cast, player, to);
    }

    // Block: with no mark yet (or sneaking), it marks the spot above the block. Otherwise the items lying there, and the
    // block itself (not one holding things, not an unbreakable one, and only if it can stay where it lands), go to the
    // mark (or the caster's respawn point).
    public static void warpBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        if (!(cast.caster() instanceof ServerPlayer player)) return;
        if (mark(player) == null || player.isShiftKeyDown()) {
            setMark(player, GlobalPos.of(cast.level().dimension(), pos.above()));
            return;
        }
        TeleportTransition to = destination(cast);
        if (to == null) return;
        ServerLevel level = cast.level();
        ServerLevel there = to.newLevel();
        BlockPos landing = BlockPos.containing(to.position());
        BlockState state = level.getBlockState(pos);
        boolean movable = !state.isAir() && state.getDestroySpeed(level, pos) >= 0 && level.getBlockEntity(pos) == null
                && there.getBlockState(landing).canBeReplaced() && state.canSurvive(there, landing);
        if (movable) {
            level.removeBlock(pos, false);
            there.setBlockAndUpdate(landing, state);
            level.sendParticles(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 30, 0.4, 0.4, 0.4, 0.2);
        }
        // Items land on top of the block if it went too, so they aren't stuck inside it
        TeleportTransition itemsTo = movable ? to.withPosition(to.position().add(0, 1, 0)) : to;
        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, new AABB(pos.relative(face)).inflate(1))) warp(cast, item, itemsTo);
    }

    // Mob: it goes to the caster's mark (or their respawn point). Bosses hold their ground.
    public static void warpMob(SpellCast cast, Entity target)
    {
        if (target instanceof LivingEntity living && living.is(net.neoforged.neoforge.common.Tags.EntityTypes.BOSSES)) return;
        TeleportTransition to = destination(cast);
        if (to != null) warp(cast, target, to);
    }


    // ---- Tool: one tier lower ----

    // The block can be harvested with a tool one tier below what it needs, for a while (SpellMobEffects.onHarvestCheck)
    public static void markLowerTier(SpellCast cast, BlockPos pos, Direction face)
    {
        BlockMarks.mark(cast.level(), pos, BlockMarks.LOWER_TIER, cast.stats().duration() * 12);
    }
}
