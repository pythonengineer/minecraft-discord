package net.minecraft.src;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class GuiFurnace extends GuiContainer {
	private TileEntityFurnace field_978_j;

	public GuiFurnace(InventoryPlayer var1, TileEntityFurnace var2) {
		this.field_978_j = var2;
		this.inventorySlots.add(new SlotInventory(this, var2, 0, 56, 17));
		this.inventorySlots.add(new SlotInventory(this, var2, 1, 56, 53));
		this.inventorySlots.add(new SlotInventory(this, var2, 2, 116, 35));

		int var3;
		for(var3 = 0; var3 < 3; ++var3) {
			for(int var4 = 0; var4 < 9; ++var4) {
				this.inventorySlots.add(new SlotInventory(this, var1, var4 + (var3 + 1) * 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for(var3 = 0; var3 < 9; ++var3) {
			this.inventorySlots.add(new SlotInventory(this, var1, var3, 8 + var3 * 18, 142));
		}

	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Furnace", 60, 6, 4210752);
		this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float var1) {
		int var2 = this.mc.renderEngine.getTexture("/gui/furnace.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var2);
		int var3 = (this.width - this.xSize) / 2;
		int var4 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var3, var4, 0, 0, this.xSize, this.ySize);
		int var5;
		if(this.field_978_j.isBurning()) {
			var5 = this.field_978_j.getBurnTimeRemainingScaled(12);
			this.drawTexturedModalRect(var3 + 56, var4 + 36 + 12 - var5, 176, 12 - var5, 14, var5 + 2);
		}

		var5 = this.field_978_j.getCookProgressScaled(24);
		this.drawTexturedModalRect(var3 + 79, var4 + 34, 176, 14, var5 + 1, 16);
	}

    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i) {
        ItemStack itemstack = null;
        SlotInventory slot = (SlotInventory) this.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return null;
                }
            } else if (i != 1 && i != 0) {
                if (TileEntityFurnace.getSmeltingResultItem(itemstack1.itemID) != -1) {
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
