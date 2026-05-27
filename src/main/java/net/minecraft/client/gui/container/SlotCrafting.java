package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

class SlotCrafting extends Slot {
    private final IInventory craftMatrix;

    public SlotCrafting(GuiContainer containerGui, IInventory craftMatrix, IInventory inventory, int slot, int x, int y) {
        super(containerGui, inventory, 0, x, y);
        this.craftMatrix = craftMatrix;
    }

    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    public void onPickupFromSlot() {
        for(int i1 = 0; i1 < this.craftMatrix.getSizeInventory(); ++i1) {
            if(this.craftMatrix.getStackInSlot(i1) != null) {
                this.craftMatrix.decrStackSize(i1, 1);
            }
        }

    }
}
