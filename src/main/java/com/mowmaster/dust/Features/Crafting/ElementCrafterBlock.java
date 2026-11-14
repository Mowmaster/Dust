package com.mowmaster.dust.Features.Crafting;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.StationBlock;
import com.mowmaster.dust.Features.Machines.StationInteractions;
import com.mowmaster.dust.Features.Machines.StationSlot;
import com.mowmaster.mowlib.tint.IColorableBlock;
import com.mowmaster.mowlib.util.CBPixelBox;
import com.mowmaster.mowlib.util.LeftClickCooldown;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

// The Element Crafter (ElementCrafterBlockEntity has the rules):
//   right-click a grid cell with an item          puts the stack in (StationInteractions)
//   right-click a jar with element dust           fills it (only its element, once it has one)
//   right-click a jar with an empty hand          says what's in it; sneaking, clears an empty jar's element
//   left-click a filled grid cell                 takes from it (StationInteractions)
//   left-click anywhere else, with a result       crafts it (sneak: a stack); with no result, it's mined as normal
//   right-click with a dye (not on a grid cell)   dyes it (MowLib's IColorableBlock): the slabs and posts together
// Tint indices (the model): 0 the slabs and 1 the posts (one dye colour for both), 2-5 the jar lids (their element's
// colour). The block entity answers all six (ElementCrafterBlockEntity.getTint); MowLib registers the tint sources.
@EventBusSubscriber(modid = DustReferences.MODID)
public class ElementCrafterBlock extends StationBlock implements IColorableBlock
{
    // One dyeable region: the slabs and posts (tint indices 0 and 1)
    public static final int DYE_REGIONS = 1;
    // The lids' tint indices come after the slabs' and posts'
    public static final int FIRST_LID = 2;

    // The slab-shaped table with its middle columns, and the jars round it
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 12, 0, 16, 16, 16),
            Block.box(4, 0, 4, 12, 12, 12),
            Block.box(6, 4, 0, 10, 11, 4), Block.box(12, 4, 6, 16, 11, 10),
            Block.box(6, 4, 12, 10, 11, 16), Block.box(0, 4, 6, 4, 11, 10));

    public ElementCrafterBlock(Properties properties)
    {
        super(properties, SHAPE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ElementCrafterBlockEntity(pos, state);
    }

    // ---- dyeing (IColorableBlock) ----

    // One tint source per model tint index: MowLib registers this many for the block (MowLibClient), each reading the
    // block entity's getTint. Only region 0 is dyed (IColorableBlock's getTintRegion is 0 wherever you click).
    @Override
    public int getTintRegionCount()
    {
        return FIRST_LID + ElementCrafterLayout.JARS;
    }

    // It has its own block entity (the grid and jars), not MowLib's plain tint block entity
    @Override
    public boolean usesTintBlockEntity()
    {
        return false;
    }

    // The jar under the click, or -1
    private static int jarAt(Level level, BlockPos pos, BlockHitResult hit)
    {
        for (int jar = 0; jar < ElementCrafterLayout.JARS; jar++) {
            if (StationInteractions.inArea(level, pos, hit, ElementCrafterLayout.jar(jar))) return jar;
        }
        return -1;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof ElementCrafterBlockEntity crafter)) return InteractionResult.PASS;
        // Dust on a jar that won't take it (another element's, or full): say why rather than doing nothing
        int jar = jarAt(level, pos, hit);
        DustElement dust = CrystalElements.ofDust(stack);
        if (jar >= 0 && dust != null && !crafter.canFill(jar, stack)) {
            if (!level.isClientSide()) {
                DustElement element = crafter.jarElement(jar);
                player.sendOverlayMessage(element != null && element != dust
                        ? Component.translatable("dust.element_crafter.jar_other", element.name())
                        : Component.translatable("dust.element_crafter.jar_full"));
            }
            return InteractionResult.FAIL;
        }
        // Into the clicked grid cell or jar if it takes it (a dye on a grid cell is an ingredient); otherwise a dye
        // dyes the crafter
        InteractionResult result = StationInteractions.rightClick(stack, level, pos, player, hit);
        if (result != InteractionResult.TRY_WITH_EMPTY_HAND) return result;
        return tryDyeBlock(stack, state, level, pos, player, hit);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof ElementCrafterBlockEntity crafter)) return InteractionResult.PASS;
        int jar = jarAt(level, pos, hit);
        if (jar < 0) return InteractionResult.PASS;
        if (!level.isClientSide()) {
            DustElement element = crafter.jarElement(jar);
            if (player.isShiftKeyDown()) {
                if (crafter.clearJar(jar)) {
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 0.8F, 1.0F);
                    player.sendOverlayMessage(Component.translatable("dust.element_crafter.jar_cleared"));
                }
                else if (element != null) player.sendOverlayMessage(Component.translatable("dust.element_crafter.jar_not_empty"));
            }
            else if (element == null) player.sendOverlayMessage(Component.translatable("dust.element_crafter.jar_unset"));
            else player.sendOverlayMessage(Component.translatable("dust.element_crafter.jar_contents",
                    crafter.jarAmount(jar), ElementCrafterLayout.JAR_CAPACITY, element.name()));
        }
        return InteractionResult.SUCCESS;
    }

    // Left-click crafts. StationInteractions handles a click on a filled grid cell (taking from it), so that's left
    // alone here; anywhere else, while there's a result to craft, the click is cancelled (no mining) on both sides and
    // the server crafts. The client knows there's a result from the synced ghost.
    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event)
    {
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) return;
        Player player = event.getEntity();
        Level level = player.level();
        BlockPos pos = event.getPos();
        if (!(level.getBlockEntity(pos) instanceof ElementCrafterBlockEntity crafter) || !crafter.hasResult()) return;
        HitResult hit = player.pick(player.blockInteractionRange(), 1.0F, false);
        if (!(hit instanceof BlockHitResult blockHit) || !blockHit.getBlockPos().equals(pos)) return;
        StationSlot slot = StationInteractions.slotAt(level, pos, CBPixelBox.pixelInside(blockHit));
        if (slot != null && !slot.contents().get().isEmpty()) return;

        event.setCanceled(true);
        if (level instanceof ServerLevel serverLevel && LeftClickCooldown.ready(player, level)) {
            crafter.craft(serverLevel, player, player.isShiftKeyDown());
        }
    }
}
