package io.github.madis0;

import com.mojang.blaze3d.systems.RenderSystem;
//import dev.tr7zw.exordium.ExordiumModBase;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.world.Difficulty;

import java.util.Locale;
import java.util.Objects;

public class OneBarElements {
    private final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    private final ClientProperties clientProperties = new ClientProperties();
    private final PlayerProperties playerProperties = new PlayerProperties();
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final Difficulty difficulty = Objects.requireNonNull(client.getCameraEntity()).getWorld().getDifficulty();
    private final DrawContext drawContext;
    private final TextRenderer textRenderer = client.textRenderer;
    private static final boolean hasExordium = FabricLoader.getInstance().isModLoaded("exordium");

    boolean hasHunger = playerProperties.hasHunger && !config.disableHunger;

    public OneBarElements(DrawContext context){
        drawContext = context;
    }

    public void renderOneBar(){
       // if(hasExordium) {
       //     ExordiumModBase.correctBlendMode();
       //     ExordiumModBase.setForceBlend(true);
       // }

        PlayerEntity playerEntity = MinecraftClient.getInstance().player;
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
            if(config.otherBars.showSaturationBar) saturationBar();
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
            drawContext.fillGradient(RenderLayer.getGui(), x1, y1, x2, y2, color, Calculations.manipulateColor(color, config.gradientShift), 0);
    }

    private void barBackground(){
        renderBar(clientProperties.baseStartW, clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.backgroundColor);
    }

    private void armorBar(){
        var gap = 0.1F;
        var chestplateLength = gap + playerProperties.helmetMaxArmor;
        var leggingsLength = chestplateLength + gap + playerProperties.chestplateMaxArmor;
        var bootsLength =  leggingsLength + gap + playerProperties.leggingsMaxArmor;

        var totalLength = bootsLength + playerProperties.bootsMaxArmor;
        var elytraDurability = playerProperties.getArmorElementDurability(client.player, EquipmentSlot.CHEST, 8); //8 aka same full width as Diamond/Netherite

        if (!config.armor.showSegmentedArmorBar)
            renderBar(clientProperties.baseStartW, clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(playerProperties.armor, playerProperties.maxArmor), clientProperties.baseStartH, config.armor.armorColor);
        else {
            renderBar(clientProperties.baseRelativeEndW(0, totalLength), clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(playerProperties.helmetArmor, totalLength), clientProperties.baseStartH, config.armor.armorColor);
            if (!playerProperties.hasElytra)
                renderBar(clientProperties.baseRelativeEndW(chestplateLength, totalLength), clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(chestplateLength + playerProperties.chestplateArmor, totalLength), clientProperties.baseStartH, config.armor.armorColor);
            else if (config.armor.showElytraDurabilityBar && !config.armor.showArmorDurabilityBar)
                renderBar(clientProperties.baseRelativeEndW(chestplateLength, totalLength), clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(chestplateLength + elytraDurability, totalLength), clientProperties.baseStartH, config.armor.elytraDurabilityColor); // duplicated from armorDurabilityBar() as a fallback
            renderBar(clientProperties.baseRelativeEndW(leggingsLength, totalLength), clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(leggingsLength + playerProperties.leggingsArmor, totalLength), clientProperties.baseStartH, config.armor.armorColor);
            renderBar(clientProperties.baseRelativeEndW(bootsLength, totalLength), clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(bootsLength + playerProperties.bootsArmor, totalLength), clientProperties.baseStartH, config.armor.armorColor);
        }
    }

    private void armorDurabilityBar(){
        var gap = 0.1F;
        var chestplateLength = gap + playerProperties.helmetMaxArmor;
        var leggingsLength = chestplateLength + gap + playerProperties.chestplateMaxArmor;
        var bootsLength =  leggingsLength + gap + playerProperties.leggingsMaxArmor;
        var totalLength = bootsLength + playerProperties.bootsMaxArmor;

        var helmetDurability = playerProperties.getArmorElementDurability(client.player, EquipmentSlot.HEAD, playerProperties.helmetArmor);
        var chestplateDurability = playerProperties.getArmorElementDurability(client.player, EquipmentSlot.CHEST, playerProperties.chestplateArmor);
        var elytraDurability = playerProperties.getArmorElementDurability(client.player, EquipmentSlot.CHEST, 8); //8 aka same full width as Diamond/Netherite
        var leggingsDurability = playerProperties.getArmorElementDurability(client.player, EquipmentSlot.LEGS, playerProperties.leggingsArmor);
        var bootsDurability = playerProperties.getArmorElementDurability(client.player, EquipmentSlot.FEET, playerProperties.bootsArmor);

        if(!config.armor.showSegmentedArmorBar){
            if(playerProperties.maxArmorDurability > 0)
                renderBar(clientProperties.baseStartW, clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(playerProperties.armorDurability, playerProperties.maxArmor), clientProperties.baseStartH, config.armor.armorDurabilityColor);
        }
        else {
            renderBar(clientProperties.baseRelativeEndW(0, totalLength), clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(helmetDurability, totalLength), clientProperties.baseStartH, config.armor.armorDurabilityColor);
            if(!playerProperties.hasElytra)
                renderBar(clientProperties.baseRelativeEndW(chestplateLength, totalLength), clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(chestplateLength + chestplateDurability, totalLength), clientProperties.baseStartH, config.armor.armorDurabilityColor);
            else if(config.armor.showElytraDurabilityBar)
                renderBar(clientProperties.baseRelativeEndW(chestplateLength, totalLength), clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(chestplateLength + elytraDurability, totalLength), clientProperties.baseStartH, config.armor.elytraDurabilityColor);
            renderBar(clientProperties.baseRelativeEndW(leggingsLength, totalLength), clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(leggingsLength + leggingsDurability, totalLength), clientProperties.baseStartH, config.armor.armorDurabilityColor);
            renderBar(clientProperties.baseRelativeEndW(bootsLength, totalLength), clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(bootsLength + bootsDurability, totalLength), clientProperties.baseStartH, config.armor.armorDurabilityColor);
        }
    }

