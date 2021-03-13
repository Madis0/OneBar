package io.github.madis0.mixin;

import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HungerManager.class)
public interface HungerManagerMixin {
    @Accessor
    float getExhaustion();

    @Accessor
    int getFoodStarvationTimer();
}
