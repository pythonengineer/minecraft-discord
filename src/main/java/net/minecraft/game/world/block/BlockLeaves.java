package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockLeaves extends BlockLeavesBase {
    private int leafTexIndex;

	protected BlockLeaves(int blockID, int textureIndex) {
		super(blockID, textureIndex, Material.leaves, false);
		this.leafTexIndex = textureIndex;
		this.setTickOnLoad(true);
	}

    public void onNeighborBlockChange(World world, int i2, int i3, int i4, int i5) {
        super.onNeighborBlockChange(world, i2, i3, i4, i5);
    }

    public void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
    }

    public int quantityDropped(EaglercraftRandom rand) {
        return rand.nextInt(20) == 0 ? 1 : 0;
    }

	public int idDropped(int metadata, EaglercraftRandom rand) {
		return Block.sapling.blockID;
	}

    public boolean isOpaqueCube() {
        return !this.graphicsLevel;
    }

    public void setGraphicsLevel(boolean fancyGraphics) {
        this.graphicsLevel = fancyGraphics;
        this.blockIndexInTexture = this.leafTexIndex + (fancyGraphics ? 0 : 1);
    }

    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        super.onEntityWalking(world, x, y, z, entity);
    }
}
