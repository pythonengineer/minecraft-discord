package net.minecraft.game.item;

import net.minecraft.game.world.block.Block;

public final class ItemAxe extends ItemTool {
    private static Block[] blocksEffectiveAgainst = new Block[]{Block.planks, Block.bookshelf, Block.wood, Block.chest};

    public ItemAxe(int itemID, int axeType) {
        super(itemID, 3, axeType, blocksEffectiveAgainst);
    }
}
