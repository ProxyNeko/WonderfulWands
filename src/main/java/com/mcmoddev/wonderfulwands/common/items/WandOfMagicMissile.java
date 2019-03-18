package com.mcmoddev.wonderfulwands.common.items;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import com.mcmoddev.wonderfulwands.common.projectiles.EntityMagicMissile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandOfMagicMissile extends Wand {

    public static final String itemName = "wand_missiles";
    public static int cooldown = 10;
    public static int defaultCharges = 64;

    public WandOfMagicMissile() {
        super(defaultCharges);
        setTranslationKey(WonderfulWands.MODID + "_" + itemName);
    }

    @Override
    public int getRepairCost() {
        return 2;
    }

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

        playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, world, player);
        if (!world.isRemote) {
            world.spawnEntity(new EntityMagicMissile(world, player));
        }
        return stack;
    }
}