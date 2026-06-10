package net.minecraft.game.world.block;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockCactus extends Block {
    protected BlockCactus(int i1, int i2) {
        super(i1, i2, Material.cactus);
    }

    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return side == 1 ? this.blockIndexInTexture - 1 : (side == 0 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
    }

    public boolean canBlockStay(World world1, int i2, int i3, int i4) {
        if(world1.getBlockMaterial(i2 - 1, i3, i4).isSolid()) {
            return false;
        } else if(world1.getBlockMaterial(i2 + 1, i3, i4).isSolid()) {
            return false;
        } else if(world1.getBlockMaterial(i2, i3, i4 - 1).isSolid()) {
            return false;
        } else if(world1.getBlockMaterial(i2, i3, i4 + 1).isSolid()) {
            return false;
        } else {
            int i5 = world1.getBlockId(i2, i3 - 1, i4);
            return i5 == Block.cactus.blockID || i5 == Block.sand.blockID;
        }
    }

    public void onBlockClicked(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
        entityPlayer.attackEntityFrom((Entity)null, 1);
        super.onBlockClicked(worldObj, x, y, z, entityPlayer);
    }

    public void onEntityWalking(World world1, int i2, int i3, int i4, Entity entity5) {
        entity5.attackEntityFrom((Entity)null, 1);
        super.onEntityWalking(world1, i2, i3, i4, entity5);
    }

    public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
        entityPlayer5.attackEntityFrom((Entity)null, 1);
        return super.blockActivated(world1, i2, i3, i4, entityPlayer5);
    }
}
