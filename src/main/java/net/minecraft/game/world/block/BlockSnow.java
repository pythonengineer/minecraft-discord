package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.EnumSkyBlock;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockSnow extends Block {
    protected BlockSnow(int id, int tex) {
        super(id, tex, Material.snow);
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
        int i5 = world1.getBlockId(i2, i3 - 1, i4);
        return i5 != 0 && Block.blocksList[i5].isOpaqueCube() ? world1.getBlockMaterial(i2, i3 - 1, i4).getIsSolid() : false;
    }

    public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
        this.canSnowStay(world1, i2, i3, i4);
    }

    private boolean canSnowStay(World worldObj, int x, int y, int z) {
        if(!this.canPlaceBlockAt(worldObj, x, y, z)) {
            this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
            worldObj.setBlockWithNotify(x, y, z, 0);
            return false;
        } else {
            return true;
        }
    }

    public void harvestBlock(World worldObj, int x, int y, int z, int metadata) {
        int i6 = Item.snowball.shiftedIndex;
        float f7 = 0.7F;
        double d8 = (double)(worldObj.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.5D;
        double d10 = (double)(worldObj.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.5D;
        double d12 = (double)(worldObj.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.5D;
        EntityItem entityItem14 = new EntityItem(worldObj, (double)x + d8, (double)y + d10, (double)z + d12, new ItemStack(i6));
        entityItem14.delayBeforeCanPickup = 10;
        worldObj.spawnEntityInWorld(entityItem14);
        worldObj.setBlockWithNotify(x, y, z, 0);
    }

    public int idDropped(int i1, EaglercraftRandom random2) {
        return Item.snowball.shiftedIndex;
    }

    public int quantityDropped(EaglercraftRandom random1) {
        return 0;
    }

    public void updateTick(World worldObj, int x, int y, int z, EaglercraftRandom rand) {
        if(worldObj.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11) {
            this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
            worldObj.setBlockWithNotify(x, y, z, 0);
        }

    }

    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        Material material6 = blockAccess.getBlockMaterial(x, y, z);
        return side == 1 ? true : (material6 == this.material ? false : super.shouldSideBeRendered(blockAccess, x, y, z, side));
    }
}
