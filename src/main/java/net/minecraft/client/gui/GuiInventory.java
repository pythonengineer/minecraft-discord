package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

public class GuiInventory extends GuiScreen {
    private RenderBlocks blockRenderer = new RenderBlocks(Tessellator.instance);
    private ItemStack selectedItem = null;
    protected int xSize = 176;
    protected int ySize = 166;
    protected List slotsList = new ArrayList();

    public GuiInventory() {
    }

    public GuiInventory(IInventory var1) {
        this.allowUserInput = true;

        int var2;
        for(var2 = 0; var2 < 4; ++var2) {
            this.slotsList.add(new Slot(this, var1, var1.getSizeInventory() - 1 - var2, 8, 8 + var2 * 18));
        }

        for(var2 = 0; var2 < 3; ++var2) {
            for(int var3 = 0; var3 < 9; ++var3) {
                this.slotsList.add(new Slot(this, var1, var3 + (var2 + 1) * 9, 8 + var3 * 18, 84 + var2 * 18));
            }
        }

        for(var2 = 0; var2 < 9; ++var2) {
            this.slotsList.add(new Slot(this, var1, var2, 8 + var2 * 18, 142));
        }

    }

    public final void drawScreen(int var1, int var2) {
        drawGradientRect(0, 0, this.width, this.height, 1610941696, -1607454624);
        int var3 = (this.width - this.xSize) / 2;
        int var4 = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer();
        GL11.glPushMatrix();
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var3, (float)var4, 0.0F);
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(52.0F, 73.0F, 24.0F);
        GL11.glScalef(24.0F, -24.0F, 24.0F);
        GL11.glRotatef(10.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(10.0F, 1.0F, 0.0F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderGlobal.renderManager.renderEntityWithPosYaw(this.mc.thePlayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_NORMALIZE);

        for(int var5 = 0; var5 < this.slotsList.size(); ++var5) {
            Slot var6 = (Slot)this.slotsList.get(var5);
            IInventory var10001 = Slot.a(var6);
            int var11 = var6.yPos;
            int var10 = var6.xPos;
            int var9 = var6.slotIndex;
            IInventory var8 = var10001;
            ItemStack var13 = var8.getStackInSlot(var9);
            this.renderItem(var13, var10, var11);
            if(var6.isAtCursorPos(var1, var2) &&
               (!PointerInputAbstraction.isTouchMode() || primaryTouchPoint != -1)) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int var7 = var6.xPos;
                int var12 = var6.yPos;
                drawGradientRect(var7, var12, var7 + 16, var12 + 16, -2130706433, -2130706433);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }

        if(this.selectedItem != null) {
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            this.renderItem(this.selectedItem, var1 - var3 - 8, var2 - var4 - 8);
        }

        GL11.glDisable(GL11.GL_NORMALIZE);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        this.drawGuiContainerForegroundLayer();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

    protected void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString("PLAYER NAME", 84, 8, 4210752);
        this.fontRenderer.drawString("ATK: 100", 84, 24, 4210752);
        this.fontRenderer.drawString("DEF: 100", 84, 32, 4210752);
        this.fontRenderer.drawString("SPD: 100", 84, 40, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer() {
        int var1 = this.mc.renderEngine.getTexture("/gui/inventory.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1);
        var1 = (this.width - this.xSize) / 2;
        int var2 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var1, var2, 0, 0, this.xSize, this.ySize);
    }

    private void renderItem(ItemStack var1, int var2, int var3) {
        if(var1 != null) {
            int var4;
            if(var1.itemID < 256) {
                var4 = var1.itemID;
                int var5 = this.mc.renderEngine.getTexture("/terrain.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5);
                Block var6 = Block.blocksList[var4];
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(var2 - 2), (float)(var3 + 3), 0.0F);
                GL11.glScalef(10.0F, 10.0F, 10.0F);
                GL11.glTranslatef(1.0F, 0.5F, 8.0F);
                GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.blockRenderer.renderBlockOnInventory(var6);
                GL11.glPopMatrix();
            } else if(var1.getItem().getIconIndex() >= 0) {
                GL11.glDisable(GL11.GL_LIGHTING);
                var4 = this.mc.renderEngine.getTexture("/gui/items.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
                this.drawTexturedModalRect(var2, var3, var1.getItem().getIconIndex() % 16 << 4, var1.getItem().getIconIndex() / 16 << 4, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
            }

            if(var1.stackSize > 1) {
                String var7 = "" + var1.stackSize;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                this.fontRenderer.drawStringWithShadow(var7, var2 + 19 - 2 - this.fontRenderer.getStringWidth(var7), var3 + 6 + 3, 16777215);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }

        }
    }

    protected final void mouseClicked(int var1, int var2, int var3) {
        if(var3 == 0 || var3 == 1) {
            int var6 = var2;
            int var5 = var1;
            GuiInventory var4 = this;
            int var7 = 0;

            Slot var10000;
            while(true) {
                if(var7 >= var4.slotsList.size()) {
                    var10000 = null;
                    break;
                }

                Slot var8 = (Slot)var4.slotsList.get(var7);
                if(var8.isAtCursorPos(var5, var6)) {
                    var10000 = var8;
                    break;
                }

                ++var7;
            }

            Slot var11 = var10000;
            if(var11 != null) {
                ItemStack var12 = var11.getCurrentItemStack();
                if(var12 == null && this.selectedItem == null) {
                    return;
                }

                if(var12 != null && this.selectedItem == null) {
                    var6 = var3 == 0 ? var12.stackSize : 1;
                    this.selectedItem = var12.splitStack(var6);
                    if(var12.stackSize == 0) {
                        var11.putStack((ItemStack)null);
                    }

                    var11.onPickupFromSlot();
                } else if(var12 == null && this.selectedItem != null && var11.isItemValid()) {
                    var6 = var3 == 0 ? this.selectedItem.stackSize : 1;
                    if(var6 > Slot.a(var11).getInventoryStackLimit()) {
                        var6 = Slot.a(var11).getInventoryStackLimit();
                    }

                    var11.putStack(this.selectedItem.splitStack(var6));
                    if(this.selectedItem.stackSize == 0) {
                        this.selectedItem = null;
                    }
                } else {
                    if(var12 == null || this.selectedItem == null || !var11.isItemValid()) {
                        return;
                    }

                    if(var12.itemID != this.selectedItem.itemID) {
                        if(this.selectedItem.stackSize > Slot.a(var11).getInventoryStackLimit()) {
                            return;
                        }

                        var11.putStack(this.selectedItem);
                        this.selectedItem = var12;
                    } else {
                        if(var12.itemID != this.selectedItem.itemID) {
                            return;
                        }

                        ItemStack var9;
                        if(var3 != 0) {
                            if(var3 == 1) {
                                var6 = 1;
                                if(1 > Slot.a(var11).getInventoryStackLimit() - var12.stackSize) {
                                    var6 = Slot.a(var11).getInventoryStackLimit() - var12.stackSize;
                                }

                                var9 = this.selectedItem;
                                if(var6 > var9.getItem().getItemStackLimit() - var12.stackSize) {
                                    var9 = this.selectedItem;
                                    var6 = var9.getItem().getItemStackLimit() - var12.stackSize;
                                }

                                this.selectedItem.splitStack(var6);
                                if(this.selectedItem.stackSize == 0) {
                                    this.selectedItem = null;
                                }

                                var12.stackSize += var6;
                            }

                            return;
                        }

                        var6 = this.selectedItem.stackSize;
                        if(var6 > Slot.a(var11).getInventoryStackLimit() - var12.stackSize) {
                            var6 = Slot.a(var11).getInventoryStackLimit() - var12.stackSize;
                        }

                        var9 = this.selectedItem;
                        if(var6 > var9.getItem().getItemStackLimit() - var12.stackSize) {
                            var9 = this.selectedItem;
                            var6 = var9.getItem().getItemStackLimit() - var12.stackSize;
                        }

                        this.selectedItem.splitStack(var6);
                        if(this.selectedItem.stackSize == 0) {
                            this.selectedItem = null;
                        }

                        var12.stackSize += var6;
                    }
                }
            } else if(this.selectedItem != null) {
                var5 = (this.width - this.xSize) / 2;
                var6 = (this.height - this.ySize) / 2;
                if(var1 < var5 || var2 < var6 || var1 >= var5 + this.xSize || var2 >= var6 + this.xSize) {
                    EntityPlayerSP var10 = this.mc.thePlayer;
                    if(var3 == 0) {
                        var10.dropPlayerItemWithRandomChoice(this.selectedItem);
                        this.selectedItem = null;
                    }

                    if(var3 == 1) {
                        var10.dropPlayerItemWithRandomChoice(this.selectedItem.splitStack(1));
                        if(this.selectedItem.stackSize == 0) {
                            this.selectedItem = null;
                        }
                    }
                } else if (PointerInputAbstraction.isTouchMode()) {
                    this.selectedItem = null;
                }
            }
        }

    }

    protected final void keyTyped(char var1, int var2) {
        if(var2 == 1 || var2 == this.mc.options.keyBindInventory.keyCode) {
            this.mc.displayGuiScreen((GuiScreen)null);
        }

    }

    public void onGuiClosed() {
        if(this.selectedItem != null) {
            this.mc.thePlayer.dropPlayerItemWithRandomChoice(this.selectedItem);
        }

    }

    protected float getTouchModeScale() {
        return 1.25f;
    }

    private int primaryTouchPoint = -1;

    protected void touchStarted(int touchX, int touchY, int uid) {
        if (primaryTouchPoint == -1) {
            primaryTouchPoint = uid;
            mouseClicked(touchX, touchY, 0);
        }
    }

    protected void touchMoved(int touchX, int touchY, int uid) {
        if (primaryTouchPoint == uid) {
        }
    }

    protected void touchEndMove(int touchX, int touchY, int uid) {
        if (primaryTouchPoint == uid) {
            primaryTouchPoint = -1;
            mouseClicked(touchX, touchY, 0);
        }
    }

    protected void touchTapped(int touchX, int touchY, int uid) {
        if (primaryTouchPoint == uid) {
            primaryTouchPoint = -1;
        }
    }

    protected boolean shouldTouchGenerateMouseEvents() {
        return false;
    }
}
