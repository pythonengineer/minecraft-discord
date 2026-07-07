package net.minecraft.src;

public class CraftingInventoryPlayerCB extends CraftingInventoryCB {
	public InventoryCrafting craftMatrix;
	public IInventory craftResult;
	public boolean isSinglePlayer;

	public CraftingInventoryPlayerCB(InventoryPlayer var1) {
		this(var1, true);
	}

	public CraftingInventoryPlayerCB(InventoryPlayer var1, boolean var2) {
		this.craftMatrix = new InventoryCrafting(this, 2, 2);
		this.craftResult = new InventoryCraftResult();
		this.isSinglePlayer = false;
		this.isSinglePlayer = var2;
		this.func_20117_a(new SlotCrafting(this.craftMatrix, this.craftResult, 0, 144, 36));

		int var3;
		int var4;
		for(var3 = 0; var3 < 2; ++var3) {
			for(var4 = 0; var4 < 2; ++var4) {
				this.func_20117_a(new Slot(this.craftMatrix, var4 + var3 * 2, 88 + var4 * 18, 26 + var3 * 18));
			}
		}

		for(var3 = 0; var3 < 4; ++var3) {
			this.func_20117_a(new SlotArmor(this, var1, var1.getSizeInventory() - 1 - var3, 8, 8 + var3 * 18, var3));
		}

		for(var3 = 0; var3 < 3; ++var3) {
			for(var4 = 0; var4 < 9; ++var4) {
				this.func_20117_a(new Slot(var1, var4 + (var3 + 1) * 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for(var3 = 0; var3 < 9; ++var3) {
			this.func_20117_a(new Slot(var1, var3, 8 + var3 * 18, 142));
		}

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	public void onCraftMatrixChanged(IInventory var1) {
		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix));
	}

	public void onCraftGuiClosed(EntityPlayer var1) {
		super.onCraftGuiClosed(var1);

		for(int var2 = 0; var2 < 4; ++var2) {
			ItemStack var3 = this.craftMatrix.getStackInSlot(var2);
			if(var3 != null) {
				var1.dropPlayerItem(var3);
				this.craftMatrix.setInventorySlotContents(var2, (ItemStack)null);
			}
		}

	}

	public boolean func_20120_b(EntityPlayer var1) {
		return true;
	}

    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.slots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i == 0) {
                if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
                    return null;
                }
            } else if (i >= 1 && i < 5) {
                if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
                    return null;
                }
            } else if (i >= 5 && i < 9) {
                if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
                    return null;
                }
            } else if (itemstack.getItem() instanceof ItemArmor
                    && !((Slot) this.slots.get(5 + ((ItemArmor) itemstack.getItem()).armorType))
                            .getHasStack()) {
                int j = 5 + ((ItemArmor) itemstack.getItem()).armorType;
                if (!this.mergeItemStack(itemstack1, j, j + 1, false)) {
                    return null;
                }
            } else if (i >= 9 && i < 36) {
                if (!this.mergeItemStack(itemstack1, 36, 45, false)) {
                    return null;
                }
            } else if (i >= 36 && i < 45) {
                if (!this.mergeItemStack(itemstack1, 9, 36, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot();
        }

        return itemstack;
    }

    public boolean canMergeSlot(ItemStack itemstack, Slot slot) {
        return slot.inventory != this.craftResult && super.canMergeSlot(itemstack, slot);
    }
}
