package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityFallingSand;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockSand extends Block {
    public static boolean fallInstantly = false;

	public BlockSand(int blockID, int textureIndex) {
		super(blockID, textureIndex, Material.sand);
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		world.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		world.scheduleBlockUpdate(x, y, z, this.blockID);
	}

    public void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
        this.tryToFall(world, x, y, z);
    }

    private void tryToFall(World world, int x, int y, int z) {
        if(canFallBelow(world, x, y - 1, z) && y >= 0) {
            EntityFallingSand entityFallingSand8 = new EntityFallingSand(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, this.blockID);
            if(fallInstantly) {
                while(!entityFallingSand8.isDead) {
                    entityFallingSand8.onUpdate();
                }
            } else {
                world.spawnEntityInWorld(entityFallingSand8);
            }
        }

    }

	public int tickRate() {
		return 3;
	}

	public static boolean canFallBelow(World world, int xCoord, int yCoord, int zCoord) {
		int i4 = world.getBlockId(xCoord, yCoord, zCoord);
		if(i4 == 0) {
			return true;
		} else if(i4 == Block.fire.blockID) {
			return true;
		} else {
			Material material5 = Block.blocksList[i4].material;
			return material5 == Material.water ? true : material5 == Material.lava;
		}
	}
}
