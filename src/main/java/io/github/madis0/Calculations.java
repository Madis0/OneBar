package io.github.madis0;

import net.minecraft.util.math.MathHelper;

import java.text.DecimalFormat;

public class Calculations {
    public static int RelativeW(int start, int end, int value, int total){
        if(value < total)
            return MathHelper.ceil(start + ((float)(end - start) / total * value));
        else
            return end;
    }

    public static int GetPreciseInt(float number){
        float precision = 10000.0F;
        return MathHelper.ceil(number * precision);
    }

    public static String MakeFraction(int number, boolean useFractions){
        DecimalFormat df =  new DecimalFormat("0.#");

        if(useFractions)
            return df.format((float) number / 2);
        else
            return String.valueOf(number);
    }
}
