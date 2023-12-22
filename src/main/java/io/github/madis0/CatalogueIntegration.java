package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screen.Screen;

/**
 * Provides support for configuring options through Catalogue.
 */
@SuppressWarnings("unused")
public final class CatalogueIntegration {

    public static Screen createConfigScreen(Screen parent, ModContainer mod) {
        return AutoConfig.getConfigScreen(ModConfig.class, parent).get();
    }
}