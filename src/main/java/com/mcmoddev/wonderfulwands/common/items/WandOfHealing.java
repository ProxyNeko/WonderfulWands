package com.mcmoddev.wonderfulwands.common.items;

import com.mcmoddev.wonderfulwands.common.util.RayTrace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class WandOfHealing extends Wand {

    private static int range = 16;
    private static int defaultCharges = 64;
    private static float healingAmount = 5.0F;

    public WandOfHealing() {
        super(defaultCharges);
    }

    @Override
    public int getRepairCost() {
        return 4;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItemMainhand());
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
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
    public int getMaxItemUseDuration(ItemStack stack) {
        return 7200;
    }

    /**
     * Invoked when the player releases the right-click button
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase player, int timeRemain) {
        if (player instanceof EntityPlayer && !((EntityPlayer) player).capabilities.isCreativeMode) {
            if (isOutOfCharge(stack)) {
                playSound(noChargeSound, world, player);
                return;
            }
        }

        Entity target;
        RayTraceResult trace = RayTrace.rayTraceBlocksAndEntities(world, range, player);
        if (trace == null) {
            target = player;
        } else {
            target = trace.entityHit;
        }
        if (!(target instanceof EntityLivingBase)) {
            target = player;
        }
        if (target instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) target;
            float health = entity.getHealth();
            float maxHealth = entity.getMaxHealth();
            if (health < maxHealth) {
                entity.setHealth(Math.min(maxHealth, health + healingAmount));
                playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, world, player);
                playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, world, entity);
                entity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("regeneration"), 60, 0));
                entity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("glowing"), 60, 0));
                if (player instanceof EntityPlayer && !((EntityPlayer) player).capabilities.isCreativeMode) {
                    stack.damageItem(1, player);
                }
            }
        }
    }
}