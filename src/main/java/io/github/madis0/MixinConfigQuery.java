package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfig;

public class MixinConfigQuery {
    private static final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    public static boolean isOneBarEnabled() {
        return config.showOneBar;
    }

    public static boolean isCompatModeEnabled() {
        return config.compatibilityMode;
    }

    public static boolean isHotbarTooltipsDown(){
        return config.otherBars.hotbarTooltipsDown;
    }

    public static boolean showMountJump(){
        return config.entity.showMountJump;
    }

    public static boolean isLocatorBarEnabled() {
        return (config.otherBars.locatorBarMode != ModConfig.LocatorBarMode.DISABLED.ordinal() && PlayerProperties.locatorBarAvailable) || MixinConfigQuery.isCompatModeEnabled();
    }

    public static boolean isLocatorBarMode(ModConfig.LocatorBarMode mode){
        return config.otherBars.locatorBarMode == mode.ordinal();
    }

    public static int getLocatorBarHeight() {
        ClientProperties clientProperties = new ClientProperties();

        // Convert the config int back to the enum, with DISABLED as a fallback:
        ModConfig.LocatorBarMode modeEnum;
        try {
            modeEnum = ModConfig.LocatorBarMode.values()[config.otherBars.locatorBarMode];
        } catch (ArrayIndexOutOfBoundsException e) {
            modeEnum = ModConfig.LocatorBarMode.DISABLED;
        }

        return switch (modeEnum) {
            case HOTBAR -> clientProperties.locatorBarOriginalH;
            case BOSSBAR -> clientProperties.locatorBarBossBarH;
            default -> 0;
        };
    }


}
