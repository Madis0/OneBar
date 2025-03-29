package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.util.math.MathHelper;

public class Calculations {
    private static final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    /**
     * Calculates an intermediate coordinate between a start and end value based on a given proportion.
     *
     * <p>
     * This method computes the coordinate by determining the proportional distance between
     * {@code start} and {@code end} using the fraction {@code value / total}. If {@code value} is less
     * than {@code total}, the computed coordinate is rounded up; otherwise, the end coordinate is returned.
     * </p>
     *
     * @param start the starting coordinate
     * @param end the ending coordinate
     * @param value the current relative progress value
     * @param total the total value representing the full width or range
     * @return the calculated coordinate relative to the start position
     */
    public static int relativeW(int start, int end, int value, int total){
        return value < total ? MathHelper.ceil(start + ((float) (end - start) / total * value)) : end;
    }

    public static int getPreciseInt(float number){
        return MathHelper.ceil(number * 10000.0F);
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


    /**
     * Converts horse's jump strength to jump height, may not be 100% accurate
     * <a href="https://github.com/d4rkm0nkey/HorseStatsVanilla/blob/main/src/client/java/monkey/lumpy/horse/stats/vanilla/util/Converter.java">Source</a>
     * @param strength Horse jump strength
     * @return Jump height in blocks
     */
    public static double getHorseJumpHeight(double strength) {
        double height = 0;
        double velocity = strength;
        while(velocity > 0) {
            height += velocity;
            velocity = (velocity - .08) * .98 * .98;
        }
        return height;
    }

    public static double getCamelDashDistance(float strength) {
        // Minecraft gravity constant (approximately)
        final double gravity = 0.08; // Blocks/tick^2
        final double jumpVelocityOneBlock = Math.sqrt(2 * gravity * 1); // Velocity needed to reach 1 block high

        // Default values for movement speed and velocity multiplier
        final double defaultMovementSpeed = 0.12; // Adjusted for the camel's movement speed
        final double defaultVelocityMultiplier = 1.0; // Neutral velocity multiplier

        // Horizontal velocity calculation with adjusted factor
        double horizontalVelocity = 11.1111F * strength * defaultMovementSpeed * defaultVelocityMultiplier;

        // Time in the air (based on jumping 1 block high)
        double initialVerticalVelocity = jumpVelocityOneBlock * strength;
        double timeUp = initialVerticalVelocity / gravity; // Time to reach the peak
        double timeDown = timeUp; // Symmetrical falling
        double totalTimeInAir = timeUp + timeDown;

        // Horizontal distance = horizontal velocity * total time in air
        return horizontalVelocity * totalTimeInAir;
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

    public static int calculatePercentage(int value, int max) {
        if (max == 0) {
            throw new IllegalArgumentException("Max value cannot be zero.");
        }
        // Calculate percentage and round to the nearest integer
        return Math.round((value * 100.0f) / max);
    }
}
