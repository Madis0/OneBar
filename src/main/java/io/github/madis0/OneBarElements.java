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

    public OneBarElements(MatrixStack matrixStack){
        stack = matrixStack;
    }

    public void renderOneBar(){
        PlayerEntity playerEntity = MinecraftClient.getInstance().player;
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
            if(config.otherBars.heldFoodHungerBar) heldFoodBar();
            barText();
            if(config.otherBars.showArmorBar) armorBar();
            if(config.otherBars.showSaturationBar) saturationBar();
        }
    }

    private void barBackground(){
        DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.backgroundColor);
    }

    private void armorBar(){
        DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(playerProperties.armor, playerProperties.maxArmor), clientProperties.baseStartH, config.otherBars.armorColor);
    }

    private void saturationBar(){
        DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseEndH, clientProperties.baseRelativeEndW(playerProperties.saturation, playerProperties.maxHunger), clientProperties.baseEndH + 1, config.otherBars.saturationColor);
    }

    private void heldFoodBar(){
        //TODO: rethink for hunger, health and saturation display
        if(playerProperties.hasHunger){
            if(playerProperties.hunger >= playerProperties.heldFoodHunger)
                DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.heldFoodHunger, playerProperties.maxHunger), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.otherBars.heldFoodHungerGoodColor);
            else
                DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.heldFoodHunger, playerProperties.maxHunger), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.otherBars.heldFoodHungerWasteColor);

        }
    }

    private void naturalRegenerationBar(){
        if (playerProperties.naturalRegenerationHealth > playerProperties.health){ // The if and float avoid regen being visible behind health if regen will not happen, because health is shown in precise floats
            float lessPreciseRegen = (float)playerProperties.naturalRegenerationHealth - (float)0.2;

            DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseRelativeEndW(Math.max(Calculations.GetPreciseInt(lessPreciseRegen), Calculations.GetPreciseInt(playerProperties.rawHealth)), Calculations.GetPreciseInt(playerProperties.maxRawHealth)), clientProperties.baseEndH, config.goodThings.naturalRegenerationColor);
        }
    }

    private void regenerationBar(){
        DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseRelativeEndW(Math.max(playerProperties.regenerationHealth, 0), clientProperties.xpEndW), clientProperties.baseEndH, config.goodThings.regenerationColor);
    }

    private void healthBar(){
        DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseRelativeEndW(Calculations.GetPreciseInt(playerProperties.rawHealth), Calculations.GetPreciseInt(playerProperties.maxRawHealth)), clientProperties.baseEndH, config.goodThings.healthColor);
    }

    private void poisonBar(){
        DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.maxHealth - playerProperties.poisonHealth, playerProperties.maxHealth), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.poisonColor);
    }

    private void witherBar(){
        DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.maxHealth - playerProperties.witherHealth, playerProperties.maxHealth), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.witherColor);
    }

    private void hungerEffectBar(){
        if (playerProperties.hungerEffectEstimate > playerProperties.hunger && !difficulty.equals(Difficulty.PEACEFUL)){
            DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.hungerEffectEstimate, playerProperties.maxHunger), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.hungerEffectColor);
        }
    }

    private void hungerBar(){
        DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.hunger, playerProperties.maxHunger), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.hungerColor);
    }

    private void airBar(){
        DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.rawAir, playerProperties.maxRawAir), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.airColor);
    }

    private void freezeBar(){
        DrawableHelper.fill(stack, clientProperties.baseRelativeStartW(playerProperties.rawFreeze, playerProperties.maxRawFreeze), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.freezeColor);
    }

    private void fireBar(){
        if (playerProperties.isBurning && !playerProperties.hasFireResistance){
            DrawableHelper.fill(stack, clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.fireColor);
        }
    }

    private void barText(){
        String value = "";
        boolean showHealthParentheses = config.textSettings.estimatesParentheses &&
                (playerProperties.hasHunger || playerProperties.hasHungerEffect || playerProperties.isUnderwater || playerProperties.isFreezing || playerProperties.isBurning || playerProperties.hasAbsorption || (playerProperties.hasResistance && config.goodThings.showResistance)) &&
                ((playerProperties.naturalRegenerationHealth > playerProperties.health && !config.uhcMode) || playerProperties.hasRegeneration || playerProperties.isStarving || playerProperties.hasPoison || playerProperties.hasWither || playerProperties.isGettingFreezeDamage || playerProperties.isBurningOnFire || playerProperties.isDrowning || playerProperties.isSuffocating);

        boolean showHungerParentheses = config.textSettings.estimatesParentheses && config.healthEstimates && (playerProperties.hasHungerEffect || (playerProperties.hasHunger && playerProperties.isHoldingFood && config.otherBars.heldFoodHungerBar));

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
                if (playerProperties.hasRegeneration)
                    value += arrowRight + Calculations.MakeFraction(playerProperties.regenerationHealth, config.textSettings.estimatesItalic);
                if (playerProperties.isStarving)
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
            if (playerProperties.hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates))
                value += minus;

            if (showHungerParentheses)
                value += pStart;
            if (playerProperties.hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates))
                value += Calculations.MakeFraction(playerProperties.hunger, false);
            if (playerProperties.hasHunger && playerProperties.saturation < 1 && config.badThings.showHungerDecreasing)
                value += arrowDown;
            if (playerProperties.hasHungerEffect && config.healthEstimates)
                value += arrowRight + Calculations.MakeFraction(playerProperties.hungerEffectEstimate, config.textSettings.estimatesItalic);
            if (playerProperties.hasHunger && playerProperties.isHoldingFood && config.otherBars.heldFoodHungerBar)
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
