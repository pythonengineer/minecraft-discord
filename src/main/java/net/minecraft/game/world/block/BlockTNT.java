package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockTNT extends Block {
	public BlockTNT(int blockID, int textureIndex) {
		super(blockID, textureIndex, Material.tnt);
	}

	public int getBlockTextureFromSide(int side) {
		return side == 0 ? this.blockIndexInTexture + 2 : (side == 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
	}

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
        if(blockID > 0 && Block.blocksList[blockID].canProvidePower() && world.isBlockIndirectlyGettingPowered(x, y, z)) {
            this.onBlockDestroyedByPlayer(world, x, y, z, 0);
            world.setBlockWithNotify(x, y, z, 0);
        }

    }

	public int quantityDropped(EaglercraftRandom rand) {
		return 0;
	}

	public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
		EntityTNTPrimed entityTNTPrimed5 = new EntityTNTPrimed(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
		entityTNTPrimed5.fuse = world.rand.nextInt(entityTNTPrimed5.fuse / 4) + entityTNTPrimed5.fuse / 8;
		world.spawnEntityInWorld(entityTNTPrimed5);
	}

	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
		EntityTNTPrimed entityTNTPrimed6 = new EntityTNTPrimed(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
		world.spawnEntityInWorld(entityTNTPrimed6);
		world.playSoundAtEntity(entityTNTPrimed6, "random.fuse", 1.0F, 1.0F);
	}
}
