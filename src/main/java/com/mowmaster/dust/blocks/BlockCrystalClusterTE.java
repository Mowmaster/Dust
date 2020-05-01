package com.mowmaster.dust.blocks;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.Random;

import static com.mowmaster.dust.references.Reference.MODID;

public class BlockCrystalClusterTE extends DirectionalBlock implements IWaterLoggable {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    protected static final VoxelShape CUP = VoxelShapes.or(Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D));
    protected static final VoxelShape CDOWN = VoxelShapes.or(Block.makeCuboidShape(5.0D, 16.0D, 5.0D, 11.0D, 6.0D, 11.0D));
    //height goes in the -z direction
    protected static final VoxelShape CNORTH = VoxelShapes.or(Block.makeCuboidShape(5.0D, 5.0D, 6.0D, 11.0D, 11.0D, 16.0D));
    //height goes in the +x direction
    protected static final VoxelShape CEAST = VoxelShapes.or(Block.makeCuboidShape(0.0D, 5.0D, 5.0D, 10.0D, 11.0D, 11.0D));
    protected static final VoxelShape CSOUTH = VoxelShapes.or(Block.makeCuboidShape(5.0D, 5.0D, 10.0D, 11.0D, 11.0D, 0.0D));
    protected static final VoxelShape CWEST = VoxelShapes.or(Block.makeCuboidShape(16.0D, 5.0D, 5.0D, 6.0D, 11.0D, 11.0D));



    public BlockCrystalClusterTE(Properties builder)
    {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP).with(WATERLOGGED, Boolean.valueOf(false)).with(LIT, Boolean.valueOf(false)));
    }

    /*public void dropAllItems() {
        for(List<ItemStack> list : this.allInventories) {
            for(int i = 0; i < list.size(); ++i) {
                ItemStack itemstack = list.get(i);
                if (!itemstack.isEmpty()) {
                    this.player.dropItem(itemstack, true, false);
                    list.set(i, ItemStack.EMPTY);
                }
            }
        }

    }*/

    @Override
    public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, IFluidState fluidStateIn) {
        if (!state.get(BlockStateProperties.WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
            if (!worldIn.isRemote()) {
                worldIn.setBlockState(pos, state.with(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 3);
                worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public Fluid pickupFluid(IWorld worldIn, BlockPos pos, BlockState state) {
        if (state.get(BlockStateProperties.WATERLOGGED)) {
            worldIn.setBlockState(pos, state.with(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)), 3);
            return Fluids.WATER;
        } else {
            return Fluids.EMPTY;
        }
    }

    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return !state.get(WATERLOGGED);
    }

    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    public int getLightValue(BlockState state) {
        return state.get(LIT) ? super.getLightValue(state) : 0;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        switch(state.get(FACING)) {
            case UP:
            default:
                return (CUP);
            case DOWN:
                return (CDOWN);
            case NORTH:
                return (CNORTH);
            case EAST:
                return (CEAST);
            case SOUTH:
                return (CSOUTH);
            case WEST:
                return (CWEST);
        }
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via IBlockState#withRotation(Rotation) whenever possible. Implementing/overriding is
     * fine.
     */
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via IBlockState#withMirror(Mirror) whenever possible. Implementing/overriding is fine.
     */
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.with(FACING, mirrorIn.mirror(state.get(FACING)));
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        Direction direction = context.getFace();
        BlockState blockstate = context.getWorld().getBlockState(context.getPos().offset(direction.getOpposite()));
        return blockstate.getBlock() == this && blockstate.get(FACING) == direction ? this.getDefaultState().with(FACING, direction.getOpposite()).with(WATERLOGGED, Boolean.valueOf(false)).with(LIT, Boolean.valueOf(false)) : this.getDefaultState().with(FACING, direction).with(WATERLOGGED, Boolean.valueOf(false)).with(LIT, Boolean.valueOf(false));
    }


    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        if(!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileCrystalCluster) {
                TileCrystalCluster tilePedestal = (TileCrystalCluster) tileEntity;

                if(player.isCrouching())
                {
                    if (player.getHeldItemMainhand().isEmpty())
                    {

                    }
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return (state.get(LIT))?(15):(0);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING,WATERLOGGED,LIT);
    }

    /*private int getRedstoneLevel(World worldIn, BlockPos pos)
    {
        int hasItem=0;
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof TileCrystalCluster) {
            TileCrystalCluster pedestal = (TileCrystalCluster) tileEntity;
            int counter = pedestal.getItemInPedestal().getCount();
            if(counter<=0) {hasItem=0;}
            else if(counter>0 && counter<=1) {hasItem=1;}
            else if(counter>1 && counter<=4) {hasItem=2;}//1-4
            else if(counter>4 && counter<=9) {hasItem=3;}//4-9
            else if(counter>9 && counter<=14) {hasItem=4;}//9-14
            else if(counter>14 && counter<=19) {hasItem=5;}//14-19
            else if(counter>19 && counter<=24) {hasItem=6;}//19-24
            else if(counter>24 && counter<=29) {hasItem=7;}//24-29
            else if(counter>29 && counter<=34) {hasItem=8;}//29-34
            else if(counter>34 && counter<=39) {hasItem=9;}//34-39
            else if(counter>39 && counter<=44) {hasItem=10;}//39-44
            else if(counter>44 && counter<=49) {hasItem=11;}//44-49
            else if(counter>49 && counter<=54) {hasItem=12;}//49-54
            else if(counter>54 && counter<=59) {hasItem=13;}//54-59
            else if(counter>59 && counter<=63) {hasItem=14;}//59-63
            else if(counter>63) {hasItem=15;}
        }
        return hasItem;
    }*/

    /*@Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return blockState.getWeakPower(blockAccess,pos,side);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return getRedstoneLevel(worldIn,pos);
    }*/


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileCrystalCluster();
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via IBlockState...getRenderType() whenever possible. Implementing/overriding is fine.
     */
    @Deprecated
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        //Direction direction = stateIn.get(FACING);
        double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);
        if (rand.nextInt(5) == 0) {
            //worldIn.addParticle(ParticleTypes.END_ROD, d0 + (double)direction.getXOffset() * d3, d1 + (double)direction.getYOffset() * d3, d2 + (double)direction.getZOffset() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D);
        }
    }

    private static final ResourceLocation R_CLUSTER = new ResourceLocation(MODID, "crystal/crystalcluster");

    public static final Block B_CLUSTER = new BlockCrystalClusterTE(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(R_CLUSTER);

    public static final Item I_CLUSTER = new BlockItem(B_CLUSTER, new Item.Properties().group(dust.BLOCK_GROUP)) {}.setRegistryName(R_CLUSTER);

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(I_CLUSTER);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(B_CLUSTER);
    }

}