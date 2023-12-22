package io.github.madis0;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BlockFinder {
    public static BlockState findClosestBlock(PlayerEntity player, Block targetBlock, int radius) {
        World world = player.getWorld();
        BlockPos playerPos = player.getBlockPos();
        BlockState blockState = null;
        BlockPos closestPos = null;
        double closestDistance = Double.POSITIVE_INFINITY;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos currentPos = playerPos.add(x, y, z);
                    blockState = world.getBlockState(currentPos);
                    if (blockState.getBlock() == targetBlock) {
                        if (hasAdjacentAir(world, currentPos)) {
                            double dx = currentPos.getX() - playerPos.getX();
                            double dy = currentPos.getY() - playerPos.getY();
                            double dz = currentPos.getZ() - playerPos.getZ();
                            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                            if (distance < closestDistance) {
                                closestPos = currentPos;
                                closestDistance = distance;
                            }
                        }
                    }
                }
            }
        }

        return closestPos != null ? world.getBlockState(closestPos) : null;
    }

    public static BlockEntity findClosestBlockEntity(PlayerEntity player, Class<? extends BlockEntity> targetEntityClass, int radius) {
        World world = player.getWorld();
        BlockPos playerPos = player.getBlockPos();
        BlockEntity closestEntity = null;
        BlockPos closestPos = null;
        double closestDistance = Double.POSITIVE_INFINITY;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos currentPos = playerPos.add(x, y, z);
                    BlockEntity blockEntity = world.getBlockEntity(currentPos);
                    if (targetEntityClass.isInstance(blockEntity)) {
                        double dx = currentPos.getX() - playerPos.getX();
                        double dy = currentPos.getY() - playerPos.getY();
                        double dz = currentPos.getZ() - playerPos.getZ();
                        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                        if (distance < closestDistance) {
                            closestEntity = blockEntity;
                            closestPos = currentPos;
                            closestDistance = distance;
                        }
                    }
                }
            }
        }

        return closestEntity;
    }


    private static boolean hasAdjacentAir(World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = pos.offset(direction);
            BlockState adjacentState = world.getBlockState(adjacentPos);
            if (adjacentState.isAir()) {
                return true;
            }
        }
        return false;
    }
}
