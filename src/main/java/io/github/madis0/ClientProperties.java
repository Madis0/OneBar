package io.github.madis0;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.util.Arm;

import java.util.Objects;

public class ClientProperties {
    public final int baseStartW;
    public final int baseEndW;
    public int baseStartH;
    public final int baseEndH;
    public int xpStartW;
    public final int xpEndW;
    public int xpStartH;
    public final int xpEndH;
    public final int horseJumpStartW;
    public final int horseJumpEndW;
    public final int horseJumpStartH;
    public final int horseJumpEndH;

    public final int camelJumpStartW;
    public final int camelJumpEndW;
    public final int camelJumpStartH;
    public final int camelJumpEndH;
    public final int mountStartH;
    public final int mountEndH;

    public final boolean isHardcore;

    public ClientProperties(){
        MinecraftClient client = MinecraftClient.getInstance();
        int scaledWidth = client.getWindow().getScaledWidth();
        int scaledHeight = client.getWindow().getScaledHeight();

        baseStartW = scaledWidth / 2 - 91;
        baseEndW = baseStartW + 182;
        baseStartH = scaledHeight - 33;

        if (FabricLoader.getInstance().getObjectShare().get("raised:gui") instanceof Integer distance) {
            baseStartH -= distance;
        }

        if (MinecraftClient.getInstance().options.debugTpsEnabled){
            baseStartH -= 50;
        }

        baseEndH = baseStartH + 9;

        if (client.options.getMainArm().getValue() == Arm.RIGHT){
            xpStartW = baseEndW + 4;
            if(client.options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR)
                xpStartW = xpStartW + 20;
        }
        else if (client.options.getMainArm().getValue() == Arm.LEFT) {
            xpStartW = baseStartW - 22;
            if(client.options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR)
                xpStartW = xpStartW - 20;
        }

        if (MinecraftClient.getInstance().options.debugTpsEnabled)
            xpStartH = baseStartH + 11;
        else
            xpStartH = baseStartH + 28;

        xpEndW = xpStartW + 18;
        xpEndH = xpStartH + 1;

        horseJumpStartW = (scaledWidth / 2) - 7;
        horseJumpStartH = (scaledHeight / 2) + 15;
        horseJumpEndW = horseJumpStartW + 13;
        horseJumpEndH = horseJumpStartH + 50;

        camelJumpStartW = baseStartW + 65;
        camelJumpEndW = camelJumpStartW + 50;
        camelJumpStartH = horseJumpStartH;
        camelJumpEndH = camelJumpStartH + 7;

        mountStartH = baseStartH - 12;
        mountEndH = mountStartH + 9;

        isHardcore = Objects.requireNonNull(client.world).getLevelProperties().isHardcore();
    }

    public int baseRelativeEndW(int value, int total){
        return Calculations.relativeW(baseStartW, baseEndW, value, total);
    }

    public int baseRelativeStartW(int value, int total){
        return Calculations.relativeW(baseEndW, baseStartW, value, total);
    }

    public int baseRelativeEndW(float value, float total){
        return baseRelativeEndW(Calculations.getPreciseInt(value), Calculations.getPreciseInt(total));
    }

    public int baseRelativeStartW(float value, float total){
        return baseRelativeStartW(Calculations.getPreciseInt(value), Calculations.getPreciseInt(total));
    }

    public int camelRelativeEndW(int value, int total){
        return Calculations.relativeW(camelJumpStartW, camelJumpEndW, value, total);
    }
    public int camelRelativeEndW(long value, long total){
        return camelRelativeEndW(Calculations.getPreciseInt(value), Calculations.getPreciseInt(total));
    }
}
