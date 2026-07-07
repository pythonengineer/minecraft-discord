package net.minecraft.src;

public class CraftingInventoryDispenserCB extends CraftingInventoryCB {
	private TileEntityDispenser field_21149_a;

	public CraftingInventoryDispenserCB(IInventory var1, TileEntityDispenser var2) {
		this.field_21149_a = var2;

		int var3;
		int var4;
		for(var3 = 0; var3 < 3; ++var3) {
			for(var4 = 0; var4 < 3; ++var4) {
				this.func_20117_a(new Slot(var2, var4 + var3 * 3, 61 + var4 * 18, 17 + var3 * 18));
			}
		}

		for(var3 = 0; var3 < 3; ++var3) {
			for(var4 = 0; var4 < 9; ++var4) {
				this.func_20117_a(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for(var3 = 0; var3 < 9; ++var3) {
			this.func_20117_a(new Slot(var1, var3, 8 + var3 * 18, 142));
		}

	}

	public boolean func_20120_b(EntityPlayer var1) {
		return this.field_21149_a.canInteractWith(var1);
	}

    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.slots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < 9) {
                if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 9, false)) {
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
}
