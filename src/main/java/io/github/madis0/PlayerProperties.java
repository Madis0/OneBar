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

public class PlayerProperties {
    public float rawHealth;
    public float maxRawHealth;
    public int health;
    public int maxHealth;
    public int absorption;
    public boolean hasAbsorption;

    public int maxArmor;
    public int armor;

    public int maxHunger;
    public int hunger;
    public boolean hasHunger;
    public boolean isStarving;
    public float rawSaturation;
    public int saturation;

    public int maxRawAir;
    public int rawAir;
    public int air;
    public boolean isUnderwater;
    public boolean isDrowning;

    public boolean isSuffocating;

    public boolean isBurning;
    public boolean isBurningOnFire;
    public int burnSource;
    public int burningMultiplier;

    public int maxRawFreeze;
    public int rawFreeze;
    public int freeze;
    public boolean isFreezing;
    public boolean isGettingFreezeDamage;

    public int resistancePercent;
    public int regenerationHealth;
    public int poisonHealth;
    public int witherHealth;
    public boolean hasResistance;
    public boolean hasRegeneration;
    public boolean hasPoison;
    public boolean hasWither;
    public boolean hasFireResistance;
    public boolean hasWaterBreathing;

    public boolean hasHungerEffect;
    public int hungerEffectSaturationLoss;
    public int hungerEffectEstimate;
    public int previousHungerEffectEstimate;
    public int starvationHealthEstimate;

    public int naturalRegenerationAddition;
    public int naturalRegenerationHealth;
    public int previousNaturalRegenerationHealth;

    public int xpLevel;
    public int maxXp;
    public int xp;

    public PlayerProperties(){
        PlayerEntity playerEntity = MinecraftClient.getInstance().player;
        assert playerEntity != null;
        HungerManager hungerManager = playerEntity.getHungerManager();
        Difficulty difficulty = playerEntity.world.getDifficulty();

        // Player property calculations

        rawHealth = playerEntity.getHealth();
        maxRawHealth = playerEntity.getMaxHealth();
        health = MathHelper.ceil(rawHealth);
        maxHealth = MathHelper.ceil(maxRawHealth);
        absorption = MathHelper.ceil(playerEntity.getAbsorptionAmount());
        hasAbsorption = absorption > 0;

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
        isUnderwater =  playerEntity.isSubmergedInWater() || rawAir > 0;
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

        hasResistance = playerEntity.hasStatusEffect(StatusEffects.RESISTANCE);
        hasRegeneration = playerEntity.hasStatusEffect(StatusEffects.REGENERATION);
        hasPoison = playerEntity.hasStatusEffect(StatusEffects.POISON);
        hasWither = playerEntity.hasStatusEffect(StatusEffects.WITHER);
        hasFireResistance = playerEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
        hasWaterBreathing = playerEntity.hasStatusEffect(StatusEffects.WATER_BREATHING) || playerEntity.hasStatusEffect(StatusEffects.CONDUIT_POWER);
        hasHungerEffect = playerEntity.hasStatusEffect(StatusEffects.HUNGER) && !difficulty.equals(Difficulty.PEACEFUL);
    }
}
