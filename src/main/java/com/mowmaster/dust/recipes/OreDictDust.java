package com.mowmaster.dust.recipes;

import com.mowmaster.dust.blocks.buildingblocks.BlockDustBasic;
import com.mowmaster.dust.blocks.buildingblocks.BlockDustBasicMeta;
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
        OreDictionary.registerOre("plankWood", new ItemStack(BlockDustBasicMeta.dustplanks,1,0));
        OreDictionary.registerOre("plankWood", new ItemStack(BlockDustBasicMeta.dustplanks,1,1));
        OreDictionary.registerOre("plankWood", new ItemStack(BlockDustBasicMeta.dustplanks,1,2));
        OreDictionary.registerOre("plankWood", new ItemStack(BlockDustBasicMeta.dustplanks,1,3));
        OreDictionary.registerOre("plankWood", new ItemStack(BlockDustBasicMeta.dustplanks,1,4));
        OreDictionary.registerOre("plankWood", new ItemStack(BlockDustBasicMeta.dustplanks,1,5));
        OreDictionary.registerOre("plankWood", new ItemStack(BlockDustBasicMeta.dustplanks,1,6));
        OreDictionary.registerOre("plankWood", new ItemStack(BlockDustBasicMeta.dustplanks,1,7));

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
        OreDictionary.registerOre("oreCrystal", BlockCrystalOre.ore);
        OreDictionary.registerOre("stone", BlockDustBasicMeta.duststone);

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

        OreDictionary.registerOre("blockCharcoal",new ItemStack(BlockCharcoal.charcoalBlockColors));


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
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.ancientCoin));

        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.userUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.shearingUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.chopperUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.crafter1Upgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.crafter4Upgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.crafter9Upgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.exportUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.singleExportUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.importUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.dropperUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.placerUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.breakerUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.filterUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.fuzzyFilterUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.filterModUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.filterBlacklistUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.fuzzyFilterBlacklistUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.filterModBlacklistUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.furnaceUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.upgradeEnchanted));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.upgradeEnchantedBlacklist));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.effectUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.growerUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.planterUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.harvesterUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.enchantUpgrade));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.expCollector));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.expDropper));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.expRelay));
        OreDictionary.registerOre("ancientCoin",new ItemStack(ItemRegistry.expTank));




    }
}
