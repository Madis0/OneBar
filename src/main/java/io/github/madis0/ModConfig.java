package io.github.madis0;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "onebar")
public class ModConfig implements ConfigData {

    public boolean showOneBar = true;
    @ConfigEntry.Gui.Tooltip
    public boolean showText = true;
    @ConfigEntry.Gui.Tooltip
    public boolean healthEstimates = true;
    @ConfigEntry.Gui.Tooltip
    public boolean uhcMode = false;
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int textColor = 0xBBFFFFFF;
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int backgroundColor = 0xFF000000;

    @ConfigEntry.Gui.CollapsibleObject
    public GoodThings goodThings = new GoodThings();
    @ConfigEntry.Gui.CollapsibleObject
    public BadThings badThings = new BadThings();
    @ConfigEntry.Gui.CollapsibleObject
    public Entity entity = new Entity();
    @ConfigEntry.Gui.CollapsibleObject
    public OtherBars otherBars = new OtherBars();
    @ConfigEntry.Gui.CollapsibleObject
    public Experimental experimental = new Experimental();

    public static class GoodThings {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int healthColor = 0xFFD32F2F;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int naturalRegenerationColor = 0xFFFFC107;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int regenerationColor = 0xFFEC407A;
        public boolean showResistance = true;
    }

    public static class BadThings {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int hungerColor = 0xBB3E2723;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int hungerEffectColor = 0xBB827717;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int airColor = 0xBB1A237E;
        public boolean showFireBar = true;
        public boolean showFireText = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int fireColor = 0x99C43E00;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int poisonColor = 0xBB8C9900;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int witherColor = 0xCC37474f;
    }

    public static class Entity {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int healthColor = 0xFFF57F17;
        public boolean showHorseJump = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int jumpColor = 0xFF795548;
    }

    public static class OtherBars {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int xpColor = 0xFF00C853;
        public boolean showArmorBar = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int armorColor = 0x99FFFFFF;
        public boolean heldFoodHungerBar = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int heldFoodHungerWasteColor = 0x99FFB04C;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int heldFoodHungerGoodColor = 0xBB76FF03;
    }

    public static class Experimental {
        public boolean useFractions = false;
        public boolean showHungerDecreasing = false;
    }
}
