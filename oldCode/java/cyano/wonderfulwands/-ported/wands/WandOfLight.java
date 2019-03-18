package cyano.wonderfulwands.wands;

import cyano.wonderfulwands.WonderfulWandsOld;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WandOfLight extends Wand {
	public static final String itemName = "wand_light";

	public static int cooldown = 10;
	
	public static int defaultCharges = 256;
	
	static final int MAX_RANGE = 64;
	
	public WandOfLight() {
		super(defaultCharges);
		this.setTranslationKey(WonderfulWandsOld.MODID +"_"+ itemName);
	}


	@Override
	public int getBaseRepairCost() {
		return 1;
	}
	@Override public int getMaxItemUseDuration(ItemStack par1ItemStack){
		return 1200;
	}
	@Override public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.BOW;
    }

	@Override  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer playerEntity, EnumHand hand){
		playerEntity.setActiveHand(hand);
		return  new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerEntity.getHeldItemMainhand());
	 }
	@Override public boolean onItemUse(ItemStack srcItemStack, EntityPlayer playerEntity, World world, BlockPos coord, EnumFacing blockFace, float par8, float par9, float par10){
		return false;
	}
	@Override public void onPlayerStoppedUsing (ItemStack srcItemStack, World world, EntityLivingBase playerEntity, int timeRemain){
		int chargetime = this.getMaxItemUseDuration(srcItemStack) - timeRemain;
		if(chargetime < 3) return;
		if (playerEntity instanceof EntityPlayer && !((EntityPlayer)playerEntity).capabilities.isCreativeMode)
		{
			if(isOutOfCharge(srcItemStack)){
				// wand out of magic
				playSound(noChargeAttackSound,world,playerEntity);
				return;
			}
		}
		
		Vec3d vector = playerEntity.getLookVec();
		Vec3d origin = (new Vec3d(playerEntity.posX, playerEntity.posY + playerEntity.getEyeHeight(), playerEntity.posZ)).add(vector);
		
		boolean success = placeMageLight(world, origin, vector, MAX_RANGE);
		
		if(success){
	        playSound(SoundEvents.BLOCK_NOTE_PLING,world,playerEntity);
			if (playerEntity instanceof EntityPlayer && !((EntityPlayer)playerEntity).capabilities.isCreativeMode)
			{
				srcItemStack.damageItem(1, playerEntity);
			}
		}
		
	}

	private boolean placeMageLight(World w, Vec3d start,  Vec3d velocity, int rangeLimit) {
		BlockPos block = new BlockPos(start);
		if(w.isAirBlock(block)){
			Vec3d pos = start;
			for(int i = 0; i < rangeLimit; i++){
				Vec3d next = pos.add(velocity);
				BlockPos nextBlock = new BlockPos(next);
				if(w.isAirBlock(nextBlock)){
					// keep moving
					w.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, pos.x, pos.y, pos.z, 
							(w.rand.nextFloat() - 0.5f) * 0.2f, (w.rand.nextFloat() - 0.5f) * 0.2f, (w.rand.nextFloat() - 0.5f) * 0.2f,
							new int[0]);
					pos = next;
					block = nextBlock;
					if(pos.y < 0 ){
						pos = new Vec3d(pos.x, 0, pos.y);
						break;
					}
					if(pos.y > 255){
						pos = new Vec3d(pos.x, 255, pos.y);
						break;
					}
				} else {
					//place mage light
					break;
				}
			}
			if(!w.isRemote){
				w.setBlockState(block, WonderfulWandsOld.mageLight.getDefaultState());
			}
			return true;
		}
		return false;
	}

}
