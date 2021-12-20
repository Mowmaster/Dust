package com.mowmaster.dust.References;


import com.mowmaster.dust.World.GeodeGen.GeodeFeaturesRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.mowmaster.dust.References.Constants.MODID;
import static com.mowmaster.dust.World.GeodeGen.GeodeFeatures.*;
import static com.mowmaster.dust.World.GeodeGen.GeodeFeatures.GEODE_333;

public class ColorReference
{
    public static final IntegerProperty COLOR_RED = IntegerProperty.create("color_red", 0, 3);
    public static final IntegerProperty COLOR_GREEN = IntegerProperty.create("color_green", 0, 3);
    public static final IntegerProperty COLOR_BLUE = IntegerProperty.create("color_blue", 0, 3);
    public static final int DEFAULTCOLOR = 16777215;
    public static final List<Integer> DEFAULTCOLORLIST = Arrays.asList(3,3,3);


    //Map all out entries out, this saves us from wierd maths and allows us to set specific values for combinations.
    //Could maybe eventually rely on a custom recipe handler for this? but this might be faster???

    public static int getColor(List<Integer> listRGB)
    {
        Random rand = new Random();
        List<Integer> listedRGB = (listRGB.equals(Arrays.asList(-1,-1,-1)))?(Arrays.asList(rand.nextInt(3),rand.nextInt(3),rand.nextInt(3))):(listRGB);
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

        return COLORS_REFERENCE.getOrDefault(listedRGB,DEFAULTCOLOR);
    }

    public static List<Integer> getIntColor(int color)
    {
        Random rand = new Random();

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

        return (color ==-1)?(Arrays.asList(rand.nextInt(3),rand.nextInt(3),rand.nextInt(3))):(COLORS_INT_REFERENCE.getOrDefault(color,DEFAULTCOLORLIST));
    }


    public static String getColorName(int color)
    {
        Random rand = new Random();

        Map<Integer,String> COLORS_INT_REFERENCE = Map.ofEntries(
                Map.entry(2763306,MODID + "_colorName000"),
                Map.entry(85,MODID + "_colorName001"),
                Map.entry(170,MODID + "_colorName002"),
                Map.entry(255,MODID + "_colorName003"),
                Map.entry(21760,MODID + "_colorName010"),
                Map.entry(21845,MODID + "_colorName011"),
                Map.entry(21930,MODID + "_colorName012"),
                Map.entry(22015,MODID + "_colorName013"),
                Map.entry(43520,MODID + "_colorName020"),
                Map.entry(43605,MODID + "_colorName021"),
                Map.entry(43690,MODID + "_colorName022"),
                Map.entry(43775,MODID + "_colorName023"),
                Map.entry(65280,MODID + "_colorName030"),
                Map.entry(65365,MODID + "_colorName031"),
                Map.entry(65450,MODID + "_colorName032"),
                Map.entry(65535,MODID + "_colorName033"),
                Map.entry(5570560,MODID + "_colorName100"),
                Map.entry(5570645,MODID + "_colorName101"),
                Map.entry(5570730,MODID + "_colorName102"),
                Map.entry(5570815,MODID + "_colorName103"),
                Map.entry(5592320,MODID + "_colorName110"),
                Map.entry(5592405,MODID + "_colorName111"),
                Map.entry(5592490,MODID + "_colorName112"),
                Map.entry(5592575,MODID + "_colorName113"),
                Map.entry(5614080,MODID + "_colorName120"),
                Map.entry(5614165,MODID + "_colorName121"),
                Map.entry(5614250,MODID + "_colorName122"),
                Map.entry(5614335,MODID + "_colorName123"),
                Map.entry(5635840,MODID + "_colorName130"),
                Map.entry(5635925,MODID + "_colorName131"),
                Map.entry(5636010,MODID + "_colorName132"),
                Map.entry(5636095,MODID + "_colorName133"),
                Map.entry(11141120,MODID + "_colorName200"),
                Map.entry(11141205,MODID + "_colorName201"),
                Map.entry(11141290,MODID + "_colorName202"),
                Map.entry(11141375,MODID + "_colorName203"),
                Map.entry(11162880,MODID + "_colorName210"),
                Map.entry(11162965,MODID + "_colorName211"),
                Map.entry(11163050,MODID + "_colorName212"),
                Map.entry(11163135,MODID + "_colorName213"),
                Map.entry(11184640,MODID + "_colorName220"),
                Map.entry(11184725,MODID + "_colorName221"),
                Map.entry(11184810,MODID + "_colorName222"),
                Map.entry(11184895,MODID + "_colorName223"),
                Map.entry(11206400,MODID + "_colorName230"),
                Map.entry(11206485,MODID + "_colorName231"),
                Map.entry(11206570,MODID + "_colorName232"),
                Map.entry(11206655,MODID + "_colorName233"),
                Map.entry(16711680,MODID + "_colorName300"),
                Map.entry(16711765,MODID + "_colorName301"),
                Map.entry(16711850,MODID + "_colorName302"),
                Map.entry(16711935,MODID + "_colorName303"),
                Map.entry(16733440,MODID + "_colorName310"),
                Map.entry(16733525,MODID + "_colorName311"),
                Map.entry(16733610,MODID + "_colorName312"),
                Map.entry(16733695,MODID + "_colorName313"),
                Map.entry(16755200,MODID + "_colorName320"),
                Map.entry(16755285,MODID + "_colorName321"),
                Map.entry(16755370,MODID + "_colorName322"),
                Map.entry(16755455,MODID + "_colorName323"),
                Map.entry(16776960,MODID + "_colorName330"),
                Map.entry(16777045,MODID + "_colorName331"),
                Map.entry(16777130,MODID + "_colorName332"),
                Map.entry(16777215,MODID + "_colorName333")
        );

        return COLORS_INT_REFERENCE.getOrDefault(color,COLORS_INT_REFERENCE.get(getColor(Arrays.asList(rand.nextInt(3),rand.nextInt(3),rand.nextInt(3)))));
    }




