package com.mowmaster.dust.recipes;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.misc.AchievementHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import static com.mowmaster.dust.blocks.BlockRegistry.*;
import static com.mowmaster.dust.items.ItemRegistry.bit;
import static com.mowmaster.dust.items.ItemRegistry.crystal;
import static com.mowmaster.dust.items.ItemRegistry.dust;

/**
 * Created by KingMowmaster on 3/1/2017.
 */
public class CraftingRecipes
{
    public static void CraftingRecipes() {
        for (int i = 0; i < 8; i++) {
            //[WIP] - Until the Crusher next Version-Crystals to Dust
            GameRegistry.addShapelessRecipe(new ItemStack(dust, 2, i), new ItemStack(crystal, 1, i));
            //Dust to bits
            GameRegistry.addShapelessRecipe(new ItemStack(bit, 9, i), new ItemStack(dust, 1, i));
            //Bits to dust
            GameRegistry.addShapelessRecipe(new ItemStack(dust, 1, i),
                    new ItemStack(bit, 1, i), new ItemStack(bit, 1, i), new ItemStack(bit, 1, i),
                    new ItemStack(bit, 1, i), new ItemStack(bit, 1, i), new ItemStack(bit, 1, i),
                    new ItemStack(bit, 1, i), new ItemStack(bit, 1, i), new ItemStack(bit, 1, i));
        }
        //Dust Block into Dust
        GameRegistry.addShapelessRecipe(new ItemStack(dust, 1, 0), new ItemStack(BlockRegistry.redDust));
        GameRegistry.addShapelessRecipe(new ItemStack(dust, 1, 1), new ItemStack(BlockRegistry.blueDust));
        GameRegistry.addShapelessRecipe(new ItemStack(dust, 1, 2), new ItemStack(BlockRegistry.yellowDust));
        GameRegistry.addShapelessRecipe(new ItemStack(dust, 1, 3), new ItemStack(BlockRegistry.purpleDust));
        GameRegistry.addShapelessRecipe(new ItemStack(dust, 1, 5), new ItemStack(BlockRegistry.orangeDust));
        GameRegistry.addShapelessRecipe(new ItemStack(dust, 1, 4), new ItemStack(BlockRegistry.greenDust));
        GameRegistry.addShapelessRecipe(new ItemStack(dust, 1, 6), new ItemStack(BlockRegistry.whiteDust));
        GameRegistry.addShapelessRecipe(new ItemStack(dust, 1, 7), new ItemStack(BlockRegistry.blackDust));

        //Use for Stone Dust
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.COBBLESTONE,1),new ItemStack(dust,1,8),new ItemStack(dust,1,8),new ItemStack(dust,1,8),new ItemStack(dust,1,8));
        
        //Craftable Crystals
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.redCrystalFive),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,0),new ItemStack(crystal,1,0),new ItemStack(crystal,1,0),new ItemStack(crystal,1,0),new ItemStack(crystal,1,0));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.blueCrystalFive),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,1),new ItemStack(crystal,1,1),new ItemStack(crystal,1,1),new ItemStack(crystal,1,1),new ItemStack(crystal,1,1));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.yellowCrystalFive),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,2),new ItemStack(crystal,1,2),new ItemStack(crystal,1,2),new ItemStack(crystal,1,2),new ItemStack(crystal,1,2));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.purpleCrystalFive),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,3),new ItemStack(crystal,1,3),new ItemStack(crystal,1,3),new ItemStack(crystal,1,3),new ItemStack(crystal,1,3));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.orangeCrystalFive),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,5),new ItemStack(crystal,1,5),new ItemStack(crystal,1,5),new ItemStack(crystal,1,5),new ItemStack(crystal,1,5));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.greenCrystalFive),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,4),new ItemStack(crystal,1,4),new ItemStack(crystal,1,4),new ItemStack(crystal,1,4),new ItemStack(crystal,1,4));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.whiteCrystalFive),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,6),new ItemStack(crystal,1,6),new ItemStack(crystal,1,6),new ItemStack(crystal,1,6),new ItemStack(crystal,1,6));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.blackCrystalFive),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,7),new ItemStack(crystal,1,7),new ItemStack(crystal,1,7),new ItemStack(crystal,1,7),new ItemStack(crystal,1,7));

        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.redCrystalFour),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,0),new ItemStack(crystal,1,0),new ItemStack(crystal,1,0),new ItemStack(crystal,1,0));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.blueCrystalFour),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,1),new ItemStack(crystal,1,1),new ItemStack(crystal,1,1),new ItemStack(crystal,1,1));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.yellowCrystalFour),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,2),new ItemStack(crystal,1,2),new ItemStack(crystal,1,2),new ItemStack(crystal,1,2));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.purpleCrystalFour),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,3),new ItemStack(crystal,1,3),new ItemStack(crystal,1,3),new ItemStack(crystal,1,3));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.orangeCrystalFour),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,5),new ItemStack(crystal,1,5),new ItemStack(crystal,1,5),new ItemStack(crystal,1,5));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.greenCrystalFour),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,4),new ItemStack(crystal,1,4),new ItemStack(crystal,1,4),new ItemStack(crystal,1,4));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.whiteCrystalFour),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,6),new ItemStack(crystal,1,6),new ItemStack(crystal,1,6),new ItemStack(crystal,1,6));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.blackCrystalFour),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,7),new ItemStack(crystal,1,7),new ItemStack(crystal,1,7),new ItemStack(crystal,1,7));

        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.redCrystalThree),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,0),new ItemStack(crystal,1,0),new ItemStack(crystal,1,0));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.blueCrystalThree),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,1),new ItemStack(crystal,1,1),new ItemStack(crystal,1,1));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.yellowCrystalThree),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,2),new ItemStack(crystal,1,2),new ItemStack(crystal,1,2));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.purpleCrystalThree),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,3),new ItemStack(crystal,1,3),new ItemStack(crystal,1,3));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.orangeCrystalThree),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,5),new ItemStack(crystal,1,5),new ItemStack(crystal,1,5));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.greenCrystalThree),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,4),new ItemStack(crystal,1,4),new ItemStack(crystal,1,4));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.whiteCrystalThree),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,6),new ItemStack(crystal,1,6),new ItemStack(crystal,1,6));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.blackCrystalThree),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,7),new ItemStack(crystal,1,7),new ItemStack(crystal,1,7));

        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.redCrystalTwo),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,0),new ItemStack(crystal,1,0));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.blueCrystalTwo),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,1),new ItemStack(crystal,1,1));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.yellowCrystalTwo),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,2),new ItemStack(crystal,1,2));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.purpleCrystalTwo),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,3),new ItemStack(crystal,1,3));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.orangeCrystalTwo),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,5),new ItemStack(crystal,1,5));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.greenCrystalTwo),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,4),new ItemStack(crystal,1,4));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.whiteCrystalTwo),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,6),new ItemStack(crystal,1,6));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.blackCrystalTwo),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,7),new ItemStack(crystal,1,7));

        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.redCrystalOne),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,0));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.blueCrystalOne),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,1));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.yellowCrystalOne),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,2));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.purpleCrystalOne),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,3));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.orangeCrystalOne),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,5));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.greenCrystalOne),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,4));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.whiteCrystalOne),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,6));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockRegistry.blackCrystalOne),new ItemStack(Blocks.STONE,1)
                ,new ItemStack(crystal,1,7));

        //Blocks
        GameRegistry.addShapedRecipe(new ItemStack(redstone, 8), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust, 1, 0)});
        GameRegistry.addShapedRecipe(new ItemStack(redstonestairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(redstone)});
        GameRegistry.addShapedRecipe(new ItemStack(redstoneslabs, 6), new Object[]{"XXX", 'X', new ItemStack(redstone)});
        GameRegistry.addShapedRecipe(new ItemStack(redstonewalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(redstone)});
        GameRegistry.addShapedRecipe(new ItemStack(redstonefences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(redstone), 'Y', new ItemStack(redstoneslabs)});
        GameRegistry.addShapedRecipe(new ItemStack(redbricks2, 4), new Object[]{"XX", "XX", 'X', new ItemStack(redstone)});
        GameRegistry.addShapedRecipe(new ItemStack(redbrickstairs2, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(redbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(redbrickslabs2, 6), new Object[]{"XXX", 'X', new ItemStack(redbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(redbrickwalls2, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(redbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(redbrickfences2, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(redbricks2), 'Y', new ItemStack(redbrickslabs2)});
        GameRegistry.addShapedRecipe(new ItemStack(redbricks, 4), new Object[]{"XX", "XX", 'X', new ItemStack(redbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(redbrickstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(redbricks)});
        GameRegistry.addShapedRecipe(new ItemStack(redbrickslabs, 6), new Object[]{"XXX", 'X', new ItemStack(redbricks)});
        GameRegistry.addShapedRecipe(new ItemStack(redbrickwalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(redbricks)});
        GameRegistry.addShapedRecipe(new ItemStack(redbrickfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(redbricks), 'Y', new ItemStack(redbrickslabs)});
        GameRegistry.addShapelessRecipe(new ItemStack(redplanks, 4), new ItemStack(logred, 1));
        GameRegistry.addShapedRecipe(new ItemStack(redplankstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(redplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(redplankslabs, 6), new Object[]{"XXX", 'X', new ItemStack(redplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(redplankfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(redplanks), 'Y', new ItemStack(redplankslabs)});

        GameRegistry.addShapedRecipe(new ItemStack(bluestone, 8), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust, 1, 1)});
        GameRegistry.addShapedRecipe(new ItemStack(bluestonestairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(bluestone)});
        GameRegistry.addShapedRecipe(new ItemStack(bluestoneslabs, 6), new Object[]{"XXX", 'X', new ItemStack(bluestone)});
        GameRegistry.addShapedRecipe(new ItemStack(bluestonewalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(bluestone)});
        GameRegistry.addShapedRecipe(new ItemStack(bluestonefences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(bluestone), 'Y', new ItemStack(bluestoneslabs)});
        GameRegistry.addShapedRecipe(new ItemStack(bluebricks2, 4), new Object[]{"XX", "XX", 'X', new ItemStack(bluestone)});
        GameRegistry.addShapedRecipe(new ItemStack(bluebrickstairs2, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(bluebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(bluebrickslabs2, 6), new Object[]{"XXX", 'X', new ItemStack(bluebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(bluebrickwalls2, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(bluebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(bluebrickfences2, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(bluebricks2), 'Y', new ItemStack(bluebrickslabs2)});
        GameRegistry.addShapedRecipe(new ItemStack(bluebricks, 4), new Object[]{"XX", "XX", 'X', new ItemStack(bluebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(bluebrickstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(bluebricks)});
        GameRegistry.addShapedRecipe(new ItemStack(bluebrickslabs, 6), new Object[]{"XXX", 'X', new ItemStack(bluebricks)});
        GameRegistry.addShapedRecipe(new ItemStack(bluebrickwalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(bluebricks)});
        GameRegistry.addShapedRecipe(new ItemStack(bluebrickfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(bluebricks), 'Y', new ItemStack(bluebrickslabs)});
        GameRegistry.addShapelessRecipe(new ItemStack(blueplanks, 4), new ItemStack(logblue, 1));
        GameRegistry.addShapedRecipe(new ItemStack(blueplankstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(blueplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(blueplankslabs, 6), new Object[]{"XXX", 'X', new ItemStack(blueplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(blueplankfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(blueplanks), 'Y', new ItemStack(blueplankslabs)});

        GameRegistry.addShapedRecipe(new ItemStack(yellowstone, 8), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust, 1, 2)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowstonestairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(yellowstone)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowstoneslabs, 6), new Object[]{"XXX", 'X', new ItemStack(yellowstone)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowstonewalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(yellowstone)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowstonefences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(yellowstone), 'Y', new ItemStack(yellowstoneslabs)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowbricks2, 4), new Object[]{"XX", "XX", 'X', new ItemStack(yellowstone)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowbrickstairs2, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(yellowbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowbrickslabs2, 6), new Object[]{"XXX", 'X', new ItemStack(yellowbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowbrickwalls2, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(yellowbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowbrickfences2, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(yellowbricks2), 'Y', new ItemStack(yellowbrickslabs2)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowbricks, 4), new Object[]{"XX", "XX", 'X', new ItemStack(yellowbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowbrickstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(yellowbricks)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowbrickslabs, 6), new Object[]{"XXX", 'X', new ItemStack(yellowbricks)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowbrickwalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(yellowbricks)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowbrickfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(yellowbricks), 'Y', new ItemStack(yellowbrickslabs)});
        GameRegistry.addShapelessRecipe(new ItemStack(yellowplanks, 4), new ItemStack(logyellow, 1));
        GameRegistry.addShapedRecipe(new ItemStack(yellowplankstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(yellowplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowplankslabs, 6), new Object[]{"XXX", 'X', new ItemStack(yellowplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(yellowplankfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(yellowplanks), 'Y', new ItemStack(yellowplankslabs)});

        GameRegistry.addShapedRecipe(new ItemStack(purplestone, 8), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust, 1, 3)});
        GameRegistry.addShapedRecipe(new ItemStack(purplestonestairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(purplestone)});
        GameRegistry.addShapedRecipe(new ItemStack(purplestoneslabs, 6), new Object[]{"XXX", 'X', new ItemStack(purplestone)});
        GameRegistry.addShapedRecipe(new ItemStack(purplestonewalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(purplestone)});
        GameRegistry.addShapedRecipe(new ItemStack(purplestonefences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(purplestone), 'Y', new ItemStack(purplestoneslabs)});
        GameRegistry.addShapedRecipe(new ItemStack(purplebricks2, 4), new Object[]{"XX", "XX", 'X', new ItemStack(purplestone)});
        GameRegistry.addShapedRecipe(new ItemStack(purplebrickstairs2, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(purplebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(purplebrickslabs2, 6), new Object[]{"XXX", 'X', new ItemStack(purplebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(purplebrickwalls2, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(purplebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(purplebrickfences2, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(purplebricks2), 'Y', new ItemStack(purplebrickslabs2)});
        GameRegistry.addShapedRecipe(new ItemStack(purplebricks, 4), new Object[]{"XX", "XX", 'X', new ItemStack(purplebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(purplebrickstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(purplebricks)});
        GameRegistry.addShapedRecipe(new ItemStack(purplebrickslabs, 6), new Object[]{"XXX", 'X', new ItemStack(purplebricks)});
        GameRegistry.addShapedRecipe(new ItemStack(purplebrickwalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(purplebricks)});
        GameRegistry.addShapedRecipe(new ItemStack(purplebrickfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(purplebricks), 'Y', new ItemStack(purplebrickslabs)});
        GameRegistry.addShapelessRecipe(new ItemStack(purpleplanks, 4), new ItemStack(logpurple, 1));
        GameRegistry.addShapedRecipe(new ItemStack(purpleplankstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(purpleplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(purpleplankslabs, 6), new Object[]{"XXX", 'X', new ItemStack(purpleplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(purpleplankfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(purpleplanks), 'Y', new ItemStack(purpleplankslabs)});

        GameRegistry.addShapedRecipe(new ItemStack(greenstone, 8), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust, 1, 4)});
        GameRegistry.addShapedRecipe(new ItemStack(greenstonestairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(greenstone)});
        GameRegistry.addShapedRecipe(new ItemStack(greenstoneslabs, 6), new Object[]{"XXX", 'X', new ItemStack(greenstone)});
        GameRegistry.addShapedRecipe(new ItemStack(greenstonewalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(greenstone)});
        GameRegistry.addShapedRecipe(new ItemStack(greenstonefences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(greenstone), 'Y', new ItemStack(greenstoneslabs)});
        GameRegistry.addShapedRecipe(new ItemStack(greenbricks2, 4), new Object[]{"XX", "XX", 'X', new ItemStack(greenstone)});
        GameRegistry.addShapedRecipe(new ItemStack(greenbrickstairs2, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(greenbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(greenbrickslabs2, 6), new Object[]{"XXX", 'X', new ItemStack(greenbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(greenbrickwalls2, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(greenbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(greenbrickfences2, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(greenbricks2), 'Y', new ItemStack(greenbrickslabs2)});
        GameRegistry.addShapedRecipe(new ItemStack(greenbricks, 4), new Object[]{"XX", "XX", 'X', new ItemStack(greenbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(greenbrickstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(greenbricks)});
        GameRegistry.addShapedRecipe(new ItemStack(greenbrickslabs, 6), new Object[]{"XXX", 'X', new ItemStack(greenbricks)});
        GameRegistry.addShapedRecipe(new ItemStack(greenbrickwalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(greenbricks)});
        GameRegistry.addShapedRecipe(new ItemStack(greenbrickfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(greenbricks), 'Y', new ItemStack(greenbrickslabs)});
        GameRegistry.addShapelessRecipe(new ItemStack(greenplanks, 4), new ItemStack(loggreen, 1));
        GameRegistry.addShapedRecipe(new ItemStack(greenplankstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(greenplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(greenplankslabs, 6), new Object[]{"XXX", 'X', new ItemStack(greenplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(greenplankfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(greenplanks), 'Y', new ItemStack(greenplankslabs)});

        GameRegistry.addShapedRecipe(new ItemStack(orangestone, 8), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust, 1, 5)});
        GameRegistry.addShapedRecipe(new ItemStack(orangestonestairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(orangestone)});
        GameRegistry.addShapedRecipe(new ItemStack(orangestoneslabs, 6), new Object[]{"XXX", 'X', new ItemStack(orangestone)});
        GameRegistry.addShapedRecipe(new ItemStack(orangestonewalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(orangestone)});
        GameRegistry.addShapedRecipe(new ItemStack(orangestonefences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(orangestone), 'Y', new ItemStack(orangestoneslabs)});
        GameRegistry.addShapedRecipe(new ItemStack(orangebricks2, 4), new Object[]{"XX", "XX", 'X', new ItemStack(orangestone)});
        GameRegistry.addShapedRecipe(new ItemStack(orangebrickstairs2, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(orangebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(orangebrickslabs2, 6), new Object[]{"XXX", 'X', new ItemStack(orangebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(orangebrickwalls2, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(orangebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(orangebrickfences2, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(orangebricks2), 'Y', new ItemStack(orangebrickslabs2)});
        GameRegistry.addShapedRecipe(new ItemStack(orangebricks, 4), new Object[]{"XX", "XX", 'X', new ItemStack(orangebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(orangebrickstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(orangebricks)});
        GameRegistry.addShapedRecipe(new ItemStack(orangebrickslabs, 6), new Object[]{"XXX", 'X', new ItemStack(orangebricks)});
        GameRegistry.addShapedRecipe(new ItemStack(orangebrickwalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(orangebricks)});
        GameRegistry.addShapedRecipe(new ItemStack(orangebrickfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(orangebricks), 'Y', new ItemStack(orangebrickslabs)});
        GameRegistry.addShapelessRecipe(new ItemStack(orangeplanks, 4), new ItemStack(logorange, 1));
        GameRegistry.addShapedRecipe(new ItemStack(orangeplankstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(orangeplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(orangeplankslabs, 6), new Object[]{"XXX", 'X', new ItemStack(orangeplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(orangeplankfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(orangeplanks), 'Y', new ItemStack(orangeplankslabs)});

        GameRegistry.addShapedRecipe(new ItemStack(whitestone, 8), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust, 1, 6)});
        GameRegistry.addShapedRecipe(new ItemStack(whitestonestairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(whitestone)});
        GameRegistry.addShapedRecipe(new ItemStack(whitestoneslabs, 6), new Object[]{"XXX", 'X', new ItemStack(whitestone)});
        GameRegistry.addShapedRecipe(new ItemStack(whitestonewalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(whitestone)});
        GameRegistry.addShapedRecipe(new ItemStack(whitestonefences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(whitestone), 'Y', new ItemStack(whitestoneslabs)});
        GameRegistry.addShapedRecipe(new ItemStack(whitebricks2, 4), new Object[]{"XX", "XX", 'X', new ItemStack(whitestone)});
        GameRegistry.addShapedRecipe(new ItemStack(whitebrickstairs2, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(whitebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(whitebrickslabs2, 6), new Object[]{"XXX", 'X', new ItemStack(whitebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(whitebrickwalls2, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(whitebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(whitebrickfences2, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(whitebricks2), 'Y', new ItemStack(whitebrickslabs2)});
        GameRegistry.addShapedRecipe(new ItemStack(whitebricks, 4), new Object[]{"XX", "XX", 'X', new ItemStack(whitebricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(whitebrickstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(whitebricks)});
        GameRegistry.addShapedRecipe(new ItemStack(whitebrickslabs, 6), new Object[]{"XXX", 'X', new ItemStack(whitebricks)});
        GameRegistry.addShapedRecipe(new ItemStack(whitebrickwalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(whitebricks)});
        GameRegistry.addShapedRecipe(new ItemStack(whitebrickfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(whitebricks), 'Y', new ItemStack(whitebrickslabs)});
        GameRegistry.addShapelessRecipe(new ItemStack(whiteplanks, 4), new ItemStack(logwhite, 1));
        GameRegistry.addShapedRecipe(new ItemStack(whiteplankstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(whiteplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(whiteplankslabs, 6), new Object[]{"XXX", 'X', new ItemStack(whiteplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(whiteplankfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(whiteplanks), 'Y', new ItemStack(whiteplankslabs)});

        GameRegistry.addShapedRecipe(new ItemStack(blackstone, 8), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust, 1, 7)});
        GameRegistry.addShapedRecipe(new ItemStack(blackstonestairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(blackstone)});
        GameRegistry.addShapedRecipe(new ItemStack(blackstoneslabs, 6), new Object[]{"XXX", 'X', new ItemStack(blackstone)});
        GameRegistry.addShapedRecipe(new ItemStack(blackstonewalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(blackstone)});
        GameRegistry.addShapedRecipe(new ItemStack(blackstonefences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(blackstone), 'Y', new ItemStack(blackstoneslabs)});
        GameRegistry.addShapedRecipe(new ItemStack(blackbricks2, 4), new Object[]{"XX", "XX", 'X', new ItemStack(blackstone)});
        GameRegistry.addShapedRecipe(new ItemStack(blackbrickstairs2, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(blackbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(blackbrickslabs2, 6), new Object[]{"XXX", 'X', new ItemStack(blackbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(blackbrickwalls2, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(blackbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(blackbrickfences2, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(blackbricks2), 'Y', new ItemStack(blackbrickslabs2)});
        GameRegistry.addShapedRecipe(new ItemStack(blackbricks, 4), new Object[]{"XX", "XX", 'X', new ItemStack(blackbricks2)});
        GameRegistry.addShapedRecipe(new ItemStack(blackbrickstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(blackbricks)});
        GameRegistry.addShapedRecipe(new ItemStack(blackbrickslabs, 6), new Object[]{"XXX", 'X', new ItemStack(blackbricks)});
        GameRegistry.addShapedRecipe(new ItemStack(blackbrickwalls, 6), new Object[]{"XXX", "XXX", 'X', new ItemStack(blackbricks)});
        GameRegistry.addShapedRecipe(new ItemStack(blackbrickfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(blackbricks), 'Y', new ItemStack(blackbrickslabs)});
        GameRegistry.addShapelessRecipe(new ItemStack(blackplanks, 4), new ItemStack(logblack, 1));
        GameRegistry.addShapedRecipe(new ItemStack(blackplankstairs, 4), new Object[]{"X  ", "XX ", "XXX", 'X', new ItemStack(blackplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(blackplankslabs, 6), new Object[]{"XXX", 'X', new ItemStack(blackplanks)});
        GameRegistry.addShapedRecipe(new ItemStack(blackplankfences, 5), new Object[]{"XYX", "XYX", 'X', new ItemStack(blackplanks), 'Y', new ItemStack(blackplankslabs)});
    }
}


