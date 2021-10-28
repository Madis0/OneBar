package io.github.madis0.mixin;

import io.github.madis0.ModConfig;
import io.github.madis0.OneBarElements;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Final @Shadow private MinecraftClient client;
    @Shadow protected abstract LivingEntity getRiddenEntity();

    private final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    private OneBarElements oneBarElements;
    private boolean showOneBar = false;

    @Inject(at = @At("TAIL"), method = "render")
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo info) {
        oneBarElements = new OneBarElements(matrixStack);
        showOneBar = config.showOneBar; // This var exists because it also shows whether oneBarElements is initialized

        boolean barsVisible = !client.options.hudHidden && Objects.requireNonNull(client.interactionManager).hasStatusBars();
        if(showOneBar && barsVisible) oneBarElements.renderOneBar();
    }

    // "Compatibility" injections

    @Inject(method = "renderStatusBars", at = @At(value = "TAIL"), cancellable = true)
    private void hideHudCompat(MatrixStack matrices, CallbackInfo ci){
        if(config.compatibilityMode) genericCancel(ci);
    }
    @Inject(method = "renderExperienceBar", at = @At(value = "TAIL"), cancellable = true)
    private void hideXpBarCompat(MatrixStack matrices, int x, CallbackInfo ci){
        if(config.compatibilityMode) genericCancel(ci);
    }
    @Inject(method = "renderMountJumpBar", at = @At(value = "TAIL"), cancellable = true)
    private void hideHorseJumpCompat(MatrixStack matrices, int x, CallbackInfo ci) {
        if(config.compatibilityMode) mountJump(ci);
    }
    @Inject(method = "renderMountHealth", at = @At(value = "TAIL"), cancellable = true)
    private void hideHorseHealthCompat(MatrixStack matrices, CallbackInfo ci) {
        if(config.compatibilityMode) mountHealth(ci);
    }

    // "Override" injections

    @Inject(method = "renderStatusBars", at = @At(value = "HEAD"), cancellable = true)
    private void hideHud(MatrixStack matrices, CallbackInfo ci){
        if(!config.compatibilityMode) genericCancel(ci);
    }
    @Inject(method = "renderExperienceBar", at = @At(value = "HEAD"), cancellable = true)
    private void hideXpBar(MatrixStack matrices, int x, CallbackInfo ci){
        if(!config.compatibilityMode) genericCancel(ci);
    }
    @Inject(method = "renderMountJumpBar", at = @At(value = "HEAD"), cancellable = true)
    private void hideHorseJump(MatrixStack matrices, int x, CallbackInfo ci) {
        if(!config.compatibilityMode) mountJump(ci);
    }
    @Inject(method = "renderMountHealth", at = @At(value = "HEAD"), cancellable = true)
    private void hideHorseHealth(MatrixStack matrices, CallbackInfo ci) {
        if(!config.compatibilityMode) mountHealth(ci);
    }

    private void genericCancel(CallbackInfo ci){
        if(showOneBar) ci.cancel();
    }

    private void mountJump(CallbackInfo ci){
        if(showOneBar) {
            ci.cancel();
            if(config.entity.showHorseJump)
                oneBarElements.jumpBar();
        }
    }

    private void mountHealth(CallbackInfo ci){
        if(showOneBar){
            ci.cancel();
            oneBarElements.mountBar(getRiddenEntity());
        }
    }
}
