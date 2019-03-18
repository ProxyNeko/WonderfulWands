package com.mcmoddev.wonderfulwands.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class IllusoryBlock extends Block {

    public final String name;
    public static BlockPos pos;
    public static IBlockAccess access;
    private static final Map<Block, IllusoryBlock> blockLookup = new HashMap<>();

    public IllusoryBlock(Block sourceBlock) {
        this(
                sourceBlock.getMapColor(sourceBlock.getDefaultState(), access, pos),
                "illusion_" + (sourceBlock.getTranslationKey().replace("tile.", "")),
                sourceBlock, sourceBlock.getTranslationKey().replace("tile.", "")
        );
    }

    public IllusoryBlock(MapColor color, String name, Block sourceBlock) {
        this(color, name, sourceBlock, sourceBlock.getTranslationKey().replace("tile.", ""));
    }

    public IllusoryBlock(MapColor color, String name, Block sourceBlock, String sourceBlockName) {
        this(color, name, sourceBlock, sourceBlockName, sourceBlockName);
    }

    public IllusoryBlock(MapColor color, String name, Block sourceBlock, String sourceBlockName, String sourceBlockUnlocalizedName) {
        super(Material.CARPET, color);
        setHardness(0.0F);
        setSoundType(sourceBlock.getSoundType());
        this.name = name;
        setTranslationKey(sourceBlockUnlocalizedName);
        blockLookup.put(sourceBlock, this);
    }

    public static IllusoryBlock getIllusionForBlock(Block block) {
        return blockLookup.get(block);
    }

    public static Map<Block, IllusoryBlock> getLookUpTable() {
        return Collections.unmodifiableMap(blockLookup);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos blockPos) {
        return NULL_AABB;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune) {
        return null;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos blockPos, IBlockState state, EntityPlayer player) {
        return true;
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
        return BlockRenderLayer.SOLID;
    }
}