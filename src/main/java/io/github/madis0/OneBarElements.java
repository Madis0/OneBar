package io.github.madis0;

//import dev.tr7zw.exordium.ExordiumModBase;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import java.util.Locale;
import java.util.Objects;

public class OneBarElements {
    private final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    private final ClientProperties clientProperties = new ClientProperties();
    private final PlayerProperties playerProperties = new PlayerProperties();
    private final Minecraft client = Minecraft.getInstance();
    private final Difficulty difficulty = Objects.requireNonNull(client.getCameraEntity()).level().getDifficulty();
    private final GuiGraphics drawContext;
    private final Font textRenderer = client.font;
    private static final boolean hasExordium = FabricLoader.getInstance().isModLoaded("exordium");

    boolean hasHunger = playerProperties.hasHunger && !config.disableHunger;
    float armorBarGap = 0.1F;
    float armorBarChestplateLength = armorBarGap + playerProperties.helmetMaxArmor;
    float armorBarLeggingsLength = armorBarChestplateLength + armorBarGap + playerProperties.chestplateMaxArmor;
    float armorBarBootsLength =  armorBarLeggingsLength + armorBarGap + playerProperties.leggingsMaxArmor;
    float armorBarTotalLength = armorBarBootsLength + playerProperties.bootsMaxArmor;
    float elytraDurability = playerProperties.getArmorElementDurability(Objects.requireNonNull(client.player), EquipmentSlot.CHEST, 8); //8 aka same full width as Diamond/Netherite

    public OneBarElements(GuiGraphics context){
        drawContext = context;
    }

    public void renderOneBar(){
       // if(hasExordium) {
       //     ExordiumModBase.correctBlendMode();
       //     ExordiumModBase.setForceBlend(true);
       // }

        Player playerEntity = Minecraft.getInstance().player;
        if (playerEntity != null) {
            barBackground();
            if(config.healthEstimates && !config.uhcMode) naturalRegenerationBar();
            //if(config.healthEstimates && !config.uhcMode) heldFoodHealthBar();
            if(config.healthEstimates) regenerationBar();
            healthBar();
            if(config.healthEstimates){
                poisonBar();
                witherBar();
                hungerEffectBar();
            }
            hungerBar();
            if(config.goodThings.heldFoodHungerBar) heldFoodHungerBar();
            if(config.badThings.showWarden) wardenBar();
            if(config.badThings.showFire) fireBar();
            if(config.badThings.showLevitation)levitationBar();
            freezeBar();
            airBar();
            xpBar();
            barText();
            if(config.armor.showArmorBar) armorBar();
            if(config.armor.showArmorDurabilityBar) armorDurabilityBar();
            if(config.armor.showElytraDurabilityBar) elytraDurabilityBar();
            if(config.armor.showShieldDurabilityBar) shieldDurabilityBar();
            if(config.armor.showShieldCooldownBar) shieldCooldownBar();
            if(config.goodThings.showSaturationBar) saturationBar();
            //if(config.healthEstimates && config.otherBars.showSaturationBar) heldFoodSaturationBar();

            //  if(hasExordium) {
            //      ExordiumModBase.setForceBlend(false);
            //      RenderSystem.defaultBlendFunc();
            //  }
        }
    }

    private void renderBar(int x1, int y1, int x2, int y2, int color){
        if(!config.enableGradient)
            drawContext.fill(x1, y1, x2, y2, color);
        else
            drawContext.fillGradient(x1, y1, x2, y2, color, Calculations.manipulateColor(color, config.gradientShift));
    }

    private void renderLeftToRightBar(float currentValue, float maxValue, int color) {
        renderBar(clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseRelativeEndW(currentValue, maxValue), clientProperties.baseEndH, color);
    }

    private void renderLeftToRightBar(float currentValue, float maxValue, int color, int startH, int endH) {
        renderBar(clientProperties.baseStartW, startH, clientProperties.baseRelativeEndW(currentValue, maxValue), endH, color);
    }

    private void renderRightToLeftBar(float currentValue, float maxValue, int color) {
        renderBar(clientProperties.baseRelativeStartW(currentValue, maxValue), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, color);
    }

    private void renderRightToLeftBar(float currentValue, float maxValue, int color, int startH, int endH) {
        renderBar(clientProperties.baseRelativeStartW(currentValue, maxValue), startH, clientProperties.baseEndW, endH, color);
    }

    private void renderLeftToRightBarWithOffset(float offset, float currentValue, float totalLength, int color, int startH, int endH) {
        renderBar(clientProperties.baseRelativeEndW(offset, totalLength), startH, clientProperties.baseRelativeEndW(offset + currentValue, totalLength), endH, color);
    }

