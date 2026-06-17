package net.minecraft.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.game.IInventory;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;

public class CraftingInventoryCB {
    protected List list = new ArrayList();

    public void onCraftGuiClosed(EntityPlayer entityPlayer) {
        InventoryPlayer inventoryPlayer2 = entityPlayer.inventory;
        if(inventoryPlayer2.draggedItemStack != null) {
            entityPlayer.dropPlayerItem(inventoryPlayer2.draggedItemStack);
        }

    }

    public void onCraftMatrixChanged(IInventory inventory) {
    }
}
