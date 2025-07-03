package io.github.madis0.mixin;

import io.github.madis0.MixinConfigQuery;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LocatorBar.class)
public abstract class LocatorBarMixin {

    @ModifyArg(
            method = "renderBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V"
            ),
            index = 3
    )
    private int modifyHeightBar(int original) {
        if(!MixinConfigQuery.isOneBarEnabled() || MixinConfigQuery.isCompatModeEnabled())
            return original;

        return MixinConfigQuery.getLocatorBarHeight();
    }

    @ModifyVariable(
            method = "renderAddons(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",
            at = @At("STORE"), // Could also use "LOAD" depending on when you want to intercept
            index = 3
    )
    private int modifyHeightAddons(int original) {
        if(!MixinConfigQuery.isOneBarEnabled() || MixinConfigQuery.isCompatModeEnabled())
            return original;

        return MixinConfigQuery.getLocatorBarHeight();
    }

    @Inject(method = "renderAddons", at = @At(value = "HEAD"), cancellable = true)
    private void hideBar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if(MixinConfigQuery.isOneBarEnabled() && !MixinConfigQuery.isCompatModeEnabled() && !MixinConfigQuery.isLocatorBarEnabled())
            ci.cancel();
    }

    @Inject(method = "renderAddons", at = @At(value = "HEAD"), cancellable = true)
    private void hideAddons(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if(MixinConfigQuery.isOneBarEnabled() && !MixinConfigQuery.isCompatModeEnabled() && !MixinConfigQuery.isLocatorBarEnabled())
            ci.cancel();
    }
}
