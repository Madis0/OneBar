package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.util.math.MathHelper;
import java.text.DecimalFormat;

public class Calculations {
    private static final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    public static int RelativeW(int start, int end, int value, int total){
        return value < total ? MathHelper.ceil(start + ((float) (end - start) / total * value)) : end;
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

    public static float GetEstimatedHealthDelta(int constant, int rawLevel, int duration){
        // See canApplyUpdateEffect and https://www.geeksforgeeks.org/bitwise-shift-operators-in-java/
        int ticks = Math.max(constant >> rawLevel, 1);
        float healthPerSec = 20 / (float)ticks;
        float durationPerSec = (float)duration / 20;
        return healthPerSec * durationPerSec;
    }

    public static float GetEstimatedHealthRegen(int constant, int rawLevel, int duration, float currentRawHealth, float maxRawHealth){
        return Math.min(currentRawHealth + GetEstimatedHealthDelta(constant, rawLevel, duration), maxRawHealth);
    }

    public static float GetEstimatedHealthDamage(int constant, int rawLevel, int duration, float currentRawHealth, float minRawHealth){
        return Math.max(currentRawHealth - GetEstimatedHealthDelta(constant, rawLevel, duration), minRawHealth);
    }

    public static float GetNaturalRegenAddition(float rawSaturation, float hunger){
        // Approximate formula for calculating regeneration addition health: saturation + (2.5 - hunger) * exhaustion max / 6 exhaustion per healed heart
        return (rawSaturation + (float)(2.5 - hunger)) * (float)4 / (float)6;
    }

    public static String GetSubscriptNumber(Object number){ // assumes any number type
        return String.valueOf(number)
                .replace('0', '₀')
                .replace('1', '₁')
                .replace('2', '₂')
                .replace('3', '₃')
                .replace('4', '₄')
                .replace('5', '₅')
                .replace('6', '₆')
                .replace('7', '₇')
                .replace('8', '₈')
                .replace('9', '₉');
    }

}
