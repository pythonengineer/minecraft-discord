package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.item.recipe.CraftingManager;

public final class GuiCrafting extends GuiInventory {
    private InventoryCrafting inventoryCrafting = new InventoryCrafting(this);
    private IInventory iInventory = new InventoryCraftResult(this);

    public final void onGuiClosed() {
        super.onGuiClosed();

        for(int var1 = 0; var1 < 9; ++var1) {
            ItemStack var2 = this.inventoryCrafting.getStackInSlot(var1);
            if(var2 != null) {
                this.mc.thePlayer.dropPlayerItemWithRandomChoice(var2);
            }
        }

    }

    public GuiCrafting(InventoryPlayer var1) {
        this.slotsList.add(new SlotCrafting(this, this.iInventory, 0, 124, 34));

        int var2;
        int var3;
        for(var2 = 0; var2 < 3; ++var2) {
            for(var3 = 0; var3 < 3; ++var3) {
                this.slotsList.add(new Slot(this, this.inventoryCrafting, var3 + var2 * 3, 30 + var3 * 18, 16 + var2 * 18));
            }
        }

        for(var2 = 0; var2 < 3; ++var2) {
            for(var3 = 0; var3 < 9; ++var3) {
                this.slotsList.add(new Slot(this, var1, var3 + (var2 + 1) * 9, 8 + var3 * 18, 84 + var2 * 18));
            }
        }

        for(var2 = 0; var2 < 9; ++var2) {
            this.slotsList.add(new Slot(this, var1, var2, 8 + var2 * 18, 142));
        }

    }

    protected final void onCraftMatrixChanged() {
        int[] var1 = new int[9];

        for(int var2 = 0; var2 < 9; ++var2) {
            ItemStack var3 = InventoryCrafting.a(this.inventoryCrafting)[var2];
            if(var3 == null) {
                var1[var2] = -1;
            } else {
                var1[var2] = var3.itemID;
            }
        }

        this.iInventory.setInventorySlotContents(0, CraftingManager.getInstance().addRecipe(var1));
    }

    protected final void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString("Crafting", 8, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    protected final void drawGuiContainerBackgroundLayer() {
        int var1 = this.mc.renderEngine.getTexture("/gui/crafting.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1);
        var1 = (this.width - this.xSize) / 2;
        int var2 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var1, var2, 0, 0, this.xSize, this.ySize);
    }

    static InventoryCrafting a(GuiCrafting var0) {
        return var0.inventoryCrafting;
    }
}
