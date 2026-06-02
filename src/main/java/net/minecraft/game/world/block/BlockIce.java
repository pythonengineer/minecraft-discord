package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.world.EnumSkyBlock;
import net.minecraft.game.world.IBlockAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockIce extends BlockBreakable {
    public BlockIce(int blockID, int tex) {
        super(blockID, tex, Material.ice, false);
        this.slipperiness = 0.98F;
        this.setTickOnLoad(true);
    }

    public int getRenderBlockPass() {
        return 1;
    }

    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
        return super.shouldSideBeRendered(iBlockAccess1, i2, i3, i4, 1 - i5);
    }

    public void onBlockRemoval(World world1, int i2, int i3, int i4) {
        world1.setBlockWithNotify(i2, i3, i4, Block.waterMoving.blockID);
    }

    public int quantityDropped(EaglercraftRandom random1) {
        return 0;
    }

    public void updateTick(World world1, int i2, int i3, int i4, EaglercraftRandom random5) {
        if(world1.getSavedLightValue(EnumSkyBlock.Block, i2, i3, i4) > 11 - Block.lightOpacity[this.blockID]) {
            this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
            world1.setBlockWithNotify(i2, i3, i4, Block.waterStill.blockID);
        }

    }
}
