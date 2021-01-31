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

        int hunger = 20 - hungerManager.getFoodLevel(); // TODO: source constant?
        int health = MathHelper.ceil(playerEntity.getHealth());
        int armor = playerEntity.getArmor();
        int air = (playerEntity.getMaxAir() - playerEntity.getAir()) / 15;
        int xpLevel = playerEntity.experienceLevel;
        int xpTotal = 183; // TODO: source constant?
        int xpProg = (int)(playerEntity.experienceProgress * xpTotal);

        // TODO: relative coords, currently only visible in maximized
        resetBar();
        renderHealth(health);
        renderHunger(hunger);
        renderArmor(armor);
        renderAir(air);
        renderXp(xpProg, xpTotal, xpLevel);

        renderText(health, hunger, air);
    }

    private void resetBar(){
        int backgroundColor = 0xFF000000;
        int healthStartW = 228;
        int healthEndW = 411;
        int healthStartH = 300;
        int healthEndH = 309;

        int xpStartW = 420;
        int xpEndW = 440;
        int xpStartH = 345;
        int xpEndH = 346;

        DrawableHelper.fill(stack, healthStartW, healthStartH, healthEndW, healthEndH, backgroundColor);
        DrawableHelper.fill(stack, xpStartW, xpStartH, xpEndW, xpEndH, backgroundColor);
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
        int hungerColor = 0xFF3E2723;
        int endW = 228;
        int startW = 411;
        int startH = 300;
        int endH = 309;

        int relativeEndW = startW + ((endW - startW) / 20 * hunger);

        DrawableHelper.fill(stack, startW, startH, relativeEndW, endH, hungerColor);
    }

    private void renderAir(int air){
        int airColor = 0xFF1A237E;
        int endW = 228;
        int startW = 411;
        int startH = 300;
        int endH = 309;

        int relativeEndW;

        if(air < 20)
            relativeEndW = startW + ((endW - startW) / 20 * air);
        else
            relativeEndW = endW;

        DrawableHelper.fill(stack, startW, startH, relativeEndW, endH, airColor);
    }

    private void renderArmor(int armor){
        int armorColor = 0xFFFFFFFF;
        int startW = 228;
        int endW = 411;
        int startH = 299;
        int endH = 300;

        int relativeEndW;

        if(armor < 20)
            relativeEndW = startW + ((endW - startW) / 20 * armor);
        else
            relativeEndW = endW;

        DrawableHelper.fill(stack, startW, startH, relativeEndW, endH, armorColor);
    }

    private void renderText(int health, int hunger, int air){
        String value;
        if (hunger < 1 && air < 1)
            value = String.valueOf(health);
        else if(hunger >= 1 && air < 1)
            value = String.valueOf(health) + "-" + String.valueOf(hunger);
        else
            value = String.valueOf(health) + "-A" + String.valueOf(air);
        int textColor = 0xFFFFFFFF;
        int textX = 396;
        int textY = 301;
        client.textRenderer.draw(stack, value, textX, textY, textColor);
    }

    private void renderXp(int xp, int total, int level){
        int xpColor = 0xFF4CAF50;
        int startW = 420;
        int endW = 440;
        int startH = 345;
        int endH = 346;

        int textX = 425;
        int textY = 335;

        int relativeEndW;

        if(xp < total)
            relativeEndW = (startW + ((endW - startW) / total * xp));
        else
            relativeEndW = endW;

        //String value = String.valueOf(level) + " " + String.valueOf(xp) + "/" + String.valueOf(total) + " (" + String.valueOf(relativeEndW) + ")";

        DrawableHelper.fill(stack, startW, startH, relativeEndW, endH, xpColor);
        client.textRenderer.drawWithShadow(stack, String.valueOf(level), textX, textY, xpColor);
    }
}
