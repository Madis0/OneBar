package io.github.madis0;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.HumanoidArm;
import java.util.Objects;

public class ClientProperties {
    public final int baseStartW;
    public final int baseEndW;
    public int baseStartH;
    public final int baseEndH;

    public int armorStartH;
    public int armorEndH;

    public int saturationStartH;
    public int saturationEndH;

    public int xpStartW;
    public final int xpEndW;
    public int xpStartH;
    public final int xpEndH;

    public final int jumpStartW;
    public final int jumpEndW;
    public final int jumpStartH;
    public final int jumpEndH;

    public final int dashStartW;
    public final int dashEndW;
    public final int dashStartH;
    public final int dashEndH;
    public final int mountStartH;
    public final int mountEndH;

    public int locatorBarOriginalH;
    public int locatorBarBossBarH;
    public int locatorBarHeightConst;

    public final int tooltipSurvivalH;
    public final int tooltipCreativeH;
    public final int tooltipSurvivalMountH;
    public final int tooltipCreativeLocatorH;
    public final int tooltipCreativeMountCompatH;

    public final boolean isHardcore;

    public ClientProperties(){
        Minecraft client = Minecraft.getInstance();
        int scaledWidth = client.getWindow().getGuiScaledWidth();
        int scaledHeight = client.getWindow().getGuiScaledHeight();

        boolean compatMode = MixinConfigQuery.isCompatModeEnabled();
        boolean hasHotbarLocatorBar = MixinConfigQuery.isLocatorBarEnabled() && MixinConfigQuery.isLocatorBarMode(ModConfig.LocatorBarMode.HOTBAR);
        locatorBarHeightConst = 7;

        boolean f3IsCovering = Minecraft.getInstance().gui.getDebugOverlay().showFpsCharts() ||
                Minecraft.getInstance().gui.getDebugOverlay().showNetworkCharts();

        baseStartW = scaledWidth / 2 - 91;
        baseEndW = baseStartW + 182;
        baseStartH = scaledHeight - 33 - (hasHotbarLocatorBar ? locatorBarHeightConst : 0);

        if (FabricLoader.getInstance().getObjectShare().get("raised:hud") instanceof Integer distance) {
            baseStartH -= distance;
        }

        if (compatMode) {
            baseStartH -= 20;
        }
        else if (f3IsCovering) {
            baseStartH -= 42;
        }

        baseEndH = baseStartH + 9;

        armorStartH = baseStartH - 1;
        armorEndH = baseStartH;

        saturationStartH = baseEndH;
        saturationEndH = baseEndH + 1;

        if (client.options.mainHand().get() == HumanoidArm.RIGHT){
            xpStartW = baseEndW + 4;
            if(client.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR)
                xpStartW += 20;
        }
        else if (client.options.mainHand().get() == HumanoidArm.LEFT) {
            xpStartW = baseStartW - 22;
            if(client.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR)
                xpStartW -= 20;
        }

        if (f3IsCovering || compatMode) {
            xpStartH = baseStartH + 11;
        }
        else if(hasHotbarLocatorBar){
            xpStartH = baseStartH + 28 + locatorBarHeightConst;
        }
        else {
            xpStartH = baseStartH + 28;
        }

        xpEndW = xpStartW + 18;
        xpEndH = xpStartH + 1;

        jumpStartW = (scaledWidth / 2) - 7;
        jumpStartH = (scaledHeight / 2) + 15;
        jumpEndW = jumpStartW + 13;
        jumpEndH = jumpStartH + 50;

        dashStartW = baseStartW + 65;
        dashEndW = dashStartW + 50;
        dashStartH = jumpStartH;
        dashEndH = dashStartH + 7;

        mountStartH = baseStartH - 12;
        mountEndH = mountStartH + 9;

        locatorBarOriginalH = scaledHeight - 24 - 5;
        locatorBarBossBarH = 12;

        tooltipSurvivalH = baseStartH - 11;
        tooltipSurvivalMountH = mountStartH - 11;

        tooltipCreativeH = (scaledHeight - 33) - 14;
        tooltipCreativeLocatorH = tooltipCreativeH - locatorBarHeightConst;
        tooltipCreativeMountCompatH = tooltipCreativeH - locatorBarHeightConst - 20;

        isHardcore = Objects.requireNonNull(client.level).getLevelData().isHardcore();
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

    public int dashRelativeEndW(int value, int total){
        return Calculations.relativeW(dashStartW, dashEndW, value, total);
    }
    public int dashRelativeEndW(long value, long total){
        return dashRelativeEndW(Calculations.getPreciseInt(value), Calculations.getPreciseInt(total));
    }
}
