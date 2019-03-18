package com.mcmoddev.wonderfulwands.common.items;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class WandOfWebbing extends Wand {

    public static int cooldown = 10;
    static final int MAX_RANGE = 64;
    public static int defaultCharges = 64;
    public static final String itemName = "wand_webbing";

    public WandOfWebbing() {
        super(defaultCharges);
        setTranslationKey(WonderfulWands.MODID + "_" + itemName);
    }

    @Override
    public int getRepairCost() {
        return 1;
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
        }

        Vec3d vector = player.getLookVec();
        Vec3d origin = (new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ)).add(vector);
        boolean success = placeSpiderWeb(world, origin, vector, MAX_RANGE);
        if (success) {
            playSound(SoundEvents.ENTITY_SPIDER_AMBIENT, world, player);
            if (player instanceof EntityPlayer && !((EntityPlayer) player).capabilities.isCreativeMode) {
                stack.damageItem(1, player);
            }
        }
    }

    private boolean placeSpiderWeb(World world, Vec3d start, Vec3d velocity, int rangeLimit) {
        BlockPos block = new BlockPos(start);
        if (world.isAirBlock(block)) {
            Vec3d pos = start;
            for (int i = 0; i < rangeLimit; i++) {
                Vec3d next = pos.add(velocity);
                BlockPos nextBlock = new BlockPos(next);
                if (world.isAirBlock(nextBlock)) {
                    // keep moving
                    world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, pos.x, pos.y, pos.z,
                            (world.rand.nextFloat() - 0.5f) * 0.2f, (world.rand.nextFloat() - 0.5f) * 0.2f,
                            (world.rand.nextFloat() - 0.5f) * 0.2f, new int[0]);
                    pos = next;
                    block = nextBlock;
                    if (pos.y < 0) {
                        pos = new Vec3d(pos.x, 0, pos.y);
                        break;
                    }
                    if (pos.y > 255) {
                        pos = new Vec3d(pos.x, 255, pos.y);
                        break;
                    }
                } else {
                    //place mage light
                    break;
                }
            }
            if (!world.isRemote) {
                world.setBlockState(block, Blocks.WEB.getDefaultState());
                Random r = world.rand;
                for (int dx = -2; dx <= 2; dx++) {
                    for (int dy = -2; dy <= 2; dy++) {
                        for (int dz = -2; dz <= 2; dz++) {
                            BlockPos p = block.add(dx, dy, dz);
                            if (world.isAirBlock(p) && r.nextInt(5) == 0) {
                                world.setBlockState(p, Blocks.WEB.getDefaultState());
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}