package io.github.madis0.mixin;

import io.github.madis0.Calculations;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
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

    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Shadow protected abstract LivingEntity getRiddenEntity();

    private MinecraftClient client;
    private MatrixStack stack;
    private PlayerEntity playerEntity;
    private HungerManager hungerManager;

    boolean showVanilla = false;
    boolean showOneBar = true;
    boolean showArmor = true;
    boolean showJump = true;
    boolean barsVisible;

    int baseStartW;
    int baseEndW;
    int baseStartH;
    int baseEndH;
    int xpStartW;
    int xpEndW;
    int xpStartH;
    int xpEndH;
    int jumpStartW;
    int jumpEndW;
    int jumpStartH;
    int jumpEndH;
    int mountStartH;
    int mountEndH;
    int backgroundColor;

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

        jumpStartW = (this.scaledWidth / 2) - 2;
        jumpStartH = (this.scaledHeight / 2) + 10;
        jumpEndW = jumpStartW + 3;
        jumpEndH = jumpStartH + 50;

        mountStartH = baseStartH - 12;
        mountEndH = mountStartH + 9;

        backgroundColor = 0xFF000000;

        barsVisible = !client.options.hudHidden && client.interactionManager.hasStatusBars();

        if (client.interactionManager == null) throw new AssertionError();

        if(showOneBar && barsVisible) renderBar();
        if(showOneBar && showArmor && barsVisible) armorBar();

        mountBar();

    }

    //TODO: overwrite only on-demand

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE"), cancellable = true)
    private void renderStatusBars(MatrixStack matrices, CallbackInfo ci){
        if(!showVanilla) ci.cancel();
    }
    @Inject(method = "renderExperienceBar", at = @At(value = "INVOKE"), cancellable = true)
    private void renderExperienceBar(MatrixStack matrices, int x, CallbackInfo ci){
        if(!showVanilla) ci.cancel();
    }

    @Inject(method = "renderMountJumpBar", at = @At(value = "INVOKE"), cancellable = true)
    private void renderMountJumpBar(MatrixStack matrices, int x, CallbackInfo ci) {
        if(!showVanilla) ci.cancel();
        if(showOneBar && showJump) jumpBar();
    }
    @Inject(method = "renderMountHealth", at = @At(value = "INVOKE"), cancellable = true)
    private void renderMountHealth(MatrixStack matrices, CallbackInfo ci) {
        if(!showVanilla) ci.cancel();
        if(showOneBar) mountBar();
    }

    private void renderBar(){
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null) {
            healthBar();
            hungerBar();
            airBar();
            xpBar();
            barText();
        }
    }

    private void healthBar(){
        float rawHealth = playerEntity.getHealth();
        float maxHealth = playerEntity.getMaxHealth();

        int healthColor = 0xFFD32F2F;
        DrawableHelper.fill(stack, baseStartW, baseStartH, baseEndW, baseEndH, backgroundColor);
        DrawableHelper.fill(stack, baseStartW, baseStartH, baseRelativeEndW(Calculations.getPreciseInt(rawHealth), Calculations.getPreciseInt(maxHealth)), baseEndH, healthColor);
    }

    private void armorBar(){
        int maxArmor = 20;
        int armor = playerEntity.getArmor();

        int armorColor = 0x99FFFFFF;
        DrawableHelper.fill(stack, baseStartW, baseStartH - 1, baseRelativeEndW(armor, maxArmor), baseStartH, armorColor);
    }

    private void hungerBar(){
        int maxHunger = 20;
        int hunger = maxHunger - hungerManager.getFoodLevel();
        //float saturation = hungerManager.getSaturationLevel(); //TODO: usage TBD

        int hungerColor = 0xBB3E2723;
        DrawableHelper.fill(stack, baseRelativeStartW(hunger, maxHunger), baseStartH, baseEndW, baseEndH, hungerColor);
    }

    private void airBar(){
        int maxAir = playerEntity.getMaxAir();
        int rawAir = maxAir - playerEntity.getAir();

        int airColor = 0xBB1A237E;
        DrawableHelper.fill(stack, baseRelativeStartW(rawAir, maxAir), baseStartH, baseEndW, baseEndH, airColor);
    }

    private void barText(){
        int health = MathHelper.ceil(playerEntity.getHealth());
        int absorption = MathHelper.ceil(playerEntity.getAbsorptionAmount());

        int maxHunger = 20;
        int hunger = maxHunger - hungerManager.getFoodLevel();

        int maxAir = playerEntity.getMaxAir();
        int rawAir = maxAir - playerEntity.getAir();
        int air = rawAir / 15;

        boolean hardcore = playerEntity.world.getLevelProperties().isHardcore();

        String value = String.valueOf(health);

        if (absorption > 0)
            value += "+" + absorption; //TODO: effect time
        if (air > 0)
            value += "-a" + air;
        if (hunger > 0)
            value += "-" + hunger;
        if (hardcore)
            value += "!";

        int textColor = 0x99FFFFFF;
        int textX = baseEndW - client.textRenderer.getWidth(value);
        int textY = baseStartH + 1;

        client.textRenderer.draw(stack, value, textX, textY, textColor);
    }

    private void xpBar(){
        int xpLevel = playerEntity.experienceLevel;
        int maxXp = 183;
        int xp = (int)(playerEntity.experienceProgress * maxXp);

        int xpColor = 0xFF00C853;
        int relativeEndW = Calculations.relativeW(xpStartW, xpEndW, xp, maxXp);

        int textX = xpStartW + 3;
        int textY = xpStartH - 10;

        client.textRenderer.drawWithShadow(stack, String.valueOf(xpLevel), textX, textY, xpColor);
        DrawableHelper.fill(stack, xpStartW, xpStartH, xpEndW, xpEndH, backgroundColor);
        DrawableHelper.fill(stack, xpStartW, xpStartH, relativeEndW, xpEndH, xpColor);
    }

    private void jumpBar(){
        if (client.player == null) throw new AssertionError();

        int maxHeight = Calculations.getPreciseInt(1.0F);
        int height = Calculations.getPreciseInt(client.player.method_3151());

        int jumpColor = 0xFF795548;

        int relativeStartH = Calculations.relativeW(jumpEndH, jumpStartH, height, maxHeight);
        DrawableHelper.fill(stack, jumpStartW, jumpStartH, jumpEndW, jumpEndH, backgroundColor);
        DrawableHelper.fill(stack, jumpStartW, jumpEndH, jumpEndW, relativeStartH, jumpColor);
    }

    private void mountBar(){
        LivingEntity livingEntity = this.getRiddenEntity();
        if (livingEntity != null) {
            float rawHealth = livingEntity.getHealth();
            float maxHealth = livingEntity.getMaxHealth();
            int health = (int) Math.ceil(rawHealth);

            int healthColor = 0xFFF57F17;
            int textColor = 0x99FFFFFF;

            String value = String.valueOf(health);
            int textX = baseEndW - client.textRenderer.getWidth(value);
            int textY = mountStartH + 1;

            DrawableHelper.fill(stack, baseStartW, mountStartH, baseEndW, mountEndH, backgroundColor);
            DrawableHelper.fill(stack, baseStartW, mountStartH, baseRelativeEndW(Calculations.getPreciseInt(rawHealth), Calculations.getPreciseInt(maxHealth)), mountEndH, healthColor);
            client.textRenderer.draw(stack, value, textX, textY, textColor);
        }
    }

    private void debugText(String value){
        client.textRenderer.drawWithShadow(stack, value, baseEndW + 15, baseStartH + 1, 0xFFFFFFFF);
    }

    private int baseRelativeEndW(int value, int total){
        return Calculations.relativeW(baseStartW, baseEndW, value, total);
    }

    private int baseRelativeStartW(int value, int total){
        return Calculations.relativeW(baseEndW, baseStartW, value, total);
    }
}
