package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.IShearable;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// The fusions' own effects (modifiers/FusionModifier, registered in SpellRegistries)
public final class FusionParts
{
    private FusionParts() {}

    // Fracture + Earth, on a block: breaks it as a pickaxe would.
    //   Potency    the pickaxe's tier: under 1 wood, 1 stone, 2 iron, 3 diamond, 4 and up netherite. A block that needs a
    //              better pickaxe isn't touched.
    //   Luck       Fortune: one level for every 5% of luck (5 Luck levels), up to Fortune V
    //   Silky      Silk Touch
    public static void mine(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.getDestroySpeed(level, pos) < 0) return;
        ItemStack pickaxe = pickaxe(cast.stats().potency());
        if (state.requiresCorrectToolForDrops() && !pickaxe.isCorrectToolForDrops(state)) return;
        // The registry of enchantments is data (it can change with datapacks), so they're looked up through the level
        var enchantments = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        if (SpellRegistries.silky(cast)) {
            pickaxe.enchant(enchantments.getOrThrow(Enchantments.SILK_TOUCH), 1);
        } else {
            withFortune(cast, pickaxe);
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        // The drops this pickaxe would get, worked out before the block goes
        List<ItemStack> drops = Block.getDrops(state, level, pos, blockEntity, cast.caster(), pickaxe);
        level.destroyBlock(pos, false, cast.caster());
        for (ItemStack drop : drops) Block.popResource(level, pos, drop);
        // Experience from ores and the like (true: as a player mining it would)
        state.spawnAfterBreak(level, pos, pickaxe, true);
    }

    private static ItemStack pickaxe(float potency)
    {
        if (potency >= 4) return new ItemStack(Items.NETHERITE_PICKAXE);
        if (potency >= 3) return new ItemStack(Items.DIAMOND_PICKAXE);
        if (potency >= 2) return new ItemStack(Items.IRON_PICKAXE);
        if (potency >= 1) return new ItemStack(Items.STONE_PICKAXE);
        return new ItemStack(Items.WOODEN_PICKAXE);
    }

