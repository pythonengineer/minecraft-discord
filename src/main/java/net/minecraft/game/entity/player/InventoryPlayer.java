package net.minecraft.game.entity.player;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

public final class InventoryPlayer implements IInventory {
	public ItemStack[] mainInventory = new ItemStack[36];
	public ItemStack[] armorInventory = new ItemStack[4];
	public int currentItem = 0;
	private EntityPlayer player;

	public InventoryPlayer(EntityPlayer playerEntity) {
		this.player = playerEntity;
	}

	public final ItemStack getCurrentItem() {
		return this.mainInventory[this.currentItem];
	}

	private int storeItemStack(int shiftedIndex) {
		for(int i2 = 0; i2 < this.mainInventory.length; ++i2) {
			if(this.mainInventory[i2] != null && this.mainInventory[i2].itemID == shiftedIndex) {
				return i2;
			}
		}

		return -1;
	}

	public int getFirstEmptyStack() {
		for(int i1 = 0; i1 < this.mainInventory.length; ++i1) {
			if(this.mainInventory[i1] == null) {
				return i1;
			}
		}

		return -1;
	}

	public final void changeCurrentItem(int blockID) {
		if((blockID = this.storeItemStack(blockID)) >= 0 && blockID < 9) {
			this.currentItem = blockID;
		}
	}

	public final boolean consumeInventoryItem(int shiftedIndex) {
		if((shiftedIndex = this.storeItemStack(shiftedIndex)) < 0) {
			return false;
		} else {
			if(--this.mainInventory[shiftedIndex].stackSize <= 0) {
				this.mainInventory[shiftedIndex] = null;
			}

			return true;
		}
	}

	public final boolean addItemStackToInventory(ItemStack stack) {
		if(stack.itemDmg == 0) {
			int i4 = stack.stackSize;
			int i3 = stack.itemID;
			int i6 = i3;
			InventoryPlayer inventoryPlayer5 = this;
			int i7 = 0;

			int i10001;
			while(true) {
				if(i7 >= inventoryPlayer5.mainInventory.length) {
					i10001 = -1;
					break;
				}

				if(inventoryPlayer5.mainInventory[i7] != null && inventoryPlayer5.mainInventory[i7].itemID == i6 && inventoryPlayer5.mainInventory[i7].stackSize < inventoryPlayer5.mainInventory[i7].getItem().getItemStackLimit() && inventoryPlayer5.mainInventory[i7].stackSize < 64) {
					i10001 = i7;
					break;
				}

				++i7;
			}

			int i9 = i10001;
			if(i10001 < 0) {
				i9 = this.getFirstEmptyStack();
			}

			if(i9 < 0) {
				i10001 = i4;
			} else {
				if(this.mainInventory[i9] == null) {
					this.mainInventory[i9] = new ItemStack(i3, 0);
				}

				i3 = i4;
				if(i4 > this.mainInventory[i9].getItem().getItemStackLimit() - this.mainInventory[i9].stackSize) {
					i3 = this.mainInventory[i9].getItem().getItemStackLimit() - this.mainInventory[i9].stackSize;
				}

				if(i3 > 64 - this.mainInventory[i9].stackSize) {
					i3 = 64 - this.mainInventory[i9].stackSize;
				}

				if(i3 == 0) {
					i10001 = i4;
				} else {
					i4 -= i3;
					this.mainInventory[i9].stackSize += i3;
					this.mainInventory[i9].animationsToGo = 5;
					i10001 = i4;
				}
			}

			stack.stackSize = i10001;
			if(stack.stackSize == 0) {
				return true;
			}
		}

		int i2;
		if((i2 = this.getFirstEmptyStack()) >= 0) {
			this.mainInventory[i2] = stack;
			this.mainInventory[i2].animationsToGo = 5;
			return true;
		} else {
			return false;
		}
	}

	public final ItemStack decrStackSize(int slot, int decrementAmount) {
		ItemStack[] itemStack3 = this.mainInventory;
		if(slot >= this.mainInventory.length) {
			itemStack3 = this.armorInventory;
			slot -= this.mainInventory.length;
		}

		if(itemStack3[slot] != null) {
			ItemStack decrementAmount1;
			if(itemStack3[slot].stackSize <= decrementAmount) {
				decrementAmount1 = itemStack3[slot];
				itemStack3[slot] = null;
				return decrementAmount1;
			} else {
				decrementAmount1 = itemStack3[slot].splitStack(decrementAmount);
				if(itemStack3[slot].stackSize == 0) {
					itemStack3[slot] = null;
				}

				return decrementAmount1;
			}
		} else {
			return null;
		}
	}

	public final void setInventorySlotContents(int slot, ItemStack stack) {
		ItemStack[] itemStack3 = this.mainInventory;
		if(slot >= this.mainInventory.length) {
			itemStack3 = this.armorInventory;
			slot -= this.mainInventory.length;
		}

		itemStack3[slot] = stack;
	}

	public final int getSizeInventory() {
		return this.mainInventory.length + 4;
	}

	public final ItemStack getStackInSlot(int slot) {
		ItemStack[] itemStack2 = this.mainInventory;
		if(slot >= this.mainInventory.length) {
			itemStack2 = this.armorInventory;
			slot -= this.mainInventory.length;
		}

		return itemStack2[slot];
	}

	public final String getInvName() {
		return "Inventory";
	}

	public final int getInventoryStackLimit() {
		return 64;
	}

	public final int getTotalArmorValue() {
		int i1 = 0;
		int i2 = 0;
		int i3 = 0;

		for(int i4 = 0; i4 < this.armorInventory.length; ++i4) {
			if(this.armorInventory[i4] != null && this.armorInventory[i4].getItem() instanceof ItemArmor) {
				int i5 = this.armorInventory[i4].getMaxDamage();
				int i6 = this.armorInventory[i4].itemDmg;
				i6 = i5 - i6;
				i2 += i6;
				i3 += i5;
				i5 = ((ItemArmor)this.armorInventory[i4].getItem()).damageReduceAmount;
				i1 += i5;
			}
		}

		if(i3 == 0) {
			return 0;
		} else {
			return (i1 - 1) * i2 / i3 + 1;
		}
	}

	public final void dropAllItems() {
		int i1;
		for(i1 = 0; i1 < this.mainInventory.length; ++i1) {
			if(this.mainInventory[i1] != null) {
				this.player.dropPlayerItemWithRandomChoice(this.mainInventory[i1], true);
				this.mainInventory[i1] = null;
			}
		}

		for(i1 = 0; i1 < this.armorInventory.length; ++i1) {
			if(this.armorInventory[i1] != null) {
				this.player.dropPlayerItemWithRandomChoice(this.armorInventory[i1], true);
				this.armorInventory[i1] = null;
			}
		}

	}

	public final void onInventoryChanged() {
	}
}