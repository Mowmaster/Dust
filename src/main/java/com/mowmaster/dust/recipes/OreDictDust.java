package com.mowmaster.dust.recipes;

import com.mowmaster.dust.blocks.BlockCharcoal;
import com.mowmaster.dust.blocks.BlockCrystalOre;
import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictDust
{
    public static void addEntries()
    {
        OreDictionary.registerOre("plankWood", BlockRegistry.redplanks);
        OreDictionary.registerOre("plankWood", BlockRegistry.blueplanks);
        OreDictionary.registerOre("plankWood", BlockRegistry.yellowplanks);
        OreDictionary.registerOre("plankWood", BlockRegistry.purpleplanks);
        OreDictionary.registerOre("plankWood", BlockRegistry.orangeplanks);
        OreDictionary.registerOre("plankWood", BlockRegistry.greenplanks);
        OreDictionary.registerOre("plankWood", BlockRegistry.whiteplanks);
        OreDictionary.registerOre("plankWood", BlockRegistry.blackplanks);

        OreDictionary.registerOre("treeSapling", BlockRegistry.saplingred);
        OreDictionary.registerOre("treeSapling", BlockRegistry.saplingblue);
        OreDictionary.registerOre("treeSapling", BlockRegistry.saplingyellow);
        OreDictionary.registerOre("treeSapling", BlockRegistry.saplingpurple);
        OreDictionary.registerOre("treeSapling", BlockRegistry.saplingorange);
        OreDictionary.registerOre("treeSapling", BlockRegistry.saplinggreen);
        OreDictionary.registerOre("treeSapling", BlockRegistry.saplingwhite);
        OreDictionary.registerOre("treeSapling", BlockRegistry.saplingblack);

        OreDictionary.registerOre("logWood", BlockRegistry.logred);
        OreDictionary.registerOre("logWood", BlockRegistry.logblue);
        OreDictionary.registerOre("logWood", BlockRegistry.logyellow);
        OreDictionary.registerOre("logWood", BlockRegistry.logpurple);
        OreDictionary.registerOre("logWood", BlockRegistry.logorange);
        OreDictionary.registerOre("logWood", BlockRegistry.loggreen);
        OreDictionary.registerOre("logWood", BlockRegistry.logwhite);
        OreDictionary.registerOre("logWood", BlockRegistry.logblack);

        OreDictionary.registerOre("treeLeaves", BlockRegistry.leaf);

        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.redore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.blueore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.yellowore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.purpleore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.orangeore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.greenore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.whiteore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.blackore);

        OreDictionary.registerOre("stone", BlockRegistry.redstone);
        OreDictionary.registerOre("stone", BlockRegistry.bluestone);
        OreDictionary.registerOre("stone", BlockRegistry.yellowstone);
        OreDictionary.registerOre("stone", BlockRegistry.purplestone);
        OreDictionary.registerOre("stone", BlockRegistry.orangestone);
        OreDictionary.registerOre("stone", BlockRegistry.greenstone);
        OreDictionary.registerOre("stone", BlockRegistry.whitestone);
        OreDictionary.registerOre("stone", BlockRegistry.blackstone);

        OreDictionary.registerOre("gemCrystal", ItemRegistry.crystal);
        OreDictionary.registerOre("dustCrystal", ItemRegistry.dust);
        OreDictionary.registerOre("bitDust", ItemRegistry.bit);

        OreDictionary.registerOre("charcoal",new ItemStack(ItemRegistry.charcoalRed));
        OreDictionary.registerOre("charcoal",new ItemStack(ItemRegistry.charcoalBlue));
        OreDictionary.registerOre("charcoal",new ItemStack(ItemRegistry.charcoalYellow));
        OreDictionary.registerOre("charcoal",new ItemStack(ItemRegistry.charcoalPurple));
        OreDictionary.registerOre("charcoal",new ItemStack(ItemRegistry.charcoalGreen));
        OreDictionary.registerOre("charcoal",new ItemStack(ItemRegistry.charcoalOrange));
        OreDictionary.registerOre("charcoal",new ItemStack(ItemRegistry.charcoalWhite));
        OreDictionary.registerOre("charcoal",new ItemStack(ItemRegistry.charcoalBlack));

        OreDictionary.registerOre("blockCharcoal",new ItemStack(BlockCharcoal.charcoalRed));
        OreDictionary.registerOre("blockCharcoal",new ItemStack(BlockCharcoal.charcoalBlue));
        OreDictionary.registerOre("blockCharcoal",new ItemStack(BlockCharcoal.charcoalYellow));
        OreDictionary.registerOre("blockCharcoal",new ItemStack(BlockCharcoal.charcoalPurple));
        OreDictionary.registerOre("blockCharcoal",new ItemStack(BlockCharcoal.charcoalGreen));
        OreDictionary.registerOre("blockCharcoal",new ItemStack(BlockCharcoal.charcoalOrange));
        OreDictionary.registerOre("blockCharcoal",new ItemStack(BlockCharcoal.charcoalWhite));
        OreDictionary.registerOre("blockCharcoal",new ItemStack(BlockCharcoal.charcoalBlack));


        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinA));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinB));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinC));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinD));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinE));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinF));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinG));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinH));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinI));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinJ));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinK));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinL));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinM));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinN));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinO));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinP));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinQ));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinR));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinS));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinT));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinU));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinV));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinW));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinX));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinY));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoinZ));


    }
}
