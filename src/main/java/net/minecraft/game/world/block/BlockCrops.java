package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;

public final class BlockCrops extends BlockFlower {
	protected BlockCrops(int i1, int i2) {
		super(59, 88);
		this.blockIndexInTexture = 88;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
	}

	protected final boolean canThisPlantGrowOnThisBlockID(int blockID) {
		return blockID == Block.tilledField.blockID;
	}

	public final void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		super.updateTick(world, x, y, z, rand);
		int i6;
		if(world.getBlockLightValue(x, y + 1, z) >= 9 && (i6 = world.getBlockMetadata(x, y, z)) < 7) {
			int i11 = z;
			int i10 = y;
			int i9 = x;
			World world8 = world;
			float f12 = 1.0F;
			int i13 = world.getBlockId(x, y, z - 1);
			int i14 = world.getBlockId(x, y, z + 1);
			int i15 = world.getBlockId(x - 1, y, z);
			int i16 = world.getBlockId(x + 1, y, z);
			int i17 = world.getBlockId(x - 1, y, z - 1);
			int i18 = world.getBlockId(x + 1, y, z - 1);
			int i19 = world.getBlockId(x + 1, y, z + 1);
			int i20 = world.getBlockId(x - 1, y, z + 1);
			boolean z22 = i15 == this.blockID || i16 == this.blockID;
			boolean z21 = i13 == this.blockID || i14 == this.blockID;
			boolean z7 = i17 == this.blockID || i18 == this.blockID || i19 == this.blockID || i20 == this.blockID;

			for(i14 = x - 1; i14 <= i9 + 1; ++i14) {
				for(i16 = i11 - 1; i16 <= i11 + 1; ++i16) {
					i17 = world8.getBlockId(i14, i10 - 1, i16);
					float f23 = 0.0F;
					if(i17 == Block.tilledField.blockID) {
						f23 = 1.0F;
						if(world8.getBlockMetadata(i14, i10 - 1, i16) > 0) {
							f23 = 3.0F;
						}
					}

					if(i14 != i9 || i16 != i11) {
						f23 /= 4.0F;
					}

					f12 += f23;
				}
			}

			if(z7 || z22 && z21) {
				f12 /= 2.0F;
			}

			if(rand.nextInt((int)(100.0F / f12)) == 0) {
				++i6;
				world.setBlockMetadata(x, y, z, i6);
			}
		}

	}

	public final int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		if(metadata < 0) {
			metadata = 7;
		}

		return this.blockIndexInTexture + metadata;
	}

	public final int getRenderType() {
		return 6;
	}

	public final void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
		super.onBlockDestroyedByPlayer(world, x, y, z, metadata);

		for(int i6 = 0; i6 < 3; ++i6) {
			if(world.rand.nextInt(15) <= metadata) {
				float f7 = world.rand.nextFloat() * 0.7F + 0.15F;
				float f8 = world.rand.nextFloat() * 0.7F + 0.15F;
				float f9 = world.rand.nextFloat() * 0.7F + 0.15F;
				EntityItem entityItem10;
				(entityItem10 = new EntityItem(world, (double)((float)x + f7), (double)((float)y + f8), (double)((float)z + f9), new ItemStack(Item.seeds))).delayBeforeCanPickup = 10;
				world.spawnEntityInWorld(entityItem10);
			}
		}

	}

	public final int idDropped(int metadata, EaglercraftRandom rand) {
		System.out.println("Get resource: " + metadata);
		return metadata == 7 ? Item.wheat.shiftedIndex : -1;
	}

	public final int quantityDropped(EaglercraftRandom rand) {
		return 1;
	}
}