    public static List<Integer> ALL_COLORS = Arrays.asList(
            2763306,
            85,
            170,
            255,
            21760,
            21845,
            21930,
            22015,
            43520,
            43605,
            43690,
            43775,
            65280,
            65365,
            65450,
            65535,
            5570560,
            5570645,
            5570730,
            5570815,
            5592320,
            5592405,
            5592490,
            5592575,
            5614080,
            5614165,
            5614250,
            5614335,
            5635840,
            5635925,
            5636010,
            5636095,
            11141120,
            11141205,
            11141290,
            11141375,
            11162880,
            11162965,
            11163050,
            11163135,
            11184640,
            11184725,
            11184810,
            11184895,
            11206400,
            11206485,
            11206570,
            11206655,
            16711680,
            16711765,
            16711850,
            16711935,
            16733440,
            16733525,
            16733610,
            16733695,
            16755200,
            16755285,
            16755370,
            16755455,
            16776960,
            16777045,
            16777130,
            16777215
    );

    /*
    We need to probably store all itemstack color data as an int value
    the blockstates will always be RGB, but thats fine, this reference should be able to help unify things
     */

    public static ItemStack addColorToItemStack(ItemStack stackIn, int r, int g, int b)
    {
        CompoundTag blockColors = stackIn.getOrCreateTag();
        blockColors.putInt(MODID+"_color",getColor(Arrays.asList(r,g,b)));
        stackIn.setTag(blockColors);
        return stackIn;
    }

    public static ItemStack addColorToItemStack(ItemStack stackIn, int color)
    {
        CompoundTag blockColors = stackIn.getOrCreateTag();
        blockColors.putInt(MODID+"_color",color);
        stackIn.setTag(blockColors);
        return stackIn;
    }

    public static BlockState addColorToBlockState(BlockState stateIn, int color)
    {
        List<Integer> getColor = getIntColor(color);
        BlockState newState = stateIn.setValue(COLOR_RED,getColor.get(0)).setValue(COLOR_GREEN,getColor.get(1)).setValue(COLOR_BLUE,getColor.get(2));
        return newState;
    }

    public static BlockState addColorToBlockState(BlockState stateIn, int red, int green, int blue)
    {
        BlockState newState = stateIn.setValue(COLOR_RED,red).setValue(COLOR_GREEN,green).setValue(COLOR_BLUE,blue);
        return newState;
    }

    public static int getColorFromStateInt(BlockState state)
    {
        return getColor(getColorFromStateList(state));
    }

    public static List<Integer> getColorFromStateList(BlockState state)
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

    public static int getColorFromItemStackInt(ItemStack stackIn)
    {
        CompoundTag blockColors = stackIn.getOrCreateTag();
        if(blockColors.contains(MODID+"_color"))
        {
            int color = blockColors.getInt(MODID+"_color");
            return color;
        }

        return DEFAULTCOLOR;
    }

    public static int getColorFromTag(CompoundTag tag)
    {
        CompoundTag blockColors = tag;
        if(blockColors.contains(MODID+"_color"))
        {
            int color = blockColors.getInt(MODID+"_color");
            return color;
        }

        return DEFAULTCOLOR;
    }

