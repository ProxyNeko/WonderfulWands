package com.mcmoddev.wonderfulwands.common.items;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import com.mcmoddev.wonderfulwands.common.util.RayTrace;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WandOfStorms extends Wand {

    public static int cooldown = 20;
    public static int defaultCharges = 64;
    public static final int areaOfEffectDiameter = 64;

    public WandOfStorms() {
        super(defaultCharges);
    }

    @Override
    public int getRepairCost() {
        return 1;
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

        playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, world, player);
        if (!world.isRemote) {
            RayTraceResult trace = RayTrace.rayTraceBlocksAndEntities(world, 64, player);
            if (trace == null || trace.typeOfHit == RayTraceResult.Type.MISS) { //missed! Drop random lightning
                int r = 32;
                int d = r * 2;
                BlockPos target = new BlockPos(player.posX + world.rand.nextInt(d) - r, player.posY, player.posZ + world.rand.nextInt(d) - r);
                while (target.getY() < 255 && !world.isAirBlock(target)) {
                    target = target.up();
                }
                while (target.getY() > 0 && world.isAirBlock(target)) {
                    target = target.down();
                }
                world.addWeatherEffect(new EntityLightningBolt(world, target.getX(), target.getY(), target.getZ(), false));
            } else {
                Vec3d target = trace.hitVec;
                if (target == null) {
                    target = new Vec3d(trace.getBlockPos().getX(), trace.getBlockPos().getY(), trace.getBlockPos().getZ());
                }
                world.addWeatherEffect(new EntityLightningBolt(world, target.x, target.y, target.z, false));
            }
        }
        return stack;
    }
}