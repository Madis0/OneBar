package io.github.madis0.mixin;

import io.github.madis0.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = InGameHud.class, priority = 800)
public abstract class InGameHudMixin {
    @Final @Shadow private MinecraftClient client;
    @Shadow protected abstract LivingEntity getRiddenEntity();

    private OneBarElements oneBarElements;
    private boolean showOneBar = false;

    @Inject(at = @At("HEAD"), method = "render")
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        oneBarElements = new OneBarElements(context);
        showOneBar = MixinConfigQuery.isOneBarEnabled(); // This var exists because it also shows whether oneBarElements is initialized

        boolean barsVisible = !client.options.hudHidden && Objects.requireNonNull(client.interactionManager).hasStatusBars();
        if(showOneBar && barsVisible) oneBarElements.renderOneBar();
        if(showOneBar && MixinConfigQuery.showMountJump() && !client.options.hudHidden) oneBarElements.mountJumpBar();

        PlayerProperties.setLocatorBarAvailable(client.player.networkHandler.getWaypointHandler().hasWaypoint());
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V"), cancellable = true)
    private void hideHudCompat(DrawContext context, CallbackInfo ci){
        if(showOneBar && MixinConfigQuery.isCompatModeEnabled())
            ci.cancel();
    }

    @Inject(method = "renderStatusBars", at = @At(value = "HEAD"), cancellable = true)
    private void hideHud(DrawContext context, CallbackInfo ci){
        if(showOneBar && !MixinConfigQuery.isCompatModeEnabled())
            ci.cancel();
    }

    @Inject(method = "renderMountHealth", at = @At(value = "HEAD"), cancellable = true)
    private void hideMountHealth(DrawContext context, CallbackInfo ci) {
        if(showOneBar){
            ci.cancel();
            oneBarElements.mountBar(getRiddenEntity());
        }
    }

    @Inject(method = "getCurrentBarType", at = @At("HEAD"), cancellable = true)
    private void forceLocatorBar(CallbackInfoReturnable<Object> cir) {
        if(showOneBar && !MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isLocatorBarEnabled()){
            try {
                Class<?> barTypeClass = Class.forName("net.minecraft.client.gui.hud.InGameHud$BarType");
                Object locatorConst = Enum.valueOf((Class<Enum>) barTypeClass, "LOCATOR");
                cir.setReturnValue(locatorConst);
                cir.cancel();
            } catch (Exception e) {
                // fallback or log
                e.printStackTrace();
            }
        }
    }

    @ModifyVariable(method = "renderHeldItemTooltip", at = @At(value = "STORE"), ordinal = 2)
    private int renderHeldItemTooltip(int k) {
        if (!showOneBar || !MixinConfigQuery.isHotbarTooltipsDown()) {
            return k;
        }

        boolean creativeOrSpectator = PlayerProperties.isCreativeOrSpectator;
        boolean hasMount = (getRiddenEntity() != null);
        boolean hasLocator = MixinConfigQuery.isLocatorBarEnabled() && MixinConfigQuery.isLocatorBarMode(ModConfig.LocatorBarMode.ONEBAR);
        ClientProperties clientProperties = new ClientProperties();

        if (!creativeOrSpectator) { // Survival
            if (hasLocator) {
                return hasMount ? clientProperties.tooltipSurvivalLocatorMountH : clientProperties.tooltipSurvivalLocatorH;
            } else {
                return hasMount ? clientProperties.tooltipSurvivalMountH : clientProperties.tooltipSurvivalH;
            }
        } else { // Creative or spectator
            if (hasLocator) {
                return hasMount ? clientProperties.tooltipCreativeLocatorMountH : clientProperties.tooltipCreativeLocatorH;
            } else {
                return clientProperties.tooltipCreativeH;
            }
        }
    }
}
