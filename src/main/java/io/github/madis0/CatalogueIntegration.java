package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfigClient;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screens.Screen;

@SuppressWarnings("unused")
public final class CatalogueIntegration {

    public static Screen createConfigScreen(Screen parent, ModContainer mod) {
        return AutoConfigClient.getConfigScreen(ModConfig.class, parent).get();
    }
}