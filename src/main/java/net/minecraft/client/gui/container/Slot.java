package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public class Slot {
	public final int slotIndex;
	public final int xDisplayPosition;
	public final int yDisplayPosition;
	public final IInventory inventory;
	private final GuiContainer slotContainer;

    public Slot(GuiContainer container, IInventory inventory, int slot, int x, int y) {
        this.slotContainer = container;
        this.inventory = inventory;
        this.slotIndex = slot;
        this.xDisplayPosition = x;
        this.yDisplayPosition = y;
    }

    public final boolean getIsMouseOverSlot(int x, int y) {
        int i3 = (this.slotContainer.width - this.slotContainer.xSize) / 2;
        int i4 = (this.slotContainer.height - this.slotContainer.ySize) / 2;
        x -= i3;
        y -= i4;
        return x >= this.xDisplayPosition - 1 && x < this.xDisplayPosition + 16 + 1 && y >= this.yDisplayPosition - 1 && y < this.yDisplayPosition + 16 + 1;
    }

	public void onPickupFromSlot() {
        this.onSlotChanged();
	}

    public boolean isItemValid(ItemStack stack) {
        return true;
    }

    public ItemStack getStack() {
        return this.inventory.getStackInSlot(this.slotIndex);
    }

    public boolean getHasStack() {
        return this.getStack() != null;
    }

    public ItemStack decrStackSize(int i) {
        return this.inventory.decrStackSize(this.slotIndex, i);
    }

    public final void putStack(ItemStack stack) {
        this.inventory.setInventorySlotContents(this.slotIndex, stack);
        this.onSlotChanged();
    }

    public int getRenderIndex() {
        return -1;
    }

    public final void onSlotChanged() {
        this.inventory.onInventoryChanged();
    }
}
