package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;

public class OneBar implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

		// --- Keybindings ---

		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

		KeyBinding showOneBar = KeyBindingHelper.registerKeyBinding(new KeyBinding("text.autoconfig.onebar.option.showOneBar", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), "text.autoconfig.onebar.title"));
		KeyBinding healthEstimates = KeyBindingHelper.registerKeyBinding(new KeyBinding("text.autoconfig.onebar.option.healthEstimates", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), "text.autoconfig.onebar.title"));
		KeyBinding uhcMode = KeyBindingHelper.registerKeyBinding(new KeyBinding("text.autoconfig.onebar.option.uhcMode", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), "text.autoconfig.onebar.title"));
		KeyBinding disableHunger = KeyBindingHelper.registerKeyBinding(new KeyBinding("text.autoconfig.onebar.option.disableHunger", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), "text.autoconfig.onebar.title"));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (showOneBar.wasPressed()) {
				config.showOneBar = !config.showOneBar;
			}

			while (healthEstimates.wasPressed()) {
				config.healthEstimates = !config.healthEstimates;
				ShowState(client, config.healthEstimates, "text.autoconfig.onebar.option.healthEstimates");
			}

			while (uhcMode.wasPressed()) {
				config.uhcMode = !config.uhcMode;
				ShowState(client, config.uhcMode, "text.autoconfig.onebar.option.uhcMode");
			}
			while (disableHunger.wasPressed()) {
				config.disableHunger = !config.disableHunger;
				ShowState(client, config.disableHunger, "text.autoconfig.onebar.option.disableHunger");
			}
		});
	}

	private static void ShowState(MinecraftClient client, boolean variable, String translationKey){
		client.player.sendMessage(new TranslatableText(variable ? "options.on.composed" : "options.off.composed", new TranslatableText(translationKey).getString()), true);
	}
}
