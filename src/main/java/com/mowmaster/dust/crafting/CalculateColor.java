package com.mowmaster.dust.crafting;

public class CalculateColor
{

    public static int getColorFromRGB(int red, int green, int blue)
    {
        int rgbInt = 0;
        int r = 0;
        int g = 0;
        int b = 0;

        r= Math.multiplyExact(red,65536);
        g= Math.multiplyExact(green,256);
        b= blue;

        rgbInt = r+g+b;

        return rgbInt;
    }



    public static int getColorValueFromDust(int count)
    {
        float colorcount = 0;
        int colorINT = 0;

        if(count>0)
        {
            //System.out.println("Color: "+count);
            float value1 = (count%6);
            if(value1 == 0.0f)
            {
                value1 = 6;
            }
            //System.out.println("Value: "+value1);
            colorcount=(value1*(42.66f))-1;
            //System.out.println("ColorCount: "+colorcount);
        }

        colorINT = Math.round(colorcount);

        //1red 41
        //2red 84
        //3red 127
        //4red
        //5red
        //6red

        return colorINT;
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
