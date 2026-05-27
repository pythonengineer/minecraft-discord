package net.minecraft.game.item.recipe;

import net.minecraft.game.item.ItemStack;

public class CraftingRecipe {
    private int width;
    private int height;
    private int[] ingredientMap;
    private ItemStack resultStack;
    public final int resultId;

    public CraftingRecipe(int width, int height, int[] items, ItemStack output) {
        this.resultId = output.itemID;
        this.width = width;
        this.height = height;
        this.ingredientMap = items;
        this.resultStack = output;
    }

    public boolean matchRecipe(int[] items) {
        for(int i2 = 0; i2 <= 3 - this.width; ++i2) {
            for(int i3 = 0; i3 <= 3 - this.height; ++i3) {
                if(this.checkMatch(items, i2, i3, true)) {
                    return true;
                }

                if(this.checkMatch(items, i2, i3, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkMatch(int[] items, int width, int height, boolean offsetRecipe) {
        for(int i5 = 0; i5 < 3; ++i5) {
            for(int i6 = 0; i6 < 3; ++i6) {
                int i7 = i5 - width;
                int i8 = i6 - height;
                int i9 = -1;
                if(i7 >= 0 && i8 >= 0 && i7 < this.width && i8 < this.height) {
                    if(offsetRecipe) {
                        i9 = this.ingredientMap[this.width - i7 - 1 + i8 * this.width];
                    } else {
                        i9 = this.ingredientMap[i7 + i8 * this.width];
                    }
                }

                if(items[i5 + i6 * 3] != i9) {
                    return false;
                }
            }
        }

        return true;
    }

    public ItemStack createResult(int[] i1) {
        return new ItemStack(this.resultStack.itemID, this.resultStack.stackSize);
    }

    public int getRecipeSize() {
        return this.width * this.height;
    }
}
