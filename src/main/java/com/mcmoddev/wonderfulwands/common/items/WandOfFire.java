package com.mcmoddev.wonderfulwands.common.items;

import com.mcmoddev.wonderfulwands.common.projectiles.Fireball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WandOfFire extends Wand {

    private static int cooldown = 15;
    private static int defaultCharges = 64;

    public WandOfFire() {
        super(defaultCharges);
    }

    @Override
    public int getRepairCost() {
        return 2;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return cooldown;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItemMainhand());
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

    /**
     * Invoked when the player releases the right-click button
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase player, int timeRemain) {
        super.onPlayerStoppedUsing(stack, world, player, timeRemain);
    }

    /**
     * This method is invoked after the item has been used for an amount of time equal to the duration
     * provided to the EntityPlayer.setItemInUse(stack, duration).
     */
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase player) {
        if (player instanceof EntityPlayer && !((EntityPlayer) player).capabilities.isCreativeMode) {
            if (isOutOfCharge(stack)) {
                playSound(noChargeSound, world, player);
                return stack;
            }
            stack.damageItem(1, player);
        }

        playSound(SoundEvents.ENTITY_FIREWORK_LAUNCH, world, player);
        if (!world.isRemote) {
            double vecX = (double) (-MathHelper.sin(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI));
            double vecY = (double) (-MathHelper.sin(player.rotationPitch / 180.0F * (float) Math.PI));
            double vecZ = (double) (MathHelper.cos(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI));
            double deltaX = (double) (-MathHelper.sin(player.rotationYaw / 180.0F * (float) Math.PI));
            double deltaZ = (double) (MathHelper.cos(player.rotationYaw / 180.0F * (float) Math.PI));
            Fireball fireball = new Fireball(world, player, player.posX + deltaX, player.posY + 1, player.posZ + deltaZ, vecX, vecY, vecZ);
            world.spawnEntity(fireball);
        }
        return stack;
    }
}