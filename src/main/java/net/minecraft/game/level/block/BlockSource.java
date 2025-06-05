package net.minecraft.game.level.block;

import net.minecraft.game.level.World;

public final class BlockSource extends Block {
    private int source;

    protected BlockSource(int var1, int var2) {
        super(var1, Block.blocksList[var2].blockIndexInTexture);
        this.source = var2;
    }

    public final void onBlockAdded(World var1, int var2, int var3, int var4) {
        super.onBlockAdded(var1, var2, var3, var4);
        if(var1.getBlockId(var2 - 1, var3, var4) == 0) {
            var1.setBlockWithNotify(var2 - 1, var3, var4, this.source);
        }

        if(var1.getBlockId(var2 + 1, var3, var4) == 0) {
            var1.setBlockWithNotify(var2 + 1, var3, var4, this.source);
        }

        if(var1.getBlockId(var2, var3, var4 - 1) == 0) {
            var1.setBlockWithNotify(var2, var3, var4 - 1, this.source);
        }

        if(var1.getBlockId(var2, var3, var4 + 1) == 0) {
            var1.setBlockWithNotify(var2, var3, var4 + 1, this.source);
        }

    }
}
