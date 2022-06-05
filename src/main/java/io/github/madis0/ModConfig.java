package io.github.madis0;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "onebar")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean showOneBar = true;
    @ConfigEntry.Gui.Tooltip
    public boolean healthEstimates = true;
    @ConfigEntry.Gui.Tooltip
    public boolean uhcMode = false;
    @ConfigEntry.Gui.Tooltip
    public boolean disableHunger = false;
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int backgroundColor = 0xFF000000;

    public static class TextSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean showText = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int textColor = 0xB3FFFFFF;
        @ConfigEntry.Gui.Tooltip
        public boolean estimatesItalic = true;
        @ConfigEntry.Gui.Tooltip
        public boolean estimatesParentheses = true;
        @ConfigEntry.Gui.Tooltip
        public boolean useEmoji = true;
        @ConfigEntry.Gui.Tooltip
        public boolean useFractions = false;
    }

    public static class GoodThings {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int healthColor = 0xFFD32F2F;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int naturalRegenerationColor = 0xFFFFC107;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int regenerationColor = 0xFFEC407A;
        @ConfigEntry.Gui.Tooltip
        public boolean showResistance = true;
        @ConfigEntry.Gui.Tooltip
        public boolean heldFoodHungerBar = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int heldFoodHungerWasteColor = 0xA6FFB04C;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int heldFoodHungerGoodColor = 0xBF76FF03;
    }

    public static class BadThings {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int hungerColor = 0xBF3E2723;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int hungerEffectColor = 0xA6827717;
        @ConfigEntry.Gui.Tooltip
        public boolean showHungerDecreasing = false;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int airColor = 0xA61A237E;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int freezeColor = 0xA68EACBB;
        @ConfigEntry.Gui.Tooltip
        public boolean showFire = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int fireColor = 0xA6C43E00;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int poisonColor = 0xA68C9900;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int witherColor = 0xA637474f;
        @ConfigEntry.Gui.Tooltip
        public boolean showBadOmen = true;
    }

    public static class Entity {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int healthColor = 0xFFF57F17;
        @ConfigEntry.Gui.Tooltip
        public boolean showHorseJump = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showHorseJumpText = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int jumpColor = 0xFF795548;
    }

    public static class OtherBars {
        @ConfigEntry.Gui.Tooltip
        public boolean adaptiveXpBar = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int xpColor = 0xFF00C853;
        @ConfigEntry.Gui.Tooltip
        public boolean lapisCounter = false;
        @ConfigEntry.Gui.Tooltip
        public boolean lapisTimesEnchantable = false;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int lapisColor = 0xFF2196F3;
        @ConfigEntry.Gui.Tooltip
        public boolean showArmorBar = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int armorColor = 0xA6FFFFFF;
        @ConfigEntry.Gui.Tooltip
        public boolean showArmorDurabilityBar = false;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int armorDurabilityColor = 0xA600B8D4;
        @ConfigEntry.Gui.Tooltip
        public boolean showElytraDurabilityBar = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int elytraDurabilityColor = 0xCCE040FB;
        @ConfigEntry.Gui.Tooltip
        public boolean showSaturationBar = false;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int saturationColor = 0xE6F9A825;
        @ConfigEntry.Gui.Tooltip
        public boolean hotbarTooltipsDown = true;
        @ConfigEntry.Gui.Tooltip
        public boolean compatibilityMode = false;
    }

    @ConfigEntry.Category("textSettings")
    @ConfigEntry.Gui.TransitiveObject
    public TextSettings textSettings = new TextSettings();
    @ConfigEntry.Category("goodThings")
    @ConfigEntry.Gui.TransitiveObject
    public GoodThings goodThings = new GoodThings();
    @ConfigEntry.Category("badThings")
    @ConfigEntry.Gui.TransitiveObject
    public BadThings badThings = new BadThings();
    @ConfigEntry.Category("entity")
    @ConfigEntry.Gui.TransitiveObject
    public Entity entity = new Entity();
    @ConfigEntry.Category("otherBars")
    @ConfigEntry.Gui.TransitiveObject
    public OtherBars otherBars = new OtherBars();
}
