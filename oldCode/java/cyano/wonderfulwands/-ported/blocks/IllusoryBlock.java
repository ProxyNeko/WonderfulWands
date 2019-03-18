package cyano.wonderfulwands.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
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
    private static final Map<Block, IllusoryBlock> blockLookup = new HashMap<Block, IllusoryBlock>();

    public IllusoryBlock(Block sourceBlock) {
        this(
                sourceBlock.getMapColor(sourceBlock.getDefaultState()),
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
        this.setCreativeTab(CreativeTabs.MISC);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.CLOTH);
        this.name = name;
        this.setTranslationKey(sourceBlockUnlocalizedName);
        blockLookup.put(sourceBlock, this);
    }

    public static IllusoryBlock getIllusionForBlock(Block b) {
        return blockLookup.get(b);
    }

    public static Map<Block, IllusoryBlock> getLookUpTable() {
        return Collections.unmodifiableMap(blockLookup);
    }


    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }


    @Override
    public Item getItemDropped(IBlockState state, Random prng, int fortune) {
        return null;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube(IBlockState bs) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState bs) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.SOLID;
    }
}