    public static List<Integer> getColorFromItemStackRGBList(ItemStack stackIn)
    {
        return getIntColor(getColorFromItemStackInt(stackIn));
    }

    public static List<Integer> mixColorsList(int r1, int g1, int b1, int r2, int g2, int b2)
    {
        Random rand = new Random();

        //333 color increases intensity by 1
        //000 color decreases intensity by 1
        int luck = rand.nextInt(10);
        int luckR = rand.nextInt(10);
        int luckG = rand.nextInt(10);
        int luckB = rand.nextInt(10);
        int luckRR = rand.nextInt(10);
        int luckGG = rand.nextInt(10);
        int luckBB = rand.nextInt(10);

        boolean is333 = (r1+g1+b1 == 9 || r2+g2+b2 == 9)?((luck>=5)?(true):(false)):(false);
        boolean is000 = (r1+g1+b1 == 0 || r2+g2+b2 == 0)?((luck>=5)?(true):(false)):(false);

        double redIntensity = (is333 || is000)?((r1+g1+b1 == 9 || r1+g1+b1 == 0)?(r2/3):(r1/3)):((r1==0 || r2==0)?((r1>=r2 && !is333)?(r1/3):(r2/3)):((r1+r2)/6));
        double greenIntensity = (is333 || is000)?((r1+g1+b1 == 9 || r1+g1+b1 == 0)?(g2/3):(g1/3)):((g1==0 || g2==0)?((g1>=g2 && !is333)?(g1/3):(g2/3)):((g1+g2)/6));
        double blueIntensity = (is333 || is000)?((r1+g1+b1 == 9 || r1+g1+b1 == 0)?(b2/3):(b1/3)):((b1==0 || b2==0)?((b1>=b2 && !is333)?(b1/3):(b2/3)):((b1+b2)/6));
        double totalIntensity = (((double)(r1+g1+b1+r2+g2+b2))/18.0);

        int R = ((((g1==3 && g2==3)||(b1==3 && b2==3)) ) )?((int)Math.round(((redIntensity*3)*totalIntensity) + ((luckRR>=5)?(-0.1):(0.1)))):((int)Math.round(redIntensity*3));
        int G = ((((r1==3 && r2==3)||(b1==3 && b2==3)) ) )?((int)Math.round(((greenIntensity*3)*totalIntensity) + ((luckGG>=5)?(-0.1):(0.1)))):((int)Math.round(greenIntensity*3));
        int B = ((((g1==3 && g2==3)||(r1==3 && r2==3)) ) )?((int)Math.round(((blueIntensity*3)*totalIntensity) + ((luckBB>=5)?(-0.1):(0.1)))):((int)Math.round(blueIntensity*3));

        R+= ((is333)?((luckR>=5)?(1):(0)):(0)) + ((is000)?((luckR>=5)?(-1):(0)):(0));
        G+= ((is333)?((luckG>=5)?(1):(0)):(0)) + ((is000)?((luckG>=5)?(-1):(0)):(0));
        B+= ((is333)?((luckB>=5)?(1):(0)):(0)) + ((is000)?((luckB>=5)?(-1):(0)):(0));

        int red = (R>3)?(3):((R<0)?(0):(R));
        int green = (G>3)?(3):((G<0)?(0):(G));
        int blue = (B>3)?(3):((B<0)?(0):(B));

        return Arrays.asList(red,green,blue);
    }

    public static List<Integer> mixColorsList(int color1, int color2)
    {
        List<Integer> color1List = getIntColor(color1);
        List<Integer> color2List = getIntColor(color2);

        return mixColorsList(color1List.get(0), color1List.get(1), color1List.get(2), color2List.get(0), color2List.get(1), color2List.get(2));
    }

    public static int mixColorsInt(int r1, int g1, int b1, int r2, int g2, int b2)
    {
        return getColor(mixColorsList(r1, g1, b1, r2, g2, b2));
    }

    public static int mixColorsInt(int color1, int color2)
    {
        List<Integer> color1List = getIntColor(color1);
        List<Integer> color2List = getIntColor(color2);
        List<Integer> colorOutput = mixColorsList(color1List.get(0), color1List.get(1), color1List.get(2), color2List.get(0), color2List.get(1), color2List.get(2));

        return getColor(colorOutput);
    }


    /*
    *
    * RGB to Int is
    * r*65536 + g*256 + b
     */
    public static List<Integer> getTrueColorFromInt(int color)
    {
        int red=Math.floorDiv(((color/65536)%256),1);
        int green=Math.floorDiv(((color/256)%256),1);
        int blue=Math.floorDiv((color%256),1);

        return Arrays.asList(red,green,blue);
    }






}
