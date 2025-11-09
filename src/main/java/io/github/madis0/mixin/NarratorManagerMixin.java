package io.github.madis0.mixin;

import net.minecraft.client.GameNarrator;
import net.minecraft.client.NarratorStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameNarrator.class)
public interface NarratorManagerMixin {

    @Invoker("getStatus")
    NarratorStatus invokeGetNarratorMode();
}

