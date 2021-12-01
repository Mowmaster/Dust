package com.mowmaster.dust.Block.GeneratedBlocks;

import com.mowmaster.dust.Block.BaseBlocks.BaseColoredBlock;
import com.mowmaster.dust.Block.BaseBlocks.BaseColoredCrystalBlock;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.*;

import static com.mowmaster.dust.References.Constants.MODID;

public class BaseCrystalClusterBlock extends BaseColoredCrystalBlock implements SimpleWaterloggedBlock
{
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected final VoxelShape northAabb;
    protected final VoxelShape southAabb;
    protected final VoxelShape eastAabb;
    protected final VoxelShape westAabb;
    protected final VoxelShape upAabb;
    protected final VoxelShape downAabb;

    public BaseCrystalClusterBlock(int p_152015_, int p_152016_,Properties p_152726_) {

        super(p_152726_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(FACING, Direction.UP));
        this.upAabb = Block.box((double)p_152016_, 0.0D, (double)p_152016_, (double)(16 - p_152016_), (double)p_152015_, (double)(16 - p_152016_));
        this.downAabb = Block.box((double)p_152016_, (double)(16 - p_152015_), (double)p_152016_, (double)(16 - p_152016_), 16.0D, (double)(16 - p_152016_));
        this.northAabb = Block.box((double)p_152016_, (double)p_152016_, (double)(16 - p_152015_), (double)(16 - p_152016_), (double)(16 - p_152016_), 16.0D);
        this.southAabb = Block.box((double)p_152016_, (double)p_152016_, 0.0D, (double)(16 - p_152016_), (double)(16 - p_152016_), (double)p_152015_);
        this.eastAabb = Block.box(0.0D, (double)p_152016_, (double)p_152016_, (double)p_152015_, (double)(16 - p_152016_), (double)(16 - p_152016_));
        this.westAabb = Block.box((double)(16 - p_152015_), (double)p_152016_, (double)p_152016_, 16.0D, (double)(16 - p_152016_), (double)(16 - p_152016_));
    }

    public VoxelShape getShape(BlockState p_152021_, BlockGetter p_152022_, BlockPos p_152023_, CollisionContext p_152024_) {
        Direction direction = p_152021_.getValue(FACING);
        switch(direction) {
            case NORTH:
                return this.northAabb;
            case SOUTH:
                return this.southAabb;
            case EAST:
                return this.eastAabb;
            case WEST:
                return this.westAabb;
            case DOWN:
                return this.downAabb;
            case UP:
            default:
                return this.upAabb;
        }
    }

    public boolean canSurvive(BlockState p_152026_, LevelReader p_152027_, BlockPos p_152028_) {
        Direction direction = p_152026_.getValue(FACING);
        BlockPos blockpos = p_152028_.relative(direction.getOpposite());
        return p_152027_.getBlockState(blockpos).isFaceSturdy(p_152027_, blockpos, direction);
    }

