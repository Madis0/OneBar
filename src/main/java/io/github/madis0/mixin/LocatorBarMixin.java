package io.github.madis0.mixin;

import io.github.madis0.MixinConfigQuery;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.contextualbar.LocatorBarRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LocatorBarRenderer.class)
public abstract class LocatorBarMixin {

    @ModifyArg(
            method = "renderBackground(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"
            ),
            index = 3
    )
    private int modifyHeightBar(int original) {
        if(!MixinConfigQuery.isOneBarEnabled() || MixinConfigQuery.isCompatModeEnabled())
            return original;

        return MixinConfigQuery.getLocatorBarHeight();
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V",
            at = @At("STORE"), // Could also use "LOAD" depending on when you want to intercept
            index = 3
    )
    private int modifyHeightAddons(int original) {
        if(!MixinConfigQuery.isOneBarEnabled() || MixinConfigQuery.isCompatModeEnabled())
            return original;

        return MixinConfigQuery.getLocatorBarHeight();
    }

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void hideBar(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci){
        if(MixinConfigQuery.isOneBarEnabled() && !MixinConfigQuery.isLocatorBarEnabled())
            ci.cancel();
    }

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void hideAddons(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci){
        if(MixinConfigQuery.isOneBarEnabled() && !MixinConfigQuery.isLocatorBarEnabled())
            ci.cancel();
    }
}
