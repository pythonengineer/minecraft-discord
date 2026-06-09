package net.minecraft.game.item;

import net.minecraft.game.world.block.Block;

public class ItemSpade extends ItemTool {
    private static Block[] blocksEffectiveAgainst = new Block[]{Block.grass, Block.dirt, Block.sand, Block.gravel, Block.snow, Block.blockSnow};

    public ItemSpade(int itemID, int harvestLevel) {
        super(itemID, 1, harvestLevel, blocksEffectiveAgainst);
    }

    public boolean canHarvestBlock(Block block) {
        return block == Block.snow ? true : block == Block.blockSnow;
    }
}
