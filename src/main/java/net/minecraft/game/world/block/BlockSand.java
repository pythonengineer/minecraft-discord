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

	public final void onBlockAdded(World world1, int x, int y, int z) {
		world1.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public final void onNeighborBlockChange(World world1, int x, int y, int z, int blockID) {
		world1.scheduleBlockUpdate(x, y, z, this.blockID);
	}

    public final void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
        int i12 = z;
        z = y;
        y = x;
        World world11 = world;
        int i8 = z - 1;
        int i6;
        Material material13;
        if(((i6 = world.getBlockId(y, i8, i12)) == 0 ? true : (i6 == Block.fire.blockID ? true : ((material13 = Block.blocksList[i6].blockMaterial) == Material.water ? true : material13 == Material.lava))) && z >= 0) {
            EntityFallingSand entityFallingSand10 = new EntityFallingSand(world, (float)y + 0.5F, (float)z + 0.5F, (float)i12 + 0.5F, this.blockID);
            if(fallInstantly) {
                while(!entityFallingSand10.isDead) {
                    entityFallingSand10.onUpdate();
                }
            } else {
                world11.spawnEntityInWorld(entityFallingSand10);
            }
        }

    }

	public final int tickRate() {
		return 3;
	}
}