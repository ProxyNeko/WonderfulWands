package com.mcmoddev.wonderfulwands.common.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class DeathSkull extends EntityWitherSkull {

    public static float damage = 10f;
    public static final float explosionForce = 3f;

    public DeathSkull(World world) {
        super(world);
    }

    public DeathSkull(World world, EntityLivingBase entity, double accelX, double accelY, double accelZ) {
        super(world, entity, accelX, accelY, accelZ);
    }

    public DeathSkull(World world, EntityLivingBase entity, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(world, entity, accelX, accelY, accelZ);
        this.setPosition(posX, posY, posZ);
        Double d3 = (double) MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d3 * 0.1D;
        this.accelerationY = accelY / d3 * 0.1D;
        this.accelerationZ = accelZ / d3 * 0.1D;
    }

    public DeathSkull(World world, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(world, posX, posY, posZ, accelX, accelY, accelZ);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    @Override
    protected void onImpact(RayTraceResult impact) {
        if (!this.world.isRemote) {
            if (impact.entityHit != null) {
                impact.entityHit.attackEntityFrom(DamageSource.MAGIC, 21);
            }

            double radius = 3;
            if (impact.hitVec != null) {
                AxisAlignedBB areaOfEffect = new AxisAlignedBB(impact.hitVec.x - radius, impact.hitVec.y - radius,
                        impact.hitVec.z - radius, impact.hitVec.x + radius, impact.hitVec.y + radius,
                        impact.hitVec.z + radius);
                List<EntityLivingBase> collateralDamage = world.getEntitiesWithinAABB(EntityLivingBase.class, areaOfEffect);
                PotionEffect wither = new PotionEffect(Potion.getPotionFromResourceLocation("wither"), 210, 1);
                for (EntityLivingBase victim : collateralDamage) {
                    victim.addPotionEffect(wither);
                    victim.attackEntityFrom(DamageSource.MAGIC, 10);
                }
            }
            world.newExplosion(this, this.posX, this.posY, this.posZ, explosionForce, false, this.world.getGameRules().getBoolean("mobGriefing"));
            setDead();
        }
    }
}