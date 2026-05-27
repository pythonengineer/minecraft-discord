package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;

public class BlockCrops extends BlockFlower {
	protected BlockCrops(int i1, int i2) {
		super(i1, i2);
		this.blockIndexInTexture = i2;
		this.setTickOnLoad(true);
        float f3 = 0.5F;
        this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, 0.25F, 0.5F + f3);
	}

	protected boolean canThisPlantGrowOnThisBlockID(int blockID) {
		return blockID == Block.tilledField.blockID;
	}

	public void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		super.updateTick(world, x, y, z, rand);
        if(world.getBlockLightValue(x, y + 1, z) >= 9) {
            int i6 = world.getBlockMetadata(x, y, z);
            if(i6 < 7) {
                float f7 = this.updateTick(world, x, y, z);
                if(rand.nextInt((int)(100.0F / f7)) == 0) {
                    ++i6;
                    world.setBlockMetadataWithNotify(x, y, z, i6);
                }
            }
        }

    }

    private float updateTick(World world, int x, int y, int z) {
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

		for(i14 = x - 1; i14 <= x + 1; ++i14) {
			for(i16 = z - 1; i16 <= z + 1; ++i16) {
				i17 = world.getBlockId(i14, y - 1, i16);
				float f23 = 0.0F;
				if(i17 == Block.tilledField.blockID) {
					f23 = 1.0F;
					if(world.getBlockMetadata(i14, y - 1, i16) > 0) {
						f23 = 3.0F;
					}
				}

				if(i14 != x || i16 != z) {
					f23 /= 4.0F;
				}

				f12 += f23;
			}
		}

		if(z7 || z22 && z21) {
			f12 /= 2.0F;
		}

		return f12;
	}

	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		if(metadata < 0) {
			metadata = 7;
		}

		return this.blockIndexInTexture + metadata;
	}

	public int getRenderType() {
		return 6;
	}

	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
		super.onBlockDestroyedByPlayer(world, x, y, z, metadata);

		for(int i6 = 0; i6 < 3; ++i6) {
			if(world.rand.nextInt(15) <= metadata) {
                float f7 = 0.7F;
                float f8 = world.rand.nextFloat() * f7 + (1.0F - f7) * 0.5F;
                float f9 = world.rand.nextFloat() * f7 + (1.0F - f7) * 0.5F;
                float f10 = world.rand.nextFloat() * f7 + (1.0F - f7) * 0.5F;
                EntityItem entityItem11 = new EntityItem(world, (double)((float)x + f8), (double)((float)y + f9), (double)((float)z + f10), new ItemStack(Item.seeds));
                entityItem11.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(entityItem11);
			}
		}

	}

	public int idDropped(int metadata, EaglercraftRandom rand) {
		System.out.println("Get resource: " + metadata);
		return metadata == 7 ? Item.wheat.shiftedIndex : -1;
	}

	public int quantityDropped(EaglercraftRandom rand) {
		return 1;
	}
}
