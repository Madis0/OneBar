package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.util.math.MathHelper;
import java.text.DecimalFormat;

public class Calculations {
    private static final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    public static int RelativeW(int start, int end, int value, int total){
        if(value < total)
            return MathHelper.ceil(start + ((float)(end - start) / total * value));
        else
            return end;
    }

    public static int GetPreciseInt(float number){
        return MathHelper.ceil(number * 10000.0F);
    }

    public static String MakeFraction(int number, boolean italic){
        DecimalFormat df =  new DecimalFormat("0.#");
        String result;

        if(config.textSettings.useFractions)
            result = df.format((float) number / 2);
        else
            result = String.valueOf(number);

        if(number < 0) // Replace minus with hyphen (\u8208) to make it 2 px shorter and therefore more aesthetic
            result = result.replace("-", "‐");

        if(italic)
            result = "§o" + result + "§r";
        
        return result;
    }

    public static int GetEstimatedHealthDelta(int constant, int rawLevel, int duration){
        // See canApplyUpdateEffect and https://www.geeksforgeeks.org/bitwise-shift-operators-in-java/
        int ticks = Math.max(constant >> rawLevel, 1);
        float healthPerSec = 20 / (float)ticks;
        float durationPerSec = (float)duration / 20;
        return (int)(healthPerSec * durationPerSec);
    }

    public static int GetEstimatedHealthRegen(int constant, int rawLevel, int duration, int currentHealth, int maxHealth){
        return Math.min(currentHealth + GetEstimatedHealthDelta(constant, rawLevel, duration), maxHealth);
    }

    public static int GetEstimatedHealthDamage(int constant, int rawLevel, int duration, int currentHealth, int minHealth){
        return Math.max(currentHealth - GetEstimatedHealthDelta(constant, rawLevel, duration), minHealth);
    }

    public static int GetNaturalRegenAddition(float rawSaturation, int hunger){
        // Approximate formula for calculating regeneration addition health: saturation + (2.5 - hunger) * exhaustion max / 6 exhaustion per healed heart
        return MathHelper.ceil(((rawSaturation + (float)(2.5 - hunger)) * (float)4 / (float)6));
    }
}
