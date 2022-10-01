package io.github.madis0;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.tr7zw.exordium.ExordiumModBase;
import io.github.madis0.mixin.DrawableHelperAccessor;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.world.Difficulty;

import java.util.Locale;
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
        if(OneBar.doesClassExist("dev.tr7zw.exordium.ExordiumModBase")) {
            ExordiumModBase.correctBlendMode();
            ExordiumModBase.setForceBlend(true);
        }

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
            if(config.badThings.showWarden) wardenBar();
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

            if(OneBar.doesClassExist("dev.tr7zw.exordium.ExordiumModBase")) {
                ExordiumModBase.setForceBlend(false);
                RenderSystem.defaultBlendFunc();
            }
        }
    }

    private void renderBar(int x1, int y1, int x2, int y2, int color){
        if(!config.enableGradient)
            DrawableHelper.fill(stack, x1, y1, x2, y2, color);
        else
            DrawableHelperAccessor.callFillGradient(stack, x1, y1, x2, y2, color, Calculations.ManipulateColor(color, config.gradientShift), 0);
    }

    private void barBackground(){
        renderBar(clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.backgroundColor);
    }

    private void armorBar(){
        renderBar(clientProperties.baseStartW, clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(playerProperties.armor, playerProperties.maxArmor), clientProperties.baseStartH, config.otherBars.armorColor);
    }

    private void armorDurabilityBar(){
        if(playerProperties.maxArmorDurability > 0)
            renderBar(clientProperties.baseStartW, clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(playerProperties.armorDurability, playerProperties.maxArmor), clientProperties.baseStartH, config.otherBars.armorDurabilityColor);
    }

    private void elytraDurabilityBar(){
        if(playerProperties.isFlyingWithElytra)
            renderBar(clientProperties.baseStartW, clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(playerProperties.elytraDurability, playerProperties.elytraMaxDurability), clientProperties.baseStartH, config.otherBars.elytraDurabilityColor);
    }

    private void saturationBar(){
        renderBar(clientProperties.baseStartW, clientProperties.baseEndH, clientProperties.baseRelativeEndW(playerProperties.saturationRaw, playerProperties.maxFoodLevelRaw), clientProperties.baseEndH + 1, config.otherBars.saturationColor);
    }

    private void heldFoodHungerBar(){
        if(hasHunger){
            if(playerProperties.heldFoodHungerEstimate >= 0) // Used food
                renderBar(clientProperties.baseRelativeStartW(playerProperties.hunger, playerProperties.maxFoodLevel), clientProperties.baseStartH, clientProperties.baseRelativeEndW(playerProperties.maxFoodLevel - playerProperties.heldFoodHungerEstimate, playerProperties.maxFoodLevel), clientProperties.baseEndH, config.goodThings.heldFoodHungerGoodColor);
            else // Wasted food
                renderBar(clientProperties.baseRelativeStartW(playerProperties.heldFoodHunger, playerProperties.maxFoodLevel), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.goodThings.heldFoodHungerWasteColor);
        }
    }

    private void heldFoodSaturationBar(){ // Still WIP
        renderBar(clientProperties.baseStartW, clientProperties.baseEndH, clientProperties.baseRelativeEndW(playerProperties.heldFoodSaturationEstimateRaw, playerProperties.maxFoodLevelRaw), clientProperties.baseEndH + 1, config.goodThings.heldFoodHungerGoodColor);
    }

    private void heldFoodHealthBar(){ // Still WIP
        if(playerProperties.heldFoodHealthEstimateRaw > playerProperties.healthRaw) {
            renderBar(clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseRelativeEndW(Math.max(playerProperties.heldFoodHealthEstimateRaw, playerProperties.healthRaw), playerProperties.maxHealthRaw), clientProperties.baseEndH, config.goodThings.naturalRegenerationColor);
        }
    }

    private void naturalRegenerationBar(){
        if (playerProperties.naturalRegenerationHealthRaw > playerProperties.healthRaw){
            renderBar(clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseRelativeEndW(Math.max(playerProperties.naturalRegenerationHealth, playerProperties.health), playerProperties.maxHealthRaw), clientProperties.baseEndH, config.goodThings.naturalRegenerationColor);
        }
    }

    private void regenerationBar(){
        if(playerProperties.hasRegeneration)
            renderBar(clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseRelativeEndW(Math.max(playerProperties.regenerationHealthRaw, playerProperties.healthRaw), playerProperties.maxHealthRaw), clientProperties.baseEndH, config.goodThings.regenerationColor);
    }

    private void healthBar(){
        renderBar(clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseRelativeEndW(playerProperties.healthRaw, playerProperties.maxHealthRaw), clientProperties.baseEndH, config.goodThings.healthColor);
    }

    private void poisonBar(){
        renderBar(clientProperties.baseRelativeStartW(playerProperties.maxHealthRaw - playerProperties.poisonHealthRaw, playerProperties.maxHealthRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.poisonColor);
    }

    private void witherBar(){
        renderBar(clientProperties.baseRelativeStartW(playerProperties.maxHealthRaw - playerProperties.witherHealthRaw, playerProperties.maxHealthRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.witherColor);
    }

    private void wardenBar(){
        renderBar(clientProperties.baseRelativeStartW(playerProperties.rawWardenDanger, playerProperties.rawMaxWardenDanger), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.wardenColor);
    }

    private void hungerEffectBar(){
        if (playerProperties.hungerEffectEstimateRaw > playerProperties.hunger && !difficulty.equals(Difficulty.PEACEFUL) && !config.disableHunger){
            renderBar(clientProperties.baseRelativeStartW(playerProperties.hungerEffectEstimateRaw, playerProperties.maxFoodLevelRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.hungerEffectColor);
        }
    }

    private void hungerBar(){
        if(hasHunger)
            renderBar(clientProperties.baseRelativeStartW(playerProperties.hungerRaw, playerProperties.maxFoodLevelRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.hungerColor);
    }

    private void airBar(){
        renderBar(clientProperties.baseRelativeStartW(playerProperties.airRaw, playerProperties.maxAirRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.airColor);
    }

    private void freezeBar(){
        renderBar(clientProperties.baseRelativeStartW(playerProperties.freezeRaw, playerProperties.maxFreezeRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.freezeColor);
    }

    private void fireBar(){
        if (playerProperties.isBurning && !playerProperties.hasFireResistance){
            renderBar(clientProperties.baseRelativeStartW((playerProperties.maxHealthRaw - playerProperties.healthRaw) + playerProperties.burningMultiplier, playerProperties.maxHealthRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.fireColor);
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

            value += Calculations.EmojiOrText("text.onebar.healthEmoji","text.onebar.health", true, Calculations.MakeFraction(playerProperties.health, false));

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
                if (playerProperties.isWardenAngry)
                    value += arrowRight + Calculations.MakeFraction(0, config.textSettings.estimatesItalic);
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
            value += plus + Calculations.EmojiOrText("text.onebar.absorptionEmoji","text.onebar.absorption", true, Calculations.MakeFraction(playerProperties.absorption, false));

        if(config.textSettings.showText) { // Separated if because order matters
            if (playerProperties.hasResistance && config.goodThings.showResistance)
                value += plus + Calculations.EmojiOrText("text.onebar.resistanceEmoji","text.onebar.resistance", false, playerProperties.resistancePercent);

            // Subtractive values

            if(playerProperties.isWardenNear && config.badThings.showWarden)
                value += minus + Calculations.EmojiOrText("text.onebar.wardenEmoji","text.onebar.warden",false, Calculations.MakeFraction(playerProperties.wardenDanger, false));
            if (playerProperties.isUnderwater && !playerProperties.hasWaterBreathing)
                value += minus + Calculations.EmojiOrText("text.onebar.airEmoji", "text.onebar.air", false, Calculations.MakeFraction(playerProperties.air, false));
            if (playerProperties.isUnderwater && playerProperties.hasWaterBreathing)
                value += minus + para + "m" + Calculations.EmojiOrText("text.onebar.airEmoji","text.onebar.air", false, Calculations.MakeFraction(playerProperties.air, false)) + para + "r";
            if (playerProperties.isFreezing)
                value += minus + Calculations.EmojiOrText("text.onebar.freezeEmoji", "text.onebar.freeze", false, Calculations.MakeFraction(playerProperties.freeze, false));
            if (playerProperties.isBurning && !playerProperties.hasFireResistance && config.badThings.showFire)
                value += minus + Calculations.EmojiOrText("text.onebar.fireEmoji","text.onebar.fire", false, playerProperties.burningMultiplier);
            if (playerProperties.isBurning && playerProperties.hasFireResistance && config.badThings.showFire)
                value += minus + para + "m" + Calculations.EmojiOrText("text.onebar.fireEmoji","text.onebar.fire", false, playerProperties.burningMultiplier) + para + "r";
            if (playerProperties.hasBadOmen && config.badThings.showBadOmen)
                value += minus + Calculations.EmojiOrText("text.onebar.badOmenEmoji","text.onebar.badOmen", false, playerProperties.badOmenLevel);
            if (clientProperties.isHardcore)
                value += minus + Calculations.EmojiOrText("text.onebar.hardcoreEmoji","text.onebar.hardcore", false, (Object) null);
            if (hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates && !config.disableHunger))
                value += minus;

            if (showHungerParentheses)
                value += pStart;
            if (hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates && !config.disableHunger))
                value += Calculations.EmojiOrText("text.onebar.hungerEmoji","text.onebar.hunger", true, Calculations.MakeFraction(playerProperties.hunger, false));
            if (hasHunger && playerProperties.saturation < 1 && config.badThings.showHungerDecreasing)
                value += arrowDown;
            if (playerProperties.hasHungerEffect && !config.disableHunger && config.healthEstimates)
                value += arrowRight + Calculations.MakeFraction(playerProperties.hungerEffectEstimate, config.textSettings.estimatesItalic);
            if (hasHunger && playerProperties.isHoldingFood && config.goodThings.heldFoodHungerBar)
                value += arrowRight + Calculations.MakeFraction(playerProperties.heldFoodHungerEstimate, config.textSettings.estimatesItalic);
            if (showHungerParentheses)
                value += pEnd;
        }

        int textX = clientProperties.baseEndW - client.textRenderer.getWidth(value);
        int textY = clientProperties.baseStartH + 1;

        client.textRenderer.draw(stack, value, textX, textY, config.textSettings.textColor);
    }

    private void xpBar(){
        int relativeEndW = Calculations.RelativeW(clientProperties.xpStartW, clientProperties.xpEndW, playerProperties.xp, playerProperties.maxXp);

        int textX = clientProperties.xpStartW + 9;
        int textY = clientProperties.xpStartH - 10;

        var lapisText = "";
        if(config.otherBars.lapisCounter)
        {
            if(config.otherBars.lapisTimesEnchantable) {
                lapisText = Calculations.GetSubscriptNumber(playerProperties.lapisLazuliMax) + "ₓ";
                if((config.otherBars.adaptiveXpBar && playerProperties.lapisLazuliMax < 1))
                    lapisText = "";
            }
            else {
                lapisText = Calculations.GetSubscriptNumber(playerProperties.lapisLazuli) + "";
                if(config.otherBars.adaptiveXpBar && playerProperties.lapisLazuli < 1)
                    lapisText = "";
            }
        }
        
        if(!config.otherBars.adaptiveXpBar || playerProperties.xpLevel > 0){
            int sizeLimit = !client.options.getForceUnicodeFont().getValue() ? 10000 : 1000000;
            int edgeAlignedConst = 13;

            if(playerProperties.xpLevel >= 0 && playerProperties.xpLevel < sizeLimit){
                DrawableHelper.drawCenteredText(stack, client.textRenderer, String.valueOf(playerProperties.xpLevel), textX, textY, config.otherBars.xpColor);
            }
            else if(playerProperties.xpLevel >= sizeLimit){
                if(client.options.getMainArm().getValue() == Arm.RIGHT)
                    client.textRenderer.drawWithShadow(stack, String.valueOf(playerProperties.xpLevel), textX - edgeAlignedConst, textY, config.otherBars.xpColor);
                else if(client.options.getMainArm().getValue()  == Arm.LEFT)
                    client.textRenderer.drawWithShadow(stack, String.valueOf(playerProperties.xpLevel), textX + edgeAlignedConst - client.textRenderer.getWidth(String.valueOf(playerProperties.xpLevel)), textY, config.otherBars.xpColor);
            }

            if(config.otherBars.lapisCounter){
                var lapisTextX = clientProperties.xpEndW + 1;
                if(client.options.getMainArm().getValue()  == Arm.LEFT)
                    lapisTextX = clientProperties.xpStartW - 1 - client.textRenderer.getWidth(lapisText);

                var lapisTextY = clientProperties.xpStartH - 5;
                client.textRenderer.drawWithShadow(stack, lapisText, lapisTextX, lapisTextY, config.otherBars.lapisColor);
            }
        }
        if(!config.otherBars.adaptiveXpBar || playerProperties.xp > 0){
            renderBar(clientProperties.xpStartW, clientProperties.xpStartH, clientProperties.xpEndW, clientProperties.xpEndH, config.backgroundColor);
            renderBar(clientProperties.xpStartW, clientProperties.xpStartH, relativeEndW, clientProperties.xpEndH, config.otherBars.xpColor);
        }
    }

    public void jumpBar(LivingEntity mountEntity){
        int barHeight = Calculations.GetPreciseInt(1.0F);
        int jumpHeight = Calculations.GetPreciseInt(Objects.requireNonNull(client.player).getMountJumpStrength());

        double heightInBlocks = Math.max(0, ((AbstractHorseEntity)mountEntity).getJumpStrength() *
                                                Calculations.HorseJumpStrengthToJumpHeight(Objects.requireNonNull(client.player).getMountJumpStrength()));

        String roundedHeightInBlocks = Calculations.GetSubscriptNumber(Double.parseDouble(String.format(Locale.US, "%,.1f",(heightInBlocks))));

        int relativeStartH = Calculations.RelativeW(clientProperties.jumpEndH, clientProperties.jumpStartH, jumpHeight, barHeight);
        renderBar(clientProperties.jumpStartW, clientProperties.jumpStartH, clientProperties.jumpEndW, clientProperties.jumpEndH, config.backgroundColor);
        //renderBar(clientProperties.jumpStartW, clientProperties.jumpEndH, clientProperties.jumpEndW, relativeStartH, config.entity.jumpColor);
        DrawableHelper.fill(stack, clientProperties.jumpStartW, clientProperties.jumpEndH, clientProperties.jumpEndW, relativeStartH, config.entity.jumpColor); //TODO: fix gradient

        int textX = clientProperties.jumpEndW - client.textRenderer.getWidth(roundedHeightInBlocks);
        int textY = clientProperties.jumpEndH - 10;

        if(config.textSettings.showText && config.entity.showHorseJumpText)
            client.textRenderer.draw(stack, roundedHeightInBlocks, textX, textY, config.textSettings.textColor);
    }

    public void mountBar(LivingEntity mountEntity){
        if (mountEntity == null) {return;}
        float mountRawHealth = mountEntity.getHealth();
        float mountMaxHealth = mountEntity.getMaxHealth();
        int health = (int) Math.ceil(mountRawHealth);
        int horseArmor = mountEntity.getArmor();
        int horseMaxArmor = 11; // Diamond horse armor

        String value = Calculations.MakeFraction(health, false);
        int textX = clientProperties.baseEndW - client.textRenderer.getWidth(value);
        int textY = clientProperties.mountStartH + 1;

        renderBar(clientProperties.baseStartW, clientProperties.mountStartH, clientProperties.baseEndW, clientProperties.mountEndH, config.backgroundColor);
        renderBar(clientProperties.baseStartW, clientProperties.mountStartH, clientProperties.baseRelativeEndW(Calculations.GetPreciseInt(mountRawHealth), Calculations.GetPreciseInt(mountMaxHealth)), clientProperties.mountEndH, config.entity.healthColor);
        if(config.otherBars.showArmorBar) renderBar(clientProperties.baseStartW, clientProperties.mountStartH - 1, clientProperties.baseRelativeEndW(horseArmor, horseMaxArmor), clientProperties.mountStartH, config.otherBars.armorColor);
        if(config.textSettings.showText) client.textRenderer.draw(stack, value, textX, textY, config.textSettings.textColor);
    }

    private void debugText(String value){
        client.textRenderer.drawWithShadow(stack, value, clientProperties.baseEndW + 15, clientProperties.baseStartH + 1, config.textSettings.textColor);
    }
}
