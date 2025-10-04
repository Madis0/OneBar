package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class OneBar implements ClientModInitializer {
    static KeyBinding.Category ONEBAR_MAIN;

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

		// --- Keybindings ---

		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ONEBAR_MAIN = KeyBinding.Category.create(Identifier.of("onebar", "main"));

		KeyBinding showOneBar = registerKeybind("text.autoconfig.onebar.option.showOneBar");
		KeyBinding healthEstimates = registerKeybind("text.autoconfig.onebar.option.healthEstimates");
		KeyBinding uhcMode = registerKeybind("text.autoconfig.onebar.option.uhcMode");
		KeyBinding disableHunger = registerKeybind("text.autoconfig.onebar.option.disableHunger");
		KeyBinding configScreen = registerKeybind("text.autoconfig.onebar.title");

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (showOneBar.wasPressed()) {
				config.showOneBar = !config.showOneBar;
			}
			while (healthEstimates.wasPressed()) {
				config.healthEstimates = !config.healthEstimates;
				showState(client, config.healthEstimates, "text.autoconfig.onebar.option.healthEstimates");
			}
			while (uhcMode.wasPressed()) {
				config.uhcMode = !config.uhcMode;
				showState(client, config.uhcMode, "text.autoconfig.onebar.option.uhcMode");
			}
			while (disableHunger.wasPressed()) {
				config.disableHunger = !config.disableHunger;
				showState(client, config.disableHunger, "text.autoconfig.onebar.option.disableHunger");
			}
			while (configScreen.wasPressed()) {
				client.setScreen(AutoConfig.getConfigScreen(ModConfig.class, null).get());
			}
		});
	}

	private static void showState(MinecraftClient client, boolean variable, String translationKey){
		assert client.player != null;
		client.player.sendMessage(Text.translatable(variable ? "options.on.composed" : "options.off.composed", Text.translatable(translationKey).getString()), true);
	}

    private static KeyBinding registerKeybind(String translationKey) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(translationKey, InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), ONEBAR_MAIN));
    }
}
