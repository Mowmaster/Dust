package com.mowmaster.dust.References;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.mowmaster.dust.References.Constants.MODID;

public class ColorReference
{
    public static int getColor(List<Integer> listRGB)
    {
        Map<List<Integer>,Integer> COLORS_REFERENCE = Map.ofEntries(
                Map.entry(Arrays.asList(0,0,0),2763306),
                Map.entry(Arrays.asList(0,0,1),85),
                Map.entry(Arrays.asList(0,0,2),170),
                Map.entry(Arrays.asList(0,0,3),255),
                Map.entry(Arrays.asList(0,1,0),21760),
                Map.entry(Arrays.asList(0,1,1),21845),
                Map.entry(Arrays.asList(0,1,2),21930),
                Map.entry(Arrays.asList(0,1,3),22015),
                Map.entry(Arrays.asList(0,2,0),43520),
                Map.entry(Arrays.asList(0,2,1),43605),
                Map.entry(Arrays.asList(0,2,2),43690),
                Map.entry(Arrays.asList(0,2,3),43775),
                Map.entry(Arrays.asList(0,3,0),65280),
                Map.entry(Arrays.asList(0,3,1),65365),
                Map.entry(Arrays.asList(0,3,2),65450),
                Map.entry(Arrays.asList(0,3,3),65535),
                Map.entry(Arrays.asList(1,0,0),5570560),
                Map.entry(Arrays.asList(1,0,1),5570645),
                Map.entry(Arrays.asList(1,0,2),5570730),
                Map.entry(Arrays.asList(1,0,3),5570815),
                Map.entry(Arrays.asList(1,1,0),5592320),
                Map.entry(Arrays.asList(1,1,1),5592405),
                Map.entry(Arrays.asList(1,1,2),5592490),
                Map.entry(Arrays.asList(1,1,3),5592575),
                Map.entry(Arrays.asList(1,2,0),5614080),
                Map.entry(Arrays.asList(1,2,1),5614165),
                Map.entry(Arrays.asList(1,2,2),5614250),
                Map.entry(Arrays.asList(1,2,3),5614335),
                Map.entry(Arrays.asList(1,3,0),5635840),
                Map.entry(Arrays.asList(1,3,1),5635925),
                Map.entry(Arrays.asList(1,3,2),5636010),
                Map.entry(Arrays.asList(1,3,3),5636095),
                Map.entry(Arrays.asList(2,0,0),11141120),
                Map.entry(Arrays.asList(2,0,1),11141205),
                Map.entry(Arrays.asList(2,0,2),11141290),
                Map.entry(Arrays.asList(2,0,3),11141375),
                Map.entry(Arrays.asList(2,1,0),11162880),
                Map.entry(Arrays.asList(2,1,1),11162965),
                Map.entry(Arrays.asList(2,1,2),11163050),
                Map.entry(Arrays.asList(2,1,3),11163135),
                Map.entry(Arrays.asList(2,2,0),11184640),
                Map.entry(Arrays.asList(2,2,1),11184725),
                Map.entry(Arrays.asList(2,2,2),11184810),
                Map.entry(Arrays.asList(2,2,3),11184895),
                Map.entry(Arrays.asList(2,3,0),11206400),
                Map.entry(Arrays.asList(2,3,1),11206485),
                Map.entry(Arrays.asList(2,3,2),11206570),
                Map.entry(Arrays.asList(2,3,3),11206655),
                Map.entry(Arrays.asList(3,0,0),16711680),
                Map.entry(Arrays.asList(3,0,1),16711765),
                Map.entry(Arrays.asList(3,0,2),16711850),
                Map.entry(Arrays.asList(3,0,3),16711935),
                Map.entry(Arrays.asList(3,1,0),16733440),
                Map.entry(Arrays.asList(3,1,1),16733525),
                Map.entry(Arrays.asList(3,1,2),16733610),
                Map.entry(Arrays.asList(3,1,3),16733695),
                Map.entry(Arrays.asList(3,2,0),16755200),
                Map.entry(Arrays.asList(3,2,1),16755285),
                Map.entry(Arrays.asList(3,2,2),16755370),
                Map.entry(Arrays.asList(3,2,3),16755455),
                Map.entry(Arrays.asList(3,3,0),16776960),
                Map.entry(Arrays.asList(3,3,1),16777045),
                Map.entry(Arrays.asList(3,3,2),16777130),
                Map.entry(Arrays.asList(3,3,3),16777215)
        );

        return COLORS_REFERENCE.getOrDefault(listRGB,16777215);
    }

