package io.github.madis0;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Stream;

public class PlayerProperties {
    Difficulty difficulty;

    public static boolean isCreativeOrSpectator = false;

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

    public boolean hasElytra;
    public final boolean isFlyingWithElytra;
    public int elytraDurability;
    public int elytraMaxDurability;

    public boolean isMendingAnything;

    public boolean usesShield;
    public int shieldDurability;
    public int shieldMaxDurability;
    public float shieldAxedCooldown;
    public float shieldAxedMaxCooldown;
    public float shieldRaiseTicksRemaining;
    public float shieldMaxRaiseTicks;

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
    public int raidOmenWaves;
    public String trialOmenMinutes;

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
        Player playerEntity = Minecraft.getInstance().player;
        FoodData hungerManager = Objects.requireNonNull(playerEntity).getFoodData();
        difficulty = playerEntity.level().getDifficulty();

        // Player property calculations
        isCreativeOrSpectator = playerEntity.isCreative() || playerEntity.isSpectator();

        hasResistance = playerEntity.hasEffect(MobEffects.RESISTANCE);
        hasRegeneration = playerEntity.hasEffect(MobEffects.REGENERATION);
        hasPoison = playerEntity.hasEffect(MobEffects.POISON);
        hasWither = playerEntity.hasEffect(MobEffects.WITHER);
        hasFireResistance = playerEntity.hasEffect(MobEffects.FIRE_RESISTANCE);
        hasWaterBreathing = playerEntity.hasEffect(MobEffects.WATER_BREATHING) || playerEntity.hasEffect(MobEffects.CONDUIT_POWER);
        hasHungerEffect = playerEntity.hasEffect(MobEffects.HUNGER) && !difficulty.equals(Difficulty.PEACEFUL);
        hasBadOmen = playerEntity.hasEffect(MobEffects.BAD_OMEN) && !difficulty.equals(Difficulty.PEACEFUL);
        hasRaidOmen = playerEntity.hasEffect(MobEffects.RAID_OMEN) && !difficulty.equals(Difficulty.PEACEFUL);
        hasTrialOmen = playerEntity.hasEffect(MobEffects.TRIAL_OMEN) && !difficulty.equals(Difficulty.PEACEFUL);
        hasInvisibility = playerEntity.hasEffect(MobEffects.INVISIBILITY);
        hasGlowing = playerEntity.hasEffect(MobEffects.GLOWING);
        hasWeaving = playerEntity.hasEffect(MobEffects.WEAVING);
        hasOozing = playerEntity.hasEffect(MobEffects.OOZING);
        hasWindCharged = playerEntity.hasEffect(MobEffects.WIND_CHARGED);
        hasInfested = playerEntity.hasEffect(MobEffects.INFESTED);
        hasLevitation = playerEntity.hasEffect(MobEffects.LEVITATION);

        healthRaw = playerEntity.getHealth();
        maxHealthRaw = playerEntity.getMaxHealth();
        health = Mth.ceil(healthRaw);
        maxHealth = Mth.ceil(maxHealthRaw);
        absorption = Mth.ceil(playerEntity.getAbsorptionAmount());
        hasAbsorption = absorption > 0;

        maxArmor = playerEntity.invulnerableDuration;
        armor = playerEntity.getArmorValue();

        var playerArmorSlots = Arrays.stream(EquipmentSlot.values())
                .filter(slot -> slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR)
                .toArray(EquipmentSlot[]::new);

        for (EquipmentSlot slot : playerArmorSlots) {
            var armorStack = playerEntity.getItemBySlot(slot);
            if (!armorStack.isEmpty() && !armorStack.is(Items.ELYTRA)) {
                rawArmorDurability += armorStack.getMaxDamage() - armorStack.getDamageValue();
                rawMaxArmorDurability += armorStack.getMaxDamage();
            }
            else if (armorStack.is(Items.ELYTRA)) {
                hasElytra = true;
                elytraDurability = armorStack.getMaxDamage() - armorStack.getDamageValue();
                elytraMaxDurability = armorStack.getMaxDamage();
            }
        }

        ItemStack shieldStack = playerEntity.getMainHandItem().is(Items.SHIELD)
                ? playerEntity.getMainHandItem()
                : playerEntity.getOffhandItem().is(Items.SHIELD)
                ? playerEntity.getOffhandItem()
                : ItemStack.EMPTY;

