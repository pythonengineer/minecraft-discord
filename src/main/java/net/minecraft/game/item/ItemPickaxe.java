package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;

public final class ItemPickaxe extends ItemTool {
    private static Block[] blocksEffectiveAgainst = new Block[]{Block.cobblestone, Block.stairDouble, Block.stairSingle, Block.stone, Block.cobblestoneMossy, Block.oreIron, Block.blockSteel, Block.oreCoal, Block.blockGold, Block.oreGold, Block.oreDiamond, Block.blockDiamond};

    public ItemPickaxe(int var1, int var2) {
        super(var1, 2, var2, blocksEffectiveAgainst);
    }
}
