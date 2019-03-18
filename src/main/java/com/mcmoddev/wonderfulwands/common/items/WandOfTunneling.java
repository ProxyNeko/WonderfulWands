package com.mcmoddev.wonderfulwands.common.items;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandOfTunneling extends Wand {

    public static int range = 16;
    public static int cooldown = 10;
    public static int defaultCharges = 64;
    public static final String itemName = "wand_tunneling";

    public WandOfTunneling() {
        super(defaultCharges);
        setTranslationKey(WonderfulWands.MODID + "_" + itemName);
        setMaxDamage(defaultCharges + 1);
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

        mineTunnel(world, pos, facing.getOpposite());
        playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, world, player);
        if (!player.capabilities.isCreativeMode) {
            stack.damageItem(1, player);
        }
        return true;
    }

    protected void mineTunnel(World world, BlockPos pos, EnumFacing facing) {
        for (int i = 0; i < range; i += 3) {
            if (pos.getY() < 1) {
                break;
            }
            mineChunk(world, pos);
            pos = pos.offset(facing, 3);
        }
    }

    protected void mineChunk(World world, BlockPos pos) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    mineBlock(world, pos.add(dx, dy, dz));
                }
            }
        }
    }

    protected boolean mineBlock(World world, BlockPos pos) {
        IBlockState targetBlock = world.getBlockState(pos);
        if (targetBlock.getBlock() == Blocks.BEDROCK) {
            return false;
        }

        if (targetBlock.getBlockHardness(world, pos) < 100.0F) {
            // mine it
            int fortuneLevel = 0;
            world.setBlockToAir(pos);
            targetBlock.getBlock().dropBlockAsItemWithChance(world, pos, targetBlock, 1F, fortuneLevel);
            return true;
        }
        return false;
    }
}