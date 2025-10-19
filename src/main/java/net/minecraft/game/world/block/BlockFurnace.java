package net.minecraft.game.world.block;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.block.tileentity.TileEntityFurnace;
import net.minecraft.game.world.material.Material;

public final class BlockFurnace extends BlockContainer {
	private final boolean isActive;

	protected BlockFurnace(int var1, boolean var2) {
		super(var1, Material.rock);
		this.isActive = var2;
		this.blockIndexInTexture = 45;
	}

	public final void onNeighborBlockChange(World var1, int var2, int var3, int var4) {
		super.onNeighborBlockChange(var1, var2, var3, var4);
		f(var1, var2, var3, var4);
	}

	private static void f(World var0, int var1, int var2, int var3) {
		int var4 = var0.getBlockId(var1, var2, var3 - 1);
		int var5 = var0.getBlockId(var1, var2, var3 + 1);
		int var6 = var0.getBlockId(var1 - 1, var2, var3);
		int var7 = var0.getBlockId(var1 + 1, var2, var3);
		byte var8 = 3;
		if(Block.opaqueCubeLookup[var4] && !Block.opaqueCubeLookup[var5]) {
			var8 = 3;
		}

		if(Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var4]) {
			var8 = 2;
		}

		if(Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var7]) {
			var8 = 5;
		}

		if(Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var6]) {
			var8 = 4;
		}

		var0.setBlockMetadataWithNotify(var1, var2, var3, var8);
	}

	public final int getBlockTexture(World var1, int var2, int var3, int var4, int var5) {
		if(var5 == 1) {
			return Block.stone.blockIndexInTexture;
		} else if(var5 == 0) {
			return Block.stone.blockIndexInTexture;
		} else {
			int var6 = var1.getBlockMetadata(var2, var3, var4);
			if(var6 == 0) {
				f(var1, var2, var3, var4);
				var6 = var1.getBlockMetadata(var2, var3, var4);
			}

			return var5 != var6 ? this.blockIndexInTexture : (this.isActive ? this.blockIndexInTexture + 16 : this.blockIndexInTexture - 1);
		}
	}

	public final int getBlockTextureFromSide(int var1) {
		return var1 == 1 ? Block.stone.blockID : (var1 == 0 ? Block.stone.blockID : (var1 == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture));
	}

	public final boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		TileEntityFurnace var6 = (TileEntityFurnace)var1.getBlockTileEntity(var2, var3, var4);
		var5.displayGUIFurnace(var6);
		return true;
	}

	protected final TileEntity a_() {
		return new TileEntityFurnace();
	}
}
