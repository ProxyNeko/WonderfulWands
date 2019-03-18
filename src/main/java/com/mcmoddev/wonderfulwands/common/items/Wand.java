package com.mcmoddev.wonderfulwands.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public abstract class Wand extends Item {

    //TODO Set the cooldown for each wand properly. -ProxyNeko
    public abstract int getRepairCost();
    private List<ItemStack> allowedItems = null;
    public static SoundEvent noChargeSound = SoundEvents.ENTITY_ITEM_PICKUP;

    //Todo See if this can be replaced with the vanilla version. (Not sure about this but we will see) -ProxyNeko
    //protected abstract boolean onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ);
    protected abstract boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ);

    public Wand(int numCharges) {
        super();
        maxStackSize = 1;
        setMaxDamage(numCharges + 1);
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BLOCK;
    }

    /**
     * returns true if the wand is on its last damage point
     */
    public boolean isOutOfCharge(ItemStack stack) {
        return stack.getItemDamage() >= (stack.getMaxDamage() - 1);
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack material) {
        // repair with gold ingots
        if (allowedItems == null) {
            allowedItems = OreDictionary.getOres("ingotGold");
        }

        for (int i = 0; i < allowedItems.size(); i++) {
            if (allowedItems.get(i).getTranslationKey().equals(material.getTranslationKey())) {
                return true;
            }
        }
        return false;
    }

    /**
     * plays a sound at the player location
     */
    protected void playSound(SoundEvent sound, World world, Entity player) {
        playSound(world, player.getPositionVector().add(0, 1, 0), 12, sound);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float x, float y, float z) {
        if (onItemUse(player.getHeldItemMainhand(), player, world, pos, facing, x, y, z)) {
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        StringBuilder builder = new StringBuilder();
        int max = stack.getMaxDamage() - 1;
        builder.append(max - stack.getItemDamage()).append('/').append(max);
        tooltip.add(builder.toString());
    }

    protected void playSound(World world, Vec3d position, double range, SoundEvent sound) {
        playSound(world, position, range, sound, 1.0F, 1.0F);
    }

    protected void playSound(World world, Vec3d position, double range, SoundEvent sound, float volume, float pitch) {
        if (world.isRemote) {
            return;
        }

        AxisAlignedBB area = new AxisAlignedBB(
                position.x - range, position.y - range, position.z - range,
                position.x + range, position.y + range, position.z + range
        );
        List<EntityPlayerMP> players = world.getEntitiesWithinAABB(EntityPlayerMP.class, area);
        SPacketCustomSound soundPacket = new SPacketCustomSound(sound.getRegistryName().toString(),
                SoundCategory.PLAYERS, position.x, position.y, position.z, volume, pitch);
        for (EntityPlayerMP player : players) {
            player.connection.sendPacket(soundPacket);
        }
    }

    protected void playFadedSound(World world, Vec3d position, double range, SoundEvent sound, float volume, float pitch) {
        if (world.isRemote) {
            return;
        }

        AxisAlignedBB area = new AxisAlignedBB(
                position.x - range, position.y - range, position.z - range,
                position.x + range, position.y + range, position.z + range
        );
        List<EntityPlayerMP> players = world.getEntitiesWithinAABB(EntityPlayerMP.class, area);
        for (EntityPlayerMP player : players) {
            float distSqr = (float) player.getPositionVector().squareDistanceTo(position);
            float localVolume = Math.min(volume, volume / distSqr);
            SPacketCustomSound soundPacket = new SPacketCustomSound(sound.getRegistryName().toString(),
                    SoundCategory.PLAYERS, position.x, position.y, position.z, localVolume, pitch);
            player.connection.sendPacket(soundPacket);
        }
    }
}