package net.minecraft.game.world.block;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntityFurnace;
import net.minecraft.game.world.material.Material;

public final class BlockFurnace extends BlockContainer {
	private final boolean isActive;

	protected BlockFurnace(int var1, boolean var2) {
		super(var1, Material.rock);
		this.isActive = var2;
		this.blockIndexInTexture = 45;
	}

	public final int getBlockTexture(World var1, int var2, int var3, int var4, int var5) {
		if(var5 == 1) {
			return Block.stone.blockIndexInTexture;
		} else if(var5 == 0) {
			return Block.stone.blockIndexInTexture;
		} else {
            int var6 = var1.getBlockMetadata(var2, var3, var4);
            if(var6 == 0) {
                int var10 = var1.getBlockId(var2, var3, var4 - 1);
                int var11 = var1.getBlockId(var2, var3, var4 + 1);
                int var12 = var1.getBlockId(var2 - 1, var3, var4);
                int var13 = var1.getBlockId(var2 + 1, var3, var4);
                byte var14 = 3;
                if(Block.opaqueCubeLookup[var10] && !Block.opaqueCubeLookup[var11]) {
                    var14 = 3;
                }

                if(Block.opaqueCubeLookup[var11] && !Block.opaqueCubeLookup[var10]) {
                    var14 = 2;
                }

                if(Block.opaqueCubeLookup[var12] && !Block.opaqueCubeLookup[var13]) {
                    var14 = 5;
                }

                if(Block.opaqueCubeLookup[var13] && !Block.opaqueCubeLookup[var12]) {
                    var14 = 4;
                }

                var1.setBlockMetadataWithNotify(var2, var3, var4, var14);
                var6 = var1.getBlockMetadata(var2, var3, var4);
            }

            return var5 != var6 ? this.blockIndexInTexture : (this.isActive ? this.blockIndexInTexture + 16 : this.blockIndexInTexture - 1);
        }
    }

	public final int getBlockTextureFromSide(int var1) {
		return var1 == 1 ? Block.stone.blockID : (var1 == 0 ? Block.stone.blockID : (var1 == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture));
	}

	public final boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		var5.displayGUIFurnace((TileEntityFurnace)null);
		return true;
	}
}
