package com.mcmoddev.wonderfulwands.common.entities;

import com.mcmoddev.wonderfulwands.api.blocks.WWBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Random;

public class EntityLightWisp extends EntityLivingBase {

    private long nextUpdateTime = 0;
    private short lifeCounter = LIFESPAN;
    private static final short LIFESPAN = 30 * 20;
    private static final long UPDATE_INTERVAL = 8;
    private final double dt = 4.0 / UPDATE_INTERVAL;
    private BlockPos lastPos = new BlockPos(0, 1, 0);

    public EntityLightWisp(World world) {
        super(world);
        lifeCounter = LIFESPAN;
        isImmuneToFire = true;
        nextUpdateTime = world.rand.nextInt((int) UPDATE_INTERVAL);
    }

    public EntityLightWisp(World world, BlockPos startingPosition) {
        super(world);
        posX = startingPosition.getX() + 0.5;
        posY = startingPosition.getY();
        posZ = startingPosition.getZ() + 0.5;
    }

    @Override
    protected void damageEntity(DamageSource damageSource, float amount) {
        // immune to wall suffocation
        if (DamageSource.IN_WALL.equals(damageSource.damageType)) {
            return;
        }
        super.damageEntity(damageSource, amount);
    }

    @Override
    public void readFromNBT(NBTTagCompound root) {
        super.readFromNBT(root);
        if (root.hasKey("t")) {
            this.lifeCounter = root.getShort("t");
        } else {
            this.lifeCounter = LIFESPAN;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound root) {
        //TODO Find and fix the NPE that crashes the game when you use the Wand Of Greater Light
        //Extra note: Can't test this on my own OS as it locks my mouse and keyboard so I can't close it without rebooting the system manually. - Proxy
        super.writeToNBT(root);
        root.setShort("t", this.lifeCounter);
        return root;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if (world.isRemote) {
            return;
        }

        --lifeCounter;
        long time = this.world.getTotalWorldTime();
        if (time > nextUpdateTime) {
            nextUpdateTime = time + UPDATE_INTERVAL;
            BlockPos pos = getPosition();
            int light = getLight(pos);
            if (canPlace(pos) && light < 8) {
                turnIntoMageLight();
            } else {
                wanderFrom(pos, light);
            }
            lastPos = pos;
        }

        if (lifeCounter <= 0) {
            world.removeEntity(this);
        }
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slot) {
        return null;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slot, ItemStack stack) {
        // do nothing
    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.LEFT;
    }

    private int getLight(BlockPos pos) {
        if (pos.getY() < 0 || pos.getY() > 255) {
            return 16;
        }

        IBlockState block = world.getBlockState(pos);
        if (block.isFullBlock()) {
            return 16;
        }

        if (!(world.getChunk(pos).isLoaded())) {
            return 16;
        }
        return world.getChunk(pos).getLightFor(EnumSkyBlock.BLOCK, pos);
    }

    private boolean canPlace(BlockPos pos) {
        return world.isAirBlock(pos);
    }


    public void turnIntoMageLight() {
        world.setBlockState(getPosition(), WWBlocks.MAGE_LIGHT.getDefaultState());
        isDead = true;
        world.removeEntity(this);
    }

    public void wanderFrom(BlockPos pos, int light) {
        // first, check if neighbor is darker than here
        BlockPos target = null;
        BlockPos[] coords = new BlockPos[4];
        coords[0] = pos.north();
        coords[1] = pos.east();
        coords[2] = pos.south();
        coords[3] = pos.west();
        shuffle(coords, getRNG());
        for (int i = 0; i < coords.length; i++) {
            coords[i] = moveToGroundLevel(coords[i]);
            int l = getLight(coords[i]);
            if (l < light) {
                light = l;
                target = coords[i];
            }
        }
        // if target is still null, then we are stuck in a local minimum, look further afield
        if (target == null) {
            coords = new BlockPos[8];
            coords[0] = pos.add(-12, 0, 0);
            coords[1] = pos.add(-8, 0, -8);
            coords[2] = pos.add(0, 0, -12);
            coords[3] = pos.add(8, 0, -8);
            coords[4] = pos.add(12, 0, 0);
            coords[5] = pos.add(8, 0, 8);
            coords[6] = pos.add(0, 0, 12);
            coords[7] = pos.add(-8, 0, 8);
            shuffle(coords, getRNG());

            for (int i = 0; i < coords.length; i++) {
                coords[i] = moveToGroundLevel(coords[i]);
                int l = getLight(coords[i]);
                if (l < light) {
                    light = l;
                    target = coords[i];
                }
            }
        }

        // if target is still null, then we are stuck in a brightly lit area, make long jump in random direction
        if (target == null) {
            final float r = 24;
            float theta = this.getEntityWorld().rand.nextFloat() * 6.2832f;
            float dx = r * MathHelper.sin(theta);
            float dz = r * MathHelper.cos(theta);
            target = moveToGroundLevel(pos.add(dx, 0, dz));
        }

        setPosition(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
    }

    private BlockPos moveToGroundLevel(BlockPos coord) {
        if (world.isAirBlock(coord)) {
            BlockPos down = coord.down();
            while (world.isAirBlock(down) && down.getY() > 0) {
                coord = down;
                down = coord.down();
            }
        } else {
            while (!world.isAirBlock(coord) && coord.getY() < 255) {
                coord = coord.up();
            }
        }
        return coord;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }


    private static void shuffle(Object[] in, Random prng) {
        for (int i = in.length - 1; i > 1; i--) {
            Object o = in[i];
            int r = prng.nextInt(i);
            in[i] = in[r];
            in[r] = o;
        }
    }
}