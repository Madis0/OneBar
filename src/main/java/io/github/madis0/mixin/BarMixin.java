package io.github.madis0.mixin;

import io.github.madis0.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.Bar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Bar.class, priority = 800)
public interface BarMixin {
    @Inject(method = "drawExperienceLevel", at = @At(value = "TAIL"), cancellable = true)
    private static void hideXpLevelCompat(DrawContext context, TextRenderer textRenderer, int level, CallbackInfo ci){
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(config.otherBars.compatibilityMode && config.showOneBar)
            ci.cancel();
    }

    @Inject(method = "drawExperienceLevel", at = @At(value = "HEAD"), cancellable = true)
    private static void hideXpLevel(DrawContext context, TextRenderer textRenderer, int level, CallbackInfo ci){
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(!config.otherBars.compatibilityMode && config.showOneBar)
            ci.cancel();
    }
}
