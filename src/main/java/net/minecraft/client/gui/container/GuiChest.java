package net.minecraft.client.gui.container;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;

public class GuiChest extends GuiContainer {
    private IInventory upperChestInventory;
    private IInventory lowerChestInventory;
    private int inventoryRows = 0;

    public GuiChest(IInventory upperChestInventory, IInventory lowerChestInventory) {
        this.upperChestInventory = upperChestInventory;
        this.lowerChestInventory = lowerChestInventory;
        this.allowUserInput = false;
        this.inventoryRows = lowerChestInventory.getSizeInventory() / 9;
        this.ySize = 114 + this.inventoryRows * 18;
        int i3 = (this.inventoryRows - 4) * 18;

        int i4;
        int i5;
        for(i4 = 0; i4 < this.inventoryRows; ++i4) {
            for(i5 = 0; i5 < 9; ++i5) {
                this.inventorySlots.add(new Slot(this, lowerChestInventory, i5 + i4 * 9, 8 + i5 * 18, 18 + i4 * 18));
            }
        }

        for(i4 = 0; i4 < 3; ++i4) {
            for(i5 = 0; i5 < 9; ++i5) {
                this.inventorySlots.add(new Slot(this, upperChestInventory, i5 + (i4 + 1) * 9, 8 + i5 * 18, 103 + i4 * 18 + i3));
            }
        }

        for(i4 = 0; i4 < 9; ++i4) {
            this.inventorySlots.add(new Slot(this, upperChestInventory, i4, 8 + i4 * 18, i3 + 161));
        }

    }

    protected void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString(this.lowerChestInventory.getInvName(), 8, 6, 4210752);
        this.fontRenderer.drawString(this.upperChestInventory.getInvName(), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float f1) {
        int i1 = this.mc.renderEngine.getTexture("/gui/container.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i1);
        i1 = (this.width - this.xSize) / 2;
        int i2 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i1, i2, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(i1, i2 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }

    public ItemStack transferStackInSlot(EntityPlayer var1, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < this.inventoryRows * 9) {
                if (!this.mergeItemStack(itemstack1, this.inventoryRows * 9, this.inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.inventoryRows * 9, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
