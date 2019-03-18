package com.mcmoddev.wonderfulwands.common.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBoltLightning extends Entity {

    private int life = 0;
    private int tileX = 0, tileY = 0, tileZ = 0;
    private double length = 1;
    private EntityLivingBase owner;

    public EntityBoltLightning(World world) {
        super(world);
        posX = 0;
        posY = 0;
        posZ = 0;
        rotationPitch = 0;
        rotationYaw = 0;
    }

    public EntityBoltLightning(World world, EntityLivingBase owner, double posX, double posY, double posZ, float yaw, float pitch, double length) {
        super(world);
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        tileX = (int) posX;
        tileY = (int) posY;
        tileZ = (int) posZ;
        rotationPitch = pitch;
        rotationYaw = yaw;
        this.owner = owner;
        this.length = length;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        final int lifeSpan = 13;
        if (++life > lifeSpan) {
            setDead();
        }
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        tileX = tag.getShort("xTile");
        tileY = tag.getShort("yTile");
        tileZ = tag.getShort("zTile");
        length = tag.getByte("len") * 0.01d;

        if (tag.hasKey("direction", 9)) {
            NBTTagList nbttaglist = tag.getTagList("direction", 6);
            motionX = nbttaglist.getFloatAt(0);
            motionY = nbttaglist.getFloatAt(1);
            motionZ = nbttaglist.getFloatAt(2);
        } else {
            setDead();
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        tag.setShort("xTile", (short) this.tileX);
        tag.setShort("yTile", (short) this.tileY);
        tag.setShort("zTile", (short) this.tileZ);
        tag.setByte("len", (byte) (this.length * 100f));
        tag.setTag("direction", this.newFloatNBTList((float) this.motionX, (float) this.motionY, (float) this.motionZ));
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void setPositionAndRotation(double posX, double posY, double posZ, float yaw, float pitch) {
        setPosition(posX, posY, posZ);
        setRotation(yaw, pitch);
    }
}