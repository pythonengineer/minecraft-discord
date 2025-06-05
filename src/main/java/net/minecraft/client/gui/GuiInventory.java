package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

public class GuiInventory extends GuiScreen {
    private RenderBlocks blockRenderer;
    private Slot selectedItem;
    protected int xSize;
    protected int ySize;
    protected List slotsList;

    public GuiInventory() {
        this.blockRenderer = new RenderBlocks(Tessellator.instance);
        this.selectedItem = null;
        this.xSize = 176;
        this.ySize = 166;
        this.slotsList = new ArrayList();
        this.allowUserInput = true;
    }

    public GuiInventory(IInventory var1) {
        this();

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
        this.b();
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
            this.renderItem(Slot.b(var6), var6.slotIndex, var6.xPos, var6.yPos);
            if(var6.isAtCursorPos(var1, var2) &&
               (!PointerInputAbstraction.isTouchMode() || primaryTouchPoint != -1)) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int var7 = var6.xPos;
                int var9 = var6.yPos;
                drawGradientRect(var7, var9, var7 + 16, var9 + 16, -2130706433, -2130706433);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }

        if(this.selectedItem != null) {
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            Slot var8 = this.selectedItem;
            this.selectedItem = null;
            this.renderItem(Slot.b(var8), var8.slotIndex, var1 - var3 - 8, var2 - var4 - 8);
            this.selectedItem = var8;
        }

        GL11.glDisable(GL11.GL_NORMALIZE);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        this.b_();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

    protected void b_() {
        this.fontRenderer.drawString("PLAYER NAME", 84, 8, 4210752);
        this.fontRenderer.drawString("ATK: 100", 84, 24, 4210752);
        this.fontRenderer.drawString("DEF: 100", 84, 32, 4210752);
        this.fontRenderer.drawString("SPD: 100", 84, 40, 4210752);
    }

    protected void b() {
        int var1 = this.mc.renderEngine.getTexture("/gui/inventory.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1);
        var1 = (this.width - this.xSize) / 2;
        int var2 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var1, var2, 0, 0, this.xSize, this.ySize);
    }

    private void renderItem(IInventory var1, int var2, int var3, int var4) {
        ItemStack var5 = var1.getStackInSlot(var2);
        int var6;
        if(var5 == null || this.selectedItem != null && this.selectedItem.slotIndex == var2 && Slot.b(this.selectedItem) == var1) {
            if(var2 > 50) {
                GL11.glDisable(GL11.GL_LIGHTING);
                var6 = this.mc.renderEngine.getTexture("/gui/items.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var6);
                this.drawTexturedModalRect(var3, var4, 240, 63 - var2 << 4, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
            }

        } else {
            if(var5.itemID < 256) {
                var6 = var5.itemID;
                var2 = this.mc.renderEngine.getTexture("/terrain.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
                Block var7 = Block.blocksList[var6];
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(var3 - 2), (float)(var4 + 3), 0.0F);
                GL11.glScalef(10.0F, 10.0F, 10.0F);
                GL11.glTranslatef(1.0F, 0.5F, 8.0F);
                GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.blockRenderer.renderBlockOnInventory(var7);
                GL11.glPopMatrix();
            } else if(var5.getItem().getIconIndex() >= 0) {
                GL11.glDisable(GL11.GL_LIGHTING);
                var6 = this.mc.renderEngine.getTexture("/gui/items.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var6);
                this.drawTexturedModalRect(var3, var4, var5.getItem().getIconIndex() % 16 << 4, var5.getItem().getIconIndex() / 16 << 4, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
            }

            if(var5.stackSize > 1) {
                String var8 = "" + var5.stackSize;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                this.fontRenderer.drawStringWithShadow(var8, var3 + 19 - 2 - this.fontRenderer.getStringWidth(var8), var4 + 6 + 3, 16777215);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }

        }
    }

    protected final void mouseClicked(int var1, int var2, int var3) {
        if(var3 == 0) {
            int var5 = var2;
            int var4 = var1;
            GuiInventory var10 = this;
            int var6 = 0;

            Slot var10000;
            while(true) {
                if(var6 >= var10.slotsList.size()) {
                    var10000 = null;
                    break;
                }

                Slot var7 = (Slot)var10.slotsList.get(var6);
                if(var7.isAtCursorPos(var4, var5)) {
                    var10000 = var7;
                    break;
                }

                ++var6;
            }

            Slot var11 = var10000;
            if(var11 != null) {
                if(var11 == this.selectedItem) {
                    this.selectedItem = null;
                    return;
                }

                if(Slot.b(var11).getStackInSlot(var11.slotIndex) != null) {
                    if(this.selectedItem == null) {
                        this.selectedItem = var11;
                        return;
                    }

                    this.selectedItem.putStacks(var11);
                    return;
                }

                if(this.selectedItem != null) {
                    this.selectedItem.putStacks(var11);
                    this.selectedItem = null;
                    return;
                }
            } else if(this.selectedItem != null) {
                var3 = (this.width - this.xSize) / 2;
                var4 = (this.height - this.ySize) / 2;
                if(var1 < var3 || var2 < var4 || var1 >= var3 + this.xSize || var2 >= var4 + this.xSize) {
                    ItemStack var9 = Slot.b(this.selectedItem).decrStackSize(this.selectedItem.slotIndex, 1);
                    this.mc.thePlayer.dropPlayerItemWithRandomChoice(var9);
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
