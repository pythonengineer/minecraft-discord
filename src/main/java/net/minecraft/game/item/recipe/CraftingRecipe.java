package net.minecraft.game.item.recipe;

import net.minecraft.game.item.ItemStack;

public final class CraftingRecipe {
    private int recipeWidth;
    private int recipeHeight;
    private int[] recipeItems;
    private ItemStack recipeOutput;

    public CraftingRecipe(int width, int height, int[] items, ItemStack output) {
        this.recipeWidth = width;
        this.recipeHeight = height;
        this.recipeItems = items;
        this.recipeOutput = output;
    }

    public final boolean matches(int[] items) {
        for(int i2 = 0; i2 <= 3 - this.recipeWidth; ++i2) {
            for(int i3 = 0; i3 <= 3 - this.recipeHeight; ++i3) {
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
                if(i7 >= 0 && i8 >= 0 && i7 < this.recipeWidth && i8 < this.recipeHeight) {
                    if(offsetRecipe) {
                        i9 = this.recipeItems[this.recipeWidth - i7 - 1 + i8 * this.recipeWidth];
                    } else {
                        i9 = this.recipeItems[i7 + i8 * this.recipeWidth];
                    }
                }

                if(items[i5 + i6 * 3] != i9) {
                    return false;
                }
            }
        }

        return true;
    }

    public final ItemStack getCraftingResult() {
        return new ItemStack(this.recipeOutput.itemID, this.recipeOutput.stackSize);
    }

    public final int getRecipeSize() {
        return this.recipeWidth * this.recipeHeight;
    }
}