package io.github.madis0.mixin;

import io.github.madis0.PlayerProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.SoulFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(SoulFireBlock.class)
public abstract class ClientSoulFireBlockMixin extends BaseFireBlock {

    public ClientSoulFireBlockMixin(Properties settings, float damage) {
        super(settings, damage);
    }

    @Shadow
    @Override
    protected boolean canBurn(BlockState state) {
        return true;
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl) {
        PlayerProperties.setPlayerBurningOnSoulFire(entity.isAlwaysTicking());
        super.entityInside(state, world, pos, entity, handler, bl);
    }
}