    private void renderRightToLeftBarMiddle(float leftValue, float rightValue, float maxValue, int color) {
        renderBar(clientProperties.baseRelativeStartW(leftValue, maxValue), clientProperties.baseStartH, clientProperties.baseRelativeEndW(rightValue, maxValue), clientProperties.baseEndH, color);
    }

    private void renderLeftToRightBarMiddle(float leftValue, float rightValue, float maxValue, int color, int startH, int endH) {
        float endValue = leftValue < rightValue ? rightValue : leftValue + rightValue;
        renderBar(clientProperties.baseRelativeEndW(leftValue, maxValue), startH, clientProperties.baseRelativeEndW(endValue, maxValue), endH, color);
    }

    private void barBackground(){
        renderBar(clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.backgroundColor);
    }

    private void armorBar(){
        if (!config.armor.showSegmentedArmorBar) {
            renderLeftToRightBar(playerProperties.armor, playerProperties.maxArmor, config.armor.armorColor, clientProperties.armorStartH, clientProperties.armorEndH);
        } else {
            // Helmet
            renderLeftToRightBar(playerProperties.helmetArmor, armorBarTotalLength, config.armor.armorColor, clientProperties.armorStartH, clientProperties.armorEndH);
            // Chestplate
            if (!playerProperties.hasElytra) {
                renderLeftToRightBarWithOffset(armorBarChestplateLength, playerProperties.chestplateArmor, armorBarTotalLength, config.armor.armorColor, clientProperties.armorStartH, clientProperties.armorEndH);
            } else if (config.armor.showElytraDurabilityBar && !config.armor.showArmorDurabilityBar) {
                renderLeftToRightBarWithOffset(armorBarChestplateLength, elytraDurability, armorBarTotalLength, config.armor.elytraDurabilityColor, clientProperties.armorStartH, clientProperties.armorEndH);
            }
            // Leggings
            renderLeftToRightBarWithOffset(armorBarLeggingsLength, playerProperties.leggingsArmor, armorBarTotalLength, config.armor.armorColor, clientProperties.armorStartH, clientProperties.armorEndH);
            // Boots
            renderLeftToRightBarWithOffset(armorBarBootsLength, playerProperties.bootsArmor, armorBarTotalLength, config.armor.armorColor, clientProperties.armorStartH, clientProperties.armorEndH);
        }
    }

    private void armorDurabilityBar(){
        if (client.player == null) return;

        var helmetDurability = playerProperties.getArmorElementDurability(client.player, EquipmentSlot.HEAD, playerProperties.helmetArmor);
        var chestplateDurability = playerProperties.getArmorElementDurability(client.player, EquipmentSlot.CHEST, playerProperties.chestplateArmor);
        var leggingsDurability = playerProperties.getArmorElementDurability(client.player, EquipmentSlot.LEGS, playerProperties.leggingsArmor);
        var bootsDurability = playerProperties.getArmorElementDurability(client.player, EquipmentSlot.FEET, playerProperties.bootsArmor);

        if(!config.armor.showSegmentedArmorBar){
            if(playerProperties.maxArmorDurability > 0)
                renderLeftToRightBar(playerProperties.armorDurability, playerProperties.maxArmor, config.armor.armorDurabilityColor, clientProperties.armorStartH, clientProperties.armorEndH);
        } else {
            // Helmet
            renderLeftToRightBar(helmetDurability, armorBarTotalLength, config.armor.armorDurabilityColor, clientProperties.armorStartH, clientProperties.armorEndH);
            // Chestplate
            if(!playerProperties.hasElytra)
                renderLeftToRightBarWithOffset(armorBarChestplateLength, chestplateDurability, armorBarTotalLength, config.armor.armorDurabilityColor, clientProperties.armorStartH, clientProperties.armorEndH);
            else if(config.armor.showElytraDurabilityBar)
                renderLeftToRightBarWithOffset(armorBarChestplateLength, elytraDurability, armorBarTotalLength, config.armor.elytraDurabilityColor, clientProperties.armorStartH, clientProperties.armorEndH);
            // Leggings
            renderLeftToRightBarWithOffset(armorBarLeggingsLength, leggingsDurability, armorBarTotalLength, config.armor.armorDurabilityColor, clientProperties.armorStartH, clientProperties.armorEndH);
            // Boots
            renderLeftToRightBarWithOffset(armorBarBootsLength, bootsDurability, armorBarTotalLength, config.armor.armorDurabilityColor, clientProperties.armorStartH, clientProperties.armorEndH);
        }
    }

    private void elytraDurabilityBar(){
        if(playerProperties.isFlyingWithElytra && !config.armor.showSegmentedArmorBar)
            renderLeftToRightBar(playerProperties.elytraDurability, playerProperties.elytraMaxDurability, config.armor.elytraDurabilityColor, clientProperties.armorStartH, clientProperties.armorEndH);
    }

