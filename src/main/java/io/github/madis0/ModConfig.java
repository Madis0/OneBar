package io.github.madis0;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "onebar")
public class ModConfig implements ConfigData {
    public boolean showOneBar = true;
    public boolean showVanilla = false;
    public boolean showArmor = true;
    public boolean showJump = true;
    public boolean leftHanded = false;
    public boolean fractions = false;
}
