package net.minecraft.src;

public class ContainerFurnace extends Container {
	private TileEntityFurnace furnace;
	private int cookTime = 0;
	private int burnTime = 0;
	private int itemBurnTime = 0;

	public ContainerFurnace(InventoryPlayer var1, TileEntityFurnace var2) {
		this.furnace = var2;
		this.addSlot(new Slot(var2, 0, 56, 17));
		this.addSlot(new Slot(var2, 1, 56, 53));
		this.addSlot(new SlotFurnace(var1.player, var2, 2, 116, 35));

		int var3;
		for(var3 = 0; var3 < 3; ++var3) {
			for(int var4 = 0; var4 < 9; ++var4) {
				this.addSlot(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for(var3 = 0; var3 < 9; ++var3) {
			this.addSlot(new Slot(var1, var3, 8 + var3 * 18, 142));
		}

	}

	public void updateCraftingResults() {
		super.updateCraftingResults();

		for(int var1 = 0; var1 < this.field_20121_g.size(); ++var1) {
			ICrafting var2 = (ICrafting)this.field_20121_g.get(var1);
			if(this.cookTime != this.furnace.furnaceCookTime) {
				var2.func_20158_a(this, 0, this.furnace.furnaceCookTime);
			}

			if(this.burnTime != this.furnace.furnaceBurnTime) {
				var2.func_20158_a(this, 1, this.furnace.furnaceBurnTime);
			}

			if(this.itemBurnTime != this.furnace.currentItemBurnTime) {
				var2.func_20158_a(this, 2, this.furnace.currentItemBurnTime);
			}
		}

		this.cookTime = this.furnace.furnaceCookTime;
		this.burnTime = this.furnace.furnaceBurnTime;
		this.itemBurnTime = this.furnace.currentItemBurnTime;
	}

	public void func_20112_a(int var1, int var2) {
		if(var1 == 0) {
			this.furnace.furnaceCookTime = var2;
		}

		if(var1 == 1) {
			this.furnace.furnaceBurnTime = var2;
		}

		if(var1 == 2) {
			this.furnace.currentItemBurnTime = var2;
		}

	}

	public boolean isUsableByPlayer(EntityPlayer var1) {
		return this.furnace.canInteractWith(var1);
	}

    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.slots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return null;
                }
            } else if (i != 1 && i != 0) {
                if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1.itemID) != null) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                    }
                } else if (TileEntityFurnace.isItemFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return null;
                    }
                } else if (i >= 3 && i < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return null;
                    }
                } else if (i >= 30 && i < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
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

            slot.onPickupFromSlot(itemstack1);
        }

        return itemstack;
    }
}
