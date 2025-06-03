package io.github.madis0;

import com.google.common.base.Predicates;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angriness;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;

import java.text.DecimalFormat;
import java.util.*;

public class PlayerProperties {
    Difficulty difficulty;

    public final boolean hasResistance;
    public final boolean hasRegeneration;
    public final boolean hasPoison;
    public final boolean hasWither;
    public final boolean hasFireResistance;
    public final boolean hasWaterBreathing;
    public final boolean hasHungerEffect;
    public final boolean hasBadOmen;
    public final boolean hasRaidOmen;
    public final boolean hasTrialOmen;
    public final boolean hasInvisibility;
    public final boolean hasGlowing;
    public final boolean hasWeaving;
    public final boolean hasOozing;
    public final boolean hasWindCharged;
    public final boolean hasInfested;

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

    public int helmetArmor;
    public int helmetMaxArmor;
    public float helmetMaxDurability;
    public int chestplateArmor;
    public int chestplateMaxArmor;
    public float chestplateMaxDurability;
    public int leggingsArmor;
    public int leggingsMaxArmor;
    public float leggingsMaxDurability;
    public int bootsArmor;
    public int bootsMaxArmor;
    public float bootsMaxDurability;

    public boolean hasAnyArmorItem;

    public boolean hasTotemOfUndying;
    public int amountTotemOfUndying;
    public boolean isHoldingTotemOfUndying;
    
    public boolean hasArrowsStuck;
    public static boolean locatorBarAvailable;
    public boolean isVisibleOnLocatorBar;

    public int elytraDurability;
    public int elytraMaxDurability;
    public boolean hasElytra;
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
    public final boolean isInWater;
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

    public int maxLevitationTimeRaw;
    public final int levitationTimeRaw;
    public final int maxLevitationTime;
    public final int levitationTime;
    public double levitationResultYRaw;
    public double levitationFallHeightRaw;
    public int levitationFallHeight;
    public boolean levitationFallHurts;
    public int levitationFallHealthEstimate;
    public double belowBlockYRaw;
    public int belowBlockY;
    public double normalFallHeightRaw;
    public String normalFallHeightDisplay;
    public boolean normalFallHurts;
    public int normalFallHealthEstimate;
    public final boolean hasLevitation;

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

    public int badOmenLevel;
    public int raidOmenLevel;
    public int trialOmenLevel;

    public float naturalRegenerationAddition;
    public float naturalRegenerationHealthRaw;
    public int naturalRegenerationHealth;
    public float previousNaturalRegenerationHealth;

    public final int xpLevel;
    public final int maxXp;
    public final int xp;

    public int lapisLazuli;
    public int lapisLazuliMax;

    public boolean isHoldingFood;
    public int heldFoodHunger;
    public int heldFoodHungerEstimate;
    public float heldFoodSaturation;
    public float heldFoodSaturationEstimateRaw;
    public float heldFoodHealthEstimateRaw;
    public int heldFoodHealthEstimate;

    public boolean isWardenNear;
    public boolean isWardenAngry;
    public int wardenDanger;
    public int maxWardenDanger;
    public int rawWardenDanger;
    public int rawMaxWardenDanger;