        boolean hasShield = !shieldStack.isEmpty();
        shieldMaxDurability = hasShield ? shieldStack.getMaxDamage() : 0;
        shieldDurability = hasShield ? shieldMaxDurability - shieldStack.getDamageValue() : 0;
        usesShield = hasShield && playerEntity.isBlocking();

        shieldAxedCooldown = hasShield
                ? playerEntity.getCooldowns().getCooldownPercent(shieldStack, 0.0F)
                : 0.0F;
        shieldAxedMaxCooldown = 1;

        shieldMaxRaiseTicks = hasShield ? Objects.requireNonNull(shieldStack.get(DataComponents.BLOCKS_ATTACKS)).blockDelayTicks() : 0;
        shieldRaiseTicksRemaining = hasShield && playerEntity.isUsingItem()
                && playerEntity.getUseItem() == shieldStack ?
                Math.max(0, shieldMaxRaiseTicks - (shieldStack.getUseDuration(playerEntity) - playerEntity.getUseItemRemainingTicks()))
                : 0;

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

        hasAnyArmorItem = (playerEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() != Items.AIR ||
                           playerEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() != Items.AIR ||
                           playerEntity.getItemBySlot(EquipmentSlot.LEGS).getItem() != Items.AIR ||
                           playerEntity.getItemBySlot(EquipmentSlot.FEET).getItem() != Items.AIR ||
                           playerEntity.getItemBySlot(EquipmentSlot.OFFHAND).getItem() != Items.AIR);

        amountTotemOfUndying = playerEntity.getInventory().countItem(Items.TOTEM_OF_UNDYING);
        hasTotemOfUndying = amountTotemOfUndying > 0;
        isHoldingTotemOfUndying = playerEntity.getMainHandItem().is(Items.TOTEM_OF_UNDYING) ||
                                  playerEntity.getOffhandItem().is(Items.TOTEM_OF_UNDYING);

        hasArrowsStuck = playerEntity.getArrowCount() > 0;

        isVisibleOnLocatorBar = locatorBarAvailable && !playerEntity.isShiftKeyDown() && !hasInvisibility &&
                !Set.of(Items.CREEPER_HEAD, Items.DRAGON_HEAD, Items.PIGLIN_HEAD, Items.PLAYER_HEAD, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.ZOMBIE_HEAD, Items.CARVED_PUMPKIN).contains(playerEntity.getItemBySlot(EquipmentSlot.HEAD).getItem());

        isFlyingWithElytra = playerEntity.isFallFlying();

        Holder<Enchantment> mendingEntry = playerEntity.level()
                .registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(ResourceLocation.withDefaultNamespace("mending"))
                .orElseThrow();

        isMendingAnything = Stream.concat(
                        Stream.of(playerEntity.getMainHandItem(), playerEntity.getOffhandItem()),
                        Arrays.stream(playerArmorSlots).map(playerEntity::getItemBySlot)
                )
                .anyMatch(stack -> EnchantmentHelper.getItemEnchantmentLevel(mendingEntry, stack) > 0 && stack.isDamaged());

        maxFoodLevel = playerEntity.invulnerableDuration;
        maxFoodLevelRaw = (float)maxFoodLevel; // Used for saturation calculations
        foodLevel = hungerManager.getFoodLevel();
        hunger = maxFoodLevel - foodLevel;
        hungerRaw = (float)hunger;
        hasHunger = hunger > 0;
        isStarving = hunger >= maxFoodLevel;
        saturationRaw = hungerManager.getSaturationLevel();
        saturation = Mth.ceil(saturationRaw);
        extraRegenFoodLevel = hunger < 3 ? hunger : 0;
        saturationLoss = maxFoodLevel - saturation;
        hasSaturation = saturationRaw > 0;

        maxAirRaw = playerEntity.getMaxAirSupply();
        airRaw = maxAirRaw - playerEntity.getAirSupply();
        air = Math.min(airRaw, maxAirRaw) / (int) Calculations.getPrettyDivisor(maxAirRaw, playerEntity.invulnerableDuration);
        isInWater = playerEntity.isInWater();
        isUnderwater =  playerEntity.isUnderWater() || airRaw > 0;
        isDrowning = airRaw >= maxAirRaw;

        isSuffocating = playerEntity.isInWall();

        isBurning = playerEntity.displayFireAnimation();

        int currentFireTicks = playerEntity.getRemainingFireTicks();

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

