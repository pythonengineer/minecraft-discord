package net.minecraft.game.level.block;

public final class BlockOreBlock extends Block {
	public BlockOreBlock(int var1, int var2) {
		super(var1);
		this.blockIndexInTexture = var2;
	}

	public final int getBlockTexture(int var1) {
		return var1 == 1 ? this.blockIndexInTexture - 16 : (var1 == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture);
	}
}
