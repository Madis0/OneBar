package io.github.madis0.mixin;

import io.github.madis0.MixinConfigQuery;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ContextualBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ContextualBar.class)
public interface BarMixin {
    @Inject(method = "extractExperienceLevel", at = @At(value = "HEAD"), cancellable = true)
    private static void hideXpLevel(GuiGraphicsExtractor graphics, Font font, int experienceLevel, CallbackInfo ci){
        if(MixinConfigQuery.isOneBarEnabled() && !MixinConfigQuery.isCompatModeEnabled())
            ci.cancel();
    }
}
