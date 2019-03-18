package com.mcmoddev.wonderfulwands.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

/**
 * Created by Chris on 4/17/2016.
 */
public class BlockFeyRailPowered extends BlockRailPowered {

    public BlockFeyRailPowered() {
        super();
        setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, EnumRailDirection.NORTH_SOUTH).withProperty(POWERED, true));
    }

    @Override
    protected void updateState(IBlockState blockState, World world, BlockPos blockPos, Block neighborBlock) {
        // disable by redstone instead of powered by redstone
        boolean wasPowered = (blockState.getValue(POWERED));
        boolean isPowered = !world.isBlockPowered(blockPos);

        if (isPowered != wasPowered) {
            world.setBlockState(blockPos, blockState.withProperty(POWERED, isPowered), 3);
            world.notifyNeighborsOfStateChange(blockPos.down(), this, isPowered);

            if ((blockState.getValue(SHAPE)).isAscending()) {
                world.notifyNeighborsOfStateChange(blockPos.up(), this, isPowered);
            }
        }
    }

    @Override
    public void onMinecartPass(World world, EntityMinecart cart, BlockPos blockPos) {
        double maxSpeed = cart.getCurrentCartSpeedCapOnRail();
        double dx = cart.motionX;
        double dz = cart.motionZ;
        double speed = Math.sqrt(dx * dx + dz * dz);
        if (speed > 0) {
            if (world.getBlockState(blockPos).getValue(POWERED)) {
                // act as an accelerator
                cart.motionX = maxSpeed * dx / speed;
                cart.motionZ = maxSpeed * dz / speed;
            } else {
                // act as a break
                double newSpeed = Math.max(0, speed - (0.0625 * maxSpeed));
                cart.motionX = newSpeed * dx / speed;
                cart.motionZ = newSpeed * dz / speed;
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos blockPos, IBlockState blockState, int fortune) {
        return Collections.emptyList();
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player) {
        return false;
    }
}