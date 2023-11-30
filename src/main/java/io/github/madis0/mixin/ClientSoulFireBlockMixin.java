package io.github.madis0.mixin;

import io.github.madis0.PlayerProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(SoulFireBlock.class)
public abstract class ClientSoulFireBlockMixin extends AbstractFireBlock {

    public ClientSoulFireBlockMixin(Settings settings, float damage) {
        super(settings, damage);
    }

    @Shadow
    @Override
    protected boolean isFlammable(BlockState state) {
        return true;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        PlayerProperties.setPlayerBurningOnSoulFire(entity.isPlayer());
        super.onEntityCollision(state, world, pos, entity);
    }
}