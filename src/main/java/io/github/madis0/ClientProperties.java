package io.github.madis0;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;

public class ClientProperties {
    public int heldFoodHunger;
    public boolean isHardcore;

    public ClientProperties(){
        PlayerEntity playerEntity = MinecraftClient.getInstance().player;

        heldFoodHunger = 0;
        ItemStack heldItem = playerEntity.getMainHandStack();
        if(!heldItem.isFood()) heldItem = playerEntity.getOffHandStack();

        if(heldItem.isFood()){
            FoodComponent itemFood = heldItem.getItem().getFoodComponent();
            heldFoodHunger = itemFood.getHunger();
        }

        isHardcore = playerEntity.world.getLevelProperties().isHardcore();
    }
}
