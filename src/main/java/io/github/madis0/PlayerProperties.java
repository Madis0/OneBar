package io.github.madis0;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundInstanceListener;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;

import java.util.Objects;

public class PlayerProperties {
    Difficulty difficulty;

    public final boolean hasResistance;
    public final boolean hasRegeneration;
    public final boolean hasPoison;
    public final boolean hasWither;
    public final boolean hasFireResistance;
    public final boolean hasWaterBreathing;
    public final boolean hasHungerEffect;

    public final float healthRaw;
    public final int health;
    public final float maxHealthRaw;
    public final int maxHealth;
    public final int absorption;
    public final boolean hasAbsorption;

    public final int maxArmor;
    public final int armor;
    public int rawMaxArmorDurability;
    public int rawArmorDurability;
    public final float maxArmorDurability;
    public final float armorDurability;
    public int elytraDurability;
    public int elytraMaxDurability;
    public final boolean isFlyingWithElytra;

    public final int maxFoodLevel;
    public final float maxFoodLevelRaw;
    public final int foodLevel;
    public final int hunger;
    public final float hungerRaw;
    public final boolean hasHunger;
    public final boolean isStarving;
    public final float saturationRaw;
    public final int saturation;
    public float extraRegenFoodLevel;
    public final int saturationLoss;
    public final boolean hasSaturation;

    public final int maxAirRaw;
    public final int airRaw;
    public final int air;
    public final boolean isUnderwater;
    public final boolean isDrowning;

    public final boolean isSuffocating;

    public final boolean isBurning;
    public final boolean isBurningOnFire;
    public static boolean isBurningOnSoulFire;
    public int burningMultiplier;

    public final int maxFreezeRaw;
    public final int freezeRaw;
    public final int freeze;
    public final boolean isFreezing;
    public final boolean isGettingFreezeDamage;

    public int resistancePercent;
    public float regenerationHealthRaw;
    public int regenerationHealth;
    public float poisonHealthRaw;
    public int poisonHealth;
    public float witherHealthRaw;
    public int witherHealth;

    public float hungerEffectSaturationLoss;
    public int hungerEffectEstimate;
    public float hungerEffectEstimateRaw;
    public float previousHungerEffectEstimate;
    public int starvationHealthEstimate;

    public float naturalRegenerationAddition;
    public float naturalRegenerationHealthRaw;
    public int naturalRegenerationHealth;
    public float previousNaturalRegenerationHealth;

    public final int xpLevel;
    public final int maxXp;
    public final int xp;

    public boolean isHoldingFood;
    public int heldFoodHunger;
    public int heldFoodHungerEstimate;
    public float heldFoodSaturation;
    public float heldFoodSaturationEstimateRaw;
    public float heldFoodHealthEstimateRaw;
    public int heldFoodHealthEstimate;

    public int wardenDanger;
    public int maxWardenDanger;
    public int rawWardenDanger;
    public int rawMaxWardenDanger;

