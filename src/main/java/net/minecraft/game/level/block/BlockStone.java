package net.minecraft.game.level.block;

public final class BlockStone extends Block {
	public BlockStone(int var1, int var2) {
		super(var1, var2);
	}

    public final int idDropped() {
        return Block.cobblestone.blockID;
    }
}
