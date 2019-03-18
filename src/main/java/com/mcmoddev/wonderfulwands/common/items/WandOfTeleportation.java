package com.mcmoddev.wonderfulwands.common.items;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WandOfTeleportation extends Wand {

    public static int defaultCharges = 64;
    public static final String itemName = "wand_teleportation";

    public WandOfTeleportation() {
        super(defaultCharges);
        setTranslationKey(WonderfulWands.MODID + "_" + itemName);
        setMaxDamage(defaultCharges + 1);
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
        if (chargetime < 3) {
            return;
        }

        if (player instanceof EntityPlayer && !((EntityPlayer) player).capabilities.isCreativeMode) {
            if (isOutOfCharge(stack)) {
                playSound(noChargeSound, world, player);
                return;
            }
            stack.damageItem(1, player);
        }

        playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, world, player);

        final int maxRange = 160;
        Vec3d origin = (new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ));
        Vec3d vector = player.getLookVec();
        Vec3d pos = origin;
        Vec3d next = pos;
        BlockPos coord = new BlockPos(pos);

        for (int i = 0; i < maxRange; i++) {
            next = pos.add(vector);
            if (next.y < 0 || next.y > 255) {
                break;
            }

            BlockPos nextBlock = new BlockPos(next);
            if (world.isAreaLoaded(coord, 1, true)) {
                if (world.isAirBlock(nextBlock)) {
                    world.spawnParticle(EnumParticleTypes.PORTAL,
                            pos.x + (world.rand.nextDouble() - 0.5), pos.y + (world.rand.nextDouble() - 0.5),
                            pos.z + (world.rand.nextDouble() - 0.5), (world.rand.nextFloat() - 0.5f) * 0.2f,
                            (world.rand.nextFloat() - 0.5f) * 0.2f, (world.rand.nextFloat() - 0.5f) * 0.2f,
                            new int[0]);
                    pos = next;
                    coord = nextBlock;
                } else {
                    if (world.isAirBlock(nextBlock.up())) {
                        coord = nextBlock.up();
                    }
                    break;
                }
            } else {
                break;
            }
        }

        player.setLocationAndAngles(coord.getX() + 0.5, coord.getY() + 0.25, coord.getZ() + 0.5, player.rotationYaw, player.rotationPitch);
        if (world.isRemote) {
            player.setVelocity(0, 0, 0);
        }

        player.fallDistance = 0;
        playSound(player.getEntityWorld(), next, 12, SoundEvents.ENTITY_ENDERMEN_TELEPORT);
    }
}