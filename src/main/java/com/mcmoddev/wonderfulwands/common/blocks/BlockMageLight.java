package com.mcmoddev.wonderfulwands.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockMageLight extends Block {

    private final AxisAlignedBB bounds;

    public BlockMageLight() {
        super(Material.CIRCUITS);
        setHardness(0.0F);
        setLightLevel(1F);
        setSoundType(SoundType.GLASS);
        float f = 0.25F;
        float min = 0.5F - f;
        float max = 0.5F + f;
        bounds = new AxisAlignedBB(min, min, min, max, max, max);
    }


    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos blockPos) {
        return NULL_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World world, BlockPos blockPos) {
        return bounds.offset(blockPos);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune) {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState blockState, World world, BlockPos blockPos, Random random) {
        final double d0 = blockPos.getX() + 0.5;
        final double d1 = blockPos.getY() + 0.5;
        final double d2 = blockPos.getZ() + 0.5;
        final double d3 = 0.22;
        final double d4 = 0.27;

        world.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0, 0.0, 0.0, new int[0]);
    }
}