    private void elytraDurabilityBar(){
        if(playerProperties.isFlyingWithElytra && !config.armor.showSegmentedArmorBar)
            renderBar(clientProperties.baseStartW, clientProperties.baseStartH - 1, clientProperties.baseRelativeEndW(playerProperties.elytraDurability, playerProperties.elytraMaxDurability), clientProperties.baseStartH, config.armor.elytraDurabilityColor);
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

    private void levitationBar(){
        renderBar(clientProperties.baseRelativeStartW(playerProperties.levitationTimeRaw, playerProperties.maxLevitationTimeRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.levitationColor);
    }

    private void fireBar(){
        if (playerProperties.isBurning && !playerProperties.hasFireResistance){
            renderBar(clientProperties.baseRelativeStartW((playerProperties.maxHealthRaw - playerProperties.healthRaw) + playerProperties.burningMultiplier, playerProperties.maxHealthRaw), clientProperties.baseStartH, clientProperties.baseEndW, clientProperties.baseEndH, config.badThings.fireColor);
        }
    }

    private void barText(){
        String value = "";
        boolean showHealthParentheses = config.textSettings.estimatesParentheses &&
                (((hasHunger || playerProperties.hasHungerEffect && !config.disableHunger || playerProperties.isUnderwater || playerProperties.isFreezing || playerProperties.isBurning || playerProperties.hasAbsorption || (playerProperties.hasResistance && config.goodThings.showResistance)) &&
                ((playerProperties.naturalRegenerationHealth > playerProperties.health && !config.uhcMode) || playerProperties.hasRegeneration || playerProperties.isStarving && !config.disableHunger || playerProperties.hasPoison || playerProperties.hasWither || playerProperties.isGettingFreezeDamage
                        || playerProperties.isBurningOnFire || playerProperties.isDrowning || playerProperties.isSuffocating)) || (playerProperties.levitationFallHurts && playerProperties.hasLevitation && config.badThings.showFallHeight)
                || (playerProperties.normalFallHurts && !playerProperties.hasLevitation && config.badThings.showFallHeight));

        boolean showHungerParentheses = config.textSettings.estimatesParentheses && config.healthEstimates && (playerProperties.hasHungerEffect && !config.disableHunger || (hasHunger && playerProperties.isHoldingFood && config.goodThings.heldFoodHungerBar));

        final String arrowRight = Text.translatable("text.onebar.healthEstimateEmoji").getString();
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
            if(playerProperties.hasGoldenArmorItem && config.armor.showMobHeads)
                value += plus + Calculations.emojiOrText("text.onebar.mobHeadPiglinEmoji","text.onebar.mobHeadPiglin", false, (Object) null);
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
            if (playerProperties.isUnderwater && !playerProperties.hasWaterBreathing)
                value += minus + Calculations.emojiOrText("text.onebar.airEmoji", "text.onebar.air", false, Calculations.makeFraction(playerProperties.air, false));
            if (playerProperties.isUnderwater && playerProperties.hasWaterBreathing)
                value += minus + para + "m" + Calculations.emojiOrText("text.onebar.airEmoji","text.onebar.air", false, Calculations.makeFraction(playerProperties.air, false)) + para + "r";
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
            if (playerProperties.hasBadOmen && config.badThings.showBadOmen)
                value += minus + Calculations.emojiOrText("text.onebar.badOmenEmoji","text.onebar.badOmen", false, playerProperties.badOmenLevel);
            if (clientProperties.isHardcore)
                value += minus + Calculations.emojiOrText("text.onebar.hardcoreEmoji","text.onebar.hardcore", false, (Object) null);
            if (hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates && !config.disableHunger))
                value += minus;

            if (showHungerParentheses)
                value += pStart;
            if (hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates && !config.disableHunger))
                value += Calculations.emojiOrText("text.onebar.hungerEmoji","text.onebar.hunger", true, Calculations.makeFraction(playerProperties.hunger, false));
            if (hasHunger && playerProperties.saturation < 1 && config.badThings.showHungerDecreasing)
                value += Text.translatable("text.onebar.gettingHungrierEmoji").getString();
            if (playerProperties.hasHungerEffect && !config.disableHunger && config.healthEstimates)
                value += arrowRight + Calculations.makeFraction(playerProperties.hungerEffectEstimate, config.textSettings.estimatesItalic);
            if (hasHunger && playerProperties.isHoldingFood && config.goodThings.heldFoodHungerBar)
                value += arrowRight + Calculations.makeFraction(playerProperties.heldFoodHungerEstimate, config.textSettings.estimatesItalic);
            if (showHungerParentheses)
                value += pEnd;
        }

