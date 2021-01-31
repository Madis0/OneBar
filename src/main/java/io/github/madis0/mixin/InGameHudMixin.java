package io.github.madis0.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow protected abstract PlayerEntity getCameraPlayer();

    MinecraftClient client;
    MatrixStack stack;
    boolean disableVanilla = false; // TODO: make it work

    int baseStartW = 229;
    int baseEndW = 411;
    int baseStartH = 315;
    int baseEndH = baseStartH + 9;

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
        //int fire = playerEntity.getFireTicks(); //TODO: only returns 0, 1 or -20
        //debugText("Fire:" + fire);

        resetBar();
        renderHealth(health);
        renderHunger(hunger);
        renderArmor(armor);
        renderAir(air);
        renderXp(xpProg, xpTotal, xpLevel);
        renderText(health, hunger, air);
    }

    //TODO: overwrite only on-demand

    @Overwrite
    private void renderStatusBars(MatrixStack matrices){
    }
    @Overwrite
    public void renderExperienceBar(MatrixStack matrices, int x){
    }

    private void resetBar(){
        int backgroundColor = 0xFF000000;

        int xpStartW = 420;
        int xpEndW = 440;
        int xpStartH = 345;
        int xpEndH = 346;

        DrawableHelper.fill(stack, baseStartW, baseStartH, baseEndW, baseEndH, backgroundColor);
        DrawableHelper.fill(stack, xpStartW, xpStartH, xpEndW, xpEndH, backgroundColor);
    }

    private void renderHealth(int health){
        int healthColor = 0xFFD32F2F;
        DrawableHelper.fill(stack, baseStartW, baseStartH, relativeEndW(health, 20), baseEndH, healthColor);
    }

    private void renderArmor(int armor){
        int armorColor = 0xFFFFFFFF;
        DrawableHelper.fill(stack, baseStartW, baseStartH - 1, relativeEndW(armor, 20), baseStartH, armorColor);
    }

    private void renderHunger(int hunger){
        int hungerColor = 0xFF3E2723;
        DrawableHelper.fill(stack, relativeStartW(hunger, 20), baseStartH, baseEndW, baseEndH, hungerColor);
    }

    private void renderAir(int air){
        int airColor = 0xFF1A237E;
        DrawableHelper.fill(stack, relativeStartW(air, 20), baseStartH, baseEndW, baseEndH, airColor);
    }

    private void renderText(int health, int hunger, int air){
        String value;
        if (hunger < 1 && air < 1)
            value = String.valueOf(health);
        else if(hunger >= 1 && air < 1)
            value = health + "-" + hunger;
        else
            value = health + "-A" + air;
        int textColor = 0xFFFFFFFF;
        client.textRenderer.draw(stack, value, baseEndW - 15, baseStartH + 1, textColor);
    }

    private void renderXp(int xp, int total, int level){
        int xpColor = 0xFF4CAF50;
        int startW = 420;
        int endW = 440;
        int startH = 345;
        int endH = 346;

        int relativeEndW;

        if(xp < total)
            relativeEndW = startW + ((endW - startW) / total * xp);
        else
            relativeEndW = endW;

        int textX = 425;
        int textY = 335;

        //String value = level + " " + xp + "/" + total + " (" + relativeEndW + ")";

        DrawableHelper.fill(stack, startW, startH, relativeEndW, endH, xpColor);
        client.textRenderer.drawWithShadow(stack, String.valueOf(level), textX, textY, xpColor);
    }

    private int relativeEndW(int value, int total){
        if(value < total)
            return baseStartW + ((baseEndW - baseStartW) / total * value);
        else
            return baseEndW;
    }

    private int relativeStartW(int value, int total){
        if(value < total)
            return baseEndW + ((baseStartW - baseEndW) / total * value);
        else
            return baseStartW;
    }

    private void debugText(String value){
        client.textRenderer.drawWithShadow(stack, value, baseEndW + 15, baseStartH + 1, 0xFFFFFFFF);
    }
}
