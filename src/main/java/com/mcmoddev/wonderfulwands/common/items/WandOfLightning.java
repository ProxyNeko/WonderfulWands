package com.mcmoddev.wonderfulwands.common.items;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import com.mcmoddev.wonderfulwands.common.projectiles.EntityBoltLightning;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class WandOfLightning extends Wand {

    public static final String itemName = "wand_lightning";
    public static int cooldown = 20;
    public static final double rangeMax = 16;
    public static final double radius = 2;
    public static int defaultCharges = 64;
    public static final float damage = 8;

    public WandOfLightning() {
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

    private final double piOver2 = Math.PI / 2;

    /**
     * This method is invoked after the item has been used for an amount of time equal to the duration
     * provided to the EntityPlayer.setItemInUse(stack, duration).
     */
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase player) {
        if (player instanceof EntityPlayer && !((EntityPlayer) player).capabilities.isCreativeMode) {
            if (isOutOfCharge(stack)) {
                // wand out of magic
                playSound(noChargeSound, world, player);
                return stack;
            }
            stack.damageItem(1, player);
        }

        if (!world.isRemote) {
            // zap all entities in a 16 block long, 4 block wide cylinder...
            // first, calculate range limit (which will be shorter if we hit a solid block)
            double vecX = (double) (-MathHelper.sin(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI));
            double vecY = (double) (-MathHelper.sin(player.rotationPitch / 180.0F * (float) Math.PI));
            double vecZ = (double) (MathHelper.cos(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI));

            double range = 0;
            double nx = player.posX, ny = player.posY + 1, nz = player.posZ;
            while (range < rangeMax) {
                if (world.getBlockState(new BlockPos((int) nx, (int) ny, (int) nz)).isOpaqueCube()) {
                    break;
                }
                nx += vecX;
                ny += vecY;
                nz += vecZ;
                range += 1;
            }

            if (range < 2) {
                range = 2;
            }

            final double rangeSqr = range * range;
            vecX *= range;
            vecY *= range;
            vecZ *= range;

            EntityLightningBolt fakeBolt = new EntityLightningBolt(world, nx, ny, nz, true);

            AxisAlignedBB bb = new AxisAlignedBB(player.posX - range, player.posY - range,
                    player.posZ - range, player.posX + range, player.posY + range, player.posZ + range);
            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
            for (int i = 0; i < entities.size(); i++) {
                if (entities.get(i) != null) {
                    continue;
                }

                EntityLivingBase entity = entities.get(i);
                if (player == entity) {
                    continue;
                }

                double dx = (entity.posX - player.posX);
                double dy = (entity.posY - player.posY);
                double dz = (entity.posZ - player.posZ);
                double distSqr = dx * dx + dy * dy + dz * dz;
                if (distSqr < rangeSqr) {
                    // target in range, but is it in AoE?
                    double dist = Math.sqrt(distSqr);
                    double angle = Math.acos(dotProduct(dx, dy, dz, vecX, vecY, vecZ) / (dist * range));
                    if (angle < piOver2) {
                        double perpendicular = dist * Math.sin(angle);
                        if (perpendicular < radius) {
                            // in AoE
                            entity.attackEntityFrom(DamageSource.MAGIC, damage);
                            entity.onStruckByLightning(fakeBolt);
                        }
                    }
                }
            }

            playFadedSound(world, player.getPositionVector(), 64, SoundEvents.ENTITY_GENERIC_EXPLODE, 4F, 1.7F);
            playSound(world, player.getPositionVector(), 64, SoundEvents.ENTITY_LIGHTNING_THUNDER, 1F, 1.7F);

            world.spawnEntity(new EntityBoltLightning(world, player, player.posX, player.posY + 1, player.posZ,
                    player.rotationYaw, player.rotationPitch, range / rangeMax));
        }
        return stack;
    }

    private static double dotProduct(double x1, double y1, double z1, double x2, double y2, double z2) {
        return x1 * x2 + y1 * y2 + z1 * z2;
    }
}