    // Luck as Fortune on a tool: one level for every 5% of luck (5 Luck levels), up to Fortune V. The tool comes back.
    private static ItemStack withFortune(SpellCast cast, ItemStack tool)
    {
        int fortune = Math.min(5, (int) Math.floor(cast.stats().luck() * 100 / 5));
        // The registry of enchantments is data (it can change with datapacks), so they're looked up through the level
        if (fortune > 0) tool.enchant(cast.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), fortune);
        return tool;
    }

    // ---- Plants + Tool: Harvest ----
    // Harvesting takes what has grown and leaves the plant to grow again, as a farmer would:
    //   crops          wheat, carrots, potatoes, beetroots, nether wart, cocoa (and other mods' crops that are
    //                  CropBlocks), fully grown only: what they drop (luck is Fortune), and they're planted again young
    //                  with one of the seeds they dropped
    //   berries        a sweet berry bush's or a glow berry vine's, picked off
    //   melons, pumpkins                   broken for what they drop
    //   sugar cane, cactus, bamboo, kelp   cut down to the bottom one, which grows again
    // The drops go through NeoForge's block drop event, as a broken block's do, so with a player harvesting, crops give
    // their element orbs as when they're broken by hand (DustOrbDrops). A Spell Caster's harvest gives none.
    // Self: the block the caster stands in (a crop), or if there's nothing there, the one they stand on; with the old
    // size stat, a square of them. On a block: harvested, or if there's nothing to harvest, sheared. On a creature:
    // sheared.

    public static void harvestUnder(SpellCast cast, LivingEntity target)
    {
        // A little above the feet: standing on farmland or soul sand (not full blocks), the feet are inside the block's
        // space, and blockPosition() would be the farmland rather than the crop on it
        BlockPos feet = BlockPos.containing(target.getX(), target.getY() + 0.25, target.getZ());
        int size = cast.stats().size();
        for (BlockPos at : BlockPos.betweenClosed(feet.offset(-size, 0, -size), feet.offset(size, 0, size))) {
            // betweenClosed reuses one mutable position: copied before it's kept
            BlockPos pos = at.immutable();
            if (!harvest(cast, pos)) harvest(cast, pos.below());
        }
    }

    public static void harvestOrShear(SpellCast cast, BlockPos pos, Direction face)
    {
        if (!harvest(cast, pos)) shear(cast, pos);
    }

    // Harvests what's at pos; false if there's nothing grown there to harvest
    public static boolean harvest(SpellCast cast, BlockPos pos)
    {
        ServerLevel level = cast.level();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        // Each case names the block's class and binds it (pattern matching), or tests the block itself
        BlockState young;
        if (block instanceof CropBlock crop) {
            young = crop.isMaxAge(state) ? crop.getStateForAge(0) : null;
        } else if (block instanceof NetherWartBlock) {
            young = state.getValue(NetherWartBlock.AGE) >= NetherWartBlock.MAX_AGE ? state.setValue(NetherWartBlock.AGE, 0) : null;
        } else if (block instanceof CocoaBlock) {
            young = state.getValue(CocoaBlock.AGE) >= CocoaBlock.MAX_AGE ? state.setValue(CocoaBlock.AGE, 0) : null;
        } else if (block instanceof SweetBerryBushBlock) {
            return pickBerries(cast, pos, state);
        } else if (block instanceof CaveVines) {
            // As a player picking them (the vine's own code: its berry loot table, sound and state)
            return CaveVines.hasGlowBerries(state) && CaveVines.use(cast.caster(), state, level, pos).consumesAction();
        } else if (state.is(Blocks.MELON) || state.is(Blocks.PUMPKIN)) {
            reap(cast, pos, state, null, Blocks.AIR.defaultBlockState());
            return true;
        } else {
            return cutColumn(cast, pos, state);
        }
        if (young == null) return false;
        // A crop's block item is its seed (wheat's is wheat seeds; carrots are their own)
        reap(cast, pos, state, block.asItem(), young);
        return true;
    }

    // A sweet berry bush with berries (age 2 or 3): picked as by hand (1-2 berries, one more when it's fully grown),
    // back to age 1
    private static boolean pickBerries(SpellCast cast, BlockPos pos, BlockState state)
    {
        int age = state.getValue(SweetBerryBushBlock.AGE);
        if (age < 2) return false;
        ServerLevel level = cast.level();
        int count = 1 + level.getRandom().nextInt(2) + (age == SweetBerryBushBlock.MAX_AGE ? 1 : 0);
        Block.popResource(level, pos, new ItemStack(Items.SWEET_BERRIES, count));
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
        BlockState picked = state.setValue(SweetBerryBushBlock.AGE, 1);
        level.setBlock(pos, picked, Block.UPDATE_CLIENTS);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(cast.caster(), picked));
        return true;
    }

    // Breaks a grown plant for what it drops (luck as Fortune), leaving `after` in its place; with a seed, one of the
    // seeds it drops is kept back to plant it again
    private static void reap(SpellCast cast, BlockPos pos, BlockState state, @Nullable Item seed, BlockState after)
    {
        ServerLevel level = cast.level();
        ItemStack tool = withFortune(cast, new ItemStack(Items.IRON_HOE));
        BlockEntity blockEntity = level.getBlockEntity(pos);
        List<ItemStack> drops = new ArrayList<>(Block.getDrops(state, level, pos, blockEntity, cast.caster(), tool));
        if (seed != null) takeOne(drops, seed);
        // Level event 2001: the block's breaking particles and sound
        level.levelEvent(2001, pos, Block.getId(state));
        level.setBlock(pos, after, Block.UPDATE_ALL);
        drop(cast, pos, state, blockEntity, drops, tool);
    }

    // Takes one of the item out of the drops (a seed kept back for planting again)
    private static void takeOne(List<ItemStack> drops, Item item)
    {
        for (int i = 0; i < drops.size(); i++) {
            ItemStack stack = drops.get(i);
            if (!stack.is(item)) continue;
            if (stack.getCount() > 1) drops.set(i, stack.copyWithCount(stack.getCount() - 1));
            else drops.remove(i);
            return;
        }
    }

    // Puts the drops in the world where a broken block's go, through NeoForge's block drop event (BlockDropsEvent: other
    // mods, and Dust's own orb drops, see what dropped and who broke it)
    private static void drop(SpellCast cast, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, List<ItemStack> drops, ItemStack tool)
    {
        ServerLevel level = cast.level();
        if (!level.getGameRules().get(GameRules.BLOCK_DROPS)) return;
        List<ItemEntity> entities = new ArrayList<>();
        for (ItemStack stack : drops) {
            if (stack.isEmpty()) continue;
            // Where Block.popResource puts a drop: a little way off the block's middle
            ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5 + Mth.nextDouble(level.getRandom(), -0.25, 0.25),
                    pos.getY() + 0.375 + Mth.nextDouble(level.getRandom(), -0.25, 0.25),
                    pos.getZ() + 0.5 + Mth.nextDouble(level.getRandom(), -0.25, 0.25), stack);
            entity.setDefaultPickUpDelay();
            entities.add(entity);
        }
        CommonHooks.handleBlockDrops(level, pos, state, blockEntity, entities, cast.caster(), tool);
    }

    // Sugar cane, cactus, bamboo and kelp: everything above the bottom one is cut (each drops as when it's broken), so
    // the plant grows again. False if it isn't one, or it's only one block tall.
    private static boolean cutColumn(SpellCast cast, BlockPos pos, BlockState state)
    {
        if (!isColumnPlant(state)) return false;
        ServerLevel level = cast.level();
        BlockPos bottom = pos;
        while (sameColumn(state, level.getBlockState(bottom.below()))) bottom = bottom.below();
        BlockPos top = pos;
        while (sameColumn(state, level.getBlockState(top.above()))) top = top.above();
        if (top.equals(bottom)) return false;
        // From the top down, so nothing above is left hanging
        for (BlockPos at = top; at.getY() > bottom.getY(); at = at.below()) level.destroyBlock(at, true, cast.caster());
        return true;
    }

    private static boolean isColumnPlant(BlockState state)
    {
        return state.is(Blocks.SUGAR_CANE) || state.is(Blocks.CACTUS) || state.is(Blocks.BAMBOO) || state.is(Blocks.KELP) || state.is(Blocks.KELP_PLANT);
    }

    // Two blocks of the same tall plant (kelp's top and its stem are two blocks, one plant)
    private static boolean sameColumn(BlockState plant, BlockState other)
    {
        boolean kelp = plant.is(Blocks.KELP) || plant.is(Blocks.KELP_PLANT);
        return kelp ? other.is(Blocks.KELP) || other.is(Blocks.KELP_PLANT) : other.is(plant.getBlock());
    }

    // Shears on a block: a full beehive or bee nest gives up its honeycomb (as shears in a dispenser take it), and a
    // block only shears get as itself (leaves, grass, ferns, vines, cobwebs, seagrass...) is cut, dropping itself.
    // False if shears do nothing to it.
    public static boolean shear(SpellCast cast, BlockPos pos)
    {
        ServerLevel level = cast.level();
        BlockState state = level.getBlockState(pos);
        ItemStack shears = new ItemStack(Items.SHEARS);
        if (state.getBlock() instanceof BeehiveBlock hive && state.getValue(BeehiveBlock.HONEY_LEVEL) >= BeehiveBlock.MAX_HONEY_LEVELS) {
            level.playSound(null, pos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
            BeehiveBlock.dropHoneycomb(level, shears, state, level.getBlockEntity(pos), cast.caster(), pos);
            hive.releaseBeesAndResetHoneyLevel(level, state, pos, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
            level.gameEvent(cast.caster(), GameEvent.SHEAR, pos);
            return true;
        }
        if (!onlyShearsKeep(level, pos, state, shears)) return false;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        List<ItemStack> drops = Block.getDrops(state, level, pos, blockEntity, cast.caster(), shears);
        level.destroyBlock(pos, false, cast.caster());
        drop(cast, pos, state, blockEntity, drops, shears);
        return true;
    }

    // Whether shears get this block as itself and a bare hand doesn't. A bare hand's drops can be down to chance
    // (leaves sometimes drop a sapling), so that's tried a few times.
    private static boolean onlyShearsKeep(ServerLevel level, BlockPos pos, BlockState state, ItemStack shears)
    {
        Item self = state.getBlock().asItem();
        if (state.isAir() || self == Items.AIR) return false;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (Block.getDrops(state, level, pos, blockEntity, null, shears).stream().noneMatch(stack -> stack.is(self))) return false;
        for (int i = 0; i < 3; i++) {
            if (Block.getDrops(state, level, pos, blockEntity, null, ItemStack.EMPTY).stream().anyMatch(stack -> stack.is(self))) return false;
        }
        return true;
    }

    // Shears on a creature: anything shears work on (sheep, mooshrooms, snow golems, bogged, and other mods' creatures
    // through NeoForge's IShearable), as a player's shears would do it
    public static void shearMob(SpellCast cast, Entity target)
    {
        if (!(target instanceof IShearable shearable)) return;
        ServerLevel level = cast.level();
        ItemStack shears = new ItemStack(Items.SHEARS);
        BlockPos pos = target.blockPosition();
        Player player = cast.caster() instanceof Player caster ? caster : null;
        if (!shearable.isShearable(player, shears, level, pos)) return;
        for (ItemStack drop : shearable.onSheared(player, shears, level, pos)) shearable.spawnShearedDrop(level, pos, drop);
        target.gameEvent(GameEvent.SHEAR, cast.caster());
    }
}
