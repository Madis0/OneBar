package io.github.madis0.mixin;

import io.github.madis0.MixinConfigQuery;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ExperienceBar.class, priority = 800)
public abstract class ExperienceBarMixin {
    @Inject(method = "renderBar", at = @At(value = "TAIL"), cancellable = true)
    private void hideBarCompat(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if(MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            ci.cancel();
    }

    @Inject(method = "renderAddons", at = @At(value = "TAIL"), cancellable = true)
    private void renderAddonsCompat(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if(MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            ci.cancel();
    }

    @Inject(method = "renderAddons", at = @At(value = "HEAD"), cancellable = true)
    private void hideBar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if(!MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            ci.cancel();
    }

    @Inject(method = "renderAddons", at = @At(value = "HEAD"), cancellable = true)
    private void hideAddons(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if(!MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            ci.cancel();
    }
}