    private void shieldDurabilityBar(){
        if(playerProperties.usesShield){
            renderLeftToRightBar(playerProperties.shieldDurability, playerProperties.shieldMaxDurability, config.armor.shieldDurabilityColor, clientProperties.armorStartH, clientProperties.armorEndH);
        }
    }

    private void shieldCooldownBar(){
        if(playerProperties.shieldRaiseTicksRemaining > 0)
            renderLeftToRightBar(playerProperties.shieldDurability, playerProperties.shieldMaxDurability, config.armor.shieldCooldownColor, clientProperties.armorStartH, clientProperties.armorEndH);
        else if(playerProperties.shieldAxedCooldown > 0)
            renderLeftToRightBar(playerProperties.shieldAxedCooldown, playerProperties.shieldAxedMaxCooldown, config.armor.shieldCooldownColor, clientProperties.armorStartH, clientProperties.armorEndH);
    }

    private void saturationBar(){
        renderLeftToRightBar(playerProperties.saturationRaw, playerProperties.maxFoodLevelRaw, config.goodThings.saturationColor, clientProperties.saturationStartH, clientProperties.saturationEndH);
    }

    private void heldFoodHungerBar(){
        if(hasHunger){
            if(playerProperties.heldFoodHungerEstimate >= 0) { // Used food
                renderRightToLeftBarMiddle(playerProperties.hunger, playerProperties.maxFoodLevel - playerProperties.heldFoodHungerEstimate, playerProperties.maxFoodLevel, config.goodThings.heldFoodHungerGoodColor);
            } else { // Wasted food
                renderRightToLeftBar(playerProperties.heldFoodHunger, playerProperties.maxFoodLevel, config.goodThings.heldFoodHungerWasteColor);
            }
        }
    }

    private void heldFoodSaturationBar(){ // Still WIP
        renderLeftToRightBar(playerProperties.heldFoodSaturationEstimateRaw, playerProperties.maxFoodLevelRaw, config.goodThings.heldFoodHungerGoodColor, clientProperties.saturationStartH, clientProperties.saturationEndH);
    }

    private void heldFoodHealthBar(){
        if(playerProperties.heldFoodHealthEstimateRaw > playerProperties.healthRaw)
            renderLeftToRightBar(Math.max(playerProperties.heldFoodHealthEstimateRaw, playerProperties.healthRaw), playerProperties.maxHealthRaw, config.goodThings.naturalRegenerationColor);
    }

    private void naturalRegenerationBar(){
        if(playerProperties.naturalRegenerationHealthRaw > playerProperties.healthRaw)
            renderLeftToRightBar(Math.max(playerProperties.naturalRegenerationHealth, playerProperties.health), playerProperties.maxHealthRaw, config.goodThings.naturalRegenerationColor);
    }

    private void regenerationBar(){
        if(playerProperties.hasRegeneration)
            renderLeftToRightBar(Math.max(playerProperties.regenerationHealthRaw, playerProperties.healthRaw), playerProperties.maxHealthRaw, config.goodThings.regenerationColor);
    }

    private void healthBar(){
        renderLeftToRightBar(playerProperties.healthRaw, playerProperties.maxHealthRaw, config.goodThings.healthColor);
    }

    private void poisonBar(){
        renderRightToLeftBar(playerProperties.maxHealthRaw - playerProperties.poisonHealthRaw, playerProperties.maxHealthRaw, config.badThings.poisonColor);
    }

    private void witherBar(){
        renderRightToLeftBar(playerProperties.maxHealthRaw - playerProperties.witherHealthRaw, playerProperties.maxHealthRaw, config.badThings.witherColor);
    }

    private void wardenBar(){
        renderRightToLeftBar(playerProperties.rawWardenDanger, playerProperties.rawMaxWardenDanger, config.badThings.wardenColor);
    }

    private void hungerEffectBar(){
        if (playerProperties.hungerEffectEstimateRaw > playerProperties.hunger && !difficulty.equals(Difficulty.PEACEFUL) && !config.disableHunger){
            renderRightToLeftBar(playerProperties.hungerEffectEstimateRaw, playerProperties.maxFoodLevelRaw, config.badThings.hungerEffectColor);
        }
    }

    private void hungerBar(){
        if(hasHunger)
            renderRightToLeftBar(playerProperties.hungerRaw, playerProperties.maxFoodLevelRaw, config.badThings.hungerColor);
    }

    private void airBar(){
        renderRightToLeftBar(playerProperties.airRaw, playerProperties.maxAirRaw, config.badThings.airColor);
    }

    private void freezeBar(){
        renderRightToLeftBar(playerProperties.freezeRaw, playerProperties.maxFreezeRaw, config.badThings.freezeColor);
    }

    private void levitationBar(){
        renderRightToLeftBar(playerProperties.levitationTimeRaw, playerProperties.maxLevitationTimeRaw, config.badThings.levitationColor);
    }

