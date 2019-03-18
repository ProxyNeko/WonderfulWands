package com.mcmoddev.wonderfulwands.common.items;

import com.mcmoddev.wonderfulwands.common.entities.EntityLightWisp;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandOfGreaterLight extends Wand {

    private static int cooldown = 10;
    private static int defaultCharges = 64;
    //TODO Don't use this wand it crashes Linux... (Personal note for Proxy since his OS needs a reboot to gain the mouse back)

    public WandOfGreaterLight() {
        super(defaultCharges);
    }

    @Override
    public int getRepairCost() {
        return 3;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 1200;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
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

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase player, int timeRemain) {
        int chargetime = this.getMaxItemUseDuration(stack) - timeRemain;
        if (chargetime < 5) return;
        BlockPos center = player.getPosition();
        if (player instanceof EntityPlayer && !((EntityPlayer) player).capabilities.isCreativeMode) {
            if (isOutOfCharge(stack)) {
                playSound(noChargeSound, world, player);
                return;
            }
            stack.damageItem(1, player);
        }

        if (!world.isRemote) {
            EntityLightWisp[] entity = new EntityLightWisp[9];
            entity[0] = new EntityLightWisp(world, center);
            entity[1] = new EntityLightWisp(world, center.add(-1, 0, -1));
            entity[2] = new EntityLightWisp(world, center.add(0, 0, -1));
            entity[3] = new EntityLightWisp(world, center.add(1, 0, -1));
            entity[4] = new EntityLightWisp(world, center.add(-1, 0, 0));
            entity[5] = new EntityLightWisp(world, center.add(1, 0, 0));
            entity[6] = new EntityLightWisp(world, center.add(-1, 0, 1));
            entity[7] = new EntityLightWisp(world, center.add(0, 0, 1));
            entity[8] = new EntityLightWisp(world, center.add(1, 0, 1));
            for (int i = 0; i < entity.length; i++) {
                world.spawnEntity(entity[i]);
            }
        }

        playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, world, player);
    }
}