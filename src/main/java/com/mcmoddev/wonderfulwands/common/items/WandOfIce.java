package com.mcmoddev.wonderfulwands.common.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandOfIce extends Wand {

    private static int cooldown = 10;
    private static int defaultCharges = 256;

    public WandOfIce() {
        super(defaultCharges);
    }

    @Override
    public int getRepairCost() {
        return 1;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        int targetX = pos.getX(), targetY = pos.getY(), targetZ = pos.getZ();
        if (!player.capabilities.isCreativeMode) {
            if (isOutOfCharge(stack)) {
                playSound(noChargeSound, world, player);
                return true;
            }
        }

        int blocksChanged = 0;
        int rSquared = 9;
        for (int dy = 2; dy >= -2; dy--) {
            int y = targetY + dy;
            for (int dx = -2; dx <= 2; dx++) {
                int x = targetX + dx;
                for (int dz = -2; dz <= 2; dz++) {
                    int z = targetZ + dz;
                    BlockPos block = new BlockPos(x, y, z);
                    // spheritize
                    if ((dy * dy + dx * dx + dz * dz) <= rSquared) {
                        // set to ice
                        blocksChanged += freezeBlock(world, block);
                    }
                }
            }
        }

        if (blocksChanged > 0) {
            stack.damageItem(1, player);
            playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, world, player);
            return true;
        } else {
            return false;
        }
    }

    protected int freezeBlock(World world, BlockPos pos) {
        IBlockState block = world.getBlockState(pos);
        Block target = block.getBlock();
        if (target == Blocks.WATER || target == Blocks.FLOWING_WATER) {
            world.setBlockState(pos, Blocks.ICE.getDefaultState());
            return 1;
        } else if (target == Blocks.LAVA || target == Blocks.FLOWING_LAVA) {
            world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
            return 1;
        } else if (target == Blocks.SNOW_LAYER) {
            if ((block.getValue(BlockSnow.LAYERS)) < 8) {
                world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState()
                        .withProperty(BlockSnow.LAYERS, ((block.getValue(BlockSnow.LAYERS)) + 1)));
            } else {
                world.setBlockState(pos, Blocks.SNOW.getDefaultState());
            }
        } else if (target.isFullCube(block) && world.isAirBlock(pos.up())) {
            world.setBlockState(pos.up(), Blocks.SNOW_LAYER.getDefaultState());
            return 1;
        }
        return 0;
    }
}