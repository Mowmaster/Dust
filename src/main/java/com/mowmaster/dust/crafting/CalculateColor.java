package com.mowmaster.dust.crafting;

public class CalculateColor
{

    public static int getColorFromRGB(float red, float green, float blue)
    {
        int rgbInt = 0;
        int r = 0;
        int g = 0;
        int b = 0;

        r= Math.round(red*65536);
        g= Math.round(green*256);
        b= Math.round(blue);

        rgbInt = r+g+b;

        return rgbInt;
    }



    public static float getColorValueFromDust(int count)
    {
        float colorcount = 0;

        if(count>0)
        {
            //System.out.println("Color: "+count);
            float value1 = (count%5);
            if(value1 == 0.0f)
            {
                value1 = 5;
            }
            //System.out.println("Value: "+value1);
            colorcount=(value1*(51.2f))-1;
            //System.out.println("ColorCount: "+colorcount);
        }

        //1red 41
        //2red 84
        //3red 127
        //4red
        //5red
        //6red

        return colorcount;
    }

    public static int[] getRGBColorFromInt(int getINTColor)
    {
        int[] intRGB = {};
        int r = 0;
        int g = 0;
        int b = 0;

        intRGB[0]= Math.floorDiv(((getINTColor/65536)%256),1);
        intRGB[1]=Math.floorDiv(((getINTColor/256)%256),1);
        intRGB[2]=Math.floorDiv((getINTColor%256),1);

        return intRGB;
    }


}
