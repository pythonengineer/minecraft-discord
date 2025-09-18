package net.minecraft.game.item.recipe;

import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

public final class RecipesWeapons {
	private String[][] a = new String[][]{{"X", "X", "#"}};
	private Object[][] b = new Object[][]{{Block.planks, Block.cobblestone, Item.ingotIron, Item.diamond, Item.ingotGold}, {Item.swordWood, Item.swordStone, Item.swordSteel, Item.swordDiamond, Item.swordGold}};

	public final void addRecipes(CraftingManager var1) {
		for(int var2 = 0; var2 < this.b[0].length; ++var2) {
			Object var3 = this.b[0][var2];

			for(int var4 = 0; var4 < this.b.length - 1; ++var4) {
				Item var5 = (Item)this.b[var4 + 1][var2];
				var1.addRecipe(new ItemStack(var5), new Object[]{this.a[var4][0], this.a[var4][1], this.a[var4][2], Character.valueOf('#'), Item.stick, Character.valueOf('X'), var3});
			}
		}

		var1.addRecipe(new ItemStack(Item.bow, 1), new Object[]{" #X", "# X", " #X", Character.valueOf('X'), Item.silk, Character.valueOf('#'), Item.stick});
		var1.addRecipe(new ItemStack(Item.arrow, 4), new Object[]{"X", "#", "Y", Character.valueOf('Y'), Item.feather, Character.valueOf('X'), Item.ingotIron, Character.valueOf('#'), Item.stick});
	}
}
