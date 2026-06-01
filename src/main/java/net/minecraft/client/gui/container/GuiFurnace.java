package net.minecraft.client.gui.container;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.block.tileentity.TileEntityFurnace;

public class GuiFurnace extends GuiContainer {
    private TileEntityFurnace furnaceInventory;

    public GuiFurnace(InventoryPlayer playerInventory, TileEntityFurnace furnaceTileEntity) {
        this.furnaceInventory = furnaceTileEntity;
        this.inventorySlots.add(new Slot(this, furnaceTileEntity, 0, 56, 17));
        this.inventorySlots.add(new Slot(this, furnaceTileEntity, 1, 56, 53));
        this.inventorySlots.add(new Slot(this, furnaceTileEntity, 2, 116, 35));

        int i4;
        for(i4 = 0; i4 < 3; ++i4) {
            for(int i3 = 0; i3 < 9; ++i3) {
                this.inventorySlots.add(new Slot(this, playerInventory, i3 + (i4 + 1) * 9, 8 + i3 * 18, 84 + i4 * 18));
            }
        }

        for(i4 = 0; i4 < 9; ++i4) {
            this.inventorySlots.add(new Slot(this, playerInventory, i4, 8 + i4 * 18, 142));
        }

    }

    protected void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString("Furnace", 60, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float f1) {
        int i1 = this.mc.renderEngine.getTexture("/gui/furnace.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i1);
        i1 = (this.width - this.xSize) / 2;
        int i2 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i1, i2, 0, 0, this.xSize, this.ySize);
        int i3;
        if(this.furnaceInventory.isBurning()) {
            i3 = this.furnaceInventory.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(i1 + 56, i2 + 36 + 12 - i3, 176, 12 - i3, 14, i3 + 2);
        }

        i3 = this.furnaceInventory.getCookProgressScaled(24);
        this.drawTexturedModalRect(i1 + 79, i2 + 34, 176, 14, i3 + 1, 16);
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
                if (TileEntityFurnace.getCookedItem(itemstack1.itemID) != -1) {
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
