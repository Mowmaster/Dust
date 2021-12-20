package com.mowmaster.dust.Block.BlockEntities.DustJar;

import com.mowmaster.dust.Block.BaseBlocks.BaseColoredBlock;
import com.mowmaster.dust.Block.BlockEntities.Pedestal.BasePedestalBlockEntity;
import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import com.mowmaster.dust.Capabilities.Dust.IDustHandler;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.Items.ColorApplicator;
import com.mowmaster.dust.Items.ColoredCrystalDustBase;
import com.mowmaster.dust.Items.Filters.IPedestalFilter;
import com.mowmaster.dust.Items.Tools.IPedestalTool;
import com.mowmaster.dust.Items.Upgrades.Pedestal.IPedestalUpgrade;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.Util.MessageUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Arrays;

import static com.mowmaster.dust.References.Constants.MODID;


public class DustJarBlock extends BaseColoredBlock implements EntityBlock {

    protected final VoxelShape JAR;

    public DustJarBlock(Properties p_152915_) {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR));
        this.JAR = Shapes.or(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 13.0D, 13.0D));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        ItemStack picked = state.getBlock().getCloneItemStack(world, pos, state);
        int getColor = ColorReference.getColorFromStateInt(state);
        return ColorReference.addColorToItemStack(picked,getColor);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return this.JAR;
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {

        if(!p_60504_.isClientSide())
        {
            BlockEntity blockEntity = p_60504_.getBlockEntity(p_60505_);
            if(blockEntity instanceof DustJarBlockEntity)
            {
                DustJarBlockEntity jar = ((DustJarBlockEntity)blockEntity);

                ItemStack itemInHand = p_60506_.getItemInHand(p_60507_);
                ItemStack itemInMainHand = p_60506_.getMainHandItem();
                ItemStack itemInOffHand = p_60506_.getOffhandItem();

                if(itemInMainHand.getItem() instanceof ColorApplicator)
                {
                    int getColor = ColorReference.getColorFromItemStackInt(itemInMainHand);
                    BlockState newState = ColorReference.addColorToBlockState(p_60503_,getColor);
                    p_60504_.setBlock(p_60505_,newState,3);
                    return InteractionResult.SUCCESS;
                }
                else if(itemInOffHand.getItem() instanceof ColorApplicator)
                {
                    int getColor = ColorReference.getColorFromItemStackInt(itemInOffHand);
                    BlockState newState = ColorReference.addColorToBlockState(p_60503_,getColor);
                    p_60504_.setBlock(p_60505_,newState,3);
                    return InteractionResult.SUCCESS;
                }
                else if(itemInHand.getItem() instanceof ColoredCrystalDustBase)
                {
                    //System.out.println("THIS IS DUST");
                    if(jar.hasDust())
                    {
                        //System.out.println("HAS DUST");
                        if(jar.canAcceptDust(DustMagic.getDustMagicInItemStack(itemInHand)))
                        {
                            if(jar.addDust(DustMagic.getDustMagicInItemStack(itemInHand), IDustHandler.DustAction.SIMULATE)>0)
                            {
                                itemInHand.shrink(jar.addDust(DustMagic.getDustMagicInItemStack(itemInHand), IDustHandler.DustAction.EXECUTE));
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                    else
                    {
                        if(jar.addDust(DustMagic.getDustMagicInItemStack(itemInHand), IDustHandler.DustAction.SIMULATE)>0)
                        {
                            itemInHand.shrink(jar.addDust(DustMagic.getDustMagicInItemStack(itemInHand), IDustHandler.DustAction.EXECUTE));
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
                else if(itemInMainHand.isEmpty())
                {
                    if(p_60506_.isCrouching())
                    {
                        if(jar.hasDust())
                        {
                            DustMagic magic = jar.getStoredDust();
                            MessageUtils.messagePlayerChatWithAppend(p_60506_,ChatFormatting.WHITE,MODID + ".dust_in_jar", Arrays.asList(ColorReference.getColorName(magic.getDustColor()),": ",""+magic.getDustAmount()+""));
                        }
                    }
                }

            }

        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return DeferredBlockEntityTypes.DUST_JAR.get().create(pos, state);
    }

    /*@Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : (level0, pos, state0, blockEntity) -> ((BasePedestalBlockEntity) blockEntity).tick();
    }*/
}
