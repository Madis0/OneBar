package io.github.madis0.mixin;

import io.github.madis0.MixinConfigQuery;
import io.github.madis0.OneBarElements;
import io.github.madis0.accessor.DrawContextAccessor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.contextualbar.JumpableVehicleBarRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = JumpableVehicleBarRenderer.class)
public abstract class JumpBarMixin {

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void hideBar(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci){
        if(!MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            ci.cancel();
    }

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void hideAddons(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci){
        if(!MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            ci.cancel();
    }
}
