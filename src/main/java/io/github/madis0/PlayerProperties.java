package io.github.madis0;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angriness;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PlayerProperties {
    Difficulty difficulty;
    private final MinecraftClient client = MinecraftClient.getInstance();

    public final boolean hasResistance;
    public final boolean hasRegeneration;
    public final boolean hasPoison;
    public final boolean hasWither;
    public final boolean hasFireResistance;
    public final boolean hasWaterBreathing;
    public final boolean hasHungerEffect;
    public final boolean hasBadOmen;
    public final boolean hasInvisibility;
    public final boolean hasGlowing;

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
    public boolean hasGoldenArmorItem;

    public boolean hasTotemOfUndying;
    public int amountTotemOfUndying;
    public boolean isHoldingTotemOfUndying;
    
    public boolean hasArrowsStuck;

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
    public double belowBlockYRaw;
    public int belowBlockY;
    public double normalFallHeightRaw;
    public int normalFallHeight;
    public boolean normalFallHurts;
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
        difficulty = playerEntity.world.getDifficulty();

        // Player property calculations
        hasResistance = playerEntity.hasStatusEffect(StatusEffects.RESISTANCE);
        hasRegeneration = playerEntity.hasStatusEffect(StatusEffects.REGENERATION);
        hasPoison = playerEntity.hasStatusEffect(StatusEffects.POISON);
        hasWither = playerEntity.hasStatusEffect(StatusEffects.WITHER);
        hasFireResistance = playerEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
        hasWaterBreathing = playerEntity.hasStatusEffect(StatusEffects.WATER_BREATHING) || playerEntity.hasStatusEffect(StatusEffects.CONDUIT_POWER);
        hasHungerEffect = playerEntity.hasStatusEffect(StatusEffects.HUNGER) && !difficulty.equals(Difficulty.PEACEFUL);
        hasBadOmen = playerEntity.hasStatusEffect(StatusEffects.BAD_OMEN) && !difficulty.equals(Difficulty.PEACEFUL);
        hasInvisibility = playerEntity.hasStatusEffect(StatusEffects.INVISIBILITY);
        hasGlowing = playerEntity.hasStatusEffect(StatusEffects.GLOWING);
        hasLevitation = playerEntity.hasStatusEffect(StatusEffects.LEVITATION);

        healthRaw = playerEntity.getHealth();
        maxHealthRaw = playerEntity.getMaxHealth();
        health = MathHelper.ceil(healthRaw);
        maxHealth = MathHelper.ceil(maxHealthRaw);
        absorption = MathHelper.ceil(playerEntity.getAbsorptionAmount());
        hasAbsorption = absorption > 0;

        maxArmor = playerEntity.defaultMaxHealth;
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

        hasGoldenArmorItem = (playerEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() == Items.GOLDEN_HELMET ||
                              playerEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.GOLDEN_CHESTPLATE ||
                              playerEntity.getEquippedStack(EquipmentSlot.LEGS).getItem() == Items.GOLDEN_LEGGINGS ||
                              playerEntity.getEquippedStack(EquipmentSlot.FEET).getItem() == Items.GOLDEN_BOOTS);

        amountTotemOfUndying = playerEntity.getInventory().count(Items.TOTEM_OF_UNDYING);
        hasTotemOfUndying = amountTotemOfUndying > 0;
        isHoldingTotemOfUndying = (playerEntity.getEquippedStack(EquipmentSlot.MAINHAND).getItem() == Items.TOTEM_OF_UNDYING ||
                                   playerEntity.getEquippedStack(EquipmentSlot.OFFHAND).getItem() == Items.TOTEM_OF_UNDYING);

        hasArrowsStuck = playerEntity.getStuckArrowCount() > 0;

        ItemStack chestItem = playerEntity.getEquippedStack(EquipmentSlot.CHEST);
        if (chestItem.isOf(Items.ELYTRA) && ElytraItem.isUsable(chestItem)) {
            hasElytra = true;
            elytraDurability = chestItem.getMaxDamage() - chestItem.getDamage();
            elytraMaxDurability = chestItem.getMaxDamage();
        }
        isFlyingWithElytra = playerEntity.isFallFlying();

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
        int rawBurningSource = playerEntity.getFireTicks();

        // Reset soul fire state if burning state changes
        if(rawBurningSource != 1) isBurningOnSoulFire = false;

        if(rawBurningSource == -20) burningMultiplier = 1;
        if(rawBurningSource == 1) burningMultiplier = 2;
        if(isBurningOnSoulFire) burningMultiplier = 3;
        if(rawBurningSource == 0) burningMultiplier = 4;
        isBurningOnFire = (burningMultiplier == 2 || burningMultiplier == 4) && !hasFireResistance;

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
        double y = playerEntity.getY();
        double voidLimit = -128;
        BlockState state; //TODO: use material to determine fall damage
        if(playerEntity.getWorld() != null && (!playerEntity.isOnGround() || playerEntity.isSneaking())){
            var world = playerEntity.getWorld();
            var x = playerEntity.getX();
            var z = playerEntity.getZ();
            while((state = world.getBlockState(new BlockPos(x, --y, z))).isAir() && y >= voidLimit) { //TODO: detect if block is solid
                belowBlockYRaw = y;
            }
        }
        belowBlockY = (int) Math.round(belowBlockYRaw);

        if(playerEntity.hasStatusEffect(StatusEffects.LEVITATION)){
            var effect = playerEntity.getStatusEffect(StatusEffects.LEVITATION);
            var estHeight = (effect.getAmplifier() + 1) * 0.9 * ((float) effect.getDuration() / 20);
            levitationResultYRaw = playerEntity.getY() + estHeight;
            levitationFallHeightRaw = levitationResultYRaw - belowBlockYRaw;
            levitationFallHeight = (int) Math.round(levitationFallHeightRaw);
            levitationFallHurts = levitationFallHeight >= 4;
        }
        normalFallHeightRaw = playerEntity.getY() - belowBlockYRaw;
        normalFallHeight = (int) Math.round(normalFallHeightRaw);
        normalFallHurts = normalFallHeight >= 4;

        badOmenLevel = hasBadOmen ? Objects.requireNonNull(playerEntity.getStatusEffect(StatusEffects.BAD_OMEN)).getAmplifier() + 1: 0;

        xpLevel = playerEntity.experienceLevel;
        maxXp = 183; //renderExperienceBar @ InGameHud.class
        xp = (int)(playerEntity.experienceProgress * maxXp);
        lapisLazuli = playerEntity.getInventory().count(Items.LAPIS_LAZULI) +
                (playerEntity.getInventory().count(Items.LAPIS_BLOCK) * 9);

        if(client.currentScreen instanceof EnchantmentScreen){
            lapisLazuli += ((EnchantmentScreen) client.currentScreen).getScreenHandler().getLapisCount();
        }

        if (client.currentScreen != null && client.currentScreen instanceof HandledScreen<?>){
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
                naturalRegenerationAddition = Calculations.getNaturalRegenAddition(saturationRaw, hungerRaw);
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
            heldFoodHealthEstimateRaw = !difficulty.equals(Difficulty.PEACEFUL) ? Math.min(healthRaw + Calculations.getNaturalRegenAddition(saturationRaw + extraRegenFoodLevel + heldFoodSaturation, hungerRaw), maxFoodLevelRaw) : maxHealthRaw - healthRaw;
            heldFoodSaturationEstimateRaw = Math.max(heldFoodSaturation - (maxHealthRaw - healthRaw) - extraRegenFoodLevel, 0);
        }
        else {
            heldFoodHealthEstimateRaw = 0;
            heldFoodSaturationEstimateRaw = 0;
        }
        heldFoodHealthEstimate = (int) Math.ceil(heldFoodHealthEstimateRaw);

        rawMaxWardenDanger = 149;
        maxWardenDanger = 20;
        rawWardenDanger = 0;
        wardenDanger = 0;
        isWardenNear = false;
        isWardenAngry = false;

        WardenEntity warden = getClosestWarden(playerEntity, 100);

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

    private WardenEntity getClosestWarden(PlayerEntity player, int range){
        TargetPredicate targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(range + 1);
        Box boundingBox = player.getBoundingBox().expand(range, range, range);
        List<WardenEntity> nearbyWardens = player.world.getTargets(WardenEntity.class, targetPredicate, player, boundingBox);

        return nearbyWardens.stream().min(Comparator.comparingDouble(e ->
                Calculations.getDistance(player.getX(), player.getY(), player.getZ(), e.getX(), e.getY(), e.getZ()))).orElse(null);
    }

    private int getArmorElementArmor(PlayerEntity playerEntity, EquipmentSlot slot){
        var armorItem = playerEntity.getEquippedStack(slot).getItem();
        if(armorItem instanceof ArmorItem){
            return ((ArmorItem)armorItem).getProtection();
        }
        return 0;
    }

    private int getArmorItemMaxArmor(Item armorItem){
        if(armorItem instanceof ArmorItem)
            return ((ArmorItem)armorItem).getProtection();
        return 0;
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

        if(headItem == Items.ZOMBIE_HEAD)
            return Calculations.emojiOrText("text.onebar.mobHeadZombieEmoji","text.onebar.mobHeadZombie", false, (Object) null);
        else if(headItem == Items.SKELETON_SKULL)
            return Calculations.emojiOrText("text.onebar.mobHeadSkeletonEmoji","text.onebar.mobHeadSkeleton", false, (Object) null);
        else if(headItem == Items.CREEPER_HEAD)
            return Calculations.emojiOrText("text.onebar.mobHeadCreeperEmoji","text.onebar.mobHeadCreeper", false, (Object) null);
        else if(headItem == Items.CARVED_PUMPKIN)
            return Calculations.emojiOrText("text.onebar.mobHeadEndermanEmoji","text.onebar.mobHeadEnderman", false, (Object) null);
        else
            return null;
    }
}
