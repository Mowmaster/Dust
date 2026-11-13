package com.mowmaster.dust.Features.Spells.blocks;

import com.mowmaster.mowlib.tint.IColorableBlock;
import com.mowmaster.mowlib.tint.ITintHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

// Light's block effect: a floating mote of light you can walk through. It gives full light (normal, white: Minecraft
// can't light in colour without another mod), and the mote and its sparkles are tinted. Its colour is kept by MowLib's
// tint system (IColorableBlock: a CBTintBlockEntity, model tintindex 0), so a dye on it changes it.
public class SpellLightBlock extends Block implements IColorableBlock
{
    public static final int DEFAULT_COLOR = 0xFFF2B0;
    private static final VoxelShape SHAPE = Block.box(5, 5, 5, 11, 11, 11);

    public SpellLightBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public int getDefaultColor(int region)
    {
        return DEFAULT_COLOR;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return Shapes.empty();
    }

    // A dye mixes into its colour (MowLib's dyeing)
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return tryDyeBlock(stack, state, level, pos, player, hit);
    }

    // Places a light at pos in a colour; false if there's no room
    public boolean place(Level level, BlockPos pos, int color)
    {
        if (!level.getBlockState(pos).isAir()) return false;
        level.setBlockAndUpdate(pos, defaultBlockState());
        if (level.getBlockEntity(pos) instanceof ITintHolder tint) tint.setColor(0, color);
        return true;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (random.nextInt(3) != 0) return;
        int color = level.getBlockEntity(pos) instanceof ITintHolder tint ? tint.getTint(0) : DEFAULT_COLOR;
        level.addParticle(new DustParticleOptions(color, 0.5F), pos.getX() + 0.3 + random.nextDouble() * 0.4, pos.getY() + 0.3 + random.nextDouble() * 0.4,
                pos.getZ() + 0.3 + random.nextDouble() * 0.4, 0, 0.01, 0);
    }
}
