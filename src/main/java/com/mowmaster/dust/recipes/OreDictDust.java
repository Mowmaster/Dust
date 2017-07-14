package com.mowmaster.dust.recipes;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by KingMowmaster on 7/14/2017.
 */
public class OreDictDust
{
    public static void addEntries()
    {
        //NEoAOreDictionary.registerOre("dustPyrothium",fake_pyrothium);

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

        OreDictionary.registerOre("oreCrystal", BlockRegistry.redOre);
        OreDictionary.registerOre("oreCrystal", BlockRegistry.blueOre);
        OreDictionary.registerOre("oreCrystal", BlockRegistry.yellowOre);
        OreDictionary.registerOre("oreCrystal", BlockRegistry.purpleOre);
        OreDictionary.registerOre("oreCrystal", BlockRegistry.orangeOre);
        OreDictionary.registerOre("oreCrystal", BlockRegistry.greenOre);
        OreDictionary.registerOre("oreCrystal", BlockRegistry.whiteOre);
        OreDictionary.registerOre("oreCrystal", BlockRegistry.blackOre);

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





    }
}
