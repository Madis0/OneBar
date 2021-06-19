package io.github.madis0.mixin;

import io.github.madis0.Calculations;
import io.github.madis0.ModConfig;
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
    private PlayerEntity playerEntity;
    private HungerManager hungerManager;
    private final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    Difficulty difficulty;

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

    float rawHealth;
    float maxRawHealth;
    int health;
    int maxHealth;
    int absorption;

    int maxArmor;
    int armor;

    int maxHunger;
    int hunger;
    boolean hasHunger;
    boolean isStarving;
    float rawSaturation;
    int saturation;

    int maxRawAir;
    int rawAir;
    int air;
    boolean isUnderwater;
    boolean isDrowning;

    boolean isSuffocating;

    boolean isBurning;
    boolean isBurningOnFire;
    int burnSource;
    int burningMultiplier;

    int maxRawFreeze;
    int rawFreeze;
    int freeze;
    boolean isFreezing;
    boolean isGettingFreezeDamage;

    int resistancePercent;
    int regenerationHealth;
    int poisonHealth;
    int witherHealth;
    boolean hasRegeneration;
    boolean hasPoison;
    boolean hasWither;
    boolean hasFireResistance;
    boolean hasWaterBreathing;

    boolean hasHungerEffect;
    int hungerEffectSaturationLoss;
    int hungerEffectEstimate;
    int previousHungerEffectEstimate;
    int starvationHealthEstimate;

    int naturalRegenerationAddition;
    int naturalRegenerationHealth;
    int previousNaturalRegenerationHealth;

    int xpLevel;
    int maxXp;
    int xp;

    int heldFoodHunger;
    boolean isHardcore;

    @Inject(at = @At("TAIL"), method = "render")
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo info) {
        stack = matrixStack;
        playerEntity = this.getCameraPlayer();
        hungerManager = playerEntity.getHungerManager();
        difficulty = playerEntity.world.getDifficulty();

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

        // Player property calculations

        rawHealth = playerEntity.getHealth();
        maxRawHealth = playerEntity.getMaxHealth();
        health = MathHelper.ceil(rawHealth);
        maxHealth = MathHelper.ceil(maxRawHealth);
        absorption = MathHelper.ceil(playerEntity.getAbsorptionAmount());

        maxArmor = 20;
        armor = playerEntity.getArmor();

        maxHunger = 20;
        hunger = maxHunger - hungerManager.getFoodLevel();
        hasHunger = hunger > 0;
        isStarving = hunger >= maxHunger;
        rawSaturation = hungerManager.getSaturationLevel();
        saturation = MathHelper.ceil(rawSaturation);

        maxRawAir = playerEntity.getMaxAir();
        rawAir = maxRawAir - playerEntity.getAir();
        air = Math.min(rawAir, maxRawAir) / 15;
        isUnderwater =  playerEntity.isSubmergedInWater();
        isDrowning = rawAir >= maxRawAir;

        isSuffocating = playerEntity.isInsideWall();

        isBurning = playerEntity.doesRenderOnFire();
        burnSource = playerEntity.getFireTicks();
        if(burnSource == -20) burningMultiplier = 1;
        if(burnSource == 1) burningMultiplier = 2;
        if(burnSource == 0) burningMultiplier = 4;
        isBurningOnFire = (burningMultiplier == 2 || burningMultiplier == 4);

        maxRawFreeze = playerEntity.getMinFreezeDamageTicks();
        rawFreeze = playerEntity.getFrozenTicks();
        freeze = rawFreeze / 7;
        isFreezing = rawFreeze > 0;
        isGettingFreezeDamage = playerEntity.isFreezing() && !difficulty.equals(Difficulty.PEACEFUL);

        xpLevel = playerEntity.experienceLevel;
        maxXp = 183;
        xp = (int)(playerEntity.experienceProgress * maxXp);

        heldFoodHunger = 0;
        ItemStack heldItem = playerEntity.getMainHandStack();
        if(!heldItem.isFood()) heldItem = playerEntity.getOffHandStack();

        if(heldItem.isFood()){
            FoodComponent itemFood = heldItem.getItem().getFoodComponent();
            heldFoodHunger = itemFood.getHunger();
        }

        isHardcore = playerEntity.world.getLevelProperties().isHardcore();

        // Potion effects

        StatusEffectInstance resistanceEffect = playerEntity.getStatusEffect(StatusEffects.RESISTANCE);
        resistancePercent = 0;
        if(resistanceEffect != null) resistancePercent = (resistanceEffect.getAmplifier() + 1) * 20;

        StatusEffectInstance regenerationEffect = playerEntity.getStatusEffect(StatusEffects.REGENERATION);
        regenerationHealth = 0;
        if(regenerationEffect != null) regenerationHealth =
                Calculations.GetEstimatedHealthRegen(50,
                                                     regenerationEffect.getAmplifier(),
                                                     regenerationEffect.getDuration(),
                                                     health,
                                                     maxHealth);

        StatusEffectInstance poisonEffect = playerEntity.getStatusEffect(StatusEffects.POISON);
        poisonHealth = maxHealth;
        if(poisonEffect != null) poisonHealth =
                Calculations.GetEstimatedHealthDamage(25,
                                                      poisonEffect.getAmplifier(),
                                                      poisonEffect.getDuration(),
                                                      health,
                                                      1);

        StatusEffectInstance witherEffect = playerEntity.getStatusEffect(StatusEffects.WITHER);
        witherHealth = maxHealth;
        if(witherEffect != null) witherHealth =
                Calculations.GetEstimatedHealthDamage(40,
                                                      witherEffect.getAmplifier(),
                                                      witherEffect.getDuration(),
                                                      health,
                                                      0);

        StatusEffectInstance hungerEffect = playerEntity.getStatusEffect(StatusEffects.HUNGER);
        hungerEffectSaturationLoss = 0;
        if(hungerEffect != null) {
            int duration = hungerEffect.getDuration();
            float hungerEffectExhaustionLoss = 0.005F * (float)(hungerEffect.getAmplifier() + 1) * duration;
            // Exhaustion is server-side, so lost saturation is rounded up to be approximate
            hungerEffectSaturationLoss = (int) Math.ceil(hungerEffectExhaustionLoss / (float)4);

            if ((hunger + hungerEffectSaturationLoss) != (previousHungerEffectEstimate - 1)) {
                hungerEffectEstimate = Math.max(Math.min(hunger + hungerEffectSaturationLoss, maxHunger), 0);
                previousHungerEffectEstimate = hungerEffectEstimate;
            }
        }
        else {
            hungerEffectEstimate = hunger;
            previousHungerEffectEstimate = hungerEffectEstimate;
        }

        if(isStarving){
            if(difficulty == Difficulty.EASY)
                starvationHealthEstimate = Math.min(10, health);
            else if(difficulty == Difficulty.NORMAL)
                starvationHealthEstimate = Math.min(1, health);
            else if(difficulty == Difficulty.HARD)
                starvationHealthEstimate = 0;
        }

        naturalRegenerationAddition = 0;
        if(health < maxHealth){
            // Approximate formula for calculating regeneration addition health: saturation + (2.5 - hunger) * exhaustion max / 6 exhaustion per healed heart
            if (hunger < 3 && !difficulty.equals(Difficulty.PEACEFUL))
                naturalRegenerationAddition = MathHelper.ceil((((float)saturation + (float)(2.5 - hunger)) * (float)4 / (float)6));
            else if(difficulty.equals(Difficulty.PEACEFUL))
                naturalRegenerationAddition = maxHealth - health; // because saturation goes from 2 to 0 for some reason

            if((health + naturalRegenerationAddition) != (previousNaturalRegenerationHealth + 1)){
                naturalRegenerationHealth = Math.min(health + naturalRegenerationAddition, maxHealth);
                previousNaturalRegenerationHealth = naturalRegenerationHealth;                
            }
        }
        else {
            naturalRegenerationHealth = health;
            previousNaturalRegenerationHealth = naturalRegenerationHealth;
        }

        hasRegeneration = playerEntity.hasStatusEffect(StatusEffects.REGENERATION);
        hasPoison = playerEntity.hasStatusEffect(StatusEffects.POISON);
        hasWither = playerEntity.hasStatusEffect(StatusEffects.WITHER);
        hasFireResistance = playerEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
        hasWaterBreathing = playerEntity.hasStatusEffect(StatusEffects.WATER_BREATHING) || playerEntity.hasStatusEffect(StatusEffects.CONDUIT_POWER);
        hasHungerEffect = playerEntity.hasStatusEffect(StatusEffects.HUNGER) && !difficulty.equals(Difficulty.PEACEFUL);

        // Method calls

        boolean barsVisible = !client.options.hudHidden && client.interactionManager.hasStatusBars();

        if(config.showOneBar && barsVisible) renderOneBar();
        if(config.showOneBar && barsVisible && config.otherBars.showArmorBar) armorBar();
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
        }
    }

    private void barBackground(){
        DrawableHelper.fill(stack, baseStartW, baseStartH, baseEndW, baseEndH, config.backgroundColor);
    }

    private void armorBar(){
        DrawableHelper.fill(stack, baseStartW, baseStartH - 1, baseRelativeEndW(armor, maxArmor), baseStartH, config.otherBars.armorColor);
    }

    private void heldFoodBar(){
        if(hasHunger){
            if(hunger >= heldFoodHunger)
                DrawableHelper.fill(stack, baseRelativeStartW(heldFoodHunger, maxHunger), baseEndH, baseEndW, baseEndH + 1, config.otherBars.heldFoodHungerGoodColor);
            else
                DrawableHelper.fill(stack, baseRelativeStartW(heldFoodHunger, maxHunger), baseEndH, baseEndW, baseEndH + 1, config.otherBars.heldFoodHungerWasteColor);

        }
    }

    private void naturalRegenerationBar(){
        if (naturalRegenerationHealth > health){ // The if and float avoid regen being visible behind health if regen will not happen, because health is shown in precise floats
            float lessPreciseRegen = (float)naturalRegenerationHealth - (float)0.2;

            DrawableHelper.fill(stack, baseStartW, baseStartH, baseRelativeEndW(Math.max(Calculations.GetPreciseInt(lessPreciseRegen), Calculations.GetPreciseInt(rawHealth)), Calculations.GetPreciseInt(maxRawHealth)), baseEndH, config.goodThings.naturalRegenerationColor);
        }
    }

    private void regenerationBar(){
        DrawableHelper.fill(stack, baseStartW, baseStartH, baseRelativeEndW(Math.max(regenerationHealth, 0), maxHealth), baseEndH, config.goodThings.regenerationColor);
    }

    private void healthBar(){
        DrawableHelper.fill(stack, baseStartW, baseStartH, baseRelativeEndW(Calculations.GetPreciseInt(rawHealth), Calculations.GetPreciseInt(maxRawHealth)), baseEndH, config.goodThings.healthColor);
    }

    private void poisonBar(){
        DrawableHelper.fill(stack, baseRelativeStartW(maxHealth - poisonHealth, maxHealth), baseStartH, baseEndW, baseEndH, config.badThings.poisonColor);
    }

    private void witherBar(){
        DrawableHelper.fill(stack, baseRelativeStartW(maxHealth - witherHealth, maxHealth), baseStartH, baseEndW, baseEndH, config.badThings.witherColor);
    }

    private void hungerEffectBar(){
        if (hungerEffectEstimate > hunger && !difficulty.equals(Difficulty.PEACEFUL)){
            DrawableHelper.fill(stack, baseRelativeStartW(hungerEffectEstimate, maxHunger), baseStartH, baseEndW, baseEndH, config.badThings.hungerEffectColor);
        }
    }

    private void hungerBar(){
        DrawableHelper.fill(stack, baseRelativeStartW(hunger, maxHunger), baseStartH, baseEndW, baseEndH, config.badThings.hungerColor);
    }

    private void airBar(){
        DrawableHelper.fill(stack, baseRelativeStartW(rawAir, maxRawAir), baseStartH, baseEndW, baseEndH, config.badThings.airColor);
    }

    private void freezeBar(){
        DrawableHelper.fill(stack, baseRelativeStartW(rawFreeze, maxRawFreeze), baseStartH, baseEndW, baseEndH, config.badThings.freezeColor);
    }

    private void fireBar(){
        if (isBurning && !hasFireResistance){
            DrawableHelper.fill(stack, baseStartW, baseStartH, baseEndW, baseEndH, config.badThings.fireColor);
        }
    }

    private void barText(){
        String value = "";
        if(config.textSettings.showText) {

            // Health values

            if (config.healthEstimates && config.textSettings.estimatesParentheses && (hasHunger || hasHungerEffect) &&
                    ((naturalRegenerationHealth > health && !config.uhcMode) || hasRegeneration || isStarving || hasPoison || hasWither || isGettingFreezeDamage || isBurningOnFire || isDrowning || isSuffocating))
                value += "(";

            value += Calculations.MakeFraction(health, false);

            if(config.healthEstimates){
                if (naturalRegenerationHealth > health && !config.uhcMode)
                    value += "→" + Calculations.MakeFraction(naturalRegenerationHealth, config.textSettings.estimatesItalic);
                if (hasRegeneration)
                    value += "→" + Calculations.MakeFraction(regenerationHealth, config.textSettings.estimatesItalic);
                if (isStarving)
                    value += "→" + Calculations.MakeFraction(starvationHealthEstimate, config.textSettings.estimatesItalic);
                if (hasPoison)
                    value += "→" + Calculations.MakeFraction(poisonHealth, config.textSettings.estimatesItalic);
                if (hasWither)
                    value += "→" + Calculations.MakeFraction(witherHealth, config.textSettings.estimatesItalic);
                if (isGettingFreezeDamage)
                    value += "→" + Calculations.MakeFraction(0, config.textSettings.estimatesItalic);
                if (isBurningOnFire)
                    value += "→" + Calculations.MakeFraction(0, config.textSettings.estimatesItalic);
                if (isDrowning)
                    value += "→" + Calculations.MakeFraction(0, config.textSettings.estimatesItalic);
                if (isSuffocating)
                    value += "→" + Calculations.MakeFraction(0, config.textSettings.estimatesItalic);
                if (config.textSettings.estimatesParentheses && (hasHunger || hasHungerEffect) &&
                        ((naturalRegenerationHealth > health && !config.uhcMode) || hasRegeneration || isStarving || hasPoison || hasWither || isGettingFreezeDamage || isBurningOnFire || isDrowning || isSuffocating))
                    value += ")";
            }
        }

        // Additive values

        if (absorption > 0)
            value += "+" + Calculations.MakeFraction(absorption, false);

        if(config.textSettings.showText) { // Separated if because order matters
            if (resistancePercent > 0 && config.goodThings.showResistance)
                value += "+" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.resistanceEmoji" : "text.onebar.resistance", resistancePercent).getString();

            // Subtractive values

            if (isUnderwater && !hasWaterBreathing)
                value += "-" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.airEmoji" : "text.onebar.air", Calculations.MakeFraction(air, false)).getString();
            if (isUnderwater && hasWaterBreathing)
                value += "-§m" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.airEmoji" : "text.onebar.air", Calculations.MakeFraction(air, false)).getString() + "§r";
            if (isFreezing)
                value += "-" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.freezeEmoji" : "text.onebar.freeze", Calculations.MakeFraction(freeze, false)).getString();
            if (isBurning && !hasFireResistance && config.badThings.showFire)
                value += "-" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.fireEmoji" : "text.onebar.fire", burningMultiplier).getString();
            if (isBurning && hasFireResistance && config.badThings.showFire)
                value += "-§m" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.fireEmoji" : "text.onebar.fire", burningMultiplier).getString() + "§r";
            if (hasHunger || (hasHungerEffect && config.healthEstimates))
                value += "-";
            if (hasHungerEffect && config.healthEstimates && config.textSettings.estimatesParentheses)
                value += "(";
            if (hasHunger || (hasHungerEffect && config.healthEstimates))
                value += Calculations.MakeFraction(hunger, false);
            if (hasHunger && saturation < 1 && config.badThings.showHungerDecreasing)
                value += "↓";
            if (hasHungerEffect && config.healthEstimates)
                value += "→" + Calculations.MakeFraction(hungerEffectEstimate, config.textSettings.estimatesItalic);
            if (hasHungerEffect && config.healthEstimates && config.textSettings.estimatesParentheses)
                value += ")";
        }

        if (isHardcore)
            value += new TranslatableText(config.textSettings.useEmoji ? "text.onebar.hardcoreEmoji" : "text.onebar.hardcore").getString();

        int textX = baseEndW - client.textRenderer.getWidth(value);
        int textY = baseStartH + 1;

        client.textRenderer.draw(stack, value, textX, textY, config.textSettings.textColor);
    }

    private void xpBar(){
        int relativeEndW = Calculations.RelativeW(xpStartW, xpEndW, xp, maxXp);

        int textX = xpStartW + 3;
        int textY = xpStartH - 10;

        client.textRenderer.drawWithShadow(stack, String.valueOf(xpLevel), textX, textY, config.otherBars.xpColor);
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

            String value = String.valueOf(health);
            int textX = baseEndW - client.textRenderer.getWidth(value);
            int textY = mountStartH + 1;

            DrawableHelper.fill(stack, baseStartW, mountStartH, baseEndW, mountEndH, config.backgroundColor);
            DrawableHelper.fill(stack, baseStartW, mountStartH, baseRelativeEndW(Calculations.GetPreciseInt(mountRawHealth), Calculations.GetPreciseInt(mountMaxHealth)), mountEndH, config.entity.healthColor);
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
