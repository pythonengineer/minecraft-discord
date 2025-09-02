package net.minecraft.game.item.recipe;

import java.util.ArrayList;
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
		for(int var1 = 0; var1 < 4; ++var1) {
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

			Item var3 = null;
			Item var4 = null;
			Item var5 = null;
			Item var6 = null;
			if(var1 == 0) {
				var3 = Item.pickaxeWood;
				var4 = Item.shovelWood;
				var5 = Item.axeWood;
				var6 = Item.swordWood;
			} else if(var1 == 1) {
				var3 = Item.pickaxeStone;
				var4 = Item.shovelStone;
				var5 = Item.axeStone;
				var6 = Item.swordStone;
			} else if(var1 == 2) {
				var3 = Item.pickaxeSteel;
				var4 = Item.shovel;
				var5 = Item.axeSteel;
				var6 = Item.swordSteel;
			} else {
				var3 = Item.pickaxeDiamond;
				var4 = Item.shovelDiamond;
				var5 = Item.axeDiamond;
				var6 = Item.swordDiamond;
			}

			this.addRecipe(new ItemStack(var3), new Object[]{"XXX", " # ", " # ", Character.valueOf('#'), Item.stick, Character.valueOf('X'), var2});
			this.addRecipe(new ItemStack(var4), new Object[]{"X", "#", "#", Character.valueOf('#'), Item.stick, Character.valueOf('X'), var2});
			this.addRecipe(new ItemStack(var5), new Object[]{"XX", "X#", " #", Character.valueOf('#'), Item.stick, Character.valueOf('X'), var2});
			this.addRecipe(new ItemStack(var6), new Object[]{"X", "X", "#", Character.valueOf('#'), Item.stick, Character.valueOf('X'), var2});
		}

		this.addRecipe(new ItemStack(Item.stick, 4), new Object[]{"#", "#", Character.valueOf('#'), Block.planks});
		this.addRecipe(new ItemStack(Block.blockGold), new Object[]{"##", "##", Character.valueOf('#'), Item.ingotGold});
		this.addRecipe(new ItemStack(Block.blockSteel), new Object[]{"##", "##", Character.valueOf('#'), Item.ingotIron});
		this.addRecipe(new ItemStack(Block.blockDiamond), new Object[]{"##", "##", Character.valueOf('#'), Item.diamond});
		this.addRecipe(new ItemStack(Block.torch, 4), new Object[]{"X", "#", Character.valueOf('X'), Item.coal, Character.valueOf('#'), Item.stick});
	}

	private void addRecipe(ItemStack var1, Object... var2) {
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

	public final ItemStack addRecipe(int[] var1) {
		for(int var2 = 0; var2 < this.recipes.size(); ++var2) {
			ShapedRecipes var3 = (ShapedRecipes)this.recipes.get(var2);
			if(var3.matches(var1)) {
				return var3.getCraftingResult();
			}
		}

		return null;
	}
}
