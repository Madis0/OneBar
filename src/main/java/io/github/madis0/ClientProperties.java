package io.github.madis0;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;

public class ClientProperties {

    public int baseStartW;
    public int baseEndW;
    public int baseStartH;
    public int baseEndH;
    public int xpStartW;
    public int xpEndW;
    public int xpStartH;
    public int xpEndH;
    public int jumpStartW;
    public int jumpEndW;
    public int jumpStartH;
    public int jumpEndH;
    public int mountStartH;
    public int mountEndH;

    public int heldFoodHunger;
    public boolean isHardcore;

    public ClientProperties(){
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity playerEntity = client.player;
        int scaledWidth = client.getWindow().getScaledWidth();
        int scaledHeight = client.getWindow().getScaledHeight();

        baseStartW = scaledWidth / 2 - 91;
        baseEndW = baseStartW + 182;
        baseStartH = scaledHeight - 33;
        baseEndH = baseStartH + 9;

        if (client.options.mainArm == Arm.RIGHT && client.options.attackIndicator != AttackIndicator.HOTBAR)
            xpStartW = baseEndW + 6;
        else if (client.options.mainArm == Arm.RIGHT)
            xpStartW = baseEndW + 6 + 20;
        else if (client.options.mainArm == Arm.LEFT && client.options.attackIndicator != AttackIndicator.HOTBAR)
            xpStartW = baseStartW - 22;
        else if (client.options.mainArm == Arm.LEFT)
            xpStartW = baseStartW - 22 - 20;

        xpEndW = xpStartW + 18;
        xpStartH = baseStartH + 28;
        xpEndH = xpStartH + 1;

        jumpStartW = (scaledWidth / 2) - 2;
        jumpStartH = (scaledHeight / 2) + 10;
        jumpEndW = jumpStartW + 3;
        jumpEndH = jumpStartH + 50;

        mountStartH = baseStartH - 12;
        mountEndH = mountStartH + 9;

        heldFoodHunger = 0;
        ItemStack heldItem = playerEntity.getMainHandStack();
        if(!heldItem.isFood()) heldItem = playerEntity.getOffHandStack();

        if(heldItem.isFood()){
            FoodComponent itemFood = heldItem.getItem().getFoodComponent();
            heldFoodHunger = itemFood.getHunger();
        }

        isHardcore = playerEntity.world.getLevelProperties().isHardcore();
    }

    public int baseRelativeEndW(int value, int total){
        return Calculations.RelativeW(baseStartW, baseEndW, value, total);
    }

    public int baseRelativeStartW(int value, int total){
        return Calculations.RelativeW(baseEndW, baseStartW, value, total);
    }
}
