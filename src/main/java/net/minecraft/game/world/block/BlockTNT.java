package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockTNT extends Block {
	public BlockTNT(int blockID, int textureIndex) {
		super(46, 8, Material.tnt);
	}

	public final int getBlockTextureFromSide(int side) {
		return side == 0 ? this.blockIndexInTexture + 2 : (side == 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
	}

	public final int quantityDropped(EaglercraftRandom rand) {
		return 0;
	}

	public final void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
		EntityTNTPrimed x1;
		(x1 = new EntityTNTPrimed(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F)).fuse = world.rand.nextInt(x1.fuse / 4) + x1.fuse / 8;
		world.entityJoinedWorld(x1);
	}

	public final void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
		EntityTNTPrimed x1 = new EntityTNTPrimed(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
		world.entityJoinedWorld(x1);
		world.playSoundAtEntity(x1, "random.fuse", 1.0F, 1.0F);
	}
}