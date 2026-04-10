package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockFlowing extends BlockFluid {
	private int numAdjacentSources = 0;
	private boolean[] isOptimalFlowDirection = new boolean[4];
	private int[] flowCost = new int[4];

	protected BlockFlowing(int i1, Material material2) {
		super(i1, material2);
	}

	private void updateFlow(World world, int x, int y, int z) {
		int i5 = world.getBlockMetadata(x, y, z);
		world.setBlock(x, y, z, this.blockID + 1);
		world.setBlockAndMetadata(x, y, z, i5);
		world.markBlocksDirty(x, y, z, x, y, z);
	}

	public final void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		int i7;
		int i14;
		if((i14 = this.getFlowDecay(world, x, y, z)) > 0) {
			this.numAdjacentSources = 0;
            int i6 = this.getSmallestFlowDecay(world, x - 1, y, z, -100);
            i6 = this.getSmallestFlowDecay(world, x + 1, y, z, i6);
            i6 = this.getSmallestFlowDecay(world, x, y, z - 1, i6);
            if((i7 = (i6 = this.getSmallestFlowDecay(world, x, y, z + 1, i6)) + this.liquidType) >= 8 || i6 < 0) {
				i7 = -1;
			}

			if(this.getFlowDecay(world, x, y + 1, z) >= 0) {
				if((i6 = this.getFlowDecay(world, x, y + 1, z)) >= 8) {
					i7 = i6;
				} else {
					i7 = i6 + 8;
				}
			}

			if(this.numAdjacentSources >= 2) {
				i7 = 0;
			}

			if(this.blockMaterial == Material.lava && i14 < 8 && i7 < 8 && i7 > i14) {
				i7 = i14;
			}

			if(i7 != i14) {
				i14 = i7;
				if(i7 < 0) {
					world.setBlockWithNotify(x, y, z, 0);
				} else {
					world.setBlockAndMetadataWithNotify(x, y, z, this.blockID, i7);
					world.scheduleBlockUpdate(x, y, z, this.blockID);
				}
			} else {
				this.updateFlow(world, x, y, z);
			}
		} else {
			this.updateFlow(world, x, y, z);
		}

		if(liquidCanDisplaceBlock(world, x, y - 1, z)) {
			if(i14 >= 8) {
				world.setBlockAndMetadataWithNotify(x, y - 1, z, this.blockID, i14);
			} else {
				world.setBlockAndMetadataWithNotify(x, y - 1, z, this.blockID, i14 + 8);
			}
		} else {
			if(i14 >= 0 && blockBlocksFlow(world, x, y - 1, z)) {
				int i10 = z;
				int i9 = y;
				int i8 = x;
				World world17 = world;
				BlockFlowing blockFlowing15 = this;

				int i11;
				int i12;
				for(i11 = 0; i11 < 4; ++i11) {
					blockFlowing15.flowCost[i11] = 1000;
					i12 = i8;
					int i13 = i10;
					if(i11 == 0) {
						i12 = i8 - 1;
					}

					if(i11 == 1) {
						++i12;
					}

					if(i11 == 2) {
						i13 = i10 - 1;
					}

					if(i11 == 3) {
						++i13;
					}

					if(!blockBlocksFlow(world17, i12, i9, i13) && (world17.getBlockMaterial(i12, i9, i13) != blockFlowing15.blockMaterial || world17.getBlockMetadata(i12, i9, i13) != 0)) {
						if(!blockBlocksFlow(world17, i12, i9 - 1, i13)) {
							blockFlowing15.flowCost[i11] = 0;
						} else {
							blockFlowing15.flowCost[i11] = blockFlowing15.calculateFlowCost(world17, i12, i9, i13, 1);
						}
					}
				}

				i11 = blockFlowing15.flowCost[0];

				for(i12 = 1; i12 < 4; ++i12) {
					if(blockFlowing15.flowCost[i12] < i11) {
						i11 = blockFlowing15.flowCost[i12];
					}
				}

				for(i12 = 0; i12 < 4; ++i12) {
					blockFlowing15.isOptimalFlowDirection[i12] = blockFlowing15.flowCost[i12] == i11;
				}

				boolean[] z16 = blockFlowing15.isOptimalFlowDirection;
                i7 = i14 + this.liquidType;
                if(i14 >= 8) {
                    i7 = 1;
                }

                if(i7 >= 8) {
					return;
				}

				if(z16[0] && liquidCanDisplaceBlock(world, x - 1, y, z)) {
					world.setBlockAndMetadataWithNotify(x - 1, y, z, this.blockID, i7);
				}

				if(z16[1] && liquidCanDisplaceBlock(world, x + 1, y, z)) {
					world.setBlockAndMetadataWithNotify(x + 1, y, z, this.blockID, i7);
				}

				if(z16[2] && liquidCanDisplaceBlock(world, x, y, z - 1)) {
					world.setBlockAndMetadataWithNotify(x, y, z - 1, this.blockID, i7);
				}

				if(z16[3] && liquidCanDisplaceBlock(world, x, y, z + 1)) {
					world.setBlockAndMetadataWithNotify(x, y, z + 1, this.blockID, i7);
				}
			}

		}
	}

	private int calculateFlowCost(World world, int x, int y, int z, int blocksTravelled) {
		int i6 = 1000;

		for(int i7 = 0; i7 < 4; ++i7) {
			int i8 = x;
			int i9 = z;
			if(i7 == 0) {
				i8 = x - 1;
			}

			if(i7 == 1) {
				++i8;
			}

			if(i7 == 2) {
				i9 = z - 1;
			}

			if(i7 == 3) {
				++i9;
			}

			if(!blockBlocksFlow(world, i8, y, i9) && (world.getBlockMaterial(i8, y, i9) != this.blockMaterial || world.getBlockMetadata(i8, y, i9) != 0)) {
				if(!blockBlocksFlow(world, i8, y - 1, i9)) {
					return blocksTravelled;
				}

				if(blocksTravelled < 4 && (i8 = this.calculateFlowCost(world, i8, y, i9, blocksTravelled + 1)) < i6) {
					i6 = i8;
				}
			}
		}

		return i6;
	}

	private static boolean blockBlocksFlow(World world, int x, int y, int z) {
		int world1;
		return (world1 = world.getBlockId(x, y, z)) == 0 ? false : Block.blocksList[world1].blockMaterial.isSolid();
	}

	private int getSmallestFlowDecay(World world, int x, int y, int z, int flowDecay) {
		int i6;
		if((i6 = this.getFlowDecay(world, x, y, z)) < 0) {
			return flowDecay;
		} else {
			if(i6 == 0) {
				++this.numAdjacentSources;
			}

			if(i6 >= 8) {
				i6 = 0;
			}

			return flowDecay >= 0 && i6 >= flowDecay ? flowDecay : i6;
		}
	}

	private static boolean liquidCanDisplaceBlock(World world, int x, int y, int z) {
		Material world1;
		return (world1 = world.getBlockMaterial(x, y, z)) == Material.air || world1 == Material.circuits;
	}

	public final void onBlockAdded(World world, int x, int y, int z) {
		world.scheduleBlockUpdate(x, y, z, this.blockID);
	}
}