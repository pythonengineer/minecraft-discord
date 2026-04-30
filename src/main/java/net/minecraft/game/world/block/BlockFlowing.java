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
        world.setBlockAndMetadata(x, y, z, this.blockID + 1, i5);
		world.markBlocksDirty(x, y, z, x, y, z);
	}

	public final void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		int i6 = this.getFlowDecay(world, x, y, z);
		boolean z7 = true;
		int i8;
		int i9;
		if(i6 > 0) {
			this.numAdjacentSources = 0;
			i8 = this.getSmallestFlowDecay(world, x - 1, y, z, -100);
			i8 = this.getSmallestFlowDecay(world, x + 1, y, z, i8);
			i8 = this.getSmallestFlowDecay(world, x, y, z - 1, i8);
			if((i9 = (i8 = this.getSmallestFlowDecay(world, x, y, z + 1, i8)) + this.liquidType) >= 8 || i8 < 0) {
				i9 = -1;
			}

			if(this.getFlowDecay(world, x, y + 1, z) >= 0) {
				if((i8 = this.getFlowDecay(world, x, y + 1, z)) >= 8) {
					i9 = i8;
				} else {
					i9 = i8 + 8;
				}
			}

			if(this.numAdjacentSources >= 2 && this.blockMaterial == Material.water) {
				i9 = 0;
			}

			if(this.blockMaterial == Material.lava && i6 < 8 && i9 < 8 && i9 > i6 && rand.nextInt(4) != 0) {
				i9 = i6;
				z7 = false;
			}

			if(i9 != i6) {
				i6 = i9;
				if(i9 < 0) {
					world.setBlockWithNotify(x, y, z, 0);
				} else {
                    world.setBlockMetadata(x, y, z, i9);
                    world.scheduleBlockUpdate(x, y, z, this.blockID);
                    world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
				}
			} else if(z7) {
				this.updateFlow(world, x, y, z);
			}
		} else {
			this.updateFlow(world, x, y, z);
		}

		if(this.liquidCanDisplaceBlock(world, x, y - 1, z)) {
			if(i6 >= 8) {
				world.setBlockAndMetadataWithNotify(x, y - 1, z, this.blockID, i6);
			} else {
				world.setBlockAndMetadataWithNotify(x, y - 1, z, this.blockID, i6 + 8);
			}
		} else {
			if(i6 >= 0 && (i6 == 0 || blockBlocksFlow(world, x, y - 1, z))) {
				int i10 = z;
				i9 = y;
				i8 = x;
				World world15 = world;
				BlockFlowing blockFlowing14 = this;

				int i11;
				int i12;
				for(i11 = 0; i11 < 4; ++i11) {
					blockFlowing14.flowCost[i11] = 1000;
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

					if(!blockBlocksFlow(world15, i12, i9, i13) && (world15.getBlockMaterial(i12, i9, i13) != blockFlowing14.blockMaterial || world15.getBlockMetadata(i12, i9, i13) != 0)) {
						if(!blockBlocksFlow(world15, i12, i9 - 1, i13)) {
							blockFlowing14.flowCost[i11] = 0;
						} else {
							blockFlowing14.flowCost[i11] = blockFlowing14.calculateFlowCost(world15, i12, i9, i13, 1, i11);
						}
					}
				}

				i11 = blockFlowing14.flowCost[0];

				for(i12 = 1; i12 < 4; ++i12) {
					if(blockFlowing14.flowCost[i12] < i11) {
						i11 = blockFlowing14.flowCost[i12];
					}
				}

				for(i12 = 0; i12 < 4; ++i12) {
					blockFlowing14.isOptimalFlowDirection[i12] = blockFlowing14.flowCost[i12] == i11;
				}

				boolean[] z16 = blockFlowing14.isOptimalFlowDirection;
				i9 = i6 + this.liquidType;
				if(i6 >= 8) {
					i9 = 1;
				}

				if(i9 >= 8) {
					return;
				}

                if(z16[0]) {
                    this.flowIntoBlock(world, x - 1, y, z, i9);
                }

                if(z16[1]) {
                    this.flowIntoBlock(world, x + 1, y, z, i9);
                }

                if(z16[2]) {
                    this.flowIntoBlock(world, x, y, z - 1, i9);
                }

                if(z16[3]) {
                    this.flowIntoBlock(world, x, y, z + 1, i9);
                }
            }

        }
    }

    private void flowIntoBlock(World world, int x, int y, int z, int metadata) {
        if(this.liquidCanDisplaceBlock(world, x, y, z)) {
            int i6;
            if((i6 = world.getBlockId(x, y, z)) > 0) {
                if(this.blockMaterial == Material.lava) {
                    triggerLavaMixEffects(world, x, y, z);
                } else {
                    Block.blocksList[i6].harvestBlock(world, x, y, z, world.getBlockMetadata(x, y, z));
                }
            }

            world.setBlockAndMetadataWithNotify(x, y, z, this.blockID, metadata);
        }

    }

	private int calculateFlowCost(World world, int x, int y, int z, int blocksTravelled, int flowCost) {
		int i7 = 1000;

		for(int i8 = 0; i8 < 4; ++i8) {
			if((i8 != 0 || flowCost != 1) && (i8 != 1 || flowCost != 0) && (i8 != 2 || flowCost != 3) && (i8 != 3 || flowCost != 2)) {
				int i9 = x;
				int i10 = z;
				if(i8 == 0) {
					i9 = x - 1;
				}

				if(i8 == 1) {
					++i9;
				}

				if(i8 == 2) {
					i10 = z - 1;
				}

				if(i8 == 3) {
					++i10;
				}

				if(!blockBlocksFlow(world, i9, y, i10) && (world.getBlockMaterial(i9, y, i10) != this.blockMaterial || world.getBlockMetadata(i9, y, i10) != 0)) {
					if(!blockBlocksFlow(world, i9, y - 1, i10)) {
						return blocksTravelled;
					}

					if(blocksTravelled < 4 && (i9 = this.calculateFlowCost(world, i9, y, i10, blocksTravelled + 1, i8)) < i7) {
						i7 = i9;
					}
				}
			}
		}

		return i7;
	}

	private static boolean blockBlocksFlow(World world, int x, int y, int z) {
		int world1;
		return (world1 = world.getBlockId(x, y, z)) != Block.doorWood.blockID && world1 != Block.signStanding.blockID && world1 != Block.ladder.blockID ? (world1 == 0 ? false : Block.blocksList[world1].blockMaterial.isSolid()) : true;
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

	private boolean liquidCanDisplaceBlock(World world, int x, int y, int z) {
        Material material5;
        return (material5 = world.getBlockMaterial(x, y, z)) == this.blockMaterial ? false : (material5 == Material.lava ? false : !blockBlocksFlow(world, x, y, z));
    }

	public final void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		if(world.getBlockId(x, y, z) == this.blockID) {
			world.scheduleBlockUpdate(x, y, z, this.blockID);
		}

	}
}