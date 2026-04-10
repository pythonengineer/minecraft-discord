package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockLeaves extends BlockLeavesBase {
    private int leafTexIndex = 52;

	protected BlockLeaves(int blockID, int textureIndex) {
		super(18, 52, Material.leaves, true);
		this.setTickOnLoad(true);
	}

    public final void updateTick(World world, int x, int y, int z, EaglercraftRandom rand) {
        if(!world.getBlockMaterial(x, y - 1, z).isSolid()) {
            for(int i8 = x - 3; i8 <= x + 3; ++i8) {
                for(int i6 = y - 1; i6 <= y; ++i6) {
                    for(int i7 = z - 3; i7 <= z + 3; ++i7) {
                        if(world.getBlockId(i8, i6, i7) == Block.wood.blockID) {
                            return;
                        }
                    }
                }
            }

            this.harvestBlock(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.setBlockWithNotify(x, y, z, 0);
        }
    }

	public final int quantityDropped(EaglercraftRandom rand) {
		return rand.nextInt(10) == 0 ? 1 : 0;
	}

	public final int idDropped(int metadata, EaglercraftRandom rand) {
		return Block.sapling.blockID;
	}

    public final boolean isOpaqueCube() {
        return !this.graphicsLevel;
    }

    public final void setGraphicsLevel(boolean fancyGraphics) {
        this.graphicsLevel = fancyGraphics;
        this.blockIndexInTexture = this.leafTexIndex + (fancyGraphics ? 0 : 1);
    }
}