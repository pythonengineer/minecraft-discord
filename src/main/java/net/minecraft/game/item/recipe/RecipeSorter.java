package net.minecraft.game.item.recipe;

import java.util.Comparator;

class RecipeSorter implements Comparator {
    final CraftingManager craftingManager;

    RecipeSorter(CraftingManager craftingManager1) {
        this.craftingManager = craftingManager1;
    }

    public int a(CraftingRecipe craftingRecipe1, CraftingRecipe craftingRecipe2) {
        return craftingRecipe2.getRecipeSize() < craftingRecipe1.getRecipeSize() ? -1 : (craftingRecipe2.getRecipeSize() > craftingRecipe1.getRecipeSize() ? 1 : 0);
    }

    public int compare(Object object1, Object object2) {
        return this.a((CraftingRecipe)object1, (CraftingRecipe)object2);
    }
}
