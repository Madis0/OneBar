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

        if(config.textSettings.useFractions)
            return df.format((float) number / 2);
        else
            return String.valueOf(number);
    }

    public static int GetEstimatedHealthRegen(int constant, int rawLevel, int duration, int currentHealth, int maxHealth){
        // See canApplyUpdateEffect and https://www.geeksforgeeks.org/bitwise-shift-operators-in-java/
        int ticks = constant >> rawLevel;
        float healthPerSec = 20 / (float)ticks;
        float durationPerSec = (float)duration / 20;
        int addedHealth = (int)(healthPerSec * durationPerSec);
        int newHealth = currentHealth + addedHealth;
        return Math.min(newHealth, maxHealth);
    }

    public static int GetEstimatedHealthDamage(int constant, int rawLevel, int duration, int currentHealth, int minHealth){
        int ticks = constant >> rawLevel;
        float healthPerSec = 20 / (float)ticks;
        float durationPerSec = (float)duration / 20;
        int removedHealth = (int)(healthPerSec * durationPerSec);
        int newHealth = currentHealth - removedHealth;
        return Math.max(newHealth, minHealth);
    }
}
