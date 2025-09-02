package net.minecraft.game.item.recipe;

import net.minecraft.game.item.ItemStack;

public final class ShapedRecipes {
    private int recipeWidth;
    private int recipeHeight;
    private int[] recipeItems;
    private ItemStack recipeOutput;

    public ShapedRecipes(int var1, int var2, int[] var3, ItemStack var4) {
        this.recipeWidth = var1;
        this.recipeHeight = var2;
        this.recipeItems = var3;
        this.recipeOutput = var4;
    }

    public final boolean matches(int[] var1) {
        for(int var2 = 0; var2 <= 3 - this.recipeWidth; ++var2) {
            for(int var3 = 0; var3 <= 3 - this.recipeHeight; ++var3) {
                int var7 = var3;
                int var6 = var2;
                int[] var5 = var1;
                ShapedRecipes var4 = this;
                int var8 = 0;

                boolean var10000;
                label50:
                while(true) {
                    if(var8 >= 3) {
                        var10000 = true;
                        break;
                    }

                    for(int var9 = 0; var9 < 3; ++var9) {
                        int var10 = var8 - var6;
                        int var11 = var9 - var7;
                        int var12 = -1;
                        if(var10 >= 0 && var11 >= 0 && var10 < var4.recipeWidth && var11 < var4.recipeHeight) {
                            var12 = var4.recipeItems[var10 + var11 * var4.recipeWidth];
                        }

                        if(var5[var8 + var9 * 3] != var12) {
                            var10000 = false;
                            break label50;
                        }
                    }

                    ++var8;
                }

                if(var10000) {
                    return true;
                }
            }
        }

        return false;
    }

    public final ItemStack getCraftingResult() {
        return new ItemStack(this.recipeOutput.itemID, this.recipeOutput.stackSize);
    }
}
