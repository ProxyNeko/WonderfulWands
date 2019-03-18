package com.mcmoddev.wonderfulwands.common.items;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import com.mcmoddev.wonderfulwands.api.blocks.WWBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandOfRails extends Wand {

    public static int cooldown = 10;
    public static int defaultCharges = 256;
    public static final String itemName = "wand_rails";

    public WandOfRails() {
        super(defaultCharges);
        setTranslationKey(WonderfulWands.MODID + "_" + itemName);
    }

    @Override
    public int getRepairCost() {
        return 2;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return false;
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
        if (isSelected && !world.isRemote) {
            if (this.placeRail(world, entity.getPosition(), stack) || this.placeRail(world, entity.getPosition().down(), stack)) {
                if (entity instanceof EntityPlayer && !((EntityPlayer) entity).capabilities.isCreativeMode) {
                    stack.damageItem(1, (EntityPlayer) entity);
                }
                super.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, world, entity);
            }
        }
    }

    public boolean placeRail(World world, BlockPos pos, ItemStack stack) {
        IBlockState current = world.getBlockState(pos);
        BlockPos basePos = pos.down();
        IBlockState base = world.getBlockState(basePos);
        if (base.isSideSolid(world, basePos, EnumFacing.UP) && current.getBlock().isReplaceable(world, pos)) {
            BlockPos lastPos = getLastPosition(stack);
            IBlockState rail;
            if (lastPos == null // last position not set
                    || (lastPos.getY() != pos.getY() && (lastPos.getX() == pos.getX() || lastPos.getZ() == pos.getZ())) // slope
                    || (getUnpoweredRailCount(stack) > 6 && (lastPos.getX() == pos.getX() || lastPos.getZ() == pos.getZ())) // distance from last powered rail
                    || (pos.distanceSq(lastPos) > 8 && (lastPos.getX() == pos.getX() || lastPos.getZ() == pos.getZ())) // discontinuous rail
            ) {
                rail = WWBlocks.FEY_RAIL_POWERED.getDefaultState();
                resetUnpoweredRailCount(stack);
            } else {
                // non-powered rail (can make turns)
                rail = WWBlocks.FEY_RAIL.getDefaultState();
                incrementUnpoweredRailCount(stack);
            }
            world.setBlockState(pos, rail, 3);
            setLastPosition(pos, stack);
            return true;
        }
        return false;
    }

    private static BlockPos getLastPosition(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("LP")) {
            int[] coord = stack.getTagCompound().getIntArray("LP");
            if (coord.length > 2) {
                return new BlockPos(coord[0], coord[1], coord[2]);
            }
        }
        return null;
    }

    private static void setLastPosition(BlockPos pos, ItemStack stack) {
        NBTTagCompound itemTag;
        if (stack.hasTagCompound()) {
            itemTag = stack.getTagCompound();
        } else {
            itemTag = new NBTTagCompound();
        }

        int[] coord = new int[3];
        coord[0] = pos.getX();
        coord[1] = pos.getY();
        coord[2] = pos.getZ();
        itemTag.setIntArray("LP", coord);
        stack.setTagCompound(itemTag);
    }

    private static int getUnpoweredRailCount(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("UC")) {
            return stack.getTagCompound().getInteger("UC");
        }
        return 0;
    }

    private static void incrementUnpoweredRailCount(ItemStack stack) {
        NBTTagCompound itemTag;
        if (stack.hasTagCompound()) {
            itemTag = stack.getTagCompound();
            itemTag.setInteger("UC", itemTag.getInteger("UC") + 1);
        } else {
            itemTag = new NBTTagCompound();
            itemTag.setInteger("UC", 1);
        }

        stack.setTagCompound(itemTag);
    }

    private static void resetUnpoweredRailCount(ItemStack stack) {
        NBTTagCompound itemTag;
        if (stack.hasTagCompound()) {
            itemTag = stack.getTagCompound();
        } else {
            itemTag = new NBTTagCompound();
        }

        itemTag.setInteger("UC", 0);
        stack.setTagCompound(itemTag);
    }
}