package net.minecraft.client.player;

import net.minecraft.game.level.block.Block;

public final class InventoryPlayer {
	public int[] mainInventory = new int[9];
	public int[] stackSize = new int[9];
	public int[] animationsToGo = new int[9];
	public int currentItem = 0;

	public InventoryPlayer() {
		for(int var1 = 0; var1 < 9; ++var1) {
			this.mainInventory[var1] = -1;
			this.stackSize[var1] = 0;
		}

	}

	public final int getCurrentItem() {
		return this.mainInventory[this.currentItem];
	}

	public final int getInventorySlotContainItem(int var1) {
		for(int var2 = 0; var2 < this.mainInventory.length; ++var2) {
			if(var1 == this.mainInventory[var2]) {
				return var2;
			}
		}

		return -1;
	}

	public final void replaceSlot(Block var1) {
		if(var1 != null) {
			int var2 = this.getInventorySlotContainItem(var1.blockID);
			if(var2 >= 0) {
				this.mainInventory[var2] = this.mainInventory[this.currentItem];
			}

			this.mainInventory[this.currentItem] = var1.blockID;
		}

	}

	public final boolean consumeInventoryItem(int var1) {
		var1 = this.getInventorySlotContainItem(var1);
		if(var1 < 0) {
			return false;
		} else {
			if(--this.stackSize[var1] <= 0) {
				this.mainInventory[var1] = -1;
			}

			return true;
		}
	}
}