    public static List<Integer> getIntColor(int color)
    {
        Map<Integer,List<Integer>> COLORS_INT_REFERENCE = Map.ofEntries(
                Map.entry(2763306,Arrays.asList(0,0,0)),
                Map.entry(85,Arrays.asList(0,0,1)),
                Map.entry(170,Arrays.asList(0,0,2)),
                Map.entry(255,Arrays.asList(0,0,3)),
                Map.entry(21760,Arrays.asList(0,1,0)),
                Map.entry(21845,Arrays.asList(0,1,1)),
                Map.entry(21930,Arrays.asList(0,1,2)),
                Map.entry(22015,Arrays.asList(0,1,3)),
                Map.entry(43520,Arrays.asList(0,2,0)),
                Map.entry(43605,Arrays.asList(0,2,1)),
                Map.entry(43690,Arrays.asList(0,2,2)),
                Map.entry(43775,Arrays.asList(0,2,3)),
                Map.entry(65280,Arrays.asList(0,3,0)),
                Map.entry(65365,Arrays.asList(0,3,1)),
                Map.entry(65450,Arrays.asList(0,3,2)),
                Map.entry(65535,Arrays.asList(0,3,3)),
                Map.entry(5570560,Arrays.asList(1,0,0)),
                Map.entry(5570645,Arrays.asList(1,0,1)),
                Map.entry(5570730,Arrays.asList(1,0,2)),
                Map.entry(5570815,Arrays.asList(1,0,3)),
                Map.entry(5592320,Arrays.asList(1,1,0)),
                Map.entry(5592405,Arrays.asList(1,1,1)),
                Map.entry(5592490,Arrays.asList(1,1,2)),
                Map.entry(5592575,Arrays.asList(1,1,3)),
                Map.entry(5614080,Arrays.asList(1,2,0)),
                Map.entry(5614165,Arrays.asList(1,2,1)),
                Map.entry(5614250,Arrays.asList(1,2,2)),
                Map.entry(5614335,Arrays.asList(1,2,3)),
                Map.entry(5635840,Arrays.asList(1,3,0)),
                Map.entry(5635925,Arrays.asList(1,3,1)),
                Map.entry(5636010,Arrays.asList(1,3,2)),
                Map.entry(5636095,Arrays.asList(1,3,3)),
                Map.entry(11141120,Arrays.asList(2,0,0)),
                Map.entry(11141205,Arrays.asList(2,0,1)),
                Map.entry(11141290,Arrays.asList(2,0,2)),
                Map.entry(11141375,Arrays.asList(2,0,3)),
                Map.entry(11162880,Arrays.asList(2,1,0)),
                Map.entry(11162965,Arrays.asList(2,1,1)),
                Map.entry(11163050,Arrays.asList(2,1,2)),
                Map.entry(11163135,Arrays.asList(2,1,3)),
                Map.entry(11184640,Arrays.asList(2,2,0)),
                Map.entry(11184725,Arrays.asList(2,2,1)),
                Map.entry(11184810,Arrays.asList(2,2,2)),
                Map.entry(11184895,Arrays.asList(2,2,3)),
                Map.entry(11206400,Arrays.asList(2,3,0)),
                Map.entry(11206485,Arrays.asList(2,3,1)),
                Map.entry(11206570,Arrays.asList(2,3,2)),
                Map.entry(11206655,Arrays.asList(2,3,3)),
                Map.entry(16711680,Arrays.asList(3,0,0)),
                Map.entry(16711765,Arrays.asList(3,0,1)),
                Map.entry(16711850,Arrays.asList(3,0,2)),
                Map.entry(16711935,Arrays.asList(3,0,3)),
                Map.entry(16733440,Arrays.asList(3,1,0)),
                Map.entry(16733525,Arrays.asList(3,1,1)),
                Map.entry(16733610,Arrays.asList(3,1,2)),
                Map.entry(16733695,Arrays.asList(3,1,3)),
                Map.entry(16755200,Arrays.asList(3,2,0)),
                Map.entry(16755285,Arrays.asList(3,2,1)),
                Map.entry(16755370,Arrays.asList(3,2,2)),
                Map.entry(16755455,Arrays.asList(3,2,3)),
                Map.entry(16776960,Arrays.asList(3,3,0)),
                Map.entry(16777045,Arrays.asList(3,3,1)),
                Map.entry(16777130,Arrays.asList(3,3,2)),
                Map.entry(16777215,Arrays.asList(3,3,3))
        );

        return COLORS_INT_REFERENCE.getOrDefault(color,Arrays.asList(3,3,3));
    }

