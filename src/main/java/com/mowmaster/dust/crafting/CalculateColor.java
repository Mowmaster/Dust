package com.mowmaster.dust.crafting;

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
    }

    public static int[] getRGBColorFromInt(int getINTColor)
    {
        int[] intRGB = new int[]{0,0,0};

        intRGB[0]= Math.floorDiv(((getINTColor/65536)%256),1);
        intRGB[1]=Math.floorDiv(((getINTColor/256)%256),1);
        intRGB[2]=Math.floorDiv((getINTColor%256),1);

        return intRGB;
    }


}
