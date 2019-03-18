package com.mcmoddev.wonderfulwands.common.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityMagicMissile extends EntityArrow {

    public EntityMagicMissile(World world) {
        super(world);
        init();
    }

    private void init() {
        pickupStatus = PickupStatus.DISALLOWED;
        setDamage(2.5);
    }

    public EntityMagicMissile(World world, double posX, double posY, double posZ) {
        super(world, posX, posY, posZ);
        init();
    }

    public EntityMagicMissile(World world, EntityLivingBase shooter) {
        super(world, shooter);
        init();
        shoot(shooter, shooter.rotationPitch, shooter.rotationYaw, 0.0F, 3.0F, 1.0F);
    }

    @Override
    protected ItemStack getArrowStack() {
        return null;
    }
}