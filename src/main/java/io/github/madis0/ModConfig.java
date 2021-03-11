package io.github.madis0;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "onebar")
public class ModConfig implements ConfigData {

    public boolean showOneBar = true;
    public boolean showVanilla = false;
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int textColor = 0x99FFFFFF;
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int backgroundColor = 0xFF000000;

    @ConfigEntry.Gui.CollapsibleObject
    public GoodThings goodThings = new GoodThings();
    @ConfigEntry.Gui.CollapsibleObject
    public BadThings badThings = new BadThings();
    @ConfigEntry.Gui.CollapsibleObject
    public Entity entity = new Entity();
    @ConfigEntry.Gui.CollapsibleObject
    public Experience experience = new Experience();
    @ConfigEntry.Gui.CollapsibleObject
    public Experimental experimental = new Experimental();

    public static class GoodThings {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int healthColor = 0xFFD32F2F;
        public boolean showArmor = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int armorColor = 0x99FFFFFF;
        public boolean showResistance = true;
    }

    public static class BadThings {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int hungerColor = 0xBB3E2723;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int airColor = 0xBB1A237E;
        public boolean showFire = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int fireColor = 0xBBC43E00;

    }

    public static class Entity {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int healthColor = 0xFFF57F17;
        public boolean showHorseJump = true;
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int jumpColor = 0xFF795548;

    }

    public static class Experience {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int xpColor = 0xFF00C853;
        public boolean leftHanded = false;
    }

    public static class Experimental {
        public boolean useFractions = false;
    }
}
