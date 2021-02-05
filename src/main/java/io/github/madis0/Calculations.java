package io.github.madis0;

import net.minecraft.util.math.MathHelper;

public class Calculations {
        public static int relativeW(int start, int end, int value, int total){
            if(value < total)
                return MathHelper.ceil(start + ((float)(end - start) / total * value));
            else
                return end;
        }

        public static int getPreciseInt(float number){
            float precision = 10000.0F;
            return MathHelper.ceil(number * precision);
        }
}
