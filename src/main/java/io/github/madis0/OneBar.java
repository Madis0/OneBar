package io.github.madis0;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.AutoConfigClient;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class OneBar implements ClientModInitializer {
    static KeyMapping.Category ONEBAR_MAIN;

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

		// --- Keybindings ---

		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ONEBAR_MAIN = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("onebar", "main"));

		KeyMapping showOneBar = registerKeybind("text.autoconfig.onebar.option.showOneBar");
		KeyMapping healthEstimates = registerKeybind("text.autoconfig.onebar.option.healthEstimates");
		KeyMapping uhcMode = registerKeybind("text.autoconfig.onebar.option.uhcMode");
		KeyMapping disableHunger = registerKeybind("text.autoconfig.onebar.option.disableHunger");
		KeyMapping configScreen = registerKeybind("text.autoconfig.onebar.title");

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (showOneBar.consumeClick()) {
				config.showOneBar = !config.showOneBar;
			}
			while (healthEstimates.consumeClick()) {
				config.healthEstimates = !config.healthEstimates;
				showState(client, config.healthEstimates, "text.autoconfig.onebar.option.healthEstimates");
			}
			while (uhcMode.consumeClick()) {
				config.uhcMode = !config.uhcMode;
				showState(client, config.uhcMode, "text.autoconfig.onebar.option.uhcMode");
			}
			while (disableHunger.consumeClick()) {
				config.disableHunger = !config.disableHunger;
				showState(client, config.disableHunger, "text.autoconfig.onebar.option.disableHunger");
			}
			while (configScreen.consumeClick()) {
				client.setScreen(AutoConfigClient.getConfigScreen(ModConfig.class, null).get());
			}
		});
	}

	private static void showState(Minecraft client, boolean variable, String translationKey){
		assert client.player != null;
		client.player.displayClientMessage(Component.translatable(variable ? "options.on.composed" : "options.off.composed", Component.translatable(translationKey).getString()), true);
	}

    private static KeyMapping registerKeybind(String translationKey) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(translationKey, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), ONEBAR_MAIN));
    }
}
