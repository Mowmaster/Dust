package com.mowmaster.dust.Blocks.GeneratedBlocks;

import com.mowmaster.dust.Blocks.BaseBlocks.BaseColoredCrystalBlock;
import com.mowmaster.dust.CreativeTabs.DustBlockTabs;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.Random;

import static com.mowmaster.dust.References.Constants.MODID;

public class BaseCrystalNodeBlock extends BaseColoredCrystalBlock
{
    public static final int GROWTH_CHANCE = 5;
    private static final Direction[] DIRECTIONS = Direction.values();

    public BaseCrystalNodeBlock(BlockBehaviour.Properties p_152726_) {
        super(p_152726_);
    }

    public PushReaction getPistonPushReaction(BlockState p_152733_) {
        return PushReaction.DESTROY;
    }

    public void randomTick(BlockState p_152728_, ServerLevel p_152729_, BlockPos p_152730_, Random p_152731_) {
        if (p_152731_.nextInt(5) == 0) {
            Direction direction = DIRECTIONS[p_152731_.nextInt(DIRECTIONS.length)];
            BlockPos blockpos = p_152730_.relative(direction);
            BlockState blockstate = p_152729_.getBlockState(blockpos);
            int red = 3;
            int green = 3;
            int blue = 3;
            Block block = null;

            if (canClusterGrowAtState(blockstate)) {
                blockstate = p_152729_.getBlockState(p_152730_);
                red = blockstate.getValue(COLOR_RED);
                green = blockstate.getValue(COLOR_GREEN);
                blue = blockstate.getValue(COLOR_BLUE);
                block = BaseCrystalClusterBlock.BLK_BLOCK_INERTCLUSTER_SMALL;
            } else if (blockstate.is(BaseCrystalClusterBlock.BLK_BLOCK_INERTCLUSTER_SMALL) && blockstate.getValue(BaseCrystalClusterBlock.FACING) == direction) {
                red = blockstate.getValue(COLOR_RED);
                green = blockstate.getValue(COLOR_GREEN);
                blue = blockstate.getValue(COLOR_BLUE);
                block = BaseCrystalClusterBlock.BLK_BLOCK_INERTCLUSTER_MEDIUM;
            } else if (blockstate.is(BaseCrystalClusterBlock.BLK_BLOCK_INERTCLUSTER_MEDIUM) && blockstate.getValue(BaseCrystalClusterBlock.FACING) == direction) {
                red = blockstate.getValue(COLOR_RED);
                green = blockstate.getValue(COLOR_GREEN);
                blue = blockstate.getValue(COLOR_BLUE);
                block = BaseCrystalClusterBlock.BLK_BLOCK_INERTCLUSTER_LARGE;
            } else if (blockstate.is(BaseCrystalClusterBlock.BLK_BLOCK_INERTCLUSTER_LARGE) && blockstate.getValue(BaseCrystalClusterBlock.FACING) == direction) {
                red = blockstate.getValue(COLOR_RED);
                green = blockstate.getValue(COLOR_GREEN);
                blue = blockstate.getValue(COLOR_BLUE);
                block = BaseCrystalClusterBlock.BLK_BLOCK_INERTCLUSTER_FULL;
            }

            if (block != null) {
                BlockState blockstate1 = block.defaultBlockState().setValue(BaseCrystalClusterBlock.FACING, direction).setValue(COLOR_RED,red).setValue(COLOR_GREEN,green).setValue(COLOR_BLUE,blue).setValue(BaseCrystalClusterBlock.WATERLOGGED, Boolean.valueOf(blockstate.getFluidState().getType() == Fluids.WATER));
                p_152729_.setBlockAndUpdate(blockpos, blockstate1);
            }

        }
    }

    public static boolean canClusterGrowAtState(BlockState p_152735_) {
        return p_152735_.isAir() || p_152735_.is(Blocks.WATER) && p_152735_.getFluidState().getAmount() == 8;
    }



    private static final ResourceLocation RES_BLOCK_INERTNODE = new ResourceLocation(MODID, "block_inertnode");
    public static final Block BLK_BLOCK_INERTNODE = new BaseCrystalNodeBlock(BlockBehaviour.Properties.of(Material.AMETHYST).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()).setRegistryName(RES_BLOCK_INERTNODE);
    public static final Item ITM_BLOCK_INERTNODE = new BlockItem(BLK_BLOCK_INERTNODE, new Item.Properties().tab(DustBlockTabs.TAB_BLOCKS)){}.setRegistryName(RES_BLOCK_INERTNODE);

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(ITM_BLOCK_INERTNODE);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(BLK_BLOCK_INERTNODE);
    }

    public static void handleItemColors(ColorHandlerEvent.Item event)
    {
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1) {return ColorReference.getColorFromItemStackInt(itemstack);} else {return -1;}},ITM_BLOCK_INERTNODE);
    }

    public static void handleBlockColors(ColorHandlerEvent.Block event)
    {
        event.getBlockColors().register((blockstate, blockReader, blockPos, tintIndex) -> {if (tintIndex == 1) {return ColorReference.getColor(Arrays.asList(blockstate.getValue(COLOR_RED),blockstate.getValue(COLOR_GREEN),blockstate.getValue(COLOR_BLUE)));} else {return -1;}},BLK_BLOCK_INERTNODE);
    }
}
