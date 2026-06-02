package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.EnumSkyBlock;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockSnow extends Block {
    protected BlockSnow(int i1, int i2) {
        super(i1, i2, Material.snow);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setTickOnLoad(true);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
        return null;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
        return world1.getBlockMaterial(i2, i3 - 1, i4).getIsSolid();
    }

    public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
        this.canSnowStay(world1, i2, i3, i4);
    }

    private boolean canSnowStay(World world1, int i2, int i3, int i4) {
        if(!this.canPlaceBlockAt(world1, i2, i3, i4)) {
            this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
            world1.setBlockWithNotify(i2, i3, i4, 0);
            return false;
        } else {
            return true;
        }
    }

    public int idDropped(int i1, EaglercraftRandom random2) {
        return 0;
    }

    public int quantityDropped(EaglercraftRandom random1) {
        return 0;
    }

    public void updateTick(World world1, int i2, int i3, int i4, EaglercraftRandom random5) {
        if(world1.getSavedLightValue(EnumSkyBlock.Block, i2, i3, i4) > 11) {
            this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
            world1.setBlockWithNotify(i2, i3, i4, 0);
        }

    }
}
