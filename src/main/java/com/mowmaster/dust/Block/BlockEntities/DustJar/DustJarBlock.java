package com.mowmaster.dust.Block.BlockEntities.DustJar;

import com.mowmaster.dust.Block.BaseBlocks.BaseColoredBlock;
import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import com.mowmaster.dust.Capabilities.Dust.IDustHandler;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.Items.ColorApplicator;
import com.mowmaster.dust.Items.ColoredCrystalDustBase;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.Util.MessageUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mowmaster.dust.References.Constants.MODID;


public class DustJarBlock extends BaseColoredBlock implements EntityBlock {

    protected final VoxelShape JAR;

    public DustJarBlock(Properties p_152915_) {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR));
        this.JAR = Shapes.or(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 13.0D, 13.0D));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return this.JAR;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        ItemStack picked = state.getBlock().getCloneItemStack(world, pos, state);
        int getColor = ColorReference.getColorFromStateInt(state);
        if(world.getBlockEntity(pos) instanceof DustJarBlockEntity)
        {
            DustJarBlockEntity jar = (DustJarBlockEntity)world.getBlockEntity(pos);
            if(picked.hasTag())
            {
                CompoundTag tagToAdd = jar.save(picked.getTag());
                picked.setTag(tagToAdd);
            }
            else
            {
                CompoundTag tagToAdd = jar.save(new CompoundTag());
                picked.setTag(tagToAdd);
            }
        }
        return ColorReference.addColorToItemStack(picked,getColor);
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        if(p_49850_ instanceof Player)
        {
            Player player = ((Player)p_49850_);
            DustMagic getJarMagic = DustMagic.getDustMagicInItemStack(p_49851_);
            int capacity = (p_49851_.getTag().contains(MODID + "_dustCapacity"))?(p_49851_.getTag().getInt(MODID + "_dustCapacity")):(1000);
            int color = ColorReference.getColorFromItemStackInt(p_49851_);
            p_49847_.setBlockAndUpdate(p_49848_,ColorReference.addColorToBlockState(p_49849_,color));
            if(p_49847_.getBlockEntity(p_49848_) instanceof DustJarBlockEntity)
            {
                DustJarBlockEntity jar = (DustJarBlockEntity)p_49847_.getBlockEntity(p_49848_);
                jar.setDustCapacity(capacity);
                jar.addDust(getJarMagic, IDustHandler.DustAction.EXECUTE);
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
        return new ArrayList<>();
    }

    @Override
    public void playerWillDestroy(Level p_56212_, BlockPos p_56213_, BlockState p_56214_, Player p_56215_) {

        if(!p_56212_.isClientSide())
        {
            if (p_56214_.getBlock() instanceof DustJarBlock) {
                if (!p_56212_.isClientSide && !p_56215_.isCreative()) {
                    ItemStack itemstack = new ItemStack(DeferredRegisterTileBlocks.BLOCK_DUST_JAR.get());
                    int getColor = ColorReference.getColorFromStateInt(p_56214_);
                    ItemStack newStack = ColorReference.addColorToItemStack(itemstack,getColor);
                    newStack.setCount(1);
                    if(p_56212_.getBlockEntity(p_56213_) instanceof DustJarBlockEntity)
                    {
                        DustJarBlockEntity jar = (DustJarBlockEntity)p_56212_.getBlockEntity(p_56213_);
                        if(newStack.hasTag())
                        {
                            CompoundTag tagToAdd = jar.save(newStack.getTag());
                            newStack.setTag(tagToAdd);
                        }
                        else
                        {
                            CompoundTag tagToAdd = jar.save(new CompoundTag());
                            newStack.setTag(tagToAdd);
                        }
                    }
                    ItemEntity itementity = new ItemEntity(p_56212_, (double)p_56213_.getX() + 0.5D, (double)p_56213_.getY() + 0.5D, (double)p_56213_.getZ() + 0.5D, newStack);
                    itementity.setDefaultPickUpDelay();
                    p_56212_.addFreshEntity(itementity);
                }
            }
        }
    }

    /*
    Backup Reference
    https://github.com/Lothrazar/Cyclic/blob/trunk/1.18/src/main/java/com/lothrazar/cyclic/block/tank/BlockFluidTank.java#L114

    @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    try {
      IFluidHandlerItem storage = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).orElse(null);
      BlockEntity container = world.getBlockEntity(pos);
      if (storage != null && container != null) {
        IFluidHandler storageTile = container.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
        if (storageTile != null) {
          storageTile.fill(storage.getFluidInTank(0), FluidAction.EXECUTE);
        }
      }
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Error during fill from item ", e);
    }
    //set default state
    state = state.setValue(TANK_ABOVE, false).setValue(TANK_BELOW, false);
    world.setBlockAndUpdate(pos, state);
  }

  @Override
  public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity ent, ItemStack stackTool) {
    super.playerDestroy(world, player, pos, state, ent, stackTool);
    ItemStack tankStack = new ItemStack(this);
    if (ent != null) {
      IFluidHandler fluidInTile = ent.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
      // note a DIFFERENT cap here for the item
      IFluidHandler fluidInStack = tankStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).orElse(null);
      if (fluidInStack != null) {
        //now give
        FluidStack fs = fluidInTile.getFluidInTank(0);
        ((FluidHandlerCapabilityStack) fluidInStack).setFluid(fs);
      }
    }
    if (world.isClientSide == false) {
      world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), tankStack));
    }
  }
     */

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
