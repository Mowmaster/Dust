package com.mowmaster.dust.blocks;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.item.ItemCrystalWrench;
import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeBase;
import com.mowmaster.dust.tiles.TilePedestal;
import com.mowmaster.dust.tiles.TileTrap;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
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

public class BlockTrap extends Block implements IWaterLoggable {


    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape BLOCK = VoxelShapes.or(Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D));

    public BlockTrap(Properties builder)
    {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, Boolean.valueOf(false)));
    }

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

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        PlayerEntity player = context.getPlayer();

        if(player.isInWater())
        {
            return this.getDefaultState().with(WATERLOGGED, Boolean.valueOf(true));
        }
        else return this.getDefaultState().with(WATERLOGGED, Boolean.valueOf(false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BLOCK;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.or(Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D));
    }

    /*Directly From CactusBlock Code*/
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileTrap)
        {
            TileTrap tileTrap = (TileTrap) tileentity;

            if(!worldIn.isRemote)
            {
                if(this.equals(BLOCKTRAPMOB))
                {
                    if(entityIn instanceof MobEntity)
                    {
                        ((MobEntity) entityIn).addPotionEffect(tileTrap.getTrapEffect());
                    }
                }
                else
                {
                    if(entityIn instanceof PlayerEntity)
                    {
                        ((PlayerEntity) entityIn).addPotionEffect(tileTrap.getTrapEffect());
                    }
                }
            }
        }
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileTrap();
    }

    @Deprecated
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);
        if (rand.nextInt(5) == 0) {
            worldIn.addParticle(ParticleTypes.ENCHANT, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.25D, (double)pos.getZ() + 0.5D, d0, d1, d2);
        }
    }

    private static final ResourceLocation TRAPPLAYER = new ResourceLocation(MODID, "trapplayer");
    private static final ResourceLocation TRAPMOB = new ResourceLocation(MODID, "trapmob");

    public static final Block BLOCKTRAPPLAYER = new BlockTrap(Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(TRAPPLAYER);
    public static final Block BLOCKTRAPMOB = new BlockTrap(Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(TRAPMOB);

    public static final Item ITEMTRAPPLAYER = new BlockItem(BLOCKTRAPPLAYER, new Item.Properties().group(dust.BLOCK_GROUP)) {}.setRegistryName(TRAPPLAYER);
    public static final Item ITEMTRAPMOB = new BlockItem(BLOCKTRAPMOB, new Item.Properties().group(dust.BLOCK_GROUP)) {}.setRegistryName(TRAPMOB);

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(ITEMTRAPPLAYER);
        event.getRegistry().register(ITEMTRAPMOB);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(BLOCKTRAPPLAYER);
        event.getRegistry().register(BLOCKTRAPMOB);
    }


}
