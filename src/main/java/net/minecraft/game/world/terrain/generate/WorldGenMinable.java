package net.minecraft.game.world.terrain.generate;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.lax1dude.eaglercraft.util.MathHelper;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class WorldGenMinable extends WorldGenerator {
	private int minableBlockId;

	public WorldGenMinable(int blockID) {
		this.minableBlockId = blockID;
	}

	public final boolean generate(World world, EaglercraftRandom rand, int x, int y, int z) {
		float f6 = rand.nextFloat() * (float)Math.PI;
		double d7 = (double)((float)(x + 8) + MathHelper.sin(f6) * 2.0F);
		double d9 = (double)((float)(x + 8) - MathHelper.sin(f6) * 2.0F);
		double d11 = (double)((float)(z + 8) + MathHelper.cos(f6) * 2.0F);
		double d13 = (double)((float)(z + 8) - MathHelper.cos(f6) * 2.0F);
		double d15 = (double)(y + rand.nextInt(3) + 2);
		double d17 = (double)(y + rand.nextInt(3) + 2);

		for(x = 0; x <= 16; ++x) {
			double d20 = d7 + (d9 - d7) * (double)x / 16.0D;
			double d22 = d15 + (d17 - d15) * (double)x / 16.0D;
			double d24 = d11 + (d13 - d11) * (double)x / 16.0D;
			double d26 = rand.nextDouble();
			double d28 = (double)(MathHelper.sin((float)x / 16.0F * (float)Math.PI) + 1.0F) * d26 + 1.0D;
			double d30 = (double)(MathHelper.sin((float)x / 16.0F * (float)Math.PI) + 1.0F) * d26 + 1.0D;

			for(y = (int)(d20 - d28 / 2.0D); y <= (int)(d20 + d28 / 2.0D); ++y) {
				for(z = (int)(d22 - d30 / 2.0D); z <= (int)(d22 + d30 / 2.0D); ++z) {
					for(int i41 = (int)(d24 - d28 / 2.0D); i41 <= (int)(d24 + d28 / 2.0D); ++i41) {
						double d35 = ((double)y + 0.5D - d20) / (d28 / 2.0D);
						double d37 = ((double)z + 0.5D - d22) / (d30 / 2.0D);
						double d39 = ((double)i41 + 0.5D - d24) / (d28 / 2.0D);
						if(d35 * d35 + d37 * d37 + d39 * d39 < 1.0D && world.getBlockId(y, z, i41) == Block.stone.blockID) {
							world.setBlock(y, z, i41, this.minableBlockId);
						}
					}
				}
			}
		}

		return true;
	}
}