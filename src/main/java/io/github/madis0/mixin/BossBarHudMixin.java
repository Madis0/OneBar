package io.github.madis0.mixin;

import io.github.madis0.MixinConfigQuery;
import io.github.madis0.ModConfig;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BossBarHud.class)
public abstract class BossBarHudMixin {
    @ModifyConstant(
            method = "render(Lnet/minecraft/client/gui/DrawContext;)V",
            constant = @Constant(intValue = 12)
    )
    private int adjustBossBarStartY(int original) {
        return MixinConfigQuery.isLocatorBarMode(ModConfig.LocatorBarMode.BOSSBAR) && MixinConfigQuery.isLocatorBarEnabled() ?
                original + 9 + 9 : original; // 9 for bossbar, 9 for bossbar label
    }
}
