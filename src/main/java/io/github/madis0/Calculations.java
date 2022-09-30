package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
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
                .replace('9', '₉')
                .replace('.','ͺ');
    }

    public static String EmojiOrText(String emojiPhrase, String textPhrase, Object... args){
        return Text.translatable(config.textSettings.useEmoji ? emojiPhrase : textPhrase, args).getString();
    }

    public static String EmojiOrText(String emojiPhrase, String textPhrase){
        return EmojiOrText(emojiPhrase, textPhrase, (Object) null);
    }

    /**
     * Converts horse's jump strength to jump height, may not be 100% accurate
     * <a href="https://github.com/d4rkm0nkey/HorseStatsVanilla/blob/main/src/main/java/monkey/lumpy/horse/stats/vanilla/util/Converter.java">Source</a>
     * @param strength Horse jump strength
     * @return Jump height in blocks
     */
    public static double HorseJumpStrengthToJumpHeight(double strength) {
        return -0.1817584952 * strength * strength * strength + 3.689713992 * strength * strength + 2.128599134 * strength - 0.343930367;
    }

    /**
     * Gets the distance between two entities that have coordinates specified
     * @param x1 Entity one x-coordinate
     * @param y1 Entity one y-coordinate
     * @param z1 Entity one z-coordinate
     * @param x2 Entity two x-coordinate
     * @param y2 Entity two y-coordinate
     * @param z2 Entity two z-coordinate
     * @return Distance between two entities
     */
    public static double GetDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return ((x1-x2) * (x1-x2)) + ((y1-y2) * (y1-y2)) + ((z1-z2) * (z1-z2));
    }

    /**
     * Calculates the pretty divisor values for things that do not actually max out at total health, e.g. air, freezing, warden.
     * @param max Maximum value
     * @return Maximum value divided by health default maximum
     */
    public static double GetPrettyDivisor(int max, int dividedBy){
        return (double)max / dividedBy;
    }

    /**
     * Makes a color lighter or darker by entered percentage.
     * @param color Color integer
     * @param factor Percentage to change the color by
     * @return New color integer
     */
    public static int manipulateColor(int color, float factor) {
        Color clr = new Color(color, true);

        Color newClr = new Color(Math.max((int)(clr.getRed() * factor), 0),
                Math.max((int)(clr.getGreen() * factor), 0),
                Math.max((int)(clr.getBlue() * factor), 0),
                clr.getAlpha());

        var str = "0x" + String.format("%02X", newClr.getAlpha()) + String.format("%02X", newClr.getRed()) + String.format("%02X", newClr.getGreen()) + String.format("%02X", newClr.getBlue());

        return Integer.decode(str);
    }
}
