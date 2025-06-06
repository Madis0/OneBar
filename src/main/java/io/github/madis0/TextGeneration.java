package io.github.madis0;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.text.DecimalFormat;
import java.util.Objects;

public class TextGeneration {
    private final PlayerProperties playerProperties = new PlayerProperties();
    private final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    private final ClientProperties clientProperties = new ClientProperties();
    private final MinecraftClient client = MinecraftClient.getInstance();
    private boolean useSpeech;

    public TextGeneration() {
        this.useSpeech = false;
    }
    public TextGeneration(boolean useSpeech) {
        this.useSpeech = useSpeech;
    }

    public String GenerateOneBarText()
    {
        String value = "";
        boolean hasHunger = playerProperties.hasHunger && !config.disableHunger;

        boolean showHealthParentheses = config.textSettings.estimatesParentheses &&
                (((hasHunger || playerProperties.hasHungerEffect && !config.disableHunger || playerProperties.isUnderwater || playerProperties.isFreezing || playerProperties.isBurning || playerProperties.hasAbsorption || (playerProperties.hasResistance && config.goodThings.showResistance)) &&
                        ((playerProperties.naturalRegenerationHealth > playerProperties.health && !config.uhcMode) || playerProperties.hasRegeneration || playerProperties.isStarving && !config.disableHunger || playerProperties.hasPoison || playerProperties.hasWither || playerProperties.isGettingFreezeDamage
                                || playerProperties.isBurningOnFire || playerProperties.isDrowning || playerProperties.isSuffocating)) || (playerProperties.levitationFallHurts && playerProperties.hasLevitation && config.badThings.showFallHeight)
                        || (playerProperties.normalFallHurts && !playerProperties.hasLevitation && config.badThings.showFallHeight));

        boolean showHungerParentheses = config.textSettings.estimatesParentheses && config.healthEstimates && (playerProperties.hasHungerEffect && !config.disableHunger || (hasHunger && playerProperties.isHoldingFood && config.goodThings.heldFoodHungerBar));

        final String arrowRight = getSymbol("text.onebar.estimateTo");
        final String plus = getSymbol("text.onebar.plus");
        final String minus = getSymbol("text.onebar.minus");
        final String noStart = getSymbol("text.onebar.noStart");
        final String noEnd = getSymbol("text.onebar.noEnd");
        final String pStart = getSymbol("text.onebar.parStart");
        final String pEnd = getSymbol("text.onebar.parEnd");

        if(config.textSettings.showText) {

            // Health values

            if (config.healthEstimates && showHealthParentheses)
                value += pStart;

            value += getSymbol("text.onebar.health",
                    config.textSettings.rawHealth ?
                            getFraction(Math.round(playerProperties.healthRaw * 100.0) / 100.0) :
                            getFraction(playerProperties.health, false));

            if(config.healthEstimates){
                if (playerProperties.naturalRegenerationHealth > playerProperties.health && !config.uhcMode)
                    value += arrowRight + getFraction(playerProperties.naturalRegenerationHealth);
                //if (hasHunger && playerProperties.isHoldingFood && playerProperties.heldFoodHealthEstimate > playerProperties.health)
                //    value += arrowRight + getFraction(playerProperties.heldFoodHealthEstimate);
                if (playerProperties.levitationFallHurts && playerProperties.hasLevitation && config.badThings.showFallHeight)
                    value += arrowRight + getFraction(playerProperties.levitationFallHealthEstimate);
                if (playerProperties.normalFallHurts && !playerProperties.hasLevitation && config.badThings.showFallHeight)
                    value += arrowRight + getFraction(playerProperties.normalFallHealthEstimate);
                if (playerProperties.hasRegeneration)
                    value += arrowRight + getFraction(playerProperties.regenerationHealth);
                if (playerProperties.isStarving && hasHunger)
                    value += arrowRight + getFraction(playerProperties.starvationHealthEstimate);
                if (playerProperties.hasPoison)
                    value += arrowRight + getFraction(playerProperties.poisonHealth);
                if (playerProperties.hasWither)
                    value += arrowRight + getFraction(playerProperties.witherHealth);
                if (playerProperties.isWardenAngry)
                    value += arrowRight + getFraction(0);
                if (playerProperties.isGettingFreezeDamage)
                    value += arrowRight + getFraction(0);
                if (playerProperties.isBurningOnFire)
                    value += arrowRight + getFraction(0);
                if (playerProperties.isDrowning)
                    value += arrowRight + getFraction(0);
                if (playerProperties.isSuffocating)
                    value += arrowRight + getFraction(0);
                if (showHealthParentheses)
                    value += pEnd;
            }
        }

        // Additive values

        if (playerProperties.hasAbsorption)
            value += plus + getSymbol("text.onebar.absorption", getFraction(playerProperties.absorption, false));

        if(config.textSettings.showText) { // Separated if because order matters
            if (playerProperties.hasResistance && config.goodThings.showResistance)
                value += plus + getSymbol("text.onebar.resistance", playerProperties.resistancePercent);
            if(getMobHead(client.player) != null && config.armor.showMobHeads)
                value += plus + getMobHead(client.player);
            if(playerProperties.hasPiglinDeterArmorItem && config.armor.showMobHeads)
                value += plus + getSymbol("text.onebar.mobHeadPiglin");
            if(playerProperties.hasInvisibility && !playerProperties.hasAnyArmorItem && !playerProperties.hasArrowsStuck && !playerProperties.hasGlowing && config.goodThings.showInvisibility)
                value += plus + getSymbol("text.onebar.invisibility");
            if(playerProperties.hasInvisibility && (playerProperties.hasAnyArmorItem || playerProperties.hasArrowsStuck || playerProperties.hasGlowing) && config.goodThings.showInvisibility)
                value += plus + noStart + getSymbol("text.onebar.invisibility") + noEnd;
            if(playerProperties.hasTotemOfUndying && playerProperties.isHoldingTotemOfUndying && config.goodThings.showTotemOfUndying)
                value += plus + getSymbol("text.onebar.totemOfUndying", playerProperties.amountTotemOfUndying);
            if(playerProperties.hasTotemOfUndying && !playerProperties.isHoldingTotemOfUndying && config.goodThings.showTotemOfUndying)
                value += plus + noStart + getSymbol("text.onebar.totemOfUndying", playerProperties.amountTotemOfUndying) + noEnd;

            // Subtractive values

            if(playerProperties.isWardenNear && config.badThings.showWarden)
                value += minus + getSymbol("text.onebar.warden", getFraction(playerProperties.wardenDanger, false));
            if (playerProperties.isUnderwater && !playerProperties.hasWaterBreathing)
                value += minus + getSymbol( "text.onebar.air", getFraction(playerProperties.air, false));
            if (playerProperties.isUnderwater && playerProperties.hasWaterBreathing)
                value += minus + noStart + getSymbol("text.onebar.air", getFraction(playerProperties.air, false)) + noEnd;
            if (playerProperties.isFreezing)
                value += minus + getSymbol("text.onebar.freeze", getFraction(playerProperties.freeze, false));
            if (playerProperties.isBurning && !playerProperties.hasFireResistance && config.badThings.showFire)
                value += minus + getSymbol("text.onebar.fire", playerProperties.burningMultiplier);
            if (playerProperties.isBurning && playerProperties.hasFireResistance && config.badThings.showFire)
                value += minus + noStart + getSymbol("text.onebar.fire", playerProperties.burningMultiplier) + noEnd;
            if (playerProperties.hasLevitation && !playerProperties.isInWater && config.badThings.showLevitation)
                value += minus + getSymbol("text.onebar.levitation", playerProperties.levitationTime);
            if (playerProperties.hasLevitation && playerProperties.isInWater && config.badThings.showLevitation)
                value += minus + noStart + getSymbol("text.onebar.levitation", playerProperties.levitationTime + noEnd);
            if (playerProperties.levitationFallHurts && config.badThings.showFallHeight && config.badThings.showLevitation)
                value += getSymbol("text.onebar.falling", playerProperties.levitationFallHeight);
            if (playerProperties.normalFallHurts && config.badThings.showFallHeight && !playerProperties.hasLevitation)
                value += minus + getSymbol("text.onebar.falling", playerProperties.normalFallHeightDisplay);
            if (playerProperties.hasGlowing && config.badThings.showGlowing)
                value += minus + getSymbol("text.onebar.glowing");
            if (playerProperties.hasInfested && config.badThings.showInfested)
                value += minus + getSymbol("text.onebar.infested");
            if (playerProperties.hasWeaving && config.badThings.showPostDeathEffects)
                value += minus + getSymbol("text.onebar.weaving");
            if (playerProperties.hasOozing && config.badThings.showPostDeathEffects)
                value += minus + getSymbol("text.onebar.oozing");
            if (playerProperties.hasWindCharged && config.badThings.showPostDeathEffects)
                value += minus + getSymbol("text.onebar.windCharged");
            if (playerProperties.hasBadOmen && config.badThings.showOmens)
                value += minus + getSymbol("text.onebar.badOmen", playerProperties.badOmenLevel);
            if (playerProperties.hasRaidOmen && config.badThings.showOmens)
                value += minus + getSymbol("text.onebar.raidOmen", playerProperties.raidOmenLevel);
            if (playerProperties.hasTrialOmen && config.badThings.showOmens)
                value += minus + getSymbol("text.onebar.trialOmen", playerProperties.trialOmenLevel);
            if (clientProperties.isHardcore)
                value += minus + getSymbol("text.onebar.hardcore");
            if (hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates && !config.disableHunger))
                value += minus;

            if (showHungerParentheses)
                value += pStart;
            if (hasHunger || (playerProperties.hasHungerEffect && config.healthEstimates && !config.disableHunger))
                value += getSymbol("text.onebar.hunger", getFraction(playerProperties.hunger, false));
            if (hasHunger && playerProperties.saturation < 1 && config.badThings.showHungerDecreasing)
                value += getSymbol("text.onebar.gettingHungrier");
            if (playerProperties.hasHungerEffect && !config.disableHunger && config.healthEstimates)
                value += arrowRight + getFraction(playerProperties.hungerEffectEstimate);
            if (hasHunger && playerProperties.isHoldingFood && config.goodThings.heldFoodHungerBar)
                value += arrowRight + getFraction(playerProperties.heldFoodHungerEstimate);
            if (showHungerParentheses)
                value += pEnd;
        }
        return value;
    }

    public String GenerateMountBarText(float rawHealth, int health){
        return getSymbol("text.onebar.mountHealth",
                config.textSettings.rawHealth ?
                        getFraction(Math.round(rawHealth * 100.0) / 100.0) :
                        getFraction(health, false));
    }

    public String GenerateMountSpeechText(){
        String value = "";
        //Mount health, armor, jump distance, jump cooldown
        return value;
    }

    public String GenerateArmorSpeechText(){
        String value = "";
        //Armor global/individual, elytra, saturation
        return value;
    }

    public String GenerateExperienceSpeechText(){
        String value = "";

        if(playerProperties.xpLevel > 0){
            value += getSymbol("text.onebar.xpLevel", playerProperties.xpLevel);
        }
        if(playerProperties.xp > 0){
            value += getSymbol("text.onebar.xp", Calculations.calculatePercentage(playerProperties.xp, playerProperties.maxXp));
        }
        if(playerProperties.lapisLazuli > 0){
            value += getSymbol("text.onebar.lapisLazuli", playerProperties.lapisLazuli);
        }
        if(playerProperties.lapisLazuliMax > 0){
            value += getSymbol("text.onebar.lapisLazuliMax", playerProperties.lapisLazuliMax);
        }

        return value;
    }

    private boolean translationStringValid(String key){
        return !Objects.equals(Text.translatable(key).getString(), key);
    }

    private String getFraction(double number){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(number);
    }

    private String getFraction(int number){
        return getFraction(number, config.textSettings.estimatesItalic);
    }

    public String getFraction(int number, boolean isEffectEstimate){
        DecimalFormat df = new DecimalFormat(config.textSettings.useFractionsPadZeroes ? "0.0#" : "0.#");
        String result;

        if(config.textSettings.useFractions)
            result = df.format((float) number / 2);
        else
            result = String.valueOf(number);

        if(number < 0) // Make minus more aesthetic or differentiate in speech
            result = getSymbol("text.onebar.negative", result.replace("-", ""));

        if(isEffectEstimate && !useSpeech) // Use italic format for estimates
            result = "§o" + result + "§r";

        return result;
    }

    public String getSymbol(String stringKey){
        return getSymbol(stringKey, (Object) null);
    }

    public String getSymbol(String stringKey, Object... parameters){
        String suffix = "";

        if(useSpeech){
            suffix = ".speech";
        }
        else if(config.textSettings.extraSymbols && translationStringValid(stringKey + ".extra")){
            suffix = ".extra";
            if(config.textSettings.useEmoji && translationStringValid(stringKey + suffix + ".emoji"))
                suffix += ".emoji";
        }
        else if(config.textSettings.useEmoji && translationStringValid(stringKey + ".emoji")){
            suffix = ".emoji";
        }
        else if(!translationStringValid(stringKey) && !config.textSettings.extraSymbols) // If extras are turned off, output value only
            return String.valueOf(parameters[0]);

        return Text.translatable(stringKey + suffix, parameters).getString();
    }

    public String getMobHead(PlayerEntity playerEntity){
        Item headItem = playerEntity.getEquippedStack(EquipmentSlot.HEAD).getItem();

        if(headItem == Items.ZOMBIE_HEAD)
            return getSymbol("text.onebar.mobHeadZombie");
        else if(headItem == Items.SKELETON_SKULL)
            return getSymbol("text.onebar.mobHeadSkeleton");
        else if(headItem == Items.CREEPER_HEAD)
            return getSymbol("text.onebar.mobHeadCreeper");
        else if(headItem == Items.CARVED_PUMPKIN)
            return getSymbol("text.onebar.mobHeadEnderman");
        else
            return null;
    }
}
