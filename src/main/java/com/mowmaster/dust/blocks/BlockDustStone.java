package com.mowmaster.dust.blocks;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class BlockDustStone extends Block
{
    public BlockDustStone(Properties builder) {
        super(builder);
    }

    public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
        return this;
    }

    private static final ResourceLocation RES_STONE_RED = new ResourceLocation(Reference.MODID, "stonered");

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(STONE_RED_ITEM);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(STONE_RED);
    }

    public static final Block STONE_RED = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLACK)
            .hardnessAndResistance(5.0F, 10.0F)
            .sound(SoundType.STONE))
            .setRegistryName(RES_STONE_RED);

    public static final Item STONE_RED_ITEM = new ItemBlock(STONE_RED, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS))
    {


    }.setRegistryName(RES_STONE_RED);


}
