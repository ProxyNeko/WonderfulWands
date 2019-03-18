package com.mcmoddev.wonderfulwands.common.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick.EnumType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandOfGrowth extends Wand {

    private static int cooldown = 10;
    private static int defaultCharges = 128;
    private static final PropertyEnum<EnumType> stoneblockVariantKey = PropertyEnum.create("variant", EnumType.class);

    public WandOfGrowth() {
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

        boolean success = growBlock(player, world, pos);
        if (success) {
            playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, world, player);
            if (!player.capabilities.isCreativeMode) {
                stack.damageItem(1, player);
            }
        }
        return success;
    }

    protected boolean growBlock(EntityPlayer player, World world, BlockPos pos) {
        IBlockState targetBS = world.getBlockState(pos);
        Block targetBlock = targetBS.getBlock();
        ItemStack stack = new ItemStack(Items.DYE, 1, 15);
        int targetX = pos.getX();
        int targetY = pos.getY();
        int targetZ = pos.getZ();
        if (targetBlock == Blocks.CACTUS) {
            int y = targetY + 1;
            while (world.getBlockState(new BlockPos(targetX, y, targetZ)).getBlock() == Blocks.CACTUS && y < 255) {
                y++;
            }
            if (world.isAirBlock(new BlockPos(targetX, y, targetZ))) {
                world.setBlockState(new BlockPos(targetX, y, targetZ), Blocks.CACTUS.getDefaultState());
            }
            return true;
        } else if (targetBlock == Blocks.REEDS) {
            int y = targetY + 1;
            while (world.getBlockState(new BlockPos(targetX, y, targetZ)).getBlock() == Blocks.REEDS && y < 255) {
                y++;
            }
            if (world.isAirBlock(new BlockPos(targetX, y, targetZ))) {
                world.setBlockState(new BlockPos(targetX, y, targetZ), Blocks.REEDS.getDefaultState());
            }
            return true;
        } else if (targetBlock == Blocks.COBBLESTONE) {
            world.setBlockState(pos, Blocks.MOSSY_COBBLESTONE.getDefaultState());
            return true;
        } else if (targetBlock == Blocks.STONEBRICK) {
            if (targetBS.getProperties().get(stoneblockVariantKey) == EnumType.DEFAULT) {
                world.setBlockState(pos, Blocks.STONEBRICK.getStateFromMeta(1));
                return true;
            }
        }

        return ItemDye.applyBonemeal(stack, world, pos);
    }
}