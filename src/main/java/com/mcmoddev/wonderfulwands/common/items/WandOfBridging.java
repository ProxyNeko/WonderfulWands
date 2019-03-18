package com.mcmoddev.wonderfulwands.common.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WandOfBridging extends Wand {

    private final int limit = 32;
    private static int cooldown = 10;
    private Block bridgeBlock;
    private static int defaultCharges = 64;

    public WandOfBridging() {
        super(defaultCharges);
        this.bridgeBlock = Blocks.COBBLESTONE;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getRepairCost() {
        return 2;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 7200;
    }

    /**
     * Callback for item usage, invoked when right-clicking on a block. If the item
     * does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return false;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItemMainhand());
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase player, int timeRemain) {
        if (player instanceof EntityPlayer && !((EntityPlayer) player).capabilities.isCreativeMode) {
            if (isOutOfCharge(stack)) {
                playSound(noChargeSound, world, player);
                return;
            }
        }

        int blocksChanged = 0;
        Vec3d delta = player.getLookVec();
        delta = (new Vec3d(delta.x, 0, delta.z)).normalize();
        BlockPos playerPos = player.getPosition().down();
        Vec3d originPrime = new Vec3d(playerPos.getX(), playerPos.getY(), playerPos.getZ());
        int[] changeTracker = new int[3];
        Vec3d pos = originPrime.add(delta);
        for (int i = 0; i < limit; i++) {
            int blockDelta = 0;
            BlockPos block = new BlockPos(pos);
            blockDelta += placeBridgeBlock(world, block);
            blockDelta += placeBridgeBlock(world, block.north());
            blockDelta += placeBridgeBlock(world, block.south());
            blockDelta += placeBridgeBlock(world, block.east());
            blockDelta += placeBridgeBlock(world, block.west());

            changeTracker[0] = changeTracker[1];
            changeTracker[1] = changeTracker[2];
            changeTracker[2] = blockDelta;
            if (changeTracker[0] + changeTracker[1] + changeTracker[2] == 0) {
                // hit a wall, stop
                break;
            }

            blocksChanged += blockDelta;
            // move forward
            pos = pos.add(delta);
        }

        if (blocksChanged > 0) {
            playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, world, player);
            if (player instanceof EntityPlayer && !((EntityPlayer) player).capabilities.isCreativeMode) {
                stack.damageItem(1, player);
            }
        }
        return;
    }

    private int placeBridgeBlock(World world, BlockPos pos) {
        if (world.isRemote) return 0;
        if (world.isAirBlock(pos)) {
            world.setBlockState(pos, bridgeBlock.getDefaultState());
            return 1;
        }

        IBlockState current = world.getBlockState(pos);
        if (current.getMaterial().blocksMovement()) return 0;
        world.setBlockState(pos, bridgeBlock.getDefaultState());
        return 1;
    }
}