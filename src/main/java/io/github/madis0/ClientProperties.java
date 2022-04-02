package io.github.madis0;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.util.Arm;

import java.util.Objects;

public class ClientProperties {
    public final int baseStartW;
    public final int baseEndW;
    public final int baseStartH;
    public final int baseEndH;
    public int xpStartW;
    public final int xpEndW;
    public final int xpStartH;
    public final int xpEndH;
    public final int jumpStartW;
    public final int jumpEndW;
    public final int jumpStartH;
    public final int jumpEndH;
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
        baseEndH = baseStartH + 9;

        if (client.options.getMainArm().getValue()  == Arm.RIGHT){
            xpStartW = baseEndW + 4;
            if(client.options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR)
                xpStartW = xpStartW + 20;
        }
        else if (client.options.getMainArm().getValue()  == Arm.LEFT) {
            xpStartW = baseStartW - 22;
            if(client.options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR)
                xpStartW = xpStartW - 20;
        }

        xpEndW = xpStartW + 18;
        xpStartH = baseStartH + 28;
        xpEndH = xpStartH + 1;

        jumpStartW = (scaledWidth / 2) - 2;
        jumpStartH = (scaledHeight / 2) + 10;
        jumpEndW = jumpStartW + 3;
        jumpEndH = jumpStartH + 50;

        mountStartH = baseStartH - 12;
        mountEndH = mountStartH + 9;

        isHardcore = Objects.requireNonNull(client.world).getLevelProperties().isHardcore();
    }

    public int baseRelativeEndW(int value, int total){
        return Calculations.RelativeW(baseStartW, baseEndW, value, total);
    }

    public int baseRelativeStartW(int value, int total){
        return Calculations.RelativeW(baseEndW, baseStartW, value, total);
    }

    public int baseRelativeEndW(float value, float total){
        return baseRelativeEndW(Calculations.GetPreciseInt(value), Calculations.GetPreciseInt(total));
    }

    public int baseRelativeStartW(float value, float total){
        return baseRelativeStartW(Calculations.GetPreciseInt(value), Calculations.GetPreciseInt(total));
    }
}