    public PlayerProperties(){
        PlayerEntity playerEntity = MinecraftClient.getInstance().player;
        HungerManager hungerManager = Objects.requireNonNull(playerEntity).getHungerManager();
        difficulty = playerEntity.getWorld().getDifficulty();

        // Player property calculations
        hasResistance = playerEntity.hasStatusEffect(StatusEffects.RESISTANCE);
        hasRegeneration = playerEntity.hasStatusEffect(StatusEffects.REGENERATION);
        hasPoison = playerEntity.hasStatusEffect(StatusEffects.POISON);
        hasWither = playerEntity.hasStatusEffect(StatusEffects.WITHER);
        hasFireResistance = playerEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
        hasWaterBreathing = playerEntity.hasStatusEffect(StatusEffects.WATER_BREATHING) || playerEntity.hasStatusEffect(StatusEffects.CONDUIT_POWER);
        hasHungerEffect = playerEntity.hasStatusEffect(StatusEffects.HUNGER) && !difficulty.equals(Difficulty.PEACEFUL);
        hasBadOmen = playerEntity.hasStatusEffect(StatusEffects.BAD_OMEN) && !difficulty.equals(Difficulty.PEACEFUL);
        hasRaidOmen = playerEntity.hasStatusEffect(StatusEffects.RAID_OMEN) && !difficulty.equals(Difficulty.PEACEFUL);
        hasTrialOmen = playerEntity.hasStatusEffect(StatusEffects.TRIAL_OMEN) && !difficulty.equals(Difficulty.PEACEFUL);
        hasInvisibility = playerEntity.hasStatusEffect(StatusEffects.INVISIBILITY);
        hasGlowing = playerEntity.hasStatusEffect(StatusEffects.GLOWING);
        hasWeaving = playerEntity.hasStatusEffect(StatusEffects.WEAVING);
        hasOozing = playerEntity.hasStatusEffect(StatusEffects.OOZING);
        hasWindCharged = playerEntity.hasStatusEffect(StatusEffects.WIND_CHARGED);
        hasInfested = playerEntity.hasStatusEffect(StatusEffects.INFESTED);
        hasLevitation = playerEntity.hasStatusEffect(StatusEffects.LEVITATION);

        healthRaw = playerEntity.getHealth();
        maxHealthRaw = playerEntity.getMaxHealth();
        health = MathHelper.ceil(healthRaw);
        maxHealth = MathHelper.ceil(maxHealthRaw);
        absorption = MathHelper.ceil(playerEntity.getAbsorptionAmount());
        hasAbsorption = absorption > 0;

        maxArmor = playerEntity.defaultMaxHealth;
        armor = playerEntity.getArmor();

        var playerArmorSlots = Arrays.stream(EquipmentSlot.values())
                .filter(slot -> slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR)
                .toArray(EquipmentSlot[]::new);

        for (EquipmentSlot slot : playerArmorSlots) {
            var armorStack = playerEntity.getEquippedStack(slot);
            if (!armorStack.isEmpty() && !armorStack.isOf(Items.ELYTRA)) {
                rawArmorDurability += armorStack.getMaxDamage() - armorStack.getDamage();
                rawMaxArmorDurability += armorStack.getMaxDamage();
            }
            else if (armorStack.isOf(Items.ELYTRA)) {
                hasElytra = true;
                elytraDurability = armorStack.getMaxDamage() - armorStack.getDamage();
                elytraMaxDurability = armorStack.getMaxDamage();
            }
        }

        maxArmorDurability = (float)armor; // Abstraction
        armorDurability = rawArmorDurability > 0 ? (((float)rawArmorDurability / rawMaxArmorDurability) * maxArmorDurability) : 0;

        helmetArmor = getArmorElementArmor(playerEntity, EquipmentSlot.HEAD);
        helmetMaxArmor = getArmorItemMaxArmor(Items.NETHERITE_HELMET);
        helmetMaxDurability = getArmorElementMaxDurability(playerEntity, EquipmentSlot.HEAD);

        chestplateArmor = getArmorElementArmor(playerEntity, EquipmentSlot.CHEST);
        chestplateMaxArmor = getArmorItemMaxArmor(Items.NETHERITE_CHESTPLATE);
        chestplateMaxDurability = getArmorElementMaxDurability(playerEntity, EquipmentSlot.CHEST);

        leggingsArmor = getArmorElementArmor(playerEntity, EquipmentSlot.LEGS);
        leggingsMaxArmor = getArmorItemMaxArmor(Items.NETHERITE_LEGGINGS);
        leggingsMaxDurability = getArmorElementMaxDurability(playerEntity, EquipmentSlot.LEGS);

        bootsArmor = getArmorElementArmor(playerEntity, EquipmentSlot.FEET);
        bootsMaxArmor = getArmorItemMaxArmor(Items.NETHERITE_BOOTS);
        bootsMaxDurability = getArmorElementMaxDurability(playerEntity, EquipmentSlot.FEET);

        hasAnyArmorItem = (playerEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() != Items.AIR ||
                           playerEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.AIR ||
                           playerEntity.getEquippedStack(EquipmentSlot.LEGS).getItem() != Items.AIR ||
                           playerEntity.getEquippedStack(EquipmentSlot.FEET).getItem() != Items.AIR ||
                           playerEntity.getEquippedStack(EquipmentSlot.OFFHAND).getItem() != Items.AIR);

        amountTotemOfUndying = playerEntity.getInventory().count(Items.TOTEM_OF_UNDYING);
        hasTotemOfUndying = amountTotemOfUndying > 0;
        isHoldingTotemOfUndying = (playerEntity.getEquippedStack(EquipmentSlot.MAINHAND).getItem() == Items.TOTEM_OF_UNDYING ||
                                   playerEntity.getEquippedStack(EquipmentSlot.OFFHAND).getItem() == Items.TOTEM_OF_UNDYING);

        hasArrowsStuck = playerEntity.getStuckArrowCount() > 0;

        isVisibleOnLocatorBar = locatorBarAvailable && !playerEntity.isSneaking() && !hasInvisibility && (playerEntity.getGameMode() != GameMode.SPECTATOR) &&
                !Set.of(Items.CREEPER_HEAD, Items.DRAGON_HEAD, Items.PIGLIN_HEAD, Items.PLAYER_HEAD, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.ZOMBIE_HEAD, Items.CARVED_PUMPKIN).contains(playerEntity.getEquippedStack(EquipmentSlot.HEAD).getItem());

        isFlyingWithElytra = playerEntity.isGliding();

        maxFoodLevel = playerEntity.defaultMaxHealth;
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
        air = Math.min(airRaw, maxAirRaw) / (int) Calculations.getPrettyDivisor(maxAirRaw, playerEntity.defaultMaxHealth);
        isInWater = playerEntity.isTouchingWater();
        isUnderwater =  playerEntity.isSubmergedInWater() || airRaw > 0;
        isDrowning = airRaw >= maxAirRaw;

        isSuffocating = playerEntity.isInsideWall();

        isBurning = playerEntity.doesRenderOnFire();

        int currentFireTicks = playerEntity.getFireTicks();

        final int NO_FIRE_TICKS = 0;
        final int BURNING_FIRE_TICKS = 160;
        final int IN_LAVA_FIRE_TICKS = 300;

        if (currentFireTicks != BURNING_FIRE_TICKS) {
            isBurningOnSoulFire = false;
        }

        if (currentFireTicks == NO_FIRE_TICKS && isBurning) {
            burningMultiplier = 1; // Burning in air.
        } else if (currentFireTicks == BURNING_FIRE_TICKS) {
            burningMultiplier = isBurningOnSoulFire ? 3 : 2; // 3 for soul fire, 2 for normal fire.
        } else if (currentFireTicks == IN_LAVA_FIRE_TICKS) {
            burningMultiplier = 4; // Burning in lava.
        }

        isBurningOnFire = isBurning && !hasFireResistance;

        maxFreezeRaw = playerEntity.getMinFreezeDamageTicks();
        freezeRaw = playerEntity.getFrozenTicks();
        freeze = freezeRaw / (int) Calculations.getPrettyDivisor(maxFreezeRaw, playerEntity.defaultMaxHealth);
        isFreezing = freezeRaw > 0;
        isGettingFreezeDamage = playerEntity.isFrozen() && !difficulty.equals(Difficulty.PEACEFUL);

        maxLevitationTimeRaw = 200;
        levitationTimeRaw = hasLevitation ? Math.max(Objects.requireNonNull(playerEntity.getStatusEffect(StatusEffects.LEVITATION)).getDuration(), 0) : 0;
        maxLevitationTime = maxLevitationTimeRaw / 20;
        levitationTime = levitationTimeRaw / (int) Calculations.getPrettyDivisor(maxLevitationTimeRaw, maxLevitationTime);

        belowBlockYRaw = playerEntity.getY();
        double y = (int)playerEntity.getY();
        double voidLimit = -128;
        BlockState state; //TODO: use material to determine fall damage
        if(playerEntity.getWorld() != null && (!playerEntity.isOnGround() || playerEntity.isSneaking())){
            var world = playerEntity.getWorld();
            var x = (int)playerEntity.getX();
            var z = (int)playerEntity.getZ();
            while((state = world.getBlockState(new BlockPos(x, (int)--y, z))).isAir() && y >= voidLimit) { //TODO: detect if block is solid and what height it has
                belowBlockYRaw = y;
            }
        }
        belowBlockY = (int) Math.round(belowBlockYRaw);

        if(playerEntity.hasStatusEffect(StatusEffects.LEVITATION)){
            var effect = playerEntity.getStatusEffect(StatusEffects.LEVITATION);
            var estHeight = (Objects.requireNonNull(effect).getAmplifier() + 1) * 0.9 * ((float) effect.getDuration() / 20);
            levitationResultYRaw = playerEntity.getY() + estHeight;
            levitationFallHeightRaw = getFallingHeightEstimate(playerEntity, levitationResultYRaw - belowBlockYRaw);

            levitationFallHeight = (int) Math.round(levitationFallHeightRaw); //Round to avoid excessive flickering
            levitationFallHurts = levitationFallHeight > 3;
            levitationFallHealthEstimate = getFallingHealthEstimate(healthRaw, levitationFallHeight, levitationFallHurts);
        }
        normalFallHeightRaw = getFallingHeightEstimate(playerEntity, playerEntity.getY() - belowBlockYRaw);

        normalFallHurts = normalFallHeightRaw > 3;
        normalFallHealthEstimate = getFallingHealthEstimate(healthRaw, normalFallHeightRaw, normalFallHurts);
        normalFallHeightDisplay = new DecimalFormat("0.#").format(normalFallHeightRaw);

        badOmenLevel = hasBadOmen ? Objects.requireNonNull(playerEntity.getStatusEffect(StatusEffects.BAD_OMEN)).getAmplifier() + 1: 0;
        raidOmenLevel = hasRaidOmen ? Objects.requireNonNull(playerEntity.getStatusEffect(StatusEffects.RAID_OMEN)).getAmplifier() + 1: 0;
        // 20 ticks * 60 sec * 15 min = one "level" of trial omen
        trialOmenLevel = hasTrialOmen ? (int)(Objects.requireNonNull(playerEntity.getStatusEffect(StatusEffects.TRIAL_OMEN)).getDuration() / (20 * 60 * 15)) + 1 : 0;

        xpLevel = playerEntity.experienceLevel;
        maxXp = 183; //renderExperienceBar @ InGameHud.class
        xp = (int)(playerEntity.experienceProgress * maxXp);
        lapisLazuli = playerEntity.getInventory().count(Items.LAPIS_LAZULI) +
                (playerEntity.getInventory().count(Items.LAPIS_BLOCK) * 9);

        MinecraftClient client = MinecraftClient.getInstance();
        if(client.currentScreen instanceof EnchantmentScreen){
            lapisLazuli += ((EnchantmentScreen) client.currentScreen).getScreenHandler().getLapisCount();
        }

        if (client.currentScreen instanceof HandledScreen<?>){
            var pickedUpItemInInventory = ((HandledScreen<?>) client.currentScreen).getScreenHandler().getCursorStack();
            if(pickedUpItemInInventory.isOf(Items.LAPIS_BLOCK)) lapisLazuli += pickedUpItemInInventory.getCount() * 9;
            if(pickedUpItemInInventory.isOf(Items.LAPIS_LAZULI)) lapisLazuli += pickedUpItemInInventory.getCount();
            if(pickedUpItemInInventory.isOf(Items.TOTEM_OF_UNDYING)) amountTotemOfUndying += pickedUpItemInInventory.getCount();
        }

        lapisLazuliMax = 0;

        int timesCanEnchant = (xpLevel - 27) / 3;
        int lapisCanEnchant = lapisLazuli / 3;
        if(timesCanEnchant > 0) lapisLazuliMax = Math.min(timesCanEnchant, lapisCanEnchant);

        // Potion effects

        StatusEffectInstance resistanceEffect = playerEntity.getStatusEffect(StatusEffects.RESISTANCE);
        resistancePercent = 0;
        if(resistanceEffect != null) resistancePercent = (resistanceEffect.getAmplifier() + 1) * 20;

        StatusEffectInstance regenerationEffect = playerEntity.getStatusEffect(StatusEffects.REGENERATION);
        regenerationHealthRaw = 0;
        if(regenerationEffect != null)
            regenerationHealthRaw = Calculations.getEstimatedHealthRegen(50,
                                                    regenerationEffect.getAmplifier(),
                                                    regenerationEffect.getDuration(),
                                                    healthRaw,
                                                    maxHealthRaw);
        regenerationHealth = MathHelper.ceil(regenerationHealthRaw);

        StatusEffectInstance poisonEffect = playerEntity.getStatusEffect(StatusEffects.POISON);
        poisonHealthRaw = maxHealthRaw;
        if(poisonEffect != null)
            poisonHealthRaw = Calculations.getEstimatedHealthDamage(25,
                                                    poisonEffect.getAmplifier(),
                                                    poisonEffect.getDuration(),
                                                    healthRaw,
                                                    1);
        poisonHealth = MathHelper.ceil(poisonHealthRaw);


        StatusEffectInstance witherEffect = playerEntity.getStatusEffect(StatusEffects.WITHER);
        witherHealthRaw = maxHealthRaw;
        if(witherEffect != null)
            witherHealthRaw = Calculations.getEstimatedHealthDamage(40,
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

            if (MathHelper.ceil(hungerRaw + hungerEffectSaturationLoss) != (MathHelper.ceil(previousHungerEffectEstimate) - 1)) {
                hungerEffectEstimateRaw = !hasSaturation ? Math.max(Math.min(hungerRaw + hungerEffectSaturationLoss, maxFoodLevelRaw), 0) : hungerRaw;
                previousHungerEffectEstimate = hungerEffectEstimateRaw;
            }
        }
        else {
            hungerEffectEstimateRaw = hungerRaw;
            previousHungerEffectEstimate = hungerEffectEstimateRaw;
        }
        hungerEffectEstimate = MathHelper.ceil(hungerEffectEstimateRaw);

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
                naturalRegenerationAddition = Calculations.getNaturalRegenAddition(saturationRaw, hungerRaw);
            else if(difficulty.equals(Difficulty.PEACEFUL))
                naturalRegenerationAddition = maxHealthRaw - healthRaw;

            if(MathHelper.ceil(health + naturalRegenerationAddition) != (MathHelper.ceil(previousNaturalRegenerationHealth) + 1)){
                naturalRegenerationHealthRaw = Math.min(healthRaw + naturalRegenerationAddition, maxHealthRaw);
                previousNaturalRegenerationHealth = naturalRegenerationHealth;
            }
        }
        else {
            naturalRegenerationHealthRaw = healthRaw;
            previousNaturalRegenerationHealth = naturalRegenerationHealthRaw;
        }
        naturalRegenerationHealth = MathHelper.ceil(naturalRegenerationHealthRaw);

