package net.minecraft.game.item.recipe;

import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;

public final class CraftingManager {
    private static final CraftingManager instance = new CraftingManager();

    public static ItemStack addRecipe(int[] var0) {
        return var0[0] == Item.apple.shiftedIndex ? new ItemStack(Item.arrow.shiftedIndex) : null;
    }
}
