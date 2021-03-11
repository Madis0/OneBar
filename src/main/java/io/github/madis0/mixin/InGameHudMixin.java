package io.github.madis0.mixin;

import io.github.madis0.Calculations;
import io.github.madis0.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
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
    private final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

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

        if(!config.experience.leftHanded){
            xpStartW = baseEndW + 2;
        }
        else {
            xpStartW = baseStartW - 28;
        }
        xpEndW = xpStartW + 18;
        xpStartH = baseStartH + 28;
        xpEndH = xpStartH + 1;

        jumpStartW = (this.scaledWidth / 2) - 2;
        jumpStartH = (this.scaledHeight / 2) + 10;
        jumpEndW = jumpStartW + 3;
        jumpEndH = jumpStartH + 50;

        mountStartH = baseStartH - 12;
        mountEndH = mountStartH + 9;

        boolean barsVisible = !client.options.hudHidden && client.interactionManager.hasStatusBars();

        if (client.interactionManager == null) throw new AssertionError();

        if(config.showOneBar && barsVisible) renderBar();
        if(config.showOneBar && barsVisible && config.goodThings.showArmor) armorBar();

        mountBar();

    }

    //TODO: overwrite only on-demand

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE"), cancellable = true)
    private void renderStatusBars(MatrixStack matrices, CallbackInfo ci){
        if(!config.showVanilla) ci.cancel();
    }
    @Inject(method = "renderExperienceBar", at = @At(value = "INVOKE"), cancellable = true)
    private void renderExperienceBar(MatrixStack matrices, int x, CallbackInfo ci){
        if(!config.showVanilla) ci.cancel();
    }

    @Inject(method = "renderMountJumpBar", at = @At(value = "INVOKE"), cancellable = true)
    private void renderMountJumpBar(MatrixStack matrices, int x, CallbackInfo ci) {
        if(!config.showVanilla) ci.cancel();
        if(config.showOneBar && config.entity.showHorseJump) jumpBar();
    }
    @Inject(method = "renderMountHealth", at = @At(value = "INVOKE"), cancellable = true)
    private void renderMountHealth(MatrixStack matrices, CallbackInfo ci) {
        if(!config.showVanilla) ci.cancel();
        if(config.showOneBar) mountBar();
    }

    private void renderBar(){
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null) {
            healthBar();
            hungerBar();
            fireBar();
            airBar();
            xpBar();
            barText();
        }
    }

    private void healthBar(){
        float rawHealth = playerEntity.getHealth();
        float maxHealth = playerEntity.getMaxHealth();

        DrawableHelper.fill(stack, baseStartW, baseStartH, baseEndW, baseEndH, config.backgroundColor);
        DrawableHelper.fill(stack, baseStartW, baseStartH, baseRelativeEndW(Calculations.GetPreciseInt(rawHealth), Calculations.GetPreciseInt(maxHealth)), baseEndH, config.goodThings.healthColor);
    }

    private void armorBar(){
        int maxArmor = 20;
        int armor = playerEntity.getArmor();

        DrawableHelper.fill(stack, baseStartW, baseStartH - 1, baseRelativeEndW(armor, maxArmor), baseStartH, config.goodThings.armorColor);
    }

    private void hungerBar(){
        int maxHunger = 20;
        int hunger = maxHunger - hungerManager.getFoodLevel();
        //float saturation = hungerManager.getSaturationLevel(); //TODO: usage TBD

        DrawableHelper.fill(stack, baseRelativeStartW(hunger, maxHunger), baseStartH, baseEndW, baseEndH, config.badThings.hungerColor);
    }

    private void airBar(){
        int maxAir = playerEntity.getMaxAir();
        int rawAir = maxAir - playerEntity.getAir();

        DrawableHelper.fill(stack, baseRelativeStartW(rawAir, maxAir), baseStartH, baseEndW, baseEndH, config.badThings.airColor);
    }

    private void fireBar(){
        if(config.badThings.showFire && playerEntity.doesRenderOnFire() && !playerEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)){
            DrawableHelper.fill(stack, baseStartW, baseStartH, baseEndW, baseEndH, config.badThings.fireColor);
        }
    }

    private void barText(){
        int health = MathHelper.ceil(playerEntity.getHealth());
        int absorption = MathHelper.ceil(playerEntity.getAbsorptionAmount());

        int maxHunger = 20;
        int hunger = maxHunger - hungerManager.getFoodLevel();

        int maxAir = playerEntity.getMaxAir();
        int rawAir = maxAir - playerEntity.getAir();
        int air = rawAir / 15;
        boolean altAir =  playerEntity.isSubmergedInWater();
        boolean fire = playerEntity.doesRenderOnFire();

        boolean hardcore = playerEntity.world.getLevelProperties().isHardcore();

        String value = Calculations.MakeFraction(health);

        if (absorption > 0)
            value += "+" + Calculations.MakeFraction(absorption); //TODO: effect time
        if (air > 0 || altAir)
            value += "-" + new TranslatableText("text.onebar.air").getString() + Calculations.MakeFraction(air);
        if (fire)
            value += "-" + new TranslatableText("text.onebar.fire").getString();
        if (hunger > 0)
            value += "-" + Calculations.MakeFraction(hunger);
        if (hardcore)
            value += "!";

        int textX = baseEndW - client.textRenderer.getWidth(value);
        int textY = baseStartH + 1;

        client.textRenderer.draw(stack, value, textX, textY, config.textColor);
    }

    private void xpBar(){
        int xpLevel = playerEntity.experienceLevel;
        int maxXp = 183;
        int xp = (int)(playerEntity.experienceProgress * maxXp);

        int relativeEndW = Calculations.RelativeW(xpStartW, xpEndW, xp, maxXp);

        int textX = xpStartW + 3;
        int textY = xpStartH - 10;

        client.textRenderer.drawWithShadow(stack, String.valueOf(xpLevel), textX, textY, config.experience.xpColor);
        DrawableHelper.fill(stack, xpStartW, xpStartH, xpEndW, xpEndH, config.backgroundColor);
        DrawableHelper.fill(stack, xpStartW, xpStartH, relativeEndW, xpEndH, config.experience.xpColor);
    }

    private void jumpBar(){
        if (client.player == null) throw new AssertionError();

        int maxHeight = Calculations.GetPreciseInt(1.0F);
        int height = Calculations.GetPreciseInt(client.player.method_3151());


        int relativeStartH = Calculations.RelativeW(jumpEndH, jumpStartH, height, maxHeight);
        DrawableHelper.fill(stack, jumpStartW, jumpStartH, jumpEndW, jumpEndH, config.backgroundColor);
        DrawableHelper.fill(stack, jumpStartW, jumpEndH, jumpEndW, relativeStartH, config.entity.jumpColor);
    }

    private void mountBar(){
        LivingEntity livingEntity = this.getRiddenEntity();
        if (livingEntity != null) {
            float rawHealth = livingEntity.getHealth();
            float maxHealth = livingEntity.getMaxHealth();
            int health = (int) Math.ceil(rawHealth);

            String value = String.valueOf(health);
            int textX = baseEndW - client.textRenderer.getWidth(value);
            int textY = mountStartH + 1;

            DrawableHelper.fill(stack, baseStartW, mountStartH, baseEndW, mountEndH, config.backgroundColor);
            DrawableHelper.fill(stack, baseStartW, mountStartH, baseRelativeEndW(Calculations.GetPreciseInt(rawHealth), Calculations.GetPreciseInt(maxHealth)), mountEndH, config.entity.healthColor);
            client.textRenderer.draw(stack, value, textX, textY, config.textColor);
        }
    }

    private void debugText(String value){
        client.textRenderer.drawWithShadow(stack, value, baseEndW + 15, baseStartH + 1, config.textColor);
    }

    private int baseRelativeEndW(int value, int total){
        return Calculations.RelativeW(baseStartW, baseEndW, value, total);
    }

    private int baseRelativeStartW(int value, int total){
        return Calculations.RelativeW(baseEndW, baseStartW, value, total);
    }
}
