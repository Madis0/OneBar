package io.github.madis0.mixin;

import io.github.madis0.accessor.DrawContextAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin implements DrawContextAccessor {

    @Shadow
    @Final
    private MinecraftClient client;

    @Override
    public MinecraftClient getClient() {
        return client;
    }
}
