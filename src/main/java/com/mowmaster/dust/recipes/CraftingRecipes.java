package com.mowmaster.dust.recipes;

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
    public static void ICraftingRecipes()
    {
        for(int i =0; i < 8; i++)
        {
            GameRegistry.addShapelessRecipe(new ItemStack(dust,1,i),new ItemStack(crystal,1,i));
            GameRegistry.addShapelessRecipe(new ItemStack(dust,1,i),
                    new ItemStack(bit,1,i),new ItemStack(bit,1,i),new ItemStack(bit,1,i),
                    new ItemStack(bit,1,i),new ItemStack(bit,1,i),new ItemStack(bit,1,i),
                    new ItemStack(bit,1,i),new ItemStack(bit,1,i),new ItemStack(bit,1,i));


            GameRegistry.addShapedRecipe(new ItemStack(redstone,8),new Object[]{"XXX","XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust,1,0)});
            GameRegistry.addShapedRecipe(new ItemStack(redstonestairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(redstone)});
            GameRegistry.addShapedRecipe(new ItemStack(redstoneslabs,6),new Object[]{"XXX", 'X', new ItemStack(redstone)});
            GameRegistry.addShapedRecipe(new ItemStack(redstonewalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(redstone)});
            GameRegistry.addShapedRecipe(new ItemStack(redstonefences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(redstone), 'Y', new ItemStack(redstoneslabs)});
            GameRegistry.addShapedRecipe(new ItemStack(redbricks2,4),new Object[]{"XX","XX", 'X', new ItemStack(redstone)});
            GameRegistry.addShapedRecipe(new ItemStack(redbrickstairs2,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(redbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(redbrickslabs2,6),new Object[]{"XXX", 'X', new ItemStack(redbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(redbrickwalls2,6),new Object[]{"XXX","XXX", 'X', new ItemStack(redbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(redbrickfences2,5),new Object[]{"XYX","XYX", 'X', new ItemStack(redbricks2), 'Y', new ItemStack(redbrickslabs2)});
            GameRegistry.addShapedRecipe(new ItemStack(redbricks,4),new Object[]{"XX","XX", 'X', new ItemStack(redbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(redbrickstairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(redbricks)});
            GameRegistry.addShapedRecipe(new ItemStack(redbrickslabs,6),new Object[]{"XXX", 'X', new ItemStack(redbricks)});
            GameRegistry.addShapedRecipe(new ItemStack(redbrickwalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(redbricks)});
            GameRegistry.addShapedRecipe(new ItemStack(redbrickfences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(redbricks), 'Y', new ItemStack(redbrickslabs)});

            GameRegistry.addShapedRecipe(new ItemStack(bluestone,8),new Object[]{"XXX","XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust,1,1)});
            GameRegistry.addShapedRecipe(new ItemStack(bluestonestairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(bluestone)});
            GameRegistry.addShapedRecipe(new ItemStack(bluestoneslabs,6),new Object[]{"XXX", 'X', new ItemStack(bluestone)});
            GameRegistry.addShapedRecipe(new ItemStack(bluestonewalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(bluestone)});
            GameRegistry.addShapedRecipe(new ItemStack(bluestonefences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(bluestone), 'Y', new ItemStack(bluestoneslabs)});
            GameRegistry.addShapedRecipe(new ItemStack(bluebricks2,4),new Object[]{"XX","XX", 'X', new ItemStack(bluestone)});
            GameRegistry.addShapedRecipe(new ItemStack(bluebrickstairs2,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(bluebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(bluebrickslabs2,6),new Object[]{"XXX", 'X', new ItemStack(bluebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(bluebrickwalls2,6),new Object[]{"XXX","XXX", 'X', new ItemStack(bluebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(bluebrickfences2,5),new Object[]{"XYX","XYX", 'X', new ItemStack(bluebricks2), 'Y', new ItemStack(bluebrickslabs2)});
            GameRegistry.addShapedRecipe(new ItemStack(bluebricks,4),new Object[]{"XX","XX", 'X', new ItemStack(bluebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(bluebrickstairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(bluebricks)});
            GameRegistry.addShapedRecipe(new ItemStack(bluebrickslabs,6),new Object[]{"XXX", 'X', new ItemStack(bluebricks)});
            GameRegistry.addShapedRecipe(new ItemStack(bluebrickwalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(bluebricks)});
            GameRegistry.addShapedRecipe(new ItemStack(bluebrickfences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(bluebricks), 'Y', new ItemStack(bluebrickslabs)});

            GameRegistry.addShapedRecipe(new ItemStack(yellowstone,8),new Object[]{"XXX","XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust,1,2)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowstonestairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(yellowstone)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowstoneslabs,6),new Object[]{"XXX", 'X', new ItemStack(yellowstone)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowstonewalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(yellowstone)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowstonefences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(yellowstone), 'Y', new ItemStack(yellowstoneslabs)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowbricks2,4),new Object[]{"XX","XX", 'X', new ItemStack(yellowstone)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowbrickstairs2,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(yellowbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowbrickslabs2,6),new Object[]{"XXX", 'X', new ItemStack(yellowbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowbrickwalls2,6),new Object[]{"XXX","XXX", 'X', new ItemStack(yellowbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowbrickfences2,5),new Object[]{"XYX","XYX", 'X', new ItemStack(yellowbricks2), 'Y', new ItemStack(yellowbrickslabs2)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowbricks,4),new Object[]{"XX","XX", 'X', new ItemStack(yellowbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowbrickstairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(yellowbricks)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowbrickslabs,6),new Object[]{"XXX", 'X', new ItemStack(yellowbricks)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowbrickwalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(yellowbricks)});
            GameRegistry.addShapedRecipe(new ItemStack(yellowbrickfences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(yellowbricks), 'Y', new ItemStack(yellowbrickslabs)});

            GameRegistry.addShapedRecipe(new ItemStack(purplestone,8),new Object[]{"XXX","XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust,1,3)});
            GameRegistry.addShapedRecipe(new ItemStack(purplestonestairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(purplestone)});
            GameRegistry.addShapedRecipe(new ItemStack(purplestoneslabs,6),new Object[]{"XXX", 'X', new ItemStack(purplestone)});
            GameRegistry.addShapedRecipe(new ItemStack(purplestonewalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(purplestone)});
            GameRegistry.addShapedRecipe(new ItemStack(purplestonefences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(purplestone), 'Y', new ItemStack(purplestoneslabs)});
            GameRegistry.addShapedRecipe(new ItemStack(purplebricks2,4),new Object[]{"XX","XX", 'X', new ItemStack(purplestone)});
            GameRegistry.addShapedRecipe(new ItemStack(purplebrickstairs2,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(purplebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(purplebrickslabs2,6),new Object[]{"XXX", 'X', new ItemStack(purplebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(purplebrickwalls2,6),new Object[]{"XXX","XXX", 'X', new ItemStack(purplebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(purplebrickfences2,5),new Object[]{"XYX","XYX", 'X', new ItemStack(purplebricks2), 'Y', new ItemStack(purplebrickslabs2)});
            GameRegistry.addShapedRecipe(new ItemStack(purplebricks,4),new Object[]{"XX","XX", 'X', new ItemStack(purplebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(purplebrickstairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(purplebricks)});
            GameRegistry.addShapedRecipe(new ItemStack(purplebrickslabs,6),new Object[]{"XXX", 'X', new ItemStack(purplebricks)});
            GameRegistry.addShapedRecipe(new ItemStack(purplebrickwalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(purplebricks)});
            GameRegistry.addShapedRecipe(new ItemStack(purplebrickfences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(purplebricks), 'Y', new ItemStack(purplebrickslabs)});

            GameRegistry.addShapedRecipe(new ItemStack(greenstone,8),new Object[]{"XXX","XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust,1,4)});
            GameRegistry.addShapedRecipe(new ItemStack(greenstonestairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(greenstone)});
            GameRegistry.addShapedRecipe(new ItemStack(greenstoneslabs,6),new Object[]{"XXX", 'X', new ItemStack(greenstone)});
            GameRegistry.addShapedRecipe(new ItemStack(greenstonewalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(greenstone)});
            GameRegistry.addShapedRecipe(new ItemStack(greenstonefences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(greenstone), 'Y', new ItemStack(greenstoneslabs)});
            GameRegistry.addShapedRecipe(new ItemStack(greenbricks2,4),new Object[]{"XX","XX", 'X', new ItemStack(greenstone)});
            GameRegistry.addShapedRecipe(new ItemStack(greenbrickstairs2,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(greenbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(greenbrickslabs2,6),new Object[]{"XXX", 'X', new ItemStack(greenbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(greenbrickwalls2,6),new Object[]{"XXX","XXX", 'X', new ItemStack(greenbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(greenbrickfences2,5),new Object[]{"XYX","XYX", 'X', new ItemStack(greenbricks2), 'Y', new ItemStack(greenbrickslabs2)});
            GameRegistry.addShapedRecipe(new ItemStack(greenbricks,4),new Object[]{"XX","XX", 'X', new ItemStack(greenbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(greenbrickstairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(greenbricks)});
            GameRegistry.addShapedRecipe(new ItemStack(greenbrickslabs,6),new Object[]{"XXX", 'X', new ItemStack(greenbricks)});
            GameRegistry.addShapedRecipe(new ItemStack(greenbrickwalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(greenbricks)});
            GameRegistry.addShapedRecipe(new ItemStack(greenbrickfences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(greenbricks), 'Y', new ItemStack(greenbrickslabs)});

            GameRegistry.addShapedRecipe(new ItemStack(orangestone,8),new Object[]{"XXX","XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust,1,5)});
            GameRegistry.addShapedRecipe(new ItemStack(orangestonestairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(orangestone)});
            GameRegistry.addShapedRecipe(new ItemStack(orangestoneslabs,6),new Object[]{"XXX", 'X', new ItemStack(orangestone)});
            GameRegistry.addShapedRecipe(new ItemStack(orangestonewalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(orangestone)});
            GameRegistry.addShapedRecipe(new ItemStack(orangestonefences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(orangestone), 'Y', new ItemStack(orangestoneslabs)});
            GameRegistry.addShapedRecipe(new ItemStack(orangebricks2,4),new Object[]{"XX","XX", 'X', new ItemStack(orangestone)});
            GameRegistry.addShapedRecipe(new ItemStack(orangebrickstairs2,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(orangebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(orangebrickslabs2,6),new Object[]{"XXX", 'X', new ItemStack(orangebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(orangebrickwalls2,6),new Object[]{"XXX","XXX", 'X', new ItemStack(orangebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(orangebrickfences2,5),new Object[]{"XYX","XYX", 'X', new ItemStack(orangebricks2), 'Y', new ItemStack(orangebrickslabs2)});
            GameRegistry.addShapedRecipe(new ItemStack(orangebricks,4),new Object[]{"XX","XX", 'X', new ItemStack(orangebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(orangebrickstairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(orangebricks)});
            GameRegistry.addShapedRecipe(new ItemStack(orangebrickslabs,6),new Object[]{"XXX", 'X', new ItemStack(orangebricks)});
            GameRegistry.addShapedRecipe(new ItemStack(orangebrickwalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(orangebricks)});
            GameRegistry.addShapedRecipe(new ItemStack(orangebrickfences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(orangebricks), 'Y', new ItemStack(orangebrickslabs)});

            GameRegistry.addShapedRecipe(new ItemStack(whitestone,8),new Object[]{"XXX","XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust,1,6)});
            GameRegistry.addShapedRecipe(new ItemStack(whitestonestairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(whitestone)});
            GameRegistry.addShapedRecipe(new ItemStack(whitestoneslabs,6),new Object[]{"XXX", 'X', new ItemStack(whitestone)});
            GameRegistry.addShapedRecipe(new ItemStack(whitestonewalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(whitestone)});
            GameRegistry.addShapedRecipe(new ItemStack(whitestonefences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(whitestone), 'Y', new ItemStack(whitestoneslabs)});
            GameRegistry.addShapedRecipe(new ItemStack(whitebricks2,4),new Object[]{"XX","XX", 'X', new ItemStack(whitestone)});
            GameRegistry.addShapedRecipe(new ItemStack(whitebrickstairs2,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(whitebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(whitebrickslabs2,6),new Object[]{"XXX", 'X', new ItemStack(whitebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(whitebrickwalls2,6),new Object[]{"XXX","XXX", 'X', new ItemStack(whitebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(whitebrickfences2,5),new Object[]{"XYX","XYX", 'X', new ItemStack(whitebricks2), 'Y', new ItemStack(whitebrickslabs2)});
            GameRegistry.addShapedRecipe(new ItemStack(whitebricks,4),new Object[]{"XX","XX", 'X', new ItemStack(whitebricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(whitebrickstairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(whitebricks)});
            GameRegistry.addShapedRecipe(new ItemStack(whitebrickslabs,6),new Object[]{"XXX", 'X', new ItemStack(whitebricks)});
            GameRegistry.addShapedRecipe(new ItemStack(whitebrickwalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(whitebricks)});
            GameRegistry.addShapedRecipe(new ItemStack(whitebrickfences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(whitebricks), 'Y', new ItemStack(whitebrickslabs)});

            GameRegistry.addShapedRecipe(new ItemStack(blackstone,8),new Object[]{"XXX","XYX", "XXX", 'X', new ItemStack(Blocks.STONE), 'Y', new ItemStack(dust,1,7)});
            GameRegistry.addShapedRecipe(new ItemStack(blackstonestairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(blackstone)});
            GameRegistry.addShapedRecipe(new ItemStack(blackstoneslabs,6),new Object[]{"XXX", 'X', new ItemStack(blackstone)});
            GameRegistry.addShapedRecipe(new ItemStack(blackstonewalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(blackstone)});
            GameRegistry.addShapedRecipe(new ItemStack(blackstonefences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(blackstone), 'Y', new ItemStack(blackstoneslabs)});
            GameRegistry.addShapedRecipe(new ItemStack(blackbricks2,4),new Object[]{"XX","XX", 'X', new ItemStack(blackstone)});
            GameRegistry.addShapedRecipe(new ItemStack(blackbrickstairs2,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(blackbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(blackbrickslabs2,6),new Object[]{"XXX", 'X', new ItemStack(blackbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(blackbrickwalls2,6),new Object[]{"XXX","XXX", 'X', new ItemStack(blackbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(blackbrickfences2,5),new Object[]{"XYX","XYX", 'X', new ItemStack(blackbricks2), 'Y', new ItemStack(blackbrickslabs2)});
            GameRegistry.addShapedRecipe(new ItemStack(blackbricks,4),new Object[]{"XX","XX", 'X', new ItemStack(blackbricks2)});
            GameRegistry.addShapedRecipe(new ItemStack(blackbrickstairs,4),new Object[]{"X  ","XX ", "XXX", 'X', new ItemStack(blackbricks)});
            GameRegistry.addShapedRecipe(new ItemStack(blackbrickslabs,6),new Object[]{"XXX", 'X', new ItemStack(blackbricks)});
            GameRegistry.addShapedRecipe(new ItemStack(blackbrickwalls,6),new Object[]{"XXX","XXX", 'X', new ItemStack(blackbricks)});
            GameRegistry.addShapedRecipe(new ItemStack(blackbrickfences,5),new Object[]{"XYX","XYX", 'X', new ItemStack(blackbricks), 'Y', new ItemStack(blackbrickslabs)});




        }

    }




}


