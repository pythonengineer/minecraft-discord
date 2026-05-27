package net.minecraft.game.world.terrain.generate;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class WorldGenTrees extends WorldGenerator {
	public boolean generate(World world, EaglercraftRandom rand, int x, int y, int z) {
		int i6 = rand.nextInt(3) + 4;
		boolean z7 = true;
		if(y > 0 && y + i6 + 1 <= 128) {
			int i8;
			int i10;
			int i11;
			int i12;
			for(i8 = y; i8 <= y + 1 + i6; ++i8) {
				byte b9 = 1;
				if(i8 == y) {
					b9 = 0;
				}

				if(i8 >= y + 1 + i6 - 2) {
					b9 = 2;
				}

				for(i10 = x - b9; i10 <= x + b9 && z7; ++i10) {
					for(i11 = z - b9; i11 <= z + b9 && z7; ++i11) {
						if(i8 >= 0 && i8 < 128) {
							if((i12 = world.getBlockId(i10, i8, i11)) != 0 && i12 != Block.leaves.blockID) {
								z7 = false;
							}
						} else {
							z7 = false;
						}
					}
				}
			}

			if(!z7) {
				return false;
			} else if(((i8 = world.getBlockId(x, y - 1, z)) == Block.grass.blockID || i8 == Block.dirt.blockID) && y < 128 - i6 - 1) {
				world.setBlock(x, y - 1, z, Block.dirt.blockID);

				int i15;
				for(i15 = y - 3 + i6; i15 <= y + i6; ++i15) {
					i10 = i15 - (y + i6);
					i11 = 1 - i10 / 2;

					for(i12 = x - i11; i12 <= x + i11; ++i12) {
						int i14 = i12 - x;

						for(i8 = z - i11; i8 <= z + i11; ++i8) {
							int i13 = i8 - z;
							if((Math.abs(i14) != i11 || Math.abs(i13) != i11 || rand.nextInt(2) != 0 && i10 != 0) && !Block.opaqueCubeLookup[world.getBlockId(i12, i15, i8)]) {
								world.setBlock(i12, i15, i8, Block.leaves.blockID);
							}
						}
					}
				}

				for(i15 = 0; i15 < i6; ++i15) {
                    if((i10 = world.getBlockId(x, y + i15, z)) == 0 || i10 == Block.leaves.blockID) {
                        world.setBlock(x, y + i15, z, Block.wood.blockID);
                    }
				}

				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
