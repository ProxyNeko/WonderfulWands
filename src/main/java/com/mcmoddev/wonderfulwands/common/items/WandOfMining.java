package com.mcmoddev.wonderfulwands.common.items;

import com.mcmoddev.wonderfulwands.WonderfulWands;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandOfMining extends Wand {

    public static final String itemName = "wand_mining";
    public static int cooldown = 10;
    public static int defaultCharges = 256;

    public WandOfMining() {
        super(defaultCharges);
        setTranslationKey(WonderfulWands.MODID + "_" + itemName);
        setMaxDamage(defaultCharges + 1);
    }

    @Override
    public int getRepairCost() {
        return 1;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing blockFace, float hitX, float hitY, float hitZ) {
        if (!player.capabilities.isCreativeMode) {
            if (isOutOfCharge(stack)) {
                playSound(noChargeSound, world, player);
                return true;
            }
        }
        boolean success = mineBlock(player, world, pos);
        if (success) {
            if (!player.capabilities.isCreativeMode) {
                stack.damageItem(1, player);
            }
        }
        return success;
    }

    private final ItemStack fauxPick = new ItemStack(Items.IRON_PICKAXE);

    /**
     * Acts like iron pickaxe
     *
     * @param player
     * @param world
     * @return True if anything happened, false otherwise (invalid target)
     */
    protected boolean mineBlock(EntityPlayer player, World world, BlockPos pos) {
        IBlockState target = world.getBlockState(pos);
        if (target.getBlock() == Blocks.BEDROCK) {
            return false;
        }

        if (fauxPick.canHarvestBlock(target) || target.getBlockHardness(world, pos) < 1.0F) {
            int fortuneLevel = 0;
            world.setBlockToAir(pos);
            target.getBlock().dropBlockAsItemWithChance(world, pos, target, 1F, fortuneLevel);
            return true;
        }
        return false;
    }
}