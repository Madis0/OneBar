package io.github.madis0.mixin;

import io.github.madis0.MixinConfigQuery;
import io.github.madis0.OneBarElements;
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

import java.util.Objects;

@Mixin(value = InGameHud.class, priority = 800)
public abstract class InGameHudMixin {
    @Final @Shadow private MinecraftClient client;
    @Shadow protected abstract LivingEntity getRiddenEntity();

    private OneBarElements oneBarElements;
    private boolean showOneBar = false;

    @Inject(at = @At("TAIL"), method = "render")
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        oneBarElements = new OneBarElements(context);
        showOneBar = MixinConfigQuery.isOneBarEnabled(); // This var exists because it also shows whether oneBarElements is initialized

        boolean barsVisible = !client.options.hudHidden && Objects.requireNonNull(client.interactionManager).hasStatusBars();
        if(showOneBar && barsVisible) oneBarElements.renderOneBar();
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V"), cancellable = true)
    private void hideHudCompat(DrawContext context, CallbackInfo ci){
        if(MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            ci.cancel();
    }

    @Inject(method = "renderStatusBars", at = @At(value = "HEAD"), cancellable = true)
    private void hideHud(DrawContext context, CallbackInfo ci){
        if(!MixinConfigQuery.isCompatModeEnabled() && MixinConfigQuery.isOneBarEnabled())
            ci.cancel();
    }

    @Inject(method = "renderMountHealth", at = @At(value = "HEAD"), cancellable = true)
    private void hideMountHealth(DrawContext context, CallbackInfo ci) {
        if(showOneBar){
            ci.cancel();
            oneBarElements.mountBar(getRiddenEntity());
        }
    }

    @ModifyVariable(method = "renderHeldItemTooltip", at = @At(value = "STORE"), ordinal = 2)
    private int renderHeldItemTooltip(int k){
        if(showOneBar && MixinConfigQuery.isHotbarTooltipsDown()) {
            if (getRiddenEntity() == null && !Objects.requireNonNull(client.interactionManager).getCurrentGameMode().isCreative())
                return k + 14;
            else if (Objects.requireNonNull(client.interactionManager).getCurrentGameMode().isCreative())
                return k + 12;
            return k + 2;
        }
        return k;
    }
}
