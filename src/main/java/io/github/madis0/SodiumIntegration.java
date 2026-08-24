package io.github.madis0;

import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import me.shedaniel.autoconfig.AutoConfigClient;

public class SodiumIntegration implements ConfigEntryPoint {
    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        builder.registerOwnModOptions()
                .setName("OneBar")
                .setNonTintedIcon(Identifier.fromNamespaceAndPath("onebar", "icon.png"))
                .setColorTheme(builder.createColorTheme().setBaseThemeRGB(0xFFD32F2F))
                .addPage(builder.createExternalPage()
                        .setName(Component.translatable("options.title"))
                        .setScreenConsumer((Screen screen) -> Minecraft.getInstance().setScreenAndShow(
                                AutoConfigClient.getConfigScreen(ModConfig.class, screen).get()
                        ))
                );
    }
}
