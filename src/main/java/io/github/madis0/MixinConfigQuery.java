package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfig;

public class MixinConfigQuery {
    private static final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    public static boolean isOneBarEnabled() {
        return config.showOneBar;
    }

    public static boolean isCompatModeEnabled() {
        return config.otherBars.compatibilityMode;
    }

    public static boolean isMountJumpEnabled() {
        return config.entity.showMountJump;
    }

    public static boolean isHotbarTooltipsDown(){
        return config.otherBars.hotbarTooltipsDown;
    }
}
