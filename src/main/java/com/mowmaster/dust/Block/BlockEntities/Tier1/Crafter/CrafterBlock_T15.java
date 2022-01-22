package com.mowmaster.dust.Block.BlockEntities.Tier1.Crafter;

import com.mowmaster.dust.Block.BlockEntities.DustJar.DustJarBlockItem;
import com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15.ScrollCrafterBlockEntity_T15;
import com.mowmaster.dust.Block.BlockEntities.Tier1.Tier1BaseBlock;
import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import com.mowmaster.dust.Capabilities.Dust.IDustHandler;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.Items.ColoredCrystalDustBase;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.Util.MessageUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Arrays;

import static com.mowmaster.dust.References.Constants.MODID;
/*
public class CrafterBlock_T15 extends Tier1BaseBlock {


    public static final DirectionProperty SIDED_ROTATION_4 = BlockStateProperties.HORIZONTAL_FACING;
    protected final VoxelShape TABLE;

    public CrafterBlock_T15(Properties p_152915_) {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR).setValue(SIDED_ROTATION_4, Direction.NORTH));
        this.TABLE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 4.0D, 12.0D, 4.0D),
                Block.box(12.0D, 0.0D, 12.0D, 16.0D, 12.0D, 16.0D),
                Block.box(0.0D, 0.0D, 12.0D, 4.0D, 12.0D, 16.0D),
                Block.box(12.0D, 0.0D, 0.0D, 16.0D, 12.0D, 4.0D),
                Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return this.TABLE;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        ItemStack picked = state.getBlock().getCloneItemStack(world, pos, state);
        int getColor = ColorReference.getColorFromStateInt(state);
        return ColorReference.addColorToItemStack(picked,getColor);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
        LevelAccessor levelaccessor = p_48689_.getLevel();
        BlockPos blockpos = p_48689_.getClickedPos();
        Direction direction = p_48689_.getClickedFace();
        BlockState blockstate = levelaccessor.getBlockState(p_48689_.getClickedPos().relative(direction.getOpposite()));
        int getColor = ColorReference.getColorFromStateInt(blockstate);
        return blockstate.is(this) &&
                blockstate.getValue(SIDED_ROTATION_4) == direction
                ?
                ColorReference.addColorToBlockState(this.defaultBlockState(),getColor).setValue(SIDED_ROTATION_4, p_48689_.getHorizontalDirection())
                :
                ColorReference.addColorToBlockState(this.defaultBlockState(),getColor).setValue(SIDED_ROTATION_4, p_48689_.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState p_152033_, Rotation p_152034_) {
        return p_152033_.setValue(SIDED_ROTATION_4, p_152034_.rotate(p_152033_.getValue(SIDED_ROTATION_4)));
    }

    @Override
    public BlockState mirror(BlockState p_152030_, Mirror p_152031_) {
        return p_152030_.rotate(p_152031_.getRotation(p_152030_.getValue(SIDED_ROTATION_4)));
    }

    //Left Click Action
    @Override
    public void attack(BlockState p_60499_, Level p_60500_, BlockPos p_60501_, Player p_60502_) {
        if(!p_60500_.isClientSide())
        {
            BlockEntity blockEntity = p_60500_.getBlockEntity(p_60501_);
            if(blockEntity instanceof CrafterBlockEntity_T15 crafter)
            {
                ItemStack itemInHand = p_60502_.getMainHandItem();
                ItemStack itemInOffHand = p_60502_.getOffhandItem();
                ItemStack itemInMainHand = p_60502_.getMainHandItem();

                if(!p_60502_.isCrouching())
                {
                    if(crafter.getLastUsedSlot()>0)
                    {
                        ItemHandlerHelper.giveItemToPlayer(p_60502_,crafter.removeLastItemFromTable());
                    }
                }
            }
        }

        super.attack(p_60499_, p_60500_, p_60501_, p_60502_);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {

        if(!p_60504_.isClientSide())
        {
            ItemStack itemInHand = p_60506_.getItemInHand(p_60507_);
            ItemStack itemInMainHand = p_60506_.getMainHandItem();

            if(p_60504_.getBlockEntity(p_60505_) instanceof CrafterBlockEntity_T15 table)
            {
                if(!table.isFullyRepaired()) return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
                else if(table.isItemAllowedInTable(itemInMainHand))
                {
                    if(table.addItemInTable(itemInMainHand,true).getCount() < itemInMainHand.getCount())
                    {
                        if(!itemInMainHand.isEmpty())
                        {
                            ItemStack stackReturned = table.addItemInTable(itemInMainHand,false);
                            itemInMainHand.shrink(itemInMainHand.getCount()-stackReturned.getCount());
                        }
                        else table.addItemInTable(itemInMainHand,false);
                    }
                }
            }
        }
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(p_60515_.getBlock() != p_60518_.getBlock())
        {
            BlockEntity blockEntity = p_60516_.getBlockEntity(p_60517_);
            if(blockEntity instanceof CrafterBlockEntity_T15 crafter) {
                crafter.dropInventoryItems(p_60516_,p_60517_);
                crafter.dropInventoryItemsPrivate(p_60516_,p_60517_);
            }
            super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_152043_) {
        p_152043_.add(SIDED_ROTATION_4, COLOR_RED, COLOR_GREEN, COLOR_BLUE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return DeferredBlockEntityTypes.CRAFTER_T15.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : (level0, pos, state0, blockEntity) -> ((CrafterBlockEntity_T15) blockEntity).tick();
    }
}*/
