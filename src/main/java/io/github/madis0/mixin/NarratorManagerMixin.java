package io.github.madis0.mixin;

import net.minecraft.client.option.NarratorMode;
import net.minecraft.client.util.NarratorManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NarratorManager.class)
public interface NarratorManagerMixin {

    @Invoker("getNarratorMode")
    NarratorMode invokeGetNarratorMode();
}

