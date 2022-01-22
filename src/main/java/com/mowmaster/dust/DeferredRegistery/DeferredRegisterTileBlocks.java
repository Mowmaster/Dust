package com.mowmaster.dust.DeferredRegistery;

import com.mowmaster.dust.Block.BlockEntities.CrystalCluster.EffectCrystalClusterBlock;
import com.mowmaster.dust.Block.BlockEntities.CustomDustBlock.CustomPowderedBlock;
import com.mowmaster.dust.Block.BlockEntities.DustJar.DustJarBlock;
import com.mowmaster.dust.Block.BlockEntities.DustJar.DustJarBlockItem;
import com.mowmaster.dust.Block.BlockEntities.Pedestal.BasePedestalBlock;
//import com.mowmaster.dust.Block.BlockEntities.Tier1.Crafter.CrafterBlock_T15;
import com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.Crusher.CrusherBlock_T15;
import com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.Furnaces.BlastFurnace.BlastFurnaceBlock_T15;
import com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.Furnaces.Furnace.SmelterFurnaceBlock_T15;
import com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.Furnaces.Smoker.SmokerFurnaceBlock_T15;
import com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15.ScrollCrafterBlock_T15;
import com.mowmaster.dust.CreativeTabs.DustBlockTabs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.mowmaster.dust.References.Constants.MODID;

public class DeferredRegisterTileBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,MODID);

    public static final RegistryObject<Block> BLOCK_PEDESTAL = registerBlock("block_pedestal",
            () -> new BasePedestalBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> BLOCK_POWDERED_DUST = registerBlock("block_powdered_dust",
            () -> new CustomPowderedBlock(BlockBehaviour.Properties.of(Material.SAND).strength(0.25F).sound(SoundType.SAND).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> BLOCK_CRYSTAL_CLUSTER = registerBlock("block_crystal_cluster",
            () -> new EffectCrystalClusterBlock(BlockBehaviour.Properties.of(Material.AMETHYST).strength(2.0F).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> BLOCK_DUST_JAR = registerBlockJar("block_dust_jar",
            () -> new DustJarBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(0.25F).sound(SoundType.GLASS)));

    public static final RegistryObject<Block> BLOCK_CRAFTER_SCROLL_T15 = registerBlock("block_crafter_scroll_t15",
            () -> new ScrollCrafterBlock_T15(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> BLOCK_FURNACE_BLAST_T15 = registerBlock("block_furnace_blast_t15",
            () -> new BlastFurnaceBlock_T15(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> BLOCK_FURNACE_SMOKER_T15 = registerBlock("block_furnace_smoker_t15",
            () -> new SmokerFurnaceBlock_T15(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> BLOCK_FURNACE_SMELTER_T15 = registerBlock("block_furnace_smelter_t15",
            () -> new SmelterFurnaceBlock_T15(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> BLOCK_CRUSHER_T15 = registerBlock("block_crusher_t15",
            () -> new CrusherBlock_T15(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).sound(SoundType.STONE)));

    /*public static final RegistryObject<Block> BLOCK_CRAFTER_T15 = registerBlock("block_crafter_t15",
            () -> new CrafterBlock_T15(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).sound(SoundType.STONE)));
*/







    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        DeferredRegisterItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(DustBlockTabs.TAB_BLOCKS)));
    }

    private static <T extends Block> RegistryObject<T> registerBlockJar(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerJarBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerJarBlockItem(String name, RegistryObject<T> block) {
        DeferredRegisterItems.ITEMS.register(name, () -> new DustJarBlockItem(block.get(),
                new Item.Properties().tab(DustBlockTabs.TAB_BLOCKS)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
