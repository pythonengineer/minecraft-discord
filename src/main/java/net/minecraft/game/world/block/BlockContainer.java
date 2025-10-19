package net.minecraft.game.world.block;

import net.minecraft.game.world.World;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.material.Material;

public abstract class BlockContainer extends Block {
    protected BlockContainer(int var1, Material var2) {
        super(var1, var2);
    }

    public void onNeighborBlockChange(World var1, int var2, int var3, int var4) {
        super.onNeighborBlockChange(var1, var2, var3, var4);
        var1.setBlockTileEntity(var2, var3, var4, this.a_());
    }

    protected abstract TileEntity a_();
}
