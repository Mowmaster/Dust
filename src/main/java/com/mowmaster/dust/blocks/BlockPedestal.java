package com.mowmaster.dust.blocks;

import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

import static com.mowmaster.dust.references.Reference.MODID;

public class BlockPedestal extends Block {

    public BlockPedestal(Properties builder)
    {
        super(builder);
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

    private static final ResourceLocation RESLOC_PEDESTAL_STONE = new ResourceLocation(MODID, "pedestal/pedestal_stone");

    public static final Block BLOCK_PEDESTAL_STONE = new BlockPedestal(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE)).setRegistryName(RESLOC_PEDESTAL_STONE);

    public static final Item ITEM_PEDESTAL_STONE = new BlockItem(BLOCK_PEDESTAL_STONE, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)) {}.setRegistryName(RESLOC_PEDESTAL_STONE);

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
