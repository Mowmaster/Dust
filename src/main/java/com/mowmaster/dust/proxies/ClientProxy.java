package com.mowmaster.dust.proxies;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ClientProxy extends CommonProxy
{
    @Override
    @SideOnly(Side.CLIENT)
    public void PreInit()
    {
        ItemRegistry.registerRenders();
        BlockRegistry.registerRenders();
    }

    @Override
    public void registerModelBakeryVarients()
    {
        ModelBakery.registerItemVariants(ItemRegistry.crystal,
                new ResourceLocation(Reference.MODID,"crystal_red"),
                new ResourceLocation(Reference.MODID,"crystal_blue"),
                new ResourceLocation(Reference.MODID,"crystal_yellow"),
                new ResourceLocation(Reference.MODID,"crystal_purple"),
                new ResourceLocation(Reference.MODID,"crystal_green"),
                new ResourceLocation(Reference.MODID,"crystal_orange"),
                new ResourceLocation(Reference.MODID,"crystal_white"),
                new ResourceLocation(Reference.MODID,"crystal_black"),
                new ResourceLocation(Reference.MODID,"crystal_clear")
        );

        ModelBakery.registerItemVariants(ItemRegistry.dust,
                new ResourceLocation(Reference.MODID,"dust_red"),
                new ResourceLocation(Reference.MODID,"dust_blue"),
                new ResourceLocation(Reference.MODID,"dust_yellow"),
                new ResourceLocation(Reference.MODID,"dust_purple"),
                new ResourceLocation(Reference.MODID,"dust_green"),
                new ResourceLocation(Reference.MODID,"dust_orange"),
                new ResourceLocation(Reference.MODID,"dust_white"),
                new ResourceLocation(Reference.MODID,"dust_black"),
                new ResourceLocation(Reference.MODID,"dust_stone")
        );
        ModelBakery.registerItemVariants(ItemRegistry.bit,
                new ResourceLocation(Reference.MODID,"bit_red"),
                new ResourceLocation(Reference.MODID,"bit_blue"),
                new ResourceLocation(Reference.MODID,"bit_yellow"),
                new ResourceLocation(Reference.MODID,"bit_purple"),
                new ResourceLocation(Reference.MODID,"bit_green"),
                new ResourceLocation(Reference.MODID,"bit_orange"),
                new ResourceLocation(Reference.MODID,"bit_white"),
                new ResourceLocation(Reference.MODID,"bit_black")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockRegistry.orered),
                new ResourceLocation(Reference.MODID, "ore_red_ore"),
                new ResourceLocation(Reference.MODID, "ore_red_five"),
                new ResourceLocation(Reference.MODID, "ore_red_four"),
                new ResourceLocation(Reference.MODID, "ore_red_three"),
                new ResourceLocation(Reference.MODID, "ore_red_two"),
                new ResourceLocation(Reference.MODID, "ore_red_one"),
                new ResourceLocation(Reference.MODID, "ore_red_base")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockRegistry.oreblue),
                new ResourceLocation(Reference.MODID, "ore_blue_ore"),
                new ResourceLocation(Reference.MODID, "ore_blue_five"),
                new ResourceLocation(Reference.MODID, "ore_blue_four"),
                new ResourceLocation(Reference.MODID, "ore_blue_three"),
                new ResourceLocation(Reference.MODID, "ore_blue_two"),
                new ResourceLocation(Reference.MODID, "ore_blue_one"),
                new ResourceLocation(Reference.MODID, "ore_blue_base")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockRegistry.oreyellow),
                new ResourceLocation(Reference.MODID, "ore_yellow_ore"),
                new ResourceLocation(Reference.MODID, "ore_yellow_five"),
                new ResourceLocation(Reference.MODID, "ore_yellow_four"),
                new ResourceLocation(Reference.MODID, "ore_yellow_three"),
                new ResourceLocation(Reference.MODID, "ore_yellow_two"),
                new ResourceLocation(Reference.MODID, "ore_yellow_one"),
                new ResourceLocation(Reference.MODID, "ore_yellow_base")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockRegistry.orepurple),
                new ResourceLocation(Reference.MODID, "ore_purple_ore"),
                new ResourceLocation(Reference.MODID, "ore_purple_five"),
                new ResourceLocation(Reference.MODID, "ore_purple_four"),
                new ResourceLocation(Reference.MODID, "ore_purple_three"),
                new ResourceLocation(Reference.MODID, "ore_purple_two"),
                new ResourceLocation(Reference.MODID, "ore_purple_one"),
                new ResourceLocation(Reference.MODID, "ore_purple_base")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockRegistry.oreorange),
                new ResourceLocation(Reference.MODID, "ore_orange_ore"),
                new ResourceLocation(Reference.MODID, "ore_orange_five"),
                new ResourceLocation(Reference.MODID, "ore_orange_four"),
                new ResourceLocation(Reference.MODID, "ore_orange_three"),
                new ResourceLocation(Reference.MODID, "ore_orange_two"),
                new ResourceLocation(Reference.MODID, "ore_orange_one"),
                new ResourceLocation(Reference.MODID, "ore_orange_base")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockRegistry.oregreen),
                new ResourceLocation(Reference.MODID, "ore_green_ore"),
                new ResourceLocation(Reference.MODID, "ore_green_five"),
                new ResourceLocation(Reference.MODID, "ore_green_four"),
                new ResourceLocation(Reference.MODID, "ore_green_three"),
                new ResourceLocation(Reference.MODID, "ore_green_two"),
                new ResourceLocation(Reference.MODID, "ore_green_one"),
                new ResourceLocation(Reference.MODID, "ore_green_base")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockRegistry.orewhite),
                new ResourceLocation(Reference.MODID, "ore_white_ore"),
                new ResourceLocation(Reference.MODID, "ore_white_five"),
                new ResourceLocation(Reference.MODID, "ore_white_four"),
                new ResourceLocation(Reference.MODID, "ore_white_three"),
                new ResourceLocation(Reference.MODID, "ore_white_two"),
                new ResourceLocation(Reference.MODID, "ore_white_one"),
                new ResourceLocation(Reference.MODID, "ore_white_base")
        );

        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockRegistry.oreblack),
                new ResourceLocation(Reference.MODID, "ore_black_ore"),
                new ResourceLocation(Reference.MODID, "ore_black_five"),
                new ResourceLocation(Reference.MODID, "ore_black_four"),
                new ResourceLocation(Reference.MODID, "ore_black_three"),
                new ResourceLocation(Reference.MODID, "ore_black_two"),
                new ResourceLocation(Reference.MODID, "ore_black_one"),
                new ResourceLocation(Reference.MODID, "ore_black_base")
        );


    }

}
