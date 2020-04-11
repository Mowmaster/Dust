package com.mowmaster.dust.recipes;

import com.mowmaster.dust.blocks.treebits.BlockDustLog;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class SmeltingRecipes
{
    //This code is probably a hack, but hey, it works!?
    private static final boolean immersiveE=Loader.isModLoaded("immersiveengineering");
    private static final boolean forestry=Loader.isModLoaded("forestry");
    private static final boolean embers=Loader.isModLoaded("embers");

    private static final ItemStack ingotCopper = (immersiveE)?(new ItemStack(Item.getByNameOrId("immersiveengineering:metal"),1,0)):
            ((forestry)?(new ItemStack(Item.getByNameOrId("forestry:ingot_copper"),1)):
                    ((embers)?(new ItemStack(Item.getByNameOrId("embers:ingot_copper"),1)):(ItemStack.EMPTY)));

    private static final ItemStack ingotAluminium = (immersiveE)?(new ItemStack(Item.getByNameOrId("immersiveengineering:metal"),1,1)):((embers)?(new ItemStack(Item.getByNameOrId("embers:ingot_aluminum"),1)):(ItemStack.EMPTY));
    private static final ItemStack ingotLead = (immersiveE)?(new ItemStack(Item.getByNameOrId("immersiveengineering:metal"),1,2)):((embers)?(new ItemStack(Item.getByNameOrId("embers:ingot_lead"),1)):(ItemStack.EMPTY));
    private static final ItemStack ingotSilver = (immersiveE)?(new ItemStack(Item.getByNameOrId("immersiveengineering:metal"),1,3)):((embers)?(new ItemStack(Item.getByNameOrId("embers:ingot_silver"),1)):(ItemStack.EMPTY));
    private static final ItemStack ingotNickel = (immersiveE)?(new ItemStack(Item.getByNameOrId("immersiveengineering:metal"),1,4)):((embers)?(new ItemStack(Item.getByNameOrId("embers:ingot_nickel"),1)):(ItemStack.EMPTY));
    private static final ItemStack ingotUranium = (immersiveE)?(new ItemStack(Item.getByNameOrId("immersiveengineering:metal"),1,5)):(ItemStack.EMPTY);
    private static final ItemStack ingotTin = (forestry)?(new ItemStack(Item.getByNameOrId("forestry:ingot_tin"),1)):((embers)?(new ItemStack(Item.getByNameOrId("embers:ingot_tin"),1)):(ItemStack.EMPTY));
    //End of Hack Setup!?

    public static void init()
    {
        GameRegistry.addSmelting(BlockDustLog.logred, new ItemStack(ItemRegistry.charcoalRed),0.7f);
        GameRegistry.addSmelting(BlockDustLog.logblue, new ItemStack(ItemRegistry.charcoalBlue),0.7f);
        GameRegistry.addSmelting(BlockDustLog.logyellow, new ItemStack(ItemRegistry.charcoalYellow),0.7f);
        GameRegistry.addSmelting(BlockDustLog.logpurple, new ItemStack(ItemRegistry.charcoalPurple),0.7f);
        GameRegistry.addSmelting(BlockDustLog.loggreen, new ItemStack(ItemRegistry.charcoalGreen),0.7f);
        GameRegistry.addSmelting(BlockDustLog.logorange, new ItemStack(ItemRegistry.charcoalOrange),0.7f);
        GameRegistry.addSmelting(BlockDustLog.logwhite, new ItemStack(ItemRegistry.charcoalWhite),0.7f);
        GameRegistry.addSmelting(BlockDustLog.logblack, new ItemStack(ItemRegistry.charcoalBlack),0.7f);
        GameRegistry.addSmelting(new ItemStack(ItemRegistry.dust,1,9),new ItemStack(Items.IRON_INGOT,1),0.35f);
        GameRegistry.addSmelting(new ItemStack(ItemRegistry.dust,1,10),new ItemStack(Items.GOLD_INGOT,1),0.35f);

        GameRegistry.addSmelting(new ItemStack(ItemRegistry.dust_compat,1,0),ingotCopper,0.35f);
        GameRegistry.addSmelting(new ItemStack(ItemRegistry.dust_compat,1,1),ingotAluminium,0.35f);
        GameRegistry.addSmelting(new ItemStack(ItemRegistry.dust_compat,1,2),ingotLead,0.35f);
        GameRegistry.addSmelting(new ItemStack(ItemRegistry.dust_compat,1,3),ingotSilver,0.35f);
        GameRegistry.addSmelting(new ItemStack(ItemRegistry.dust_compat,1,4),ingotNickel,0.35f);
        GameRegistry.addSmelting(new ItemStack(ItemRegistry.dust_compat,1,5),ingotUranium,0.35f);
        GameRegistry.addSmelting(new ItemStack(ItemRegistry.dust_compat,1,6),ingotTin,0.35f);

        for(ItemStack stack : OreDictionary.getOres("ancientCoin")){ GameRegistry.addSmelting(stack, new ItemStack(Items.GOLD_INGOT,1), 0.10f); }
    }
}
