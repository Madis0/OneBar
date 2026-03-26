package io.github.madis0.mixin;

import io.github.madis0.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.Gui.ContextualInfo;
import net.minecraft.world.entity.LivingEntity;

@Mixin(value = Gui.class)
public abstract class InGameHudMixin {
    @Shadow protected abstract LivingEntity getPlayerVehicleWithHealth();

    private boolean showOneBar = MixinConfigQuery.isOneBarEnabled();;

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