        int textX = clientProperties.baseEndW - client.textRenderer.getWidth(value);
        int textY = clientProperties.baseStartH + 1;

        drawContext.drawText(textRenderer, value, textX, textY, config.textSettings.textColor, false);
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
            int sizeLimit = !client.options.getForceUnicodeFont().getValue() ? 10000 : 1000000;
            int edgeAlignedConst = 13;

            if(playerProperties.xpLevel >= 0 && playerProperties.xpLevel < sizeLimit){
                drawContext.drawCenteredTextWithShadow(textRenderer, String.valueOf(playerProperties.xpLevel), textX, textY, config.otherBars.xpColor);
            }
            else if(playerProperties.xpLevel >= sizeLimit){
                if(client.options.getMainArm().getValue() == Arm.RIGHT)
                    drawContext.drawTextWithShadow(textRenderer, String.valueOf(playerProperties.xpLevel), textX - edgeAlignedConst, textY, config.otherBars.xpColor);
                else if(client.options.getMainArm().getValue()  == Arm.LEFT)
                    drawContext.drawTextWithShadow(textRenderer, String.valueOf(playerProperties.xpLevel), textX + edgeAlignedConst - client.textRenderer.getWidth(String.valueOf(playerProperties.xpLevel)), textY, config.otherBars.xpColor);
            }

