package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

class SlotArmor extends SlotInventory {
    final int armorType;
    final GuiInventory guiInventory;

    SlotArmor(GuiInventory inventoryGui, GuiContainer container, IInventory inventory, int slot, int x, int y, int armorType) {
        super(container, inventory, slot, x, y);
        this.guiInventory = inventoryGui;
        this.armorType = armorType;
    }

    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ItemArmor ? ((ItemArmor)stack.getItem()).armorType == this.armorType : false;
    }

    public int getBackgroundIconIndex() {
        return 15 + this.armorType * 16;
    }
}
