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

    public void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
        if(!world.getBlockMaterial(x, y - 1, z).isSolid()) {
            byte b6 = 2;

            for(int i7 = x - b6; i7 <= x + b6; ++i7) {
                for(int i8 = y - 1; i8 <= y + 1; ++i8) {
                    for(int i9 = z - b6; i9 <= z + b6; ++i9) {
                        if(world.getBlockId(i7, i8, i9) == Block.wood.blockID) {
                            return;
                        }
                    }
                }
            }

            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.setBlockWithNotify(x, y, z, 0);
        }
    }

	public int quantityDropped(EaglercraftRandom rand) {
		return rand.nextInt(10) == 0 ? 1 : 0;
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
