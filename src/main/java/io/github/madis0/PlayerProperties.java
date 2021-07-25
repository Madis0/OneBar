package io.github.madis0;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;

import java.util.Objects;

public class PlayerProperties {
    public final boolean hasResistance;
    public final boolean hasRegeneration;
    public final boolean hasPoison;
    public final boolean hasWither;
    public final boolean hasFireResistance;
    public final boolean hasWaterBreathing;
    public final boolean hasHungerEffect;

    public final float rawHealth;
    public final float maxRawHealth;
    public final int health;
    public final int maxHealth;
    public final int absorption;
    public final boolean hasAbsorption;

    public final int maxArmor;
    public final int armor;

    public final int maxHunger;
    public final int foodLevel;
    public final int hunger;
    public final boolean hasHunger;
    public final boolean isStarving;
    public final float rawSaturation;
    public final int saturation;
    public final int saturationLoss;

    public final int maxRawAir;
    public final int rawAir;
    public final int air;
    public final boolean isUnderwater;
    public final boolean isDrowning;

    public final boolean isSuffocating;

    public final boolean isBurning;
    public final boolean isBurningOnFire;
    public static boolean isBurningOnSoulFire;
    public int burningMultiplier;

    public final int maxRawFreeze;
    public final int rawFreeze;
    public final int freeze;
    public final boolean isFreezing;
    public final boolean isGettingFreezeDamage;

    public int resistancePercent;
    public int regenerationHealth;
    public int poisonHealth;
    public int witherHealth;

    public int hungerEffectSaturationLoss;
    public int hungerEffectEstimate;
    public int previousHungerEffectEstimate;
    public int starvationHealthEstimate;

    public int naturalRegenerationAddition;
    public int naturalRegenerationHealth;
    public int previousNaturalRegenerationHealth;

    public final int xpLevel;
    public final int maxXp;
    public final int xp;

    public int heldFoodHunger;

    public PlayerProperties(){
        PlayerEntity playerEntity = MinecraftClient.getInstance().player;
        HungerManager hungerManager = Objects.requireNonNull(playerEntity).getHungerManager();
        Difficulty difficulty = playerEntity.world.getDifficulty();

        // Player property calculations
        hasResistance = playerEntity.hasStatusEffect(StatusEffects.RESISTANCE);
        hasRegeneration = playerEntity.hasStatusEffect(StatusEffects.REGENERATION);
        hasPoison = playerEntity.hasStatusEffect(StatusEffects.POISON);
        hasWither = playerEntity.hasStatusEffect(StatusEffects.WITHER);
        hasFireResistance = playerEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
        hasWaterBreathing = playerEntity.hasStatusEffect(StatusEffects.WATER_BREATHING) || playerEntity.hasStatusEffect(StatusEffects.CONDUIT_POWER);
        hasHungerEffect = playerEntity.hasStatusEffect(StatusEffects.HUNGER) && !difficulty.equals(Difficulty.PEACEFUL);

        rawHealth = playerEntity.getHealth();
        maxRawHealth = playerEntity.getMaxHealth();
        health = MathHelper.ceil(rawHealth);
        maxHealth = MathHelper.ceil(maxRawHealth);
        absorption = MathHelper.ceil(playerEntity.getAbsorptionAmount());
        hasAbsorption = absorption > 0;

        maxArmor = 20;
        armor = playerEntity.getArmor();

        maxHunger = 20;
        foodLevel = hungerManager.getFoodLevel();
        hunger = maxHunger - foodLevel;
        isStarving = hunger >= maxHunger;
        rawSaturation = hungerManager.getSaturationLevel();
        saturation = MathHelper.ceil(rawSaturation);
        saturationLoss = maxHunger - saturation;
        hasHunger = hunger > 0;


        maxRawAir = playerEntity.getMaxAir();
        rawAir = maxRawAir - playerEntity.getAir();
        air = Math.min(rawAir, maxRawAir) / 15;
        isUnderwater =  playerEntity.isSubmergedInWater() || rawAir > 0;
        isDrowning = rawAir >= maxRawAir;

        isSuffocating = playerEntity.isInsideWall();

        isBurning = playerEntity.doesRenderOnFire();
        int rawBurningSource = playerEntity.getFireTicks();

        // Reset soul fire state if burning state changes
        if(rawBurningSource != 1) isBurningOnSoulFire = false;

        if(rawBurningSource == -20) burningMultiplier = 1;
        if(rawBurningSource == 1) burningMultiplier = 2;
        if(rawBurningSource == 0 || isBurningOnSoulFire) burningMultiplier = 4;
        isBurningOnFire = (burningMultiplier == 2 || burningMultiplier == 4) && !hasFireResistance;

        maxRawFreeze = playerEntity.getMinFreezeDamageTicks();
        rawFreeze = playerEntity.getFrozenTicks();
        freeze = rawFreeze / 7;
        isFreezing = rawFreeze > 0;
        isGettingFreezeDamage = playerEntity.isFreezing() && !difficulty.equals(Difficulty.PEACEFUL);

        xpLevel = playerEntity.experienceLevel;
        maxXp = 183;
        xp = (int)(playerEntity.experienceProgress * maxXp);

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

        heldFoodHunger = 0;
        ItemStack heldItem = Objects.requireNonNull(playerEntity).getMainHandStack();
        if(!heldItem.isFood()) heldItem = playerEntity.getOffHandStack();

        if(heldItem.isFood()){
            FoodComponent itemFood = heldItem.getItem().getFoodComponent();
            heldFoodHunger = Objects.requireNonNull(itemFood).getHunger();
        }
    }

    public static void SetPlayerBurningOnSoulFire(boolean isBurning){
        isBurningOnSoulFire = isBurning;
    }
}
