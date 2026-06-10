package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockLeaves extends BlockLeavesBase {
	private int leafTexIndex;
	private int decayCounter = 0;

	protected BlockLeaves(int id, int tex) {
		super(id, tex, Material.leaves, false);
		this.leafTexIndex = tex;
		this.setTickOnLoad(true);
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		this.decayCounter = 0;
		this.updateCurrentLeaves(world1, i2, i3, i4);
		super.onNeighborBlockChange(world1, i2, i3, i4, i5);
	}

	public void updateConnectedLeaves(World worldObj, int x, int y, int z, int metadata) {
		if(worldObj.getBlockId(x, y, z) == this.blockID) {
			int i6 = worldObj.getBlockMetadata(x, y, z);
			if(i6 != 0 && i6 == metadata - 1) {
				this.updateCurrentLeaves(worldObj, x, y, z);
			}
		}
	}

	public void updateCurrentLeaves(World worldObj, int x, int y, int z) {
		if(this.decayCounter++ < 100) {
			int i5 = worldObj.getBlockMaterial(x, y - 1, z).isSolid() ? 16 : 0;
			int i6 = worldObj.getBlockMetadata(x, y, z);
			if(i6 == 0) {
				i6 = 1;
				worldObj.setBlockMetadataWithNotify(x, y, z, 1);
			}

			i5 = this.getConnectionStrength(worldObj, x, y - 1, z, i5);
			i5 = this.getConnectionStrength(worldObj, x, y, z - 1, i5);
			i5 = this.getConnectionStrength(worldObj, x, y, z + 1, i5);
			i5 = this.getConnectionStrength(worldObj, x - 1, y, z, i5);
			i5 = this.getConnectionStrength(worldObj, x + 1, y, z, i5);
			int i7 = i5 - 1;
			if(i7 < 10) {
				i7 = 1;
			}

			if(i7 != i6) {
				worldObj.setBlockMetadataWithNotify(x, y, z, i7);
				this.updateConnectedLeaves(worldObj, x, y - 1, z, i6);
				this.updateConnectedLeaves(worldObj, x, y + 1, z, i6);
				this.updateConnectedLeaves(worldObj, x, y, z - 1, i6);
				this.updateConnectedLeaves(worldObj, x, y, z + 1, i6);
				this.updateConnectedLeaves(worldObj, x - 1, y, z, i6);
				this.updateConnectedLeaves(worldObj, x + 1, y, z, i6);
			}

		}
	}

	private int getConnectionStrength(World worldObj, int x, int y, int z, int metadata) {
		int i6 = worldObj.getBlockId(x, y, z);
		if(i6 == Block.wood.blockID) {
			return 16;
		} else {
			if(i6 == this.blockID) {
				int i7 = worldObj.getBlockMetadata(x, y, z);
				if(i7 != 0 && i7 > metadata) {
					return i7;
				}
			}

			return metadata;
		}
	}

	public void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		int i6 = world.getBlockMetadata(x, y, z);
		if(i6 == 0) {
			this.decayCounter = 0;
			this.updateCurrentLeaves(world, x, y, z);
		} else if(i6 == 1) {
			this.removeLeaves(world, x, y, z);
		} else if(rand.nextInt(10) == 0) {
			this.updateCurrentLeaves(world, x, y, z);
		}

	}

	private void removeLeaves(World worldObj, int x, int y, int z) {
		this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
		worldObj.setBlockWithNotify(x, y, z, 0);
	}

    public int quantityDropped(EaglercraftRandom rand) {
        return rand.nextInt(20) == 0 ? 1 : 0;
    }

    public int idDropped(int metadata, EaglercraftRandom rand) {
        return Block.sapling.blockID;
    }

    public boolean isOpaqueCube() {
        return !this.graphicsLevel;
    }

    public void setGraphicsLevel(boolean fancyGraphics) {
        this.graphicsLevel = fancyGraphics;
        this.blockIndexInTexture = this.leafTexIndex + (fancyGraphics ? 0 : 1);
    }

    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        super.onEntityWalking(world, x, y, z, entity);
    }
}
