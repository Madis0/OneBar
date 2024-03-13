package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import java.text.DecimalFormat;

public class Calculations {
    private static final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    public static int relativeW(int start, int end, int value, int total){
        return value < total ? MathHelper.ceil(start + ((float) (end - start) / total * value)) : end;
    }

    public static int getPreciseInt(float number){
        return MathHelper.ceil(number * 10000.0F);
    }

    public static String makeFraction(int number, boolean italic){
        DecimalFormat df = new DecimalFormat(config.textSettings.useFractionsPadZeroes ? "0.0#" : "0.#");
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

    public static float getEstimatedHealthDelta(int constant, int rawLevel, int duration){
        // See canApplyUpdateEffect and https://www.geeksforgeeks.org/bitwise-shift-operators-in-java/
        int ticks = Math.max(constant >> rawLevel, 1);
        float healthPerSec = 20 / (float)ticks;
        float durationPerSec = (float)duration / 20;
        return healthPerSec * durationPerSec;
    }

    public static float getEstimatedHealthRegen(int constant, int rawLevel, int duration, float currentRawHealth, float maxRawHealth){
        return Math.min(currentRawHealth + getEstimatedHealthDelta(constant, rawLevel, duration), maxRawHealth);
    }

    public static float getEstimatedHealthDamage(int constant, int rawLevel, int duration, float currentRawHealth, float minRawHealth){
        return Math.max(currentRawHealth - getEstimatedHealthDelta(constant, rawLevel, duration), minRawHealth);
    }

    public static float getNaturalRegenAddition(float rawSaturation, float hunger){
        // Approximate formula for calculating regeneration addition health: saturation + (2.5 - hunger) * exhaustion max / 6 exhaustion per healed heart
        return (rawSaturation + (float)(2.5 - hunger)) * (float)4 / (float)6;
    }

    public static String getSubscriptNumber(Object number){ // assumes any number type
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
                .replace(".","ִ")
                .replace('-','₋');
    }

    public static String emojiOrText(String emojiPhrase, String textPhrase, boolean extra, Object... args){
        if(extra && !config.textSettings.extraSymbols)
            return String.valueOf(args[0]);

        return Text.translatable(config.textSettings.useEmoji ? emojiPhrase : textPhrase, args).getString();
    }

    /**
     * Converts horse's jump strength to jump height, may not be 100% accurate
     * <a href="https://github.com/d4rkm0nkey/HorseStatsVanilla/blob/main/src/main/java/monkey/lumpy/horse/stats/vanilla/util/Converter.java">Source</a>
     * @param strength Horse jump strength
     * @return Jump height in blocks
     */
    public static double horseJumpStrengthToJumpHeight(double strength) {
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
    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return ((x1-x2) * (x1-x2)) + ((y1-y2) * (y1-y2)) + ((z1-z2) * (z1-z2));
    }

    /**
     * Calculates the pretty divisor values for things that do not actually max out at total health, e.g. air, freezing, warden.
     * @param max Maximum value
     * @return Maximum value divided by health default maximum
     */
    public static double getPrettyDivisor(int max, int dividedBy){
        return (double)max / dividedBy;
    }

    /**
     * Makes a color lighter or darker by entered percentage. Code by FlashyReese
     * @param color Color integer
     * @param factor Percentage to change the color by
     * @return New color integer
     */
    public static int manipulateColor(int color, int factor) {
        // Unpack color
        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;

        // Manipulate color
        int nr = clampTo8Bit((int) (r * ((float)factor / 100)));
        int ng = clampTo8Bit((int) (g * ((float)factor / 100)));
        int nb = clampTo8Bit((int) (b * ((float)factor / 100)));

        // Pack color ARGB
        return a << 24 | nr << 16 | ng << 8 | nb;
    }

    private static int clampTo8Bit(int v) {
        return (v & ~0xFF) != 0 ? ((~v) >> 31) & 0xFF : v;
    }
}
