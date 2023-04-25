package io.github.madis0.mixin;

import io.github.madis0.ModConfig;
import io.github.madis0.OneBarElements;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
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

    private final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    private OneBarElements oneBarElements;
    private boolean showOneBar = false;

    @Inject(at = @At("TAIL"), method = "render")
    public void render(DrawContext drawContext, float tickDelta, CallbackInfo ci) {
        oneBarElements = new OneBarElements(drawContext);
        showOneBar = config.showOneBar; // This var exists because it also shows whether oneBarElements is initialized

        boolean barsVisible = !client.options.hudHidden && Objects.requireNonNull(client.interactionManager).hasStatusBars();
        if(showOneBar && barsVisible) oneBarElements.renderOneBar();
    }

    // "Compatibility" injections


    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V"), cancellable = true)
    private void hideHudCompat(MatrixStack matrices, CallbackInfo ci){
        if(config.otherBars.compatibilityMode) genericCancel(ci);
    }
    @Inject(method = "renderExperienceBar", at = @At(value = "TAIL"), cancellable = true)
    private void hideXpBarCompat(MatrixStack matrices, int x, CallbackInfo ci){
        if(config.otherBars.compatibilityMode) genericCancel(ci);
    }
    @Inject(method = "renderMountJumpBar", at = @At(value = "TAIL"), cancellable = true)
    private void hideMountJumpCompat(JumpingMount jumpingMount, MatrixStack matrices, int x, CallbackInfo ci) {
        if(config.otherBars.compatibilityMode) mountJump(ci);
    }

    // "Override" injections

    @Inject(method = "renderStatusBars", at = @At(value = "HEAD"), cancellable = true)
    private void hideHud(MatrixStack matrices, CallbackInfo ci){
        if(!config.otherBars.compatibilityMode) genericCancel(ci);
    }
    @Inject(method = "renderExperienceBar", at = @At(value = "HEAD"), cancellable = true)
    private void hideXpBar(MatrixStack matrices, int x, CallbackInfo ci){
        if(!config.otherBars.compatibilityMode) genericCancel(ci);
    }
    @Inject(method = "renderMountJumpBar", at = @At(value = "HEAD"), cancellable = true)
    private void hideMountJump(JumpingMount jumpingMount, MatrixStack matrices, int x, CallbackInfo ci) {
        if(!config.otherBars.compatibilityMode) mountJump(ci);
    }
    @Inject(method = "renderMountHealth", at = @At(value = "HEAD"), cancellable = true)
    private void hideMountHealth(MatrixStack matrices, CallbackInfo ci) {
        if(showOneBar){
            ci.cancel();
            oneBarElements.mountBar(getRiddenEntity());
        }
    }

    @ModifyVariable(method = "renderHeldItemTooltip", at = @At(value = "STORE"), ordinal = 2)
    private int renderHeldItemTooltip(int k){
        if(showOneBar && config.otherBars.hotbarTooltipsDown) {
            if (getRiddenEntity() == null && !Objects.requireNonNull(client.interactionManager).hasCreativeInventory())
                return k + 14;
            else if (Objects.requireNonNull(client.interactionManager).hasCreativeInventory())
                return k + 12;
            return k + 2;
        }
        return k;
    }

    private void genericCancel(CallbackInfo ci){
        if(showOneBar) ci.cancel();
    }

    private void mountJump(CallbackInfo ci){
        if(showOneBar) {
            ci.cancel();
            var entity = getRiddenEntity();

            if(config.entity.showMountJump){
                if(!(entity instanceof HorseEntity))
                    oneBarElements.camelJumpBar(getRiddenEntity());
                else
                    oneBarElements.horseJumpBar(getRiddenEntity());
            }
        }
    }
}