        heldFoodHunger = 0;
        ItemStack heldFoodItem = Objects.requireNonNull(playerEntity).getMainHandStack();
        if(!heldFoodItem.getComponents().contains(DataComponentTypes.FOOD)) heldFoodItem = playerEntity.getOffHandStack();

        if(heldFoodItem.getComponents().contains(DataComponentTypes.FOOD)){
            isHoldingFood = true;
            FoodComponent itemFood = heldFoodItem.getItem().getComponents().get(DataComponentTypes.FOOD);
            heldFoodHunger = Objects.requireNonNull(itemFood).nutrition();
            heldFoodSaturation = Objects.requireNonNull(itemFood).saturation() * heldFoodHunger * 2.0F; // See HungerManager -> add() for more info
        }
        else {
            isHoldingFood = false;
        }

        heldFoodHungerEstimate = hunger - heldFoodHunger;

        if (isHoldingFood && hasHunger){
            heldFoodHealthEstimateRaw = !difficulty.equals(Difficulty.PEACEFUL) ? Math.min(healthRaw + Calculations.getNaturalRegenAddition(saturationRaw + extraRegenFoodLevel + heldFoodSaturation, hungerRaw), maxFoodLevelRaw) : maxHealthRaw - healthRaw;
            heldFoodSaturationEstimateRaw = Math.max(heldFoodSaturation - (maxHealthRaw - healthRaw) - extraRegenFoodLevel, 0);
        }
        else {
            heldFoodHealthEstimateRaw = 0;
            heldFoodSaturationEstimateRaw = 0;
        }
        heldFoodHealthEstimate = MathHelper.ceil(heldFoodHealthEstimateRaw);

