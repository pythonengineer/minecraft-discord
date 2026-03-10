package net.minecraft.client.gui.container;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.item.recipe.CraftingManager;

public final class GuiInventory extends GuiContainer {
    private InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    private IInventory craftResult = new InventoryCraftResult();
    private float xSize_lo;
    private float ySize_lo;

	public GuiInventory(IInventory var1) {
		this.allowUserInput = true;
        this.slotsList.add(new SlotCrafting(this, this.craftMatrix, this.craftResult, 0, 144, 36));

        int i2;
        int i3;
        for(i2 = 0; i2 < 2; ++i2) {
            for(i3 = 0; i3 < 2; ++i3) {
                this.slotsList.add(new Slot(this, this.craftMatrix, i3 + (i2 << 1), 88 + i3 * 18, 26 + i2 * 18));
            }
        }

		for(i2 = 0; i2 < 4; ++i2) {
            this.slotsList.add(new SlotArmor(this, this, var1, var1.getSizeInventory() - 1 - i2, 8, 8 + i2 * 18, i2));
		}

		for(i2 = 0; i2 < 3; ++i2) {
			for(i3 = 0; i3 < 9; ++i3) {
				this.slotsList.add(new Slot(this, var1, i3 + (i2 + 1) * 9, 8 + i3 * 18, 84 + i2 * 18));
			}
		}

		for(i2 = 0; i2 < 9; ++i2) {
			this.slotsList.add(new Slot(this, var1, i2, 8 + i2 * 18, 142));
		}

	}

	public final void onGuiClosed() {
		super.onGuiClosed();

        for(int i1 = 0; i1 < this.craftMatrix.getSizeInventory(); ++i1) {
            ItemStack itemStack = this.craftMatrix.getStackInSlot(i1);
            if(itemStack != null) {
                this.mc.thePlayer.dropPlayerItem(itemStack);
            }
        }

	}

	public final void onCraftMatrixChanged() {
		int[] i1 = new int[9];

		for(int i2 = 0; i2 < 3; ++i2) {
			for(int i3 = 0; i3 < 3; ++i3) {
				int i4 = -1;
				if(i2 < 2 && i3 < 2) {
                    ItemStack itemStack = this.craftMatrix.getStackInSlot(i2 + (i3 << 1));
                    if(itemStack != null) {
                        i4 = itemStack.itemID;
                    }
                }

                i1[i2 + i3 * 3] = i4;
            }
        }

        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(i1));
    }

	protected final void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Crafting", 86, 16, 4210752);
	}

    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.xSize_lo = (float)mouseX;
        this.ySize_lo = (float)mouseY;
    }

	protected final void drawGuiContainerBackgroundLayer() {
		int i1 = this.mc.renderEngine.getTexture("/gui/inventory.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderEngine.bindTexture(i1);
		i1 = (this.width - this.xSize) / 2;
		int i2 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i1, i2, 0, 0, this.xSize, this.ySize);
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(i1 + 51), (float)(i2 + 75), 50.0F);
        GL11.glScalef(-30.0F, 30.0F, 30.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f3 = this.mc.thePlayer.renderYawOffset;
        float f4 = this.mc.thePlayer.rotationYaw;
        float f5 = this.mc.thePlayer.rotationPitch;
        float f6 = (float)(i1 + 51) - this.xSize_lo;
        float f7 = (float)(i2 + 75 - 50) - this.ySize_lo;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float)Math.atan((double)(f7 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        this.mc.thePlayer.renderYawOffset = (float)Math.atan((double)(f6 / 40.0F)) * 20.0F;
        this.mc.thePlayer.rotationYaw = (float)Math.atan((double)(f6 / 40.0F)) * 40.0F;
        this.mc.thePlayer.rotationPitch = -((float)Math.atan((double)(f7 / 40.0F))) * 20.0F;
        GL11.glTranslatef(0.0F, this.mc.thePlayer.yOffset, 0.0F);
        RenderManager.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        this.mc.thePlayer.renderYawOffset = f3;
        this.mc.thePlayer.rotationYaw = f4;
        this.mc.thePlayer.rotationPitch = f5;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_NORMALIZE);
	}

    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.slotsList.get(i);
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
            } else if (itemstack.getItem() instanceof ItemArmor
                    && !((Slot) this.slotsList.get(5 + ((ItemArmor) itemstack.getItem()).armorType))
                            .getHasStack()) {
                int j = 5 + ((ItemArmor) itemstack.getItem()).armorType;
                if (!this.mergeItemStack(itemstack1, j, j + 1, false)) {
                    return null;
                }
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
        return slot.inventory != this.craftResult && super.canMergeSlot(itemstack, slot);
    }
}
