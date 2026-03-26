package io.github.madis0;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;
import net.minecraft.client.Minecraft;
import java.util.Objects;

public class BarVisibility {
    public BarVisibility(){
        render();

        if(!MixinConfigQuery.isOneBarEnabled()) return;

        if(!MixinConfigQuery.isCompatModeEnabled()){
            HudElementRegistry.removeElement(VanillaHudElements.HEALTH_BAR);
            HudElementRegistry.removeElement(VanillaHudElements.FOOD_BAR);
            HudElementRegistry.removeElement(VanillaHudElements.ARMOR_BAR);
            HudElementRegistry.removeElement(VanillaHudElements.MOUNT_HEALTH);
            HudElementRegistry.removeElement(VanillaHudElements.AIR_BAR);
            if(!MixinConfigQuery.isLocatorBarEnabled()){
                HudElementRegistry.removeElement(VanillaHudElements.INFO_BAR);
                HudElementRegistry.removeElement(VanillaHudElements.EXPERIENCE_LEVEL);
            }
        }
    }

    public void render() {
        var showOneBar = MixinConfigQuery.isOneBarEnabled(); // This var exists because it also shows whether oneBarElements is initialized
        var minecraft = Minecraft.getInstance();

        if (minecraft.options == null)
            return;
        boolean barsVisible = !minecraft.options.hideGui && Objects.requireNonNull(minecraft.gameMode).canHurtPlayer();

        if(showOneBar && barsVisible)
            HudElementRegistry.attachElementBefore(VanillaHudElements.HOTBAR, Identifier.fromNamespaceAndPath("onebar", "main"), OneBarElements::render);

        if(showOneBar && MixinConfigQuery.showMountJump() && !minecraft.options.hideGui)
            HudElementRegistry.attachElementBefore(VanillaHudElements.HOTBAR, Identifier.fromNamespaceAndPath("onebar", "main"), OneBarElements::renderEntityBar);

        PlayerProperties.setLocatorBarAvailable(minecraft.player.connection.getWaypointManager().hasWaypoints());
    }
}
