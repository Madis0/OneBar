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
    PlayerEntity playerEntity;
    HungerManager hungerManager;

    boolean showVanilla = false;
    boolean showOneBar = true;
    boolean showArmor = true;

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
        playerEntity = this.getCameraPlayer();
        hungerManager = playerEntity.getHungerManager();

        baseStartW = this.scaledWidth / 2 - 91;
        baseEndW = baseStartW + 182;
        baseStartH = this.scaledHeight - 33;
        baseEndH = baseStartH + 9;

        xpStartW = baseEndW + 2;
        xpEndW = xpStartW + 18;
        xpStartH = baseStartH + 28;
        xpEndH = xpStartH + 1;

        if(showOneBar && !playerEntity.isSpectator() && !playerEntity.isCreative()) renderBar();
        if(showArmor) armorBar();
    }

    //TODO: overwrite only on-demand

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE"), cancellable = true)
    private void renderStatusBars(MatrixStack matrices, CallbackInfo ci){
        if(!showVanilla) ci.cancel();
    }
    @Inject(method = "renderExperienceBar", at = @At(value = "INVOKE"), cancellable = true)
    public void renderExperienceBar(MatrixStack matrices, int x, CallbackInfo ci){
        if(!showVanilla) ci.cancel();
    }

    private void renderBar(){
        barBackground();
        healthBar();
        hungerBar();
        airBar();
        xpBar();
        barText();
    }

    private void barBackground(){
        int backgroundColor = 0xFF000000;

        DrawableHelper.fill(stack, baseStartW, baseStartH, baseEndW, baseEndH, backgroundColor);
        DrawableHelper.fill(stack, xpStartW, xpStartH, xpEndW, xpEndH, backgroundColor);
    }

    private void healthBar(){
        float rawHealth = playerEntity.getHealth();
        float maxHealth = playerEntity.getMaxHealth();

        int healthColor = 0xFFD32F2F;
        DrawableHelper.fill(stack, baseStartW, baseStartH, relativeEndW(getPreciseInt(rawHealth), getPreciseInt(maxHealth)), baseEndH, healthColor);
    }

    private void armorBar(){
        int maxArmor = 20;
        int armor = playerEntity.getArmor();

        int armorColor = 0xFFFFFFFF;
        DrawableHelper.fill(stack, baseStartW, baseStartH - 1, relativeEndW(armor, maxArmor), baseStartH, armorColor);
    }

    private void hungerBar(){
        int maxHunger = 20;
        int hunger = maxHunger - hungerManager.getFoodLevel();
        //float saturation = hungerManager.getSaturationLevel(); //TODO: usage TBD

        int hungerColor = 0xFF3E2723;
        DrawableHelper.fill(stack, relativeStartW(hunger, maxHunger), baseStartH, baseEndW, baseEndH, hungerColor);
    }

    private void airBar(){
        int maxAir = playerEntity.getMaxAir();
        int rawAir = maxAir - playerEntity.getAir();
        int air = rawAir / 15;

        int airColor = 0xFF1A237E;
        DrawableHelper.fill(stack, relativeStartW(rawAir, maxAir), baseStartH, baseEndW, baseEndH, airColor);
    }

    private void barText(){
        int health = MathHelper.ceil(playerEntity.getHealth());
        int maxHunger = 20;

        int hunger = maxHunger - hungerManager.getFoodLevel();

        int maxAir = playerEntity.getMaxAir();
        int rawAir = maxAir - playerEntity.getAir();
        int air = rawAir / 15;

        String value;
        if (hunger < 1 && air < 1)
            value = String.valueOf(health);
        else if(hunger >= 1 && air < 1)
            value = health + "-" + hunger;
        else
            value = health + "-A" + air;
        int textColor = 0xFFFFFFFF;
        client.textRenderer.draw(stack, value, baseEndW - client.textRenderer.getWidth(value), baseStartH + 1, textColor);
    }

    private void xpBar(){
        int xpLevel = playerEntity.experienceLevel;
        int maxXp = 183;
        int xp = (int)(playerEntity.experienceProgress * maxXp);

        int xpColor = 0xFF00C853;
        int relativeEndW;

        if(xp < maxXp)
            relativeEndW = MathHelper.ceil(xpStartW + ((float)(xpEndW - xpStartW) / maxXp * xp));
        else
            relativeEndW = xpEndW;

        int textX = xpStartW + 3;
        int textY = xpStartH - 10;

        DrawableHelper.fill(stack, xpStartW, xpStartH, relativeEndW, xpEndH, xpColor);
        client.textRenderer.drawWithShadow(stack, String.valueOf(xpLevel), textX, textY, xpColor);
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
