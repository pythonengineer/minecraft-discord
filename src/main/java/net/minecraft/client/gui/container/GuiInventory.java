package net.minecraft.client.gui.container;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.item.recipe.CraftingManager;

public final class GuiInventory extends GuiContainer {
	private InventoryCrafting k = new InventoryCrafting(this, 2, 2);
	private IInventory l = new InventoryCraftResult();

	public GuiInventory(IInventory var1) {
		this.allowUserInput = true;
		this.inventorySlots.add(new SlotCrafting(this, this.k, this.l, 0, 144, 36));

		int var2;
		int var3;
		for(var2 = 0; var2 < 2; ++var2) {
			for(var3 = 0; var3 < 2; ++var3) {
				this.inventorySlots.add(new Slot(this, this.k, var3 + (var2 << 1), 88 + var3 * 18, 26 + var2 * 18));
			}
		}

		for(var2 = 0; var2 < 4; ++var2) {
			this.inventorySlots.add(new Slot(this, var1, var1.getSizeInventory() - 1 - var2, 8, 8 + var2 * 18));
		}

		for(var2 = 0; var2 < 3; ++var2) {
			for(var3 = 0; var3 < 9; ++var3) {
				this.inventorySlots.add(new Slot(this, var1, var3 + (var2 + 1) * 9, 8 + var3 * 18, 84 + var2 * 18));
			}
		}

		for(var2 = 0; var2 < 9; ++var2) {
			this.inventorySlots.add(new Slot(this, var1, var2, 8 + var2 * 18, 142));
		}

	}

	public final void onGuiClosed() {
		super.onGuiClosed();

		for(int var1 = 0; var1 < this.k.getSizeInventory(); ++var1) {
			ItemStack var2 = this.k.getStackInSlot(var1);
			if(var2 != null) {
				this.mc.thePlayer.dropPlayerItemWithRandomChoice(var2);
			}
		}

	}

	public final void guiCraftingItemsCheck() {
		int[] var1 = new int[9];

		for(int var2 = 0; var2 < 3; ++var2) {
			for(int var3 = 0; var3 < 3; ++var3) {
				int var4 = -1;
				if(var2 < 2 && var3 < 2) {
					ItemStack var5 = this.k.getStackInSlot(var2 + (var3 << 1));
					if(var5 != null) {
						var4 = var5.itemID;
					}
				}

				var1[var2 + var3 * 3] = var4;
			}
		}

		this.l.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(var1));
	}

	protected final void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Crafting", 86, 16, 4210752);
	}

	protected final void drawGuiContainerBackgroundLayer() {
		int var1 = this.mc.renderEngine.getTexture("/gui/inventory.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1);
		var1 = (this.width - this.xSize) / 2;
		int var2 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var1, var2, 0, 0, this.xSize, this.ySize);
	}

    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i == 0) {
                if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
                    return null;
                }
            } else if (i >= 1 && i < 5) {
                if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
                    return null;
                }
            } else if (i >= 5 && i < 9) {
                if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
                    return null;
                }
            //} else if (itemstack.getItem() instanceof ItemArmor
            //        && !((Slot) this.inventorySlots.get(5 + ((ItemArmor) itemstack.getItem()).armorType))
            //                .getHasStack()) {
            //    int j = 5 + ((ItemArmor) itemstack.getItem()).armorType;
            //    if (!this.mergeItemStack(itemstack1, j, j + 1, false)) {
            //        return null;
            //    }
            } else if (i >= 9 && i < 36) {
                if (!this.mergeItemStack(itemstack1, 36, 45, false)) {
                    return null;
                }
            } else if (i >= 36 && i < 45) {
                if (!this.mergeItemStack(itemstack1, 9, 36, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot();
        }

        return itemstack;
    }
}
