package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.terrain.generate.WorldGenTrees;

public final class BlockSapling extends BlockFlower {
	protected BlockSapling(int i1, int i2) {
		super(6, 15);
		this.setBlockBounds(0.099999994F, 0.0F, 0.099999994F, 0.9F, 0.8F, 0.9F);
	}

	public final void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		super.updateTick(world, x, y, z, rand);
		if(world.getBlockLightValue(x, y + 1, z) >= 9 && rand.nextInt(5) == 0) {
			int i6;
			if((i6 = world.getBlockMetadata(x, y, z)) < 15) {
				world.setBlockMetadata(x, y, z, i6 + 1);
				return;
			}

			world.setBlock(x, y, z, 0);
			if(!(new WorldGenTrees()).generate(world, rand, x, y, z)) {
				world.setBlock(x, y, z, this.blockID);
			}
		}

	}
}