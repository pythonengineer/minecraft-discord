package net.minecraft.game.item.recipe;

import java.util.Comparator;

final class RecipeSorter implements Comparator {
    RecipeSorter(CraftingManager craftingManager) {
    }

    public final int compare(Object craftingRecipe1, Object craftingRecipe2) {
        CraftingRecipe craftingRecipe10000 = (CraftingRecipe)craftingRecipe1;
        CraftingRecipe craftingRecipe21 = (CraftingRecipe)craftingRecipe2;
        CraftingRecipe craftingRecipe11 = craftingRecipe10000;
        return craftingRecipe21.getRecipeSize() < craftingRecipe11.getRecipeSize() ? -1 : (craftingRecipe21.getRecipeSize() > craftingRecipe11.getRecipeSize() ? 1 : 0);
    }
}