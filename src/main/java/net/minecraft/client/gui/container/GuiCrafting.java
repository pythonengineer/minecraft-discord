package net.minecraft.client.gui.container;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.item.recipe.CraftingManager;

public final class GuiCrafting extends GuiContainer {
	private InventoryCrafting craftingInventory = new InventoryCrafting(this, 3, 3);
	private IInventory craftingResultInventory = new InventoryCraftResult();

	public GuiCrafting(InventoryPlayer playerInventory) {
		this.inventorySlots.add(new SlotCrafting(this, this.craftingInventory, this.craftingResultInventory, 0, 124, 35));

		int i2;
		int i3;
		for(i2 = 0; i2 < 3; ++i2) {
			for(i3 = 0; i3 < 3; ++i3) {
				this.inventorySlots.add(new Slot(this, this.craftingInventory, i3 + i2 * 3, 30 + i3 * 18, 17 + i2 * 18));
			}
		}

		for(i2 = 0; i2 < 3; ++i2) {
			for(i3 = 0; i3 < 9; ++i3) {
				this.inventorySlots.add(new Slot(this, playerInventory, i3 + (i2 + 1) * 9, 8 + i3 * 18, 84 + i2 * 18));
			}
		}

		for(i2 = 0; i2 < 9; ++i2) {
			this.inventorySlots.add(new Slot(this, playerInventory, i2, 8 + i2 * 18, 142));
		}

	}

	public final void onGuiClosed() {
		super.onGuiClosed();

		for(int i1 = 0; i1 < 9; ++i1) {
			ItemStack itemStack = this.craftingInventory.getStackInSlot(i1);
			if(itemStack != null) {
				this.mc.thePlayer.dropPlayerItem(itemStack);
			}
		}

	}

	public final void onCraftMatrixChanged() {
		int[] i1 = new int[9];

		for(int i2 = 0; i2 < 3; ++i2) {
			for(int i3 = 0; i3 < 3; ++i3) {
				int i4 = i2 + i3 * 3;
				ItemStack itemStack = this.craftingInventory.getStackInSlot(i4);
				if(itemStack == null) {
					i1[i4] = -1;
				} else {
					i1[i4] = itemStack.itemID;
				}
			}
		}

		this.craftingResultInventory.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(i1));
	}

	protected final void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Crafting", 28, 6, 4210752);
		this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	protected final void drawGuiContainerBackgroundLayer() {
		int i1 = this.mc.renderEngine.getTexture("/gui/crafting.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderEngine.bindTexture(i1);
		i1 = (this.width - this.xSize) / 2;
		int i2 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i1, i2, 0, 0, this.xSize, this.ySize);
	}

    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i == 0) {
                if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
                    return null;
                }
            } else if (i >= 10 && i < 37) {
                if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
                    return null;
                }
            } else if (i >= 37 && i < 46) {
                if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
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

    public boolean canMergeSlot(ItemStack itemstack, Slot slot) {
        return slot.inventory != this.craftingResultInventory && super.canMergeSlot(itemstack, slot);
    }
}
