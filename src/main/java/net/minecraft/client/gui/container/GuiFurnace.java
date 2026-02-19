package net.minecraft.client.gui.container;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.block.tileentity.TileEntityFurnace;

public final class GuiFurnace extends GuiContainer {
    private TileEntityFurnace furnaceInventory;

    public GuiFurnace(InventoryPlayer var1, TileEntityFurnace var2) {
        new InventoryCraftResult();
        this.furnaceInventory = var2;
        this.inventorySlots.add(new Slot(this, var2, 0, 56, 17));
        this.inventorySlots.add(new Slot(this, var2, 1, 56, 53));
        this.inventorySlots.add(new Slot(this, var2, 2, 116, 35));

        int var4;
        for(var4 = 0; var4 < 3; ++var4) {
            for(int var3 = 0; var3 < 9; ++var3) {
                this.inventorySlots.add(new Slot(this, var1, var3 + (var4 + 1) * 9, 8 + var3 * 18, 84 + var4 * 18));
            }
        }

        for(var4 = 0; var4 < 9; ++var4) {
            this.inventorySlots.add(new Slot(this, var1, var4, 8 + var4 * 18, 142));
        }

    }

    protected final void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString("Furnace", 60, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    protected final void drawGuiContainerBackgroundLayer() {
        int var1 = this.mc.renderEngine.getTexture("/gui/furnace.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderEngine.bindTexture(var1);
        var1 = (this.width - this.xSize) / 2;
        int var2 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var1, var2, 0, 0, this.xSize, this.ySize);
        int var3;
        if(this.furnaceInventory.isBurning()) {
            var3 = this.furnaceInventory.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(var1 + 56, var2 + 36 + 12 - var3, 176, 12 - var3, 14, var3 + 2);
        }

        var3 = this.furnaceInventory.getCookProgressScaled(24);
        this.drawTexturedModalRect(var1 + 79, var2 + 34, 176, 14, var3 + 1, 16);
    }

    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return null;
                }
            } else if (i != 1 && i != 0) {
                if (TileEntityFurnace.smeltItem(itemstack1.itemID) != -1) {
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

            slot.onPickupFromSlot();
        }

        return itemstack;
    }
}
