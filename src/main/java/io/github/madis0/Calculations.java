package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.util.math.MathHelper;
import java.text.DecimalFormat;

public class Calculations {
    private static ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

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

    public static String MakeFraction(int number){
        DecimalFormat df =  new DecimalFormat("0.#");

        if(config.experimental.useFractions)
            return df.format((float) number / 2);
        else
            return String.valueOf(number);
    }
}
