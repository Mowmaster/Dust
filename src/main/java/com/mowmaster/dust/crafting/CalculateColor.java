package com.mowmaster.dust.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.command.TextComponentHelper;

public class CalculateColor
{

    public static int getColorFromRGB(double red, double green, double blue)
    {
        int rgbInt = 0;
        int r = 0;
        int g = 0;
        int b = 0;

        //System.out.println("Red: "+red);
        //System.out.println("Green: "+green);
        //System.out.println("Blue: "+blue);
        r= rounder((red*65536));
        g= rounder(green*256);
        b= rounder(blue);

        rgbInt = r+g+b;

        return rgbInt;
    }

    public static int rounder(double numIn)
    {
        //System.out.println("NumIn: "+numIn);
        int numOut = 0;

        double numProc = Math.floor((numIn - Math.floor(numIn))*10);
        //System.out.println(numProc);
        if(numProc>=5)
        {
            numOut = (int)Math.floor((double)numIn) + 1;
        }
        else {
            numOut = (int)Math.floor((double)numIn);
        }

        return numOut;
    }

    public static double getColorValueFromDust(int count)
    {
        double colorcount = 0;

        if(count>0)
        {
            //System.out.println("Color: "+count);
            double value1 = (count%3);
            if(value1 == 0.0d)
            {
                value1 = 3;
            }
            //System.out.println("Value: "+value1);
            colorcount=(value1*(85.33d))-1;
            //System.out.println("ColorCount: "+colorcount);
        }

        return colorcount;
    }


    /*public static double getColorValueFromDust(int count)
    {
        double colorcount = 0;

        if(count>0)
        {
            //System.out.println("Color: "+count);
            double value1 = (count%5);
            if(value1 == 0.0d)
            {
                value1 = 5;
            }
            //System.out.println("Value: "+value1);
            colorcount=(value1*(51.2d))-1;
            //System.out.println("ColorCount: "+colorcount);
        }

        return colorcount;
    }*/

    public static int[] getRGBColorFromInt(int getINTColor)
    {
        int[] intRGB = new int[]{0,0,0};

        intRGB[0]= Math.floorDiv(((getINTColor/65536)%256),1);
        intRGB[1]=Math.floorDiv(((getINTColor/256)%256),1);
        intRGB[2]=Math.floorDiv((getINTColor%256),1);

        return intRGB;
    }

    /*public static double[] getRGBColorFromStack(ItemStack stack)
    {

        int INTColor = stack.getTag().getInt("color");
        int counted = stack.getCount();
        double[] intRGB = new double[]{0.0,0.0,0.0};
        double r=getRGBColorFromInt(INTColor)[0];
        double g=getRGBColorFromInt(INTColor)[1];
        double b=getRGBColorFromInt(INTColor)[2];
        *//*if(r>0.0)r+=1;
        if(g>0.0)g+=1;
        if(b>0.0)b+=1;*//*

        double red = r/5;
        double green = g/5;
        double blue = b/5;

        double value1 = (counted%5);
        if(value1 == 0.0d)
        {
            value1 = 5;
        }

        intRGB[0]= (value1*red);
        intRGB[1]= (value1*green);
        intRGB[2]= (value1*blue);

        *//*if(intRGB[0]>0.0d)intRGB[0]-=1;
        if(intRGB[1]>0.0d)intRGB[1]-=1;
        if(intRGB[2]>0.0d)intRGB[2]-=1;*//*

        return intRGB;
    }*/

    public static double[] getRGBColorFromStack(ItemStack stack)
    {
        int INTColor = stack.getTag().getInt("color");
        int counted = stack.getCount();
        double[] intRGB = new double[]{0.0,0.0,0.0};
        double r=getRGBColorFromInt(INTColor)[0];
        double g=getRGBColorFromInt(INTColor)[1];
        double b=getRGBColorFromInt(INTColor)[2];
        /*if(r>0.0)r+=1;
        if(g>0.0)g+=1;
        if(b>0.0)b+=1;*/

        double red = r/3;
        double green = g/3;
        double blue = b/3;

        double value1 = (counted%3);
        if(value1 == 0.0d)
        {
            value1 = 3;
        }

        intRGB[0]= (value1*red);
        intRGB[1]= (value1*green);
        intRGB[2]= (value1*blue);

        /*if(intRGB[0]>0.0d)intRGB[0]-=1;
        if(intRGB[1]>0.0d)intRGB[1]-=1;
        if(intRGB[2]>0.0d)intRGB[2]-=1;*/

        return intRGB;
    }

}
