package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockFire extends Block {
	private int[] chanceToEncourageFire = new int[256];
	private int[] abilityToCatchFire = new int[256];

	protected BlockFire(int var1, int var2) {
		super(51, 31, Material.fire);
		this.initializeBlock(Block.planks.blockID, 5, 20);
		this.initializeBlock(Block.wood.blockID, 5, 5);
		this.initializeBlock(Block.leaves.blockID, 30, 60);
		this.initializeBlock(Block.bookShelf.blockID, 30, 20);
		this.initializeBlock(Block.tnt.blockID, 15, 100);

		for(var1 = 0; var1 < 16; ++var1) {
			this.initializeBlock(Block.clothRed.blockID + var1, 30, 60);
		}

		this.setTickOnLoad(true);
	}

	private void initializeBlock(int var1, int var2, int var3) {
		this.chanceToEncourageFire[var1] = var2;
		this.abilityToCatchFire[var1] = var3;
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int getRenderType() {
		return 3;
	}

	public final int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	public final boolean isCollidable() {
		return false;
	}

	public final boolean canBlockCatchFire(World var1, int var2, int var3, int var4) {
		return this.chanceToEncourageFire[var1.getBlockId(var2, var3, var4)] > 0;
	}

	public final boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
        return var1.isBlockNormalCube(var2, var3 - 1, var4) || (this.canBlockCatchFire(var1, var2 + 1, var3, var4) ? true : (this.canBlockCatchFire(var1, var2 - 1, var3, var4) ? true : (this.canBlockCatchFire(var1, var2, var3 - 1, var4) ? true : (this.canBlockCatchFire(var1, var2, var3 + 1, var4) ? true : (this.canBlockCatchFire(var1, var2, var3, var4 - 1) ? true : this.canBlockCatchFire(var1, var2, var3, var4 + 1))))));
    }
}
