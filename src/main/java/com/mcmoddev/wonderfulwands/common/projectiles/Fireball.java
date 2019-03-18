package com.mcmoddev.wonderfulwands.common.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class Fireball extends EntityLargeFireball {

    public Fireball(World world, EntityLivingBase entity, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(world, entity, accelX, accelY, accelZ);
        setPosition(posX, posY, posZ);
        Double d3 = (double) MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        accelerationX = accelX / d3 * 0.1D;
        accelerationY = accelY / d3 * 0.1D;
        accelerationZ = accelZ / d3 * 0.1D;
    }

    @Override
    protected void onImpact(RayTraceResult impact) {
        if (!this.world.isRemote) {
            double radius = 2.0;
            if (impact.hitVec != null) {
                AxisAlignedBB areaOfEffect = new AxisAlignedBB(impact.hitVec.x - radius, impact.hitVec.y - radius,
                        impact.hitVec.z - radius, impact.hitVec.x + radius, impact.hitVec.y + radius,
                        impact.hitVec.z + radius);
                List<EntityLivingBase> collateralDamage = world.getEntitiesWithinAABB(EntityLivingBase.class, areaOfEffect);
                for (EntityLivingBase victim : collateralDamage) {
                    victim.attackEntityFrom(DamageSource.MAGIC, 5);
                    victim.setFire(5);
                }
            }
        }
        super.onImpact(impact);
    }
}