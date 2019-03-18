package com.mcmoddev.wonderfulwands.common.items;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandOfHarvesting extends Wand {

    private static int cooldown = 10;
    private static int defaultCharges = 128;

    public WandOfHarvesting() {
        super(defaultCharges);
        setMaxDamage(defaultCharges + 1);
    }

    @Override
    public int getRepairCost() {
        return 1;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        int targetX = pos.getX(), targetY = pos.getY(), targetZ = pos.getZ();
        if (isHarvestable(world, pos)) {
            if (!player.capabilities.isCreativeMode) {
                if (isOutOfCharge(stack)) {
                    playSound(noChargeSound, world, player);
                    return true;
                }
                stack.damageItem(1, player);
            }

            final int r = 3;
            for (int yPos = targetY + r; yPos >= targetY - r; yPos--) {
                if (yPos < 0) {
                    continue;
                }
                for (int xPos = targetX - r; xPos <= targetX + r; xPos++) {
                    for (int zPos = targetZ - r; zPos <= targetZ + r; zPos++) {
                        harvestBlock(world, new BlockPos(xPos, yPos, zPos));
                    }
                }
            }
            playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, world, player);
            return true;
        }
        return false;
    }

    protected void harvestBlock(World world, BlockPos pos) {
        float dropChance = 1;
        int fortuneLevel = 0;
        if (isHarvestable(world, pos)) {
            IBlockState block = world.getBlockState(pos);
            world.setBlockToAir(pos);
            if (!world.isRemote) {
                block.getBlock().dropBlockAsItemWithChance(world, pos, block, dropChance, fortuneLevel);
            }
        }
    }

    protected boolean isHarvestable(World world, BlockPos pos) {
        Material mat = world.getBlockState(pos).getMaterial();
        return mat == Material.CACTUS || mat == Material.LEAVES || mat == Material.PLANTS
                || mat == Material.GOURD || mat == Material.VINE || mat == Material.WEB
                || mat == Material.DRAGON_EGG || mat == Material.SPONGE;
    }
}