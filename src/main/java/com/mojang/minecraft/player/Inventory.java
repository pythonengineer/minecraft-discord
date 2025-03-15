package com.mojang.minecraft.player;

public final class Inventory {
	public int[] slots = new int[9];
	public int[] count = new int[9];
	public int[] popTime = new int[9];
	public int selected = 0;

	public Inventory() {
		for(int i1 = 0; i1 < 9; ++i1) {
			this.slots[i1] = -1;
			this.count[i1] = 0;
		}

	}

	public final int getSelected() {
		return this.slots[this.selected];
	}

	public int containsTileAt(int index) {
		for(int i2 = 0; i2 < this.slots.length; ++i2) {
			if(index == this.slots[i2]) {
				return i2;
			}
		}

		return -1;
	}

	public final void swapPaint(int index) {
		if(index > 0) {
			index = 1;
		}

		if(index < 0) {
			index = -1;
		}

		for(this.selected -= index; this.selected < 0; this.selected += this.slots.length) {
		}

		while(this.selected >= this.slots.length) {
			this.selected -= this.slots.length;
		}

	}

	public final boolean removeResource(int index) {
		if((index = this.containsTileAt(index)) < 0) {
			return false;
		} else {
			if(--this.count[index] <= 0) {
				this.slots[index] = -1;
			}

			return true;
		}
	}
}