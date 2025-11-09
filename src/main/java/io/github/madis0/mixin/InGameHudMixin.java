package io.github.madis0.mixin;

import io.github.madis0.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.Gui.ContextualInfo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;

@Mixin(value = Gui.class)
public abstract class InGameHudMixin {
    @Final @Shadow private Minecraft minecraft;
    @Shadow protected abstract LivingEntity getPlayerVehicleWithHealth();

    private OneBarElements oneBarElements;
    private boolean showOneBar = false;

    @Inject(at = @At("HEAD"), method = "render")
    public void render(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
        oneBarElements = new OneBarElements(context);
        showOneBar = MixinConfigQuery.isOneBarEnabled(); // This var exists because it also shows whether oneBarElements is initialized

        boolean barsVisible = !minecraft.options.hideGui && minecraft.gameMode.canHurtPlayer();
        if(showOneBar && barsVisible) oneBarElements.renderOneBar();
        if(showOneBar && MixinConfigQuery.showMountJump() && !minecraft.options.hideGui) oneBarElements.mountJumpBar();

        PlayerProperties.setLocatorBarAvailable(minecraft.player.connection.getWaypointManager().hasWaypoints());
    }

    @Inject(method = "renderPlayerHealth", at = @At(value = "HEAD"), cancellable = true)
    private void hideHud(GuiGraphics context, CallbackInfo ci){
        if(showOneBar && !MixinConfigQuery.isCompatModeEnabled())
            ci.cancel();
    }

    @Inject(method = "renderVehicleHealth", at = @At(value = "HEAD"), cancellable = true)
    private void hideMountHealth(GuiGraphics context, CallbackInfo ci) {
        if(showOneBar && !MixinConfigQuery.isCompatModeEnabled())
            ci.cancel();
        oneBarElements.mountBar(getPlayerVehicleWithHealth());
    }

    @Inject(method = "nextContextualInfoState", at = @At("HEAD"), cancellable = true)
    private void forceLocatorBar(CallbackInfoReturnable<ContextualInfo> cir) {
        if (showOneBar && !MixinConfigQuery.isCompatModeEnabled()) {
            ContextualInfo barType = MixinConfigQuery.isLocatorBarEnabled()
                    ? ContextualInfo.LOCATOR
                    : ContextualInfo.EMPTY;
            cir.setReturnValue(barType);
            cir.cancel();
        }
    }

    @ModifyVariable(method = "renderSelectedItemName", at = @At(value = "STORE"), ordinal = 2)
    private int renderHeldItemTooltip(int k) {
        if (!showOneBar || !MixinConfigQuery.isHotbarTooltipsDown()) {
            return k;
        }

        boolean creativeOrSpectator = PlayerProperties.isCreativeOrSpectator;
        boolean hasMount = (getPlayerVehicleWithHealth() != null);
        boolean hasLocator = MixinConfigQuery.isLocatorBarEnabled() && MixinConfigQuery.isLocatorBarMode(ModConfig.LocatorBarMode.HOTBAR) || MixinConfigQuery.isCompatModeEnabled();
        ClientProperties clientProperties = new ClientProperties();

        if (!creativeOrSpectator) { // Survival
            return hasMount ? clientProperties.tooltipSurvivalMountH : clientProperties.tooltipSurvivalH;
        } else { // Creative or spectator
            if(MixinConfigQuery.isCompatModeEnabled() && hasMount)
                return clientProperties.tooltipCreativeMountCompatH;
            else if (hasLocator)
                return clientProperties.tooltipCreativeLocatorH;
            return clientProperties.tooltipCreativeH;
        }
    }
}
