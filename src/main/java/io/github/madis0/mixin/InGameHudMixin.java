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

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Final @Shadow private MinecraftClient client;
    @Shadow protected abstract LivingEntity getRiddenEntity();

    private ModConfig config;
    private OneBarElements oneBarElements;

    @Inject(at = @At("TAIL"), method = "render")
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo info) {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        oneBarElements = new OneBarElements(matrixStack);

        // Method calls

        boolean barsVisible = !client.options.hudHidden && client.interactionManager.hasStatusBars();
        if(config.showOneBar && barsVisible) oneBarElements.renderOneBar();
    }

    // Injections to vanilla methods

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE"), cancellable = true)
    private void renderStatusBars(MatrixStack matrices, CallbackInfo ci){
        if(config.showOneBar) ci.cancel();
    }
    @Inject(method = "renderExperienceBar", at = @At(value = "INVOKE"), cancellable = true)
    private void renderExperienceBar(MatrixStack matrices, int x, CallbackInfo ci){
        if(config.showOneBar) ci.cancel();
    }

    @Inject(method = "renderMountJumpBar", at = @At(value = "INVOKE"), cancellable = true)
    private void renderMountJumpBar(MatrixStack matrices, int x, CallbackInfo ci) {
        if(config.showOneBar) ci.cancel();
        if(config.entity.showHorseJump) oneBarElements.jumpBar();
    }
    @Inject(method = "renderMountHealth", at = @At(value = "INVOKE"), cancellable = true)
    private void renderMountHealth(MatrixStack matrices, CallbackInfo ci) {
        if(config.showOneBar){
            ci.cancel();
            oneBarElements.mountBar(getRiddenEntity());
        }
    }
}
