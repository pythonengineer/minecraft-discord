package net.minecraft.src;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;

public class GuiInventory extends GuiContainer {
	private CraftingInventoryPlayerCB field_977_j;
	private float xSize_lo;
	private float ySize_lo;

	public GuiInventory(IInventory var1, ItemStack[] var2) {
		this.field_948_f = true;
		this.field_977_j = new CraftingInventoryPlayerCB(var2);
		this.inventorySlots.add(new SlotCrafting(this, this.field_977_j.craftMatrix, this.field_977_j.craftResult, 0, 144, 36));

		int var3;
		int var4;
		for(var3 = 0; var3 < 2; ++var3) {
			for(var4 = 0; var4 < 2; ++var4) {
				this.inventorySlots.add(new SlotInventory(this, this.field_977_j.craftMatrix, var4 + var3 * 2, 88 + var4 * 18, 26 + var3 * 18));
			}
		}

		for(var3 = 0; var3 < 4; ++var3) {
			this.inventorySlots.add(new SlotArmor(this, this, var1, var1.getSizeInventory() - 1 - var3, 8, 8 + var3 * 18, var3));
		}

		for(var3 = 0; var3 < 3; ++var3) {
			for(var4 = 0; var4 < 9; ++var4) {
				this.inventorySlots.add(new SlotInventory(this, var1, var4 + (var3 + 1) * 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for(var3 = 0; var3 < 9; ++var3) {
			this.inventorySlots.add(new SlotInventory(this, var1, var3, 8 + var3 * 18, 142));
		}

	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Crafting", 86, 16, 4210752);
	}

	public void drawScreen(int var1, int var2, float var3) {
		super.drawScreen(var1, var2, var3);
		this.xSize_lo = (float)var1;
		this.ySize_lo = (float)var2;
	}

	protected void drawGuiContainerBackgroundLayer(float var1) {
		int var2 = this.mc.renderEngine.getTexture("/gui/inventory.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var2);
		int var3 = (this.width - this.xSize) / 2;
		int var4 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var3, var4, 0, 0, this.xSize, this.ySize);
		GL11.glEnable(GL11.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(var3 + 51), (float)(var4 + 75), 50.0F);
		float var5 = 30.0F;
		GL11.glScalef(-var5, var5, var5);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float var6 = this.mc.thePlayer.field_735_n;
		float var7 = this.mc.thePlayer.rotationYaw;
		float var8 = this.mc.thePlayer.rotationPitch;
		float var9 = (float)(var3 + 51) - this.xSize_lo;
		float var10 = (float)(var4 + 75 - 50) - this.ySize_lo;
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-((float)Math.atan((double)(var10 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		this.mc.thePlayer.field_735_n = (float)Math.atan((double)(var9 / 40.0F)) * 20.0F;
		this.mc.thePlayer.rotationYaw = (float)Math.atan((double)(var9 / 40.0F)) * 40.0F;
		this.mc.thePlayer.rotationPitch = -((float)Math.atan((double)(var10 / 40.0F))) * 20.0F;
		GL11.glTranslatef(0.0F, this.mc.thePlayer.yOffset, 0.0F);
		RenderManager.instance.func_853_a(this.mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		this.mc.thePlayer.field_735_n = var6;
		this.mc.thePlayer.rotationYaw = var7;
		this.mc.thePlayer.rotationPitch = var8;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_RESCALE_NORMAL);
	}

    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i) {
        ItemStack itemstack = null;
        SlotInventory slot = (SlotInventory) this.inventorySlots.get(i);
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
                    && !((Slot) this.inventorySlots.get(5 + ((ItemArmor) itemstack.getItem()).armorType))
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

    public boolean canMergeSlot(ItemStack itemstack, SlotInventory slot) {
        return slot.inventory != this.field_977_j.craftResult && super.canMergeSlot(itemstack, slot);
    }
}