            if(config.otherBars.lapisCounter){
                var lapisTextX = clientProperties.xpEndW + 1;
                if(client.options.getMainArm().getValue()  == Arm.LEFT)
                    lapisTextX = clientProperties.xpStartW - 1 - client.textRenderer.getWidth(lapisText);

                var lapisTextY = clientProperties.xpStartH - 5;
                drawContext.drawTextWithShadow(textRenderer, lapisText, lapisTextX, lapisTextY, config.otherBars.lapisColor);
            }
        }
        if(!config.otherBars.adaptiveXpBar || playerProperties.xp > 0){
            renderBar(clientProperties.xpStartW, clientProperties.xpStartH, clientProperties.xpEndW, clientProperties.xpEndH, config.backgroundColor);
            renderBar(clientProperties.xpStartW, clientProperties.xpStartH, relativeEndW, clientProperties.xpEndH, config.otherBars.xpColor);
        }
    }

    public void horseJumpBar(LivingEntity mountEntity){
        int barHeight = Calculations.getPreciseInt(1.0F);
        int jumpHeight = Calculations.getPreciseInt(Objects.requireNonNull(client.player).getMountJumpStrength());

        double heightInBlocks = Math.max(0, ((AbstractHorseEntity)mountEntity).getJumpStrength() *
                                                Calculations.horseJumpStrengthToJumpHeight(Objects.requireNonNull(client.player).getMountJumpStrength()));

        String roundedHeightInBlocks = Calculations.getSubscriptNumber(Double.parseDouble(String.format(Locale.US, "%,.1f",(heightInBlocks))));

        int relativeStartH = Calculations.relativeW(clientProperties.horseJumpEndH, clientProperties.horseJumpStartH, jumpHeight, barHeight);
        renderBar(clientProperties.horseJumpStartW, clientProperties.horseJumpStartH, clientProperties.horseJumpEndW, clientProperties.horseJumpEndH, config.backgroundColor);
        //renderBar(clientProperties.jumpStartW, clientProperties.jumpEndH, clientProperties.jumpEndW, relativeStartH, config.entity.jumpColor);
        drawContext.fill(clientProperties.horseJumpStartW, clientProperties.horseJumpEndH, clientProperties.horseJumpEndW, relativeStartH, config.entity.jumpColor); //TODO: fix gradient

        int textX = clientProperties.horseJumpEndW - client.textRenderer.getWidth(roundedHeightInBlocks);
        int textY = clientProperties.horseJumpEndH - 10;

        if(config.textSettings.showText && config.entity.showMountJumpText)
            drawContext.drawText(textRenderer, roundedHeightInBlocks, textX, textY, config.textSettings.textColor, false);
    }

    public void camelJumpBar(LivingEntity mountEntity){
        int jumpStrength = Calculations.getPreciseInt(Math.max(Objects.requireNonNull(client.player).getMountJumpStrength(), 0)); //TODO: strength can be negative???
        int maxStrength = Calculations.getPreciseInt(1.0F);
        int cooldown = Objects.requireNonNull(Objects.requireNonNull(client.player).getJumpingMount()).getJumpCooldown();
        int maxCooldown = 50;
        int cooldownVisible = cooldown / 20;

        int relativeEndW = clientProperties.camelRelativeEndW(jumpStrength, maxStrength);
        int relativeEndWCooldown = clientProperties.camelRelativeEndW(cooldown, maxCooldown);

        if(relativeEndWCooldown > relativeEndW){
            camelCooldownBar(relativeEndWCooldown, cooldownVisible);
        }
        else {
            renderBar(clientProperties.camelJumpStartW, clientProperties.camelJumpStartH, clientProperties.camelJumpEndW, clientProperties.camelJumpEndH, config.backgroundColor);
            renderBar(clientProperties.camelJumpStartW, clientProperties.camelJumpStartH, relativeEndW, clientProperties.camelJumpEndH, config.entity.jumpColor);
        }
    }

    private void camelCooldownBar(int relativeEndW, int cooldownTimer){
        if(config.entity.showMountCooldown){
            renderBar(clientProperties.camelJumpStartW, clientProperties.camelJumpStartH, clientProperties.camelJumpEndW, clientProperties.camelJumpEndH, config.backgroundColor);
            renderBar(clientProperties.camelJumpStartW, clientProperties.camelJumpStartH, relativeEndW, clientProperties.camelJumpEndH, config.entity.cooldownColor);

            if(config.textSettings.showText && config.entity.showMountCooldownText){
                String text = Calculations.getSubscriptNumber(-1 - cooldownTimer);
                int textX = clientProperties.camelJumpEndW - client.textRenderer.getWidth(text);
                int textY = clientProperties.camelJumpEndH - 9;
                drawContext.drawText(textRenderer, text, textX, textY, config.textSettings.textColor, false);
            }
        }
    }

    public void mountBar(LivingEntity mountEntity){
        if (mountEntity == null) {return;}
        float mountRawHealth = mountEntity.getHealth();
        float mountMaxHealth = mountEntity.getMaxHealth();
        int health = (int) Math.ceil(mountRawHealth);
        int horseArmor = mountEntity.getArmor();
        int horseMaxArmor = ((AnimalArmorItem)Items.DIAMOND_HORSE_ARMOR).getProtection();

        String value = Calculations.emojiOrText("text.onebar.mountHealthEmoji","text.onebar.mountHealth", true, config.textSettings.rawHealth ? (Math.round(mountRawHealth * 100.0) / 100.0) : Calculations.makeFraction(health, false));
        int textX = clientProperties.baseEndW - client.textRenderer.getWidth(value);
        int textY = clientProperties.mountStartH + 1;

        renderBar(clientProperties.baseStartW, clientProperties.mountStartH, clientProperties.baseEndW, clientProperties.mountEndH, config.backgroundColor);
        renderBar(clientProperties.baseStartW, clientProperties.mountStartH, clientProperties.baseRelativeEndW(Calculations.getPreciseInt(mountRawHealth), Calculations.getPreciseInt(mountMaxHealth)), clientProperties.mountEndH, config.entity.healthColor);
        if(config.armor.showArmorBar) renderBar(clientProperties.baseStartW, clientProperties.mountStartH - 1, clientProperties.baseRelativeEndW(horseArmor, horseMaxArmor), clientProperties.mountStartH, config.armor.armorColor);
        if(config.textSettings.showText) drawContext.drawText(textRenderer, value, textX, textY, config.textSettings.textColor, false);

        if(mountEntity instanceof CamelEntity){
            long standingUpMax = 52;
            var standingUpTimer = Math.min(standingUpMax, ((CamelEntity)mountEntity).getLastPoseTickDelta());
            int standingUpTimerVisible = Math.round((standingUpMax - standingUpTimer) / (float)20);

            if(((CamelEntity)mountEntity).isChangingPose()){
                camelCooldownBar(clientProperties.camelRelativeEndW(standingUpMax - standingUpTimer, standingUpMax), standingUpTimerVisible);
            }
        }

    }

    private void debugText(String value){
        drawContext.drawTextWithShadow(textRenderer, value, clientProperties.baseEndW + 15, clientProperties.baseStartH + 1, config.textSettings.textColor);
    }
}
