package com.mcmoddev.wonderfulwands.common.items;

import net.minecraft.block.BlockVine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandOfClimbing extends Wand {

    private static int cooldown = 10;
    private static int defaultCharges = 64;

    public WandOfClimbing() {
        super(defaultCharges);
    }

    @Override
    public int getRepairCost() {
        return 1;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!player.capabilities.isCreativeMode) {
            if (isOutOfCharge(stack)) {
                playSound(noChargeSound, world, player);
                return true;
            }
        }

        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            return false;
        }

        boolean success = growVines(world, pos.offset(facing)) > 0;
        if (success) {
            playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, world, player);
            if (!player.capabilities.isCreativeMode) {
                stack.damageItem(1, player);
            }
        }
        return success;
    }

    private int growVines(World world, BlockPos pos) {
        int numVinesAdded = 0;
        BlockPos lookPos = pos;
        // first, go down
        while (lookPos.getY() > 0 && world.isAirBlock(lookPos)) {
            int n = placeVinesAt(world, lookPos);
            if (n == 0) {
                break;
            }

            numVinesAdded += n;
            lookPos = lookPos.down();
        }

        lookPos = pos.up();
        // then go up
        while (lookPos.getY() > 0 && world.isAirBlock(lookPos)) {
            int n = placeVinesAt(world, lookPos);
            if (n == 0) break;
            numVinesAdded += n;
            lookPos = lookPos.up();
        }
        return numVinesAdded;
    }

    private int placeVinesAt(World world, BlockPos pos) {
        boolean north = false, east = false, south = false, west = false, canPlaceVines = false;
        if (world.getBlockState(pos.north()).getMaterial().blocksMovement()) {
            north = true;
            canPlaceVines = true;
        }

        if (world.getBlockState(pos.east()).getMaterial().blocksMovement()) {
            east = true;
            canPlaceVines = true;
        }

        if (world.getBlockState(pos.south()).getMaterial().blocksMovement()) {
            south = true;
            canPlaceVines = true;
        }

        if (world.getBlockState(pos.west()).getMaterial().blocksMovement()) {
            west = true;
            canPlaceVines = true;
        }

        if (canPlaceVines) {
            world.setBlockState(pos, Blocks.VINE.getDefaultState()
                    .withProperty(BlockVine.UP, false)
                    .withProperty(BlockVine.NORTH, north)
                    .withProperty(BlockVine.EAST, east)
                    .withProperty(BlockVine.SOUTH, south)
                    .withProperty(BlockVine.WEST, west));
            return 1;
        } else {
            return 0;
        }
    }
}