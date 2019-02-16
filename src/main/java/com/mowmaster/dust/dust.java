package com.mowmaster.dust;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Reference.MODID, bus = Bus.MOD)
@Mod(Reference.MODID)
public class dust
{
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
        @Override
        public int getBurnTime(ItemStack itemBlock)
        {
            return 16000;
        }

    }.setRegistryName(RES_STONE_RED);
}