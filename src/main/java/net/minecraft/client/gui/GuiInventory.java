package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.PointerInputAbstraction;
import net.lax1dude.eaglercraft.lwjgl.opengl.GL11;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

public final class GuiInventory extends GuiScreen {
    private RenderBlocks blockRenderer = new RenderBlocks(Tessellator.instance);
    private int itemStack = -1;
    private List inventorySlots = new ArrayList();

    public GuiInventory() {
        this.allowUserInput = true;

        int var1;
        for(var1 = 0; var1 < 4; ++var1) {
            this.inventorySlots.add(new Slot(this, 63 - var1, 8, 8 + var1 * 18));
        }

        for(var1 = 0; var1 < 3; ++var1) {
            for(int var2 = 0; var2 < 9; ++var2) {
                this.inventorySlots.add(new Slot(this, var2 + (var1 + 1) * 9, 8 + var2 * 18, 84 + var1 * 18));
            }
        }

        for(var1 = 0; var1 < 9; ++var1) {
            this.inventorySlots.add(new Slot(this, var1, 8 + var1 * 18, 142));
        }

    }

    public final void drawScreen(int var1, int var2) {
        drawGradientRect(0, 0, this.width, this.height, 1610941696, -1607454624);
        int var3 = this.mc.renderEngine.getTexture("/gui/inventory.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var3);
        var3 = (this.width - 176) / 2;
        int var4 = (this.height - 184) / 2;
        this.drawTexturedModalRect(var3, var4, 0, 0, 176, 184);
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
        this.mc.renderGlobal.renderManager.doRender(this.mc.thePlayer, this.mc.renderEngine, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_NORMALIZE);

        int var8;
        for(var8 = 0; var8 < this.inventorySlots.size(); ++var8) {
            Slot var6 = (Slot)this.inventorySlots.get(var8);
            this.renderItemIntoGUI(var6.slotIndex, var6.xPos, var6.yPos);
            if(var6.isAtCursorPos(var1, var2)
                && (!PointerInputAbstraction.isTouchMode() || primaryTouchPoint != -1)) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int var7 = var6.xPos;
                int var9 = var6.yPos;
                drawGradientRect(var7, var9, var7 + 16, var9 + 16, -2130706433, -2130706433);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }

        if(this.itemStack >= 0) {
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            var8 = this.itemStack;
            this.itemStack = -1;
            this.renderItemIntoGUI(var8, var1 - var3 - 8, var2 - var4 - 8);
            this.itemStack = var8;
        }

        GL11.glDisable(GL11.GL_NORMALIZE);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        this.fontRenderer.drawString("PLAYER NAME", 84, 8, 4210752);
        this.fontRenderer.drawString("ATK: 100", 84, 24, 4210752);
        this.fontRenderer.drawString("DEF: 100", 84, 32, 4210752);
        this.fontRenderer.drawString("SPD: 100", 84, 40, 4210752);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

    private void renderItemIntoGUI(int var1, int var2, int var3) {
        ItemStack var4 = this.mc.thePlayer.inventory.mainInventory[var1];
        int var5;
        if(var4 != null && this.itemStack != var1) {
            if(var4.itemID < 256) {
                var1 = var4.itemID;
                var5 = this.mc.renderEngine.getTexture("/terrain.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5);
                Block var6 = Block.blocksList[var1];
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(var2 - 2), (float)(var3 + 3), 0.0F);
                GL11.glScalef(10.0F, 10.0F, 10.0F);
                GL11.glTranslatef(1.0F, 0.5F, 8.0F);
                GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.blockRenderer.renderBlockOnInventory(var6);
                GL11.glPopMatrix();
            } else if(var4.getItem().getIconIndex() >= 0) {
                GL11.glDisable(GL11.GL_LIGHTING);
                var5 = this.mc.renderEngine.getTexture("/gui/items.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5);
                this.drawTexturedModalRect(var2, var3, var4.getItem().getIconIndex() % 16 << 4, var4.getItem().getIconIndex() / 16 << 4, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
            }

            if(var4.stackSize > 1) {
                String var7 = "" + var4.stackSize;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                this.fontRenderer.drawStringWithShadow(var7, var2 + 19 - 2 - this.fontRenderer.getStringWidth(var7), var3 + 6 + 3, 16777215);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }

        } else {
            if(var1 > 50) {
                GL11.glDisable(GL11.GL_LIGHTING);
                var5 = this.mc.renderEngine.getTexture("/gui/items.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5);
                this.drawTexturedModalRect(var2, var3, 240, 63 - var1 << 4, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
            }

        }
    }

    protected final void mouseClicked(int var1, int var2, int var3) {
        if(var3 == 0) {
            int var5 = var2;
            int var4 = var1;
            GuiInventory var8 = this;
            int var6 = 0;

            Slot var10000;
            while(true) {
                if(var6 >= var8.inventorySlots.size()) {
                    var10000 = null;
                    break;
                }

                Slot var7 = (Slot)var8.inventorySlots.get(var6);
                if(var7.isAtCursorPos(var4, var5)) {
                    var10000 = var7;
                    break;
                }

                ++var6;
            }

            Slot var9 = var10000;
            if(var9 != null) {
                if(var9.slotIndex == this.itemStack) {
                    this.itemStack = -1;
                    return;
                }

                if(this.mc.thePlayer.inventory.mainInventory[var9.slotIndex] != null) {
                    if(this.itemStack < 0) {
                        this.itemStack = var9.slotIndex;
                        return;
                    }

                    this.mc.thePlayer.inventory.swapSlots(this.itemStack, var9.slotIndex);
                    return;
                }

                if(this.itemStack >= 0) {
                    this.mc.thePlayer.inventory.swapSlots(this.itemStack, var9.slotIndex);
                    this.itemStack = -1;
                    return;
                }
            } else if(this.itemStack > 0) {
                var3 = (this.width - 176) / 2;
                var4 = (this.height - 184) / 2;
                if(var1 < var3 || var2 < var4 || var1 >= var3 + 176 || var2 >= var4 + 176) {
                    this.mc.thePlayer.dropPlayerItemWithRandomChoice(this.itemStack);
                } else if (PointerInputAbstraction.isTouchMode()) {
                    this.itemStack = -1;
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
