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

    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    MinecraftClient client;
    MatrixStack stack;
    boolean disableVanilla = false; // TODO: make it work

    int baseStartW;
    int baseEndW;
    int baseStartH;
    int baseEndH;

    int xpStartW;
    int xpEndW;
    int xpStartH;
    int xpEndH;

    @Inject(at = @At("TAIL"), method = "render")
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo info) {
        client = MinecraftClient.getInstance();
        stack = matrixStack;

        baseStartW = this.scaledWidth / 2 - 91;
        baseEndW = baseStartW + 182;
        baseStartH = this.scaledHeight - 33;
        baseEndH = baseStartH + 9;

        xpStartW = baseEndW + 2;
        xpEndW = xpStartW + 18;
        xpStartH = baseStartH + 28;
        xpEndH = xpStartH + 1;

        PlayerEntity playerEntity = this.getCameraPlayer();
        HungerManager hungerManager = playerEntity.getHungerManager();

        float rawHealth = playerEntity.getHealth();
        float maxHealth = playerEntity.getMaxHealth();
        int health = MathHelper.ceil(rawHealth);

        int maxHunger = 20;
        int rawFood = hungerManager.getFoodLevel();
        float saturation = hungerManager.getSaturationLevel(); //TODO: usage TBD
        int hunger = maxHunger - rawFood;

        int maxArmor = 20;
        int armor = playerEntity.getArmor();

        int maxAir = playerEntity.getMaxAir();
        int rawAir = maxAir - playerEntity.getAir();
        int air = rawAir / 15;

        int xpLevel = playerEntity.experienceLevel;
        int maxXp = 183;
        int xp = (int)(playerEntity.experienceProgress * maxXp);

        //int fire = playerEntity.getFireTicks(); //TODO: only returns 0, 1 or -20
        //debugText("Fire:" + fire);

        resetBar();
        renderHealth(rawHealth, maxHealth);
        renderHunger(hunger, maxHunger);
        renderArmor(armor, maxArmor);
        renderAir(rawAir, maxAir);
        renderXp(xp, maxXp, xpLevel);
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

        DrawableHelper.fill(stack, baseStartW, baseStartH, baseEndW, baseEndH, backgroundColor);
        DrawableHelper.fill(stack, xpStartW, xpStartH, xpEndW, xpEndH, backgroundColor);
    }

    private void renderHealth(float health, float total){
        int healthColor = 0xFFD32F2F;
        DrawableHelper.fill(stack, baseStartW, baseStartH, relativeEndW(getPreciseInt(health), getPreciseInt(total)), baseEndH, healthColor);
    }

    private void renderArmor(int armor, int total){
        int armorColor = 0xFFFFFFFF;
        DrawableHelper.fill(stack, baseStartW, baseStartH - 1, relativeEndW(armor, total), baseStartH, armorColor);
    }

    private void renderHunger(int hunger, int total){
        int hungerColor = 0xFF3E2723;
        DrawableHelper.fill(stack, relativeStartW(hunger, total), baseStartH, baseEndW, baseEndH, hungerColor);
    }

    private void renderAir(int air, int total){
        int airColor = 0xFF1A237E;
        DrawableHelper.fill(stack, relativeStartW(air, total), baseStartH, baseEndW, baseEndH, airColor);
    }

    private void renderText(int health, int hunger, int air){
        String value;
        if (hunger < 1 && air < 1)
            value = String.valueOf(health);
        else if(hunger >= 1 && air < 1)
            value = health + "-" + hunger;
        else
            value = health + "-a" + air;
        int textColor = 0xFFFFFFFF;
        client.textRenderer.draw(stack, value, baseEndW - client.textRenderer.getWidth(value), baseStartH + 1, textColor);
    }

    private void renderXp(int xp, int total, int level){
        int xpColor = 0xFF00C853;
        int relativeEndW;

        if(xp < total)
            relativeEndW = MathHelper.ceil(xpStartW + ((float)(xpEndW - xpStartW) / total * xp));
        else
            relativeEndW = xpEndW;

        int textX = xpStartW + 3;
        int textY = xpStartH - 10;

        DrawableHelper.fill(stack, xpStartW, xpStartH, relativeEndW, xpEndH, xpColor);
        client.textRenderer.drawWithShadow(stack, String.valueOf(level), textX, textY, xpColor);
    }

    private int relativeEndW(int value, int total){
        if(value < total)
            return MathHelper.ceil(baseStartW + ((float)(baseEndW - baseStartW) / total * value));
        else
            return baseEndW;
    }

    private int relativeStartW(int value, int total){
        if(value < total)
            return MathHelper.ceil(baseEndW + ((float)(baseStartW - baseEndW) / total * value));
        else
            return baseStartW;
    }

    private void debugText(String value){
        client.textRenderer.drawWithShadow(stack, value, baseEndW + 15, baseStartH + 1, 0xFFFFFFFF);
    }

    private int getPreciseInt(float number){
        float precision = 10000.0F;
        return MathHelper.ceil(number * precision);
    }
}
