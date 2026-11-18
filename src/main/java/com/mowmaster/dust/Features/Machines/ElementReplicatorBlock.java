package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.MagicalFlintAndSteelItem;
import com.mowmaster.mowlib.tint.IColorableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

// The Element Replicator (ElementReplicatorBlockEntity has the rules):
//   right-click the bottom sigil with an item           puts it there to be copied (only an item you've fully worked out)
//   right-click with a Magical Flint and Steel          starts a copy (so do a dispenser firing one, or a fire spell)
//   right-click with an empty hand                      says what a copy takes (red: the jars nearby are short)
//   left-click the bottom sigil / the copy on top       takes it (StationInteractions)
//   right-click with a dye (not on the bottom sigil)    dyes it (MowLib's IColorableBlock): slabs, rails, sigils and posts together
// Tint indices (the model): 0 the slabs, rails and sigils, 1 the posts; one dye colour for both. The block entity answers
// them (getTint); MowLib registers the tint sources.
public class ElementReplicatorBlock extends StationBlock implements IColorableBlock
{
    // One dyeable region: everything tinted (tint indices 0 and 1)
    public static final int DYE_REGIONS = 1;
    private static final int TINT_INDICES = 2;

    // The base slab, the space between the posts (so clicks into it land on the item to copy there), the four posts,
    // the top slab and its rails
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 4, 16),
            Block.box(4, 4, 4, 12, 12, 12),
            Block.box(0.5, 4, 0.5, 3.5, 12, 3.5), Block.box(12.5, 4, 0.5, 15.5, 12, 3.5),
            Block.box(0.5, 4, 12.5, 3.5, 12, 15.5), Block.box(12.5, 4, 12.5, 15.5, 12, 15.5),
            Block.box(0, 12, 0, 16, 15, 16),
            Block.box(0, 15, 0, 2, 16, 16), Block.box(14, 15, 0, 16, 16, 16),
            Block.box(2, 15, 0, 14, 16, 2), Block.box(2, 15, 14, 14, 16, 16));

    public ElementReplicatorBlock(Properties properties)
    {
        super(properties, SHAPE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ElementReplicatorBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (type != DustBlockEntityRegistry.ELEMENT_REPLICATOR.get()) return null;
        BlockEntityTicker<ElementReplicatorBlockEntity> ticker = level.isClientSide() ? ElementReplicatorBlockEntity::clientTick : ElementReplicatorBlockEntity::serverTick;
        return (BlockEntityTicker<T>) ticker;
    }

    // ---- dyeing (IColorableBlock) ----

    // One tint source per model tint index (MowLib registers this many for the block, each reading the block entity's
    // getTint). Only region 0 is dyed (IColorableBlock's getTintRegion is 0 wherever you click).
    @Override
    public int getTintRegionCount()
    {
        return TINT_INDICES;
    }

    // It has its own block entity, not MowLib's plain tint block entity
    @Override
    public boolean usesTintBlockEntity()
    {
        return false;
    }

    // ---- clicking ----

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof ElementReplicatorBlockEntity replicator)) return InteractionResult.PASS;

        if (stack.getItem() instanceof MagicalFlintAndSteelItem) {
            // The client can't see the jars' dust, so it only turns down what it can see; the server has the last word
            String problem = level instanceof ServerLevel serverLevel ? replicator.cannotStart(serverLevel) : replicator.cannotStart();
            if (problem != null) {
                if (!level.isClientSide()) player.sendOverlayMessage(Component.translatable("dust.replicator." + problem));
                return InteractionResult.FAIL;
            }
            if (level instanceof ServerLevel serverLevel) {
                replicator.start(serverLevel);
                stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }
        // Onto the bottom sigil if it takes it (a dye there is something to copy); otherwise a dye dyes the replicator
        InteractionResult result = StationInteractions.rightClick(stack, level, pos, player, hit);
        if (result != InteractionResult.TRY_WITH_EMPTY_HAND) return result;
        return tryDyeBlock(stack, state, level, pos, player, hit);
    }

    // An empty hand: what a copy takes. (Holding something it didn't take, the click is left to that item: a block is
    // placed against it as usual.)
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!player.getMainHandItem().isEmpty() || !(level.getBlockEntity(pos) instanceof ElementReplicatorBlockEntity replicator)) return InteractionResult.PASS;
        if (level instanceof ServerLevel serverLevel) replicator.tellCost(serverLevel, player);
        return InteractionResult.SUCCESS;
    }
}
