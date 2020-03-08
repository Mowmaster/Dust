package com.mowmaster.dust.blocks;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.item.ItemCrystalWrench;
import com.mowmaster.dust.item.pedestalUpgrades.UpgradeBase;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

import java.util.Random;

import static com.mowmaster.dust.references.Reference.MODID;

public class BlockPedestal extends DirectionalBlock {


    /*private static VoxelShape CUP = Block.makeCuboidShape((1-lengthWidth)/2, 0.0D, (1-lengthWidth)/2, 1-((1-lengthWidth)/2), height, 1-((1-lengthWidth)/2));
    private static VoxelShape CDOWN = Block.makeCuboidShape((1-lengthWidth)/2, 1-height, (1-lengthWidth)/2, 1-((1-lengthWidth)/2), 1.0D, 1-((1-lengthWidth)/2));
    private static VoxelShape CNORTH = Block.makeCuboidShape((1-lengthWidth)/2, 1-((1-lengthWidth)/2), 1.0D, 1-((1-lengthWidth)/2), (1-lengthWidth)/2, 1-height);
    private static VoxelShape CSOUTH = Block.makeCuboidShape((1-lengthWidth)/2, 1-((1-lengthWidth)/2), 0.0D, 1-((1-lengthWidth)/2), (1-lengthWidth)/2, height);
    private static VoxelShape CWEST = Block.makeCuboidShape(1.0D, 1-((1-lengthWidth)/2), (1-lengthWidth)/2, 1-height, (1-lengthWidth)/2, 1-((1-lengthWidth)/2));
    private static VoxelShape CEAST = Block.makeCuboidShape(0.0D, 1-((1-lengthWidth)/2), (1-lengthWidth)/2, height, (1-lengthWidth)/2, 1-((1-lengthWidth)/2));*/
    //Can be multiple pieces???
    protected static final VoxelShape CUP = VoxelShapes.or(Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D),
            Block.makeCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 3.0D, 12.0D),
            Block.makeCuboidShape(5.0D, 3.0D, 5.0D, 11.0D, 4.0D, 11.0D),
            Block.makeCuboidShape(4.5D, 4.0D, 4.5D, 11.5D, 5.0D, 11.5D),
            Block.makeCuboidShape(5.0D, 5.0D, 5.0D, 11.0D, 10.0D, 11.0D),
            Block.makeCuboidShape(4.0D, 10.0D, 4.0D, 12.0D, 12.0D, 12.0D));
    protected static final VoxelShape CDOWN = VoxelShapes.or(Block.makeCuboidShape(4.0D, 16.0D, 4.0D, 12.0D, 14.0D, 12.0D),
            Block.makeCuboidShape(5.0D, 14.0D, 5.0D, 11.0D, 9.0D, 11.0D),
            Block.makeCuboidShape(4.5D, 9.0D, 4.5D, 11.5D, 8.0D, 11.5D),
            Block.makeCuboidShape(5.0D, 8.0D, 5.0D, 11.0D, 7.0D, 11.0D),
            Block.makeCuboidShape(4.0D, 6.0D, 4.0D, 12.0D, 5.0D, 12.0D),
            Block.makeCuboidShape(3.0D, 5.0D, 3.0D, 13.0D, 3.0D, 13.0D));
    protected static final VoxelShape CNORTH = VoxelShapes.or(Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D),
            Block.makeCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 3.0D, 12.0D),
            Block.makeCuboidShape(5.0D, 3.0D, 5.0D, 11.0D, 4.0D, 11.0D),
            Block.makeCuboidShape(4.5D, 4.0D, 4.5D, 11.5D, 5.0D, 11.5D),
            Block.makeCuboidShape(5.0D, 5.0D, 5.0D, 11.0D, 10.0D, 11.0D),
            Block.makeCuboidShape(4.0D, 10.0D, 4.0D, 12.0D, 12.0D, 12.0D));
    protected static final VoxelShape CEAST = VoxelShapes.or(Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D),
            Block.makeCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 3.0D, 12.0D),
            Block.makeCuboidShape(5.0D, 3.0D, 5.0D, 11.0D, 4.0D, 11.0D),
            Block.makeCuboidShape(4.5D, 4.0D, 4.5D, 11.5D, 5.0D, 11.5D),
            Block.makeCuboidShape(5.0D, 5.0D, 5.0D, 11.0D, 10.0D, 11.0D),
            Block.makeCuboidShape(4.0D, 10.0D, 4.0D, 12.0D, 12.0D, 12.0D));
    protected static final VoxelShape CSOUTH = VoxelShapes.or(Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D),
            Block.makeCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 3.0D, 12.0D),
            Block.makeCuboidShape(5.0D, 3.0D, 5.0D, 11.0D, 4.0D, 11.0D),
            Block.makeCuboidShape(4.5D, 4.0D, 4.5D, 11.5D, 5.0D, 11.5D),
            Block.makeCuboidShape(5.0D, 5.0D, 5.0D, 11.0D, 10.0D, 11.0D),
            Block.makeCuboidShape(4.0D, 10.0D, 4.0D, 12.0D, 12.0D, 12.0D));
    protected static final VoxelShape CWEST = VoxelShapes.or(Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D),
            Block.makeCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 3.0D, 12.0D),
            Block.makeCuboidShape(5.0D, 3.0D, 5.0D, 11.0D, 4.0D, 11.0D),
            Block.makeCuboidShape(4.5D, 4.0D, 4.5D, 11.5D, 5.0D, 11.5D),
            Block.makeCuboidShape(5.0D, 5.0D, 5.0D, 11.0D, 10.0D, 11.0D),
            Block.makeCuboidShape(4.0D, 10.0D, 4.0D, 12.0D, 12.0D, 12.0D));


    public BlockPedestal(Properties builder)
    {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP));
    }


    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        switch(state.get(FACING)) {
            case UP:
            default:
                return CUP;
            case DOWN:
                return CDOWN;
            case NORTH:
                return CNORTH;
            case EAST:
                return CEAST;
            case SOUTH:
                return CSOUTH;
            case WEST:
                return CWEST;
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
        Direction direction = context.getFace();
        BlockState blockstate = context.getWorld().getBlockState(context.getPos().offset(direction.getOpposite()));
        return blockstate.getBlock() == this && blockstate.get(FACING) == direction ? this.getDefaultState().with(FACING, direction.getOpposite()) : this.getDefaultState().with(FACING, direction);
    }

    /*Directly From CactusBlock Code*/
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        //entityIn.attackEntityFrom(DamageSource.CACTUS, 1.0F);
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        if(!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TilePedestal) {
                TilePedestal tilePedestal = (TilePedestal) tileEntity;

                if(player.isCrouching())
                {
                    if (player.getHeldItemMainhand().isEmpty())
                    {
                        if (tilePedestal.hasCoin()) {
                            player.inventory.addItemStackToInventory(tilePedestal.removeCoin());
                        }
                    }
                }
                else if(!tilePedestal.hasCoin())
                {
                    //player.getHeldItemMainhand().getItem() instanceof ItemCoin ||
                    if(player.getHeldItemMainhand().getItem() instanceof UpgradeBase)
                    {
                        if(tilePedestal.addCoin(player.getHeldItemMainhand()))
                        {
                            player.getHeldItemMainhand().shrink(1);
                        }
                    }
                    else if (player.getHeldItemMainhand().isEmpty()) {
                        if (tilePedestal.hasItem()) {
                            player.inventory.addItemStackToInventory(tilePedestal.removeItem());
                        }
                    }
                    else
                    {
                        int availableSpace = tilePedestal.canAcceptItems(player.getHeldItemMainhand());
                        if(availableSpace>0)
                        {
                            if (tilePedestal.addItem(player.getHeldItemMainhand()))
                            {
                                player.getHeldItemMainhand().shrink(availableSpace);
                                return ActionResultType.SUCCESS;
                            }
                        }
                        else return ActionResultType.SUCCESS;
                    }
                }

                else if(player.getHeldItemMainhand().getItem() instanceof ItemCrystalWrench)
                {
                    return ActionResultType.FAIL;
                }

                else if (player.getHeldItemMainhand().isEmpty()) {
                    if (tilePedestal.hasItem()) {
                        player.inventory.addItemStackToInventory(tilePedestal.removeItem());
                    }
                }
                else
                {
                    int availableSpace = tilePedestal.canAcceptItems(player.getHeldItemMainhand());
                    if(availableSpace>0)
                    {
                        if (tilePedestal.addItem(player.getHeldItemMainhand()))
                        {
                            player.getHeldItemMainhand().shrink(availableSpace);
                            return ActionResultType.SUCCESS;
                        }
                    }
                    else return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TilePedestal();
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



    private static final ResourceLocation RESLOC_PEDESTAL_STONE = new ResourceLocation(MODID, "pedestal/pedestal_stone");

    public static final Block BLOCK_PEDESTAL_STONE = new BlockPedestal(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_PEDESTAL_STONE);

    public static final Item ITEM_PEDESTAL_STONE = new BlockItem(BLOCK_PEDESTAL_STONE, new Item.Properties().group(dust.itemGroup)) {}.setRegistryName(RESLOC_PEDESTAL_STONE);

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(ITEM_PEDESTAL_STONE);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(BLOCK_PEDESTAL_STONE);
    }


}