    public static ItemStack addColorToItemStackRGB(ItemStack stackIn, int r, int g, int b)
    {
        CompoundTag blockColors = stackIn.getOrCreateTag();
        blockColors.putIntArray(MODID+"_color_rgb",Arrays.asList(r,g,b));
        stackIn.setTag(blockColors);
        return stackIn;
    }

    public static ItemStack addColorToItemStackInt(ItemStack stackIn, int color)
    {
        List<Integer> rbgColor= getIntColor(color);
        CompoundTag blockColors = stackIn.getOrCreateTag();
        blockColors.putIntArray(MODID+"_color_int",Arrays.asList(rbgColor.get(0),rbgColor.get(1),rbgColor.get(2)));
        stackIn.setTag(blockColors);
        return stackIn;
    }

    public static int getColorFromItemStackInt(ItemStack stackIn)
    {
        CompoundTag blockColors = stackIn.getOrCreateTag();
        if(blockColors.contains(MODID+"_color_int"))
        {
            int[] colors = blockColors.getIntArray(MODID+"_color_int");
            if(colors.length==3)return ColorReference.getColor(Arrays.asList(colors[0],colors[1],colors[2]));
            else if(colors.length==4)return ColorReference.getColor(Arrays.asList(colors[1],colors[2],colors[3]));
            else return ColorReference.getColor(Arrays.asList(3,3,3));
        }

        return 16777215;
    }

    public static int[] getColorFromItemStackRGB(ItemStack stackIn)
    {
        CompoundTag blockColors = stackIn.getOrCreateTag();
        if(blockColors.contains(MODID+"_color_rgb"))
        {
            int[] colors = blockColors.getIntArray(MODID+"_color_rgb");
            return colors;
        }

        return new int[]{3,3,3};
    }

    public static final IntegerProperty COLOR_RED = IntegerProperty.create("color_red", 0, 3);
    public static final IntegerProperty COLOR_GREEN = IntegerProperty.create("color_green", 0, 3);
    public static final IntegerProperty COLOR_BLUE = IntegerProperty.create("color_blue", 0, 3);


    public static List<Integer> getRGBFromState(BlockState state)
    {
        int r = 0;
        int g = 0;
        int b = 0;
        if(state.getBlock().getRegistryName().getNamespace().equals(MODID))
        {
            if(state.hasProperty(COLOR_RED))r=state.getValue(COLOR_RED);
            if(state.hasProperty(COLOR_GREEN))g=state.getValue(COLOR_GREEN);
            if(state.hasProperty(COLOR_BLUE))b=state.getValue(COLOR_BLUE);
        }
        return Arrays.asList(r,g,b);
    }


}