        maxFreezeRaw = playerEntity.getTicksRequiredToFreeze();
        freezeRaw = playerEntity.getTicksFrozen();
        freeze = freezeRaw / (int) Calculations.getPrettyDivisor(maxFreezeRaw, playerEntity.invulnerableDuration);
        isFreezing = freezeRaw > 0;
        isGettingFreezeDamage = playerEntity.isFullyFrozen() && !difficulty.equals(Difficulty.PEACEFUL);

        maxLevitationTimeRaw = 200;
        levitationTimeRaw = hasLevitation ? Math.max(Objects.requireNonNull(playerEntity.getEffect(MobEffects.LEVITATION)).getDuration(), 0) : 0;
        maxLevitationTime = maxLevitationTimeRaw / 20;
        levitationTime = levitationTimeRaw / (int) Calculations.getPrettyDivisor(maxLevitationTimeRaw, maxLevitationTime);

        belowBlockYRaw = playerEntity.getY();
        double y = (int)playerEntity.getY();
        double voidLimit = -128;
        BlockState state; //TODO: use material to determine fall damage
        if(playerEntity.level() != null && (!playerEntity.onGround() || playerEntity.isShiftKeyDown())){
            var world = playerEntity.level();
            var x = (int)playerEntity.getX();
            var z = (int)playerEntity.getZ();
            while((state = world.getBlockState(new BlockPos(x, (int)--y, z))).isAir() && y >= voidLimit) { //TODO: detect if block is solid and what height it has
                belowBlockYRaw = y;
            }
        }
        belowBlockY = (int) Math.round(belowBlockYRaw);

        if(playerEntity.hasEffect(MobEffects.LEVITATION)){
            var effect = playerEntity.getEffect(MobEffects.LEVITATION);
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

        badOmenLevel = hasBadOmen ? Objects.requireNonNull(playerEntity.getEffect(MobEffects.BAD_OMEN)).getAmplifier() + 1: 0;
        raidOmenWaves = calculateRaidWaves(playerEntity);
        trialOmenMinutes = getTrialOmenTimeString(playerEntity);

        xpLevel = playerEntity.experienceLevel;
        maxXp = 183; //renderExperienceBar @ InGameHud.class
        xp = (int)(playerEntity.experienceProgress * maxXp);
        lapisLazuli = playerEntity.getInventory().countItem(Items.LAPIS_LAZULI) +
                (playerEntity.getInventory().countItem(Items.LAPIS_BLOCK) * 9);

        Minecraft client = Minecraft.getInstance();
        if(client.screen instanceof EnchantmentScreen){
            lapisLazuli += ((EnchantmentScreen) client.screen).getMenu().getGoldCount();
        }

        if (client.screen instanceof AbstractContainerScreen<?>){
            var pickedUpItemInInventory = ((AbstractContainerScreen<?>) client.screen).getMenu().getCarried();
            if(pickedUpItemInInventory.is(Items.LAPIS_BLOCK)) lapisLazuli += pickedUpItemInInventory.getCount() * 9;
            if(pickedUpItemInInventory.is(Items.LAPIS_LAZULI)) lapisLazuli += pickedUpItemInInventory.getCount();
            if(pickedUpItemInInventory.is(Items.TOTEM_OF_UNDYING)) amountTotemOfUndying += pickedUpItemInInventory.getCount();
        }

        lapisLazuliMax = 0;

        int timesCanEnchant = (xpLevel - 27) / 3;
        int lapisCanEnchant = lapisLazuli / 3;
        if(timesCanEnchant > 0) lapisLazuliMax = Math.min(timesCanEnchant, lapisCanEnchant);

        // Potion effects

        MobEffectInstance resistanceEffect = playerEntity.getEffect(MobEffects.RESISTANCE);
        resistancePercent = 0;
        if(resistanceEffect != null) resistancePercent = (resistanceEffect.getAmplifier() + 1) * 20;

        MobEffectInstance regenerationEffect = playerEntity.getEffect(MobEffects.REGENERATION);
        regenerationHealthRaw = 0;
        if(regenerationEffect != null)
            regenerationHealthRaw = Calculations.getEstimatedHealthRegen(50,
                                                    regenerationEffect.getAmplifier(),
                                                    regenerationEffect.getDuration(),
                                                    healthRaw,
                                                    maxHealthRaw);
        regenerationHealth = Mth.ceil(regenerationHealthRaw);

        MobEffectInstance poisonEffect = playerEntity.getEffect(MobEffects.POISON);
        poisonHealthRaw = maxHealthRaw;
        if(poisonEffect != null)
            poisonHealthRaw = Calculations.getEstimatedHealthDamage(25,
                                                    poisonEffect.getAmplifier(),
                                                    poisonEffect.getDuration(),
                                                    healthRaw,
                                                    1);
        poisonHealth = Mth.ceil(poisonHealthRaw);


        MobEffectInstance witherEffect = playerEntity.getEffect(MobEffects.WITHER);
        witherHealthRaw = maxHealthRaw;
        if(witherEffect != null)
            witherHealthRaw = Calculations.getEstimatedHealthDamage(40,
                                                    witherEffect.getAmplifier(),
                                                    witherEffect.getDuration(),
                                                    healthRaw,
                                                    0);
        witherHealth = Mth.ceil(witherHealthRaw);

        MobEffectInstance hungerEffect = playerEntity.getEffect(MobEffects.HUNGER);
        hungerEffectSaturationLoss = 0;
        if(hungerEffect != null) {
            int duration = hungerEffect.getDuration();
            float hungerEffectExhaustionLoss = 0.005F * (float)(hungerEffect.getAmplifier() + 1) * duration;
            hungerEffectSaturationLoss = hungerEffectExhaustionLoss / (float)4;

            if (Mth.ceil(hungerRaw + hungerEffectSaturationLoss) != (Mth.ceil(previousHungerEffectEstimate) - 1)) {
                hungerEffectEstimateRaw = !hasSaturation ? Math.max(Math.min(hungerRaw + hungerEffectSaturationLoss, maxFoodLevelRaw), 0) : hungerRaw;
                previousHungerEffectEstimate = hungerEffectEstimateRaw;
            }
        }
        else {
            hungerEffectEstimateRaw = hungerRaw;
            previousHungerEffectEstimate = hungerEffectEstimateRaw;
        }
        hungerEffectEstimate = Mth.ceil(hungerEffectEstimateRaw);

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

            if(Mth.ceil(health + naturalRegenerationAddition) != (Mth.ceil(previousNaturalRegenerationHealth) + 1)){
                naturalRegenerationHealthRaw = Math.min(healthRaw + naturalRegenerationAddition, maxHealthRaw);
                previousNaturalRegenerationHealth = naturalRegenerationHealth;
            }
        }
        else {
            naturalRegenerationHealthRaw = healthRaw;
            previousNaturalRegenerationHealth = naturalRegenerationHealthRaw;
        }
        naturalRegenerationHealth = Mth.ceil(naturalRegenerationHealthRaw);

