package io.github.madis0.mixin;

import io.github.madis0.MixinConfigQuery;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ContextualBarRenderer.class)
public interface BarMixin {
    @Inject(method = "extractExperienceLevel", at = @At(value = "HEAD"), cancellable = true)
    private static void hideXpLevel(GuiGraphicsExtractor context, Font textRenderer, int level, CallbackInfo ci){
        if(MixinConfigQuery.isOneBarEnabled() && !MixinConfigQuery.isCompatModeEnabled())
            ci.cancel();
    }
}
