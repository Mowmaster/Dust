package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.Features.Machines.AlchemicalDust;
import com.mowmaster.dust.Features.Magic.Awakening;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

import java.util.List;

// Burns one item to see the colour of its dust: the fire it lights (DustFireBlock) burns in that colour.
//   Right-click a block   burns one dropped item lying where the fire would go, or else one item from the offhand
//   Right-click lava      burns the lava itself (none is used up): the fire burns on its surface
// Something with no readable dust (not in DustCompositions) won't catch: a puff of smoke and nothing is used.
// A player carrying Research Notes notes the colour on the burned thing's entry (DustResearch).
public class MagicalFlintAndSteelItem extends Item
{
    public MagicalFlintAndSteelItem(Properties properties)
    {
        super(properties);
    }

    // Clicking a block (or lava in front of it)
    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (player != null) {
            InteractionResult lava = tryBurnLava(level, player, context.getHand());
            if (lava != null) return lava;
        }

        BlockPos firePos = context.getClickedPos().relative(context.getClickedFace());
        DustFireBlock fire = (DustFireBlock) DustBlockRegistry.DUST_FIRE.get();
        if (!level.getBlockState(firePos).canBeReplaced() || !fire.defaultBlockState().canSurvive(level, firePos)) {
            return InteractionResult.FAIL;
        }

        // What to burn: a dropped item there with dust in it, or else the offhand item
        ItemEntity dropped = droppedItemToBurn(level, firePos);
        ItemStack burning = dropped != null ? dropped.getItem() : offhandToBurn(player, context.getHand());
        DustComposition composition = DustCompositions.of(burning);
        if (composition == null) {
            fizzle(level, player, firePos);
            return InteractionResult.FAIL;
        }
        // The client stops here; the server burns it (and tells the client what changed)
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        ItemStack burned = burning.copyWithCount(1);
        if (!fire.light(level, firePos, composition.color())) return InteractionResult.FAIL;
        if (dropped != null) {
            // shrink empties the stack at 1; an item entity with an empty stack removes itself on its next tick,
            // but setItem makes the change show at once
            burning.shrink(1);
            dropped.setItem(burning);
        } else if (player != null && !player.hasInfiniteMaterials()) {
            burning.shrink(1);
        }
        struck(level, player, firePos, context.getItemInHand(), context.getHand());
        if (player instanceof ServerPlayer serverPlayer) {
            DustResearch.noteBurned(serverPlayer, ResearchTabs.entryForItem(burned), composition.color());
            // Alchemical Dust lets out one of its element's effects as it burns (Features/Magic/Awakening)
            DustElement alchemical = AlchemicalDust.of(burned);
            if (alchemical != null) Awakening.burnedDust(serverPlayer, firePos, alchemical);
        }
        return InteractionResult.SUCCESS;
    }

    // Clicking with nothing in reach but lava (useOn isn't called when no block is hit)
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        InteractionResult lava = tryBurnLava(level, player, hand);
        return lava != null ? lava : InteractionResult.PASS;
    }

    // If the player is looking at a lava source nearer than any block, lights a fire on it in lava's colour.
    // null: not looking at lava, carry on as normal.
    private @Nullable InteractionResult tryBurnLava(Level level, Player player, InteractionHand hand)
    {
        // Item#getPlayerPOVHitResult: what the player's crosshair hits within reach. SOURCE_ONLY stops at fluid source
        // blocks (not at flowing fluid), so it hits the lava surface rather than the block under it.
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hit.getType() != HitResult.Type.BLOCK) return null;
        BlockPos lavaPos = hit.getBlockPos();
        FluidState fluid = level.getFluidState(lavaPos);
        DustComposition composition = DustCompositions.of(fluid);
        if (composition == null) return null;

        BlockPos firePos = lavaPos.above();
        DustFireBlock fire = (DustFireBlock) DustBlockRegistry.DUST_FIRE.get();
        if (!level.getBlockState(firePos).isAir()) return InteractionResult.FAIL;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (!fire.light(level, firePos, composition.color())) return InteractionResult.FAIL;
        struck(level, player, firePos, player.getItemInHand(hand), hand);
        if (player instanceof ServerPlayer serverPlayer) {
            // The fluid's own block (minecraft:lava) is its research entry
            ResearchEntry entry = ResearchTabs.entryForBlock(fluid.createLegacyBlock());
            DustResearch.noteBurned(serverPlayer, entry, composition.color());
        }
        return InteractionResult.SUCCESS;
    }

    // The first dropped item in the fire's space that has dust in it (or the first one at all, so a dud fizzles)
    private static @Nullable ItemEntity droppedItemToBurn(Level level, BlockPos firePos)
    {
        // getEntitiesOfClass: every entity of that class inside the box (here, the block space the fire goes in)
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(firePos), ItemEntity::isAlive);
        for (ItemEntity item : items) {
            if (DustCompositions.of(item.getItem()) != null) return item;
        }
        return items.isEmpty() ? null : items.getFirst();
    }

    // The offhand item, when the flint and steel is in the main hand
    private static ItemStack offhandToBurn(@Nullable Player player, InteractionHand hand)
    {
        return player != null && hand == InteractionHand.MAIN_HAND ? player.getOffhandItem() : ItemStack.EMPTY;
    }

    // The strike: sound, game event, and a point of durability
    private static void struck(Level level, @Nullable Player player, BlockPos firePos, ItemStack tool, InteractionHand hand)
    {
        level.playSound(null, firePos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
        level.gameEvent(player, GameEvent.BLOCK_PLACE, firePos);
        if (player != null) {
            EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            tool.hurtAndBreak(1, player, slot);
        }
    }

    // Nothing to burn: sparks and a little smoke
    private static void fizzle(Level level, @Nullable Player player, BlockPos pos)
    {
        level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 0.6F, 1.6F);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, 6, 0.2, 0.1, 0.2, 0.01);
        }
    }
}