        heldFoodHunger = 0;
        ItemStack heldFoodItem = Objects.requireNonNull(playerEntity).getMainHandItem();
        if(!heldFoodItem.getComponents().has(DataComponents.FOOD)) heldFoodItem = playerEntity.getOffhandItem();

        if(heldFoodItem.getComponents().has(DataComponents.FOOD)){
            isHoldingFood = true;
            FoodProperties itemFood = heldFoodItem.getItem().components().get(DataComponents.FOOD);
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
        heldFoodHealthEstimate = Mth.ceil(heldFoodHealthEstimateRaw);

        rawMaxWardenDanger = 149;
        maxWardenDanger = 20;
        rawWardenDanger = 0;
        wardenDanger = 0;
        isWardenNear = false;
        isWardenAngry = false;

        Warden warden = getClosestWarden(playerEntity);

        if(warden != null){
            isWardenNear = true;
            rawWardenDanger = warden.getClientAngerLevel();
            isWardenAngry = rawWardenDanger > AngerLevel.ANGRY.getMinimumAnger();
            wardenDanger = (int) (rawWardenDanger / Calculations.getPrettyDivisor(rawMaxWardenDanger, playerEntity.invulnerableDuration));
        }
    }

    public static void setPlayerBurningOnSoulFire(boolean isBurning){
        isBurningOnSoulFire = isBurning;
    }

    public static void setLocatorBarAvailable(boolean isAvailable){
        locatorBarAvailable = isAvailable;
    }

    private double getFallingHeightEstimate(Player playerEntity, double height){
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
        return Mth.ceil(value);
    }