    public PlayerProperties(){
        PlayerEntity playerEntity = MinecraftClient.getInstance().player;
        HungerManager hungerManager = Objects.requireNonNull(playerEntity).getHungerManager();
        difficulty = playerEntity.world.getDifficulty();

        // Player property calculations
        hasResistance = playerEntity.hasStatusEffect(StatusEffects.RESISTANCE);
        hasRegeneration = playerEntity.hasStatusEffect(StatusEffects.REGENERATION);
        hasPoison = playerEntity.hasStatusEffect(StatusEffects.POISON);
        hasWither = playerEntity.hasStatusEffect(StatusEffects.WITHER);
        hasFireResistance = playerEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
        hasWaterBreathing = playerEntity.hasStatusEffect(StatusEffects.WATER_BREATHING) || playerEntity.hasStatusEffect(StatusEffects.CONDUIT_POWER);
        hasHungerEffect = playerEntity.hasStatusEffect(StatusEffects.HUNGER) && !difficulty.equals(Difficulty.PEACEFUL);

        healthRaw = playerEntity.getHealth();
        maxHealthRaw = playerEntity.getMaxHealth();
        health = MathHelper.ceil(healthRaw);
        maxHealth = MathHelper.ceil(maxHealthRaw);
        absorption = MathHelper.ceil(playerEntity.getAbsorptionAmount());
        hasAbsorption = absorption > 0;

        maxArmor = 20;
        armor = playerEntity.getArmor();
        for (ItemStack armorItem : playerEntity.getArmorItems()) {
            if(!armorItem.isOf(Items.ELYTRA))
                rawArmorDurability += armorItem.getMaxDamage() - armorItem.getDamage();
        }
        for (ItemStack armorItem : playerEntity.getArmorItems()) {
            if(!armorItem.isOf(Items.ELYTRA))
                rawMaxArmorDurability += armorItem.getMaxDamage();
        }
        maxArmorDurability = (float)armor; // Abstraction
        armorDurability = rawArmorDurability > 0 ? (((float)rawArmorDurability / rawMaxArmorDurability) * maxArmorDurability) : 0;

        ItemStack chestItem = playerEntity.getEquippedStack(EquipmentSlot.CHEST);
        if (chestItem.isOf(Items.ELYTRA) && ElytraItem.isUsable(chestItem)) {
            elytraDurability = chestItem.getMaxDamage() - chestItem.getDamage();
            elytraMaxDurability = chestItem.getMaxDamage();
        }
        isFlyingWithElytra = playerEntity.isFallFlying();

        maxFoodLevel = 20;
        maxFoodLevelRaw = (float)maxFoodLevel; // Used for saturation calculations
        foodLevel = hungerManager.getFoodLevel();
        hunger = maxFoodLevel - foodLevel;
        hungerRaw = (float)hunger;
        hasHunger = hunger > 0;
        isStarving = hunger >= maxFoodLevel;
        saturationRaw = hungerManager.getSaturationLevel();
        saturation = MathHelper.ceil(saturationRaw);
        extraRegenFoodLevel = hunger < 3 ? hunger : 0;
        saturationLoss = maxFoodLevel - saturation;
        hasSaturation = saturationRaw > 0;

        maxAirRaw = playerEntity.getMaxAir();
        airRaw = maxAirRaw - playerEntity.getAir();
        air = Math.min(airRaw, maxAirRaw) / 15;
        isUnderwater =  playerEntity.isSubmergedInWater() || airRaw > 0;
        isDrowning = airRaw >= maxAirRaw;

        isSuffocating = playerEntity.isInsideWall();

        isBurning = playerEntity.doesRenderOnFire();
        int rawBurningSource = playerEntity.getFireTicks();

        // Reset soul fire state if burning state changes
        if(rawBurningSource != 1) isBurningOnSoulFire = false;

        if(rawBurningSource == -20) burningMultiplier = 1;
        if(rawBurningSource == 1) burningMultiplier = 2;
        if(rawBurningSource == 0 || isBurningOnSoulFire) burningMultiplier = 4;
        isBurningOnFire = (burningMultiplier == 2 || burningMultiplier == 4) && !hasFireResistance;

        maxFreezeRaw = playerEntity.getMinFreezeDamageTicks();
        freezeRaw = playerEntity.getFrozenTicks();
        freeze = freezeRaw / 7;
        isFreezing = freezeRaw > 0;
        isGettingFreezeDamage = playerEntity.isFreezing() && !difficulty.equals(Difficulty.PEACEFUL);

        xpLevel = playerEntity.experienceLevel;
        maxXp = 183;
        xp = (int)(playerEntity.experienceProgress * maxXp);

        // Potion effects

        StatusEffectInstance resistanceEffect = playerEntity.getStatusEffect(StatusEffects.RESISTANCE);
        resistancePercent = 0;
        if(resistanceEffect != null) resistancePercent = (resistanceEffect.getAmplifier() + 1) * 20;

        StatusEffectInstance regenerationEffect = playerEntity.getStatusEffect(StatusEffects.REGENERATION);
        regenerationHealthRaw = 0;
        if(regenerationEffect != null)
            regenerationHealthRaw = Calculations.GetEstimatedHealthRegen(50,
                                                    regenerationEffect.getAmplifier(),
                                                    regenerationEffect.getDuration(),
                                                    healthRaw,
                                                    maxHealthRaw);
        regenerationHealth = MathHelper.ceil(regenerationHealthRaw);

        StatusEffectInstance poisonEffect = playerEntity.getStatusEffect(StatusEffects.POISON);
        poisonHealthRaw = maxHealthRaw;
        if(poisonEffect != null)
            poisonHealthRaw = Calculations.GetEstimatedHealthDamage(25,
                                                    poisonEffect.getAmplifier(),
                                                    poisonEffect.getDuration(),
                                                    healthRaw,
                                                    1);
        poisonHealth = MathHelper.ceil(poisonHealthRaw);


        StatusEffectInstance witherEffect = playerEntity.getStatusEffect(StatusEffects.WITHER);
        witherHealthRaw = maxHealthRaw;
        if(witherEffect != null)
            witherHealthRaw = Calculations.GetEstimatedHealthDamage(40,
                                                    witherEffect.getAmplifier(),
                                                    witherEffect.getDuration(),
                                                    healthRaw,
                                                    0);
        witherHealth = MathHelper.ceil(witherHealthRaw);

        StatusEffectInstance hungerEffect = playerEntity.getStatusEffect(StatusEffects.HUNGER);
        hungerEffectSaturationLoss = 0;
        if(hungerEffect != null) {
            int duration = hungerEffect.getDuration();
            float hungerEffectExhaustionLoss = 0.005F * (float)(hungerEffect.getAmplifier() + 1) * duration;
            hungerEffectSaturationLoss = hungerEffectExhaustionLoss / (float)4;

            if (Math.ceil(hungerRaw + hungerEffectSaturationLoss) != (Math.ceil(previousHungerEffectEstimate) - 1)) {
                hungerEffectEstimateRaw = !hasSaturation ? Math.max(Math.min(hungerRaw + hungerEffectSaturationLoss, maxFoodLevelRaw), 0) : hungerRaw;
                previousHungerEffectEstimate = hungerEffectEstimateRaw;
            }
        }
        else {
            hungerEffectEstimateRaw = hungerRaw;
            previousHungerEffectEstimate = hungerEffectEstimateRaw;
        }
        hungerEffectEstimate = (int) Math.ceil(hungerEffectEstimateRaw);

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
            if (hunger < 3 && !difficulty.equals(Difficulty.PEACEFUL))
                naturalRegenerationAddition = Calculations.GetNaturalRegenAddition(saturationRaw, hungerRaw);
            else if(difficulty.equals(Difficulty.PEACEFUL))
                naturalRegenerationAddition = maxHealthRaw - healthRaw;

            if(Math.ceil(health + naturalRegenerationAddition) != (Math.ceil(previousNaturalRegenerationHealth) + 1)){
                naturalRegenerationHealthRaw = Math.min(healthRaw + naturalRegenerationAddition, maxHealthRaw);
                previousNaturalRegenerationHealth = naturalRegenerationHealth;
            }
        }
        else {
            naturalRegenerationHealthRaw = healthRaw;
            previousNaturalRegenerationHealth = naturalRegenerationHealthRaw;
        }
        naturalRegenerationHealth = (int) Math.ceil(naturalRegenerationHealthRaw);

