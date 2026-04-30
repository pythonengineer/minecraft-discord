package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.block.tileentity.TileEntityFurnace;
import net.minecraft.game.world.material.Material;

public final class BlockFurnace extends BlockContainer {
	private final boolean isActive;

	protected BlockFurnace(int blockID, boolean isActive) {
		super(blockID, Material.rock);
		this.isActive = isActive;
		this.blockIndexInTexture = 45;
	}

    public final void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        int i5 = (world = world).getBlockId(x, y, z - 1);
        int i6 = world.getBlockId(x, y, z + 1);
        int i7 = world.getBlockId(x - 1, y, z);
        int i8 = world.getBlockId(x + 1, y, z);
        byte b9 = 3;
        if(Block.opaqueCubeLookup[i5] && !Block.opaqueCubeLookup[i6]) {
            b9 = 3;
        }

        if(Block.opaqueCubeLookup[i6] && !Block.opaqueCubeLookup[i5]) {
            b9 = 2;
        }

        if(Block.opaqueCubeLookup[i7] && !Block.opaqueCubeLookup[i8]) {
            b9 = 5;
        }

        if(Block.opaqueCubeLookup[i8] && !Block.opaqueCubeLookup[i7]) {
            b9 = 4;
        }

        world.setBlockMetadata(x, y, z, b9);
    }

    public final int getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
        if(side == 1) {
            return Block.stone.blockIndexInTexture;
        } else if(side == 0) {
            return Block.stone.blockIndexInTexture;
        } else {
            int i6 = iBlockAccess.getBlockMetadata(x, y, z);
            return side != i6 ? this.blockIndexInTexture : (this.isActive ? this.blockIndexInTexture + 16 : this.blockIndexInTexture - 1);
        }
    }

	public final void randomDisplayTick(World world, int x, int y, int z, EaglercraftRandom rand) {
		if(this.isActive) {
			int i6 = world.getBlockMetadata(x, y, z);
			float x1 = (float)x + 0.5F;
			float y1 = (float)y + rand.nextFloat() * 6.0F / 16.0F;
			float z1 = (float)z + 0.5F;
			float rand1 = rand.nextFloat() * 0.6F - 0.3F;
			if(i6 == 4) {
				world.spawnParticle("smoke", (double)(x1 - 0.52F), (double)y1, (double)(z1 + rand1), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double)(x1 - 0.52F), (double)y1, (double)(z1 + rand1), 0.0D, 0.0D, 0.0D);
			} else if(i6 == 5) {
				world.spawnParticle("smoke", (double)(x1 + 0.52F), (double)y1, (double)(z1 + rand1), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double)(x1 + 0.52F), (double)y1, (double)(z1 + rand1), 0.0D, 0.0D, 0.0D);
			} else if(i6 == 2) {
				world.spawnParticle("smoke", (double)(x1 + rand1), (double)y1, (double)(z1 - 0.52F), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double)(x1 + rand1), (double)y1, (double)(z1 - 0.52F), 0.0D, 0.0D, 0.0D);
			} else {
				if(i6 == 3) {
					world.spawnParticle("smoke", (double)(x1 + rand1), (double)y1, (double)(z1 + 0.52F), 0.0D, 0.0D, 0.0D);
					world.spawnParticle("flame", (double)(x1 + rand1), (double)y1, (double)(z1 + 0.52F), 0.0D, 0.0D, 0.0D);
				}

			}
		}
	}

	public final int getBlockTextureFromSide(int side) {
		return side == 1 ? Block.stone.blockID : (side == 0 ? Block.stone.blockID : (side == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture));
	}

	public final boolean blockActivated(World world, int x, int y, int z, EntityPlayer playerEntity) {
		TileEntityFurnace world1 = (TileEntityFurnace)world.getBlockTileEntity(x, y, z);
		playerEntity.displayGUIFurnace(world1);
		return true;
	}

	protected final TileEntity getBlockEntity() {
		return new TileEntityFurnace();
	}
}