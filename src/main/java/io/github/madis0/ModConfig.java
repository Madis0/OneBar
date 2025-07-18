package io.github.madis0;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "onebar")
public class ModConfig implements ConfigData {
    public enum LocatorBarMode {
        DISABLED,
        HOTBAR,
        BOSSBAR;
    }

    @ConfigEntry.Gui.Tooltip
    public boolean showOneBar = true;
    @ConfigEntry.Gui.Tooltip
    public boolean compatibilityMode = false;
    @ConfigEntry.Gui.Tooltip
    public boolean healthEstimates = true;
    @ConfigEntry.Gui.Tooltip
    public boolean uhcMode = false;
    @ConfigEntry.Gui.Tooltip
    public boolean disableHunger = false;
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int backgroundColor = 0xFF000000;
    @ConfigEntry.Gui.Tooltip
    public boolean enableGradient = false;
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 200, min = 0)
    public int gradientShift = 50;

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
        public boolean extraSymbols = false;
        @ConfigEntry.Gui.Tooltip
        public boolean useFractions = false;
        @ConfigEntry.Gui.Tooltip
        public boolean useFractionsPadZeroes = true;
        @ConfigEntry.Gui.Tooltip
        public boolean rawHealth = false;
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
        public boolean showInvisibility = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showTotemOfUndying = false;
        @ConfigEntry.Gui.Tooltip
        public boolean heldFoodHungerBar = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int heldFoodHungerGoodColor = 0xBF76FF03;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int heldFoodHungerWasteColor = 0xA6FFB04C;
        @ConfigEntry.Gui.Tooltip
        public boolean showSaturationBar = false;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int saturationColor = 0xE6F9A825;
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
        public boolean showLevitation = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int levitationColor = 0xA66A1B9A;
        @ConfigEntry.Gui.Tooltip
        public boolean showFallHeight = false;
        @ConfigEntry.Gui.Tooltip
        public boolean showGlowing = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showOmens = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showWarden = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int wardenColor = 0xBF004d40;
        @ConfigEntry.Gui.Tooltip
        public boolean showInfested = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showPostDeathEffects = false;
    }

    public static class Entity {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int healthColor = 0xFFF57F17;
        @ConfigEntry.Gui.Tooltip
        public boolean showMountJump = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showMountJumpText = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int jumpColor = 0xFF795548;
        @ConfigEntry.Gui.Tooltip
        public boolean showMountCooldown = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showMountCooldownText = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int cooldownColor = 0xFFDD2C00;
    }

    public static class Armor {
        @ConfigEntry.Gui.Tooltip
        public boolean showArmorBar = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int armorColor = 0xA6FFFFFF;
        @ConfigEntry.Gui.Tooltip
        public boolean showArmorDurabilityBar = false;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int armorDurabilityColor = 0xA600B8D4;
        @ConfigEntry.Gui.Tooltip
        public boolean showSegmentedArmorBar = false;
        @ConfigEntry.Gui.Tooltip
        public boolean showElytraDurabilityBar = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int elytraDurabilityColor = 0xCCE040FB;
        @ConfigEntry.Gui.Tooltip
        public boolean showShieldDurabilityBar = false;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int shieldDurabilityColor = 0xEE78909C;
        @ConfigEntry.Gui.Tooltip
        public boolean showShieldCooldownBar = false;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int shieldCooldownColor = 0xEEFF3D00;
        @ConfigEntry.Gui.Tooltip
        public boolean showMobHeads = false;
    }

    public static class OtherBars {
        @ConfigEntry.Gui.Tooltip
        public boolean hotbarTooltipsDown = true;
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
        @ConfigEntry.BoundedDiscrete(max = 2, min = 0)
        public int locatorBarMode = LocatorBarMode.HOTBAR.ordinal();
        @ConfigEntry.Gui.Tooltip
        public boolean showLocatability = false;
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
    @ConfigEntry.Category("armor")
    @ConfigEntry.Gui.TransitiveObject
    public Armor armor = new Armor();
    @ConfigEntry.Category("otherBars")
    @ConfigEntry.Gui.TransitiveObject
    public OtherBars otherBars = new OtherBars();
}
