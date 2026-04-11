package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityFallingSand;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockSand extends Block {
	public BlockSand(int blockID, int textureIndex) {
		super(blockID, textureIndex, Material.sand);
	}

	public final void onBlockAdded(World world1, int x, int y, int z) {
		world1.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public final void onNeighborBlockChange(World world1, int x, int y, int z, int blockID) {
		world1.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public final void updateTick(World world1, int x, int y, int z, EaglercraftRandom random5) {
		int i8 = y - 1;
		int i6;
		Material material10;
		if(((i6 = world1.getBlockId(x, i8, z)) == 0 ? true : (i6 == Block.fire.blockID ? true : ((material10 = Block.blocksList[i6].blockMaterial) == Material.water ? true : material10 == Material.lava))) && y >= 0) {
			world1.entityJoinedWorld(new EntityFallingSand(world1, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, this.blockID));
		}

	}

	public final int tickRate() {
		return 3;
	}
}