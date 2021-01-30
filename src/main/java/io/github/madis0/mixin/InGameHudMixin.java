package io.github.madis0.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow protected abstract PlayerEntity getCameraPlayer();

    MinecraftClient client;
    MatrixStack stack;

    @Inject(at = @At("TAIL"), method = "render")
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo info) {
        client = MinecraftClient.getInstance();
        stack = matrixStack;

        PlayerEntity playerEntity = this.getCameraPlayer();
        HungerManager hungerManager = playerEntity.getHungerManager();
        int hunger = 20 - hungerManager.getFoodLevel(); // TODO: no constant in source?
        int health = MathHelper.ceil(playerEntity.getHealth()); //TODO: original health for animation

        // TODO: relative coords, currently only visible in maximized
        resetBar();
        renderHealth(health);
        renderHunger(hunger);

        renderText(health, hunger);
    }

    private void resetBar(){
        DrawableHelper.fill(stack, 228, 300, 411, 309, 0xFF000000);
    }

    private void renderHealth(int health){
        int healthColor = 0xFFD32F2F;
        int startW = 228;
        int endW = 411;
        int startH = 300;
        int endH = 309;

        int relativeEndW;

        if(health < 20)
            relativeEndW = startW + ((endW - startW) / 20 * health);
        else
            relativeEndW = endW;

        DrawableHelper.fill(stack, startW, startH, relativeEndW, endH, healthColor);
    }

    private void renderHunger(int hunger){
        int healthColor = 0xFF4E342E;
        int endW = 228;
        int startW = 411;
        int startH = 300;
        int endH = 309;

        int relativeEndW = startW + ((endW - startW) / 20 * hunger);

        DrawableHelper.fill(stack, startW, startH, relativeEndW, endH, healthColor);
    }

    private void renderText(int health, int hunger){
        String value = String.valueOf(health) + "-" + String.valueOf(hunger);
        int textColor = 0xFFFFFFFF;
        int textX = 396;
        int textY = 301;
        client.textRenderer.draw(stack, value, textX, textY, textColor);
    }
}
