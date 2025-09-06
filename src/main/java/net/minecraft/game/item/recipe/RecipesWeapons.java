package net.minecraft.game.item.recipe;

import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

public final class RecipesWeapons {
    public static void addRecipes(CraftingManager var0) {
        for(int var1 = 0; var1 < 5; ++var1) {
            Object var2 = null;
            if(var1 == 0) {
                var2 = Block.planks;
            }

            if(var1 == 1) {
                var2 = Block.cobblestone;
            }

            if(var1 == 2) {
                var2 = Item.ingotIron;
            }

            if(var1 == 3) {
                var2 = Item.diamond;
            }

            if(var1 == 4) {
                var2 = Item.ingotGold;
            }

            Item var3 = null;
            if(var1 == 0) {
                var3 = Item.swordWood;
            } else if(var1 == 1) {
                var3 = Item.swordStone;
            } else if(var1 == 2) {
                var3 = Item.swordSteel;
            } else if(var1 == 3) {
                var3 = Item.swordDiamond;
            } else if(var1 == 4) {
                var3 = Item.swordGold;
            }

            var0.addRecipe(new ItemStack(var3), new Object[]{"X", "X", "#", Character.valueOf('#'), Item.stick, Character.valueOf('X'), var2});
        }

    }
}
