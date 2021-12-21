package com.mowmaster.dust.Block.BlockEntities.DustJar;

import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import static com.mowmaster.dust.References.Constants.MODID;

public class DustJarBlockItem extends BlockItem {
    public DustJarBlockItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    public static int getFillLevel(ItemStack stack) {

        // 0 - Empty
        // 1 - Has Dust
        // 2 - 10%
        // 3 - 20%
        // 4 - 30%
        // 5 - 40%
        // 6 - 50%
        // 7 - 60%
        // 8 - 70%
        // 9 - 80%
        // 10 - 90%
        // 11 - Full

        DustMagic getJarMagic = DustMagic.getDustMagicInItemStack(stack);
        double amount = (double)getJarMagic.getDustAmount();
        double capacity = (stack.hasTag())?((stack.getTag().contains(MODID + "_dustCapacity"))?(stack.getTag().getInt(MODID + "_dustCapacity")):(1000)):(1000);
        double heightRenderMultiplier = (double)(10.0 * (amount/capacity));

        if(heightRenderMultiplier<=0)return 0;
        else if(heightRenderMultiplier<=0.99)return 1;
        else if(heightRenderMultiplier<=1.99)return 2;
        else if(heightRenderMultiplier<=2.99)return 3;
        else if(heightRenderMultiplier<=3.99)return 4;
        else if(heightRenderMultiplier<=4.99)return 5;
        else if(heightRenderMultiplier<=5.99)return 6;
        else if(heightRenderMultiplier<=6.99)return 7;
        else if(heightRenderMultiplier<=7.99)return 8;
        else if(heightRenderMultiplier<=8.99)return 9;
        else if(heightRenderMultiplier<=9.99)return 10;
        else if(heightRenderMultiplier>=10.0)return 11;
        else return 0;
    }

    public static int getFillColor(ItemStack stack) {
        DustMagic getJarMagic = DustMagic.getDustMagicInItemStack(stack);
        return getJarMagic.getDustColor();
    }


}
