package io.github.madis0.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.DrawableHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    MinecraftClient client;
    MatrixStack stack;

    @Inject(at = @At("TAIL"), method = "render")
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo info) {
        client = MinecraftClient.getInstance();
        stack = matrixStack;

        // TODO: relative coords, currently only visible in maximized
        resetBar();
        renderHealth(15);

    }

    private void resetBar(){
        DrawableHelper.fill(stack, 228, 300, 411, 309, 0xFF000000);
    }

    private void renderHealth(int health){
        String value = String.valueOf(health);

        int startW = 228;
        int endW = 411;
        int startH = 300;
        int endH = 309;

        int relativeEndW = startW + ((endW - startW) / 20 * health);

        client.textRenderer.draw(stack, value, 396, 301, 0xFFFFFFFF);
        DrawableHelper.fill(stack, startW, startH, relativeEndW, endH, 0xFFD32F2F);
    }
}
