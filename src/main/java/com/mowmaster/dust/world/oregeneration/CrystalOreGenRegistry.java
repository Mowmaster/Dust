package com.mowmaster.dust.world.oregeneration;

import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by KingMowmaster on 2/11/2016.
 */
public class CrystalOreGenRegistry {

    public static void OreGenReg()
    {
        GameRegistry.registerWorldGenerator(new CrystalOreGenWhite(),0);
        GameRegistry.registerWorldGenerator(new CrystalOreGenBlack(),0);
        GameRegistry.registerWorldGenerator(new CrystalOreGenPurple(),0);
        GameRegistry.registerWorldGenerator(new CrystalOreGenOrange(),0);
        GameRegistry.registerWorldGenerator(new CrystalOreGenGreen(),0);
        GameRegistry.registerWorldGenerator(new CrystalOreGenYellow(),0);
        GameRegistry.registerWorldGenerator(new CrystalOreGenBlue(),0);
        GameRegistry.registerWorldGenerator(new CrystalOreGenRed(),0);
        GameRegistry.registerWorldGenerator(new CrystalOreGen(),0);
        GameRegistry.registerWorldGenerator(new CrystalOreGenOtherWorld(),0);
    }
}