    private Warden getClosestWarden(Player player){
        // A warden is aware of all targetable entities within a 49×51×49 box around itself. https://minecraft.wiki/w/Warden#Behavior
        AABB boundingBox = player.getBoundingBox().inflate(49, 51, 49);
        List<Warden> nearbyWardens = player.level().getEntities(
                EntityTypeTest.forClass(Warden.class),
                boundingBox,
                Predicates.alwaysTrue()
        );

        return nearbyWardens.stream().min(Comparator.comparingDouble(e ->
               Calculations.getDistance(player.getX(), player.getY(), player.getZ(), e.getX(), e.getY(), e.getZ()))).orElse(null);
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


    private int getArmorElementArmor(Player playerEntity, EquipmentSlot slot) {
        return getProtectionFromArmor(playerEntity.getItemBySlot(slot));
    }

    private int getArmorItemMaxArmor(Item armorItem) {
        return getProtectionFromArmor(new ItemStack(armorItem));
    }

    public float getArmorElementDurability(Player playerEntity, EquipmentSlot slot, float maxLimit){
        ItemStack armorItem = playerEntity.getItemBySlot(slot);
        if (armorItem != ItemStack.EMPTY){
            var rawArmorDurability = armorItem.getMaxDamage() - armorItem.getDamageValue();
            var rawMaxArmorDurability = armorItem.getMaxDamage();
            return rawArmorDurability > 0 ? (((float)rawArmorDurability / rawMaxArmorDurability) * maxLimit) : 0;
        }
        return (float)0;
    }

    private float getArmorElementMaxDurability(Player playerEntity, EquipmentSlot slot){
        ItemStack armorItem = playerEntity.getItemBySlot(slot);
        if (armorItem != ItemStack.EMPTY)
            return (float)armorItem.getMaxDamage();
        return (float)0;
    }

    private static int calculateMobDetectionRange(Player playerEntity, double baseRange) {
        if (playerEntity.level().getDifficulty() == Difficulty.PEACEFUL) {
            return 0;
        }

        double modifiedRange = baseRange;

        // Apply the head reduction
        modifiedRange *= 0.50;

        // Sneaking check
        if (playerEntity.isShiftKeyDown()) {
            modifiedRange *= 0.80; // 80% of normal
        }

        // Invisibility check
        if (playerEntity.hasEffect(MobEffects.INVISIBILITY)) {
            // Count how many armor slots are occupied
            int armorPieces = 0;
            if (playerEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() != Items.AIR) {
                armorPieces++;
            }
            if (playerEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() != Items.AIR) {
                armorPieces++;
            }
            if (playerEntity.getItemBySlot(EquipmentSlot.LEGS).getItem() != Items.AIR) {
                armorPieces++;
            }
            if (playerEntity.getItemBySlot(EquipmentSlot.FEET).getItem() != Items.AIR) {
                armorPieces++;
            }

            double invisFactor = 0.175 * armorPieces; // 17.5% per piece
            if (invisFactor > 1.0) {
                invisFactor = 1.0;
            }
            modifiedRange *= invisFactor;
        }

        // Round up to the next whole number
        return (int) Math.ceil(modifiedRange);
    }

    private int calculateRaidWaves(Player player) {
        Difficulty diff = player.level().getDifficulty();
        int baseWaves = switch (diff) {
            case PEACEFUL -> 0;
            case EASY -> 3;
            case NORMAL -> 5;
            case HARD -> 7;
        };

        if (player.hasEffect(MobEffects.RAID_OMEN)) {
            int amplifier = player.getEffect(MobEffects.RAID_OMEN).getAmplifier();
            if ((amplifier + 1) >= 2) {
                baseWaves++;
            }
        }

        return baseWaves;
    }

    public String getTrialOmenTimeString(Player player) {
        if (!player.hasEffect(MobEffects.TRIAL_OMEN)) {
            return "";
        }
        MobEffectInstance trial = player.getEffect(MobEffects.TRIAL_OMEN);
        int ticks = trial.getDuration();
        int totalSeconds = ticks / 20;

        if (totalSeconds >= 60) {
            int minutes = totalSeconds / 60;
            return Component.translatable("text.onebar.trialOmenEmoji.minutes", minutes).getString();
        } else if (totalSeconds >= 30) {
            return Component.translatable("text.onebar.trialOmenEmoji.underMinute").getString();
        } else {
            return String.valueOf(totalSeconds);
        }
    }

}
