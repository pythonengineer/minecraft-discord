package net.minecraft.client.gui;

import net.minecraft.game.IInventory;

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
            if(GuiCrafting.a(this.craftMatrix).getStackInSlot(var1) != null) {
                GuiCrafting.a(this.craftMatrix).decrStackSize(var1, 1);
            }
        }

    }
}
