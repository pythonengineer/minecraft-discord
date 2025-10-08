package net.minecraft.game.world.block.tileentity;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public final class TileEntityFurnace extends TileEntity implements IInventory {
	private ItemStack[] furnaceItemStacks = new ItemStack[3];
	private int furnaceBurnTime = 0;
	private int currentItemBurnTime = 0;
	private int furnaceCookTime = 0;

	public final int getSizeInventory() {
		return this.furnaceItemStacks.length;
	}

	public final ItemStack getStackInSlot(int var1) {
		return this.furnaceItemStacks[var1];
	}

	public final ItemStack decrStackSize(int var1, int var2) {
		if(this.furnaceItemStacks[var1] != null) {
			ItemStack var3;
			if(this.furnaceItemStacks[var1].stackSize <= var2) {
				var3 = this.furnaceItemStacks[var1];
				this.furnaceItemStacks[var1] = null;
				return var3;
			} else {
				var3 = this.furnaceItemStacks[var1].splitStack(var2);
				if(this.furnaceItemStacks[var1].stackSize == 0) {
					this.furnaceItemStacks[var1] = null;
				}

				return var3;
			}
		} else {
			return null;
		}
	}

	public final void setInventorySlotContents(int var1, ItemStack var2) {
		this.furnaceItemStacks[var1] = var2;
		if(var2 != null && var2.stackSize > 64) {
			var2.stackSize = 64;
		}

	}

	public final String getInvName() {
		return "Chest";
	}

	public final int getInventoryStackLimit() {
		return 64;
	}
}
