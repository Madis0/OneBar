package io.github.madis0.mixin;

import io.github.madis0.MixinConfigQuery;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ExperienceBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ExperienceBar.class)
public abstract class ExperienceBarMixin {
    @Inject(method = "extractRenderState", at = @At(value = "HEAD"), cancellable = true)
    private void hideBar(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci){
        if(!MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            ci.cancel();
    }

    @Inject(method = "extractRenderState", at = @At(value = "HEAD"), cancellable = true)
    private void hideAddons(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci){
        if(!MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            ci.cancel();
    }
}
