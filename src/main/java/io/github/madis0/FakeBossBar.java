package io.github.madis0;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;

import java.util.UUID;

public class FakeBossBar extends BossBar {
    public FakeBossBar(UUID uuid, Text name, Color color, Style style) {
        super(uuid, name, color, style);
    }
}
