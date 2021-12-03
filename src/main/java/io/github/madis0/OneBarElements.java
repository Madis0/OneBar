package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.Difficulty;
import java.util.Objects;

public class OneBarElements {
    private final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    private final ClientProperties clientProperties = new ClientProperties();
    private final PlayerProperties playerProperties = new PlayerProperties();
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final Difficulty difficulty = Objects.requireNonNull(client.getCameraEntity()).world.getDifficulty();
    private final MatrixStack stack;

    boolean hasHunger = playerProperties.hasHunger && !config.disableHunger;

    public OneBarElements(MatrixStack matrixStack){
        stack = matrixStack;
    }

    public void renderOneBar(){
        PlayerEntity playerEntity = MinecraftClient.getInstance().player;
        if (playerEntity != null) {
            barBackground();
            if(config.healthEstimates && !config.uhcMode) naturalRegenerationBar();
            //if(config.healthEstimates && !config.uhcMode) heldFoodHealthBar();
            if(config.healthEstimates) regenerationBar();
            healthBar();
            if(config.healthEstimates) poisonBar();
            if(config.healthEstimates) witherBar();
            if(config.healthEstimates) hungerEffectBar();
            hungerBar();
            if(config.goodThings.heldFoodHungerBar) heldFoodHungerBar();
            if(config.badThings.showFire) fireBar();
            freezeBar();
            airBar();
            xpBar();
            barText();
            if(config.otherBars.showArmorBar) armorBar();
            if(config.otherBars.showArmorDurabilityBar) armorDurabilityBar();
            if(config.otherBars.showElytraDurabilityBar) elytraDurabilityBar();
            if(config.otherBars.showSaturationBar) saturationBar();
            //if(config.healthEstimates && config.otherBars.showSaturationBar) heldFoodSaturationBar();
        }
    }