        heldFoodHunger = 0;
        ItemStack heldItem = Objects.requireNonNull(playerEntity).getMainHandStack();
        if(!heldItem.isFood()) heldItem = playerEntity.getOffHandStack();

        if(heldItem.isFood()){
            isHoldingFood = true;
            FoodComponent itemFood = heldItem.getItem().getFoodComponent();
            heldFoodHunger = Objects.requireNonNull(itemFood).getHunger();
            heldFoodSaturation = Objects.requireNonNull(itemFood).getSaturationModifier() * heldFoodHunger * 2.0F; // See HungerManager -> add() for more info
        }
        else {
            isHoldingFood = false;
        }

        heldFoodHungerEstimate = hunger - heldFoodHunger;

        if (isHoldingFood && hasHunger){
            heldFoodHealthEstimateRaw = !difficulty.equals(Difficulty.PEACEFUL) ? Math.min(healthRaw + Calculations.GetNaturalRegenAddition(saturationRaw + extraRegenFoodLevel + heldFoodSaturation, hungerRaw), maxFoodLevelRaw) : maxHealthRaw - healthRaw;
            heldFoodSaturationEstimateRaw = Math.max(heldFoodSaturation - (maxHealthRaw - healthRaw) - extraRegenFoodLevel, 0);
        }
        else {
            heldFoodHealthEstimateRaw = 0;
            heldFoodSaturationEstimateRaw = 0;
        }
        heldFoodHealthEstimate = (int) Math.ceil(heldFoodHealthEstimateRaw);

        rawMaxWardenDanger = 620; //31 sec in ticks
        maxWardenDanger = 20;

        if(playerEntity.hasStatusEffect(StatusEffects.DARKNESS)){
            rawWardenDanger = playerEntity.getStatusEffect(StatusEffects.DARKNESS).getDuration();
            wardenDanger = rawWardenDanger / 31;
        }

        var aa = MinecraftClient.getInstance().getSoundManager();
    }

    public static void SetPlayerBurningOnSoulFire(boolean isBurning){
        isBurningOnSoulFire = isBurning;
    }
}
