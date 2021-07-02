package io.github.madis0;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "onebar")
public class ModConfig implements ConfigData {

    public final boolean showOneBar = true;
    @ConfigEntry.Gui.Tooltip
    public final boolean healthEstimates = true;
    public final boolean uhcMode = false;
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public final int backgroundColor = 0xFF000000;

    @ConfigEntry.Gui.CollapsibleObject
    public final TextSettings textSettings = new TextSettings();
    @ConfigEntry.Gui.CollapsibleObject
    public final GoodThings goodThings = new GoodThings();
    @ConfigEntry.Gui.CollapsibleObject
    public final BadThings badThings = new BadThings();
    @ConfigEntry.Gui.CollapsibleObject
    public final Entity entity = new Entity();
    @ConfigEntry.Gui.CollapsibleObject
    public final OtherBars otherBars = new OtherBars();

    public static class TextSettings {
        @ConfigEntry.Gui.Tooltip
        public final boolean showText = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int textColor = 0xB3FFFFFF;
        public final boolean estimatesItalic = true;
        public final boolean estimatesParentheses = true;
        public final boolean useEmoji = true;
        public final boolean useFractions = false;
    }

    public static class GoodThings {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int healthColor = 0xFFD32F2F;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int naturalRegenerationColor = 0xFFFFC107;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int regenerationColor = 0xFFEC407A;
        public final boolean showResistance = true;
    }

    public static class BadThings {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int hungerColor = 0xBF3E2723;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int hungerEffectColor = 0xA6827717;
        public final boolean showHungerDecreasing = false;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int airColor = 0xA61A237E;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int freezeColor = 0xA68EACBB;
        public final boolean showFire = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int fireColor = 0xA6C43E00;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int poisonColor = 0xA68C9900;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int witherColor = 0xA637474f;
    }

    public static class Entity {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int healthColor = 0xFFF57F17;
        public final boolean showHorseJump = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int jumpColor = 0xFF795548;
    }

    public static class OtherBars {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int xpColor = 0xFF00C853;
        public final boolean showArmorBar = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int armorColor = 0xA6FFFFFF;
        public final boolean heldFoodHungerBar = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int heldFoodHungerWasteColor = 0xA6FFB04C;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public final int heldFoodHungerGoodColor = 0xBF76FF03;
    }
}