    private void fireBar(){
        if (playerProperties.isBurning && !playerProperties.hasFireResistance){
            renderRightToLeftBar((playerProperties.maxHealthRaw - playerProperties.healthRaw) + playerProperties.burningMultiplier, playerProperties.maxHealthRaw, config.badThings.fireColor);
        }
    }

    private void barText(){
        if (client.player == null) return;

        String value = "";
        boolean showHealthParentheses = config.textSettings.estimatesParentheses &&
                (((hasHunger || playerProperties.hasHungerEffect && !config.disableHunger || playerProperties.isUnderwater || playerProperties.isFreezing || playerProperties.isBurning || playerProperties.hasAbsorption || (playerProperties.hasResistance && config.goodThings.showResistance)) &&
                ((playerProperties.naturalRegenerationHealth > playerProperties.health && !config.uhcMode) || playerProperties.hasRegeneration || playerProperties.isStarving && !config.disableHunger || playerProperties.hasPoison || playerProperties.hasWither || playerProperties.isGettingFreezeDamage
                        || playerProperties.isBurningOnFire || playerProperties.isDrowning || playerProperties.isSuffocating)) || (playerProperties.levitationFallHurts && playerProperties.hasLevitation && config.badThings.showFallHeight)
                || (playerProperties.normalFallHurts && !playerProperties.hasLevitation && config.badThings.showFallHeight));

        boolean showHungerParentheses = config.textSettings.estimatesParentheses && config.healthEstimates && (playerProperties.hasHungerEffect && !config.disableHunger || (hasHunger && playerProperties.isHoldingFood && config.goodThings.heldFoodHungerBar));

        final String arrowRight = Component.translatable("text.onebar.healthEstimateEmoji").getString();
        final String plus = "+";
        final String minus = "-";
        final String para = "§";
        final String pStart = "(";
        final String pEnd = ")";

        if(config.textSettings.showText) {

            // Health values

            if (config.healthEstimates && showHealthParentheses)
                value += pStart;

            value += Calculations.emojiOrText("text.onebar.healthEmoji","text.onebar.health", true, config.textSettings.rawHealth ? (Math.round(playerProperties.healthRaw * 100.0) / 100.0) : Calculations.makeFraction(playerProperties.health, false));

            if(config.healthEstimates){
                if (playerProperties.naturalRegenerationHealth > playerProperties.health && !config.uhcMode)
                    value += arrowRight + Calculations.makeFraction(playerProperties.naturalRegenerationHealth, config.textSettings.estimatesItalic);
                //if (hasHunger && playerProperties.isHoldingFood && playerProperties.heldFoodHealthEstimate > playerProperties.health)
                //    value += arrowRight + Calculations.MakeFraction(playerProperties.heldFoodHealthEstimate, config.textSettings.estimatesItalic);
                if (playerProperties.levitationFallHurts && playerProperties.hasLevitation && config.badThings.showFallHeight)
                    value += arrowRight + Calculations.makeFraction(playerProperties.levitationFallHealthEstimate, config.textSettings.estimatesItalic);
                if (playerProperties.normalFallHurts && !playerProperties.hasLevitation && config.badThings.showFallHeight)
                    value += arrowRight + Calculations.makeFraction(playerProperties.normalFallHealthEstimate, config.textSettings.estimatesItalic);
                if (playerProperties.hasRegeneration)
                    value += arrowRight + Calculations.makeFraction(playerProperties.regenerationHealth, config.textSettings.estimatesItalic);
                if (playerProperties.isStarving && hasHunger)
                    value += arrowRight + Calculations.makeFraction(playerProperties.starvationHealthEstimate, config.textSettings.estimatesItalic);
                if (playerProperties.hasPoison)
                    value += arrowRight + Calculations.makeFraction(playerProperties.poisonHealth, config.textSettings.estimatesItalic);
                if (playerProperties.hasWither)
                    value += arrowRight + Calculations.makeFraction(playerProperties.witherHealth, config.textSettings.estimatesItalic);
                if (playerProperties.isWardenAngry)
                    value += arrowRight + Calculations.makeFraction(0, config.textSettings.estimatesItalic);
                if (playerProperties.isGettingFreezeDamage)
                    value += arrowRight + Calculations.makeFraction(0, config.textSettings.estimatesItalic);
                if (playerProperties.isBurningOnFire)
                    value += arrowRight + Calculations.makeFraction(0, config.textSettings.estimatesItalic);
                if (playerProperties.isDrowning)
                    value += arrowRight + Calculations.makeFraction(0, config.textSettings.estimatesItalic);
                if (playerProperties.isSuffocating)
                    value += arrowRight + Calculations.makeFraction(0, config.textSettings.estimatesItalic);
                if (showHealthParentheses)
                    value += pEnd;
            }
        }

        // Additive values

        if (playerProperties.hasAbsorption)
            value += plus + Calculations.emojiOrText("text.onebar.absorptionEmoji","text.onebar.absorption", true, Calculations.makeFraction(playerProperties.absorption, false));

        if(config.textSettings.showText) { // Separated if because order matters
            if (playerProperties.hasResistance && config.goodThings.showResistance)
                value += plus + Calculations.emojiOrText("text.onebar.resistanceEmoji","text.onebar.resistance", false, playerProperties.resistancePercent);
            if(PlayerProperties.getMobHead(client.player) != null && config.armor.showMobHeads)
                value += plus + PlayerProperties.getMobHead(client.player);
            if(playerProperties.hasInvisibility && !playerProperties.hasAnyArmorItem && !playerProperties.hasArrowsStuck && !playerProperties.hasGlowing && config.goodThings.showInvisibility)
                value += plus + Calculations.emojiOrText("text.onebar.invisibilityEmoji","text.onebar.invisibility", false, (Object) null);
            if(playerProperties.hasInvisibility && (playerProperties.hasAnyArmorItem || playerProperties.hasArrowsStuck || playerProperties.hasGlowing) && config.goodThings.showInvisibility)
                value += plus + para + "m" + Calculations.emojiOrText("text.onebar.invisibilityEmoji","text.onebar.invisibility", false, (Object) null) + para + "r";
            if(playerProperties.hasTotemOfUndying && playerProperties.isHoldingTotemOfUndying && config.goodThings.showTotemOfUndying)
                value += plus + Calculations.emojiOrText("text.onebar.totemOfUndyingEmoji","text.onebar.totemOfUndying", false, playerProperties.amountTotemOfUndying);
            if(playerProperties.hasTotemOfUndying && !playerProperties.isHoldingTotemOfUndying && config.goodThings.showTotemOfUndying)
                value += plus + para + "m" + Calculations.emojiOrText("text.onebar.totemOfUndyingEmoji","text.onebar.totemOfUndying", false, playerProperties.amountTotemOfUndying) + para + "r";

            // Subtractive values

            if(playerProperties.isWardenNear && config.badThings.showWarden)
                value += minus + Calculations.emojiOrText("text.onebar.wardenEmoji","text.onebar.warden",false, Calculations.makeFraction(playerProperties.wardenDanger, false));
            if (playerProperties.isUnderwater && !playerProperties.hasWaterBreathing && !playerProperties.hasWaterPause)
                value += minus + Calculations.emojiOrText("text.onebar.airEmoji", "text.onebar.air", false, Calculations.makeFraction(playerProperties.air, false));
            if (playerProperties.isUnderwater && playerProperties.hasWaterBreathing && !playerProperties.hasWaterPause)
                value += minus + para + "m" + Calculations.emojiOrText("text.onebar.airEmoji","text.onebar.air", false, Calculations.makeFraction(playerProperties.air, false)) + para + "r";
            if (playerProperties.isUnderwater && !playerProperties.hasWaterBreathing && playerProperties.hasWaterPause)
                value += minus + Calculations.emojiOrText("text.onebar.airPauseEmoji","text.onebar.airPause", false, Calculations.makeFraction(playerProperties.air, false));
            if (playerProperties.isUnderwater && playerProperties.hasWaterBreathing && playerProperties.hasWaterPause)
                value += minus + para + "m" + Calculations.emojiOrText("text.onebar.airPauseEmoji","text.onebar.airPause", false, Calculations.makeFraction(playerProperties.air, false)) + para + "r";
            if (playerProperties.isFreezing)
                value += minus + Calculations.emojiOrText("text.onebar.freezeEmoji", "text.onebar.freeze", false, Calculations.makeFraction(playerProperties.freeze, false));
            if (playerProperties.isBurning && !playerProperties.hasFireResistance && config.badThings.showFire)
                value += minus + Calculations.emojiOrText("text.onebar.fireEmoji","text.onebar.fire", false, playerProperties.burningMultiplier);
            if (playerProperties.isBurning && playerProperties.hasFireResistance && config.badThings.showFire)
                value += minus + para + "m" + Calculations.emojiOrText("text.onebar.fireEmoji","text.onebar.fire", false, playerProperties.burningMultiplier) + para + "r";
            if (playerProperties.hasLevitation && !playerProperties.isInWater && config.badThings.showLevitation)
                value += minus + Calculations.emojiOrText("text.onebar.levitationEmoji", "text.onebar.levitation", false, playerProperties.levitationTime);
            if (playerProperties.hasLevitation && playerProperties.isInWater && config.badThings.showLevitation)
                value += minus + para + "m" + Calculations.emojiOrText("text.onebar.levitationEmoji", "text.onebar.levitation", false, playerProperties.levitationTime + para + "r");
            if (playerProperties.levitationFallHurts && config.badThings.showFallHeight && config.badThings.showLevitation)
                value += Calculations.emojiOrText("text.onebar.fallingEmoji", "text.onebar.falling", false, playerProperties.levitationFallHeight);
            if (playerProperties.normalFallHurts && config.badThings.showFallHeight && !playerProperties.hasLevitation)
                value += minus + Calculations.emojiOrText("text.onebar.fallingEmoji", "text.onebar.falling", false, playerProperties.normalFallHeightDisplay);
            if (playerProperties.hasGlowing && config.badThings.showGlowing)
                value += minus + Calculations.emojiOrText("text.onebar.glowingEmoji","text.onebar.glowing", false, (Object) null);
            if (playerProperties.hasInfested && config.badThings.showInfested)
                value += minus + Calculations.emojiOrText("text.onebar.infestedEmoji","text.onebar.infested", false, (Object) null);
            if (playerProperties.hasWeaving && config.badThings.showPostDeathEffects)
                value += minus + Calculations.emojiOrText("text.onebar.weavingEmoji","text.onebar.weaving", false, (Object) null);
            if (playerProperties.hasOozing && config.badThings.showPostDeathEffects)
                value += minus + Calculations.emojiOrText("text.onebar.oozingEmoji","text.onebar.oozing", false, (Object) null);
            if (playerProperties.hasWindCharged && config.badThings.showPostDeathEffects)
                value += minus + Calculations.emojiOrText("text.onebar.windChargedEmoji","text.onebar.windCharged", false, (Object) null);
            if (playerProperties.hasBadOmen && config.badThings.showOmens)
                value += minus + Calculations.emojiOrText("text.onebar.badOmenEmoji","text.onebar.badOmen", false, playerProperties.badOmenLevel);
            if (playerProperties.hasRaidOmen && config.badThings.showOmens)
                value += minus + Calculations.emojiOrText("text.onebar.raidOmenEmoji","text.onebar.raidOmen", false, playerProperties.raidOmenWaves);
            if (playerProperties.hasTrialOmen && config.badThings.showOmens)
                value += minus + Calculations.emojiOrText("text.onebar.trialOmenEmoji","text.onebar.trialOmen", false, playerProperties.trialOmenMinutes);
            if(playerProperties.isVisibleOnLocatorBar && config.otherBars.showLocatability)
                value += minus + Calculations.emojiOrText("text.onebar.locatorEmoji","text.onebar.locator", false, (Object) null);
            if (clientProperties.isHardcore)
                value += minus + Calculations.emojiOrText("text.onebar.hardcoreEmoji","text.onebar.hardcore", false, (Object) null);
            if (hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates && !config.disableHunger))
                value += minus;

            if (showHungerParentheses)
                value += pStart;
            if (hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates && !config.disableHunger))
                value += Calculations.emojiOrText("text.onebar.hungerEmoji","text.onebar.hunger", true, Calculations.makeFraction(playerProperties.hunger, false));
            if (hasHunger && playerProperties.saturation < 1 && config.badThings.showHungerDecreasing)
                value += Component.translatable("text.onebar.gettingHungrierEmoji").getString();
            if (playerProperties.hasHungerEffect && !config.disableHunger && config.healthEstimates)
                value += arrowRight + Calculations.makeFraction(playerProperties.hungerEffectEstimate, config.textSettings.estimatesItalic);
            if (hasHunger && playerProperties.isHoldingFood && config.goodThings.heldFoodHungerBar)
                value += arrowRight + Calculations.makeFraction(playerProperties.heldFoodHungerEstimate, config.textSettings.estimatesItalic);
            if (showHungerParentheses)
                value += pEnd;
        }

        int textX = clientProperties.baseEndW - client.font.width(value);
        int textY = clientProperties.baseStartH + 1;

        drawContext.drawString(textRenderer, value, textX, textY, config.textSettings.textColor, false);
    }

    private void xpBar(){
        int relativeEndW = Calculations.relativeW(clientProperties.xpStartW, clientProperties.xpEndW, playerProperties.xp, playerProperties.maxXp);

        int textX = clientProperties.xpStartW + 9;
        int textY = clientProperties.xpStartH - 10;

        var lapisText = "";
        if(config.otherBars.lapisCounter)
        {
            if(config.otherBars.lapisTimesEnchantable) {
                lapisText = Calculations.getSubscriptNumber(playerProperties.lapisLazuliMax) + "ₓ";
                if((config.otherBars.adaptiveXpBar && playerProperties.lapisLazuliMax < 1))
                    lapisText = "";
            }
            else {
                lapisText = Calculations.getSubscriptNumber(playerProperties.lapisLazuli) + "";
                if(config.otherBars.adaptiveXpBar && playerProperties.lapisLazuli < 1)
                    lapisText = "";
            }
        }
        
        if(!config.otherBars.adaptiveXpBar || playerProperties.xpLevel > 0){
            int sizeLimit = !client.options.forceUnicodeFont().get() ? 10000 : 1000000;
            int edgeAlignedConst = 13;

            if(playerProperties.xpLevel >= 0 && playerProperties.xpLevel < sizeLimit){
                drawContext.drawCenteredString(textRenderer, String.valueOf(playerProperties.xpLevel), textX, textY, config.otherBars.xpColor);
            }
            else if(playerProperties.xpLevel >= sizeLimit){
                if(client.options.mainHand().get() == HumanoidArm.RIGHT)
                    drawContext.drawString(textRenderer, String.valueOf(playerProperties.xpLevel), textX - edgeAlignedConst, textY, config.otherBars.xpColor);
                else if(client.options.mainHand().get()  == HumanoidArm.LEFT)
                    drawContext.drawString(textRenderer, String.valueOf(playerProperties.xpLevel), textX + edgeAlignedConst - client.font.width(String.valueOf(playerProperties.xpLevel)), textY, config.otherBars.xpColor);
            }

            var lapisTextX = clientProperties.xpEndW + 1;
            var lapisTextY = clientProperties.xpStartH - 5;

            var mendingTextY = textY - 8;
            if(client.options.mainHand().get() == HumanoidArm.LEFT){
                lapisTextX = clientProperties.xpStartW - 1 - client.font.width(lapisText);
            }

            if(config.otherBars.lapisCounter){
                drawContext.drawString(textRenderer, lapisText, lapisTextX, lapisTextY, config.otherBars.lapisColor);
            }

            if(config.otherBars.mendingIndicator && playerProperties.isMendingAnything){
                drawContext.drawCenteredString(textRenderer, Calculations.emojiOrText("text.onebar.mendingEmoji", "text.onebar.mending", false), textX, mendingTextY, config.otherBars.mendingColor);
            }
        }
        if(!config.otherBars.adaptiveXpBar || playerProperties.xp > 0){
            renderBar(clientProperties.xpStartW, clientProperties.xpStartH, clientProperties.xpEndW, clientProperties.xpEndH, config.backgroundColor);
            renderBar(clientProperties.xpStartW, clientProperties.xpStartH, relativeEndW, clientProperties.xpEndH, config.otherBars.xpColor);
        }
    }

    public void mountJumpBar() {
        if(client.player == null) return;

        var entity = client.player.getControlledVehicle();
        if (entity == null) return;

        if (entity instanceof AbstractHorse && !(entity instanceof Camel)) {
            jumpBar();
        } else { // Camel, Nautilus, modded mobs
            dashBar();
        }
    }

    public void jumpBar(){
        if (client.player == null || client.player.jumpableVehicle() == null) return;

        int barHeight = Calculations.getPreciseInt(1.0F);
        int jumpHeight = Calculations.getPreciseInt(client.player.getJumpRidingScale());

        double heightInBlocks = Math.max(0, client.player.getJumpRidingScale() *
                                                Calculations.getJumpHeight(client.player.getJumpRidingScale()));

        String roundedHeightInBlocks = Calculations.getSubscriptNumber(Double.parseDouble(String.format(Locale.US, "%,.1f",(heightInBlocks))));

        int relativeStartH = Calculations.relativeW(clientProperties.jumpEndH, clientProperties.jumpStartH, jumpHeight, barHeight);
        renderBar(clientProperties.jumpStartW, clientProperties.jumpStartH, clientProperties.jumpEndW, clientProperties.jumpEndH, config.backgroundColor);
        renderBar(clientProperties.jumpStartW, relativeStartH, clientProperties.jumpEndW, clientProperties.jumpEndH, config.entity.jumpColor);

        int textX = clientProperties.jumpEndW - client.font.width(roundedHeightInBlocks);
        int textY = clientProperties.jumpEndH - 10;

        if(config.textSettings.showText && config.entity.showMountJumpText)
            drawContext.drawString(textRenderer, roundedHeightInBlocks, textX, textY, config.textSettings.textColor, false);
    }

    public void dashBar(){
        if (client.player == null || client.player.jumpableVehicle() == null) return;

        int jumpStrength = Calculations.getPreciseInt(Math.max(client.player.getJumpRidingScale(), 0)); //TODO: strength can be negative???
        int maxStrength = Calculations.getPreciseInt(1.0F);
        int cooldown = Objects.requireNonNull(client.player.jumpableVehicle()).getJumpCooldown();
        int maxCooldown = 50;
        int cooldownVisible = cooldown / 20;

        double distanceInBlocks = Math.max(0, Calculations.getDashDistance(client.player.getJumpRidingScale()));

        int relativeEndW = clientProperties.dashRelativeEndW(jumpStrength, maxStrength);
        int relativeEndWCooldown = clientProperties.dashRelativeEndW(cooldown, maxCooldown);

        if(relativeEndWCooldown > relativeEndW){
            dashCooldownBar(relativeEndWCooldown, cooldownVisible);
        }
        else {
            renderBar(clientProperties.dashStartW, clientProperties.dashStartH, clientProperties.dashEndW, clientProperties.dashEndH, config.backgroundColor);
            renderBar(clientProperties.dashStartW, clientProperties.dashStartH, relativeEndW, clientProperties.dashEndH, config.entity.jumpColor);

            if(config.textSettings.showText && config.entity.showMountJumpText){
                String roundedDistanceInBlocks = Calculations.getSubscriptNumber(Double.parseDouble(String.format(Locale.US, "%,.1f",(distanceInBlocks))));
                int textX = clientProperties.dashEndW - client.font.width(roundedDistanceInBlocks);
                int textY = clientProperties.dashEndH - 9;
                drawContext.drawString(textRenderer, roundedDistanceInBlocks, textX, textY, config.textSettings.textColor, false);
            }
        }
    }

    private void dashCooldownBar(int relativeEndW, int cooldownTimer){
        if(config.entity.showMountCooldown){
            renderBar(clientProperties.dashStartW, clientProperties.dashStartH, clientProperties.dashEndW, clientProperties.dashEndH, config.backgroundColor);
            renderBar(clientProperties.dashStartW, clientProperties.dashStartH, relativeEndW, clientProperties.dashEndH, config.entity.cooldownColor);

            if(config.textSettings.showText && config.entity.showMountCooldownText){
                String text = Calculations.getSubscriptNumber(-1 - cooldownTimer);
                int textX = clientProperties.dashEndW - client.font.width(text);
                int textY = clientProperties.dashEndH - 9;
                drawContext.drawString(textRenderer, text, textX, textY, config.textSettings.textColor, false);
            }
        }
    }

    public static int getProtectionFromArmor(ItemStack armorItem) {
        ItemAttributeModifiers attributeModifierComponent = armorItem.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (attributeModifierComponent == null)
            return 0;
        ResourceKey<Attribute> ARMOR = Attributes.ARMOR.unwrapKey().get();

        return attributeModifierComponent.modifiers().stream()
                .filter(entry -> entry.attribute().is(ARMOR))
                .mapToInt(entry -> (int) entry.modifier().amount())
                .findFirst()
                .orElse(0);
    }


    public void mountBar(LivingEntity mountEntity){
        if (mountEntity == null) {return;}
        float mountRawHealth = mountEntity.getHealth();
        float mountMaxHealth = mountEntity.getMaxHealth();
        int health = (int) Math.ceil(mountRawHealth);
        int mountArmor = mountEntity.getArmorValue();
        int mountMaxArmor = getProtectionFromArmor(new ItemStack((Items.NETHERITE_HORSE_ARMOR)));

        String value = Calculations.emojiOrText("text.onebar.mountHealthEmoji","text.onebar.mountHealth", true, config.textSettings.rawHealth ? (Math.round(mountRawHealth * 100.0) / 100.0) : Calculations.makeFraction(health, false));
        int textX = clientProperties.baseEndW - client.font.width(value);
        int textY = clientProperties.mountStartH + 1;

        renderBar(clientProperties.baseStartW, clientProperties.mountStartH, clientProperties.baseEndW, clientProperties.mountEndH, config.backgroundColor);
        renderBar(clientProperties.baseStartW, clientProperties.mountStartH, clientProperties.baseRelativeEndW(Calculations.getPreciseInt(mountRawHealth), Calculations.getPreciseInt(mountMaxHealth)), clientProperties.mountEndH, config.entity.healthColor);
        if(config.armor.showArmorBar) renderBar(clientProperties.baseStartW, clientProperties.mountStartH - 1, clientProperties.baseRelativeEndW(mountArmor, mountMaxArmor), clientProperties.mountStartH, config.armor.armorColor);
        if(config.textSettings.showText) drawContext.drawString(textRenderer, value, textX, textY, config.textSettings.textColor, false);

        if(mountEntity instanceof Camel){
            long standingUpMax = 52;
            var standingUpTimer = Math.min(standingUpMax, ((Camel)mountEntity).getPoseTime());
            int standingUpTimerVisible = Math.round((standingUpMax - standingUpTimer) / (float)20);

            if(((Camel)mountEntity).isInPoseTransition()){
                dashCooldownBar(clientProperties.dashRelativeEndW(standingUpMax - standingUpTimer, standingUpMax), standingUpTimerVisible);
            }
        }

    }

    private void debugText(String value){
        drawContext.drawString(textRenderer, value, clientProperties.baseEndW + 15, clientProperties.baseStartH + 1, config.textSettings.textColor);
    }
}
