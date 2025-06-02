package io.github.madis0.mixin;

import io.github.madis0.MixinConfigQuery;
import io.github.madis0.OneBarElements;
import io.github.madis0.accessor.DrawContextAccessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.JumpBar;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.passive.CamelEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = JumpBar.class, priority = 800)
public abstract class JumpBarMixin {
    @Inject(method = "renderBar", at = @At(value = "TAIL"), cancellable = true)
    private void hideBarCompat(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if(MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            mountJump(context, ci);
    }

    @Inject(method = "renderAddons", at = @At(value = "TAIL"), cancellable = true)
    private void renderAddonsCompat(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if(MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            mountJump(context, ci);
    }

    @Inject(method = "renderAddons", at = @At(value = "HEAD"), cancellable = true)
    private void hideBar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if(!MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            mountJump(context, ci);
    }

    @Inject(method = "renderAddons", at = @At(value = "HEAD"), cancellable = true)
    private void hideAddons(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        if(!MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            mountJump(context, ci);
    }

    private void mountJump(DrawContext context, CallbackInfo ci){
        var oneBarElements = new OneBarElements(context);
        if(MixinConfigQuery.isOneBarEnabled()) {
            ci.cancel();
            var entity = ((DrawContextAccessor)context).getClient().player.getControllingVehicle();

            if(MixinConfigQuery.isMountJumpEnabled()){
                if(entity instanceof CamelEntity)
                    oneBarElements.camelJumpBar();
                else // Ideally works for any modded entity too
                    oneBarElements.horseJumpBar();
            }
        }
    }
}
