package net.minecraft.game.item.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

public final class CraftingManager {
	private static final CraftingManager instance = new CraftingManager();
	private List recipes = new ArrayList();

	public static final CraftingManager getInstance() {
		return instance;
	}

	private CraftingManager() {
		(new RecipesTools()).addRecipes(this);
        (new RecipesWeapons()).addRecipes(this);
		(new RecipesIngots()).addRecipes(this);
		new RecipesBowl();
		this.addRecipe(new ItemStack(Item.bowlSoup), new Object[]{"Y", "X", "#", Character.valueOf('X'), Block.mushroomBrown, Character.valueOf('Y'), Block.mushroomRed, Character.valueOf('#'), Item.bowlEmpty});
		this.addRecipe(new ItemStack(Item.bowlSoup), new Object[]{"Y", "X", "#", Character.valueOf('X'), Block.mushroomRed, Character.valueOf('Y'), Block.mushroomBrown, Character.valueOf('#'), Item.bowlEmpty});
		new RecipesBlocks();
		this.addRecipe(new ItemStack(Block.chest), new Object[]{"###", "# #", "###", Character.valueOf('#'), Block.planks});
		this.addRecipe(new ItemStack(Block.workbench), new Object[]{"##", "##", Character.valueOf('#'), Block.planks});
        this.addRecipe(new ItemStack(Block.clothGray, 1), new Object[]{"###", "###", "###", Character.valueOf('#'), Item.silk});
		this.addRecipe(new ItemStack(Block.tnt, 1), new Object[]{"X#X", "#X#", "X#X", Character.valueOf('X'), Item.gunpowder, Character.valueOf('#'), Block.sand});
		this.addRecipe(new ItemStack(Block.stairSingle, 3), new Object[]{"###", Character.valueOf('#'), Block.cobblestone});
		this.addRecipe(new ItemStack(Item.stick, 4), new Object[]{"#", "#", Character.valueOf('#'), Block.planks});
		this.addRecipe(new ItemStack(Block.torch, 4), new Object[]{"X", "#", Character.valueOf('X'), Item.coal, Character.valueOf('#'), Item.stick});
		this.addRecipe(new ItemStack(Item.bowlEmpty, 4), new Object[]{"# #", " # ", Character.valueOf('#'), Block.planks});
		Collections.sort(this.recipes, new RecipeSorter(this));
        System.out.println(this.recipes.size() + " recipes");
	}

	final void addRecipe(ItemStack var1, Object... var2) {
		String var3 = "";
		int var4 = 0;
		int var5 = 0;

		int var6;
		String var7;
		for(var6 = 0; var2[var4] instanceof String; var3 = var3 + var7) {
			var7 = (String)var2[var4++];
			++var6;
			var5 = var7.length();
		}

		int var9;
		HashMap var11;
		for(var11 = new HashMap(); var4 < var2.length; var4 += 2) {
			Character var8 = (Character)var2[var4];
			var9 = 0;
			if(var2[var4 + 1] instanceof Item) {
				var9 = ((Item)var2[var4 + 1]).shiftedIndex;
			} else if(var2[var4 + 1] instanceof Block) {
				var9 = ((Block)var2[var4 + 1]).blockID;
			}

			var11.put(var8, Integer.valueOf(var9));
		}

		int[] var12 = new int[var5 * var6];

		for(var9 = 0; var9 < var5 * var6; ++var9) {
			char var10 = var3.charAt(var9);
			if(var11.containsKey(Character.valueOf(var10))) {
				var12[var9] = ((Integer)var11.get(Character.valueOf(var10))).intValue();
			} else {
				var12[var9] = -1;
			}
		}

		this.recipes.add(new ShapedRecipes(var5, var6, var12, var1));
	}

	public final ItemStack findMatchingRecipe(int[] var1) {
		for(int var2 = 0; var2 < this.recipes.size(); ++var2) {
			ShapedRecipes var3 = (ShapedRecipes)this.recipes.get(var2);
			if(var3.matches(var1)) {
				return var3.getCraftingResult();
			}
		}

		return null;
	}
}
