package net.minecraft.game.world.block;

import net.lax1dude.eaglercraft.EaglercraftRandom;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.EnumSkyBlock;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockSnowBlock extends Block {
    protected BlockSnowBlock(int id, int tex) {
        super(id, tex, Material.craftedSnow);
        this.setTickOnLoad(true);
    }

    public int idDropped(int i1, EaglercraftRandom random2) {
        return Item.snowball.shiftedIndex;
    }

    public int quantityDropped(EaglercraftRandom random1) {
        return 4;
    }

    public void updateTick(World worldObj, int x, int y, int z, EaglercraftRandom rand) {
        if(worldObj.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11) {
            this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
            worldObj.setBlockWithNotify(x, y, z, 0);
        }

    }
}