        rawMaxWardenDanger = 149;
        maxWardenDanger = 20;
        rawWardenDanger = 0;
        wardenDanger = 0;
        isWardenNear = false;
        isWardenAngry = false;

        WardenEntity warden = getClosestWarden(playerEntity);

        if(warden != null){
            isWardenNear = true;
            rawWardenDanger = warden.getAnger();
            isWardenAngry = rawWardenDanger > Angriness.ANGRY.getThreshold();
            wardenDanger = (int) (rawWardenDanger / Calculations.getPrettyDivisor(rawMaxWardenDanger, playerEntity.defaultMaxHealth));
        }
    }

    public static void setPlayerBurningOnSoulFire(boolean isBurning){
        isBurningOnSoulFire = isBurning;
    }

    public static void setLocatorBarEnabled(boolean isEnabled){
        locatorBarAvailable = isEnabled;
    }

    private double getFallingHeightEstimate(PlayerEntity playerEntity, double height){
        //TODO: not precise enough
        /*
        ItemStack[] armorTypes = {
                playerEntity.getEquippedStack(EquipmentSlot.HEAD),
                playerEntity.getEquippedStack(EquipmentSlot.CHEST),
                playerEntity.getEquippedStack(EquipmentSlot.LEGS),
                playerEntity.getEquippedStack(EquipmentSlot.FEET)
        };

        for (var piece : armorTypes) {
            height = (height * (1 - (4 * ((double)EnchantmentHelper.getLevel(Enchantments.PROTECTION, piece) / 100))));
            height = (height * (1 - (12 * ((double)EnchantmentHelper.getLevel(Enchantments.FEATHER_FALLING, piece) / 100))));
        }
        */
        // TODO: figure out fall speed in ticks and compare it with effect duration
        /*
        if(playerEntity.hasStatusEffect(StatusEffects.JUMP_BOOST))
            height = height - (playerEntity.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier() + 1);
        if(playerEntity.hasStatusEffect(StatusEffects.RESISTANCE))
            height = (height * (1 - ((float)resistancePercent / 100)));
        if(playerEntity.hasStatusEffect(StatusEffects.SLOW_FALLING)) height = 0;
        */
        return height;
    }

    private int getFallingHealthEstimate(float health, double height, boolean hurts){
        double value = hurts ? (health - Math.max(0, height - 3)) : health;
        if(value == 0) value = 1; // Fatal height is always +0.5 blocks of the estimate
        if(value <= -1) value = 0;
        return MathHelper.ceil(value);
    }

    private WardenEntity getClosestWarden(PlayerEntity player){
        // A warden is aware of all targetable entities within a 49×51×49 box around itself. https://minecraft.wiki/w/Warden#Behavior
        Box boundingBox = player.getBoundingBox().expand(49, 51, 49);
        List<WardenEntity> nearbyWardens = player.getWorld().getEntitiesByType(
                TypeFilter.instanceOf(WardenEntity.class),
                boundingBox,
                Predicates.alwaysTrue()
        );

        return nearbyWardens.stream().min(Comparator.comparingDouble(e ->
               Calculations.getDistance(player.getX(), player.getY(), player.getZ(), e.getX(), e.getY(), e.getZ()))).orElse(null);
    }

    public static int getProtectionFromArmor(ItemStack armorItem) {
        AttributeModifiersComponent attributeModifierComponent = armorItem.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (attributeModifierComponent == null)
            return 0;
        RegistryKey<EntityAttribute> ARMOR = EntityAttributes.ARMOR.getKey().get();

        return attributeModifierComponent.modifiers().stream()
                .filter(entry -> entry.attribute().matchesKey(ARMOR))
                .mapToInt(entry -> (int) entry.modifier().value())
                .findFirst()
                .orElse(0);
    }


    private int getArmorElementArmor(PlayerEntity playerEntity, EquipmentSlot slot) {
        return getProtectionFromArmor(playerEntity.getEquippedStack(slot));
    }

    private int getArmorItemMaxArmor(Item armorItem) {
        return getProtectionFromArmor(new ItemStack(armorItem));
    }

    public float getArmorElementDurability(PlayerEntity playerEntity, EquipmentSlot slot, float maxLimit){
        ItemStack armorItem = playerEntity.getEquippedStack(slot);
        if (armorItem != ItemStack.EMPTY){
            var rawArmorDurability = armorItem.getMaxDamage() - armorItem.getDamage();
            var rawMaxArmorDurability = armorItem.getMaxDamage();
            return rawArmorDurability > 0 ? (((float)rawArmorDurability / rawMaxArmorDurability) * maxLimit) : 0;
        }
        return (float)0;
    }

    private float getArmorElementMaxDurability(PlayerEntity playerEntity, EquipmentSlot slot){
        ItemStack armorItem = playerEntity.getEquippedStack(slot);
        if (armorItem != ItemStack.EMPTY)
            return (float)armorItem.getMaxDamage();
        return (float)0;
    }

    public static String getMobHead(PlayerEntity playerEntity){
        Item headItem = playerEntity.getEquippedStack(EquipmentSlot.HEAD).getItem();
        boolean hasPiglinDeterArmorItem = (playerEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() == Items.GOLDEN_HELMET ||
                playerEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() == Items.PIGLIN_HEAD ||
                playerEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.GOLDEN_CHESTPLATE ||
                playerEntity.getEquippedStack(EquipmentSlot.LEGS).getItem() == Items.GOLDEN_LEGGINGS ||
                playerEntity.getEquippedStack(EquipmentSlot.FEET).getItem() == Items.GOLDEN_BOOTS);

        if(headItem == Items.ZOMBIE_HEAD)
            return Calculations.emojiOrText("text.onebar.mobHeadZombieEmoji","text.onebar.mobHeadZombie", false, (Object) null);
        else if(headItem == Items.SKELETON_SKULL)
            return Calculations.emojiOrText("text.onebar.mobHeadSkeletonEmoji","text.onebar.mobHeadSkeleton", false, (Object) null);
        else if(headItem == Items.CREEPER_HEAD)
            return Calculations.emojiOrText("text.onebar.mobHeadCreeperEmoji","text.onebar.mobHeadCreeper", false, (Object) null);
        else if(hasPiglinDeterArmorItem)
            return Calculations.emojiOrText("text.onebar.mobHeadPiglinEmoji","text.onebar.mobHeadPiglin", false, (Object) null);
        else if(headItem == Items.CARVED_PUMPKIN)
            return Calculations.emojiOrText("text.onebar.mobHeadEndermanEmoji","text.onebar.mobHeadEnderman", false, (Object) null);
        else if(PlayerProperties.locatorBarAvailable && (headItem == Items.PLAYER_HEAD || headItem == Items.DRAGON_HEAD || headItem == Items.WITHER_SKELETON_SKULL))
            return Calculations.emojiOrText("text.onebar.mobHeadLocatorEmoji","text.onebar.mobHeadLocator", false, (Object) null);
        else
            return null;
    }
}