    private void barBackground(){
        DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.backgroundColor);
    }

    private void armorBar(){
        DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(playerProperties.armor, playerProperties.maxArmor), clientProperties.baseStartH, config.otherBars.armorColor);
    }

    private void armorDurabilityBar(){
        if(playerProperties.maxArmorDurability > 0)
            DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(playerProperties.armorDurability, playerProperties.maxArmor), clientProperties.baseStartH, config.otherBars.armorDurabilityColor);
    }

    private void elytraDurabilityBar(){
        if(playerProperties.isFlyingWithElytra)
            DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(playerProperties.elytraDurability, playerProperties.elytraMaxDurability), clientProperties.baseStartH, config.otherBars.elytraDurabilityColor);
    }

    private void saturationBar(){
        DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseEndH, clientProperties.baseRelativeEndW(playerProperties.saturationRaw, playerProperties.maxFoodLevelRaw), clientProperties.baseEndH + 1, config.otherBars.saturationColor);
    }

    private void heldFoodHungerBar(){
        if(hasHunger){
            if(playerProperties.heldFoodHungerEstimate >= 0) // Used food
                DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.hunger, playerProperties.maxFoodLevel), clientProperties.baseStartH, clientProperties.baseRelativeEndW(playerProperties.maxFoodLevel - playerProperties.heldFoodHungerEstimate, playerProperties.maxFoodLevel), clientProperties.baseEndH, config.goodThings.heldFoodHungerGoodColor);
            else // Wasted food
                DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.heldFoodHunger, playerProperties.maxFoodLevel), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.goodThings.heldFoodHungerWasteColor);
        }
    }

    private void heldFoodSaturationBar(){ // Still WIP
        DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseEndH, clientProperties.baseRelativeEndW(playerProperties.heldFoodSaturationEstimateRaw, playerProperties.maxFoodLevelRaw), clientProperties.baseEndH + 1, config.goodThings.heldFoodHungerGoodColor);
    }

    private void heldFoodHealthBar(){ // Still WIP
        if(playerProperties.heldFoodHealthEstimateRaw > playerProperties.healthRaw) {
            DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseRelativeEndW(Math.max(playerProperties.heldFoodHealthEstimateRaw, playerProperties.healthRaw), playerProperties.maxHealthRaw), clientProperties.baseEndH, config.goodThings.naturalRegenerationColor);
        }
    }

    private void naturalRegenerationBar(){
        if (playerProperties.naturalRegenerationHealthRaw > playerProperties.healthRaw){
            DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseRelativeEndW(Math.max(playerProperties.naturalRegenerationHealth, playerProperties.health), playerProperties.maxHealthRaw), clientProperties.baseEndH, config.goodThings.naturalRegenerationColor);
        }
    }

    private void regenerationBar(){
        if(playerProperties.hasRegeneration)
            DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseRelativeEndW(Math.max(playerProperties.regenerationHealthRaw, playerProperties.healthRaw), playerProperties.maxHealthRaw), clientProperties.baseEndH, config.goodThings.regenerationColor);
    }

    private void healthBar(){
        DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseRelativeEndW(playerProperties.healthRaw, playerProperties.maxHealthRaw), clientProperties.baseEndH, config.goodThings.healthColor);
    }

    private void poisonBar(){
        DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.maxHealthRaw - playerProperties.poisonHealthRaw, playerProperties.maxHealthRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.poisonColor);
    }

    private void witherBar(){
        DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.maxHealthRaw - playerProperties.witherHealthRaw, playerProperties.maxHealthRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.witherColor);
    }

    private void hungerEffectBar(){
        if (playerProperties.hungerEffectEstimateRaw > playerProperties.hunger && !difficulty.equals(Difficulty.PEACEFUL) && !config.disableHunger){
            DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.hungerEffectEstimateRaw, playerProperties.maxFoodLevelRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.hungerEffectColor);
        }
    }

    private void hungerBar(){
        if(hasHunger)
            DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.hungerRaw, playerProperties.maxFoodLevelRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.hungerColor);
    }

    private void airBar(){
        DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.airRaw, playerProperties.maxAirRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.airColor);
    }

    private void freezeBar(){
        DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.freezeRaw, playerProperties.maxFreezeRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.freezeColor);
    }

    private void fireBar(){
        if (playerProperties.isBurning && !playerProperties.hasFireResistance){
            DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.fireColor);
        }
    }

    private void barText(){
        String value = "";
        boolean showHealthParentheses = config.textSettings.estimatesParentheses &&
                (hasHunger || playerProperties.hasHungerEffect && !config.disableHunger || playerProperties.isUnderwater || playerProperties.isFreezing || playerProperties.isBurning || playerProperties.hasAbsorption || (playerProperties.hasResistance && config.goodThings.showResistance)) &&
                ((playerProperties.naturalRegenerationHealth > playerProperties.health && !config.uhcMode) || playerProperties.hasRegeneration || playerProperties.isStarving && !config.disableHunger || playerProperties.hasPoison || playerProperties.hasWither || playerProperties.isGettingFreezeDamage || playerProperties.isBurningOnFire || playerProperties.isDrowning || playerProperties.isSuffocating);

        boolean showHungerParentheses = config.textSettings.estimatesParentheses && config.healthEstimates && (playerProperties.hasHungerEffect && !config.disableHunger || (hasHunger && playerProperties.isHoldingFood && config.goodThings.heldFoodHungerBar));

        String arrowRight = "→";
        String arrowDown = "↓";
        String plus = "+";
        String minus = "-";
        String para = "§";
        String pStart = "(";
        String pEnd = ")";

        if(config.textSettings.showText) {

            // Health values

            if (config.healthEstimates && showHealthParentheses)
                value += pStart;

            value += Calculations.MakeFraction(playerProperties.health, false);

            if(config.healthEstimates){
                if (playerProperties.naturalRegenerationHealth > playerProperties.health && !config.uhcMode)
                    value += arrowRight + Calculations.MakeFraction(playerProperties.naturalRegenerationHealth, config.textSettings.estimatesItalic);
                //if (hasHunger && playerProperties.isHoldingFood && playerProperties.heldFoodHealthEstimate > playerProperties.health)
                //    value += arrowRight + Calculations.MakeFraction(playerProperties.heldFoodHealthEstimate, config.textSettings.estimatesItalic);
                if (playerProperties.hasRegeneration)
                    value += arrowRight + Calculations.MakeFraction(playerProperties.regenerationHealth, config.textSettings.estimatesItalic);
                if (playerProperties.isStarving && hasHunger)
                    value += arrowRight + Calculations.MakeFraction(playerProperties.starvationHealthEstimate, config.textSettings.estimatesItalic);
                if (playerProperties.hasPoison)
                    value += arrowRight + Calculations.MakeFraction(playerProperties.poisonHealth, config.textSettings.estimatesItalic);
                if (playerProperties.hasWither)
                    value += arrowRight + Calculations.MakeFraction(playerProperties.witherHealth, config.textSettings.estimatesItalic);
                if (playerProperties.isGettingFreezeDamage)
                    value += arrowRight + Calculations.MakeFraction(0, config.textSettings.estimatesItalic);
                if (playerProperties.isBurningOnFire)
                    value += arrowRight + Calculations.MakeFraction(0, config.textSettings.estimatesItalic);
                if (playerProperties.isDrowning)
                    value += arrowRight + Calculations.MakeFraction(0, config.textSettings.estimatesItalic);
                if (playerProperties.isSuffocating)
                    value += arrowRight + Calculations.MakeFraction(0, config.textSettings.estimatesItalic);
                if (showHealthParentheses)
                    value += pEnd;
            }
        }

        // Additive values

        if (playerProperties.hasAbsorption)
            value += plus + Calculations.MakeFraction(playerProperties.absorption, false);

        if(config.textSettings.showText) { // Separated if because order matters
            if (playerProperties.hasResistance && config.goodThings.showResistance)
                value += plus + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.resistanceEmoji" : "text.onebar.resistance", playerProperties.resistancePercent).getString();

            // Subtractive values

            if (playerProperties.isUnderwater && !playerProperties.hasWaterBreathing)
                value += minus + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.airEmoji" : "text.onebar.air", Calculations.MakeFraction(playerProperties.air, false)).getString();
            if (playerProperties.isUnderwater && playerProperties.hasWaterBreathing)
                value += minus + para + "m" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.airEmoji" : "text.onebar.air", Calculations.MakeFraction(playerProperties.air, false)).getString() + para + "r";
            if (playerProperties.isFreezing)
                value += minus + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.freezeEmoji" : "text.onebar.freeze", Calculations.MakeFraction(playerProperties.freeze, false)).getString();
            if (playerProperties.isBurning && !playerProperties.hasFireResistance && config.badThings.showFire)
                value += minus + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.fireEmoji" : "text.onebar.fire", playerProperties.burningMultiplier).getString();
            if (playerProperties.isBurning && playerProperties.hasFireResistance && config.badThings.showFire)
                value += minus + para + "m" + new TranslatableText(config.textSettings.useEmoji ? "text.onebar.fireEmoji" : "text.onebar.fire", playerProperties.burningMultiplier).getString() + para + "r";
            if (hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates && !config.disableHunger))
                value += minus;

            if (showHungerParentheses)
                value += pStart;
            if (hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates && !config.disableHunger))
                value += Calculations.MakeFraction(playerProperties.hunger, false);
            if (hasHunger && playerProperties.saturation < 1 && config.badThings.showHungerDecreasing)
                value += arrowDown;
            if (playerProperties.hasHungerEffect && !config.disableHunger && config.healthEstimates)
                value += arrowRight + Calculations.MakeFraction(playerProperties.hungerEffectEstimate, config.textSettings.estimatesItalic);
            if (hasHunger && playerProperties.isHoldingFood && config.goodThings.heldFoodHungerBar)
                value += arrowRight + Calculations.MakeFraction(playerProperties.heldFoodHungerEstimate, config.textSettings.estimatesItalic);
            if (showHungerParentheses)
                value += pEnd;
        }

        if (clientProperties.isHardcore)
            value += new TranslatableText(config.textSettings.useEmoji ? "text.onebar.hardcoreEmoji" : "text.onebar.hardcore").getString();

        int textX = clientProperties.baseEndW - client.textRenderer.getWidth(value);
        int textY = clientProperties.baseStartH + 1;

        client.textRenderer.draw(stack, value, textX, textY, config.textSettings.textColor);
    }

    private void xpBar(){
        int relativeEndW = Calculations.RelativeW(clientProperties.xpStartW, clientProperties.xpEndW, playerProperties.xp, playerProperties.maxXp);

        int textX = clientProperties.xpStartW + 3;
        int textY = clientProperties.xpStartH - 10;

        client.textRenderer.drawWithShadow(stack, String.valueOf(playerProperties.xpLevel), textX, textY, config.otherBars.xpColor);
        DrawableHelper.fill(stack, clientProperties.xpStartW, clientProperties.xpStartH, clientProperties.xpEndW, clientProperties.xpEndH, config.backgroundColor);
        DrawableHelper.fill(stack, clientProperties.xpStartW, clientProperties.xpStartH, relativeEndW, clientProperties.xpEndH, config.otherBars.xpColor);
    }

    public void jumpBar(){
        int barHeight = Calculations.GetPreciseInt(1.0F);
        int jumpHeight = Calculations.GetPreciseInt(Objects.requireNonNull(client.player).getMountJumpStrength());

        int relativeStartH = Calculations.RelativeW(clientProperties.jumpEndH, clientProperties.jumpStartH, jumpHeight, barHeight);
        DrawableHelper.fill(stack, clientProperties.jumpStartW, clientProperties.jumpStartH, clientProperties.jumpEndW, clientProperties.jumpEndH, config.backgroundColor);
        DrawableHelper.fill(stack, clientProperties.jumpStartW, clientProperties.jumpEndH, clientProperties.jumpEndW, relativeStartH, config.entity.jumpColor);
    }

    public void mountBar(LivingEntity mountEntity){
        if (mountEntity != null) {
            float mountRawHealth = mountEntity.getHealth();
            float mountMaxHealth = mountEntity.getMaxHealth();
            int health = (int) Math.ceil(mountRawHealth);
            int horseArmor = mountEntity.getArmor();
            int horseMaxArmor = 11; // Diamond horse armor

            String value = String.valueOf(health);
            int textX = clientProperties.baseEndW - client.textRenderer.getWidth(value);
            int textY = clientProperties.mountStartH + 1;

            DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.mountStartH, clientProperties.baseEndW, clientProperties.mountEndH, config.backgroundColor);
            DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.mountStartH, clientProperties.baseRelativeEndW(Calculations.GetPreciseInt(mountRawHealth), Calculations.GetPreciseInt(mountMaxHealth)), clientProperties.mountEndH, config.entity.healthColor);
            if(config.otherBars.showArmorBar) DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.mountStartH - 1, clientProperties.baseRelativeEndW(horseArmor, horseMaxArmor), clientProperties.mountStartH, config.otherBars.armorColor);
            if(config.textSettings.showText) client.textRenderer.draw(stack, value, textX, textY, config.textSettings.textColor);
        }
    }

    private void debugText(String value){
        client.textRenderer.drawWithShadow(stack, value, clientProperties.baseEndW + 15, clientProperties.baseStartH + 1, config.textSettings.textColor);
    }
}