    public BlockState updateShape(BlockState p_152036_, Direction p_152037_, BlockState p_152038_, LevelAccessor p_152039_, BlockPos p_152040_, BlockPos p_152041_) {
        if (p_152036_.getValue(WATERLOGGED)) {
            p_152039_.getFluidTicks();
            //.scheduleTick(p_152040_, Fluids.WATER, Fluids.WATER.getTickDelay(p_152039_))
        }

        return p_152037_ == p_152036_.getValue(FACING).getOpposite() && !p_152036_.canSurvive(p_152039_, p_152040_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_152036_, p_152037_, p_152038_, p_152039_, p_152040_, p_152041_);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_152019_) {
        LevelAccessor levelaccessor = p_152019_.getLevel();
        BlockPos blockpos = p_152019_.getClickedPos();
        BlockState blockstate = p_152019_.getLevel().getBlockState(p_152019_.getClickedPos());
        if (blockstate.is(this))
        {
            int getColor = ColorReference.getColorFromStateInt(blockstate);
            return ColorReference.addColorToBlockState(this.defaultBlockState(),getColor).setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)).setValue(FACING, p_152019_.getClickedFace());
        }
        else return super.getStateForPlacement(p_152019_);
    }

    public BlockState rotate(BlockState p_152033_, Rotation p_152034_) {
        return p_152033_.setValue(FACING, p_152034_.rotate(p_152033_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_152030_, Mirror p_152031_) {
        return p_152030_.rotate(p_152031_.getRotation(p_152030_.getValue(FACING)));
    }

    public FluidState getFluidState(BlockState p_152045_) {
        return p_152045_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_152045_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_152043_) {
        p_152043_.add(WATERLOGGED, FACING, COLOR_RED, COLOR_GREEN, COLOR_BLUE);
    }

    public PushReaction getPistonPushReaction(BlockState p_152733_) {
        return PushReaction.DESTROY;
    }

    //SOme way to chage the color of the crystals given blocks around it???
    public void randomTick(BlockState p_152728_, ServerLevel p_152729_, BlockPos p_152730_, Random p_152731_) {
        if (p_152731_.nextInt(25) == 0) {
            Direction direction = p_152728_.getValue(FACING);
            BlockState blockstate = p_152729_.getBlockState(p_152730_);
            int getColor = ColorReference.getColorFromStateInt(blockstate);
            Block block = null;
        }
    }

    public VoxelShape getOcclusionShape(BlockState p_154272_, BlockGetter p_154273_, BlockPos p_154274_) {
        return Shapes.empty();
    }

    public VoxelShape getCollisionShape(BlockState p_154285_, BlockGetter p_154286_, BlockPos p_154287_, CollisionContext p_154288_) {
        return Shapes.empty();
    }

    public void entityInside(BlockState p_57270_, Level p_57271_, BlockPos p_57272_, Entity p_57273_) {
        boolean isCreative = false;
        if (p_57273_ instanceof LivingEntity) {
            if(p_57273_ instanceof ServerPlayer)isCreative = ((ServerPlayer) p_57273_).isCreative();

            if(!isCreative)
            {
                p_57273_.makeStuckInBlock(p_57270_, new Vec3((double)0.8F, 0.75D, (double)0.8F));
                if (!p_57271_.isClientSide && (p_57273_.xOld != p_57273_.getX() || p_57273_.zOld != p_57273_.getZ())) {
                    double d0 = Math.abs(p_57273_.getX() - p_57273_.xOld);
                    double d1 = Math.abs(p_57273_.getZ() - p_57273_.zOld);

                    DamageSource CrystalCluster = new DamageSource(MODID + getCrystalDamageType(p_57270_)).damageHelmet().bypassArmor().setScalesWithDifficulty().setExplosion();

                    if (d0 >= (double)0.003F || d1 >= (double)0.003F) {
                        //WORLD.explode(TARGET_ENTITY,DamageSource,ExplosionDamageCalculator,BlockPosX,BlockPosY,BlockPosZ,ExplosionRadius,SpawnFire, Explosion.BlockInteraction);
                        p_57271_.explode(p_57273_,CrystalCluster,new EntityBasedExplosionDamageCalculator(p_57273_),p_57272_.getX()+0.5,p_57272_.getY()+2.0,p_57272_.getZ()+0.5,getExplosionSize(p_57270_),false, Explosion.BlockInteraction.NONE);
                        //Instead of blowing up the full area, just destroy the block walked into
                        p_57271_.setBlock(p_57272_,Blocks.AIR.defaultBlockState(),3);
                        float damage = getExplosionSize(p_57270_);
                        p_57273_.hurt(CrystalCluster,Math.scalb(damage,2));
                    }
                }
            }

        }
    }

    private float getExplosionSize(BlockState state)
    {
        if(state.getBlock().equals(DeferredRegisterBlocks.CRYSTAL_CLUSTER_SMALL.get())) return 1.0f;
        else if(state.getBlock().equals(DeferredRegisterBlocks.CRYSTAL_CLUSTER_MEDIUM.get())) return 2.0f;
        else if(state.getBlock().equals(DeferredRegisterBlocks.CRYSTAL_CLUSTER_LARGE.get())) return 4.0f;
        else if(state.getBlock().equals(DeferredRegisterBlocks.CRYSTAL_CLUSTER_FULL.get())) return 8.0f;
        return 0.0f;
    }

    private String getCrystalDamageType(BlockState state)
    {
        if(state.getBlock() instanceof BaseColoredBlock)
        {

            switch(ColorReference.getColorFromStateInt(state))
            {
                case 2763306: return "death000";
                        case 85: return "death001";
                        case 170: return "death002";
                        case 255: return "death003";
                        case 21760: return "death010";
                        case 21845: return "death011";
                        case 21930: return "death012";
                        case 22015: return "death013";
                        case 43520: return "death020";
                        case 43605: return "death021";
                        case 43690: return "death022";
                        case 43775: return "death023";
                        case 65280: return "death030";
                        case 65365: return "death031";
                        case 65450: return "death032";
                        case 65535: return "death033";
                        case 5570560: return "death100";
                        case 5570645: return "death101";
                        case 5570730: return "death102";
                        case 5570815: return "death103";
                        case 5592320: return "death110";
                        case 5592405: return "death111";
                        case 5592490: return "death112";
                        case 5592575: return "death113";
                        case 5614080: return "death120";
                        case 5614165: return "death121";
                        case 5614250: return "death122";
                        case 5614335: return "death123";
                        case 5635840: return "death130";
                        case 5635925: return "death131";
                        case 5636010: return "death132";
                        case 5636095: return "death133";
                        case 11141120: return "death200";
                        case 11141205: return "death201";
                        case 11141290: return "death202";
                        case 11141375: return "death203";
                        case 11162880: return "death210";
                        case 11162965: return "death211";
                        case 11163050: return "death212";
                        case 11163135: return "death213";
                        case 11184640: return "death220";
                        case 11184725: return "death221";
                        case 11184810: return "death222";
                        case 11184895: return "death223";
                        case 11206400: return "death230";
                        case 11206485: return "death231";
                        case 11206570: return "death232";
                        case 11206655: return "death233";
                        case 16711680: return "death300";
                        case 16711765: return "death301";
                        case 16711850: return "death302";
                        case 16711935: return "death303";
                        case 16733440: return "death310";
                        case 16733525: return "death311";
                        case 16733610: return "death312";
                        case 16733695: return "death313";
                        case 16755200: return "death320";
                        case 16755285: return "death321";
                        case 16755370: return "death322";
                        case 16755455: return "death323";
                        case 16776960: return "death330";
                        case 16777045: return "death331";
                        case 16777130: return "death332";
                        case 16777215: return "death333";
                default: return "deathDefault";
            }
        }

        return "deathReturner";
    }
}
