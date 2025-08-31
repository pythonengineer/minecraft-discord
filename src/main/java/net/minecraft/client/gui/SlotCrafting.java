package net.minecraft.client.gui;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public final class SlotCrafting extends Slot {
    private GuiCrafting craftMatrix;

    public SlotCrafting(GuiCrafting var1, IInventory var2, int var3, int var4, int var5) {
        super(var1, var2, 0, 124, 34);
        this.craftMatrix = var1;
    }

    public final boolean isItemValid() {
        return false;
    }

    public final void onPickupFromSlot() {
        for(int var1 = 0; var1 < 9; ++var1) {
            GuiCrafting.a(this.craftMatrix).setInventorySlotContents(var1, (ItemStack)null);
        }

    }
}
