package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

final class SlotArmor extends Slot {
    private int armorType;

    SlotArmor(GuiInventory inventoryGui, GuiContainer container, IInventory inventory, int slot, int x, int y, int armorType) {
        super(container, inventory, slot, 8, y);
        this.armorType = armorType;
    }

    public final boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ItemArmor ? ((ItemArmor)stack.getItem()).armorType == this.armorType : false;
    }

    public final int getRenderIndex() {
        return 15 + (this.armorType << 4);
    }
}
