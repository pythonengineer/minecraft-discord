package net.minecraft.game.level.block;

import net.minecraft.game.level.World;

public final class BlockGlass extends Block {
    private boolean renderSide = false;

    protected BlockGlass(int var1, int var2, boolean var3) {
        super(20, 49);
    }

    public final boolean isOpaqueCube() {
        return false;
    }

    public final boolean shouldSideBeRendered(World var1, int var2, int var3, int var4, int var5) {
        int var6 = var1.getBlockId(var2, var3, var4);
        return !this.renderSide && var6 == this.blockID ? false : super.shouldSideBeRendered(var1, var2, var3, var4, var5);
    }
}
