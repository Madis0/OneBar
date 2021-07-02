package io.github.madis0.mixin;

import io.github.madis0.Calculations;
import io.github.madis0.ClientProperties;
import io.github.madis0.ModConfig;
import io.github.madis0.PlayerProperties;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Final;
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
    @Final @Shadow private MinecraftClient client;
    @Shadow protected abstract LivingEntity getRiddenEntity();

    private MatrixStack stack;
    private final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    Difficulty difficulty;
    PlayerProperties playerProperties;
    ClientProperties clientProperties;

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
        stack = matrixStack;
        playerProperties = new PlayerProperties();
        difficulty = client.world.getDifficulty();

        // Dimensions and locations

        baseStartW = this.scaledWidth / 2 - 91;
        baseEndW = baseStartW + 182;
        baseStartH = this.scaledHeight - 33;
        baseEndH = baseStartH + 9;

        if (client.options.mainArm == Arm.RIGHT && client.options.attackIndicator != AttackIndicator.HOTBAR)
            xpStartW = baseEndW + 6;
        else if (client.options.mainArm == Arm.RIGHT)
            xpStartW = baseEndW + 6 + 20;
        else if (client.options.mainArm == Arm.LEFT && client.options.attackIndicator != AttackIndicator.HOTBAR)
            xpStartW = baseStartW - 22;
        else if (client.options.mainArm == Arm.LEFT)
            xpStartW = baseStartW - 22 - 20;

        xpEndW = xpStartW + 18;
        xpStartH = baseStartH + 28;
        xpEndH = xpStartH + 1;

        jumpStartW = (this.scaledWidth / 2) - 2;
        jumpStartH = (this.scaledHeight / 2) + 10;
        jumpEndW = jumpStartW + 3;
        jumpEndH = jumpStartH + 50;

        mountStartH = baseStartH - 12;
        mountEndH = mountStartH + 9;

        // Method calls

        boolean barsVisible = !client.options.hudHidden && client.interactionManager.hasStatusBars();

        if(config.showOneBar && barsVisible) renderOneBar();
        if(config.showOneBar) mountBar();
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
        if(config.entity.showHorseJump) jumpBar();
    }
    @Inject(method = "renderMountHealth", at = @At(value = "INVOKE"), cancellable = true)
    private void renderMountHealth(MatrixStack matrices, CallbackInfo ci) {
        if(config.showOneBar){
            ci.cancel();
            mountBar();
        }
    }

    private void renderOneBar(){
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null) {
            barBackground();
            if(config.healthEstimates && !config.uhcMode) naturalRegenerationBar();
            if(config.healthEstimates) regenerationBar();
            healthBar();
            if(config.healthEstimates) poisonBar();
            if(config.healthEstimates) witherBar();
            if(config.healthEstimates) hungerEffectBar();
            hungerBar();
            if(config.badThings.showFire) fireBar();
            freezeBar();
            airBar();
            xpBar();
            barText();
            if(config.otherBars.heldFoodHungerBar) heldFoodBar();
            if(config.otherBars.showArmorBar) armorBar();
        }
    }

    private void barBackground(){
        DrawableHelper.fill(stack, baseStartW, baseStartH, baseEndW, baseEndH, config.backgroundColor);
    }

    private void armorBar(){
        DrawableHelper.fill(stack, baseStartW, baseStartH - 1, baseRelativeEndW(playerProperties.armor, playerProperties.maxArmor), baseStartH, config.otherBars.armorColor);
    }

    private void heldFoodBar(){
        if(playerProperties.hasHunger){
            if(playerProperties.hunger >= clientProperties.heldFoodHunger)
                DrawableHelper.fill(stack, baseRelativeStartW(clientProperties.heldFoodHunger, playerProperties.maxHunger), baseEndH, baseEndW, baseEndH + 1, config.otherBars.heldFoodHungerGoodColor);
            else
                DrawableHelper.fill(stack, baseRelativeStartW(clientProperties.heldFoodHunger, playerProperties.maxHunger), baseEndH, baseEndW, baseEndH + 1, config.otherBars.heldFoodHungerWasteColor);

        }
    }

    private void naturalRegenerationBar(){
        if (playerProperties.naturalRegenerationHealth > playerProperties.health){ // The if and float avoid regen being visible behind health if regen will not happen, because health is shown in precise floats
            float lessPreciseRegen = (float)playerProperties.naturalRegenerationHealth - (float)0.2;

            DrawableHelper.fill(stack, baseStartW, baseStartH, baseRelativeEndW(Math.max(Calculations.GetPreciseInt(lessPreciseRegen), Calculations.GetPreciseInt(playerProperties.rawHealth)), Calculations.GetPreciseInt(playerProperties.maxRawHealth)), baseEndH, config.goodThings.naturalRegenerationColor);
        }
    }

    private void regenerationBar(){
        DrawableHelper.fill(stack, baseStartW, baseStartH, baseRelativeEndW(Math.max(playerProperties.regenerationHealth, 0), xpEndW), baseEndH, config.goodThings.regenerationColor);
    }

    private void healthBar(){
        DrawableHelper.fill(stack, baseStartW, baseStartH, baseRelativeEndW(Calculations.GetPreciseInt(playerProperties.rawHealth), Calculations.GetPreciseInt(playerProperties.maxRawHealth)), baseEndH, config.goodThings.healthColor);
    }

    private void poisonBar(){
        DrawableHelper.fill(stack, baseRelativeStartW(playerProperties.maxHealth - playerProperties.poisonHealth, playerProperties.maxHealth), baseStartH, baseEndW, baseEndH, config.badThings.poisonColor);
    }

    private void witherBar(){
        DrawableHelper.fill(stack, baseRelativeStartW(playerProperties.maxHealth - playerProperties.witherHealth, playerProperties.maxHealth), baseStartH, baseEndW, baseEndH, config.badThings.witherColor);
    }

    private void hungerEffectBar(){
        if (playerProperties.hungerEffectEstimate > playerProperties.hunger && !difficulty.equals(Difficulty.PEACEFUL)){
            DrawableHelper.fill(stack, baseRelativeStartW(playerProperties.hungerEffectEstimate, playerProperties.maxHunger), baseStartH, baseEndW, baseEndH, config.badThings.hungerEffectColor);
        }
    }

    private void hungerBar(){
        DrawableHelper.fill(stack, baseRelativeStartW(playerProperties.hunger, playerProperties.maxHunger), baseStartH, baseEndW, baseEndH, config.badThings.hungerColor);
    }

    private void airBar(){
        DrawableHelper.fill(stack, baseRelativeStartW(playerProperties.rawAir, playerProperties.maxRawAir), baseStartH, baseEndW, baseEndH, config.badThings.airColor);
    }

    private void freezeBar(){
        DrawableHelper.fill(stack, baseRelativeStartW(playerProperties.rawFreeze, playerProperties.maxRawFreeze), baseStartH, baseEndW, baseEndH, config.badThings.freezeColor);
    }

    private void fireBar(){
        if (playerProperties.isBurning && !playerProperties.hasFireResistance){
            DrawableHelper.fill(stack, baseStartW, baseStartH, baseEndW, baseEndH, config.badThings.fireColor);
        }
    }

    private void barText(){
        String value = "";
        if(config.textSettings.showText) {

            // Health values

            if (config.healthEstimates && config.textSettings.estimatesParentheses && (playerProperties.hasHunger || playerProperties.hasHungerEffect || playerProperties.isUnderwater || playerProperties.isFreezing || playerProperties.isBurning || playerProperties.hasAbsorption || (playerProperties.hasResistance && config.goodThings.showResistance)) &&
                    ((playerProperties.naturalRegenerationHealth > playerProperties.health && !config.uhcMode) || playerProperties.hasRegeneration || playerProperties.isStarving || playerProperties.hasPoison || playerProperties.hasWither || playerProperties.isGettingFreezeDamage || playerProperties.isBurningOnFire || playerProperties.isDrowning || playerProperties.isSuffocating))
                value += "(";

            value += Calculations.MakeFraction(playerProperties.health, false);

            if(config.healthEstimates){
                if (playerProperties.naturalRegenerationHealth > playerProperties.health && !config.uhcMode)
                    value += "→" + Calculations.MakeFraction(playerProperties.naturalRegenerationHealth, config.textSettings.estimatesItalic);
                if (playerProperties.hasRegeneration)
                    value += "→" + Calculations.MakeFraction(playerProperties.regenerationHealth, config.textSettings.estimatesItalic);
                if (playerProperties.isStarving)
                    value += "→" + Calculations.MakeFraction(playerProperties.starvationHealthEstimate, config.textSettings.estimatesItalic);
                if (playerProperties.hasPoison)
                    value += "→" + Calculations.MakeFraction(playerProperties.poisonHealth, config.textSettings.estimatesItalic);
                if (playerProperties.hasWither)
                    value += "→" + Calculations.MakeFraction(playerProperties.witherHealth, config.textSettings.estimatesItalic);
                if (playerProperties.isGettingFreezeDamage)
                    value += "→" + Calculations.MakeFraction(0, config.textSettings.estimatesItalic);
                if (playerProperties.isBurningOnFire)
                    value += "→" + Calculations.MakeFraction(0, config.textSettings.estimatesItalic);
                if (playerProperties.isDrowning)
                    value += "→" + Calculations.MakeFraction(0, config.textSettings.estimatesItalic);
                if (playerProperties.isSuffocating)
                    value += "→" + Calculations.MakeFraction(0, config.textSettings.estimatesItalic);
                if (config.textSettings.estimatesParentheses && (playerProperties.hasHunger || playerProperties.hasHungerEffect || playerProperties.isUnderwater || playerProperties.isFreezing || playerProperties.isBurning || playerProperties.hasAbsorption || (playerProperties.hasResistance && config.goodThings.showResistance)) &&
                        ((playerProperties.naturalRegenerationHealth > playerProperties.health && !config.uhcMode) || playerProperties.hasRegeneration || playerProperties.isStarving || playerProperties.hasPoison || playerProperties.hasWither || playerProperties.isGettingFreezeDamage || playerProperties.isBurningOnFire || playerProperties.isDrowning || playerProperties.isSuffocating))
                    value += ")";
            }
        }

        // Additive values

        if (playerProperties.hasAbsorption)
            value += "+" + Calculations.MakeFraction(playerProperties.absorption, false);

        if(config.textSettings.showText) { // Separated if because order matters
            if (playerProperties.hasResistance && config.goodThings.showResistance)
                value += "+" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.resistanceEmoji" : "text.onebar.resistance", playerProperties.resistancePercent).getString();

            // Subtractive values

            if (playerProperties.isUnderwater && !playerProperties.hasWaterBreathing)
                value += "-" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.airEmoji" : "text.onebar.air", Calculations.MakeFraction(playerProperties.air, false)).getString();
            if (playerProperties.isUnderwater && playerProperties.hasWaterBreathing)
                value += "-§m" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.airEmoji" : "text.onebar.air", Calculations.MakeFraction(playerProperties.air, false)).getString() + "§r";
            if (playerProperties.isFreezing)
                value += "-" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.freezeEmoji" : "text.onebar.freeze", Calculations.MakeFraction(playerProperties.freeze, false)).getString();
            if (playerProperties.isBurning && !playerProperties.hasFireResistance && config.badThings.showFire)
                value += "-" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.fireEmoji" : "text.onebar.fire", playerProperties.burningMultiplier).getString();
            if (playerProperties.isBurning && playerProperties.hasFireResistance && config.badThings.showFire)
                value += "-§m" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.fireEmoji" : "text.onebar.fire", playerProperties.burningMultiplier).getString() + "§r";
            if (playerProperties.hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates))
                value += "-";
            if (playerProperties.hasHungerEffect && config.healthEstimates && config.textSettings.estimatesParentheses)
                value += "(";
            if (playerProperties.hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates))
                value += Calculations.MakeFraction(playerProperties.hunger, false);
            if (playerProperties.hasHunger && playerProperties.saturation < 1 && config.badThings.showHungerDecreasing)
                value += "↓";
            if (playerProperties.hasHungerEffect && config.healthEstimates)
                value += "→" + Calculations.MakeFraction(playerProperties.hungerEffectEstimate, config.textSettings.estimatesItalic);
            if (playerProperties.hasHungerEffect && config.healthEstimates && config.textSettings.estimatesParentheses)
                value += ")";
        }

        if (clientProperties.isHardcore)
            value += new TranslatableText(config.textSettings.useEmoji ? "text.onebar.hardcoreEmoji" : "text.onebar.hardcore").getString();

        int textX = baseEndW - client.textRenderer.getWidth(value);
        int textY = baseStartH + 1;

        client.textRenderer.draw(stack, value, textX, textY, config.textSettings.textColor);
    }

    private void xpBar(){
        int relativeEndW = Calculations.RelativeW(xpStartW, xpEndW, playerProperties.xp, playerProperties.maxXp);

        int textX = xpStartW + 3;
        int textY = xpStartH - 10;

        client.textRenderer.drawWithShadow(stack, String.valueOf(playerProperties.xpLevel), textX, textY, config.otherBars.xpColor);
        DrawableHelper.fill(stack, xpStartW, xpStartH, xpEndW, xpEndH, config.backgroundColor);
        DrawableHelper.fill(stack, xpStartW, xpStartH, relativeEndW, xpEndH, config.otherBars.xpColor);
    }

    private void jumpBar(){
        int barHeight = Calculations.GetPreciseInt(1.0F);
        int jumpHeight = Calculations.GetPreciseInt(client.player.getMountJumpStrength());

        int relativeStartH = Calculations.RelativeW(jumpEndH, jumpStartH, jumpHeight, barHeight);
        DrawableHelper.fill(stack, jumpStartW, jumpStartH, jumpEndW, jumpEndH, config.backgroundColor);
        DrawableHelper.fill(stack, jumpStartW, jumpEndH, jumpEndW, relativeStartH, config.entity.jumpColor);
    }

    private void mountBar(){
        LivingEntity mountEntity = this.getRiddenEntity();
        if (mountEntity != null) {
            float mountRawHealth = mountEntity.getHealth();
            float mountMaxHealth = mountEntity.getMaxHealth();
            int health = (int) Math.ceil(mountRawHealth);
            int horseArmor = mountEntity.getArmor();
            int horseMaxArmor = 11; // Diamond horse armor

            String value = String.valueOf(health);
            int textX = baseEndW - client.textRenderer.getWidth(value);
            int textY = mountStartH + 1;

            DrawableHelper.fill(stack, baseStartW, mountStartH, baseEndW, mountEndH, config.backgroundColor);
            DrawableHelper.fill(stack, baseStartW, mountStartH, baseRelativeEndW(Calculations.GetPreciseInt(mountRawHealth), Calculations.GetPreciseInt(mountMaxHealth)), mountEndH, config.entity.healthColor);
            if(config.otherBars.showArmorBar) DrawableHelper.fill(stack, baseStartW, mountStartH - 1, baseRelativeEndW(horseArmor, horseMaxArmor), mountStartH, config.otherBars.armorColor);
            if(config.textSettings.showText) client.textRenderer.draw(stack, value, textX, textY, config.textSettings.textColor);
        }
    }

    private void debugText(String value){
        client.textRenderer.drawWithShadow(stack, value, baseEndW + 15, baseStartH + 1, config.textSettings.textColor);
    }

    private int baseRelativeEndW(int value, int total){
        return Calculations.RelativeW(baseStartW, baseEndW, value, total);
    }

    private int baseRelativeStartW(int value, int total){
        return Calculations.RelativeW(baseEndW, baseStartW, value, total);
    }
}
