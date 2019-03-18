package com.mcmoddev.wonderfulwands.common.items;

import com.mcmoddev.wonderfulwands.common.blocks.IllusoryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandOfIllusions extends Wand {

    private static int cooldown = 10;
    private static int defaultCharges = 64;

    public WandOfIllusions() {
        super(defaultCharges);
        setMaxDamage(defaultCharges + 1);
    }

    @Override
    public int getRepairCost() {
        return 2;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!player.capabilities.isCreativeMode) {
            if (isOutOfCharge(stack)) {
                playSound(noChargeSound, world, player);
                return true;
            }
        }

        boolean success = convertBlock(player, world, pos);
        if (success) {
            playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, world, player);
            if (!player.capabilities.isCreativeMode) {
                stack.damageItem(1, player);
            }
        }
        return success;
    }

    /**
     * turns block into illusion
     * @param player
     * @param world
     * @param pos
     * @return True if anything happened, false otherwise (invalid target)
     */
    protected boolean convertBlock(EntityPlayer player, World world, BlockPos pos) {
        IBlockState targetBlock = world.getBlockState(pos);
        Block illusoryBlock = IllusoryBlock.getIllusionForBlock(targetBlock.getBlock());
        if (illusoryBlock == null) {
            return false;
        }

        IBlockState newBlock = illusoryBlock.getDefaultState();
        world.setBlockState(pos, newBlock);
        return true;
    }
}