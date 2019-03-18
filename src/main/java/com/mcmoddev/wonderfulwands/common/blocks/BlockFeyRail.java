package com.mcmoddev.wonderfulwands.common.blocks;

import net.minecraft.block.BlockRail;
import net.minecraft.block.state.IBlockState;
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
public class BlockFeyRail extends BlockRail {

    public BlockFeyRail() {
        super();
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
