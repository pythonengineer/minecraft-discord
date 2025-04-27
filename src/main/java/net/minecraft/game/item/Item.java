package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public abstract class Item {
    public static Item[] itemsList = new Item[1024];
    public static Item apple;
    public final int shiftedIndex;
    protected int iconIndex;

    protected Item(int var1) {
        this.shiftedIndex = var1;
        itemsList[var1] = this;
    }

    public final int getIconIndex() {
        return this.iconIndex;
    }

    public void onItemUse(ItemStack var1, World var2, int var3, int var4, int var5, int var6) {
    }

    public float getStrVsBlock(Block var1) {
        return 1.0F;
    }

    public boolean onPlaced(ItemStack var1, EntityPlayer var2) {
        return false;
    }

    public boolean shouldUseOnTouchEagler(ItemStack itemStack) {
        return false;
    }

    static {
        for(int var0 = 0; var0 < 256; ++var0) {
            if(Block.blocksList[var0] != null) {
                itemsList[var0] = new ItemBlock(var0);
            }
        }

        ItemTool var10000 = new ItemTool(256, new Block[]{Block.grass, Block.dirt, Block.sand, Block.gravel});
        byte var1 = 52;
        ItemTool var2 = var10000;
        var2.iconIndex = var1;
        var10000 = new ItemTool(257, new Block[]{Block.cobblestone, Block.stairDouble, Block.stairSingle, Block.stone, Block.cobblestoneMossy, Block.oreIron, Block.blockSteel, Block.oreCoal, Block.blockGold, Block.oreGold});
        var1 = 68;
        var2 = var10000;
        var2.iconIndex = var1;
        var10000 = new ItemTool(258, new Block[]{Block.planks, Block.bookShelf, Block.wood});
        var1 = 84;
        var2 = var10000;
        var2.iconIndex = var1;
        ItemFlintAndSteel var5 = new ItemFlintAndSteel(259);
        var1 = 5;
        ItemFlintAndSteel var3 = var5;
        var3.iconIndex = var1;
        ItemFood var6 = new ItemFood(260, 4);
        var1 = 4;
        ItemFood var4 = var6;
        var4.iconIndex = var1;
        apple = var4;
    }
}
