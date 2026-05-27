package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.terrain.generate.WorldGenTrees;

public class BlockSapling extends BlockFlower {
	protected BlockSapling(int i1, int i2) {
		super(i1, i2);
		float f3 = 0.4F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, f3 * 2.0F, 0.5F + f3);
	}

	public void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		super.updateTick(world, x, y, z, rand);
		if(world.getBlockLightValue(x, y + 1, z) >= 9 && rand.nextInt(5) == 0) {
			int i6 = world.getBlockMetadata(x, y, z);
			if(i6 < 15) {
				world.setBlockMetadataWithNotify(x, y, z, i6 + 1);
			} else {
				world.setBlock(x, y, z, 0);
				WorldGenTrees worldGenTrees7 = new WorldGenTrees();
				if(!worldGenTrees7.generate(world, rand, x, y, z)) {
					world.setBlock(x, y, z, this.blockID);
				}
			}
		}

	}
}
