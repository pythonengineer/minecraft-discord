package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.IInventory;
import net.minecraft.game.InventoryLargeChest;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.block.tileentity.TileEntityChest;
import net.minecraft.game.world.material.Material;

public final class BlockChest extends BlockContainer {
	private EaglercraftRandom random = new EaglercraftRandom();

	protected BlockChest(int blockID) {
		super(54, Material.ground);
		this.blockIndexInTexture = 26;
	}

	public final int getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
		if(side == 1) {
			return this.blockIndexInTexture - 1;
		} else if(side == 0) {
			return this.blockIndexInTexture - 1;
		} else {
			int i6 = iBlockAccess.getBlockId(x, y, z - 1);
			int i7 = iBlockAccess.getBlockId(x, y, z + 1);
			int i8 = iBlockAccess.getBlockId(x - 1, y, z);
			int i9 = iBlockAccess.getBlockId(x + 1, y, z);
			int i10;
			int i11;
			int world1;
			byte x1;
			if(i6 != this.blockID && i7 != this.blockID) {
				if(i8 != this.blockID && i9 != this.blockID) {
					byte b14 = 3;
					if(Block.opaqueCubeLookup[i6] && !Block.opaqueCubeLookup[i7]) {
						b14 = 3;
					}

					if(Block.opaqueCubeLookup[i7] && !Block.opaqueCubeLookup[i6]) {
						b14 = 2;
					}

					if(Block.opaqueCubeLookup[i8] && !Block.opaqueCubeLookup[i9]) {
						b14 = 5;
					}

					if(Block.opaqueCubeLookup[i9] && !Block.opaqueCubeLookup[i8]) {
						b14 = 4;
					}

					return side == b14 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
				} else if(side != 4 && side != 5) {
					i10 = 0;
					if(i8 == this.blockID) {
						i10 = -1;
					}

					i11 = iBlockAccess.getBlockId(i8 == this.blockID ? x - 1 : x + 1, y, z - 1);
					world1 = iBlockAccess.getBlockId(i8 == this.blockID ? x - 1 : x + 1, y, z + 1);
					if(side == 3) {
						i10 = -1 - i10;
					}

					x1 = 3;
					if((Block.opaqueCubeLookup[i6] || Block.opaqueCubeLookup[i11]) && !Block.opaqueCubeLookup[i7] && !Block.opaqueCubeLookup[world1]) {
						x1 = 3;
					}

					if((Block.opaqueCubeLookup[i7] || Block.opaqueCubeLookup[world1]) && !Block.opaqueCubeLookup[i6] && !Block.opaqueCubeLookup[i11]) {
						x1 = 2;
					}

					return (side == x1 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + i10;
				} else {
					return this.blockIndexInTexture;
				}
			} else if(side != 2 && side != 3) {
				i10 = 0;
				if(i6 == this.blockID) {
					i10 = -1;
				}

				i11 = iBlockAccess.getBlockId(x - 1, y, i6 == this.blockID ? z - 1 : z + 1);
				world1 = iBlockAccess.getBlockId(x + 1, y, i6 == this.blockID ? z - 1 : z + 1);
				if(side == 4) {
					i10 = -1 - i10;
				}

				x1 = 5;
				if((Block.opaqueCubeLookup[i8] || Block.opaqueCubeLookup[i11]) && !Block.opaqueCubeLookup[i9] && !Block.opaqueCubeLookup[world1]) {
					x1 = 5;
				}

				if((Block.opaqueCubeLookup[i9] || Block.opaqueCubeLookup[world1]) && !Block.opaqueCubeLookup[i8] && !Block.opaqueCubeLookup[i11]) {
					x1 = 4;
				}

				return (side == x1 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + i10;
			} else {
				return this.blockIndexInTexture;
			}
		}
	}

	public final int getBlockTextureFromSide(int side) {
		return side == 1 ? this.blockIndexInTexture - 1 : (side == 0 ? this.blockIndexInTexture - 1 : (side == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture));
	}

	public final boolean canPlaceBlockAt(World world, int x, int y, int z) {
		int i5 = 0;
		if(world.getBlockId(x - 1, y, z) == this.blockID) {
			++i5;
		}

		if(world.getBlockId(x + 1, y, z) == this.blockID) {
			++i5;
		}

		if(world.getBlockId(x, y, z - 1) == this.blockID) {
			++i5;
		}

		if(world.getBlockId(x, y, z + 1) == this.blockID) {
			++i5;
		}

		return i5 > 1 ? false : (this.isThereANeighborChest(world, x - 1, y, z) ? false : (this.isThereANeighborChest(world, x + 1, y, z) ? false : (this.isThereANeighborChest(world, x, y, z - 1) ? false : !this.isThereANeighborChest(world, x, y, z + 1))));
	}

	private boolean isThereANeighborChest(World world, int x, int y, int z) {
		return world.getBlockId(x, y, z) != this.blockID ? false : (world.getBlockId(x - 1, y, z) == this.blockID ? true : (world.getBlockId(x + 1, y, z) == this.blockID ? true : (world.getBlockId(x, y, z - 1) == this.blockID ? true : world.getBlockId(x, y, z + 1) == this.blockID)));
	}

	public final void onBlockRemoval(World world, int x, int y, int z) {
		TileEntityChest tileEntityChest5 = (TileEntityChest)world.getBlockTileEntity(x, y, z);

		for(int i6 = 0; i6 < tileEntityChest5.getSizeInventory(); ++i6) {
			ItemStack itemStack7;
			if((itemStack7 = tileEntityChest5.getStackInSlot(i6)) != null) {
				float f8 = this.random.nextFloat() * 0.8F + 0.1F;
				float f9 = this.random.nextFloat() * 0.8F + 0.1F;
				float f10 = this.random.nextFloat() * 0.8F + 0.1F;

				while(itemStack7.stackSize > 0) {
					int i11;
					if((i11 = this.random.nextInt(21) + 10) > itemStack7.stackSize) {
						i11 = itemStack7.stackSize;
					}

					itemStack7.stackSize -= i11;
					EntityItem entityItem12;
					(entityItem12 = new EntityItem(world, (double)((float)x + f8), (double)((float)y + f9), (double)((float)z + f10), new ItemStack(itemStack7.itemID, i11, itemStack7.itemDamage))).motionZ = (double)((float)this.random.nextGaussian() * 0.05F);
					entityItem12.motionY = (double)((float)this.random.nextGaussian() * 0.05F + 0.2F);
					entityItem12.motionX = (double)((float)this.random.nextGaussian() * 0.05F);
					world.entityJoinedWorld(entityItem12);
				}
			}
		}

		super.onBlockRemoval(world, x, y, z);
	}

	public final boolean blockActivated(World world, int x, int y, int z, EntityPlayer playerEntity) {
		Object object6 = (TileEntityChest)world.getBlockTileEntity(x, y, z);
		if(world.isBlockNormalCube(x, y + 1, z)) {
			return true;
		} else if(world.getBlockId(x - 1, y, z) == this.blockID && world.isBlockNormalCube(x - 1, y + 1, z)) {
			return true;
		} else if(world.getBlockId(x + 1, y, z) == this.blockID && world.isBlockNormalCube(x + 1, y + 1, z)) {
			return true;
		} else if(world.getBlockId(x, y, z - 1) == this.blockID && world.isBlockNormalCube(x, y + 1, z - 1)) {
			return true;
		} else if(world.getBlockId(x, y, z + 1) == this.blockID && world.isBlockNormalCube(x, y + 1, z + 1)) {
			return true;
		} else {
			if(world.getBlockId(x - 1, y, z) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (TileEntityChest)world.getBlockTileEntity(x - 1, y, z), (IInventory)object6);
			}

			if(world.getBlockId(x + 1, y, z) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (IInventory)object6, (TileEntityChest)world.getBlockTileEntity(x + 1, y, z));
			}

			if(world.getBlockId(x, y, z - 1) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (TileEntityChest)world.getBlockTileEntity(x, y, z - 1), (IInventory)object6);
			}

			if(world.getBlockId(x, y, z + 1) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (IInventory)object6, (TileEntityChest)world.getBlockTileEntity(x, y, z + 1));
			}

			playerEntity.displayGUIChest((IInventory)object6);
			return true;
		}
	}

	protected final TileEntity getBlockEntity() {
		return new TileEntityChest();
	}
}