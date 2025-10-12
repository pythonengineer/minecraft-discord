package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.IInventory;
import net.minecraft.game.InventoryLargeChest;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockChest extends BlockContainer {
	protected BlockChest(int var1) {
		super(54, Material.wood);
		new EaglercraftRandom();
		this.blockIndexInTexture = 26;
	}

	public final int getBlockTexture(World var1, int var2, int var3, int var4, int var5) {
		if(var5 == 1) {
			return this.blockIndexInTexture - 1;
		} else if(var5 == 0) {
			return this.blockIndexInTexture - 1;
		} else {
			int var6 = var1.getBlockId(var2, var3, var4 - 1);
			int var7 = var1.getBlockId(var2, var3, var4 + 1);
			int var8 = var1.getBlockId(var2 - 1, var3, var4);
			int var9 = var1.getBlockId(var2 + 1, var3, var4);
			int var10;
			int var11;
			int var12;
			byte var13;
			if(var6 != this.blockID && var7 != this.blockID) {
				if(var8 != this.blockID && var9 != this.blockID) {
					byte var14 = 3;
					if(Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var7]) {
						var14 = 3;
					}

					if(Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var6]) {
						var14 = 2;
					}

					if(Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var9]) {
						var14 = 5;
					}

					if(Block.opaqueCubeLookup[var9] && !Block.opaqueCubeLookup[var8]) {
						var14 = 4;
					}

					return var5 == var14 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
				} else if(var5 != 4 && var5 != 5) {
					var10 = 0;
					if(var8 == this.blockID) {
						var10 = -1;
					}

					var11 = var1.getBlockId(var8 == this.blockID ? var2 - 1 : var2 + 1, var3, var4 - 1);
					var12 = var1.getBlockId(var8 == this.blockID ? var2 - 1 : var2 + 1, var3, var4 + 1);
					if(var5 == 3) {
						var10 = -1 - var10;
					}

					var13 = 3;
					if((Block.opaqueCubeLookup[var6] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var12]) {
						var13 = 3;
					}

					if((Block.opaqueCubeLookup[var7] || Block.opaqueCubeLookup[var12]) && !Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var11]) {
						var13 = 2;
					}

					return (var5 == var13 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + var10;
				} else {
					return this.blockIndexInTexture;
				}
			} else if(var5 != 2 && var5 != 3) {
				var10 = 0;
				if(var6 == this.blockID) {
					var10 = -1;
				}

				var11 = var1.getBlockId(var2 - 1, var3, var6 == this.blockID ? var4 - 1 : var4 + 1);
				var12 = var1.getBlockId(var2 + 1, var3, var6 == this.blockID ? var4 - 1 : var4 + 1);
				if(var5 == 4) {
					var10 = -1 - var10;
				}

				var13 = 5;
				if((Block.opaqueCubeLookup[var8] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var9] && !Block.opaqueCubeLookup[var12]) {
					var13 = 5;
				}

				if((Block.opaqueCubeLookup[var9] || Block.opaqueCubeLookup[var12]) && !Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var11]) {
					var13 = 4;
				}

				return (var5 == var13 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + var10;
			} else {
				return this.blockIndexInTexture;
			}
		}
	}

	public final int getBlockTextureFromSide(int var1) {
		return var1 == 1 ? this.blockIndexInTexture - 1 : (var1 == 0 ? this.blockIndexInTexture - 1 : (var1 == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture));
	}

	public final boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		int var5 = 0;
		if(var1.getBlockId(var2 - 1, var3, var4) == this.blockID) {
			++var5;
		}

		if(var1.getBlockId(var2 + 1, var3, var4) == this.blockID) {
			++var5;
		}

		if(var1.getBlockId(var2, var3, var4 - 1) == this.blockID) {
			++var5;
		}

		if(var1.getBlockId(var2, var3, var4 + 1) == this.blockID) {
			++var5;
		}

		return var5 > 1 ? false : (this.isThereANeighborChest(var1, var2 - 1, var3, var4) ? false : (this.isThereANeighborChest(var1, var2 + 1, var3, var4) ? false : (this.isThereANeighborChest(var1, var2, var3, var4 - 1) ? false : !this.isThereANeighborChest(var1, var2, var3, var4 + 1))));
	}

	private boolean isThereANeighborChest(World var1, int var2, int var3, int var4) {
		return var1.getBlockId(var2, var3, var4) != this.blockID ? false : (var1.getBlockId(var2 - 1, var3, var4) == this.blockID ? true : (var1.getBlockId(var2 + 1, var3, var4) == this.blockID ? true : (var1.getBlockId(var2, var3, var4 - 1) == this.blockID ? true : var1.getBlockId(var2, var3, var4 + 1) == this.blockID)));
	}

	public final boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		InventoryLargeChest var6 = null;
        if(var1.isBlockNormalCube(var2, var3 + 1, var4)) {
            return true;
        } else if(var1.getBlockId(var2 - 1, var3, var4) == this.blockID && var1.isBlockNormalCube(var2 - 1, var3 + 1, var4)) {
            return true;
        } else if(var1.getBlockId(var2 + 1, var3, var4) == this.blockID && var1.isBlockNormalCube(var2 + 1, var3 + 1, var4)) {
            return true;
        } else if(var1.getBlockId(var2, var3, var4 - 1) == this.blockID && var1.isBlockNormalCube(var2, var3 + 1, var4 - 1)) {
            return true;
        } else if(var1.getBlockId(var2, var3, var4 + 1) == this.blockID && var1.isBlockNormalCube(var2, var3 + 1, var4 + 1)) {
            return true;
		} else {
			if(var1.getBlockId(var2 - 1, var3, var4) == this.blockID) {
				var6 = new InventoryLargeChest("Large chest", (IInventory)null, (IInventory)null);
			}

			if(var1.getBlockId(var2 + 1, var3, var4) == this.blockID) {
				var6 = new InventoryLargeChest("Large chest", var6, (IInventory)null);
			}

			if(var1.getBlockId(var2, var3, var4 - 1) == this.blockID) {
				var6 = new InventoryLargeChest("Large chest", (IInventory)null, var6);
			}

			if(var1.getBlockId(var2, var3, var4 + 1) == this.blockID) {
				var6 = new InventoryLargeChest("Large chest", var6, (IInventory)null);
			}

			var5.displayGUIChest(var6);
			return true;
		}
	}
}
