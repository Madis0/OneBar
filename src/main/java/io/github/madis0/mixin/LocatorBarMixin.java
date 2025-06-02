package io.github.madis0.mixin;

import io.github.madis0.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LocatorBar.class, priority = 800)
public abstract class LocatorBarMixin {
    private static final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    @Inject(method = "renderBar", at = @At(value = "TAIL"), cancellable = true)
    private void hideBarCompat(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if(config.otherBars.compatibilityMode && config.showOneBar)
            ci.cancel();
    }

    @Inject(method = "renderBar", at = @At(value = "HEAD"), cancellable = true)
    private void hideBar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if(!config.otherBars.compatibilityMode && config.showOneBar)
            ci.cancel();
    }
}
