package com.mowmaster.dust.recipes;

import com.mowmaster.dust.blocks.buildingblocks.BlockDustBasic;
import com.mowmaster.dust.blocks.treebits.BlockCharcoal;
import com.mowmaster.dust.blocks.crystal.BlockCrystalOre;
import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.treebits.BlockDustLeaf;
import com.mowmaster.dust.blocks.treebits.BlockDustLog;
import com.mowmaster.dust.blocks.treebits.SaplingRegister;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictDust
{
    public static void addEntries()
    {
        OreDictionary.registerOre("plankWood", BlockDustBasic.redplanks);
        OreDictionary.registerOre("plankWood", BlockDustBasic.blueplanks);
        OreDictionary.registerOre("plankWood", BlockDustBasic.yellowplanks);
        OreDictionary.registerOre("plankWood", BlockDustBasic.purpleplanks);
        OreDictionary.registerOre("plankWood", BlockDustBasic.orangeplanks);
        OreDictionary.registerOre("plankWood", BlockDustBasic.greenplanks);
        OreDictionary.registerOre("plankWood", BlockDustBasic.whiteplanks);
        OreDictionary.registerOre("plankWood", BlockDustBasic.blackplanks);

        OreDictionary.registerOre("treeSapling", SaplingRegister.saplingred);
        OreDictionary.registerOre("treeSapling", SaplingRegister.saplingblue);
        OreDictionary.registerOre("treeSapling", SaplingRegister.saplingyellow);
        OreDictionary.registerOre("treeSapling", SaplingRegister.saplingpurple);
        OreDictionary.registerOre("treeSapling", SaplingRegister.saplingorange);
        OreDictionary.registerOre("treeSapling", SaplingRegister.saplinggreen);
        OreDictionary.registerOre("treeSapling", SaplingRegister.saplingwhite);
        OreDictionary.registerOre("treeSapling", SaplingRegister.saplingblack);

        OreDictionary.registerOre("logWood", BlockDustLog.logred);
        OreDictionary.registerOre("logWood", BlockDustLog.logblue);
        OreDictionary.registerOre("logWood", BlockDustLog.logyellow);
        OreDictionary.registerOre("logWood", BlockDustLog.logpurple);
        OreDictionary.registerOre("logWood", BlockDustLog.logorange);
        OreDictionary.registerOre("logWood", BlockDustLog.loggreen);
        OreDictionary.registerOre("logWood", BlockDustLog.logwhite);
        OreDictionary.registerOre("logWood", BlockDustLog.logblack);

        OreDictionary.registerOre("treeLeaves", BlockDustLeaf.leaf);

        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.redore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.blueore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.yellowore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.purpleore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.orangeore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.greenore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.whiteore);
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.blackore);

        OreDictionary.registerOre("stone", BlockDustBasic.redstone);
        OreDictionary.registerOre("stone", BlockDustBasic.bluestone);
        OreDictionary.registerOre("stone", BlockDustBasic.yellowstone);
        OreDictionary.registerOre("stone", BlockDustBasic.purplestone);
        OreDictionary.registerOre("stone", BlockDustBasic.orangestone);
        OreDictionary.registerOre("stone", BlockDustBasic.greenstone);
        OreDictionary.registerOre("stone", BlockDustBasic.whitestone);
        OreDictionary.registerOre("stone", BlockDustBasic.blackstone);

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
